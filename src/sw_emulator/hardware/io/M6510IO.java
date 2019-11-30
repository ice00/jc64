/**
 * @(#)M6510IO.java 1999/10/14
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

import sw_emulator.hardware.io.IO8;

/**
 * This class manage the 8 bidirectional I/O ports of the M6510 cpu, at register
 * data view.
 *
 * The <code>readFromPort</code> and <code>writeToPort</code> are provided for
 * the processor read and write operation.
 * The external chip may read (write) the output (input) port bits by using the
 * apposite methods of <code>port</code> variable of <code>IO6</code> class.
 *
 * @see sw_emulator.hardware.io.IO8
 *
 * @author Ice
 * @version 1.00 14/10/1999
 */
public class M6510IO {
  /**
   * The bidirectional I/O port of 8 bits
   */
  public IO8 port=new IO8();

  /**
   * Read the direction/value of I/O port
   *
   * @param addr the 0/1 address
   * @return the readed byte
   */
  public int readFromPort(int addr) {
    if (addr==0) {
      return port.portDir;
    } else {
        return port.getP0_()|
               port.getP1_()|
               port.getP2_()|
               port.getP3_()|
               port.getP4_()|
               port.getP5_()|
               port.getP6_()|
               port.getP7_();
      }
  }

  /**
   * Write the direction/value of I/O port
   *
   * @param addr the 0/1 address
   * @param value the value to write
   */
  public void writeToPort(int addr, int value) {
    if (addr==0) {               // write port bits direction
      port.portDir=value;
      advice(0xFF);              // advice for all (this is most general!)
    } else {                     // write port bits output value
        port.remember=0;
        port.setP0_(value & 0x01);
        port.setP1_(value & 0x02);
        port.setP2_(value & 0x04);
        port.setP3_(value & 0x08);
        port.setP4_(value & 0x10);
        port.setP5_(value & 0x20);
        port.setP6_(value & 0x40);
        port.setP7_(value & 0x80);
        if (port.remember!=0)
          advice(port.remember); // advice chip connected to output port
    }
  }

  /**
   * This method is provide to advice the connected chip to an output port that
   * the value is changed. The <code>value</code> variable contain the bit of
   * port that have changed the value. You should use a <code>Monitor</code> to
   * notify that a port value have change the value using an
   * <code>opSignal</code>, while the chip that attend output changes, must use
   * a <code>opWait</code> operation.
   *
   * @param value the bits of port that have changes their value
   */
  public void advice(int value) {
    // we do nothing (suppose output port not used)
  }
}