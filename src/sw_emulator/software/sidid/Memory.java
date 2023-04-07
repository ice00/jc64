/**
 * @(#)Memory.java 2023/04/07
 *
 * ICE Team Free Software Group
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
package sw_emulator.software.sidid;

import java.io.File;
import java.io.FileOutputStream;

/**
 *Sidld memory
 *  
 * @author ice
 */
public class Memory {
  public final int MEM_NONE          = 0x00;
  public final int MEM_READ          = 0x01;
  public final int MEM_WRITE         = 0x02;
  public final int MEM_EXECUTE       = 0x04;
  public final int MEM_READ_FIRST    = 0x10;
  public final int MEM_WRITE_FIRST   = 0x20;
  public final int MEM_EXECUTE_FIRST = 0x40;
  public final int MEM_SAMPLE        = 0x80;  // never used anymore
  
  /** Memory of C64 */
  byte[] memory=new byte[0x10000];
  
  /** Use static access to reduce patching of file */
  public static Memory instance=new Memory();
  
  private Memory() {    
  }
  
  /**
   * Clear the actual data
   */
  public void clear() {
    memory=new byte[0x10000];
  }
  
  /**
   * Set previous memory address as executed 
   * 
   * @param address the address
   */
  public void setExecuteMinus(int address) {
    if (--address>=0) {
      memory[address] |= MEM_EXECUTE;
    
      if ((memory[address] & (MEM_READ_FIRST | MEM_WRITE_FIRST | MEM_EXECUTE_FIRST))==0) memory[address] |= MEM_EXECUTE_FIRST;  
    } 
  }
  
  /**
   * Set this memory address as executed 
   * 
   * @param address the address
   */
  public void setExecute(int address) {     
    memory[address] |= MEM_EXECUTE;
    
    if ((memory[address] & (MEM_READ_FIRST | MEM_WRITE_FIRST | MEM_EXECUTE_FIRST))==0) memory[address] |= MEM_EXECUTE_FIRST;
  }
  
  /**
   * Set this memory address as read 
   * 
   * @param address the address
   */
  public void setRead(int address) {
    memory[address] |= MEM_READ;
    
    if ((memory[address] & (MEM_READ_FIRST | MEM_WRITE_FIRST | MEM_EXECUTE_FIRST))==0) memory[address] |= MEM_READ_FIRST;    
  }
  
  /**
   * Set this memory address as write
   * 
   * @param address the address
   */
  public void setWrite(int address) {
    memory[address] |= MEM_WRITE;
    
    if ((memory[address] & (MEM_READ_FIRST | MEM_WRITE_FIRST | MEM_EXECUTE_FIRST))==0) memory[address] |= MEM_WRITE_FIRST;    
  }
  
  /**
   * Close and so write file of data
   * 
   * @param name the name of tune
   * @param tune the tune number
   */
  public void close(String name, int tune) {
    byte[] heather={0x53, 0x49, 0x44, 0x4c, 0x44, 0x20, 0x52, 0x41, 
                    0x4d, 0x20, 0x46, 0x4c, 0x41, 0x47, 0x53, 0x20};  
      
    try {
      File outputFile = new File(name+"_"+tune+".bin");
      FileOutputStream outputStream = new FileOutputStream(outputFile);
      outputStream.write(heather);
      outputStream.write(memory);
      outputStream.flush();
      outputStream.close();        
    } catch (Exception e) {
        System.err.println(e);
      } 
  }
}