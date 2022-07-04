/**
 * @(#)Project.java 2019/12/03
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
import java.util.Objects;
import sw_emulator.software.MemoryDasm;

/**
 * Container for the project
 * 
 * Version 0: initial
 * Version 1: add isGarbage onto memoryDasm
 *            add TargetType
 * Version 2: add CRT chip number
 * Version 3: add constants for assembler
 *            add index for memoryDasm
 * Version 4: add relocates
 * Version 5: add patches
 * Version 6: add constants for 16bits
 * Version 7: gzip format
 * Version 8: add 16 constants instead of 10
 * 
 * @author ice
 */
public class Project implements Cloneable { 
  /** Actual version of project */ 
  public static final byte ACTUAL_VERSION=8;       
    
  /** Type of the file */
  public FileType fileType;
  
  /** Name of the project */  
  public String name; 
  
  /** Path + name of file to disassemble */
  public String file;
  
  /** Description of the file */
  public String description;
  
  /** Contains the data of input file */
  public byte[] inB;
  
  /** Mpr contents */
  public MPR mpr;
  
  /** Target machine type*/
  public TargetType targetType;
  
  /** Memory flag of SIDLD */
  public byte[] memoryFlags=new byte[0xFFFF+1];
  
  /** Memory for dasm*/
  public MemoryDasm[] memory=new MemoryDasm[0xFFFF+1];
  
  /** CRT chip */
  public int chip;
  
  /** Constant for assembler */
  public Constant constant=new Constant();
  
  /** Relocate entries */
  public Relocate[] relocates;
  
  /** Patch entries*/
  public Patch[] patches;
  
  /** Freeze image */
  public Freeze[] freezes;

  /**
   * Construct the project
   */
  public Project() {
   for (int i=0; i<memory.length; i++) {
     memory[i]=new MemoryDasm();  
     memory[i].address=i;  
   }
  }  
    
  public void setData(byte[] inB) {
    this.inB=inB;
    fileType=FileType.getFileType(inB);
    description=fileType.getDescription(inB);
    
    if (fileType==FileType.MPR) {
      mpr=new MPR();
      mpr.getElements(inB);
    }
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.fileType);
    hash = 89 * hash + Objects.hashCode(this.name);
    hash = 89 * hash + Objects.hashCode(this.file);
    hash = 89 * hash + Objects.hashCode(this.description);
    hash = 89 * hash + Arrays.hashCode(this.inB);
    hash = 89 * hash + Arrays.hashCode(this.memoryFlags);
    hash = 89 * hash + Arrays.deepHashCode(this.memory);
    hash = 89 * hash + Objects.hashCode(this.targetType);
    hash = 89 * hash + MPR.hashCode(this.mpr); 
    hash = 89 * hash + this.chip;
    hash = 89 * hash + Arrays.hashCode(this.constant.table);
    hash = 89 * hash + Arrays.hashCode(this.relocates);
    hash = 89 * hash + Arrays.hashCode(this.patches);
    hash = 89 * hash + Arrays.hashCode(this.freezes);
    return hash;
  }
  
  @Override
  public Project clone() { 
    Project p = new Project();
      
    p.description=this.description;
    p.file=this.file;
    p.fileType=this.fileType;
    p.targetType=this.targetType;
    if (this.inB!=null) p.inB=this.inB.clone();
    if (p.memoryFlags!=null) p.memoryFlags=this.memoryFlags.clone();
    p.name=this.name;
    if (this.mpr!=null) p.mpr=(MPR)this.mpr.clone();
      
    for (int i=0; i<this.memory.length; i++) {
      if (this.memory[i]!=null) p.memory[i]=this.memory[i].clone();
    }
    
    p.chip=this.chip;    
    p.constant=(Constant)this.constant.clone();
        
    if (p.relocates!=null) {
      for (int i=0; i<this.relocates.length; i++) {
        p.relocates[i]=(Relocate)this.relocates[i].clone();
      }  
    }
    if (p.patches!=null) {
      for (int i=0; i<this.relocates.length; i++) {
        p.patches[i]=(Patch)this.patches[i].clone();
      }        
    }
    if (p.freezes!=null) {
      for (int i=0; i<this.freezes.length; i++) {
        p.freezes[i]=(Freeze)this.freezes[i].clone();
      }        
    }
      
    return p;
  }    

  @Override
  public boolean equals(Object o) {
    if (! (o instanceof Project)) return false;
    Project p=(Project)o;
    
    if (!Objects.equals(this.name,p.name)) return false;
    if (!Objects.equals(this.file,p.file)) return false;
    if (!Objects.equals(this.description,p.description)) return false;
    if (this.fileType != p.fileType) return false;
    if (this.targetType != p.targetType) return false;
    if (!Arrays.equals(this.memoryFlags, p.memoryFlags)) return false;
    if (!Arrays.equals(this.inB, p.inB)) return false;
    if (!Arrays.equals(this.memory, p.memory)) return false;
    if (this.mpr==null && p.mpr!=null) return false;
    if (this.mpr!=null && p.mpr==null) return false;
    if (this.mpr!=null && p.mpr!=null) {
      if (!this.mpr.header.equals(p.mpr.header)) return false;
      if (this.mpr.block!=p.mpr.block) return false;
      for (int i=0; i<this.mpr.block; i++) {
        if (!Arrays.equals((byte[])this.mpr.blocks.get(i), p.mpr.blocks.get(i))) return false;
      }  
    }
    
    for (int i=0; i<this.memory.length; i++) {
      if (!this.memory[i].equals(p.memory[i])) return false;   
    }   
    
    if (this.chip!=p.chip) return false;
    if (!this.constant.equals(p.constant)) return false;
    
    if (this.relocates!=null && p.relocates!=null) {
      for (int i=0; i<this.relocates.length; i++) {
        if (!this.relocates[i].equals(p.relocates[i])) return false;  
      }  
    }
    
    if (this.patches!=null && p.patches!=null) {
      for (int i=0; i<this.patches.length; i++) {
        if (!this.patches[i].equals(p.patches[i])) return false;  
      }  
    }
    if (this.freezes!=null && p.freezes!=null) {
      for (int i=0; i<this.freezes.length; i++) {
        if (!this.freezes[i].equals(p.freezes[i])) return false;  
      }  
    }
    
    return true;
  }
  
}
