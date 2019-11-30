/**
 * @(#)ResetCartridge.java 1999/10/23
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

import sw_emulator.hardware.io.CartridgeIO;
import sw_emulator.hardware.bus.Bus;
import sw_emulator.util.Monitor;

/**
 * A reset cartridge.
 * This emulate a simple cartridge: a button for resetting the commodore 64.
 * Use the <code>pressButton</code> for generating a reset signal.
 *
 * @author Ice
 * @version 1.00 23/10/1999
 */
public class ResetCartridge extends Cartridge {


 /**
   * Construct a reset cartdrige.
   *
   * @param io the cartridge expansion port io
   * @param clock a monitor where synchronized with a clock
   * @param bus the external bus
   */
  public ResetCartridge(CartridgeIO io, Monitor clock, Bus bus) {
   super(io, clock, bus);
 }

  /**
   * Generate the reset signals
   */
  public void pressButton() {
    if (power) io.notifySignal(S_RESET, 1);
  }

  /**
   * The reset become normal
   */
  public void releasButton() {
    io.notifySignal(S_RESET, 0);
  }
}