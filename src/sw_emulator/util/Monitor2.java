/**
 * @(#)Monitor.java 1999/09/19
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

package sw_emulator.util;

import java.lang.InterruptedException;

/**
 * Implement a Monitor for sinchronizing threads
 * The available operation to the monitor are <code>opSignal</code> and
 * <code>opWait</code>. 
 * 
 * An internal counter is used for know when all the
 * given threads that use this monitor have finish to make their body.
 * This is needed as the caller of <code>opSignal</code> has to know
 * when the other has finish before going away. 
 * 
 * In the real system,
 * this is not needed as the operations terminate in the clock period 
 * of time. <code>opNotify</code> is to use for notify the thread that
 * call <code>opSignal</code> that this thread will do a <code>opWait</code>
 * in his body. Be carefull: if one thred use <code>opNotify</code> and will
 * not do an <code>opWait</code>, a deadlook will occurs.
 *
 * @author Ice
 * @version 1.00 19/09/1999
 */
public class Monitor2 extends Monitor{

  /** Contains the name of the monitor (used as debug info) */
  protected String name;
  
  /** The actual threads counter */
  protected int counter;
  
  /** The max threads counter value to use */
  protected int maxCounter=0;
  
  final Object monitorExt=new Object();
  final Object monitorInt=new Object();

  /**
   * Build a named monitor
   *
   * @param name the monitor debug name
   */
  public Monitor2(String name) {
    this.name=name;
  }
  
  
  
  /**
   * Notify that this thread will do an <code>opWait</code> to this monitor
   */
  public void opNotify() {
    //System.out.println("NOTIFY: "+name+" "+counter+" "+maxCounter);  
    synchronized(monitorExt) {
      maxCounter++;
    }  
  }

  /**
   * Subspend the thread until a <code>opSignal</code> operation is made
   */
  public void opWait() {
    //System.out.println("WAIT: "+name+" "+counter+" "+maxCounter);
    synchronized(monitorExt) {
      try {
        if (counter>0) counter--;
        monitorExt.wait();
      } catch (InterruptedException e) {
          System.err.println("Thread error for monitor "+name+": "+e);
        }
    }
  }

  /**
   * Resume all the thread that was subspended by <code>opWait</code> operation
   */
  public void opSignal() {
    //System.out.println("SIGNAL: "+name+" "+counter+" "+maxCounter);
    synchronized(monitorExt) { 
      counter=maxCounter;
      monitorExt.notifyAll();
    }
  }
  
  /**
   * Return true if all threads have finish
   * 
   * @return true if all threads have finish
   */
  public  boolean isFinish() {
    ///System.out.println("ISFINISH: "+name+" "+counter+" "+maxCounter);      
    return (counter==0);
  }  
  
  
  /**
   * Subspend the thread until a <code>opSignal</code> operation is made
   */
  public void opWait2() {
    //System.out.println("WAIT: "+name+" "+counter+" "+maxCounter);
    synchronized(monitorInt) {
      try {
        monitorInt.wait(1);
      } catch (InterruptedException e) {
          System.err.println("Thread error for monitor "+name+": "+e);
        }
    }
  }

  /**
   * Resume all the thread that was subspended by <code>opWait</code> operation
   */
  public void opSignal2() {
    //System.out.println("SIGNAL: "+name+" "+counter+" "+maxCounter);
    synchronized(monitorInt) { 
      monitorInt.notifyAll();
    }
  }
  

  /**
   * Return the name of the monitor
   *
   * @return the name of the monitor
   */
  public String getName() {
    return name;
  }
}