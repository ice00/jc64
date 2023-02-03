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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import sw_emulator.math.Unsigned;
import sw_emulator.swing.main.Carets;
import sw_emulator.swing.main.Carets.Type;
import sw_emulator.swing.main.Constant;
import sw_emulator.swing.main.DataType;
import sw_emulator.swing.main.Option;

/**
 * Assembler builder.
 * It manages the creation of a source output based onto rules
 * 
 * @author ice
 */
public class Assembler {
   private static final String SPACES="                                                                                                                        ";  
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
   * Return spaces/tabs to use in comment after data
   * 
   * @param skip dimension to skip
   * @return the spaces/tabs
   */
  protected static String getDataCSpacesTabs(int skip) {
    return SPACES.substring(0, (option.numDataCSpaces-skip<0 ? 1: option.numDataCSpaces-skip))+TABS.substring(0, option.numDataCTabs);
  }   
    
  /**
   * Return spaces/tabs to use in comment after instruction
   * 
   * @param skip amount to skip
   * @return the spaces/tabs
   */
  protected String getInstrCSpacesTabs(int skip) {
    return SPACES.substring(0, (option.numInstrCSpaces-skip<0 ? 1:option.numInstrCSpaces-skip))+TABS.substring(0, option.numInstrCTabs);
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
   * Convert an 8 bit 0/1 string to monocolor dots
   * 
   * @param bin the bin to convert
   * @return the converted peace
   */
  protected static String BinToMono(String bin) {
    String d0="";
    String d1="";    
      
    switch (option.dotsType) {
        case Option.DOTS_ASCII:
          d0=".";  
          d1="*";
          break;  
        case Option.DOTS_UTF16:
          d0="\u2591";  
          d1="\u2589";  
          break;  
    }                   
      
    return bin.replace("0", d0).replace("1", d1);
  }
                            
  
  /**
   * Convert an 8 bit 0/1 string to multicolor dots
   * 
   * @param bin the bin to convert
   * @return the converted peace
   */
  protected static String BinToMulti(String bin) {
    String d00="";
    String d11="";
    String d01="";
    String d10="";
      
    switch (option.dotsType) {
        case Option.DOTS_ASCII:
          d00="..";  
          d01="@@";
          d10="##";
          d11="**";
          break;  
        case Option.DOTS_UTF16:
          d00="\u2591\u2591";  
          d01="\u2592\u2592";
          d10="\u2593\u2593";
          d11="\u2589\u2589";  
          break;  
    }
      
    String p0=bin.substring(0, 2).replace("00", d00).replace("11", d11).replace("01", d01).replace("10", d10);
    String p1=bin.substring(2, 4).replace("00", d00).replace("11", d11).replace("01", d01).replace("10", d10);
    String p2=bin.substring(4, 6).replace("00", d00).replace("11", d11).replace("01", d01).replace("10", d10);
    String p3=bin.substring(6, 8).replace("00", d00).replace("11", d11).replace("01", d01).replace("10", d10);
    
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
     },     
     GLASS {
       @Override
       public String getName() {
         return "Glass";
       }
     }
     
     ;        
    
     /**
      * Get the name of assembler
      *
      * @return the name of assembler
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
      FAKEZ,            // cpu equ 80
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
          case FAKEZ:
            str.append(getDataSpacesTabs()).append("cpu: equ 80\n\n");
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
        
        int start=str.length();
         
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
        
        carets.add(start, str.length(), lastMem, Type.LABEL);
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
        if (lastMem.userComment != null /*&& !"".equals(lastMem.userComment)*/) comment=lastMem.userComment;
        
        if (comment==null || "".equals(comment)) {
          str.append("\n");
          return;
        }
        
        int start=str.length();
        
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
        
        carets.add(start, str.length(), lastMem, Type.COMMENT);
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
    *  -> #if UNDEF xxx #endif
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
      SHARP_IF,        // #if UNDEF xxx #endif
      MARK_IF,         // !if 0 { xxx }
      DOT_COMMENT;     // .comment xxx .endc// .comment xxx .endc
    
      @Override
      public void flush(StringBuilder str) {
        if (lastMem==null || lastMem.userBlockComment==null) return;
        
        int start=str.length();
          
        // split by new line
        String[] lines = lastMem.userBlockComment.split("\\r?\\n");  
        
        // there macro in comment?
        if (lastMem.userBlockComment.contains("[<")) {
            
          // expand them  
          String tmp;
          String[] tmpLines;
          
          ArrayList<String> alist=new ArrayList();
          for (String line : lines) {
            tmp=getMacro(line);
            // if length differs, then it was exploded
            if (tmp.length()>line.length()) {
              tmpLines=tmp.split("\\r?\\n");
              for (String tmpLine: tmpLines) {
                alist.add(tmpLine);   
              }
            } else alist.add(tmp);
          }
          
          // give back the list of (exploed) lines
          lines=new String[alist.size()];
          for (int i=0; i<lines.length; i++) {
            lines[i]=alist.get(i);
          }
        }
     
        switch (aBlockComment) {
          case SEMICOLON:    
            for (String line : lines) {
              if (" ".equals(line)) str.append("\n");
              else str.append(";").append(line).append("\n");   
            }                      
            break;  
         case DOUBLE_BAR:    
            for (String line : lines) {
              if (" ".equals(line)) str.append("\n");
              else str.append("//").append(line).append("\n");   
            }                      
            break;           
          case CSTYLE:
            boolean isOpen=false;
            for (String line : lines) {
              if (" ".equals(line)) {
                if (isOpen) {
                  str.append("*/\n\n");
                  isOpen=false;
                }
                else str.append("\n\n");                
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
              if (" ".equals(line)) {
                if (isOpen) {
                  str.append(" endif\n\n");
                  isOpen=false;
                }
                else str.append("\n\n");    
              } else {
                  if (!isOpen) {
                    isOpen=true;
                    str.append(" if 0\n");
                  }
                  str.append(line).append("\n");
                }   
            }        
            if (isOpen) str.append(" endif\n");   
            break;
          case DOT_IF:
            isOpen=false;
            for (String line : lines) {
              if (" ".equals(line)) {
                if (isOpen) {
                  str.append(".endif\n\n");
                  isOpen=false;
                }
                else str.append("\n\n");    
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
              if (" ".equals(line)) {
                if (isOpen) {
                  str.append(".fi\n\n");
                  isOpen=false;
                }
                else str.append("\n\n");    
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
          case SHARP_IF:
            isOpen=false;
            for (String line : lines) {
              if (" ".equals(line)) {
                if (isOpen) {
                  str.append("#endif\n\n");
                  isOpen=false;
                }
                else str.append("\n\n");    
              } else {
                  if (!isOpen) {
                    isOpen=true;
                    str.append("#if UNDEF \n");
                  }
                  str.append(line).append("\n");
                }   
            }        
            if (isOpen) str.append("#endif\n");    
            break;             
          case MARK_IF:
            isOpen=false;
            for (String line : lines) {
              if (" ".equals(line)) {
                if (isOpen) {
                  str.append("}\n\n");
                  isOpen=false;
                }
                else str.append("\n\n");    
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
              if (" ".equals(line)) {
                if (isOpen) {
                  str.append(".comment\n\n");
                  isOpen=false;
                }
                else str.append("\n\n");    
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
              
        carets.add(start, str.length(), lastMem, Type.BLOCK_COMMENT);
      }
      
      /** Enum for direction of blocks in memory */
      enum Dir {NONE, UPDN, DNUP};
      
      /** Enum for char block type */
      enum Chars {NONE(0, 0), MONO(11, 7), MULTI(12, 7);
        public int left;
        public int right;
      
        Chars(int left, int right) {
          this.left=left;  
          this.right=right;
        }
      };      
      
      /** Enum for sprite block type */
      enum Sprites {NONE(0, 0), MONO(13, 7), MULTI(14, 7);
        public int left;
        public int right;
      
        Sprites(int left, int right) {
          this.left=left;  
          this.right=right;
        }
      };

      /**
       * Get the line or macro explosion
       * Possible macro: 
       * <ul>
       *  <li>[<CHAR#MONO#NxM#UPDN>]</li>
       *  <li>[<CHAR#MONO#NxM#DNUP>]</li>
       * <li>[<CHAR#MULTI#NxM#UPDN>]</li>
       * <li>[<CHAR#MULTI#NxM#DNUP>]</li>
       * <li>[<SPRITE#MONO#NxM#UPDN>]</li>
       * <li>[<SPRITE#MONO#NxM#DNUP>] </li>
       * <li>[<SPRITE#MULTI#NxM#UPDN>]</li>
       * <li>[<SPRITE#MULTI#NxM#DNUP>]</li>
       * </ul>
       * 
       * @param line the line to process
       * 
       * @return the line or macro explosion
       */
      private String getMacro(String line) {
        Dir dir; 
        Chars chars=Chars.NONE;
        Sprites sprites=Sprites.NONE;
        
        String uline=line.toUpperCase();

        // search for macro ending
        if (uline.endsWith("#UPDN>]")) dir=Dir.UPDN;
        else if (uline.endsWith("#DNUP>]")) dir=Dir.DNUP;
        else return line;          
          
        // look for chars
        if (uline.startsWith("[<CHAR#MONO#")) chars=Chars.MONO;
        if (uline.startsWith("[<CHAR#MULTI#")) chars=Chars.MULTI;
        
         // look for sprites
        if (uline.startsWith("[<SPRITE#MONO#")) sprites=Sprites.MONO;
        if (uline.startsWith("[<SPRITE#MULTI#")) sprites=sprites.MULTI;
                
        if (chars==Chars.NONE && sprites==Sprites.NONE) return line;
        
        //determine the NxM size
        String body="";
        if (chars!=Chars.NONE) body=uline.substring(chars.left+1, uline.length()-chars.right).trim();
        if (sprites!=Sprites.NONE) body=uline.substring(sprites.left+1, uline.length()-sprites.right).trim();   
        
        int pos=body.indexOf("X");
        if (pos<=0) return line;
        
        int left=0;
        int right=0;
        
        try {
          left=Integer.parseInt(body.substring(0, pos));
          right=Integer.parseInt(body.substring(pos+1));
        } catch (Exception e) {
          return line;
        }
        
        if (left==0 || right==0) return line;
        
        StringBuffer buf=new StringBuffer();    
        
        // abort if users try to force to go ovet the actual memory  
        try {
            //lastMem.address
         
          if (chars!=Chars.NONE) {
             // up to dx, then sx dn 
             if (dir==Dir.UPDN) {
               for (int r=0; r<right; r++) {  
                 for (int i=0; i<8; i++) {
                   buf.append(" ");
                   for (int c=0; c<left; c++) {
                     if (chars==Chars.MONO) buf.append(BinToMono(Integer.toBinaryString((memory[lastMem.address+8*(r*left+c)+i].copy & 0xFF) + 0x100).substring(1)));
                     else buf.append(BinToMulti(Integer.toBinaryString((memory[lastMem.address+8*(r*left+c)+i].copy & 0xFF) + 0x100).substring(1)));
                   }
                   buf.append("\n");
                 } 
               }  
             } else {
                 // dir is now sx to dn, then up to dx 
                 for (int r=0; r<right; r++) {  
                  for (int i=0; i<8; i++) {
                    buf.append(" ");
                    for (int c=0; c<left; c++) {
                      if (chars==Chars.MONO) buf.append(BinToMono(Integer.toBinaryString((memory[lastMem.address+8*(c*right+r)+i].copy & 0xFF) + 0x100).substring(1)));
                      else buf.append(BinToMulti(Integer.toBinaryString((memory[lastMem.address+8*(c*right+r)+i].copy & 0xFF) + 0x100).substring(1)));
                    }
                    buf.append("\n");
                   } 
                 }  
               }
             
             return buf.toString();
          }
          
          if (sprites!=Sprites.NONE) {
             // up to dx, then sx dn 
             if (dir==Dir.UPDN) {
               for (int r=0; r<right; r++) {  
                 for (int i=0; i<21; i++) {
                   buf.append(" ");
                   for (int c=0; c<left; c++) {
                     if (sprites==Sprites.MONO) {
                        buf.append(BinToMono(Integer.toBinaryString((memory[lastMem.address+64*(r*left+c)+(i*3)].copy & 0xFF) + 0x100).substring(1)));
                        buf.append(BinToMono(Integer.toBinaryString((memory[lastMem.address+64*(r*left+c)+(i*3)+1].copy & 0xFF) + 0x100).substring(1)));
                        buf.append(BinToMono(Integer.toBinaryString((memory[lastMem.address+64*(r*left+c)+(i*3)+2].copy & 0xFF) + 0x100).substring(1)));
                     } else {
                          buf.append(BinToMulti(Integer.toBinaryString((memory[lastMem.address+64*(r*left+c)+(i*3)].copy & 0xFF) + 0x100).substring(1)));
                          buf.append(BinToMulti(Integer.toBinaryString((memory[lastMem.address+64*(r*left+c)+(i*3)+1].copy & 0xFF) + 0x100).substring(1)));
                          buf.append(BinToMulti(Integer.toBinaryString((memory[lastMem.address+64*(r*left+c)+(i*3)+2].copy & 0xFF) + 0x100).substring(1)));
                       }
                   }
                   buf.append("\n");
                 } 
               }  
             } else {
                 // dir is now sx to dn, then up to dx 
                 for (int r=0; r<right; r++) {  
                  for (int i=0; i<21; i++) {
                    buf.append(" ");
                    for (int c=0; c<left; c++) {
                      if (sprites==Sprites.MONO) {
                        buf.append(BinToMono(Integer.toBinaryString((memory[lastMem.address+64*(c*right+r)+(i*3)].copy & 0xFF) + 0x100).substring(1)));
                        buf.append(BinToMono(Integer.toBinaryString((memory[lastMem.address+64*(c*right+r)+(i*3)+1].copy & 0xFF) + 0x100).substring(1)));
                        buf.append(BinToMono(Integer.toBinaryString((memory[lastMem.address+64*(c*right+r)+(i*3)+2].copy & 0xFF) + 0x100).substring(1)));
                      } else {
                          buf.append(BinToMulti(Integer.toBinaryString((memory[lastMem.address+64*(c*right+r)+(i*3)].copy & 0xFF) + 0x100).substring(1)));
                          buf.append(BinToMulti(Integer.toBinaryString((memory[lastMem.address+64*(c*right+r)+(i*3)+1].copy & 0xFF) + 0x100).substring(1)));
                          buf.append(BinToMulti(Integer.toBinaryString((memory[lastMem.address+64*(c*right+r)+(i*3)+2].copy & 0xFF) + 0x100).substring(1)));                          
                        }
                    }
                    buf.append("\n");
                   } 
                 }  
               }
             
             return buf.toString();
          }
        } catch (Exception e) {
            return line;
        }
          
        return line;  
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
      MARK_BY_BYTE,       //  !by $xx
      EIGHT_BYTE,         //    !8 $xx   
      ZEROEIGHT_BYTE,     //   !08 $xx   
      DB_BYTE;            //    db $xx
      
      @Override
      public void flush(StringBuilder str) {
        if (list.isEmpty()) return; 
        
        MemoryDasm mem;
        MemoryDasm memRel;
        MemoryDasm memRel2;
        
        int initial=str.length();
        int start=initial;
        
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
          case DB_BYTE:
            str.append(getDataSpacesTabs()).append(("db "));    
            break;  
        }
          
        Iterator<MemoryDasm> iter=list.iterator();
        while (iter.hasNext()) {
          // accodate each bytes in the format choosed
          mem=iter.next();
          memRel=listRel.pop();
          memRel2=listRel2.pop();
          
          if (mem.type=='<' || mem.type=='>' || mem.type=='^' || mem.type=='\\') {    
            char type;
            // add a automatic label onto references byte
            if (memRel.dasmLocation==null || "".equals(memRel.dasmLocation)) {
              memRel.dasmLocation="W"+ShortToExe(memRel.address);
            } 
            switch (mem.type) {
              case '^':
                  type='>';                  
                  break;
              case '\\':
                  type='<';                  
                  break;
              default:
                  type=mem.type;
                  break;
            }
            
            if (memRel2!=null) {                
              switch (memRel.type) {
                case '+':
                  /// this is a memory in table label
                  int pos=memRel.address-memRel.related;
                  if (memRel2.userLocation!=null && !"".equals(memRel2.userLocation)) str.append(getRightType(type, memRel2.userLocation+"+"+pos));
                  else if (memRel2.dasmLocation!=null && !"".equals(memRel2.dasmLocation)) str.append(getRightType(type, memRel2.dasmLocation+"+"+pos));
                  else str.append(getRightType(type,"$"+ShortToExe((int)memRel.related)+"+"+pos));
                  break;
                case '^':
                case '\\':    
                  /// this is a memory in table label
                  int rel=(memRel.related>>16) & 0xFFFF;
                  pos=memRel.address-rel;
                  if (memRel2.userLocation!=null && !"".equals(memRel2.userLocation)) str.append(getRightType(type,memRel2.userLocation+"+"+pos));
                  else if (memRel2.dasmLocation!=null && !"".equals(memRel2.dasmLocation)) str.append(getRightType(type,memRel2.dasmLocation+"+"+pos));
                  else str.append(getRightType(type,"$"+ShortToExe(rel)+"+"+pos));
                  break;
                case '-':
                  /// this is a memory in table label
                  pos=memRel.address-memRel.related;
                  if (memRel2.userLocation!=null && !"".equals(memRel2.userLocation)) str.append(getRightType(type,memRel2.userLocation+pos));  
                  else if (memRel2.dasmLocation!=null && !"".equals(memRel2.dasmLocation)) str.append(getRightType(type,memRel2.dasmLocation+pos));
                  else str.append(getRightType(type,"$"+ShortToExe((int)memRel.related)+pos));
                  break;             
                default:
                  if (memRel.userLocation!=null && !"".equals(memRel.userLocation)) str.append(getRightType(type,memRel.userLocation));
                  else if (memRel.dasmLocation!=null && !"".equals(memRel.dasmLocation)) str.append(getRightType(type,memRel.dasmLocation));
                  else str.append(getRightType(type,"$"+ShortToExe(memRel.address)));          
                  break;
              }                
            }           
            else if (memRel.userLocation!=null && !"".equals(memRel.userLocation)) str.append(getRightType(type,memRel.userLocation));
            else if (memRel.dasmLocation!=null && !"".equals(memRel.dasmLocation)) str.append(getRightType(type,memRel.dasmLocation));
            else str.append(getRightType(type,"$"+ShortToExe(memRel.address)));              
          } else str.append(getByteType(mem.dataType, mem.copy, mem.index));
          
          carets.add(start, str.length(), mem, Type.BYTE);
          
          if (!listRel.isEmpty()) str.append(", ");  
          else {
            if (mem.dasmLocation==null && mem.userLocation==null) {
              str.append(getDataCSpacesTabs(str.length()-initial-getDataSpacesTabs().length()));
              MemoryDasm tmp=lastMem;
              lastMem=mem;
              aComment.flush(str);  
              lastMem=tmp;
            } else str.append("\n");            
          }
          
          start=str.length();
        }
        list.clear();
      }  
      
      /**
       * Get the right type representation for M6502 or Z80 representation
       * In M6502: <xx, >xx
       * In Z80:   xx,  xx>>8
       * 
       * @param type the type to valuate
       * @param value the value 
       * @return the right type value
       */
      private String getRightType(char type, String value) {
        switch (aByte) {
            case DB_BYTE:
              if (type=='<') return value;
              else return value+">>8";
            default:
              return type+value;              
        }  
      }
      
      /**
       * Return the byte represented as by the given type
       * 
       * @param dataType the type to use 
       * @param value the byte value
       * @param index the index of constant
       * @return the converted string
       */
      private String getByteType(DataType dataType, byte value, byte index) {
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
            if (index!=-1) {
              String res=constant.table[index][value & 0xFF];  
              if (res!=null && !"".equals(res)) return res;
            } 
            
            switch (dataType)   {
              case BYTE_DEC:
                return ""+Unsigned.done(value);
              case BYTE_BIN:
                return "%"+Integer.toBinaryString((value & 0xFF) + 0x100).substring(1);
              case BYTE_CHAR:
                int val=(value & 0xFF);
                switch (option.assembler) {
                  case DASM:
                    if (
                       (!option.allowUtf && (val<0x20 || val==0x22 || (val>127))) ||     
                       (option.allowUtf && ((val==0x00) || (val==0x0A) || (val==0x22) || (val>127)))  
                       ) return "$"+ByteToExe(Unsigned.done(value));
                    else return "'"+(char)Unsigned.done(value); 
                  case TMPX:
                    if (
                        (!option.allowUtf && (val<=0x19) || (val==0x22) || (val>127)) ||    
                        (option.allowUtf && ((val==0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                       ) return "$"+ByteToExe(Unsigned.done(value));
                    else return "'"+(char)Unsigned.done(value); 
                  case CA65:
                    if (
                        (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                        (option.allowUtf && ((val==0x0A) || (val==0x22) || (val>127)))   
                       ) return "$"+ByteToExe(Unsigned.done(value));
                    else if (val>=0x20) return "'"+(char)Unsigned.done(value)+"'"; 
                         else return "\""+(char)Unsigned.done(value)+"\""; 
                  case ACME:
                    if (
                        (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                        (option.allowUtf && ( (val==0x00) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                       ) return "$"+ByteToExe(Unsigned.done(value));
                    else  if (val==0x27 || val==0x5C) return "'\\"+(char)Unsigned.done(value)+"'"; 
                          else return "'"+(char)Unsigned.done(value)+"'";   
                  case KICK:
                    if (!option.allowUtf &&  (val<=0x1F || val>=0x80)) return "$"+ByteToExe(Unsigned.done(value));                      
                    if (val==0x0A || (val>=0x0C && val<=0x0F) 
                                  || val==0x040 || val==0x05B 
                                  || val==0x05D
                                  || (val>=0x61 && val<=0x7A)  
                                  || val==0x7F
                                  || val==0xA0
                                  || val==0xA3
                        ) return "$"+ByteToExe(Unsigned.done(value));
                    else return "'"+(char)Unsigned.done(value)+"'";      
                  case TASS64:
                    if (
                        (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) || 
                        (option.allowUtf && ((val==0x0A) || (val==0x0D) ||
                         (val==0x22) || (val>127)))                      
                       ) return "$"+ByteToExe(Unsigned.done(value));    
                    else return "\""+(char)Unsigned.done(value)+"\"";
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
     SIXTEEN_WORD,        //    !16 $xxyy
     DW_WORD;             //     dw $xxyy
     
     @Override
     public void flush(StringBuilder str) {         
       if (list.isEmpty()) return; 
       
       MemoryDasm memLow;
       MemoryDasm memHigh;
       MemoryDasm memRelLow;
       MemoryDasm memRelHigh;
       MemoryDasm memRel2Low;
       MemoryDasm memRel2High;
     
       int pos1=str.length();  // store initial position       
       int start=pos1;
       
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
         case DW_WORD:
           str.append(getDataSpacesTabs()).append(("dw "));   
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
           memRel2Low=listRel2.pop();
           
           memHigh=list.pop();
           memRelHigh=listRel.pop();           
           memRel2High=listRel2.pop();
           
           if ((memLow.type=='<' || memLow.type=='\\') && (memHigh.type=='>' || memHigh.type=='^') && (memLow.related & 0xFFFF)==(memHigh.related & 0xFFFF)) {
               
             if (memRel2Low!=null) {                
               switch (memRelLow.type) {
                 case '+':
                   /// this is a memory in table label
                   int pos=memRelLow.address-memRelLow.related;
                   if (memRel2Low.userLocation!=null && !"".equals(memRel2Low.userLocation)) str.append(memRel2Low.userLocation+"+"+pos);
                   else if (memRel2Low.dasmLocation!=null && !"".equals(memRel2Low.dasmLocation)) str.append(memRel2Low.dasmLocation+"+"+pos);
                   else str.append("$"+ShortToExe((int)memRelLow.related)+"+"+pos);
                   break;
                 case '^':
                 case '\\':    
                   /// this is a memory in table label
                   int rel=(memRelLow.related>>16) & 0xFFFF;
                   pos=memRelLow.address-rel;
                   if (memRel2Low.userLocation!=null && !"".equals(memRel2Low.userLocation)) str.append(memRel2Low.userLocation+"+"+pos);
                   else if (memRel2Low.dasmLocation!=null && !"".equals(memRel2Low.dasmLocation)) str.append(memRel2Low.dasmLocation+"+"+pos);
                   else str.append("$"+ShortToExe(rel)+"+"+pos);
                   break;
                 case '-':
                   /// this is a memory in table label
                   pos=memRelLow.address-memRelLow.related;
                   if (memRel2Low.userLocation!=null && !"".equals(memRel2Low.userLocation)) str.append(memRel2Low.userLocation+pos);  
                   else if (memRel2Low.dasmLocation!=null && !"".equals(memRel2Low.dasmLocation)) str.append(memRel2Low.dasmLocation+pos);
                   else str.append("$"+ShortToExe((int)memRelLow.related)+pos);
                   break;             
                 default:
                   if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation);
                   else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation);
                   else str.append("$"+ShortToExe(memRelLow.address));          
                   break;
               }                
             }  else {        
                  if (memRelLow.dasmLocation==null || "".equals(memRelLow.dasmLocation)) {
                     memRelLow.dasmLocation="W"+ShortToExe(memRelLow.address);
                  } 
              
                  if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation);
                  else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation);
                       else str.append("$").append(ShortToExe(memRelLow.address));  
                }
             isFirst=false;
           } else {
               // if cannot make a word with relative locations, force all to be of byte type
               if (memLow.type=='<'  || memLow.type=='>'  || memLow.type=='^'  || memLow.type=='\\'  ||
                   memHigh.type=='>' || memHigh.type=='<' || memHigh.type=='^' || memHigh.type=='\\' )  {
                 list.addFirst(memHigh);
                 list.addFirst(memLow);
                 listRel.addFirst(memRelHigh);
                 listRel.addFirst(memRelLow);
                 listRel2.addFirst(null);
                 listRel2.addFirst(null);
                 if (isFirst) {
                   str.replace(pos1, pos2, "");
                   isFirst=false;
                 }
                 aByte.flush(str);
               }
               else {
                 if (aWord==DOT_SINT && memHigh.copy<0) str.append("-$").append(ByteToExe(Math.abs(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));
                 else {
                   // look for constant  
                   if (memLow.index!=-1 && memHigh.index!=-1 && memLow.index==memHigh.index) {
                     String res=constant.table[memLow.index][(memLow.copy & 0xFF) + ((memHigh.copy & 0xFF)<<8)];  
                     if (res!=null && !"".equals(res)) str.append(res);
                     else str.append("$").append(ByteToExe(Unsigned.done(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));
                   } else str.append("$").append(ByteToExe(Unsigned.done(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));
                 }                            
                 isFirst=false;  
               }    
             }
           carets.add(start, str.length(), memLow, Type.WORD);
           
           
           if (list.size()>=2) str.append(", ");
           else {
             if (memHigh.dasmLocation==null && memHigh.userLocation==null) {
               str.append(getDataCSpacesTabs(str.length()-pos1-getDataSpacesTabs().length()));
               MemoryDasm tmp=lastMem;
               lastMem=memHigh;
               aComment.flush(str);  
               lastMem=tmp;
             } else str.append("\n");            
           }
           
           start=str.length();
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
     MACRO5_WORD_SWAPPED,           //  [.amc] $yyxx    (Glass)
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
       int start=pos1;
        
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
           str.append(getDataSpacesTabs()).append("#Swapped").append(index).append(" ");    
           break;
         case MACRO5_WORD_SWAPPED:
           str.append(getDataSpacesTabs()).append("Swapped").append(index).append(" ");    
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
           listRel2.pop();
           
           memHigh=list.pop();
           memRelHigh=listRel.pop();  
           listRel2.pop();
           
           if ((memLow.type=='<' || memLow.type=='\\') && (memHigh.type=='>' || memHigh.type=='^') && 
              (memLow.related & 0xFFFF)==(memHigh.related & 0xFFFF)) {
             if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation);
             else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation);
                  else str.append("$").append(ShortToExe(memRelLow.address)); 
             isFirst=false;
           } else {
             // if cannot make a word with relative locations, force all to be of byte type
             if (memLow.type=='<' || memLow.type=='>' || memLow.type=='^' || memLow.type=='\\' || 
                 memHigh.type=='>' || memHigh.type=='<' || memHigh.type=='^' || memHigh.type=='\\')  {
               list.addFirst(memHigh);
               list.addFirst(memLow);
               listRel.addFirst(memRelHigh);
               listRel.addFirst(memRelLow);
               listRel2.addFirst(null);
               listRel2.addFirst(null);
               
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
           
           carets.add(start, str.length(), memLow, Type.WORD_SWAPPED);
           
           if (list.size()>=2) str.append(", ");           
           else {
            if (memHigh.dasmLocation==null && memHigh.userLocation==null) {
              str.append(getDataCSpacesTabs(str.length()-pos1-getDataSpacesTabs().length()));
              MemoryDasm tmp=lastMem;
              lastMem=memHigh;
              aComment.flush(str);  
              lastMem=tmp;
            } else if (aWordSwapped==MACRO1_WORD_SWAPPED) str.append(")\n");
                   else str.append("\n");   
            
            start=str.length();
          }                      
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
              .append(spaces).append("!macro Swapped5 twobyte, twobyte2, twobyte3, twobyte4, twobyte5 {\n")
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
              .append(spaces).append("!macro Swapped8 twobyte, twobyte2, twobyte3, twobyte4, twobyte5, twobyte6, twobyte7, twobyte8 {\n")
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
         case MACRO5_WORD_SWAPPED:
           str.append(spaces).append("Swapped1: macro ?twobyte \n")
              .append(spaces).append("   db ?twobyte & 255, ( ?twobyte >> 8) & 255\n")
              .append(spaces).append("endm\n\n")
              .append(spaces).append("Swapped2: macro ?twobyte, ?twobyte2 \n")
              .append(spaces).append("   db ?twobyte & 255, ( ?twobyte >> 8) & 255\n")
              .append(spaces).append("   db ?twobyte2 & 255, ( ?twobyte2 >> 8) & 255\n")        
              .append(spaces).append("endm\n\n")
              .append(spaces).append("Swapped3: macro ?twobyte, ?twobyte2, ?twobyte3 \n")
              .append(spaces).append("   db ?twobyte & 255, ( ?twobyte >> 8) & 255\n")
              .append(spaces).append("   db ?twobyte2 & 255, ( ?twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte3 & 255, ( ?twobyte3 >> 8) & 255\n")         
              .append(spaces).append("endm\n\n")
              .append(spaces).append("Swapped4: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4 \n")
              .append(spaces).append("   db ?twobyte & 255, ( ?twobyte >> 8) & 255\n")
              .append(spaces).append("   db ?twobyte2 & 255, ( ?twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte3 & 255, ( ?twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte4 & 255, ( ?twobyte4 >> 8) & 255\n")          
              .append(spaces).append("endm\n\n")
              .append(spaces).append("Swapped5: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4, ?twobyte5 \n")
              .append(spaces).append("   db ?twobyte & 255, ( ?twobyte >> 8) & 255\n")
              .append(spaces).append("   db ?twobyte2 & 255, ( ?twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte3 & 255, ( ?twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte4 & 255, ( ?twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   db ?twobyte5 & 255, ( ?twobyte5 >> 8) & 255\n")                      
              .append(spaces).append("endm\n\n")
              .append(spaces).append("Swapped6: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4, ?twobyte5, ?twobyte6 \n")
              .append(spaces).append("   db ?twobyte & 255, ( ?twobyte >> 8) & 255\n")
              .append(spaces).append("   db ?twobyte2 & 255, ( ?twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte3 & 255, ( ?twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte4 & 255, ( ?twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   db ?twobyte5 & 255, ( ?twobyte5 >> 8) & 255\n")  
              .append(spaces).append("   db ?twobyte6 & 255, ( ?twobyte6 >> 8) & 255\n")                      
              .append(spaces).append("endm\n\n")
              .append(spaces).append("Swapped7: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4, ?twobyte5, ?twobyte6, ?twobyte7 \n")
              .append(spaces).append("   db ?twobyte & 255, ( ?twobyte >> 8) & 255\n")
              .append(spaces).append("   db ?twobyte2 & 255, ( ?twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte3 & 255, ( ?twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte4 & 255, ( ?twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   db ?twobyte5 & 255, ( ?twobyte5 >> 8) & 255\n")  
              .append(spaces).append("   db ?twobyte6 & 255, ( ?twobyte6 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte7 & 255, ( ?twobyte7 >> 8) & 255\n")                      
              .append(spaces).append("endm\n\n")
              .append(spaces).append("Swapped8: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4, ?twobyte5, ?twobyte6, ?twobyte7, ?twobyte8 \n")
              .append(spaces).append("   db ?twobyte & 255, ( ?twobyte >> 8) & 255\n")
              .append(spaces).append("   db ?twobyte2 & 255, ( ?twobyte2 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte3 & 255, ( ?twobyte3 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte4 & 255, ( ?twobyte4 >> 8) & 255\n")  
              .append(spaces).append("   db ?twobyte5 & 255, ( ?twobyte5 >> 8) & 255\n")  
              .append(spaces).append("   db ?twobyte6 & 255, ( ?twobyte6 >> 8) & 255\n") 
              .append(spaces).append("   db ?twobyte7 & 255, ( ?twobyte7 >> 8) & 255\n")  
              .append(spaces).append("   db ?twobyte8 & 255, ( ?twobyte8 >> 8) & 255\n")                    
              .append(spaces).append("endm\n\n");                                  
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
     MACRO5_TRIBYTE,           //  [.mac] $xxyyzz    (Glass)
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
       
       int initial=str.length();
       int start=initial;
       
       MemoryDasm mem;
       
       Iterator<MemoryDasm> iter=list.iterator();
       while (iter.hasNext()) {
         mem=iter.next();
         // we cannot handle memory reference inside tribyte
         if (mem.type=='<' || mem.type=='>' || mem.type=='^' || mem.type=='\\') {
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
         case MACRO5_TRIBYTE:  
           str.append(getDataSpacesTabs()).append("Tribyte").append(index).append(" ");  
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
           
           listRel2.pop();
           listRel2.pop();
           listRel2.pop();
           
           if (aTribyte==DOT_LINT_TRIBYTE && mem1.copy<0) {
              str.append("-$").append(ByteToExe(Math.abs(mem1.copy)))
                              .append(ByteToExe(Unsigned.done(mem2.copy)))
                              .append(ByteToExe(Unsigned.done(mem3.copy)));;
           } else {
               str.append("$").append(ByteToExe(Unsigned.done(mem1.copy)))
                              .append(ByteToExe(Unsigned.done(mem2.copy)))
                              .append(ByteToExe(Unsigned.done(mem3.copy)));
           }
           
           carets.add(start, str.length(), mem1, Type.TRIBYTE);
           
           if (list.size()>=3) str.append(", ");
           else {
            if (mem3.dasmLocation==null && mem3.userLocation==null) {
              str.append(getDataCSpacesTabs(str.length()-initial-getDataSpacesTabs().length()));
              MemoryDasm tmp=lastMem;
              lastMem=mem3;
              aComment.flush(str);  
              lastMem=tmp;
            } else if (aTribyte==MACRO1_TRIBYTE) str.append(")\n");
                else str.append("\n");       
           }  
           
           start=str.length();
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
               .append(spaces).append(".macro Tribyte2 tribyte, tribyte2 \n")
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
         case MACRO5_TRIBYTE:
            str.append(spaces).append("Tribyte1: macro ?tribyte \n")
               .append(spaces).append("  db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Tribyte2: macro ?tribyte, ?tribyte2 \n")
               .append(spaces).append("  db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
               .append(spaces).append("  db ?tribyte2 >> 16, ( ?tribyte2 >> 8) & 255,  ?tribyte2 & 255\n")
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Tribyte3: macro ?tribyte, ?tribyte2, ?tribyte3 \n")
               .append(spaces).append("  db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
               .append(spaces).append("  db ?tribyte2 >> 16, ( ?tribyte2 >> 8) & 255,  ?tribyte2 & 255\n")
               .append(spaces).append("  db ?tribyte3 >> 16, ( ?tribyte3 >> 8) & 255,  ?tribyte3 & 255\n")          
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Tribyte4: macro ?tribyte, ?tribyte2, ?tribyte3, ?tribyte4 \n")
               .append(spaces).append("  db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
               .append(spaces).append("  db ?tribyte2 >> 16, ( ?tribyte2 >> 8) & 255,  ?tribyte2 & 255\n")
               .append(spaces).append("  db ?tribyte3 >> 16, ( ?tribyte3 >> 8) & 255,  ?tribyte3 & 255\n") 
               .append(spaces).append("  db ?tribyte4 >> 16, ( ?tribyte4 >> 8) & 255,  ?tribyte4 & 255\n")        
               .append(spaces).append("endm\n\n")                       
               .append(spaces).append("Tribyte5: macro ?tribyte, ?tribyte2, ?tribyte3, ?tribyte4, ?tribyte5 \n")
               .append(spaces).append("  db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
               .append(spaces).append("  db ?tribyte2 >> 16, ( ?tribyte2 >> 8) & 255,  ?tribyte2 & 255\n")
               .append(spaces).append("  db ?tribyte3 >> 16, ( ?tribyte3 >> 8) & 255,  ?tribyte3 & 255\n")
               .append(spaces).append("  db ?tribyte4 >> 16, ( ?tribyte4 >> 8) & 255,  ?tribyte4 & 255\n")
               .append(spaces).append("  db ?tribyte5 >> 16, ( ?tribyte5 >> 8) & 255,  ?tribyte5 & 255\n")
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Tribyte6: macro ?tribyte, ?tribyte2, ?tribyte3, ?tribyte4, ?tribyte5, ?tribyte6 \n")
               .append(spaces).append("  db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
               .append(spaces).append("  db ?tribyte2 >> 16, ( ?tribyte2 >> 8) & 255,  ?tribyte2 & 255\n")
               .append(spaces).append("  db ?tribyte3 >> 16, ( ?tribyte3 >> 8) & 255,  ?tribyte3 & 255\n")
               .append(spaces).append("  db ?tribyte4 >> 16, ( ?tribyte4 >> 8) & 255,  ?tribyte4 & 255\n")
               .append(spaces).append("  db ?tribyte5 >> 16, ( ?tribyte5 >> 8) & 255,  ?tribyte5 & 255\n")  
               .append(spaces).append("  db ?tribyte6 >> 16, ( ?tribyte6 >> 8) & 255,  ?tribyte6 & 255\n")      
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Tribyte7: macro ?tribyte, ?tribyte2, ?tribyte3, ?tribyte4, ?tribyte5, ?tribyte6, ?tribyte7 \n")
               .append(spaces).append("  db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
               .append(spaces).append("  db ?tribyte2 >> 16, ( ?tribyte2 >> 8) & 255,  ?tribyte2 & 255\n")
               .append(spaces).append("  db ?tribyte3 >> 16, ( ?tribyte3 >> 8) & 255,  ?tribyte3 & 255\n") 
               .append(spaces).append("  db ?tribyte4 >> 16, ( ?tribyte4 >> 8) & 255,  ?tribyte4 & 255\n")   
               .append(spaces).append("  db ?tribyte5 >> 16, ( ?tribyte5 >> 8) & 255,  ?tribyte5 & 255\n")    
               .append(spaces).append("  db ?tribyte6 >> 16, ( ?tribyte6 >> 8) & 255,  ?tribyte6 & 255\n")  
               .append(spaces).append("  db ?tribyte7 >> 16, ( ?tribyte7 >> 8) & 255,  ?tribyte7 & 255\n")    
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Tribyte8: macro ?tribyte, ?tribyte2, ?tribyte3, ?tribyte4, ?tribyte5, ?tribyte6, ?tribyte7, ?tribyte8 \n")
               .append(spaces).append("  db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
               .append(spaces).append("  db ?tribyte2 >> 16, ( ?tribyte2 >> 8) & 255,  ?tribyte2 & 255\n")
               .append(spaces).append("  db ?tribyte3 >> 16, ( ?tribyte3 >> 8) & 255,  ?tribyte3 & 255\n") 
               .append(spaces).append("  db ?tribyte4 >> 16, ( ?tribyte4 >> 8) & 255,  ?tribyte4 & 255\n")   
               .append(spaces).append("  db ?tribyte5 >> 16, ( ?tribyte5 >> 8) & 255,  ?tribyte5 & 255\n")    
               .append(spaces).append("  db ?tribyte7 >> 16, ( ?tribyte7 >> 8) & 255,  ?tribyte7 & 255\n") 
               .append(spaces).append("  db ?tribyte8 >> 16, ( ?tribyte8 >> 8) & 255,  ?tribyte8 & 255\n")     
               .append(spaces).append("endm\n\n");               
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
     DD_LONG,              //     dd $xxyyzzkk
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
       
       int initial=str.length();
       int start=initial;
       
       MemoryDasm mem;
       
       Iterator<MemoryDasm> iter=list.iterator();
       while (iter.hasNext()) {
         mem=iter.next();
         // we cannot handle memory reference inside long
         if (mem.type=='<' || mem.type=='>' || mem.type=='^' || mem.type=='\\') {
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
         case DD_LONG:
           str.append(getDataSpacesTabs()).append("dd ");  
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
           
           listRel2.pop();
           listRel2.pop();
           listRel2.pop();
           listRel2.pop();
           
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
           
           carets.add(start, str.length(), mem1, Type.LONG);
           
           if (list.size()>=4) str.append(", ");
           else {
             if (mem4.dasmLocation==null && mem4.userLocation==null) {
               str.append(getDataCSpacesTabs(str.length()-initial-getDataSpacesTabs().length()));
               MemoryDasm tmp=lastMem;
               lastMem=mem4;
               aComment.flush(str);  
               lastMem=tmp;
             } else str.append("\n");       
           }
           
           start=str.length();           
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
     DW_ADDR,                  //     dw $xxyy
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
       int start=pos1;
       
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
         case DW_ADDR:
           str.append(getDataSpacesTabs()).append(("dw "));  
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
           listRel2.pop();
           
           memHigh=list.pop();
           memRelHigh=listRel.pop(); 
           listRel2.pop();
           
           if ((memLow.type=='<' || memLow.type=='\\') && (memHigh.type=='>' || memHigh.type=='^') && (memLow.related & 0xFFFF)==(memHigh.related & 0xFFFF)) {
               
             // add a automatic label onto references byte  
             if (memRelLow.dasmLocation==null || "".equals(memRelLow.dasmLocation)) {
                 memRelLow.dasmLocation="W"+ShortToExe(memRelLow.address);
             } 
             
             if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation);
             else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation);
                  else str.append("$").append(ShortToExe(memRelLow.address));  
             isFirst=false;
           } else {
               // if cannot make a word with relative locations, force all to be of byte type
               if (memLow.type=='<'  || memLow.type=='>' || memLow.type=='^' || memLow.type=='\\' ||
                   memHigh.type=='>' || memHigh.type=='<' || memHigh.type=='^' || memHigh.type=='\\')  {
                 list.addFirst(memHigh);
                 list.addFirst(memLow);
                 listRel.addFirst(memRelHigh);
                 listRel.addFirst(memRelLow);
                 listRel2.addFirst(null);
                 listRel2.addFirst(null);
                 
                 if (isFirst) {
                   str.replace(pos1, pos2, "");
                   isFirst=false;
                 }
                 aByte.flush(str);
               }
               else {
                 // look fopr constant  
                 if (memLow.index!=-1 && memHigh.index!=-1 && memLow.index==memHigh.index) {
                   String res=constant.table[memLow.index][(memLow.copy & 0xFF) + ((memHigh.copy & 0xFF)<<8)];  
                   if (res!=null && !"".equals(res)) str.append(res);
                   else str.append("$").append(ByteToExe(Unsigned.done(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));
                 } else str.append("$").append(ByteToExe(Unsigned.done(memHigh.copy))).append(ByteToExe(Unsigned.done(memLow.copy)));                         
                 isFirst=false;  
               }    
             }
           
           carets.add(start, str.length(), memLow, Type.ADDRESS);
           
           if (list.size()>=2) str.append(", ");
           else {
             if (memHigh.dasmLocation==null && memHigh.userLocation==null) {
               str.append(getDataCSpacesTabs(str.length()-pos1-getDataSpacesTabs().length()));
               MemoryDasm tmp=lastMem;
               lastMem=memHigh;
               aComment.flush(str);  
               lastMem=tmp;
             } else str.append("\n");       
           }
           
           start=str.length();
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
     MACRO5_HEX,    // [.macro] %xx.. (Glass)
     MACRO5_BIN,    // [.macro] %b..  (Glass)
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
          mem3.dasmComment=BinToMono(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))+
                           BinToMono(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))+
                           BinToMono(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1));
          lastMem=mem3;
          
          int initial=str.length();
          int start=initial;
          
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
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
               
               listRel2.pop();
               listRel2.pop();
               listRel2.pop();
               break;                
            case MACRO_HEX:
            case MACRO3_HEX: 
            case MACRO5_HEX:
              str.append(getDataSpacesTabs())
                 .append("MonoSpriteLine $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
              break;
            case MACRO_BIN:
            case MACRO3_BIN:
            case MACRO5_BIN:    
              str.append(getDataSpacesTabs())
                 .append("MonoSpriteLine %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
               listRel.pop();
               listRel.pop();
               listRel.pop();  
               
               listRel2.pop();
               listRel2.pop();
               listRel2.pop();
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
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
               
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
               
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
               
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
              break;               
            }
          carets.add(start, str.length(), mem1, Type.MONO_SPRITE);
          ///start=str.length();
          
          str.append(getDataCSpacesTabs(str.length()-initial-getDataSpacesTabs().length()));
          // flush comment only for macro as byte flush it itself
          if (aMonoSprite!=BYTE_HEX && aMonoSprite!=BYTE_BIN) aComment.flush(str);
          else str.append("\n");
          
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
         case MACRO5_HEX:  
         case MACRO5_BIN:          
           str.append(getDataSpacesTabs()).append("MonoSpriteLine: macro ?tribyte \n")
              .append(getDataSpacesTabs()).append(" db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
              .append(getDataSpacesTabs()).append("endm\n\n");                         
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
     MACRO5_HEX,    // [.macro] %xx.. (Glass)
     MACRO5_BIN,    // [.macro] %b..  (Glass)
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
          
          int initial=str.length();
          int start=initial;
          
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
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
               
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
              break;               
            case MACRO_HEX:
            case MACRO3_HEX:    
            case MACRO5_HEX:
              str.append(getDataSpacesTabs()).append("MultiSpriteLine $")
                 .append(ByteToExe(Unsigned.done(mem1.copy)))
                 .append(ByteToExe(Unsigned.done(mem2.copy)))
                 .append(ByteToExe(Unsigned.done(mem3.copy)))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
              break;
            case MACRO_BIN:
            case MACRO3_BIN:  
            case MACRO5_BIN:    
              str.append(getDataSpacesTabs()).append("MultiSpriteLine %")
                 .append(Integer.toBinaryString((mem1.copy & 0xFF) + 0x100).substring(1))
                 .append(Integer.toBinaryString((mem2.copy & 0xFF) + 0x100).substring(1))        
                 .append(Integer.toBinaryString((mem3.copy & 0xFF) + 0x100).substring(1))
                 .append("  ");
              listRel.pop();
              listRel.pop();
              listRel.pop();  
               
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
               
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
               
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
              
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
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
               
              listRel2.pop();
              listRel2.pop();
              listRel2.pop();
              break;               
            }      
          carets.add(start, str.length(), mem1, Type.MULTI_SPRITE);
          
          str.append(getDataCSpacesTabs(str.length()-initial-getDataSpacesTabs().length()));
          // flush comment only for macro as byte flush it itself
          if (aMultiSprite!=BYTE_HEX && aMultiSprite!=BYTE_BIN) aComment.flush(str);
          else str.append("\n");
          
          //start=str.length();
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
         case MACRO5_HEX:  
         case MACRO5_BIN:          
           str.append(getDataSpacesTabs()).append("MultiSpriteLine: macro ?tribyte \n")
              .append(getDataSpacesTabs()).append(" db ?tribyte >> 16, ( ?tribyte >> 8) & 255,  ?tribyte & 255\n")
              .append(getDataSpacesTabs()).append("endm\n\n");                         
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
      DB_BYTE_TEXT,       //    db "xxx"
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
        int start=pos1;
               
        switch (aText) {
          case DOT_BYTE_TEXT:
            str.append(getDataSpacesTabs()).append((".byte "));
            break;
          case DOT_BYT_TEXT:
            str.append(getDataSpacesTabs()).append((".byt "));
            break;  
          case DB_BYTE_TEXT:
            str.append(getDataSpacesTabs()).append(("db "));  
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
          listRel2.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if (
                 (!option.allowUtf && ((val<0x20 || val==0x22 || (val>127)))) ||     
                 (option.allowUtf && ((val==0x00) || (val==0x0A) || (val==0x22) || (val>127)))  
                 )     
              {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }                  
              break;
            case TMPX:
              if (
                  (!option.allowUtf && ((val<0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 ) {                                    
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
                  listRel2.push(null);
                  
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
                  str.append((char)val);  
                }                  
              break;  
            case CA65:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x0A) || (val==0x22) || (val>127)))   
                 ) {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }     
              break;
            case ACME:                
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ( (val==0x00) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 ) {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                 if  (val==0x5C) str.append("\\");
                 str.append((char)val);  
                }                  
              break;              
            case KICK:
              if (isFirst) {
                position=str.length();
                str.append("@\"");
                isString=true;
                isFirst=false;  
              }    
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x40) ||
                    (val==0x5B) ||
                    (val==0x5D) ||
                    (val>=0x61 && val<=0x7A) ||
                    (val==0x7F) ||
                    (val>=0xA0))) ||    
                  (option.allowUtf && ((val<=0x02) ||
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
                    (val==0xA3)))
                  )                      
                   {
                str.append("\\$").append(ByteToExe(val));   
                isSpecial=true;
              } else if (val==0x22) {
                       str.append("\\\"");
                       isSpecial=true;
                     }
                else if (val==0x5C) { 
                       str.append("\\\\");
                       isSpecial=true;
                     }
                else str.append((char)val);                
              break; 
            case TASS64:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) || 
                  (option.allowUtf && ((val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)))                      
                 ) {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }   
              break;    
            case GLASS:
              // don't use allowUTF  
              if (
                  (val!=0x00 && val!=0x07 && val!=0x09 && val!=0x0A && val!=0x0C && val!=0x0D && val!=0x1B && val!=0x27) &&     
                 ((( val<0x20 || (val>127))))   
                 )     
              {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                 
                 switch (val) {
                   case 0x00:
                     str.append("\\0");  
                     break;     
                   case 0x07:
                      str.append("\\a"); 
                      break;
                   case 0x09:
                      str.append("\\t"); 
                      break;  
                   case 0x0A:
                      str.append("\\n"); 
                      break;  
                   case 0x0C:
                      str.append("\\f"); 
                      break; 
                   case 0x0D:
                      str.append("\\r"); 
                      break;  
                   case 0x1B:
                      str.append("\\e"); 
                      break;  
                   case 0x27:
                      str.append("\\'"); 
                      break; 
                   case 0x22:
                      str.append("\\\""); 
                      break;     
                   case 0x5C:
                      str.append("\\\\");
                      break;
                   default:
                      str.append((char)val); 
                      break;
                  }                                           
                }                  
              break;
          }         
          carets.add(start, str.length(), mem, Type.TEXT);
          
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");
            if (option.assembler==Assembler.Name.KICK && !isSpecial) str.setCharAt(position, ' ');
          }
          
          start=str.length();
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
     DB_BYTE_NUMTEXT,         // ->     db "xxx"
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
       int start=pos1;
         
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
         case DB_BYTE_NUMTEXT:
            str.append(getDataSpacesTabs()).append(("db "));  
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
         listRel2.pop();
       }
      
        while (!list.isEmpty()) {
          // accodate each bytes in the format choosed
          mem=list.pop();
          memRel=listRel.pop();
          listRel2.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if (isFirst) {  
               str.append("$").append(ByteToExe(val)); 
               isFirst=false;                   
              } else {  
                  if (
                      (!option.allowUtf && (val<0x20 || val==0x22 || (val>127))) ||     
                      (option.allowUtf && ((val==0x00) || (val==0x0A) || (val==0x22) || (val>127)))  
                     )  {
                      if (isString) {
                        str.append("\"");
                        isString=false;  
                      }
                      if (isFirst) {
                        str.append("$").append(ByteToExe(val)); 
                        isFirst=false;
                      } else str.append(", $").append(ByteToExe(val));      
                  } else {
                    if (isFirst) {
                         isFirst=false;
                         isString=true;
                         str.append("\"");
                    } else if (!isString) {
                             str.append(", \"");
                             isString=true;  
                           }  
                     str.append((char)val);  
                  }     
              }
              break;
            case TMPX:
              if (
                  (!option.allowUtf && ((val<0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 )   {                                    
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
                  listRel2.push(null);
                  
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
                  str.append((char)val);  
                }                  
              break;  
            case CA65:
              if (isFirst) {  
               str.append("$").append(ByteToExe(val)); 
               isFirst=false;                   
              } else {  
                  if (
                      (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                      (option.allowUtf && ((val==0x0A) || (val==0x22) || (val>127)))   
                     )  {
                    if (isString) {
                      str.append("\"");
                      isString=false;  
                    }
                    if (isFirst) {
                      str.append("$").append(ByteToExe(val)); 
                      isFirst=false;
                    } else str.append(", $").append(ByteToExe(val));      
                  } else {
                      if (isFirst) {
                          isFirst=false;
                          isString=true;
                          str.append("\"");
                     } else if (!isString) {
                              str.append(", \"");
                              isString=true;  
                            }  
                      str.append((char)val);  
                    }    
              }
              break;  
            case ACME:   
              if (isFirst) {  
               str.append("$").append(ByteToExe(val)); 
               isFirst=false;                   
              } else {  
                if (
                    (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                    (option.allowUtf && ( (val==0x00) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                   )  {
                    if (isString) {
                       str.append("\"");
                       isString=false;  
                    }
                    if (isFirst) {
                      str.append("$").append(ByteToExe(val)); 
                      isFirst=false;
                    } else str.append(", $").append(ByteToExe(val));      
                } else {
                   if (isFirst) {
                        isFirst=false;
                        isString=true;
                        str.append("\"");
                   } else if (!isString) {
                            str.append(", \"");
                            isString=true;  
                          }  
                   if  (val==0x5C) str.append("\\");
                   str.append((char)val);  
                  } 
              }
              break;  
            case KICK:
              if (isFirst) {
                str.append("@\"\\$").append(ByteToExe(Unsigned.done(mem.copy)));
                isString=true;
                isFirst=false; 
              } else {   
                  if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x40) ||
                    (val==0x5B) ||
                    (val==0x5D) ||
                    (val>=0x61 && val<=0x7A) ||
                    (val==0x7F) ||
                    (val>=0xA0))) ||    
                  (option.allowUtf && ((val<=0x02) ||
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
                    (val==0xA3)))
                  )  {
                  str.append("\\$").append(ByteToExe(val));   
                } else if (val==0x22) str.append("\\\"");
                  else if (val==0x5C) str.append("\\\\");
                  else str.append((char)val);     
                }
              break;    
            case TASS64:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) || 
                  (option.allowUtf && ((val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)))                      
                 ) {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }   
              break;  
              
            case GLASS:
              // don't use allowUTF  
              if (isFirst) {  
               str.append("$").append(ByteToExe(val)); 
               isFirst=false;                   
              } else {              
                  if (
                      (val!=0x00 && val!=0x07 && val!=0x09 && val!=0x0A && val!=0x0C && val!=0x0D && val!=0x1B && val!=0x27) &&     
                     ((( val<0x20 || (val>127))))   
                     )     
                  {
                      if (isString) {
                        str.append("\"");
                        isString=false;  
                      }
                      if (isFirst) {
                        str.append("$").append(ByteToExe(val)); 
                        isFirst=false;
                      } else str.append(", $").append(ByteToExe(val));      
                  } else {
                     if (isFirst) {
                          isFirst=false;
                          isString=true;
                          str.append("\"");
                     } else if (!isString) {
                              str.append(", \"");
                              isString=true;  
                            }  

                     switch (val) {
                       case 0x00:
                         str.append("\\0");  
                         break;     
                       case 0x07:
                          str.append("\\a"); 
                          break;
                       case 0x09:
                          str.append("\\t"); 
                          break;  
                       case 0x0A:
                          str.append("\\n"); 
                          break;  
                       case 0x0C:
                          str.append("\\f"); 
                          break; 
                       case 0x0D:
                          str.append("\\r"); 
                          break;  
                       case 0x1B:
                          str.append("\\e"); 
                          break;  
                       case 0x27:
                          str.append("\\'"); 
                          break; 
                       case 0x22:
                          str.append("\\\""); 
                          break;     
                       case 0x5C:
                          str.append("\\\\");
                          break;
                       default:
                          str.append((char)val); 
                          break;
                      }                                           
                    }   
                }
              break;
          }   
          
          carets.add(start, str.length(), mem, Type.NUM_TEXT);
          
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");
          }
          
          start=str.length();
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
     DB_BYTE_ZEROTEXT,        // ->      db "xxx"
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
       boolean isSpecial=false;
       int position=0;
       
       int pos1=str.length(); 
       int start=pos1;
         
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
         case DB_BYTE_ZEROTEXT:
           str.append(getDataSpacesTabs()).append(("db "));
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
          listRel2.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if (
                  (!option.allowUtf && (val<0x20 || val==0x22 || (val>127))) ||     
                  (option.allowUtf && ((val==0x00) || (val==0x0A) || (val==0x22) || (val>127)))  
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }                  
              break;
           case TMPX:
              if (
                  (!option.allowUtf && (val<0x08) || ((val==0x0A) || (val==0x0D) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
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
                  listRel2.push(null);
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
                  str.append((char)val);  
                }   
              if (list.size()==1) {
                  // terminating 0 is ommitted
                list.pop();
                listRel.pop();
                listRel2.pop();
              }
              break; 
            case CA65:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x0A) || (val==0x22) || (val>127)))   
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }   
              if (list.size()==1) {
                  // terminating 0 is ommitted
                list.pop();
                listRel.pop();
                listRel2.pop();
              }
              break;  
           case ACME:                
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ( (val==0x00) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  if  (val==0x5C) str.append("\\");
                  str.append((char)val);  
                }  
              if (list.size()==1) {
                  // terminating 0 is ommitted
                list.pop();
                listRel.pop();
                listRel2.pop();
              }
              break;  
            case KICK:
              if (isFirst) {
                position=str.length();
                str.append("@\"");
                isString=true;
                isFirst=false;  
              }    
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x40) ||
                    (val==0x5B) ||
                    (val==0x5D) ||
                    (val>=0x61 && val<=0x7A) ||
                    (val==0x7F) ||
                    (val>=0xA0))) ||    
                  (option.allowUtf && ((val<=0x02) ||
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
                    (val==0xA3)))
                  )  {
                str.append("\\$").append(ByteToExe(val));  
                isSpecial=true;
              } else if (val==0x22) {
                       str.append("\\\"");
                       isSpecial=true;
                     }
                else if (val==0x5C) { 
                       str.append("\\\\");
                       isSpecial=true;
                     }
                else str.append((char)val);                
              break;         
            case TASS64:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) || 
                  (option.allowUtf && ((val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)))                      
                 ) {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }   
              if (list.size()==1) {
                  // terminating 0 is ommitted
                list.pop();
                listRel.pop();
                listRel2.pop();
              }
              break;   
            case GLASS:
              // don't use allowUTF  
              if (
                  (val!=0x00 && val!=0x07 && val!=0x09 && val!=0x0A && val!=0x0C && val!=0x0D && val!=0x1B && val!=0x27) &&     
                 ((( val<0x20 || (val>127))))   
                 )     
              {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                 
                 switch (val) {
                   case 0x00:
                     str.append("\\0");  
                     break;     
                   case 0x07:
                      str.append("\\a"); 
                      break;
                   case 0x09:
                      str.append("\\t"); 
                      break;  
                   case 0x0A:
                      str.append("\\n"); 
                      break;  
                   case 0x0C:
                      str.append("\\f"); 
                      break; 
                   case 0x0D:
                      str.append("\\r"); 
                      break;  
                   case 0x1B:
                      str.append("\\e"); 
                      break;  
                   case 0x27:
                      str.append("\\'"); 
                      break; 
                   case 0x22:
                      str.append("\\\""); 
                      break;     
                   case 0x5C:
                      str.append("\\\\");
                      break;
                   default:
                      str.append((char)val); 
                      break;
                  }                                           
                }                  
              break;              
          }   
          
          carets.add(start, str.length(), mem, Type.ZERO_TEXT);
          
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");
            if (option.assembler==Assembler.Name.KICK && !isSpecial) str.setCharAt(position, ' ');
          }
          
          start=str.length();
       }     
     }
   }
   
      
   /**
    * Text terminated with high bit 1
    */
   public enum HighText implements ActionType {
      DOT_BYTE_HIGHTEXT,       // ->   .byte "xxx"
      DOT_BYT_HIGHTEXT,        // ->   .byt  "xxx"
      DOT_TEXT_HIGHTEXT,       // ->   .text "xxx"
      DOT_TEXT_S_HIGHTEXT,     // ->  .text s"xxx"
      DB_BYTE_HIGHTEXT,        // ->      db "xxx"     
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
       int start=pos1;
         
       switch (aHighText) {
         case DOT_SHIFT_HIGHTEXT:
           str.append(getDataSpacesTabs()).append((".shift "));
           break;  
         case DOT_TEXT_HIGHTEXT:
           str.append(getDataSpacesTabs()).append((".text "));
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
         case DB_BYTE_HIGHTEXT:
           str.append(getDataSpacesTabs()).append(("db "));
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
          listRel2.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if (
                  (!option.allowUtf && (val<0x20 || val==0x22 || (val>127))) ||     
                  (option.allowUtf && ((val==0x00) || (val==0x0A) || (val==0x22) || (val>127)))  
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }  
              break;
           case TMPX:
              if (list.size()==1) {
                // terminating has 1 converted to 0
                mem=list.pop();
                listRel.pop();
                listRel2.pop();
                
                str.append((char)(mem.copy & 0x7F));  
              } 
              if (
                  (!option.allowUtf && ((val<0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 )   {                                    
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
                  listRel2.push(null);
                  
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
                  str.append((char)val);  
                }   
              break; 
            case CA65:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x0A) || (val==0x22) || (val>127)))   
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }   
              break;  
           case ACME:                
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ( (val==0x00) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  if  (val==0x5C) str.append("\\");
                  str.append((char)val);  
                }  
              break;  
            case KICK:
              if (isFirst) {
                str.append("@\"");
                isString=true;
                isFirst=false;  
              }    
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x40) ||
                    (val==0x5B) ||
                    (val==0x5D) ||
                    (val>=0x61 && val<=0x7A) ||
                    (val==0x7F) ||
                    (val>=0xA0))) ||    
                  (option.allowUtf && ((val<=0x02) ||
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
                    (val==0xA3)))
                  )  {
                str.append("\\$").append(ByteToExe(val));   
              } else if (val==0x22) {
                       str.append("\\\"");
                     }
                else if (val==0x5C) { 
                       str.append("\\\\");
                     }
                else str.append((char)val);                
              break;         
            case TASS64:                
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) || 
                  (option.allowUtf && ((val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)))                      
                 ) {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }            
              if (list.size()==1) {
                // terminating has 1 converted to 0
                mem=list.pop();
                listRel.pop();
                listRel2.pop();
                
                str.append((char)(mem.copy & 0x7F));  
              }
              break; 
            case GLASS:
              // don't use allowUTF  
              if (
                  (val!=0x00 && val!=0x07 && val!=0x09 && val!=0x0A && val!=0x0C && val!=0x0D && val!=0x1B && val!=0x27) &&     
                 ((( val<0x20 || (val>127))))   
                 )     
              {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                 
                 switch (val) {
                   case 0x00:
                     str.append("\\0");  
                     break;     
                   case 0x07:
                      str.append("\\a"); 
                      break;
                   case 0x09:
                      str.append("\\t"); 
                      break;  
                   case 0x0A:
                      str.append("\\n"); 
                      break;  
                   case 0x0C:
                      str.append("\\f"); 
                      break; 
                   case 0x0D:
                      str.append("\\r"); 
                      break;  
                   case 0x1B:
                      str.append("\\e"); 
                      break;  
                   case 0x27:
                      str.append("\\'"); 
                      break; 
                   case 0x22:
                      str.append("\\\""); 
                      break;     
                   case 0x5C:
                      str.append("\\\\");
                      break;
                   default:
                      str.append((char)val); 
                      break;
                  }                                           
                }                  
              break;   
          }   
          
           carets.add(start, str.length(), mem, Type.HIGH_TEXT);
           
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");

          }
          
          start=str.length();
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
      DB_BYTE_SHIFTTEXT,        // ->      db "xxx"
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
        if (list.isEmpty()) return;    
        
        boolean isString=false;
        boolean isFirst=true;
        boolean isSpecial=false;
        int position=0;
        
        int pos1=str.length();
        int start=pos1;
               
        switch (aShiftText) {
          case DOT_BYTE_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append((".byte "));
            break;
          case DOT_BYT_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append((".byt "));
            break;  
          case BYTE_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append(("byte "));  
            break;
          case DC_BYTE_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append(("dc "));  
            break;
          case DC_B_BYTE_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append(("dc.b "));  
            break;
         case DB_BYTE_SHIFTTEXT:
           str.append(getDataSpacesTabs()).append(("db "));
           break;   
          case MARK_TEXT_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append(("!text "));  
            break;
          case MARK_TX_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append(("!tx "));  
            break; 
          case MARK_RAW_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append(("!raw "));  
            break;            
          case DOT_TEXT_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append((".text "));   
            break;  
          case DOT_TEXT_L_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append((".text l"));   
            break;      
          case DOT_SHIFTL_SHIFTTEXT:
            str.append(getDataSpacesTabs()).append((".shiftl "));   
            break;              
        }       
        
        int pos2=str.length();
        
        MemoryDasm mem;
        MemoryDasm memRel;
      
        while (!list.isEmpty()) {
          // accodate each bytes in the format choosed
          mem=list.pop();
          memRel=listRel.pop();
          listRel2.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if (
                  (!option.allowUtf && (val<0x20 || val==0x22 || (val>127))) ||     
                  (option.allowUtf && ((val==0x00) || (val==0x0A) || (val==0x22) || (val>127)))  
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }                  
              break;
            case TMPX:
              val>>=1;
              if (
                  (!option.allowUtf && ((val<0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 )   {                                    
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
                  listRel2.push(null);
                          
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
                  str.append((char)val);  
                }                  
              break;    
            case CA65:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x0A) || (val==0x22) || (val>127)))   
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }     
              break;
            case ACME:                
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ( (val==0x00) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  if  (val==0x5C) str.append("\\");
                  str.append((char)val);  
                }                  
              break;              
            case KICK:
              if (isFirst) {
                position=str.length();
                str.append("@\"");
                isString=true;
                isFirst=false;  
              }    
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x40) ||
                    (val==0x5B) ||
                    (val==0x5D) ||
                    (val>=0x61 && val<=0x7A) ||
                    (val==0x7F) ||
                    (val>=0xA0))) ||    
                  (option.allowUtf && ((val<=0x02) ||
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
                    (val==0xA3)))
                  )  {
                str.append("\\$").append(ByteToExe(val));   
                isSpecial=true;
              } else if (val==0x22) {
                       str.append("\\\"");
                       isSpecial=true;
                     }
                else if (val==0x5C) { 
                       str.append("\\\\");
                       isSpecial=true;
                     }
                else str.append((char)val);                
              break; 
            case TASS64:
              val>>=1;  
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) || 
                  (option.allowUtf && ((val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)))                      
                 ) {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }   
              break;  
            case GLASS:
              // don't use allowUTF  
              if (
                  (val!=0x00 && val!=0x07 && val!=0x09 && val!=0x0A && val!=0x0C && val!=0x0D && val!=0x1B && val!=0x27) &&     
                 ((( val<0x20 || (val>127))))   
                 )     
              {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                 
                 switch (val) {
                   case 0x00:
                     str.append("\\0");  
                     break;     
                   case 0x07:
                      str.append("\\a"); 
                      break;
                   case 0x09:
                      str.append("\\t"); 
                      break;  
                   case 0x0A:
                      str.append("\\n"); 
                      break;  
                   case 0x0C:
                      str.append("\\f"); 
                      break; 
                   case 0x0D:
                      str.append("\\r"); 
                      break;  
                   case 0x1B:
                      str.append("\\e"); 
                      break;  
                   case 0x27:
                      str.append("\\'"); 
                      break; 
                   case 0x22:
                      str.append("\\\""); 
                      break;     
                   case 0x5C:
                      str.append("\\\\");
                      break;
                   default:
                      str.append((char)val); 
                      break;
                  }                                           
                }                  
              break;  
            }       
          
          carets.add(start, str.length(), mem, Type.SHIFT_TEXT);
          
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");
            if (option.assembler==Assembler.Name.KICK && !isSpecial) str.setCharAt(position, ' ');
          }
          
          start=str.length();
        }
      }            
   }  
   
   /**
    * Text to screen code
    */
   public enum ScreenText implements ActionType {
      DOT_BYTE_SCREENTEXT,       // ->   .byte "xxx"
      DOT_BYT_SCREENTEXT,        // ->   .byt  "xxx"
      DOT_TEXT_SCREENTEXT,       // ->   .text "xxx"
      DOT_SCREEN_SCREENTEXT,     // -> .screen "xxx"
      DB_BYTE_SCREENTEXT,        // ->      db "xxx"
      BYTE_SCREENTEXT,           // ->    byte "xxx"
      MARK_SCR_SCREENTEXT,       // ->    !scr "xxx"
      DC_BYTE_SCREENTEXT,        // ->      dc "xxx"
      DC_B_BYTE_SCREENTEXT       // ->    dc.b "xxx"
      ;

      @Override
      public void flush(StringBuilder str) {
        if (list.isEmpty()) return;    
        
        boolean isString=false;
        boolean isFirst=true;
        boolean isSpecial=false;
        int position=0;
        
        int pos1=str.length();
        int start=pos1;
               
        switch (aScreenText) {
          case DOT_BYTE_SCREENTEXT:
            str.append(getDataSpacesTabs()).append((".byte "));
            break;
          case DOT_BYT_SCREENTEXT:
            str.append(getDataSpacesTabs()).append((".byt "));
            break;  
          case BYTE_SCREENTEXT:
            str.append(getDataSpacesTabs()).append(("byte "));  
            break;
          case DC_BYTE_SCREENTEXT:
            str.append(getDataSpacesTabs()).append(("dc "));  
            break;
          case DC_B_BYTE_SCREENTEXT:
            str.append(getDataSpacesTabs()).append(("dc.b "));  
            break;
          case DB_BYTE_SCREENTEXT:
           str.append(getDataSpacesTabs()).append(("db "));
           break;    
          case MARK_SCR_SCREENTEXT:
            str.append(getDataSpacesTabs()).append(("!scr "));  
            break;            
          case DOT_TEXT_SCREENTEXT:
            str.append(getDataSpacesTabs()).append((".text "));   
            break;  
          case DOT_SCREEN_SCREENTEXT:
            str.append(getDataSpacesTabs()).append((".screen "));   
            break;                  
        }     
        
        int pos2=str.length();
        
        MemoryDasm mem;
        MemoryDasm memRel;
      
        while (!list.isEmpty()) {
          // accodate each bytes in the format choosed
          mem=list.pop();
          memRel=listRel.pop();
          listRel2.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if (
                  (!option.allowUtf && (val<0x20 || val==0x22 || (val>127))) ||     
                  (option.allowUtf && ((val==0x00) || (val==0x0A) || (val==0x22) || (val>127)))  
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }                  
              break;
            case TMPX:              
              if ( (val==0x22) ||
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
                  listRel2.push(null);
                  
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
                if (val==0) str.append('@');  
                else if (val<=0x1A) str.append((char)(val+96)); 
                else if (val<=0x1F) str.append((char)(val+64)); 
                else if (val==64) str.append('`');
                else str.append((char)val); 
              break;    
            case CA65:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x0A) || (val==0x22) || (val>127)))   
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }     
              break;
            case ACME:                        
              if ( (val==0x22) ||
                   (val>127) 
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                 if (val==0x5C) str.append("\\");
                 if (val==0) str.append('@');  
                 else if (val<=0x1A) str.append((char)(val+96)); 
                 else if (val<=0x1F) str.append((char)(val+64)); 
                 else if (val==64) str.append('`');
                 else str.append((char)val);  
                }   
                
              break;              
            case KICK:
              if (isFirst) {
                position=str.length();
                str.append("@\"");
                isString=true;
                isFirst=false;  
              }    
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x40) ||
                    (val==0x5B) ||
                    (val==0x5D) ||
                    (val>=0x61 && val<=0x7A) ||
                    (val==0x7F) ||
                    (val>=0xA0))) ||    
                  (option.allowUtf && ((val<=0x02) ||
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
                    (val==0xA3)))
                  )  {
                str.append("\\$").append(ByteToExe(val));   
                isSpecial=true;
              } else if (val==0x22) {
                       str.append("\\\"");
                       isSpecial=true;
                     }
                else if (val==0x5C) { 
                       str.append("\\\\");
                       isSpecial=true;
                     }
                else str.append((char)val);                
              break; 
            case TASS64:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) || 
                  (option.allowUtf && ((val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)))                      
                 ) {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }   
              break;  
            case GLASS:
              // don't use allowUTF  
              if (
                  (val!=0x00 && val!=0x07 && val!=0x09 && val!=0x0A && val!=0x0C && val!=0x0D && val!=0x1B && val!=0x27) &&     
                 ((( val<0x20 || (val>127))))   
                 )     
              {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                 
                 switch (val) {
                   case 0x00:
                     str.append("\\0");  
                     break;     
                   case 0x07:
                      str.append("\\a"); 
                      break;
                   case 0x09:
                      str.append("\\t"); 
                      break;  
                   case 0x0A:
                      str.append("\\n"); 
                      break;  
                   case 0x0C:
                      str.append("\\f"); 
                      break; 
                   case 0x0D:
                      str.append("\\r"); 
                      break;  
                   case 0x1B:
                      str.append("\\e"); 
                      break;  
                   case 0x27:
                      str.append("\\'"); 
                      break; 
                   case 0x22:
                      str.append("\\\""); 
                      break;     
                   case 0x5C:
                      str.append("\\\\");
                      break;
                   default:
                      str.append((char)val); 
                      break;
                  }                                           
                }                  
              break;   
          }     
          
          carets.add(start, str.length(), mem, Type.SCREEN_TEXT);
          
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");
            if (option.assembler==Assembler.Name.KICK && !isSpecial) str.setCharAt(position, ' ');
          }
          
          start=str.length();
        }
      }
   }  
   
   /**
    * Text to pewtascii code
    */
   public enum PetasciiText implements ActionType {
      DOT_BYTE_PETASCIITEXT,       // ->   .byte "xxx"
      DOT_BYT_PETASCIITEXT,        // ->   .byt  "xxx"
      DOT_TEXT_PETASCIITEXT,       // ->   .text "xxx"
      DB_BYTE_PETASCIITEXT,        // ->      db "xxx"
      BYTE_PETASCIITEXT,           // ->    byte "xxx"
      MARK_PET_PETASCIITEXT,       // ->    !pet "xxx"
      DC_BYTE_PETASCIITEXT,        // ->      dc "xxx"
      DC_B_BYTE_PETASCIITEXT       // ->    dc.b "xxx"
      ;

      @Override
      public void flush(StringBuilder str) {
        if (list.isEmpty()) return;    
        
        boolean isString=false;
        boolean isFirst=true;
        boolean isSpecial=false;
        int position=0;
        
        int pos1=str.length();
        int start=pos1;
               
        switch (aPetasciiText) {
          case DOT_BYTE_PETASCIITEXT:
            str.append(getDataSpacesTabs()).append((".byte "));
            break;
          case DOT_BYT_PETASCIITEXT:
            str.append(getDataSpacesTabs()).append((".byt "));
            break;  
          case BYTE_PETASCIITEXT:
            str.append(getDataSpacesTabs()).append(("byte "));  
            break;
          case DC_BYTE_PETASCIITEXT:
            str.append(getDataSpacesTabs()).append(("dc "));  
            break;
          case DC_B_BYTE_PETASCIITEXT:
            str.append(getDataSpacesTabs()).append(("dc.b "));  
            break;
          case DB_BYTE_PETASCIITEXT:
            str.append(getDataSpacesTabs()).append(("db "));  
            break;  
          case MARK_PET_PETASCIITEXT:
            str.append(getDataSpacesTabs()).append(("!pet "));  
            break;            
          case DOT_TEXT_PETASCIITEXT:
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
          listRel2.pop();
          
          // not all char can be converted in string
          
          int val=(mem.copy & 0xFF);  
          switch (option.assembler) {
            case DASM:
              if (
                  (!option.allowUtf && (val<0x20 || val==0x22 || (val>127))) ||     
                  (option.allowUtf && ((val==0x00) || (val==0x0A) || (val==0x22) || (val>127)))  
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }                  
              break;
            case TMPX:
              if (
                  (!option.allowUtf && ((val<0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x08) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 )   {                                    
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
                  listRel2.push(null);
                  
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
                  str.append((char)val);  
                }                  
              break;    
            case CA65:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ((val==0x0A) || (val==0x22) || (val>127)))   
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }     
              break;
            case ACME:                 
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) ||    
                  (option.allowUtf && ( (val==0x00) || (val==0x0A) || (val==0x0D) || (val==0x22) || (val>127)))   
                 )  {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  

                  if  (val==0x5C) str.append("\\");
                  if (val>=0xC1 && val<=0xDA) str.append((char)(val & 0x7F));
                  else if (val>=0x41 && val<=0x5A) str.append((char)(val+32));
                  else str.append((char)val);  
                }     
                
              break;              
            case KICK:
              if (isFirst) {
                position=str.length();
                str.append("@\"");
                isString=true;
                isFirst=false;  
              }    
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x40) ||
                    (val==0x5B) ||
                    (val==0x5D) ||
                    (val>=0x61 && val<=0x7A) ||
                    (val==0x7F) ||
                    (val>=0xA0))) ||    
                  (option.allowUtf && ((val<=0x02) ||
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
                    (val==0xA3)))
                  )  {
                str.append("\\$").append(ByteToExe(val));   
                isSpecial=true;
              } else if (val==0x22) {
                       str.append("\\\"");
                       isSpecial=true;
                     }
                else if (val==0x5C) { 
                       str.append("\\\\");
                       isSpecial=true;
                     }
                else str.append((char)val);                
              break; 
            case TASS64:
              if (
                  (!option.allowUtf && ((val<=0x1F) || (val==0x22) || (val>127))) || 
                  (option.allowUtf && ((val==0x0A) ||
                   (val==0x0D) ||
                   (val==0x22) ||
                   (val>127)))                      
                 ) {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                  str.append((char)val);  
                }   
              break;  
            case GLASS:
              // don't use allowUTF  
              if (
                  (val!=0x00 && val!=0x07 && val!=0x09 && val!=0x0A && val!=0x0C && val!=0x0D && val!=0x1B && val!=0x27) &&     
                 ((( val<0x20 || (val>127))))   
                 )     
              {
                  if (isString) {
                    str.append("\"");
                    isString=false;  
                  }
                  if (isFirst) {
                    str.append("$").append(ByteToExe(val)); 
                    isFirst=false;
                  } else str.append(", $").append(ByteToExe(val));      
              } else {
                 if (isFirst) {
                      isFirst=false;
                      isString=true;
                      str.append("\"");
                 } else if (!isString) {
                          str.append(", \"");
                          isString=true;  
                        }  
                 
                 switch (val) {
                   case 0x00:
                     str.append("\\0");  
                     break;     
                   case 0x07:
                      str.append("\\a"); 
                      break;
                   case 0x09:
                      str.append("\\t"); 
                      break;  
                   case 0x0A:
                      str.append("\\n"); 
                      break;  
                   case 0x0C:
                      str.append("\\f"); 
                      break; 
                   case 0x0D:
                      str.append("\\r"); 
                      break;  
                   case 0x1B:
                      str.append("\\e"); 
                      break;  
                   case 0x27:
                      str.append("\\'"); 
                      break; 
                   case 0x22:
                      str.append("\\\""); 
                      break;     
                   case 0x5C:
                      str.append("\\\\");
                      break;
                   default:
                      str.append((char)val); 
                      break;
                  }                                           
                }                  
              break;     
          }           
          
          carets.add(start, str.length(), mem, Type.PETASCII_TEXT);
          
          if (list.isEmpty()) { 
            if (isString) str.append("\"\n");
            else str.append("\n");
            if (option.assembler==Assembler.Name.KICK && !isSpecial) str.setCharAt(position, ' ');
          }
          
          start=str.length();
        }
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
      MACRO3_STACKWORD,          // -> [.mac] $xxyyzz    (CA65) 
      MACRO4_STACKWORD          // -> [.mac] $xxyyzz    (Glass) 
      ;

      @Override
      public void flush(StringBuilder str) {
       if (list.isEmpty()) return; 
       
       MemoryDasm memLow;
       MemoryDasm memHigh;
       MemoryDasm memRelLow;
       MemoryDasm memRelHigh;
     
       int pos1=str.length();  // store initial position
       int start=pos1;
       int index=(int)(list.size()/2);
       
       // create starting command according to the kind of byte
       switch (aStackWord) {
         case DOT_RTA_STACKWORD:
           str.append(getDataSpacesTabs()).append((".rta "));  
           break;
         case MACRO_STACKWORD:
         case MACRO3_STACKWORD: 
         case MACRO4_STACKWORD:     
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
           listRel2.pop();
           
           memHigh=list.pop();
           memRelHigh=listRel.pop();           
           listRel2.pop();
           
           if (memLow.type=='<' && memHigh.type=='>' && memLow.related==memHigh.related) {
             if (memRelLow.userLocation!=null && !"".equals(memRelLow.userLocation)) str.append(memRelLow.userLocation).append("+1");
             else if (memRelLow.dasmLocation!=null && !"".equals(memRelLow.dasmLocation)) str.append(memRelLow.dasmLocation).append("+1");
                  else str.append("$").append(ShortToExe(memRelLow.address+1));  
             isFirst=false;
           } else {
               // if cannot make a word with relative locations, force all to be of byte type
               if (memLow.type=='<'  || memLow.type=='>'  || memLow.type=='^'  || memLow.type=='\\' ||
                   memHigh.type=='>' || memHigh.type=='<' || memHigh.type=='^' || memHigh.type=='\\')  {
                 list.addFirst(memHigh);
                 list.addFirst(memLow);
                 listRel.addFirst(memRelHigh);
                 listRel.addFirst(memRelLow);
                 listRel2.addFirst(null);
                 listRel2.addFirst(null);
                 
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
           carets.add(start, str.length(), memLow, Type.STACK_WORD);
           
           if (list.size()>=2) str.append(", ");
           else if (aStackWord==MACRO1_STACKWORD) str.append(")\n");
           else str.append("\n");
           
           start=str.length();
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
         case MACRO4_STACKWORD:
            str.append(spaces).append("Stack1: macro ?twobyte \n")
               .append(spaces).append("  db ?twobyte-1\n")
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Stack2: macro ?twobyte, ?twobyte2 \n")
               .append(spaces).append("  db ?twobyte-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1\n")
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Stack3: macro ?twobyte, ?twobyte2, ?twobyte3 \n")
               .append(spaces).append("  db ?twobyte-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1\n")   
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Stack4: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4 \n")
               .append(spaces).append("  db ?twobyte-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1\n")  
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1\n")          
               .append(spaces).append("endm\n\n")                       
               .append(spaces).append("Stack5: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4, ?twobyte5\n")
               .append(spaces).append("  db ?twobyte-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1\n")  
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1\n")
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Stack6: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4, ?twobyte5, ?twobyte6\n")
               .append(spaces).append("  db ?twobyte-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1\n")  
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1, ?twobyte6-1\n")     
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Stack7: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4, ?twobyte5, ?twobyte6, ?twobyte7\n")
               .append(spaces).append("  db ?twobyte-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1\n")   
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1\n")   
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1\n")   
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1, ?twobyte6-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1, ?twobyte6-1, ?twobyte7-1\n")    
               .append(spaces).append("endm\n\n")
               .append(spaces).append("Stack8: macro ?twobyte, ?twobyte2, ?twobyte3, ?twobyte4, ?twobyte5, ?twobyte6, ?twobyte7, ?twobyte8\n")
               .append(spaces).append("  db ?twobyte-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1\n")
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1\n")  
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1\n")   
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1\n")  
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1, ?twobyte6-1\n")     
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1, ?twobyte6-1, ?twobyte7-1\n")   
               .append(spaces).append("  db ?twobyte-1, ?twobyte2-1, ?twobyte3-1, ?twobyte4-1, ?twobyte5-1, ?twobyte6-1, ?twobyte7-1, ?twobyte8-1\n")      
               .append(spaces).append("endm\n\n");               
           break;
          }
        }            
   }

   
   /** Fifo list of memory locations */
   protected static LinkedList<MemoryDasm> list=new LinkedList();
   
   /** Fifo list of related memory locations */
   protected static LinkedList<MemoryDasm> listRel=new LinkedList();   
   
   /** Fifo list of related memory locations of second kind */
   protected static LinkedList<MemoryDasm> listRel2=new LinkedList();
   
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
   
   /** Assembler text to screen code type */
   protected static Assembler.ScreenText aScreenText;  
   
   /** Assembler text to petascii code type */
   protected static Assembler.PetasciiText aPetasciiText; 
   
   /** Memory constant */
   protected static Constant constant;
   
   /** Carets to use */
   protected static Carets carets;
   
   /** Memory dasm for block macro comments **/
   protected static MemoryDasm[] memory;
   
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
    * @param aScreenText the text to screen code
    * @param aPetasciiText the text to petascii code
    * @param constant the constants to use
    * @param carets the carets to use
    * @param memory all memory for macro block comments
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
                         Assembler.ShiftText aShiftText,
                         Assembler.ScreenText aScreenText,
                         Assembler.PetasciiText aPetasciiText,
                         Constant constant,
                         Carets carets,
                         MemoryDasm[] memory
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
     Assembler.aScreenText=aScreenText;
     Assembler.aPetasciiText=aPetasciiText;
      
     isMonoSpriteBlock=false;
     sizeMonoSpriteBlock=0;
     isMultiSpriteBlock=false;
     sizeMultiSpriteBlock=0;
     
     this.constant=constant;
     this.carets=carets;
     this.memory=memory;
   } 
   
   /**
    * Get current carets
    * 
    * @return current cattets
    */
   public Carets getCarets() {
     return carets;  
   }
   
   /**
    * Put the value into the buffer and manage 
    * 
    * @param str the string builder where put result
    * @param mem the memory being processed
    * @param memRel eventual memory related
    * @param memRel2 eventual memory related of second kind
    */
   public void putValue(StringBuilder str, MemoryDasm mem, MemoryDasm memRel, MemoryDasm memRel2) {
     ActionType type=actualType;  
     lastMem=mem;
     
     // if there is a block comments use it
     if (mem.userBlockComment!=null && !"".equals(mem.userBlockComment)) {
       flush(str);
       type=actualType;
       actualType=aBlockComment;
       flush(str);
       actualType=type;
     }     
       
     // if this is a label then the type will change  
     if (!(lastMem.type=='+' || lastMem.type=='-' || lastMem.type=='^' || lastMem.type=='\\')) {
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
           str.append(getDataCSpacesTabs(size-getDataSpacesTabs().length()));
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
     listRel2.add(memRel2);     
     
     // we are processing bytes?
     if (actualType instanceof Byte) {
       // look if it is time to aggregate data
       if (list.size()==option.maxByteAggregate) actualType.flush(str);     
       
       if (mem.dasmLocation==null && mem.userLocation==null) {
         // look for comment inside
         String comment=lastMem.dasmComment;
         if (lastMem.userComment != null) comment=lastMem.userComment;        
         if (!(comment==null || "".equals(comment))) actualType.flush(str);  
       }  
     } else
     // we are processing word?    
     if (actualType instanceof Word) {
       // look if it is time to aggregate data
       if (list.size()==option.maxWordAggregate*2) actualType.flush(str);   
       
       if (mem.dasmLocation==null && mem.userLocation==null) {
         // look for comment inside
         String comment=lastMem.dasmComment;
         if (lastMem.userComment != null) comment=lastMem.userComment;        
         if (!(comment==null || "".equals(comment))) actualType.flush(str);  
       }
     } else
     // we are processing word swapped?    
     if (actualType instanceof WordSwapped) {
       // look if it is time to aggregate data
       if (list.size()==option.maxSwappedAggregate*2) actualType.flush(str);   
       
       if (mem.dasmLocation==null && mem.userLocation==null) {
         // look for comment inside
         String comment=lastMem.dasmComment;
         if (lastMem.userComment != null) comment=lastMem.userComment;        
         if (!(comment==null || "".equals(comment))) actualType.flush(str);  
       }
     } else         
     // we are processing tribyte?    
     if (actualType instanceof Tribyte) {
       // look if it is time to aggregate data
       if (list.size()==option.maxTribyteAggregate*3) actualType.flush(str);        
       
       if (mem.dasmLocation==null && mem.userLocation==null) {
         // look for comment inside
         String comment=lastMem.dasmComment;
         if (lastMem.userComment != null) comment=lastMem.userComment;        
         if (!(comment==null || "".equals(comment))) actualType.flush(str);  
       }
     } else
     // we are processing long?    
     if (actualType instanceof Long) {
       // look if it is time to aggregate data
       if (list.size()==option.maxLongAggregate*4) actualType.flush(str);  

       if (mem.dasmLocation==null && mem.userLocation==null) {
         // look for comment inside
         String comment=lastMem.dasmComment;
         if (lastMem.userComment != null) comment=lastMem.userComment;        
         if (!(comment==null || "".equals(comment))) actualType.flush(str);  
       }       
     } else
     // we are processing address?    
     if (actualType instanceof Address) {
       // look if it is time to aggregate data
       if (list.size()==option.maxAddressAggregate*2) actualType.flush(str); 
       
       if (mem.dasmLocation==null && mem.userLocation==null) {
         // look for comment inside
         String comment=lastMem.dasmComment;
         if (lastMem.userComment != null) comment=lastMem.userComment;        
         if (!(comment==null || "".equals(comment))) actualType.flush(str);  
       }
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
     } else
     // we are processing left shift text?
     if (actualType instanceof ShiftText) {
       // look if it is time to aggregate data
       if (list.size()==option.maxTextAggregate) actualType.flush(str);         
     } else
     // we are processing text to screen code?
     if (actualType instanceof ScreenText) {
       // look if it is time to aggregate data
       if (list.size()==option.maxTextAggregate) actualType.flush(str);         
     } else
     // we are processing text to petascii code?
     if (actualType instanceof PetasciiText) {
       // look if it is time to aggregate data
       if (list.size()==option.maxTextAggregate) actualType.flush(str);         
     } 
   }
   
   /**
    * Put the starting string
    * 
    * @param str the stream for output
    */
   public void setStarting(StringBuilder str) {
     aStarting.flush(str);
   }   
   
   /**
    * Put the origin of PC
    * 
    * @param str the stream for output
    * @param pc the program counter to set
    */
   public void setOrg(StringBuilder str, int pc) {
     lastPC=pc;
     aOrigin.flush(str);
   }      
   
   /**
    * Put constants values
    * 
    * @param str the stream for output
    * @param constant the constants to use
    */
   public void setConstant(StringBuilder str, Constant constant) {
     boolean already;  
     String val;
     
     for (int i=0; i<Constant.COLS; i++) {
       already=false;
       
       for (int j=0; j<Constant.ROWS; j++) {
         val=constant.table[i][j];
         if (val!=null && !"".equals(val) && constant.isConstant(val)) {
           if (!already) {             
             MemoryDasm mem=new MemoryDasm();
             mem.userBlockComment=" \nConstants of type "+i;
             setBlockComment(str, mem);
               
             already=true;  
           }  
           
           switch (option.assembler) {
               case KICK:
                 str.append(".const "+val+" = "+"$"+ByteToExe(j)+"\n");  
                 break;
               case GLASS:
                 str.append(val+" equ $"+ByteToExe(j)+"\n");
                 break;  
               default:
                 str.append(val+" = "+"$"+ByteToExe(j)+"\n");
                 break;  
           }
           
         }
       }         
     } 
     str.append("\n");
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
     listRel2.clear();  
     
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
     listRel2.add(null);
     listRel2.add(null);
     
     actualType=aByte;
     flush(str);
     actualType=null;
   }
   
   /**
    * Set bytes of a relative memory location (it deletes anything that threre are in queue) in reverse order
    * 
    * @param str the output stream
    * @param address the relative address
    * @param label the label of address
    */
   public void setByteRelRev(StringBuilder str, int address, String label) {
     MemoryDasm lowMem=new MemoryDasm();
     MemoryDasm highMem=new MemoryDasm();
     MemoryDasm lowMemRel=new MemoryDasm();
     MemoryDasm highMemRel=new MemoryDasm();
       
     list.clear();
     listRel.clear();  
     listRel2.clear();  
     
     lowMem.type='<';
     lowMem.related=address;
     highMem.type='>';
     highMem.related=address;
     list.add(highMem);
     list.add(lowMem);
     
     lowMemRel.dasmLocation=label;
     highMemRel.dasmLocation=label;
     listRel.add(highMemRel);
     listRel.add(lowMemRel);
     listRel2.add(null);
     listRel2.add(null);
     
     actualType=aByte;
     flush(str);
     actualType=null;
   }
   
   /**
    * Set a word and put to ouptput stream (it deletes anything that threre are in queue)
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
     listRel2.clear();
     
     list.add(lowMem);
     listRel.add(null);
     listRel2.add(null);
     
     list.add(highMem);
     listRel.add(null);
     listRel2.add(null);
     
     //int size=0;
     
     if (comment!=null) {
       highMem.userComment=comment;
       lastMem=highMem;
       //size=str.length();
     }
     
     actualType=aWord;
     flush(str);
     actualType=null;
     
     //  if (comment!=null) {
     //    str.deleteCharAt(str.length()-1);            // remove \n
     //    size=str.length()-size;                      // get number of chars used  
     //    str.append(SPACES.substring(0, SPACES.length()-size));
     //   aComment.flush(str);  
     // }
   }
   
   /**
    * Set a byte and put to ouptput stream (it deletes anything that threre are in queue)
    * 
    * @param str the output stream
    * @param low the byte
    * @param comment eventual comment to add
    */
   public void setByte(StringBuilder str, byte low, String comment) {
     MemoryDasm lowMem=new MemoryDasm();
     
     lowMem.copy=low;
     lowMem.dataType=DataType.BYTE_HEX;
     lowMem.type='B';
     lowMem.related=-1;
     
     list.clear();
     listRel.clear();
     listRel2.clear();
     
     list.add(lowMem);
     listRel.add(null);
     listRel2.add(null);
     
     if (comment!=null) {
       lowMem.userComment=comment;
       lastMem=lowMem;
     }
     
     actualType=aWord;
     flush(str);
     actualType=null;    
   }
   
   /**
    * Set a text and put to ouptput stream (it deletes anything that threre are in queue)
    * 
    * @param str the output stream
    * @param text the text to add
    */
   public void setText(StringBuilder str, String text) {
     MemoryDasm mem;     
     
     list.clear();
     listRel.clear();
     listRel2.clear();
               
     for (char val: text.toCharArray()) {
       mem=new MemoryDasm();
       mem.copy=(byte)val;
       mem.dataType=DataType.TEXT;
       list.add(mem);
       listRel.add(null);
       listRel2.add(null);
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
    * Set the label if present 
    * 
    * @param str the stream to use
    * @param mem the memory with comment
    */
   public void setLabel(StringBuilder str, MemoryDasm mem) {
     lastMem=mem;
     aLabel.flush(str);
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
       case SHIFT_TEXT:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aShiftText;      
       case SCREEN_TEXT:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aScreenText; 
       case PETASCII_TEXT:
         isMonoSpriteBlock=false;
         isMultiSpriteBlock=false;   
         numText=null;
         return aPetasciiText;        
     }
     
     // default is of Byte type
     isMonoSpriteBlock=false;
     isMultiSpriteBlock=false; 
     numText=null;
     return aByte;
   }   

  /**
   * Add constants to the source
   * 
   * @add constant to source that are in memory
   * 
   * @return the constants
   */
  protected String addConstants(MemoryDasm[] memory) {
    String label;  
    String tmp;
      
    StringBuilder result=new StringBuilder();
    
    for (MemoryDasm mem : memory) {
      if (mem.isInside && !mem.isGarbage) continue;
      
      // for garbage inside, only if there is a user label makes it outs
      if (mem.isGarbage && mem.userLocation!=null && !"".equals(mem.userLocation)) {
        
        // look for block comment
        if (mem.userBlockComment!=null && !"".equals(mem.userBlockComment)) {  
          setBlockComment(result, mem);
        } 
        
        label=mem.userLocation;
        
        int start=result.length();
        
        if (option.assembler==Assembler.Name.KICK) {
          if (mem.address<=0xFF) tmp=".label "+label+" = $"+ByteToExe(mem.address);  
          else tmp=".label "+label+" = $"+ShortToExe(mem.address);  
        } else {
          if (mem.address<=0xFF) tmp=label+" = $"+ByteToExe(mem.address);  
          else tmp=label+" = $"+ShortToExe(mem.address);
        }
                     
        result.append(tmp).append(getInstrCSpacesTabs(tmp.length()));      
        
        getCarets().add(start, result.length(), mem, Carets.Type.LABEL);
          
        setComment(result, mem);                    
        
        continue;
      } 
      
      // look for block comment
      if (mem.userBlockComment!=null && !"".equals(mem.userBlockComment)) {  
        setBlockComment(result, mem);
      }   
      
      // look for constant
      label=null;
      if (mem.userLocation!=null && !"".equals(mem.userLocation)) label=mem.userLocation;
      else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) label=mem.dasmLocation;
      
      if (label!=null) {
         
        int start=result.length();
        
        if (option.assembler==Assembler.Name.KICK) {
          if (mem.address<=0xFF) tmp=".label "+label+" = $"+ByteToExe(mem.address);  
          else tmp=".label "+label+" = $"+ShortToExe(mem.address);  
        } else {
          if (mem.address<=0xFF) tmp=label+" = $"+ByteToExe(mem.address);  
          else tmp=label+" = $"+ShortToExe(mem.address);
        }
                      
        result.append(tmp).append(getInstrCSpacesTabs(tmp.length()));          
        
        getCarets().add(start, result.length(), mem, Carets.Type.LABEL);
          
        setComment(result, mem);                             
      }
    }
    return result.append("\n").toString();
  }
}
               

