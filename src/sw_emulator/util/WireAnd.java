/**
 * @(#)WireAnd.java 2000/02/15
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

package sw_emulator.util;

/**
 * Emulate a wire and of 2 line.
 * This class use two input lines and a output line of the wire and.
 * A 0 value means GROUND signal, a 1 value means no signal, but it can be used
 * as 1 signal in some MOS chips.
 *
 * @author Ice
 * @version 1.00 15/02/2000
 */
public class WireAnd {
  /**
   * First line of the wire
   */
  private int in1=1;

  /**
   * Second line of the wire
   */
  private int in2=1;

  /**
   * The output of the wire and
   */
  public int out=1;

  /**
   * Set or unset the line 1 using passed value
   *
   * @param val the 0/1 value
   */
  public void line1(int val) {
    if (val==0) setLine1();
    else unsetLine1();
  }

  /**
   * Set the line 1 to GROUND
   */
  public void setLine1() {
    in1=0;
    out=0;
  }

  /**
   * Unset the line 1 from GROUND
   */
  public void unsetLine1() {
    in1=1;
    out=in2;
  }

  /**
   * Set or unset the line 2 using passed value
   *
   * @param val the 0/1 value
   */
  public void line2(int val) {
    if (val==0) setLine2();
    else unsetLine2();
  }

 /**
   * Set the line 2 to GROUND
   */
  public void setLine2() {
    in2=0;
    out=0;
  }

  /**
   * Unset the line 2 from GROUND
   */
  public void unsetLine2() {
    in2=1;
    out=in1;
  }
}