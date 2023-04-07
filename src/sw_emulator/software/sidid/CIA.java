/**
 * @(#)CIA.java 2023/03/19
 *
 * ICE Team free software group
 *
 * This file is part of C64 Java Software Emulator.
 * See README for copyright notice.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 */
package sw_emulator.software.sidid; 


/**
 * Emulate the CIA of cRSID original by Hermit
 * 
 * @author stefano_tognon
 */
public class CIA {  
  /** reference to the containing C64 */
  C64 c64;
  
  /** Old or new CIA? (have 1 cycle difference in cases) */
  int chipModel;  
  
  /** CIA-baseaddress location in C64-memory (IO) */
  int baseAddress; 
  
  /** CIA-baseaddress location in host's memory for writing */
  int[] basePtrWR;   
  
  /** CIA-baseaddress location in host's memory for reading  */
  int[] basePtrRD; 
  
  public static final int PORTA=0;
  public static final int PORTB=1;
  public static final int DDRA=2;
  public static final int DDRB=3;
  
  //Write:Set Timer-latch, Read: read Timer
  public static final int TIMERAL=4;
  public static final int TIMERAH=5;
  public static final int TIMERBL=6;
  public static final int TIMERBH=7; 
  
  public static final int TOD_TENTHSECONDS=8;
  public static final int TOD_SECONDS=9;
  public static final int TOD_MINUTES=0xA;
  public static final int TOD_HOURS=0xB;
  public static final int SERIAL_DATA=0xC; 
  public static final int INTERRUPTS=0xD;
  public static final int CONTROLA=0xE;
  public static final int CONTROLB=0xF;  
  
  //(Read or Write operation determines which one:)
  public static final int INTERRUPT_HAPPENED=0x80;
  public static final int SET_OR_CLEAR_FLAGS=0x80;
   
  //flags/masks of interrupt-sources
  public static final int FLAGn=0x10;
  public static final int SERIALPORT=0x08;
  public static final int ALARM=0x04;
  public static final int TIMERB=0x02;
  public static final int TIMERA=0x01; 
  
  public static final int ENABLE_TIMERA=0x01;
  public static final int PORTB6_TIMERA=0x02;
  public static final int TOGGLED_PORTB6=0x04;
  public static final int ONESHOT_TIMERA=0x08;
  public static final int FORCELOADA_STROBE=0x10;
  public static final int TIMERA_FROM_CNT=0x20;
  public static final int SERIALPORT_IS_OUTPUT=0x40;
  public static final int TIMEOFDAY_50Hz=0x80;
  
  public static final int ENABLE_TIMERB=0x01;
  public static final int PORTB7_TIMERB=0x02;
  public static final int TOGGLED_PORTB7=0x04;
  public static final int ONESHOT_TIMERB=0x08;
  public static final int FORCELOADB_STROBE=0x10;
  public static final int TIMERB_FROM_CPUCLK=0x00;
  public static final int TIMERB_FROM_CNT=0x20;
  public static final int TIMERB_FROM_TIMERA=0x40;
  public static final int TIMERB_FROM_TIMERA_AND_CNT = 0x60;
  public static final int TIMEOFDAY_WRITE_SETS_ALARM = 0x80;
  
  /**
   * Construct the CIA
   * 
   * @param c64 the C64 reference
   * @param baseAddress the base address in memory
   */
  public CIA(C64 c64, int baseAddress) {
    this.c64 = c64;
    this.baseAddress = baseAddress;
    chipModel = 0;    
    basePtrWR = c64.ioBankWR;
    basePtrRD = c64.ioBankRD;
    
    initChip();
}
  
  /**
   * Init the CIA chip
   */
  public void initChip() {
    for (int i=baseAddress; i<baseAddress+0x10; ++i) {
      basePtrWR[i] = basePtrRD[i] = 0x00;  
    }
  }
  
  /**
   * Emulate the CIA 
   * 
   * @param cycles the processor cycles
   * @return if IRQ happened
   */
  public byte emulateCIA (byte cycles) {
    int tmp;  
      
    //TimerA
    if ((basePtrWR[CONTROLA+baseAddress] & FORCELOADA_STROBE)!=0) { //force latch into counter (strobe-input)
      basePtrRD[TIMERAH+baseAddress] = basePtrWR[TIMERAH+baseAddress]; 
      basePtrRD[TIMERAL+baseAddress] = basePtrWR[TIMERAL+baseAddress];
    }
    else if ( (basePtrWR[CONTROLA+baseAddress] & (ENABLE_TIMERA|TIMERA_FROM_CNT)) == ENABLE_TIMERA ) { //Enabled, counts Phi2
           tmp = ( (basePtrRD[TIMERAH+baseAddress]<<8) + basePtrRD[TIMERAL+baseAddress] ) - cycles; //count timer
           if (tmp <= 0) { //Timer counted down
             tmp += (basePtrWR[TIMERAH+baseAddress]<<8) + basePtrWR[TIMERAL+baseAddress]; //reload timer
             if ((basePtrWR[CONTROLA+baseAddress] & ONESHOT_TIMERA)!=0) {  //disable if one-shot
                 basePtrWR[CONTROLA+baseAddress] &= ~ENABLE_TIMERA;  
             } 
             basePtrRD[INTERRUPTS+baseAddress] |= TIMERA;
             if ((basePtrWR[INTERRUPTS+baseAddress] & TIMERA)!=0) { //generate interrupt if mask allows
               basePtrRD[INTERRUPTS+baseAddress] |= INTERRUPT_HAPPENED;
             }
           }
           basePtrRD[TIMERAH+baseAddress] =  (tmp >> 8); 
           basePtrRD[TIMERAL+baseAddress] =  (tmp & 0xFF);
    }
    basePtrWR[CONTROLA+baseAddress] &= ~FORCELOADA_STROBE; //strobe is edge-sensitive
    basePtrRD[CONTROLA+baseAddress] = basePtrWR[CONTROLA+baseAddress]; //control-registers are readable

    //TimerB
    if ((basePtrWR[CONTROLB+baseAddress] & FORCELOADB_STROBE)!=0) { //force latch into counter (strobe-input)
      basePtrRD[TIMERBH+baseAddress] = basePtrWR[TIMERBH+baseAddress];
      basePtrRD[TIMERBL+baseAddress] = basePtrWR[TIMERBL+baseAddress];
    } //what about clocking TimerB by TimerA? (maybe not used in any music)
    else if ( (basePtrWR[CONTROLB+baseAddress] & (ENABLE_TIMERB|TIMERB_FROM_TIMERA)) == ENABLE_TIMERB ) { //Enabled, counts Phi2
           tmp = ( (basePtrRD[TIMERBH+baseAddress]<<8) + basePtrRD[TIMERBL+baseAddress] ) - cycles;//count timer
           if (tmp <= 0) { //Timer counted down
             tmp += (basePtrWR[TIMERBH+baseAddress]<<8) + basePtrWR[TIMERBL+baseAddress]; //reload timer
             if ((basePtrWR[CONTROLB+baseAddress] & ONESHOT_TIMERB)!=0) { //disable if one-shot
                 basePtrWR[CONTROLB+baseAddress] &= ~ENABLE_TIMERB;
             } 
             basePtrRD[INTERRUPTS+baseAddress] |= TIMERB;
             if ((basePtrWR[INTERRUPTS+baseAddress] & TIMERB)!=0) { //generate interrupt if mask allows
               basePtrRD[INTERRUPTS+baseAddress] |= INTERRUPT_HAPPENED;
             }
           }
           basePtrRD[TIMERBH+baseAddress] = (tmp >> 8); 
           basePtrRD[TIMERBL+baseAddress] = (tmp & 0xFF);
    }
    basePtrWR[CONTROLB+baseAddress] &= ~FORCELOADB_STROBE; //strobe is edge-sensitive
    basePtrRD[CONTROLB+baseAddress] = basePtrWR[CONTROLB+baseAddress]; //control-registers are readable

    return (byte)(basePtrRD[INTERRUPTS+baseAddress] & INTERRUPT_HAPPENED);
  }
  
  /**
   * Write CIA IRQ mask
   * 
   * @param value the mask value to use
   */
  public void writeCIAIRQmask (int value) {
    if ((value&0x80)!=0) basePtrWR[0xD+baseAddress] |= (value&0x1F);
    else basePtrWR[0xD+baseAddress] &= ~(value&0x1F);
  }

  /**
   * Acknowledge CIA IRQ
   */
  public void acknowledgeCIAIRQ () {
    //reading a CIA interrupt-register clears its read-part and IRQ-flag
    basePtrRD[0xD+baseAddress] = 0x00; 
  }
}
