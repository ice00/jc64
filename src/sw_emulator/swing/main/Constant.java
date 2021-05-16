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
  /** Columns */  
  public static final int COLS=10;  
  
  /** Rows */
  public static final int ROWS=256;
  
    
  /** String tables of constants  */  
  public String[][] table=new String[10][256];

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
    * @return 
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
}
