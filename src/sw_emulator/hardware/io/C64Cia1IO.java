/**
 * @(#)C64Cia1IO.java 1999/12/29
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

import sw_emulator.util.Monitor;
import sw_emulator.util.WireAnd;
import sw_emulator.hardware.cpu.M6510;
import sw_emulator.hardware.cartridge.Cartridge;

/**
 * Emulate the Cia 1 I/O in the C64.
 *
 * @author Ice
 * @version 1.00 29/12/1999
 */
public class C64Cia1IO extends M6526IO {

  /**
   * C64 cpu
   */
  private M6510 cpu;

  /**
   * C64 cartridge expansion port
   */
  private Cartridge exp;

  /**
   * The C64 keyboard monitor connected to the Cia 1
   */
  private Monitor monitor;

  /**
   * The wire and line see by the Cia 1 port A
   */
  private WireAnd[] wireLine;

  /**
   * Remember the connected clocked chips
   *
   * @param cpu the C64 cpu
   * @param exp the C64 cartridge expansion port
   * @param monitor the C64 keyborad monitor
   * @param wireLine the wire line see by the Cia 1 port A
   */
  public C64Cia1IO(M6510 cpu, Cartridge exp, Monitor monitor,
                                          WireAnd[] wireLine) {
    this.cpu=cpu;
    this.exp=exp;
    this.monitor=monitor;
    this.wireLine=wireLine;
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
  @Override
  public void advice(int type, int value) {
    if (type==T_PORTA) {
      if ((value & 0x01)!=0) wireLine[0].line1(portA.getP0());
      if ((value & 0x02)!=0) wireLine[0].line1(portA.getP1());
      if ((value & 0x04)!=0) wireLine[0].line1(portA.getP2());
      if ((value & 0x08)!=0) wireLine[0].line1(portA.getP3());
      if ((value & 0x10)!=0) wireLine[0].line1(portA.getP4());
      if ((value & 0x20)!=0) wireLine[0].line1(portA.getP5());
      if ((value & 0x40)!=0) wireLine[0].line1(portA.getP6());
      if ((value & 0x80)!=0) wireLine[0].line1(portA.getP7());

      monitor.opSignal();                     // resume the keyboard
    }
  }

  /**
   * Notify a signal to the chip
   *
   * @param type the type of signal
   * @param value the value of the signal (0/1)
   */
  @Override
  public void notifySignal(int type, int value) {
    switch (type) {
      case S_IRQ:                                     // notify IRQ signal
        cpu.notifySignal(type, value);                // to Cpu
        exp.notifySignal(type, value);                // to Cartridge
        break;
    }
  }
}