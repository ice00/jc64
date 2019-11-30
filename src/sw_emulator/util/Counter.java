/*
 * @(#)@(#)Counter.java 2006/01/06
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
 */

package sw_emulator.util;

/**
 * Clocked counter with latch used by Cia
 *
 * @author Ice
 * @version 1.00 06/01/2006
 */
public class Counter {
  
  /** Actual counter value (16 bit) */
  protected int counter=0;
  
  /** Latch value (16 bit) */
  protected int latch=0xFFFF;
  
  /** Decrement state: true=timer is counting */
  protected boolean decState=false;
  
  /** True if counter is lo load from latch */
  protected boolean toLoad=false;
  
  /**
   * Set the low value of latch
   *
   * @param value the 8 bit value
   */ 
  public void setLow(int value) {
    latch=(latch & 0xFF00) | value;    
  }
  
  /**
   * Set the low value of latch
   *
   * @param value the 8 bit value
   */ 
  public void setHigh(int value) {
    latch=(latch & 0x00FF) | value<<8;    
    
    // if not decrementing, counter is loaded with latch
    if (!decState) counter=latch;
  }  
  
  /**
   * Get the low of counter
   *
   * @return the low of counter
   */
  public int getLow() {
    return counter & 0x00FF;
  }
  
  /**
   * Get the high of counter
   *
   * @return the low of counter
   */
  public int getHigh() {
    return (counter & 0xFF00)>>8;
  }  
  
  /**
   * Execute operations during a clock of counter
   */
  public void clock() {
    if (decState) {
      counter--;
      if (counter<0) counter=0xFFFF;  /// ?
    }
    
    if (toLoad) {
      counter=latch;
      toLoad=false;
    }
  }
  
  /**
   * Return if counter is 0
   *
   * @return true id counter is 0
   */
  public boolean isZero() {
    return counter==0;
  }
  
  /**
   * Set to load counter from latch
   */
  public void load() {
    toLoad=true;
  }
  
  /**
   * Set the state of decrement
   *
   * @param state the state to use for decrement
   */
  public void setDec(boolean state) {
    decState=state;
  }
}
