/**
 * @(#)Sid.java 2000/04/15
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

package sw_emulator.hardware.chip;

import sw_emulator.util.Monitor;
import sw_emulator.hardware.powered;
import sw_emulator.hardware.signaller;
import sw_emulator.hardware.bus.readableBus;
import sw_emulator.hardware.bus.writeableBus;
import sw_emulator.util.Monitor2;

/**
 * Emulate the Sid chip.
 *
 * @author Ice
 * @version 1.00 15/04/2000
 */
public /*abstract*/ class Sid extends Thread implements powered, signaller,
                                               readableBus, writeableBus{

  /**
   * The state of power
   */
  private boolean power=false;

  /**
   * The monitor where synchronization with a clock
   */
  protected Monitor monitor;

  /**
   * Write a byte to the bus at specific address location.
   *
   * @param addr the address location
   * @param value the byte value
   */
  public void write(int addr, byte value) {
    switch (addr) {
      default:
        // do nothing, write ignored
    }
  }

  /**
   * Read a byte from the bus at specific address location.
   *
   * @param addr the address location
   * @return the readed byte
   */
  public byte read(int addr) {
    switch (addr) {
      default :
        return (byte)0xFF;                           // not connected
    }
  }

  /**
   * Execute the cycles of SID according to external clock.
   */
  public void run() {
    while (true) {
      if (!power)                                  // there's power ?
        while (!power) {                           // no, attend power
          yield();                                  // give mutex
        }

      ///monitor.opSignal2();
      monitor.opWait();                            // attend synchronization
      //....
    }
  }

  /**
   * Notify a signal to the chip
   *
   * @param type the type of signal
   * @param value the value of the signal (0/1)
   */
  public void notifySignal(int type, int value) {
    switch (type) {
      case S_RESET:
        //....
        break;

      default:
        System.err.println("ERROR: an invalid "+type+
                           " signal was notify to SID");
    }
  }

 /**
   * Power on the electronic component
   */
  public void powerOn() {
      power=true;   // power is on
  }

  /**
   * Power off the electronic component
   */
  public void powerOff() {
      power=false;   // power is off
  }
}