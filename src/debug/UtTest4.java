

package sw_emulator;

import sw_emulator.hardware.cpu.*;
import sw_emulator.hardware.bus.*;
import sw_emulator.hardware.memory.*;
import sw_emulator.util.*;
import sw_emulator.software.machine.*;

public class UtTest4 {//extends M6510 {

  public C64Dasm dasm;

  public ROM kernal;

  public ROM basic;
/**
  public UtTest4(Monitor monitor, Bus bus, ROM kernal, ROM basic) {
    dasm=new C64Dasm();
    this.monitor=monitor;
    this.bus=bus;
    this.kernal=kernal;
    this.basic=basic;
    start();
  }*/

/**
  public void decode() {
    if (regPC<0xE000)
     if (regPC>0xA000)
       ;///System.out.print(dasm.cdasm(basic.memory, regPC-1-0xA000, regPC-0xA000, regPC-1));
     else
       System.out.println(regPC);
    else ;///System.out.print(dasm.cdasm(kernal.memory, regPC-1-0xE000, regPC-0xE000, regPC-1));
    super.decode();
  }*/
}