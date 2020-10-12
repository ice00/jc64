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

/**
 * Comment the memory location of Plus4 for the disassembler
 * It performs also a multy language comments.
 * 
 * @author ice
 */
public class CVic20Dasm extends M6510Dasm {
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
                
            }
          default:
            switch ((int)addr) {    
              case 0x00: return "Jump for USR";
              case 0x01: 
              case 0x02: return "Vector for USR";
              case 0x03: 
              case 0x04: return "Float-Fixed vector";
              case 0x05: 
              case 0x06: return "Fixed-Float vector";
              case 0x07: return "Search character";
              case 0x08: return "Scan-quotes flag";
              case 0x09: return "TAB column save";
              case 0x0A: return "0=LOAD, 1=VERIFY";
              case 0x0B: return "Input buffer pointer/# subscript";
              case 0x0C: return "Default DIM flag";
              case 0x0D: return "Type: FF=string, 00=numeric";
              case 0x0E: return "Type: 80=integer, 00=floating point";
              case 0x0F: return "DATA scan/LlST quote/memory flag";
              case 0x10: return "Subscript/FNx flag";
              case 0x11: return "0 = INPUT;$40 = GET;$98 = READ";
              case 0x12: return "ATN sign/Comparison eval flag";
              case 0x13: return "Current l/O prompt flag";
              case 0x14:
              case 0x15: return "Integer value";
              case 0x16: return "Pointer: temporary string stack";
              case 0x17: 
              case 0x18: return "Last temp string vector";
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
              case 0x26:
              case 0x27:
              case 0x28:
              case 0x29:
              case 0x2A: return "Product area for multiplication";
              case 0x2B: 
              case 0x2C: return "Pointer: Start of Basic";
              case 0x2D: 
              case 0x2E: return "Pointer: Start of Variables";
              case 0x2F: 
              case 0x30: return "Pointer: Start of Arrays";
              case 0x31: 
              case 0x32: return "Pointer: End of Arrays";
              case 0x33: 
              case 0x34: return "Pointer: String storage (moving down)";
              case 0x35: 
              case 0x36: return "Utility string pointer";
              case 0x37: 
              case 0x38: return "Pointer: Limit of memory";
              case 0x39: 
              case 0x3A: return "Current Basic line number";
              case 0x3B: 
              case 0x3C: return "Previous Basic line number";
              case 0x3D: 
              case 0x3E: return "Pointer: Basic statement for CONT";
              case 0x3F: 
              case 0x40: return "Current DATA line number";
              case 0x41: 
              case 0x42: return "Current DATA address"; 
              case 0x43: 
              case 0x44: return "Input vector";
              case 0x45: 
              case 0x46: return "Current variable name";
              case 0x47: 
              case 0x48: return "Current variable address";
              case 0x49: 
              case 0x4A: return "Variable pointer for FOR/NEXT";
              case 0x4B: 
              case 0x4C: return "Y-save; op-save; Basic pointer save";
              case 0x4D: return "Comparison symbol accumulator";
              case 0x4E:
              case 0x4F:
              case 0x50:
              case 0x51: 
              case 0x52: 
              case 0x53: return "Misc work area, pointers, etc";
              case 0x54: 
              case 0x55: 
              case 0x56: return "Jump vector for functions";
              case 0x57:     
              case 0x58:
              case 0x59:      
              case 0x5A:      
              case 0x5B:      
              case 0x5C:
              case 0x5D: 
              case 0x5E:
              case 0x5F: 
              case 0x60: return "Misc numeric work area";   
              case 0x61: return "Accum#1: Exponent";
              case 0x62:
              case 0x63: 
              case 0x64: 
              case 0x65: return "Accum#1: Mantissa"; 
              case 0x66: return "Accum#1: Sign";
              case 0x67: return "Series evaluation constant pointer";
              case 0x68: return "Accum#1 hi-order (overflow)";
              case 0x69: 
              case 0x6A: 
              case 0x6B:
              case 0x6C: 
              case 0x6D: 
              case 0x6E: return "Accum#2: Exponent, etc.";
              case 0x6F: return "Sign comparison, Acc#1 vs #2"; 
              case 0x70: return "Accum#1 lo-order (rounding)";
              case 0x71: 
              case 0x72: return "Cassette buffer length/Series pointer";
              case 0x7A: 
              case 0x7B: return "Basic pointer (within subroutine)";              
              case 0x8B: 
              case 0x8C:
              case 0x8D: 
              case 0x8E:
              case 0x8F: return "RND seed value";
              case 0x90: return "Status word ST";
              case 0x91: return "Keyswitch PIA: STOP and RVS flags";
              case 0x92: return "Timing constant for tape";  
              case 0x93: return "Load=0, Verify=1";
              case 0x94: return "Serial output: deferred char flag";
              case 0x95: return "Serial deferred character";
              case 0x96: return "Tape EOT received";
              case 0x97: return "Register save";
              case 0x98: return "How many open files";
              case 0x99: return "Input device (normally 0)";
              case 0x9A: return "Output (CMD) device, normally 3";
              case 0x9B: return "Tape character parity";
              case 0x9C: return "Byte-received flag";
              case 0x9D: return "Direct=$80/RUN=0 output control"; 
              case 0x9E: return "Tape Pass 1 error log/char buffer";
              case 0x9F: return "Tape Pass 2 error log corrected"; 
              case 0xA0: 
              case 0xA1:
              case 0xA2: return "Jiffy Clock (HML)";
              case 0xA3: return "Serial bit count/EOI flag"; 
              case 0xA4: return "Cycle count";
              case 0xA5: return "Countdown, tape write/bit count";
              case 0xA6: return "Pointer: tape buffer";
              case 0xA7: return "Tape Write ldr count/Read pass/inbit";
              case 0xA8: return "Tape Write new byte/Read error/inbit";
              case 0xA9: return "Write start bit/Read bit err/stbit";
              case 0xAA: return "Tape Scan;Cnt;Ld;End/byte assy";
              case 0xAB: return " Write lead length/Rd checksum/parity";
              case 0xAC:
              case 0xAD: return "Pointer: tape buffer, scrolling";
              case 0xAE: 
              case 0xAF: return "Tape end addresses/End of program";
              case 0xB0:
              case 0xB1: return "Tape timing constants";
              case 0xB2:
              case 0xB3: return "Pointer: start of tape buffer";              
              case 0xB4: return "Tape timer (1 =enable); bit cnt";
              case 0xB5: return "Tape EOT/RS-232 next bit to send";
              case 0xB6: return "Read character error/outbyte buffer";
              case 0xB7: return "# characters in file name";
              case 0xB8: return "Current logical file";
              case 0xB9: return "Current secondary address";
              case 0xBA: return "Current device";
              case 0xBB: 
              case 0xBC: return "Pointer: to file name";
              case 0xBD: return "Write shift word/Read input char";
              case 0xBE: return "# blocks remaining to Write/Read";
              case 0xBF: return "Serial word buffer";
              case 0xC0: return "Tape motor interlock";
              case 0xC1:
              case 0xC2: return "I/O start addresses"; 
              case 0xC3:
              case 0xC4: return "KERNAL setup pointer";
              case 0xC5: return "Current key pressed"; 
              case 0xC6: return "# chars in keyboard buffer";
              case 0xC7: return "Screen reverse flag";
              case 0xC8: return "Pointer: End-of-line for input";
              case 0xC9:
              case 0xCA: return "Input cursor log (row, column)";
              case 0xCB: return "Which key: 64 if no key";
              case 0xCC: return "Cursor enable (0=flash cursor)";
              case 0xCD: return "Cursor timing countdown";
              case 0xCE: return "Character under cursor";
              case 0xCF: return "Cursor in blink phase"; 
              case 0xD0: return "Input from screen/from keyboard"; 
              case 0xD1:
              case 0xD2: return "Pointer to screen line";
              case 0xD3: return "Position of cursor on above line";
              case 0xD4: return "0=direct cursor, else programmed";
              case 0xD5: return "Current screen line length";
              case 0xD6: return "Row where cursor lives";
              case 0xD7: return "Last inkey/checksum/buffer";
              case 0xD8: return "# of INSERTs outstanding"; 
              case 0xF1: return "Dummy screen link";
              case 0xF2: return "Screen row marker";
              case 0xF3: 
              case 0xF4: return "Screen color pointer";   
              case 0xF5:
              case 0xF6: return "Keyboard pointer";
              case 0xF7: 
              case 0xF8: return "RS-232 Rcv pointer";
              case 0xF9: 
              case 0xFA: return "RS-232 Tx pointer"; 
              case 0xFB:
              case 0xFC:
              case 0xFD:
              case 0xFE: return "Operating system free zero page space";
              case 0xFF: return "Basic storage";  
                
              case 0x281:
              case 0x282: return "Start of memory for op system";
              case 0x283:
              case 0x284: return "Top of memory for op system";   
              case 0x285: return "Serial bus timeout flag";
              case 0x286: return "Current color code"; 
              case 0x287: return "Color under cursor";    
              case 0x288: return "Screen memory page";                
              case 0x289: return "Max size of keyboard buffer";
              case 0x28A: return "Key repeat (128=repeat all keys)";
              case 0x28B: return "Repeat speed counter";
              case 0x28C: return "Repeat delay counter";
              case 0x28D: return "Keyboard Shift/Control flag";
              case 0x28E: return "Last keyboard shift pattern";
              case 0x28F:
              case 0x290: return "Pointer: decode logic";
              case 0x291: return "Shift mode switch (0=enabled, 128=locked)";
              case 0x292: return "Autoscrolldownflag (0=on, <>0=off)";
              case 0x293: return "RS-232 control register";
              case 0x294: return "RS-232 command register";
              case 0x295:
              case 0x296: return "Nonstandard (Bit time/2-100)";
              case 0x297: return "RS-232 status register";
              case 0x298: return "Number of bits to send";
              case 0x299: 
              case 0x29A: return "Baud rate (full) bit time";
              case 0x29B: return "RS-232 receive pointer";
              case 0x29C: return "RS-232 input pointer";
              case 0x29D: return "RS-232 transmit pointer";
              case 0x29E: return "RS-232 output pointer";
              case 0x29F:
              case 0x2A0: return "Holds IRQ during tape operations";
           
              case 0x300:
              case 0x301: return "Error message link";
              case 0x302:
              case 0x303: return "Basic warm start link";
              case 0x304:
              case 0x305: return "Crunch Basic tokens link";
              case 0x306:
              case 0x307: return "Print tokens link";
              case 0x308:
              case 0x309: return "Start new Basic code link";
              case 0x30A:
              case 0x30B: return "Get arithmetic element link";
              case 0x30C: return "Storage for 6502 .A register";
              case 0x30D: return "Storage for 6502 .X register";
              case 0x30E: return "Storage for 6502 .Y register";
              case 0x30F: return "Storage for 6502 .P register";
              case 0x314:
              case 0x315: return "Hardware (IRQ) interrupt vector [EABF]";
              case 0x316:
              case 0x317: return "Break interrupt vector [FED2]";
              case 0x318:
              case 0x319: return "NMI interrupt vector [FEAD]";
              case 0x31A:
              case 0x31B: return "OPEN vector [F40A]";
              case 0x31C:
              case 0x31D: return "CLOSE vector [F34A]";
              case 0x31E:
              case 0x31F: return "Set-input vector [F2C7]";
              case 0x320:
              case 0x321: return "Set-output vector [F309]";
              case 0x322:
              case 0x323: return "Restore l/O vector [F3F3]";
              case 0x324:
              case 0x325: return "INPUT vector [F20E]";
              case 0x326:
              case 0x327: return "Output vector [F27A]";
              case 0x328:
              case 0x329: return "Test-STOP vector [F770]";
              case 0x32A:
              case 0x32B: return "GET vector [F1F5]";
              case 0x32C:
              case 0x32D: return "Abort l/O vector [F3EF]";
              case 0x32E:
              case 0x32F: return "User vector (default BRK) [FED2]";
              case 0x330:
              case 0x331: return "Link to load RAM [F549]";
              case 0x332:
              case 0x333: return "Link to save RAM [F685]";     
              
              case 0x9000: return "Vic: bits 0-6 horizontal centering, bit 7 sets interlace scan";
              case 0x9001: return "Vic: vertical centering";
              case 0x9002: return "Vic: bits 0-6 set # of columns, bit 7 is part of video matrix address";
              case 0x9003: return "Vic: bits 1-6 set # of rows, bit 0 sets 8x8 or 16x8 chars";
              case 0x9004: return "Vic: TV raster beam line";
              case 0x9005: return "Vic: bits 0-3 start of character memory (default = 0), bits 4-7 is rest of video address (default= F)";
              case 0x9006: return "Vic: horizontal position of light pen";
              case 0x9007: return "Vic: vertical position of light pen";
              case 0x9008: return "Vic: digitized value of paddle X";
              case 0x9009: return "Vic: digitized value of paddle Y";
              case 0x900A: return "Vic: frequency for oscillator 1 (low) (on: 128-255)";
              case 0x900B: return "Vic: frequency for oscillator 2 (medium) (on: 128-255)";
              case 0x900C: return "Vic: frequency for oscillator 3 (high) (on: 128-255)";
              case 0x900D: return "Vic: frequency of noise source";
              case 0x900E: return "Vic: bit 0-3 sets volume of all sound, bits 4-7 are auxiliary color information";
              case 0x900F: return "Vic: Screen and border color register, bits 4-7 select background color, bits 0-2 select border color, bit 3 selects inverted or normal mode";
              
              case 0x9110: return "Via #1: Port B output register";
              case 0x9111: return "Via #1: Port A output register";
              case 0x9112: return "Via #1: Data direction register B";
              case 0x9113: return "Via #1: Data direction register A";
              case 0x9114: return "Via #1: Timer 1 low byte";
              case 0x9115: return "Via #1: Timer 1 high byte & counter";
              case 0x9116: return "Via #1: Timer 1 low byte";
              case 0x9117: return "Via #1: Timer 1 high byte";
              case 0x9118: return "Via #1: Timer 2 low byte";
              case 0x9119: return "Via #1: Timer 2 high byte";
              case 0x911A: return "Via #1: Shift register";
              case 0x911B: return "Via #1: Auxiliary control register";
              case 0x911C: return "Via #1: Peripheral control register";
              case 0x911D: return "Via #1: Interrupt flag register";
              case 0x911E: return "Via #1: Interrupt enable register";
              case 0x911F: return "Via #1 :Port A (Sense cassette switch)";
              
              case 0x9120: return "Via #1: Port B output register";
              case 0x9121: return "Via #1: Port A output register";
              case 0x9122: return "Via #1: Data direction register B";
              case 0x9123: return "Via #1: Data direction register A";
              case 0x9124: return "Via #1: Timer 1 low byte latch";
              case 0x9125: return "Via #1: Timer 1 high byte latch";
              case 0x9126: return "Via #1: Timer 1 low byte counter";
              case 0x9127: return "Via #1: Timer 1 high byte counter";
              case 0x9128: return "Via #1: Timer 2 low byte";
              case 0x9129: return "Via #1: Timer 2 high byte";
              case 0x912A: return "Via #1: Shift register";
              case 0x912B: return "Via #1: Auxiliary control register";
              case 0x912C: return "Via #1: Peripheral control register";
              case 0x912D: return "Via #1: Interrupt flag register";
              case 0x912E: return "Via #1: Interrupt enable register";
              case 0x912F: return "Via #1: Port A output register";              
              
              case 0xC000: 
              case 0xC001: return "Basic Restart Vectors";
              case 0xC00C: 
              case 0xC00D: return "BASIC Command Vectors";
              case 0xC052: return "";
              case 0xC053: return "BASIC Function Vectors";
              case 0xC080: 
              case 0xC081: return "BASIC Operator Vectors	";
              case 0xC09E: return "BASIC Command Keyword Table";
              case 0xC129: return "BASIC Misc. Keyword Table";
              case 0xC140: return "BASIC Operator Keyword Table";
              case 0xC14D: return "BASIC Function Keyword Table";
              case 0xC19E: return "Error Message Table";
              case 0xC328: 
              case 0xC329: return "Error Message Pointers";
              case 0xC364: return "Misc. Messages: ok";
              case 0xC369: return "Misc. Messages: error";
              case 0xC38A: return "Find FOR/GOSUB Entry on Stack";
              case 0xC3B8: return "Open Space in Memory";
              case 0xC3FB: return "Check Stack Depth";
              case 0xC408: return "Check Memory Overlap";
              case 0xC435: return "Output ?OUT OF MEMORY Error";
              case 0xC437: return "Error Routine";
              case 0xC469: return "Break Entry";
              case 0xC474: return "Restart BASIC";
              case 0xC480: return "Input & Identify BASIC Line";
              case 0xC49C: return "Get Line Number & Tokenise Text";
              case 0xC4A2: return "Insert BASIC Text";
              case 0xC533: return "Rechain Lines";
              case 0xC560: return "Input Line Into Buffer";
              case 0xC579: return "Tokenise Input Buffer";
              case 0xC613: return "Search for Line Number";
              case 0xC642: return "Perform [new]";
              case 0xC65E: return "Perform [clr]";
              case 0xC68E: return "Reset TXTPTR";
              case 0xC69C: return "Perform [list]";
              case 0xC717: return "Handle LIST Character";
              case 0xC742: return "Perform [for]";
              case 0xC7AE: return "BASIC Warm Start";
              case 0xC7C4: return "Check End of Program";
              case 0xC7E1: return "Prepare to execute statement";
              case 0xC7ED: return "Perform BASIC Keyword";
              case 0xC81D: return "Perform [restore]";
              case 0xC82C: return "Perform [stop], [end], break";
              case 0xC857: return "Perform [cont]";
              case 0xC871: return "Perform [run]";
              case 0xC883: return "Perform [gosub]";
              case 0xC8A0: return "Perform [goto]";
              case 0xC8D2: return "Perform [return]";
              case 0xC8F8: return "Perform [data]";
              case 0xC906: return "Search for Next Statement/Line";
              case 0xC928: return "Perform [if]";
              case 0xC93B: return "Perform [rem]";
              case 0xC94B: return "Perform [on]";
              case 0xC96B: return "Fetch linnum From BASIC";
              case 0xC9A5: return "Perform [let]";
              case 0xC9C4: return "Assign Integer";
              case 0xC9D6: return "Assign Floating Point";
              case 0xC9D9: return "Assign String";
              case 0xC9E3: return "Assign TI$";
              case 0xCA2C: return "Add Digit to FAC#1";
              case 0xCA80: return "Perform [print]#";
              case 0xCA86: return "Perform [cmd]";
              case 0xC99A: return "Print String From Memory";
              case 0xCAA0: return "Perform [print]";
              case 0xCAB8: return "Output Variable";
              case 0xCAD7: return "Output CR/LF";
              case 0xCAE8: return "Handle comma, TAB(, SPC(";
              case 0xCB1E: return "Output String";
              case 0xCB3B: return "Output Format Character";
              case 0xCB4B: return "Handle Bad Data";
              case 0xCB7B: return "Perform [get]";
              case 0xCBA5: return "Perform [input#]";
              case 0xCBBF: return "Perform [input]";
              case 0xCBEA: return "Read Input Buffer";
              case 0xCBF9: return "Do Input Prompt";
              case 0xC06: return "Perform [read]";
              case 0xCC35: return "General Purpose Read Routine";
              case 0xCCFC: return "Input Error Messages";
              
              
              
            default:
                if ((addr>=0x73) && (addr<=0x8A)) return "CHRGET subroutine (get BASIC char)";  
                if ((addr>=0xD9) && (addr<=0xF0)) return "Screen line link table"; 
                if ((addr>=0x100) && (addr<=0x10A)) return "Floating to ASCII work area";
                if ((addr>=0x10B) && (addr<=0x013E)) return "Tape error log";
                if ((addr>=0x100) && (addr<=0x1FF)) return "Processor stack area";
                if ((addr>=0x200) && (addr<=0x258)) return "Basic input buffer";
                if ((addr>=0x259) && (addr<=0x262)) return "Logical file table";
                if ((addr>=0x263) && (addr<=0x26C)) return "Device # table";
                if ((addr>=0x26D) && (addr<=0x276)) return "Secondary Address table";
                if ((addr>=0x277) && (addr<=0x280)) return "Keyboard buffer";
                if ((addr>=0x2A1) && (addr<=0x2FF)) return "Program indirects";
                if ((addr>=0x33C) && (addr<=0x03FB)) return "Cassette buffer";
                if ((addr>=0x400) && (addr<=0x0FFF)) return "3K expansion RAM area";
                if ((addr>=0x1000) && (addr<=0x11FF)) return "User Basic area/Screen memory";
                if ((addr>=0x1200) && (addr<=0x1DFF)) return "User Basic area";
                if ((addr>=0x1E00) && (addr<=0x1FFF)) return "Screen memory";
                if ((addr>=0x2000) && (addr<=0x3FFF)) return "8K expansion RAM/ROM block 1";
                if ((addr>=0x4000) && (addr<=0x5FFF)) return "8K expansion RAM/ROM block 2";
                if ((addr>=0x6000) && (addr<=0x7FFF)) return "8K expansion RAM/ROM block 3";
                if ((addr>=0x8000) && (addr<=0x83FF)) return "4K Character generator ROM/Upper case and graphics";
                if ((addr>=0x8400) && (addr<=0x87FF)) return "4K Character generator ROM/Reversed upper case and graphics";
                if ((addr>=0x8800) && (addr<=0x8BFF)) return "4K Character generator ROM/Upper and lower case";
                if ((addr>=0x8C00) && (addr<=0x8FFF)) return "4K Character generator ROM/Reversed upper and lower case";
                if ((addr>=0x9400) && (addr<=0x95FF)) return "location of COLOR RAM with additional RAM at blk 1";
                if ((addr>=0x9600) && (addr<=0x97FF)) return "Normal location of COLOR RAM";
                if ((addr>=0x9800) && (addr<=0x9BFF)) return "I/O block 2";
                if ((addr>=0x9C00) && (addr<=0x9FFF)) return "I/O block 3";
                if ((addr>=0xA000) && (addr<=0xBFFF)) return "8K decoded block for expansion ROM";
              }    
        }
  
    }
    return super.dcom(iType, aType, addr, value);
  }    
}
