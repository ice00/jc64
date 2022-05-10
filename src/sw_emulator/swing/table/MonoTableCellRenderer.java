/**
 * @(#)MonoTableCelleRenderer.java 2022/04/30
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
package sw_emulator.swing.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Color the cells according to the mono char
 * 
 * @author ice
 */
public class MonoTableCellRenderer extends DefaultTableCellRenderer {  
    /** Background color */
    Color background;
            
    /** Foreground color */
    Color foreground;

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row,
                                                 int column) {
        
      Component c=super.getTableCellRendererComponent(table, value,
                                                      isSelected, hasFocus,
                                                      row, column);
      
      if (value==null) return c;
      if (isSelected) return c;      
      
      if ((int)value==1) {
        c.setBackground(foreground);
        c.setForeground(foreground);
      } else {
        c.setBackground(background);  
        c.setForeground(background);
      }
      return c;
    }

    /**
     * Set the disassembly to use 
     * 
     * @param background background color
     * @param foreground foreground color
     */
    public void setup(Color background, Color foreground) {
      this.background=background;
      this.foreground=foreground;
    }
    
}
