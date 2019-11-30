/**
 * @(#)disassembler.java 1999/08/20
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

package sw_emulator.software.cpu;

import java.lang.String;

/**
 * The interface <code>disassembler</code> represents an object that can
 * disassembler a code for one CPU microprocessor.
 * To do this the method <code>dasm</code> is provided.
 * The <code>dcom</code> is also provided for giving comment to the instruction
 * being disassembled (it may be used for comment many specific location for one
 * hardware architecture that use that cpu).
 *
 * @author Ice
 * @version 1.00 20/08/1999
 */
public interface disassembler {
  /**
   * Return the mnemonic assembler instruction rapresent by passed code bytes.
   *
   * @param buffer the buffer containg the data
   * @param pos the actual position in the buffer
   * @param pc the program counter value associated to the bytes being address
   *           by the <code>pos</code> in the buffer
   * @return a string menemonic rapresentation of instruction
   */
  public String dasm(byte[] buffer, int pos, long pc);

  /**
   * Return a comment string for the passed instruction
   *
   * @param iType the type of instruction
   * @param aType the type of addressing used by instruction
   * @param addr the address value (if needed by this istruction type)
   * @param value the operation value (if needed by this instruction)
   * @return a comment string
   */
  public String dcom(int iType, int aType, long addr, long value);
}