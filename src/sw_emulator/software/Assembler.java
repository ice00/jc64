/**
 * @(#)Assembler 2020/11/01
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
package sw_emulator.software;

/**
 * Assembler definitions:
 * ->Name
 * ->Label
 * 
 * @author ice
 */
public class Assembler {
   /** 
    * Name of the assembler
    */ 
   public enum Name {
     DASM {
       @Override
       public String getName() {
         return "Dasm";
       }
     },     
     TMPX {
       @Override
       public String getName() {
         return "TMPx";
       }
     },     
     CA65 {
       @Override
       public String getName() {
         return "CA65";
       }
     },     
     ACME {
       @Override
       public String getName() {
         return "ACME";
       }
     },     
     KICK {
       @Override
       public String getName() {
         return "KickAssembler";
       }
     };        
    
     /**
      * Get the char of this data type
      * 
      * @return the char
      */
     public abstract String getName();
   }
   
   /**
    * Label declaration type
    *  -> xxxx
    *  -> xxxx:
    */
   public enum Label {
      NAME,               // xxxx
      NAME_COLON          // xxxx:
   }    
   
   /**
    * Byte declaration type
    *  -> .byte $xx
    *  -> byte $xx
    *  -> dc $xx
    *  -> dc.b $xx
    *  -> -byt $xx
    *  -> !byte $xx
    *  -> !8 $xx
    */
   public enum Byte {
      DOT_BYTE,           // .byte $xx
      BYTE,               //  byte $xx
      DC_BYTE,            //    dc $xx
      DC_B_BYTE,          //  dc.b $xx
      BYT_BYTE,           //  .byt $xx
      MARK_BYTE,          // !byte $xx 
      EIGHT_BYTE          //    !8 $xx     
   }    
}
               

