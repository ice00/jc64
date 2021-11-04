/**
 * @(#)Relocate.java 2021/11/03
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
 * Relocate addresses definitions
 * 
 * @author ice
 */
public class Relocate {
  /** Starting position from where to relocate */  
  public int fromStart;
  
  /** Ending position from where to relocate*/
  public int fromEnd;
  
  /** Starting position to where to relocate */
  public int toStart;
  
  /** Ending position to where to relocate */
  public int toEnd;
  
  /**
   * Check if the reange are valid
   * 
   * @return true if range are valid
   */
  public boolean isValidRange() {
   if (fromEnd < fromStart) return false;
   if (toEnd < toStart) return false;
   
   if (fromEnd-fromStart!=toEnd-toStart) return false;

   return true;   
  }
}
