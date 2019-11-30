/**
 * @(#)AndPort.java 2000/01/21
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

package sw_emulator.util;

import sw_emulator.hardware.signaller;

/**
 * An And Port.
 * This is a part of a chip (74LS08), but for speed up porpuse it is divided
 * into his componenets and are thinked to use in the C64 emulator.
 * Due to it's simplicity, no monitor are used for activate this class like in
 * the other <code>Thread</code> chips.
 *
 * @author Ice
 * @version 1.00 21/01/2000
 */
public class AndPort implements signaller {
  /**
   * The output chip connected to this port
   */
  protected signaller output;

  /**
   * The input type signal 1 of port
   */
  protected int sig1;

  /**
   * The input type signal 2 of port
   */
  protected int sig2;

  /**
   * The output type signal of port
   */
  protected int sigOut;

  /**
   * The actual value of signal type 1
   */
  protected int val1=1;        // pulled up

  /**
   * The actual value of signal type 2
   */
  protected int val2=1;        // pulled up

  /**
   * Construct an and port.
   *
   * @param output the output chip to give signals
   * @param sig1 the type of input signal 1
   * @param sig2 the type of input signal 2
   * @param sigOut the type of output signal
   */
  public AndPort(signaller output, int sig1, int sig2, int sigOut) {
    this.output=output;
    this.sig1=sig1;
    this.sig2=sig2;
    this.sigOut=sigOut;
  }

  /**
   * Notify a signal to the chip
   *
   * @param type the type of signal
   * @param value the value of the signal (0/1)
   */
  public void notifySignal(int type, int value) {
    if (type==sig1) {
      val1=value;
      output.notifySignal(sigOut, val1 & val2);
    } else {
        if (type==sig2) {
          val2=value;
          output.notifySignal(sigOut, val1 & val2);
        } else {
            System.err.println("ERROR: an invalid "+type+
                           " signal was sent to an and port");
          }
      }
  }
}