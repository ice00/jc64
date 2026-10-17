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

  /** Progress listener, called before each backend call */
  public interface Progress {
    /**
     * @param chunk           number of the chunk being sent (1-based)
     * @param estimatedChunks estimated total chunks (can change while running)
     * @param fromLine        first line of the chunk (1-based)
     * @param toLine          last line of the chunk (1-based)
     * @param totalLines      lines of the whole listing
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

  public Map<String, String> run(String source, Map<String, String> knownLabels,
          Caller caller, Progress progress) throws IOException {
    return run(source, knownLabels, caller, progress, null);
  }

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

  private int chunkBudgetChars() {
    int overheadTokens = (int) Math.ceil(promptOverheadChars / charsPerToken);
    int hintTokens = (int) (context * LABEL_SHARE);
    int free = context - outputReserve - overheadTokens - hintTokens;
    return (int) (Math.max(free, 128) * charsPerToken);
  }

  private static final Pattern BREAK = Pattern.compile("\\b(RTS|RTI|JMP)\\b", Pattern.CASE_INSENSITIVE);

  /** Exclusive end index; prefers to cut after RTS/RTI/JMP or on blank lines */
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

  /** Addresses that start a line in this chunk (ADDRESS BYTES INSTRUCTION) */
  private static Set<String> definedAddresses(String chunk) {
    Set<String> set = new HashSet<>();
    Matcher m = DEFINED.matcher(chunk);
    while (m.find()) set.add(m.group(1).toUpperCase());
    return set;
  }

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

  private static int firstInt(String text, String regex) {
    if (text == null) return 0;
    Matcher m = Pattern.compile(regex).matcher(text);
    try {
      return m.find() ? Integer.parseInt(m.group(1)) : 0;
    } catch (NumberFormatException e) {
      return 0;
    }
  }

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