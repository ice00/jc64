/**
 * @(#)ViceMemMapParser.java 2026/01/26
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

import java.io.*;
import java.util.regex.*;

/**
 * Convert the vice memory map to memory flags
 *
 * @author ice
 */
public class ViceMemMapParser {

  private static final Pattern LINE
          = Pattern.compile("^([0-9a-fA-F]{4}):\\s+([rwx-]{3})\\s+([rwx-]{3})\\s+([rwx-]{3}).*$");

  /**
   * Parse the vice memory map to get the memory flags
   *
   * @param filename the file name
   * @return the memory flags
   * @throws IOException if there are i/o errors
   */
  public static MemoryFlags parseViceMemMap(String filename) throws IOException {
    byte[] mem = new byte[65536];

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;

      while ((line = br.readLine()) != null) {

        // Skip all that not starts with "xxxx:"
        if (!line.matches("^[0-9A-Fa-f]{4}:.*")) continue;
        
        Matcher m = LINE.matcher(line);
        if (!m.matches()) continue;

        int address = Integer.parseInt(m.group(1), 16);

        String ioFlags = m.group(2);  // ignored
        String romFlags = m.group(3); // ignored
        String ramFlags = m.group(4); // *** coloum RAM ***

        byte sidFlags = 0;

        if (ramFlags.charAt(0) == 'r') {
          sidFlags |= memoryState.MEM_READ;
        }
        if (ramFlags.charAt(1) == 'w') {
          sidFlags |= memoryState.MEM_WRITE;
        }
        if (ramFlags.charAt(2) == 'x') {
          sidFlags |= memoryState.MEM_EXECUTE;
        }

        mem[address] = sidFlags;
      }
    }

    return new MemoryFlags(mem);
  }

}
