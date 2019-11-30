// test M6510 emulation in a simple C64 memory map

package sw_emulator;

import sw_emulator.hardware.cpu.*;
import sw_emulator.hardware.memory.*;
import sw_emulator.hardware.bus.*;
import sw_emulator.hardware.*;

public class Test4 {

 /**
   * Path where the roms images are stored
   */
 public static final String ROM_PATH="e:\\programmi\\jbuilder2\\myprojects\\sw_emulator\\";

  public C64Bus bus=null;//new C64Bus(null, null, null);

 /**
  * The 64Kbyte of ram memory
  */
 public Memory ram=new Memory(64*1024, 0);

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
  * The 1Kb of color ram
  */
 public ColorRAM color=new ColorRAM(1024, 0xD800, bus);

 /**
  * The 8Mhz clock signal
  */
 public Clock clock=new Clock(Clock.PAL);

 /**
  * The Motorola 6510 cpu
  */
 ///public UtTest4 cpu=new UtTest4(clock.monitor, bus, kernal, basic);

  /**
   * Table for read functions pointers
   */
  protected readableBus[] rT;

  /**
   * Table for write functions pointers
   */
  protected writeableBus[] wT;

 /**
  * Initialize the C64 Memories chip (RAM and ROMs).
  * If there's error, the program halt with error message.
  */
 public void initMemory() {
   if (!(basic.load(ROM_PATH+"Basic.rom") &&
       kernal.load(ROM_PATH+"Kernal.rom") &&
       chargen.load(ROM_PATH+"Char.rom"))) {
     System.err.println("Error reading the ROM image.");
     System.exit(1);
   }
 }


 public Test4() {
   int i;

   initMemory();
   wT=new writeableBus[0x10000>>8];
   rT=new readableBus[0x10000>>8];
   for (i=0; i<(0x10000>>8); i++) {
     wT[i]=ram;
     rT[i]=ram;
   }

   for (i=(0xA000>>8); i<(0xC000>>8); i++)
    rT[i]=basic;

   for (i=(0xE000>>8); i<(0x10000>>8); i++)
     rT[i]=kernal;

   if (rT[255]==kernal)
     System.out.println("OK");

   bus.setTableCpu(rT, wT);
   clock.setRealTime(1000);
   clock.startClock();
   ///cpu.powerOn();
   while (true) {
     //System.out.println(cpu.regPC);
   }

 }

 public static void main(String[] args) {
  Test4 test4 = new Test4();
 }


}