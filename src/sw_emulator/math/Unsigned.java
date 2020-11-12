/**
 * @(#)Unsigned.java 1999/08/21
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

package sw_emulator.math;

/**
 * Calculate unsigned int of passed value
 *
 * @author Ice
 * @version 1.00 20/08/1999
 */
public final class Unsigned {

  /**
   * Calculate the unsigned int of a byte
   *
   * @param value the byte value to convert
   * @return the unsigned int of the value
   */
  public static final int done(byte value) {
    //int tmp=value;
    //if (tmp<0) tmp+=256;
    //return tmp;
    return (value & 0xFF);
  }

  /**
   * Calculate the unsigned int of a short
   *
   * @param value the byte value to convert
   * @return the unsigned int of the value
   */
  public static final int done(short value) {
    //int tmp=value;
    //if (tmp<0) tmp+=65438;
    //return tmp;
    return (value & 0xFFFF);
  }
}
