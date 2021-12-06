/**
 * @(#)WizardTableCelleRenderer.java 2021/12/06
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
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Cell renderer for wizard
 * 
 * @author ice
 */
public class WizardTableCellRenderer extends DefaultTableCellRenderer {
  JSpinner spinner;  
  boolean showBorder;

  /**
   * Spinner with size 
   * 
   * @param spinner 
   */
  public WizardTableCellRenderer(JSpinner spinner) {
    this.spinner=spinner;  
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    int size=(Integer)spinner.getValue();
    
    if (size%8==column && ((int)(size/8))==row) showBorder=true;
    else showBorder=false;
    return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
  }    
  
  @Override
  public void paint(Graphics g) {
    super.paint(g); 

    if (showBorder) {      
      Rectangle bounds = g.getClipBounds();
      g.setColor(Color.blue);
      g.drawLine(0, 0, bounds.width, bounds.height);  
      g.drawLine(0, bounds.height, bounds.width, 0);  
    } 
  }
}
