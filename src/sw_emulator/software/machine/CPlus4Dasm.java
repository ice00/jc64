/**
 * @(#)C128Dasm.java 2020/10/11
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
package sw_emulator.software.machine;

import sw_emulator.software.cpu.M6510Dasm;
import static sw_emulator.software.cpu.M6510Dasm.A_ABS;
import static sw_emulator.software.cpu.M6510Dasm.A_ABX;
import static sw_emulator.software.cpu.M6510Dasm.A_ABY;
import static sw_emulator.software.cpu.M6510Dasm.A_IDX;
import static sw_emulator.software.cpu.M6510Dasm.A_IDY;
import static sw_emulator.software.cpu.M6510Dasm.A_IND;
import static sw_emulator.software.cpu.M6510Dasm.A_REL;
import static sw_emulator.software.cpu.M6510Dasm.A_ZPG;
import static sw_emulator.software.cpu.M6510Dasm.A_ZPX;
import static sw_emulator.software.cpu.M6510Dasm.A_ZPY;
import static sw_emulator.software.machine.CVic20Dasm.LANG_ITALIAN;

/**
 * Comment the memory location of Plus4 for the disassembler
 * It performs also a multy language comments.
 * 
 * @author ice
 */
public class CPlus4Dasm extends M6510Dasm {
  // Available language
  public static final byte LANG_ENGLISH=1;
  public static final byte LANG_ITALIAN=2;

  /** Actual selected language (default=english) */
  public byte language=LANG_ENGLISH;   
  
  /**
   * Return a comment string for the passed instruction
   *
   * @param iType the type of instruction
   * @param aType the type of addressing used by the instruction
   * @param addr the address value (if needed by this istruction type)
   * @param value the operation value (if needed by this instruction)
   * @return a comment string
   */
  @Override
  public String dcom(int iType, int aType, long addr, long value) {
    switch (aType) {
      case A_ZPG:
      case A_ZPX:
      case A_ZPY:
      case A_ABS:
      case A_ABX:
      case A_ABY:
      case A_REL:
      case A_IND:
      case A_IDX:
      case A_IDY:    
        // do not get comment if appropriate option is not selected  

        switch (language) {
          case LANG_ITALIAN:
            switch ((int)addr) {
          
            default:
            }      
          default:
            switch ((int)addr) {                  
              case 0x00: return "7501 on-chip data-direction register";
              case 0x01: return "7501 on-chip 8-bit Input/Output register";
              case 0x02: return "Token 'search' looks for (run-time stack)";
              case 0x03: 
              case 0x04: 
              case 0x05: 
              case 0x06: return "Temp (renumber)";
              case 0x07: return "Search character";
              case 0x08: return "Flag: scan for quote at end of string";
              case 0x09: return "Screen column from last TAB";
              case 0x0A: return "Flag: 0 = load 1 - verify";
              case 0x0B: return "Input buffer pointer/No. of subsctipts";
              case 0x0C: return "Flag: Default Array DIMension";
              case 0x0D: return "Data type: $FF = string, $00 = numeric";
              case 0x0E: return "Data type: $80 = integer, $00 = floating";
              case 0x0F: return "Flag: DATA scan/LIST quote/garbage coll";
              case 0x10: return "Flag: subscript ref / user function coll";
              case 0x11: return "Flag: $00 = INPUT, $43 = GET, $98 = READ";
              case 0x12: return "Flag TAN siqn / comparison result";
              case 0x13: return "Flag: INPUT prompt";
              case 0x14:
              case 0x15: return "Temp: integer value";
              case 0x16: return "Pointer: temporary string stack";
              case 0x17: 
              case 0x18: return "Last temp string address";
              case 0x19:         
              case 0x1A:
              case 0x1B:                  
              case 0x1C:    
              case 0x1D:    
              case 0x1E:    
              case 0x1F:    
              case 0x20: 
              case 0x21: return "Stack for temporary strings";                    
              case 0x22: 
              case 0x23: 
              case 0x24:
              case 0x25: return "Utility pointer area";  
              case 0x2B: 
              case 0x2C: return "Pointer: start of BASIC text";
              case 0x2D: 
              case 0x2E: return "Pointer: start of BASIC variables";
              case 0x2F: 
              case 0x30: return "Pointer: start of BASIC arrays";
              case 0x31: 
              case 0x32: return "Pointer: end of BASIC arrays (+1)";
              case 0x33: 
              case 0x34: return "Pointer: bottom of string storage";
              case 0x35: 
              case 0x36: return "Utility string pointer";
              case 0x37: 
              case 0x38: return "Pointer: highest address used by BASIC";
              case 0x39: 
              case 0x3A: return "Current BASIC line number";              
              case 0x3F: 
              case 0x40: return "Current DATA line number";
              case 0x41: 
              case 0x42: return "Pointer: Current DATA item address"; 
              case 0x43: 
              case 0x44: return "Vector: INPUT routine";
              case 0x45: 
              case 0x46: return "Current BASIC variable name";
              case 0x47: 
              case 0x48: return "Pointer: Current BASIC variable data";
              case 0x49: 
              case 0x4A: return "Pointer: Index variable for FOR/NEXT";
              case 0x61: return "Floating-point accumulator #1: exponent";   
              case 0x62:
              case 0x63: 
              case 0x64: 
              case 0x65: return "Floating accum. #1: mantissa"; 
              case 0x66: return "Floating accum. #1: sign";
              case 0x67: return "Pointer: series evaluation constant";
              case 0x68: return "Floating accum. #1: overflow digit";              
              case 0x69: return "Floating-point accumulator #2: exponent";
              case 0x6A: 
              case 0x6B:
              case 0x6C: 
              case 0x6D: return "Floating accum. #2: mantissa";
              case 0x6E: return "Floating accum. #2: sign";
              case 0x6F: return "Sign comparison result: accum. #1 vs #2"; 
              case 0x70: return "Floating accum. #1. low-order (rounding)";
              case 0x71: 
              case 0x72: return "Pointer: cassette buffer";
              case 0x73:
              case 0x74: return "Increment value for auto (0 = off)";
              case 0x75: return "Flag if 10K hires allocated";
              case 0x78: return "Used as temp Eor indirect loads";
              case 0x79: 
              case 0x7A: 
              case 0x7B: return "Descriptor for DSS"; 
              case 0x7C:
              case 0x7D: return "Top of run time stack";
              case 0x7E:
              case 0x7F: return "Temps used by music (tone & volume)";
              case 0x83: return "Current graphic mode";
              case 0x84: return "Current color selected";
              case 0x85: return "Multicolor 1";
              case 0x86: return "Foreground color";
              case 0x87: return "Maximum # of columns";
              case 0x88: return "Maximum # of rows";
              case 0x89: return "Paint-left flag";
              case 0x8A: return "Paint-Right flag";              
              case 0x8B: return "Stop paint if not BG (Not same Color)";
              case 0x90: return "Kernal I/O status word: ST";
              case 0x91: return "Flag: STOP key/RVS key"; 
              case 0x92: return "Temp";  
              case 0x93: return "Flag:  0=load, 1=verify";
              case 0x94: return "Plag: serial bus - output char buffered";
              case 0x95: return "Buffered character for serial bus";
              case 0x96: return "Temp for basin";
              case 0x97: return "# of open files/index to file table";
              case 0x98: return "Default input device (0)";
              case 0x99: return "Default output (CMD) device (3)";
              case 0x9A: return "Flag: $80=direct mode  $00=program";
              case 0x9B: return "Tape pass 1 error log";
              case 0x9C: return "Tape pass 2 error log";
              case 0x9F:          
              case 0xA0: 
              case 0xA1:
              case 0xA2: return "Temp data area";
              case 0xA3: 
              case 0xA4: 
              case 0xA5: return "Real-time jiffy clock (approx) 1/60 sec";
              case 0xA6: return "Serial bus usage (EOI on output)";
              case 0xA7: return "Byte to be written/read on/off tape";
              case 0xA8: return "Temp used by serial routine";
              case 0xAB: return "Length of current file name";
              case 0xAC: return "Current logical fiie number";
              case 0xAD: return "Current seconda.y address";
              case 0xAE: return "Current device number";
              case 0xAF: 
              case 0xB0: return "Pointer: current file nam";
              case 0xB2:
              case 0xB3: return "I/O start address";
              case 0xB4: 
              case 0xB5: return "Load ram base";
              case 0xB6: 
              case 0xB7: return "Base pointer to cassette base";
              case 0xBA: 
              case 0xBB: return "Pointer to data for tape writes";  
              case 0xBC: 
              case 0xBD: return "Pointer to immediate string for primms"; 
              case 0xBE: 
              case 0xBF: return "Pointer to byte to be fetched in bank fetc";
              case 0xC0: 
              case 0xC1: return "Temp for scrolling";
              case 0xC2: return "RVS field flag on";
              case 0xC4: return "X position at start";
              case 0xC6: return "Flag: shift mode for print";
              case 0xC7: return "case 0xC7: return \"Screen reverse flag\";";
              case 0xC8:
              case 0xC9: return "Pointer: current screen line address";
              case 0xCA: return "Cursor column on current line";
              case 0xCB: return "Flag: editor in quote mode, $00=no";
              case 0xCC: return "Editor temp use";
              case 0xCD: return "Current cursor physical line number";
              case 0xCE: return "Temp data area";
              case 0xCF: return "Flag: insert mode, >0 = # INSTs"; 
              case 0xE9: return "Screen line link table/editor temps"; 
              case 0xEA:
              case 0xEB: return "Screen editor color IP";
              case 0xEC:
              case 0xED: return "Key scan table indirect";
              case 0xEF: return "Index to keyboard queue";
              case 0xF0: return "Pause flag";
              case 0xF1:
              case 0xF2: return "Monitor ZP storage";
              case 0xF5: return "Temp for checksum calculation";
              case 0xF7: return "Which pass we are doing str";
              case 0xF8: return "Type of block";
              case 0xF9: return "(B.7 = 1)=> for wr, (B.6 = 1)=> for rd";
              case 0xFA: return "Save xreg for quick stopkey test"; 
              case 0xFB: return "Current bank configuration";
              case 0xFC: return "Char to send for a x-on (RS232)";
              case 0xFD: return "Char to send for a x-off (RS232)";
              case 0xFE: return "Editor temporary use";
              
              case 0x110: return "Temp Locations for save/restore A";
              case 0x111: return "Temp Locations for save/restore Y";
              case 0x112: return "Temp Locations for save/restore X";
              case 0x25D: return "DOS loop counter";
              case 0x26F: return "DOS disk drive 1";
              case 0x270:
              case 0x271: return "DOS filename 1 addr";
              case 0x272: return "DOS filename 2 length";
              case 0x273: return "DOS disk drive 2";
              case 0x274:
              case 0x275: return "DOS filename 2 addr";
              case 0x276: return "DOS logical address";
              case 0x277: return "DOS phys addr";
              case 0x278: return "DOS secordary address";
              case 0x279: 
              case 0x27A: return "DOS disk identifier";
              case 0x27B: return "DOS DID flag";
              case 0x27C: return "DOS output string buffer";
              case 0x2AD:
              case 0x2AE: return "Current x position";
              case 0x2AF:
              case 0x2B0: return "Current y position";
              case 0x2B1:
              case 0x2B2: return "X coordinate destination";
              case 0x2B3:
              case 0x2B4: return "Y coordinate destination";
              case 0x2C5: return "Sign of angle";
              case 0x2C6:
              case 0x2C7: return "Sine of value of angle";
              case 0x2C8:
              case 0x2C9: return "Cosine of value of angle";
              case 0x2CA:
              case 0x2CB: return "Temps for angle distance routines";
              case 0x2CC: return "Placeholder";
              case 0x2CD: return "Pointer to begin no.";
              case 0x2CE: return "Pointer to end no.";
              case 0x2CF: return "Dollar flag";
              case 0x2D0: return "Comma flag";
              case 0x2D1: return "Counter";
              case 0x2D2: return "Sign exponent";
              case 0x2D3: return "Pointer to exponent";
              case 0x2D4: return "# of digits before decimal point";
              case 0x2D5: return "Justify flag";
              case 0x2D6: return "# of pos before decimal point (field)";
              case 0x2D7: return "# of pos after decimal point (field)";
              case 0x2D8: return "+/- flag (field)";
              case 0x2D9: return "Exponent flag (field)";
              case 0x2DA: return "Switch/Characters column counter";
              case 0x2DB: return "Char counter (field)/Characters row counter";
              case 0x2DC: return "Sign no.";
              case 0x2DD: return "Blank/star flag";
              case 0x2DE: return "Pointer to beginning of field";
              case 0x2DF: return "Length of format";
              case 0x2E0: return "Length of format";
              case 0x2F1:
              case 0x2F3: return "Ptr to routine: convert float to integer";
              case 0x2F4:
              case 0x2F5: return "Ptr to routine: convert integer to float";
              case 0x2FE:
              case 0x2FF: return "Vector for function cartridge users";
              case 0x300:
              case 0x301: return "Indirect Error (Output Error in .X)";
              case 0x302:
              case 0x303: return "Indirect Main (System Direct Loop)";
              case 0x304:
              case 0x305: return "Indirect Crunch (Tokenization Routine)";
              case 0x306:
              case 0x307: return "Indirect List (Char List)";
              case 0x308:
              case 0x309: return "Indirect Gone (Character Dispatch)";
              case 0x30A:
              case 0x30B: return "Indirect Eval (Symbol Evaluation)";
              case 0x30C: 
              case 0x30D: return "Escape token crunch";
              case 0x314:
              case 0x315: return "IRQ Ram Vector";
              case 0x316:
              case 0x317: return "BRK Instr RAM Vector";
              case 0x318:
              case 0x319: return "Indirects for Code";
              case 0x330:
              case 0x331: return "Savesp";
              case 0x3F3:
              case 0x3F4: return "Length of data to be written to tape";
              case 0x3F5:
              case 0x3F6: return "Length of data to be read from tape";
              case 0x4A2:
              case 0x4A3:    
              case 0x4A4: return "Numeric constant for Basic";
              case 0x4E7: return "Print using fill symbol [space]";
              case 0x4E8: return "Print using comma symbol [;]";
              case 0x4E9: return "Print using D.P. symbol  [.]";
              case 0x4EA: return "Print using monetary symbol [$]";
              case 0x4EB:
              case 0x4EC:    
              case 0x4ED:    
              case 0x4EE: return "Temp for instr";
              case 0x4EF: return "Last error number";
              case 0x4F0:    
              case 0x4F1: return "Line # of last error";
              case 0x4F2:    
              case 0x4F3: return "Line to go on error";
              case 0x4F4: return "Hold trap no. temporarily";
              case 0x4FC:    
              case 0x4FD: return "Table of pending jiffies (2's comp)";   
              case 0x508: return "'cold' or 'warm' start status";
              case 0x531: 
              case 0x532: return "Start of memory [1000]";
              case 0x533:
              case 0x534: return "return \"Start of memory [1000]\";";
              case 0x535: return "IEEE timeout flag";
              case 0x536: return "File end reached = 1, 0 otherwise";
              case 0x537: return "# of chars left in buffer (for R & W)";
              case 0x538: return "# of total valid chars in buffer (R)";
              case 0x539: return "Ptr to next char in buffer (for R & W)";
              case 0x53A: return "Contains type of current cass file";
              case 0x53B: return "Active attribute byte";
              case 0x53C: return "Character flash flag";
              case 0x53D: return "Free";
              case 0x53E: return "OC Base location of screen (top) [0C]";
              case 0x540: return "Key repeat flag";
              case 0x543: return "Shift flag byte";
              case 0x544: return "Last shift pattern";
              case 0x545:
              case 0x546: return "Indirect for keyboard table setup";
              case 0x547: return "shift, C="; 
              case 0x548: return "Auto scroll down flag (0=on,0<>off)";
              case 0x54B: return "Monitor non-zpage storage";
              case 0x55B: return "Used by various monitor routines";
              case 0x55D: return "Used for programmable keys";
              case 0x5E7: return "Temp for data write to kennedy";
              case 0x5E8: return "Select for kennedy read or write";
              case 0x5E9: return "Kennedy's dev #";
              case 0x5EA: return "Rennedy present = $ff, else = $00";
              case 0x5EB: return "Temp for type of open for kennedy";
              case 0x5EC: 
              case 0x5ED:
              case 0x5EE: 
              case 0x5EF: return "Physical Address Table";
              case 0x5F0: 
              case 0x5F1: return "Long jump address";
              case 0x5F2: return "Long jump accumulator";
              case 0x5F3: return "Long jump x register";
              case 0x5F4: return "Long jump status register";
              case 0x7B0: return "Byte to be written on tape";
              case 0x7B1: return "Temp for parity calc";
              case 0x7B2: 
              case 0x7B3: return "Temp for write-header";
              case 0x5B5: return "Local index for READBYTE routine: ";
              case 0x5B6: return "Pointer into the error stack";
              case 0x5B7: return "Number of first pass errors";
              case 0x7BE: return "Stack marker for stopkey recover";
              case 0x7BF: return "Stack marker for dropkey recover";
              case 0x7C0:
              case 0x7C1:
              case 0x7C2:
              case 0x7C3: return "Params passed to RDBLOK";
              case 0x7C4: return "Temp stat save for RDBLOK";
              case 0x7C5: return "# consec shorts to find in leader";
              case 0x7C6: return "# Errors fatal in RD countdown";
              case 0x7C7: return "Temp for Verify command";
              case 0x7C8:
              case 0x7C9:    
              case 0x7CA:    
              case 0x7CB: return "Pipe temp for T1";
              case 0x7CC: return "Read error propagate";
              case 0x7CD: return "User chracter to send";
              case 0x7CE: return "0 = empty ; 1 = full";
              case 0x7CF: return "System character to send";
              case 0x7D0: return "0 = empty ; 1 = full";
              case 0x7D1: return "Pntr to front of input queue";
              case 0x7D2: return "Pntr to rear of input queue";
              case 0x7D3: return "# of chars in input queue";
              case 0x7D4: return "Temp status for ACIA";
              case 0x7D5: return "Temp for input routine";
              case 0x7D6: return "FLG for local pause";
              case 0x7D7: return "FLG for remote pause";
              case 0x7D8: return "FLG to indicate presence of ACIA";
              case 0x7E5: return "Screen bottom (0...24)";
              case 0x7E6: return "Screen top";
              case 0x7E7: return "Screen left (0...39)";
              case 0x7E8: return "Screen right";
              case 0x7E9: return "Negative = scroll out";
              case 0x7EA: return "Insert mode: FF = on, 00 = off";
              case 0x7F2: return "Registers for SYS command";
              case 0x7F6: return "Key scan index";
              case 0x7F7: return "Flag to disable CTRL-S pause";
              case 0x7F8: return "MSB for monitor fetches from ROM=0;RAM=1";
              case 0x7F9: return "MSB for color/lim table in RAM=0;ROM=1";
              case 0x7FA: return "ROM mask for split screen";
              case 0x7FB: return "VM base mask for split screen";
              case 0x7FC: return "Motor lock semaphore for cassette";
              case 0x7FD: return "PAL tod";    
              case 0xFCF1: 
              case 0xFCF2:
              case 0xFCF3: return "JMP to cartridge IRQ routine";
              case 0xFCF4: 
              case 0xFCF5:
              case 0xFCF6: return "JMP to PHOENIX routine";
              case 0xFCF7: 
              case 0xFCF8:
              case 0xFCF9: return "JMP to LONG FETCH routine";
              case 0xFCFA:   
              case 0xFCFB:
              case 0xFCFC: return "JMP to LONG JUMP routine";    
              case 0xFCFD:   
              case 0xFCFE:
              case 0xFCFF: return "JMP to LONG IRQ routine";                               
              case 0xFD00: return "6551 ACIA: DATA port";
              case 0xFD01: return "6551 ACIA: STATUS port";              
              case 0xFD02: return "6551 ACIA: COMMAND port";
              case 0xFD03: return "6551 ACIA: CONTROL port";
              case 0xFD04:
              case 0xFD08:
              case 0xFD0C: return "6551 ACIA: copy";
              case 0xFD10: return "6529B: User Port PIO (P0-P7)";
              case 0xFD30: return "6529B: Keyboard PIO  Keyboard Matrix Connector";
              case 0xFF00: return "TED: Timer 1 low";
              case 0xFF01: return "TED: Timer 1 high";
              case 0xFF02: return "TED: Timer 2 low";
              case 0xFF03: return "TED: Timer 2 high";
              case 0xFF04: return "TED: Timer 3 low";
              case 0xFF05: return "TED: Timer 3 high";
              case 0xFF06: return "TED: Test, ECM, BMM, Blank, Rows, Y2, Y1, Y0";
              case 0xFF07: return "TED: RVS off PAL, Freeze, MCM, Columns, X2, X1, X0";
              case 0xFF08: return "TED: Keyboard Latch";
              case 0xFF09: return "TED: Interrupt";
              case 0xFF0A: return "TED: Interrupt mask";
              case 0xFF0B: return "TED: RC7, RC6, RC5, RC4, RC3, RC2, RC1, RC0";
              case 0xFF0C: return "TED: C9, CUR8";
              case 0xFF0D: return "TED: CUR7, CUR6, CUR5, CUR4, CUR3, CUR2, CUR1, CUR0";
              case 0xFF0E: return "TED: Voice #1 frequency, bits 0-7";
              case 0xFF0F: return "TED: Voice #2 frequency, bits 0-7";
              case 0xFF10: return "TED: Voice #2 frequency, bits 8 & 9";
              case 0xFF11: return "TED: Bits 0-3 : Volume control";
              case 0xFF12: return "TED: Bit 0-1 : Voice #1 frequency, bits 8 & 9";
              case 0xFF13: return "TED: Bit 0 Clock status";
              case 0xFF14: return "TED: Bits 3-7 Video matrix/color memory base address";
              case 0xFF15: return "TED: Background color register";
              case 0xFF16: return "TED: Color register #1";
              case 0xFF17: return "TED: Color register #2";
              case 0xFF18: return "TED: Color registes #3";
              case 0xFF19: return "TED: Color registes #4";
              case 0xFF1A: return "TED: Bit map reload high";
              case 0xFF1B: return "TED: Bit map reload low";
              case 0xFF1C: return "TED: Bit 0 : Vertical line bit 8";
              case 0xFF1D: return "TED: Bits 0-7 : Vertical line bits 0-7";
              case 0xFF1E: return "TED: Horizontal position";
              case 0xFF1F: return "TED: Blink, vertical sub address";
              case 0xFF3E: return "ROM select";
              case 0xFF3F: return "RAM select";
              case 0xFF49:   
              case 0xFF4A:
              case 0xFF4B: return "JMP to define function key routine"; 
              case 0xFF4C:   
              case 0xFF4D:
              case 0xFF4E: return "JMP to PRINT routine";   
              case 0xFF4F:   
              case 0xFF50:
              case 0xFF51: return "JMP to PRIMM routine";
              case 0xFF52:   
              case 0xFF53:
              case 0xFF54: return "JMP to ENTRY routine";              
              case 0xFF80: return "Release of Kernel: (MSB: 0=NTSC ; 1=PAL)";
              case 0xFF81: return "Initialize screen editor";
              case 0xFF84: return "Initialize I/O devices";
              case 0xFF87: return "Ram test";
              case 0xFF8A: return "Restore vectors to initial values";
              case 0xFF8D: return "Change vectors for user";
              case 0xFF90: return "Control O.S. messages";
              case 0xFF93: return "Send SA after LISTEN";
              case 0xFF96: return "Send SA after TALK";
              case 0xFF99: return "Set/Read top of memory";
              case 0xFF9C: return "Set/Read bottom of memory";
              case 0xFF9F: return "Scan keyboard";
              case 0xFFA2: return "Set timeout in DMA disk";
              case 0xFFA5: return "Handshake serial bus or DMA disk byte in";
              case 0xFFA8: return "Handshake serial bus or DMA disk byte out";
              case 0xFFAB: return "Send UNTALK out serial bus or DMA disk";
              case 0xFFAE: return "Send UNLISTEN out serial bus or DMA disk";
              case 0xFFB1: return "Send LISTEN out serial bus or DMA disk";
              case 0xFFB4: return "Send TALK out serial bus or DMA disk";
              case 0xFFB7: return "Return I/O STATUS byte";
              case 0xFFBA: return "Set LA, FA, SA";
              case 0xFFBD: return "Set length and FN address";
              case 0xFFC0: return "Open logical file";
              case 0xFFC3: return "Close logical file";
              case 0xFFC6: return "Open channel in";
              case 0xFFC9: return "open channel out";
              case 0xFFCC: return "Close I/O channels";
              case 0xFFCF: return "Input from channel";
              case 0xFFD2: return "output to channel";
              case 0xFFD5: return "Load from file";
              case 0xFFD8: return "Save to file";
              case 0xFFDB: return "Set internal clock";
              case 0xFFDE: return "Read internal clock";
              case 0xFFE1: return "Scan STOP key";
              case 0xFFE4: return "Get character from queue";
              case 0xFFE7: return "Close all files";
              case 0xFFEA: return "Increment clock";
              case 0xFFED: return "Screen org.";
              case 0xFFF0: return "Read/Set X,Y coord of cursor";
              case 0xFFF3: return "Return location of start of I/O";
            default:
                if ((addr>=0xD0) && (addr<=0xD7)) return "Area for use by speech software"; 
                if ((addr>=0xD8) && (addr<=0xE8)) return "Area for use by application software";   
                if ((addr>=0x113) && (addr<=0x122)) return "Color/luminance table in RAM";
                if ((addr>=0x124) && (addr<=0x1FF)) return "System stack";
                if ((addr>=0x200) && (addr<=0x258)) return "Basic/monitor input buffer";
                if ((addr>=0x259) && (addr<=0x25C)) return "Basic storage";
                if ((addr>=0x25E) && (addr<=0x26D)) return "Area for filename";
                if ((addr>=0x27D) && (addr<=0x2AC)) return "Area used to build DOS string";
                if ((addr>=0x333) && (addr<=0x3F2)) return "Cassette tape buffer";
                if ((addr>=0x3F7) && (addr<=0x436)) return "RS-232 input queue";
                if ((addr>=0x494) && (addr<=0x4A1)) return "Shared ROM fetch sub";
                if ((addr>=0x4A5) && (addr<=0x4AF)) return "Txtptr";
                if ((addr>=0x4B0) && (addr<=0x4BA)) return "Index & Index1";
                if ((addr>=0x4BB) && (addr<=0x4C5)) return "Index2";
                if ((addr>=0x4C6) && (addr<=0x4D0)) return "Strng1";
                if ((addr>=0x4D1) && (addr<=0x4DB)) return "Lowtr";
                if ((addr>=0x4DC) && (addr<=0x4E6)) return "Facmo";
                if ((addr>=0x509) && (addr<=0x512)) return "Logical file numbers";
                if ((addr>=0x513) && (addr<=0x51C)) return "Primary device numbers";
                if ((addr>=0x51D) && (addr<=0x526)) return "Secondary addresses";
                if ((addr>=0x527) && (addr<=0x530)) return "IRQ keyboard buffer";   
                if ((addr>=0x55F) && (addr<=0x556)) return "Table of P.F. lengths";  
                if ((addr>=0x567) && (addr<=0x5E6)) return "P.F. Key storage area";  
                if ((addr>=0x5F5) && (addr<=0x65D)) return "RAM areas for banking";  
                if ((addr>=0x65E) && (addr<=0x6EB)) return "RAM area for speech";  
                if ((addr>=0x6EC) && (addr<=0x7AF)) return "BASIC run-time stack";  
                if ((addr>=0x7B8) && (addr<=0x7BD)) return "Time constant";
                if ((addr>=0x7D9) && (addr<=0x7E4)) return "Indirect routine downloaded";
                if ((addr>=0x800) && (addr<=0xBFF)) return "Color memory (Text)";
                if ((addr>=0xC00) && (addr<=0xCFF)) return "Video matrix (Text)";
                if ((addr>=0x1000) && (addr<=0x17FF)) return "BASIC RAM (without graphics)";
                if ((addr>=0x1800) && (addr<=0x1BFF)) return "Luminance for bit map screen";
                if ((addr>=0x1C00) && (addr<=0x1FFF)) return "Color for bit map";
                if ((addr>=0x2000) && (addr<=0x3FFF)) return "Graphics screen data";
                if ((addr>=0x4000) && (addr<=0x7FFF)) return "BASIC RAM (with graphics)";
                if ((addr>=0x8000) && (addr<=0xBFFF)) return "BASIC ROM";
                if ((addr>=0xC000) && (addr<=0xCFFF)) return "BASIC Expansion";
                if ((addr>=0xD000) && (addr<=0xD7FF)) return "Character table";
                if ((addr>=0xFD00) && (addr<=0xFD0F)) return "6551 ACIA";
                if ((addr>=0xFD10) && (addr<=0xFD1F)) return "6529B";
                if ((addr>=0xFD30) && (addr<=0xFD3F)) return "6529B";
            }
        }
    }  
    return super.dcom(iType, aType, addr, value);
  }             
}
