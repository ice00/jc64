/**
 * @(#)VIC.java 2023/03/19
 *
 * ICE Team free software group
 *
 * This file is part of JIIT64 Java Ice Team Tracker 64
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
 * Emulate the VIC of cRSID original by Hermit
 * 
 * @author ice00
 */
public class VIC {
  /** Reference to the containing C64 */
  C64 c64;         
  
  /** Timing differences between models */
  int chipModel;   
  
  /** VIC-baseaddress location in C64-memory (IO)*/
  int baseAddress; 
  
  /** VIC-baseaddress location in host's memory for writing */
  int[] basePtrWR;   
  
  /** VIC-baseaddress location in host's memory for reading */
  int[] basePtrRD;   
  
  int rasterLines;
  int rasterRowCycles;
  int rowCycleCnt;
  
  public static final int CONTROL = 0x11;
  public static final int RASTERROWL = 0x12;
  public static final int SPRITE_ENABLE=0x15;
  public static final int INTERRUPT = 0x19;
  public static final int INTERRUPT_ENABLE = 0x1A;
  
  public static final int RASTERROWMSB = 0x80;
  public static final int DISPLAY_ENABLE = 0x10;
  public static final int ROWS = 0x08;
  public static final int YSCROLL_MASK = 0x07;
  
  public static final int VIC_IRQ = 0x80;
  public static final int RASTERROW_MATCH_IRQ = 0x01;
  
  /**
   * Construct the VIC
   * 
   * @param C64 the C64 reference
   * @param baseAddress the base address in memory
   */
  public VIC (C64 C64, int baseAddress) {
    this.c64 = C64;
    this.baseAddress = baseAddress;
    chipModel = 0;    
    basePtrWR = C64.ioBankWR; 
    basePtrRD = C64.ioBankRD;
    
    initChip();
  }
  
  /**
   * Init the VIC chip
   */
  void initChip () {
    for (int i=baseAddress; i<baseAddress+0x3F; ++i) {
      basePtrWR[i] = basePtrRD[i] = 0x00;
    }
    rowCycleCnt=0;
  }
  
  /**
   * Emulate the VIC
   * 
   * @param cycles the processor cycles
   * @return if irq happened
   */
  public byte emulateVIC (byte cycles) {
    int rasterRow;

    rowCycleCnt += cycles;
    if (rowCycleCnt >= rasterRowCycles) {
       rowCycleCnt -= rasterRowCycles;
       rasterRow = ( (basePtrRD[CONTROL+baseAddress]&RASTERROWMSB) << 1 ) + basePtrRD[RASTERROWL+baseAddress];
       ++rasterRow; 
       if (rasterRow >= rasterLines) rasterRow = 0;
       basePtrRD[CONTROL+baseAddress] = ( basePtrRD[CONTROL+baseAddress] & ~RASTERROWMSB ) | ((rasterRow&0x100)>>1);
       basePtrRD[RASTERROWL+baseAddress] = rasterRow & 0xFF;

       if ((basePtrWR[INTERRUPT_ENABLE+baseAddress] & RASTERROW_MATCH_IRQ)!=0) {
         if ( rasterRow == ( (basePtrWR[CONTROL+baseAddress]&RASTERROWMSB) << 1 ) + basePtrWR[RASTERROWL+baseAddress] ) {
           basePtrRD[INTERRUPT+baseAddress] |= VIC_IRQ | RASTERROW_MATCH_IRQ;
         }
       }
    }
    return (byte)(basePtrRD[INTERRUPT+baseAddress] & VIC_IRQ);
  }
  
  /**
   * Acknowledge VIC raster irq
   */
  public void acknowledgeVICrasterIRQ () {
    //An 1 is to be written into the irq-flag (bit0) of $d019 to clear it and deassert irq signal
    //if (VIC->basePtrWR[INTERRUPT] & RASTERROW_MATCH_IRQ) { //acknowledge raster-interrupt by writing to $d019 bit0?
    //But oftentimes INC/LSR/etc. RMW commands are used to acknowledge VIC irq, they work on real
    //CPU because it writes the unmodified original value itself to memory before writing the modified there
    basePtrWR[INTERRUPT+baseAddress] &= ~RASTERROW_MATCH_IRQ;             //prepare for next acknowledge-detection
    basePtrRD[INTERRUPT+baseAddress] &= ~(VIC_IRQ | RASTERROW_MATCH_IRQ); //remove irq flag and state 
  }  
}
