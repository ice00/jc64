package debug;

import sw_emulator.hardware.device.raster;
import sw_emulator.hardware.device.TV;

import java.io.*;

class TestTV2
{
 raster tv;

 public TestTV2()
 {
  tv=new TV();
  byte[] charData = load("data/char.rom");
  write(charData);
  //tv.updateFrame();
  tv.displayCurrentFrame();
  }
 
 static byte[] load(String file)
 {
  byte[] data=null;
  try{
      File f=new File(file);
      BufferedInputStream is=new BufferedInputStream(new FileInputStream(f));        
      data = new byte[(int)f.length()];
      is.read(data);
      is.close(); 
   }catch(Exception e){
          System.err.println("Errore :="+e.getMessage());
          }
  return data;
  } 

 void write(byte[] charData)
 {

  int k=0;
  int err=0; 

  while(err<=8)
  { 
   for(int i=0;i<8;i++)
     {
     for(int j=0;j<40;j++)
        {
         try{
          write(charData[k+i+j*8]);
          }catch(Exception e)
                {
                // tv.flush();
                 err++;
                 break;
                 }
         }
      }
  k+=40*8;
  }

  }

 void write(byte value)
 {

  byte mask=(byte)-128;

  if((value&mask)!=0)
    {
     tv.sendPixel(raster.LIGHT_BLUE);
     }  
  else
     {
     tv.sendPixel(raster.BLUE);
     }

  mask=(byte)64;
  for(int i=0;i<7;i++)
     {
      if((value&mask)!=0){
        tv.sendPixel(raster.LIGHT_BLUE);
        }
      else
        {
        tv.sendPixel(raster.BLUE);
        }      
      mask>>=1;
      }
  }

 public static void main(String argv[])
 {
  TestTV2 test=new TestTV2();
  }

}