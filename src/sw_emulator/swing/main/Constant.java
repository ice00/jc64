/**
 * @(#)Costant.java 2021/04/24
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

import java.util.Arrays;

/**
 * Constant for compiler
 * 
 * @author ice
 */
public class Constant {
  // Allowed chars  
  private static final String allowed="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/*+-_><()";
  private static final String notallowed="/*+-><()";
    
  /** Columns */  
  public static final int COLS=20; 
  
  /** Min Columns */  
  public static final int MIN_COLS=10;  
  
  /** Min Rows */
  public static final int MIN_ROWS=256;
    
  /** Rows */
  public static final int ROWS=0xFFFF+1;
    
  /** String tables of constants  */  
  public String[][] table=new String[COLS][ROWS];

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }

    final Constant other = (Constant) obj;
    if (!Arrays.deepEquals(this.table, other.table)) {
      return false;
    }
    return true;
  }

   @Override
   protected Object clone() {
     Constant res=new Constant();       
     
     for (int i=0; i<COLS; i++) {
       for (int j=0; j<ROWS; j++) {
         res.table[i][j]=table[i][j];
       }     
     }  
     
     return res;
   }   
   
   /**
    * True if the value is allowed (not present or not in reserved words)
    * 
    * @param value
    * @return true if the value is allowed
    */
   public boolean isAllowed(String value) {
     if (value==null || "".equals(value)) return false;
       
     for (int i=0; i<COLS; i++) {
       for (int j=0; j<ROWS; j++) {
         if (value.equals(table[i][j])) return false;
       }     
     }    
     
     return true;
   }
   
   /**
    * True if all values in constants are valid
    * 
    * @param value the vlaue
    * @return true if all correct
    */
   public boolean isCorrect(String value) {
     for (char c: value.toUpperCase().toCharArray()) {
       if (!allowed.contains(""+c)) return false;       
     }  
     return true;
   }
   
   /**
    * True if this is a constant to declare (so not dependig form other)
    * 
    * @param value the value to check
    * @return true if true constant
    */
   public boolean isConstant(String value) {
     if (!Character.isAlphabetic(value.charAt(0))) return false;
     
     for (char c: value.toUpperCase().toCharArray()) {
       if (notallowed.contains(""+c)) return false;       
     } 
     
     return true;  
   }
} 
