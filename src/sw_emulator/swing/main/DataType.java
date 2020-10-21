/**
 * @(#)DataTypejava 2020/10/18
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
 * Target machine type for disassembly
 * 
 * @author ice
 */
 public enum DataType {
    NONE {
        @Override
        public char getChar() {
          return ' ';
        }
    },  
     
    BYTE_HEX {
        @Override
        public char getChar() {
          return 'B';
        }
    },
    BYTE_DEC {
        @Override
        public char getChar() {
          return 'D';
        }
    },
    BYTE_BIN {
        @Override
        public char getChar() {
          return 'Y';
        }
    },
    BYTE_CHAR {
        @Override
        public char getChar() {
          return 'R';
        }
    },    
    WORD {
        @Override
        public char getChar() {
          return 'W';
        }
    },
    SWAPPED {
        @Override
        public char getChar() {
          return 'P';
        }
    },  
    TRIBYTE {
        @Override
        public char getChar() {
          return 'E';
        }
    },     
    LONG {
        @Override
        public char getChar() {
          return 'L';
        }
    },
    ADDRESS {
        @Override
        public char getChar() {
          return 'A';
        }
    },
    STACK {
        @Override
        public char getChar() {
          return 'S';
        }
    },
    TEXT {
        @Override
        public char getChar() {
          return 'T';
        }
    },
    NUM_TEXT {
        @Override
        public char getChar() {
          return 'N';
        }
    },
    ZERO_TEXT {
        @Override
        public char getChar() {
          return 'Z';
        }
    },
    HIGH_TEXT {
        @Override
        public char getChar() {
          return 'M';
        }        
    },
    SHIFT_TEXT {
        @Override
        public char getChar() {
          return 'H';
        }        
    },
    SCREEN_TEXT {
        @Override
        public char getChar() {
          return 'C';
        }        
    },
    PETASCII_TEXT {
        @Override
        public char getChar() {
          return 'I';
        }        
    }   
    
    ;
   
    /**
     * Get the char of this data type
     * 
     * @return the char
     */
    public abstract char getChar();
}    

