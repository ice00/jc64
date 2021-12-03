/**
 * @(#)ConstantTableCelleRenderer.java 2021/05/15
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
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import sw_emulator.software.MemoryDasm;
import sw_emulator.software.cpu.M6510Dasm;
import sw_emulator.swing.main.Constant;


/**
 * Validate the cells with some rules
 * 
 * @author ice
 */
public class ConstantCellEditor extends DefaultCellEditor {
  private final JTextField textField;  
  private static final Border red = new LineBorder(Color.red);
  private static final Border black = new LineBorder(Color.black);
  private Constant constant;
  private MemoryDasm[] memories;

  public ConstantCellEditor(JTextField textField) {
    super(textField);
    this.textField=textField;
  }
  
  public void setCostant(Constant constant, MemoryDasm[] memories) {
    this.constant=constant;   
    this.memories=memories;
  }

  @Override
  public boolean stopCellEditing() {

    String actual=textField.getText();
    
    if (constant==null) return super.stopCellEditing();
    
    if (actual==null || "".equals(actual)) return super.stopCellEditing();
    
    String tmp=actual.toUpperCase();
    for (String val: M6510Dasm.mnemonics) {
      if (tmp.equals(val)) {
        textField.setBorder(red);
        return false;  
      }
    }

    if (!constant.isAllowed(actual) || actual.length()>5 || Character.isDigit(actual.charAt(0))) {
      textField.setBorder(red);
      return false;
    }
    
    for (MemoryDasm memory : memories) {
      if (actual.equals(memory.dasmLocation) || actual.equals(memory.userLocation)) {
        textField.setBorder(red);
        return false;
      }
    }
     
    return super.stopCellEditing();
  }
  
   @Override
    public Component getTableCellEditorComponent(JTable table,
        Object value, boolean isSelected, int row, int column) {
        textField.setBorder(black);
        return super.getTableCellEditorComponent(
            table, value, isSelected, row, column);
    }
}
