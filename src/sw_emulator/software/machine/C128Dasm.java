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
              case 0x00: return "6510 Data I/O direction register";
              case 0x01: return "6510 Data I/O port";
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
              case 0x19: return "Last Temp String Address";  
              case 0x1B: return "Stack For Temp Strings";
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
              case 0x7F: return "Flag: 0=direct mode";
              case 0x80:
              case 0x81: return "DOS, USING work flags";
              case 0x82: return "Stack pointer save for errors";              
              case 0x83: return "Graphic color source";
              case 0x84: return "Multicolor 1 (1)";
              case 0x85: return "Multicolor 2 (2)";
              case 0x86: return "Graphic foreground color (13)";
              case 0x87: 
              case 0x88: return "Graphic scale factors X";
              case 0x89: 
              case 0x8A: return "Graphic scale factors Y";              
              case 0x8B: return "Stoppaint if not Background/Not same color";
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
              case 0xDA: return "Pointers For MOVLIN/Temporary For TAB & LINE WRAP Routines";
              case 0xDB: return "Another Temporary Place To Save A Reg.";
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
              case 0xE8:    
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
              
              case 0x110: return "DOS Loop Counter";
              case 0x111: return "DOS Filename 1 Len";
              case 0x112: return "DOS Disk Drive 1";
              case 0x113: return "DOS Filename 2 Len"; 
              case 0x114: return "DOS Disk Drive 2";  
              case 0x115: return "DOS Filename 2 Addr"; 
              case 0x116:
              case 0x117: return "BLOAD/BSAVE Starting Address";  
              case 0x118:
              case 0x119: return "BLOAD/BSAVE Ending Address";  
              case 0x11B: return "DOS Logical Addr [00]";                 
              case 0x11C: return "DOS Phys Addr [08]";
              case 0x11D: return "DOS Sec. Addr [6F]";
              case 0x11E: return "DOS Record Length";
              case 0x120: return "DOS Disk ID";
              case 0x122: return "DOS DSK ID FLG SPACE Used by PRINT USING";
              case 0x123: return "Pointer to Begin. NO.";
              case 0x124: return "Pointer to End NO.";
              case 0x125: return "Dollar Flag";
              case 0x126: return "Comma Flag, PLAY: VOXTUM flag";
              case 0x127: return "Counter";
              case 0x128: return "Sign Exponent";
              case 0x129: return "Pointer to Exponent";
              case 0x12A: return "# Of Digits Before Decimal Point";
              case 0x12B: return "Justify Flag";
              case 0x12C: return "# Of Pos Before Decimal Point (Field)";
              case 0x12D: return "# Of Pos After Decimal Point (Field)";
              case 0x12E: return "+/- Flag (Field)";
              case 0x12F: return "Exponent Flag (Field)";
              case 0x130: return "Switch";
              case 0x131: return "Char Counter (Field)";
              case 0x132: return "Sign No";
              case 0x133: return "Blank/Star Flag";
              case 0x134: return "Pointer to Begin of Field";
              case 0x135: return "Length of Format";
              case 0x136: return "Pointer to End Of Field";
              case 0x2FC:
              case 0x2FD: return "Function Execute Hook";
              case 0x300:
              case 0x301: return "Error Message Link [4D3F]";
              case 0x302:
              case 0x303: return "BASIC Warm Start Link	[4DC6]";
              case 0x304:
              case 0x305: return "Crunch BASIC Tokens Link [430D]";
              case 0x306:
              case 0x307: return "Print Tokens Link [5151]";
              case 0x308:
              case 0x309: return "Start New BASIC Code Link [4AA2]";
              case 0x30A:
              case 0x30B: return "Get Arithmetic Element Link [78DA]";
              case 0x30C:
              case 0x30D: return "Crunch FE Hook [4321]";
              case 0x30E: 
              case 0x30F: return "List FE Hook [51CD]";
              case 0x310:
              case 0x311: return "Execute FE Hook [4BA9]";
              case 0x312:
              case 0x313: return "Unused";
              case 0x314:
              case 0x315: return "IRQ Vector [FA65]";
              case 0x316:
              case 0x317: return "Break Interrupt Vector [B003]";
              case 0x318:
              case 0x319: return "NMI Interrupt Vector[FA40]";
              case 0x31A:
              case 0x31B: return "OPEN Vector [EFBD]";
              case 0x31C:
              case 0x31D: return "CLOSE Vector [F188]";
              case 0x31E:
              case 0x31F: return "Set Input Vector [F106]";
              case 0x320:
              case 0x321: return "Set Output Vector [F14C]";
              case 0x322:
              case 0x323: return "Restore I/O Vector [F226]";
              case 0x324:
              case 0x325: return "Input Vector [EF06]";
              case 0x326:
              case 0x327: return "Output Vector	[EF79]";
              case 0x328:
              case 0x329: return "Test STOP Key [F66E]";
              case 0x32A:
              case 0x32B: return "GET Vector [EEEB]";
              case 0x32C:
              case 0x32D: return "Abort I/O Vector [F222]";
              case 0x32E:
              case 0x32F: return "Machine Lang Monitor Link [B006]";
              case 0x330:
              case 0x331: return "LOAD Link [F26C]";
              case 0x332:
              case 0x333: return "SAVE Link [F54E]";
              case 0x334:
              case 0x335: return "Print Control Code Link [C7B9]";
              case 0x336:
              case 0x337: return "Print High ASCII Code Link [C805]";
              case 0x338:
              case 0x339: return "Print ESC Sequence Link [C9C1]";
              case 0x33A:
              case 0x33B: return "Keyscan Link [C5E1]";
              case 0x33C:
              case 0x33D: return "Store Key [C6AD]";
              case 0x33E:
              case 0x33F: return "Pointer to KBD Decoding Table: Unshifted [FA80/FD29]";
              case 0x340:
              case 0x341: return "Pointer to KBD Decoding Table: Shifted [FAD9/FD82]";
              case 0x342:
              case 0x343: return "Pointer to KBD Decoding Table: Commodore [FB32/FDDB]";
              case 0x344:
              case 0x345: return "Pointer to KBD Decoding Table: Control [FB8B/FE34] 1)";
              case 0x346:
              case 0x347: return "Pointer to KBD Decoding Table: Alt [FA80/FD29]";
              case 0x348:
              case 0x349: return "Pointer to KBD Decoding Table: Ascii/DIN [FB4E/FD29]";              
              case 0x35E:
              case 0x35F: 
              case 0x360:
              case 0x362: return "Bitmap Of Line Wraps";
              case 0x386: return "CHRGOT Entry";
              case 0x3D2:
              case 0x3D3: 
              case 0x3D4: return "Numeric Constant For BASIC";
              case 0x3D5: return "Current Bank For SYS, POKE, PEEK";
              case 0x3D6: 
              case 0x3D7:
              case 0x3D8: 
              case 0x3D9: return "INSTR Work Values";
              case 0x3DA: return "Bank Pointer For String/Number CONVERT RTN";
              case 0x3DB: 
              case 0x3DC:
              case 0x3DE: return "Sprite: Work bytes for SSHAPE";
              case 0x3DF: return "FAC#1 Overflow";
              case 0x3E0: 
              case 0x3E1: return "Sprite: Work bytes for SPRSAV";
              case 0x3E2: return "Graphic Foreground/ Background Color Nybbles";
              case 0x3E3: return "Graphic Foreground/ Multicolor 1 Color Nybbles";
              case 0xA00:
              case 0xA01: return "Vector to Restart System (BASIC Warm) [4003]";
              case 0xA02: return "KERNAL Warm/Cold Init'n Status Byte";
              case 0xA03: return "PAL/NTSC System Flag";
              case 0xA04: return "Flags RESET vs. NMI Status for init'n rtns";
              case 0xA05: 
              case 0xA06: return "Ptr to Bottom of Avail. Memory in System Bank";
              case 0xA07: 
              case 0xA08: return "Ptr to Top of Available Memory in System Bank";
              case 0xA09: return "Tape Handler preserves IRQ Indirect here";
              case 0xA0B: return "TOD Sense during tape operations";
              case 0xA0C: return "CIA 1 Interrupt Log";
              case 0xA0D: return "CIA 1 Timer Enabled";
              case 0xA0F: return "RS-232 Enables";
              case 0xA10: return "RS-232 Control Register";
              case 0xA11: return "RS-232 Command Register";
              case 0xA12: return "RS-232 User Baud Rate";
              case 0xA14: return "RS-232 Status Register";
              case 0xA15: return "RS-232 Number of Bits To Send";
              case 0xA16: return "RS-232 Baud Rate Full Bit Time (Created by OPEN)";
              case 0xA18: return "RS-232 Receive Pointer";
              case 0xA19: return "RS-232 Input Pointer";
              case 0xA1A: return "RS-232 Transmit Pointer";
              case 0xA1B: return "RS-232 Send Pointer";
              case 0xA1C:
              case 0xA1D:
              case 0xA1E:
              case 0xA1F: return "Sleep Countdown, FFFF = disable";
              case 0xA20: return "Keyboard Buffer Size (10)";
              case 0xA21: return "Screen Freeze Flag";
              case 0xA22: return "Key Repeat: 128 = all, 64 = none";
              case 0xA23: return "Key Repeat Timing";
              case 0xA24: return "Key Repeat Pause";
              case 0xA25: return "Graphics / Text Toggle Latch";
              case 0xA26: return "40-Col Cursor Mode";
              case 0xA27:
              case 0xA28:
              case 0xA29:
              case 0xA2A: return "40-Col Blink Values";
              case 0xA2B: return "80-Col Cursor Mode";
              case 0xA2C: return "40-Col Video $D018 Image";
              case 0xA2D: return "VIC Bit-Map Base Pointer";
              case 0xA2E:
              case 0xA2F: return "80-Col Pages-Screen, Color";
              case 0xA80: return "Compare Buffer (32 bytes)";
              case 0xAA0:
              case 0xAA1: return "MLM";
              case 0xAAB: return "ASM/DIS";
              case 0xAAC: return "For Assembler";
              case 0xAAF: return "Byte Temp used all over";
              case 0xAB0: return "Byte Temp used all over";
              case 0xAB1: return "Byte Temp for Assembler";
              case 0xAB2: return "Save .X here during Indirect Subroutine Calls";
              case 0xAB3: return "Direction Indicator For 'TRANSFER'";
              case 0xAB4: 
              case 0xAB5: return "Parse Number Conversion";
              case 0xAB7: return "Parse Number Conversion";
              case 0xAC0: return "PAT Counter";
              case 0xAC1:
              case 0xAC2:
              case 0xAC3:
              case 0xAC4: return "ROM Physical Address Table";
              case 0xAC5: return "Flag: KBD";
              case 0x1131: return "Current X Position";
              case 0x1133: return "Current Y Position";
              case 0x1135: return "X-Coordinate Destination";
              case 0x1137: return "Y-Coordinate Destination";
              case 0x1139: return "Line Drawing Variables";
              case 0x1149: return "Sign Of Angle";
              case 0x114A: return "Sine Of Value Of Angle";
              case 0x114C: return "Cosine of Value of Angle";
              case 0x114E: return "Temps For Angle Distance Routines";
              case 0x1150: return "CIRCLE Center, X Coordinate, BOX POINT 1 X-Coord.";
              case 0x1152: return "CIRCLE Center, Y Coordinate, BOX POINT 1 Y-Coord.";
              case 0x1154: return "X Radius, BOX Rotation Angle";
              case 0x1156: return "Y Radius";
              case 0x1158: return "CIRCLE Rotation Angle";
              case 0x115A: return "BOX: Length of a side";
              case 0x115C: return "Arc Angle Start";              
              case 0x115E: return "Arc Angle End, Char's Col. Counter";
              case 0x1160: return "X Radius * COS(Rotation Angle)";
              case 0x1162: return "Y Radius * SIN(Rotation Angle)";
              case 0x1164: return "X Radius * SIN(Rotation Angle)";
              case 0x1166: return "Y Radius * COS(Rotation Angle)";
              case 0x1168: return "HIGH BYTE: ADDR OF CHARROM For 'CHAR' CMD.";
              case 0x1169: return "Temp For GSHAPE";
              case 0x116A: return "SCALE Mode Flag";
              case 0x116B: return "Double Width Flag";
              case 0x116C: return "Box Fill Flag";
              case 0x116D: return "Temp For Bit Mask";
              case 0x116F: return "Trace Mode: FF = on";
              case 0x1170:
              case 0x1171:
              case 0x1172:
              case 0x1173: return "Renumbering Pointers";
              case 0x1174:
              case 0x1175:
              case 0x1176:
              case 0x1177: return "Directory Work Pointers";  
              case 0x117A:
              case 0x117B: return "Float-fixed Vector [849F]";
              case 0x117C:
              case 0x117D: return "Fixed-float Vector [793C]";
              case 0x11E6: return "Sprite X-High Positions";
              case 0x11E7:
              case 0x11E8: return "Sprite Bumb Masks (sprite - backgnd)";
              case 0x11E9:
              case 0x11EA: return "Light Pen Values, X and Y";
              case 0x11EB: return "CHRGEN ROM Page, Text Mode [D8]";
              case 0x11EC: return "CHRGEN ROM Page, Graphics Mode [D0]";
              case 0x11ED: return "Secondary Address For RECORD";
              case 0x1200:
              case 0x1201: return "Previous BASIC Line";
              case 0x1202:
              case 0x1203: return "Pointer: BASIC Statement for CONTINUE";
              case 0x1204: return "PRINT USING Fill Symbol";
              case 0x1205: return "PRINT USING Comma Symbol";
              case 0x1206: return "PRINT USING D.P. Symbol";
              case 0x1207: return "Print Using Monetary Symbol";
              case 0x1208: return "Used by Error Trapping Routine - Last Err No";
              case 0x1209:
              case 0x120A: return "Line # of Last Error. FFFF if No Error";
              case 0x120B:
              case 0x120C: return "TRAP Address, FFFF=none";
              case 0x120D: return "Hold Trap # of Tempor.";
              case 0x1210:
              case 0x1211: return "End of Basic, Bank 0";
              case 0x1212: 
              case 0x1213: return "Basic Program Limit [FF00]";
              case 0x1214: 
              case 0x1215: 
              case 0x1216: 
              case 0x1217: return "DO Work Pointers";
              case 0x1218: 
              case 0x1219:
              case 0x121A: return "USR Program Jump [7D28]";
              case 0x121B: 
              case 0x121C: 
              case 0x121D: 
              case 0x121E:
              case 0x121F: return "RND Seed Value";
              case 0x1220: return "Degrees Per CIRCLE Segment";
              case 0x1221: return "'Cold' or 'Warm' Reset Status";
              case 0x1222: return "Sound Tempo";
              case 0x1223:
              case 0x1224: return "Remaining Note Length LO/HI, Voice 1";
              case 0x1225:
              case 0x1226: return "Remaining Note Length LO/HI, Voice 2";
              case 0x1227:
              case 0x1228: return "Remaining Note Length LO/HI, Voice 3";
              case 0x1229:
              case 0x122A: return "Note Length LO/HI";
              case 0x122B: return "Octave";
              case 0x122C: return "Flag: 01 = Sharp, FF = Flat";
              case 0x122D:
              case 0x122E: return "Pitch";
              case 0x122F: return "Music Sequencer (Voice Number)";
              case 0x1230: return "Wave";
              case 0x1233: return "Flag: Play Dotted Note";
              case 0x1234: 
              case 0x1235: 
              case 0x1236:
              case 0x1237: return "Note Image";
              case 0x1271:
              case 0x1272:
              case 0x1273:
              case 0x1274: return "Note: xx, xx, volume";
              case 0x1275: return "Previous Volume Image";
              case 0x1276:
              case 0x1277:
              case 0x1278: return "Collision IRQ Task Table";
              case 0x127F: return "Collision Mask";
              case 0x1280: return "Collision Work Value";
              case 0x1281: return "SOUND Voice";
              case 0x1282: return "SOUND Time LO";
              case 0x1285: 
              case 0x1286:
              case 0x1287: return "SOUND Time HI";
              case 0x1288:
              case 0x1289:
              case 0x128A: return "SOUND Max LO";
              case 0x128B: return "SOUND Max HI";
              case 0x128E: return "SOUND Min LO";
              case 0x1291: return "SOUND Min HI";
              case 0x1294: return "SOUND Direction";
              case 0x1297: return "SOUND Step LO";
              case 0x129A: return "SOUND Step HI";
              case 0x129B: return "SOUND Freq LO";
              case 0x12A0: return "SOUND Freq HI";
              case 0x12A3: return "Temp Time LO";
              case 0x12A4: return "Temp Time HI";
              case 0x12A5: return "Temp Max LO";
              case 0x12A6: return "Temp Max HI";
              case 0x12A7: return "Temp Min LO";
              case 0x12A8: return "Temp Min HI";
              case 0x12A9: return "Temp Direction";
              case 0x12AA: return "Temp Step LO";
              case 0x12AB: return "Temp Step HI";
              case 0x12AC: return "Temp Freq LO";
              case 0x12AD: return "Temp Freq HI";
              case 0x12AE: return "Temp Pulse LO";
              case 0x12AF: return "Temp Pulse HI";
              case 0x12B0: return "Temp Waveform";
              case 0x12B1: 
              case 0x12B2: return "PEN/POT Work Values";
              case 0x12B7:
              case 0x12FA: 
              case 0x12FB: return "Used BY SPRDEF & SAVSPR";
              case 0x12FC: return "Sprite Number. Used BY SPRDEF & SAVSPR";
              case 0x12FD: return "Used by BASIC IRQ to block all but one IRQ call";
              
              default:  
                if ((addr>=0x19) && (addr<=0x23)) return "Stack for temporary strings";
                if ((addr>=0x59) && (addr<=0x62)) return "Miscellaneous numeric work area";
                if ((addr>=0x100) && (addr<=0x10F)) return "Tape Read Errors, Area to build filename in (16 bytes)";
                if ((addr>=0x100) && (addr<=0x1FF)) return "System Stack";
                if ((addr>=0x200) && (addr<=0x2A1)) return "BASIC & Monitor input buffer";
                if ((addr>=0x2A2) && (addr<=0x2AE)) return "Bank Peek Subroutine (Kernal RAM)";
                if ((addr>=0x2AF) && (addr<=0x2BD)) return "Bank Poke Subroutine";
                if ((addr>=0x2BE) && (addr<=0x2CC)) return "Bank Compare Subroutine";
                if ((addr>=0x2CD) && (addr<=0x2E2)) return "JSR to Another Bank";
                if ((addr>=0x2E3) && (addr<=0x2FB)) return "JMP to Another Bank";
                if ((addr>=0x34A) && (addr<=0x353)) return "IRQ Keyboard Buffer (10 Bytes)  FF = No key";
                if ((addr>=0x354) && (addr<=0x35D)) return "Bitmap Of TAB Stops (10 Bytes)";
                if ((addr>=0x362) && (addr<=0x36B)) return "Logical File Number Table";
                if ((addr>=0x36C) && (addr<=0x375)) return "Device Number Table";
                if ((addr>=0x376) && (addr<=0x37F)) return "Secondary Addresse Table";
                if ((addr>=0x380) && (addr<=0x39E)) return "CHRGET Subroutine";
                if ((addr>=0x39F) && (addr<=0x3AA)) return "Fetch From RAM Bank 0";
                if ((addr>=0x3AB) && (addr<=0x3B6)) return "Fetch From RAM Bank 1";
                if ((addr>=0x3B7) && (addr<=0x3BF)) return "Index1 Indirect Fetch From RAM Bank 1";
                if ((addr>=0x3C0) && (addr<=0x3C8)) return "Index2 Indirect Fetch From RAM Bank 0";
                if ((addr>=0x3C9) && (addr<=0x3D1)) return "Txtptr Fetch From RAM Bank 0";
                if ((addr>=0x3F0) && (addr<=0x3F6)) return "DMA Link Code";
                if ((addr>=0x400) && (addr<=0x7E7)) return "VIC 40-Column Text Screen";
                if ((addr>=0x7E8) && (addr<=0x7fF)) return "Sprite Identity Pointers For Text Mode";
                if ((addr>=0x800) && (addr<=0x9FF)) return "BASIC Pseudo Stack (gosub and loop addresses and commands)";
                if ((addr>=0xA40) && (addr<=0xA5A)) return "40/80 Pointer Swap (to E0-FA)";
                if ((addr>=0xA60) && (addr<=0xA6D)) return "40/80 Data Swap (0354-0361)";
                if ((addr>=0xB00) && (addr<=0xBBF)) return "Cassette Buffer";
                if ((addr>=0xC00) && (addr<=0xDFF)) return "RS-232 Input, Output Buffers";
                if ((addr>=0xE00) && (addr<=0xFFF)) return "System Sprites (56-63)";
                if ((addr>=0x1000) && (addr<=0x1009)) return "Programmed Key Lenghts";
                if ((addr>=0x100A) && (addr<=0x10FF)) return "Programmed Key Definitions";
                if ((addr>=0x1100) && (addr<=0x1130)) return "DOS Command Staging Area";
                if ((addr>=0x1131) && (addr<=0x116E)) return "Graphics Work Area";
                if ((addr>=0x1178) && (addr<=0x1197)) return "Graphics Index";
                if ((addr>=0x117E) && (addr<=0x11D5)) return "Sprite Motion Tables (8 x 11 bytes)";
                if ((addr>=0x11D6) && (addr<=0x11E5)) return "Sprite X/Y Positions";
                if ((addr>=0x11EE) && (addr<=0x11FF)) return "Unused";
                if ((addr>=0x1239) && (addr<=0x123E)) return "Current Envelope Pattern";
                if ((addr>=0x123F) && (addr<=0x1270)) return "AD(SR) Pattern";
                if ((addr>=0x1249) && (addr<=0x1252)) return "(AD)SR Pattern";
                if ((addr>=0x1253) && (addr<=0x125C)) return "Waveform Pattern";
                if ((addr>=0x125D) && (addr<=0x1266)) return "Pulse Width Lo Pattern";
                if ((addr>=0x1267) && (addr<=0x1270)) return "Pulse Width Hi Pattern";
                if ((addr>=0x1279) && (addr<=0x127E)) return "Collision IRQ Address Tables";
                if ((addr>=0x1800) && (addr<=0x1BFF)) return "Reserved for Key Functions";
                if ((addr>=0x1C00) && (addr<=0x1FF7)) return "Video Color Matrix For Graphics Mode";
                if ((addr>=0x1FF8) && (addr<=0x1FFF)) return "Sprite Identity Pointers For Graphics Mode";
                if ((addr>=0x2000) && (addr<=0x3FFF)) return "Screen Memory For Graphics Mode";
                if ((addr>=0x4000) && (addr<=0xCFFF)) return "BASIC ROM";
            }      
      }
    }  
    return super.dcom(iType, aType, addr, value);       
  }
}
