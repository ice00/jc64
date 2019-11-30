/**
 * @(#)TV.java 2000/01/21
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

package sw_emulator.hardware.device;

import sw_emulator.hardware.device.raster;

import java.awt.*;
import java.awt.image.*;


/**
 * TV is a class which display on the screen the output of the VIC.
 * In my idea TV behaves like a printer with a carriage which
 * moves every time the VIC send a pixel.
 *
 *@author Michele Caira  caira_mik@hotmail.com
 *@version 1.2 28/4/2000
 */

public class TV
	     extends Canvas
	     implements raster
{
 
 /**
  * Width of video-buffer. 
  */
 public static final int SCREEN_W = 405;//320;
 /**
 * Height of video-buffer. 
 */
 public static final int SCREEN_H = 284;//200;
 /**
 * Size of video-buffer. 
 */
 public static final int SCREEN_SIZE = SCREEN_W*SCREEN_H;

 /**
  * Create a specific ColorModel for C64.
  */ 
 public static ColorModel createC64colorModel()
 {
  byte[] red =   {0,-1,-83,115,-67,115, 66, -1, -56,-124,  -1,123,-108,-91,-100,-50};
  byte[] green = {0,-1, 82, -9,107,-25, 66, -1,-114,  99,-100,123,-108, -1,-100,-50};
  byte[] blue =  {0,-1, 74, -9,-67,115,-58,123,  47,  57,-100,123,-108,-91,  -9,-50};

  return new IndexColorModel(8,16,red,green,blue); 
  }
 
 /**
  * The carriage mentioned below. 
  */ 
 private int carriage;	

 /**
  * The video-buffer.
  */	
 private int[]             screen;
 private Image             image;	
 private MemoryImageSource source;

 
 /**
  * The color used as background.
  */
 protected int backgroundClr;

 /**
  * Variabili per la visualizzazione 'ritardata'.
  */
 protected int updateDelay;
 protected int timerDelay;

 public TV()
 {
  super();

  backgroundClr=BLUE;
  carriage=-1;	

  updateDelay=0;
  timerDelay=-1; 

  screen=new int[SCREEN_W*SCREEN_H];
  clear();
  source=new MemoryImageSource
        (SCREEN_W,SCREEN_H,createC64colorModel(),screen,0,SCREEN_W);
  source.setAnimated(true);
  image=createImage(source);
 }
   
 public void paint(Graphics g)
 {
  update(g); 
  } 
  
 public void update(Graphics g)
 {
  g.drawImage(image,0,0,getWidth(),getHeight(),this);
  }

 /**
  * Clear the video-buffer with the background color.
  */
 public final void clear()
 {
  for(int i=0;i<SCREEN_SIZE;i++)
      screen[i]=backgroundClr;
  }

 /**
 *  Set the background color. 
 *
 *@param backgroundClr the palette-index of the background color. 
 */
 public final void setBackgroundClr(int backgroundClr)
 {
  if(backgroundClr>-1 && backgroundClr<PALETTE_SIZE)
     this.backgroundClr=backgroundClr;
  }

 /**
 *  Return the palette-index of the background color. 
 */
 public final int getBackgroundClr()
 {
  return backgroundClr;
  }

 /**
  * Set the update delay. 
  */
 public final void setUpdateDelay(int updateDelay)
 {
  if(updateDelay>-1)
     this.updateDelay=updateDelay;
  }

 /**
  * Simulate the scroll of a video-page.
  * This will scroll a single line.
  */
  public void scroll()
  {
   System.arraycopy(screen,SCREEN_W,screen,0,63680); //SCREEN_SIZE-SCREEN_W := 63680
   int pos=SCREEN_SIZE-1;
   for(int i=0;i<SCREEN_W;i++)
       screen[pos--]=backgroundClr;
  }
  
  /**
   * Scroll the video-page.
   *
   * @param n the number of lines to scroll. 
   */
  public void scroll(int n)
  {
   int jump=(n<<8)+(n<<6);
   System.arraycopy(screen,jump,screen,0,screen.length-jump);
   int pos=SCREEN_SIZE-1;
   for(int i=0;i<jump;i++)
       screen[pos--]=backgroundClr;
  }

  /**
   * Scroll 8 lines. It could be useful in the text-mode.
   */
  public void scroll8()
  {
   System.arraycopy(screen,0xA00,screen,0,0xF000);
   int pos=SCREEN_SIZE-1;
   for(int i=0;i<0xA00;i++)
       screen[pos--]=backgroundClr;
   }


 /**
  * Move the carriage to next line.
  * If the carriage exceds the screen size the frame is displayed.
  */
 public void flush()
 {
  carriage+=(SCREEN_W-((++carriage)%SCREEN_W));
  if(carriage>=SCREEN_SIZE)
     newFrame();
  }

 /**
 * Move foward the carriage of the specified positions.
 * If the carriage exceds the screen size the frame is displayed.
 */
 public void flush(int steps)
 {
  carriage+=steps;
  if(carriage>=SCREEN_SIZE)
     newFrame();
  }


 //----------------------INTERFACE RASTER----------------------------

 /**
 *  Copy the pixel sent by the VIC in the video-buffer. 
 *
 * @param pixel the pixel sent by the VIC.
 */  
 public int sendPixel(int pixel)
 {
  try{
      screen[++carriage]=pixel;
     }catch(ArrayIndexOutOfBoundsException e)
           {
            System.err.println("TV - sendPixel(int) - ArrayIndexException caught:"+carriage+" > "+SCREEN_SIZE);	 
            return -1;
            }
  return carriage;
  }

 /**
 *  Copy  pixels sent by the VIC in the video-buffer. 
 *
 * @param pixels pixels sent by the VIC
 */  
 public int sendPixel(int[] pixels)
 {
  try{
      System.arraycopy(pixels,0,screen,carriage+1,pixels.length);
      carriage+=pixels.length; 
   }catch(ArrayIndexOutOfBoundsException e)
 	 {
 	  System.err.println("TV - sendPixel(int[]) - ArrayIndexException caught:"+carriage+" > "+SCREEN_SIZE);	 
          return -1;
 	  }
  return carriage;         
  }

 /**
 *  Copy  pixels sent by the VIC in the video-buffer. 
 *
 * @param pixels pixels sent by the VIC.
 * @param start  first value to copy from the pixels buffer.
 */
 public int sendPixel(int[] pixels,int start)
 {
  try{
      System.arraycopy(pixels,start,screen,carriage+1,pixels.length-start);
      carriage+=(pixels.length-start);
   }catch(ArrayIndexOutOfBoundsException e)
 	  {
 	   System.err.println("TV - sendPixel(int[],int,int) - ArrayIndexException caught:"+carriage+" > "+SCREEN_SIZE);	 
           return -1;
 	  }
  return carriage;
  } 

 /**
 *  Copy  pixels sent by the VIC in the video-buffer. 
 *
 * @param pixels pixels sent by the VIC.
 * @param start  first value to copy from the pixels buffer.
 */
 public int sendPixel(int[] pixels,int start, int offset)
 {
  try{
      System.arraycopy(pixels,start,screen,carriage+1,offset);
      carriage+=offset;
   }catch(ArrayIndexOutOfBoundsException e)
 	   {
 	    System.err.println("TV - sendPixel(int[],int,int) - ArrayIndexException caught:"+carriage+" > "+SCREEN_SIZE);	 
          return -1;
 	    }
  return carriage;
  } 

 public int sendPixel8(int[] pixels)
 {
  try{
      System.arraycopy(pixels,0,screen,carriage+1,0x08);
      carriage+=0x08;
   }catch(ArrayIndexOutOfBoundsException e)
         {
          System.err.println("TV - sendPixel8(int[]) - ArrayIndexException caught:"+carriage+" > "+SCREEN_SIZE);        
          return -1;
          }
  return carriage;
  }

 public int sendPixel8(int[] pixels, int start)
 {
  try{
      System.arraycopy(pixels,start,screen,carriage+1,0x08);
      carriage+=0x08;
   }catch(ArrayIndexOutOfBoundsException e)
         {
          System.err.println("TV - sendPixel8(int[],int) - ArrayIndexException caught:"+carriage+" > "+SCREEN_SIZE);        
          return -1;
          }
  return carriage;
  }

 /**
  * Display on the screen the current frame.
  */
 public final void displayCurrentFrame()
 {
  if(++timerDelay==updateDelay)
     {
     source.newPixels();
     repaint();
     timerDelay=-1;
     }
  }

  /**
  * Reset variables. 
  */  
 public final void newFrame()
 {
  carriage=-1; 
  displayCurrentFrame();
  }
  
}//TV