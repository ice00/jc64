/**
 * @(#)dinamic.java 1999/11/03
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

package sw_emulator.hardware.memory;

/**
 * The interface <code>dinamic</code> rapresent the refresh of rows of a dinamic
 * RAM memory.
 * Use the <code>refreshLine/code> to make the refresh of a line.
 *
 * @author Ice
 * @version 1.00 03/11/1999
 */
public interface dinamic {
  /**
   * Refresh the line <code>lineNumber</code> of a dinamic memory.
   *
   * @param lineNumber the number of line to refresh
   */
  public void refreshLine(int lineNumber);
}