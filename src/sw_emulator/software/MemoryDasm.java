/**
 * @(#)memoryState.java 2019/12/21
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
package sw_emulator.software;

/**
 * Memory cell for the disassembler
 * 
 * @author ice
 */
public class MemoryDasm {
  /** Address of memory (0..$FFFF) */  
  public int address;  
  
  /** Memory comment from dasm disassemble */
  public String dasmComment;
  
  /** Memory comment from user (if not null it surclass the dasm one) */
  public String userComment;
  
  /** Location defined by dasm disassemble */
  public String dasmLocation;
  
  /** Location defined by user */
  public String userLocation;
  
  /** True if the memory area is inside the read data to disassemble */
  public boolean isInside;
  
  /** True if this is code */
  public boolean isCode;
  
  /** True if this is data */
  public boolean isData;
}
