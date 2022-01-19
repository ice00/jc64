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
 * Target data type for disassembly
 * 
 * @author ice
 */
 public enum DataType {
    NONE {
        @Override
        public char getChar() {
          return ' ';
        }

        @Override
        public String getDescription() {
          return "Nothing selected";
        }
    },  
     
    BYTE_HEX {
        @Override
        public char getChar() {
          return 'B';
        }

        @Override
        public String getDescription() {
          return "Byte (Hex)";
        }
    },
    BYTE_DEC {
        @Override
        public char getChar() {
          return 'D';
        }

        @Override
        public String getDescription() {
          return "Byte (Decimal)";
        }
    },
    BYTE_BIN {
        @Override
        public char getChar() {
          return 'Y';
        }

        @Override
        public String getDescription() {
          return "Byte (Binary)";
        }
    },
    BYTE_CHAR {
        @Override
        public char getChar() {
          return 'R';
        }

        @Override
        public String getDescription() {
          return "Byte (Character)";
        }
    },    
    WORD {
        @Override
        public char getChar() {
          return 'W';
        }

        @Override
        public String getDescription() {
          return "Word";
        }
    },
    SWAPPED {
        @Override
        public char getChar() {
          return 'P';
        }

        @Override
        public String getDescription() {
          return "Word swapped)";
        }
    },  
    TRIBYTE {
        @Override
        public char getChar() {
          return 'E';
        }

        @Override
        public String getDescription() {
          return "Tribyte";
        }
    },     
    LONG {
        @Override
        public char getChar() {
          return 'L';
        }

        @Override
        public String getDescription() {
          return "Long";
        }
    },
    ADDRESS {
        @Override
        public char getChar() {
          return 'A';
        }

        @Override
        public String getDescription() {
          return "Address";
        }
    },
    STACK {
        @Override
        public char getChar() {
          return 'S';
        }

        @Override
        public String getDescription() {
          return "Stack";
        }
    },
    TEXT {
        @Override
        public char getChar() {
          return 'T';
        }

        @Override
        public String getDescription() {
          return "Text";
        }
    },
    NUM_TEXT {
        @Override
        public char getChar() {
          return 'N';
        }

        @Override
        public String getDescription() {
           return "Text with length";
        }
    },
    ZERO_TEXT {
        @Override
        public char getChar() {
          return 'Z';
        }

        @Override
        public String getDescription() {
          return "Text terminated with zero";
        }
    },
    HIGH_TEXT {
        @Override
        public char getChar() {
          return 'M';
        }        

        @Override
        public String getDescription() {
          return "Text with high bit 1";
        }
    },
    SHIFT_TEXT {
        @Override
        public char getChar() {
          return 'H';
        }        

        @Override
        public String getDescription() {
           return "Text shifted with high bit 1";
        }
    },
    SCREEN_TEXT {
        @Override
        public char getChar() {
          return 'C';
        }        

        @Override
        public String getDescription() {
           return "Text converted to screen code";
        }
    },
    PETASCII_TEXT {
        @Override
        public char getChar() {
          return 'I';
        }        

        @Override
        public String getDescription() {
          return "Text converted to PetAscii";
        }
    }, 
    MONO_SPRITE{
        @Override
        public char getChar() {
          return 'O';
        }        

        @Override
        public String getDescription() {
          return "Monocromatic sprite";
        }
    }, 
    MULTI_SPRITE{
        @Override
        public char getChar() {
          return 'F';
        }        

        @Override
        public String getDescription() {
          return "Multicolor sprite";
        }
    };
    
    /**
     * Get the char of this data type
     * 
     * @return the char
     */
    public abstract char getChar();
    
    /**
     * Get the description of this data type
     * 
     * @return the description
     */
    public abstract String getDescription();
}    

