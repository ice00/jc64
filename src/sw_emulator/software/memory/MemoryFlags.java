/**
 * @(#)MemoryFlags.java 2003/10/13
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

package sw_emulator.software.memory;

import sw_emulator.software.memory.memoryState;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Define a memory state of one program.
 * The state is given by flags like in Sidln program.
 *
 * @author Ice
 * @version 1.00 13/10/2003
 */
public class MemoryFlags implements memoryState {

  /** the memory with the sidln flags */
  byte[] memory;

  /**
   * Construct a memory flags status with the passed names of files
   *
   * @param names the names of the files
   */
  public MemoryFlags(String[] names) {
    if (names==null) {
      // all memory is of execution
      memory=getExecMemory(0, 65535);
    } else {
         // memory is from files
         memory=realAllBinFile(names);
      }
  } 

  /**
   * Get the memory state likes that all memory locations are of cpu instructions 
   *
   * @param startAddress the starting address of the memory to return
   * @param endAddress the ending address of memory to return  
   * @return the state of memory as a array of SIDLN flags from 
   *         <code>startAddress</code> and <code>endAddress</code>
   */  
  protected byte[] getExecMemory(int startAddress, int endAddress) {
    if (endAddress<startAddress) return null;
  
    byte[] res=new byte[endAddress-startAddress];
    for (int i=0; i<endAddress-startAddress; i++) {
      res[i]=memoryState.MEM_EXECUTE;
    }
    return res;
  }

  /**
   * Get the memory state as an array of SIDLN flags
   *
   * @param startAddress the starting address of the memory to return
   * @param endAddress the ending address of memory to return  
   * @return the state of memory as a array of SIDLN flags from 
   *         <code>startAddress</code> and <code>endAddress</code>
   */
  public byte[] getMemoryState(int startAddress, int endAddress) {
    if (endAddress<startAddress) return null;
  
    int size;  
    byte[] res=new byte[size=endAddress-startAddress];
    for (int i=0; i<size; i++) {
      // copy the needed portion
      res[i]=memory[startAddress+i];
    }
        
    return res;
  }
  
  /**
   * Read a BIN raw image of SIDLN flags
   *
   * @param name the name of the file to open
   * @return the memory read from file
   */
  protected byte[] readBinFile(String name) {
    int result=0;         // result of reading byte
    int done=0;           // number of bytes done
    byte[] memory=new byte[65535];    
    byte[] header=new byte[16];
    
    BufferedInputStream file;
    
    System.out.println("Reading file: "+name);

    // see if the file is present
    try {
      file=new BufferedInputStream(new FileInputStream(name));
    } catch (FileNotFoundException e) {
        System.out.println("Input file not found: abort"+e);
        return null;
      }
      catch (SecurityException e1) {
        System.out.println("Security exception in open the input file: abort\n");
        return null;
      }    

    // reading the header of file
    try {
      for (;;) {
        result=file.read(header, done, header.length-done);
        done+=result;
        if (done>=header.length) break;
        if (result<=0) {
          System.out.println("Not all the bytes readed from "+name);
          return null;
        }
      }        
    } catch (IOException e) {
        System.err.println(e);
        return null;
      } 
      
    String sHeader=new String(header);      
   
    if (!sHeader.equals("SIDLD RAM FLAGS ")) {
      System.out.println("Not a valid SIDLN ram image found");
    }
    
    done=0;
    // reading the body of file
    try {
      for (;;) {
        result=file.read(memory, done, memory.length-done);
        done+=result;
        if (done>=memory.length) break;
        if (result<=0) {
          System.out.println("Not all the bytes readed from "+name);
          return null;
        }
      }
      file.close();
    } catch (IOException e) {
        System.err.println(e);
        return null;
      }
    return memory;  
  }
  
  /**
   * Read all the passed raw bin files and compact all the information
   *
   * @param names the names of the files
   * @return the conpacted memory state of all files
   */
  protected byte[] realAllBinFile(String[] names) {
    byte[] memory=new byte[65535];
    byte[] res;
 
    for (int i=0; i<names.length; i++) {
      res=readBinFile(names[i]);
      memory=orMemory(res,memory);
    }
    return memory;
  }
  
  /**
   * Make the or of two memory map
   *
   * @param mem1 the firt mempry map
   * @param mem2 the second memory map
   * @return the resulting memory map
   */
  protected byte[] orMemory(byte[] mem1, byte[] mem2) {
    byte[] res=new byte[65535];
   
    for (int i=0; i<res.length; i++) {
      res[i]=(byte)(mem1[i] | mem2[i]);
    }
    
    return res;
  }
}
