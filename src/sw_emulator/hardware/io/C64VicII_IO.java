/**
 * @(#)C64VicII_IO.java 1999/12/29
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

import sw_emulator.util.AndPort;
import sw_emulator.hardware.cpu.M6510;
import sw_emulator.hardware.cartridge.Cartridge;

/**
 * Emulate the IO of the VIC II chip in the C64
 * The methods <code>notifySignal</code> notify the Vic signals to the right
 * chips like in the C64.
 * In this case the chips that receive Vic signals are cpu and cartridge, but
 * for cpu there's AEC and BA signals that pass throw logical and with DMA
 * signal from cartridge.
 *
 * @author Ice
 * @version 1.00 29/12/1999
 */
public class C64VicII_IO extends VicII_IO {
  /**
   * The cpu view by the Vic in the C64
   */
  protected M6510 cpu;

  /**
   * The cartridge port view by Vic in the C64
   */
  protected Cartridge exp;

  /**
   * The 74LS08 And port pin output 6
   */
  protected AndPort and6;

  /**
   * The 74LS08 And port pin output 3
   */
  protected AndPort and3;

  /**
   * Memorize VIC IO chips connection in the C64
   *
   * @param cpu the cpu view by Vic in the C64
   * @param exp the cartridge view by Vic in the C64
   * @param and6 the 74LS08 And port 6
   * @param and3 the 74LS08 And port 3
   */
  public C64VicII_IO(M6510 cpu, Cartridge exp,
                     AndPort and6, AndPort and3) {
    this.cpu=cpu;
    this.exp=exp;
    this.and6=and6;
    this.and3=and3;
  }

  /**
   * Notify a signal to the chips like in the C64
   * The signals used are:
   * <ul>
   *   <li>IRQ </li>
   *   <li>AEC </li>
   *   <li>BA  </li>
   * </ul>
   *
   * @param type the type of signal
   * @param value the value of the signal (0/1)
   */
  public void notifySignal(int type, int value) {
    switch (type) {
      case S_IRQ:
        cpu.notifySignal(type, value);            // notify to cpu
        exp.notifySignal(type, value);            // notify to exp
        break;
      case S_AEC:
        and6.notifySignal(type, value);           // notify indirectly to cpu
        exp.notifySignal(type, value);            // notify to exp
        break;
      case S_BA:
        and3.notifySignal(type, value);           // notify indirectly to cpu
        exp.notifySignal(type, value);            // notify to expansion
        break;
      default:
        System.err.println("ERROR: an invalid "+type+
                           " signal was sent by VicII to external chips");
    }
  }
}