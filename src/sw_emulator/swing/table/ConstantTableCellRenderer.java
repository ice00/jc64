/**
 * @(#)ConstantTableCelleRenderer.java 2024/03/01
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
import sw_emulator.swing.main.Constant;

/**
 * Table cell renderer for constants
 * 
 * @author stefano_tognon
 */
public class ConstantTableCellRenderer extends DefaultTableCellRenderer {
    Constant constant=new Constant();
    
    /**
     * Set up the constant to use
     * 
     * @param constant the constant
     */
    public void setConstant(Constant constant) {
        this.constant = constant;
    }
       
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
      Component c=super.getTableCellRendererComponent(table, value,
                                                      isSelected, hasFocus,
                                                      row, column);
      
      if (isSelected) return c;     
      
      setBackground(Color.white);
      if (row>=0 && column>0) {
        String comment=constant.comment[column-1][row];
      
        if (comment!=null && !"".equals(comment)) {
          setToolTipText(comment);
          setBackground(Color.green);
        } else {
            setToolTipText(null);
        }
      }
      return c;  
    }    
}
