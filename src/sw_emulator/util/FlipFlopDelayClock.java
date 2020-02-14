/*
 * @(#)FlipFlopDelayClock.java 2020/02/11
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
 *  02111-1307  USA.*
 *
 */

package sw_emulator.util;

/**
 * A Clocked Delay FlipFlop with <code>set</code>, <code>reset</code> and <code>clock</code>
 * operations.
 *
 * The output state of the flip flop is taken by the input value (set or reset) when there is 
 * the clock operation
 * 
 * S/R ->[  ]->out
 *        ^
 *        |
 *      Clock
 * 
 * @author Ice
 * @version 1.00 06/01/2006
 */
public class FlipFlopDelayClock {
  
  /**
   * The input state of the flip/flop
   */
  private boolean inState=false;
  
  /**
   * The output state of the flip/flop
   */
  private boolean outState=false;  
  
  /**
   * Set the state of the flip/flop
   * 
   * @param state set (true) or reset (false) state
   */
  public void set(boolean state) {
    inState=state;
  }  
  
 /**
   * Set the state of the flip/flop
   */
  public void set() {
    inState=true;
  }

  /**
   * Reset the state of the flip/flop
   */
  public void reset() {
    inState=false;
  }
  
  /**
   * Clock operation
   */
  public void clock() {
    outState=inState;
  }
  
  /**
   * Determine if the flip/flop is set
   *
   * @return true if the flip/flop is set
   */
  public boolean isSet() {
    return outState;
  }

  /**
   * Determine if the flip/flop is reset
   *
   * @return true if the flip/flop is reset
   */
  public boolean isReset() {
    return !outState;
  }   
  
  /**
   * Get the actual state
   * 
   * @return the actual state
   */
  public boolean getState() {
    return outState;  
  }
  
  /**
   * Get a 0/1 representation of the output state
   *
   * @return a integer representation of output value
   */
  public int toInt() {
    if (outState) return 1;
    else return 0;
  }
}
