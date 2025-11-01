/**
 * @(#)XRefManager.java 2025/10/18
 *
 * ICE Team Free Software Group
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
package sw_emulator.software.memory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manage cross-reference for addresses
 */
public class XRefManager {

  /**
   * Type of XRef
   */
  public enum XRefType {
    READ, // LDA, LDX, LDY, etc.
    WRITE, // STA, STX, STY, etc.
    CALL, // JSR
    JUMP, // JMP
    BRANCH, // BCC, BCS, BNE, etc.
    POINTER, // Puntatori (LDA ($FE),Y)
    COMPARE, // CMP, CPX, CPY
    MODIFY, // INC, DEC, ASL, LSR, etc.
    BIT_TEST        // BIT
  }

  /**
   * A single XRef
   */
  public static class XRef {

    public final int sourceAddress;    // Indirizzo da cui parte il riferimento
    public final XRefType type;        // Tipo di riferimento
    public final String instruction;   // Istruzione completa (es: "LDA $D000")
    public final String context;       // Contesto opzionale

    public XRef(int sourceAddress, XRefType type, String instruction, String context) {
      this.sourceAddress = sourceAddress;
      this.type = type;
      this.instruction = instruction;
      this.context = context;
    }

    @Override
    public String toString() {
      return String.format("$%04X: %s - %s", sourceAddress, type, instruction);
    }
  }

  // Principal map: target address -> list of references 
  private final Map<Integer, List<XRef>> xrefMap = new HashMap<>();

  // Cache for precalculate statistics
  private final Map<Integer, XRefStats> statsCache = new HashMap<>();
  private boolean cacheDirty = true;

  /**
   * Statistics for a target address
   */
  public static class XRefStats {
    public final int totalReferences;
    public final int readCount;
    public final int writeCount;
    public final int callCount;
    public final int jumpCount;
    public final int branchCount;
    public final int pointerCount;
    public final int compareCount;
    public final int modifyCount;
    public final int bitTestCount;
    public final List<Integer> sourceAddresses;

    public XRefStats(List<XRef> xrefs) {
      this.totalReferences = xrefs.size();
      this.readCount = (int) xrefs.stream().filter(x -> x.type == XRefType.READ).count();
      this.writeCount = (int) xrefs.stream().filter(x -> x.type == XRefType.WRITE).count();
      this.callCount = (int) xrefs.stream().filter(x -> x.type == XRefType.CALL).count();
      this.jumpCount = (int) xrefs.stream().filter(x -> x.type == XRefType.JUMP).count();
      this.branchCount = (int) xrefs.stream().filter(x -> x.type == XRefType.BRANCH).count();
      this.pointerCount = (int) xrefs.stream().filter(x -> x.type == XRefType.POINTER).count();
      this.compareCount = (int) xrefs.stream().filter(x -> x.type == XRefType.COMPARE).count();
      this.modifyCount = (int) xrefs.stream().filter(x -> x.type == XRefType.MODIFY).count();
      this.bitTestCount = (int) xrefs.stream().filter(x -> x.type == XRefType.BIT_TEST).count();
      this.sourceAddresses = xrefs.stream()
              .map(x -> x.sourceAddress)
              .distinct()
              .collect(Collectors.toList());
    }
  }

  /**
   * Add load reference (LDA, LDX, LDY)
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addRead(int sourceAddress, int targetAddress, String instruction, String context) {
    addXRef(sourceAddress, targetAddress, XRefType.READ, instruction, context);
  }

  /**
   * Add a store reference (STA, STX, STY)
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addWrite(int sourceAddress, int targetAddress, String instruction, String context) {
    addXRef(sourceAddress, targetAddress, XRefType.WRITE, instruction, context);
  }

  /**
   * Add a subroutine call (JSR)
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addCall(int sourceAddress, int targetAddress, String instruction, String context) {
    addXRef(sourceAddress, targetAddress, XRefType.CALL, instruction, context);
  }

  /**
   * Add a jump (JMP)
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addJump(int sourceAddress, int targetAddress, String instruction, String context) {
    addXRef(sourceAddress, targetAddress, XRefType.JUMP, instruction, context);
  }

  /**
   * Add a conditional branch (BCC, BCS, BNE, etc.)
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addBranch(int sourceAddress, int targetAddress, String instruction, String context) {
    addXRef(sourceAddress, targetAddress, XRefType.BRANCH, instruction, context);
  }

  /**
   * Add a pointer reference (LDA ($FE),Y)
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addPointer(int sourceAddress, int targetAddress, String instruction, String context) {
    addXRef(sourceAddress, targetAddress, XRefType.POINTER, instruction, context);
  }

  /**
   * Add a compare (CMP, CPX, CPY)
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addCompare(int sourceAddress, int targetAddress, String instruction, String context) {
    addXRef(sourceAddress, targetAddress, XRefType.COMPARE, instruction, context);
  }

  /**
   * Add a modify (INC, DEC, ASL, LSR, etc.)
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addModify(int sourceAddress, int targetAddress, String instruction, String context) {
    addXRef(sourceAddress, targetAddress, XRefType.MODIFY, instruction, context);
  }

  /**
   * Add a bit test (BIT)
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addBitTest(int sourceAddress, int targetAddress, String instruction, String context) {
    addXRef(sourceAddress, targetAddress, XRefType.BIT_TEST, instruction, context);
  }

  /**
   * Generic adding of an instruction reference
   *
   * @param sourceAddress the source address of instruction
   * @param targetAddress the target address (by operand) of instruction
   * @param instruction the instruction in assembly format
   * @param context the context of this operation
   */
  public void addXRef(int sourceAddress, int targetAddress, XRefType type,
          String instruction, String context) {
    XRef xref = new XRef(sourceAddress, type, instruction, context);

    synchronized (xrefMap) {
      xrefMap.computeIfAbsent(targetAddress, k -> new ArrayList<>()).add(xref);
      cacheDirty = true;
    }
  }

  /**
   * Retrun all references for a target address
   *
   * @param targetAddress the target address
   * @return the references
   */
  public List<XRef> getXRefsForAddress(int targetAddress) {
    synchronized (xrefMap) {
      return xrefMap.getOrDefault(targetAddress, new ArrayList<>());
    }
  }

  /**
   * Return references filtered by type
   *
   * @param targetAddress the references
   * @param type the type to use
   * @return the references
   */
  public List<XRef> getXRefsForAddress(int targetAddress, XRefType type) {
    return getXRefsForAddress(targetAddress).stream()
            .filter(x -> x.type == type)
            .collect(Collectors.toList());
  }

  /**
   * Verify if an address has reference
   *
   * @param targetAddress the target address
   * @return true if reference is present
   */
  public boolean hasXRefs(int targetAddress) {
    synchronized (xrefMap) {
      return xrefMap.containsKey(targetAddress)
              && !xrefMap.get(targetAddress).isEmpty();
    }
  }

  /**
   * Return references of an address
   *
   * @param targetAddress the target address
   * @return the reference
   */
  public XRefStats getStatsForAddress(int targetAddress) {
    updateCacheIfNeeded();
    return statsCache.getOrDefault(targetAddress, new XRefStats(new ArrayList<>()));
  }

  /**
   * Return all address with references
   *
   * @return the addresses with references
   */
  public Set<Integer> getAllReferencedAddresses() {
    synchronized (xrefMap) {
      return new HashSet<>(xrefMap.keySet());
    }
  }

  /**
   * Return all address with references of the given type
   *
   * @param type the type of reference
   * @return the addresses with references
   */
  public List<XRef> getAllXRefsOfType(XRefType type) {
    synchronized (xrefMap) {
      return xrefMap.values().stream()
              .flatMap(List::stream)
              .filter(x -> x.type == type)
              .collect(Collectors.toList());
    }
  }

  /**
   * Generate an preview string for tooltip
   *
   * @param targetAddress the target address
   * @return the preview string
   */
  public String generatePreview(int targetAddress) {
    if (!hasXRefs(targetAddress)) {
      return "No cross-references";
    }

    XRefStats stats = getStatsForAddress(targetAddress);
    StringBuilder sb = new StringBuilder();

    sb.append(String.format("X-Ref: $%04X\n", targetAddress));
    if (stats.readCount > 0) {
      sb.append(String.format("• Read: %d times\n", stats.readCount));
    }
    if (stats.writeCount > 0) {
      sb.append(String.format("• Write: %d times\n", stats.writeCount));
    }
    if (stats.callCount > 0) {
      sb.append(String.format("• Called: %d times\n", stats.callCount));
    }
    if (stats.jumpCount > 0) {
      sb.append(String.format("• Jumped: %d times\n", stats.jumpCount));
    }
    if (stats.branchCount > 0) {
      sb.append(String.format("• Branched: %d times\n", stats.branchCount));
    }

    return sb.toString();
  }

  /**
   * Generate a complete table for GUI visualization
   *
   * @param targetAddress the target address
   * @return the complete visualization
   */
  public List<Object[]> generateTableData(int targetAddress) {
    List<Object[]> tableData = new ArrayList<>();
    List<XRef> xrefs = getXRefsForAddress(targetAddress);

    // Ordina per indirizzo sorgente
    xrefs.sort(Comparator.comparingInt(x -> x.sourceAddress));

    for (XRef xref : xrefs) {
      tableData.add(new Object[]{
        xref.type,
        String.format("$%04X", xref.sourceAddress),
        xref.instruction,
        xref.context != null ? xref.context : ""
      });
    }

    return tableData;
  }

  /**
   * Find all address with references in the given range
   *
   * @param startAddress the start address
   * @param endAddress the ending address
   * @return the addresses with references
   */
  public List<XRef> findXRefsToRange(int startAddress, int endAddress) {
    List<XRef> result = new ArrayList<>();

    synchronized (xrefMap) {
      for (int addr = startAddress; addr <= endAddress; addr++) {
        if (xrefMap.containsKey(addr)) {
          result.addAll(xrefMap.get(addr));
        }
      }
    }

    return result;
  }

  /**
   * Update the cache if needed
   */
  private void updateCacheIfNeeded() {
    if (cacheDirty) {
      synchronized (xrefMap) {
        statsCache.clear();
        for (Integer address : xrefMap.keySet()) {
          statsCache.put(address, new XRefStats(xrefMap.get(address)));
        }
        cacheDirty = false;
      }
    }
  }

  /**
   * clear all references
   */
  public void clear() {
    synchronized (xrefMap) {
      xrefMap.clear();
      statsCache.clear();
      cacheDirty = true;
    }
  }

  /**
   * clear the references for a specific address
   *
   * @param targetAddress the target address
   */
  public void clearForAddress(int targetAddress) {
    synchronized (xrefMap) {
      xrefMap.remove(targetAddress);
      statsCache.remove(targetAddress);
    }
  }

  /**
   * Return the total stored references
   */
  public int getTotalReferenceCount() {
    synchronized (xrefMap) {
      return xrefMap.values().stream().mapToInt(List::size).sum();
    }
  }

  /**
   * Process one instruction and add references automatically for the M6510
   * processor
   *
   * @param address the address of the instruction
   * @param mnemonic the menemonic of the instruction
   * @param operand the address of the operand
   * @param fullInstruction the instruction in assembly format
   */
  public void processInstructionM6510(int address, String mnemonic, int operand, String fullInstruction) {
    switch (mnemonic.toUpperCase()) {
      case "LDA":
      case "LDX":
      case "LDY":
        addRead(address, operand, fullInstruction, "Load");
        break;

      case "STA":
      case "STX":
      case "STY":
        addWrite(address, operand, fullInstruction, "Store");
        break;

      case "JSR":
        addCall(address, operand, fullInstruction, "Subroutine call");
        break;

      case "JMP":
        addJump(address, operand, fullInstruction, "Jump");
        break;

      case "BCC":
      case "BCS":
      case "BEQ":
      case "BMI":
      case "BNE":
      case "BPL":
      case "BVC":
      case "BVS":
        addBranch(address, operand, fullInstruction, "Branch");
        break;

      case "CMP":
      case "CPX":
      case "CPY":
        addCompare(address, operand, fullInstruction, "Compare");
        break;

      case "INC":
      case "DEC":
      case "ASL":
      case "LSR":
      case "ROL":
      case "ROR":
        addModify(address, operand, fullInstruction, "Modify");
        break;

      case "BIT":
        addBitTest(address, operand, fullInstruction, "Bit test");
        break;
    }
  }

  /**
   * Process one instruction and add references automatically for the Z80
   * processor
   *
   * @param address the address of the instruction
   * @param mnemonic the menemonic of the instruction
   * @param operand the address of the operand
   * @param fullInstruction the instruction in assembly format
   */
  public void processInstructionZ80(int address, String mnemonic, int operand, String fullInstruction) {
    String upperMnemonic = mnemonic.toUpperCase();

    switch (upperMnemonic) {
      case "LDI":
      case "LDIR":
      case "LDD":
      case "LDDR":
        if (isMemoryOperand(fullInstruction, true)) {
          addRead(address, operand, fullInstruction, "Z80 Load from memory");
        }
        break;

      case "LD":
        if (isMemoryOperand(fullInstruction, true)) {
          addRead(address, operand, fullInstruction, "Z80 Load from memory");
        }
        if (isMemoryOperand(fullInstruction, false)) {
          addWrite(address, operand, fullInstruction, "Z80 Store to memory");
        }
        break;

      case "PUSH":
        addWrite(address, operand, fullInstruction, "Z80 Push to stack");
        break;

      case "POP":
        addRead(address, operand, fullInstruction, "Z80 Pop from stack");
        break;

      case "CALL":
      case "RST":
        addCall(address, operand, fullInstruction, "Z80 Subroutine call");
        break;

      case "JP":
      case "JR":
      case "DJNZ":
        addJump(address, operand, fullInstruction, "Z80 Jump");
        break;

      case "RET":
      case "RETI":
      case "RETN":
        addJump(address, operand, fullInstruction, "Z80 Return");
        break;

      case "IN":
      case "INI":
      case "INIR":
      case "IND":
      case "INDR":
        addRead(address, operand, fullInstruction, "Z80 Input from port");
        break;

      case "OUT":
      case "OUTI":
      case "OTIR":
      case "OUTD":
      case "OTDR":
        addWrite(address, operand, fullInstruction, "Z80 Output to port");
        break;

      case "ADD":
      case "ADC":
      case "SUB":
      case "SBC":
      case "AND":
      case "OR":
      case "XOR":
      case "CP":
        if (isMemoryOperand(fullInstruction, true)) {
          addRead(address, operand, fullInstruction, "Z80 Arithmetic operation");
        }
        break;

      case "INC":
      case "DEC":
        if (isMemoryOperand(fullInstruction, false)) {
          addModify(address, operand, fullInstruction, "Z80 Modify memory");
        }
        break;

      case "RL":
      case "RLC":
      case "RR":
      case "RRC":
      case "SLA":
      case "SRA":
      case "SRL":
      case "RLD":
      case "RRD":
        if (isMemoryOperand(fullInstruction, false)) {
          addModify(address, operand, fullInstruction, "Z80 Rotate memory");
        }
        break;

      case "BIT":
        if (isMemoryOperand(fullInstruction, true)) {
          addBitTest(address, operand, fullInstruction, "Z80 Bit test");
        }
        break;

      case "SET":
      case "RES":
        if (isMemoryOperand(fullInstruction, false)) {
          addModify(address, operand, fullInstruction, "Z80 Bit modify");
        }
        break;

      case "CPI":
      case "CPIR":
      case "CPD":
      case "CPDR":
        addRead(address, operand, fullInstruction, "Z80 Block compare");
        break;

      default:
        if (isMemoryOperand(fullInstruction, true)) {
          addRead(address, operand, fullInstruction, "Z80 Memory access");
        }
        break;
    }
  }

  /**
   * Verify if a Z80 instruction access the memory
   *
   * @param fullInstruction complete instruction
   * @param isSource true to verify source operand, false for destination
   * destinazione
   * @return true if the operan access to the memory
   */
  private boolean isMemoryOperand(String fullInstruction, boolean isSource) {
    // Pattern to identy memory access on Z80
    // (HL), (IX+d), (IY+d), (BC), (DE), (nn)

    String[] parts = fullInstruction.split("[,\\s]+");

    if (isSource) {
      // Verify last operand (source)
      if (parts.length >= 2) {
        String source = parts[parts.length - 1].trim();
        return source.matches("\\(.*\\)");
      }
    } else {
      // Verify the fist operand after the mnemonic (destination)
      if (parts.length >= 2) {
        String dest = parts[1].trim();
        return dest.matches("\\(.*\\)");
      }
    }

    return false;
  }

  /**
   * Method for instructions of Z80 with 2 operands
   *
   * @param address the instruction address
   * @param mnemonic the mnemonic of instruction
   * @param operand1 first operand
   * @param operand2 second operand
   * @param fullInstruction full assembly instruction
   */
  public void processInstructionZ80(int address, String mnemonic, int operand1, int operand2, String fullInstruction) {
    String upperMnemonic = mnemonic.toUpperCase();

    switch (upperMnemonic) {
      case "LD":
        // LD (dest), (src) - handle both operands
        if (isMemoryReference(operand1)) {
          addWrite(address, operand1, fullInstruction, "Z80 Store to memory");
        }
        if (isMemoryReference(operand2)) {
          addRead(address, operand2, fullInstruction, "Z80 Load from memory");
        }
        break;

      case "EX":
        // EX (SP), HL o similar - swap in memory
        if (isMemoryReference(operand1) || isMemoryReference(operand2)) {
          addRead(address, operand1, fullInstruction, "Z80 Exchange read");
          addWrite(address, operand1, fullInstruction, "Z80 Exchange write");
        }
        break;
    }
  }

  /**
   * Verify if an operand is a memory reference
   */
  private boolean isMemoryReference(int operand) {
    // This is an approssimation
    return operand >= 0 && operand <= 0xFFFF;
  }

  /**
   * Full method for processing Z80 operand patterns
   *
   * @param address the address of instruction
   * @param mnemonic the instruction mnemonic
   * @param fullInstruction full assembly instruction
   */
  public void processInstructionZ80Detailed(int address, String mnemonic, String fullInstruction) {
    String upperMnemonic = mnemonic.toUpperCase();
    String[] tokens = fullInstruction.split("\\s+");

    if (tokens.length < 2) {
      return;
    }

    String operands = fullInstruction.substring(mnemonic.length()).trim();
    String[] operandList = operands.split(",");

    for (int i = 0; i < operandList.length; i++) {
      String op = operandList[i].trim();

      // Identify Z80 operand that refers to memory
      if (op.matches("\\([HL]L?\\)")
              || // (HL)
              op.matches("\\(IX[+-]?\\d*\\)")
              || // (IX), (IX+d)
              op.matches("\\(IY[+-]?\\d*\\)")
              || // (IY), (IY+d)
              op.matches("\\([BCDE]C?\\)")
              || // (BC), (DE)
              op.matches("\\(\\$?[0-9A-Fa-f]+\\)")) {  // (nn) - absolute address

        /// This not work well with our labels for addresses
        int memoryAddress = extractAddressFromOperand(op);

        if (memoryAddress != -1) {
          if (isReadOperation(upperMnemonic, i)) {
            addRead(address, memoryAddress, fullInstruction, "Z80 Memory read");
          } else if (isWriteOperation(upperMnemonic, i)) {
            addWrite(address, memoryAddress, fullInstruction, "Z80 Memory write");
          } else if (isModifyOperation(upperMnemonic)) {
            addModify(address, memoryAddress, fullInstruction, "Z80 Memory modify");
          }
        }
      }
    }

    if (upperMnemonic.equals("CALL") || upperMnemonic.equals("JP")
            || upperMnemonic.equals("JR") || upperMnemonic.equals("RST")) {

      if (operandList.length > 0) {
        int targetAddress = extractAddressFromOperand(operandList[0].trim());
        if (targetAddress != -1) {
          if (upperMnemonic.equals("CALL") || upperMnemonic.equals("RST")) {
            addCall(address, targetAddress, fullInstruction, "Z80 Subroutine call");
          } else {
            addJump(address, targetAddress, fullInstruction, "Z80 Jump");
          }
        }
      }
    }
  }

  /**
   * Estract a numeric address from Z80 operands
   */
  private int extractAddressFromOperand(String operand) {
    try {
      // Rimuovi parentesi e altri caratteri non numerici
      String clean = operand.replaceAll("[\\(\\)\\+\\-IXYHL]", "").trim();

      // Se è vuoto, potrebbe essere (HL), (IX), etc. - non possiamo estrarre un indirizzo fisso
      if (clean.isEmpty()) {
        return -1;
      }

      // Verify for hex number
      if (clean.matches("\\$[0-9A-Fa-f]+")) {
        return Integer.parseInt(clean.substring(1), 16);
      } else if (clean.matches("[0-9A-Fa-f]+[Hh]")) {
        return Integer.parseInt(clean.substring(0, clean.length() - 1), 16);
      } else if (clean.matches("[0-9]+")) {
        return Integer.parseInt(clean);
      }
    } catch (NumberFormatException e) {
      // no number
    }
    return -1;
  }

  /**
   * Determines whether an operation is a read operation based on the mnemonic
   * and the operand position.
   */
  private boolean isReadOperation(String mnemonic, int operandIndex) {
    switch (mnemonic) {
      case "LD":
        return operandIndex == 1; // LD dest, src - src is read
      case "ADD":
      case "ADC":
      case "SUB":
      case "SBC":
      case "AND":
      case "OR":
      case "XOR":
      case "CP":
      case "BIT":
        return true; // All those read the operand
      case "IN":
      case "INI":
      case "INIR":
      case "IND":
      case "INDR":
        return true;
      case "POP":
        return true;
      default:
        return false;
    }
  }

  /**
   * Determines whether an operation is a write operation based on the mnemonic
   * and the operand position.
   */
  private boolean isWriteOperation(String mnemonic, int operandIndex) {
    switch (mnemonic) {
      case "LD":
        return operandIndex == 0; // LD dest, src - dest is write
      case "OUT":
      case "OUTI":
      case "OTIR":
      case "OUTD":
      case "OTDR":
        return true;
      case "PUSH":
        return true;
      case "SET":
      case "RES":
        return true;
      default:
        return false;
    }
  }

  /**
   * Determina se un'operazione modifica la memoria (lettura + scrittura)
   */
  private boolean isModifyOperation(String mnemonic) {
    switch (mnemonic) {
      case "INC":
      case "DEC":
      case "RL":
      case "RLC":
      case "RR":
      case "RRC":
      case "SLA":
      case "SRA":
      case "SRL":
      case "RLD":
      case "RRD":
      case "EX":
        return true;
      default:
        return false;
    }
  }

  /**
   * Full method for processing I8048 operand patterns
   * 
   * @param address the address of the instruction
   * @param mnemonic the menemonic of the instruction
   * @param operand the address of the operand
   * @param fullInstruction the instruction in assembly format
   */
  public void processInstructionI8048(int address, String mnemonic, int operand, String fullInstruction) {
    String upperMnemonic = mnemonic.toUpperCase();

    switch (upperMnemonic) {
      case "MOV":
        // MOV A, @R0 - MOV A, @R1 (indirect via register)
        if (fullInstruction.matches("MOV A, @R[01]")) {
          addRead(address, operand, fullInstruction, "I8048 Load indirect");
        } // MOV A, direct (ldirect read)
        else if (fullInstruction.matches("MOV A, \\$?[0-9A-Fa-f]+")) {
          addRead(address, operand, fullInstruction, "I8048 Load direct");
        } // MOV @R0, A - MOV @R1, A (indirect write)
        else if (fullInstruction.matches("MOV @R[01], A")) {
          addWrite(address, operand, fullInstruction, "I8048 Store indirect");
        } // MOV direct, A (direct write)
        else if (fullInstruction.matches("MOV \\$?[0-9A-Fa-f]+, A")) {
          addWrite(address, operand, fullInstruction, "I8048 Store direct");
        }
        break;

      case "IN":
        addRead(address, operand, fullInstruction, "I8048 Input from port");
        break;

      case "OUTL":
        addWrite(address, operand, fullInstruction, "I8048 Output to port");
        break;

      case "INS":
        addRead(address, operand, fullInstruction, "I8048 Input from bus");
        break;

      case "OUTL BUS":
        addWrite(address, operand, fullInstruction, "I8048 Output to bus");
        break;

      case "CALL":
        addCall(address, operand, fullInstruction, "I8048 Subroutine call");
        break;

      case "JMP":
        addJump(address, operand, fullInstruction, "I8048 Jump");
        break;

      case "JC":   // Jump if carry
      case "JNC":  // Jump if no carry
      case "JZ":   // Jump if zero
      case "JNZ":  // Jump if not zero
      case "JT0":  // Jump if test 0
      case "JNT0": // Jump if not test 0
      case "JT1":  // Jump if test 1
      case "JNT1": // Jump if not test 1
      case "JF0":  // Jump if flag 0
      case "JF1":  // Jump if flag 1
      case "JTF":  // Jump if timer flag
      case "JB":   // Jump if bit (8048/8049)
        addBranch(address, operand, fullInstruction, "I8048 Conditional jump");
        break;

      case "INC":
      case "DEC":
        if (isMemoryOperandI8048(fullInstruction)) {
          addModify(address, operand, fullInstruction, "I8048 Modify memory");
        }
        break;

      case "CPL":  // Complement bit
      case "CLR":  // Clear bit
      case "SETB": // Set bit
        if (isMemoryOperandI8048(fullInstruction)) {
          addModify(address, operand, fullInstruction, "I8048 Bit operation");
        }
        break;

      case "XCH":  // Exchange
        if (isMemoryOperandI8048(fullInstruction)) {
          addModify(address, operand, fullInstruction, "I8048 Exchange");
        }
        break;

      case "ANLD": // AND data to port
      case "ORLD": // OR data to port
        addWrite(address, operand, fullInstruction, "I8048 Expander output");
        break;

      case "MOVX":
        if (fullInstruction.matches("MOVX A, @R[01]")) {
          addRead(address, operand, fullInstruction, "I8048 External read");
        } else if (fullInstruction.matches("MOVX @R[01], A")) {
          addWrite(address, operand, fullInstruction, "I8048 External write");
        }
        break;

      default:
        if (isMemoryOperandI8048(fullInstruction)) {
          addRead(address, operand, fullInstruction, "I8048 Memory access");
        }
        break;
    }
  }

  /**
   * Check if an I8048 instruction accesses memory
   */
  private boolean isMemoryOperandI8048(String fullInstruction) {
    //Pattern: @R0, @R1, direct (number), ports
    return fullInstruction.matches(".*(@R[01]|\\$?[0-9A-Fa-f]+|P[0-2]|BUS).*");
  }

  /**
   * Specialized method for I8048 with detailed operand analysis
   * 
   * @param address the address of the instruction
   * @param mnemonic the menemonic of the instruction
   * @param fullInstruction the instruction in assembly format
   */
  public void processInstructionI8048Detailed(int address, String mnemonic, String fullInstruction) {
    String upperMnemonic = mnemonic.toUpperCase();
    String[] tokens = fullInstruction.split("\\s+");

    if (tokens.length < 2) {
      return;
    }

    String operands = fullInstruction.substring(mnemonic.length()).trim();
    String[] operandList = operands.split(",");

    for (int i = 0; i < operandList.length; i++) {
      String op = operandList[i].trim();

      if (op.matches("@R[01]")
              || // @R0, @R1
              op.matches("\\$?[0-9A-Fa-f]+")
              || // direct addresses
              op.matches("P[0-2]")
              || // P0, P1, P2
              op.matches("BUS")) {  // BUS

        int memoryAddress = extractAddressFromI8048Operand(op);

        if (memoryAddress != -1) {
          if (isI8048ReadOperation(upperMnemonic, i)) {
            addRead(address, memoryAddress, fullInstruction, "I8048 Memory read");
          } else if (isI8048WriteOperation(upperMnemonic, i)) {
            addWrite(address, memoryAddress, fullInstruction, "I8048 Memory write");
          } else if (isI8048ModifyOperation(upperMnemonic)) {
            addModify(address, memoryAddress, fullInstruction, "I8048 Memory modify");
          }
        }
      }
    }

    if (upperMnemonic.equals("CALL") || upperMnemonic.equals("JMP")
            || upperMnemonic.startsWith("J")) { 

      if (operandList.length > 0) {
        int targetAddress = extractAddressFromI8048Operand(operandList[0].trim());
        if (targetAddress != -1) {
          if (upperMnemonic.equals("CALL")) {
            addCall(address, targetAddress, fullInstruction, "I8048 Subroutine call");
          } else {
            // Distingui tra jump e branch
            if (upperMnemonic.equals("JMP")) {
              addJump(address, targetAddress, fullInstruction, "I8048 Jump");
            } else {
              addBranch(address, targetAddress, fullInstruction, "I8048 Conditional jump");
            }
          }
        }
      }
    }
  }

  /**
   * Extracts a numeric address from an I8048 operand
   */
  private int extractAddressFromI8048Operand(String operand) {
    try {
      // Handling @R0 and @R1 - indirect addressing
      if (operand.equals("@R0")) {
        return 0; // R0 typically points to RAM area
      }
      if (operand.equals("@R1")) {
        return 1; // R1 typically points to RAM area
      }

      if (operand.equals("P0")) {
        return 0x00;
      }
      if (operand.equals("P1")) {
        return 0x01;
      }
      if (operand.equals("P2")) {
        return 0x02;
      }
      if (operand.equals("BUS")) {
        return 0x03;
      }

      String clean = operand.replaceAll("[^0-9A-Fa-f]", "").trim();

      if (clean.isEmpty()) {
        return -1;
      }

      // Check if it is hexadecimal
      if (operand.matches(".*[Hh].*")) {
        return Integer.parseInt(clean, 16);
      } else if (operand.contains("$")) {
        return Integer.parseInt(clean, 16);
      } else if (clean.matches("[0-9A-Fa-f]+")) {
        // Assumes hexadecimal if it contains letters, decimal otherwise
        if (clean.matches(".*[A-Fa-f].*")) {
          return Integer.parseInt(clean, 16);
        } else {
          return Integer.parseInt(clean);
        }
      }
    } catch (NumberFormatException e) {
     
    }
    return -1;
  }

  /**
   * Determines whether an I8048 operation is a read operation
   */
  private boolean isI8048ReadOperation(String mnemonic, int operandIndex) {
    switch (mnemonic) {
      case "MOV":
        return operandIndex == 1; // MOV dest, src - src is read
      case "IN":
      case "INS":
      case "MOVX":
        return true;
      case "ANL":
      case "ORL":
      case "XRL": // Logic operations
        return operandIndex == 1;
      default:
        return false;
    }
  }

  /**
   * Determines whether an I8048 operation is a write operation
   */
  private boolean isI8048WriteOperation(String mnemonic, int operandIndex) {
    switch (mnemonic) {
      case "MOV":
        return operandIndex == 0; // MOV dest, src - dest is write
      case "OUTL":
      case "MOVX":
        return true;
      case "ANLD":
      case "ORLD": // Expander operations
        return true;
      case "INC":
      case "DEC":
      case "CPL":
      case "CLR":
      case "SETB":
        return true;
      default:
        return false;
    }
  }

  /**
   * Determines whether an I8048 operation modifies memory (read + write)
   */
  private boolean isI8048ModifyOperation(String mnemonic) {
    switch (mnemonic) {
      case "XCH":  // Exchange
      case "XCHD": // Exchange digit
      case "SWAP": // Swap nibbles
        return true;
      default:
        return false;
    }
  }
}
