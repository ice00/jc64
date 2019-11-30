/**
 * @(#)signaller.java 1999/12/07
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
 * The interface <code>signaller</code> represents an electronical component
 * that can receive external signals.
 * The methods <code>notifySignal</code> is to be used for giving a signal to
 * the chip.
 * Many type of signals are provided with specific value: this are intended to
 * be standard for chips that implements <code>signaller</code> even if you can
 * use the codify you need.
 *
 * @author Ice
 * @version 1.00 07/12/1999
 */
public interface signaller {
  // external signals that cpu can receive
  public static final int S_RESET  = 1;    // Reset signal
  public static final int S_IRQ    = 2;    // Irq signal
  public static final int S_NMI    = 3;    // Nmi signal
  public static final int S_RDY    = 4;    // RDY (ready)
  public static final int S_AEC    = 5;    // AEC (tri-states)
  public static final int S_BA     = 6;    // BA
  public static final int S_SP     = 7;    // SP signal
  public static final int S_CNT    = 8;    // CNT signal
  public static final int S_TOD    = 9;    // TOD signal
  public static final int S_FLAG   = 10;   // FLAG signal
  public static final int S_LORAM  = 11;   // LORAM signal
  public static final int S_HIRAM  = 12;   // HIRAM signal
  public static final int S_CHAREN = 13;   // CHAREN signal
  public static final int S_VA14   = 14;   // VA14 signal
  public static final int S_VA15   = 15;   // VA15 signal
  public static final int S_GAME   = 16;   // GAME signal
  public static final int S_EXROM  = 17;   // EXROM signal
  public static final int S_DMA    = 18;   // DMA signal

  /**
   * Notify a signal to the chip
   *
   * @param type the type of signal
   * @param value the value of the signal (0/1)
   */
  public void notifySignal(int type, int value);
}