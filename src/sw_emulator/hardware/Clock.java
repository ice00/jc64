/**
 * @(#)Clock.java 1999/09/19
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

import sw_emulator.util.Monitor;


/**
 * Creates a clock circuits.
 * The clock period is at 8Mhz, and the clock can be not in real-time but
 * slow or accelerate.
 * The thread that wants to be clocked, must use the monitor of this class.
 *
 * @author Ice
 * @version 1.00 19/09/1999
 */
public class Clock extends Thread {

  /** The monitor used to synchronized at 8Mhz */
  public Monitor monitor=new Monitor("Clock at 8Mhz");
  
  public static final int PAL=1;
  public static final int NTSC=2;

  /** The type of clock (for PAL or NTSC) */
  private int type=PAL;

  /** Coefficient for real time */
  private double realTime=1;

  /** True if clock is started */
  private boolean started=false;
  
  /** Cycle before calling a tod */
  private int cycleTod=0;
  
  /** devices that can receive TOD signal */
  private signaller[] devices=null;

  /**
   * Create a clock of 8Mhz for PAL or NTSC
   *
   * @param type the type (PAL or NTSC)
   */
  public Clock(int type) {
    setType(type);
    setPriority(MIN_PRIORITY);
    setName("CLOCK");              // new name for this thread
    start();
  }

  /**
   * Set the actual type of clock (default is PAL)
   * 
   * @param type the type to set
   */
  public void setType(int type) {
    if ((type==PAL) || (type==NTSC))
      this.type=type;
    else this.type=PAL;   
  }

  /**
   * Select the real time of the clock.
   * A 1 means real time, <1 means accelerate time, >1 means slow time
   *
   * @param realTime the coefficient for real time
   */
  public void setRealTime(double realTime) {
   if (realTime<0) this.realTime=1;
   else this.realTime=realTime;
  }

  /**
   * Register external devices that can receive TOD signal
   * 
   * @param devices the device to add
   */
  public void registerTod(signaller[] devices) {
     this.devices=devices;
  }
  
  /**
   * Starts the clock tick emulation
   */
  public synchronized void startClock() {
    started=true;
  }

  /**
   * Stops the clock tick emulation
   */
  public synchronized void stopClock() {
    started=false;
  }

  /**
   * Notify a clock tick in the monitor
   */
  public void run() {
    ///long start=0;
    while (true) {
      while (started==false) {       // attend a start command
        this.yield();
      }

      
      //if (type==NTSC) wait((int)(12*realTime), (int)(222*realTime));
      //else            wait((int)(12*realTime), (int)(690*realTime));
       
      // attend that the connected circuits have finish
 //     while (!monitor.isFinish()) {
 //       yield();
 //       //monitor.opWait2();
 //     }
      
      
      monitor.opSignal();
      
      // test for generating a tod high signal
      if (--cycleTod<=0) {
        cycleTod=160000;        /// to fix for pal/ntsc
        if (devices!=null) { 
          for (int i=0; i<devices.length; i++) {
            devices[i].notifySignal(signaller.S_TOD, 1); // 0 to 1
          }
        }
        
       /// start = System.nanoTime();
      }
      
      // test for generating a tod low signal
      if (cycleTod==80000) {      /// to fix for pal/ntsc
        if (devices!=null) { 
          for (int i=0; i<devices.length; i++) {
            devices[i].notifySignal(signaller.S_TOD, 0); // 1 to 0
          }
        }   
        
       /// long finish = System.nanoTime();
       /// long timeElapsed = finish - start;
       /// System.err.println(timeElapsed/1000000);
      }     
    }
  }
}