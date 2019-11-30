/**
 * @(#)FlipFlop.java 1999/11/17
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

package sw_emulator.util;

/**
 * A FlipFlop with <code>set</code>, <code>reset</code> and <code>invert</code>
 * operations.
 *
 * @author Ice
 * @version 1.00 17/11/1999
 */
public class FlipFlop {
  /**
   * The state of the flip/flop
   */
  private boolean state=false;

  /**
   * Set the state of the flip/flop
   */
  public void set() {
    state=true;
  }

  /**
   * Reset the state of the flip/flop
   */
  public void reset() {
    state=false;
  }

  /**
   * Invert the state of the filp/flop
   */
  public void invert() {
   state=!state;
  }

  /**
   * Determine if the flip/flop is set
   *
   * @return true if the flip/flop is set
   */
  public boolean isSet() {
    return state;
  }

  /**
   * Determine if the flip/flop is reset
   *
   * @return true if the flip/flop is reset
   */
  public boolean isReset() {
    return !state;
  }
}