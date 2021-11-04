/**
 * @(#)Block.java 2021/11/04
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
 * A block of memory for disassembly
 *  
 * @author ice
 */
public class Block {
  /** Input buffer of data */  
  public byte[]  inB;
            
  /** Start position in buffer */
  public int startBuffer;
  
  /** End position in buffer*/
  public int endBuffer; 
  
  /** Start address in memory of the position in buffer */
  public int startAddress;
  
  /** End address in memory of the position in buffer */
  public int endAddress;
}
