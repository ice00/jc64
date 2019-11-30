/**
 * @(#)ROM.java 1999/09/02
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

import sw_emulator.hardware.memory.Memory;
import java.lang.String;

/**
 * Create a ROM memory by disabling the <code>write</code> method
 *
 * @author Ice
 * @version 1.00 02/09/1999
 */
public class ROM extends Memory {

  /**
   * Construct an empty block of memory location
   *
   * @param size the size in byte of memory
   * @param address the starting address of memory
   */
  public ROM(int size, int address) {
    super(size, address);
  }

  /**
   * Write a byte to the bus at specific address location.
   * Does nothing becouse the ROM can not be writing more of one
   *
   * @param addr the address location
   * @param value the byte value
   */
   public void write(int addr, byte value) {
   }
}