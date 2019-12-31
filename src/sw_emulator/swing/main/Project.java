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

import sw_emulator.software.MemoryDasm;

/**
 * Container for the project
 * 
 * @author ice
 */
public class Project implements Cloneable{ 
  /** Actual version of project */ 
  public static final byte ACTUAL_VERSION=0;       
    
  /**
   * Type of the file
   */
  public FileType fileType;
  
  /** Name of the project */  
  public String name; 
  
  /** Path + name of file to disassemblate */
  public String file;
  
  /** Description of the file */
  public String description;
  
  /** Contains the data of input file */
  public byte[] inB;
  
  /** Memory flag of SIDLD */
  public byte[] memoryFlags=new byte[0xFFFF+1];
  
  /** Memory for dasm*/
  public MemoryDasm[] memory=new MemoryDasm[0xFFFF+1];

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
  }
  
  @Override
  public Project clone() {
    try {
      Project p = (Project) super.clone();
      return p;
    } catch (CloneNotSupportedException e) {
        System.err.println();
        return null;
      }
  }    
}
