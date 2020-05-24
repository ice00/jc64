/**
 * @(#)C64M6510IO.java 1999/10/15
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

import sw_emulator.hardware.chip.PLA82S100;
import sw_emulator.hardware.signaller;

/**
 * Manage the I/O of M6510 cpu in a C64 computer.
 * It has port 0,1,2,3,5 for output and 4 for input.
 *
 * @author Ice
 * @version 1.00 15/10/1999
 */
public class C64M6510IO extends M6510IO {

  /**
   * The PLA82S100 chip for notify the bits 0, 1, and 2 changes (MMU)
   */
  public PLA82S100 pla;

  /**
   * See the output port that has change their values and notify the news to
   * the appropriate chip.
   *
   * @param value the bits of port that have changes their value
   */
  public void advice(int value) {
    if ((value & 0x07)!=0) {        // are some MMU signal changed ?

      if ((value & 0x01)!=0)
        pla.notifySignal(signaller.S_LORAM, port.getP0());

      if ((value & 0x02)!=0)
        pla.notifySignal(signaller.S_HIRAM, port.getP1());

      if ((value & 0x04)!=0)
        pla.notifySignal(signaller.S_CHAREN, port.getP2());

      pla.monitor.opSignal();       // resume the pla for managing the changes
    }
  }

  /**
   * Construct the C64 M6510 I/O. Some output signals are default to up logical
   * value.
   *
   * @param pla the PLA82S100 chip in the Commodore 64
   */
  public C64M6510IO(PLA82S100 pla) {
    this.pla=pla;
    port.defaultP0=1;
    port.defaultP1=1;
    port.defaultP2=1;
    port.defaultP4=1;
    port.fallBackTime=350000; // 350 msec.
    port.isOpenP6=true;
    port.isOpenP7=true;
  }
}