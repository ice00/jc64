/**
 * @(#)Shared.java 2021/04/17
 *
 * ICE Team free software group
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
package sw_emulator.swing;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import javax.swing.JTable;
import javax.swing.JViewport;

/**
 * Contains all shared object accessible by the application
 * 
 * @author ice
 */
public class Shared {
  /** List of used frames. Each frame must register here */  
  public static ArrayList framesList=new ArrayList();   
  
  /** Version of the application */
  public static final String VERSION="1.7";
 
  /** Instance UUID */
  public static final UUID uuid=UUID.randomUUID();
  
  /**
   * Convert a unsigned short (containing in a int) to Exe upper case 4 chars
   *
   * @param value the short value to convert
   * @return the exe string rapresentation of byte
   */
  protected static String ShortToExe(int value) {
    int tmp=value;

    if (value<0) return "????";
    
    String ret=Integer.toHexString(tmp);
    int len=ret.length();
    switch (len) {
      case 1:
        ret="000"+ret;
        break;
     case 2:
        ret="00"+ret;
        break;
     case 3:
        ret="0"+ret;
        break;
    }
    return ret.toUpperCase(Locale.ENGLISH);
  }      
  
  public static void scrollToCenter(JTable table, int rowIndex, int vColIndex) {
    if (!(table.getParent() instanceof JViewport)) {
      return;
    }
    JViewport viewport = (JViewport) table.getParent();
    Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
    Rectangle viewRect = viewport.getViewRect();
    rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);

    int centerX = (viewRect.width - rect.width) / 2;
    int centerY = (viewRect.height - rect.height) / 2;
    if (rect.x < centerX) {
      centerX = -centerX;
    }
    if (rect.y < centerY) {
      centerY = -centerY;
    }
    rect.translate(centerX, centerY);
    viewport.scrollRectToVisible(rect);
  }
}
