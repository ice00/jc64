/**
 * @(#)M6526.java 1999/10/24
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

import sw_emulator.hardware.powered;
import sw_emulator.hardware.signaller;
import sw_emulator.hardware.bus.readableBus;
import sw_emulator.hardware.bus.writeableBus;
import sw_emulator.hardware.io.M6526IO;
import sw_emulator.math.Unsigned;
import sw_emulator.util.Monitor;
import sw_emulator.util.FlipFlop;
import sw_emulator.util.FlipFlopClock;
import sw_emulator.util.Counter;
import sw_emulator.util.FlipFlopDelayClock;

/**
 * MOS 6526 cia chip implementation
 *
 * TOD emulation are based onto documentation of Commodore Book
 * The other is based onto "A software Model of the CIA6526" by Wolfgang Lorenz
 *
 * @author Ice
 * @version 1.00 23/10/1999
 */
public class M6526 extends Thread implements powered, signaller,
                                             readableBus, writeableBus{
  public static final int NTSC=0;
  public static final int PAL=1;
  
  /** The IRQ signal to use */
  protected int sig_irq=signaller.S_IRQ;


  /** The state of power */
  protected boolean power=false;

  /** The actual input FLAG signal */
  protected int inputFLAG=1;
  
  /** the actual input CNT signal */
  protected int inputCNT=0;

  /** The I/O of M6526 */
  public M6526IO io;
  
  /** The clock monitor */
  protected Monitor monitor;
  
  
  // states are n bits for better managing
  
  /** State of CRA0 field (bit 0) START */
  public int CRA0;
  
  /** State of CRA1 field (bit 1) PBON */
  public int CRA1;
  
  /** State of CRA2 field (bit 2) OUTMODE */
  public int CRA2;
  
  /** State of CRA3 field (bit 3) RUNMODE */
  public int CRA3;
  
  /** State of CRA4 field (bit 4) LOAD */
  public int CRA4;
  
  /** State of CRA5 field (bit 5) INMODE */
  public int CRA5;
  
  /** State of CRA6 field (bit 6) SPMODE */
  public int CRA6;
  
  /** State of CRA7 field (bit 7) TODIN */
  public int CRA7;
  
  
    /** State of CRB0 field (bit 0) START */
  public int CRB0;
  
  /** State of CRB1 field (bit 1) PBON */
  public int CRB1;
  
  /** State of CRB2 field (bit 2) OUTMODE */
  public int CRB2;
  
  /** State of CRB3 field (bit 3) RUNMODE */
  public int CRB3;
  
  /** State of CRB4 field (bit 4) LOAD */
  public int CRB4;
  
  /** State of CRB56 filed (bits 5-6) INMODE*/
  public int CRB56;
  
  /** State of CRB7 field (bit 0) TOD/ALLARM */
  public int CRB7;
  
  
  // interrupt control register
  
  /** Control: Interrupt register */
  public int C_IR=0;
  
  /** Control: Flag */
  public int C_FLG=0;

  /** Control: Serial Port */
  public int C_SP=0;
  
  /** Control: Alarm */
  public int C_ALRM=0;
  
  /** Control Timer B */
  public int C_TB=0;
  
  /** Control: Timer A */
  public int C_TA=0;
  
  
  /** Mask: Interrupt register */
  public int M_SC=0;
  
  /** Mask: Flag */
  public int M_FLG=0;

  /** Mask: Serial Port */
  public int M_SP=0;
  
  /** Mask: Alarm */
  public int M_ALRM=0;
  
  /** Mask Timer B */
  public int M_TB=0;
  
  /** Mask: Timer A */
  public int M_TA=0;  
  
  
  /** Interrupt1 flip flop */
  protected FlipFlopClock fInterrupt1=new FlipFlopClock(); 
  
  
  /** CountA0 flip flop */
  protected FlipFlop fCountA0=new FlipFlop();

  /** CountA1 delay flip flop */
  protected FlipFlopDelayClock fCountA1=new FlipFlopDelayClock();
  
  /** LoadA0 flip flop */
  protected FlipFlop fLoadA0=new FlipFlop();
  
  /** LoadA1 delay flip flop */
  protected FlipFlopDelayClock fLoadA1=new FlipFlopDelayClock();
  
  /** OneShotA0 flip flop */
  protected FlipFlopDelayClock fOneShotA0=new FlipFlopDelayClock();  
  
  /** CountA2 flip flop */
  protected FlipFlopClock fCountA2=new FlipFlopClock();  
  
  /** CountA3 delay flip flop */
  protected FlipFlopDelayClock fCountA3=new FlipFlopDelayClock(); 
  
  /** TimerA flip flop */
  protected FlipFlop fTimerA=new FlipFlop();  
  
  /** Timer A */
  protected Counter timerA=new Counter();
  
    
  
  /** CountB0 flip flop */
  protected FlipFlop fCountB0=new FlipFlop();

  /** CountB1 delay flip flop */
  protected FlipFlopDelayClock fCountB1=new FlipFlopDelayClock(); 
  
  /** LoadB0 flip flop */
  protected FlipFlop fLoadB0=new FlipFlop();
  
  /** LoadB1 delay flip flop */
  protected FlipFlopDelayClock fLoadB1=new FlipFlopDelayClock();
    
  /** OneShotB0 delay flip flop */
  protected FlipFlopDelayClock fOneShotB0=new FlipFlopDelayClock();    
  
  /** CountB2 flip flop */
  protected FlipFlopClock fCountB2=new FlipFlopClock();  
  
  /** CountB3 delay flip flop */
  protected FlipFlopDelayClock fCountB3=new FlipFlopDelayClock();   

  /** TimerB flip flop */
  protected FlipFlop fTimerB=new FlipFlop();     
  
  /** Timer B */
  protected Counter timerB=new Counter();
    
  
  
  /** Input Timer A value */
  protected int inTimerA=0;
  
  /** Input Timer B value */
  protected int inTimerB=0;
  
  
  /** Output Timer A value */
  protected int outTimerA=0;
  
  /** Output Timer B value */
  protected int outTimerB=0;  
    
  
  /// TOD variables
  
  /** Number of cycle for reacing a TOD signal of 1/10th sec. */
  protected int MAX_TOD=5; // suppose PAL  
  
  /** The actual input TOD signal */
  protected int inputTOD=0;  
  
  /**
   * The counter of TOD signal. It is decremented, so we may change the PAL to
   * NTSC pin without emulation problem.
   */
  private int countTOD;
  
  /** True if TOD is being writed */
  private boolean isWritingTod=false;
  
  /** True if tod is to increment */
  private boolean isTodToIncrement=false;
  
  
  /** Allarm value of Tod 10ths */
  public int allarmTodThs;
  
  /** Allarm value of Tod second */
  public int allarmTodSec;
  
  /** Allarm value of Tod minute */
  public int allarmTodMin;
  
  /** Allarm value of Tod hour */
  public int allarmTodHr;
  
  
  /** Latch value of Tod 10ths */
  public int latchTodThs;
  
  /** Latch value of Tod second */
  public int latchTodSec;
  
  /** Latch value of Tod minute */
  public int latchTodMin;
  
  /** Latch value of Tod hour */
  public int latchTodHr; 
  
  
  /** Timer value of Tod 10ths */
  public int timerTodThs;
  
  /** Timer value of Tod second */
  public int timerTodSec;
  
  /** Timer value of Tod minute */
  public int timerTodMin;
  
  /** Timer value of Tod hour */
  public int timerTodHr;
  
  /**
   * Construct the cia chip
   *
   * @param type the type of cia (NTSC, PAL)
   * @param monitor the clock monitor at 1Mhz
   * @param io the io of the chip
   */
  public M6526(int type, Monitor monitor, M6526IO io) {
    if (type==NTSC) MAX_TOD=6;
    this.monitor=monitor;
    this.io=io;
    setName("CIA1");               // use this name for the thread
    start();
  }
  
  /**
   * Construct the cia chip with a special signal for the irq
   *
   * @param type the type of cia (NTSC, PAL)
   * @param monitor the clock monitor at 1Mhz
   * @param io the io of the chip
   * @param sig_irq signal to use for irq
   */
  public M6526(int type, Monitor monitor, M6526IO io, int sig_irq) {        
    this.sig_irq=sig_irq;
    if (type==NTSC) MAX_TOD=6;
    this.monitor=monitor;
    this.io=io;
    setName("CIA2");               // use this name for the thread    
    start();    
  }

  /**
   * Write a byte to the bus at specific address location.
   *
   * @param addr the address location
   * @param value the byte value
   */
  @Override
  public void write(int addr, byte value) {
    addr&=0x0F;
    switch (addr) {
      case 0x00:                                     // Port A data
      case 0x01:                                     // Port B data
      case 0x02:                                     // Port A direction
      case 0x03:                                     // Port B direction
        io.writeToPort(addr, value);
        break;
      case 0x04:                                     // TA low
        timerA.setLow(value);
        break;
      case 0x05:                                     // TA high
        timerA.setHigh(value);     
        break;        
      case 0x06:                                     // TB low
        timerB.setLow(value);       
        break;
      case 0x07:                                     // TB high
        timerB.setHigh(value);         
        break;                
      case 0x08:                                     // TOD 10TH
        if (CRB7==0) {                               // TOD
          timerTodThs=(value & 0x0F);
          isWritingTod=false;                        // tod is not being writing
        } else {                                     // ALLARM 
            allarmTodThs=(value & 0x0F);
          }
        break;
      case 0x09:                                     // TOD sec
        if (CRB7==0) {                               // TOD
          timerTodSec=(value & 0x7F);
        } else {                                     // ALLARM 
            allarmTodSec=(value & 0x7F);
          }        
        break;
      case 0x0A:                                     // TOD min
        if (CRB7==0) {                               // TOD
          timerTodMin=(value & 0x0F);
        } else {                                     // ALLARM 
            allarmTodMin=(value & 0x0F);
          }        
        break;
      case 0x0B:                                     // TOD hr
        if (CRB7==0) {                               // TOD
          timerTodHr=(value & 0x9F);
          isWritingTod=true;                         // tod is being writing
        } else {                                     // ALLARM 
            allarmTodHr=(value & 0x9F);
          }        
        break;
      case 0x0D:                                     // ICR
        M_SC=(value>>7)& 0x01; 
        if ((value & 0x01)==1) M_TA=M_SC;
        if (((value>>=1) & 0x01)==1) M_TB=M_SC;
        if (((value>>=1) & 0x01)==1) M_ALRM=M_SC;
        if (((value>>=1) & 0x01)==1) M_SP=M_SC;
        if (((value>>=1) & 0x01)==1) M_FLG=M_SC;
        break;
      case 0x0E:                                     // CRA
        CRA0=value & 0x01;
        CRA1=(value>>=1)& 0x01;
        CRA2=(value>>=1)& 0x01;
        CRA3=(value>>=1)& 0x01;
        CRA4=(value>>=1)& 0x01;        
        CRA5=(value>>=1)& 0x01;               
        CRA6=(value>>=1)& 0x01;                
        CRA7=(value>>=1)& 0x01;
                                                     // START
        fTimerA.set();
        // as reset in this flip flop can occurs in the phi phase,
        // here we must update CountA2  
        fCountA2.set(CRA0==1 & inTimerA==1);       

        if (CRA4==1) fLoadA0.set();                  // force loadA0 
        fOneShotA0.set(CRA3==1);                     // RUNMODE        
        break;
      case 0x0F:                                     // CRB       
        CRB0=value & 0x01; 
        CRB1=(value>>=1)& 0x01;
        CRB2=(value>>=1)& 0x01;
        CRB3=(value>>=1)& 0x01;
        CRB4=(value>>=1)& 0x01;
        CRB56=(value>>=1)& 0x03;        
        CRB7=(value>>=2)& 0x01;                      // TOD/ALLARM    
                                                     // START
          fTimerB.set();
          // as reset in this flip flop can occurs in the phi phase,
          // here we must update CountA2    
          fCountB2.set(CRB0==1 && inTimerB==1);
          
        if (CRB4==1) fLoadB0.set();                  // force loadA0 
        fOneShotB0.set(CRB3==1);                    // RUNMODE
        
        break;
    }
  }

  /**
   * Read a byte from the bus at specific address location.
   *
   * @param addr the address location
   * @return the readed byte
   */
  @Override
  public byte read(int addr) {
    addr&=0x0F;
    switch (addr) {
      case 0x00:                                     // Port A data
      case 0x01:                                     // Port B data
      case 0x02:                                     // Port A direction
      case 0x03:                                     // Port B direction
        return (byte)io.readFromPort(addr);
      case 0x04:                                     // TA low      
        return (byte)timerA.getLow();
      case 0x05:                                     // TA high         
        return (byte)timerA.getHigh();  
      case 0x06:                                     // TB low       
        return (byte)timerB.getLow();       
      case 0x07:                                     // TB high           
        return (byte)timerB.getHigh();       
      case 0x08:                                     // TOD 10TH 
        return (byte)latchTodThs;
      case 0x09:                                     // TOD sec 
        return (byte)latchTodSec;
      case 0x0A:                                     // TOD min      
        return (byte)latchTodMin;
      case 0x0B:                                     // TOD hr
        latchTodHr=timerTodHr;      // actual values are latched
        latchTodMin=timerTodMin;
        latchTodSec=timerTodSec;
        latchTodThs=timerTodThs;
        return (byte)latchTodHr;
      case 0x0D:                                     // ICR        
        int res=C_IR<<7+
                C_FLG<<4+
                C_SP<<3+
                C_ALRM<<2+
                C_TB<<1+
                C_TA;
        // clear according to figure 5
        io.notifySignal(sig_irq, 1);  // disable interrupt
                                      // the flip flop reset signal is below  
        C_IR=0;
        C_FLG=0;
        C_SP=0;
        C_ALRM=0;
        C_TB=0;
        C_TA=0;               
        return (byte)res;
      case 0x0E:                                     // CRA state 
        return (byte)(CRA0+
                     (CRA1<<1)+
                     (CRA2<<2)+
                     (CRA3<<3)+
                     (CRA4<<4)+
                     (CRA5<<5)+
                     (CRA6<<6)+
                     (CRA7<<7));
      case 0x0F:                                     // CRB state 
        return (byte)(CRB0+
                     (CRB1<<1)+
                     (CRB2<<2)+
                     (CRB3<<3)+
                     (CRB4<<4)+
                     (CRB56<<5)+                     
                     (CRB7<<7));        
     }
    
    fInterrupt1.reset(addr==0x0D); 
    return 0;
  }

  /**
   * Notify a signal to the chip.
   * Note: the signal that are level significative (like FLAG and TOD) may
   * notify only when the level changes, else there will be timing error.
   * E.g. a sequenxe of 1___0___1___0___1___0... is good, while a sequence of
   * 1_1_0_0_1_1_0_0_1_1_0_0... is not good, even if the translaction in the
   * same period are the same. This is done for speed up pourpuse. If we have
   * that kind of sequence, we must use: <BR>
   * <pre>
   *  case xx:
   *    if (inputxx!=value) {
   *    // same operactions as now
   *    }
   * </pre>
   *
   * @param type the type of signal
   * @param value the value of the signal (0/1)
   */
  @Override
  public void notifySignal(int type, int value) {
    switch (type) {
      case S_TOD:
        inputTOD=value;                            // store in buffer the value
        if (value==1) {                            // is there a level 0 to 1?
          if (countTOD--==0) {
            countTOD=MAX_TOD;                      // repristinate the counter
            isTodToIncrement=true;                 // tod is to increment
          }
        }
        break;
      case S_FLAG:
        inputFLAG=value;                           // store in buffer the value
        if (value==0) {                            // is there a level 1 to 0?

        }
        break;
      case S_CNT:
        inputCNT=value;
        if (value==1) {                            // is there a level 0 to 1?
          fCountA0.set();                          // set the CountA0 flipflop
          fCountB0.set();                          // set the CountB0 flipflop
        }
        break;
    }
  }

  /**
   * Set up the connection of IO with the external.
   *
   * @param io the external connection
   */
  public void setIO(M6526IO io) {
    this.io=io;
  }
  
  /**
   * Execute the body of a cia clock (syncroned clocked part)
   */
  protected void bodySync() {
    // tod testing
    if (isTodToIncrement) {      
      isTodToIncrement=false;
      
      // increment Tod 10 Ths
      timerTodThs++;
      if (timerTodThs==0x0A) {
        timerTodThs=0;
        
        //increment seconds        
        timerTodSec++;
        if ((timerTodSec & 0x0F)==0x0A) {
          timerTodSec+=6;
          if (timerTodSec==0x60) {
            timerTodSec=0;
            
            // increment minutes
            timerTodMin++;
            if ((timerTodMin & 0x0F)==0x0A) {
              timerTodMin+=6;
              if (timerTodMin==0x60) {
                timerTodMin=0;
                
                // increment hours
                timerTodHr++;
                if ((timerTodHr & 0x1F)==0x0A) {
                  timerTodHr+=6;
                } else {
                    // there is an AM/PM change
                    if ((timerTodHr & 0x1F)==0x13) {
                      timerTodHr &=0x8F;
                      timerTodHr ^=0x80;
                    }                  
                  }
              }
            }
          }        
        }
      }
      // see if now there is an allarm to activate
      if (allarmTodThs==timerTodThs && 
          allarmTodSec==timerTodSec &&
          allarmTodMin==timerTodMin &&
          allarmTodHr==timerTodHr) {
        // fire an irq
        
      }
    }
    
    // emulate all the state changed by clock ih flip flop
    fCountA0.reset();
    fCountA1.clock();
    fLoadA0.reset();
    fLoadA1.clock();
    fOneShotA0.clock();
    fCountA3.clock();
    fCountA2.clock();
    timerA.clock();
    
    fCountB0.reset();
    fCountB1.clock();
    fLoadB0.reset();
    fLoadB1.clock();
    fOneShotB0.clock();
    fCountB3.clock();    ///
    fCountB2.clock();    ///
    timerB.clock();
    
    fInterrupt1.clock();
    // check for interrupt
    if (fInterrupt1.isSet()) {
      io.notifySignal(sig_irq, 0);   // notify an irq
    }
  }
  
  /**
   * Execute the body of async logic (not clocked) of cia
   * Emulate all the login and connections between the flip flop
   */
  protected void bodyAsync() {
    boolean temp;  
      
    /// these are to be called before the clock for having the force load
    fLoadA1.set(fLoadA0.isSet());        
    fLoadB1.set(fLoadB0.isSet());
    
    // emulate as in figure 3    
    timerA.setDec(fCountA3.isSet());    
    timerB.setDec(fCountB3.isSet());

    // emulate as in figure 3
    fCountA3.set(fCountA2.isSet());  
    fCountB3.set(fCountB2.isSet());
    
    // calculate outTimerA output value
    // emulate as in figure 3    
    outTimerA=((fCountA2.isSet() && timerA.isZero()) ? 1:0);   
    outTimerB=((fCountB2.isSet() && timerB.isZero()) ? 1:0);
  
    temp=(outTimerA==1) || fLoadA1.isSet();
    timerA.load(temp);
    fCountA2.reset(temp);  

    temp=(outTimerB==1) || fLoadB1.isSet();
    timerB.load(temp);
    fCountB2.reset(temp);
        
    // emulate as in figure 1
    fCountA1.set(fCountA0.isSet());
    fCountB1.set(fCountB0.isSet());      
    
    // calculate inTimerA input value
    // emulate as in figure 1    
    if (CRA5==1) inTimerA=fCountA1.toInt();          // cnt gives CountA1
    else         inTimerA=1;                         // phi2 alway gives 1
    
    
    // calculate inTimerB input value (outTimerA value must be already done)
    // emulate as in figure 2    
    switch (CRB56) {
      case 0x00:
        inTimerB=1;                                  // phi2 alway gives 1
        break;
      case 0x01:
        inTimerB=fCountB1.toInt();                   // cnt gives CountB1
        break;
      case 0x02:
        inTimerB=outTimerA;                          // same output as A
        break;
      case 0x03:
        inTimerB=outTimerA & inputCNT;               // and of cnt/intput A
        break;        
    }    
    
    // emulate as in figure 3    
    if (((CRA3==1) || fOneShotA0.isSet()) && outTimerA==1) fTimerA.reset();    
    fCountA2.set((fTimerA.isSet() && inTimerA==1));
    
    if (((CRB3==1) || fOneShotB0.isSet()) && outTimerB==1) fTimerB.reset();    
    fCountB2.set((fTimerB.isSet() && inTimerB==1));
    
    // emulate as in figure 5
    fInterrupt1.set(((outTimerA==1 && M_TA==1) || (outTimerB==1 && M_TB==1)));
  }
  
  /**
   * Performs the body of cia emulation.
   * The emulation don't start if the io port is not set up with a correct
   * handler.
   */
  public void run() {
    monitor.opNotify();                         // notify that we will use it      
      
    while(io==null) {
      yield();
    }  
    
    while(true) {   
      while (!power) {
        yield();                              // give mutex to other threads
      }      
      
      bodyAsync();                            // execute tha async part of body
      bodySync();                             // execute the cia clock boby      
      monitor.opWait();                       // attend clock signal 
    }  
  }
    
  /**
   * Power on the electronic component
   */
  public void powerOn() {
      power=true;   // power is on
  }

  /**
   * Power off the electronic component
   */
  public void powerOff() {
      power=false;   // power is off
  }
}