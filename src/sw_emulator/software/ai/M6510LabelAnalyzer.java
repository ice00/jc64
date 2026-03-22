/**
 * @(#)M6510LabelAnalyzer 2026/03/18
 *
 * ICE Team free software group
 *
 * This file is part of C64 Java Software Emulator.
 * See README for copyright notice.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 *  02111-1307  USA.
 */
package sw_emulator.software.ai;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Sends a disassembled M6510 (Commodore 64) source listing to an AI API and
 * returns a map of memory address -> meaningful label name.
 *
 * Supports two backends:
 *   LOCAL  – LM Studio running on localhost (no API key required)
 *   CLOUD  – Anthropic Claude via OpenAI-compatible endpoint
 *
 * Uses HttpURLConnection (not HttpClient) for maximum JVM compatibility.
 *
 * @author ice
 */
public class M6510LabelAnalyzer {

  // ---------------------------------------------------------------------------
  // Timeouts  (independent of backend)
  // ---------------------------------------------------------------------------

  private static final int CONNECT_TIMEOUT_MS  = 5_000;
  private static final int RESPONSE_TIMEOUT_MS = 180_000; // 3 min – local models can be slow

  // ---------------------------------------------------------------------------
  // System prompt
  // ---------------------------------------------------------------------------

  private static final String SYSTEM_PROMPT
          = "You are an expert in M6510 assembly language for the Commodore 64.\n"
          + "\n"
          + "You will receive a disassembled source listing in the format:\n"
          + "  ADDRESS  BYTECODES  INSTRUCTION  [optional comment]\n"
          + "\n"
          + "Lines that define a label have the label on a separate line immediately\n"
          + "before the corresponding address line, ending with a colon, e.g.:\n"
          + "  myLabel:\n"
          + "  09A0  A9 01  LDA #$01\n"
          + "\n"
          + "Your task:\n"
          + "1. Identify every address that is a branch/jump/call target\n"
          + "   (BEQ, BNE, BCC, BCS, BPL, BMI, BVC, BVS, JMP, JSR, etc.).\n"
          + "2. Assign each one a short, meaningful English name that reflects\n"
          + "   what the code at that address actually does.\n"
          + "3. If a label already has a meaningful name in the source (not a\n"
          + "   generic placeholder like W1234), keep it exactly as-is.\n"
          + "4. Generic placeholders like W09A0 or W0B50 MUST be renamed.\n"
          + "\n"
          + "For each label output exactly one line in this format:\n"
          + "  [#ADDRESS LABEL_NAME#]\n"
          + "Rules:\n"
          + "- ADDRESS is the 4-digit hex address (uppercase).\n"
          + "- LABEL_NAME is a single camelCase English word.\n"
          + "- Every label line MUST start with [# and end with #].\n"
          + "- You may add reasoning or comments outside the markers,\n"
          + "  but ONLY lines wrapped in [# #] will be used.\n"
          + "Example output:\n"
          + "  [#0900 start#]\n"
          + "  [#090A waitForRaster#]\n"
          + "  [#0950 irqHandler#]\n";

  // ---------------------------------------------------------------------------
  // Instance state
  // ---------------------------------------------------------------------------

  private AIBackendConfig config;  // set per-call via analyzeLabels()

  /** Token usage reported by the API for the last call. Updated after every callApi(). */
  private volatile TokenUsage lastUsage = new TokenUsage(0, 0, 0);

  private static final Logger LOG = Logger.getLogger(M6510LabelAnalyzer.class.getName());

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Creates an analyzer with no backend configured.
   * A config MUST be passed to every analyzeLabels() call.
   */
  public M6510LabelAnalyzer() { }

  /** 
   * Returns the last config used, or null if never called.
   * 
   * @return the configuration
   */
  public AIBackendConfig getConfig() { return config; }

  // ---------------------------------------------------------------------------
  // Public API  –  BLOCKING (call from background thread / SwingWorker)
  // ---------------------------------------------------------------------------

  /**
   * Analyzes a source listing using the given backend config.
   *
   * @param config  backend to use for this call (must not be null)
   * @param source  disassembled listing
   * @return the result
   * @throws java.io.IOException
   */
  public Map<String, String> analyzeLabels(AIBackendConfig config, CharSequence source)
          throws IOException {
    return analyzeLabels(config, source, null);
  }

  /**
   * Analyzes a source listing using the given backend config and known labels for context.
   *
   * @param config       backend to use for this call (must not be null)
   * @param source       disassembled listing
   * @param knownLabels  labels confirmed in previous calls, or null
   * @return the result as map
   * @throws java.io.IOException 
   */
  public Map<String, String> analyzeLabels(AIBackendConfig config, CharSequence source,
          Map<String, String> knownLabels) throws IOException {
    applyConfig(config);
    return parseResponse(callApi(source, knownLabels));
  }

  /**
   * Analyzes a source listing using the given backend config and known labels for context.
   * 
   * @param config       backend to use for this call (must not be null)
   * @param source       disassembled listing
   * @param knownLabels  labels confirmed in previous calls, or null
   * @return the result as list
   * @throws IOException 
   */
  public List<LabelEntry> analyzeLabelsAsList(AIBackendConfig config, CharSequence source,
          Map<String, String> knownLabels) throws IOException {
    applyConfig(config);
    return parseResponseAsList(callApi(source, knownLabels));
  }

  // ---------------------------------------------------------------------------
  // Public API  –  ASYNC (safe to call from Swing EDT)
  //
  // Example:
  //   analyzer.analyzeLabelsAsync(source, null,
  //       labels -> SwingUtilities.invokeLater(() -> updateUI(labels)),
  //       ex     -> SwingUtilities.invokeLater(() -> showError(ex)));
  // ---------------------------------------------------------------------------

  /**
   * Async version – safe to call from the Swing EDT.
   *
   * @param config       backend to use for this call (must not be null)
   * @param source       disassembled listing
   * @param knownLabels  labels confirmed in previous calls, or null
   * @param onSuccess    called on the completion thread when analysis succeeds
   * @param onError      called on the completion thread on any error
   */
  public void analyzeLabelsAsync(AIBackendConfig config,
          CharSequence source,
          Map<String, String> knownLabels,
          Consumer<Map<String, String>> onSuccess,
          Consumer<Exception> onError) {

    final String sourceSnap = source.toString();

    CompletableFuture.runAsync(() -> {
      try {
        Map<String, String> result = analyzeLabels(config, sourceSnap, knownLabels);
        onSuccess.accept(result);
      } catch (Exception e) {
        onError.accept(e);
      }
    });
  }

  // ---------------------------------------------------------------------------
  // Connection pre-check
  // ---------------------------------------------------------------------------

  /**
   * Checks whether the LOCAL backend TCP port is open.
   * Always returns true for CLOUD (rely on normal HTTP error handling).
   *
   * @param timeoutMs  milliseconds to wait
   * @return true if reachable
   */
  public boolean isBackendReachable(int timeoutMs) {
    if (!config.isLocalReachabilityCheck()) return true;
    try (Socket s = new Socket()) {
      s.connect(new java.net.InetSocketAddress(
              config.getLocalHost(), config.getLocalPort()), timeoutMs);
      LOG.info(config.getDisplayName() + " reachable on "
              + config.getLocalHost() + ":" + config.getLocalPort());
      return true;
    } catch (IOException e) {
      LOG.warning(config.getDisplayName() + " NOT reachable: " + e.getMessage());
      return false;
    }
  }

  // ---------------------------------------------------------------------------
  // Core HTTP call  –  uses HttpURLConnection
  // ---------------------------------------------------------------------------

  /**
   * Validates and stores the config for the current call. 
   */
  private void applyConfig(AIBackendConfig cfg) {
    if (cfg == null)
      throw new IllegalArgumentException(
              "AIBackendConfig is null – pass a valid config to analyzeLabels()");
    this.config = cfg;
  }

  /**
   * Call the ap
   * 
   * @param source the source
   * @param knownLabels the labels already good
   * @return the result
   * @throws IOException 
   */
  private String callApi(CharSequence source, Map<String, String> knownLabels)
          throws IOException {

    if (config.isLocalReachabilityCheck() && !isBackendReachable(3000)) {
      throw new IOException(
              "Cannot reach " + config.getDisplayName()
              + " at " + config.getLocalHost() + ":" + config.getLocalPort()
              + ". Make sure the local server is running.");
    }

    // Build JSON body
    String body = buildRequestBody(source, knownLabels);

    LOG.log(Level.INFO, "→ POST {0} [{1}] model={2} body={3} chars",
            new Object[]{ config.getEndpoint(), config.getDisplayName(),
                          config.getModel(), body.length() });

    long t0 = System.currentTimeMillis();

    String responseBody = httpPost(body);
    long elapsed = System.currentTimeMillis() - t0;
    LOG.log(Level.INFO, "← response in {0} ms", elapsed);
    LOG.log(Level.FINE, "Response body: {0}", responseBody);
    return extractText(responseBody);
  }

  // ---------------------------------------------------------------------------
  // Private helpers
  // ---------------------------------------------------------------------------

  /**
   * Build the request body
   * 
   * @param source the source
   * @param knownLabels the labels already good
   * @return the result
   */
  private String buildRequestBody(CharSequence source, Map<String, String> knownLabels) {
    JSONObject body = new JSONObject();
    body.put("model", config.getModel());
    body.put("max_tokens", config.getMaxOutputTokens());

    JSONArray messages = new JSONArray();
    messages.put(new JSONObject().put("role", "system").put("content", SYSTEM_PROMPT));
    messages.put(new JSONObject().put("role", "user").put("content",
            buildUserMessage(source, knownLabels)));
    body.put("messages", messages);

    return body.toString();
  }

  /**
   * Build the user message
   * 
   * @param source the source message
   * @param knownLabels the known labels
   * @return the message
   */
  private String buildUserMessage(CharSequence source, Map<String, String> knownLabels) {
    StringBuilder sb = new StringBuilder();
    if (knownLabels != null && !knownLabels.isEmpty()) {
      sb.append("Previously identified labels (keep names consistent):\n");
      for (Map.Entry<String, String> e : knownLabels.entrySet()) {
        sb.append(e.getKey()).append("  ").append(e.getValue()).append('\n');
      }
      sb.append("\nNew source block to analyze:\n");
    }
    sb.append(source);
    return sb.toString();
  }

  /**
   * Extract the text from response 
   * 
   * @param responseJson the response
   * @return the text
   */
  private String extractText(String responseJson) {
    JSONObject json = new JSONObject(responseJson);

    // Capture token usage reported by the API (always present in the response)
    if (json.has("usage")) {
      JSONObject u = json.getJSONObject("usage");
      int prompt     = u.optInt("prompt_tokens",     0);
      int completion = u.optInt("completion_tokens", 0);
      int total      = u.optInt("total_tokens",      0);
      lastUsage = new TokenUsage(prompt, completion, total);
      LOG.log(Level.INFO, "Token usage – prompt: {0}  completion: {1}  total: {2}",
              new Object[]{ prompt, completion, total });
    }

    return json
            .getJSONArray("choices")
            .getJSONObject(0)
            .getJSONObject("message")
            .getString("content");
  }

  // ---------------------------------------------------------------------------
  // Token counting
  // ---------------------------------------------------------------------------

  /**
   * Returns the token usage reported by the API for the LAST call to
   * analyzeLabels / analyzeLabelsAsList.
   * Updated after every successful call; zero until the first call completes.
   * 
   * @return the last usage
   */
  public TokenUsage getLastTokenUsage() {
    return lastUsage; 
  }

  /**
   * Fast LOCAL estimate of how many tokens a text will consume.
   *
   * Rule of thumb valid for most LLM tokenizers (GPT, Llama, Gemma, Claude):
   *   ~4 characters per token for English/code text.
   * Assembly listings have short tokens (hex digits, mnemonics) so the real
   * count is often a bit higher; we use 3.5 chars/token as a conservative
   * estimate that avoids underestimating.
   *
   * For an EXACT count use countTokensExact(), which makes a real API call.
   *
   * @param text  any CharSequence (String, StringBuffer, StringBuilder…)
   * @return      estimated token count
   */
  public static int estimateTokens(CharSequence text) {
    if (text == null || text.length() == 0) return 0;
    // Count non-whitespace runs as a proxy for tokens, then scale
    int words = 0;
    boolean inWord = false;
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (Character.isWhitespace(c)) {
        inWord = false;
      } else if (!inWord) {
        inWord = true;
        words++;
      }
    }
    // Assembly tokens are short: ~1.5 words per token is a reasonable ratio
    return (int) Math.ceil(words / 1.5);
  }

  /**
   * Returns the EXACT input-token count by sending the text to the API with
   * max_tokens=1 and reading the prompt_tokens field from the response.
   *
   * This makes a real (cheap) API round-trip. The result is always accurate
   * regardless of the model or tokenizer in use.
   *
   * @param text  text to measure (will be sent as a user message)
   * @return      exact prompt token count as reported by the API
   * @throws IOException on network or API error
   */
  public int countTokensExact(CharSequence text) throws IOException {
    JSONObject body = new JSONObject();
    body.put("model", config.getModel());
    body.put("max_tokens", 1);   // we only care about prompt tokens, not the reply

    JSONArray messages = new JSONArray();
    messages.put(new JSONObject().put("role", "system").put("content", SYSTEM_PROMPT));
    messages.put(new JSONObject().put("role", "user").put("content", text.toString()));
    body.put("messages", messages);

    String responseJson = httpPost(body.toString());
    JSONObject json = new JSONObject(responseJson);
    if (!json.has("usage")) {
      throw new IOException("API response did not contain a 'usage' field");
    }
    return json.getJSONObject("usage").getInt("prompt_tokens");
  }

  /**
   * Low-level POST helper shared by callApi() and countTokensExact().
   * Returns the raw response body string.
   */
  private String httpPost(String bodyJson) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) new URL(config.getEndpoint()).openConnection();
    try {
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
      conn.setReadTimeout(RESPONSE_TIMEOUT_MS);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");

      // Auth header
      if (config.requiresApiKey()) {
        String prefix = config.getAuthHeaderPrefix();
        String value  = (prefix != null && !prefix.isBlank())
                        ? prefix + " " + config.getApiKey()
                        : config.getApiKey();
        conn.setRequestProperty(config.getAuthHeaderName(), value);
      }

      // Extra headers (e.g. anthropic-version)
      String[] extra = config.getExtraHeaders();
      for (int i = 0; i + 1 < extra.length; i += 2) {
        conn.setRequestProperty(extra[i], extra[i + 1]);
      }

      try (OutputStream os = conn.getOutputStream()) {
        os.write(bodyJson.getBytes(StandardCharsets.UTF_8));
        os.flush();
      }

      int status = conn.getResponseCode();
      InputStream is = (status == 200) ? conn.getInputStream() : conn.getErrorStream();
      String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);

      if (status != 200) {
        throw new IOException("API error HTTP " + status + " [" + config + "]: " + response);
      }
      return response;
    } finally {
      conn.disconnect();
    }
  }

  /**
   * Parse the response
   * 
   * @param raw the raw string
   * @return the maps of labels
   */
  private Map<String, String> parseResponse(String raw) {
    Map<String, String> result = new LinkedHashMap<>();
    for (LabelEntry e : parseResponseAsList(raw)) {
      result.put(e.address, e.label);
    }
    return result;
  }

  /**
   * Parses the raw model response into a list of LabelEntry objects.
   *
   * Strategy (in order of priority):
   *  1. Scan raw text for [# #] markers (closed or unclosed on same line).
   *     Done BEFORE think-stripping because some models write markers before </think>.
   *  2. Strip <think>/<thinking> blocks, scan again for any remaining markers.
   *  3. Deduplicate by address (keep first occurrence).
   *  4. Fallback: plain "ADDR LABEL" lines where ADDR is 4 hex digits.
   */
  private List<LabelEntry> parseResponseAsList(String raw) {
    List<LabelEntry> result = new ArrayList<>();
    if (raw == null || raw.isBlank()) return result;

    // Pass 1 – scan raw text for [# #] markers
    extractMarkers(raw, result);

    // Pass 2 – strip <think> blocks then scan again
    String cleaned = raw
            .replaceAll("(?si)<think>.*?</think>", "")
            .replaceAll("(?si)<thinking>.*?</thinking>", "")
            .trim();

    if (cleaned.length() < raw.length()) {
      LOG.info("Stripped <think>/<thinking> block(s) from model response");
      extractMarkers(cleaned, result);
    }

    // Deduplicate by address, preserving first occurrence
    List<LabelEntry> deduped = new ArrayList<>();
    java.util.Set<String> seen = new java.util.LinkedHashSet<>();
    for (LabelEntry e : result) {
      if (seen.add(e.address)) deduped.add(e);
    }
    result = deduped;

    // Pass 3 – fallback: plain "ADDR LABEL" lines on the cleaned text
    if (result.isEmpty()) {
      LOG.warning("No [# #] markers found – falling back to line-by-line parsing");
      for (String line : cleaned.split("\\r?\\n")) {
        line = line.trim();
        if (line.isEmpty()) continue;
        String[] parts = line.split("\\s+", 2);
        if (parts.length == 2 && parts[0].matches("[0-9A-Fa-f]{4}")) {
          result.add(new LabelEntry(parts[0].toUpperCase(), parts[1].trim()));
        }
      }
    }

    return result;
  }

  /**
   * Scans {@code text} line by line for label markers.
   *
   * Rule: [# and #] MUST appear on the same line to be valid.
   * This avoids false positives from multi-line reasoning blocks
   * or example markers repeated by the model inside <think>.
   *
   * Accepted formats (both on a single line):
   *   Closed   :  [#0900 start#]
   *   Unclosed :  [#0900 start       (no #] on the line – still accepted)
   *
   * The token between the markers must be "ADDR LABEL"
   * where ADDR is exactly 4 hex digits.
   */
  private void extractMarkers(String text, List<LabelEntry> out) {
    for (String line : text.split("\\r?\\n")) {
      int start = line.indexOf("[#");
      if (start < 0) continue;                    // no opening marker on this line

      // Determine end: #] on same line, or end of line
      int closeMarker = line.indexOf("#]", start + 2);
      String token;
      if (closeMarker >= 0) {
        token = line.substring(start + 2, closeMarker).trim();
      } else {
        // Unclosed on this line – take rest of line after [#
        token = line.substring(start + 2).trim();
        LOG.fine("Tolerated unclosed [# marker: " + token);
      }

      // Validate: must start with a 4-hex-digit address followed by the label.
      // Label is a single word (no spaces allowed) – take only parts[1],
      // ignoring anything after the first space (extra words = model noise).
      String[] parts = token.split("\\s+");
      if (parts.length >= 2 && parts[0].matches("[0-9A-Fa-f]{4}")) {
        String addr  = parts[0].toUpperCase();
        // Strip any stray trailing #] the model may have appended
        String label = parts[1].replaceAll("[#\\]]+$", "").trim();
        if (!label.isEmpty()) {
          out.add(new LabelEntry(addr, label));
        }
      }
    }
  }

 
  // ---------------------------------------------------------------------------
  // Demo main
  // ---------------------------------------------------------------------------

  public static void main(String[] args) {
    Logger.getLogger("").setLevel(Level.INFO);

    // Analyzer has no backend at creation time
    M6510LabelAnalyzer analyzer = new M6510LabelAnalyzer();

    // Config is chosen at call time – swap freely between calls
    AIBackendConfig cfg = AIBackendConfig.lmStudio();
    // AIBackendConfig cfg = AIBackendConfig.anthropic("sk-ant-...", "claude-sonnet-4-6");
    // AIBackendConfig cfg = AIBackendConfig.gemini("AIza...", "gemini-2.0-flash");

    System.out.println("Backend : " + cfg);

    if (!analyzer.isBackendReachable(3000)) {
      System.err.println("LM Studio not running. Aborting.");
      return;
    }

    StringBuffer source = new StringBuffer();
    source.append("start:\n");
    source.append("0900  78         SEI\n");
    source.append("0901  20 E0 0A   JSR  W0AE0\n");
    source.append("W0942:\n");
    source.append("0942  C8         INY\n");
    source.append("0943  4C 42 09   JMP  W0942\n");
    source.append("irq:\n");
    source.append("0950  8D B1 0A   STA  storeA+1\n");

    // --- Token estimate (no API call needed) ---
    int estimated = M6510LabelAnalyzer.estimateTokens(source);
    System.out.println("Estimated tokens (local, no API call): " + estimated);

    try {
      // --- Exact token count (one cheap API call) ---
      int exact = analyzer.countTokensExact(source);
      System.out.println("Exact prompt tokens (from API):        " + exact);

      // --- Real analysis ---
      Map<String, String> labels = analyzer.analyzeLabels(cfg, source);
      System.out.println("=== Identified Labels ===");
      labels.forEach((addr, name) -> System.out.printf("%-6s  %s%n", addr, name));

      // --- Token usage of the last call ---
      System.out.println("Last call usage: " + analyzer.getLastTokenUsage());

    } catch (IOException ex) {
      Logger.getLogger(M6510LabelAnalyzer.class.getName()).log(Level.SEVERE, "Failed", ex);
    }
  }
}
