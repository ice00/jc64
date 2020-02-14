/**
 * @(#)IO8.java 1999/10/24
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
 * This class manage a 8 bits bidirectional I/O port.
 * See <code>IO6</code> for the details
 *
 * @see IO6
 * @author Ice
 * @version 1.01 27/05/2000
 */
public class IO8 extends IO6{
  /**
   * Default output value of port 6 if it is set to input
   */
  public int defaultP6=1;

  /**
   * Default output value of port 7 if it is set to input
   */
  public int defaultP7=1;

  /**
   * Set the value of input port 6
   * The value is store in input buffer
   *
   * @param bit the bit value of input port
   */
  public void setP6(int bit) {
    if (bit==0) portDataIn&=~0x40;
    else portDataIn|=0x40;
  }

  /**
   * Set the value of input port 7
   * The value is store in input buffer
   *
   * @param bit the bit value of input port
   */
  public void setP7(int bit) {
    if (bit==0) portDataIn&=~0x80;
    else portDataIn|=0x80;
  }

  /**
   * Get the value of output port 6
   * If the port if not of output we return a default value
   *
   * @return the 0/1 bit value
   */
  public int getP6() {
    if ((portDir & 0x40)!=0)       // output port ?
      return (portDataOut & 0x40)>>6;
      else return defaultP6;
  }

  /**
   * Get the value of output port 7
   * If the port if not of output we return a default value
   *
   * @return the 0/1 bit value
   */
  public int getP7() {
    if ((portDir & 0x80)!=0)       // output port ?
      return (portDataOut & 0x80)>>7;
      else return defaultP7;
  }

  /**
   * Set the value of output port 6 (by internal operation)
   * If the value is different from the precedent, we remember this occurence.
   *
   * @param value the value to give to bit (it is just correctly shifted)
   */
  protected void setP6_(int value) {
    if ((portDir & 0x40)!=0) { // output port ?
      if (((portDataOut & 0x40)^value)!=0) remember|=0x40;
    }
    if (value!=0) portDataOut|=value;
      else portDataOut&=~0x40;
  }

  /**
   * Set the value of output port 7 (by internal operation)
   * If the value is different from the precedent, we remember this occurence.
   *
   * @param value the value to give to bit (it is just correctly shifted)
   */
  protected void setP7_(int value) {
    if ((portDir & 0x80)!=0) { // output port ?
      if (((portDataOut & 0x80)^value)!=0) remember|=0x80;
    }
    if (value!=0) portDataOut|=value;
    else portDataOut&=~0x80;
  }

  /**
   * Get the value of input port 6 (by internal operation)
   *
   * @return the 0/1 bit correctly shifted
   */
  protected int getP6_() {
    if ((portDir & 0x40)==0)   // port of input ?
      return (portDataIn & (defaultP6<<6) & 0x40);
    else return (portDataIn & portDataOut & 0x40);
  }

  /**
   * Get the value of input port 7 (by internal operation)
   *
   * @return the 0/1 bit correctly shifted
   */
  protected int getP7_() {
    if ((portDir & 0x80)==0)   // port of input ?
      return (portDataIn & (defaultP7<<7) & 0x80);
    else return (portDataIn & portDataOut & 0x80);
  }
}