/**
 * @(#)M6526IO.java 1999/10/24
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
import sw_emulator.hardware.signaller;

/**
 * This class manage the two 8 bidirectional I/O ports of the M6526 cia, at
 * register data view and all the output signals.
 *
 * The <code>readFromPort</code> and <code>writeToPort</code> are provided for
 * the cia read and write operations.
 * The external chip may read (write) the output (input) port bits by using the
 * apposite methods of <code>port</code> variable of <code>IO8</code> class.
 *
 * The <code>advice</code> method is to be used for notify output signal
 * changes to not clocked chips, while <code>notifySignal</code> may be used for
 * clocked chips.
 *
 * @see sw_emulator.hardware.io.IO8
 *
 * @author Ice
 * @version 1.00 24/10/1999
 */
public abstract class M6526IO implements signaller {
  // type of I/O that advice for
  public static final int T_PORTA=0; // port A
  public static final int T_PORTB=1; // port B

  /**
   * The A bidirectional I/O port of 8 bits
   */
  public IO8 portA=new IO8();

  /**
   * The B bidirectional I/O port of 8 bits
   */
  public IO8 portB=new IO8();

  /**
   * Read the direction/value of I/O port
   *
   * @param addr the 0/1 address
   * @return the readed byte
   */
  public int readFromPort(int addr) {
    switch (addr) {
      case 0x00:
        return portA.getP0_()|
               portA.getP1_()|
               portA.getP2_()|
               portA.getP3_()|
               portA.getP4_()|
               portA.getP5_()|
               portA.getP6_()|
               portA.getP7_();
      case 0x01:
        return portB.getP0_()|
               portB.getP1_()|
               portB.getP2_()|
               portB.getP3_()|
               portB.getP4_()|
               portB.getP5_()|
               portB.getP6_()|
               portB.getP7_();
      case 0x02:
        return portA.portDir;
      case 0x03:
        return portB.portDir;
    }
    return 0;
  }

  /**
   * Write the direction/value of I/O port
   *
   * @param addr the 0/1 address
   * @param value the value to write
   */
  public void writeToPort(int addr, int value) {
    switch (addr) {
      case 0x00:
        portA.remember=0;
        portA.setP0_(value & 0x01);
        portA.setP1_(value & 0x02);
        portA.setP2_(value & 0x04);
        portA.setP3_(value & 0x08);
        portA.setP4_(value & 0x10);
        portA.setP5_(value & 0x20);
        if (portA.remember!=0)
          advice(T_PORTA, portA.remember); // advice chip connected to output port
        break;
      case 0x01:
        portB.remember=0;
        portB.setP0_(value & 0x01);
        portB.setP1_(value & 0x02);
        portB.setP2_(value & 0x04);
        portB.setP3_(value & 0x08);
        portB.setP4_(value & 0x10);
        portB.setP5_(value & 0x20);
        if (portB.remember!=0)
          advice(T_PORTB, portB.remember); // advice chip connected to output port
        break;
      case 0x02:                    // write port A bits direction
        portA.portDir=value & 0xFF;
        advice(T_PORTA, 0x3F);      // advice for all (this is most general!)
        break;
      case 0x03:                    // write port B bits direction
        portB.portDir=value & 0xFF;
        advice(T_PORTB, 0x3F);      // advice for all (this is most general!)
        break;
    }
  }

  /**
   * This method is provide to advice the connected chip to an output port that
   * the value is changed. The <code>value</code> variable contain the bit of
   * port that has changed the value. You should use a <code>Monitor</code> to
   * notify that a port value has change the value using an
   * <code>opSignal</code>, while the chip that attend output changes, must use
   * a <code>opWait</code> operation. If some bits are used by clocked signals,
   * then you must call <code>notifySignal</code> for this chips.
   *
   * @param type the type of I/O that <code>advice</code> is for.
   * @param value the bits of port that have changes their value
   */
  public abstract void advice(int type, int value);
}
