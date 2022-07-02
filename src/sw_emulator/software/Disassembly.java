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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import sw_emulator.math.Unsigned;
import sw_emulator.software.cpu.CpuDasm;
import sw_emulator.software.cpu.M6510Dasm;
import sw_emulator.software.machine.C64MusDasm;
import sw_emulator.software.machine.C64SidDasm;
import sw_emulator.swing.Shared;
import sw_emulator.swing.main.Block;
import sw_emulator.swing.main.Carets;
import sw_emulator.swing.main.Constant;
import sw_emulator.swing.main.FileType;
import sw_emulator.swing.main.MPR;
import sw_emulator.swing.main.Option;
import sw_emulator.swing.main.Patch;
import sw_emulator.swing.main.Relocate;
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
  
  /** Carets for source area */
  public Carets caretsSource=new Carets();
  
  /** Carets for previuw area */
  public Carets caretsPreview=new Carets();
  
  /** Actual carets being used */
  private Carets actualCarets;
  
  
  /** Buffer of data to disassemble */
  private byte[] inB;
  
  /** Eventual multiple program */
  private MPR mpr;
  
  /** Eventual CRT chip */
  private int chip;
  
  /** The type of file */
  private FileType fileType;
  
  /** The option for disassembler */
  private Option option;
  
  /** The relocates for disassembly */
  private Relocate[] relocates;
  
  /** The patches for disassembly  */
  private Patch[] patches;
  
  
  /** Blocks of memory to disassemble */
  public ArrayList<Block> blocks;
  
  /** Constants */
  public Constant constant;
  
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
   
  /** Asembler text terminated with high bit 1 */
  protected static Assembler.HighText aHighText;  
   
  /** Asembler text left shifted */
  protected static Assembler.ShiftText aShiftText;  
   
  /** Asembler text to screen code */
  protected static Assembler.ScreenText aScreenText;    
  
  /** Asembler text to screen code */
  protected static Assembler.PetasciiText aPetasciiText; 
  
  /** Reuse a string builder to avoid too much GC */
  private final StringBuilder builder=new StringBuilder();
    
  /**
   * Disassemble the given data
   * 
   * @param fileType the file type
   * @param inB the buffer
   * @param option for disassembler
   * @param memory the memory for dasm
   * @param constant the constants to use
   * @param mpr eventual MPR blocks to use
   * @param relocates eventual relocates to use
   * @param patches eventual patches to apply
   * @param chip eventual CRT chip
   * @param targetType target machine type
   * @param asSource true if disassembly output should be as a source file
   */
  public void dissassembly(FileType fileType, byte[] inB, Option option,  
                           MemoryDasm[] memory, Constant constant, MPR mpr,
                           Relocate[] relocates, Patch[] patches,
                           int chip, TargetType targetType, boolean asSource) {
    this.inB=inB;
    this.fileType=fileType;
    this.option=option;
    this.mpr=mpr;
    this.chip=chip;
    this.constant=constant;
    this.relocates=relocates;
    this.patches=patches;

    this.memory=memory;
    
    // clear previus carets identification and associate the actual caret to use
    if (asSource) actualCarets=caretsSource;
    else actualCarets=caretsPreview;
    actualCarets.clear();
     
    // avoid to process null data  
    if (inB==null) {
      source="";
      disassembly="";
      return;
    }    
    
    blocks=new ArrayList();
    
    switch (fileType) {
      case CRT:  
        disassemblyCRT(asSource, targetType);  
        break;  
      case MUS:
        dissassemblyMUS(asSource);                
        break;
      case SID:
        dissassemblySID(asSource);  
        break;
      case NSF:
        dissassemblyNSF(asSource);  
        break;       
      case PRG:
        disassemlyPRG(asSource, targetType);  
        break;
      case MPR:
        disassemlyMPR(asSource, targetType);  
        break;     
       case VSF:
        disassemlyVSF(asSource, targetType);  
        break;       
       case AY:
        disassemblyAY(asSource, targetType);
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
        
    C64MusDasm mus=new C64MusDasm();
    
    builder.setLength(0);
        
    musPC=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
    v1Length=Unsigned.done(inB[2])+Unsigned.done(inB[3])*256;
    v2Length=Unsigned.done(inB[4])+Unsigned.done(inB[5])*256;
    v3Length=Unsigned.done(inB[6])+Unsigned.done(inB[7])*256;

    // calculate pointer to voice data
    ind1=8;
    ind2=ind1+v1Length;
    ind3=ind2+v2Length;
    txtA=v1Length+v2Length+v3Length+8;
    
    builder.append(fileType.getDescription(inB));
    builder.append("\n");
    builder.append(fileType.getDescription(inB)).append("\n");
    builder.append("VOICE 1 MUSIC DATA: \n\n");
    builder.append(mus.cdasm(inB, ind1, ind2-1, ind1+musPC));
    builder.append("\nVOICE 2 MUSIC DATA: \n\n");
    builder.append(mus.cdasm(inB, ind2, ind3-1, ind2+musPC));
    builder.append("\nVOICE 3 MUSIC DATA: \n\n");
    builder.append(mus.cdasm(inB, ind3, txtA-1, ind3+musPC));    
    
    disassembly=builder.toString();
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
    Block block;
    
    setupAssembler();
      
    C64SidDasm sid=new C64SidDasm();
    sid.setMemory(memory);
    sid.setConstant(constant);
    sid.setOption(option, assembler);      
    sid.setMode(option.illegalOpcodeMode);
  
    psidLAddr=Unsigned.done(inB[9])+Unsigned.done(inB[8])*256;    
    psidIAddr=Unsigned.done(inB[11])+Unsigned.done(inB[10])*256;
    psidPAddr=Unsigned.done(inB[13])+Unsigned.done(inB[12])*256;    
    if (option.createPSID && !option.notMarkPSID) {
      if (psidIAddr!=0) memory[psidIAddr].userLocation=option.psidInitSongsLabel;
      if (psidPAddr!=0) memory[psidPAddr].userLocation=option.psidPlaySoundsLabel;
    }  
    
    psidDOff=Unsigned.done(inB[0x07])+Unsigned.done(inB[0x06])*256;
    
    block=new Block();
    
    //calculate address for disassembler
    if (psidLAddr==0) {
      sidPC=Unsigned.done(inB[psidDOff])+Unsigned.done(inB[psidDOff+1])*256;
      sidPos=psidDOff+2;
      block.startAddress=sidPC;
      block.endAddress=sidPC+inB.length-sidPos-1;      
    } else {
        sidPos=psidDOff;
        sidPC=psidLAddr;
        block.startAddress=sidPC;
        block.endAddress=sidPC+inB.length-sidPos-1;     
      }
    block.startBuffer=sidPos;
    block.endBuffer=sidPos+(block.endAddress-block.startAddress);
    block.inB=inB.clone();
    blocks.add(block);

        
    builder.setLength(0);
    
    if (asSource) {
      sid.upperCase=option.opcodeUpperCaseSource;
        
      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(builder, mem);
      
      assembler.setConstant(builder, constant);
      
      assembler.setStarting(builder);
      assembler.setMacro(builder, memory);
      
      // calculate org for header
      int header=sidPC;   
      
      // look if there are relocate before that position
      if (relocates!=null) {
        for (int i=0; i<relocates.length; i++) {
          if (relocates[i].toStart<header) header=relocates[i].toStart;
        }   
      }
      
      
      header-=sidPos;            
      
      if (option.createPSID) {
        assembler.setOrg(builder, header);
                
        // create header of PSID
        if (inB[0]=='P') assembler.setText(builder, "PSID");
        else assembler.setText(builder, "RSID");
      
        assembler.setWord(builder, inB[0x04], inB[0x05], "version");
        assembler.setWord(builder, inB[0x06], inB[0x07], "data offset");
        assembler.setWord(builder, inB[0x08], inB[0x09], "load address in CBM format");     
        if (option.notMarkPSID) {
          assembler.setWord(builder, inB[0xA], inB[0xB], "init songs");
          assembler.setWord(builder, inB[0xC], inB[0xD], "play sound");
        } else {
          if (psidIAddr!=0) assembler.setByteRelRev(builder, psidIAddr, option.psidInitSongsLabel);
          else assembler.setWord(builder, (byte)0, (byte)0, "init songs");
          if (psidPAddr!=0) assembler.setByteRelRev(builder, psidPAddr, option.psidPlaySoundsLabel);          
          else assembler.setWord(builder, (byte)0, (byte)0, "play sound");
        }
        assembler.setWord(builder, inB[0x0E], inB[0x0F], "songs");
        assembler.setWord(builder, inB[0x10], inB[0x11], "default song");
        assembler.setWord(builder, inB[0x12], inB[0x13], "speed");
        assembler.setWord(builder, inB[0x14], inB[0x15], "speed");
   
        addString(builder, 0x16, 0x36);
        addString(builder, 0x36, 0x56);
        addString(builder, 0x56, 0x76);
      
        // test if version > 1
        if (inB[0x07]>0x76) {
          assembler.setWord(builder, inB[0x76], inB[0x77], "word flag");
          assembler.setWord(builder, inB[0x78], inB[0x79], "start and page length");
          assembler.setWord(builder, inB[0x7A], inB[0x7B], "second and third SID address");      
        }
        builder.append("\n");
      }  
      if (psidLAddr==0) {
          if (option.createPSID) assembler.setWord(builder, inB[0x7C], inB[0x7D], "read load address"); 
          psidLAddr=Unsigned.done(inB[0x7C])+Unsigned.done(inB[0x7D])*256;  // modify this value as used for org starting
      }
      builder.append("\n");      
    } else {
        sid.upperCase=option.opcodeUpperCasePreview;
        builder.append(fileType.getDescription(inB));
        builder.append("\n");
      }  
    
    // add blocks from relocate
    addRelocate(blocks); 
    
    disassemblyBlocks(asSource, sid, builder);
    
    if (asSource) source=builder.toString();
    else disassembly=builder.toString();
  }
  
  /**
   * Disassembly a NSF file
   * 
   * @param asSource true if output should be as a source file
   */
  private void dissassemblyNSF(boolean asSource) {
    int nsfDOff;     // psid data offeset   
    int nsfLAddr;    // psid load address
    int nsfIAddr;    // psid init address
    int nsfPAddr;    // psid play address     
    int nsfPos;       // Position in buffer of start of sid program     
    int nsfPC;        // PC value of start of sid program 
    Block block;
    
    setupAssembler();
      
    C64SidDasm sid=new C64SidDasm();
    sid.setMemory(memory);
    sid.setConstant(constant);
    sid.setOption(option, assembler);      
    sid.setMode(option.illegalOpcodeMode);
  
    nsfLAddr=Unsigned.done(inB[8])+Unsigned.done(inB[9])*256;    
    nsfIAddr=Unsigned.done(inB[10])+Unsigned.done(inB[11])*256;
    nsfPAddr=Unsigned.done(inB[12])+Unsigned.done(inB[13])*256;    
    if (option.createPSID && !option.notMarkPSID) {
      if (nsfIAddr!=0) memory[nsfIAddr].userLocation=option.psidInitSongsLabel;
      if (nsfPAddr!=0) memory[nsfPAddr].userLocation=option.psidPlaySoundsLabel;
    }  
    
    nsfDOff=0x80; // fixed point
    
    block=new Block();
    
    //calculate address for disassembler
    nsfPos=nsfDOff;
    nsfPC=nsfLAddr;
    block.startAddress=nsfPC;
    block.endAddress=nsfPC+inB.length-nsfPos-1;     
  
    
    block.startBuffer=nsfPos;
    block.endBuffer=nsfPos+(block.endAddress-block.startAddress);
    block.inB=inB.clone();
    blocks.add(block);

    builder.setLength(0);
    
    if (asSource) {
      sid.upperCase=option.opcodeUpperCaseSource;
        
      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(builder, mem);
      
      assembler.setConstant(builder, constant);
      
      assembler.setStarting(builder);
      assembler.setMacro(builder, memory);
      
      // calculate org for header
      int header=nsfPC;   
      
      // look if there are relocate before that position
      if (relocates!=null) {
        for (int i=0; i<relocates.length; i++) {
          if (relocates[i].toStart<header) header=relocates[i].toStart;
        }   
      }
      
      
      header-=nsfPos;            
      
      if (option.createPSID) {
        assembler.setOrg(builder, header);
                
        // create header of PSID
        assembler.setText(builder, "NESM");
      
        assembler.setWord(builder, inB[0x04], inB[0x05], "version");
        assembler.setByte(builder, inB[0x06], "songs");
        assembler.setByte(builder, inB[0x07], "default song");
        
        assembler.setWord(builder, inB[0x08], inB[0x09], "load address");     
        if (option.notMarkPSID) {
          assembler.setWord(builder, inB[0xA], inB[0xB], "init songs");
          assembler.setWord(builder, inB[0xC], inB[0xD], "play sound");
        } else {
          if (nsfIAddr!=0) assembler.setByteRelRev(builder, nsfIAddr, option.psidInitSongsLabel);
          else assembler.setWord(builder, (byte)0, (byte)0, "init songs");
          if (nsfPAddr!=0) assembler.setByteRelRev(builder, nsfPAddr, option.psidPlaySoundsLabel);          
          else assembler.setWord(builder, (byte)0, (byte)0, "play sound");
        }
        
        addString(builder, 0x0E, 0x2E);
        addString(builder, 0x2E, 0x4E);
        addString(builder, 0x4E, 0x6E);
        
        assembler.setWord(builder, inB[0x6E], inB[0x6F], "speed NTSC");  
        assembler.setByte(builder, inB[0x70], null);
        assembler.setByte(builder, inB[0x71], null);
        assembler.setByte(builder, inB[0x72], null);
        assembler.setByte(builder, inB[0x73], null);
        assembler.setByte(builder, inB[0x74], null);
        assembler.setByte(builder, inB[0x75], null);
        assembler.setByte(builder, inB[0x76], null);
        assembler.setByte(builder, inB[0x77], "Bankswitch init"); 
        assembler.setWord(builder, inB[0x78], inB[0x79], "speed PAL"); 
        assembler.setByte(builder, inB[0x7A], "PAL/NTSC bits");
        assembler.setByte(builder, inB[0x7B], "Extra sound chips");
        
        assembler.setByte(builder, inB[0x7C], "Expansion 1");
        assembler.setByte(builder, inB[0x7D], "Expansion 2");
        assembler.setByte(builder, inB[0x7E], "Expansion 3");
        assembler.setByte(builder, inB[0x7F], "Expansion 4");
        
        builder.append("\n");
      }  

      builder.append("\n");      
    } else {
        sid.upperCase=option.opcodeUpperCasePreview;
        builder.append(fileType.getDescription(inB));
        builder.append("\n");
      }  
    
    // add blocks from relocate
    addRelocate(blocks); 
    
    disassemblyBlocks(asSource, sid, builder);
    
    if (asSource) source=builder.toString();
    else disassembly=builder.toString();
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
    CpuDasm prg;
    Block block;
    
    setupAssembler();
    
    prg=targetType.getDasm();   
    prg.setMemory(memory);
    prg.setConstant(constant);
    prg.setOption(option,  assembler);
    if (prg instanceof M6510Dasm) ((M6510Dasm)prg).setMode(option.illegalOpcodeMode);
    
    block=new Block();   
    block.startAddress=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
    block.startBuffer=2;
    block.endAddress=inB.length-1-block.startBuffer+block.startAddress;
    block.endBuffer=inB.length-1;
    block.inB=inB.clone();            
    blocks.add(block);

    builder.setLength(0);
    
    if (asSource) {
      prg.upperCase=option.opcodeUpperCaseSource;  

      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(builder, mem);
      
      assembler.setConstant(builder, constant);

      assembler.setStarting(builder);
      assembler.setMacro(builder, memory);
    } else {    
        prg.upperCase=option.opcodeUpperCasePreview;
        builder.append(fileType.getDescription(inB));
        builder.append("\n");
      } 
    
    // add blocks from relocate
    addRelocate(blocks);
    
    // add startup for DASM if this is the case
    if (asSource && option.assembler==Assembler.Name.DASM && option.dasmF3Comp) {
      int start=Math.max(0, getMinAddress());
        
      assembler.setOrg(builder, start-2); 
      assembler.setWord(builder, (byte)(start & 0xFF), (byte)(start>>8), null);
      builder.append("\n");
    } 
    
    disassemblyBlocks(asSource, prg, builder);
    
    if (asSource) source=builder.toString();
    else disassembly=builder.toString();
  }   
  
  /**
   * Disassembly a CRT file
   * 
   * @param asSource true if output should be as a source file
   * @param targetType the target machine type
   */
  private void disassemblyCRT(boolean asSource, TargetType targetType) {
    CpuDasm prg;
    Block block;
    
    setupAssembler();
      
    prg=targetType.getDasm();          
    prg.setMemory(memory);
    prg.setConstant(constant);
    prg.setOption(option,  assembler);
    if (prg instanceof M6510Dasm) ((M6510Dasm)prg).setMode(option.illegalOpcodeMode);
    
    // get start and end address for the selected chip
    int header=Math.max(
                      ((inB[0x10]&0xFF)<<24)+((inB[0x11]&0xFF)<<16)+
                      ((inB[0x12]&0xFF)<<8)+(inB[0x13]&0xFF), 0x40);
    
    int pos=header;
    int index=0;
    if (chip!=0) {
      try {       
        for (int i=0; i<chip; i++) {        
          pos=pos+((inB[pos+0x4]&0xFF)<<24)
                    +((inB[pos+0x5]&0xFF)<<16)
                    +((inB[pos+0x6]&0xFF)<<8)
                    +(inB[pos+0x7]&0xFF);
          if (pos>=inB.length) {
            pos=header;
            break;
          }
        }  
      } catch (Exception e) {
          pos=header; // force to use the chip 0
        }  
    }
      
    // pos= position of starting of CHIP area inside buffer
    
    block=new Block();   
    block.startAddress=((inB[pos+0xC]&0xFF)<<8)+(inB[pos+0xD]&0xFF);
    block.startBuffer=pos+0x10;
    block.endAddress=block.startAddress+((inB[pos+0xE]&0xFF)<<8)+(inB[pos+0xF]&0xFF)-1; 
    block.endBuffer=block.startBuffer+((inB[pos+0xE]&0xFF)<<8)+(inB[pos+0xF]&0xFF)-1;
    block.inB=inB.clone();            
    blocks.add(block);
    

    builder.setLength(0);
    
    if (asSource) {
      prg.upperCase=option.opcodeUpperCaseSource;  

      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(builder, mem);     
      assembler.setConstant(builder, constant);
      assembler.setStarting(builder);
      assembler.setMacro(builder, memory);
    } else {    
        prg.upperCase=option.opcodeUpperCasePreview;
        builder.append(fileType.getDescription(inB))
           .append("\nCHIP=").append(chip).append("\n");
      } 
            
    // add blocks from relocate
    addRelocate(blocks);    
    
    // add startup for DASM if this is the case
    if (asSource && option.assembler==Assembler.Name.DASM && option.dasmF3Comp) {
      int start=Math.max(0, getMinAddress());
        
      assembler.setOrg(builder, start-2); 
      assembler.setWord(builder, (byte)(start & 0xFF), (byte)(start>>8), null);
      builder.append("\n");
    } 
        
    disassemblyBlocks(asSource, prg, builder);   
    
    if (asSource) source=builder.toString();
    else disassembly=builder.toString();
  }    
  
  /**
   * Disassembly a VSF file
   * 
   * @param asSource true if output should be as a source file
   * @param targetType the target machine type
   */
  private void disassemlyVSF(boolean asSource, TargetType targetType) {
    CpuDasm prg;
    Block block;
    
    setupAssembler();
      
    prg=targetType.getDasm();    
    
    prg.setMemory(memory);
    prg.setConstant(constant);
    prg.setOption(option,  assembler);
    if (prg instanceof M6510Dasm) ((M6510Dasm)prg).setMode(option.illegalOpcodeMode);
    
    int pos;
    
    if (((inB[37] & 0xff) == 0x56) &&
        ((inB[38] & 0xff) == 0x49) &&
        ((inB[39] & 0xff) == 0x43) &&
        ((inB[40] & 0xff) == 0x45)
       ) pos=58;
    else pos=37;
        
    int actPos;
    int size=0;
    boolean find=false;
    
    while (pos<inB.length-21) {
      size=((inB[pos+21] & 0xff)<<24)+
           ((inB[pos+20] & 0xff)<<16)+
           ((inB[pos+19] & 0xff)<<8)+
           (inB[pos+18] & 0xff);
            
      actPos=pos;
      pos+=size;  
        
      if ((inB[actPos]& 0xff)   != 0x43) continue;
      if ((inB[actPos+1]& 0xff) != 0x36) continue;
      if ((inB[actPos+2]& 0xff) != 0x34) continue;
      if ((inB[actPos+3]& 0xff) != 0x4D) continue;
      if ((inB[actPos+4]& 0xff) != 0x45) continue;
      if ((inB[actPos+5]& 0xff) != 0x4D) continue;
      
      pos=actPos;
      find=true;
      break;
    }
    
    if (!find) {
     if (asSource) source="";
     else disassembly="";
     return;
    }
           
    // calculate start/end address
    block=new Block();
    block.startAddress=0;    
    block.startBuffer=pos+26;
    block.endAddress=65535;
    block.endBuffer=block.startBuffer+block.endAddress;
    block.inB=inB.clone();
    blocks.add(block);

    builder.setLength(0);
    
    if (asSource) {
      prg.upperCase=option.opcodeUpperCaseSource;  

      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(builder, mem);
      
      assembler.setConstant(builder, constant);

      assembler.setStarting(builder);
      assembler.setMacro(builder, memory); 
    } else {    
        prg.upperCase=option.opcodeUpperCasePreview;
        builder.append(fileType.getDescription(inB));
        builder.append("\n");
      }      
    
    // add blocks from relocate
    addRelocate(blocks);    
    
    // add startup for DASM if this is the case
    if (asSource && option.assembler==Assembler.Name.DASM && option.dasmF3Comp) {
      int start=Math.max(0, getMinAddress());
        
      assembler.setOrg(builder, start-2); 
      assembler.setWord(builder, (byte)(start & 0xFF), (byte)(start>>8), null);
      builder.append("\n");
    } 
            
    disassemblyBlocks(asSource, prg, builder);   
    
    if (asSource) source=builder.toString();
    else disassembly=builder.toString();
  }   
  
  /**
   * Disassembly a MPR file
   * 
   * @param asSource true if output should be as a source file
   * @param targetType the target machine type
   */
  private void disassemlyMPR(boolean asSource, TargetType targetType) {
    CpuDasm prg;
    Block block;
    
    setupAssembler();
      
    prg=targetType.getDasm();
       
    prg.setMemory(memory);
    prg.setConstant(constant);
    prg.setOption(option,  assembler);
    if (prg instanceof M6510Dasm) ((M6510Dasm)prg).setMode(option.illegalOpcodeMode);
    
    if (mpr==null) return;
    
    builder.setLength(0);
    
    if (asSource) {
      prg.upperCase=option.opcodeUpperCaseSource;  
      
      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(builder, mem);      
      assembler.setConstant(builder, constant);
      assembler.setStarting(builder);
      assembler.setMacro(builder, memory);
    } else {    
        prg.upperCase=option.opcodeUpperCasePreview;
        
        builder.append(fileType.getDescription(this.inB));
        builder.append("\n");        
      }
    
    // generate the blocks
    for (byte[] inB: mpr.blocks) {
      block=new Block();
      block.inB=inB.clone();
      
      block.startAddress=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
      block.startBuffer=2;
      block.endAddress=inB.length-1-block.startBuffer+block.startAddress;      
      block.endBuffer=inB.length-1;
      blocks.add(block);
    }
    
    // add blocks from relocate
    addRelocate(blocks);
    
    // add startup for DASM if this is the case
    if (asSource && option.assembler==Assembler.Name.DASM && option.dasmF3Comp) {
      int start=Math.max(0, getMinAddress());
        
      assembler.setOrg(builder, start-2); 
      assembler.setWord(builder, (byte)(start & 0xFF), (byte)(start>>8), null);
      builder.append("\n");
    }   
    
    disassemblyBlocks(asSource, prg, builder);
    
    if (asSource) source=builder.toString();
    else disassembly=builder.toString();
  }
  
  /**
   * Disassembly an AY file
   * 
   * @param asSource true if output should be as a source file
   * @param targetType the target machine type
   */
  private void disassemblyAY(boolean asSource, TargetType targetType) {
    int posStruct, posData, posPoint, posAddr, posBlock;  
    int address, length;
        
    CpuDasm prg;
    Block block;
    
    setupAssembler();
      
    prg=targetType.getDasm();
       
    prg.setMemory(memory);
    prg.setConstant(constant);
    prg.setOption(option,  assembler);
    if (prg instanceof M6510Dasm) ((M6510Dasm)prg).setMode(option.illegalOpcodeMode);
    
    builder.setLength(0);
    
    if (asSource) {
      prg.upperCase=option.opcodeUpperCaseSource;  

      MemoryDasm mem=new MemoryDasm();
      mem.userBlockComment=getAssemblerDescription();
      assembler.setBlockComment(builder, mem);
      
      assembler.setConstant(builder, constant);

      assembler.setStarting(builder);
      assembler.setMacro(builder, memory);
    } else {    
        prg.upperCase=option.opcodeUpperCasePreview;
        builder.append(fileType.getDescription(inB));
        builder.append("\n");
      } 
    
    try {
      posStruct=((inB[18] & 0xFF)<<8)+(inB[19] & 0xFF)+18;
            
      int tunes=(inB[16] & 0xFF)+1;
      for (int i=0; i<tunes; i++) {
        posData=((inB[posStruct+i*4+2] & 0xFF)<<8)+(inB[posStruct+i*4+3] & 0xFF)+posStruct+i*4+2;
        posPoint=((inB[posData+10] & 0xFF)<<8)+(inB[posData+11] & 0xFF)+posData+10;
        posAddr=((inB[posData+12] & 0xFF)<<8)+(inB[posData+13] & 0xFF)+posData+12;
            
        // blocks are 0 temrinating, suppose to have max 256 of them
        for (int j=0; j<256; j++) {
          address=((inB[posAddr+j*6] & 0xFF)<<8)+(inB[posAddr+j*6+1] & 0xFF);
          if (address==0) break;

          length=((inB[posAddr+j*6+2] & 0xFF)<<8)+(inB[posAddr+j*6+3] & 0xFF);
          posBlock=((inB[posAddr+j*6+4] & 0xFF)<<8)+(inB[posAddr+j*6+5] & 0xFF)+posAddr+j*6+4;
          
          block=new Block();
          block.startAddress=address;
          block.endAddress=address+length-1;
          block.startBuffer=posBlock;
          block.endBuffer=posBlock+length-1;
          block.inB=inB; 
          
          // check if this block is already in
          boolean find=false;
          for (Block b: blocks) {
            if (b.startAddress==block.startAddress &&
                b.endAddress==block.endAddress &&
                b.startBuffer==block.startBuffer &&
                b.endBuffer==block.endBuffer) {
               find=true;
               break;
             }  
          }
          
          if (!find) blocks.add(block);
        }
     }
    } catch (Exception e) {
        System.err.println(e);        
      }   
    
    // add blocks from relocate
    addRelocate(blocks);
     
         
    disassemblyBlocks(asSource, prg, builder);
    
    if (asSource) source=builder.toString();
    else disassembly=builder.toString();            
  }
  
  /**
   * Get the min starting address inside the blovks
   * 
   * @return the min starting address
   */
  private int getMinAddress() {
    int min=0xffff;
    
    for (Block block: blocks) {
      if (block.startAddress<min) min=block.startAddress;            
    }  
    
    return min;
  }
  
  /**
   * Disassembly the blocks we have 
   * 
   * @param asSource true if the disassembly is as source
   * @param tmp the buffer for output
   */
  private void disassemblyBlocks(boolean asSource, CpuDasm prg, StringBuilder tmp) {
    Block block;

    // sort by asc memory address
    Collections.sort(blocks, (Block block2, Block block1) -> block2.startAddress-block1.startAddress);
    
    if (option.useSidFreq) SidFreq.instance.reset();    
    
    actualCarets.setOffset(tmp.length());
    tmp.append(assembler.addConstants(memory));
        
    Iterator<Block> iter=blocks.iterator();
    while (iter.hasNext()) {
      block=iter.next();
      
      applyPatches(block);
      
      markInside(block.inB, block.startAddress, block.endAddress, block.startBuffer);            
     
      // search for SID frequency table
      if (option.useSidFreq)
          SidFreq.instance.identifyFreq(block.inB, memory, block.startBuffer, 
             block.endBuffer, block.startAddress-block.startBuffer,
             option.sidFreqLoLabel, option.sidFreqHiLabel, 
             option.sidFreqMarkMem, option.sidFreqCreateLabel,
             option.sidFreqCreateComment);      

      // add an offset due to previous strings added
      if (asSource) {
        assembler.setOrg(tmp, block.startAddress);        
        actualCarets.setOffset(tmp.length());
        tmp.append(prg.csdasm(block.inB, block.startBuffer, block.endBuffer, block.startAddress));
      } else {    
          actualCarets.setOffset(tmp.length());
          tmp.append(prg.cdasm(block.inB, block.startBuffer, block.endBuffer, block.startAddress));
        }       
    }
  }
  
  /**
   * Apply patches in this memory block
   * 
   * @param block the block where to apply patched
   */
  private void applyPatches(Block block) {
    if (patches==null) return;
    
    for (Patch patch: patches) {
      if (block.startAddress<=patch.address && patch.address<=block.endAddress) {
        block.inB[patch.address-block.startAddress+block.startBuffer]=(byte)patch.value;
      } 
    }
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
   * Add relocate blocks
   * 
   * @param list the list where to add blocks
   */
  private void addRelocate(ArrayList<Block> list) {
    if (relocates==null)   return;
    
    byte[] inB;
    int size;
    Block block;
    
   for (Relocate relocate:relocates) {
     size=relocate.fromEnd-relocate.fromStart+1;
     inB=new byte[size];
     for (int i=0; i<size; i++) {
       inB[i]=memory[relocate.fromStart+i].copy;  
     } 
     
     block=new Block();
     block.inB=inB;
     block.startAddress=relocate.toStart;
     block.endAddress=relocate.toEnd;
     block.startBuffer=0;
     block.endBuffer=size-1;
     list.add((block));
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
        aHighText=option.dasmHighText;   
        aShiftText=option.dasmShiftText; 
        aScreenText=option.dasmScreenText; 
        aPetasciiText=option.dasmPetasciiText;
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
        aHighText=option.tmpxHighText;
        aShiftText=option.tmpxShiftText;
        aScreenText=option.tmpxScreenText; 
        aPetasciiText=option.tmpxPetasciiText;
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
        aHighText=option.ca65HighText; 
        aShiftText=option.ca65ShiftText;
        aScreenText=option.ca65ScreenText; 
        aPetasciiText=option.ca65PetasciiText;
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
        aHighText=option.acmeHighText;
        aShiftText=option.acmeShiftText;
        aScreenText=option.acmeScreenText; 
        aPetasciiText=option.acmePetasciiText;
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
        aHighText=option.kickHighText;
        aShiftText=option.kickShiftText;
        aScreenText=option.kickScreenText; 
        aPetasciiText=option.kickPetasciiText;
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
        aHighText=option.tass64HighText;
        aShiftText=option.tass64ShiftText;
        aScreenText=option.tass64ScreenText; 
        aPetasciiText=option.tass64PetasciiText;
        break;
      case GLASS:
        aStarting=option.glassStarting;
        aOrigin=option.glassOrigin;
        aLabel=option.glassLabel;
        aComment=option.glassComment;
        aBlockComment=option.glassBlockComment;
        aByte=option.glassByte;
        aWord=option.glassWord;
        aWordSwapped=option.glassWordSwapped;
        aTribyte=option.glassTribyte;
        aLong=option.glassLong;
        aAddress=option.glassAddress;
        aStackWord=option.glassStackWord;
        aMonoSprite=option.glassMonoSprite;
        aMultiSprite=option.glassMultiSprite;
        aText=option.glassText;
        aNumText=option.glassNumText;
        aZeroText=option.glassZeroText;
        aHighText=option.glassHighText;   
        aShiftText=option.glassShiftText; 
        aScreenText=option.glassScreenText; 
        aPetasciiText=option.glassPetasciiText;
        break;  
    }
    
    assembler.setOption(option, aStarting, aOrigin, aLabel, aComment, 
                        aBlockComment, aByte, aWord, aWordSwapped,
                        aTribyte, aLong, aAddress, aStackWord,
                        aMonoSprite, aMultiSprite, 
                        aText, aNumText, aZeroText, aHighText, aShiftText,
                        aScreenText, aPetasciiText, constant, actualCarets);      
  }
  
  /**
   * Get the assembler description
   * 
   * @return the assembler description
   */
  public String getAssemblerDescription() {
    switch (option.heather) {
      case Option.HEATHER_STANDARD:  
        return  "****************************\n"+
          "  JC64dis version "+Shared.VERSION+"\n"+
          "  \n"+
          "  Source in "+option.assembler.getName()+" format\n"+
          "****************************\n";        
      case Option.HEATHER_NONE:
        return null;
      case Option.HEATHER_CUSTOM:
        return option.custom;
    }  
    return null;    
  }
}
