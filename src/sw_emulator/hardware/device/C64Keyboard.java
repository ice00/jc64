/**
 * @(#)C64Keyboard.java 2000/04/16
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

package sw_emulator.hardware.device;

import sw_emulator.util.Monitor;
import sw_emulator.util.WireAnd;
import sw_emulator.hardware.io.C64Cia1IO;

/**
 * Emulate the C64 matrix keyboard.
 * This is the C64 matrix keyboard layout:
 * <pre>
 *    PB    0     1     2     7     4     5     6     3
 *         |     |     |     |     |     |     |     |
 *     ----1---- <- --CTRL--STOP-SPACE--- -----O-----2---
 *         |     |     |     |     |     |     |     |
 *     ----3-----W-----A---SHIFT---Z-----S-----E-----4---
 *         |     |     |     |     |     |     |     |
 *     ----5-----R-----D-----X-----C-----F-----T-----6---
 *         |     |     |     |     |     |     |     |
 *     ----7-----Y-----G-----V-----@-----H-----U-----8---
 *         |     |     |     |     |     |     |     |
 *     ----9-----I-----J-----N-----M-----K-----D-----0---
 *         |     |     |     |     |     |     |     |
 *     --- + ----P-----L-----,-----.----- ----- ---- - --
 *         |     |     |     |     |     |     |     |
 *     ---- -----*-----:-----;---SHIFT---=-----^----CLR--
 *         |     |     |     |     |     |     |     |
 *     ---DEL---RET---L/R---U/D---F1----F3----F5----   --
 *         |     |     |     |     |     |     |     |
 *
 * </pre>
 * This class manage also the wire and lines that something input and output
 * lines connected to the keyboard have (due to the joystick connection).
 * For speed up function, the wire and are used for all the input and output
 * lines: a line that are not wire and, means that the second line of
 * <code>WireAnd</code> are not used.
 *
 * @author Ice
 * @version 1.00 16/04/2000
 */
public class C64Keyboard extends Keyboard{
  /**
   * The matrix for keys in the C64 keyboard.
   * The last line has the RESTORE keys like the last key)
   */
  protected static int[][] c64Matrix={
   {K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE,
    K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE},
   {K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE,
    K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE},
   {K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE,
    K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE},
   {K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE,
    K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE},
   {K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE,
    K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE},
   {K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE,
    K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE, K_RELEASE}
  };

  /**
   * The wire columns lines view by keyboard
   */
  public WireAnd[] colLines={new WireAnd(), new WireAnd(),
                             new WireAnd(), new WireAnd(),
                             new WireAnd(), new WireAnd(),
                             new WireAnd(), new WireAnd()};

  /**
   * The wire rows lines view by keyboard
   */
  public WireAnd[] rowLines={new WireAnd(), new WireAnd(),
                             new WireAnd(), new WireAnd(),
                             new WireAnd(), new WireAnd(),
                             new WireAnd(), new WireAnd()};

  /**
   * The Cia 1 I/O ports
   */
  public C64Cia1IO cia;

  /**
   * Monitor where attend synchronization
   */
  public Monitor monitor=new Monitor("CIA 1");

  /**
   * Construct the C64 matrix keyboard.
   *
   * @param cia the Cia 1 I/O ports
   */
  public C64Keyboard(C64Cia1IO cia){
    super(c64Matrix);
    this.cia=cia;
  }

  /**
   * Set the I/O from/to Cia
   *
   * @param cia the Cia 1 I/O ports
   */
  public void setIO(C64Cia1IO cia){
    this.cia=cia;
  }

  /**
   * Update the state of output for Cia 1.
   * This is done by gives the key pressed in the columns that have a ground
   * signal to the output rows.
   *
   * Note that not all the input and output lines are wire and of two lines.
   */
  public void updateState() {
    int a,b;                               // row/col index

    int[] tmp={1, 1, 1, 1, 1, 1, 1, 1};    // matrix result

    for (b=0; b<8; b++) {
      if (colLines[b].out==0)              // is this line selected ?
        for (a=0; a<8; a++) {
          tmp[a]&=keysMatrix[a][b];        // remember key pressed
        }
    }

    for (a=0; a<8; a++) {                  // send the value of output lines
      rowLines[a].line1(tmp[a]);
    }
    cia.portB.setP0(rowLines[0].out);
    cia.portB.setP1(rowLines[1].out);
    cia.portB.setP2(rowLines[2].out);
    cia.portB.setP3(rowLines[3].out);
    cia.portB.setP4(rowLines[4].out);
    cia.portB.setP5(rowLines[5].out);
    cia.portB.setP6(rowLines[6].out);
    cia.portB.setP7(rowLines[7].out);
  }

  /**
   * Attend input changes and then calculate output
   */
  public void run(){
    while (cia==null)
      yield();

    while (true) {
      monitor.opWait();                       // attend cia or joystick event
      updateState();                          // update state of output
    }
  }
}