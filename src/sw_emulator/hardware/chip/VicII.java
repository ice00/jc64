/**
 * @(#)VicII.java 1999/10/16
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

import sw_emulator.util.Monitor;
import sw_emulator.hardware.powered;
import sw_emulator.hardware.signaller;
import sw_emulator.hardware.bus.readableBus;
import sw_emulator.hardware.bus.writeableBus;
import sw_emulator.hardware.bus.Bus;
import sw_emulator.hardware.memory.dinamic;
import sw_emulator.hardware.io.VicII_IO;
import sw_emulator.hardware.device.raster;
import sw_emulator.util.FlipFlop;

/**
 * Emulate the Vic II chip.
 * The Vic can access to 16Kbyte of memory at time, but his data bus is of 12
 * bits; the up 4 bits is for color.
 *
 * Note: the variables and methods naming is like that are used in Vic Article,
 *       and the comment in sources between "..." are taken from Vic Article too.
 *
 * Features not implemented as in real VIC:
 * <ul>
 *  <li>the value of AEC signal is equal to the expected only in fi2 of the
 *      cycle. This simplify the work of thread sinchronization, because if BA
 *      is high, AEC goes from low to high, but when it is low in this phase,
 *      no other chips are using (actively) the bus (in the source code a
 *      comment with AEC!! show this incompatibily).</li>
 * </ul>
 *
 * Reference:
 *  Vic Article of Christian Bauer
 *  Pal Timing of Marko M"akel"a
 *  Missing Cycle of Pasi Ojala
 *
 * @author Ice
 * @version 1.00 16/10/1999
 */
public abstract class VicII extends Thread implements powered,
                                               readableBus, writeableBus{

  // idle state if all value different from below
  // note: for semplicity it's assumed that bit 3=1 means idle state
  public static final int VIC_STANDARD_TEXT     = 0x00; // standard text mode
  public static final int VIC_MULTICOLOR_TEXT   = 0x01; // multicolor text mode
  public static final int VIC_STANDARD_BITMAP   = 0x02; // standard bitmap mode
  public static final int VIC_MULTICOLOR_BITMAP = 0x03; // multicolor bitmap
  public static final int VIC_ECM_TEXT          = 0x04; // ECM text mode
  public static final int VIC_INVALID_TEXT      = 0x05; // invalid text mode
  public static final int VIC_INVALID_BITMAP1   = 0x06; // invalid bitmap mode 1
  public static final int VIC_INVALID_BITMAP2   = 0x07; // invalid bitmap mode 2

  public static final int RASTER_UP = 0x30;   // begin of interval for bad line
  public static final int RASTER_DW = 0xF7;   // end of interval for bad line

  public static final int LEFT0   = 0x1F;     // left comparison for CSEL=0
  public static final int LEFT1   = 0x18;     // left comparison for CSEL=1
  public static final int RIGHT0  = 0x14F;    // right comparison for CSEL=0
  public static final int RIGHT1  = 0x158;    // right comparison for CSEL=1
  public static final int TOP0    = 0x37;     // top comparison for RSEL=0
  public static final int TOP1    = 0x33;     // top comparison for RSEL=1
  public static final int BOTTOM0 = 0xF7;     // bottom comparison for RSEL=0
  public static final int BOTTOM1 = 0xFB;     // bottom comparison for RSEL=1

  public static final int[] LEFT   = {LEFT0,   LEFT1};
  public static final int[] RIGHT  = {RIGHT0,  RIGHT1};
  public static final int[] TOP    = {TOP0,    TOP1};
  public static final int[] BOTTOM = {BOTTOM0, BOTTOM1};

  public static final byte S_BA  = signaller.S_BA;
  public static final byte S_AEC = signaller.S_AEC;
  public static final byte S_IRQ = signaller.S_IRQ;

  public static final int BACKGR = 0;  // background priority
  public static final int FOREGR = 1;  // foreground priority

  /** First Vertical blanking line (line not visible) */
  public int firstVblankLine;

  /** Last Vertical blancking line (line not visible) */
  public int lastVblankLine;

  /** First Visible X Coordinate */
  public int firstVisXCoo;

  /** Last Visible X Coordinate */
  public int lastVisXCoo;

  /** First X coordinate */
  public int maxCycle;

  /** First X coordinate */
  public int firstXCoo;

  /** Last X position */
  public int lastXPos;

  /** Number of (raster) lines */
  public int linesNumber;

  /** External monitor of the dot clock */
  public Monitor extMonitor;

  /** Internal monitor for generate clock at 1/8 of dot clock */
  public Monitor intMonitor=new Monitor("VIC II fi clock");

  /** Actual VIC cycle */
  protected int cycle;

  /** State of bad line (true means this is a bad line) */
  protected boolean badLine;

  /** Allow the generation of bad line (if true) */
  protected boolean allowBadLine;

  /** Used for calculating <code>allowBadLine</code> */
  private boolean den=false;

  /** The state of power */
  private boolean power=false;

  /** The bus view of the Vic */
  protected int view;

  /** The bus where the Vic can read information (we not use write operation) */
  protected Bus bus;

  /** Vic IO signals */
  protected VicII_IO io;

  /** A sequence of dinamic devices that need refresh */
  public dinamic[] devicesToRefresh;

  /** The color number to display */
  public int grColor;

  /** The background/foreground priority of the pixel */
  public int grPriority;

  /** The TV where Vic send pixels output */
  public raster tv;


  // variables for sprites

  /** Mobs X coordinate (bits 0 to 8) */
  public int[] MxX=new int[8];

  /** Mobs Y coordinate (bits 0 to 7) */
  public int[] MxY=new int[8];

  /** Memory pointer of srites */
  public int[] MPx=new int[8];

  /** MOB Data Counter (bit 0 to 5) */
  public int[] MCx=new int[8];

  /** MOB Data Counter Base (bit 0 to 5) */
  public int[] MCBASEx=new int[8];

  /** MOB Shift Register (bit 0 to 23) */
  public int[] mobSequencer=new int[8];

  /** Expansion Y flip flop */
  public FlipFlop[] expansion=new FlipFlop[] {
    new FlipFlop(), new FlipFlop(), new FlipFlop(), new FlipFlop(),
    new FlipFlop(), new FlipFlop(), new FlipFlop(), new FlipFlop()
  };

  /** DMA state of a sprite (true means must read data)  */
  public boolean[] DMAx=new boolean[8];

  /** Display of sprite (true means must be displayed) */
  public boolean[] display=new boolean[8];

  /**
   * 24 Pixels of a sprite counter
   * Note: this is used to display 24 pixels in sequence when rule 6 (of
   * section 3.8.1) match. This must improve speed (but must be checked if
   * it's 100 real Vic operation.
   */
  public int[] pixels={-1,-1,-1,-1,-1,-1,-1,-1};

  /** Sequencer counter for sprite x double features */
  private int[] pixelsCount=new int[8];

  /** Color sprite (bits 0 to 3) */
  public byte[] MxC=new byte[8];

  /** Sprite enabled (bit 0); */
  public int[] MxE=new int[8];

  /** Sprite X expansion (bit 0) */
  public int[] MxXE=new int[8];

  /** Sprite Y expansion (bit 0) */
  public int[] MxYE=new int[8];

  /** Sprite-sprite collision (bit 0) */
  public int[] MxM=new int[8];

  /** Sprite-data collision (bit 0) */
  public int[] MxD=new int[8];

  /** Sprite multicolor (bit 0) */
  public int[] MxMC=new int[8];

  /** Sprite-data priority (bit 0) */
  public int[] MxDP=new int[8];

  /** The color number to display of sprite */
  public int[] spColor=new int[8];

  /**
   * The background/foreground priority of the sprite pixel
   * Note: this (and related instructions) should be removed, because probably
   * not actively used
   */
  public int[] spPriority=new int[8];


  /** Actual raster position (bits 0 to 8) */
  public int raster;

  /** Actual raster position to compare for making a interrupt (bits 0 to 8) */
  public int rasterCompare=-1;                     // -1 not compare

  /** X position of raster */
  public int rasterX;

  /** Light pen X latched value (bits 0 to 9) */
  public int LPX=0;

  /** Light oen Y latched value (bits 0 to 8) */
  public int LPY=0;

  /** Border color (bits 0 to 3) */
  public byte EC;

  /** Background color 0 (bits 0 to 3) */
  public byte B0C;

  /** Background color 1 (bits 0 to 3) */
  public byte B1C;

  /** Background color 2 (bits 0 to 3) */
  public byte B2C;

  /** Background color 3 (bits 0 to 3) */
  public byte B3C;

  /** Sprite multicolor 0 (bits 0 to 3) */
  public byte MM0;

  /** Sprite multicolor 1 (bits 0 to 3) */
  public byte MM1;

  /** Actual state of the vic (text, bitmap, idle ...) */
  public int vicState;

  /** State of ECM field (bit 0) */
  public int ECM;

  /** State of BMM field (bit 0) */
  public int BMM;

  /** State of MCM field (bit 0) */
  public int MCM;

  /** State of DEN field (bit 0) */
  public int DEN;

  /** State of RES field (bit 0) */
  public int RES;

  /** State of RSEL field (bit 0) */
  public int RSEL;

  /** State of CSEL field (bit 0) */
  public int CSEL;

  /** State of IRQ field (bit 0) */
  public int IRQ;

  /** State of Interrupt LightPen (bit 0) */
  public int ILP;

  /** State of Interrupt Mob Mob Collision (bit 0) */
  public int IMMC;

  /** State of Interrupt Mob Bitmap Collision (bit 0) */
  public int IMBC;

  /** State of Interrupt Raster (bit 0) */
  public int IRST;

  /** State of Interrupt Enable LightPen (bit 0) */
  public int ELP;

  /** State of Interrupt Enable Mob Mob Collision (bit 0) */
  public int EMMC;

  /** State of Interrupt Enable Mob Bitmap Collision (bit 0) */
  public int EMBC;

  /** State of Interrupt Enable Raster (bit 0) */
  public int ERST;

  /**
   * Graphics Data Shift Register (bit 0 to 7)
   * Note: because the register may be reload delayed by 0..7 pixel, 16 bits
   * are used for making this delay with speed up porpoise.
   */
  public int gdSequencer;

  /** Scroll X register (bit 0 to 2) */
  public int xScroll;

  /** Scroll Y register (bit 0 to 2) */
  public int yScroll;

  /** Vic Counter (bits 0 to 9) */
  public int VC;

  /** Vic Counter Base (bits 0 to 9) */
  public int VCBase;

  /** Vic Memory pointer (bits 0 to 3) */
  public int VM;

  /** Video matrix line index */
  public int VMLI;

  /** Base Character pointer (bits 0 to 2) */
  public int CB;

  /** Row Conter (bits 0 to 2) */
  public int RC;

  /** Refresh lile counter (bits 0 to 7) */
  public int REF;

  /** Buffer for reading data of matrix and color line (bits 0 to 11) */
  protected int[] videoMatrixColor=new int[40];

  /** Internal buffer (for sequencer) of matrix and color line (12 bits) */
  protected int videoBuffer;

  /** Main Border flip flop */
  protected FlipFlop mainBorder=new FlipFlop();

  /** Vertical Border flip flop */
  protected FlipFlop verticalBorder= new FlipFlop();

  /** BA signal 0/1 */
  public int BA;

  /** Count the number of cycle with BA low */
  public int countBA;

  /** AEC signal 0/1 */
  public int AEC;

  /**
   * Construct a VicII chip.
   *
   * @param extMonitor the external clock monitor
   * @param bus the bus
   * @param view the vic bus view
   * @param io the vic io
   * @param devicesToRefresh the devices to be refreshed
   * @param tv the raster TV attached to the Vic output
   */
  public VicII(Monitor extMonitor, Bus bus, int view, VicII_IO io,
                            dinamic[] devicesToRefresh, raster tv) {
    this.extMonitor=extMonitor;
    this.bus=bus;
    this.view=view;
    this.io=io;
    this.devicesToRefresh=devicesToRefresh;
    this.tv=tv;
    setName("VicII");               // use this name for the thread
    start();
  }

  /**
   * Set up the connection of IO with the external.
   * The vic emulation is not started if this value is null equal.
   *
   * @param io the external connection
   */
  public void setIO(VicII_IO io) {
    this.io=io;
  }

  /**
   * Execute the cycles of VIC according to external dot clock.
   * The power state is looked only 1 time over 8.
   */
  public void run() {
    int tick=0;                                    // number of dot clock ticks
    extMonitor.opNotify();                         // notify that we will use it

    cycle=1;                                       // this is supposed
    rasterX=firstXCoo+4;                           // +4 is the pixels needed
                                                   // for cycle=1

    // do nothing until io connection are inserted
    while (io==null) {
      yield();
    }

    while (true) {

      // do nothig until the power is arrived   
      while (!power) {                             // there's power ?
        yield();                                   // no, attend power
      }
      
      // do nothing until the bus is available
      while (!bus.isInitialized())  {              // there's a bus?
        yield();                                   // no, attend power
      }
      


      extMonitor.opWait();                         // attend dot clock tick
      dotClock();                                  // execute dot clock operat.
      switch (++tick) {
        case 1:
          fi0low();                                // execute oper. in 1° phase
          break;
        case 5:
          fi0high();                               // execute oper. in 2° phase

          
          // attend that the connected circuits have finish
          while (!intMonitor.isFinish()) {
            yield();
          }          
          intMonitor.opSignal();                   // clock tick at 1/8 of dot
          break;
        case 8:
          cycle++;                                 // increment cycle counter
          if (cycle>maxCycle)                      // are at the last cycle
            cycle=1;                               // reset cycle counter

          tick=0;                                  // reset tick counter
          break;
      }
    }
  }

  /**
   * Write a byte to the bus at specific address location.
   *
   * @param addr the address location
   * @param value the byte value
   */
  public void write(int addr, byte value) {
    int i;

    addr&=0x3f;                                      // musk address
    switch (addr) {
      case 0x00:                                     // X coord. sprite 0
      case 0x02:                                     // X coord. sprite 1
      case 0x04:                                     // X coord. sprite 2
      case 0x06:                                     // X coord. sprite 3
      case 0x08:                                     // X coord. sprite 4
      case 0x0A:                                     // X coord. sprite 5
      case 0x0C:                                     // X coord. sprite 6
      case 0x0E:                                     // X coord. sprite 7
        MxX[addr>>1]=(MxX[addr>>1] & 0x100)+(value & 0xFF);
        break;
      case 0x01:                                     // Y coord. sprite 0
      case 0x03:                                     // Y coord. sprite 1
      case 0x05:                                     // Y coord. sprite 2
      case 0x07:                                     // Y coord. sprite 3
      case 0x09:                                     // Y coord. sprite 4
      case 0x0B:                                     // Y coord. sprite 5
      case 0x0D:                                     // Y coord. sprite 6
      case 0x0F:                                     // Y coord. sprite 7
        MxY[(addr-1)>>1]=(value & 0xFF);
        break;
      case 0x10:                                     // MSBs of X coord.
        MxX[0]=(MxX[0] & 0xFF)+(value & 0x100);
        MxX[1]=(MxX[1] & 0xFF)+(value & 0x100);
        MxX[2]=(MxX[2] & 0xFF)+(value & 0x100);
        MxX[3]=(MxX[3] & 0xFF)+(value & 0x100);
        MxX[4]=(MxX[4] & 0xFF)+(value & 0x100);
        MxX[5]=(MxX[5] & 0xFF)+(value & 0x100);
        MxX[6]=(MxX[6] & 0xFF)+(value & 0x100);
        MxX[7]=(MxX[7] & 0xFF)+(value & 0x100);
        break;
      case 0x11:                                     // Control register 1
        rasterCompare=(rasterCompare & 0xFF)+((value<<1)& 0x100);
        ECM=(value>>6)& 0x01;
        BMM=(value>>5)& 0x01;
        DEN=(value>>4)& 0x01;
        RSEL=(value>>3)& 0x01;
        yScroll=value & 0x07;
        // calculates new vic state
        vicState=(vicState & 0x09)+(ECM<<2)+(BMM<<1);
        break;
      case 0x12:                                     // Raster counter
        rasterCompare=(rasterCompare & 0x100)+(value & 0xFF);
        break;
      case 0x13:                                     // Light pen X
      case 0x14:                                     // Light pen Y
        break;   // do nothing, write access ignored
      case 0x15:                                     // Sprite enabled
        MxE[0]=value & 0x01;
        MxE[1]=(value>>=1)& 0x01;
        MxE[2]=(value>>=1)& 0x01;
        MxE[3]=(value>>=1)& 0x01;
        MxE[4]=(value>>=1)& 0x01;
        MxE[5]=(value>>=1)& 0x01;
        MxE[6]=(value>>=1)& 0x01;
        MxE[7]=(value>>=1)& 0x01;
        break;
      case 0x16:                                     // Control register 2
        RES=(value & 0x20)>>5;
        MCM=(value & 0x10)>>4;
        CSEL=(value & 0x08)>>3;
        xScroll=value & 0x07;
        // calculates new vic state
        vicState=(vicState & 0x0E)+MCM;
        break;
      case 0x17:                                     // Sprite y expansion
        for (i=0; i<8; i++) {
          MxYE[i]=value & 0x01;
          value>>=1;

          // section 3.8.1. Memory access and display
          // "1. The expansion flip flop is set as long as the bit in MxYE in
          // register $d017 corresponding to the sprite is cleared".
          if (MxYE[i]==0)
            expansion[i].set();                      // set exp. flip flop
        }
        break;
      case 0x18:                                     // Memory pointers
        VM=(value & 0xF0)>>4;
        CB=(value & 0x0E)>>1;
        break;
      case 0x19:                                     // Interrupt register
        if ((value & 0x01)==1) IRST=0;               // ack. Raster Int.
        if ((value & 0x02)==2) IMBC=0;               // ack. Mob Bit. Int.
        if ((value & 0x04)==4) IMMC=0;               // ack. Mob Mob Int.
        if ((value & 0x08)==8) ILP=0;                // ack. LightPen Int.

        if ((IRST+IMBC+IMMC+ILP==0) && (IRQ==1)) {   // are there acks?
          IRQ=0;                                     // end of IRQ
          io.notifySignal(S_IRQ, 1);                 // notify end of IRQ
        }
        break;
      case 0x1A:                                     // Interrupt enabled
        ERST=value & 0x01;                           // Enable Raster Int.
        EMBC=(value>>=1)& 0x01;                      // Enable Mob Bit. Int.
        EMMC=(value>>=1)& 0x01;                      // Enable Mob Mob Int.
        ELP=(value>>=1)& 0x01;                       // Enable LightPen Int.

        if (((ERST==1) && (IRST==1)) ||
            ((EMBC==1) && (IMBC==1)) ||
            ((EMMC==1) && (IMBC==1)) ||
            ((ELP==1) && (ILP==1))) {
          IRQ=1;                                     // start of IRQ
          io.notifySignal(S_IRQ, 0);                 // notify start of IRQ
        }
        break;
      case 0x1B:                                     // Sprite data priority
        for (i=0; i<8; i++) {
          MxDP[i]=value & 0x01;
          value>>=1;
        }
        break;
      case 0x1C:                                     // Sprite multicolor
        for (i=0; i<8; i++) {
          MxMC[i]=value & 0x01;
          value>>=1;
        }
        break;
      case 0x1D:                                     // Sprite x expansion
        for (i=0; i<8; i++) {
          MxXE[i]=value & 0x01;
          value>>=1;
        }
        break;
      case 0x1E:                                     // Sprite-sprite collision
      case 0x1F:                                     // Sprite-data collision
        // do nothing, write access ignored
        break;
      case 0x20:                                     // Border color
        EC=(byte)(value & 0x0F);
        break;
      case 0x21:                                     // Background color 0
        B0C=(byte)(value & 0x0F);
        break;
      case 0x22:                                     // Background color 1
        B1C=(byte)(value & 0x0F);
        break;
      case 0x23:                                     // Background color 2
        B2C=(byte)(value & 0x0F);
        break;
      case 0x24:                                     // Background color 3
        B3C=(byte)(value & 0x0F);
        break;
      case 0x25:                                     // Sprite multicolor 0
        MM0=(byte)(value & 0x0F);
        break;
      case 0x26:                                     // Sprite multicolor 1
        MM1=(byte)(value & 0x0F);
        break;
      case 0x27:                                     // Color sprite 0
      case 0x28:                                     // Color sprite 1
      case 0x29:                                     // Color sprite 2
      case 0x2A:                                     // Color sprite 3
      case 0x2B:                                     // Color sprite 4
      case 0x2C:                                     // Color sprite 5
      case 0x2D:                                     // Color sprite 6
      case 0x2E:                                     // Color sprite 7
        MxC[addr-0x27]=(byte)(value & 0x0F);
        break;
      default:
        // do nothing, write ignored
    }
  }

  /**
   * Read a byte from the bus at specific address location.
   *
   * @param addr the address location
   * @return the readed byte
   */
  public byte read(int addr) {
    int i;    // local cycle
    int tmp;  // temp value

    addr&=0x3f;                                      // musk addr
    switch (addr) {
      case 0x00:                                     // X coord. sprite 0
      case 0x02:                                     // X coord. sprite 1
      case 0x04:                                     // X coord. sprite 2
      case 0x06:                                     // X coord. sprite 3
      case 0x08:                                     // X coord. sprite 4
      case 0x0A:                                     // X coord. sprite 5
      case 0x0C:                                     // X coord. sprite 6
      case 0x0E:                                     // X coord. sprite 7
        return (byte)MxX[addr>>1];
      case 0x01:                                     // Y coord. sprite 0
      case 0x03:                                     // Y coord. sprite 1
      case 0x05:                                     // Y coord. sprite 2
      case 0x07:                                     // Y coord. sprite 3
      case 0x09:                                     // Y coord. sprite 4
      case 0x0B:                                     // Y coord. sprite 5
      case 0x0D:                                     // Y coord. sprite 6
      case 0x0F:                                     // Y coord. sprite 7
        return (byte)MxY[(addr-1)>>1];
      case 0x10:                                     // MSBs of X coord.
        return (byte)(
                (MxX[0]>>8)+
               ((MxX[1]>>7)& 0x02)+
               ((MxX[2]>>6)& 0x04)+
               ((MxX[3]>>5)& 0x08)+
               ((MxX[4]>>4)& 0x10)+
               ((MxX[5]>>3)& 0x20)+
               ((MxX[6]>>2)& 0x40)+
               ((MxX[7]>>1)& 0x80));
      case 0x11:                                     // Control register 1
        return (byte)(
                ((raster>>1)& 0x80)+
                (ECM<<6)+
                (BMM<<5)+
                (DEN<<4)+
                (RSEL<<3)+
                yScroll);
      case 0x12:                                     // Raster counter
        return (byte)raster;
      case 0x13:                                     // Light pen X
        return (byte)(LPX>>1);                       // Gives only 8 bits of 9
      case 0x14:                                     // Ligth pen Y
        return (byte)LPY;
      case 0x15:                                     // Sprite enabled
        return (byte)(
                 MxE[0]+
                (MxE[1]<<1)+
                (MxE[2]<<2)+
                (MxE[3]<<3)+
                (MxE[4]<<4)+
                (MxE[5]<<5)+
                (MxE[6]<<6)+
                (MxE[7]<<7));
      case 0x16:                                     // Control register 2
        return (byte)(                               // 2 bits not connected
               0xC0+
               (RES<<5)+
               (MCM<<4)+
               (CSEL<<3)+
               xScroll);
      case 0x17:                                     // Sprite y expansion
        return (byte)(
                 MxYE[0]+
                (MxYE[1]<<1)+
                (MxYE[2]<<2)+
                (MxYE[3]<<3)+
                (MxYE[4]<<4)+
                (MxYE[5]<<5)+
                (MxYE[6]<<6)+
                (MxYE[7]<<7));
      case 0x18:                                     // Memory pointers
        return (byte)(
               (VM<<4)+
               (CB<<1)+
               1);                                   // 1 bit not connected
      case 0x19:                                     // Interrupt register
        return (byte) (
               IRST+
              (IMBC<<1)+
              (IMMC<<2)+
              (ILP<<3)+
              (IRQ<<7));
      case 0x1A:                                     // Interrupt enabled
        return (byte)(
                ERST+
               (EMBC<<1)+
               (EMMC<<2)+
               (ELP<<3));
      case 0x1B:                                     // Sprite data priority
        return (byte)(
                 MxDP[0]+
                (MxDP[1]<<1)+
                (MxDP[2]<<2)+
                (MxDP[3]<<3)+
                (MxDP[4]<<4)+
                (MxDP[5]<<5)+
                (MxDP[6]<<6)+
                (MxDP[7]<<7));
      case 0x1C:                                     // Sprite multicolor
        return (byte)(
                 MxMC[0]+
                (MxMC[1]<<1)+
                (MxMC[2]<<2)+
                (MxMC[3]<<3)+
                (MxMC[4]<<4)+
                (MxMC[5]<<5)+
                (MxMC[6]<<6)+
                (MxMC[7]<<7));
      case 0x1D:                                     // Sprite X expansion
        return (byte)(
                 MxXE[0]+
                (MxXE[1]<<1)+
                (MxXE[2]<<2)+
                (MxXE[3]<<3)+
                (MxXE[4]<<4)+
                (MxXE[5]<<5)+
                (MxXE[6]<<6)+
                (MxXE[7]<<7));
      case 0x1E:                                     // Sprite-sprite collision
        tmp=0;
        for (i=0; i<8; i++) {
          tmp+=(MxM[i]<<i);
          MxM[i]=0;                                  // register is cleared
        }
        return (byte)tmp;
      case 0x1F:                                     // Sprite-data collision
        tmp=0;
        for (i=0; i<8; i++) {
          tmp+=(MxD[i]<<i);
          MxM[i]=0;                                  // register is cleared
        }
        return (byte)tmp;
      case 0x20:                                     // Border color
        return (byte)((EC & 0x0F)| 0xF0);
      case 0x21:                                     // Background color 0
        return (byte)((B0C & 0x0F)| 0xF0);
      case 0x22:                                     // Background color 1
        return (byte)((B1C & 0x0F)| 0xF0);
      case 0x23:                                     // Background color 2
        return (byte)((B2C & 0x0F)| 0xF0);
      case 0x24:                                     // Background color 3
        return (byte)((B3C & 0x0F)| 0xF0);
      case 0x25:                                     // Sprite multicolor 0
        return (byte)((MM0 & 0x0F)| 0xF0);
      case 0x26:                                     // Sprite multicolor 1
        return (byte)((MM1 & 0x0F)| 0xF0);
      case 0x27:                                     // Color sprite 0
      case 0x28:                                     // Color sprite 1
      case 0x29:                                     // Color sprite 2
      case 0x2A:                                     // Color sprite 3
      case 0x2B:                                     // Color sprite 4
      case 0x2C:                                     // Color sprite 5
      case 0x2D:                                     // Color sprite 6
      case 0x2E:                                     // Color sprite 7
        return (byte)((MxC[addr-0x27] & 0x0F)| 0xF0);
      default :
        return (byte)0xFF;                           // not connected
    }
  }

  /**
   * Performs a c-accesses.
   * It reads 12 bits: 8 for data, 4 for color. The address is the same for all
   * the graphics mode:
   * <ul>
   *  <li>VM13 VM12 VM11 VM10 VC9 VC8 VC7 VC6 VC5 VC4 VC3 VC2 VC1 VC0</li>
   * </ul>
   *
   * @return 12 bits of readed data from bus
   */
  protected int c_access() {
    return bus.load((VM<<10)+VC, view, AEC);
  }

  /**
   * Performs a g-accesses.
   * It read 12 bits: 8 for data, 4 not used. The address depends by the
   * mode graphics:
   * <ul>
   *  <li>CB13 CB12 CB11 D7 D6 D5 D4 D3 D2 D1 D0 RC2 RC1 RC0 for standard and
   *      multicolor mode</li>
   *  <li>CB13 VC9 VC8 VC7 VC6 Vc5 VC4 VC3 VC2 VC1 VC0 RC2 RC1 RC0 for
   *      standard and multicolor bitmap</li>
   *  <li>CB13 CB12 CB11 0 0 D5 D4 D3 D2 D1 D0 RC2 RC1 RC0 for ECM and
   *      invalid text mode</li>
   *  <li>CB13 VC9 VC8 0 0 Vc5 VC4 VC3 VC2 VC1 VC0 RC2 RC1 RC0 for invalid
   *      bitmaps</li>
   * </ul>
   *
   * @return 12 bits of readed data from bus
   */
  protected int g_access() {
    switch (vicState) {
      case VIC_STANDARD_TEXT:
      case VIC_MULTICOLOR_TEXT:
        return bus.load((CB<<11)+
                         ((videoBuffer & 0xFF)<<3)+
                         RC, view, 0);               // AEC!!
      case VIC_STANDARD_BITMAP:
      case VIC_MULTICOLOR_BITMAP:
        return bus.load( ((CB & 0x04)<<11)+
                         (VC<<3)+
                         RC, view, 0);               // AEC!!
      case VIC_ECM_TEXT:
      case VIC_INVALID_TEXT:
        return bus.load((CB<<11)+
                        ((videoBuffer & 0x3F)<<3)+
                        RC, view, 0);                // AEC!!
      case VIC_INVALID_BITMAP1:
      case VIC_INVALID_BITMAP2:
        return bus.load( ((CB & 0x04)<<11)+
                         ((VC & 0x19F)<<3)+
                         RC, view, 0);               // AEC!!
      default:                                       // idle state
       if (ECM==0) return bus.load(0x3FFF, view, 0); // AEC!!
         else return bus.load(0x39FF, view, 0);      // AEC!!
    }
  }

  /**
   * Performs a r-access.
   * Referesh dinamic devices attached to the VIC at address:
   * <ul>
   *  <li>1 1 1 1 1 1 REF7 REF6 REF5 REF4 REF3 REF2 REF1 REF0</li>
   * </ul>
   */
  protected void r_access() {
    int i;

    for (i=0; i<devicesToRefresh.length; i++)
      devicesToRefresh[i].refreshLine(REF);
  }

  /**
   * Performs a i-access.
   * Read always from address 0x3FFF
   */
  protected int i_access() {
    return bus.load(0x3FFF, view, 0);              // AEC!!
  }

  /**
   * Performs a p-access.
   * Read the sprite pointer at address:
   * <ul>
   *  <li>VM13 VM12 VM11 VM10 1 1 1 1 1 1 1 number2 number1 number0</li>
   * </ul>
   *
   * @param number the sprite number (must be 0 to 7)
   */
  protected int p_access(int number) {
    return bus.load( (VM<<10)+
                     0x3F8+
                     number, view, 0);             // AEC!!
  }

  /**
   * Performs a s-access.
   * Read Mob data at address:
   * <ul>
   *  <li>MP7 MP6 MP5 MP4 MP3 MP2 MP1 MP0 MC5 MC4 MC3 MC2 MC1 MC0</li>
   * </ul>
   *
   * @param number the sprite number (must be 0 to 7)
   */
  protected int s_access(int number) {
    return (bus.load((MPx[number]<<6)+
                      MCx[number], view, 0)
           )& 0xFF;                                // AEC!!
  }

  /**
   * Cycle code for mob 0...
   * BA is set low if mob 0 is allowed to be displayed
   */
  protected void cycleMob0Allow() {
    // set BA low for sprite 0 is necessary
    if (DMAx[0]) {
      setBA(0);                                    // BA is now low
    }
  }

  /**
   * Cycle code for mob 0...
   * Read the sprite pointer of mob 0.
   */
  protected void cycleMob0Pointer() {
    MPx[0]=p_access(0);                            // read sprite pointer 0
  }

  /**
   * Cycle code for mob 0...
   * Read the mob 0 left byte if DMA of mob is on.
   */
  protected void cycleMob0Left() {
    if (DMAx[0]) {                                 // is DMA of mob 0 on?
      mobSequencer[0]=0;
      mobSequencer[0]|=(s_access(0)<<16);          // read left byte of mob
      MCx[0]++;                                    // inc Mob data counter
     }
  }

  /**
   * Cycle code for mob 0...
   * Read the mob 0 middle byte if the DMA of mob is on, then set BA low for
   * sprite 2 if necessary.
   */
  protected void cycleMob0Middle() {
    // read data of sprite 0 if necessary
    if (!DMAx[0]) {                                // is DMA of mob 0 off?
      i_access();                                  // idle access
    } else {
        mobSequencer[0]|=(s_access(0)<<8);         // read middle byte of mob
        MCx[0]++;                                  // inc Mob data counter
      }

    // set BA low for sprite 2 is necessary
    if (DMAx[2]) {
      setBA(0);                                    // BA is now low
    }
  }

  /**
   * Cycle code for mob 0...
   * Read the mob 0 right byte if DMA of mob is on.
   */
  protected void cycleMob0Right() {
    if (DMAx[0]) {                                 // is DMA of mob 0 on?
      mobSequencer[0]|=s_access(0);                // read right byte of mob
      MCx[0]++;                                    // inc Mob data counter
    }
  }

  /**
   * Cycle code for mob 1...
   * BA is set low if mob 0 is allowed to be displayed
   */
  protected void cycleMob1Allow() {
    // set BA low for sprite 1 is necessary
    if (DMAx[1]) {
      setBA(0);                                    // BA is now low
    }
  }

  /**
   * Cycle code for mob 1...
   * Read the sprite pointer of mob 1 and set BA high if mob 1 and 2 off.
   */
  protected void cycleMob1Pointer() {
    MPx[1]=p_access(1);                            // read sprite pointer 1

    // BA high if sprites 1 and 2 off
    if (!DMAx[1] && !DMAx[2]) {
      setBA(1);                                    // BA is now high
    }
  }

  /**
   * Cycle code for mob 1...
   * Read the mob 1 left byte if DMA of mob is on.
   */
  protected void cycleMob1Left() {
    if (DMAx[1]) {                                 // is DMA of mob 1 on?
      mobSequencer[1]=0;
      mobSequencer[1]|=(s_access(1)<<16);          // read left byte of mob
      MCx[1]++;                                    // inc Mob data counter
     }
  }

  /**
   * Cycle code for mob 1...
   * Read the mob 1 middle byte if the DMA of mob is on, then set BA low for
   * sprite 3 if necessary.
   */
  protected void cycleMob1Middle() {
    // read data of sprite 1 if necessary
    if (!DMAx[1]) {                                // is DMA of mob 1 off?
      i_access();                                  // idle access
    } else {
        mobSequencer[1]|=(s_access(1)<<8);         // read middle byte of mob
        MCx[1]++;                                  // inc Mob data counter
      }

    // set BA low for sprite 3 is necessary
    if (DMAx[3]) {
      setBA(0);                                    // BA is now low
    }
  }

  /**
   * Cycle code for mob 1...
   * Read the mob 1 right byte if DMA of mob is on.
   */
  protected void cycleMob1Right() {
    if (DMAx[1]) {                                 // is DMA of mob 1 on?
      mobSequencer[1]|=s_access(1);                // read right byte of mob
      MCx[1]++;                                    // inc Mob data counter
    }
  }

  /**
   * Cycle code for mob 2...
   * Read the sprite pointer of mob 1 and set BA high if mob 2 and 3 off.
   */
  protected void cycleMob2Pointer() {
    MPx[2]=p_access(2);                            // read sprite pointer 2

    // BA high if sprites 2 and 3 off
    if (!DMAx[2] && !DMAx[3]) {
      setBA(1);                                    // BA is now high
    }
  }

  /**
   * Cycle code for mob 2...
   * Read the mob 2 left byte if DMA of mob is on.
   */
  protected void cycleMob2Left() {
    if (DMAx[2]) {                                 // is DMA of mob 2 on?
      mobSequencer[2]=0;
      mobSequencer[2]|=(s_access(2)<<16);          // read left byte of mob
      MCx[2]++;                                    // inc Mob data counter
     }
  }

  /**
   * Cycle code for mob 2...
   * Read the mob 2 middle byte if the DMA of mob is on, then set BA low for
   * sprite 4 if necessary.
   */
  protected void cycleMob2Middle() {
    // read data of sprite 2 if necessary
    if (!DMAx[2]) {                                // is DMA of mob 2 off?
      i_access();                                  // idle access
    } else {
        mobSequencer[2]|=(s_access(2)<<8);         // read middle byte of mob
        MCx[2]++;                                  // inc Mob data counter
      }

    // set BA low for sprite 4 is necessary
    if (DMAx[4]) {
      setBA(0);                                    // BA is now low
    }
  }

  /**
   * Cycle code for mob 2...
   * Read the mob 2 right byte if DMA of mob is on.
   */
  protected void cycleMob2Right() {
    if (DMAx[2]) {                                 // is DMA of mob 2 on?
      mobSequencer[2]|=s_access(2);                // read right byte of mob
      MCx[2]++;                                    // inc Mob data counter
    }
  }

  /**
   * Cycle code for mob 3...
   * Read the sprite pointer of mob 3 and set BA high if mob 3 and 4 off.
   */
  protected void cycleMob3Pointer() {
    MPx[3]=p_access(3);                            // read sprite pointer 3

    // BA high if sprites 3 and 4 off
    if (!DMAx[3] && !DMAx[4]) {
      setBA(1);                                    // BA is now high
    }
  }

  /**
   * Cycle code for mob 3...
   * Read the mob 3 left byte if DMA of mob is on.
   */
  protected void cycleMob3Left() {
    if (DMAx[3]) {                                 // is DMA of mob 3 on?
      mobSequencer[3]=0;
      mobSequencer[3]|=(s_access(3)<<16);          // read left byte of mob
      MCx[3]++;                                    // inc Mob data counter
     }
  }

  /**
   * Cycle code for mob 3...
   * Read the mob 3 middle byte if the DMA of mob is on, then set BA low for
   * sprite 5 if necessary.
   */
  protected void cycleMob3Middle() {
    // read data of sprite 3 if necessary
    if (!DMAx[3]) {                                // is DMA of mob 3 off?
      i_access();                                  // idle access
    } else {
        mobSequencer[3]|=(s_access(3)<<8);         // read middle byte of mob
        MCx[3]++;                                  // inc Mob data counter
      }

    // set BA low for sprite 5 is necessary
    if (DMAx[5]) {
      setBA(0);                                    // BA is now low
    }
  }

  /**
   * Cycle code for mob 3...
   * Read the mob 3 right byte if DMA of mob is on.
   */
  protected void cycleMob3Right() {
    if (DMAx[3]) {                                 // is DMA of mob 3 on?
      mobSequencer[3]|=s_access(3);                // read right byte of mob
      MCx[3]++;                                    // inc Mob data counter
    }
  }

  /**
   * Cycle code for mob 4...
   * Read the sprite pointer of mob 4 and set BA high if mob 4 and 5 off.
   */
  protected void cycleMob4Pointer() {
    MPx[4]=p_access(4);                            // read sprite pointer 4

    // BA high if sprites 4 and 5 off
    if (!DMAx[4] && !DMAx[5]) {
      setBA(1);                                    // BA is now high
    }
  }

  /**
   * Cycle code for mob 4...
   * Read the mob 4 left byte if DMA of mob is on.
   */
  protected void cycleMob4Left() {
    if (DMAx[4]) {                                 // is DMA of mob 4 on?
      mobSequencer[4]=0;
      mobSequencer[4]|=(s_access(4)<<16);          // read left byte of mob
      MCx[4]++;                                    // inc Mob data counter
     }
  }

  /**
   * Cycle code for mob 4...
   * Read the mob 4 middle byte if the DMA of mob is on, then set BA low for
   * sprite 6 if necessary.
   */
  protected void cycleMob4Middle() {
    // read data of sprite 4 if necessary
    if (!DMAx[4]) {                                // is DMA of mob 4 off?
      i_access();                                  // idle access
    } else {
        mobSequencer[4]|=(s_access(4)<<8);         // read middle byte of mob
        MCx[4]++;                                  // inc Mob data counter
      }

    // set BA low for sprite 6 is necessary
    if (DMAx[6]) {
      setBA(0);                                    // BA is now low
    }
  }

  /**
   * Cycle code for mob 4...
   * Read the mob 4 right byte if DMA of mob is on.
   */
  protected void cycleMob4Right() {
    if (DMAx[4]) {                                 // is DMA of mob 4 on?
      mobSequencer[4]|=s_access(4);                // read right byte of mob
      MCx[4]++;                                    // inc Mob data counter
    }
  }

  /**
   * Cycle code for mob 5...
   * Read the sprite pointer of mob 5 and set BA high if mob 5 and 6 off.
   */
  protected void cycleMob5Pointer() {
    MPx[5]=p_access(5);                            // read sprite pointer 5

    // BA high if sprites 5 and 6 off
    if (!DMAx[5] && !DMAx[6]) {
      setBA(1);                                    // BA is now high
    }
  }

  /**
   * Cycle code for mob 5...
   * Read the mob 3 left byte if DMA of mob is on.
   */
  protected void cycleMob5Left() {
    if (DMAx[5]) {                                 // is DMA of mob 5 on?
      mobSequencer[5]=0;
      mobSequencer[5]|=(s_access(5)<<16);          // read left byte of mob
      MCx[5]++;                                    // inc Mob data counter
     }
  }

  /**
   * Cycle code for mob 5...
   * Read the mob 5 middle byte if the DMA of mob is on, then set BA low for
   * sprite 7 if necessary.
   */
  protected void cycleMob5Middle() {
    // read data of sprite 5 if necessary
    if (!DMAx[5]) {                                // is DMA of mob 5 off?
      i_access();                                  // idle access
    } else {
        mobSequencer[5]|=(s_access(5)<<8);         // read middle byte of mob
        MCx[5]++;                                  // inc Mob data counter
      }

    // set BA low for sprite 7 is necessary
    if (DMAx[7]) {
      setBA(0);                                    // BA is now low
    }
  }

  /**
   * Cycle code for mob 5...
   * Read the mob 5 right byte if DMA of mob is on.
   */
  protected void cycleMob5Right() {
    if (DMAx[5]) {                                 // is DMA of mob 5 on?
      mobSequencer[5]|=s_access(5);                // read right byte of mob
      MCx[5]++;                                    // inc Mob data counter
    }
  }

  /**
   * Cycle code for mob 6...
   * Read the sprite pointer of mob 6 and set BA high if mob 6 and 7 off.
   */
  protected void cycleMob6Pointer() {
    MPx[6]=p_access(6);                            // read sprite pointer 6

    // BA high if sprites 6 and 7 off
    if (!DMAx[6] && !DMAx[7]) {
      setBA(1);                                    // BA is now high
    }
  }

  /**
   * Cycle code for mob 6...
   * Read the mob 6 left byte if DMA of mob is on.
   */
  protected void cycleMob6Left() {
    if (DMAx[6]) {                                 // is DMA of mob 6 on?
      mobSequencer[6]=0;
      mobSequencer[6]|=(s_access(6)<<16);          // read left byte of mob
      MCx[6]++;                                    // inc Mob data counter
     }
  }

  /**
   * Cycle code for mob 6...
   * Read mob 6 middle byte if the DMA of mob is on.
   */
  protected void cycleMob6Middle() {
    if (!DMAx[6]) {                                // is DMA of mob 6 off?
      i_access();                                  // idle access
    } else {
        mobSequencer[6]|=(s_access(6)<<8);         // read middle byte of mob
        MCx[6]++;                                  // inc Mob data counter
      }
  }

  /**
   * Cycle code for mob 6...
   * Read the mob 6 right byte if DMA of mob is on.
   */
  protected void cycleMob6Right() {
    if (DMAx[6]) {                                 // is DMA of mob 6 on?
      mobSequencer[6]|=s_access(6);                // read right byte of mob
      MCx[6]++;                                    // inc Mob data counter
    }
  }

  /**
   * Cycle code for mob 7...
   * Read the sprite pointer of mob 7 and set BA high if mob 7 off.
   */
  protected void cycleMob7Pointer() {
    MPx[7]=p_access(7);                            // read sprite pointer 7

    // BA high if sprites 7 off
    if (!DMAx[7]) {
      setBA(1);                                    // BA is now high
    }
  }

  /**
   * Cycle code for mob 7...
   * Read the mob 7 left byte if DMA of mob is on.
   */
  protected void cycleMob7Left() {
    if (DMAx[7]) {                                 // is DMA of mob 7 on?
      mobSequencer[7]=0;
      mobSequencer[7]|=(s_access(7)<<16);          // read left byte of mob
      MCx[7]++;                                    // inc Mob data counter
     }
  }

  /**
   * Cycle code for mob 7...
   * Read mob 7 middle byte if the DMA of mob is on.
   */
  protected void cycleMob7Middle() {
    if (!DMAx[7]) {                                // is DMA of mob 7 off?
      i_access();                                  // idle access
    } else {
        mobSequencer[7]|=(s_access(7)<<8);         // read middle byte of mob
        MCx[7]++;                                  // inc Mob data counter
      }
  }

  /**
   * Cycle code for mob 7...
   * Read the mob 7 right byte if DMA of mob is on.
   */
  protected void cycleMob7Right() {
    if (DMAx[7]) {                                 // is DMA of mob 7 on?
      mobSequencer[7]|=s_access(7);                // read right byte of mob
      MCx[7]++;                                    // inc Mob data counter
    }
  }

  /**
   * Cycle code for mob counter...
   * Increments mobs data counter base if expansion flip flops are set.
   */
  protected void cycleMobCounterInc2() {
    int i;

    // section 3.8.1 Memory access and display
    // "7. In the first phase of cycle 15, it is checked if the expansion
    // flip flop is set. if so, MCBASE is incremented by 2."

    for (i=0; i<8; i++) {
      if (expansion[i].isSet())                    // expansion flip-flop set?
        MCBASEx[i]+=2;                             // increment mob data base
    }
  }

  /**
   * Cycle code for mob counter...
   * Increments mobs data counter base if expansion flip flops are set and
   * valutates if sprites are to be displayed.
   */
  protected void cycleMobCounterInc1() {
    int i;

    // section 3.8.1 Memory access and display
    // "8. In the first phase of cycle 16, it is checked if the expansion
    // flip flop is set. If so, MCBASE is incremented by 1. After that, the
    // VIC checks if MCBASE is equal to 63 and turns off the DMA and the
    // display of a sprite if it is.

    for (i=0; i<8; i++) {
      if (expansion[i].isSet()) {                  // expansion flip-flop set?
        MCBASEx[i]++;                              // increment mob data base
      ///? }
        if (MCBASEx[i]==63) {
          DMAx[i]=false;                           // turn off DMA of sprite
          display[i]=false;                        // turn off sprite display
        }
      } ///?
    }
  }

  /**
   * Cycle code for expansion flip flop...
   * Invert the expansion flip flop if MxYE is set.
   */
  protected void cycleInvertExp() {
    int i;

    // section 3.8.1. Memory access and display
    // "2. If the MxYE bit is set in the first phase of cycle 55, the expansion
    // flip flop is inverted."
    for (i=0; i<8; i++) {
      if (MxYE[i]==1)                              // sprite Y expanded?
        expansion[i].invert();                     // invert the exp. flip flop
    }
  }

  /**
   * Cycle code for DMA of sprites...
   * Allow DMA of sprite to be on if sprite is enabled and sprite position
   * matchs the raster.
   */
  protected void cycleAllowDMA() {
    int i;

    // section 3.8.1. Memory access and display
    // "3. In the first phases of cycle 55 and 56, the VIC checks for every
    // sprite if the corresponding MxE bit in register $d015 is set and the Y
    // coordinate of the sprite match the lower 8 bits of RASTER. If this is
    // the case and the DMA for the sprite is still off, the DMA is switched
    // on, MCBASE is cleared, and if the MxYE bit is set the expansion flip
    // flop is reset."
    for (i=0; i<8; i++) {
      if ((MxE[i]==1) && (MxY[i]==(raster & 0xFF))) {
        if (!DMAx[i]) {
          DMAx[i]=true;                            // turn DMA on
          MCBASEx[i]=0;                            // clear MCBASE
          if (MxYE[i]==1)                          // expansion set?
            expansion[i].reset();                  // reset flip flop
        }
      }
    }
  }

  /**
   * Cycle code for g-access...
   * Execute a g-access and if in display state updates the counters
   */
  protected void cycleGAccess() {
    videoBuffer=videoMatrixColor[VMLI];            // update buffer
    // section 3.7.3 Graphics modes
    // "The heart of the sequencer is an 8 bit shift register that is shifted
    // by 1 bit every pixel and reloaded with new graphics data after every
    // g-access. With XSCROLL from register $d016 the reloading can be delayed
    // by 0-7 pixels, thus shifting the display up to 7 pixels to the right."
    //
    // note: the following is a speed up emulation action that use the graphics
    // data sequencer with 16 pixels instead of 8 (mappen in high of 32 bits).
    // 16+8=24, but 4 is sub because the G-access is here emulated at start of
    // the cycle.

    gdSequencer+=((g_access()& 0xff)<<(20-xScroll));    // g-access

    // section 3.7.2. VC and RC
    // "4. VC and VMLI are incremented after each g-access in display
    // state"

    if (vicState<=0x07) {                          // dislay state?
      VC++;                                        // increment Vic Counter
      VMLI++;                                      // increment VMLI
    }
  }

  /**
   * Cycle code for refreshing...
   * Refresh a line of memory.
   */
  protected void cycleRefresh() {
    r_access();                                    // refresh a line of memory
    REF--;                                         // dec refresh counter
  }

  /**
   * Cycle code for border comparison...
   * Set or reset the vertical border flip flop if raster reaches the bottom
   * or the top border line.
   */
  protected void cycleBorderComp() {
    // 3.9. The border unit
    // "2. If the Y coordinate reaches the bottom comparison value in cycle
    // 63, the vertical border flip flop is set."

    if (raster==BOTTOM[RSEL]) {
      verticalBorder.set();                        // set vertical border
    }

    // 3.9. The border unit
    // "3. If the Y coordinate reaches the top comparison value in cycle 63
    // and the DEN bit in register $d011 is set, the vertical border flip
    // flop is reset."
    // 3.10. Display Enable
    // " - If the DEN bit is cleared, the reset input of the vertical border
    // flip flop is deactivated."

     if ((raster==TOP[RSEL]) && (DEN==1)) {
       verticalBorder.reset();                      // reset vertical border
     }
  }

  /**
   * Cycle code for c-access...
   * Performs a c-access if there's a bad line condiction
   * Note: in idle state there's no c-access and the data is assumend to be 0.
   * This is included here for simplify the emulation.
   */
  protected void cycleCAccess() {
    // section 3.7.2. VC and RC
    // "...Once started, one c-access is done in the second phase of every
    // clock cycle in the range 15-54. The read data is stored in the
    // video matrix/color line at the position specified by VMLI. ..."

    // section 3.7.3:
    // "...The idle state is a bit special in that no c-accesses occur in it
    // and the sequencer uses "0" bits for the video matrix data."

    if (badLine) {
      if ((vicState & 0x08)!=0)                          // idle state?
           videoMatrixColor[VMLI]=0;                     // no c-access in idle
      else videoMatrixColor[VMLI]=c_access();            // c-access
    }
  }

  /**
   * Cycle for mobs display...
   * Set the display of sprites if DMA is on and Y coordinate matchs raster.
   */
  protected void cycleMobDisplay() {
    int i;

    // section 3.8.1. Memory access and display
    // "4. In the first phase of cycle 58 [6569 timing], the MC of every
    // sprite is loaded from it's belonging MCBASE and it is checked if
    // the DMA for the sprite is turned on and the Y coordinate of the
    // sprite matches the lower 8 bits of RASTER. If this is the case, the
    // display of the sprite is turned on.

    for (i=0; i<8; i++) {
      MCx[i]=MCBASEx[i];                            // load mob data counter
      if (DMAx[i] && (MxY[i]==(raster & 0xFF)))
        display[i]=true;                            // sprite display on
    }
  }

  /**
   * Cycle for going to idle state...
   * Goes to idle state if there's not bad line condiction and RC reaches 7.
   */
  protected void cycleGotoIdle() {
    // section 3.7.1 Idle state/display state
    // "The transition from display to idle state occurs in cycle 58 of a
    // line if the RC contains the value 7 and there is no Bad Line
    // Condition."

    if ((RC==7) && !badLine) {
      vicState|=0x08;                             // go to idle state
    }

    // section3.7.2 VC and RC
    // "5. In the first phase of cycle 58, the VIC checks if RC=7. If so,
    // the video logic goes to idle state an VCBASE is loaded from VC. If
    // the video logic is in display state afterwards (this is always the
    // case if there is a Bad Line Condiction), RC is incremented.

    if (RC==7) {
      vicState|=0x08;                              // go to idle state
      VCBase=VC;                                   // load VCbase from VC
    }

    if (vicState<=0x07) {                          // display state?
      RC++;                                        // increment row counter
    }
  }

  /**
   * Cycle code for Bad Line...
   * Evaluates if there's a bad line condiction.
   */
  protected void cycleIsBadLine() {
    // section 3.5: bad line condiction:
    // "A Bad Line Condiction is given at any arbitrary clock cycle,
    // if at the negative edge of fi0 at the beginning of the cycle
    // RASTER >= $30 and RASTER <= $F7 and the lower three bits of RASTER
    // are equal to YSCROLL and if the DEN bit was set during an arbitrary
    // cycle of raster line $30"

    // section 3.7.1 Idle state/display state
    // "The transition from idle to display state occurs as soon as there
    // is a Bad Line Condiction"

    if ((raster>=RASTER_UP) &&                     // bad line condiction ?
       (raster<=RASTER_DW) &&
       ((raster & 0x7)==yScroll) &&
        allowBadLine) {
      badLine=true;                                // this is a bad line
      vicState&=0x7;                               // go to display state
    } else badLine=false;                          // this is not a bad line
  }

  /**
   * Cycle code for raster line 0 reached...
   * If we reaches the raster line 0, performs some action.
   */
  protected void cycleRaster0() {
    // section 3.7.2. VC and RC
    // "1. Once somewhere outside of the range of raster lines $30-$F7 (i.e.
    // outside of the Bad Line range), VCBASE is reset to zero. This is
    // presumably done in raster line 0, the exact moment cannot be determined
    // and is irrelevant."

    // section 3.13. DRAM refresh
    // "... The counter is reset to $ff in raster line 0 ..."

    if ((raster==0) && (cycle==1)) {               // is raster line 0?
      VCBase=0;                                    // set VCBASE to 0
      REF=0xFF;                                    // init refresh address
    }
  }

  /**
   * Cycle code for raster $30 reached...
   * If we reach raster $30, evaluate if bad line is allowed.
   */
  protected void cycleRaster30() {
    // section 3.5 bad line condiction:
    // "... if the DEN bit was set during an arbitrary cycle of raster line $30"

    if(raster==RASTER_UP) {                        // is raster line $30?
      if (DEN==1) den=true;                        // is DEN=1 in one cycle?
      if (cycle==maxCycle) {
        allowBadLine=den;                          // allowBadline if DEN=1
        den=false;                                 // in one cycle
      }
    }
  }

  /**
   * Cycle code for Vic Counter...
   * Load the Vic counter from the base, and see for reset RC.
   */
  protected void cycleSetVicCounter() {
    // section 3.7.2. VC and RC
    // "2. In the first phase of cycle 14 of each line, VC is loaded from
    // VCBASE and VMLI is cleared. If there is a Bad Line Condiction in
    // this phase, RC is also reset to zero."

    VC=VCBase;                                     // load VC with VCBASE
    VMLI=0;                                        // clear VMLI
    if (badLine) {                                 // bad line condiction ?
      RC=0;                                        // RC reset to zero
    }
  }

  /**
   * Cycle code for bad line...
   * If there's a bad line condiction, allows c-access.
   */
  protected void cycleIsCAccess() {
    // section 3.7.2. VC and RC
    // "3. If there is a Bad line condiction in cycles 12-54, BA is set low
    // and the c-acesses are started..."

    if (badLine) {                                 // bad line condiction?
      setBA(0);                                    // BA is now low
    }
  }

  /**
   * Set the value of BA signal.
   * Notify the signal to external chip and remember it for internal use.
   */
  protected void setBA(int value) {
    if (value==0) {
      BA=0;                                        // BA low
      countBA=0;                                   // first BA low
      // notify to all the chip that BA is low
      io.notifySignal(S_BA, value);
    } else {
         BA=1;                                     // BA high
        // notify to all the chip that BA is high
        io.notifySignal(S_BA, value);
        setAEC(1);                                 // AEC goes high
      }
  }

  /**
   * Set and notify the new value of AEC
   *
   * @param value the 0/1 value of AEC
   */
  public void setAEC(int value) {
    // notify to all chip that AEC is changed
    io.notifySignal(S_AEC, value);
  }

  /**
   * Cycle code for AEC after 3 BA low...
   * AEC must go low after 3 BA signal low
   */
  protected void cycleAEC() {
    if (BA==0) {                                   // is BA low?
      if (countBA==3) setAEC(0);                   // if 3 BA then AEC low
      else countBA++;                              // else another BA low
    }
  }

  /**
   * Cycle code for raster compare for generating IRQ
   */
  protected void cycleRasterCompare() {
    if (raster==rasterCompare) {
      IRST=1;                                      // interrupt
      rasterCompare=-1;                            /// (to verify)
      if (ERST==1) {                               // is interrupt enabled?
        IRQ=1;                                     // start of IRQ
        io.notifySignal(S_IRQ, 0);                 // notify start of IRQ
      }
    }
  }

  /**
   * Execute VIC cycle operation when the clock signal is low (phase 1)
   * This method is abstract because the cycle depends by the type of Vic
   * (PAL, NTSC).
   */
  public abstract void fi0low();

  /**
   * Execute VIC cycle operation when the clock signal is high (phase 2)
   * This method is abstract because the cycle depends by the type of Vic
   * (PAL, NTSC).
   */
  public abstract void fi0high();

  /**
   * Vic dot clock execution
   */
  public void dotClock() {
    // are we not in vertical blancking interval ?

    if (!((raster>=firstVblankLine) || (raster<=lastVblankLine))) {

      // are we not in horizontal blancking interval ?
      if (((rasterX<=lastVisXCoo) || (rasterX>=firstVisXCoo))) {

        // 3.9. The border unit
        // "1. If the X coordinate reaches the right comparison value, the main
        // border flip flop is set."

        if (rasterX==RIGHT[CSEL]) {                  // x reaches right comp.?
          mainBorder.set();                          // set main border f.f.
        }

        // 3.9. The border unit
        // "4. If the X coordinate reaches the left comparison value and the Y
        // coordinate reaches the bottom one, the vertical border flip flop is
        // set."

        // 3.9. The border unit
        // "5. If the X coordinate reaches the left comparison value and the Y
        // coordinate reaches the top one and the DEN bit in register $d011 is
        // set, the vertical flip flop is reset."

        // 3.10. Display Enable
        // " - If the DEN bit is cleared, the reset input of the vertical border
        // flip flop is deactivated."

        // 3.9. The border unit
        // "6. If the X coordinate reaches the left comparison value and the
        // vertical border flip flop is not set, the main flip flop is reset."

        if (rasterX==LEFT[CSEL]) {                   // x reaches left comp.?
          if (raster==BOTTOM[RSEL]) {
            verticalBorder.set();                    // set vertical border
          }
          if ((raster==TOP[RSEL]) && (DEN==1)) {
            verticalBorder.reset();                  // reset vertical border
          }
          if (verticalBorder.isReset()) {
            mainBorder.reset();                      // reset main border
          }
        }

        display();
      }
    }

    //update raster positions

    // Section 3.4. Display generation and display window dimensions
    // "If you are wondering why the first visible X coordinates seems to come
    // after the last visible ones: This is because for the reference point to
    // mark the beginning of a raster line, the occurance of the raster IRQ has
    // been chosen, witch doesn't coincide with X coordinate 0 but with the
    // coordinate given as "First X coo. of a line". The x coordinates run up
    // to $1ff (only $1f7 on the 6569) within a line, then comes X coordinate
    // 0..."
    rasterX++;
    if (rasterX>lastXPos) {                          // is end of X position?
      rasterX=0;                                     // start of X position
    }

    if (rasterX==firstXCoo) {                        // is end of line
      raster++;                                      // next line
      if (raster>linesNumber) {                      // if end of screen?
        raster=0;                                    // start of screen
        tv.newFrame();                               // start a new frame
      }
    }
  }

  /**
   * Display unit of the Vic
   * note: very experimental implementation: probably not the definitive.
   */
  public void display() {
    int color;                     // color to be send to raster TV

    // this are calculated always
    calculateSpritesInfo();
    spritesCollision();

    // section 3.9. The border unit
    // "The main border flip flop controls the border display. If it is set,
    // the VIC displays the color stored in register $d020, otherwise it
    // displays the color that the priority multiplexer switches through from
    // the graphics or sprite data sequencer. It has the highest display
    // priority."

    // "...the vertical border flip flop controls the output of the graphics
    // data sequencer. The sequencer only outputs data if the flip flop is
    // not set, otherwise it displays the background color. This was probably
    // done to prevent sprite-graphics collisions in the border area."

    if (verticalBorder.isSet()) {
      tv.sendPixel(EC);                              // output border color
      return;
    } else {
        calculateGraphicsInfo();
        graphicsCollision();
      }

    if (mainBorder.isSet()) {
      tv.sendPixel(EC);                              // output border color
    } else {
        tv.sendPixel(multiplexerOutput());             // output color
      }
  }

  /**
   * Calculate graphics information of the next pixel to show.
   */
  public void calculateGraphicsInfo() {
    switch (vicState) {

      // section 3.7.3.1. Standard text mode (ECM/BMM/ECM=0/0/0)
      // "In standard text mode, every bit in the character generator directly
      //  corresponds to one pixel on the screen. The foreground color is given
      //  by the color nibble from the video matrix for each character, the
      //  background color is set globaly with register $d021."
      case VIC_STANDARD_TEXT:
        if ((gdSequencer & 0x80000000)==0) {
          grColor=(int)B0C;                            // background color 0
          grPriority=BACKGR;                           // background priority
        } else {
            grColor=videoBuffer>>8;                    // 4 bit color
            grPriority=FOREGR;                         // foreground priority
          }
        gdSequencer<<=1;                               // this pixel is now out
        break;

      // section 3.7.3.3 Standard bitmap mode (ECM/BMM/MCM=0/1/0)
      // "In standard bitmap mode, every bit in the bitmap directly correponds
      //  to one pixel on the screen. Foreground and background color can be
      //  arbitrary set for every 8x8 block."
      case VIC_STANDARD_BITMAP:
        if ((gdSequencer & 0x80000000)==0) {
          grColor=videoBuffer & 0x0f;                  // 4 bit color
          grPriority=BACKGR;                           // background priority
        } else {
            grColor=(videoBuffer>>4) & 0x0f;           // 4 bit color
            grPriority=FOREGR;                         // foreground priority
          }
        gdSequencer<<=1;                               // this pixel is now out
        break;

      // section 3.7.3.7. Invalid bitmap mode 1 (ECM/BMM/MCM=1/1/0)
      // "This mode also only display a black screen..."
      // "The structure of the graphics is basically as in standard bitmap
      //  mode..."
      case VIC_INVALID_BITMAP1:
        if ((gdSequencer & 0x80000000)==0) {
          grPriority=BACKGR;                           // background priority
        } else {
            grPriority=FOREGR;                         // foreground priority
          }
        grColor=0;                                     // black color
        gdSequencer<<=1;                               // this pixel is now out
        break;

      // section 3.7.3.2. Multicolor text mode (ECM/BMM/MCM=0/0/1)
      // "...If bit 11 of the c-data is zero, the character is display as in
      //  standard text mode with only the color 0-7 available for the
      //  foreground. If the bit 11 is set, each two adjacement bits of the dot
      //  matrix form one pixel..."
      case VIC_MULTICOLOR_TEXT:
        if ((videoBuffer & 0x0800)==0) {               // MC flag
          if ((gdSequencer & 0x80000000)==0) {
            grColor=B0C;                               // 4 bit color
            grPriority=BACKGR;                         // background priority
          } else {
              grColor=(videoBuffer>>8)& 0x07;          // 3 bit color
              grPriority=FOREGR;                       // foreground priority
            }
          gdSequencer<<=1;                             // this pixel is now out
        } else {
            if ((rasterX & 0x01)==0) {                 // twice indicator
              switch (gdSequencer & 0xC0000000) {
                case 0x00:
                  grColor=B0C;                         // 4 bit color
                  grPriority=BACKGR;                   // background priority
                  break;
                case 0x01:
                  grColor=B1C;                         // 4 bit color
                  grPriority=BACKGR;                   // background priority
                  break;
                case 0x02:
                  grColor=B0C;                         // 4 bit color
                  grPriority=FOREGR;                   // foreground priority
                  break;
                case 0x03:
                  grColor=(videoBuffer>>8) & 0x07;     // 3 bit color
                  grPriority=FOREGR;                   // foreground priority
                  break;
              }
            } else {
                // previous information are still valid
                gdSequencer<<=2;                       // these 2 pixels are out
              }
          }
        break;

      // section 3.7.3.4. multicolor bitmap mode (ECM/BMM/MCM=0/1/1)
      // "Similar to the multicolor text mode, this mode also forms (twice
      // as wide) pixels by combining two adjacement bits..."
      case VIC_MULTICOLOR_BITMAP:
        if ((rasterX & 0x01)==0) {                     // twice indicator
          switch (gdSequencer & 0xC0000000) {
            case 0x00:
              grColor=B0C;                             // 4 bit color
              grPriority=BACKGR;                       // background priority
              break;
            case 0x01:
              grColor=(videoBuffer>>4)& 0x0f;          // 4 bit color
              grPriority=BACKGR;                       // background priority
              break;
            case 0x02:
              grColor=videoBuffer & 0x0f;              // 4 bit color
              grPriority=FOREGR;                       // foreground priority
              break;
            case 0x03:
              grColor=(videoBuffer>>8)& 0x0f;          // 4 bit color
              grPriority=FOREGR;                       // foreground priority
              break;
          }
        } else {
            // previous information are still valid
            gdSequencer<<=2;                           // these 2 pixels are out
          }
        break;

      // section 3.7.3.5. ECM text mode (ECM/BMM/MCM/1/0/0)
      // "This text mode is the same as the standard text mode, but it allows
      //  the selection of one of four background colors for every single
      //  character. The selection is done with upper two bits of the character
      // pointer..."
      case VIC_ECM_TEXT:
        if ((gdSequencer & 0x80000000)==0) {
          switch ((videoBuffer>>6)& 0x03) {            // back. col. selection
            case 0x00:
              grColor=(int)B0C;                        // background color 0
              grPriority=BACKGR;                       // background priority
              break;
            case 0x01:
              grColor=(int)B1C;                        // background color 1
              grPriority=BACKGR;                       // background priority
              break;
            case 0x02:
              grColor=(int)B2C;                        // background color 2
              grPriority=FOREGR;                       // foreground priority
              break;
            case 0x03:
              grColor=(int)B3C;                        // background color 2
              grPriority=FOREGR;                       // foreground priority
              break;
          }
        } else {
            grColor=videoBuffer>>8;                    // 4 bit color
            grPriority=FOREGR;                         // foreground priority
          }
        gdSequencer<<=1;                               // this pixel is now out
        break;

      // section 3.7.3.6. Invalid text mode (ECM/BMM/MCM=1/0/1)
      // "Setting the ECM and MCM bits simultaneously dosn't select one of the
      //  "official" graphics modes of the VIC but creates only black pixels.."
      // "...The generated graphics is similar to that of the multicolor text
      //  mode, but the character set is limited to 64 characters as in ECM
      //  mode."
      case VIC_INVALID_TEXT:
        if ((videoBuffer & 0x0800)==0) {               // MC flag
          if ((gdSequencer & 0x80000000)==0) {
            grPriority=BACKGR;                         // background priority
          } else {
              grPriority=FOREGR;                       // foreground priority
            }
          grColor=0;                                   // black color
          gdSequencer<<=1;                             // this pixel is now out
        } else {
            if ((rasterX & 0x01)==0) {                 // twice indicator
              switch (gdSequencer & 0xC000) {
                case 0x00:
                case 0x01:
                  grPriority=BACKGR;                   // background priority
                  break;
                case 0x02:
                case 0x03:
                  grPriority=FOREGR;                   // foreground priority
              }
              grColor=0;                               // black color
            } else {
                // previous information are still valid
                gdSequencer<<=2;                       // these 2 pixels are out
              }
          }
        break;

      // section 3.7.3.8. Invalid bitmap mode 2 (ECM/BMM/MCM=1/1/1)
      // "The last invalid mode also creates a black screen..."
      // "...The structures of the graphics is basically as in multicolor
      //  bitmap mode, but the bits 9 and 10 of g-addresses are alwayszero
      //  due to the set ECM bit, with the same result as in the first invalid
      // bitmap mode..."
      case VIC_INVALID_BITMAP2:
        if ((rasterX & 0x01)==0) {                     // twice indicator
          switch (gdSequencer & 0xC0000000) {
            case 0x00:
            case 0x01:
              grPriority=BACKGR;                       // background priority
              break;
            case 0x02:
            case 0x03:
              grPriority=FOREGR;                       // foreground priority
              break;
          }
          grColor=B0C;                                 // 4 bit color
        } else {
            // previous information are still valid
            gdSequencer<<=2;                           // these 2 pixels are out
          }
        break;

      // section 3.7.3.9. Idle state
      // "In idle state, the VIC reads the graphics data from address $3fff
      //  (resp. $39ff if the ECM bit is set) and displays it in the selected
      //  graphics mode, but with the video matrix (normally read in the
      //  c-accesses) being all "0" bits."
      case (VIC_STANDARD_TEXT | 0x08):                 // in idle
      case (VIC_MULTICOLOR_TEXT | 0x08):               // in idle
      case (VIC_ECM_TEXT | 0x08):                      // in idle
        if ((gdSequencer & 0x80000000)==0) {
          grColor=(int)B0C;                            // background color 0
          grPriority=BACKGR;                           // background priority
        } else {
            grColor=0;                                 // black color
            grPriority=FOREGR;                         // foreground priority
          }
        gdSequencer<<=1;                               // this pixel is now out
        break;

      case (VIC_STANDARD_BITMAP | 0x08):               // in idle
      case (VIC_INVALID_TEXT | 0x08):                  // in idle
      case (VIC_INVALID_BITMAP1 | 0x08):               // in idle
        if ((gdSequencer & 0x80000000)==0) {
          grPriority=BACKGR;                           // background priority
        } else {
            grPriority=FOREGR;                         // foreground priority
          }
        grColor=0;                                     // black color
        gdSequencer<<=1;                               // this pixel is now out
        break;

      case (VIC_MULTICOLOR_BITMAP | 0x08):             // in idle
        if ((rasterX & 0x01)==0) {                     // twice indicator
          switch (gdSequencer & 0xC0000000) {
            case 0x00:
              grColor=(int)B0C;                        // background color 0
              grPriority=BACKGR;                       // background priority
              break;
            case 0x01:
              grColor=0;                               // black color
              grPriority=BACKGR;                       // background priority
              break;
            case 0x02:
            case 0x03:
              grColor=0;                               // black color
              grPriority=FOREGR;                       // foreground priority
              break;
          }
        } else {
            // previous information are still valid
            gdSequencer<<=2;                           // these 2 pixels are out
          }
        break;

      case (VIC_INVALID_BITMAP2 | 0x08):               // in idle
        if ((rasterX & 0x01)==0) {                     // twice indicator
          switch (gdSequencer & 0xC0000000) {
            case 0x00:
            case 0x01:
              grPriority=BACKGR;                       // background priority
              break;
            case 0x02:
            case 0x03:
              grPriority=FOREGR;                       // foreground priority
              break;
          }
          grColor=(int)B0C;                            // 4 bit color
        } else {
            // previous information are still valid
            gdSequencer<<=2;                           // these 2 pixels are out
          }
        break;
    }
  }

  /**
   * Calculate graphics sprites information of the next pixel to show.
   */
  public void calculateSpritesInfo() {
    int i;      // cycle variable

    for (i=0; i<8; i++) {                             // cycle to all the mobs
      // section 3.8.1. Memory access and display
      // "6. If the sprite display for a sprite is turned on, the shift
      //  register is shifted left by one bit with every pixel as soon as the
      //  current X coordinate of the raster beam matches the X coordinate of
      //  the sprite, and the bits that "fall off" are displayed. If the MxXE
      //  bit belonging to the sprite in register $d01d is set, the shift is
      //  done only every second pixel and the sprite appears twice as wide.
      //  If the sprite is in multicolor mode, every two adjacent bits form one
      //  pixel."

      if (display[i]) {                                // display on ?
        if ((rasterX==MxX[i]) && (pixels[i]==-1)) {     // activate drawing ?
          // note: when activated it display 24 pixel, even if MxX[i] changes
          pixels[i]=24;                                // 24 pixels to draw
          pixelsCount[i]=0;                            // reset count
        }

        if (pixels[i]!=-1) {                           // is drawing ?
          switch (MxMC[i]) {
            case 0x00:                                 // normal
              if (MxXE[i]==0) {                        // not double ?

                if ((mobSequencer[i] & 0x800000)==0) { // 0 pixel
                  spColor[i]=-1;                       // trasparent
                  spPriority[i]=BACKGR;                // background priority
                } else {                               // 1 pixel
                    spColor[i]=MxC[i];                 // sprite color
                    spPriority[i]=FOREGR;              // foreground priority
                  }
                mobSequencer[i]<<=1;                   // 1 pixel is out
                mobSequencer[i]&=0xFFFFFF;
                pixels[i]--;
              } else {                                 // double

                  if ((pixelsCount[i] % 2)==0) {
                    if ((mobSequencer[i] & 0x800000)==0) {
                      spColor[i]=-1;                   // trasparent
                      spPriority[i]=BACKGR;            // background priority
                    } else {                           // 1 pixel
                        spColor[i]=MxC[i];             // sprite color
                        spPriority[i]=FOREGR;          // foreground priority
                      }
                    pixelsCount[i]++;                  // inc pixel count
                  } else {
                      // previous information are still valid
                      mobSequencer[i]<<=1;             // 1 pixel is out
                      mobSequencer[i]&=0xFFFFFF;
                      pixels[i]--;
                      pixelsCount[i]++;                // inc pixel count
                    }
                }
              break;

            case 0x01:                                 // multicolor
              if (MxXE[i]==0) {                        // not double ?
                if ((pixelsCount[i] % 2)==0) {
                  switch (mobSequencer[i] & 0xC00000) {
                    case 0x000000:
                      spColor[i]=-1;                   // trasparent
                      spPriority[i]=BACKGR;            // background priority
                      break;
                    case 0x400000:
                      spColor[i]=MM0;                  // multicolor 0
                      spPriority[i]=BACKGR;            // background priority
                      break;
                    case 0x800000:
                      spColor[i]=MxC[i];               // sprite color
                      spPriority[i]=FOREGR;            // foreground priority
                      break;
                    case 0xC00000:
                      spColor[i]=MM1;                  // multicolor 1
                      spPriority[i]=FOREGR;            // foreground priority
                      break;
                  }
                  pixelsCount[i]++;                    // inc pixel count
                } else {
                    // previous information are still valid
                    mobSequencer[i]<<=2;               // 2 pixels are out
                    mobSequencer[i]&=0xFFFFFF;
                    pixels[i]-=2;
                    pixelsCount[i]++;                  // inc pixel count
                  }

              } else {                                 // double

                  if ((pixelsCount[i] % 4)==0) {
                    switch (mobSequencer[i] & 0xC00000) {
                      case 0x000000:
                        spColor[i]=-1;                 // trasparent
                        spPriority[i]=BACKGR;          // background priority
                        break;
                      case 0x400000:
                        spColor[i]=MM0;                // multicolor 0
                        spPriority[i]=BACKGR;          // background priority
                        break;
                      case 0x800000:
                        spColor[i]=MxC[i];             // sprite color
                        spPriority[i]=FOREGR;          // foreground priority
                        break;
                      case 0xC00000:
                        spColor[i]=MM1;                // multicolor 1
                        spPriority[i]=FOREGR;          // foreground priority
                        break;
                    }
                  }

                  if (((pixelsCount[i]+1) % 4)==0) {   // last of 4 ?
                    mobSequencer[i]<<=2;               // 2 pixels are out
                    mobSequencer[i]&=0xFFFFFF;
                    pixels[i]-=2;
                  }

                  // previous (or actual) information are still valid
                  pixelsCount[i]++;                    // inc pixel count
                }
              break;
            default:                                   // debug info
              System.err.println("ERROR: MxMC of sprite "+i+" is invalid");
          }
        } else {
            spColor[i]=-1;                             // no color
          }
      }
    }
  }

  /**
   * Claculate the output of Vic multiplexer.
   * This multiplexer chooses the right color from graphics and sprite sequencer
   * according to the priority.
   *
   * @return the color to display
   */
  public int multiplexerOutput() {
    int priority=BACKGR;   // sprite priority
    int color=-1;          // sprite color: -1 = no sprite color or transparent
    int i;                 // cycle variables

    for (i=7; i>=0; i--) {
      if (pixels[i]!=-1) {                             // are we drawing mob?
        if (spPriority[i]==FOREGR) {
          color=spColor[i];                            // use color of this
          priority=FOREGR;                             // max sprite priority
        } else {
            if ((priority==BACKGR) && (spColor[i]!=-1)) {
              color=spColor[i];                        // use color of this
            }
          }
	if (pixels[i]<=0) pixels[i]=-1;                // stop drawing mob
      }
    }

    if (color==-1)                                     // there's no sprite color?
      return grColor;                                  // graphics color

    if (priority==FOREGR)                              // max priority
      return color;                                    // sprite color

    if (grPriority==FOREGR)                            // max graphics priority
      return grColor;                                  // graphics color

    return color;                                      // sprite color
  }

  /**
   * Check for sprite to sprite collision
   */
  public void spritesCollision() {
    int i;       // cycle variables
    int j;       // cycle variables

    // section 3.8.2. Priority and collision detection
    // "A collision of sprites among themselvs is detected as soon as two or
    // more sprite data sequencers output a non-transparent pixel in the course
    // of display generation (this can also happen somewhere outside of the
    // visible screen area). In this case, the MxM bits of all affected sprites
    // are set in register $d01e and (if allowed ...), an interrupt is
    // generated."

    /// (not a speed up solution!)
    for (i=7; i<1; i--) {
      if (spColor[i]!=-1) {                            // not transparent ?
        for (j=i-1; j<0; j--) {
          if (spColor[i]!=-1) {                        // not transparent ?
            MxM[i]|=1;                                 // update collision flag
            MxM[j]|=1;                                 // update collision flag
          }
        }
      }
    }
  }

  /**
   * Check for sprites to graphics collision
   */
  public void graphicsCollision() {
    int i;     // cycle variables

    // section 3.8.2. Priority and collision detection
    // "A collision of sprites and other graphics data is detected as soon as
    // one or more sprite data sequencers output a non-trasparent pixel in the
    // course of display generation (this can also happen somewhere outside of
    // the visible screen area). In this case, the MxM bits of all affected
    // sprites are set in register $d01e (if allowed ...), an interrupt is
    // generated."

    for (i=0; i<8; i++) {
      if ((spColor[i]!=-1) && (grPriority==FOREGR)) {  // a collision
        MxD[i]|=1;                                     // update collision flag

      }
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
