/**
 * @(#)ColorTableCelleRenderer.java 2019/12/24
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
import sw_emulator.software.Disassembly;
import static sw_emulator.swing.table.DataTableModelMemory.COLUMNS.ID;

/**
 * Color the cells according to the type of code
 * 
 * @author ice
 */
public class MemoryTableCellRenderer extends DefaultTableCellRenderer {
    
    /** The disassembly with information to use */
    Disassembly disassembly;

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
      if (disassembly==null || disassembly.startAddress<0) return c;
      
      if (DataTableModelMemory.columns[table.convertColumnIndexToModel(column)]==ID) {
        if (disassembly.startMPR!=null)   {
            for (int i=0; i<disassembly.startMPR.length; i++) {
              if (row>=disassembly.startMPR[i] && row<=disassembly.endMPR[i]) {
                if (disassembly.memory[row].isCode) c.setBackground(Color.green);
                else if (disassembly.memory[row].isData) c.setBackground(Color.cyan);
                else if (disassembly.memory[row].isGarbage) c.setBackground(Color.red);
                else c.setBackground(Color.LIGHT_GRAY);                  
                return c;
              } 
            }
            c.setBackground(Color.white);
        } else {
            if (row<disassembly.startAddress || row>disassembly.endAddress) c.setBackground(Color.white);
            else if (disassembly.memory[row].isCode) c.setBackground(Color.green);
            else if (disassembly.memory[row].isData) c.setBackground(Color.cyan);
            else if (disassembly.memory[row].isGarbage) c.setBackground(Color.red);
            else c.setBackground(Color.LIGHT_GRAY);
          }
      }
      return c;
    }

    /**
     * Set the disassembly to use 
     * 
     * @param disassembly the disassembly to use
     */
    public void setDisassembly(Disassembly disassembly) {
      this.disassembly=disassembly;
    }
    
}
