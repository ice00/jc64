/**
 * @(#)Cartridge.java 1999/10/23
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

package sw_emulator.hardware.cartridge;

import sw_emulator.util.Monitor;
import sw_emulator.hardware.bus.readableBus;
import sw_emulator.hardware.bus.writeableBus;
import sw_emulator.hardware.bus.Bus;
import sw_emulator.hardware.powered;
import sw_emulator.hardware.signaller;
import sw_emulator.hardware.io.CartridgeIO;

/**
 * A base cartridge that do nothing.
 * Note that it is a code>Thread</code>, becouse it may sinchronize with fase 2
 * of the commodore clock.
 * In derived class, the <code>run</code> method must be given with the
 * cartridge core, and <code>notifySignal</code> must be implemented.
 *
 * @author Ice
 * @version 1.00 23/10/1999
 */
public class Cartridge extends Thread implements powered, signaller,
                                                 readableBus, writeableBus {
  /** The state of power */
  protected boolean power=false;

  /** A monitor where synchronizer with clock */
  protected Monitor clock;

  /** The external bus */
  protected Bus bus;

  /** The cartridge IO */
  protected CartridgeIO io;

  /**
   * This is provided only for derived class. Do not use.
   */
  protected Cartridge() {
  }

  /**
   * Construct a cartdrige.
   *
   * @param io the cartridge expansion port io
   * @param clock a monitor where synchronized with a clock
   * @param bus the external bus
   */
  public Cartridge(CartridgeIO io, Monitor clock, Bus bus) {
    this.clock=clock;
    this.io=io;
    this.bus=bus;
    start();
  }

  /**
   * Read a byte from the bus at specific address location.
   * This is open space in null cartridge.
   *
   * @param addr the address location
   * @return the readed byte
   */
  public byte read(int addr) {
    return (byte)bus.previous;                       // get previous value
  }

  /**
   * Write a byte to the bus at specific address location.
   * this is open space in base cartridge
   *
   * @param addr the address location
   * @param value the byte value
   */
  public void write(int addr, byte value) {
    // nothing to do
  }

  /**
   * Power on the electronic component
   */
  public void powerOn() {
      power=true;                                  // power is on
  }

  /**
   * Power off the electronic component
   */
  public void powerOff() {
      power=false;                                 // power is off
  }

  /**
   * Notify a signal to the chip.
   * The body is empty because this is a base cartridge.
   *
   * @param type the type of signal
   * @param value the value of the signal (0/1)
   */
  public void notifySignal(int type, int value) {
  }

  /**
   * Set up the connection of IO with the external.
   * The cpu emulation is not started if this value is null equal.
   *
   * @param io the external connection
   */
  public void setIO(CartridgeIO ioPort) {
    this.io=io;
  }
}