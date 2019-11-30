/**
 * @(#)Bus.java 1999/08/16
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

package sw_emulator.hardware.bus;

/**
 * Provide methods for writing and reading from a bus for different
 * device bus view.
 * These methods are abstract, so you must implement them.
 * Use the <code>view</code> variables for known who is that make an operation
 * to the bus for give it the appropriate memory location.
 * The <code>previous</code> variable are provided if you want that something
 * know it (but you must update it), like the M6510 cpu of C64 that in a write
 * operation to ram 0/1 (his ports), the previous precedent value (that the Vic
 * has read in the half previus cycle), must be write to ram 0/1.
 * The <code>aec</code> parameters store the actual state of AEC signal that
 * the chip that uses the bus has.
 *
 * @author Ice
 * @version 1.00 16/08/1999
 */
public abstract class Bus {
  /**
   * Previous value that the data bus have had
   */
  public int previous=0;

  /**
   * Store a byte value in address position to a chip connected to the bus using
   * approprite device view.
   *
   * @param addr the address location
   * @param value the byte value
   * @param view the bus view of the device that write to the bus
   * @param aec the AEC signals state of the chip
   */
  public abstract void store(int addr, int value, int view, int aec);

  /**
   * Load a byte value from a chip that is connected to the bus at address
   * position using a view of one device.
   *
   * @param addr the address location
   * @param view the bus view of the device that read the bus
   * @param aec the AEC signals state of the chip
   * @return the readed byte stored in 32 bits
   */
  public abstract int load(int addr, int view, int aec);
  
  /**
   * Gives true if the bus is correctly initialized
   *
   * @return true if bus is initialized correctly
   */
  public abstract boolean isInitialized();
}



