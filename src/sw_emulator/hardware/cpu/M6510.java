/**
 * @(#)M6510.java 1999/08/16
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

package sw_emulator.hardware.cpu;

import sw_emulator.math.Unsigned;
import sw_emulator.util.Monitor;
import sw_emulator.hardware.bus.Bus;
import sw_emulator.hardware.io.M6510IO;
import sw_emulator.hardware.powered;
import sw_emulator.hardware.signaller;
import java.lang.InterruptedException;
import java.lang.Thread;
import sw_emulator.util.Monitor2;

/**
 * Emulate the Mos 6510 cpu.
 * For convenience all the 8 and 16 bits registers are stored in 32 bits (int
 * type).
 *
 * The emulation is made with cycle timing, calling the methods <code>clock()
 * </code>. Even if this solution is not efficently, it permits to have an
 * accurate emulation.
 *
 * The emulation of I/O cpu port (0/1) is made here, not in the device (Ram)
 * connected to the bus. This is not efficently, but it is the real cpu
 * operation. In more, when we write to those locations, the bus write operation
 * is made with a value that was present in the data bus. So if you want to
 * emulate a C64 architecture, you can reproduce the effect that is originated
 * from the VIC II reading operation. In fact, the VIC II reads a byte from
 * memory in the same data bus of Cpu, but when the Cpu is not working with the
 * buses. So, when the Cpu writes to 0 or 1 location, it writes the previous
 * present value of VIC II reading operation.
 *
 * The <code>loadxxx()</code> methods are used to make addressing mode used by
 * bus load instructions like LDA, AND, ...
 * The <code>storexxx()</code> methods are used to make addressing mode used by
 * bus write instructions like STA, STX, ...
 * The <code>loadStorexxx()</code> methods are used to make addressing mode used
 * by bus load/write (RMW) instructions like ASL, LSR, DEC, ...
 * All the precedent methods use to call </code>load()</code> or <code>store()
 * </code> methods, becouse those use the <code>Bus</code>class to emulate a bus
 * load/write emulation.
 *
 * The BRK instruction is also used to patch the memory, using the <code></code>
 *
 * The clock timing is used by calling a <code>clock</code> method, that use an
 * external <code>Monitor</code> for sinchonization with a timing thread. If you
 * don't want a real clock emulation, you must override the <code>clock</code>
 * procedure.
 *
 * The interrupt are manage in a manner very similar that in a real cpu, see
 * <code>run</code> and <code>clock</code> for more detail.
 *
 * Referece:
 * 64doc by John West and Marko M"akel"a
 *
 * Note:
 * the instructions implementation are based onto VICE emulator
 * (see <http://www.cs.cmu.edu/~dsladic/vice/vice.html>)
 * Vice is
 * Copyright  1996-1999 Ettore Perazzoli
 * Copyright  1996-1999 Andr�Fachat
 * Copyright  1993-1994, 1997-1999 Teemu Rantanen
 * Copyright  1997-1999 Daniel Sladic
 * Copyright  1998-1999 Andreas Boose
 * Copyright  1998-1999 Tibor Biczo
 * Copyright  1993-1996 Jouko Valta
 * Copyright  1993-1994 Jarkko Sonninen
 *
 * Thanks to Vice authors.
 *
 * @see run
 * @see clock
 *
 * @author Ice
 * @version 1.00 16/08/1999
 *
 */
public class M6510 extends Thread implements powered, signaller {
  // define costants for manage flags
  public static final int P_SIGN     = 0x80;
  public static final int P_OVERFLOW = 0x40;
  public static final int P_UNUSED   = 0x20;
  public static final int P_BREAK    = 0x10;
  public static final int P_DECIMAL  = 0x08;
  public static final int P_INTERRUPT= 0x04;
  public static final int P_ZERO     = 0x02;
  public static final int P_CARRY    = 0x01;

  // define constans for manage addressing mode
  protected static final byte M_ZERO      = 1;
  protected static final byte M_ABS       = 2;
  protected static final byte M_ABS_X     = 3;
  protected static final byte M_ABS_X_RMW = 4;
  protected static final byte M_ABS_Y     = 5;
  protected static final byte M_ABS_Y_RMW = 6;
  protected static final byte M_IND_Y     = 7;
  protected static final byte M_IMP       = 8;
  protected static final byte M_IMM       = 9;
  protected static final byte M_ZERO_X    = 10;
  protected static final byte M_IND_X     = 11;
  protected static final byte M_NULL      = 12;
  protected static final byte M_ZERO_Y    = 13;
  protected static final byte M_IND       = 14;
  protected static final byte M_ACC       = 15;

  /** Input Reset signal */
  protected int sigRESET;

  /** Input IRQ signal */
  protected int sigIRQ=1;

  /** Input NMI signal */
  protected int sigNMI=1;

  /** Input RDY signal (ready) */
  protected int sigRDY;

  /** Input AEC signal (bus tristate) */
  protected int sigAEC;

  /** Power alimentation state */
  private boolean power=false;

  /** The fetched byte */
  protected int p0;

  /** The 8 bits A register */
  public int regA=0;

  /** The 8 bits X register */
  public int regX=0;

  /** The 8 bits Y register */
  public int regY=0;

  /** The 8 bits P register */
  public int regP=P_UNUSED | P_BREAK;

  /** The 8 bits S register */
  public int regS=0x1FF;

  /** The 16 bit Program Counter register */
  public int regPC=0xFFFC;

  /** The bus view of the cpu */
  protected int view;

  /** I/O Port direction register */
  public int portDir;

  /** I/O Port data register */
  public int portData;

  /** The device that are connected to the bus */
  protected Bus bus;

  /** The monitor where synchronization with a clock */
  protected Monitor monitor;

  /** The processor I/O 6 ports */
  public M6510IO ioPort;

  /** True if an IRQ interrupt is pending */
  protected boolean irqPending=false;

  /** True if a NMI interrupt is pending */
  protected boolean nmiPending=false;

  /**
   * Construct a Mos 6510 cpu.
   *
   * @param monitor the monitor where synchronization with a clock
   * @param bus the bus connected to the cpu and peripherals
   * @param view the bus cpu view
   * @param ioPort the connection of Io with the external (this may be null)
   */
  public M6510(Monitor monitor, Bus bus, int view, M6510IO ioPort) {
    this.monitor=monitor;
    this.bus=bus;
    this.view=view;
    this.ioPort=ioPort;
    setName("CPU");               // use this name for the thread
    //setPriority(MAX_PRIORITY);
    start();
  }

  /**
   * Set up the connection of IO with the external.
   * The cpu emulation is not started if this value is null equal.
   *
   * @param ioPort the external connection
   */
  public void setIO(M6510IO ioPort) {
    this.ioPort=ioPort;
  }


  // implement the addressing mode

  /**
   * Load a value from the bus.
   * If the address to read is 0/1, the cpu must read his I/O ports.
   *
   * @param addr the address of location to read
   * @return the readed byte
   */
  public int load(int addr) {
    while (sigRDY==0) {                      // is RDY low?
      monitor.opWait();                      // subspend Cpu activity
    }
    if (addr<2) return ioPort.readFromPort(addr & 0xffff);
    else        return bus.load(addr & 0xffff, view, sigAEC);
  }

  /**
   * Write a value to the bus.
   * If the address where write is 0/1, the cpu must write his I/O ports.
   *
   * @param addr the address of location to write
   * @param value the value to store
   */
  protected void store(int addr, int value) {
    if (addr<2) {
      ioPort.writeToPort(addr, value);
      // write previous value that the data bus still have.
      bus.store(addr & 0xffff, bus.previous, view, sigAEC);

    } else bus.store(addr & 0xffff, value, view, sigAEC);
  }

  /**
   * Load Zero page addressing mode
   *
   * @param addr the address location
   * @return the readed byte value (stored in 32 bits)
   */
  protected int loadZero(int addr) {
    int tmp;

    tmp=load(addr);               // read effective address
    clock();                      // 3

    return tmp;
  }

  /**
   * Load Zero X page addressing mode
   *
   * @param addr the address location
   * @return the readed byte value (stored in 32 bits)
   */
  protected int loadZeroX(int addr) {
    int tmp;

    load(addr);                   // read from address
    addr+=regX;                   // add index register
    clock();                      // 3

    tmp=load(addr & 0xff);        // read from effective address *
    clock();                      // 4
                                  // *= page boundary crossing are not handled
    return tmp;
  }

  /**
   * Load Zero Y page addressing mode
   *
   * @param addr the address location
   * @return the readed byte value (stored in 32 bits)
   */
  protected int loadZeroY(int addr) {
    int tmp;

    load(addr);                   // read from address
    addr+=regY;                   // add index register
    clock();                      // 3

    tmp=load(addr & 0xff);        // read from effective address *
    clock();                      // 4
                                  // *= page boundary crossing are not handled
    return tmp;
  }

  /**
   * Load Indirect X addressing mode
   *
   * @param addr the address location
   * @return the readed byte value (stored in 32 bits)
   */
  protected int loadIndX(int addr) {
    int tmp, tmp2;

    load(addr);                   // read from the address
    tmp2=(addr+regX)& 0xff;       // add X to it
    clock();                      // 3

    tmp=load(tmp2);               // fetch effective address low *
    clock();                      // 4

    tmp+=(load((tmp2+1)&0xff)<<8);// fetch effective address hi *
    clock();                      // 5

    tmp=load(tmp);                // read from effective address
    clock();                      // 6
                                  // *= page boundary crossing are not handled
    return tmp;
  }

  /**
   * Load Indirect Y addressing mode
   *
   * @param addr the address location
   * @return the readed byte value (stored in 32 bits)
   */
  protected int loadIndY(int addr) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp,tmp2;

    tmp2=load(addr);              // fetch effective address low
    clock();                      // 3

    if (((tmp2+regY) & 0xff00)!=0)
      cross=1;
    tmp2+=(load(addr+1)<<8);      // fetch effective address hi
    tmp2=(tmp2 & 0xff00) |
         ((tmp2+regY)& 0xff);     // add Y to low byte of effective address
    clock();                      // 4

    tmp=load(tmp2);               // read from effective address *
    tmp2+=(cross<<8);             // fix hi byte of effective address
    clock();                      // 5

    if (cross==1) {
      tmp=load(tmp2);             // read from effective address
      clock();                    // 6
    }
                                  // *= high byte of effec. addr. may be invalid
    return tmp;
  }

  /**
   * Load Absolute addressing mode
   *
   * @param addr the address location
   * @return the readed byte value (stored in 32 bits)
   */
  protected int loadAbs(int addr) {
    int tmp;

    addr|=load(regPC++)<<8;       // fetch hi byte of address
    clock();                      // 3

    tmp=load(addr);               // read from effective address
    clock();                      // 4

    return tmp;
  }

  /**
   * Load Absolute X addressing mode
   *
   * @param addr the address location
   * @return the readed byte (stored in 32 bits)
   */
  protected int loadAbsX(int addr) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp, tmp2;

    if (((addr+regX) & 0xff00)!=0)
      cross=1;
    addr|=(load(regPC++)<<8);     // fetch hi byte of address, increment PC
    tmp2=(addr & 0xff00)+
        ((addr+regX)& 0xff);      // add index register to low address byte
    clock();                      // 3

    tmp=load(tmp2);               // read from effective address *
    tmp2+=(cross<<8);             // fix the high byte of effective address
    clock();                      // 4

    if (cross==1) {
      tmp=load(tmp2);             // re-read from effective address
      clock();                    // 5
    }
                                  // *= high byte of effec. addr. may be invalid
    return tmp;
  }

  /**
   * Load Absolute Y addressing mode
   *
   * @param addr the address location
   * @return the readed byte (stored in 32 bits)
   */
  protected int loadAbsY(int addr) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp, tmp2;

    if (((addr+regY) & 0xff00)!=0)
      cross=1;
    addr|=load(regPC++)<<8;       // fetch hi byte of address, increment PC
    tmp2=(addr & 0xff00)+
         ((addr+regY)& 0xff);     // add index register to low address byte
    clock();                      // 3

    tmp=load(tmp2);               // read from effective address *
    tmp2+=(cross<<8);             // fix the high byte of effective address
    clock();                      // 4

    if (cross==1) {
      tmp=load(tmp2);             // re-read from effective address
      clock();                    // 5
    }
                                  // *= high byte of effec. addr. may be invalid
    return tmp;
  }

  /**
   * Load Store Zero page addressing mode
   *
   * @param addr the address location
   * @return the effective address and the readed byte (stored in 24 bits of 32)
   */
  protected int loadStoreZero(int addr) {
    int tmp;

    tmp=load(addr);               // read effective address
    clock();                      // 3

    store(addr, tmp);             // write the value back to the effective addr.

    return (addr<<8)+tmp;
  }

  /**
   * Load Store Zero X page addressing mode
   *
   * @param addr the address location
   * @return the effective address and the readed byte (stored in 24 bits of 32)
   */
  protected int loadStoreZeroX(int addr) {
    int tmp;

    load(addr);                   // read from address
    addr+=regX;                   // add index register
    clock();                      // 3

    tmp=load(addr & 0xff);        // read from effective address *
    clock();                      // 4

    store(addr & 0xff, tmp);      // write the value back to the effect. addr. *
                                  // *= page boundary crossing are not handled
    return ((addr & 0xff)<<8)+tmp;
  }

  /**
   * Load Store Absolute addressing mode
   *
   * @param addr the address location
   * @return the effective address and the readed byte (stored in 24 bits of 32)
   */
  protected int loadStoreAbs(int addr) {
    int tmp;

    addr|=(load(regPC++)<<8);     // fetch hi byte of address
    clock();                      // 3

    tmp=load(addr);               // read from effective address
    clock();                      // 4

    store(addr,tmp);              // write the value back to the effective addr.

    return (addr<<8)+tmp;
  }

  /**
   * Load Store Absolute X addressing mode
   *
   * @param addr the address location
   * @return the effective address and the readed byte (stored in 24 bits of 32)
   */
  protected int loadStoreAbsX(int addr) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp, tmp2;

    if (((addr+regX) & 0xff00)!=0)
      cross=1;
    addr|=(load(regPC++)<<8);     // fetch hi byte of address, increment PC
    tmp2=(addr & 0xff00)+
        ((addr+regX)& 0xff);      // add index register to low address byte
    clock();                      // 3

    tmp=load(tmp2);               // read from effective address *
    tmp2+=(cross<<8);             // fix the high byte of effective address
    clock();                      // 4

    tmp=load(tmp2);               // re-read from effective address
    clock();                      // 5

    store(tmp2, tmp);             // write the value back to the effective addr.
                                  // *= high byte of effec. addr. may be invalid
    return (tmp2<<8)+tmp;
  }

  /**
   * Load Store Absolute Y addressing mode
   *
   * @param addr the address location
   * @return the effective address and the readed byte (stored in 24 bits of 32)
   */
  protected int loadStoreAbsY(int addr) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp, tmp2;

    if (((addr+regY) & 0xff00)!=0)
      cross=1;
    addr|=(load(regPC++)<<8);     // fetch hi byte of address, increment PC
    tmp2=(addr & 0xff00)+
        ((addr+regY)& 0xff);      // add index register to low address byte
    clock();                      // 3

    tmp=load(tmp2);               // read from effective address *
    tmp2+=(cross<<8);             // fix the high byte of effective address
    clock();                      // 4

    tmp=load(tmp2);               // re-read from effective address
    clock();                      // 5

    store(tmp2, tmp);             // write the value back to the effective addr.
                                  // *= high byte of effec. addr. may be invalid
    return (tmp2<<8)+tmp;
  }

  /**
   * Load Store Indirect X addressing mode
   *
   * @param addr the address location
   * @return the effective address and the readed byte (stored in 24 bits of 32)
   */
  protected int loadStoreIndX(int addr) {
    int tmp, tmp2;

    load(addr);                   // read from the address
    tmp2=(addr+regX)& 0xff;       // add X to it
    clock();                      // 3

    tmp=load(tmp2);               // fetch effective address low
    clock();                      // 4

    tmp+=(load((tmp2+1)&0xff)<<8);// fetch effective address high *
    clock();                      // 5

    tmp2=load(tmp);               // read from effective address
    clock();                      // 6

    store(tmp, tmp2);             // write the value back to the effective addr.
                                  // *= high byte of effec. addr. may be invalid
    return (tmp<<8)+tmp2;
  }

  /**
   * Load Store Indirect Y addressing mode
   *
   * @param addr the address location
   * @return the effective address and the readed byte (stored in 24 bits of 32)
   */
 protected int loadStoreIndY(int addr) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp,tmp2;

    tmp2=load(addr);              // fetch effective address low
    clock();                      // 3

    if (((tmp2+regY) & 0xff00)!=0)
      cross=1;
    tmp2+=(load(addr+1)<<8);      // fetch effective address hi
    tmp2=(tmp2 & 0xff00) |
         ((tmp2+regY)& 0xff);     // add Y to low byte of effective address
    clock();                      // 4

    tmp=load(tmp2);               // read from effective address *
    tmp2+=(cross<<8);             // fix hi byte of effective address
    clock();                      // 5

    tmp=load(tmp2);               // read from effective address
    clock();                      // 6

    store(tmp2, tmp);             // write the value back to the effective addr.
                                  // *= high byte of effec. addr. may be invalid
    return (tmp2<<8)+tmp;
  }

  /**
   * Store Zero X page addressing mode
   *
   * @param addr the address location
   * @return the effective address
   */
  protected int storeZeroX(int addr) {
    load(addr);                   // read from address
    addr+=regX;                   // add index register
    clock();                      // 3
                                  // *=page boundary crossing are not handled
    return addr & 0xff;           // *
  }

  /**
   * Store Zero Y page addressing mode
   *
   * @param addr the address location
   * @return the effective address
   */
  protected int storeZeroY(int addr) {
    load(addr);                   // read from address
    addr+=regY;                   // add index register
    clock();                      // 3
                                  // *= page boundary crossing are not handled
    return addr & 0xff;           // *
  }

  /**
   * Store Absolute addressing mode
   *
   * @param addr the address location
   * @return the effective address
   */
  protected int storeAbs(int addr) {
    addr|=(load(regPC++)<<8);     // fetch hi byte of address
    clock();                      // 3

    return addr;
  }

  /**
   * Store Absolute X addressing mode
   *
   * @param addr the address location
   * @return the effective address
   */
  protected int storeAbsX(int addr) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp;

    if (((addr+regX) & 0xff00)!=0)
      cross=1;
    addr|=(load(regPC++)<<8);     // fetch hi byte of address, increment PC
    tmp=(addr & 0xff00)+
        ((addr+regX)& 0xff);      // add index register to low address byte
    clock();                      // 3

    load(tmp);                    // read from effective address *
    tmp+=(cross<<8);              // fix the high byte of effective address
    clock();                      // 4
                                  // *= high byte of effec. addr. may be invalid
    return tmp;
  }

  /**
   * Store Absolute Y addressing mode
   *
   * @param addr the address location
   * @return the effective address
   */
  protected int storeAbsY(int addr) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp;

    if (((addr+regY) & 0xff00)!=0)
      cross=1;
    addr|=(load(regPC++)<<8);     // fetch hi byte of address, increment PC
    tmp=(addr & 0xff00)+
        ((addr+regY)& 0xff);      // add index register to low address byte
    clock();                      // 3

    load(tmp);                    // read from effective address *
    tmp+=(cross<<8);              // fix the high byte of effective address
    clock();                      // 4
                                  // *= high byte of effec. addr. may be invalid
    return tmp;
  }

  /**
   * Store Indirect X addressing mode
   *
   * @param addr the address location
   * @return the effective address
   */
  protected int storeIndX(int addr) {
    int tmp, tmp2;

    load(addr);                   // read from the address
    tmp2=(addr+regX)& 0xff;       // add X to it
    clock();                      // 3

    tmp=load(tmp2);               // fetch effective address low *
    clock();                      // 4

    tmp+=(load((tmp2+1)&0xff)<<8);// fetch effective address hi *
    clock();                      // 5
                                  // *= page boundary crossing is not handled
    return tmp;
  }

  /**
   * Store Indirect Y addressing mode
   *
   * @param addr the address location
   * @return the effective address
   */
  protected int storeIndY(int addr) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp;

    tmp=load(addr);               // fetch effective address low
    clock();                      // 3

    if (((tmp+regY) & 0xff00)!=0)
      cross=1;
    tmp+=(load(addr+1)<<8);       // fetch effective address hi
    tmp=(tmp & 0xff00) |
         ((tmp+regY)& 0xff);      // add Y to low byte of effective address
    clock();                      // 4

    load(tmp);                    // read from effective address *
    tmp+=(cross<<8);              // fix hi byte of effective address
    clock();                      // 5
                                  // *= high byte of effec. addr. may be invalid
    return tmp;
  }


  // implement flags manage

  /**
   * set the Overflow flag to the <code>val</code> value
   *
   * @param val the 0/1 bit value
   */
  protected void setOverflow(int val) {
    if (val==0) regP&=~P_OVERFLOW;
    else regP|=P_OVERFLOW;
  }

  /**
   * set the Break flag to the <code>val</code> value
   *
   * @param val the 0/1 bit value
   */
  protected void setBreak(int val) {
    if (val==0) regP&=~P_BREAK;
    else regP|=P_BREAK;
  }

  /**
   * set the Decimal flag to the <code>val</code> value
   *
   * @param val the 0/1 bit value
   */
  protected void setDecimal(int val) {
    if (val==0) regP&=~P_DECIMAL;
    else regP|=P_DECIMAL;
  }

  /**
   * set the Interrupt flag to the <code>val</code> value
   *
   * @param val the 0/1 bit value
   */
  protected void setInterrupt(int val) {
    if (val==0) regP&=~P_INTERRUPT;
    else regP|=P_INTERRUPT;
  }

  /**
   * set the Carry flag to the <code>val</code> value
   *
   * @param val the 0/1 bit value
   */
  protected void setCarry(int val) {
    if (val==0) regP&=~P_CARRY;
    else regP|=P_CARRY;
  }

  /**
   * set the Sign flag to the <code>val</code> value
   *
   * @param val the 0/1 bit value
   */
  protected void setSign(int val) {
    if (val==0) regP&=~P_SIGN;
    else regP|=P_SIGN;
  }

  /**
   * set the Zero flag to the <code>val</code> value
   *
   * @param val the 0/1 bit value
   */
  protected void setZero(int val) {
    if (val==0) regP|=P_ZERO;
    else regP&=~P_ZERO;
  }

  /**
   * Set the Sign/Zero flags that <code>val</code>value represent
   *
   * @param val the value to evaluate Sign/Zero
   */
  protected void setNZ(int val) {
    setZero(val);
    setSign(val & P_SIGN);
  }

  /**
   * set the Decimal flag to the <code>val</code> value
   *
   * @param val the boolean value to assign
   */
  protected void setDecimal(boolean val) {
    if (val) regP|=P_DECIMAL;
    else regP&=~P_DECIMAL;
  }

  /**
   * set the Overflow flag to the <code>val</code> value
   *
   * @param val the boolean value to assign
   */
  protected void setOverflow(boolean val) {
    if (val) regP|=P_OVERFLOW;
    else regP&=~P_OVERFLOW;
  }

  /**
   * set the Break flag to the <code>val</code> value
   *
   * @param val the boolean value to assign
   */
  protected void setBreak(boolean val) {
    if (val) regP|=P_BREAK;
    else regP&=~P_BREAK;
  }

  /**
   * set the Interrupt flag to the <code>val</code> value
   *
   * @param val the boolean value to assign
   */
  protected void setInterrupt(boolean val) {
    if (val) regP|=P_INTERRUPT;
    else regP&=~P_INTERRUPT;
  }

  /**
   * set the Carry flag to the <code>val</code> value
   *
   * @param val the boolean value to assign
   */
  protected void setCarry(boolean val) {
    if (val) regP|=P_CARRY;
    else regP&=~P_CARRY;
  }

  /**
   * set the Sign flag to the <code>val</code> value
   *
   * @param val the boolean value to assign
   */
  protected void setSign(boolean val) {
    if (val) regP|=P_SIGN;
    else regP&=~P_SIGN;
  }

    /**
   * set the Zero flag to the <code>val</code> value
   *
   * @param val the boolean value to assign
   */
  protected void setZero(boolean val) {
    if (val) regP|=P_ZERO;
    else regP&=~P_ZERO;
  }


  /**
   * Called to suspend cpu activity until the next clock signal.
   * It also search for pending interrupt.
   *
   * Note: the cpu execute an interrupt when an operation is completed, so an
   * interrupt sequence is processed before the next instruction only if it is
   * notify before the last cycle of the current instruction.
   * In this emulator the interrupt sequence is valutated only when an
   * instruction is finished, so it's right to search for pending interrupt
   * before suspend the thread until the next clock signal. Infact, if this cycle
   * is the last of the instruction, we don't valutate the pending interrupt
   * when the thread is resume; instead if it is not the last, at the next clock
   * signal we find the pending interrupt.
   *
   * The unique case that is not like in the real cpu if an interrupt, arrived
   * before the last instruction, appears after that the thread is waiting, but
   * before it is resume. Sorry...
   */
  public void clock() {
    ///System.out.println("Clock");
        
    if (sigNMI==0) nmiPending=true;
    if (sigIRQ==0) irqPending=true;
     
    ///monitor.opSignal2(); 
    monitor.opWait();
  }

  // instructions implementation

  /**
   * Execute an ADC cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void ADC(int type) {
    int tmp, tmpVal;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadIndY(tmp);
        break;
    }

    tmpVal=tmp;
    if ((regP & P_DECIMAL)!=0) {
      tmp=(regA & 0x0f)+(tmpVal & 0x0f)+ (regP & P_CARRY);
      if (tmp>0x9) tmp+=0x6;
      if (tmp<=0x0f)
        tmp=(tmp & 0x0f)+(regA & 0xf0)+(tmpVal & 0xf0);
      else tmp=(tmp & 0x0f)+(regA & 0xf0)+(tmpVal & 0xf0)+ 0x10;
      setZero(!((regA+tmpVal+(regP & P_CARRY) & 0xff)!=0));
      setSign(tmp & 0x80);
      setOverflow((((regA ^ tmp) & 0x80)!=0) && !(((regA ^ tmpVal) & 0x80)!=0));
      if ((tmp & 0x1f0)> 0x90)
        tmp+=0x60;
      setCarry((tmp & 0xff0)>0xf0);
    } else {
        tmp=tmpVal+regA+(regP & P_CARRY);
        setNZ(tmp & 0xff);
        setOverflow( (((regA ^ tmpVal) & 0x80)==0)&&(((regA ^ tmp) & 0x80)!=0));
        setCarry(tmp & 0xff00);
      }
    regA=tmp & 0xff;
  }

  /**
   * Execute an ANC cpu undocument instruction.
   */
  public void ANC() {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    regA&=tmp;
    setNZ(regA);
    setCarry(regP & P_SIGN);
  }

  /**
   * Execute an AND cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void AND(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadIndY(tmp);
        break;
    }

    regA&=tmp;                   // do operation
    setNZ(regA);
  }

  /**
   * Execute an ANE cpu undocument instruction.
   */
  public void ANE() {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    regA=(regA | 0xee) & regX & tmp;
    setNZ(regA);
  }

  /**
   * Execute an ARR cpu undocument instruction.
   */
  public void ARR() {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    tmp=regA & tmp;               // do operation

    if ((regP & P_DECIMAL)!=0) {
      int tmp2=tmp;
      tmp2|=(regP & P_CARRY)<<8;
      tmp2>>=1;
      setSign(regP & P_CARRY);
      setZero(!(tmp2!=0));
      setOverflow((tmp2 ^ tmp) & 0x40);
      if (((tmp & 0x0f)+(tmp & 0x01))>0x05)
        tmp2=(tmp2 & 0xf0) | ((tmp2+0x06) & 0x0f);
      if (((tmp & 0xf0)+(tmp & 0x10))>0x50) {
        tmp2=(tmp2 & 0x0f) | ((tmp2+0x60) & 0xf0);
        setCarry(1);
      } else setCarry(0);
      regA=tmp2;
    } else {
       tmp|=(regP & P_CARRY) << 8;
       tmp>>=1;
       setNZ(tmp);
       setCarry(tmp & 0x40);
       setOverflow((tmp & 0x40) ^ ((tmp & 0x20) << 1));
       regA=tmp; 
      }
  }

  /**
   * Execute an ASL cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void ASL(int type) {
    int tmp, val;

    if (type==M_ACC) {
      load(regPC);                // fetch next byte and forget it
      clock();                    // 2

      setCarry(regA & 0x80);      // do operation on it
      regA<<=1;
      regA&=0xff;                 // 8 bits
      setNZ(regA);
      return;
    }

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    setCarry(val & 0x80);         // do operation on it
    val=(val << 1) & 0xff;
    setNZ(val);
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute an ASR cpu undocument instruction.
   */
  public void ASR() {
    int tmp;

    tmp=load(regPC++);            // fetch value, increment PC
    clock();                      // 2

    tmp=regA & tmp;               // do operation
    setCarry(tmp & 0x01);
    regA=tmp >> 1;
    setNZ(regA);
  }

  /**
   * Execute a BIT cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void BIT(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
    }

    setSign(tmp & 0x80);
    setOverflow(tmp & 0x40);
    setZero(!((tmp & regA)!=0));
  }

  /**
   * Execute a Branch cpu legal instruction.
   * Note: this instruction is execute in different manner from where described
   * in the 64doc. This is done for making the fetch opcode and increment PC a
   * features of a instruction execution, and so makes the code more simple.
   *
   * @param cond the boolean value of condition
   */
  public void BRANCH(boolean cond) {
    int cross=0;                  // 1 if there's a page boundary crossing
    int tmp;

    tmp=load(regPC++);            // fetch operand, increment PC
    clock();                      // 2

    if (!cond) {                  // is branch not taken?
      return;
    } else {
        p0=load(regPC);           // fetch opcode of next instruction

        cross=(regPC+(byte)tmp)   // real address
              & 0xffff;

        regPC=(regPC & 0xff00)+
              ((regPC+(byte)tmp)
              & 0xff);            // add operand to PCL
        cross-=regPC;             // difference with real
        clock();                  // 3
    }

    if (cross==0) {               // will PCH not changes by fix?
      return;
    } else {
                                  // *= PCH may be invalid
       p0=load(regPC);            // fetch opcode of next instruction *
       regPC+=cross;              // Fix PCH
       clock();                   // 4
      }
  }

  /**
   * Execute a BRK cpu legal instruction
   * This opocode is also used to patch the memory
   */
  public void BRK() {
    int tmp;

    tmp=load(regPC++);            // read next byte (and forget it)
    clock();                      // 2

    setBreak(1);                  // push PCH on stack (with B flag set), dec. S
    store(regS--, regPC>>8);
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 3

    store(regS--, regPC & 0xff);  // push PCL on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 4

    store(regS--, regP);          // push P on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 5

    regPC=(regPC & 0xff00)+       // fetch PCL
          load(0xfffe);           // 6
    clock();

    regPC=(regPC & 0xff)+         // fetch PCH
          (load(0xffff)<<8);
    setInterrupt(1); 
    clock();                      // 7
  }

  /**
   * Execute a CLC legal instruction
   */
  public void CLC() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    setCarry(0);
  }

  /**
   * Execute a CLD legal instruction
   */
  public void CLD() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    setDecimal(0);
  }

  /**
   * Execute a CLI legal instruction
   */
  public void CLI() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    setInterrupt(0);
  }

  /**
   * Execute a CLV legal instruction
   */
  public void CLV() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    setOverflow(0);
  }

  /**
   * Execute a CMP cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void CMP(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadIndY(tmp);
        break;
    }

    tmp=regA-tmp;
    setCarry(tmp>=0);
    setNZ(tmp);
  }

  /**
   * Execute a CPX cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void CPX(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
    }

    tmp=regX-tmp;      // do operation
    setCarry(tmp>=0);
    setNZ(tmp);
 }

  /**
   * Execute a CPY cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void CPY(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
    }

    tmp=regY-tmp;                 // do operation
    setCarry(tmp>=0);
    setNZ(tmp);
 }

  /**
   * Execute a DCP cpu undocument instruction.
   *
   * @param type the type of addressing
   */
  public void DCP(int type) {
    int tmp, val;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadStoreAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadStoreIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadStoreIndY(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    val=(val-1) & 0xff;           // do operation
    setCarry(regA >= val);
    setNZ(regA-val);
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute a DEC cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void DEC(int type) {
    int tmp, val;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;   
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadStoreAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadStoreIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadStoreIndY(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    val=(val-1) & 0xff;           // do operation
    setNZ(val);
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute a DEX legal instruction
   */
  public void DEX() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regX--;                       // decrement register
    regX&=0xFF;                   // use 8 bits
    setNZ(regX);
  }

  /**
   * Execute a DEY legal instruction
   */
  public void DEY() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regY--;                       // decrement register
    regY&=0xFF;                   // use 8 bits
    setNZ(regY);
  }

  /**
   * Execute an EOR cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void EOR(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadIndY(tmp);
        break;
    }

    regA^=tmp;
    setNZ(regA);
  }

  /**
   * Execute an INC cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void INC(int type) {
    int tmp, val;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    val=(val+1)& 0xff;            // do operation
    setNZ(val);
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute a INX legal instruction
   */
  public void INX() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regX++;                       // increment register
    regX&=0xFF;                   // use 8 bits
    setNZ(regX);
  }

  /**
   * Execute a INY legal instruction
   */
  public void INY() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regY++;                       // icrement register
    regY&=0xFF;                   // use 8 bits
    setNZ(regY);
  }

  /**
   * Execute a IRQ cpu sequence
   */
  public void IRQ() {
    int tmp;

    tmp=load(regPC++);            // read next byte (and forget it), inc. PC
    clock();                      // 2

    store(regS--, regPC>>8);      // push PCH on stack (with B flag set), dec. S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 3

    store(regS--, regPC & 0xff);  // push PCL on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 4

    store(regS--, regP);          // push P on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 5

    regPC=(regPC & 0xff00)+       // fetch PCL
          load(0xfffe);           // 6
    clock();

    regPC=(regPC & 0xff)+         // fetch PCH
          (load(0xffff)<<8);
    clock();                      // 7
  }
  
  /**
   * Execute a ISB cpu undocument instruction.
   *
   * @param type the type of addressing
   */
  public void ISB(int type) {
    int tmp, val, val2, src;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2 
    
    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadStoreAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadStoreIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadStoreIndY(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    val=(val+1) & 0xff;           // do operation
    src=val;
    val2=regA-src-((regP & P_CARRY)!=0 ? 0 : 1); 
    if ((regP & P_DECIMAL)!=0) {
      int tmpA;
      tmpA=(regA & 0xf)-(src & 0xf)-((regP & P_CARRY)!=0 ? 0 : 1); 
      if ((tmpA & 0x10)!=0)
        tmpA=((tmpA - 6) & 0xf)| ((regA & 0xf0)-(src & 0xf0)-0x10);
      else
        tmpA=(tmpA & 0xf) | ((regA & 0xf0)-(src & 0xf0));
      if ((tmpA & 0x100)!=0)
        tmpA-=0x60;
      setCarry(val2>=0);
      setNZ(val2 & 0xff);
      setOverflow((((regA^val2) & 0x80)!=0) && (((regA^src) & 0x80))!=0);
      regA=Unsigned.done((byte)tmpA);
    } else {
        setNZ(val2 & 0xff);
        setCarry(val2>=0);
        setOverflow((((regA^val2) & 0x80)!=0) && (((regA^src) & 0x80))!=0);
        regA=Unsigned.done((byte)val2);
    }
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++    
  }

  /**
   * JAM the machine
   */
  public void JAM() {
  }

  /**
   * Execute a JMP cpu legal instruction.
   *
   * @parm type the type of addressing
   */
  public void JMP(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch low addr./pointer byte, increment PC
    clock();                      // 2

    switch (type) {
      case M_ABS:
        regPC=tmp+
              (load(regPC)<<8);   // copy low addr. byte to PCL, fetch hi to PCH
        clock();                  // 3
        break;
      case M_IND:
        int tmp2;

        tmp+=(load(regPC++)<<8);  // fetch pointer address high, increment PC
        clock();                  // 3

        tmp2=load(tmp);           // fetch low address to latch
        clock();                  // 4
                                  // fetch PCH, copy latch to PCL
        regPC=(load((tmp & 0xff00)|((tmp+1)& 0xff))<<8)+tmp2;
        clock();                  // 5
    }
  }

  /**
   * Execute a JSR cpu legal instruction.
   */
  public void JSR() {
    int tmp;

    tmp=load(regPC++);            // fetch low address byte, increment PC
    clock();                      // 2

                                  // ? unknown internal operation
    load(regS);                   //
    clock();                      // 3

    store(regS--, regPC>>8);      // push PCH on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 4

    store(regS--, regPC & 0xff);  // push PCL on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 5

    regPC=tmp+(load(regPC)<<8);   // copy low addr. byte to PCL, fetch hi to PCH
    clock();                      // 6
  }

  /**
   * Execute a LAS cpu undocument instruction.
   */
  public void LAS() {
    int tmp;

    tmp=load(regPC++);            // fetch low address byte, increment PC
    clock();                      // 2

    tmp=loadAbsY(tmp);

    regA=regX=regS=regS & tmp;
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;

    setNZ(regA);
  }

  /**
   * Execute a LAX undocument instruction
   *
   * @param type the type of addressing
   */
  public void LAX(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=load(tmp);
        break;
      case M_ZERO_Y:
        tmp=loadZeroY(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_Y:
        tmp=loadAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadIndY(tmp);
        break;
    }

    regA=regX=tmp;
    setNZ(regA);
  }

  /**
   * Execute a LDA cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void LDA(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadIndY(tmp);
        break;
    }

    regA=tmp;                    // do operation
    setNZ(regA);
  }


  /**
   * Execute a LDX cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void LDX(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ZERO_Y:
        tmp=loadZeroY(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_Y:
        tmp=loadAbsY(tmp);
        break;
    }

    regX=tmp;                    // do operation
    setNZ(regX);
  }

  /**
   * Execute a LDY cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void LDY(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadAbsX(tmp);
        break;
    }

    regY=tmp;                    // do operation
    setNZ(regY);
  }

  /**
   * Execute an LSR cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void LSR(int type) {
    int tmp, val;

     if (type==M_ACC) {
      load(regPC);                // fetch next byte and forget it
      clock();                    // 2

      setCarry(regA & 0x01);      // do operation
      regA>>=1;
      setNZ(regA);
      return;
    }


    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    setCarry(val & 0x01);         // do operation
    val>>=1;
    setNZ(val);
    clock();                      // +

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute a LXA cpu undocument instruction.
   * Note: this code is not exactly becouse the real is unstable.
   */
  public void LXA() {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    regA=regX=(regA | 0xee) & tmp;
    setNZ(regA);
  }

  /**
   * Execute a NMI cpu sequence
   */
  public void NMI() {
    int tmp;

    tmp=load(regPC++);            // read next byte (and forget it), inc. PC
    clock();                      // 2

    store(regS--, regPC>>8);      // push PCH on stack (with B flag set), dec. S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 3

    store(regS--, regPC & 0xff);  // push PCL on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 4

    store(regS--, regP);          // push P on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 5

    regPC=(regPC & 0xff00)+       // fetch PCL
          load(0xfffa);           // 6
    clock();

    regPC=(regPC & 0xff)+         // fetch PCH
          (load(0xfffb)<<8);
    clock();                      // 7
  }

  /**
   * Execute a NOP cpu legal instruction
   */
  public void NOP() {
    p0=load(regPC);               // read next byte (and forget it)
    clock();                      // 2
  }

  /**
   * Execute a NOOP cpu undocument instruction
   *
   * @param type the type of addressing
   */
  public void NOOP(int type) {
    int tmp;

    tmp=load(regPC); // fetch next value, increment PC
    clock(); // 2

    switch (type) {
      case M_IMM:
        regPC++;
        break;
      case M_IMP:
        break;
      case M_ZERO:
        regPC++;
        tmp=loadZero(tmp);
        break;
      case M_ZERO_X:
        regPC++;
        tmp=loadZeroX(tmp);
        break;
      case M_ABS:
        regPC++;
        tmp=loadAbs(tmp);
        break;
      case M_ABS_X:
        regPC++;
        tmp=loadAbsX(tmp);
        break;
      case M_IND_X:
        regPC++;
        tmp=loadIndX(tmp);
        break;
   }
} 

  /**
   * Execute a ORA cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void ORA(int type) {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadIndY(tmp);
        break;
    }

    regA|=tmp;                   // do operation
    setNZ(regA);
  }

  /**
   * Execute a PHA cpu legal instruction
   */
  public void PHA() {
    p0=load(regPC);               // read next instruction byte (forget it)
    clock();                      // 2

    store(regS--, regA);          // push register on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 3
  }

  /**
   * Execute a PHP cpu legal instruction
   */
  public void PHP() {
    p0=load(regPC);               // read next instruction byte (forget it)
    clock();                      // 2

    store(regS--, regP| P_BREAK); // push register on stack, decrement S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 3
  }

  /**
   * Execute a PLA cpu legal instruction
   */
  public void PLA() {
    p0=load(regPC);               // read next instruction byte (forget it)
    clock();                      // 2

    regS++;                       // increment S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 3

    regA=load(regS);              // pop register from stack
    setNZ(regA);
    clock();                      // 4
  }

  /**
   * Execute a PLP cpu legal instruction
   */
  public void PLP() {
    p0=load(regPC);               // read next instruction byte (forget it)
    clock();                      // 2

    regS++;                       // increment S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 3

    regP=load(regS) |             // pop register from stack
         P_UNUSED |               // unused and break must be 1
         P_BREAK;
    clock();                      // 4
  }

  /**
   * Execute a RLA cpu undocument instruction.
   *
   * @param type the type of address
   */
  public void RLA(int type) {
    int tmp, val;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadStoreAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadStoreIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadStoreIndY(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;
    
    val = ((val<<1) | (regP & P_CARRY)); 

    setCarry(val & 0x100);
    regA&=val;
    setNZ(regA);
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++    
  }

  /**
   * Execute a ROL cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void ROL(int type) {
    int tmp, val;

    if (type==M_ACC) {
      load(regPC);                // fetch next byte and forget it
      clock();                    // 2

      regA=(regA<<1)|(regP & P_CARRY);
      setCarry(regA & 0x100);
      regA&=0xff;                 //8 bits
      setNZ(regA);
      return;
    }

    tmp=load(regPC++);            // fetch next value,  increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    val=(val<<1) | (regP & P_CARRY);
    setCarry(val & 0x100);
    val&=0xff;                    //8 bits
    setNZ(val);
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute a ROR cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void ROR(int type) {
    int tmp, val;

    if (type==M_ACC) {
      load(regPC);                // fetch next byte and forget it
      clock();                    // 2

      tmp=regP;
      setCarry(regA & 0x01);
      regA=(regA>>1) | (tmp<<7);
      regA&=0xff;                 // 8 bits
      setNZ(regA);
      return;
    }

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    if ((regP & P_CARRY)!=0)      // do operation
      val|=0x100;
    setCarry(val & 0x01);
    val>>=1;
    setNZ(val);
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute a RRA cpu undocument instruction.
   *
   * @param type the type of addressing
   */
  public void RRA(int type) {
    int tmp, tmp2, tmpVal, val, val2;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadStoreAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadStoreIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadStoreIndY(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    val2=val>>1;
    if ((regP & P_CARRY)!=0)
      val2|=0x80;
    setCarry(val & 0x1);   

    tmpVal=val2;
    if ((regP & P_DECIMAL)!=0) {
      tmp2=(regA & 0x0f)+(tmpVal & 0x0f)+ (regP & P_CARRY);
      if (tmp2>0x9) tmp2+=0x6;
      if (tmp2<=0x0f)
        tmp2=(tmp2 & 0x0f)+(regA & 0xf0)+(tmpVal & 0xf0);
      else tmp2=(tmp2 & 0x0f)+(regA & 0xf0)+(tmpVal & 0xf0)+ 0x10;
      setZero(!((regA+tmpVal+(regP & P_CARRY) & 0xff)!=0));
      setSign(tmp2 & 0x80);
      setOverflow((((regA^tmp2) & 0x80)!=0) && !(((regA^tmpVal) & 0x80)!=0));
      if ((tmp2 & 0x1f0)> 0x90)
        tmp2+=0x60;
      setCarry((tmp2 & 0xff0)>0xf0);
    } else {
        tmp2=tmpVal+regA+(regP & P_CARRY);
        setNZ(tmp2 & 0xff);
        setOverflow((((regA ^ tmpVal) & 0x80)==0)&&(((regA ^ tmp2) & 0x80)!=0));
        setCarry(tmp2>0xff);
      }
    regA=tmp2 & 0xff;
    clock();                      // ++

    store(tmp, val2);             // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute a RTI cpu legal instruction
   */
  public void RTI() {
   p0=load(regPC);                // read next instruction byte
   clock();                       // 2

   regS++;                        // increment S
   regS&=0x1FF;                   // regS is in 100h-1FFh
   regS|=0x100;
   clock();                       // 3

   regP=load(regS++) |            // pop P from stack, increment S
            P_UNUSED |            // unused and break must be 1
            P_BREAK;        
   regS&=0x1FF;                   // regS is in 100h-1FFh
   regS|=0x100;
   clock();                       // 4

   regPC=load(regS++);            // pop PCL from stack, increment S
   regS&=0x1FF;                   // regS is in 100h-1FFh
   regS|=0x100;
   clock();                       // 5

   regPC|=(load(regS)<<8);        // pop PCH from stack
   clock();                       // 6
  }

  /**
   * Execute a RTS cpu legal instruction
   */
  public void RTS() {
    p0=load(regPC);               // read next instruction byte
    clock();                      // 2

    regS++;                       // increment S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 3

    regPC=load(regS++);           // pop PCL from stack, increment S
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 4

    regPC|=(load(regS)<<8);       // pop PCH from stack
    clock();                      // 5

    regPC++;                      // increment PC
    clock();                      // 6
  }

  /**
   * Execute a SAX cpu undocument instruction.
   *
   * @param type the type of address
   */
  public void SAX(int type) {
    int tmp=0;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        break;
      case M_ZERO_Y:
        tmp=storeZeroY(tmp);
        break;
      case M_ABS:
        tmp=storeAbs(tmp);
        break;
      case M_IND_X:
        tmp=storeIndX(tmp);
        break;
    }

    store(tmp, regA & regX);      // write to effective address
    clock();                      // ++
  }

  /**
   * Execute a SBC cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void SBC(int type) {
    int src, tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_IMM:
        break;
      case M_ZERO:
        tmp=loadZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadIndY(tmp);
        break;
    }
    
    src=tmp;
    tmp=regA-src-((regP & P_CARRY)!=0 ? 0 : 1); 
    if ((regP & P_DECIMAL)!=0) {
      int tmpA;
      tmpA=(regA & 0xf)-(src & 0xf)-((regP & P_CARRY)!=0 ? 0 : 1); 
      if ((tmpA & 0x10)!=0)
        tmpA=((tmpA - 6) & 0xf)| ((regA & 0xf0)-(src & 0xf0)-0x10);
      else
        tmpA=(tmpA & 0xf) | ((regA & 0xf0)-(src & 0xf0));
      if ((tmpA & 0x100)!=0)
        tmpA-=0x60;
      setCarry(tmp>=0);
      setNZ(tmp & 0xff);
      setOverflow(              
        (((regA^tmp) & 0x80)!=0) 
              && 
        (((regA^src) & 0x80)!=0)
      );
      regA=Unsigned.done((byte)tmpA);
    } else {
        setNZ(tmp & 0xff);
        setCarry(tmp>=0);
       setOverflow(              
        (((regA^tmp) & 0x80)!=0) 
              && 
        (((regA^src) & 0x80)!=0)
      );
        regA=Unsigned.done((byte)tmp);
    }
  }

  /**
   * Execute a SBX cpu undocument instruction.
   */
  public void SBX() {
    int tmp;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    tmp=(regA & regX)-tmp;
    setCarry(tmp>=0);
    regX=tmp & 0xff;
    setNZ(regX);
  }

  /**
   * Execute a SEC cpu legal instruction
   */
  public void SEC() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    setCarry(1);
  }

  /**
   * Execute a SED cpu legal instruction
   */
  public void SED() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    setDecimal(1);
  }

  /**
   * Execute a SEI cpu legal instruction
   */
  public void SEI() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    setInterrupt(1);
  }

  /**
   * Execute a SHA cpu undocument instruction
   *
   * @param type the type of addressing
   */
  public void SHA(int type) {
    int tmp=0;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ABS_Y:
        tmp=storeAbsY(tmp);
        break;
      case M_IND_Y:
        tmp=storeIndY(tmp);
        break;
    }

    store(tmp, regA & regX &
          ((tmp>>8) & 0xff)+1);   // write to effective address
    clock();                      // ++
  }

  /**
   * Execute a SHX cpu undocument instruction
   */
  public void SHX() {
    int tmp=0;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    tmp=storeAbsY(tmp);

    store(tmp, regX &
          ((tmp>>8) & 0xff)+1);   // write to effective address
    clock();                      // 5
  }

  /**
   * Execute a SHY cpu undocument instruction
   */
  public void SHY() {
    int tmp=0;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    tmp=storeAbsX(tmp);

    store(tmp, regY &
          ((tmp>>8) & 0xff)+1);   // write to effective address
    clock();                      // 5
  }

  /**
   * Execute a SHS cpu undocument instruction
   */
  public void SHS() {
    int tmp=0;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    tmp=storeAbsY(tmp);

    store(tmp, regA & regX &
          ((tmp>>8) & 0xff)+1);   // write to effective address
    regS=regA & regX;
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
    clock();                      // 5
  }

  /**
   * Execute a SLO cpu undocument instruction.
   *
   * @param type the type of addressing
   */
  public void SLO(int type) {
    int tmp, val;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadStoreAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadStoreIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadStoreIndY(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    setCarry(val & 0x80);         // do operation
    val<<=1;
    regA|=val;
    setNZ(regA);
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute a SRE cpu undocument instruction.
   *
   * @param type the type of addressing
   */
  public void SRE(int type) {
    int tmp, val;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        tmp=loadStoreZero(tmp);
        break;
      case M_ZERO_X:
        tmp=loadStoreZeroX(tmp);
        break;
      case M_ABS:
        tmp=loadStoreAbs(tmp);
        break;
      case M_ABS_X:
        tmp=loadStoreAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=loadStoreAbsY(tmp);
        break;
      case M_IND_X:
        tmp=loadStoreIndX(tmp);
        break;
      case M_IND_Y:
        tmp=loadStoreIndY(tmp);
        break;
    }

    val=tmp & 0xff;               // extract the packed information
    tmp>>=8;

    setCarry(val & 0x01);         // do operation
    val>>=1;
    regA^=val;
    setNZ(regA);
    clock();                      // ++

    store(tmp, val);              // write the new value to the effective addr.
    clock();                      // ++
  }

  /**
   * Execute a STA cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void STA(int type) {
    int tmp=0;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        break;
      case M_ZERO_X:
        tmp=storeZeroX(tmp);
        break;
      case M_ABS:
        tmp=storeAbs(tmp);
        break;
      case M_ABS_X:
        tmp=storeAbsX(tmp);
        break;
      case M_ABS_Y:
        tmp=storeAbsY(tmp);
        break;
      case M_IND_X:
        tmp=storeIndX(tmp);
        break;
      case M_IND_Y:
        tmp=storeIndY(tmp);
        break;
    }

    store(tmp, regA);             // write A to effective address
    clock();                      // ++
  }

  /**
   * Execute a STX cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void STX(int type) {
    int tmp=0;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        break;
      case M_ZERO_Y:
        tmp=storeZeroY(tmp);
        break;
      case M_ABS:
        tmp=storeAbs(tmp);
        break;
    }

    store(tmp, regX);             // write X to effective address
    clock();                      // ++
  }

  /**
   * Execute a STY cpu legal instruction.
   *
   * @param type the type of addressing
   */
  public void STY(int type) {
    int tmp=0;

    tmp=load(regPC++);            // fetch next value, increment PC
    clock();                      // 2

    switch (type) {
      case M_ZERO:
        break;
      case M_ZERO_X:
        tmp=storeZeroX(tmp);
        break;
      case M_ABS:
        tmp=storeAbs(tmp);
        break;
    }

    store(tmp, regY);             // write Y to effective address
    clock();                      // ++
  }

  /**
   * Execute a TAX cpu legal instruction
   */
  public void TAX() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regX=regA;
    setNZ(regA);
  }

  /**
   * Execute a TAY cpu legal instruction
   */
  public void TAY() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regY=regA;
    setNZ(regA);
  }

  /**
   * Execute a TSX cpu legal instruction
   */
  public void TSX() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regX=regS & 0xFF;
    setNZ(regX);
  }

  /**
   * Execute a TXA cpu legal instruction
   */
  public void TXA() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regA=regX;
    setNZ(regA);
  }

  /**
   * Execute a TXS cpu legal instruction
   */
  public void TXS() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regS=regX;
    regS&=0x1FF;                  // regS is in 100h-1FFh
    regS|=0x100;
  }

  /**
   * Execute a TYA cpu legal instruction
   */
  public void TYA() {
    load(regPC);                  // fetch next byte (and forget it)
    clock();                      // 2

    regA=regY;
    setNZ(regA);
  }

  /**
   * Execute a USBC cpu undocument instruction
   */
  public void USBC() {
    SBC(M_IMM);
  }

  /**
   * Decode the opcode and execute the operation
   */
  public void decode(){
    if ((p0>=0x00) && (p0<0x20)) {
      switch (p0) {
        case 0x10: BRANCH((regP & P_SIGN)==0);  break;      // 10: BPL $nnnn

        case 0x08: PHP();                       break;      // 08: PHP

        case 0x18: CLC();                       break;      // 18: CLC

        case 0x01: ORA(M_IND_X);                break;      // 01: ORA ($nn,X)
        case 0x05: ORA(M_ZERO);                 break;      // 05: ORA $nn
        case 0x09: ORA(M_IMM);                  break;      // 09: ORA #$nn
        case 0x0D: ORA(M_ABS);                  break;      // 0D: ORA $nnnn
        case 0x11: ORA(M_IND_Y);                break;      // 11: ORA ($nn),Y
        case 0x15: ORA(M_ZERO_X);               break;      // 15: ORA $nn,X
        case 0x19: ORA(M_ABS_Y);                break;      // 19: ORA $nnnn,Y
        case 0x1D: ORA(M_ABS_X);                break;      // 1D: ORA $nnnn,X

        case 0x06: ASL(M_ZERO);                 break;      // 06: ASL $nn
        case 0x0A: ASL(M_ACC);                  break;      // 0A: ASL A
        case 0x0E: ASL(M_ABS);                  break;      // 0E: ASL $nnnn
        case 0x16: ASL(M_ZERO_X);               break;      // 16: ASL $nn,X
        case 0x1E: ASL(M_ABS_X);                break;      // 1E: ASL $nnnn,X

        case 0x03: SLO(M_IND_X);                break;      // 03: SLO ($nn,X)
        case 0x07: SLO(M_ZERO);                 break;      // 07: SLO $nn
        case 0x0F: SLO(M_ABS);                  break;      // 0F: SLO $nnnn
        case 0x13: SLO(M_IND_Y);                break;      // 13: SLO ($nn),Y
        case 0x17: SLO(M_ZERO_X);               break;      // 17: SLO $nn,X
        case 0x1B: SLO(M_ABS_Y);                break;      // 1B: SLO $nnnn,Y
        case 0x1F: SLO(M_ABS_X);                break;      // 1F: SLO $nnnn,X

        case 0x00: BRK();                       break;      // 00: BRK

        case 0x0B: ANC();                       break;      // 0B: ANC #$nn

        case 0x04: NOOP(M_ZERO);                break;      // 04: NOOP $nn
        case 0x0C: NOOP(M_ABS);                 break;      // 0C: NOOP $nnnn
        case 0x14: NOOP(M_ZERO_X);              break;      // 14: NOOP $nn,X
        case 0x1A: NOOP(M_IMP);                 break;      // 1A: NOOP
        case 0x1C: NOOP(M_ABS_X);               break;      // 1C: NOOP $nnnn,X

        case 0x02:                                          // 02: JAM
        case 0x12: JAM();                       break;      // 12: JAM
      }
      return;
    }

    if ((p0>=0x20) && (p0<0x40)) {
      switch (p0) {
        case 0x30: BRANCH((regP & P_SIGN)!=0);  break;      // 30: BMI $nnnn

        case 0x28: PLP();                       break;      // 28: PLP

        case 0x38: SEC();                       break;      // 38: SEC

        case 0x21: AND(M_IND_X);                break;      // 21: AND ($nn,X)
        case 0x25: AND(M_ZERO);                 break;      // 25: AND $nn
        case 0x29: AND(M_IMM);                  break;      // 29: AND #$nn
        case 0x2D: AND(M_ABS);                  break;      // 2D: AND $nnnn
        case 0x31: AND(M_IND_Y);                break;      // 31: AND ($nn),Y
        case 0x35: AND(M_ZERO_X);               break;      // 35: AND $nn,X
        case 0x39: AND(M_ABS_Y);                break;      // 39: AND $nnnn,Y
        case 0x3D: AND(M_ABS_X);                break;      // 3D: AND $nnnn,X

        case 0x26: ROL(M_ZERO);                 break;      // 26: ROL $nn
        case 0x2A: ROL(M_ACC);                  break;      // 2A: ROL A
        case 0x2E: ROL(M_ABS);                  break;      // 2E: ROL $nnnn
        case 0x36: ROL(M_ZERO_X);               break;      // 36: ROL $nn,X
        case 0x3E: ROL(M_ABS_X);                break;      // 3E: ROL $nnnn,X

        case 0x20: JSR();                       break;      // 20: JSR $nnnn

        case 0x24: BIT(M_ZERO);                 break;      // 24: BIT $nn
        case 0x2C: BIT(M_ABS);                  break;      // 2C: BIT $nnnn

        case 0x23: RLA(M_IND_X);                break;      // 23: RLA ($nn,X)
        case 0x27: RLA(M_ZERO);                 break;      // 27: RLA $nn
        case 0x2F: RLA(M_ABS);                  break;      // 2F: RLA $nnnn
        case 0x33: RLA(M_IND_Y);                break;      // 33: RLA ($nn),Y
        case 0x37: RLA(M_ZERO_X);               break;      // 37: RLA $nn,X
        case 0x3B: RLA(M_ABS_Y);                break;      // 3B: RLA $nnnn,Y
        case 0x3F: RLA(M_ABS_X);                break;      // 3F: RLA $nnnn,X

        case 0x2B: ANC();                       break;      // 2B: ANC #$nn

        case 0x34: NOOP(M_ZERO_X);              break;      // 34: NOOP $nn,X
        case 0x3A: NOOP(M_IMP);                 break;      // 3A: NOOP
        case 0x3C: NOOP(M_ABS_X);               break;      // 3C: NOOP $nnnn,X

        case 0x22:                                          // 22: JAM
        case 0x32: JAM();                       break;      // 32: JAM
      }
      return;
    }

    if ((p0>=0x40) && (p0<0x60)) {
      switch (p0) {
        case 0x50: BRANCH((regP & P_OVERFLOW)==0);          // 50: BVC $nnnn
                   break;
        case 0x48: PHA();                       break;      // 48: PHA

        case 0x58: CLI();                       break;      // 58: CLI

        case 0x41: EOR(M_IND_X);                break;      // 41: EOR ($nn,X)
        case 0x45: EOR(M_ZERO);                 break;      // 45: EOR $nn
        case 0x49: EOR(M_IMM);                  break;      // 49: EOR #$nn
        case 0x4D: EOR(M_ABS);                  break;      // 4D: EOR $nnnn
        case 0x51: EOR(M_IND_Y);                break;      // 51: EOR ($nn),Y
        case 0x55: EOR(M_ZERO_X);               break;      // 55: EOR $nn,X
        case 0x59: EOR(M_ABS_Y);                break;      // 59: EOR $nnnn,Y
        case 0x5D: EOR(M_ABS_X);                break;      // 5D: EOR $nnnn,X

        case 0x46: LSR(M_ZERO);                 break;      // 46: LSR $nn
        case 0x4A: LSR(M_ACC);                  break;      // 4A: LSR A
        case 0x4E: LSR(M_ABS);                  break;      // 4E: LSR $nnnn
        case 0x56: LSR(M_ZERO_X);               break;      // 56: LSR $nn,X
        case 0x5E: LSR(M_ABS_X);                break;      // 5E: LSR $nnnn,X

        case 0x40: RTI();                       break;      // 40: RTI

        case 0x4C: JMP(M_ABS);                  break;      // 4C: JMP $nnnn

        case 0x43: SRE(M_IND_X);                break;      // 43: SRE ($nn,X)
        case 0x47: SRE(M_ZERO);                 break;      // 47: SRE $nn
        case 0x4F: SRE(M_ABS);                  break;      // 4F: SRE $nnnn
        case 0x53: SRE(M_IND_Y);                break;      // 53: SRE ($nn),Y
        case 0x57: SRE(M_ZERO_X);               break;      // 57: SRE $nn,X
        case 0x5B: SRE(M_ABS_Y);                break;      // 5B: SRE $nnnn,Y
        case 0x5F: SRE(M_ABS_X);                break;      // 5F: SRE $nnnn,X

        case 0x4B: ASR();                       break;      // 4B: ASR #$nn

        case 0x44: NOOP(M_ZERO);                break;      // 44: NOOP $nn
        case 0x54: NOOP(M_ZERO_X);              break;      // 54: NOOP $nn,X
        case 0x5A: NOOP(M_IMP);                 break;      // 5A: NOOP
        case 0x5C: NOOP(M_ABS_X);               break;      // 5C: NOOP $nnnn,X

        case 0x42:                                          // 42: JAM
        case 0x52: JAM();                       break;      // 52: JAM
      }
      return;
    }

    if ((p0>=0x60) && (p0<0x80)) {
      switch (p0) {
        case 0x70: BRANCH((regP & P_OVERFLOW)!=0);          // 70: BVS $nnnn
                   break;
        case 0x68: PLA();                       break;      // 68: PLA

        case 0x78: SEI();                       break;      // 78: SEI

        case 0x61: ADC(M_IND_X);                break;      // 61: ADC ($nn,X)
        case 0x65: ADC(M_ZERO);                 break;      // 65: ADC $nn
        case 0x69: ADC(M_IMM);                  break;      // 69: ADC #$nn
        case 0x6D: ADC(M_ABS);                  break;      // 6D: ADC $nnnn
        case 0x71: ADC(M_IND_Y);                break;      // 71: ADC ($nn),Y
        case 0x75: ADC(M_ZERO_X);               break;      // 75: ADC $nn,X
        case 0x79: ADC(M_ABS_Y);                break;      // 79: ADC $nnnn,Y
        case 0x7D: ADC(M_ABS_X);                break;      // 7D: ADC $nnnn,X

        case 0x66: ROR(M_ZERO);                 break;      // 66: ROL $nn
        case 0x6A: ROR(M_ACC);                  break;      // 6A: ROL A
        case 0x6E: ROR(M_ABS);                  break;      // 6E: ROL $nnnn
        case 0x76: ROR(M_ZERO_X);               break;      // 76: ROL $nn,X
        case 0x7E: ROR(M_ABS_X);                break;      // 7E: ROL $nnnn,X

        case 0x60: RTS();                       break;      // 60: RTS

        case 0x6C: JMP(M_IND);                  break;      // 6C: JMP ($nnnn)

        case 0x63: RRA(M_IND_X);                break;      // 63: RLA ($nn,X)
        case 0x67: RRA(M_ZERO);                 break;      // 67: RLA $nn
        case 0x6F: RRA(M_ABS);                  break;      // 6F: RLA $nnnn
        case 0x73: RRA(M_IND_Y);                break;      // 73: RLA ($nn),Y
        case 0x77: RRA(M_ZERO_X);               break;      // 77: RLA $nn,X
        case 0x7B: RRA(M_ABS_Y);                break;      // 7B: RLA $nnnn,Y
        case 0x7F: RRA(M_ABS_X);                break;      // 7F: RLA $nnnn,X

        case 0x6B: ARR();                       break;      // 6B: ARR #$nn

        case 0x64: NOOP(M_ZERO);                break;      // 64: NOOP $nn
        case 0x74: NOOP(M_ZERO_X);              break;      // 74: NOOP $nn,X
        case 0x7A: NOOP(M_IMP);                 break;      // 7A: NOOP
        case 0x7C: NOOP(M_ABS_X);               break;      // 7C: NOOP $nnnn,X

        case 0x62:                                          // 62: JAM
        case 0x72: JAM();                       break;      // 72: JAM
     }
      return;
    }

    if ((p0>=0x80) && (p0<0xA0)) {
      switch (p0) {
        case 0x90: BRANCH((regP & P_CARRY)==0); break;      // 90: BCC $nnnn

        case 0x88: DEY();                       break;      // 88: DEY

        case 0x98: TYA();                       break;      // 98: TYA

        case 0x81: STA(M_IND_X);                break;      // 81: STA ($nn,X)
        case 0x85: STA(M_ZERO);                 break;      // 85: STA $nn
        case 0x8D: STA(M_ABS);                  break;      // 8D: STA $nnnn
        case 0x91: STA(M_IND_Y);                break;      // 91: STA ($nn),Y
        case 0x95: STA(M_ZERO_X);               break;      // 95: STA $nn,X
        case 0x99: STA(M_ABS_Y);                break;      // 99: STA $nnnn,Y
        case 0x9D: STA(M_ABS_X);                break;      // 9D: STA $nnnn,X

        case 0x86: STX(M_ZERO);                 break;      // 86: STX $nn
        case 0x96: STX(M_ZERO_Y);               break;      // 96: STX $nn,Y
        case 0x8E: STX(M_ABS);                  break;      // 8E: STX $nnnn

        case 0x84: STY(M_ZERO);                 break;      // 84: STX $nn
        case 0x94: STY(M_ZERO_X);               break;      // 94: STX $nn,Y
        case 0x8C: STY(M_ABS);                  break;      // 8C: STX $nnnn

        case 0x8A: TXA();                       break;      // 8A: TXA
        case 0x9A: TXS();                       break;      // 9A: TXS

        case 0x83: SAX(M_IND_X);                break;      // 83: SAX ($nn),X
        case 0x87: SAX(M_ZERO);                 break;      // 87: SAX $nn
        case 0x8F: SAX(M_ABS);                  break;      // 8F: SAX $nnnn
        case 0x97: SAX(M_ZERO_Y);               break;      // 97: SAX $nn,Y

        case 0x8B: ANE();                       break;      // 8B: ANE #$nn

        case 0x93: SHA(M_IND_Y);                break;      // 93: SHA ($nn),Y
        case 0x9F: SHA(M_ABS_Y);                break;      // 9F: SHA $nnnn,Y

        case 0x9E: SHX();                       break;      // 9E: SHX $nnnn,X
        case 0x9C: SHY();                       break;      // 9C: SHY $nnnn,X
        case 0x9B: SHS();                       break;      // 9B: SHS $nnnn,Y

        case 0x80: NOOP(M_IMM);                 break;      // 80: NOOP #$nn
        case 0x82: NOOP(M_IMM);                 break;      // 82: NOOP #$nn
        case 0x89: NOOP(M_IMM);                 break;      // 89: NOOP #$nn

        case 0x92: JAM();                       break;      // 92: JAM
      }
      return;
    }

    if ((p0>=0xA0) && (p0<0xC0)) {
      switch (p0) {
        case 0xB0: BRANCH((regP & P_CARRY)!=0); break;      // B0: BCS $nnnn

        case 0xAA: TAX();                       break;      // AA: TAX
        case 0xA8: TAY();                       break;      // A8: TAY
        case 0xBA: TSX();                       break;      // BA: TSX

        case 0xA1: LDA(M_IND_X);                break;      // A1: LDA ($nn,X)
        case 0xA5: LDA(M_ZERO);                 break;      // A5: LDA $nn
        case 0xA9: LDA(M_IMM);                  break;      // A9: LDA #$nn
        case 0xAD: LDA(M_ABS);                  break;      // AD: LDA $nnnn
        case 0xB1: LDA(M_IND_Y);                break;      // B1: LDA ($nn),Y
        case 0xB5: LDA(M_ZERO_X);               break;      // B5: LDA $nn,X
        case 0xB9: LDA(M_ABS_Y);                break;      // B9: LDA $nnnn,Y
        case 0xBD: LDA(M_ABS_X);                break;      // BD: LDA $nnnn,X

        case 0xA2: LDX(M_IMM);                  break;      // A2: LDX #$nn
        case 0xA6: LDX(M_ZERO);                 break;      // A6: LDX $nn
        case 0xAE: LDX(M_ABS);                  break;      // AE: LDX $nnnn
        case 0xB6: LDX(M_ZERO_Y);               break;      // B6: LDX $nn,Y
        case 0xBE: LDX(M_ABS_Y);                break;      // BE: LDX $nnnn,Y

        case 0xA0: LDY(M_IMM);                  break;      // A0: LDY #$nn
        case 0xA4: LDY(M_ZERO);                 break;      // A4: LDY $nn
        case 0xAC: LDY(M_ABS);                  break;      // AC: LDY $nnnn
        case 0xB4: LDY(M_ZERO_X);               break;      // B4: LDY $nn,X
        case 0xBC: LDY(M_ABS_X);                break;      // BC: LDY $nnnn,X

        case 0xB8: CLV();                       break;      // B8: CLV

        case 0xAB: LXA();                       break;      // AB: LXA #$nn

        case 0xA3: LAX(M_IND_X);                break;      // A3: LAX ($nn,X)
        case 0xA7: LAX(M_ZERO);                 break;      // A7: LAX $nn
        case 0xAF: LAX(M_ABS);                  break;      // AF: LAX $nnnn
        case 0xB3: LAX(M_IND_Y);                break;      // B3: LAX ($nn),Y
        case 0xB7: LAX(M_ZERO_Y);               break;      // B7: LAX $nn,Y
        case 0xBF: LAX(M_ABS_Y);                break;      // BF: LAX $nnnn,Y

        case 0xBB: LAS();                       break;      // BB: LAS $nnnn,Y

        case 0xB2: JAM();                       break;      // B2: JAM
      }
      return;
    }

    if ((p0>=0xC0) && (p0<0xE0)) {
      switch (p0) {
        case 0xD0: BRANCH((regP & P_ZERO)==0);  break;     // D0: BNE $nnnn

        case 0xD8: CLD();                       break;      // D8: CLD

        case 0xC1: CMP(M_IND_X);                break;      // C1: CMP ($nn,X)
        case 0xC5: CMP(M_ZERO);                 break;      // C5: CMP $nn
        case 0xC9: CMP(M_IMM);                  break;      // C9: CMP #$nn
        case 0xCD: CMP(M_ABS);                  break;      // CD: CMP $nnnn
        case 0xD1: CMP(M_IND_Y);                break;      // D1: CMP ($nn),Y
        case 0xD5: CMP(M_ZERO_X);               break;      // D5: CMP $xx,X
        case 0xD9: CMP(M_ABS_Y);                break;      // D9: CMP $nnnn,Y
        case 0xDD: CMP(M_ABS_X);                break;      // DD: CMP $nnnn,X

        case 0xC0: CPY(M_IMM);                  break;      // C0: CMP #$nn
        case 0xC4: CPY(M_ZERO);                 break;      // C4: CMP $nn
        case 0xCC: CPY(M_ABS);                  break;      // CC: CMP $nnnn

        case 0xC3: DCP(M_IND_X);                break;      // C3: DCP ($nn,X)
        case 0xC7: DCP(M_ZERO);                 break;      // C7: DCP $nn
        case 0xCF: DCP(M_ABS);                  break;      // CF: DCP $nnnn
        case 0xD3: DCP(M_IND_Y);                break;      // D3: DCP ($nn),Y
        case 0xD7: DCP(M_ZERO_X);               break;      // D7: DCP $nn,X
        case 0xDB: DCP(M_ABS_Y);                break;      // DB: DCP $nnnn,Y
        case 0xDF: DCP(M_ABS_X);                break;      // DF: DCP $nnnn,X

        case 0xC6: DEC(M_ZERO);                 break;      // C6: DEC $nn
        case 0xCE: DEC(M_ABS);                  break;      // CE: DEC $nnnn
        case 0xD6: DEC(M_ZERO_X);               break;      // D6: DEC $nn,X
        case 0xDE: DEC(M_ABS_X);                break;      // DE: DEC $nnnn,X

        case 0xCA: DEX();                       break;      // CA: DEX

        case 0xC8: INY();                       break;      // C8: INY

        case 0xCB: SBX();                       break;      // CB: SBX #$nn

        case 0xC2: NOOP(M_IMM);                 break;      // C2: NOOP #$nn
        case 0xD4: NOOP(M_ZERO_X);              break;      // D4: NOOP $nn,X
        case 0xDA: NOOP(M_IMP);                 break;      // DA: NOOP
        case 0xDC: NOOP(M_ABS_X);               break;      // DC: NOOP $nnnn,X

        case 0xD2: JAM();                       break;      // D2: JAM
      }
      return;
    }

    if ((p0>=0xE0) && (p0<=0xFF)) {
      switch (p0) {
        case 0xF0: BRANCH((regP & P_ZERO)!=0);  break;      // F0: BEQ $nnnn

        case 0xE0: CPX(M_IMM);                  break;      // E0: CMP #$nn
        case 0xE4: CPX(M_ZERO);                 break;      // E4: CMP $nn
        case 0xEC: CPX(M_ABS);                  break;      // EC: CMP $nnnn

        case 0xE6: INC(M_ZERO);                 break;      // E6: INC $nn
        case 0xEE: INC(M_ABS);                  break;      // EE: INC $nnnn
        case 0xF6: INC(M_ZERO_X);               break;      // F6: INC $nn,X
        case 0xFE: INC(M_ABS_X);                break;      // FE: INC $nnnn,X

        case 0xE8: INX();                       break;      // E8: INX

        case 0xE3: ISB(M_IND_X);                break;      // E3: ISB ($nn,X)
        case 0xE7: ISB(M_ZERO);                 break;      // E7: ISB $nn
        case 0xEF: ISB(M_ABS);                  break;      // EF: ISB $nnnn
        case 0xF3: ISB(M_IND_Y);                break;      // F3: ISB ($nn),Y
        case 0xF7: ISB(M_ZERO_X);               break;      // F7: ISB $nn,X
        case 0xFB: ISB(M_ABS_Y);                break;      // FB: ISB $nnnn,Y
        case 0xFF: ISB(M_ABS_X);                break;      // FF: ISB $nnnn,X

        case 0xF2: JAM();                       break;      // F2: JAM

        case 0xEA: NOP();                       break;      // EA: NOP

        case 0xE2: NOOP(M_IMM);                 break;      // E2: NOOP #$nn
        case 0xF4: NOOP(M_IND_X);               break;      // F4: NOOP ($nn,X)
        case 0xFA: NOOP(M_IMP);                 break;      // FA: NOOP
        case 0xFC: NOOP(M_ABS_X);               break;      // FC: NOOP $nnnn,X

        case 0xE1: SBC(M_IND_X);                break;      // E1: SBC ($nn,X)
        case 0xE5: SBC(M_ZERO);                 break;      // E5: SBC $nn
        case 0xE9: SBC(M_IMM);                  break;      // E9: SBC #$nn
        case 0xED: SBC(M_ABS);                  break;      // ED: SBC $nnnn
        case 0xF1: SBC(M_IND_Y);                break;      // F1: SBC ($nn),Y
        case 0xF5: SBC(M_ZERO_X);               break;      // F5: SBC $nn,X
        case 0xF9: SBC(M_ABS_Y);                break;      // F9: SBC $nnnn,Y
        case 0xFD: SBC(M_ABS_X);                break;      // FD: SBC $nnnn,X

        case 0xEB: USBC();                      break;      // USBC #$nn

        case 0xF8: SED();                       break;      // F8: SED
      }
      return;
    }
  }

  /**
   * Search for pending interrupts signal and execute them.
   *
   * The NMI interrupt is valutated before the IRQ. So if an IRQ and NMI overlap
   * each other (one interrupt occurs before fetching the interrupt vector for
   * the other interrupt), the NMI is execute first, and the IRQ after the first
   * instruction of NMI handler.
   * Note. the IRQ sequence is interrupted before 4 cycle if become a NMI.
   */
  public void interrupt() {
    if (nmiPending) {             // do nmi

      ///load(regPC++);              // read next byte (and forget it), inc. PC
      ///clock();                    // 2
      load(regPC++);              // read next byte opcode as usual, inc. PC
      clock();                    // 1
      load(regPC--);              // read next byte but dec PC
      clock();                    // 2
      ///      

      setBreak(1);                // push PCH on stack (with B flag set), dec. S
      store(regS--, regPC>>8);
      regS&=0x1FF;                // regS is in 100h-1FFh
      regS|=0x100;
      clock();                    // 3

      store(regS--, regPC & 0xff);// push PCL on stack, decrement S
      regS&=0x1FF;                // regS is in 100h-1FFh
      regS|=0x100;
      clock();                    // 4

      store(regS--, regP&~P_BREAK);        // push P on stack, decrement S
      regS&=0x1FF;                // regS is in 100h-1FFh
      regS|=0x100;
      clock();                    // 5
      
      regPC=(regPC & 0xff00)+     // fetch PCL
            load(0xfffa);         // 6
      clock();

      regPC=(regPC & 0xff)+       // fetch PCH
            (load(0xfffb)<<8);
      clock();                    // 7

      //synchronized (this) {       // disable NMI signal
        nmiPending=false;
      //}
      return;
    }

    if ((irqPending) &&
       ((regP & P_INTERRUPT)!=P_INTERRUPT)) {

      ///load(regPC++);              // read next byte (and forget it), inc. PC
      ///clock();                    // 2
      load(regPC++);              // read next byte opcode as usual, inc. PC
      clock();                    // 1
      load(regPC--);              // read next byte but dec PC
      clock();                    // 2
      ///           

      store(regS--, regPC>>8);    // push PCH on stack, dec. S
      regS&=0x1FF;                // regS is in 100h-1FFh
      regS|=0x100;
      clock();                    // 3

      store(regS--, regPC & 0xff);// push PCL on stack, decrement S
      regS&=0x1FF;                // regS is in 100h-1FFh
      regS|=0x100;
      clock();                    // 4

      if (nmiPending) {           // skip IRQ
        store(regS--, regP&~P_BREAK);      // push P on stack, decrement S
        regS&=0x1FF;              // regS is in 100h-1FFh
        regS|=0x100;
        clock();                  // 5

        regPC=(regPC & 0xff00)+   // fetch PCL
              load(0xfffa);       // 6
        clock();

        regPC=(regPC & 0xff)+     // fetch PCH
              (load(0xfffb)<<8);
        clock();                  // 7

        //synchronized (this) {     // disable NMI signal
          nmiPending=false;
        //}
        return;
      } else {
          store(regS--,           // push P (with BREAK at 0) on stack
                regP&~P_BREAK);   // decrement S 
          regS&=0x1FF;            // regS is in 100h-1FFh
          regS|=0x100;
          clock();                // 5                  

          setInterrupt(1);
          regPC=(regPC & 0xff00)+ // fetch PCL
                load(0xfffe);     // 6
          clock();

          regPC=(regPC & 0xff)+   // fetch PCH
                (load(0xffff)<<8);
          clock();                // 7

          //synchronized (this) {   // disable IRQ signal
          irqPending=false; 
          //}
          return;
        }
    }
  }

  /**
   * Performs the body of cpu emulation.
   * The emulation don't start if the io port are not set up with a correct
   * handler.
   */
  public void run() {
    monitor.opNotify();                         // notify that we will use it
    
    while(ioPort==null) {
      yield();
    }
   
    regA=0;
    regX=0;
    regY=0;
    regP=P_UNUSED | P_BREAK;
    regS=0x1FF;
    regPC=0xFCE2;

    while(true) {
      if (sigRESET==1) {
        //reset();
      }
      
      // do nothing until the bus is available
      while (!bus.isInitialized()) { // there's a bus?
        yield(); // no, attend power
      }

      if (!power) {
        regA=0;
        regX=0;
        regY=0;
        regP=0;
        regS=0x1FF;
        regPC=0xFFFC;
        // attend that power returned

        while (!power) {
          yield();                              // give mutex to other threads
        }
        
              // do nothing until the bus is available
      while (!bus.isInitialized()) { // there's a bus?
        yield(); // no, attend power
      }

        regPC=bus.load(regPC, view, sigAEC)+
             (bus.load(regPC+1, view, sigAEC)<<8);
        clock();                                // attend synchronization
      }
         //System.out.println(Integer.toHexString(regPC));
      interrupt();                  // search for interrupt or reset signal
      p0=load(regPC++);             // read opcode, increment pc
      regPC&=0xFFFF;                // mask PC

      clock();                      // 1
      decode();                     // decode and execute the instruction
    }
  }

  /**
   * Notify a signal to the cpu
   *
   * @param type the type of signal (S_RESET, S_IRQ, S_NMI, ...)
   * @param value the value of the signal (0/1)
   */
  public void notifySignal(int type, int value) {
    switch (type) {
      case S_IRQ:                                // interrupt IRQ
        sigIRQ=value;
        if (value==0) irqPending=false;
        break;
      case S_NMI:                                // interrupt NMI
        sigNMI=value;
        break;
      case S_RDY:                                // cpu READY
        sigRDY=value;
        break;
      case S_AEC:                                // Bus tri-state
        sigAEC=value;
        break;
      case S_RESET:                              // cpu Reset
        sigRESET=value;
        break;
      default:
        System.err.println("ERROR: an invalid "+type+
                           " signal was notify to M6510");
    }
  }

  /**
   * Power on the electronic component
   */
  public void powerOn() {
    power=true;     // power is on
  }

  /**
   * Power off the electronic component
   */
  public void powerOff() {
    power=false;    // power is off
  }
}






