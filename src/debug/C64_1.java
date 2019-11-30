/**
 * @(#)C64.java 1999/09/19
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

package sw_emulator.hardware.machine;
//package debug;

import sw_emulator.hardware.powered;
import sw_emulator.hardware.signaller;
import sw_emulator.hardware.bus.C64Bus;
import sw_emulator.hardware.memory.dinamic;
import sw_emulator.hardware.memory.DRAM;
import sw_emulator.hardware.memory.ColorRAM;
import sw_emulator.hardware.memory.ROM;
import sw_emulator.hardware.Clock;
import sw_emulator.hardware.cartridge.Cartridge;
import sw_emulator.hardware.cartridge.GameCartridge;
import sw_emulator.hardware.cpu.M6510;
import sw_emulator.hardware.chip.PLA82S100;
import sw_emulator.hardware.chip.M6569;
import sw_emulator.hardware.chip.M6526;
import sw_emulator.hardware.chip.Sid;
import sw_emulator.hardware.io.C64M6510IO;
import sw_emulator.hardware.io.C64VicII_IO;
import sw_emulator.hardware.io.C64Cia1IO;
import sw_emulator.hardware.io.C64Cia2IO;
import sw_emulator.hardware.io.C64CartridgeIO;
import sw_emulator.hardware.device.C64Keyboard;
import sw_emulator.hardware.device.TV;
import sw_emulator.hardware.device.C64Form;
import sw_emulator.util.AndPort;
import sw_emulator.software.cartridge.FileCartridge;



/**
 * Debug the game cartridge
 */

/**
 * Emulate the Commodore 64 computer.
 *
 * @author Ice
 * @version 1.00 19/09/1999
 */
public class C64_1 implements powered{

  /**
   * Path where the roms images are stored
   */
  public static final String ROM_PATH="./rom/";

  /**
   * 8Kb of Ram memory address 0x0000 0x1FFF
   */
  protected DRAM ram0=new DRAM(8*1024, 0x0000);

  /**
   * 8Kb of Ram memory address 0x2000 0x3FFF
   */
  protected DRAM ram1=new DRAM(8*1024, 0x2000);

  /**
   * 8Kb of Ram memory address 0x4000 0x5FFF
   */
  protected DRAM ram2=new DRAM(8*1024, 0x4000);

  /**
   * 8Kb of Ram memory address 0x6000 0x7FFF
   */
  protected DRAM ram3=new DRAM(8*1024, 0x6000);

  /**
   * 8Kb of Ram memory address 0x8000 0x9FFF
   */
  protected DRAM ram4=new DRAM(8*1024, 0x8000);

  /**
   * 8Kb of Ram memory address 0xA000 0xBFFF
   */
  protected DRAM ram5=new DRAM(8*1024, 0xA000);

  /**
   * 8Kb of Ram memory address 0xC000 0xDFFF
   */
  protected DRAM ram6=new DRAM(8*1024, 0xC000);

  /**
   * 8Kb of Ram memory address 0xE000 0xFFFF
   */
  protected DRAM ram7=new DRAM(8*1024, 0xE000);

  /**
   * Dinamic memories that need refresh
   */
  protected dinamic[] dinamicMemories={
    ram0, ram1, ram2, ram3, ram4, ram5, ram6, ram7
  };

  /**
   * The 8Kbyte of Basic ROM memory
   */
  public ROM basic=new ROM(8*1024, 0xA000);

  /**
   * The 8Kbyte of Kernal ROM memory
   */
  public ROM kernal=new ROM(8*1024, 0xE000);

  /**
   * The 4Kbyte of Char generator ROM memory
   */
  public ROM chargen=new ROM(4*1024, 0xD000);

  /**
   * The bus of the C64
   */
  public C64Bus bus=new C64Bus();

  /**
   * The 1Kb of color ram
   */
  public ColorRAM color=new ColorRAM(1024, 0xD800, bus);

  /**
   * The 8Mhz clock signal
   */
  public Clock clock=new Clock(Clock.PAL);

  /**
   * The TV attached to the C64 Vic II output
   */
  public TV tv=new TV();

  /**
   * The application menu bar
   */
  public C64Form c64Form=new C64Form();

  /**
   * The M6569 Vic II
   */
  public M6569 vic=new M6569(clock.monitor, bus, C64Bus.V_VIC, null,
                             dinamicMemories, tv);

  /**
   * The Mos 6510 cpu
   */
  public M6510 cpu=new M6510(vic.intMonitor, bus, C64Bus.V_CPU, null);

  /**
   * The Mos SID chip
   */
  public Sid sid=new Sid(); // to modify

  /**
   * The cartridge port
   */
  public Cartridge exp=new Cartridge(null, vic.intMonitor, bus);

  /**
   * A game Cartridge
   */
  public GameCartridge gameExp;

  /**
   * The Cia 1 chip
   */
  public M6526 cia1=new M6526(M6526.PAL, vic.intMonitor, null);

  /**
   * The Cia2 chip
   */
  public M6526 cia2=new M6526(M6526.PAL, vic.intMonitor, null);

  /**
   * The PLA82S100 chip of C64
   */
  public PLA82S100 pla=new PLA82S100(bus, exp,
                                     ram0, ram1, ram2, ram3,
                                     ram4, ram5, ram6, ram7,
                                     basic, kernal, chargen,
                                     vic, sid,
                                     color, cia1, cia2);

  /**
   * The C64 keyboard
   */
  public C64Keyboard keyb=new C64Keyboard(null);

  /**
   * The IO signals of C64 cpu
   */
  public C64M6510IO cpuIO=new C64M6510IO(pla);

  /**
   * The 74LS08 And port with output pin 6.
   * This give AEC signal to Cpu.
   */
  protected AndPort and6=new AndPort(cpu, signaller.S_AEC, signaller.S_DMA,
                                     signaller.S_AEC);

  /**
   * The 74LS08 And port with output pin 3.
   * This give RDY signal to Cpu.
   */
  protected AndPort and3=new AndPort(cpu, signaller.S_BA, signaller.S_DMA,
                                     signaller.S_RDY);

  /**
   * The Vic IO signals connections
   */
  public C64VicII_IO vicIO=new C64VicII_IO(cpu, exp, and6, and3);

  /**
   * The Cia 1 IO signals connections
   */
  public C64Cia1IO cia1IO=new C64Cia1IO(cpu, exp, keyb.monitor, keyb.colLines);

  /**
   * The Cia 2 IO signals connections
   */
  public C64Cia2IO cia2IO=new C64Cia2IO(cpu, exp, pla);

  /**
   * The expansion IO signals connections
   */
  public C64CartridgeIO expIO=new C64CartridgeIO(cpu, pla, and6, and3);

  /**
   * Manage file cartridge
   */
  public FileCartridge fileCart=new FileCartridge(expIO, vic.intMonitor, bus);

  public C64_1() {
    initMemory();
    c64Form.addTV(tv);
    c64Form.setVisible(true);

    // read cartridge:
    fileCart.setFileName("/mnt/new/home/ice/dig_dug.crt");
    System.out.println(fileCart.readFile());
    System.out.println(fileCart.determineCart());
    gameExp=(GameCartridge)fileCart.getCartridge(0);
    gameExp.setIO(expIO);

    pla.setExpansion(gameExp);

    cpu.setIO(cpuIO);                 // set cpu IO signals
    vic.setIO(vicIO);                 // set vic IO signals
    keyb.setIO(cia1IO);               // set keyb IO signals
    exp.setIO(expIO);                 // set exp IO signals
    cia1.setIO(cia1IO);               // set cia 1 IO signals
    cia2.setIO(cia2IO);               // set cia 2 IO signals
    bus.setColor(color);              // set up color ram for Vic in bus

    kernal.change(0xfd84, (byte)0xa0);      // don't fill the memory
    kernal.change(0xfd85, (byte)00);

    System.out.println("Power ON the machine");
    powerOn();

    clock.setRealTime(1); ///debug: use slow clock

    System.out.println("Start the clock...");
    clock.startClock();       System.out.println("exit main");
  }

  public static void main(String[] args) {
   C64_1 c64 = new C64_1();
  }

  /**
   * Initialize the C64 Memories chip (RAM and ROMs).
   * If there's error, the program halt with error message.
   */
  public void initMemory() {
    System.out.println("Reading machine ROMs");
    if (!(basic.load(ROM_PATH+"basic.rom") &&
        kernal.load(ROM_PATH+"kernal.rom") &&
        chargen.load(ROM_PATH+"char.rom"))) {
      System.err.println("Error reading the ROM images.");
      System.exit(1);
    }
  }

  /**
   * Power on the electronic component in the machine
   */
  public void powerOn() {
    gameExp.powerOn();
     exp.powerOn();
    pla.powerOn();
    System.out.println(" Pla: power is on");

    cpu.powerOn();
    System.out.println(" Cpu: power is on");

    vic.powerOn();
    System.out.println(" VIC: power is on");
  }

  /**
   * Power off the electronic component in the machine
   */
  public void powerOff() {
    pla.powerOff();
    System.out.println("Pla: power is off");

    cpu.powerOff();
    System.out.println(" Cpu: power is off");

    vic.powerOff();
    System.out.println(" VIC: power is off");
  }
}