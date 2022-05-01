/**
 * @(#)MultiTableCelleRenderer.java 2022/04/30
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
 * Color the cells according to the multi char
 * 
 * @author ice
 */
public class MultiTableCellRenderer extends DefaultTableCellRenderer {  
    /** Background color */
    Color background;
            
    /** Foreground color */
    Color foreground;
    
    /** Common 1 color */
    Color common1;
    
    /** Common 2 color */
    Color common2;

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
      
      switch ((int)value) {
        case 0:
          c.setBackground(background);
          c.setForeground(background);
          break;
        case 1:
          c.setBackground(foreground);
          c.setForeground(foreground);
          break;
        case 2:
          c.setBackground(common1);
          c.setForeground(common1);
          break;  
        case 3:  
          c.setBackground(common2);
          c.setForeground(common2);  
          break;
      }
      return c;
    }

    /**
     * Set the disassembly to use 
     * 
     * @param background background color
     * @param foreground foreground color
     * @param common1 common 1 color
     * @param common2 common 2 color
     */
    public void setup(Color background, Color foreground, Color common1, Color common2) {
      this.background=background;
      this.foreground=foreground;
      this.common1=common1;
      this.common2=common2;
    }
    
}
