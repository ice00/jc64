/**
 * @(#)AIContextChunker 2026/03/23
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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Splits a long disassembly into blocks that fit the context window of the AI
 * server, calls the backend once per block and merges the answers.
 *
 * Features:
 *  - context window read from the server (llama.cpp /props, LM Studio /api/v0/models),
 *    from AIBackendConfig.getContextWindow(), or a default
 *  - chars/token ratio calibrated on the real usage reported by the server
 *  - if the server still answers "context exceeded", the block is shrunk and retried
 *  - only labels for addresses DEFINED in the block are accepted (targets defined
 *    in another block are named when that block is processed)
 *
 * @author ice
 */
public class AIContextChunker {

  /** The server refused the request because it exceeds its context window */
  public static class ContextOverflowException extends IOException {
    /** Context size reported by the server, 0 if unknown */
    public final int serverContext;

    public ContextOverflowException(String msg, int serverContext) {
      super(msg);
      this.serverContext = serverContext;
    }
  }

  /** One backend call: wrap the existing "send prompt, parse answer" code */
  public interface Caller {
    /**
     * @param chunk   part of the listing
     * @param known   labels already known and relevant for this chunk (addr -> name)
     * @param current labels proposed by previous chunks and relevant for this chunk
     * @return labels found in this chunk (addr -> name)
     */
    Map<String, String> call(String chunk, Map<String, String> known, Map<String, String> current) throws IOException;
  }
  
  /**
   * Listener interface for reporting the progress of the label extraction process.
   * <p>
   * Implementations should be notified at the start of each chunk processing step to allow
   * for UI updates or logging.
   */
  public interface Progress {
    /**
     * Called before each chunk is sent to the backend for processing.
     *
     * @param chunk           The 1-based index of the current chunk being processed.
     * @param estimatedChunks The estimated total number of chunks required to process the entire source. This may change dynamically as the processing adapts.
     * @param fromLine        The 1-based index of the first line in the current chunk.
     * @param toLine          The 1-based index of the last line in the current chunk.
     * @param totalLines      The total number of lines in the entire source listing.
     */
    void update(int chunk, int estimatedChunks, int fromLine, int toLine, int totalLines);
  }

  private static final int MIN_CONTEXT = 1024;
  private static final double LABEL_SHARE = 0.15;   // part of context reserved to labels hints

  private final int maxOutputTokens;
  private int context;
  private int outputReserve;
  private double charsPerToken = 2.0;               // conservative start, calibrated later
  private int promptOverheadChars = 0;

  /**
   * Constructor of AI context chunker
   * 
   * @param context          context window in tokens
   * @param maxOutputTokens  upper limit for the answer (AIBackendConfig.getMaxOutputTokens())
   */
  public AIContextChunker(int context, int maxOutputTokens) {
    this.maxOutputTokens = maxOutputTokens;
    setContext(context);
  }

  /**
   * Set the context size to use
   * 
   * @param ctx the context size
   */
  private void setContext(int ctx) {
    context = Math.max(ctx, MIN_CONTEXT);
    outputReserve = Math.min(context / 4, maxOutputTokens);
  }

  /** 
   *  Get the output reserver tokens
   * 
   * @return tokens to pass as max_tokens for each call 
   */
  public int getOutputReserveTokens() { 
    return outputReserve;
  }

  /** 
   * Length in chars of the fixed part of the prompt (system prompt) 
   * 
   * @param chars the overhead chars
   */
  public void setPromptOverheadChars(int chars) {
    promptOverheadChars = chars;
  }

  /** 
   * Use the real numbers reported by the server for the last call 
   * 
   * @param promptChars chars in prompt
   * @param promptTokens token in prompt
   */
  public void calibrate(int promptChars, int promptTokens) {
    if (promptTokens <= 0 || promptChars <= 0) return;
    double ratio = promptChars / (double) promptTokens;
    charsPerToken = Math.max(1.0, Math.min(6.0, ratio * 0.9)); // 10% safety margin
  }

  // ---------------------------------------------------------------------------
  // Main loop
  // ---------------------------------------------------------------------------

  /**
   * Executes the label extraction process on the provided source text.
   *
   * @param source        The raw source text to be processed.
   * @param knownLabels   A map of labels already established in previous runs.
   * @param caller        The mechanism used to communicate with the LLM.
   * @param progress      An optional callback to report execution progress.
   * @return A {@link Map} of discovered address-to-name mappings.
   * @throws IOException if an error occurs during the calling process.
   */
  public Map<String, String> run(String source, Map<String, String> knownLabels,
          Caller caller, Progress progress) throws IOException {
    return run(source, knownLabels, caller, progress, null);
  }

  /**
   * Executes the label extraction process on the provided source text.
   * <p>
   * This method processes the source text in logical chunks to stay within LLM context limits,
   * extracts relevant information via a {@link Caller}, and resolves address/name collisions.
   *
   * @param source        The raw source text to be processed.
   * @param knownLabels   A map of labels already established in previous runs or known context.
   * @param caller        The mechanism used to communicate with the LLM (e.g., an API client).
   * @param progress      An optional callback to report execution progress.
   * @param cancel        An optional {@link AtomicBoolean} to allow for graceful cancellation.
   * @return A {@link Map} of discovered address-to-name mappings.
   * @throws IOException if an error occurs during the calling process.
   */
  public Map<String, String> run(String source, Map<String, String> knownLabels,
          Caller caller, Progress progress, AtomicBoolean cancel) throws IOException {

    List<String> lines = Arrays.asList(source.split("\\R", -1));
    Map<String, String> found = new LinkedHashMap<>();
    Set<String> usedNames = new HashSet<>(knownLabels.values());

    // remainingChars[i] = chars from line i to the end (to estimate the chunks left)
    long[] remainingChars = new long[lines.size() + 1];
    for (int i = lines.size() - 1; i >= 0; i--) remainingChars[i] = remainingChars[i + 1] + lines.get(i).length() + 1;

    int pos = 0, retries = 0, chunkNo = 0;
    while (pos < lines.size()) {
      if (cancel != null && cancel.get()) break;

      int budget = chunkBudgetChars();
      int end = findEnd(lines, pos, budget);
      String chunk = String.join("\n", lines.subList(pos, end));

      if (progress != null) {
        // blocks end on RTS/JMP so they are a bit smaller than the budget: use 80%
        int left = (int) Math.ceil(remainingChars[pos] / (budget * 0.8));
        progress.update(chunkNo + 1, chunkNo + Math.max(1, left), pos + 1, end, lines.size());
      }

      Set<String> tokens = tokenize(chunk);
      int hintChars = (int) (context * LABEL_SHARE * charsPerToken);
      Map<String, String> k = relevant(knownLabels, tokens, hintChars);
      Map<String, String> c = relevant(found, tokens, hintChars);

      Map<String, String> answer;
      try {
        answer = caller.call(chunk, k, c);
      } catch (ContextOverflowException e) {
        if (e.serverContext > 0 && e.serverContext < context) {
          setContext(e.serverContext);        // server window is smaller than assumed
        } else {
          charsPerToken *= 0.75;              // our token estimate was too optimistic
        }
        if (charsPerToken < 0.8 || context < MIN_CONTEXT || ++retries > 8) throw e;
        continue;                             // redo the same block, smaller
      }
      retries = 0;

      merge(found, answer, definedAddresses(chunk), knownLabels, usedNames);
      pos = end;
      chunkNo++;
    }
    return found;
  }

  // ---------------------------------------------------------------------------
  // Chunking
  // ---------------------------------------------------------------------------

  /**
   * Calculates the maximum number of characters allowed in a single chunk, based on the current 
   * context window and reserved allocations.
   * <p>
   * The calculation subtracts several reserved portions from the total context:
   * <ul>
   *   <li><b>Overhead Tokens:</b> Calculated from {@link #promptOverheadChars} converted to tokens.</li>
   *   <li><b>Hint Tokens:</b> A fixed share of the context reserved for hints, determined by {@link #LABEL_SHARE}.</li>
   *   <li><b>Output Reserve:</b> Space reserved for the expected output generation.</li>
   * </ul>
   * <p>
   * The result is converted back to characters using {@link #charsPerToken}. A minimum budget of 
   * 128 characters is enforced to ensure chunks are never too small to be meaningful.
   *
   * @return The number of characters allowed for the current chunk, constrained by context limits.
   */
  private int chunkBudgetChars() {
    int overheadTokens = (int) Math.ceil(promptOverheadChars / charsPerToken);
    int hintTokens = (int) (context * LABEL_SHARE);
    int free = context - outputReserve - overheadTokens - hintTokens;
    return (int) (Math.max(free, 128) * charsPerToken);
  }

  private static final Pattern BREAK = Pattern.compile("\\b(RTS|RTI|JMP)\\b", Pattern.CASE_INSENSITIVE);

  /**
   * Calculates the exclusive end index for a block of text, attempting to find a logical 
   * stopping point within a character budget.
   * <p>
   * The method scans lines starting from {@code start}, accumulating character counts. 
   * It prioritizes "clean" break points such as:
   * <ul>
   *   <li>Control flow instructions (e.g., RTS, RTI, JMP via the {@link #BREAK} pattern)</li>
   *   <li>Blank lines or lines containing only comments</li>
   * </ul>
   * <p>
   * <b>Heuristics:</b>
   * <ul>
   *   <li>If the budget is exceeded, it attempts to backtrack to the last "clean" break point, 
   *       provided that break point covers at least 60% of the budget.</li>
   *   <li>It ensures the block does not end with a dangling label (a line ending in ':').</li>
   * </ul>
   *
   * @param lines        The list of strings representing the lines of text to scan.
   * @param start        The index of the line to begin scanning from.
   * @param budgetChars  The maximum number of characters allowed in the resulting range.
   * @return An exclusive end index {@code end} such that {@code lines.subList(start, end)} 
   *     
   */
  private int findEnd(List<String> lines, int start, int budgetChars) {
    int size = 0, i = start, lastBreak = -1, lastBreakSize = 0;
    while (i < lines.size()) {
      String line = lines.get(i);
      int len = line.length() + 1;
      if (size + len > budgetChars && i > start) break;
      size += len;
      i++;
      String code = line.indexOf(';') >= 0 ? line.substring(0, line.indexOf(';')) : line;
      if (code.trim().isEmpty() || BREAK.matcher(code).find()) {
        lastBreak = i;
        lastBreakSize = size;
      }
    }
    int end = i;
    if (i < lines.size() && lastBreak > start && lastBreakSize >= budgetChars * 0.6) end = lastBreak;

    // never leave a "label:" line alone at the end of a block
    while (end > start + 1 && lines.get(end - 1).trim().endsWith(":")) end--;
    return end;
  }

  // ---------------------------------------------------------------------------
  // Labels relevance / merge
  // ---------------------------------------------------------------------------

  private static final Pattern TOKEN = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*|\\$?[0-9A-Fa-f]{4}\\b");
  private static final Pattern DEFINED = Pattern.compile("(?m)^\\s*([0-9A-Fa-f]{4})\\s");

    /**
   * Tokenizes a string of text into a set of normalized tokens.
   * <p>
   * This method scans the input {@code text} using the {@link #TOKEN} pattern. For each match, it adds
   * the token to the set. It also adds an uppercase version of the token, stripping a leading '$' if present.
   * <p>
   * Additionally, it handles specific placeholder formats. If a token consists of a single letter followed by
   * four hexadecimal digits (e.g., "W0AE0"), it extracts the hexadecimal part and adds that as a separate
   * token, effectively treating the placeholder as a reference to an address.
   *
   * @param text The input string to tokenize.
   * @return A set of strings containing the original tokens, their uppercase variants, and any extracted addresses.
   */
  private static Set<String> tokenize(String text) {
    Set<String> set = new HashSet<>();
    Matcher m = TOKEN.matcher(text);
    while (m.find()) {
      String t = m.group();
      set.add(t);
      String n = (t.startsWith("$") ? t.substring(1) : t).toUpperCase();
      set.add(n);
      // placeholder like W0AE0 -> address 0AE0
      if (n.length() == 5 && Character.isLetter(n.charAt(0)) && n.substring(1).matches("[0-9A-F]{4}")) {
        set.add(n.substring(1));
      }
    }
    return set;
  }
  
  /**
   * Extracts and returns all defined addresses found within the provided chunk of text.
   * <p>
   * This method scans the input {@code chunk} using a predefined regex pattern {@link #DEFINED}.
   * It captures the first group of matches, converts them to uppercase, and stores them in a new set.
   *
   * @param chunk The string containing the text to scan for addresses.
   * @return A set of uppercase strings representing the addresses found in the chunk.
   */
  private static Set<String> definedAddresses(String chunk) {
    Set<String> set = new HashSet<>();
    Matcher m = DEFINED.matcher(chunk);
    while (m.find()) set.add(m.group(1).toUpperCase());
    return set;
  }

  /**
   * Filters a map of labels, returning entries whose keys or values match specified tokens,
   * up to a maximum character count.
   * <p>
   * This method iterates through the input {@code labels} map. For each entry, it checks if
   * the key (converted to uppercase) or the value itself is present in the {@code tokens} set.
   * If a match is found, the entry is added to the output map. The process stops when the
   * cumulative length of the entries (including a buffer of 3 characters per entry) exceeds
   * the {@code maxChars} limit.
   *
   * @param labels   The source map containing label key-value pairs to filter.
   * @param tokens   The set of tokens to match against; keys are compared in uppercase.
   * @param maxChars The maximum number of characters allowed in the result set.
   * @return A new LinkedHashMap containing the filtered entries that fit within the character limit.
   */
  private static Map<String, String> relevant(Map<String, String> labels, Set<String> tokens, int maxChars) {
    Map<String, String> out = new LinkedHashMap<>();
    int chars = 0;
    for (Map.Entry<String, String> e : labels.entrySet()) {
      if (tokens.contains(e.getKey().toUpperCase()) || tokens.contains(e.getValue())) {
        chars += e.getKey().length() + e.getValue().length() + 3;
        if (chars > maxChars) break;
        out.put(e.getKey(), e.getValue());
      }
    }
    return out;
  }

  /**
   * Merges entries from the {@code answer} map into the {@code found} map based on specific priority and uniqueness rules.
   * <p>
   * This method iterates through the provided {@code answer} map and populates {@code found} with the first valid entry found for a given address.
   * It enforces uniqueness by appending the address to the name if a name collision is detected in {@code usedNames} must be updated.
   * 
   * <ul>
   *   <li>If {@code answer} is null, no action is taken.</li>
   *   <li>If an entry's name is null or empty, it is skipped.</li>
   *   <li>If {@code defined} is not empty, it only processes entries where the key matches an entry in the {@code defined} set.</li>
   *   <li>The first entry processed for a specific key "wins" and is stored in {@code found}.</li>
   *   <li>If a name is already in {@code usedNames}, it is suffixed with the address to ensure uniqueness.</li>
   * </ul>
   *
   * @param found     The destination map where discovered name-address pairs are stored.
   * @param answer    The source map containing proposed name-address pairs.
   * @param defined   A set of allowed addresses; if non-empty, only addresses in this set are processed.
   * @param known     A map used to verify if a name is the "correct" one for an address to prevent unnecessary renaming.
   * @param usedNames A set of names already assigned, used to detect and resolve name collisions.
   */
  private static void merge(Map<String, String> found, Map<String, String> answer, Set<String> defined,
          Map<String, String> known, Set<String> usedNames) {
    if (answer == null) return;
    for (Map.Entry<String, String> e : answer.entrySet()) {
      String addr = e.getKey().toUpperCase();
      String name = e.getValue();
      if (name == null || name.isEmpty()) continue;
      if (!defined.isEmpty() && !defined.contains(addr)) continue; // target lives in another block
      if (found.containsKey(addr)) continue;                       // first proposal wins
      if (usedNames.contains(name) && !name.equals(known.get(addr))) name = name + "_" + addr; // keep unique
      usedNames.add(name);
      found.put(addr, name);
    }
  }

  // ---------------------------------------------------------------------------
  // Context detection
  // ---------------------------------------------------------------------------

  /**
   * Context window for the given backend:
   *  1. AIBackendConfig.getContextWindow() if set (&gt; 0)
   *  2. for local backends: ask the server (llama.cpp /props, LM Studio /api/v0/models)
   *  3. otherwise the default
   *
   * @param cfg the backend
   * @param defaultCtx fallback (e.g. 8192)
   * @return context size in tokens
   */
  public static int detectContext(AIBackendConfig cfg, int defaultCtx) {
    if (cfg.getContextWindow() > 0) return cfg.getContextWindow();
    if (!cfg.isLocalReachabilityCheck()) return defaultCtx; // do not probe cloud services

    String root = cfg.getEndpoint().replaceAll("/(v1/)?chat/completions/?$", "");

    // llama.cpp server: {"default_generation_settings":{"n_ctx":8192,...}}
    String props = httpGet(root + "/props");
    int v = firstInt(props, "\"n_ctx\"\\s*:\\s*(\\d+)");
    if (v > 0) return v;

    // LM Studio: {"data":[{"id":"...","state":"loaded","loaded_context_length":4096,...}]}
    v = lmStudioContext(httpGet(root + "/api/v0/models"), cfg.getModel());
    if (v > 0) return v;

    return defaultCtx;
  }

  /**
   * Determine lm studio context
   * 
   * @param text the server text
   * @param model the server model
   * @return the context size
   */
  private static int lmStudioContext(String text, String model) {
    if (text == null) return 0;
    try {
      JSONArray data = new JSONObject(text).optJSONArray("data");
      if (data == null) return 0;
      int firstLoaded = 0;
      for (int i = 0; i < data.length(); i++) {
        JSONObject m = data.getJSONObject(i);
        int ctx = m.optInt("loaded_context_length", 0);
        if (ctx <= 0) continue;
        if (model != null && model.equals(m.optString("id"))) return ctx;
        if (firstLoaded == 0) firstLoaded = ctx;
      }
      return firstLoaded; // "local-model" is a placeholder: use the loaded model
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Throws ContextOverflowException if the HTTP error body says the request is too big.
   * Recognizes llama.cpp, OpenAI-like and Anthropic-like messages.
   *
   * @param errorBody the body of the error response
   * @throws ContextOverflowException when it is a context error
   */
  public static void throwIfContextError(String errorBody) throws ContextOverflowException {
    if (errorBody == null) return;
    String low = errorBody.toLowerCase();
    boolean overflow = low.contains("exceed_context_size") || low.contains("exceeds the available context size")
            || low.contains("context_length_exceeded") || low.contains("maximum context length")
            || low.contains("prompt is too long") || low.contains("context window");
    if (!overflow) return;

    int ctx = firstInt(low, "\"n_ctx\"\\s*:\\s*(\\d+)");
    if (ctx == 0) ctx = firstInt(low, "context size \\((\\d+)");
    if (ctx == 0) ctx = firstInt(low, "maximum context length is (\\d+)");
    if (ctx == 0) ctx = firstInt(low, ">\\s*(\\d+)\\s*maximum");
    throw new ContextOverflowException(errorBody, ctx);
  }

  // ---------------------------------------------------------------------------
  // Tiny HTTP GET
  // ---------------------------------------------------------------------------

  /**
   * Extracts the first integer from a text by applying a regex with a capture group.
   *
   * @param text  input text (may be null)
   * @param regex regex containing a capture group for the integer
   * @return the first matched integer, or 0 on failure
   */
  private static int firstInt(String text, String regex) {
    if (text == null) return 0;
    Matcher m = Pattern.compile(regex).matcher(text);
    try {
      return m.find() ? Integer.parseInt(m.group(1)) : 0;
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  /**
   * Performs a simple HTTP GET request with a 3-second timeout.
   *
   * @param url the URL to fetch
   * @return the response body as UTF-8 string, or null on failure
   */
  private static String httpGet(String url) {
    HttpURLConnection con = null;
    try {
      con = (HttpURLConnection) new URL(url).openConnection();
      con.setConnectTimeout(3000);
      con.setReadTimeout(3000);
      if (con.getResponseCode() != 200) return null;
      try (InputStream is = con.getInputStream()) {
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
      }
    } catch (Exception e) {
      return null;
    } finally {
      if (con != null) con.disconnect();
    }
  }
}