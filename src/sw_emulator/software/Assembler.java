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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import sw_emulator.math.Unsigned;
import sw_emulator.swing.main.DataType;
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
   private static final String SPACES="                                        ";  
   private static final String TABS="\t\t\t\t\t\t\t\t\t\t";
   
  /**
   * Return spaces/tabs to use in start of data area
   * 
   * @return the spaces/tabs
   */
  protected static String getDataSpacesTabs() {
    return SPACES.substring(0, option.numDataSpaces)+TABS.substring(0, option.numDataTabs);
  } 
   
/**
   * Convert a unsigned byte (containing in a int) to Exe upper case 2 chars
   *
   * @param value the byte value to convert
   * @return the exe string rapresentation of byte
   */
  protected static String ByteToExe(int value) {
    int tmp=value;
    
    if (value<0) return "??";
    
    String ret=Integer.toHexString(tmp);
    if (ret.length()==1) ret="0"+ret;
    return ret.toUpperCase(Locale.ENGLISH);
  }

  /**
   * Convert a unsigned short (containing in a int) to Exe upper case 4 chars
   *
   * @param value the short value to convert
   * @return the exe string rapresentation of byte
   */
  protected static String ShortToExe(int value) {
    int tmp=value;

    if (value<0) return "????";
    
    String ret=Integer.toHexString(tmp);
    int len=ret.length();
    switch (len) {
      case 1:
        ret="000"+ret;
        break;
     case 2:
        ret="00"+ret;
        break;
     case 3:
        ret="0"+ret;
        break;
    }
    return ret.toUpperCase(Locale.ENGLISH);
  }   
  
  /**
   * Convert an 8 bit 0/1 string to multicolor dots
   * @param bin the bin to convert
   * @return the converted peace
   */
  protected static String BinToMulti(String bin) {
    String p0=bin.substring(0, 2).replace("00", "..").replace("11", "**").replace("01", "@@").replace("10", "##");
    String p1=bin.substring(2, 4).replace("00", "..").replace("11", "**").replace("01", "@@").replace("10", "##");
    String p2=bin.substring(4, 6).replace("00", "..").replace("11", "**").replace("01", "@@").replace("10", "##");
    String p3=bin.substring(6, 8).replace("00", "..").replace("11", "**").replace("01", "@@").replace("10", "##");
    
    return p0+p1+p2+p3;
  }
    
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
      
      /** 
       * Setting up the action type if this is the case
       * 
       * @param str the output stream
       */
      default void setting(StringBuilder str) {};
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
     },     
     TASS64 {
       @Override
       public String getName() {
         return "64Tass";
       }
     };        
    
     /**
      * Get the char of this data type
      *  -> ORG $xxyy
      *  -> .ORG $xxyy
      *  -> * = $xxyy
      *  -> .pc $xxyy
      * @return the char
      */
     public abstract String getName();
   }
   
   /**
    * Starting declaration
    *  -> processor 6502
    *  -> cpu = 6502
    *  -> .cpu "6502"
    *  -> .cpu 6502
    *  -> .cpu _6502 
    *  -> .setcpu 6502x
    *  -> .p02
    *  -> !CPU 6510
    */
   public enum Starting implements ActionType {
      PROC,             // processor 6502
      FAKE,             //  cpu = 6502
      DOT_CPU_A,        // .cpu "6502"
      DOT_CPU,          // .cpu 6502
      DOT_CPU_UND,      // .cpu _6502
      DOT_SETCPU,       // .setcpu 6502x
      DOT_P02,          // .p02
      MARK_CPU;         // !cpu 6510
       
       
      @Override
      public void flush(StringBuilder str) {
        switch (aStarting) {
          case PROC:
            str.append(getDataSpacesTabs()).append("processor 6502\n\n");
            break;
          case FAKE:
            str.append(getDataSpacesTabs()).append("cpu = 6502\n\n");
            break;            
          case DOT_CPU_A:
            str.append(getDataSpacesTabs()).append(".cpu \"6502\"\n\n");  
            break;
          case DOT_CPU:
            str.append(getDataSpacesTabs()).append(".cpu 6502\n\n");
            break;
          case DOT_CPU_UND:
            str.append(getDataSpacesTabs()).append(".cpu _6502\n\n");
            break;  
          case DOT_SETCPU:
            str.append(getDataSpacesTabs()).append(".setcpu \"6502x\"\n\n");
            break; 
          case DOT_P02:
            str.append(getDataSpacesTabs()).append(".p02\n\n");
            break;    
          case MARK_CPU:
            str.append(getDataSpacesTabs()).append("!cpu 6510\n\n");
            break;     
        }  
      }
   } 
   
   /**
    * Origin declaration
    *  -> org $xxyy
    *  -> .org $xxyy
    *  -> *=$xxyy
    *  -> .pc $xxyy
    */
   public enum Origin implements ActionType {
      ORG,              //  org $xxyy
      DOT_ORG,          // .org $xxyy
      ASTERISK,         //   *= $xxyy
      DOT_PC;           //  .pc $xxyy
           
      @Override
      public void flush(StringBuilder str) {
        switch (aOrigin) {
          case ORG:
              str.append(getDataSpacesTabs()).append("org $").append(ShortToExe(lastPC)).append("\n\n");
            break;
          case DOT_ORG:
              str.append(getDataSpacesTabs()).append(".org $").append(ShortToExe(lastPC)).append("\n\n");
            break;
          case ASTERISK:
              str.append(getDataSpacesTabs()).append("*=$").append(ShortToExe(lastPC)).append("\n\n");
            break;
          case DOT_PC:
              str.append(getDataSpacesTabs()).append(".pc $").append(ShortToExe(lastPC)).append("\n\n");
            break;
        }    
      }
   }
   
   /**
    * Label declaration type
    *  -> xxxx
    *  -> xxxx:
    */
   public enum Label implements ActionType {
      NAME,               // xxxx
      NAME_COLON;         // xxxx:
    
      @Override
      public void flush(StringBuilder str) {
        // add the label if it was declared by dasm or user   
        String label=null;
        if (lastMem.userLocation!=null && !"".equals(lastMem.userLocation)) label=lastMem.userLocation;
        else if (lastMem.dasmLocation!=null && !"".equals(lastMem.dasmLocation)) label=lastMem.dasmLocation;
          
        switch (aLabel) {
          case NAME:
            str.append(label);  
            break; 
          case NAME_COLON:
            str.append(label).append(":");
            break;
        }
      }
    }    
   
   /**
    * Line comment
    * 
    *  -> ; xxx
    *  -> /* xxx *\/
    *  -> // xxx
    */
   public enum Comment implements ActionType {
      SEMICOLON,       // ; xxxx
      CSTYLE,          // /* xxx */
      DOUBLE_BAR;      // // xxx 
    
      @Override
      public void flush(StringBuilder str) {
        String comment=lastMem.dasmComment;
        if (lastMem.userComment != null && !"".equals(lastMem.userComment)) comment=lastMem.userComment;
        
        if (comment==null || "".equals(comment)) {
          str.append("\n");
          return;
        }
        
        switch (aComment) {
          case SEMICOLON:
            str.append("; ").append(comment).append("\n");
            break;
          case CSTYLE:
            str.append("/* ").append(comment).append(" */\n");  
            break;  
          case DOUBLE_BAR:
            str.append("// ").append(comment).append("\n");
            break;
        }
      }
    }  
   
   /**
    * Block comment
    * 
    *  -> ; xxx
    *  -> // xxx
    *  -> /\* xxx *\/
    *  -> if 0 xxx endif
    *  -> .if 0 xxx .fi
    *  -> .if 0 xxx .endif
    *  -> .if (0) xxx .endif
    *  -> !if 0 { xxx }
    *  -> .comment xxx .endc
    */
   public enum BlockComment implements ActionType {
      SEMICOLON,       // ; xxx
      DOUBLE_BAR,      // // xxx
      CSTYLE,          // /* xxx */ 
      IF,              // if 0 xxx endif
      DOT_IF_FI,       // .if 0 xxx .fi
      DOT_IF,          // .if 0 xxx .endif
      DOT_IF_P,        // .if (0) xxx .endif
      MARK_IF,         // !if 0 { xxx }
      DOT_COMMENT;     // .comment xxx .endc
    
      @Override
      public void flush(StringBuilder str) {
        // split by new line
        String[] lines = lastMem.userBlockComment.split("\\r?\\n");  
     
        switch (aBlockComment) {
          case SEMICOLON:    
            for (String line : lines) {
              if ("".equals(line) || " ".equals(line)) str.append("\n");
              else str.append(";").append(line).append("\n");   
            }                      
            break;  
         case DOUBLE_BAR:    
            for (String line : lines) {
              if ("".equals(line) || " ".equals(line)) str.append("\n");
              else str.append("//").append(line).append("\n");   
            }                      
            break;           
          case CSTYLE:
            boolean isOpen=false;
            for (String line : lines) {
              if ("".equals(line) || " ".equals(line)) {
                if (isOpen) str.append("/*\n\n");
                else {
                 str.append("\n*/\n\n");
                 isOpen=false;
                }
              } else {
                  if (!isOpen) {
                    isOpen=true;
                    str.append("/*\n");
                  }
                  str.append(line).append("\n");
                }   
            }        
            if (isOpen) str.append("*/\n");         
            break;          
          case IF:
            isOpen=false;
            for (String line : lines) {
              if ("".equals(line) || " ".equals(line)) {
                if (isOpen) str.append("endif\n\n");
                else {
                 str.append("if 0\n");
                 isOpen=false;
                }
              } else {
                  if (!isOpen) {
                    isOpen=true;
                    str.append("if 0\n");
                  }
                  str.append(line).append("\n");
                }   
            }        
            if (isOpen) str.append("endif\n");   
            break;
          case DOT_IF:
            isOpen=false;
            for (String line : lines) {
              if ("".equals(line) || " ".equals(line)) {
                if (isOpen) str.append(".endif\n\n");
                else {
                 str.append(".if 0\n");
                 isOpen=false;
                }
              } else {
                  if (!isOpen) {
                    isOpen=true;
                    str.append(".if 0\n");
                  }
                  str.append(line).append("\n");
                }   
            }        
            if (isOpen) str.append(".endif\n");    
            break; 
          case DOT_IF_FI:
            isOpen=false;
            for (String line : lines) {
              if ("".equals(line) || " ".equals(line)) {
                if (isOpen) str.append(".fi\n\n");
                else {
                 str.append(".if 0\n");
                 isOpen=false;
                }
              } else {
                  if (!isOpen) {
                    isOpen=true;
                    str.append(".if 0\n");
                  }
                  str.append(line).append("\n");
                }   
            }        
            if (isOpen) str.append(".fi\n");    
            break;   
          case DOT_IF_P:
            isOpen=false;
            for (String line : lines) {
              if ("".equals(line) || " ".equals(line)) {
                if (isOpen) str.append(".endif\n\n");
                else {
                 str.append(".if (0) {\n");
                 isOpen=false;
                }
              } else {
                  if (!isOpen) {
                    isOpen=true;
                    str.append(".if (0) {\n");
                  }
                  str.append(line).append("\n");
                }   
            }        
            if (isOpen) str.append("}\n");    
            break;             
          case MARK_IF:
            isOpen=false;
            for (String line : lines) {
              if ("".equals(line) || " ".equals(line)) {
                if (isOpen) str.append("}\n\n");
                else {
                 str.append("!if 0 {\n\n");
                 isOpen=false;
                }
              } else {
                  if (!isOpen) {
                    isOpen=true;
                    str.append("!if 0 {\n");
                  }
                  str.append(line).append("\n");
                }   
            }        
            if (isOpen) str.append("}\n");    
            break;  
          case DOT_COMMENT:
            isOpen=false;
            for (String line : lines) {
              if ("".equals(line) || " ".equals(line)) {
                if (isOpen) str.append(".comment\n\n");
                else {
                 str.append(".endc\n\n");
                 isOpen=false;
                }
              } else {
                  if (!isOpen) {
                    isOpen=true;
                    str.append(".comment\n");
                  }
                  str.append(line).append("\n");
                }   
            }        
            if (isOpen) str.append(".endc\n");   
            break;  
        } 
      }
    }    
      
   /**
    * Byte declaration type
    *  -> .byte $xx
    *  -> .char $xx
    *  -> byte $xx
    *  -> dc $xx
    *  -> dc.b $xx
    *  ->  .by $xx
    *  -> -byt $xx
    *  -> !byte $xx
    *  -> !8 $xx
    *  -> !08 $xx
    */
   public enum Byte implements ActionType {
      DOT_BYTE,           // .byte $xx
      DOT_CHAR,           // .char $xx
      BYTE,               //  byte $xx      
      DC_BYTE,            //    dc $xx
      DC_B_BYTE,          //  dc.b $xx
      DOT_BYT_BYTE,       //  .byt $xx
      DOT_BY_BYTE,        //   .by $xx
      MARK_BYTE,          // !byte $xx 
      MARK_BY_BYTE,        //  !by $xx
      EIGHT_BYTE,         //    !8 $xx   
      ZEROEIGHT_BYTE;     //   !08 $xx   
      
      @Override
      public void flush(StringBuilder str) {
        if (list.isEmpty()) return; 
        
        MemoryDasm mem;
        MemoryDasm memRel;
        
        // create starting command according to the kind of byte
        switch (aByte) {
          case DOT_BYTE:
            str.append(getDataSpacesTabs()).append((".byte "));
            break;
          case DOT_CHAR:
            str.append(getDataSpacesTabs()).append((".char "));
            break;  
          case DOT_BY_BYTE:
            str.append(getDataSpacesTabs()).append((".by "));
            break;  
          case BYTE:
            str.append(getDataSpacesTabs()).append(("byte "));
            break;
          case DC_BYTE:
            str.append(getDataSpacesTabs()).append(("dc "));   
            break;
          case DC_B_BYTE:
            str.append(getDataSpacesTabs()).append(("dc.b "));
            break;
          case MARK_BY_BYTE:
            str.append(getDataSpacesTabs()).append(("!by "));  
            break;  
          case DOT_BYT_BYTE:
            str.append(getDataSpacesTabs()).append((".byt "));  
            break;
          case MARK_BYTE:
            str.append(getDataSpacesTabs()).append(("!byte "));   
            break;  
          case EIGHT_BYTE:
            str.append(getDataSpacesTabs()).append(("!8 "));  
            break;  
          case ZEROEIGHT_BYTE:
            str.append(getDataSpacesTabs()).append(("!08 "));  
            break;    
        }
          
        Iterator<MemoryDasm> iter=list.iterator();
        while (iter.hasNext()) {
          // accodate each bytes in the format choosed
          mem=iter.next();
          memRel=listRel.pop();
          
          if (mem.type=='<' || mem.type=='>') {           
            if (memRel.userLocation!=null && !"".equals(memRel.userLocation)) str.append(mem.type).append(memRel.userLocation);
            else if (memRel.dasmLocation!=null && !"".equals(memRel.dasmLocation)) str.append(mem.type).append(memRel.dasmLocation);
                 else str.append(mem.type).append("$").append(ShortToExe(memRel.address));
          } else str.append(getByteType(mem.dataType, mem.copy));
          if (listRel.size()>0) str.append(", ");  
          else str.append("\n");
        }
        list.clear();
      }  
      
      /**
       * Return the byte represented as by the given type
       * 
       * @param dataType the type to use 
       * @param value the byte value
       * @return the converted string
       */
      private String getByteType(DataType dataType, byte value) {
        if (aByte==DOT_CHAR && value<0) {
          switch (dataType)   {
              case BYTE_DEC:
                return "-"+Math.abs(value);
              case BYTE_BIN:
                return "-%"+Integer.toBinaryString((Math.abs(value) & 0xFF) + 0x100).substring(1);
              case BYTE_CHAR:
                //return "\""+(char)Unsigned.done(value)+"\"";
                return "-'"+(char)Math.abs(value);
              case BYTE_HEX:
              default:
                return "-$"+ByteToExe(Math.abs(value));
           }            
        } else {
            switch (dataType)   {
              case BYTE_DEC:
                return ""+Unsigned.done(value);
              case BYTE_BIN:
                return "%"+Integer.toBinaryString((value & 0xFF) + 0x100).substring(1);
              case BYTE_CHAR:
                switch (option.assembler) {
                  case KICK:
                    return "'"+(char)Unsigned.done(value)+"'";      
                  case TASS64:
                    return "\""+(char)Unsigned.done(value)+"\"";
                  default:
                    return "'"+(char)Unsigned.done(value); 
                }  
              case BYTE_HEX:
              default:
                return "$"+ByteToExe(Unsigned.done(value));
           }
        }
      }
   }    
   
   /**
    * Word declaration type
    *  -> .word $xxyy
    *  ->   .wo $xxyy
    *  -> .sint $xxyy
    *  -> word $xxyy
    *  -> dc.w $xxyy
    *  -> .dbyte $xxyy
    *  -> !word $xxyy
    *  -> !16 $xxyy
    */
   public enum Word implements ActionType {
     DOT_WORD,            //  .word $xxyy
     DOT_WO_WORD,         //    .wo $xxyy
     DOT_SINT,            //  .sint $xxyy
     WORD,                //   word $xxyy
     DC_W_WORD,           //   dc.w $xxyy
     DOT_DBYTE,           // .dbyte $xxyy
     MARK_WORD,           //  !word $xxyy
     SIXTEEN_WORD;        //    !16 $xxyy
     
     @Override
     public void flush(StringBuilder str) {         
       if (list.isEmpty()) return; 
       
       MemoryDasm memLow;
       MemoryDasm memHigh;
       MemoryDasm memRelLow;
       MemoryDasm memRelHigh;
     
       int pos1=str.length();  // store initial position
       
       // create starting command according to the kind of byte
       switch (aWord) {
         case DOT_WORD:
           str.append(getDataSpacesTabs()).append((".word "));  
           break;
         case DOT_WO_WORD:
           str.append(getDataSpacesTabs()).append((".wo "));  
           break;  
         case DOT_SINT:
           str.append(getDataSpacesTabs()).append((".sint "));  
           break;           
         case WORD:
           str.append(getDataSpacesTabs()).append(("word "));   
           break;
         case DC_W_WORD:
           str.append(getDataSpacesTabs()).append(("dc.w "));  
           break;
         case DOT_DBYTE:
           str.append(getDataSpacesTabs()).append((".dbyte "));   
           break;
         case MARK_WORD:
           str.append(getDataSpacesTabs()).append(("!word "));   
           break;
         case SIXTEEN_WORD:
           str.append(getDataSpacesTabs()).append(("!16 "));  
           break;  
       }
       
       int pos2=str.length();   // store final position
       boolean isFirst=true;       // true if this is the first output
       
       while (!list.isEmpty()) {
         // if only 1 byte left, use byte coding
         if (list.size()==1) {
           if (isFirst) {
              str.replace(pos1, pos2, "");
              isFirst=false;                    
           }  
           aByte.flush(str);
         }
         else {
           memLow=list.pop();
           memRelLow=listRel.pop();
           memHigh=list.pop();
           memRelHigh=listRel.pop();           
           
           if (memLow.type=='<' && memHigh.type=='>' && memLow.related==memHigh.related) {
             if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation);
             else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation);
                  else str.append("$").append(ShortToExe(memRelLow.address));  
             isFirst=false;
           } else {
               // if cannot make a word with relative locations, force all to be of byte type
               if (memLow.type=='<' || memLow.type=='>' || memHigh.type=='>' || memHigh.type=='<')  {
                 list.addFirst(memHigh);
                 list.addFirst(memLow);
                 listRel.addFirst(memRelHigh);
                 listRel.addFirst(memRelLow);
                 if (isFirst) {
                   str.replace(pos1, pos2, "");
                   isFirst=false;
                 }
                 aByte.flush(str);
               }
               else {
                 if (aWord==DOT_SINT && memHigh.copy<0) str.append("-$").append(ByteToExe(Math.abs(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));
                 else str.append("$").append(ByteToExe(Unsigned.done(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));                            
                 isFirst=false;  
               }    
             }
           if (list.size()>=2) str.append(", ");
           else str.append("\n");
         }
       }
     } 
   }
   
   /**
    * Word swapped declaration type
    *  -> dc.s $yyxx
    *  -> .dtyb $yyxx
    *  -> [.mac] $yyxx
    */
   public enum WordSwapped implements ActionType {
     DC_DOT_S_WORD_SWAPPED,         //    dc.s $yyxx
     DOT_DTYB,                      //   .dtyb $yyxx
     MACRO1_WORD_SWAPPED,           //  [.mac] $yyxx    (KickAssembler)
     MACRO2_WORD_SWAPPED,           //  [.mac] $yyxx    (Acme)
     MACRO4_WORD_SWAPPED,           //  [.mac] $yyxx    (TMPx / Tass64)
        ;

     @Override
     public void flush(StringBuilder str) {
       if (list.isEmpty()) return; 
       
       MemoryDasm memLow;
       MemoryDasm memHigh;
       MemoryDasm memRelLow;
       MemoryDasm memRelHigh;
       
       // we have a min of 1 or a max of 8 word swapped, so use the right call for macro
       int index=(int)(list.size()/2);
       
       int pos1=str.length();      // store initial position
        
       // create starting command according to the kind of byte
       switch (aWordSwapped) {
         case DC_DOT_S_WORD_SWAPPED:
           str.append(getDataSpacesTabs()).append("dc.s ");  
           break;
         case DOT_DTYB:
           str.append(getDataSpacesTabs()).append(".dtyb ");  
           break;           
         case MACRO1_WORD_SWAPPED:
           str.append(getDataSpacesTabs()).append("Swapped").append(index).append("(");   // must close the )
           break;
         case MACRO2_WORD_SWAPPED:
           str.append(getDataSpacesTabs()).append("+Swapped").append(index).append(" ");  
           break;
         case MACRO4_WORD_SWAPPED:
           str.append(getDataSpacesTabs()).append("#Tribyte").append(index).append(" ");    
           break;
       }
       
       int pos2=str.length();      // store final position
       boolean isFirst=true;       // true if this is the first output
       
       while (!list.isEmpty()) {
         // if only 1 byte left, use byte coding
         if (list.size()==1) {
           if (isFirst) {
             str.replace(pos1, pos2, "");
             isFirst=false;                    
           }  
           aByte.flush(str);
         }
         else {
           memLow=list.pop();
           memRelLow=listRel.pop();
           memHigh=list.pop();
           memRelHigh=listRel.pop();           
           
           if (memLow.type=='<' && memHigh.type=='>' && memLow.related==memHigh.related) {
             if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation);
             else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation);
                  else str.append("$").append(ShortToExe(memRelLow.address)); 
             isFirst=false;
           } else {
             // if cannot make a word with relative locations, force all to be of byte type
             if (memLow.type=='<' || memLow.type=='>' || memHigh.type=='>' || memHigh.type=='<')  {
               list.addFirst(memHigh);
               list.addFirst(memLow);
               listRel.addFirst(memRelHigh);
               listRel.addFirst(memRelLow);
               if (isFirst) {
                 str.replace(pos1, pos2, "");
                 isFirst=false;
               }
               aByte.flush(str);
             }
             else {
               str.append("$").append(ByteToExe(Unsigned.done(memLow.copy))).append(ByteToExe(Unsigned.done(memHigh.copy)));
               isFirst=false;
             }                            
           }
           if (list.size()>=2) str.append(", ");
           else if (aWordSwapped==MACRO1_WORD_SWAPPED) str.append(")\n");
                else str.append("\n");
         }
       }
     }

     @Override
     public void setting(StringBuilder str) {
       String spaces=getDataSpacesTabs(); 
         
       switch (aWordSwapped) {
         case MACRO1_WORD_SWAPPED:
           str.append(spaces).append(".macro Swapped1 (twobyte) {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Swapped2 (twobyte, twobyte2) {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Swapped3 (twobyte, twobyte2, twobyte3) {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Swapped4 (twobyte, twobyte2, twobyte3, twobyte4) {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")        
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Swapped5 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5) {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte5 & 255, ( twobyte5 >> 8) & 255\n")                      
              .append(spaces).append("}\n\n")      
              .append(spaces).append(".macro Swapped6 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6) {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte5 & 255, ( twobyte5 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte6 & 255, ( twobyte6 >> 8) & 255\n")                      
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Swapped7 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7) {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte5 & 255, ( twobyte5 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte6 & 255, ( twobyte6 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte7 & 255, ( twobyte7 >> 8) & 255\n")                      
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Swapped8 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7, twobyte8) {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte5 & 255, ( twobyte5 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte6 & 255, ( twobyte6 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte7 & 255, ( twobyte7 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte8 & 255, ( twobyte8 >> 8) & 255\n")                    
              .append(spaces).append("}\n\n");                                  
           break;       
         case MACRO2_WORD_SWAPPED:
           str.append(spaces).append("!macro Swapped1 twobyte {\n")
              .append(spaces).append("   !byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Swapped2 twobyte, twobyte2 {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n")        
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Swapped3 twobyte, twobyte2, twobyte3 {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n")         
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Swapped4 twobyte, twobyte2, twobyte3, twobyte4 {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")          
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Swapped5 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5 {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte5 & 255, ( twobyte5 >> 8) & 255\n")                      
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Swapped6 twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6 {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte5 & 255, ( twobyte5 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte6 & 255, ( twobyte6 >> 8) & 255\n")                      
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Swapped7 twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7 {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte5 & 255, ( twobyte5 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte6 & 255, ( twobyte6 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte7 & 255, ( twobyte7 >> 8) & 255\n")                      
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Swapped8 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7, twobyte8 {\n")
              .append(spaces).append("   .byte twobyte & 255, ( twobyte >> 8) & 255\n")
              .append(spaces).append("   .byte twobyte2 & 255, ( twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte3 & 255, ( twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte4 & 255, ( twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte5 & 255, ( twobyte5 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte6 & 255, ( twobyte6 >> 8) & 255\n") 
              .append(spaces).append("   .byte twobyte7 & 255, ( twobyte7 >> 8) & 255\n")  
              .append(spaces).append("   .byte twobyte8 & 255, ( twobyte8 >> 8) & 255\n")                    
              .append(spaces).append("}\n\n");                                  
           break;            
         case MACRO4_WORD_SWAPPED:
           str.append(
             "Swapped1 .macro \n" +
             "     .byte \\1 & 255, ( \\1 >> 8) & 255\n" +
             "  .endm\n\n"+
             "Swapped2 .macro \n" +
             "     .byte \\1 & 255, ( \\1 >> 8) & 255\n" +
             "     .byte \\2 & 255, ( \\2 >> 8) & 255\n" +        
             "  .endm\n\n"+
             "Swapped3 .macro \n" +
             "     .byte \\1 & 255, ( \\1 >> 8) & 255\n" +
             "     .byte \\2 & 255, ( \\2 >> 8) & 255\n" +   
             "     .byte \\3 & 255, ( \\3 >> 8) & 255\n" +                     
             "  .endm\n\n"+
             "Swapped4 .macro \n" +
             "     .byte \\1 & 255, ( \\1 >> 8) & 255\n" +
             "     .byte \\2 & 255, ( \\2 >> 8) & 255\n" +   
             "     .byte \\3 & 255, ( \\3 >> 8) & 255\n" +     
             "     .byte \\4 & 255, ( \\4 >> 8) & 255\n" +                       
             "  .endm\n\n"+
             "Swapped5 .macro \n" +
             "     .byte \\1 & 255, ( \\1 >> 8) & 255\n" +
             "     .byte \\2 & 255, ( \\2 >> 8) & 255\n" +   
             "     .byte \\3 & 255, ( \\3 >> 8) & 255\n" +     
             "     .byte \\4 & 255, ( \\4 >> 8) & 255\n" + 
             "     .byte \\5 & 255, ( \\5 >> 8) & 255\n" +                     
             "  .endm\n\n"+     
             "Swapped6 .macro \n" +
             "     .byte \\1 & 255, ( \\1 >> 8) & 255\n" +
             "     .byte \\2 & 255, ( \\2 >> 8) & 255\n" +   
             "     .byte \\3 & 255, ( \\3 >> 8) & 255\n" +     
             "     .byte \\4 & 255, ( \\4 >> 8) & 255\n" + 
             "     .byte \\5 & 255, ( \\5 >> 8) & 255\n" +     
             "     .byte \\6 & 255, ( \\6 >> 8) & 255\n" +                      
             "  .endm\n\n"+      
             "Swapped7 .macro \n" +
             "     .byte \\1 & 255, ( \\1 >> 8) & 255\n" +
             "     .byte \\2 & 255, ( \\2 >> 8) & 255\n" +   
             "     .byte \\3 & 255, ( \\3 >> 8) & 255\n" +     
             "     .byte \\4 & 255, ( \\4 >> 8) & 255\n" + 
             "     .byte \\5 & 255, ( \\5 >> 8) & 255\n" +     
             "     .byte \\6 & 255, ( \\6 >> 8) & 255\n" +  
             "     .byte \\7 & 255, ( \\7 >> 8) & 255\n" +         
             "  .endm\n\n"+    
             "Swapped8 .macro \n" +
             "     .byte \\1 & 255, ( \\1 >> 8) & 255\n" +
             "     .byte \\2 & 255, ( \\2 >> 8) & 255\n" +   
             "     .byte \\3 & 255, ( \\3 >> 8) & 255\n" +     
             "     .byte \\4 & 255, ( \\4 >> 8) & 255\n" + 
             "     .byte \\5 & 255, ( \\5 >> 8) & 255\n" +     
             "     .byte \\6 & 255, ( \\6 >> 8) & 255\n" +  
             "     .byte \\7 & 255, ( \\7 >> 8) & 255\n" +  
             "     .byte \\8 & 255, ( \\8 >> 8) & 255\n" +                     
             "  .endm\n\n"                 
           );                                  
           break;                                                       
          }
        }       
   }
   
   /**
    * Tribyte declaration type
    */
   public enum Tribyte implements ActionType {
     MACRO_TRIBYTE,            //  [.mac] $xxyyzz    (DASM)
     MACRO1_TRIBYTE,           //  [.mac] $xxyyzz    (KickAssembler)
     MACRO3_TRIBYTE,           //  [.mac] $xxyyzz    (CA65) 
     MACRO4_TRIBYTE,           //  [.mac] $xxyyzz    (TMPx)
     DOT_LINT_TRIBYTE,         //   .lint $xxyyzz
     DOT_LONG_TRIBYTE,         //   .long $xxyyzz
     MARK_TWENTYFOUR_TRIBYTE   //     !24 $xxyyzz
     ;
     @Override
     public void flush(StringBuilder str) {
       if (list.isEmpty()) return; 
       
       if (list.size()<=2) {
         aByte.flush(str);
         return;
       }
       
       MemoryDasm mem;
       
       Iterator<MemoryDasm> iter=list.iterator();
       while (iter.hasNext()) {
         mem=iter.next();
         // we cannot handle memory reference inside tribyte
         if (mem.type=='<' || mem.type=='>') {
           // force all to be as byte even if this breaks layout
           aByte.flush(str);
           return;
         }
       }              
       
       // we have a min of 1 or a max of 8 tribyte, so use the right call for macro
       int index=(int)(list.size()/3);
        
       // create starting command according to the kind of byte
       switch (aTribyte) {
         case MACRO_TRIBYTE:
         case MACRO3_TRIBYTE:    
           str.append(getDataSpacesTabs()).append("Tribyte").append(index).append(" ");  
           break;
         case MACRO1_TRIBYTE:
           str.append(getDataSpacesTabs()).append("Tribyte").append(index).append("(");   // must close the )
           break;           
         case MACRO4_TRIBYTE:
           str.append(getDataSpacesTabs()).append("#Tribyte").append(index).append(" ");  
           break;
         case DOT_LINT_TRIBYTE:
           str.append(getDataSpacesTabs()).append((".lint "));   
           break;
         case DOT_LONG_TRIBYTE:
           str.append(getDataSpacesTabs()).append((".long "));   
           break;
         case MARK_TWENTYFOUR_TRIBYTE:
           str.append(getDataSpacesTabs()).append(("!24 "));  
           break;  
       }
       
       MemoryDasm mem1;
       MemoryDasm mem2;
       MemoryDasm mem3;
       
       while (!list.isEmpty()) {
         // if only 1 or 2 bytes left, use byte coding
         if (list.size()<=2) aByte.flush(str);
         else {
           mem1=list.pop();
           mem2=list.pop();
           mem3=list.pop();
           listRel.pop();
           listRel.pop();
           listRel.pop();
           if (aTribyte==DOT_LINT_TRIBYTE && mem1.copy<0) {
              str.append("-$").append(ByteToExe(Math.abs(mem1.copy)))
                              .append(ByteToExe(Unsigned.done(mem2.copy)))
                              .append(ByteToExe(Unsigned.done(mem3.copy)));;
           } else {
               str.append("$").append(ByteToExe(Unsigned.done(mem1.copy)))
                              .append(ByteToExe(Unsigned.done(mem2.copy)))
                              .append(ByteToExe(Unsigned.done(mem3.copy)));
           }
           if (list.size()>=3) str.append(", ");
           else if (aTribyte==MACRO1_TRIBYTE) str.append(")\n");
                else str.append("\n");
         }
       }   
     } 
     
     /** 
      * Setting up the action type if this is the case
      * 
      * @param str the output stream
      */
     @Override
     public void setting(StringBuilder str) {
       String spaces=getDataSpacesTabs(); 
       
       switch (aTribyte) {
         case MACRO_TRIBYTE:
           str.append(spaces).append(".mac Tribyte1 \n")
              .append(spaces).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Tribyte2 \n")
              .append(spaces).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(spaces).append("  .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Tribyte3 \n")
              .append(spaces).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(spaces).append("  .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n")
              .append(spaces).append("  .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Tribyte4 \n")
              .append(spaces).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(spaces).append("  .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n")
              .append(spaces).append("  .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n")
              .append(spaces).append("  .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n")
              .append(spaces).append(" .endm \n\n")
              .append(spaces).append(".mac Tribyte5 \n")
              .append(spaces).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(spaces).append("  .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n")
              .append(spaces).append("  .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n")
              .append(spaces).append("  .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n")
              .append(spaces).append("  .byte {5} >> 16, ( {5} >> 8) & 255,  {5} & 255\n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Tribyte6 \n")
              .append(spaces).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(spaces).append("  .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n")
              .append(spaces).append("  .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n")
              .append(spaces).append("  .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n")
              .append(spaces).append("  .byte {5} >> 16, ( {5} >> 8) & 255,  {5} & 255\n")
              .append(spaces).append("  .byte {6} >> 16, ( {6} >> 8) & 255,  {6} & 255\n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Tribyte7 \n")
              .append(spaces).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(spaces).append("  .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n") 
              .append(spaces).append("  .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n")
              .append(spaces).append("  .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n")
              .append(spaces).append("  .byte {5} >> 16, ( {5} >> 8) & 255,  {5} & 255\n")
              .append(spaces).append("  .byte {6} >> 16, ( {6} >> 8) & 255,  {6} & 255\n")
              .append(spaces).append("  .byte {7} >> 16, ( {7} >> 8) & 255,  {7} & 255\n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Tribyte8 \n")
              .append(spaces).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(spaces).append("  .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n")
              .append(spaces).append("  .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n")
              .append(spaces).append("  .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n")
              .append(spaces).append("  .byte {5} >> 16, ( {5} >> 8) & 255,  {5} & 255\n")
              .append(spaces).append("  .byte {6} >> 16, ( {6} >> 8) & 255,  {6} & 255\n")
              .append(spaces).append("  .byte {7} >> 16, ( {7} >> 8) & 255,  {7} & 255\n")    
              .append(spaces).append("  .byte {8} >> 16, ( {8} >> 8) & 255,  {8} & 255\n")
              .append(spaces).append(".endm \n\n");
           break;
         case MACRO1_TRIBYTE:
           str.append(spaces).append(".macro Tribyte1 (tribyte) {\n")
              .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Tribyte2 (tribyte, tribyte2) {\n")
              .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Tribyte3 (tribyte, tribyte2, tribyte3) {\n")
              .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
              .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Tribyte4 (tribyte, tribyte2, tribyte3, tribyte4) {\n")
              .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
              .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n")
              .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")          
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Tribyte5 (tribyte, tribyte2, tribyte3, tribyte4, tribyte5) {\n")
              .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
              .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n")       
              .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")  
              .append(spaces).append("  .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n")         
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Tribyte6 (tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6) {\n")
              .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
              .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n")
              .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")  
              .append(spaces).append("  .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n")  
              .append(spaces).append("  .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n")                      
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Tribyte7 (tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6, tribyte7) {\n")
              .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
              .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n")       
              .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")  
              .append(spaces).append("  .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n")  
              .append(spaces).append("  .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n")                      
              .append(spaces).append("  .byte tribyte7 >> 16, ( tribyte7 >> 8) & 255,  tribyte7 & 255\n")           
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Tribyte8 (tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6, tribyte7, tribyte8) {\n")
              .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
              .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n")
              .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")
              .append(spaces).append("  .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n")
              .append(spaces).append("  .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n")
              .append(spaces).append("  .byte tribyte7 >> 16, ( tribyte7 >> 8) & 255,  tribyte7 & 255\n")
              .append(spaces).append("  .byte tribyte8 >> 16, ( tribyte8 >> 8) & 255,  tribyte8 & 255\n")
              .append(spaces).append("}\n\n");               
           break;       
         case MACRO3_TRIBYTE:
            str.append(spaces).append(".macro Tribyte1 tribyte \n")
               .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Tribyte1 tribyte, tribyte2 \n")
               .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
               .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Tribyte3 tribyte, tribyte2, tribyte3 \n")
               .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
               .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
               .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n")          
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Tribyte4 tribyte, tribyte2, tribyte3, tribyte4 \n")
               .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
               .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
               .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n") 
               .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")        
               .append(spaces).append(".endmacro\n\n")                       
               .append(spaces).append(".macro Tribyte5 tribyte, tribyte2, tribyte3, tribyte4, tribyte5 \n")
               .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
               .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
               .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n")
               .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")
               .append(spaces).append("  .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n")
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Tribyte6 tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6 \n")
               .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
               .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
               .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n")
               .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")
               .append(spaces).append("  .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n")  
               .append(spaces).append("  .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n")      
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Tribyte7 tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6, tribyte7 \n")
               .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
               .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
               .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n") 
               .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")   
               .append(spaces).append("  .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n")    
               .append(spaces).append("  .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n")  
               .append(spaces).append("  .byte tribyte7 >> 16, ( tribyte7 >> 8) & 255,  tribyte7 & 255\n")    
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Tribyte8 tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6, tribyte7, tribyte8 \n")
               .append(spaces).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
               .append(spaces).append("  .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n")
               .append(spaces).append("  .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n") 
               .append(spaces).append("  .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n")   
               .append(spaces).append("  .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n")    
               .append(spaces).append("  .byte tribyte7 >> 16, ( tribyte7 >> 8) & 255,  tribyte7 & 255\n") 
               .append(spaces).append("  .byte tribyte8 >> 16, ( tribyte8 >> 8) & 255,  tribyte8 & 255\n")     
               .append(spaces).append(".endmacro\n\n");               
           break;   
         case MACRO4_TRIBYTE:         
           str.append(
             "Tribyte1 .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "  .endm\n\n"+
             "Tribyte2 .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 16, ( \\2 >> 8) & 255,  \\2 & 255\n" +        
             "  .endm\n\n"+     
             "Tribyte3 .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 16, ( \\2 >> 8) & 255,  \\2 & 255\n" +     
             "     .byte \\3 >> 16, ( \\3 >> 8) & 255,  \\3 & 255\n" +          
             "  .endm\n\n"+   
             "Tribyte4 .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 16, ( \\2 >> 8) & 255,  \\2 & 255\n" +     
             "     .byte \\3 >> 16, ( \\3 >> 8) & 255,  \\3 & 255\n" +
             "     .byte \\4 >> 16, ( \\4 >> 8) & 255,  \\4 & 255\n" +        
             "  .endm\n\n"+       
             "Tribyte5 .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 16, ( \\2 >> 8) & 255,  \\2 & 255\n" +     
             "     .byte \\3 >> 16, ( \\3 >> 8) & 255,  \\3 & 255\n" +
             "     .byte \\4 >> 16, ( \\4 >> 8) & 255,  \\4 & 255\n" + 
             "     .byte \\5 >> 16, ( \\5 >> 8) & 255,  \\5 & 255\n" +         
             "  .endm\n\n"+    
             "Tribyte6 .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 16, ( \\2 >> 8) & 255,  \\2 & 255\n" +     
             "     .byte \\3 >> 16, ( \\3 >> 8) & 255,  \\3 & 255\n" +
             "     .byte \\4 >> 16, ( \\4 >> 8) & 255,  \\4 & 255\n" + 
             "     .byte \\5 >> 16, ( \\5 >> 8) & 255,  \\5 & 255\n" + 
             "     .byte \\6 >> 16, ( \\6 >> 8) & 255,  \\6 & 255\n" + 
             "  .endm\n\n"+   
             "Tribyte7 .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 16, ( \\2 >> 8) & 255,  \\2 & 255\n" +     
             "     .byte \\3 >> 16, ( \\3 >> 8) & 255,  \\3 & 255\n" +
             "     .byte \\4 >> 16, ( \\4 >> 8) & 255,  \\4 & 255\n" + 
             "     .byte \\5 >> 16, ( \\5 >> 8) & 255,  \\5 & 255\n" + 
             "     .byte \\6 >> 16, ( \\6 >> 8) & 255,  \\6 & 255\n" +
             "     .byte \\7 >> 16, ( \\7 >> 8) & 255,  \\7 & 255\n" +        
             "  .endm\n\n"+    
             "Tribyte8 .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 16, ( \\2 >> 8) & 255,  \\2 & 255\n" +     
             "     .byte \\3 >> 16, ( \\3 >> 8) & 255,  \\3 & 255\n" +
             "     .byte \\4 >> 16, ( \\4 >> 8) & 255,  \\4 & 255\n" + 
             "     .byte \\5 >> 16, ( \\5 >> 8) & 255,  \\5 & 255\n" + 
             "     .byte \\6 >> 16, ( \\6 >> 8) & 255,  \\6 & 255\n" +
             "     .byte \\7 >> 16, ( \\7 >> 8) & 255,  \\7 & 255\n" +  
             "     .byte \\8 >> 16, ( \\8 >> 8) & 255,  \\8 & 255\n" +       
             "  .endm\n\n"                    
           );                       
           break;             
       }
     };     
   }   
   
   /**
    * Long declaration type
    */
   public enum Long implements ActionType  {
     LONG,                 //   long $xxyyzzkk
     DOT_LONG,             //  .long $xxyyzzkk
     DOT_DW_LONG,          //    .dw $xxyyzzkk         
     DOT_DC_L_LONG,        //  .dc.l $xxyyzzkk    
     DOT_DWORD_LONG,       // .dword $xxyyzzkk
     DOT_DLINT_LONG,       // .dlint $xxyyzzkk
     MARK_THIRTYTWO_LONG,  //    !32 $xxyyzzkk
     MACRO4_LONG           // [.mac] $xxyyzzkk  (TMPx)
        ;      
     @Override
     public void flush(StringBuilder str) {
       if (list.isEmpty()) return; 
       
       if (list.size()<=3) {
         aByte.flush(str);
         return;
       }
       
       MemoryDasm mem;
       
       Iterator<MemoryDasm> iter=list.iterator();
       while (iter.hasNext()) {
         mem=iter.next();
         // we cannot handle memory reference inside long
         if (mem.type=='<' || mem.type=='>') {
           // force all to be as byte even if this breaks layout
           aByte.flush(str);
           return;
         }
       }  
                    
       switch (aLong) {
         case LONG:
           str.append(getDataSpacesTabs()).append("long ");  
           break;             
         case DOT_LONG:
           str.append(getDataSpacesTabs()).append(".long ");   
           break;    
         case DOT_DC_L_LONG:
           str.append(getDataSpacesTabs()).append(".dc.l ");   
           break;  
         case DOT_DWORD_LONG:
           str.append(getDataSpacesTabs()).append(".dword ");  
           break;  
         case DOT_DW_LONG:
           str.append(getDataSpacesTabs()).append(".dw ");  
           break;    
         case DOT_DLINT_LONG:
           str.append(getDataSpacesTabs()).append(".dlint ");  
           break;            
         case MARK_THIRTYTWO_LONG:
           str.append(getDataSpacesTabs()).append("!32 ");  
           break;                                  
         case MACRO4_LONG: 
           // we have a min of 1 or a max of 8 tribyte, so use the right call for macro
           int index=(int)(list.size()/4);
        
           str.append(getDataSpacesTabs()).append("#Long").append(index).append(" ");  
           break;  
       }
        
       MemoryDasm mem1;
       MemoryDasm mem2;
       MemoryDasm mem3;
       MemoryDasm mem4;
       
       while (!list.isEmpty()) {
         // if only 1..3 bytes left, use byte coding
         if (list.size()<=3) aByte.flush(str);
         else {
           mem1=list.pop();
           mem2=list.pop();
           mem3=list.pop();
           mem4=list.pop();
           listRel.pop();
           listRel.pop();
           listRel.pop();
           listRel.pop();
           if (aLong==DOT_DLINT_LONG && mem1.copy<0) {
              str.append("-$").append(ByteToExe(Math.abs(mem1.copy)))
                              .append(ByteToExe(Unsigned.done(mem2.copy)))
                              .append(ByteToExe(Unsigned.done(mem3.copy)))
                              .append(ByteToExe(Unsigned.done(mem4.copy)));
           } else {
               str.append("$").append(ByteToExe(Unsigned.done(mem1.copy)))
                              .append(ByteToExe(Unsigned.done(mem2.copy)))
                              .append(ByteToExe(Unsigned.done(mem3.copy)))
                              .append(ByteToExe(Unsigned.done(mem4.copy)));
           }
           if (list.size()>=4) str.append(", ");
           else str.append("\n");
         }
       }   
      } 
      
     /** 
      * Setting up the action type if this is the case
      * 
      * @param str the output stream
      */
     @Override
     public void setting(StringBuilder str) {        
       switch (aLong) {  
         case MACRO4_LONG:         
           str.append(
             "Long1 .macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "  .endm\n\n"+
             "Long2 .macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 24, (\\2 >> 16) & 255, ( \\2 >> 8) & 255,  \\2 & 255\n" +       
             "  .endm\n\n"+   
             "Long3 .macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 24, (\\2 >> 16) & 255, ( \\2 >> 8) & 255,  \\2 & 255\n" +  
             "     .byte \\3 >> 24, (\\3 >> 16) & 255, ( \\3 >> 8) & 255,  \\3 & 255\n" +          
             "  .endm\n\n"+    
             "Long4 .macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 24, (\\2 >> 16) & 255, ( \\2 >> 8) & 255,  \\2 & 255\n" +  
             "     .byte \\3 >> 24, (\\3 >> 16) & 255, ( \\3 >> 8) & 255,  \\3 & 255\n" +      
             "     .byte \\4 >> 24, (\\4 >> 16) & 255, ( \\4 >> 8) & 255,  \\4 & 255\n" +         
             "  .endm\n\n"+     
             "Long5 .macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 24, (\\2 >> 16) & 255, ( \\2 >> 8) & 255,  \\2 & 255\n" +  
             "     .byte \\3 >> 24, (\\3 >> 16) & 255, ( \\3 >> 8) & 255,  \\3 & 255\n" +      
             "     .byte \\4 >> 24, (\\4 >> 16) & 255, ( \\4 >> 8) & 255,  \\4 & 255\n" +    
             "     .byte \\5 >> 24, (\\5 >> 16) & 255, ( \\5 >> 8) & 255,  \\5 & 255\n" +         
             "  .endm\n\n"+      
             "Long6 .macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 24, (\\2 >> 16) & 255, ( \\2 >> 8) & 255,  \\2 & 255\n" +  
             "     .byte \\3 >> 24, (\\3 >> 16) & 255, ( \\3 >> 8) & 255,  \\3 & 255\n" +      
             "     .byte \\4 >> 24, (\\4 >> 16) & 255, ( \\4 >> 8) & 255,  \\4 & 255\n" +    
             "     .byte \\5 >> 24, (\\5 >> 16) & 255, ( \\5 >> 8) & 255,  \\5 & 255\n" + 
             "     .byte \\6 >> 24, (\\6 >> 16) & 255, ( \\6 >> 8) & 255,  \\6 & 255\n" +         
             "  .endm\n\n"+  
             "Long7 .macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 24, (\\2 >> 16) & 255, ( \\2 >> 8) & 255,  \\2 & 255\n" +  
             "     .byte \\3 >> 24, (\\3 >> 16) & 255, ( \\3 >> 8) & 255,  \\3 & 255\n" +      
             "     .byte \\4 >> 24, (\\4 >> 16) & 255, ( \\4 >> 8) & 255,  \\4 & 255\n" +    
             "     .byte \\5 >> 24, (\\5 >> 16) & 255, ( \\5 >> 8) & 255,  \\5 & 255\n" + 
             "     .byte \\6 >> 24, (\\6 >> 16) & 255, ( \\6 >> 8) & 255,  \\6 & 255\n" +        
             "     .byte \\7 >> 24, (\\7 >> 16) & 255, ( \\7 >> 8) & 255,  \\7 & 255\n" +           
             "  .endm\n\n"+    
             "Long8 .macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 24, (\\2 >> 16) & 255, ( \\2 >> 8) & 255,  \\2 & 255\n" +  
             "     .byte \\3 >> 24, (\\3 >> 16) & 255, ( \\3 >> 8) & 255,  \\3 & 255\n" +      
             "     .byte \\4 >> 24, (\\4 >> 16) & 255, ( \\4 >> 8) & 255,  \\4 & 255\n" +    
             "     .byte \\5 >> 24, (\\5 >> 16) & 255, ( \\5 >> 8) & 255,  \\5 & 255\n" + 
             "     .byte \\6 >> 24, (\\6 >> 16) & 255, ( \\6 >> 8) & 255,  \\6 & 255\n" +        
             "     .byte \\7 >> 24, (\\7 >> 16) & 255, ( \\7 >> 8) & 255,  \\7 & 255\n" +  
             "     .byte \\8 >> 24, (\\8 >> 16) & 255, ( \\8 >> 8) & 255,  \\8 & 255\n" +         
             "  .endm\n\n"                  
           );
           break;
       }
     }
   }    
   
   /**
    * Address declaration type
    *  -> .word $xxyy
    *  ->   .wo $xxyy
    *  -> word $xxyy
    *  -> dc.w $xxyy
    *  -> .addr $xxyy
    *  -> !word $xxyy
    *  -> !16 $xxyy
    */
   public enum Address implements ActionType {
     DOT_WORD_ADDR,            //  .word $xxyy
     DOT_WO_WORD_ADDR,         //    .wo $xxyy
     WORD_ADDR,                //   word $xxyy
     DOT_ADDR_ADDR,            //  .addr $xxyy
     DC_W_ADDR,                //   dc.w $xxyy
     MARK_WORD_ADDR,           //  !word $xxyy
     SIXTEEN_WORD_ADDR;        //    !16 $xxyy//    !16 $xxyy
     
     @Override
     public void flush(StringBuilder str) {         
       if (list.isEmpty()) return; 
       
       MemoryDasm memLow;
       MemoryDasm memHigh;
       MemoryDasm memRelLow;
       MemoryDasm memRelHigh;
     
       int pos1=str.length();  // store initial position
       
       // create starting command according to the kind of byte
       switch (aAddress) {
         case DOT_WORD_ADDR:
           str.append(getDataSpacesTabs()).append((".word "));  
           break;
         case DOT_WO_WORD_ADDR:
           str.append(getDataSpacesTabs()).append((".wo "));  
           break;           
         case WORD_ADDR:
           str.append(getDataSpacesTabs()).append(("word "));   
           break;
         case DC_W_ADDR:
           str.append(getDataSpacesTabs()).append(("dc.w "));  
           break;
         case DOT_ADDR_ADDR:
           str.append(getDataSpacesTabs()).append((".addr "));   
           break;
         case MARK_WORD_ADDR:
           str.append(getDataSpacesTabs()).append(("!word "));   
           break;
         case SIXTEEN_WORD_ADDR:
           str.append(getDataSpacesTabs()).append(("!16 "));  
           break;  
       }
       
       int pos2=str.length();   // store final position
       boolean isFirst=true;       // true if this is the first output
       
       while (!list.isEmpty()) {
         // if only 1 byte left, use byte coding
         if (list.size()==1) {
           if (isFirst) {
              str.replace(pos1, pos2, "");
              isFirst=false;                    
           }  
           aByte.flush(str);
         }
         else {
           memLow=list.pop();
           memRelLow=listRel.pop();
           memHigh=list.pop();
           memRelHigh=listRel.pop();           
           
           if (memLow.type=='<' && memHigh.type=='>' && memLow.related==memHigh.related) {
             if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation);
             else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation);
                  else str.append("$").append(ShortToExe(memRelLow.address));  
             isFirst=false;
           } else {
               // if cannot make a word with relative locations, force all to be of byte type
               if (memLow.type=='<' || memLow.type=='>' || memHigh.type=='>' || memHigh.type=='<')  {
                 list.addFirst(memHigh);
                 list.addFirst(memLow);
                 listRel.addFirst(memRelHigh);
                 listRel.addFirst(memRelLow);
                 if (isFirst) {
                   str.replace(pos1, pos2, "");
                   isFirst=false;
                 }
                 aByte.flush(str);
               }
               else {
                 str.append("$").append(ByteToExe(Unsigned.done(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));                            
                 isFirst=false;  
               }    
             }
           if (list.size()>=2) str.append(", ");
           else str.append("\n");
         }
       }
     } 
   }   
   
   /**
    * Mono sprite declaration type
    * 
    * -> [byte] $xx.
    * -> [byte] %b..
    * -> [.mac] %xx..
    * -> [.mac] %b..
    * -> [.macro] %xx..
    * -> [.macro] %b..     
    */ 
   public enum MonoSprite implements ActionType {
     BYTE_HEX,      // [byte] $xx..
     BYTE_BIN,      // [byte] %b..
     TWENTYFOUR_HEX,// !24 $xx..
     TWENTYFOUR_BIN,// !24 %b..      
     MACRO_HEX,     // [.mac] %xx..   (Dasm)
     MACRO_BIN,     // [.mac] %b..    (Dasm)
     MACRO1_HEX,    // [.macro] %xx.. (Kick assembler)
     MACRO1_BIN,    // [.macro] %b..  (Kick assembler)
     MACRO2_HEX,    // [.macro] %xx.. (Acme)
     MACRO2_BIN,    // [.macro] %b..  (Acme) 
     MACRO3_HEX,    // [.macro] %xx.. (CA65)
     MACRO3_BIN,    // [.macro] %b..  (CA65)
     MACRO4_HEX,    // [.macro] %xx.. (TMPx)
     MACRO4_BIN,    // [.macro] %b..  (TMPx)
     ;      
     
      @Override
      public void flush(StringBuilder str) {
        if (list.isEmpty()) return; 
        
        // we must receive a list of 3 or 1 final byte (if 2, uses as bytes)
        if (list.size()>=3) {    
          MemoryDasm mem1=list.pop(); 
          MemoryDasm mem2=list.pop();
          MemoryDasm mem3=list.pop(); 
          StringBuilder tmp;
          String tmpS;
           
          // add a dasm comment with pixels
          mem3.dasmComment=Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1).replace("0", ".").replace("1", "*")+
                           Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1).replace("0", ".").replace("1", "*")+
                           Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1).replace("0", ".").replace("1", "*");
          lastMem=mem3;
          
          // now we have one row of 3 bytes
          switch (aMonoSprite) {
            case BYTE_HEX:
              mem1=mem1.clone();
              mem2=mem2.clone();
              mem3=mem3.clone();
              mem1.dataType=DataType.BYTE_HEX;
              mem2.dataType=DataType.BYTE_HEX;
              mem3.dataType=DataType.BYTE_HEX;
              list.push(mem3);
              list.push(mem2);
              list.push(mem1);
              tmp=new StringBuilder();
              aByte.flush(tmp);  
              tmpS=tmp.toString();
              str.append(tmpS.substring(0, tmpS.length()-1)).append("  ");
              break;
            case BYTE_BIN:
              mem1=mem1.clone();
              mem2=mem2.clone();
              mem3=mem3.clone();
              mem1.dataType=DataType.BYTE_BIN;
              mem2.dataType=DataType.BYTE_BIN;
              mem3.dataType=DataType.BYTE_BIN;
              list.push(mem3);
              list.push(mem2);
              list.push(mem1);
              tmp=new StringBuilder();
              aByte.flush(tmp); 
              tmpS=tmp.toString();
              str.append(tmpS.substring(0, tmpS.length()-1)).append("  ");
              break;
            case TWENTYFOUR_HEX:
              str.append(getDataSpacesTabs()).append("!24 $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case TWENTYFOUR_BIN:
              str.append(getDataSpacesTabs()).append("!24 %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;                
            case MACRO_HEX:
            case MACRO3_HEX:    
              str.append(getDataSpacesTabs())
                 .append("MonoSpriteLine $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO_BIN:
            case MACRO3_BIN:    
              str.append(getDataSpacesTabs())
                 .append("MonoSpriteLine %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;  
            case MACRO1_HEX:
              str.append(getDataSpacesTabs())
                 .append((option.kickColonMacro ? ":":""))
                 .append("MonoSpriteLine($")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append(")  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO1_BIN:
              str.append(getDataSpacesTabs())
                 .append((option.kickColonMacro ? ":":""))     
                 .append("MonoSpriteLine(%")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append(")  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break; 
            case MACRO2_HEX:
              str.append(getDataSpacesTabs()).append("+MonoSpriteLine $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO2_BIN:
              str.append(getDataSpacesTabs()).append("+MonoSpriteLine %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;    
            case MACRO4_HEX:
              str.append(getDataSpacesTabs()).append("#MonoSpriteLine $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO4_BIN:
              str.append(getDataSpacesTabs()).append("#MonoSpriteLine %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;               
            }
          aComment.flush(str);
        } else {
            // force to be as byte
            aByte.flush(str);
          }            
      }   
      
     /** 
      * Setting up the action type if this is the case
      * 
      * @param str the output stream
      */
     @Override
     public void setting(StringBuilder str) {
       switch (aMonoSprite) {
         case MACRO_HEX:
         case MACRO_BIN:
           str.append(getDataSpacesTabs()).append(".mac MonoSpriteLine \n")
              .append(getDataSpacesTabs()).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(getDataSpacesTabs()).append(".endm \n\n");          
           break;
         case MACRO1_HEX:
         case MACRO1_BIN:
           str.append(getDataSpacesTabs()).append(".macro MonoSpriteLine (tribyte) {\n")
              .append(getDataSpacesTabs()).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(getDataSpacesTabs()).append("}\n\n");
           break;  
         case MACRO2_HEX:
         case MACRO2_BIN:
           str.append(getDataSpacesTabs()).append("!macro MonoSpriteLine tribyte {\n")
              .append(getDataSpacesTabs()).append("  !byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(getDataSpacesTabs()).append("}\n\n");               
           break; 
         case MACRO3_HEX:
         case MACRO3_BIN:
           str.append(getDataSpacesTabs()).append(".macro MonoSpriteLine tribyte \n")
              .append(getDataSpacesTabs()).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(getDataSpacesTabs()).append(".endmacro\n\n");                         
           break;    
         case MACRO4_HEX:
         case MACRO4_BIN:
           str.append(
             "MonoSpriteLine .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "  .endm\n\n"
           );           
           break;             
       }
     };
   }
   
   /**
    * Multicolor sprite declaration type
    * 
    * -> [byte] $xx.
    * -> [byte] %b..
    * -> !24 $xx..
    * -> !24 %b..
    * -> [.mac] %xx..
    * -> [.mac] %b..
    * -> [.macro] %b.. 
    * -> [.macro] %xx..
    */ 
   public enum MultiSprite implements ActionType {
     BYTE_HEX,      // [byte] $xx..
     BYTE_BIN,      // [byte] %b..
     TWENTYFOUR_HEX,// !24 $xx..
     TWENTYFOUR_BIN,// !24 %b..
     MACRO_HEX,     // [.mac] %xx..   (Dasm)
     MACRO_BIN,     // [.mac] %b..    (Dasm)
     MACRO1_HEX,    // [.macro] %xx.. (Kick assembler)
     MACRO1_BIN,    // [.macro] %b..  (Kick assembler)
     MACRO2_HEX,    // [.macro] %xx.. (Acme)
     MACRO2_BIN,    // [.macro] %b..  (Acme) 
     MACRO3_HEX,    // [.macro] %xx.. (CA65)
     MACRO3_BIN,    // [.macro] %b..  (CA65)
     MACRO4_HEX,    // [.macro] %xx.. (TMPx)
     MACRO4_BIN,    // [.macro] %b..  (TMPx)
     ;      
     @Override
     public void flush(StringBuilder str) {
        if (list.isEmpty()) return; 
        
        // we must receive a list of 3 or 1 final byte (if 2, uses as bytes)
        if (list.size()>=3) {    
          MemoryDasm mem1=list.pop(); 
          MemoryDasm mem2=list.pop();
          MemoryDasm mem3=list.pop(); 
          StringBuilder tmp;
          String tmpS;
           
          // add a dasm comment with pixels
          mem3.dasmComment=BinToMulti(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))+
                           BinToMulti(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))+
                           BinToMulti(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1));
          lastMem=mem3;
          
          // now we have one row of 3 bytes
          switch (aMultiSprite) {
            case BYTE_HEX:
              mem1=mem1.clone();
              mem2=mem2.clone();
              mem3=mem3.clone();
              mem1.dataType=DataType.BYTE_HEX;
              mem2.dataType=DataType.BYTE_HEX;
              mem3.dataType=DataType.BYTE_HEX;
              list.push(mem3);
              list.push(mem2);
              list.push(mem1);
              tmp=new StringBuilder();
              aByte.flush(tmp);  
              tmpS=tmp.toString();
              str.append(tmpS.substring(0, tmpS.length()-1)).append("  ");
              break;
            case BYTE_BIN:
              mem1=mem1.clone();
              mem2=mem2.clone();
              mem3=mem3.clone();
              mem1.dataType=DataType.BYTE_BIN;
              mem2.dataType=DataType.BYTE_BIN;
              mem3.dataType=DataType.BYTE_BIN;
              list.push(mem3);
              list.push(mem2);
              list.push(mem1);
              tmp=new StringBuilder();
              aByte.flush(tmp); 
              tmpS=tmp.toString();
              str.append(tmpS.substring(0, tmpS.length()-1)).append("  ");
              break;
            case TWENTYFOUR_HEX:
              str.append(getDataSpacesTabs()).append("!24 $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case TWENTYFOUR_BIN:
              str.append(getDataSpacesTabs()).append("!24 %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;               
            case MACRO_HEX:
            case MACRO3_HEX:    
              str.append(getDataSpacesTabs()).append("MultiSpriteLine $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO_BIN:
            case MACRO3_BIN:    
              str.append(getDataSpacesTabs()).append("MultiSpriteLine %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;  
            case MACRO1_HEX:
              str.append(getDataSpacesTabs())
                 .append((option.kickColonMacro ? ":":""))     
                 .append("MultiSpriteLine($")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append(")  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO1_BIN:
              str.append(getDataSpacesTabs())
                 .append((option.kickColonMacro ? ":":""))       
                 .append("MultiSpriteLine(%")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append(")  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break; 
            case MACRO2_HEX:
              str.append(getDataSpacesTabs()).append("+MultiSpriteLine $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO2_BIN:
              str.append(getDataSpacesTabs()).append("+MultiSpriteLine %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;   
            case MACRO4_HEX:
              str.append(getDataSpacesTabs()).append("#MultiSpriteLine $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO4_BIN:
              str.append(getDataSpacesTabs()).append("#MultiSpriteLine %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;               
            }          
          aComment.flush(str);
        } else {
            // force to be as byte
            aByte.flush(str);
          }    
     }   
      
     /** 
      * Setting up the action type if this is the case
      * 
      * @param str the output stream
      */
     @Override
     public void setting(StringBuilder str) {
       switch (aMultiSprite) {
         case MACRO_HEX:
         case MACRO_BIN:
           str.append(getDataSpacesTabs()).append(".mac MultiSpriteLine \n")
              .append(getDataSpacesTabs()).append("  .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n")
              .append(getDataSpacesTabs()).append(".endm \n\n");
           break;
         case MACRO1_HEX:
         case MACRO1_BIN:
           str.append(getDataSpacesTabs()).append(".macro MultiSpriteLine (tribyte) {\n")
              .append(getDataSpacesTabs()).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(getDataSpacesTabs()).append("}\n\n");               
           break;  
         case MACRO2_HEX:
         case MACRO2_BIN:
           str.append(getDataSpacesTabs()).append("!macro MultiSpriteLine tribyte {\n")
              .append(getDataSpacesTabs()).append("  !byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(getDataSpacesTabs()).append("}\n\n");           
           break;      
         case MACRO3_HEX:
         case MACRO3_BIN:
           str.append(getDataSpacesTabs()).append(".macro MultiSpriteLine tribyte \n")
              .append(getDataSpacesTabs()).append("  .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n")
              .append(getDataSpacesTabs()).append(".endmacro\n\n");  
         case MACRO4_HEX:
         case MACRO4_BIN:
           str.append(
             "MultiSpriteLine .macro \n" +
             "     .byte \\1 >> 16, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "  .endm\n\n"
           );           
           break;            
       }
     };      
   } 
   
   /**
    * Text declaration type
    *  -> .byte "xxx"
    *  -> .byt "xxx"
    *  -> byte "xxx"
    *  -> dc "xxx"
    *  -> dc.b "xxx"
    *  -> .byt "xxx"
    *  -> !text "xxx"
    *  -> .text "xxx"
    *  -> !tx "xxx"
    *  -> !raw "xxx" 
    */
   public enum Text implements ActionType {
      DOT_BYTE_TEXT,      // .byte "xxx"
      DOT_BYT_TEXT,       //  .byt "xxx"
      BYTE_TEXT,          //  byte "xxx"
      DC_BYTE_TEXT,       //    dc "xxx"
      DC_B_BYTE_TEXT,     //  dc.b "xxx"
      DOT_BYT_BYTE_TEXT,  //  .byt "xxx"
      MARK_TEXT,          // !text "xxx"
      MARK_TX_TEXT,       //   !tx "xxx"
      MARK_RAW_TEXT,      //  !raw "xxx"
      DOT_TEXT            // .text "xxx"
      ;

      @Override
      public void flush(StringBuilder str) {
        if (list.isEmpty()) return;    
        
        boolean isString=false;
        boolean isFirst=true;
        boolean isSpecial=false;
        int position=0;
        
        int pos1=str.length();
               
        switch (aText) {
          case DOT_BYTE_TEXT:
            str.append(getDataSpacesTabs()).append((".byte "));
            break;
          case DOT_BYT_TEXT:
            str.append(getDataSpacesTabs()).append((".byt "));
            break;  
          case BYTE_TEXT:
            str.append(getDataSpacesTabs()).append(("byte "));  
            break;
          case DC_BYTE_TEXT:
            str.append(getDataSpacesTabs()).append(("dc "));  
            break;
          case DC_B_BYTE_TEXT:
            str.append(getDataSpacesTabs()).append(("dc.b "));  
            break;
          case MARK_TEXT:
            str.append(getDataSpacesTabs()).append(("!text "));  
            break;
          case MARK_TX_TEXT:
            str.append(getDataSpacesTabs()).append(("!tx "));  
            break; 
          case MARK_RAW_TEXT:
            str.append(getDataSpacesTabs()).append(("!raw "));  
            break;            
          case DOT_TEXT:
            str.append(getDataSpacesTabs()).append((".text "));   
            break;  
        }       
        
        int pos2=str.length();
        
        MemoryDasm mem;
        MemoryDasm memRel;
      
        while (!list.isEmpty()) {
          // accodate each bytes in the format choosed
          mem=list.pop();
          memRel=listRel.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if ( (val==0x00) ||
                   (val==0x0A) ||
                   (val==0x22) ||
                   (val>127)    
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }                  
              break;
            case TMPX:
              if ( (val==0x08) ||
                   (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)  
                 )  {                                    
                  // sorry, we force to be bytes as tmpx did not supports byte in line of text
                  if (isFirst) {
                    str.replace(pos1, pos2, "");
                    isFirst=false; 
                  } else                      
                      if (isString) {
                        str.append("\"\n");
                        isString=false;  
                      }                  
                  list.push(mem);
                  listRel.push(memRel);
                  aByte.flush(str);   
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }                  
              break;  
            case CA65:
              if ( (val==0x0A) ||
                   (val==0x22) ||
                   (val>127)    
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }     
              break;
            case ACME:                
              if ( (val==0x00) ||
                   (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val==0x5C) ||
                   (val>127) 
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }                  
              break;              
            case KICK:
              if (isFirst) {
                position=str.length();
                str.append("@\"");
                isString=true;
                isFirst=false;  
              }    
              if ((val<=0x02) ||
                  (val==0x0A) ||
                  (val==0x0C) ||    
                  (val==0x0D) ||
                  (val==0x0E) ||        
                  (val==0x0F) ||         
                  (val==0x40) ||
                  (val==0x5B) ||
                  (val==0x5D) ||
                  (val>=0x61 && val<=0x7A) ||
                  (val==0x7F) ||
                  (val==0xA0) ||
                  (val==0xA3)                         
                 ) {
                str.append("\\$").append(ByteToExe(Unsigned.done(mem.copy)));   
                isSpecial=true;
              } else if (val==0x22) {
                       str.append("\\\"");
                       isSpecial=true;
                     }
                else if (val==0x5C) { 
                       str.append("\\\\");
                       isSpecial=true;
                     }
                else str.append((char)(mem.copy & 0xFF));                
              break; 
            case TASS64:
              if ( (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)    
                 ){
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }   
              break;  
          }                                  
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");
            if (option.assembler==Assembler.Name.KICK && !isSpecial) str.setCharAt(position, ' ');
          }
        }
      }
   }
   
   
   /**
    * Text with number of chars
    */
   public enum NumText implements ActionType {
     DOT_PTEXT_NUMTEXT,       // -> .ptext "xxx"
     DOT_TEXT_NUMTEXT,        // ->  .text "xxx"
     DOT_TEXT_P_NUMTEXT,      // -> .text n"xxx" 
     DOT_BYTE_NUMTEXT,        // ->  .byte "xxx"
     DOT_BYT_NUMTEXT,         // ->   .byt "xxx"
     MARK_TEXT_NUMTEXT,       // ->  !text "xxx"
     MARK_TX_NUMTEXT,         // ->    !tx "xxx"
     MARK_RAW_NUMTEXT,        // ->   !raw "xxx"    
     BYTE_NUMTEXT,            // ->   byte "xxx"
     DC_NUMTEXT,              // ->     dc "xxx"
     DC_DOT_B_NUMTEXT         // ->   dc.b "xxx"
     ;     

     @Override
     public void flush(StringBuilder str) {
       if (list.isEmpty()) return;    
         
       boolean isString=false;
       boolean isFirst=true;  
       
       int pos1=str.length();
         
       switch (aNumText) {
         case DOT_PTEXT_NUMTEXT:
           str.append(getDataSpacesTabs()).append((".ptext "));
           break; 
         case DOT_TEXT_NUMTEXT:
           str.append(getDataSpacesTabs()).append((".text "));
           break;   
         case DOT_TEXT_P_NUMTEXT:
           str.append(getDataSpacesTabs()).append((".text p"));
           break;  
         case DOT_BYTE_NUMTEXT:
           str.append(getDataSpacesTabs()).append((".byte "));
           break;
         case DOT_BYT_NUMTEXT:
           str.append(getDataSpacesTabs()).append((".byt "));
           break;         
         case MARK_TEXT_NUMTEXT:
           str.append(getDataSpacesTabs()).append(("!text "));
           break;   
         case MARK_TX_NUMTEXT:
           str.append(getDataSpacesTabs()).append(("!tx "));
           break; 
         case MARK_RAW_NUMTEXT:
           str.append(getDataSpacesTabs()).append(("!raw "));
           break;            
         case BYTE_NUMTEXT:
           str.append(getDataSpacesTabs()).append(("byte "));  
           break;
         case DC_NUMTEXT:
           str.append(getDataSpacesTabs()).append(("dc "));  
           break;
         case DC_DOT_B_NUMTEXT:
           str.append(getDataSpacesTabs()).append(("dc.b "));  
           break;  
       }  
       
       int pos2=str.length();
       
       MemoryDasm mem;
       MemoryDasm memRel;
       
       if (option.assembler==Assembler.Name.TMPX || option.assembler==Assembler.Name.TASS64)  {
         // this byte is calculated by instruction
         list.pop();
         listRel.pop();
       }
      
        while (!list.isEmpty()) {
          // accodate each bytes in the format choosed
          mem=list.pop();
          memRel=listRel.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if (isFirst) {  
               str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
               isFirst=false;                   
              } else {  
                  if ( (val==0x00) ||
                       (val==0x0A) ||
                       (val==0x22) ||
                       (val>127)    
                     )  {
                      if (isString) {
                        str.append("\"");
                        isString=false;  
                      }
                      if (isFirst) {
                        str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                        isFirst=false;
                      } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
                  } else {
                    if (isFirst) {
                         isFirst=false;
                         isString=true;
                         str.append("\"");
                    } else if (!isString) {
                             str.append(", \"");
                             isString=true;  
                           }  
                     str.append((char)(mem.copy & 0xFF));  
                  }     
              }
              break;
            case TMPX:
              if ( (val==0x08) ||
                   (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)  
                 )  {                                    
                  // sorry, we force to be bytes as tmpx did not supports byte in line of text
                  if (isFirst) {
                    str.replace(pos1, pos2, "");
                    isFirst=false; 
                  } else                      
                      if (isString) {
                        str.append("\"\n");
                        isString=false;  
                      }                  
                  list.push(mem);
                  listRel.push(memRel);
                  aByte.flush(str);   
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }                  
              break;  
            case CA65:
              if (isFirst) {  
               str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
               isFirst=false;                   
              } else {  
                  if ( (val==0x0A) ||
                       (val==0x22) ||
                       (val>127)    
                     )  {
                    if (isString) {
                      str.append("\"");
                      isString=false;  
                    }
                    if (isFirst) {
                      str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                      isFirst=false;
                    } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
                  } else {
                      if (isFirst) {
                          isFirst=false;
                          isString=true;
                          str.append("\"");
                     } else if (!isString) {
                              str.append(", \"");
                              isString=true;  
                            }  
                      str.append((char)(mem.copy & 0xFF));  
                    }    
              }
              break;  
            case ACME:   
              if (isFirst) {  
               str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
               isFirst=false;                   
              } else {  
                  if ( (val==0x00) ||
                       (val==0x0A) ||
                       (val==0x0D) ||
                       (val==0x22) ||
                       (val==0x5C) ||
                       (val>127) 
                     )  {
                    if (isString) {
                       str.append("\"");
                       isString=false;  
                    }
                    if (isFirst) {
                      str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                      isFirst=false;
                    } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
                } else {
                   if (isFirst) {
                        isFirst=false;
                        isString=true;
                        str.append("\"");
                   } else if (!isString) {
                            str.append(", \"");
                            isString=true;  
                          }  
                    str.append((char)(mem.copy & 0xFF));  
                  } 
              }
              break;  
            case KICK:
              if (isFirst) {
                str.append("@\"\\$").append(ByteToExe(Unsigned.done(mem.copy)));
                isString=true;
                isFirst=false; 
              } else {   
                  if ((val<=0x02) ||
                      (val==0x0A) ||
                      (val==0x0C) ||    
                      (val==0x0D) ||
                      (val==0x0E) ||        
                      (val==0x0F) ||         
                      (val==0x40) ||
                      (val==0x5B) ||
                      (val==0x5D) ||
                      (val>=0x61 && val<=0x7A) ||
                      (val==0x7F) ||
                      (val==0xA0) ||
                      (val==0xA3)                         
                    ) {
                  str.append("\\$").append(ByteToExe(Unsigned.done(mem.copy)));   
                } else if (val==0x22) str.append("\\\"");
                  else if (val==0x5C) str.append("\\\\");
                  else str.append((char)(mem.copy & 0xFF));     
                }
              break;    
            case TASS64:
              if ( (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)    
                 ){
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }   
              break;              
          }   
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");
          }
       }     
     }       
   }   
   
   /**
    * Text terminated with zero
    */
   public enum ZeroText implements ActionType {
     DOT_NULL_ZEROTEXT,       // ->   .null "xxx"
     DOT_TEXT_ZEROTEXT,       // ->   -text "xxx" 
     DOT_TEXT_N_ZEROTEXT,     // ->   -text n"xxx" 
     DOT_BYTE_ZEROTEXT,       // ->   .byte "xxx"
     DOT_ASCIIZ_ZEROTEXT,     // -> .asciiz "xxx"
     BYTE_ZEROTEXT,           // ->    byte "xxx"
     MARK_TEXT_ZEROTEXT,      // ->   !text "xxx"
     MARK_TX_ZEROTEXT,        // ->     !tx "xxx"
     MARK_RAW_ZEROTEXT,       // ->    !raw "xxx"
     DC_BYTE_ZEROTEXT,        // ->      dc "xxx"
     DC_B_BYTE_ZEROTEXT       // ->    dc.b "xxx"
     ;

     @Override
     public void flush(StringBuilder str) {
       if (list.isEmpty()) return;  
       
       boolean isString=false;
       boolean isFirst=true;
       
       int pos1=str.length(); 
         
       switch (aZeroText) {
         case DOT_NULL_ZEROTEXT:
           str.append(getDataSpacesTabs()).append((".null "));
           break; 
         case DOT_TEXT_ZEROTEXT:
           str.append(getDataSpacesTabs()).append((".text "));
           break;   
         case DOT_TEXT_N_ZEROTEXT:
           str.append(getDataSpacesTabs()).append((".text n"));
           break;  
         case DOT_BYTE_ZEROTEXT:
           str.append(getDataSpacesTabs()).append((".byte "));
           break;
         case DOT_ASCIIZ_ZEROTEXT:
           str.append(getDataSpacesTabs()).append((".asciiz "));
           break;     
         case MARK_TEXT_ZEROTEXT:
           str.append(getDataSpacesTabs()).append(("!text "));
           break;   
         case MARK_TX_ZEROTEXT:
           str.append(getDataSpacesTabs()).append(("!tx "));
           break; 
         case MARK_RAW_ZEROTEXT:
           str.append(getDataSpacesTabs()).append(("!raw "));
           break;            
         case BYTE_ZEROTEXT:
           str.append(getDataSpacesTabs()).append(("byte "));  
           break;
         case DC_BYTE_ZEROTEXT:
           str.append(getDataSpacesTabs()).append(("dc "));  
           break;
         case DC_B_BYTE_ZEROTEXT:
           str.append(getDataSpacesTabs()).append(("dc.b "));  
           break;   
       }
       
       int pos2=str.length();
       
       MemoryDasm mem;
       MemoryDasm memRel;
      
        while (!list.isEmpty()) {
          // accodate each bytes in the format choosed
          mem=list.pop();
          memRel=listRel.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if ( (val==0x00) ||
                   (val==0x0A) ||
                   (val==0x22) ||
                   (val>127)    
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }                  
              break;
           case TMPX:
              if ( (val==0x08) ||
                   (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)  
                 )  {                                    
                  // sorry, we force to be bytes as tmpx did not supports byte in line of text
                  if (isFirst) {
                    str.replace(pos1, pos2, "");
                    isFirst=false; 
                  } else                      
                      if (isString) {
                        str.append("\"\n");
                        isString=false;  
                      }                  
                  list.push(mem);
                  listRel.push(memRel);
                  aByte.flush(str);   
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }   
              if (list.size()==1) {
                  // terminating 0 is ommitted
                list.pop();
                listRel.pop();
              }
              break; 
            case CA65:
              if ( (val==0x0A) ||
                   (val==0x22) ||
                   (val>127)    
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }   
              if (list.size()==1) {
                  // terminating 0 is ommitted
                list.pop();
                listRel.pop();
              }
              break;  
           case ACME:                
              if ( (val==0x00) ||
                   (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val==0x5C) ||
                   (val>127) 
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }  
              if (list.size()==1) {
                  // terminating 0 is ommitted
                list.pop();
                listRel.pop();
              }
              break;  
            case KICK:
              if (isFirst) {
                str.append("@\"");
                isString=true;
                isFirst=false;  
              }    
              if ((val<=0x00) ||
                  (val<=0x02) ||
                  (val==0x0A) ||
                  (val==0x0C) ||    
                  (val==0x0D) ||
                  (val==0x0E) ||        
                  (val==0x0F) ||         
                  (val==0x40) ||
                  (val==0x5B) ||
                  (val==0x5D) ||
                  (val>=0x61 && val<=0x7A) ||
                  (val==0x7F) ||
                  (val==0xA0) ||
                  (val==0xA3)                         
                 ) {
                str.append("\\$").append(ByteToExe(Unsigned.done(mem.copy)));   
              } else if (val==0x22) {
                       str.append("\\\"");
                     }
                else if (val==0x5C) { 
                       str.append("\\\\");
                     }
                else str.append((char)(mem.copy & 0xFF));                
              break;         
            case TASS64:
              if ( (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)    
                 ){
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }   
              if (list.size()==1) {
                  // terminating 0 is ommitted
                list.pop();
                listRel.pop();
              }
              break;               
          }   
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");

          }
       }     
     }
   }
   
      
   /**
    * Text terminated with high bit 1
    */
   public enum HighText implements ActionType {
      DOT_BYTE_HIGHTEXT,       // ->   .byte "xxx"
      DOT_BYT_HIGHTEXT,        // ->   .byt  "xxx"
      DOT_TEXT_S_HIGHTEXT,     // ->  .text s"xxx"
      BYTE_HIGHTEXT,           // ->    byte "xxx"
      MARK_TEXT_HIGHTEXT,      // ->   !text "xxx"      
      MARK_TX_HIGHTEXT,        // ->     !tx "xxx"
      MARK_RAW_HIGHTEXT,       // ->    !raw "xxx"
      DC_BYTE_HIGHTEXT,        // ->      dc "xxx"
      DC_B_BYTE_HIGHTEXT,      // ->    dc.b "xxx"
      DOT_SHIFT_HIGHTEXT       // ->  .shift "xxx"
      ;

      @Override
      public void flush(StringBuilder str) {
      if (list.isEmpty()) return;  
       
       boolean isString=false;
       boolean isFirst=true;
       
       int pos1=str.length(); 
         
       switch (aHighText) {
         case DOT_SHIFT_HIGHTEXT:
           str.append(getDataSpacesTabs()).append((".shift "));
           break;   
         case DOT_TEXT_S_HIGHTEXT:
           str.append(getDataSpacesTabs()).append((".text s"));
           break;  
         case DOT_BYTE_HIGHTEXT:
           str.append(getDataSpacesTabs()).append((".byte "));
           break;
         case DOT_BYT_HIGHTEXT:
           str.append(getDataSpacesTabs()).append((".byt "));
           break;     
         case MARK_TEXT_HIGHTEXT:
           str.append(getDataSpacesTabs()).append(("!text "));
           break;   
         case MARK_TX_HIGHTEXT:
           str.append(getDataSpacesTabs()).append(("!tx "));
           break; 
         case MARK_RAW_HIGHTEXT:
           str.append(getDataSpacesTabs()).append(("!raw "));
           break;            
         case BYTE_HIGHTEXT:
           str.append(getDataSpacesTabs()).append(("byte "));  
           break;
         case DC_BYTE_HIGHTEXT:
           str.append(getDataSpacesTabs()).append(("dc "));  
           break;
         case DC_B_BYTE_HIGHTEXT:
           str.append(getDataSpacesTabs()).append(("dc.b "));  
           break;   
       }
       
       int pos2=str.length();
       
       MemoryDasm mem;
       MemoryDasm memRel;
       while (!list.isEmpty()) {
          // accodate each bytes in the format choosed
          mem=list.pop();
          memRel=listRel.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if ( (val==0x00) ||
                   (val==0x0A) ||
                   (val==0x22) ||
                   (val>127)    
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }  
              break;
           case TMPX:
              if (list.size()==1) {
                // terminating has 1 converted to 0
                mem=list.pop();
                listRel.pop();
                str.append((char)(mem.copy & 0x7F));  
              } 
              if ( (val==0x08) ||
                   (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)  
                 )  {                                    
                  // sorry, we force to be bytes as tmpx did not supports byte in line of text
                  if (isFirst) {
                    str.replace(pos1, pos2, "");
                    isFirst=false; 
                  } else                      
                      if (isString) {
                        str.append("\"\n");
                        isString=false;  
                      }                  
                  list.push(mem);
                  listRel.push(memRel);
                  aByte.flush(str);   
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }   
              break; 
            case CA65:
              if ( (val==0x0A) ||
                   (val==0x22) ||
                   (val>127)    
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }   
              break;  
           case ACME:                
              if ( (val==0x00) ||
                   (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val==0x5C) ||
                   (val>127) 
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }  
              break;  
            case KICK:
              if (isFirst) {
                str.append("@\"");
                isString=true;
                isFirst=false;  
              }    
              if ((val<=0x00) ||
                  (val<=0x02) ||
                  (val==0x0A) ||
                  (val==0x0C) ||    
                  (val==0x0D) ||
                  (val==0x0E) ||        
                  (val==0x0F) ||         
                  (val==0x40) ||
                  (val==0x5B) ||
                  (val==0x5D) ||
                  (val>=0x61 && val<=0x7A) ||
                  (val==0x7F) ||
                  (val==0xA0) ||
                  (val==0xA3)                         
                 ) {
                str.append("\\$").append(ByteToExe(Unsigned.done(mem.copy)));   
              } else if (val==0x22) {
                       str.append("\\\"");
                     }
                else if (val==0x5C) { 
                       str.append("\\\\");
                     }
                else str.append((char)(mem.copy & 0xFF));                
              break;         
            case TASS64:                
              if ( (val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)    
                 ){
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(Unsigned.done(mem.copy))); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(Unsigned.done(mem.copy)));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)(mem.copy & 0xFF));  
                }            
              if (list.size()==1) {
                // terminating has 1 converted to 0
                mem=list.pop();
                listRel.pop();
                str.append((char)(mem.copy & 0x7F));  
              }
              break;               
          }   
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");

          }
        }
      }
       
   }
   
   /**
    * Text left shifted
    */
   public enum ShiftText implements ActionType {
      DOT_BYTE_SHIFTTEXT,       // ->   .byte "xxx"
      DOT_BYT_SHIFTTEXT,        // ->   .byt  "xxx"
      DOT_TEXT_SHIFTTEXT,       // ->   .text "xxx"
      DOT_TEXT_L_SHIFTTEXT,     // ->  .text l"xxx"
      BYTE_SHIFTTEXT,           // ->    byte "xxx"
      MARK_TEXT_SHIFTTEXT,      // ->   !text "xxx"      
      MARK_TX_SHIFTTEXT,        // ->     !tx "xxx"
      MARK_RAW_SHIFTTEXT,       // ->    !raw "xxx"
      DC_BYTE_SHIFTTEXT,        // ->      dc "xxx"
      DC_B_BYTE_SHIFTTEXT,      // ->    dc.b "xxx"
      DOT_SHIFTL_SHIFTTEXT      // -> .shiftl "xxx"
      ;

        @Override
        public void flush(StringBuilder str) {
            
        }
   }  
   
   /**
    * Stack Word
    */
   public enum StackWord implements ActionType {
      DOT_RTA_STACKWORD,        // ->   .rta $xxyy
      MACRO_STACKWORD,          // -> [.mac] $xxyyzz    (DASM)
      MACRO1_STACKWORD,         // -> [.mac] $xxyyzz    (KickAssembler)
      MACRO2_STACKWORD,         // -> [.mac] $xxyyzz    (Acme)
      MACRO3_STACKWORD          // -> [.mac] $xxyyzz    (CA65) 
      ;

      @Override
      public void flush(StringBuilder str) {
       if (list.isEmpty()) return; 
       
       MemoryDasm memLow;
       MemoryDasm memHigh;
       MemoryDasm memRelLow;
       MemoryDasm memRelHigh;
     
       int pos1=str.length();  // store initial position
       int index=(int)(list.size()/2);
       
       // create starting command according to the kind of byte
       switch (aStackWord) {
         case DOT_RTA_STACKWORD:
           str.append(getDataSpacesTabs()).append((".rta "));  
           break;
         case MACRO_STACKWORD:
         case MACRO3_STACKWORD:     
           str.append(getDataSpacesTabs()).append("Stack").append(index).append(" ");  
           break;           
         case MACRO1_STACKWORD:
           str.append(getDataSpacesTabs()).append("Stack").append(index).append("(");   // must close the )
           break;
         case MACRO2_STACKWORD:
           str.append(getDataSpacesTabs()).append("+Stack").append(index).append(" ");  
           break;
       }
       
       int pos2=str.length();   // store final position
       boolean isFirst=true;       // true if this is the first output
       
       while (!list.isEmpty()) {
         // if only 1 byte left, use byte coding
         if (list.size()==1) {
           if (isFirst) {
              str.replace(pos1, pos2, "");
              isFirst=false;                    
           }  
           aByte.flush(str);
         }
         else {
           memLow=list.pop();
           memRelLow=listRel.pop();
           memHigh=list.pop();
           memRelHigh=listRel.pop();           
           
           if (memLow.type=='<' && memHigh.type=='>' && memLow.related==memHigh.related) {
             if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation).append("+1");
             else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation).append("+1");
                  else str.append("$").append(ShortToExe(memRelLow.address+1));  
             isFirst=false;
           } else {
               // if cannot make a word with relative locations, force all to be of byte type
               if (memLow.type=='<' || memLow.type=='>' || memHigh.type=='>' || memHigh.type=='<')  {
                 list.addFirst(memHigh);
                 list.addFirst(memLow);
                 listRel.addFirst(memRelHigh);
                 listRel.addFirst(memRelLow);
                 if (isFirst) {
                   str.replace(pos1, pos2, "");
                   isFirst=false;
                 }
                 aByte.flush(str);
               }
               else {
                 str.append("$").append(ByteToExe(Unsigned.done(memHigh.copy))).append(ByteToExe((Unsigned.done(memLow.copy)+1)& 0xFF));                            
                 isFirst=false;  
               }    
             }
           if (list.size()>=2) str.append(", ");
           else if (aStackWord==MACRO1_STACKWORD) str.append(")\n");
           else str.append("\n");
         }
       }           
      }

      @Override
      public void setting(StringBuilder str) {
        String spaces=getDataSpacesTabs();           
          
        switch (aStackWord) {
         case MACRO_STACKWORD:
           str.append(spaces).append(".mac Stack1 \n")
              .append(spaces).append("  .word {1}-1 \n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Stack2 \n")
              .append(spaces).append("  .word {1}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1 \n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Stack3 \n")
              .append(spaces).append("  .word {1}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1 \n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Stack4 \n")
              .append(spaces).append("  .word {1}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1 \n")
              .append(spaces).append(" .endm \n\n")
              .append(spaces).append(".mac Stack5 \n")
              .append(spaces).append("  .word {1}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1 \n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Stack6 \n")
              .append(spaces).append("  .word {1}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1, {6}-1 \n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Stack7 \n")
              .append(spaces).append("  .word {1}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1 \n") 
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1, {6}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1, {6}-1, {7}-1 \n")
              .append(spaces).append(".endm \n\n")
              .append(spaces).append(".mac Stack8 \n")
              .append(spaces).append("  .word {1}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1, {6}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1, {6}-1, {7}-1 \n")
              .append(spaces).append("  .word {1}-1, {2}-1, {3}-1, {4}-1, {5}-1, {6}-1, {7}-1, {8}-1 \n")     
              .append(spaces).append(".endm \n\n");
           break;            
         case MACRO1_STACKWORD:
           str.append(spaces).append(".macro Stack1 (twobyte) {\n")
              .append(spaces).append("   .word twobyte-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Stack2 (twobyte, twobyte2) {\n")
              .append(spaces).append("   .word twobyte-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Stack3 (twobyte, twobyte2, twobyte3) {\n")
              .append(spaces).append("   .word twobyte-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Stack4 (twobyte, twobyte2, twobyte3, twobyte4) {\n")
              .append(spaces).append("   .word twobyte-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Stack5 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5) {\n")
              .append(spaces).append("   .word twobyte-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Stack6 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6) {\n")
              .append(spaces).append("   .word twobyte-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Stack7 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7) {\n")
              .append(spaces).append("   .word twobyte-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1, twobyte7-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append(".macro Stack8 (twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7, twobyte8) {\n")
              .append(spaces).append("   .word twobyte-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1, twobyte7-1\n")
              .append(spaces).append("   .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1, twobyte7-1, twobyte8-1\n")
              .append(spaces).append("}\n\n");                                  
           break;       
         case MACRO2_STACKWORD:
           str.append(spaces).append("!macro Stack1 twobyte {\n")
              .append(spaces).append("   !word twobyte-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Stack2 twobyte, twobyte2 {\n")
              .append(spaces).append("   !word twobyte-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Stack3 twobyte, twobyte2, twobyte3 {\n")
              .append(spaces).append("   !word twobyte-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Stack4 twobyte, twobyte2, twobyte3, twobyte4 {\n")
              .append(spaces).append("   !word twobyte-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Stack5 twobyte, twobyte2, twobyte3, twobyte4, twobyte5 {\n")
              .append(spaces).append("   !word twobyte-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Stack6 twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6 {\n")
              .append(spaces).append("   !word twobyte-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Stack7 twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7 {\n")
              .append(spaces).append("   !word twobyte-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1, twobyte7-1\n")
              .append(spaces).append("}\n\n")
              .append(spaces).append("!macro Stack8 twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7, twobyte8 {\n")
              .append(spaces).append("   !word twobyte-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1, twobyte7-1\n")
              .append(spaces).append("   !word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1, twobyte7-1, twobyte8-1\n")
              .append(spaces).append("}\n\n");                                  
           break;            
          case MACRO3_STACKWORD:
            str.append(spaces).append(".macro Stack1 twobyte \n")
               .append(spaces).append("  .word twobyte-1\n")
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Stack2 twobyte, twobyte2 \n")
               .append(spaces).append("  .word twobyte-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1\n")
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Stack3 twobyte, twobyte2, twobyte3 \n")
               .append(spaces).append("  .word twobyte-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1\n")   
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Stack4 twobyte, twobyte2, twobyte3, twobyte4 \n")
               .append(spaces).append("  .word twobyte-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1\n")  
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")          
               .append(spaces).append(".endmacro\n\n")                       
               .append(spaces).append(".macro Stack5 twobyte, twobyte2, twobyte3, twobyte4, twobyte5\n")
               .append(spaces).append("  .word twobyte-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1\n")  
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Stack6 twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6\n")
               .append(spaces).append("  .word twobyte-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1\n")  
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1\n")     
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Stack7 twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7\n")
               .append(spaces).append("  .word twobyte-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1\n")   
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")   
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")   
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1, twobyte7-1\n")    
               .append(spaces).append(".endmacro\n\n")
               .append(spaces).append(".macro Stack8 twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7, twobyte8\n")
               .append(spaces).append("  .word twobyte-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1\n")
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1\n")  
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1\n")   
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1\n")  
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1\n")     
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1, twobyte7-1\n")   
               .append(spaces).append("  .word twobyte-1, twobyte2-1, twobyte3-1, twobyte4-1, twobyte5-1, twobyte6-1, twobyte7-1, twobyte8-1\n")      
               .append(spaces).append(".endmacro\n\n");               
           break;                                                      
          }
        }            
   }

   
   /** Fifo list  of memory locations */
   protected static LinkedList<MemoryDasm> list=new LinkedList();
   
   /** Fifo list of related memory locations */
   protected static LinkedList<MemoryDasm> listRel=new LinkedList();   
   
   /** Option to use */
   protected static Option option;
         
   /** Last used memory dasm */
   protected static MemoryDasm lastMem=null;
   
   /** Last program counter */
   protected static int lastPC=0;
   
   
   /** Assembler starting to use */
   protected static Assembler.Starting aStarting; 
   
   /** Assembler origin to use */
   protected static Assembler.Origin aOrigin; 
   
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
   
   /** Assembler word swapped type */
   protected static Assembler.WordSwapped aWordSwapped;
   
   /** Assembler tribyte type */
   protected static Assembler.Tribyte aTribyte;
     
   /** Assembler long type */
   protected static Assembler.Long aLong;  
   
   /** Assembler address type */
   protected static Assembler.Address aAddress;   
   
   /** Assembler stack word type */
   protected static Assembler.StackWord aStackWord;   
   
   /** Assembler mono color sprite type */
   protected static Assembler.MonoSprite aMonoSprite;
   
   /** Assembler multi color sprite type */
   protected static Assembler.MultiSprite aMultiSprite;
   
   /** Assembler text type */
   protected static Assembler.Text aText;
   
   /** Assembler text with num char type */
   protected static Assembler.NumText aNumText;   
   
   /** Assembler zero text type */
   protected static Assembler.ZeroText aZeroText;
   
   /** Assembler high text type */
   protected static Assembler.HighText aHighText;
   
   /** Assembler left shift text type */
   protected static Assembler.ShiftText aShiftText;  
              
   /** Actual type being processed */
   ActionType actualType=null;

   /** True if this ia a block of monoscromatic sprite */
   boolean isMonoSpriteBlock=false;
   
   /** Actual size of monocromatic sprite block */
   int sizeMonoSpriteBlock=0;
   
   /** True if this is a block of multicolor sprite */
   boolean isMultiSpriteBlock=false;
   
   /** Actual size of multicolor sprite block */
   int sizeMultiSpriteBlock=0;
   
   /** Memory dasm with num or chars */
   MemoryDasm numText;
     
   /**
    * Set the option to use
    * 
    * @param option the option to use
    * @param aStarting the starting type to use 
    * @param aOrigin the origin type to use
    * @param aLabel the label type to use
    * @param aComment the comment type to use
    * @param aBlockComment the comment type to use
    * @param aByte the byte type to use
    * @param aWord the word type to use
    * @param aWordSwapped the word swapped type to use
    * @param aTribyte the tribyte type to use
    * @param aLong the long type to use
    * @param aAddress the address type to use
    * @param aStackWord the stack word type to use
    * @param aMonoSprite the mono sprite type to use
    * @param aMultiSprite the multi sprite type to use
    * @param aText the text type to use
    * @param aNumText the text with number of char before
    * @param aZeroText the text with 0 terminated char
    * @param aHighText the text with high 1 terminated bit
    * @param aShiftText the text left shifted
    */
   public void setOption(Option option, 
                         Assembler.Starting aStarting,
                         Assembler.Origin aOrigin,
                         Assembler.Label aLabel,  
                         Assembler.Comment aComment, 
                         Assembler.BlockComment aBlockComment,
                         Assembler.Byte aByte, 
                         Assembler.Word aWord,         
                         Assembler.WordSwapped aWordSwapped,         
                         Assembler.Tribyte aTribyte,
                         Assembler.Long aLong,
                         Assembler.Address aAddress,
                         Assembler.StackWord aStackWord,
                         Assembler.MonoSprite aMonoSprite,
                         Assembler.MultiSprite aMultiSprite,
                         Assembler.Text aText,
                         Assembler.NumText aNumText,
                         Assembler.ZeroText aZeroText,
                         Assembler.HighText aHighText,
                         Assembler.ShiftText aShiftText
                         ) {
     Assembler.aStarting=aStarting;  
     Assembler.option=option;
     Assembler.aOrigin=aOrigin;
     Assembler.aLabel=aLabel;
     Assembler.aComment=aComment;
     Assembler.aBlockComment=aBlockComment;
     Assembler.aByte=aByte;
     Assembler.aWord=aWord;
     Assembler.aWordSwapped=aWordSwapped;
     Assembler.aTribyte=aTribyte;
     Assembler.aLong=aLong;
     Assembler.aAddress=aAddress;
     Assembler.aStackWord=aStackWord;
     Assembler.aMonoSprite=aMonoSprite;
     Assembler.aMultiSprite=aMultiSprite;
     Assembler.aText=aText;
     Assembler.aNumText=aNumText;
     Assembler.aZeroText=aZeroText;
     Assembler.aHighText=aHighText;
     Assembler.aShiftText=aShiftText;
      
     isMonoSpriteBlock=false;
     sizeMonoSpriteBlock=0;
     isMultiSpriteBlock=false;
     sizeMultiSpriteBlock=0;
   } 
   
   /**
    * Put the value into the buffer and manage 
    * 
    * @param str the string builder where put result
    * @param mem the memory being processed
    * @param memRel eventual memory related
    */
   public void putValue(StringBuilder str, MemoryDasm mem, MemoryDasm memRel) {
     ActionType type=actualType;  
     lastMem=mem;
     
     // if there is a block comments use it
     if (mem.userBlockComment!=null && !"".equals(mem.userBlockComment)) {
       type=actualType;
       actualType=aBlockComment;
       flush(str);
       actualType=type;
     }     
       
     // if this is a label then the type will change  
     if (!(lastMem.type=='+' || lastMem.type=='-')) {
       if (mem.userLocation!=null && !"".equals(mem.userLocation)) type=aLabel;
       else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) type=aLabel;  
     }
      
     // test if it change type  
     if (type!=actualType) {
       flush(str);              // write back previous data
       
       // we can have only one label in a memory row, so process it if this is the case
       if (type==aLabel) {
         int size=str.length();         
         type.flush(str);             // write back the label         
       
         // check if there is a comment for the label
         if (lastMem.userComment!=null) {
           size=str.length()-size;      // get number of chars used  
           str.append(SPACES.substring(0, SPACES.length()-size));
           type=aComment;
           type.flush(str);
         } else str.append("\n");  // close label by going in a new line
       }   
              
       actualType=getType(mem);
     } else {
       type=getType(mem);
       if (type!=actualType) {
          flush(str);              // write back previous data
          actualType=type;
       }
     } 
          
     
     list.add(mem);
     listRel.add(memRel);
     
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
     // we are processing word swapped?    
     if (actualType instanceof WordSwapped) {
       // look if it is time to aggregate data
       if (list.size()==option.maxSwappedAggregate*2) actualType.flush(str);         
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
     } else
     // we are processing address?    
     if (actualType instanceof Address) {
       // look if it is time to aggregate data
       if (list.size()==option.maxAddressAggregate*2) actualType.flush(str);         
     } else
     // we are processing address?    
     if (actualType instanceof StackWord) {
       // look if it is time to aggregate data
       if (list.size()==option.maxStackWordAggregate*2) actualType.flush(str);         
     } else    
     // we are processing mono sprite?    
     if (actualType instanceof MonoSprite) {
       if ((sizeMonoSpriteBlock % 3)==0) actualType.flush(str);
       else if (sizeMonoSpriteBlock>=64) {
         actualType.flush(str);
         sizeMonoSpriteBlock=0;
       }
     } else
     // we are processing multi sprite?    
     if (actualType instanceof MultiSprite) {
       if ((sizeMultiSpriteBlock % 3)==0) actualType.flush(str);
       else if (sizeMultiSpriteBlock>=64) {
         actualType.flush(str);
         sizeMultiSpriteBlock=0;
       } 
     } else
     // we are processing text?
     if (actualType instanceof Text) {
       // look if it is time to aggregate data
       if (list.size()==option.maxTextAggregate) actualType.flush(str);         
     } else
     // we are processing text?
     if (actualType instanceof NumText) {
       if (numText==null) numText=mem;
       // look if it is time to aggregate data
       if (list.size()==numText.copy+1) {
         actualType.flush(str);
         numText=null;
       }         
     } else
     // we are processing zero text?
     if (actualType instanceof ZeroText) {
       // look if it is time to aggregate data
       if (mem.copy==0) actualType.flush(str);         
     } else
     // we are processing high text?
     if (actualType instanceof HighText) {
       // look if it is time to aggregate data
       if ((mem.copy & 0X80) !=0) actualType.flush(str);         
     }   
   }
   
   /**
    * Put the starting string
    * 
    * @param str the steam for output
    */
   public void setStarting(StringBuilder str) {
     aStarting.flush(str);
   }   
   
   /**
    * Put the origin of PC
    * 
    * @param str the steam for output
    * @param pc the program counter to set
    */
   public void setOrg(StringBuilder str, int pc) {
     lastPC=pc;
     aOrigin.flush(str);
   }      
   
   /**
    * Put macros if they are used based onto assembler and user option
    * 
    * @param str the stream for output
    * @param memory the actual memory
    */
   public void setMacro(StringBuilder str, MemoryDasm[] memory) {
     boolean hasMonoSprite=false;
     boolean hasMultiSprite=false;
     boolean hasWordSwapped=false;
     boolean hasTribyte=false;
     boolean hasLong=false;
     boolean hasStack=false;
     
     for (MemoryDasm mem:memory) {
       if (mem.dataType==DataType.MONO_SPRITE) hasMonoSprite=true;
       if (mem.dataType==DataType.MULTI_SPRITE) hasMultiSprite=true;
       if (mem.dataType==DataType.SWAPPED) hasWordSwapped=true;
       if (mem.dataType==DataType.TRIBYTE) hasTribyte=true;
       if (mem.dataType==DataType.LONG) hasLong=true;
       if (mem.dataType==DataType.STACK) hasStack=true;
     }
     
     if (hasMonoSprite) aMonoSprite.setting(str);
     if (hasMultiSprite) aMultiSprite.setting(str);
     if (hasWordSwapped) aWordSwapped.setting(str);
     if (hasTribyte) aTribyte.setting(str);
     if (hasLong) aLong.setting(str);
     if (hasStack) aStackWord.setting(str);
   }
   
   /**
    * Set bytes of a relative memory location (it deletes anything that threre are in queue)
    * 
    * @param str the output stream
    * @param address the relative address
    * @param label the label of address
    */
   public void setByteRel(StringBuilder str, int address, String label) {
     MemoryDasm lowMem=new MemoryDasm();
     MemoryDasm highMem=new MemoryDasm();
     MemoryDasm lowMemRel=new MemoryDasm();
     MemoryDasm highMemRel=new MemoryDasm();
       
     list.clear();
     listRel.clear();  
     
     lowMem.type='<';
     lowMem.related=address;
     highMem.type='>';
     highMem.related=address;
     list.add(lowMem);
     list.add(highMem);
     
     lowMemRel.dasmLocation=label;
     highMemRel.dasmLocation=label;
     listRel.add(lowMemRel);
     listRel.add(highMemRel);
     
     actualType=aByte;
     flush(str);
     actualType=null;
   }
   
   /**
    * Set a word and put to ouptput steam (it deletes anything that threre are in queue)
    * 
    * @param str the output stream
    * @param low the low byte
    * @param high the hight byte
    * @param comment eventual comment to add
    */
   public void setWord(StringBuilder str, byte low, byte high, String comment) {
     MemoryDasm lowMem=new MemoryDasm();
     MemoryDasm highMem=new MemoryDasm();
     
     lowMem.copy=low;
     lowMem.dataType=DataType.WORD;
     lowMem.type='W';
     lowMem.related=-1;
     highMem.copy=high;
     highMem.dataType=DataType.WORD;
     highMem.type='W';
     highMem.related=-1;
     
     list.clear();
     listRel.clear();
     list.add(lowMem);
     listRel.add(null);
     list.add(highMem);
     listRel.add(null);
     
     int size=0;
     
     if (comment!=null) {
       highMem.userComment=comment;
       lastMem=highMem;
       size=str.length();
     }
     
     actualType=aWord;
     flush(str);
     actualType=null;
     
     if (comment!=null) {
       str.deleteCharAt(str.length()-1);            // remove \n
       size=str.length()-size;                      // get number of chars used  
       str.append(SPACES.substring(0, SPACES.length()-size));
       aComment.flush(str);  
     }
   }
   
   /**
    * Set a text and put to ouptput steam (it deletes anything that threre are in queue)
    * 
    * @param str the output stream
    * @param text the text to add
    */
   public void setText(StringBuilder str, String text) {
     MemoryDasm mem;     
     
     list.clear();
     listRel.clear();
          
     int size=0;
     
     for (char val: text.toCharArray()) {
       mem=new MemoryDasm();
       mem.copy=(byte)val;
       mem.dataType=DataType.TEXT;
       list.add(mem);
       listRel.add(null);
     }
       
     actualType=aText;
     flush(str);
     actualType=null;          
   }
   
   /**
    * Set the comment if present 
    * 
    * @param str the stream to use
    * @param mem the memory with comment
    */
   public void setComment(StringBuilder str, MemoryDasm mem) {
     lastMem=mem;
     aComment.flush(str);
   }
   
   /**
    * Set the block comment if present 
    * 
    * @param str the stream to use
    * @param mem the memory with block comment
    */
   public void setBlockComment(StringBuilder str, MemoryDasm mem) {
     lastMem=mem;
     aBlockComment.flush(str);
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
    * Get the type for this location
    * @param mem the memory to analize
    * @return the location type
    */
   private ActionType getType(MemoryDasm mem) {     
     if (!mem.isData) {
       isMonoSpriteBlock=false;
       isMultiSpriteBlock=false;   
       return null;
     }
     
     switch (mem.dataType) {
       case BYTE_HEX:
       case BYTE_DEC:
       case BYTE_BIN:
       case BYTE_CHAR:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;    
         numText=null;
         return aByte;
       case WORD:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aWord;
       case SWAPPED:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aWordSwapped;       
       case TRIBYTE:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aTribyte;  
       case LONG:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aLong;      
       case ADDRESS:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aAddress; 
       case STACK:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aStackWord;  
       case MONO_SPRITE:
         if (!isMonoSpriteBlock) sizeMonoSpriteBlock=0;  
         isMonoSpriteBlock=true;  
         isMultiSpriteBlock=false;
         sizeMonoSpriteBlock++;
         numText=null;
         return aMonoSprite;
       case MULTI_SPRITE:
         if (!isMultiSpriteBlock) sizeMultiSpriteBlock=0; 
         isMultiSpriteBlock=true; 
         isMonoSpriteBlock=false;
         sizeMultiSpriteBlock++;
         numText=null;
         return aMultiSprite;  
       case TEXT:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         return aText; 
       case NUM_TEXT:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         return aNumText;     
       case ZERO_TEXT:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aZeroText;         
       case HIGH_TEXT:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aHighText;       
     }
     
     // default is of Byte type
     isMonoSpriteBlock=false;
     isMultiSpriteBlock=false; 
     numText=null;
     return aByte;
   }   

}
               

