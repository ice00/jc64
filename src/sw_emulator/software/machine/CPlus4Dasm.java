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
              
            default:
                if ((addr>=0xD0) && (addr<=0xD7)) return "Area for use by speech software"; 
                if ((addr>=0xD8) && (addr<=0xE8)) return "Area for use by application software";   
                if ((addr>=0x113) && (addr<=0x122)) return "Color/luminance table in RAM";
                if ((addr>=0x124) && (addr<=0x1FF)) return "System stack";
                if ((addr>=0x200) && (addr<=0x258)) return "Basic/monitor input buffer";
                if ((addr>=0x259) && (addr<=0x25C)) return "Basic storage";
                if ((addr>=0x25E) && (addr<=0x26D)) return "Area for filename";
                if ((addr>=0x27D) && (addr<=0x2AC)) return "Area used to build DOS string";
            }
        }
    }  
    return super.dcom(iType, aType, addr, value);
  }             
}
