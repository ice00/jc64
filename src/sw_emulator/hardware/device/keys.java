/**
 * @(#)Keys.java 2000/04/16
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

package sw_emulator.hardware.device;

/**
 * This interface define some methods that a matrix keyboard may use.
 * Remember that a matrix keyboard is a type of keyboard, where there's n*m
 * keys connected in a matrix like this:
 * <pre>
 *        0   0   0
 *        |   |   |
 *  A  ---@---@---@---          @= a key switch
 *        |   |   |
 *  B  ---@---@---@---
 *        |   |   |
 *        C   D   E
 * </pre>
 * The methods <code>pressKey</code> and <code>releaseKey</code> are to be
 * called where a key in the matrix is pressed ore released.
 *
 * @author Ice
 * @version 1.00 16/04/2000
 */
public interface keys {
  /**
   * Press the key in the <code>a, b</code> matrix position
   *
   * @param a the row position
   * @param b the column position
   */
  public void pressKey(int a, int b);

  /**
   * Release the key in the <code>a, b</code> matrix position
   *
   * @param a the row position
   * @param b the column position
   */
  public void releaseKey(int a, int b);
}