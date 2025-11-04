/**
 * @(#)XRefUIConstants 2025/11/02
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
 * Central configuration class for UI parameters
 * 
 * @author ice
 */
public class XRefUIConstants {
   /**
    * Utility class, not instantiable 
    */
    private XRefUIConstants() {} 
    
    // Tooltip timing
    
    /** Milliseconds delay before showing a tooltip when mouse is over an address */
    public static final int TOOLTIP_SHOW_DELAY_MS = 600;
    
    /** Milliseconds delay before automatically hide the tooltip*/
    public static final int TOOLTIP_AUTO_HIDE_DELAY_MS = 6000;
    
    /** Milliseconds delay before forcing the tooltip hiding afet a soecific actions */
    public static final int TOOLTIP_FORCE_HIDE_DELAY_MS = 100;
    
    /** Milliseconds delay for native Swing tooltip for the dismiss after moving */
    public static final int NATIVE_TOOLTIP_DISMISS_DELAY_MS = 6000;
    
    /** Milliseconds delay for native Swing tooltip for the initial delay */
    public static final int NATIVE_TOOLTIP_INITIAL_DELAY_MS = 500;   
    
    /** Type width for column in table */
    public static final int COLUMN_TYPE_WIDTH = 60;
    
    /** Address width for column in table */
    public static final int COLUMN_ADDRESS_WIDTH = 80;
    
    /** Instruction width for column in table */
    public static final int COLUMN_INSTRUCTION_WIDTH = 150;
    
     /** Context width for column in table */
    public static final int COLUMN_CONTEXT_WIDTH = 110;
}
