/**
 * @(#)Memory.java 1999/09/01
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

package sw_emulator.hardware.memory;

import sw_emulator.hardware.bus.readableBus;
import sw_emulator.hardware.bus.writeableBus;
import java.lang.String;
import java.lang.ArrayIndexOutOfBoundsException;
import java.io.InputStream;
import java.io.IOException;

/**
 * This class emulate a memory chip.
 * It provides methods for reading and writing the memory locations. The memory
 * is characterized by it's size and starting address.
 * If you want to implement a ROM, you must ovverride the method that write in
 * memory.
 *
 * @author Ice
 * @version 1.01 25/04/2002
 */
public class Memory implements readableBus, writeableBus{

  /**
   * Contains the data in memory
   */
  public byte[] memory;

  /**
   * The size (in byte, 2^ multiple) of memory
   */
  public int size;

  /**
   * The starting address of memory
   */
  public int address;

  /**
   * Construct an empty block of memory location
   *
   * @param size the size in byte of memory
   * @param address the starting address of memory
   */
  public Memory(int size, int address) {
    init(size,address);
  }

  /**
   * This is provided only for derived class. Do not use.
   */
  protected Memory() {}

  /**
   * Initialize memory
   *
   * @param size the size in byte of memory
   * @param address the starting address of memory
   */
  public void init(int size, int address) {
    this.size=size;
    this.address=address;
    memory=new byte[size];
  }

  /**
   * Erase the content of memory
   */
  public void erase() {
    for (int i=0; i<size; i++)
      memory[i]=0;
  }

  /**
   * Load the memory with specific values retrive from a buffer
   *
   * @param buffer the buffer containing the data
   * @return true if the operation is ok
   */
  public boolean load(byte[] buffer) {
    int i; // local cycle variable

    try {
      for (i=0; i<size; i++)
        memory[i]=buffer[i];
    } catch (ArrayIndexOutOfBoundsException e) {
        for (int j=buffer.length+1; j<size; j++)
          memory[j]=0;         // fill the other with 0
      }
    return true;
  }

  /**
   * Load the memory with the values stored in a file
   *
   * @param name the name of the file
   * @return false if there's some error
   */
  public boolean load(String name) {
    int result=0;         // result of reading byte
    int done=0;           // number of bytes done

    InputStream file=getClass().getResourceAsStream(name);
    if (file==null) {
      System.err.println("File not found "+name);
      return false;
    }

    try {
      for (;;) {
        result=file.read(memory, done, memory.length-done);
        done+=result;
        if (done>=memory.length) break;
        if (result<=0) {
          System.out.println("Not all the bytes readed from "+name);
          return false;
        }
      }
      file.close();
    } catch (IOException e) {
        System.err.println(e);
        return false;
      }
    return true;
  }

  /**
   * Read a byte from the bus at specific address location.
   *
   * @param addr the address location
   * @return the readed byte
   */
  public byte read(int addr) {
    return memory[(addr-address) & (size-1)];
  }

  /**
   * Write a byte to the bus at specific address location.
   *
   * @param addr the address location
   * @param value the byte value
   */
  public void write(int addr, byte value) {
    memory[(addr-address) & (size-1)]=value;
  }

  /**
   * Change a byte in the memory at address location.
   * This is provide for patching the ROM or RAM.
   *
   * @param addr the address location
   * @param value the byte value
   */
  public void change(int addr, byte value) {
    memory[(addr-address) & (size-1)]=value;
  }
}
