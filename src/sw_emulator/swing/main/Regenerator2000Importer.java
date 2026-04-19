/**
 * @(#)Regenerator2000Importer 2026/03/28
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
package sw_emulator.swing.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import static sw_emulator.software.MemoryDasm.TYPE_MAJOR;
import static sw_emulator.software.MemoryDasm.TYPE_MINOR;
import static sw_emulator.software.MemoryDasm.TYPE_MINUS;
import static sw_emulator.software.MemoryDasm.TYPE_MINUS_MAJOR;
import static sw_emulator.software.MemoryDasm.TYPE_MINUS_MINOR;
import static sw_emulator.software.MemoryDasm.TYPE_PLUS;
import static sw_emulator.software.MemoryDasm.TYPE_PLUS_MAJOR;
import static sw_emulator.software.MemoryDasm.TYPE_PLUS_MINOR;

/**
 * Imports a Regenerator2000 .regen2000proj file into a jc64 {@link Project}.
 *
 * Usage:
 * <pre>
 *   Project p = Regenerator2000Importer.importFile("/path/to/file.regen2000proj");
 * </pre>
 * 
 * @author ice
 */
public class Regenerator2000Importer {

    // -------------------------------------------------------------------------
    // Public entry point
    // -------------------------------------------------------------------------

    /**
     * Reads a .regen2000proj file from disk and returns a populated {@link Project}.
     *
     * @param filePath path to the .regen2000proj file
     * @return a new {@link Project} populated with the imported data
     * @throws IOException if the file cannot be read
     */
    public static Project importFile(String filePath) throws IOException {
        byte[] raw = Files.readAllBytes(Paths.get(filePath));
        String json = new String(raw, StandardCharsets.UTF_8);
        
        Project project=importJson(json);
        if (project!=null) project.file=filePath;
        return project;
    }

    /**
     * Parses a JSON string in Regenerator2000 format and returns a populated {@link Project}.
     *
     * @param json the JSON content of the .regen2000proj file
     * @return a new {@link Project} populated with the imported data
     * @throws IOException if raw_data_base64 decompression fails
     */
    public static Project importJson(String json) throws IOException {
        JSONObject root = new JSONObject(json);

        Project project = new Project();

        // --- version (informational) -----------------------------------------
        int version = root.optInt("version", 1);
        // We log it but do not use it for migration (extend as needed)
        System.out.println("[Regenerator2000Importer] project version = " + version);

        // --- origin (load address) -------------------------------------------
        int origin = root.optInt("origin", 0);
        project.binAddress = origin;

        // --- raw binary data -------------------------------------------------
        if (root.has("raw_data_base64")) {
            String b64 = root.getString("raw_data_base64");
            byte[] binaryData = decodeGzipBase64(b64);
            project.inB = binaryData;
            // Let jc64 detect file type and fill description
            project.setData(binaryData, true); // treat as raw binary
        }

        // --- blocks → memoryFlags --------------------------------------------
        //
        // Regenerator2000 stores block ranges: [{start, end, type_, collapsed}]
        // We expand them into the flat memoryFlags[] and MemoryDasm[].
        //
        if (root.has("blocks")) {
            JSONArray blocks = root.getJSONArray("blocks");
            applyBlocks(blocks, project, origin);
        }

        // --- labels ----------------------------------------------------------
        //
        // Format: { "hexAddr": [ {name, label_type, kind}, ... ], ... }
        // We map each label to the corresponding MemoryDasm entry.
        //
        if (root.has("labels")) {
            JSONObject labels = root.getJSONObject("labels");
            for (String addrKey : labels.keySet()) {
                int addr = parseAddr(addrKey);
                if (!isValidAddr(addr)) continue;

                JSONArray labelList = labels.getJSONArray(addrKey);
                if (labelList.length() == 0) continue;

                // Use the first label name as the MemoryDasm label.
                // If multiple labels exist, append them as additional comments.
                JSONObject first = labelList.getJSONObject(0);
                String labelName = first.optString("name", "");
                if (!labelName.isEmpty()) {
                    project.memory[addr].userLocation = labelName;
                }

                // Extra labels (beyond the first) appended to block comment
                if (labelList.length() > 1) {
                    StringBuilder extra = new StringBuilder();
                    for (int i = 1; i < labelList.length(); i++) {
                        String n = labelList.getJSONObject(i).optString("name", "");
                        if (!n.isEmpty()) {
                            if (extra.length() > 0) extra.append(", ");
                            extra.append(n);
                        }
                    }
                    if (extra.length() > 0) {
                        String existing = nvl(project.memory[addr].userBlockComment);
                        project.memory[addr].userBlockComment =
                                existing.isEmpty() ? extra.toString()
                                                   : existing + "; " + extra;
                    }
                }
            }
        }

        // --- user_side_comments (also aliased as user_comments) --------------
        //
        // "Side" = inline/end-of-line comment in Regenerator2000.
        // Maps to MemoryDasm.userComment in jc64.
        //
        String sideKey = root.has("user_side_comments") ? "user_side_comments"
                       : root.has("user_comments")      ? "user_comments"
                       : null;
        if (sideKey != null) {
            JSONObject sideComments = root.getJSONObject(sideKey);
            for (String addrKey : sideComments.keySet()) {
                int addr = parseAddr(addrKey);
                if (!isValidAddr(addr)) continue;
                project.memory[addr].userComment = sideComments.optString(addrKey, "");
            }
        }

        // --- user_line_comments ----------------------------------------------
        //
        // "Line" = full-line comment placed before the instruction in Regenerator2000.
        // Maps to MemoryDasm.userBlockComment in jc64.
        //
        if (root.has("user_line_comments")) {
            JSONObject lineComments = root.getJSONObject("user_line_comments");
            for (String addrKey : lineComments.keySet()) {
                int addr = parseAddr(addrKey);
                if (!isValidAddr(addr)) continue;
                String existing = nvl(project.memory[addr].userBlockComment);
                String incoming = lineComments.optString(addrKey, "");
                project.memory[addr].userBlockComment =
                        existing.isEmpty() ? incoming : existing + "\n" + incoming;
            }
        }

        // --- bookmarks -------------------------------------------------------
        //
        // Regenerator2000 bookmarks are { addr -> label_string }.
        // jc64 does not have a direct bookmark field; we store them as
        // userComment prefix so they are not silently lost.
        // Remove or remap this block if jc64 gains a bookmark concept.
        //
        if (root.has("bookmarks")) {
            JSONObject bookmarks = root.getJSONObject("bookmarks");
            for (String addrKey : bookmarks.keySet()) {
                int addr = parseAddr(addrKey);
                if (!isValidAddr(addr)) continue;
                String bm = bookmarks.optString(addrKey, "");
                if (!bm.isEmpty()) {
                    String existing = nvl(project.memory[addr].userComment);
                    project.memory[addr].userComment =
                            existing.isEmpty() ? "[BM:" + bm + "]"
                                               : "[BM:" + bm + "] " + existing;
                }
            }
        }
        
        // --- immediate_value_formats -----------------------------------------
        //
        // Format: { "addr": "Decimal" | "Hex" | "Binary" | "Char"
        //                  | {"LowByte": targetAddr}
        //                  | {"HighByte": targetAddr} }
        //
        // Simple string formats (Decimal/Hex/…) have no direct equivalent in
        // jc64 yet — they are parsed but currently ignored.
        //
        // LowByte/HighByte mean the immediate operand at <addr> is the low or
        // high byte of <targetAddr>.  We call applyImmediateValueFormat() so
        // you can wire up the jc64 fields in one place.
        //
        if (root.has("immediate_value_formats")) {
            JSONObject ivf = root.getJSONObject("immediate_value_formats");
            for (String addrKey : ivf.keySet()) {
                int addr = parseAddr(addrKey);
                if (!isValidAddr(addr)) continue;

                Object value = ivf.get(addrKey);

                if (value instanceof JSONObject) {
                    JSONObject fmt = (JSONObject) value;

                    if (fmt.has("LowByte")) {
                        int targetAddr = fmt.getInt("LowByte");
                        applyImmediateLowByte(project, addr, targetAddr);

                    } else if (fmt.has("HighByte")) {
                        int targetAddr = fmt.getInt("HighByte");
                        applyImmediateHighByte(project, addr, targetAddr);
                    }
                }
            }
        }

        project.targetType=TargetType.C64;
        
        // --- immediate_value_formats -----------------------------------------
        //
        // Regenerator2000 allows overriding how immediate operand values are
        // displayed (hex, decimal, binary, char).  jc64 does not currently have
        // a per-address immediate format field, so we skip these silently.
        // Extend here if jc64 adds such support.
        //

        // --- settings / UI state (ignored) -----------------------------------
        // cursor_address, hex_dump_cursor_address, right_pane_visible, etc.
        // are purely UI state and have no equivalent in jc64's Project model.

        System.out.println("[Regenerator2000Importer] Import complete. Origin=0x"
                + Integer.toHexString(origin).toUpperCase()
                + ", raw data size=" + (project.inB != null ? project.inB.length : 0));

        return project;
    }

    // -------------------------------------------------------------------------
    // Block expansion
    // -------------------------------------------------------------------------

    /**
     * Expands the Regenerator2000 block array into the project's flat
     * {@code memoryFlags[]} and {@code MemoryDasm} fields.
     *
     * In Regenerator2000, {@code start} and {@code end} are byte offsets
     * from {@code origin} into the loaded binary, so we convert them to
     * absolute 64K addresses before writing into the project arrays.
     *
     * BlockType strings (from Rust enum in project.rs):
     *   "Code", "DataByte", "DataWord", "DataAddress",
     *   "DataLoAddress", "DataHiAddress", "DataHiLoAddress", "DataLoHiAddress",
     *   "DataText", "DataTextScreen", "External", "Unknown"
     */
    private static void applyBlocks(JSONArray blocks, Project project, int origin) {
        for (int i = 0; i < blocks.length(); i++) {
            JSONObject block = blocks.getJSONObject(i);

            int start    = block.optInt("start", 0);
            int end      = block.optInt("end",   0);
            String typeStr = block.optString("type_", "Unknown");
            // collapsed is UI-only state — not stored in jc64 Project

            int absStart = origin + start;
            int absEnd   = origin + end;

            DataType dt  = blockTypeToDataType(typeStr);
            boolean isCode = "Code".equals(typeStr);

            for (int addr = absStart; addr <= absEnd; addr++) {
                if (!isValidAddr(addr)) break;

                if (isCode) {
                    project.memory[addr].isCode = true;
                    project.memory[addr].isData = false;
                } else if (dt != null) {
                    project.memory[addr].isCode = false;
                    project.memory[addr].isData = true;
                    project.memory[addr].dataType = dt;
                }
                // "External"/"Unknown" → leave cell at its default (untyped)
            }
        }
    }

    /**
     * Maps a Regenerator2000 BlockType string to the corresponding jc64
     * {@link DataType} enum value.
     *
     * Returns {@code null} for Code (handled separately as {@code isCode=true})
     * and for Unknown/External (left untyped).
     *
     * NOTE: The DataType enum names below are best-guesses based on the WORD
     * example you provided.  If any name does not compile, check the enum in
     * MemoryDasm.java and replace with the correct constant name here.
     */
    private static DataType blockTypeToDataType(String type) {
        switch (type) {
            case "Code":             return null;          // handled via isCode=true
            
            case "LowByte":
            case "HighByte":
            case "DataByte":         return DataType.BYTE_HEX;
            
            case "DataWord":         return DataType.WORD;
            case "Address":          return DataType.WORD; 
            case "HiLoWord":         return DataType.WORD; 
            case "PetsciiText":      return DataType.TEXT;        // PETSCII text
            case "ScreencodeText":   return DataType.SCREEN_TEXT; // screen-code text
                                                                   // adjust name if needed
            case "External":
            case "Unknown":
            default:                 return null;
        }
    }

    /**
     * Called when the immediate operand at {@code addr} is the <b>low byte</b>
     * of {@code targetAddr} (i.e. {@code targetAddr & 0xFF}).
     *
     * Fill in whatever jc64 fields represent this relationship.
     */
    private static void applyImmediateLowByte(Project project, int addr, int targetAddr) {
        // regenerator show the instruction address
        addr+=1;
        if (addr>0xFFFF) return;
        
        switch (project.memory[addr].type) {
            case TYPE_PLUS:
              project.memory[addr].type=TYPE_PLUS_MINOR;
              project.memory[addr].related=(project.memory[addr].related<<16)+targetAddr;
              break;
            case TYPE_MINUS:
              project.memory[addr].type=TYPE_MINUS_MINOR;
              project.memory[addr].related=(project.memory[addr].related<<16)+targetAddr;
              break;
            default:
              project.memory[addr].type=TYPE_MINOR;
              project.memory[addr].related=targetAddr;
              break;
          }
    }

    /**
     * Called when the immediate operand at {@code addr} is the <b>high byte</b>
     * of {@code targetAddr} (i.e. {@code (targetAddr >> 8) & 0xFF}).
     *
     * Fill in whatever jc64 fields represent this relationship.
     */
    private static void applyImmediateHighByte(Project project, int addr, int targetAddr) {
        // regenerator show the instruction address
        addr+=1;
        if (addr>0xFFFF) return;
        
        switch (project.memory[addr].type) {
             case TYPE_PLUS:
               project.memory[addr].type=TYPE_PLUS_MAJOR;
               project.memory[addr].related=(project.memory[addr].related<<16)+targetAddr;
               break;
             case TYPE_MINUS:
               project.memory[addr].type=TYPE_MINUS_MAJOR;
               project.memory[addr].related=(project.memory[addr].related<<16)+targetAddr;
               break;
             default:
               project.memory[addr].type=TYPE_MAJOR;
               project.memory[addr].related=targetAddr;
               break;
        }
    }
    
    // -------------------------------------------------------------------------
    // Binary data decoding
    // -------------------------------------------------------------------------

    /**
     * Decodes the {@code raw_data_base64} field: base64-decode, then gunzip.
     *
     * Regenerator2000 stores the binary payload as:
     *   gzip(rawBytes) → base64
     */
    private static byte[] decodeGzipBase64(String b64) throws IOException {
        // 1. Base64 decode
        byte[] compressed = Base64.getDecoder().decode(b64);

        // 2. Gunzip
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressed);
             GZIPInputStream gzip      = new GZIPInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buf = new byte[4096];
            int n;
            while ((n = gzip.read(buf)) != -1) {
                baos.write(buf, 0, n);
            }
            return baos.toByteArray();
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Parses a hex or decimal address string.
     * Regenerator2000 serializes addresses as plain integers in JSON
     * (e.g.  {@code "labels": { "4096": [...] }}).
     */
    private static int parseAddr(String s) {
        try {
            if (s.startsWith("0x") || s.startsWith("0X")) {
                return Integer.parseInt(s.substring(2), 16);
            }
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.err.println("[Regenerator2000Importer] Cannot parse address: " + s);
            return -1;
        }
    }

    /** Returns true if addr is a valid 64K C64 address. */
    private static boolean isValidAddr(int addr) {
        return addr >= 0 && addr <= 0xFFFF;
    }

    /** Null-safe string getter. */
    private static String nvl(String s) {
        return s == null ? "" : s;
    }
}

