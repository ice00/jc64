/**
 * @(#)DRAM.java 1999/11/03
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

/**
 * A <code>DRAM</code> is a dinamic memory device.
 * For a correct use of the memory a <code>refreshLine</code> may be done onto
 * a predefined timing interval.
 * Note that the timing is not provided; we want only to see how this device
 * should works.
 *
 * @author Ice
 * @version 1.00 02/09/1999
 */
public class DRAM extends Memory implements dinamic{
  /**
   * Construct an empty block of RAM memory locations
   *
   * @param size the size in byte of memory
   * @param address the starting address of memory
   */
  public DRAM(int size, int address) {
    super(size,address);
  }

  /**
   * Refresh the line <code>lineNumber</code> of a dinamic memory.
   *
   * @param lineNumber the number of line to refresh
   */
  public void refreshLine(int lineNumber) {
    // do nothing, we use refresh only for view how the real dinamic memory may
    // works
  }
}