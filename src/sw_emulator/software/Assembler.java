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
    *  -> .cpu "6502"
    *  -> .cpu 6502
    *  -> .setcpu 6502x
    *  -> .p02
    *  -> !CPU 6510
    */
   public enum Starting implements ActionType {
      PROC,             // processor 6502
      DOT_CPU_A,        // .cpu "6502"
      DOT_CPU,          // .cpu 6502
      DOT_SETCPU,       // .setcpu 6502x
      DOT_P02,          // .p02
      MARK_CPU;         // !cpu 6510
       
       
      @Override
      public void flush(StringBuilder str) {
        switch (aStarting) {
          case PROC:
            str.append("  processor 6502\n\n");
            break;
          case DOT_CPU_A:
            str.append("  .cpu \"6502\"\n\n");  
            break;
          case DOT_CPU:
            str.append("  .cpu 6502\n\n");
            break;
          case DOT_SETCPU:
            str.append("  .cpu 6502\n\n");
            break; 
          case DOT_P02:
            str.append("  .p02\n\n");
            break;    
          case MARK_CPU:
            str.append("  !cpu 6510\n\n");
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
              str.append("  org $").append(ShortToExe(lastPC)).append("\n\n");
            break;
          case DOT_ORG:
              str.append("  .org $").append(ShortToExe(lastPC)).append("\n\n");
            break;
          case ASTERISK:
              str.append("  *=$").append(ShortToExe(lastPC)).append("\n\n");
            break;
          case DOT_PC:
              str.append("  .pc $").append(ShortToExe(lastPC)).append("\n\n");
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
        
        if ("".equals(comment)) {
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
    *  -> ; xxxx
    *  -> /\* xxx *\/
    *  -> if 0 xxx endif
    *  -> .if 0 xxx .fi
    *  -> .if 0 xxx .endif
    *  -> .if (0) xxx .endif
    *  -> !if 0 { xxx }
    *  -> .comment xxx .endc
    */
   public enum BlockComment implements ActionType {
      SEMICOLON,       // ; xxxx
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
                  str.append(line).append("\n");
                }   
            }        
            if (!isOpen) str.append("\n*\\n");         
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
            if (!isOpen) str.append("endif\n");   
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
            if (!isOpen) str.append(".endif\n");    
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
            if (!isOpen) str.append(".fi\n");    
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
            if (!isOpen) str.append("}\n");    
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
            if (!isOpen) str.append("}\n");    
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
            if (!isOpen) str.append(".endc\n");   
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
    *  -> -byt $xx
    *  -> !byte $xx
    *  -> !8 $xx
    */
   public enum Byte implements ActionType {
      DOT_BYTE,           // .byte $xx
      DOT_CHAR,           // .char $xx
      BYTE,               //  byte $xx      
      DC_BYTE,            //    dc $xx
      DC_B_BYTE,          //  dc.b $xx
      DOT_BYT_BYTE,       //  .byt $xx
      MARK_BYTE,          // !byte $xx 
      MARK_BY_BYTE,        //  !by $xx
      EIGHT_BYTE,         //    !8 $xx   
      ZEROEIGHT_BYTE;     //   !08 $xx   //   !08 $xx   
      
      @Override
      public void flush(StringBuilder str) {
        if (list.isEmpty()) return; 
        
        MemoryDasm mem;
        MemoryDasm memRel;
        
        // create starting command according to the kind of byte
        switch (aByte) {
          case DOT_BYTE:
            str.append(("  .byte "));
            break;
          case DOT_CHAR:
            str.append(("  .char "));
            break;  
          case BYTE:
            str.append(("  byte "));
            break;
          case DC_BYTE:
           str.append(("  dc "));   
            break;
          case DC_B_BYTE:
            str.append(("  dc.b "));
            break;
          case MARK_BY_BYTE:
            str.append(("  !by "));  
            break;  
          case DOT_BYT_BYTE:
            str.append(("  .byt.b "));  
            break;
          case MARK_BYTE:
            str.append(("  !byte "));   
            break;  
          case EIGHT_BYTE:
            str.append(("  !8 "));  
            break;  
          case ZEROEIGHT_BYTE:
            str.append(("  !8 "));  
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
                //return "\""+(char)Unsigned.done(value)+"\"";
                return "'"+(char)Unsigned.done(value);
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
    *  -> .sint $xxyy
    *  -> word $xxyy
    *  -> dc.w $xxyy
    *  -> .dbyte $xxyy
    *  -> !word $xxyy
    *  -> !16 $xxyy
    */
   public enum Word implements ActionType {
     DOT_WORD,            //  .word $xxyy
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
        
       // create starting command according to the kind of byte
       switch (aWord) {
         case DOT_WORD:
           str.append(("  .word "));  
           break;
         case DOT_SINT:
           str.append(("  .sint "));  
           break;           
         case WORD:
           str.append(("  word "));   
           break;
         case DC_W_WORD:
           str.append(("  dc.w "));  
           break;
         case DOT_DBYTE:
           str.append(("  .dbyte "));   
           break;
         case MARK_WORD:
           str.append(("  !word "));   
           break;
         case SIXTEEN_WORD:
           str.append(("  !16 "));  
           break;  
       }
       
       while (!list.isEmpty()) {
         // if only 1 byte left, use byte coding
         if (list.size()==1) aByte.flush(str);
         else {
           memLow=list.pop();
           memRelLow=listRel.pop();
           memHigh=list.pop();
           memRelHigh=listRel.pop();           
           
           if (memLow.type=='<' && memHigh.type=='>' && memLow.related==memHigh.related) {
             if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation);
            else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation);
                 else str.append("$").append(ShortToExe(memRelLow.address));  
           } else {
             // if cannot make a word with relative locations, force all to be of byte type
             if (memLow.type=='<' || memLow.type=='>' || memHigh.type=='>' || memHigh.type=='<')  {
               list.addFirst(memHigh);
               list.addFirst(memLow);
               listRel.addFirst(memRelHigh);
               listRel.addFirst(memRelLow);
               aByte.flush(str);
             }
             else if (aWord==DOT_SINT && memHigh.copy<0) str.append("-$").append(ByteToExe(Math.abs(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));  
                  else str.append("$").append(ByteToExe(Unsigned.done(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));                            
           }
           if (list.size()>=2) str.append(", ");
           else str.append("\n");
         }
       }
     } 
   }
   
   /**
    * Word swapped declaration type
    *  -> .
    *  -> 
    *  ->
    *  -> 
    *  -> 
    *  -> 
    *  ->
    */
   public enum WordSwapped implements ActionType {
     DC_DOT_S_WORD_SWAPPED,
     DOT_DTYB,
     MACRO1_WORD_SWAPPED,           //  [.mac] $yyxx    (KickAssembler)
     MACRO2_WORD_SWAPPED,           //  [.mac] $yyxx    (Acme)
     MACRO4_WORD_SWAPPED,           //  [.mac] $yyxx    (TMPx / Tass64)
        ;

        @Override
        public void flush(StringBuilder str) {
            
        }

        @Override
        public void setting(StringBuilder str) {
          
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
           str.append("  Tribyte").append(index).append(" ");  
           break;
         case MACRO1_TRIBYTE:
           str.append("  Tribyte").append(index).append(" (");   // must close the )
           break;           
         case MACRO4_TRIBYTE:
           str.append("  #Tribyte").append(index).append(" ");  
           break;
         case DOT_LINT_TRIBYTE:
           str.append(("  .lint "));   
           break;
         case DOT_LONG_TRIBYTE:
           str.append(("  .long "));   
           break;
         case MARK_TWENTYFOUR_TRIBYTE:
           str.append(("  !24 "));  
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
       switch (aTribyte) {
         case MACRO_TRIBYTE:
           str.append(
             "  .mac Tribyte1 \n" +
             "     .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             "  .endm \n\n"+
             "  .mac Tribyte2 \n" +
             "     .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             "     .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n" +                     
             "  .endm \n\n"+
             "  .mac Tribyte3 \n" +
             "     .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             "     .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n" +  
             "     .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n" +                              
             "  .endm \n\n"+         
             "  .mac Tribyte4 \n" +
             "     .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             "     .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n" +  
             "     .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n" +                              
             "     .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n" +                              
             "  .endm \n\n"+                     
             "  .mac Tribyte5 \n" +
             "     .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             "     .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n" +  
             "     .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n" +                              
             "     .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n" +                              
             "     .byte {5} >> 16, ( {5} >> 8) & 255,  {5} & 255\n" +                              
             "  .endm \n\n"+
             "  .mac Tribyte6 \n" +
             "     .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             "     .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n" + 
             "     .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n" + 
             "     .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n" + 
             "     .byte {5} >> 16, ( {5} >> 8) & 255,  {5} & 255\n" + 
             "     .byte {6} >> 16, ( {6} >> 8) & 255,  {6} & 255\n" +
             "  .endm \n\n" +             
             "  .mac Tribyte7 \n" +
             "     .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             "     .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n" + 
             "     .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n" + 
             "     .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n" + 
             "     .byte {5} >> 16, ( {5} >> 8) & 255,  {5} & 255\n" + 
             "     .byte {6} >> 16, ( {6} >> 8) & 255,  {6} & 255\n" +
             "     .byte {7} >> 16, ( {7} >> 8) & 255,  {7} & 255\n" +        
             "  .endm \n\n"+       
             "  .mac Tribyte8 \n" +
             "     .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             "     .byte {2} >> 16, ( {2} >> 8) & 255,  {2} & 255\n" + 
             "     .byte {3} >> 16, ( {3} >> 8) & 255,  {3} & 255\n" + 
             "     .byte {4} >> 16, ( {4} >> 8) & 255,  {4} & 255\n" + 
             "     .byte {5} >> 16, ( {5} >> 8) & 255,  {5} & 255\n" + 
             "     .byte {6} >> 16, ( {6} >> 8) & 255,  {6} & 255\n" +
             "     .byte {8} >> 16, ( {8} >> 8) & 255,  {8} & 255\n" +        
             "  .endm \n\n"                   
           );
           break;
         case MACRO1_TRIBYTE:
           str.append(
             "  .macro Tribyte1 (tribyte) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "  }\n\n"+
             "  .macro Tribyte2 (tribyte, tribyte2) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +
             "  }\n\n"+
              "  .macro Tribyte3 (tribyte, tribyte2, tribyte3) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" +        
             "  }\n\n"+
             "  .macro Tribyte4 (tribyte, tribyte2, tribyte3, tribyte4) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" +       
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +          
             "  }\n\n"+ 
             "  .macro Tribyte5 (tribyte, tribyte2, tribyte3, tribyte4, tribyte5) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" +       
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +  
             "     .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n" +         
             "  }\n\n"+        
             "  .macro Tribyte6 (tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" +       
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +  
             "     .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n" +  
             "     .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n" +                      
             "  }\n\n"+  
             "  .macro Tribyte7 (tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6, tribyte7) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" +       
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +  
             "     .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n" +  
             "     .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n" +     
             "     .byte tribyte7 >> 16, ( tribyte7 >> 8) & 255,  tribyte7 & 255\n" +           
             "  }\n\n"+      
             "  .macro Tribyte8 (tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6, tribyte7, tribyte8) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" +       
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +  
             "     .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n" +  
             "     .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n" +     
             "     .byte tribyte7 >> 16, ( tribyte7 >> 8) & 255,  tribyte7 & 255\n" +     
             "     .byte tribyte8 >> 16, ( tribyte8 >> 8) & 255,  tribyte8 & 255\n" +              
             "  }\n\n"                     
           );               
           break;       
         case MACRO3_TRIBYTE:
            str.append(
             "  .macro Tribyte1 tribyte \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "  .endmacro\n\n"+
             "  .macro Tribyte1 tribyte, tribyte2 \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +        
             "  .endmacro\n\n"+                    
             "  .macro Tribyte3 tribyte, tribyte2, tribyte3 \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +        
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" +          
             "  .endmacro\n\n"+                     
             "  .macro Tribyte4 tribyte, tribyte2, tribyte3, tribyte4 \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +        
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" + 
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +        
             "  .endmacro\n\n"+                       
             "  .macro Tribyte5 tribyte, tribyte2, tribyte3, tribyte4, tribyte5 \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +        
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" + 
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +   
             "     .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n" +         
             "  .endmacro\n\n"+  
             "  .macro Tribyte6 tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6 \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +        
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" + 
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +   
             "     .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n" +         
             "  .endmacro\n\n"+  
             "  .macro Tribyte7 tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6, tribyte7 \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +        
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" + 
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +   
             "     .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n" +    
             "     .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n" +                     
             "  .endmacro\n\n"+  
             "  .macro Tribyte8 tribyte, tribyte2, tribyte3, tribyte4, tribyte5, tribyte6, tribyte7, tribyte8 \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "     .byte tribyte2 >> 16, ( tribyte2 >> 8) & 255,  tribyte2 & 255\n" +        
             "     .byte tribyte3 >> 16, ( tribyte3 >> 8) & 255,  tribyte3 & 255\n" + 
             "     .byte tribyte4 >> 16, ( tribyte4 >> 8) & 255,  tribyte4 & 255\n" +   
             "     .byte tribyte5 >> 16, ( tribyte5 >> 8) & 255,  tribyte5 & 255\n" +    
             "     .byte tribyte6 >> 16, ( tribyte6 >> 8) & 255,  tribyte6 & 255\n" +
             "     .byte tribyte7 >> 16, ( tribyte7 >> 8) & 255,  tribyte7 & 255\n" +        
             "  .endmacro\n\n"                   
           );               
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
           str.append(("  long "));  
           break;             
         case DOT_LONG:
           str.append(("  .long "));   
           break;    
         case DOT_DC_L_LONG:
           str.append(("  .dc.l "));   
           break;  
         case DOT_DWORD_LONG:
           str.append(("  .dword "));  
           break;  
         case DOT_DLINT_LONG:
           str.append(("  .dlint "));  
           break;            
         case MARK_THIRTYTWO_LONG:
           str.append(("  !32 "));  
           break;                                  
         case MACRO4_LONG: 
           // we have a min of 1 or a max of 8 tribyte, so use the right call for macro
           int index=(int)(list.size()/4);
        
           str.append("  #Long").append(index).append(" ");  
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
             "Long6.macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 24, (\\2 >> 16) & 255, ( \\2 >> 8) & 255,  \\2 & 255\n" +  
             "     .byte \\3 >> 24, (\\3 >> 16) & 255, ( \\3 >> 8) & 255,  \\3 & 255\n" +      
             "     .byte \\4 >> 24, (\\4 >> 16) & 255, ( \\4 >> 8) & 255,  \\4 & 255\n" +    
             "     .byte \\5 >> 24, (\\5 >> 16) & 255, ( \\5 >> 8) & 255,  \\5 & 255\n" + 
             "     .byte \\6 >> 24, (\\6 >> 16) & 255, ( \\6 >> 8) & 255,  \\6 & 255\n" +         
             "  .endm\n\n"+  
             "Long7.macro \n" +
             "     .byte \\1 >> 24, (\\1 >> 16) & 255, ( \\1 >> 8) & 255,  \\1 & 255\n" +
             "     .byte \\2 >> 24, (\\2 >> 16) & 255, ( \\2 >> 8) & 255,  \\2 & 255\n" +  
             "     .byte \\3 >> 24, (\\3 >> 16) & 255, ( \\3 >> 8) & 255,  \\3 & 255\n" +      
             "     .byte \\4 >> 24, (\\4 >> 16) & 255, ( \\4 >> 8) & 255,  \\4 & 255\n" +    
             "     .byte \\5 >> 24, (\\5 >> 16) & 255, ( \\5 >> 8) & 255,  \\5 & 255\n" + 
             "     .byte \\6 >> 24, (\\6 >> 16) & 255, ( \\6 >> 8) & 255,  \\6 & 255\n" +        
             "     .byte \\7 >> 24, (\\7 >> 16) & 255, ( \\7 >> 8) & 255,  \\7 & 255\n" +           
             "  .endm\n\n"+    
             "Long8.macro \n" +
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
              str.append("  !24 $")
                           .append(ByteToExe(Unsigned.done(mem1.copy)))
                           .append(ByteToExe(Unsigned.done(mem2.copy)))
                           .append(ByteToExe(Unsigned.done(mem3.copy)))
                           .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case TWENTYFOUR_BIN:
              str.append("  !24 %")
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
              str.append("  MonoSpriteLine $")
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
              str.append("  MonoSpriteLine %")
                           .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                           .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                           .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                           .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;  
            case MACRO1_HEX:
              str.append("  MonoSpriteLine ($")
                           .append(ByteToExe(Unsigned.done(mem1.copy)))
                           .append(ByteToExe(Unsigned.done(mem2.copy)))
                           .append(ByteToExe(Unsigned.done(mem3.copy)))
                           .append(")  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO1_BIN:
              str.append("  MonoSpriteLine (")
                           .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                           .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                           .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                           .append(")  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break; 
            case MACRO2_HEX:
              str.append("  +MonoSpriteLine $")
                           .append(ByteToExe(Unsigned.done(mem1.copy)))
                           .append(ByteToExe(Unsigned.done(mem2.copy)))
                           .append(ByteToExe(Unsigned.done(mem3.copy)))
                           .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO2_BIN:
              str.append("  +MonoSpriteLine %")
                           .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                           .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                           .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                           .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;    
            case MACRO4_HEX:
              str.append("  #MonoSpriteLine $")
                           .append(ByteToExe(Unsigned.done(mem1.copy)))
                           .append(ByteToExe(Unsigned.done(mem2.copy)))
                           .append(ByteToExe(Unsigned.done(mem3.copy)))
                           .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO4_BIN:
              str.append("  #MonoSpriteLine %")
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
           str.append(
             "  .mac MonoSpriteLine \n" +
             "     .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             "  .endm \n\n"
           );
           break;
         case MACRO1_HEX:
         case MACRO1_BIN:
           str.append(
             "  .macro MonoSpriteLine (tribyte) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "  }\n\n"
           );               
           break;  
         case MACRO2_HEX:
         case MACRO2_BIN:
           str.append(
             "  !macro MonoSpriteLine tribyte {\n" +
             "     !byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "  }\n\n"
           );               
           break; 
         case MACRO3_HEX:
         case MACRO3_BIN:
           str.append(
             "  .macro MonoSpriteLine tribyte \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "  .endmacro\n\n"
           );               
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
              str.append("  !24 $")
                           .append(ByteToExe(Unsigned.done(mem1.copy)))
                           .append(ByteToExe(Unsigned.done(mem2.copy)))
                           .append(ByteToExe(Unsigned.done(mem3.copy)))
                           .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case TWENTYFOUR_BIN:
              str.append("  !24 %")
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
              str.append("  MultiSpriteLine $")
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
              str.append("  MultiSpriteLine %")
                           .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                           .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                           .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                           .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;  
            case MACRO1_HEX:
              str.append("  MultiSpriteLine ($")
                           .append(ByteToExe(Unsigned.done(mem1.copy)))
                           .append(ByteToExe(Unsigned.done(mem2.copy)))
                           .append(ByteToExe(Unsigned.done(mem3.copy)))
                           .append(")  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO1_BIN:
              str.append("  MultiSpriteLine (")
                           .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                           .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                           .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                           .append(")  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break; 
            case MACRO2_HEX:
              str.append("  +MultiSpriteLine $")
                           .append(ByteToExe(Unsigned.done(mem1.copy)))
                           .append(ByteToExe(Unsigned.done(mem2.copy)))
                           .append(ByteToExe(Unsigned.done(mem3.copy)))
                           .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO2_BIN:
              str.append("  +MultiSpriteLine %")
                           .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                           .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                           .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                           .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               break;   
            case MACRO4_HEX:
              str.append("  #MultiSpriteLine $")
                           .append(ByteToExe(Unsigned.done(mem1.copy)))
                           .append(ByteToExe(Unsigned.done(mem2.copy)))
                           .append(ByteToExe(Unsigned.done(mem3.copy)))
                           .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              break;
            case MACRO4_BIN:
              str.append("  #MultiSpriteLine %")
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
           str.append(
             " .mac MultiSpriteLine \n" +
             "    .byte {1} >> 16, ( {1} >> 8) & 255,  {1} & 255\n" +
             " .endm \n\n"
           );
           break;
         case MACRO1_HEX:
         case MACRO1_BIN:
           str.append(
             "  .macro MultiSpriteLine (tribyte) {\n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "  }\n\n"
           );               
           break;  
         case MACRO2_HEX:
         case MACRO2_BIN:
           str.append(
             "  !macro MultiSpriteLine tribyte {\n" +
             "     !byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "  }\n\n"
           );               
           break;      
         case MACRO3_HEX:
         case MACRO3_BIN:
           str.append(
             "  .macro MultiSpriteLine tribyte \n" +
             "     .byte tribyte >> 16, ( tribyte >> 8) & 255,  tribyte & 255\n" +
             "  .endmacro\n\n"
           );  
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
     
   /** Assembler lobg type */
   protected static Assembler.Long aLong;  
   
   /** Assembler mono color sprite*/
   protected static Assembler.MonoSprite aMonoSprite;
   
   /** Asembler multi color sprite */
   protected static Assembler.MultiSprite aMultiSprite;
              
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
    * @param aLong the long type to use
    * @param aTribyte the tribyte type to use
    * @param aMonoSprite the mono sprite type to use
    * @param aMultiSprite the numti sprite type to use
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
                         Assembler.MonoSprite aMonoSprite,
                         Assembler.MultiSprite aMultiSprite) {
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
     Assembler.aMonoSprite=aMonoSprite;
     Assembler.aMultiSprite=aMultiSprite;
     
      
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
     
     for (MemoryDasm mem:memory) {
       if (mem.dataType==DataType.MONO_SPRITE) hasMonoSprite=true;
       if (mem.dataType==DataType.MULTI_SPRITE) hasMultiSprite=true;
       if (mem.dataType==DataType.SWAPPED) hasWordSwapped=true;
       if (mem.dataType==DataType.TRIBYTE) hasTribyte=true;
       if (mem.dataType==DataType.LONG) hasLong=true;
     }
     
     if (hasMonoSprite) aMonoSprite.setting(str);
     if (hasMultiSprite) aMultiSprite.setting(str);
     if (hasWordSwapped) aWordSwapped.setting(str);
     if (hasTribyte) aTribyte.setting(str);
     if (hasLong) aLong.setting(str);
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
         return aByte;
       case WORD:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         return aWord;
       case TRIBYTE:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         return aTribyte;  
       case LONG:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         return aLong;        
       case MONO_SPRITE:
         if (!isMonoSpriteBlock) sizeMonoSpriteBlock=0;  
         isMonoSpriteBlock=true;  
         isMultiSpriteBlock=false;
         sizeMonoSpriteBlock++;
         return aMonoSprite;
       case MULTI_SPRITE:
         if (!isMultiSpriteBlock) sizeMultiSpriteBlock=0; 
         isMultiSpriteBlock=true; 
         isMonoSpriteBlock=false;
         sizeMultiSpriteBlock++;
         return aMultiSprite;  
     }
     
     // default is of Byte type
     isMonoSpriteBlock=false;
     isMultiSpriteBlock=false; 
     return aByte;
   }   
}
               

