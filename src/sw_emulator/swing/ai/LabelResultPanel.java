/**
 * @(#)LabelResultPanel 2026/03/21
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
package sw_emulator.swing.ai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import sw_emulator.swing.table.LabelTableModel;

/**
 * Dynamic panel that displays the AI label suggestions in a table.
 * This panel is created fresh for each AI response and placed inside
 * the AILabelFrame's result slot (resultContainerPanel).
 *
 * @author ice
 */
public class LabelResultPanel extends JPanel {

  private final LabelTableModel model = new LabelTableModel();
  private final JTable          table = new JTable(model);
  private final JLabel          statusLabel;

  // ---------------------------------------------------------------------------
  // Constructor
  // ---------------------------------------------------------------------------

  public LabelResultPanel() {
    setLayout(new BorderLayout(0, 4));
    setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

    // Status label shown when table is empty
    statusLabel = new JLabel("Waiting for AI response…", SwingConstants.CENTER);
    statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC));
    statusLabel.setForeground(Color.GRAY);

    configureTable();

    JScrollPane scroll = new JScrollPane(table);
    scroll.setPreferredSize(new Dimension(520, 260));

    add(scroll, BorderLayout.CENTER);
  }

  // ---------------------------------------------------------------------------
  // Public API – called by AILabelFrame
  // ---------------------------------------------------------------------------

  /**
   * Populates the table with AI suggestions.
   *
   * @param suggestions   address -> AI-proposed label (from M6510LabelAnalyzer)
   * @param currentLabels address -> label currently in the disassembler
   */
  public void setResults(Map<String, String> suggestions,
                         Map<String, String> currentLabels) {
    model.setData(suggestions, currentLabels);
    statusLabel.setVisible(suggestions.isEmpty());
  }

  /** 
   * Clears the table (e.g. when a new request starts).
   */
  public void clear() {
    model.clear();
    statusLabel.setText("Waiting for AI response…");
    statusLabel.setVisible(true);
  }

  /** 
   * Selects all rows. 
   */
  public void selectAll()   {
    model.selectAll(); 
  }

  /** 
   * Deselects all rows.
   */
  public void deselectAll() { 
    model.deselectAll();
  }

  /**
   * Returns the rows the user has ticked.
   * Call this when the user clicks "Apply".
   * 
   * @return the clicked rows
   */
  public List<LabelTableModel.Row> getSelectedRows() {
    // Commit any in-progress cell edit before returning data
    if (table.isEditing()) table.getCellEditor().stopCellEditing();
    return model.getSelectedRows();
  }

  // ---------------------------------------------------------------------------
  // Table configuration
  // ---------------------------------------------------------------------------

  /**
   * Configure the visualization table
   */
  private void configureTable() {
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowHeight(22);
    table.setShowGrid(true);
    table.setGridColor(Color.LIGHT_GRAY);
    table.getTableHeader().setReorderingAllowed(false);

    // Column widths
    setColumnWidth(0, 50,  50,  50);   // Apply  (checkbox)
    setColumnWidth(1, 65,  65,  80);   // Address
    setColumnWidth(2, 140, 140, 220);  // Current label
    setColumnWidth(3, 160, 160, 260);  // AI suggestion (editable)

    // Center-align Address column
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

    // Highlight the AI suggestion column slightly
    DefaultTableCellRenderer suggRenderer = new DefaultTableCellRenderer() {
      @Override
      public java.awt.Component getTableCellRendererComponent(
              JTable t, Object value, boolean isSelected, boolean hasFocus,
              int row, int column) {
        super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
        if (!isSelected) setBackground(new Color(255, 255, 220)); // light yellow
        return this;
      }
    };
    table.getColumnModel().getColumn(3).setCellRenderer(suggRenderer);
  }

  private void setColumnWidth(int col, int min, int preferred, int max) {
    TableColumn c = table.getColumnModel().getColumn(col);
    c.setMinWidth(min);
    c.setPreferredWidth(preferred);
    c.setMaxWidth(max);
  }
}

