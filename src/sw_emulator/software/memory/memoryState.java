/**
 * @(#)memoryState.java 2003/10/13
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
package sw_emulator.software.memory;


/**
 * The interface <code>memoryState</code> represents the internal
 * state of a memory.
 *
 * Some definitions are taken from Sidln source
 *
 * @author Ice
 * @version 1.00 13/10/2003
 */
public interface memoryState {
  public final int MEM_NONE          = 0x00;
  public final int MEM_READ          = 0x01;
  public final int MEM_WRITE         = 0x02;
  public final int MEM_EXECUTE       = 0x04;
  public final int MEM_READ_FIRST    = 0x10;
  public final int MEM_WRITE_FIRST   = 0x20;
  public final int MEM_EXECUTE_FIRST = 0x40;
  public final int MEM_SAMPLE        = 0x80;
  
  /**
   * Get the memory state as an array of SIDLN flags
   *
   * @param startAddress the starting address of the memory to return
   * @param endAddress the ending address of memory to return  
   * @return the state of memory as a array of SIDLN flags from 
   *         <code>startAddress</code> and <code>endAddress</code>
   */
  public byte[] getMemoryState(int startAddress, int endAddress);
}
