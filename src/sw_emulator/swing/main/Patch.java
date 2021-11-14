/**
 * @(#)Patch.java 2021/11/14
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

/**
 * A patch for the mmemory
 * 
 * @author ice
 */
public class Patch {
  /** Address to patch */
  public int address;

  /** Value to insert */
  public int value; 
  
  /**
   * Clone the object
   * 
   * @return the cloned object
   */
  @Override
  public Object clone() {
    Patch copy=new Patch();
    copy.address=address;
    copy.value=value;
     
    return copy;
  }
    
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Patch)) return false;
    
    Patch patch=(Patch) obj;
     
    if (patch.address!=address) return false;
    if (patch.value!=value) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return super.hashCode()+79*address+71*value;
  }   
}
