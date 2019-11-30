/**
 * @(#)ColorRAM.java 1999/10/02
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

import sw_emulator.hardware.bus.Bus;

/**
 * This is the color Ram chip: 4 bits for a stored data.
 * Only the 4 lower bits are connected. The upper 4 bits are taken from previous
 * present value in the data bus.
 * Note: the upper 4 bits are significative only for the CPU read, while a VIC
 * read only the low effective readed.
 *
 * @author Ice
 * @version 1.00 02/10/1999
 */
public class ColorRAM extends Memory {
  /**
   * The bus that color ram used
   */
  private Bus bus;

  /**
   * Construct an empty block of color memory chip of C64
   *
   * @param size the size in byte of memory
   * @param address the starting address of memory
   */
  public ColorRAM(int size, int address, Bus bus) {
    super(size, address);
    this.bus=bus;
  }

  /**
   * Read a byte from the bus at specific address location.
   * The chip store only the lower 4 bits, so when we read from these locations
   * the upper 4 bits are not determinate. In the C64 the upper 4 bits came out
   * from the previous readed byte.
   *
   * @param addr the address location
   * @return the readed byte
   */
  public byte read(int addr) {
    return (byte)((memory[(addr-address) & (size-1)] & 0x0F)|
                 (bus.previous & 0xF0));
  }

  /**
   * Write a byte to the bus at specific address location.
   * Only the lower 4 bits are stored
   *
   * @param addr the address location
   * @param value the byte value
   */
  public void write(int addr, byte value) {
    memory[(addr-address) & (size-1)]=(byte)(value & 0x0F);
  }
}