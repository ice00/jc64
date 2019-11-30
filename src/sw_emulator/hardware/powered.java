/**
 * @(#)powered.java 1999/10/02
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

package sw_emulator.hardware;

/**
 * The interface <code>powered</code> represents an electronical component that
 * can be powerd on and off.
 * The methods <code>powerOn</code> and <code>powerOff</code> are to be used
 * for giving on/off power to the component.
 *
 * @author Ice
 * @version 1.00 02/10/1999
 */
public interface powered {
  /**
   * Power on the electronic component
   */
  public void powerOn();

  /**
   * Power off the electronic component
   */
  public void powerOff();
}