/**
 * @(#)raster.java 2000/01/21
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

public interface raster
{
 /**
  * @author Michele Caira   caira_mik@hotmail.com
  * @version 1.2 28/4/2000
  */ 

 /**
  * The size of C64 palette.
  */	
 public int PALETTE_SIZE = 16;
	
 /**
  * The C64 palette.
  */
 public int BLACK      = 0x00,
            WHITE      = 0x01,
            RED        = 0x02,
            CYAN       = 0x03,
            PINK       = 0x04,
            GREEN      = 0x05,
            BLUE       = 0x06,
            YELLOW     = 0x07,
            ORANGE     = 0x08,
            BROWN      = 0x09,
            LIGHT_RED  = 0x0A,
            DARK_GRAY  = 0x0B,
            MEDIUM_GRAY= 0x0C,
            LIGHT_GREEN= 0x0D,
            LIGHT_BLUE = 0x0E,
            LIGHT_GRAY = 0x0F;
           

 /**
  * Send a pixel to the raster.
  * Return -1 if something fails. 
  */
 public int sendPixel(int pixel);
 public int sendPixel(int[] pixels);
 public int sendPixel(int[] pixels, int start);
 public int sendPixel(int[] pixels, int start, int offset);
 public int sendPixel8(int[] pixels);
 public int sendPixel8(int[] pixels, int start);

 /**
  * Tell raster that a new frame is processing.
  */
 public void newFrame();

 /**
  * Ask raster to display the current output.
  * Notice that this function is called by newFrame().
  */
 public void displayCurrentFrame();
   

}//raster