package sw_emulator;

import sw_emulator.hardware.device.raster;
import sw_emulator.hardware.device.TV;


class TestTV
      implements Runnable
{
 
 raster tv;

 public void Main()
 {
  tv = new TV();
  }

 public void run()
 {
  Thread runner = Thread.currentThread();

  while(true)
       {
        for(int i=0;i<200;i++)
           {
            int randomClr=((int)(Math.random()*100.0))%raster.PALETTE_SIZE;
            for(int j=0;j<320;j++)
                tv.sendPixel(randomClr);
            }
       
        //tv.updateFrame();
        tv.displayCurrentFrame();
        tv.newFrame();

        try{
         runner.sleep(5);
         }catch(Exception e){System.err.println("Interrupted Exception.");}
        }

  }

  
  public static void main(String argv[])
  {
   TestTV prg = new TestTV();
   Thread runner = new Thread(prg);
   runner.start();
   }

}