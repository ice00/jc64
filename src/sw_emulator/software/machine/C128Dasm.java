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

/**
 * Comment the memory location of C128 for the disassembler
 * It performs also a multy language comments.
 * 
 * @author ice
 */
public class C128Dasm extends M6510Dasm {
  // Available language
  public static final byte LANG_ENGLISH=1;
  public static final byte LANG_ITALIAN=2;
  
  public byte language;///

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
    byte language=option.commentLanguage;  
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
              case 0x00: return "I/O direction register";
              case 0x01: return "I/O port";
              case 0x02: return "Token 'SEARCH' looks for, Bank Number, Jump to SYS Address";
              case 0x03: 
              case 0x04: return "SYS address, MLM register PC";
              case 0x05: return "SYS and MLM register save SR ";
              case 0x06: return "SYS and MLM register save AC";
              case 0x07: return "SYS and MLM register save XR";
              case 0x08: return "SYS and MLM register save YR";
              case 0x09: return "SYS and MLM register save SP";
              case 0x0A: return "Scan-quotes flag";
              case 0x0B: return "TAB column save";
              case 0x0C: return "Flag: 0=LOAD, 1=VERIFY";
              case 0x0D: return "Input buffer pointer/number of subscripts";
              case 0x0E: return "Default DIM flag";
              case 0x0F: return "Data Type: FF=string, 00=numeric";
              case 0x10: return "Data Type: 80=integer, 00=floating point";
              case 0x11: return "DATA scan/LIST quote/memory flag";
              case 0x12: return "Subscript/FNxx flag";
              case 0x13: return "Flag: 0=INPUT, $40=GET, $98=READ";
              case 0x14: return "ATN sign / Comparison evaluation flag";
              case 0x15: return "Current I/O prompt flag";
              case 0x16: 
              case 0x17: return "Integer value";
              case 0x18: return "Pointer: temporary string stack";
              case 0x19: return "Stack for temporary strings";        
              case 0x24:
              case 0x25: 
              case 0x26: 
              case 0x27: return "Utility pointer area";  
              case 0x28: 
              case 0x29:         
              case 0x2A:
              case 0x2B:                  
              case 0x2C: return "Product area for multiplication";  
              case 0x2D:  
              case 0x2E: return "Pointer: Start-of-BASIC (bank 0) [1C01]";   
              case 0x2F:    
              case 0x30: return "Pointer: Start-of-variables (bank 1) [0400]";
              case 0x31: 
              case 0x32: return "Pointer: Start-of-arrays";
              case 0x33: 
              case 0x34: return "Pointer: End-of-arrays";
              case 0x35: 
              case 0x36: return "Pointer string-storage (moving down)";
              case 0x37: 
              case 0x38: return "Utility string pointer";
              case 0x39: 
              case 0x3A: return "Pointer: Limit-of-memory (bank 1) [FF00]";              
              case 0x3B:
              case 0x3C: return "Current BASIC line number";
              case 0x3D:
              case 0x3E: return "Textpointer: BASIC work point (chrget)";    
              case 0x3F: 
              case 0x40: return "Utility Pointer";  
              case 0x41: 
              case 0x42: return "Current DATA line number"; 
              case 0x43: 
              case 0x44: return "Current DATA address";
              case 0x45: 
              case 0x46: return "Input vector";
              case 0x47: 
              case 0x48: return "Current variable name";
              case 0x49: 
              case 0x4A: return "Current variable address";
              case 0x4B:
              case 0x4C: return "Variable pointer for FOR/NEXT";
              case 0x4D: 
              case 0x4E: return "Y-save, op-save, BASIC pointer save";
              case 0x4F: return "Comparison symbol accumulator"; 
              case 0x50: 
              case 0x51:
              case 0x52:
              case 0x53:
              case 0x54:
              case 0x55: return "Miscellaneous work area, pointers, and so on";
              case 0x56:
              case 0x57:
              case 0x58: return "Jump vector for functions";
              case 0x60:
              case 0x61: 
              case 0x62: return "MLM Address 0";
              case 0x63: return "MLM Address 1/Accum #1 exponent";
              case 0x64: 
              case 0x65: return "MLM Address 1/Accum #1 mantissa";
              case 0x66:
              case 0x67: return "MLM Address 2/Accum #1 mantissa"; 
              case 0x68: return "MLM Address 2/Accum #1 sign";   
              case 0x69: return "Series evaluation constant pointer";
              case 0x6A:
              case 0x6B:
              case 0x6C:
              case 0x6D:
              case 0x6E:
              case 0x6F: return "Accum #2 exponent, and so on";
              case 0x70: return "Sign comparison Acc #1 versus #2";
              case 0x71: return "Accum #1 lo-order (rounding)";
              case 0x72:                   
              case 0x73: return "Cassette buffer len / Series pointer";
              case 0x74: 
              case 0x75: return "Auto line number increment";
              case 0x76: return "Graphics flag: FF = Graphics allocated, 00 = Not allocated";
              case 0x77: return "Color source number";
              case 0x78: 
              case 0x79: return "Temporary counters";
              case 0x7A:  
              case 0x7B:  
              case 0x7C: return "DS$ descriptor";
              case 0x7D: 
              case 0x7E: return "BASIC pseudo-stack pointer";
              case 0x7F: return "Flag: 0 = direct mode";
              case 0x80:
              case 0x81: return "DOS, USING work flags";
              case 0x82: return "Stack pointer save for errors";              
              case 0x83: return "Graphic color source";
              case 0x84: return "Multicolor 1 (1)";
              case 0x85: return "Multicolor 2 (2)";
              case 0x86: return "Graphic foreground color (13)";
              case 0x87: 
              case 0x88: 
              case 0x89: 
              case 0x8A: return "Graphic scale factors, X & Y";              
              case 0x8B: 
              case 0x8C:
              case 0x8D:
              case 0x8E:
              case 0x8F: return "Graphic work values";
              case 0x90: return "Status word ST";
              case 0x91: return "Keyswitch 1A: STOP and RVS flags"; 
              case 0x92: return "Timing constant for tape ($80)";  
              case 0x93: return "Work value, monitor, LOAD/SAVE: 0=LOAD, 1=VERIFY";
              case 0x94: return "Serial output, deferred character flag";
              case 0x95: return "Serial deferred character";
              case 0x96: return "Cassette work value";
              case 0x97: return "Register save";
              case 0x98: return "How many open files";
              case 0x99: return "Input device, normally 0";
              case 0x9A: return "Output CMD device, normally 3";
              case 0x9B: 
              case 0x9C: return "Tape parity, output-received flag";
              case 0x9D: return "I/O messages: 192=all, 128=commands, 64=errors, 0=nil";
              case 0x9E:
              case 0x9F: return "Tape error pointers";
              case 0xA0: 
              case 0xA1:
              case 0xA2: return "Jiffy clock HML";
              case 0xA3: 
              case 0xA4: return "Temp data area";
              case 0xA5: 
              case 0xA6: return "I/O work bytes (tape)";
              case 0xA7: return "RS-232 Input Bit Storage, Cassette Short Count";
              case 0xA8: return "RS-232 Bit Count In, Cassette Read Error";
              case 0xA9: return "RS-232 Flag For Start Bit Check, Cassette Reading Zeroes";
              case 0xAA: return "RS-232 Byte Buffer, Cassette Read Mode";
              case 0xAB: return "RS-232 Parity Storage, Cassette Short Cnt";
              case 0xAC: 
              case 0xAD: return "Pointer for tape buffer and screen scrolling";
              case 0xAE:
              case 0xAF: return "Tape end address / End of program";
              case 0xB0: 
              case 0xB1: return "Tape timing constants";
              case 0xB2:
              case 0xB3: return "Pointer: Start of tape buffer";
              case 0xB4: return "RS-232 Bit Count";
              case 0xB5: return "RS-232 Next Bit To Be Sent";
              case 0xB6: return "RS-232 Byte Buffer";
              case 0xB7: return "Number of characters in file name";
              case 0xB8: return "Current logical file";
              case 0xB9: return "Current secondary address";
              case 0xBA: return "Current device"; 
              case 0xBB: 
              case 0xBC: return "Pointer to file name";  
              case 0xBD: return "RS-232 TRNS Parity Buffer"; 
              case 0xBE: return "Cassette Read Block Count"; 
              case 0xBF: return "Serial Word Buffer";
              case 0xC0: return "Cassette Manual/Cntrolled Switch (Updated during IRQ)";
              case 0xC1:
              case 0xC2: return "I/O Start Address";
              case 0xC3:
              case 0xC4: return "Cassette LOAD Temps (2 bytes)";
              case 0xC5: return "Tape Read/Write Data";
              case 0xC6: 
              case 0xC7: return "BANKS: I/O data, filename";
              case 0xC8: 
              case 0xC9: return "RS-232 input buffer addresses  [0c00]";
              case 0xCA: 
              case 0xCB: return "RS-232 output buffer addresses [0d00]";
              case 0xCC: 
              case 0xCD: return "Keyboard decode pointer (bank 15) [fa80]";
              case 0xCE: 
              case 0xCF: return "Print string work pointer"; 
              case 0xD0: return "Number of characters in keyboard buffer";
              case 0xD1: return "Number of programmed chars waiting";
              case 0xD2: return "Programmed key character index";
              case 0xD3: return "Key shift flag";   
              case 0xD4: return "Current key code: 88=no key";
              case 0xD5: return "Previous key code: 88=no key";
              case 0xD6: return "Input from screen/from keyboard";
              case 0xD7: return "40/80 columns: 0=40 column screen";
              case 0xD8: return "Graphics mode code";   
              case 0xD9: return "Character base: 0=ROM, 4=RAM";
              case 0xDA:
              case 0xDB:
              case 0xDC:
              case 0xDD:
              case 0xDE:
              case 0xDF: return "Misc Editor work area";
              case 0xE0:
              case 0xE1: return "Pointer to screen line/cursor";
              case 0xE2:    
              case 0xE3: return "Color line pointer";
              case 0xE4: return "Current screen bottom margin";
              case 0xE5: return "Current screen top margin";
              case 0xE6: return "Current screen left margin";
              case 0xE7: return "Current screen right margin";
              case 0xE8: return "";    
              case 0xE9: return "Input cursor log (row, column)"; 
              case 0xEA: return "End-of-line for input pointer";
              case 0xEB: return "Row where cursor lives";
              case 0xEC: return "Position of cursor on screen line";
              case 0xED: return "Maximum screen lines (24)";
              case 0xEE: return "Maximum screen columns (39)";
              case 0xEF: return "Current I/O character";
              case 0xF0: return "Previous character printed";
              case 0xF1: return "Character color";
              case 0xF2: return "Temporary color save";
              case 0xF3: return "Screen reverse flag";
              case 0xF4: return "Quotes flag (Editor), 0=direct cursor, else programmed";
              case 0xF5: return "Number of INSERTs outstanding";
              case 0xF6: return "255 = Auto Insert enabled";
              case 0xF7: return "Text mode lockout (SHFT-C=): 0=enabled, 128=disabled";
              case 0xF8: return "Scrolling: 0 = enabled, 128 = disabled";
              case 0xF9: return "Bell (CTRL-G):  0= enable, 128 = disable";
              case 0xFA:
              case 0xFB:
              case 0xFC: 
              case 0xFD:
              case 0xFE: return "Not used";
              case 0xFF: return "Basic Scratch";
              default:  
                if ((addr>=0x19) && (addr<=0x23)) return "Stack for temporary strings";
                if ((addr>=0x59) && (addr<=0x62)) return "Miscellaneous numeric work area";
            }      
      }
    }  
    return super.dcom(iType, aType, addr, value);       
  }
}
