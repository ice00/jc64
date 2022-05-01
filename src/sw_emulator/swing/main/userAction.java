/**
 * @(#)userAction.java 2019/12/01
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

/**
 * Manage a user action
 * 
 * @author ice
 */
public interface userAction {
  /** New action for project */
  public static final int PROJ_NEW = 1;
  
  /** Open action for project */
  public static final int PROJ_OPEN = 2;  
  
  /** Save action for project */
  public static final int PROJ_SAVE = 3;
  
  /** Save as action for project */
  public static final int PROJ_SAVEAS = 4;
  
  /** Close action for project */
  public static final int PROJ_CLOSE = 5;
  
  /** Exit action for application */
  public static final int APP_EXIT = 6; 
  
  /** Configure action for option */
  public static final int OPTION_CONFIGURE = 7; 
  
  /** View project action for option */
  public static final int OPTION_VIEWPRJ = 8; 
  
  /** Disassemble action for source */
  public static final int SOURCE_DISASS = 9; 
  
  /** Export as disassembly action for source */
  public static final int SOURCE_EXPASDIS = 10;   
  
  /** Export as source action for source */
  public static final int SOURCE_EXPASSOURCE = 11;   
  
  /** Clear dasm comment as source action for memory */
  public static final int MEM_CLEARDCOM = 12;    
  
  /** Clear user comment as source action for memory */
  public static final int MEM_CLEARUCOM = 13;     
  
  /** Mark memory of code kind */
  public static final int MEM_MARKCODE = 14;
  
  /** Mark memory of data kind */
  public static final int MEM_MARKDATA = 15;
  
  /** Apply SIDLD of option */
  public static final int OPTION_SIDLD = 16;
  
  /** Help contents */
  public static final int HELP_CONTENTS = 17;
  
  /** License for help */
  public static final int HELP_LICENSE = 18;
  
  /** Credits help */
  public static final int HELP_CREDITS = 19;
  
  /** About help */
  public static final int HELP_ABOUT = 20;
  
  /** Find text in disassembly */
  public static final int SOURCE_FINDD = 21;
  
  /** Find text in source */
  public static final int SOURCE_FINDS = 22;
  
  /** Memory add user comment */
  public static final int MEM_ADDCOMM = 23;
  
  /** Memory add user label */
  public static final int MEM_ADDLABEL = 24;
  
  /** Memory add user block comment */
  public static final int MEM_ADDBLOCK = 25;
  
  /** Memory clear dasm label  */
  public static final int MEM_CLEARDLABEL = 26;
  
  /** Memory assign #<  */
  public static final int MEM_LOW = 28;
  
  /** Memory assign #>  */
  public static final int MEM_HIGH = 29;
  
  /** Find memory address  */
  public static final int SOURCE_FINDA = 30;  
  
  /** Memory table plus */
  public static final int MEM_PLUS = 31; 
  
  /** Memory table minus */
  public static final int MEM_MINUS = 32;   
  
  /** MPR creation option */
  public static final int OPTION_MPR = 33;
  
  /** Memory add user label opcode */
  public static final int MEM_ADDLABELOP = 34;
  
  /** Mark memory of garbage kind */
  public static final int MEM_MARKGARB = 35;
  
  /** Collaborative merge */
  public static final int PROJ_MERGE = 36;
  
  /** Mark memory of byte (hex) data kind */
  public static final int MEM_MARKDATA_B = 37;
  
  /** Mark memory of byte (decimal) data kind */
  public static final int MEM_MARKDATA_D = 38;
  
  /** Mark memory of byte (binary) data kind */
  public static final int MEM_MARKDATA_Y = 39;
  
  /** Mark memory of byte (char) data kind */
  public static final int MEM_MARKDATA_R = 40;
  
  /** Mark memory of word data kind */
  public static final int MEM_MARKDATA_W = 41;  
  
   /** Mark memory of word swapped data kind */
  public static final int MEM_MARKDATA_P = 42;  
  
  /** Mark memory of tribyte data kind */
  public static final int MEM_MARKDATA_E = 43; 
  
  /** Mark memory of long data kind */
  public static final int MEM_MARKDATA_L = 44; 
  
  /** Mark memory of address data kind */
  public static final int MEM_MARKDATA_A = 45;  
  
  /** Mark memory of stack word data kind */
  public static final int MEM_MARKDATA_S = 46;  
  
  /** Mark memory of text data kind */
  public static final int MEM_MARKDATA_T = 47;
  
  /** Mark memory of text with number of chars before data kind */
  public static final int MEM_MARKDATA_N = 48;  
  
  /** Mark memory of text terminated with 0 data kind */
  public static final int MEM_MARKDATA_Z = 49;  
  
  /** Mark memory of text with high bit 1 data kind */
  public static final int MEM_MARKDATA_M = 50;   
  
  /** Mark memory of text shifted and high bit 1 data kind */
  public static final int MEM_MARKDATA_H = 51;  
  
  /** Mark memory of text converted to screen code data kind */
  public static final int MEM_MARKDATA_C = 52;   
  
  /** Mark memory of text converted to petascii code data kind */
  public static final int MEM_MARKDATA_I = 53;   
  
  /** Assemble action for source */
  public static final int SOURCE_ASS = 54; 
  
  /** Mark memory of text converted to monocromatic sprite data kind */
  public static final int MEM_MARKDATA_O = 55;   
  
  /** Mark memory of text converted to multicolor sprite data kind */
  public static final int MEM_MARKDATA_F = 56;  
  
  /** Memory assign #<>  */
  public static final int MEM_LOWHIGH = 57;
  
  /** Memory assign #><  */
  public static final int MEM_HIGHLOW = 58;  
  
  /** Memory sub 0 assigment */
  public static final int MEM_SUB_0 = 59;
  
  /** Memory sub 1 assigment */
  public static final int MEM_SUB_1 = 60;
  
  /** Memory sub 2 assigment */
  public static final int MEM_SUB_2 = 61;
  
  /** Memory sub 3 assigment */
  public static final int MEM_SUB_3 = 62;
  
  /** Memory sub 4 assigment */
  public static final int MEM_SUB_4 = 63;
  
  /** Memory sub 5 assigment */
  public static final int MEM_SUB_5 = 64;
  
  /** Memory sub 6 assigment */
  public static final int MEM_SUB_6 = 65;  
  
  /** Memory sub 7 assigment */
  public static final int MEM_SUB_7 = 66;
  
  /** Memory sub 8 assigment */
  public static final int MEM_SUB_8 = 67;
  
  /** Memory sub 9 assigment */
  public static final int MEM_SUB_9 = 68;
  
  /** Memory sub clear assigment */
  public static final int MEM_SUB_CLEAR = 69;
  
  /** Option labels */
  public static final int OPTION_LABELS = 70;   
    
  /** Memory assign for both  */
  public static final int MEM_BOTH = 71;
  
  /** Export source as dasm format */
  public static final int SOURCE_DASM = 72;
  
  /** Export source as TMPx format */
  public static final int SOURCE_TMPX = 73;
  
  /** Export source as CA65 format */
  public static final int SOURCE_CA65 = 74;
  
  /** Export source as acme format */
  public static final int SOURCE_ACME = 75;
  
  /** Export source as kick assembler format */
  public static final int SOURCE_KICK = 76;
  
  /** Export source as 64 tass format */
  public static final int SOURCE_TASS64 = 77;
  
  /** Wizard memory */
  public static final int MEM_WIZARD=78;
  
  /** Import labels */
  public static final int HELP_IMPORT=79;
  
  /** Refactor labels */
  public static final int HELP_REFACTOR=80;
  
  /** Recent files */
  public static final int PROJ_RECENT=81;
  
  /** Clear automatic label */
  public static final int HELP_CLEARLAB=82;
  
  /** Copy action */
  public static final int APP_COPY=83;
  
  /** Paste action */
  public static final int APP_PASTE=84;
  
  /** Help undo function */
  public static final int HELP_UNDO=85;
  
  /** Clear automatic comment */
  public static final int HELP_CLEARCOM=86;
  
  /** Find hex sequences  */
  public static final int SOURCE_FINDX = 87; 
  
  /** Freeze source */
  public static final int SOURCE_FREEZE = 88;
  
  /** Show hex view */
  public static final int MEM_HEX = 89;
  
  
  /**
   * Execute the passed user action
   * 
   * @param type the type of action to execute
   */
  public abstract void execute(int type);      
}
