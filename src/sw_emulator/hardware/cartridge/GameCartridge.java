/**
 * @(#)Cartridge.java 2000/06/25
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
import sw_emulator.hardware.bus.Bus;
import sw_emulator.hardware.io.CartridgeIO;
import sw_emulator.hardware.memory.ROM;

/**
 * A game cartridge with two ROM (of 8KB) chip at address 8000h and A000h.
 * In this type of cartridge the GAME and EXROM are set to 0. Than there are
 * two 8KB ROM memory chip (and I see a space for another memory chip in
 * the schede, not used). One chip is enabled with ROML, the other with ROMH.
 *
 * @author Ice
 * @version 1.00 25/06/2000
 */
public class GameCartridge extends Cartridge {

  /** The 8KB ROM at address 8000h */
  protected ROM roml=new ROM(8*1024, 0x8000);

  /** The 8KB ROM at address A000h */
  protected ROM romh=new ROM(8*1024, 0xA000);

  /**
   * Construct a game cartdrige.
   *
   * @param io the cartridge expansion port io
   * @param clock a monitor where synchronized with a clock
   * @param bus the external bus
   * @param romlData the content of roml
   * @param romhData the content of romh
   */
  public GameCartridge(CartridgeIO io, Monitor clock, Bus bus,
                   byte[] romlData, byte[] romhData) {
    super(io, clock, bus);
    roml.load(romlData);                 // load the ROMl
    romh.load(romhData);                 // load the ROMH
  }

  /**
   * Read a byte from the bus at specific address location.
   *
   * @param addr the address location
   * @return the readed byte
   */
  public byte read(int addr) {
    switch (addr & 0xE000) {
      case 0x8000:
        return  roml.read(addr);
      case 0xA000:
        return  romh.read(addr);
      case 0xD000:
        return (byte)bus.previous;                       // get previous value
      default:
        System.err.println("Error: invalid "+addr+" add. for cartridge");
        return 0;
    }
  }

  /**
   * The body of cartridge.
   * For speed up porpoise, the GAME and EXROM are send to PLA only when power
   * become on.
   */
  public void run() {
    while (true) {
      while (!power) {
        yield();
      }                                                  // give mutex to other

      io.notifySignal(S_GAME, 0);
      io.notifySignal(S_EXROM, 0);

      while (power) {
        yield();                                         // give mutex to other
      }
    }
  }
}