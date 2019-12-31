/**
 * @(#)M6567R56A.java 2000/04/15
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

import sw_emulator.hardware.bus.readableBus;
import sw_emulator.hardware.bus.writeableBus;
import sw_emulator.hardware.bus.Bus;
import sw_emulator.hardware.io.VicII_IO;
import sw_emulator.hardware.memory.dinamic;
import sw_emulator.hardware.device.raster;
import sw_emulator.util.FlipFlop;
import sw_emulator.util.Monitor;
import sw_emulator.util.Monitor2;

/**
 * Emulate the Vic 6567R56A chip.
 * The Vic can access to 16Kbyte of memory at time, but his data bus is of 12
 * bits; the up 4 bits is for color.
 *
 * M6569 features:
 * <ul>
 *  <li>64 cycles for line</li>
 *  <li>262 lines</li>
 *  <li>234 visible lines</li>
 *  <li>411 visible pixels for line</li>
 *  <li>first vbank line: 13</li>
 *  <li>last vbank line: 40</li>
 *  <li>first X coo. of a line: 412</li>
 *  <li>first visible X coo.: 488</li>
 *  <li>last visible X coo.: 388</li>
 * </ul>
 *
 * Note: the variables and methods naming is like that are used in Vic Article.
 *
 * Reference:
 *  Vic Article of Christian Bauer
 *
 * @author Ice
 * @version 1.00 15/04/2000
 */
public class M6567R56A extends VicII {

  /**
   * Construct a VicII M6567R56A chip.
   *
   * @param extMonitor the external clock monitor
   * @param bus the bus
   * @param view the vic bus view
   * @param io the vic io
   * @param devicesToRefresh the devices to be refreshed
   * @param tv the raster TV attached to the Vic output
   */
  public M6567R56A(Monitor extMonitor, Bus bus, int view, VicII_IO io,
                                dinamic[] devicesToRefresh, raster tv) {
    super(extMonitor, bus, view, io, devicesToRefresh, tv);

    // set specific Vic values
    maxCycle=64;
    firstVblankLine=13;
    lastVblankLine=40;
    firstVisXCoo=488;
    lastVisXCoo=388;
    lastXPos=0x1ff;
    firstXCoo=412;
    linesNumber=262;


    raster=0;                                  // initialize raster position
    rasterX=firstXCoo;                         // at top of window
  }

  /**
   * Execute VIC cycle operation when the clock signal is low (phase 1)
   */
  public void fi0low() {
    cycleRaster0();                                  // reset some values
    cycleRaster30();                                 // bad line allowed?
    cycleIsBadLine();                                // is there a bad line?

    switch (cycle) {
      case 1:
        cycleMob3Pointer();                          // read mob 3 pointer
        break;

      case 2:
        cycleMob3Middle();                           // read mob 3 middle byte
        break;

      case 3:
        cycleMob4Pointer();                          // read mob 4 pointer
        break;

      case 4:
        cycleMob4Middle();                           // read mob 4 middle byte
        break;

      case 5:
        cycleMob5Pointer();                          // read mob 5 pointer
        break;

      case 6:
        cycleMob5Middle();                           // read mob 5 middle byte
        break;

      case 7:
        cycleMob6Pointer();                          // read mob 6 pointer
        break;

      case 8:
        cycleMob6Middle();                           // read mob 6 middle byte
        break;

      case 9:
        cycleMob7Pointer();                          // read mob 7 pointer
        break;

      case 10:
        cycleMob7Middle();                           // read mob 7 middle byte
        break;

      case 11:
        cycleRefresh();                             // refresh memory
        setBA(1);                                   // BA is now high
        break;

      case 12:
      case 13:
      case 14:
        cycleRefresh();                              // refresh memory
        cycleSetVicCounter();                        // set VC
        cycleIsCAccess();                            // is c-access ?
        break;

      case 15:
        cycleRefresh();                              // refresh memory
        cycleMobCounterInc2();                       // inc. mob counter base
        cycleIsCAccess();                            // is c-access ?
        break;

      case 16:
        cycleGAccess();                              // performs g-access
        cycleMobCounterInc1();                       // inc. mob counter base
        cycleIsCAccess();                            // is c-access ?
        break;

      case 17:
      case 18:
      case 19:
      case 20: case 21: case 22: case 23: case 24:
      case 25: case 26: case 27: case 28: case 29:
      case 30: case 31: case 32: case 33: case 34:
      case 35: case 36: case 37: case 38: case 39:
      case 40: case 41: case 42: case 43: case 44:
      case 45: case 46: case 47: case 48: case 49:
      case 50: case 51: case 52: case 53: case 54:
        cycleGAccess();                              // performs g-access
        cycleIsCAccess();                            // is c-access ?
        break;

      case 55:
        cycleGAccess();                              // performs g-access
        cycleInvertExp();                            // invert exp. flip flop
        cycleAllowDMA();                             // allow DMA on
        break;

      case 56:
        i_access();                                  // idle access
        cycleAllowDMA();                             // allow DMA on
        cycleMob0Allow();                            // BA low if mob 0 allow
        break;

      case 57:
        i_access();                                  // idle access
        break;

      case 58:
        i_access();                                  // idle access
        cycleMob1Allow();                            // BA low if mob 1 allow
        cycleMobDisplay();                           // set display of mobs
        cycleGotoIdle();                             // try for going to idle
        break;

      case 59:
        cycleMob0Pointer();                          // read sprite pointer 0
        break;

      case 60:
        cycleMob0Middle();                           // read mob 0 middle byte
        break;

      case 61:
        cycleMob1Pointer();                          // read mob 1 pointer
        break;

      case 62:
        cycleMob1Middle();                           // read mob 1 middle byte
        break;

      case 63:
        cycleMob2Pointer();                          // read mob 2 pointer
        break;

      case 64:
        cycleMob2Middle();                           // read mob 2 middle byte
        cycleBorderComp();                           // border comparison (or 63?)
        break;
    }

    cycleAEC();                                      // search for AEC goes 0
  }

  /**
   * Execute VIC cycle operation when the clock signal is high (phase 2)
   */
  public void fi0high() {
    switch (cycle) {
      case 1:
        cycleMob3Left();                             // read mob 3 left byte
        break;

      case 2:
        cycleMob3Right();                            // read mob 3 right byte
        break;

      case 3:
        cycleMob4Left();                             // read mob 4 left byte
        break;

      case 4:
        cycleMob4Right();                            // read mob 4 right byte
        break;

      case 5:
        cycleMob5Left();                             // read mob 5 left byte
        break;

      case 6:
        cycleMob5Right();                            // read mob 5 right byte
        break;

      case 7:
        cycleMob6Left();                             // read mob 6 left byte
        break;

      case 8:
        cycleMob6Right();                            // read mob 6 right byte
        break;

      case 9:
        cycleMob7Left();                             // read mob 7 left byte
        break;

      case 10:
        cycleMob7Right();                            // read mob 7 right byte
        break;

      case 15: case 16: case 17: case 18: case 19:
      case 20: case 21: case 22: case 23: case 24:
      case 25: case 26: case 27: case 28: case 29:
      case 30: case 31: case 32: case 33: case 34:
      case 35: case 36: case 37: case 38: case 39:
      case 40: case 41: case 42: case 43: case 44:
      case 45: case 46: case 47: case 48: case 49:
      case 50: case 51: case 52: case 53: case 54:
        cycleCAccess();                              // performs c-access
        break;

      case 58:
        cycleMob0Left();                             // read mob 0 left byte
        break;

      case 59:
        cycleMob0Right();                            // read mob 0 right byte
        break;

      case 60:
        cycleMob1Left();                             // read mob 1 left byte
        break;

      case 61:
        cycleMob1Right();                            // read mob 1 right byte
        break;

      case 62:
        cycleMob2Left();                             // read mob 2 left byte
        break;

      case 63:
        cycleMob2Right();                            // read mob 2 right byte
        break;
    }
  }
}