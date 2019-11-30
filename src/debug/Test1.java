// test the disassembler of M6510

package sw_emulator;

import java.io.*;
import sw_emulator.software.cpu.M6510Dasm;
import sw_emulator.math.Unsigned;
import java.lang.*;

public class Test1 {
  M6510Dasm dis=new M6510Dasm();

  public Test1() {
    byte[] buf={0,0,0, 0, 0, 0, 0};
    int pos=0;
    long pc=644;
    for (int i =0; i<256; i++) {
      buf[0]=(byte)i;
      buf[1]=(byte)(i+1);
      buf[2]=(byte)(i+2);
      System.out.println(dis.dasm(buf, pos, pc)+"  "+dis.dcom());
    }
    System.out.println(Unsigned.done((byte)4));
    //try {
    //  wait(10000);
    //} catch(InterruptedException e) {}
  }

  public static void main(String[] args) {
    Test1 test1 = new Test1();
    test1.invokedStandalone = true;
  }
  private boolean invokedStandalone = false;
}