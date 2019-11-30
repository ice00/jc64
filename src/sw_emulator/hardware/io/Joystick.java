/**
 * @(#)Joystick.java 2000/02/15
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

package sw_emulator.hardware.io;

/**
 * Define a digital Joystick operation
 *
 * @author Ice
 * @version 1.00 15/02/2000
 */
public abstract class Joystick {
  /**
   * Set Joystick direction 0
   */
  public abstract void setJoy0();

  /**
   * Set Joystick direction 1
   */
  public abstract void setJoy1();

  /**
   * Set Joystick direction 2
   */
  public abstract void setJoy2();

  /**
   * Set Joystick direction 3
   */
  public abstract void setJoy3();

  /**
   * Set Joystick button
   */
  public abstract void setJoyBut();

  /**
   * Reset Joystick direction 0
   */
  public abstract void resetJoy0();

  /**
   * Reset Joystick direction 1
   */
  public abstract void resetJoy1();

  /**
   * Reset Joystick direction 2
   */
  public abstract void resetJoy2();

  /**
   * Reset Joystick direction 3
   */
  public abstract void resetJoy3();

  /**
   * Reset Joystick button
   */
  public abstract void resetJoyBut();
}