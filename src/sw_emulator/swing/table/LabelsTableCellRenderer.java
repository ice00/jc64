/**
 * @(#)LabelsTableCelleRenderer.java 2022/10/01
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
import sw_emulator.software.MemoryDasm;
import static sw_emulator.swing.table.DataTableModelLabels.COLUMNS.ID;


/**
 * Color the cells according to the type of code
 * 
 * @author ice
 */
public class LabelsTableCellRenderer extends DefaultTableCellRenderer {
    /** Memory dasm */
    MemoryDasm[] memory;

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
      
      if (isSelected) return c;      
    
      if (DataTableModelLabels.columns[table.convertColumnIndexToModel(column)]==ID) {
          MemoryDasm mem=memory[Integer.parseInt((String)value,16)];

          if (mem.isCode) c.setBackground(Color.green);
          else if (mem.isData) c.setBackground(Color.cyan);
          else if (mem.isGarbage) c.setBackground(Color.red);
          else c.setBackground(Color.LIGHT_GRAY);                  
          return c;
      }
                  
      c.setBackground(Color.white);

      return c;
    }    
    
    public void setMemory(MemoryDasm[] memory) {
      this.memory=memory;
    }
}
