/**
 * @(#)Disassembly 2019/12/15
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

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import sw_emulator.math.Unsigned;
import sw_emulator.software.cpu.M6510Dasm;
import sw_emulator.software.machine.C128Dasm;
import sw_emulator.software.machine.C64Dasm;
import sw_emulator.software.machine.C64MusDasm;
import sw_emulator.software.machine.C64SidDasm;
import sw_emulator.software.machine.C1541Dasm;
import sw_emulator.software.machine.CPlus4Dasm;
import sw_emulator.software.machine.CVic20Dasm;
import sw_emulator.swing.main.FileType;
import sw_emulator.swing.main.MPR;
import sw_emulator.swing.main.Option;
import sw_emulator.swing.main.TargetType;

/**
 * Disassembly the given buffer of data
 * 
 * @author ice
 */
public class Disassembly {
  /** Source of disassembly */
  public String source;
  
  /** Raw disassembly */
  public String disassembly;    
  
  /** Starting adress of data from file */
  public int startAddress;
  
  /** Ending address of data from file */
  public int endAddress;
  
   /** Starting buffer adress of data from file */
  public int startBuffer;
  
  /** Ending buffer address of data from file */
  public int endBuffer; 
  
  /** Starting address for MPR */
  public int[] startMPR;
  
  /** Ending address for MPR */
  public int[] endMPR;
  
  /** Buffer of data to disassemble */
  private byte[] inB;
  
  /** Eventual mulpiple program */
  private MPR mpr;
  
  /** The type of file */
  private FileType fileType;
  
  /** The option for disassembler */
  private Option option;
  
  /** Memory dasm */
  public MemoryDasm[] memory;
  
  /** The assembler to use  */
  protected Assembler assembler=new Assembler();
  
  /** Assembler starting to use */
  protected Assembler.Starting aStarting;
  
  /** Assembler origin to use */
  protected Assembler.Origin aOrigin;
  
  /** Assembler label to use */
  protected Assembler.Label aLabel;
  
  /** Assembler block comment to use */
  protected Assembler.BlockComment aBlockComment; 
  
  /** Assembler line comment to use */
  protected Assembler.Comment aComment; 
  
  /** Assembler byte type */
  protected Assembler.Byte aByte;
  
  /** Assembler word type */
  protected Assembler.Word aWord;
  
  /** Assembler word swapped type */
  protected Assembler.WordSwapped aWordSwapped;
  
  /** Assembler tribyte type */
  protected Assembler.Tribyte aTribyte;
  
  /** Assembler long type */
  protected Assembler.Long aLong; 
  
  /** Assembler address type */
  protected Assembler.Address aAddress; 
   
  /** Assembler stack word type */
  protected Assembler.StackWord aStackWord; 
  
   /** Assembler mono color sprite type */
   protected static Assembler.MonoSprite aMonoSprite;
   
   /** Asembler multi color sprite type */
   protected static Assembler.MultiSprite aMultiSprite;
   
   /** Asembler text type */
   protected static Assembler.Text aText;
   
   /** Asembler text with number of chars type */
   protected static Assembler.NumText aNumText;   
   
   /** Asembler text zero terminated type */
   protected static Assembler.ZeroText aZeroText;   
    
  /**
   * Disassemble the given data
   * 
   * @param fileType the file type
   * @param inB the buffer
   * @param option for disassembler
   * @param memory the memory for dasm
   * @param mpr eventual MPR blocks to use
   * @param targetType target machine type
   * @param asSource true if disassembly output should be as a source file
   */
  public void dissassembly(FileType fileType, byte[] inB, Option option,  MemoryDasm[] memory, MPR mpr, TargetType targetType, boolean asSource) {
    this.inB=inB;
    this.fileType=fileType;
    this.option=option;
    this.mpr=mpr;

    this.memory=memory;
    
    startMPR=null;
    endMPR=null;
      
    // avoid to precess null data  
    if (inB==null) {
      source="";
      disassembly="";
      return;
    }
    
    switch (fileType) {
      case MUS:
        dissassemblyMUS(asSource);                
        break;
      case SID:
        dissassemblySID(asSource);  
        break;
      case PRG:
        disassemlyPRG(asSource, targetType);  
        break;
      case MPR:
        disassemlyMPR(asSource, targetType);  
        break;        
      case UND:            
        source="";
        disassembly="";   
        break;
    }
  }
  
  /**
   * Disassembly a MUS file
   * 
   * @param asSource true if output should be as a source file
   */
  private void dissassemblyMUS(boolean asSource) {    
    int ind1;             // Mus file voice 1 address
    int ind2;             // Mus file voice 2 address
    int ind3;             // Mus file voice 3 address 
    int txtA;             // Mus file txt address 
    int v1Length;         // length of voice 1 data
    int v2Length;         // length of voice 2 data
    int v3Length;         // length of voice 3 data
    int musPC;            // PC value of start of mus program
    
    StringBuilder tmp=new StringBuilder();    
    C64MusDasm mus=new C64MusDasm();
    
    // don't use start/end address for mus
    startAddress=-1;
    endAddress=-1;
    startBuffer=-1;
    endBuffer=-1;
    
    musPC=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
    v1Length=Unsigned.done(inB[2])+Unsigned.done(inB[3])*256;
    v2Length=Unsigned.done(inB[4])+Unsigned.done(inB[5])*256;
    v3Length=Unsigned.done(inB[6])+Unsigned.done(inB[7])*256;

    // calculate pointer to voice data
    ind1=8;
    ind2=ind1+v1Length;
    ind3=ind2+v2Length;
    txtA=v1Length+v2Length+v3Length+8;
    
    tmp.append(fileType.getDescription(inB));
    tmp.append("\n");
    tmp.append(fileType.getDescription(inB)).append("\n");
    tmp.append("VOICE 1 MUSIC DATA: \n\n");
    tmp.append(mus.cdasm(inB, ind1, ind2-1, ind1+musPC));
    tmp.append("\nVOICE 2 MUSIC DATA: \n\n");
    tmp.append(mus.cdasm(inB, ind2, ind3-1, ind2+musPC));
    tmp.append("\nVOICE 3 MUSIC DATA: \n\n");
    tmp.append(mus.cdasm(inB, ind3, txtA-1, ind3+musPC));    
    
    disassembly=tmp.toString();
    source="";
  }
  
  /**
   * Disassembly a SID file
   * 
   * @param asSource true if output should be as a source file
   */
  private void dissassemblySID(boolean asSource) {
    int psidDOff;     // psid data offeset   
    int psidLAddr;    // psid load address
    int psidIAddr;    // psid init address
    int psidPAddr;    // psid play address     
    int sidPos;       // Position in buffer of start of sid program     
    int sidPC;        // PC value of start of sid program 
    
    setupAssembler();
      
    C64SidDasm sid=new C64SidDasm();
    sid.setMemory(memory);
    sid.setOption(option, assembler );       
  
    psidLAddr=Unsigned.done(inB[9])+Unsigned.done(inB[8])*256;    
    psidIAddr=Unsigned.done(inB[11])+Unsigned.done(inB[10])*256;
    psidPAddr=Unsigned.done(inB[13])+Unsigned.done(inB[12])*256;    
    memory[psidIAddr].userLocation=option.psidInitSongsLabel;
    memory[psidPAddr].userLocation=option.psidPlaySoundsLabel;
    
    psidDOff=Unsigned.done(inB[0x07])+Unsigned.done(inB[0x06])*256;
    
    //calculate address for disassembler
    if (psidLAddr==0) {
      sidPC=Unsigned.done(inB[psidDOff])+Unsigned.done(inB[psidDOff+1])*256;
      sidPos=psidDOff+2;
      startAddress=sidPC;
      endAddress=sidPC+inB.length-sidPos-1;      
    } else {
        sidPos=psidDOff;
        sidPC=psidLAddr;
        startAddress=sidPC;
        endAddress=sidPC+inB.length-sidPos-1;     
      }
    startBuffer=sidPos;
    endBuffer=sidPos+(endAddress-startAddress);
    
    markInside(startAddress, endAddress, sidPos);
    
    // search for SID frequency table
    SidFreq.instance.identifyFreq(inB, memory, sidPos, inB.length, sidPC-sidPos,
            option.sidFreqLoLabel, option.sidFreqHiLabel);
        
    StringBuilder tmp=new StringBuilder();
    
    if (asSource) {
      sid.upperCase=option.opcodeUpperCaseSource;
        
      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(tmp, mem);
      
      assembler.setStarting(tmp);
      assembler.setMacro(tmp, memory);
      
      // calculate org for header
      int header=sidPC;   
      header-=sidPos;
      if (psidLAddr==0) header-=2;      
      assembler.setOrg(tmp, header);
      
      // create header of PSID
      if (inB[0]=='P') assembler.setText(tmp, "PSID");
      else assembler.setText(tmp, "RSID");
      
      assembler.setWord(tmp, inB[0x04], inB[0x05], "version");
      assembler.setWord(tmp, inB[0x06], inB[0x07], "data offset");
      assembler.setWord(tmp, inB[0x08], inB[0x09], "load address in CBM format");      
      assembler.setByteRel(tmp, psidIAddr, option.psidInitSongsLabel);
      assembler.setByteRel(tmp, psidPAddr, option.psidPlaySoundsLabel);
      assembler.setWord(tmp, inB[0x0E], inB[0x0F], "songs");
      assembler.setWord(tmp, inB[0x10], inB[0x12], "default song");
      assembler.setWord(tmp, inB[0x12], inB[0x13], "speed");
      assembler.setWord(tmp, inB[0x14], inB[0x15], "speed");
   
      addString(tmp, 0x16, 0x36);
      addString(tmp, 0x36, 0x56);
      addString(tmp, 0x56, 0x76);
      
      // test if version > 1
      if (inB[0x07]>0x76) {
        assembler.setWord(tmp, inB[0x76], inB[0x77], "word flag");
        assembler.setWord(tmp, inB[0x78], inB[0x79], "start and page length");
        assembler.setWord(tmp, inB[0x7A], inB[0x7B], "second and third SID address");      
      }
      tmp.append("\n");
      if (psidLAddr==0) {
        assembler.setWord(tmp, inB[0x7C], inB[0x7D], "read load address"); 
        psidLAddr=Unsigned.done(inB[0x7C])+Unsigned.done(inB[0x7D])*256;  // modify this value as used for org starting
      }
      tmp.append("\n");
      assembler.setOrg(tmp, psidLAddr);      
      
      tmp.append(sid.csdasm(inB, sidPos, inB.length, sidPC));
      source=tmp.toString();
    } else {
        sid.upperCase=option.opcodeUpperCasePreview;
        tmp.append(fileType.getDescription(inB));
        tmp.append("\n");
        tmp.append(sid.cdasm(inB, sidPos, inB.length, sidPC));
        disassembly=tmp.toString(); 
      }     
  }
  
  /**
   * Add string with 0 teminate to the given buffer as source
   * 
   * @param tmp the buffer to use 
   * @param start start address
   * @param end  end address
   */
  private void addString(StringBuilder tmp, int start, int end) {
    String str="";
    
    for (int i=start; i<end; i++) {
      str+=(char)inB[i];
    }
    assembler.setText(tmp, str);
  }
  
  /**
   * Disassembly a PRG file
   * 
   * @param asSource true if output should be as a source file
   * @param targetType the target machine type
   */
  private void disassemlyPRG(boolean asSource, TargetType targetType) {
    M6510Dasm prg;
    
    setupAssembler();
      
    switch (targetType) {
      case C64:
        prg=new C64Dasm();  
        break;  
      case C1541:
        prg=new C1541Dasm();     
        break;
      case C128:
        prg=new C128Dasm();  
        break;
      case VIC20:
        prg=new CVic20Dasm(); 
        break;
      case PLUS4:
        prg=new CPlus4Dasm();  
        break;
      default:  
        prg=new M6510Dasm();
    }      
    
    prg.setMemory(memory);
    prg.setOption(option,  assembler);
    int start=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
     
    // calculate start/end address
    startAddress=start;    
    startBuffer=2;
    endAddress=inB.length-1-startBuffer+startAddress;
    endBuffer=inB.length-1;
    
    markInside(startAddress, endAddress, 2);
     
    // search for SID frequency table
    SidFreq.instance.identifyFreq(inB, memory, startBuffer, inB.length, start-startBuffer,
            option.sidFreqLoLabel, option.sidFreqHiLabel);
     
    StringBuilder tmp=new StringBuilder();
    
    if (asSource) {
      prg.upperCase=option.opcodeUpperCaseSource;  

      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(tmp, mem);

      assembler.setStarting(tmp);
      assembler.setMacro(tmp, memory);

      if (option.dasmF3Comp) {
        assembler.setOrg(tmp, start-2);
        assembler.setWord(tmp, inB[0], inB[1], null);
        tmp.append("\n");
      }  
      assembler.setOrg(tmp, start);
      
      tmp.append(prg.csdasm(inB, 2, inB.length, start));
      source=tmp.toString();
    } else {    
        prg.upperCase=option.opcodeUpperCasePreview;
        tmp.append(fileType.getDescription(inB));
        tmp.append("\n");
        tmp.append(prg.cdasm(inB, 2, inB.length, start));
        disassembly=tmp.toString();
      }       
  }    
  
  /**
   * Disassembly a MPR file
   * 
   * @param asSource true if output should be as a source file
   * @param targetType the target machine type
   */
  private void disassemlyMPR(boolean asSource, TargetType targetType) {
    M6510Dasm prg;
    
    setupAssembler();
      
    switch (targetType) {
      case C64:
        prg=new C64Dasm();     
        break;  
      case C1541:
        prg=new C1541Dasm();   
        break;
      case C128:
        prg=new C128Dasm();  
        break;
      case VIC20:
        prg=new CVic20Dasm(); 
        break;
      case PLUS4:
        prg=new CPlus4Dasm();       
        break;
      default:  
        prg=new M6510Dasm();
    }
       
    prg.setMemory(memory);
    prg.setOption(option,  assembler);
    
    if (mpr==null) return;
    
    StringBuilder tmp=new StringBuilder();
    
    byte[] inB;
    
    boolean first=true;
    
    if (asSource) {
      prg.upperCase=option.opcodeUpperCaseSource;  
      
      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(tmp, mem);

      assembler.setStarting(tmp);
      assembler.setMacro(tmp, memory);
    } else {    
        prg.upperCase=option.opcodeUpperCasePreview;
        
        tmp.append(fileType.getDescription(this.inB));
        tmp.append("\n");        
      }
    
    // sort by asc memory address
    Collections.sort(mpr.blocks, new Comparator<byte[]>() {
        @Override
        public int compare(byte[] block2, byte[] block1)
        {

            return  (Unsigned.done(block2[0])+Unsigned.done(block2[1])*256)-
                    (Unsigned.done(block1[0])+Unsigned.done(block1[1])*256);
        }
     });
     
     startMPR=new int[mpr.block];
     endMPR=new int[mpr.block];
    
    Iterator<byte[]> iter=mpr.blocks.iterator();
    int i=0;
    while (iter.hasNext()) {
      inB=iter.next();
      
      int start=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
 
      // calculate start/end address
      startAddress=start;    
      startBuffer=2;
      endAddress=inB.length-1-startBuffer+startAddress;
      endBuffer=inB.length-1;
      
      startMPR[i]=startAddress;
      endMPR[i]=endAddress;
      i++;
      
      markInside(inB, startAddress, endAddress, 2);
     
      // search for SID frequency table
      SidFreq.instance.identifyFreq(inB, memory, startBuffer, inB.length, start-startBuffer,
             option.sidFreqLoLabel, option.sidFreqHiLabel);
      

      if (asSource) {
        if (first && option.assembler==Assembler.Name.DASM && option.dasmF3Comp) {
          assembler.setOrg(tmp, start-2); 
          assembler.setWord(tmp, inB[0], inB[1], null);
          tmp.append("\n");
          first=false;
        }
        assembler.setOrg(tmp, start);        
        tmp.append(prg.csdasm(inB, 2, inB.length, start));
      } else {    
          tmp.append(prg.cdasm(inB, 2, inB.length, start));
        }       
    }
    
    if (asSource) source=tmp.toString();
    else disassembly=tmp.toString();
  }
  
 /**
   * Convert a unsigned short (containing in a int) to Exe upper case 4 chars
   *
   * @param value the short value to convert
   * @return the exe string rapresentation of byte
   */
  protected String ShortToExe(int value) {
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
   * Mark the memory as inside
   * 
   * @param start the start of internal
   * @param end the end of internal
   * @param offset in buffer of start position
   */
  private void markInside(int start, int end, int offset) {
    for (int i=start; i<=end; i++) {
      memory[i].isInside=true;  
      memory[i].copy=inB[i-start+offset];
    }  
  }
  
  /**
   * Mark the memory as inside
   * 
   * @param inB the buffer to use
   * @param start the start of internal
   * @param end the end of internal
   * @param offset in buffer of start position
   */
  private void markInside(byte[] inB, int start, int end, int offset) {
    for (int i=start; i<=end; i++) {
      memory[i].isInside=true;  
      memory[i].copy=inB[i-start+offset];
    }  
  }  
  
  /**
   * Set up the assembler
   */
  private void setupAssembler() {
    switch (option.assembler) {
      case DASM:
        aStarting=option.dasmStarting;
        aOrigin=option.dasmOrigin;
        aLabel=option.dasmLabel;
        aComment=option.dasmComment;
        aBlockComment=option.dasmBlockComment;
        aByte=option.dasmByte;
        aWord=option.dasmWord;
        aWordSwapped=option.dasmWordSwapped;
        aTribyte=option.dasmTribyte;
        aLong=option.dasmLong;
        aAddress=option.dasmAddress;
        aStackWord=option.dasmStackWord;
        aMonoSprite=option.dasmMonoSprite;
        aMultiSprite=option.dasmMultiSprite;
        aText=option.dasmText;
        aNumText=option.dasmNumText;
        aZeroText=option.dasmZeroText;
        break;
      case TMPX:
        aStarting=option.tmpxStarting;  
        aOrigin=option.tmpxOrigin;   
        aLabel=option.tmpxLabel;
        aComment=option.tmpxComment;
        aBlockComment=option.tmpxBlockComment;
        aByte=option.tmpxByte;
        aWord=option.tmpxWord;
        aWordSwapped=option.tmpxWordSwapped;
        aTribyte=option.tmpxTribyte;
        aLong=option.tmpxLong;
        aAddress=option.tmpxAddress;
        aStackWord=option.tmpxStackWord;
        aMonoSprite=option.tmpxMonoSprite;
        aMultiSprite=option.tmpxMultiSprite;
        aText=option.tmpxText;
        aNumText=option.tmpxNumText;
        aZeroText=option.tmpxZeroText;
        break;  
      case CA65:
        aStarting=option.ca65Starting;  
        aOrigin=option.ca65Origin;  
        aLabel=option.ca65Label;
        aComment=option.ca65Comment;
        aBlockComment=option.ca65BlockComment;
        aByte=option.ca65Byte;
        aWord=option.ca65Word; 
        aWordSwapped=option.ca65WordSwapped;
        aTribyte=option.ca65Tribyte;
        aLong=option.ca65Long;
        aAddress=option.ca65Address;
        aStackWord=option.ca65StackWord;
        aMonoSprite=option.ca65MonoSprite;
        aMultiSprite=option.ca65MultiSprite;
        aText=option.ca65Text;
        aNumText=option.ca65NumText;
        aZeroText=option.ca65ZeroText;
        break;  
      case ACME:
        aStarting=option.acmeStarting;  
        aOrigin=option.acmeOrigin;    
        aLabel=option.acmeLabel;
        aComment=option.acmeComment;
        aBlockComment=option.acmeBlockComment;
        aByte=option.acmeByte;
        aWord=option.acmeWord;
        aWordSwapped=option.acmeWordSwapped;
        aTribyte=option.acmeTribyte;
        aLong=option.acmeLong;
        aAddress=option.acmeAddress;
        aStackWord=option.acmeStackWord;
        aMonoSprite=option.acmeMonoSprite;
        aMultiSprite=option.acmeMultiSprite;
        aText=option.acmeText;
        aNumText=option.acmeNumText;
        aZeroText=option.acmeZeroText;
        break;
      case KICK:
        aStarting=option.kickStarting;  
        aOrigin=option.kickOrigin;  
        aComment=option.kickComment;
        aBlockComment=option.kickBlockComment;
        aLabel=option.kickLabel;
        aByte=option.kickByte;
        aWord=option.kickWord;  
        aWordSwapped=option.kickWordSwapped;
        aTribyte=option.kickTribyte;
        aLong=option.kickLong;
        aAddress=option.kickAddress;
        aStackWord=option.kickStackWord;
        aMonoSprite=option.kickMonoSprite;
        aMultiSprite=option.kickMultiSprite;
        aText=option.kickText;
        aNumText=option.kickNumText;
        aZeroText=option.kickZeroText;
        break; 
      case TASS64:
        aStarting=option.tass64Starting;
        aOrigin=option.tass64Origin;
        aComment=option.tass64Comment;
        aBlockComment=option.tass64BlockComment;
        aLabel=option.tass64Label;
        aByte=option.tass64Byte;
        aWord=option.tass64Word;
        aWordSwapped=option.tass64WordSwapped;
        aTribyte=option.tass64Tribyte;
        aLong=option.tass64Long;
        aAddress=option.tass64Address;
        aStackWord=option.tass64StackWord;
        aMonoSprite=option.tass64MonoSprite;
        aMultiSprite=option.tass64MultiSprite;
        aText=option.tass64Text;
        aNumText=option.tass64NumText;
        aZeroText=option.tass64ZeroText;
        break;
    }
    
    assembler.setOption(option, aStarting, aOrigin, aLabel, aComment, 
                        aBlockComment, aByte, aWord, aWordSwapped,
                        aTribyte, aLong, aAddress, aStackWord,
                        aMonoSprite, aMultiSprite, 
                        aText, aNumText, aZeroText);      
  }
  
  /**
   * Get the assembler description
   * 
   * @return the assembler description
   */
  public String getAssemblerDescription() {
    return  "****************************\n"+
            "  JC64dis version 0.9\n"+
            "  \n"+
            "  Source in "+option.assembler.getName()+" format\n"+
            "****************************\n";        
  }
}
