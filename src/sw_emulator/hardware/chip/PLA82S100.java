/**
 * @(#)PLA82S100.java 1999/10/15
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

import sw_emulator.hardware.cartridge.Cartridge;
import sw_emulator.hardware.powered;
import sw_emulator.hardware.signaller;
import sw_emulator.hardware.memory.Memory;
import sw_emulator.hardware.memory.ROM;
import sw_emulator.hardware.memory.ColorRAM;
import sw_emulator.hardware.bus.C64Bus;
import sw_emulator.hardware.bus.readableBus;
import sw_emulator.hardware.bus.writeableBus;
import sw_emulator.util.Monitor1;

/**
 * Emulate the PLA 82S100 chip of a Commodore 64 computer.
 * The emulation consists of calculating the rigth table for reading/writing
 * bus operation of Cpu and Vic chips.
 *
 * For the VIC:
 * it can access 16Kb, so the 64Kb of memory space addess are divide into four
 * bank: 0, 1, 2, 3. The PLA set the correct bank using VA14 and VA15 signals.
 *
 * For the Cpu:
 * it accesses 64Kb of memory in read and write operations.
 * The available configurations are given by this signals value: LORAM, HIRAM,
 * GAME, EXROM, CHAREN.
 *
 * The chips that may notify some changing signals may use
 * <code>notifySignal</code> and then a <code>opSignal</code> to the available
 * <code>monitor</code>.
 *
 * @author Ice
 * @version 1.00 15/10/1999
 */
public class PLA82S100 extends Thread implements powered, signaller {
  
  /** The state of power */
  private boolean power=false;

  /** True if it is a ultimax configuration */
  private boolean ultimax=false;

  /** The monitor where PLA attend a signal that changing value */
  public Monitor1 monitor=new Monitor1("PLA 82S100");

  /** A copy of actual C64 bus. */
  protected C64Bus bus;

  /** Cartridge expansion port */
  protected Cartridge exp;

  /** 8Kb of Ram memory address 0x0000 0x1FFF */
  protected Memory ram0;

  /** 8Kb of Ram memory address 0x2000 0x3FFF */
  protected Memory ram1;

  /** 8Kb of Ram memory address 0x4000 0x5FFF */
  protected Memory ram2;

  /** 8Kb of Ram memory address 0x6000 0x7FFF */
  protected Memory ram3;

  /** 8Kb of Ram memory address 0x8000 0x9FFF */
  protected Memory ram4;

  /** 8Kb of Ram mmemory address 0xA000 0xBFFF */
  protected Memory ram5;

  /** 8Kb of Ram memory address 0xC000 0xDFFF */
  protected Memory ram6;

  /** 8Kb of Ram memory address 0xE000 0xFFFF */
  protected Memory ram7;

  /** The 8Kb of Basic ROM */
  protected ROM basic;

  /** The 8Kb of Kernal ROM */
  protected ROM kernal;

  /** The 4Kb of Char ROM */
  protected ROM chargen;

  /** The 40h bytes of VIC I/O */
  protected VicII vic;

  /** The 20h bytes of SID I/O */
  protected Sid sid;

  /** The 1Kb of color RAM */
  protected ColorRAM color;

  /* The 10h bytes of Cia 1 */
  protected M6526 cia1;

  /** The 10h bytes of Cia2 */
  protected M6526 cia2;


  // external input signals

  /** Loram signal from M6510 in the C64 */
  protected int loram=1;                              // default is up

  /** Hiram signal from M6510 cpu in the C64 */
  protected int hiram=1;                              // default is up

  /** Charen signal from M6510 in the C64 */
  protected int charen=1;                             // default is up

  /** Exrom signal from Cartridge in the C64 */
  protected int exrom=1;                              // default is up

  /** Game signal from Cartridge in the C64 */
  protected int game=1;                               // default is up

  /** Va14 signal from Cia2 (inverted) in the C64 */
  protected int va14;

  /** Va15 signal from Cia2 (inverted) in the C64 */
  protected int va15;



  /** Table for Vic in bank 0 (000h-3FFFh) with char ROM activated */
  private readableBus[] vicT0R=new readableBus[0x4000>>8];

  /** Table for Vic in bank 0 (0000h-3FFFh) without char ROM activated */
  private readableBus[] vicT0=new readableBus[0x4000>>8];

  /** Table for Vic in bank 1 (4000h-7FFFh) */
  private readableBus[] vicT1=new readableBus[0x4000>>8];

  /** Table for Vic in bank 2 (8000h-BFFFh) with char ROM activated */
  private readableBus[] vicT2R=new readableBus[0x4000>>8];

  /** Table for Vic in bank 2 (8000h-BFFFh) (without char ROM activated */
  private readableBus[] vicT2=new readableBus[0x4000>>8];

  /** Table for Vic in bank 3 (C000h-FFFFh) */
  private readableBus[] vicT3=new readableBus[0x4000>>8];

  /** Table for cpu read, configuration 11111. */
  private readableBus[] cpuR11111=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 11110. */
  private readableBus[] cpuR11110=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 101x1. */
  private readableBus[] cpuR101x1=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 101x0. */
  private readableBus[] cpuR101x0=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 10001. */
  private readableBus[] cpuR10001=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 10000. */
  private readableBus[] cpuR10000=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 011x1. */
  private readableBus[] cpuR011x1=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 011x0. */
  private readableBus[] cpuR011x0=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 001x-00x0. */
  private readableBus[] cpuR001x_00x0=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 11101. */
  private readableBus[] cpuR11101=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 11100. */
  private readableBus[] cpuR11100=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 01001. */
  private readableBus[] cpuR01001=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 01000. */
  private readableBus[] cpuR01000=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 11001. */
  private readableBus[] cpuR11001=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration 11000. */
  private readableBus[] cpuR11000=new readableBus[0x10000>>8];

  /** Table for cpu read, configuration xx01. */
  private readableBus[] cpuRxx01=new readableBus[0x10000>>8];

  /** Table for cpu write, configuration all to ram */
  private writeableBus[] cpuWRam=new writeableBus[0x10000>>8];

  /** Table for cpu write, configuration to ram and I/O */
  private writeableBus[] cpuWIO=new writeableBus[0x10000>>8];

  /** Table for cpu write, configuration ultimax */
  private writeableBus[] cpuWUltimax=new writeableBus[0x10000>>8];

  /**
   * Construct a PLA82S100.
   *
   * @param bus the C64 bus where changing tables
   * @param exp the cartridge expansion port.
   * @param ram0 8Kb of Ram memory address 0x0000 0x1FFF
   * @param ram1 8Kb of Ram memory address 0x2000 0x3FFF
   * @param ram2 8Kb of Ram memory address 0x4000 0x5FFF
   * @param ram3 8Kb of Ram memory address 0x6000 0x7FFF
   * @param ram4 8Kb of Ram memory address 0x8000 0x9FFF
   * @param ram5 8Kb of Ram memory address 0xA000 0xBFFF
   * @param ram6 8Kb of Ram memory address 0xC000 0xDFFF
   * @param ram7 8Kb of Ram memory address 0xE000 0xFFFF
   * @param basic the 8Kb of Basic ROM
   * @param kernal the 8Kb of kernal ROM
   * @param chargen the 4Kb of char ROM
   * @param vic the 40h bytes of VIC I/O
   * @param sid the 20h bytes of SID I/O
   * @param color the 1Kbyte of color ram
   * @param cia1 the 10h bytes of CIA1
   * @param cia2 the 10h bytes of CIA2
   */
  public PLA82S100(C64Bus bus,
                   Cartridge exp,
                   Memory ram0, Memory ram1, Memory ram2, Memory ram3,
                   Memory ram4, Memory ram5, Memory ram6, Memory ram7,
                   ROM basic, ROM kernal, ROM chargen,
                   VicII vic, Sid sid, ColorRAM color,
                   M6526 cia1, M6526 cia2) {
    // make copy of C64 bus.
    this.bus=bus;

    // make copy of Cartridge expansion port
    this.exp=exp;

    // make a copy of C64 RAM memory
    this.ram0=ram0;
    this.ram1=ram1;
    this.ram2=ram2;
    this.ram3=ram3;
    this.ram4=ram4;
    this.ram5=ram5;
    this.ram6=ram6;
    this.ram7=ram7;

    // make a copy of ROMs
    this.basic=basic;
    this.kernal=kernal;
    this.chargen=chargen;

    // make a copy of I/O and Color
    this.vic=vic;
    this.sid=sid;
    this.color=color;
    this.cia1=cia1;
    this.cia2=cia2;

    buildCpuTables();
    buildVicTables();

    setName("PLA");               // use this name for the thread
    start();
  }

  /**
   * Build the table for Vic bus reading operation
   * Remember that Vic always read the RAM, except for bank 0 and 2 where it
   * see Char ROM in not Ultimax configutation.
   */
  public void buildVicTables() {
    int i;

    // insert RAM to all tables
    for (i=0; i<(0x2000>>8); i++) {
      vicT0R[i]=ram0;
      vicT0[i]=ram0;
      vicT1[i]=ram2;
      vicT2R[i]=ram4;
      vicT2[i]=ram4;
      vicT3[i]=ram6;
    }
    for (i=(0x2000>>8); i<(0x4000>>8); i++) {
      vicT0R[i]=ram1;
      vicT0[i]=ram1;
      vicT1[i]=ram3;
      vicT2R[i]=ram5;
      vicT2[i]=ram5;
      vicT3[i]=ram7;
    }
    // insert ROM
    for (i=(0x1000>>8); i<(0x2000>>8); i++) {
      vicT0R[i]=chargen;  // address 1000h-1FFFh
      vicT2R[i]=chargen;  // address 9000h-9FFFh
    }
  }

  /**
   * Build the table for Cpu bus reading and writing operation.
   * Remember that the available configuration come out from this signals:
   * LORAM, HIRAM, GAME, EXROM, CHAREN.
   */
  public void buildCpuTables() {
    int i;

    // insert value for address from 0000h to 1FFFh
    for (i=0; i<(0x2000>>8); i++) {
      cpuR11111[i]=ram0;
      cpuR11110[i]=ram0;
      cpuR101x1[i]=ram0;
      cpuR101x0[i]=ram0;
      cpuR10001[i]=ram0;
      cpuR10000[i]=ram0;
      cpuR011x1[i]=ram0;
      cpuR011x0[i]=ram0;
      cpuR001x_00x0[i]=ram0;
      cpuR11101[i]=ram0;
      cpuR11100[i]=ram0;
      cpuR01001[i]=ram0;
      cpuR01000[i]=ram0;
      cpuR11001[i]=ram0;
      cpuR11000[i]=ram0;

      cpuWRam[i]=ram0;
      cpuWIO[i]=ram0;
    }

    // insert value for address from 0000h to 0FFFh for Ultimax mode
    for (i=0; i<(0x1000>>8); i++) {
      cpuRxx01[i]=ram0;

      cpuWUltimax[i]=ram0;
    }

    // insert value for address from 1000h to 1FFFh for Ultimax mode
    for (i=0; i<(0x1000>>8); i++) {
     // cpuRxx01[i]=open;

     // cpuWUltiomax[i]=open;
    }

    // insert value for address from 2000h to 3FFFh
    for (i=(0x2000>>8); i<(0x4000>>8); i++) {
      cpuR11111[i]=ram1;
      cpuR11110[i]=ram1;
      cpuR101x1[i]=ram1;
      cpuR101x0[i]=ram1;
      cpuR10001[i]=ram1;
      cpuR10000[i]=ram1;
      cpuR011x1[i]=ram1;
      cpuR011x0[i]=ram1;
      cpuR001x_00x0[i]=ram1;
      cpuR11101[i]=ram1;
      cpuR11100[i]=ram1;
      cpuR01001[i]=ram1;
      cpuR01000[i]=ram1;
      cpuR11001[i]=ram1;
      cpuR11000[i]=ram1;
      //cpuRxx01[i]=open;

      cpuWRam[i]=ram1;
      cpuWIO[i]=ram1;
      //cpuUltimax[i]=open;
    }

    // insert value for address from 4000h to 5FFFh
    for (i=(0x4000>>8); i<(0x6000>>8); i++) {
      cpuR11111[i]=ram2;
      cpuR11110[i]=ram2;
      cpuR101x1[i]=ram2;
      cpuR101x0[i]=ram2;
      cpuR10001[i]=ram2;
      cpuR10000[i]=ram2;
      cpuR011x1[i]=ram2;
      cpuR011x0[i]=ram2;
      cpuR001x_00x0[i]=ram2;
      cpuR11101[i]=ram2;
      cpuR11100[i]=ram2;
      cpuR01001[i]=ram2;
      cpuR01000[i]=ram2;
      cpuR11001[i]=ram2;
      cpuR11000[i]=ram2;
      //cpuRxx01[i]=open;

      cpuWRam[i]=ram2;
      cpuWIO[i]=ram2;
      //cpuUltimax[i]=open;
    }

    // insert value for address from 6000h to 7FFFh
    for (i=(0x6000>>8); i<(0x8000>>8); i++) {
      cpuR11111[i]=ram3;
      cpuR11110[i]=ram3;
      cpuR101x1[i]=ram3;
      cpuR101x0[i]=ram3;
      cpuR10001[i]=ram3;
      cpuR10000[i]=ram3;
      cpuR011x1[i]=ram3;
      cpuR011x0[i]=ram3;
      cpuR001x_00x0[i]=ram3;
      cpuR11101[i]=ram3;
      cpuR11100[i]=ram3;
      cpuR01001[i]=ram3;
      cpuR01000[i]=ram3;
      cpuR11001[i]=ram3;
      cpuR11000[i]=ram3;
      //cpuRxx01[i]=open;

      cpuWRam[i]=ram3;
      cpuWIO[i]=ram3;
      //cpuWUltimax[i]=open;
    }

    // insert value for address from 8000h to 9FFFh
    for (i=(0x8000>>8); i<(0xA000>>8); i++) {
      cpuR11111[i]=ram4;
      cpuR11110[i]=ram4;
      cpuR101x1[i]=ram4;
      cpuR101x0[i]=ram4;
      cpuR10001[i]=ram4;
      cpuR10000[i]=ram4;
      cpuR011x1[i]=ram4;
      cpuR011x0[i]=ram4;
      cpuR001x_00x0[i]=ram4;
      cpuR11101[i]=exp;        // roml
      cpuR11100[i]=exp;        // roml
      cpuR01001[i]=ram4;
      cpuR01000[i]=ram4;
      cpuR11001[i]=exp;        // roml
      cpuR11000[i]=exp;        // roml
      cpuRxx01[i]=exp;         // roml

      cpuWRam[i]=ram4;
      cpuWIO[i]=ram4;
      //cpuWUltimax[i]=ram4;
    }

    // insert value for address from A000h to BFFFh
    for (i=(0xA000>>8); i<(0xC000>>8); i++) {
      cpuR11111[i]=basic;
      cpuR11110[i]=basic;
      cpuR101x1[i]=ram5;
      cpuR101x0[i]=ram5;
      cpuR10001[i]=ram5;
      cpuR10000[i]=ram5;
      cpuR011x1[i]=ram5;
      cpuR011x0[i]=ram5;
      cpuR001x_00x0[i]=ram5;
      cpuR11101[i]=basic;
      cpuR11100[i]=basic;
      cpuR01001[i]=exp;        // romh
      cpuR01000[i]=exp;        // romh
      cpuR11001[i]=exp;        // romh
      cpuR11000[i]=exp;        // romh
      //cpuRxx01[i]=open;

      cpuWRam[i]=ram5;
      cpuWIO[i]=ram5;
      //cpuWUltimax[i]=open;
    }

    // insert value for address from C000h to CFFFh
    for (i=(0xC000>>8); i<(0xD000>>8); i++) {
      cpuR11111[i]=ram6;
      cpuR11110[i]=ram6;
      cpuR101x1[i]=ram6;
      cpuR101x0[i]=ram6;
      cpuR10001[i]=ram6;
      cpuR10000[i]=ram6;
      cpuR011x1[i]=ram6;
      cpuR011x0[i]=ram6;
      cpuR001x_00x0[i]=ram6;
      cpuR11101[i]=ram6;
      cpuR11100[i]=ram6;
      cpuR01001[i]=ram6;
      cpuR01000[i]=ram6;
      cpuR11001[i]=ram6;
      cpuR11000[i]=ram6;
      //cpuRxx01[i]=open;

      cpuWRam[i]=ram6;
      cpuWIO[i]=ram6;
      //cpuWUltimax[i]=ram6;
    }

    // insert value for address from D000h to D3FFh (VIC II)
    for (i=(0xD000>>8); i<(0xD400>>8); i++) {
      cpuR11111[i]=vic;
      cpuR11110[i]=chargen;
      cpuR101x1[i]=vic;
      cpuR101x0[i]=chargen;
      cpuR10001[i]=vic;
      cpuR10000[i]=ram6;
      cpuR011x1[i]=vic;
      cpuR011x0[i]=chargen;
      cpuR001x_00x0[i]=ram6;
      cpuR11101[i]=vic;
      cpuR11100[i]=chargen;
      cpuR01001[i]=vic;
      cpuR01000[i]=chargen;
      cpuR11001[i]=vic;
      cpuR11000[i]=chargen;
      cpuRxx01[i]=vic;

      cpuWRam[i]=ram6;
      cpuWIO[i]=vic;
      cpuWUltimax[i]=vic;
    }

    // insert value for address from D400h to D7FFh (SID)
    for (i=(0xD400>>8); i<(0xD800>>8); i++) {
      cpuR11111[i]=sid;
      cpuR11110[i]=chargen;
      cpuR101x1[i]=sid;
      cpuR101x0[i]=chargen;
      cpuR10001[i]=sid;
      cpuR10000[i]=ram6;
      cpuR011x1[i]=sid;
      cpuR011x0[i]=chargen;
      cpuR001x_00x0[i]=ram6;
      cpuR11101[i]=sid;
      cpuR11100[i]=chargen;
      cpuR01001[i]=sid;
      cpuR01000[i]=chargen;
      cpuR11001[i]=sid;
      cpuR11000[i]=chargen;
      cpuRxx01[i]=sid;

      cpuWRam[i]=ram6;
      cpuWIO[i]=sid;
      cpuWUltimax[i]=sid;
    }

    // insert value for address from D800h to DBFFh (Color)
    for (i=(0xD800>>8); i<(0xDC00>>8); i++) {
      cpuR11111[i]=color;
      cpuR11110[i]=chargen;
      cpuR101x1[i]=color;
      cpuR101x0[i]=chargen;
      cpuR10001[i]=color;
      cpuR10000[i]=ram6;
      cpuR011x1[i]=color;
      cpuR011x0[i]=chargen;
      cpuR001x_00x0[i]=ram6;
      cpuR11101[i]=color;
      cpuR11100[i]=chargen;
      cpuR01001[i]=color;
      cpuR01000[i]=chargen;
      cpuR11001[i]=color;
      cpuR11000[i]=chargen;
      cpuRxx01[i]=color;

      cpuWRam[i]=ram6;
      cpuWIO[i]=color;
      cpuWUltimax[i]=color;
    }

    // insert value for address from DC00h to DCFFh (Color)
    for (i=(0xDC00>>8); i<(0xDD00>>8); i++) {
      cpuR11111[i]=cia1;
      cpuR11110[i]=chargen;
      cpuR101x1[i]=cia1;
      cpuR101x0[i]=chargen;
      cpuR10001[i]=cia1;
      cpuR10000[i]=ram6;
      cpuR011x1[i]=cia1;
      cpuR011x0[i]=chargen;
      cpuR001x_00x0[i]=ram6;
      cpuR11101[i]=cia1;
      cpuR11100[i]=chargen;
      cpuR01001[i]=cia1;
      cpuR01000[i]=chargen;
      cpuR11001[i]=cia1;
      cpuR11000[i]=chargen;
      cpuRxx01[i]=cia1;

      cpuWRam[i]=ram6;
      cpuWIO[i]=cia1;
      cpuWUltimax[i]=cia1;
    }

    // insert value for address from DD00h to DDFFh (Color)
    for (i=(0xDD00>>8); i<(0xDE00>>8); i++) {
      cpuR11111[i]=cia2;
      cpuR11110[i]=chargen;
      cpuR101x1[i]=cia2;
      cpuR101x0[i]=chargen;
      cpuR10001[i]=cia2;
      cpuR10000[i]=ram6;
      cpuR011x1[i]=cia2;
      cpuR011x0[i]=chargen;
      cpuR001x_00x0[i]=ram6;
      cpuR11101[i]=cia2;
      cpuR11100[i]=chargen;
      cpuR01001[i]=cia2;
      cpuR01000[i]=chargen;
      cpuR11001[i]=cia2;
      cpuR11000[i]=chargen;
      cpuRxx01[i]=cia2;

      cpuWRam[i]=ram6;
      cpuWIO[i]=cia2;
      cpuWUltimax[i]=cia2;
    }

    // insert value for address from DE00h to DEFFh (Color)
    for (i=(0xDE00>>8); i<(0xDF00>>8); i++) {
      cpuR11111[i]=exp;           // exp1
      cpuR11110[i]=chargen;
      cpuR101x1[i]=exp;           // exp1
      cpuR101x0[i]=chargen;
      cpuR10001[i]=exp;           // exp1
      cpuR10000[i]=ram6;
      cpuR011x1[i]=exp;           // exp1
      cpuR011x0[i]=chargen;
      cpuR001x_00x0[i]=ram6;
      cpuR11101[i]=exp;           // exp1
      cpuR11100[i]=chargen;
      cpuR01001[i]=exp;           // exp1
      cpuR01000[i]=chargen;
      cpuR11001[i]=exp;           // exp1
      cpuR11000[i]=chargen;
      cpuRxx01[i]=exp;            // exp1

      cpuWRam[i]=ram6;
      cpuWIO[i]=exp;              // exp1
      cpuWUltimax[i]=exp;         // exp1
    }

    // insert value for address from DF00h to DFFFh (Color)
    for (i=(0xDF00>>8); i<(0xE000>>8); i++) {
      cpuR11111[i]=exp;           // exp2
      cpuR11110[i]=chargen;
      cpuR101x1[i]=exp;           // exp2
      cpuR101x0[i]=chargen;
      cpuR10001[i]=exp;           // exp2
      cpuR10000[i]=ram6;
      cpuR011x1[i]=exp;           // exp2
      cpuR011x0[i]=chargen;
      cpuR001x_00x0[i]=ram6;
      cpuR11101[i]=exp;           // exp2
      cpuR11100[i]=chargen;
      cpuR01001[i]=exp;           // exp2
      cpuR01000[i]=chargen;
      cpuR11001[i]=exp;           // exp2
      cpuR11000[i]=chargen;
      cpuRxx01[i]=exp;            // exp2

      cpuWRam[i]=ram6;
      cpuWIO[i]=exp;              // exp2
      cpuWUltimax[i]=exp;         // exp2
    }

    // insert value for address from E000h to fFFFh
    for (i=(0xE000>>8); i<(0x10000>>8); i++) {
      cpuR11111[i]=kernal;
      cpuR11110[i]=kernal;
      cpuR101x1[i]=ram7;
      cpuR101x0[i]=ram7;
      cpuR10001[i]=ram7;
      cpuR10000[i]=ram7;
      cpuR011x1[i]=kernal;
      cpuR011x0[i]=kernal;
      cpuR001x_00x0[i]=ram7;
      cpuR11101[i]=kernal;
      cpuR11100[i]=kernal;
      cpuR01001[i]=kernal;
      cpuR01000[i]=kernal;
      cpuR11001[i]=kernal;
      cpuR11000[i]=kernal;
      cpuRxx01[i]=exp;            // romh

      cpuWRam[i]=ram7;
      cpuWIO[i]=ram7;
      //cpuWUltimax[i]=open;
    }
  }

  /**
   * Activate the right tables for the bus.
   * It attends that a signal changes value, so calculate the tables (if power is
   * on). Note that the first time the calculation is done even if signals are
   * not changed, but this is right because this is the first.
   */
  public void run() {
    while (true) {
      while (!power) {          // is there power?
        yield();                // no, attend power
      }
      chooseTables();           // calculate the right tables for bus
      ///monitor.opSignal2();
      monitor.opWait();         // attend a signal changes value
    }
  }

  /**
   * Choose the right tables for the vic and cpu bus view.
   * It reads the actual value of LORAM, HIRAM, EXROM, GAME and CHAREN signals
   * for making the decision.
   */
  public void chooseTables() {
    // suppose not ultimax mode
    ultimax=false;
    // calculate current memory configuration
    int config=(loram<<4)+(hiram<<3)+(exrom<<2)+(game<<1)+charen;

    switch (config) {
      case 0x1E:       // 11110
        bus.setTableCpu(cpuR11110, cpuWRam);
        break;
      case 0x1F:       // 11111
        bus.setTableCpu(cpuR11111, cpuWIO);
        break;
      case 0x14:       // 10100
      case 0x16:       // 10110 = 101x0
        bus.setTableCpu(cpuR101x0, cpuWRam);
        break;
      case 0x15:       // 10101
      case 0x17:       // 10110 = 101x1
        bus.setTableCpu(cpuR101x1, cpuWIO);
        break;
      case 0x10:       // 10000
        bus.setTableCpu(cpuR10000, cpuWRam);
        break;
      case 0x11:       // 10001
        bus.setTableCpu(cpuR10001, cpuWIO);
        break;
      case 0x0C:       // 01100
      case 0x0E:       // 01110 = 011x0
        bus.setTableCpu(cpuR011x0, cpuWRam);
        break;
      case 0x0D:       // 01101
      case 0x0F:       // 01111 = 011x1
        bus.setTableCpu(cpuR011x1, cpuWIO);
        break;
      case 0x04:       // 00100
      case 0x05:       // 00101
      case 0x06:       // 00110
      case 0x07:       // 00111 = 001xx
      case 0x00:       // 00000
      case 0x01:       // 00001 = 00x0x
        bus.setTableCpu(cpuR001x_00x0, cpuWRam);
        break;
      case 0x1C:       // 11100
        bus.setTableCpu(cpuR11100, cpuWRam);
        break;
      case 0x1D:       // 11101
        bus.setTableCpu(cpuR11101, cpuWIO);
        break;
      case 0x08:       // 01000
        bus.setTableCpu(cpuR01000, cpuWRam);
        break;
      case 0x09:       // 01001
        bus.setTableCpu(cpuR01001, cpuWIO);
        break;
      case 0x18:       // 11000
        bus.setTableCpu(cpuR11000, cpuWRam);
        break;
      case 0x19:       // 11001
        bus.setTableCpu(cpuR11001, cpuWIO);
        break;
      case 0x02:       // 00010
      case 0x03:       // 00011
      case 0x0A:       // 01010
      case 0x0B:       // 01011
      case 0x12:       // 10010
      case 0x1B:       // 11011 = xx01x Ultimax
        bus.setTableCpu(cpuRxx01, cpuWUltimax);
        ultimax=true;
        break;
    }

    config=(va15<<1)+va14;   // bank selection
    switch (config) {
      case 0x00:
        if (ultimax) bus.setTableVic(vicT0);
        else bus.setTableVic(vicT0R);
        break;
      case 0x01:
        bus.setTableVic(vicT1);
        break;
      case 0x02:        
        if (ultimax) bus.setTableVic(vicT2);
        else bus.setTableVic(vicT2R);
        break;
      case 0x03:
        bus.setTableVic(vicT3);
        break;
    }
  }

  /**
   * Notify a signal to the chip
   *
   * @param type the type of signal
   * @param value the value of the signal (0/1)
   */
  public void notifySignal(int type, int value) {
    switch (type) {
      case S_LORAM:
        loram=value;
        break;
      case S_HIRAM:
        hiram=value;
        break;
      case S_CHAREN:
        charen=value;
        break;
      case S_GAME:
        game=value;
        break;
      case S_EXROM:
        exrom=value;
        break;
      case S_VA14:
        va14=1-value;               // use the invert of the signal
        break;
      case S_VA15:
        va15=1-value;               // use the invert of the signal
        break;
      default:
        System.err.println("ERROR: an invalid "+type+
                           " signal was notify to PLA82S100");
    }
  }

  /**
   * Set a new expansion cartridge
   *
   * @param exp the cartridge
   */
  public void setExpansion(Cartridge exp) {
    this.exp=exp;
    buildCpuTables();               // rebuilt the cpu table
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
