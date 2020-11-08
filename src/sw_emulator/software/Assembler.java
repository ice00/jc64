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

import java.util.LinkedList;
import sw_emulator.swing.main.Option;

/**
 * Assembler definitions:
 * ->Name
 * ->Label
 * ->Byte
 * ->Word
 * 
 * @author ice
 */
public class Assembler {
   /**
    * Action type
    */ 
   public interface ActionType {
     /**
      * Flush the actual data to the output stream
      * 
      * @param str the output stream
      */   
      void flush(StringBuilder str);
   } 
    
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
   public enum Label implements ActionType {
      NAME,               // xxxx
      NAME_COLON;          // xxxx:
    
      @Override
      public void flush(StringBuilder str) {
      
      }
    }    
   
   /**
    * Line comment
    */
   public enum Comment implements ActionType {
      SEMICOLON,       // ; xxxx
      CSTYLE;          // /* xxx */
    
      @Override
      public void flush(StringBuilder str) {
      
      }
    }  
   
   /**
    * Block comment
    * 
    *  -> ; xxxx
    *  -> /\* xxx *\/
    *  -> if 0 xxx endif
    */
   public enum BlockComment implements ActionType {
      SEMICOLON,       // ; xxxx
      CSTYLE,          // /* xxx */ 
      IF;              // if 0 xxx endif
    
      @Override
      public void flush(StringBuilder str) {
      
      }
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
   public enum Byte implements ActionType {
      DOT_BYTE,           // .byte $xx
      BYTE,               //  byte $xx
      DC_BYTE,            //    dc $xx
      DC_B_BYTE,          //  dc.b $xx
      BYT_BYTE,           //  .byt $xx
      MARK_BYTE,          // !byte $xx 
      EIGHT_BYTE;         //    !8 $xx     
      
      @Override
      public void flush(StringBuilder str) {
      
      }     
   }    
   
   /**
    * Word declaration type
    *  -> .word $xxyy
    *  -> word $xxyy
    *  -> dc.w $xxyy
    *  -> .dbyte $xxyy
    *  -> !word $xxyy
    *  -> !16 $xxyy
    */
   public enum Word implements ActionType {
     DOT_WORD,            //  .word $xxyy
     WORD,                //   word $xxyy
     DC_W_WORD,           //   dc.w $xxyy
     DOT_DBYTE,           // .dbyte $xxyy
     MARK_WORD,           //  !word $xxyy
     SIXTEEN_WORD;        //    !16 $xxyy
     
     @Override
     public void flush(StringBuilder str) {
      
     } 
   }
   
   /**
    * Tribyte declaration type
    */
   public enum Tribyte implements ActionType {

        ;
     @Override
     public void flush(StringBuilder str) {
      
     } 
   }   
   
   /**
    * Long declaration type
    */
   public enum Long implements ActionType  {
        ;      
       @Override
      public void flush(StringBuilder str) {
      
      } 
   }    
   
   /** Fifo list */
   LinkedList<MemoryDasm> list=new LinkedList();
   
   /** Option to use */
   protected static Option option;
   
   /** Assembler label to use */
   protected static Assembler.Label aLabel;
   
   /** Assembler block comment to use */
   protected static Assembler.BlockComment aBlockComment;  
   
   /** Assembler comment to use */
   protected static Assembler.Comment aComment;  
  
   /** Assembler byte type */
   protected static Assembler.Byte aByte;
  
   /** Assembler word type */
   protected static Assembler.Word aWord;
   
   /** Actual type being processed */
   ActionType actualType=null;
   
   
   public static void addCommentBlock(StringBuilder str, MemoryDasm mem) {
     // split by new line
     String[] lines = mem.userBlockComment.split("\\r?\\n");  
     
     switch (aBlockComment) {
       case SEMICOLON:    
         for (String line : lines) {
           if ("".equals(line) || " ".equals(line)) str.append("\n");
           else str.append(";").append(line).append("\n");   
         }                      
         break;         
       case CSTYLE:
         boolean isOpen=false;
         for (String line : lines) {
           if ("".equals(line) || " ".equals(line)) {
             if (isOpen) str.append("/*\n\n");
             else {
               str.append("\n*\\n\n");
               isOpen=false;
             }
           } else {
               if (!isOpen) {
                 isOpen=true;
                 str.append("/*\n");
               }
               str.append(";").append(line).append("\n");
           }   
         }        
         if (!isOpen) str.append("\n*\\n");         
         break;          
       case IF:
         break;
     }     
   }
  
   /**
    * Set the option to use
    * 
    * @param option the option to use
    * @param aLabel the label type to use
    * @param aComment the comment type to use
    * @param aBlockComment the comment type to use
    * @param aByte the byte type to use
    * @param aWord the word type tp use
    * 
    */
   public void setOption(Option option, 
                         Assembler.Label aLabel,  
                         Assembler.Comment aComment, 
                         Assembler.BlockComment aBlockComment,
                         Assembler.Byte aByte, 
                         Assembler.Word aWord) {
     Assembler.option=option;
     Assembler.aLabel=aLabel;
     Assembler.aComment=aComment;
     Assembler.aBlockComment=aBlockComment;
     Assembler.aByte=aByte;
     Assembler.aWord=aWord;
   } 
   
   /**
    * Put the value into the buffer and manage 
    * 
    * @param str the string builder where put result
    * @param mem the memory being processed
    */
   public void putValue(StringBuilder str, MemoryDasm mem) {
     ActionType type=actualType;  
     
     // if there is a block comments use it
     if (mem.userBlockComment!=null && !"".equals(mem.userBlockComment)) {
       type=actualType;
       actualType=aBlockComment;
       flush(str);
       actualType=type;
     }
     
       
     // if this is a label then the type will change  
     if (mem.userLocation!=null && !"".equals(mem.userLocation)) type=aLabel;
     else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) type=aLabel;  
              
     // test if it change type  
     if (type!=actualType) {
       flush(str);              // write back previous data
       actualType=getType(mem);
     }  
          
     
     list.add(mem);
     
     // we are processing bytes?
     if (actualType instanceof Byte) {
       // look if it is time to aggregate data
       if (list.size()==option.maxByteAggregate) actualType.flush(str);         
     } else
     // we are processing word?    
     if (actualType instanceof Word) {
       // look if it is time to aggregate data
       if (list.size()==option.maxWordAggregate*2) actualType.flush(str);         
     } else
     // we are processing tribyte?    
     if (actualType instanceof Tribyte) {
       // look if it is time to aggregate data
       if (list.size()==option.maxTribyteAggregate*3) actualType.flush(str);         
     } else
     // we are processing long?    
     if (actualType instanceof Long) {
       // look if it is time to aggregate data
       if (list.size()==option.maxLongAggregate*4) actualType.flush(str);         
     }         
     
   }
   
   /**
    * Flush the actual data to the output stream
    * 
    * @param str the output stream
    */
   public void flush(StringBuilder str) {
     if (actualType!=null) actualType.flush(str);
   }
   
   /**
    * Get the tyoe for this location
    * @param mem
    * @return 
    */
   private ActionType getType(MemoryDasm mem) {
     if (!mem.isData) return null;
     
     switch (mem.type) {
       case 'B':
       case 'D':
       case 'Y':
       case 'R':
         return aByte;
       case 'W':
         return aWord;
           
     }
     
     // default is of Byte type
     return aByte;
   }   
}
               

