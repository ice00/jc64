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

import java.util.Locale;
import sw_emulator.math.Unsigned;
import sw_emulator.software.machine.C64Dasm;
import sw_emulator.software.machine.C64MusDasm;
import sw_emulator.software.machine.C64SidDasm;
import sw_emulator.swing.main.FileType;
import sw_emulator.swing.main.Option;

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
  
  /** Buffer of data to disassemble */
  private byte[] inB;
  
  /** The type of file */
  private FileType fileType;
  
  /** The option for disassembler */
  private Option option;
  
  /** Memory dasm */
  public MemoryDasm[] memory;
    
  /**
   * Disassemble the given data
   * 
   * @param fileType the file type
   * @param inB the buffer
   * @param option for disassembler
   * @param memory the memory for dasm
   * @param asSource true if disassembly output should be as a source file
   */
  public void dissassembly(FileType fileType, byte[] inB, Option option,  MemoryDasm[] memory, boolean asSource) {
    this.inB=inB;
    this.fileType=fileType;
    this.option=option;

    this.memory=memory;
      
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
        disassemlyPRG(asSource);  
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
  }
  
  /**
   * Disassembly a MUS file
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
      
    C64SidDasm sid=new C64SidDasm();
    sid.language=option.commentLanguagePreview;
    sid.setMemory(memory);
    sid.setOption(option);
    
    
    psidLAddr=Unsigned.done(inB[9])+Unsigned.done(inB[8])*256;
    
    psidIAddr=Unsigned.done(inB[11])+Unsigned.done(inB[10])*256;
    psidPAddr=Unsigned.done(inB[13])+Unsigned.done(inB[12])*256;    
    memory[psidIAddr].userLocation=option.psidInitSongsLabel;
    memory[psidPAddr].userLocation=option.psidPlaySoundsLabel;
    
    psidDOff=(int)inB[7];
    
    //calculate address for disassembler
    if (psidLAddr==0) {
      sidPC=Unsigned.done(inB[psidDOff])+Unsigned.done(inB[psidDOff+1])*256;
      sidPos=psidDOff+2;
      startAddress=sidPC;
      endAddress=sidPC+inB.length-0x7c-2;      
    } else {
        sidPos=psidDOff;
        sidPC=psidLAddr;
        startAddress=sidPC;
        endAddress=sidPC+inB.length-0x7c;     
      }
    startBuffer=sidPos;
    endBuffer=sidPos+(endAddress-startAddress);
    
    markInside(startAddress, endAddress);
    
    // search for SID frequency table
    SidFreq.instance.identifyFreq(inB, memory, sidPos, inB.length, sidPC-sidPos,
            option.sidFreqLoLabel, option.sidFreqHiLabel);
        
    StringBuilder tmp=new StringBuilder();
    
    if (asSource) {
        
      tmp.append("  processor 6502\n\n");
      
      // calculate org for header
      int header=psidLAddr;
      if (header==0) header=inB[0x7C]+inB[0x7D]*256;      
      header-=(inB[0x07]+inB[0x06]*256);
      if (psidLAddr==0) header-=2;      
      tmp.append("  .org $").append(ShortToExe(header)).append("\n\n");
      
      // create header of PSID
      if (inB[0]=='P') tmp.append("  .byte \"PSID\"\n");
      else tmp.append("  .byte \"RSID\"\n");
      
      tmp.append("  .word $").append(ShortToExe(inB[0x05]+inB[0x04]*256)).append("         ; version\n");
      tmp.append("  .word $").append(ShortToExe(inB[0x07]+inB[0x06]*256)).append("         ; data offset\n");
      tmp.append("  .word $").append(ShortToExe(inB[0x09]+inB[0x08]*256)).append("         ; load address in CBM format\n");
      tmp.append("  .byte >").append(option.psidInitSongsLabel).append("\n");
      tmp.append("  .byte <").append(option.psidInitSongsLabel).append("\n");
      tmp.append("  .byte >").append(option.psidPlaySoundsLabel).append("\n");
      tmp.append("  .byte <").append(option.psidPlaySoundsLabel).append("\n");
      tmp.append("  .word $").append(ShortToExe(inB[0x0F]+inB[0x0E]*256)).append("         ; songs\n");
      tmp.append("  .word $").append(ShortToExe(inB[0x11]+inB[0x10]*256)).append("         ; default song\n");
      tmp.append("  .word $").append(ShortToExe(inB[0x13]+inB[0x12]*256)).append("         ; speed\n");
      tmp.append("  .word $").append(ShortToExe(inB[0x15]+inB[0x14]*256)).append("         ; speed\n");
   
      addString(tmp, 0x16, 0x36);
      addString(tmp, 0x36, 0x56);
      addString(tmp, 0x56, 0x76);
      
      // test if version > 1
      if (inB[0x07]>0x76) {
        tmp.append("  .word $").append(ShortToExe(inB[0x77]+inB[0x76]*256)).append("         ; word flag\n");  
        tmp.append("  .word $").append(ShortToExe(inB[0x79]+inB[0x78]*256)).append("         ; start and page length\n");  
        tmp.append("  .word $").append(ShortToExe(inB[0x7B]+inB[0x7A]*256)).append("         ; second and third SID address \n");     
      }
      tmp.append("\n");
      if (psidLAddr==0) {
        tmp.append("                      ; read load address\n");  
        tmp.append("  .byte <$").append(ShortToExe(inB[0x7C]+inB[0x7D]*256)).append("\n");
        tmp.append("  .byte >$").append(ShortToExe(inB[0x7C]+inB[0x7D]*256)).append("\n");
        psidLAddr=inB[0x7C]+inB[0x7D]*256;  // modify this value as used for org starting
      }
      tmp.append("\n");
      tmp.append("  .org ").append(ShortToExe(psidLAddr)).append("\n\n");
      
      tmp.append(sid.csdasm(inB, sidPos, inB.length, sidPC));
      source=tmp.toString();
    } else {
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
    boolean exit=false;
    tmp.append("  .byte \"");
    for (int i=start; i<end; i++) {
      if (inB[i]==0) {
        if (exit) tmp.append(",0");
        else {
          tmp.append("\",0");
          exit=true;
        }
      } else tmp.append((char)inB[i]);
    }
    tmp.append("\n");
  }
  
  /**
   * Disassembly a MUS file
   * 
   * @param asSource true if output should be as a source file
   */
  private void disassemlyPRG(boolean asSource) {
    C64Dasm prg=new C64Dasm();
    prg.language=option.commentLanguagePreview;
    prg.setMemory(memory);
    prg.setOption(option);
    int start=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
     
    // calculate start/end address
    startAddress=start;
    endAddress=inB.length-1+startAddress;
    startBuffer=2;
    endBuffer=(endAddress-startAddress);
    
    markInside(startAddress, endAddress);
     
    // search for SID frequency table
    SidFreq.instance.identifyFreq(inB, memory, startBuffer, inB.length, start-startBuffer,
            option.sidFreqLoLabel, option.sidFreqHiLabel);
     
    StringBuilder tmp=new StringBuilder();
    
    if (asSource) {
        
      tmp.append("  processor 6502\n\n");

      tmp.append("  .byte ").append(Unsigned.done(inB[0])).append("\n");
      tmp.append("  .byte ").append(Unsigned.done(inB[1])).append("\n");
      tmp.append("\n");
      tmp.append("  .org ").append(ShortToExe(start)).append("\n\n");
      
      tmp.append(prg.csdasm(inB, 2, inB.length, start));
      source=tmp.toString();
    } else {    
        tmp.append(fileType.getDescription(inB));
        tmp.append("\n");
        tmp.append(prg.cdasm(inB, 2, inB.length, start));
        disassembly=tmp.toString();
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
   * Mark the memory as inside
   * 
   * @param start the start of internal
   * @param end the end of internal
   */
  private void markInside(int start, int end) {
    for (int i=start; i<=end; i++) {
      memory[i].isInside=true;  
    }  
  }
}
