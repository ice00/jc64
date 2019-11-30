// test the disassembler of C64
 
package sw_emulator;

import sw_emulator.software.machine.*;
import java.io.*;

public class Test2 {

 public C64Dasm dis=new C64Dasm();

 public FileInputStream fi;
 public FileOutputStream fo;
 public byte[] buffer=new byte[123456];
 public String result;
 public File fin;
 public File fon;

 public Test2() {
   int b;
   int i;
 try {
   try {
     fin=new File("/home/ice/Download/sid/Matt_Gray/Driller.sid");
     fon=new File("/home/ice/driller.lst");
     System.out.println(fin.exists());
     System.out.println(fin.getName());
     System.out.println(fin.getPath());
     fi=new FileInputStream(fin);
     fo=new FileOutputStream(fon);
     b=0;
     i=0x900-0x7c-2;
     while (b!=-1) {
       b=fi.read();
       buffer[i]=(byte)b;
       i++;
     }
     System.out.println("In Buffer readed");
    { fi.read(buffer, 4, 0xc8);}
     result=null;//dis.cdasm(buffer, 0x900, 0x1600, 0x900);
     System.out.println("Out Buffe created");
     System.out.println(result);
     for ( i=0; i<result.length(); i++)
       fo.write((int)result.charAt(i));
   } catch (FileNotFoundException e) {}
   }
     catch (IOException e1) {}
 }

 public static void main(String[] args) {
  Test2 test2 = new Test2();
  test2.invokedStandalone = true;
 }
 private boolean invokedStandalone = false;
}