/**
 * @(#)Freeze.java 2022/03/15
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
package sw_emulator.swing.main;

import java.util.Objects;

/**
 * Freeze of source TXT image
 * 
 * @author ice
 */
public class Freeze implements Cloneable {
  /** Name of this freeze */  
  public String name="";  
  
  /** Text of this freeze */
  public String text="";
  
  /**
   * Clone the object
   * 
   * @return the cloned object
   */
  @Override
  public Object clone() {
    Freeze copy=new Freeze();
    copy.name=name;
    copy.text=text;
     
    return copy;
  }
    
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Freeze)) return false;
    
    Freeze freeze=(Freeze) obj;
     
    freeze.name=name;
    freeze.text=text;

    return true;
  }

  @Override
  public int hashCode() {
    return super.hashCode()+79*Objects.hashCode(name)+Objects.hashCode(text);
  }  
}
