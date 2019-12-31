/**
 * @(#)Option.java 2019/12/01
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

import sw_emulator.software.cpu.M6510Dasm;
import sw_emulator.software.machine.C64Dasm;

/**
 * Store all the options about the program
 * 
 * @author ice
 */
public class Option {
  /** Mode of the illegal opcode in preview */
  public byte illegalOpcodeModePreview = M6510Dasm.MODE1;
        
  /** Uper case for opcode in preview */
  public boolean  opcodeUpperCasePreview = true;
  
  /** Language for comment in preview */
  public byte commentLanguagePreview = C64Dasm.LANG_ENGLISH;
  
  /** Use as code (not data) if memory is undefined */
  public boolean useAsCode = true;
  
  /** Erase Dasm comment when convering to data*/
  public boolean eraseDComm = true;
  
  /** Max length of a label */
  public int maxLabelLength=25;
  
  
  
  /** Psid init song label to use as user defined */
  public String psidInitSongsLabel = "initSongs";
  
  /** Psid play sounds label to use as user definied */
  public String psidPlaySoundsLabel = "playSound";
  
  /** Sid low frequency table label */
  public String sidFreqLoLabel = "frequencyLo";
  
  /** Sid high frequency table label */
  public String sidFreqHiLabel = "frequencyHi";

}
