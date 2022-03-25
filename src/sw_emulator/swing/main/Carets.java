/**
 * @(#)Carets.java 2022/03/24
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

import java.util.ArrayList;
import sw_emulator.software.MemoryDasm;

class Caret {
   /** Starting caret position */  
   public int start;
   
   /** Ending caret position */
   public int end;
   
   /** Memory associated with those caret interval */
   MemoryDasm memory;
}

/**
 * Carets to memory position
 * 
 * @author ice
 */
public class Carets {        
  /** List of caret */  
  ArrayList<Caret> list=new ArrayList();
       
   /** Offset to use for shift */
   private int offset=0;
  
  /**
   * Clear the actual list
   */
  public void clear() {
    list.clear();
    this.offset=0;
  }
  
  /**
   * Set an offset to use for shift carets position
   * 
   * @param offset the offset to use in adding a caret
   */
  public void setOffset(int offset) {
    this.offset=offset;
  }
  
  /**
   * Add this entry into the list
   * 
   * @param start the starting caret position
   * @param end the ending caret position
   * @param memory the memory associated with those postions
   */

  public void add(int start, int end, MemoryDasm memory) {
     Caret caret=new Caret();
     
     caret.start=start+offset;
     caret.end=end+offset;
     caret.memory=memory;
     
     list.add(caret);
  }
  
  /**
   * Get the memory associated with that postion or null
   * 
   * @param position the postion to search
   * @return the memory associated with that postion
   */
  public MemoryDasm getMemory(int position) {
    for (Caret caret: list) {
      if (position>=caret.start && position<=caret.end) return caret.memory;  
    }  
    
    return null;
  }
  
  /**
   * Get (start) position that has that memory 
   * 
   * @param memory the memory to search
   * @return the position or -1
   */
  public int getPosition(MemoryDasm memory) {
    for (Caret caret: list) {
      if (caret.memory==memory) return caret.start;        
    }  
        
     return -1;
  }
}
