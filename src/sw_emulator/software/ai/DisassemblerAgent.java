/**
 * @(#)DisassemblerAgent 2026/05/30
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

import java.awt.Frame;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.json.JSONArray;
import org.json.JSONObject;
import sw_emulator.software.Assembler;
import sw_emulator.software.MemoryDasm;
import sw_emulator.software.cpu.CpuDasm;
import sw_emulator.swing.ai.JAgentProgressDialog;
import sw_emulator.swing.main.Option;

/**
 * Autonomous AI agent that analyzes a MemoryDasm[] array and applies
 * labels, line comments and block comments directly to it.
 *
 * The agent uses OpenAI-compatible "tool calling" (function calling):
 * the AI autonomously decides which tools to invoke and in what order,
 * following the analysis until it signals completion.
 *
 * Tools exposed to the AI:
 *
 *   get_disassembly(startAddr, endAddr)
 *       Returns the disassembled listing for an address range.
 *
 *   set_label(address, label)
 *       Sets userLocation on the MemoryDasm at that address.
 *
 *   set_line_comment(address, comment)
 *       Sets userComment on the MemoryDasm at that address.
 *
 *   set_block_comment(address, comment)
 *       Sets userBlockComment on the MemoryDasm at that address.
 *
 *   get_memory_info(address)
 *       Returns type (code/data/garbage), current labels and comments
 *       for a single address.
 *
 *   finish()
 *       The AI calls this when it has finished the analysis.
 *
 * Usage:
 *   DisassemblerAgent agent = new DisassemblerAgent(memory);
 *   agent.analyzeAsync(
 *       config,
 *       0x0900, 0x0FFF,          // address range to analyze
 *       (addr, label) -> { },    // called each time a label is set
 *       status -> updateUI(status),
 *       error  -> showError(error)
 *   );
 *
 * @author ice
 */
public class DisassemblerAgent {

  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------
  private static final int MAX_ITERATIONS = 30;   // safety cap on agent loops
  private static final int CONNECT_TIMEOUT = 5_000;
  private static final int RESPONSE_TIMEOUT = 180_000;

  /**
   * System prompt for starting AI
   */
  private static final String SYSTEM_PROMPT
          = "You are an expert reverse engineer specializing in M6510 assembly language\n"
          + "for the Commodore 64.\n"
          + "\n"
          + "You have been given a set of tools to analyze a disassembled C64 program.\n"
          + "Your goal is to:\n"
          + "  1. Read the disassembly using get_disassembly.\n"
          + "  2. Understand what each subroutine and branch target does.\n"
          + "  3. Assign meaningful English labels using set_label.\n"
          + "     Labels must be camelCase, single word, no spaces.\n"
          + "  4. Optionally add short English line comments with set_line_comment.\n"
          + "  5. Optionally add block comments before key routines with set_block_comment.\n"
          + "  6. Follow JSR/JMP targets outside the initial range if needed.\n"
          + "  7. Call finish() when you have completed the analysis.\n"
          + "\n"
          + "Rules:\n"
          + "  - Never overwrite an existing userLocation that is already meaningful\n"
          + "    (i.e. not a generic placeholder like W09A0).\n"
          + "  - Labels must be unique across the entire 64K address space.\n"
          + "  - Be concise in comments – one short sentence maximum.\n"
          + "  - Always call finish() at the end, even if you found nothing to label.\n"
          + "  - If labels or comments are already present, review them: improve vague names,\n"
          + "    add missing comments, correct wrong memory types. This may be a re-analysis run.\n"
          + "  - Use set_memory_type when bytes make no sense as code (e.g. lookup tables,\n"
          + "    sprite bitmaps, text strings, music data).";

  // ---------------------------------------------------------------------------
  // Tool definitions (sent to the AI in every request)
  // ---------------------------------------------------------------------------
  private static final JSONArray TOOL_DEFINITIONS = buildToolDefinitions();

  /**
   * Build the tools as JSON
   * 
   * @return the tools definitions
   */
  private static JSONArray buildToolDefinitions() {
    JSONArray tools = new JSONArray();

    tools.put(tool("get_disassembly",
            "Returns the disassembled listing for a memory address range. "
            + "Use this to read code before deciding on labels.",
            params()
                    .put("startAddr", param("integer",
                            "Start address in decimal (0..65535)"))
                    .put("endAddr", param("integer",
                            "End address in decimal (0..65535, inclusive)")),
            list("startAddr", "endAddr")));

    tools.put(tool("set_label",
            "Sets a label (userLocation) on a memory address. "
            + "Use camelCase, single word, no spaces.",
            params()
                    .put("address", param("integer", "Target address in decimal (0..65535)"))
                    .put("label", param("string", "Label name, camelCase, no spaces")),
            list("address", "label")));

    tools.put(tool("set_line_comment",
            "Sets a short inline comment (userComment) on a memory address.",
            params()
                    .put("address", param("integer", "Target address in decimal (0..65535)"))
                    .put("comment", param("string", "Short English comment, one sentence max")),
            list("address", "comment")));

    tools.put(tool("set_block_comment",
            "Sets a block comment (userBlockComment) shown before a memory address. "
            + "Use for subroutine headers.",
            params()
                    .put("address", param("integer", "Target address in decimal (0..65535)"))
                    .put("comment", param("string", "Block comment text")),
            list("address", "comment")));

    tools.put(tool("get_memory_info",
            "Returns detailed info for a single address: type, current labels, comments.",
            params()
                    .put("address", param("integer", "Address in decimal (0..65535)")),
            list("address")));

    tools.put(tool("set_memory_type",
            "Sets whether a memory address contains code or data. "
            + "Use this when you determine that something previously marked as code "
            + "is actually data (e.g. a lookup table, sprite data, string) or vice versa.",
            params()
                    .put("address", param("integer", "Target address in decimal (0..65535)"))
                    .put("type", param("string",
                            "One of: 'code', 'data', 'garbage'. "
                            + "'garbage' means unknown/unused content.")),
            list("address", "type")));

    tools.put(tool("finish",
            "Call this when you have finished the analysis. No parameters needed.",
            new JSONObject(),
            new JSONArray()));

    return tools;
  }

  // ---------------------------------------------------------------------------
  // Instance state
  // ---------------------------------------------------------------------------
  private final MemoryDasm[] memory;
  private final CpuDasm dasm;
  private final byte[] buffer;   // raw memory buffer parallel to memory[]
  private final Option option;

  private static final Logger LOG
          = Logger.getLogger(DisassemblerAgent.class.getName());

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------
  /**
   * Full constructor – uses the real M6510Dasm for accurate disassembly output.
   * This is the preferred constructor.
   *
   * @param memory the full 65536-element MemoryDasm array from the Project
   * @param dasm the configured M6510Dasm instance (with option/assembler set)
   * @param option the assembler option
   * @param buffer the raw 65536-byte memory buffer (parallel to memory[])
   */
  public DisassemblerAgent(MemoryDasm[] memory, CpuDasm dasm, Option option, byte[] buffer) {
    if (memory == null || memory.length != 65536) {
      throw new IllegalArgumentException("memory must be a 65536-element MemoryDasm array");
    }
    //if (buffer == null || buffer.length != 65536) {
    //  throw new IllegalArgumentException("buffer must be a 65536-element byte array");
    //}
    this.memory = memory;
    this.dasm = dasm;
    this.buffer = buffer;
    this.option=option;
    dasm.setMemory(memory);
    dasm.setOption(option, new Assembler());
          
  }

  /**
   * Fallback constructor – no real disassembler available. toolGetDisassembly
   * will fall back to showing raw hex bytes.
   *
   * @param memory the full 65536-element MemoryDasm array from the Project
   */
  public DisassemblerAgent(MemoryDasm[] memory) {
    if (memory == null || memory.length != 65536) {
      throw new IllegalArgumentException("memory must be a 65536-element MemoryDasm array");
    }
    this.memory = memory;
    this.dasm = null;
    this.buffer = null;
    this.option = null;
  }

  // ---------------------------------------------------------------------------
  // Public API
  // ---------------------------------------------------------------------------
  
  /**
   * Estimates the number of agent steps for a given address range.
   *
   * Each step = one tool call the AI makes:
   *   - 1 get_disassembly call per ~256 bytes (AI may split into chunks)
   *   - ~1 set_label call per ~16 bytes on average
   *   - a few set_comment, set_block_comment, finish calls
   * Minimum 10, maximum 200.
   * 
   * @param startAddr starting address
   * @param endAddr ending address
   * @return the estimate steps
   */
  public static int estimateSteps(int startAddr, int endAddr) {
    int rangeBytes  = Math.max(1, endAddr - startAddr + 1);
    int getDisasmCalls = Math.max(1, rangeBytes / 256);
    int setLabelCalls  = Math.max(1, rangeBytes / 16);
    int overhead       = 5;  // finish + comments + follow-up calls
    return Math.min(200, Math.max(10, getDisasmCalls + setLabelCalls + overhead));
  }
 
  
  /**
   * Convenience method for launching the agent from a Swing button.
   *
   * Shows a modal progress dialog, runs the agent on a background thread,
   * and updates the dialog as the agent works. The calling thread (EDT)
   * is blocked by the modal dialog until the agent finishes or the user
   * clicks Cancel.
   *
   * Example usage from an ActionListener:
   * <pre>
   *   JButton btn = new JButton("AI Analyze");
   *   btn.addActionListener(e -> {
   *       DisassemblerAgent agent = new DisassemblerAgent(memory, dasm, buffer);
   *       agent.analyzeWithProgress(ownerFrame, config, 0x0900, 0x0FFF,
   *           (addr, label) -> refreshView(),
   *           resultMsg -> statusBar.setText(resultMsg));
   *   });
   * </pre>
   *
   * @param owner        parent JFrame for the modal dialog
   * @param config       AI backend configuration
   * @param startAddr    first address to analyze (inclusive)
   * @param endAddr      last address to analyze (inclusive)
   * @param onLabelSet   called each time a label is written to MemoryDasm
   *                     (called on background thread – wrap in invokeLater if touching UI)
   * @param onFinished   called when done (on EDT) with a summary message
   */
  public void analyzeWithProgress(Frame owner,
                                   AIBackendConfig config,
                                   int startAddr,
                                   int endAddr,
                                   BiConsumer<Integer, String> onLabelSet,
                                   Consumer<String> onFinished) {
    boolean cancelRequested = false;

    int maxSteps = estimateSteps(startAddr, endAddr);
    JAgentProgressDialog dlg = new JAgentProgressDialog(owner, maxSteps);

    // Launch agent on a background thread BEFORE showing the modal dialog
    Thread agentThread = new Thread(() -> {
      try {
        runAgent(config, startAddr, endAddr,
            onLabelSet,
            msg -> dlg.step(msg),              // each status update = one step
            msg2 -> dlg.logAction(msg2));          

        String summary = cancelRequested
            ? "Cancelled by user."
            : "Analysis complete.";
        dlg.finish(summary);
        SwingUtilities.invokeLater(() -> onFinished.accept(summary));

      } catch (Exception e) {
        LOG.log(Level.SEVERE, "Agent error", e);
        String msg = "Error: " + e.getMessage();
        dlg.finish(msg);
        SwingUtilities.invokeLater(() -> onFinished.accept(msg));
      }
    }, "DisassemblerAgent");
    agentThread.setDaemon(true);
    agentThread.start();

    // Show the modal dialog – this BLOCKS the EDT until dispose() is called
    dlg.setVisible(true);
  }  
  
  /**
   * Starts the autonomous analysis in a background thread. Safe to call from
   * the Swing EDT.
   *
   * @param config backend configuration (any OpenAI-compatible provider)
   * @param startAddr first address to analyze (inclusive)
   * @param endAddr last address to analyze (inclusive)
   * @param onLabelSet called (on the background thread) each time the AI sets a
   * label: (address, labelName)
   * @param onStatus called with progress messages (for the UI status label)
   * @param onFinished called when the agent finishes (success or error)
   * @param onAction called when there ia action log
   */
  public void analyzeAsync(AIBackendConfig config,
          int startAddr,
          int endAddr,
          BiConsumer<Integer, String> onLabelSet,
          Consumer<String> onStatus,
          Consumer<String> onFinished,
          Consumer<String> onAction) {

    Thread t = new Thread(() -> {
      try {
        runAgent(config, startAddr, endAddr, onLabelSet, onStatus, onAction);
        onFinished.accept("Analysis complete.");
      } catch (Exception e) {
        LOG.log(Level.SEVERE, "Agent error", e);
        onFinished.accept("Error: " + e.getMessage());
      }
    }, "DisassemblerAgent");
    t.setDaemon(true);
    t.start();
  }

  // ---------------------------------------------------------------------------
  // Agent loop
  // ---------------------------------------------------------------------------
  private void runAgent(AIBackendConfig config,
          int startAddr,
          int endAddr,
          BiConsumer<Integer, String> onLabelSet,
          Consumer<String> onStatus,
          Consumer<String> onAction)
          throws IOException {

    // Conversation history – grows as the agent works
    JSONArray messages = new JSONArray();

    // Initial user message telling the agent what to do
    messages.put(new JSONObject()
            .put("role", "system")
            .put("content", SYSTEM_PROMPT)
            .put("role", "user")
            .put("content",
                    "Analyze the C64 disassembly in the address range $"
                    + String.format("%04X", startAddr)
                    + " to $"
                    + String.format("%04X", endAddr)
                    + ". Use get_disassembly to read the code, then set meaningful labels "
                    + "and comments. Call finish() when done."));

    onStatus.accept("Agent started – analyzing $"
            + String.format("%04X", startAddr)
            + "–$" + String.format("%04X", endAddr));

    int iterations = 0;

    while (iterations++ < MAX_ITERATIONS) {

      // Call the AI
      JSONObject response = callApi(config, messages);
      JSONObject choice = response.getJSONArray("choices").getJSONObject(0);
      JSONObject message = choice.getJSONObject("message");

      // Add assistant message to history
      messages.put(message);

      String finishReason = choice.getString("finish_reason");

      // No tool calls → agent replied with plain text (shouldn't happen, but handle it)
      if (!"tool_calls".equals(finishReason)) {
        LOG.info("Agent finished with reason: " + finishReason);
        break;
      }

      JSONArray toolCalls = message.optJSONArray("tool_calls");
      if (toolCalls == null || toolCalls.isEmpty()) {
        break;
      }

      boolean done = false;

      // Execute each tool call the AI requested
      for (int i = 0; i < toolCalls.length(); i++) {
        JSONObject call = toolCalls.getJSONObject(i);
        String callId = call.getString("id");
        String toolName = call.getJSONObject("function").getString("name");
        JSONObject args = new JSONObject(
                call.getJSONObject("function").getString("arguments"));

        LOG.info("Tool call: " + toolName + " " + argsToLogString(args));
        onAction.accept("Tool call: " + toolName + " " + argsToLogString(args));
        onStatus.accept("AI calling: " + toolName);

        String result = executeTool(toolName, args, onLabelSet);

        // Add tool result to conversation
        messages.put(new JSONObject()
                .put("role", "tool")
                .put("tool_call_id", callId)
                .put("content", result));

        if ("finish".equals(toolName)) {
          done = true;
        }
      }

      if (done) {
        break;
      }
    }

    if (iterations > MAX_ITERATIONS) {
      LOG.warning("Agent reached max iterations (" + MAX_ITERATIONS + ")");
    }
  }

  // ---------------------------------------------------------------------------
  // Tool execution
  // ---------------------------------------------------------------------------
  
 /**
   * Parses an address from a JSON argument that may be:
   *   - an integer:  {"address": 49165}
   *   - a decimal string: {"address": "49165"}
   *   - a hex string with prefix: {"address": "0xC40D"} or {"address": "$C40D"}
   *   - a bare hex string: {"address": "C40D"}
   *
   * Returns the integer address value, or -1 on parse failure.
   */
  private int parseAddress(JSONObject args, String key) {
    try {
      // First try as plain integer
      if (args.get(key) instanceof Number) {
        return args.getInt(key);
      }
      // It's a string – try to parse it
      String raw = args.getString(key).trim();

      // Strip common hex prefixes
      if (raw.startsWith("0x") || raw.startsWith("0X")) raw = raw.substring(2);
      else if (raw.startsWith("$"))                      raw = raw.substring(1);

      // If it looks like a hex string (contains A-F), parse as hex
      if (raw.matches("[0-9A-Fa-f]+")) {
        // Try decimal first if it's all digits
        if (raw.matches("[0-9]+")) return Integer.parseInt(raw);
        int hexVal = Integer.parseInt(raw, 16);
        LOG.warning("AI passed hex address \"" + raw + "\" – interpreted as decimal " + hexVal
                    + ". Prompt the model to use decimal integers.");
        return hexVal;
      }
      return Integer.parseInt(raw);
    } catch (Exception e) {
      LOG.warning("Cannot parse address from key '" + key + "': " + args.opt(key));
      return -1;
    }
  }
    
  /**
   * Execute the tools provided to AI
   * 
   * @param name the name of tool
   * @param args the arguments of tool
   * @param onLabelSet the consumer for showing labes
   * @return the result
   */
  private String executeTool(String name, JSONObject args,
                           BiConsumer<Integer, String> onLabelSet) {
    try {
        switch (name) {

            case "get_disassembly": {
                int start = parseAddress(args, "startAddr");
                int end   = parseAddress(args, "endAddr");
                return toolGetDisassembly(start, end);
            }

            case "set_label": {
                int addr = parseAddress(args, "address");
                if (addr < 0) {
                    return "Error: invalid address: " + args.opt("address");
                }
                String label = args.getString("label").trim();
                return toolSetLabel(addr, label, onLabelSet);
            }

            case "set_line_comment": {
                int addr = parseAddress(args, "address");
                String comment = args.getString("comment").trim();
                return toolSetLineComment(addr, comment);
            }

            case "set_block_comment": {
                int addr = parseAddress(args, "address");
                String comment = args.getString("comment").trim();
                return toolSetBlockComment(addr, comment);
            }

            case "get_memory_info": {
                int addr = parseAddress(args, "address");
                return toolGetMemoryInfo(addr);
            }

            case "set_memory_type": {
                int addr = parseAddress(args, "address");
                String type = args.getString("type").trim().toLowerCase();
                return toolSetMemoryType(addr, type);
            }

            case "finish":
                return "Analysis marked as complete.";

            default:
                return "Unknown tool: " + name;
        }

    } catch (Exception e) {
        return "Tool error: " + e.getMessage();
    }
}

 
  // ---------------------------------------------------------------------------
  // Tool implementations
  // ---------------------------------------------------------------------------
  /**
   * Returns the disassembled listing for the given address range.
   *
   * If a real M6510Dasm was provided at construction, uses csdasm() for
   * accurate mnemonic output (JSR $0AE0, LDA #$35, BNE label, etc.). Otherwise
   * falls back to a raw-hex approximation.
   */
  private String toolGetDisassembly(int startAddr, int endAddr) {
    startAddr = clamp(startAddr);
    endAddr = clamp(endAddr);

    if (startAddr > endAddr) {
      return "Error: startAddr > endAddr";
    }

    // --- Real disassembly via M6510Dasm ---
    if (dasm != null && buffer != null) {
      try {
        // csdasm produces clean source-style output:
        //   label:
        //     LDA #$35        ; comment
        //     JSR $0AE0
        String result = dasm.cdasm(buffer, startAddr, endAddr, startAddr);
        if (result == null || result.isBlank()) {
          return "No code/data found in range $"
                  + String.format("%04X", startAddr)
                  + "-$" + String.format("%04X", endAddr);
        }
        return result;
      } catch (Exception e) {
        LOG.warning("csdasm failed, falling back to hex: " + e.getMessage());
        // fall through to hex fallback below
      }
    }

    // --- Hex fallback (no M6510Dasm available) ---
    StringBuilder sb = new StringBuilder();
    int addr = startAddr;
    while (addr <= endAddr) {
      MemoryDasm m = memory[addr];
      if (!m.isInside) {
        addr++;
        continue;
      }

      String label = effectiveLabel(m);
      if (label != null && !label.isBlank()) {
        sb.append(label).append(":\n");
      }

      if (m.userBlockComment != null && !m.userBlockComment.isBlank()) {
        for (String line : m.userBlockComment.split("\n")) {
          sb.append("; ").append(line).append("\n");
        }
      }

      sb.append(String.format("%04X  %02X  ", addr, m.copy & 0xFF));

      if (m.isCode) {
        sb.append(opcodeHint(addr));
      } else if (m.isData) {
        sb.append(".BYTE $").append(String.format("%02X", m.copy & 0xFF));
      } else if (m.isGarbage) {
        sb.append("??? ; garbage");
      } else {
        sb.append("??? ; type not set");
      }

      String comment = m.userComment != null ? m.userComment : m.dasmComment;
      if (comment != null && !comment.isBlank()) {
        sb.append("   ; ").append(comment);
      }

      sb.append("\n");
      addr++;
    }

    if (sb.isEmpty()) {
      return "No code/data found in range $"
              + String.format("%04X", startAddr)
              + "-$" + String.format("%04X", endAddr);
    }
    return sb.toString();
  }

  /**
   * Sets or updates a user-defined label for a specific memory address.
   * <p>
   * This method enforces specific constraints on the label format:
   * <ul>
   *   <li>The label must not be empty.</li>
   *   <li>The label must not contain spaces.</li>
   * </ul>
   * <p>
   * It also implements a protection logic for "meaningful" labels.
   * Existing labels are preserved unless they are considered generic placeholders
   * (matching the pattern `W[0-9A-Fa-f]{4}`) or are shorter than 4 characters.
   *
   * @param addr      The memory address (0-65535) to label. Will be clamped to a valid range.
   * @param label     The new label text to assign.
   * @param onLabelSet An optional callback invoked when a label is successfully set.
   * @return A status message indicating success or the reason for failure/skip.
   */
  private String toolSetLabel(int addr, String label,
          BiConsumer<Integer, String> onLabelSet) {
    addr = clamp(addr);
    if (label.isEmpty()) {
      return "Error: label must not be empty";
    }
    if (label.contains(" ")) {
      return "Error: label must not contain spaces";
    }

    MemoryDasm m = memory[addr];

    // Don't overwrite a meaningful existing user label unless the new one is better.
    // "Better" means: existing is a generic placeholder (W09A0) or very short (< 4 chars).
    if (m.userLocation != null && !m.userLocation.isBlank()) {
      boolean isPlaceholder = m.userLocation.matches("W[0-9A-Fa-f]{4}");
      boolean isTooShort = m.userLocation.length() < 4;
      if (!isPlaceholder && !isTooShort) {
        return "Skipped: address $" + String.format("%04X", addr)
                + " already has meaningful label '" + m.userLocation + "'";
      }
      // Allow overwrite of placeholder or very short label – log the replacement
      LOG.info("Replacing weak label '" + m.userLocation + "' with '" + label
              + "' at $" + String.format("%04X", addr));
    }

    m.userLocation = label;
    if (onLabelSet != null) {
      onLabelSet.accept(addr, label);
    }
    LOG.info("Label set: $" + String.format("%04X", addr) + " → " + label);
    return "OK: label '" + label + "' set at $" + String.format("%04X", addr);
  }

  /**
   * Sets a single-line comment for the instruction at the specified memory address.
   * <p>
   * The comment is associated directly with the disassembled instruction at that address.
   *
   * @param addr    The memory address (0-65535) to associate the comment with.
   *                Will be clamped to a valid range.
   * @param comment The comment text to assign. Can be an empty string to clear the comment.
   * @return A status message indicating success, including the formatted address.
   */
  private String toolSetLineComment(int addr, String comment) {
    addr = clamp(addr);
    memory[addr].userComment = comment;
    return "OK: line comment set at $" + String.format("%04X", addr);
  }

   /**
   * Sets a multi-line (block) comment for the instruction at the specified memory address.
   * <p>
   * Unlike {@link #toolSetLineComment(int, String)}, this method stores a comment
   * intended to span multiple lines in disassembler output.
   *
   * @param addr    The memory address (0-65535) to associate the comment with.
   *                Will be clamped to a valid range.
   * @param comment The comment text to assign. Can be an empty string to clear the comment.
   * @return A status message indicating success, including the formatted address.
   */
  private String toolSetBlockComment(int addr, String comment) {
    addr = clamp(addr);
    memory[addr].userBlockComment = comment;
    return "OK: block comment set at $" + String.format("%04X", addr);
  }

  /**
   * Retrieves detailed information about the disassembled entry at the specified memory address.
   * <p>
   * The method returns a JSON object containing various properties of the memory entry,
   * including its classification (code/data/garbage), raw byte value, labels, and comments.
   *
   * @param addr The memory address (0-65535) to inspect.
   *             Will be clamped to a valid range.
   * @return A pretty-printed JSON string containing memory info with the following keys:
   *         <ul>
   *           <li>{@code address}: Hexadecimal address string (e.g., "1234").</li>
   *           <li>{@code isInside}: Boolean indicating if the address is inside the code/data region.</li>
   *           <li>{@code isCode}: Boolean indicating if the entry is classified as code.</li>
   *           <li>{@code isData}: Boolean indicating if the entry is classified as data.</li>
   *           <li>{@code isGarbage}: Boolean indicating if the entry is classified as garbage.</li>
   *           <li>{@code byte}: Hexadecimal byte value (e.g., "FF").</li>
   *           <li>{@code dasmLabel}: The auto-generated label from disassembly.</li>
   *           <li>{@code userLabel}: The user-defined label (empty string if null).</li>
   *           <li>{@code dasmComment}: The auto-generated comment from disassembly.</li>
   *           <li>{@code userComment}: The user-defined line comment (empty string if null).</li>
   *           <li>{@code blockComment}: The user-defined block comment (empty string if null).</li>
   *         </ul>
   */
  private String toolGetMemoryInfo(int addr) {
    addr = clamp(addr);
    MemoryDasm m = memory[addr];

    JSONObject info = new JSONObject();
    info.put("address", String.format("%04X", addr));
    info.put("isInside", m.isInside);
    info.put("isCode", m.isCode);
    info.put("isData", m.isData);
    info.put("isGarbage", m.isGarbage);
    info.put("byte", String.format("%02X", m.copy & 0xFF));
    info.put("dasmLabel", m.dasmLocation != null ? m.dasmLocation : "");
    info.put("userLabel", m.userLocation != null ? m.userLocation : "");
    info.put("dasmComment", m.dasmComment != null ? m.dasmComment : "");
    info.put("userComment", m.userComment != null ? m.userComment : "");
    info.put("blockComment", m.userBlockComment != null ? m.userBlockComment : "");

    return info.toString(2);
  }

  /**
   * Changes the classification of the memory entry at the specified address.
   * <p>
   * Valid types are "code", "data", or "garbage". The implementation ensures
   * mutual exclusivity: setting one type automatically unsets the others.
   *
   * @param addr The memory address (0-65535) to classify.
   *             Will be clamped to a valid range.
   * @param type The desired classification: "code", "data", or "garbage".
   * @return A status message indicating success or an error if the type is invalid.
   * @throws IllegalArgumentException if an invalid type string is passed.
   *         (Note: The method currently returns an error string instead, so this tag
   *         is informational; consider using Exception for strict enforcement in future refactors.)
   */
  private String toolSetMemoryType(int addr, String type) {
    addr = clamp(addr);
    MemoryDasm m = memory[addr];

    switch (type) {

      case "code": {
        m.isCode = true;
        m.isData = false;
        m.isGarbage = false;
        break;
      }

      case "data": {
        m.isCode = false;
        m.isData = true;
        m.isGarbage = false;
        break;
      }

      case "garbage": {
        m.isCode = false;
        m.isData = false;
        m.isGarbage = true;
        break;
      }

      default:
        return "Error: type must be 'code', 'data' or 'garbage', got: " + type;
    }

    LOG.info("Memory type set: $" + String.format("%04X", addr) + " → " + type);
    return "OK: $" + String.format("%04X", addr) + " marked as " + type;
  }

  /**
   * Executes an HTTP POST request to an AI API endpoint, sending a structured JSON payload.
   * <p>
   * This method handles connection setup, authentication header injection,
   * and response parsing. It supports extra custom headers defined in the configuration.
   *
   * @param config The configuration object containing the API endpoint, model,
   *               timeout settings, and authentication details.
   * @param messages An array of message objects to send to the AI model.
   * @return A {@link JSONObject} representing the parsed JSON response from the server.
   * @throws IOException If the HTTP request fails (connection errors) or the
   *         server returns a non-200 status code.
   */
  private JSONObject callApi(AIBackendConfig config, JSONArray messages)
          throws IOException {

    JSONObject body = new JSONObject();
    body.put("model", config.getModel());
    body.put("max_tokens", config.getMaxOutputTokens());
    body.put("messages", messages);
    body.put("tools", TOOL_DEFINITIONS);
    body.put("tool_choice", "auto");   // AI decides which tools to call

    HttpURLConnection conn
            = (HttpURLConnection) new URL(config.getEndpoint()).openConnection();
    try {
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setConnectTimeout(CONNECT_TIMEOUT);
      conn.setReadTimeout(RESPONSE_TIMEOUT);
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setRequestProperty("Accept", "application/json");

      if (config.requiresApiKey()) {
        String prefix = config.getAuthHeaderPrefix();
        String value = (prefix != null && !prefix.isBlank())
                ? prefix + " " + config.getApiKey()
                : config.getApiKey();
        conn.setRequestProperty(config.getAuthHeaderName(), value);
      }
      String[] extra = config.getExtraHeaders();
      for (int i = 0; i + 1 < extra.length; i += 2) {
        conn.setRequestProperty(extra[i], extra[i + 1]);
      }

      byte[] bodyBytes = body.toString().getBytes(StandardCharsets.UTF_8);
      try (OutputStream os = conn.getOutputStream()) {
        os.write(bodyBytes);
        os.flush();
      }

      int status = conn.getResponseCode();
      InputStream is = (status == 200)
              ? conn.getInputStream() : conn.getErrorStream();
      String resp = new String(is.readAllBytes(), StandardCharsets.UTF_8);

      if (status != 200) {
        throw new IOException("API error HTTP " + status + ": " + resp);
      }

      return new JSONObject(resp);

    } finally {
      conn.disconnect();
    }
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------
  
  /**
   * Converts tool call arguments into a log-friendly string representation.
   * <p>
   * Specifically, this method normalizes address-related fields
   * ({@code address}, {@code startAddr}, {@code endAddr}) to display them
   * as hexadecimal strings prefixed with "$" (e.g., "$1234"), regardless
   * of the original format passed by the AI. This ensures consistent formatting in logs.
   *
   * @param args A {@link JSONObject} containing the raw arguments from a tool call.
   * @return A pretty-printed (or compact) string representation of the arguments,
   *         with address fields formatted as hexadecimal strings.
   */
  private String argsToLogString(JSONObject args) {
    JSONObject display = new JSONObject(args.toString()); // shallow copy
    for (String key : new String[]{"address", "startAddr", "endAddr"}) {
        if (display.has(key)) {
            int val = parseAddress(display, key);
            if (val >= 0) display.put(key, "$" + String.format("%04X", val));
        }
    }
    return display.toString();
  }
  
 /**
   * Parses an address from a JSONObject argument that may be either:
   *   - an integer:  {"address": 39875}       (decimal)
   *   - a string:    {"address": "C40D"}       (hex, with or without $ prefix)
   *   - a string:    {"address": "0xC40D"}     (hex with 0x prefix)
   *
   * All formats are accepted because different AI models format tool call
   * arguments differently even when instructed to use decimal.
   
  private static int parseAddress(JSONObject args, String key) {
    Object raw = args.get(key);
    if (raw instanceof Number) {
      return clamp(((Number) raw).intValue());
    }
    // String form – strip known prefixes and parse as hex
    String s = raw.toString().trim()
                  .replaceFirst("^\$", "")    // remove leading $
                  .replaceFirst("^0[xX]", ""); // remove leading 0x
    try {
      // Try hex first (most common when AI ignores the decimal instruction)
      int val = Integer.parseInt(s, 16);
      // Sanity check: if value > 65535 it was probably decimal all along
      if (val > 65535) {
        val = Integer.parseInt(s, 10);
      }
      return clamp(val);
    } catch (NumberFormatException e) {
      try {
        return clamp(Integer.parseInt(s, 10));
      } catch (NumberFormatException e2) {
        LOG.warning("Cannot parse address from: " + raw + " – defaulting to 0");
        return 0;
      }
    }
  }  
   */
  
  /**
   * Clamps an integer value to the valid 16-bit memory address range.
   * <p>
   * This ensures that any provided address is within the range of 0 to 65535.
   * Values less than 0 are clamped to 0, and values greater than 65535 are clamped to 65535.
   *
   * @param addr The address value to clamp.
   * @return The clamped address value, guaranteed to be between 0 and 65535.
   */
  private static int clamp(int addr) {
    return Math.max(0, Math.min(65535, addr));
  }

  /**
   * Retrieves the most specific label for a memory entry.
   * <p>
   * This method prioritizes user-defined labels over auto-generated disassembly labels.
   * It returns the user-defined label if it is non-null and not blank.
   * Otherwise, it returns the disassembly label if it is non-null and not blank.
   * Returns {@code null} if neither is available or valid.
   *
   * @param m The {@link MemoryDasm} object representing the memory entry.
   * @return The preferred label string, or {@code null} if no valid label exists.
   */
  private String effectiveLabel(MemoryDasm m) {
    if (m.userLocation != null && !m.userLocation.isBlank()) {
      return m.userLocation;
    }
    if (m.dasmLocation != null && !m.dasmLocation.isBlank()) {
      return m.dasmLocation;
    }
    return null;
  }

  /**
   * Retrieves a minimal sequence of raw opcode bytes to provide context to the AI.
   * <p>
   * By reading up to 3 bytes starting at the specified address, this method
   * allows the AI to recognize instruction patterns without performing a full
   * disassembly pass. It respects the {@code isInside} flag to stop collecting
   * bytes if a non-code/data memory boundary is encountered.
   *
   * @param addr The starting memory address to inspect.
   * @return A space-separated string of hexadecimal byte values (e.g., "A9 FF 85")
   *         representing the first few bytes of the instruction stream.
   */
  private String opcodeHint(int addr) {
    // Collect up to 3 bytes
    List<String> bytes = new ArrayList<>();
    for (int i = 0; i < 3 && (addr + i) < 65536; i++) {
      if (i > 0 && !memory[addr + i].isInside) {
        break;
      }
      bytes.add(String.format("%02X", memory[addr + i].copy & 0xFF));
    }
    return String.join(" ", bytes);
  }

  /**
   * Constructs a standard JSON function definition for tool use.
   * <p>
   * This helper simplifies the creation of OpenAI-compatible tool definitions by
   * encapsulating the nested structure required for {@code type: "function"}.
   *
   * @param name        The name of the function/tool.
   * @param description A human-readable description of what the function does.
   * @param properties  A {@link JSONObject} defining the parameters' schema (types, descriptions).
   * @param required    A {@link JSONArray} listing the names of required parameters.
   * @return A {@link JSONObject} representing the complete function definition.
   */
  private static JSONObject tool(String name, String description,
          JSONObject properties, JSONArray required) {
    return new JSONObject()
            .put("type", "function")
            .put("function", new JSONObject()
                    .put("name", name)
                    .put("description", description)
                    .put("parameters", new JSONObject()
                            .put("type", "object")
                            .put("properties", properties)
                            .put("required", required)));
  }

  /**
   * Creates an empty JSON object, typically used as the base for
   * defining function parameters.
   *
   * @return A new, empty {@link JSONObject}.
   */
  private static JSONObject params() {
    return new JSONObject();
  }

  /**
   * Creates a JSON object representing a single parameter definition.
   * <p>
   * The parameter is defined by its data type (e.g., "string", "integer")
   * and a description.
   *
   * @param type        The JSON schema type of the parameter.
   * @param description A human-readable explanation of the parameter.
   * @return A {@link JSONObject} containing the {@code type} and {@code description} keys.
   */
  private static JSONObject param(String type, String description) {
    return new JSONObject()
            .put("type", type)
            .put("description", description);
  }

  /**
   * Creates a JSON array containing the given string items.
   * <p>
   * This is a utility method to simplify the creation of lists
   * for things like "required" parameter fields or enum options.
   *
   * @param items The string elements to include in the array.
   * @return A {@link JSONArray} containing the provided items.
   */
  private static JSONArray list(String... items) {
    JSONArray a = new JSONArray();
    for (String s : items) {
      a.put(s);
    }
    return a;
  }
}
