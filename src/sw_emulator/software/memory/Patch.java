/**
 * @(#)Patch.java 1999/10/18
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

import sw_emulator.hardware.memory.Memory;
import java.lang.ArrayIndexOutOfBoundsException;

/**
 * Define a memory patch.
 * A patch is a memory address and some lists of patch bytes (one list for each
 * available patch for that address).
 *
 * @author Ice
 * @version 1.00 18/10/1999
 */
public class Patch {
  /**
   * Memory address where insert the pacth
   */
  protected int address;

  /**
   * The patch values
   */
  protected byte[][] values;

  /**
   * Construct a memory patch.
   * The passed <code>values</code> is an array of array where first is the
   * number of patches available and the others are the byte values.
   *
   * @param address memory address where insert the patch
   * @param values the patch values
   */
  public Patch(int address, byte[][] values) {
    this.address=address;
    this.values=values;
  }

  /**
   * Use the patch.
   * To the passed <code>memory</code> will be insert the pacth.
   *
   * @param number the number of the patch
   * @param memory the memory where insert the patch
   */
  public void usePatch(int number, Memory memory) {
    int i;

    try {
      for (i=0; i<values[number].length; i++) {
        memory.change(address, values[number][i]);
      }
    } catch (ArrayIndexOutOfBoundsException e) {}
  }
}