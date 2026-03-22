/**
 * @(#)LabelTableModel 2026/03/21
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
package sw_emulator.swing.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

/**
 * TableModel for the AI label suggestion table.
 *
 * Columns:
 *   0 – Selected   (Boolean  – checkbox)
 *   1 – Address    (String   – hex, e.g. "09A0")
 *   2 – Old Label  (String   – current label in the disassembler, may be empty)
 *   3 – New Label  (String   – editable, proposed by AI)
 *
 * @author ice
 */
public class LabelTableModel extends AbstractTableModel {

  // ---------------------------------------------------------------------------
  // Column definitions
  // ---------------------------------------------------------------------------

  private static final int COL_SELECTED  = 0;
  private static final int COL_ADDRESS   = 1;
  private static final int COL_OLD_LABEL = 2;
  private static final int COL_NEW_LABEL = 3;

  private static final String[] COLUMN_NAMES = {
    "Apply", "Address", "Current label", "AI suggestion"
  };

  private static final Class<?>[] COLUMN_CLASSES = {
    Boolean.class, String.class, String.class, String.class
  };

  // ---------------------------------------------------------------------------
  // Row data
  // ---------------------------------------------------------------------------

  /** One row of the table. */
  public static class Row {
    public boolean selected;
    public final String address;
    public final String oldLabel;
    public String newLabel;          // editable

    public Row(boolean selected, String address, String oldLabel, String newLabel) {
      this.selected = selected;
      this.address  = address;
      this.oldLabel = oldLabel;
      this.newLabel = newLabel;
    }
  }

  private final List<Row> rows = new ArrayList<>();

  // ---------------------------------------------------------------------------
  // Population
  // ---------------------------------------------------------------------------

  /**
   * Replaces all rows with a new set of AI suggestions.
   *
   * @param suggestions   address -> AI-proposed label name
   * @param currentLabels address -> label already present in the disassembler
   *                      (pass an empty map if not available)
   */
  public void setData(Map<String, String> suggestions,
                      Map<String, String> currentLabels) {
    rows.clear();
    for (Map.Entry<String, String> e : suggestions.entrySet()) {
      String addr     = e.getKey();
      String newLabel = e.getValue();
      String oldLabel = currentLabels.getOrDefault(addr, "");
      // Pre-select only rows where the AI suggestion differs from the current label
      boolean preSelected = !newLabel.equals(oldLabel);
      rows.add(new Row(preSelected, addr, oldLabel, newLabel));
    }
    fireTableDataChanged();
  }

  /**
   * Clears all rows.
   */
  public void clear() {
    rows.clear();
    fireTableDataChanged();
  }

  // ---------------------------------------------------------------------------
  // Select / deselect all
  // ---------------------------------------------------------------------------

  /**
   * Select all rows
   */
  public void selectAll() {
    for (Row r : rows) r.selected = true;
    fireTableColumnUpdated(COL_SELECTED);
  }

  /**
   * Deselect all rows
   */
  public void deselectAll() {
    for (Row r : rows) r.selected = false;
    fireTableColumnUpdated(COL_SELECTED);
  }

  /** 
   * Returns only the rows whose checkbox is ticked.
   * 
   * @return the list of selected rows
   */
  public List<Row> getSelectedRows() {
    List<Row> result = new ArrayList<>();
    for (Row r : rows) {
      if (r.selected) result.add(r);
    }
    return result;
  }

  // ---------------------------------------------------------------------------
  // AbstractTableModel implementation
  // ---------------------------------------------------------------------------

  @Override public int getRowCount()    { return rows.size(); }
  @Override public int getColumnCount() { return COLUMN_NAMES.length; }

  @Override
  public String getColumnName(int col) { return COLUMN_NAMES[col]; }

  @Override
  public Class<?> getColumnClass(int col) { return COLUMN_CLASSES[col]; }

  @Override
  public boolean isCellEditable(int row, int col) {
    // Checkbox and new-label columns are editable
    return col == COL_SELECTED || col == COL_NEW_LABEL;
  }

  @Override
  public Object getValueAt(int row, int col) {
    Row r = rows.get(row);
    switch (col) {
      case COL_SELECTED:
        return r.selected;
      case COL_ADDRESS:
        return r.address;
      case COL_OLD_LABEL:
        return r.oldLabel;
      case COL_NEW_LABEL:
        return r.newLabel;
      default:
        return null;
    }
  }

  @Override
  public void setValueAt(Object value, int row, int col) {
    Row r = rows.get(row);
    switch (col) {
      case COL_SELECTED:
        r.selected = (Boolean) value;
        break;
      case COL_NEW_LABEL:
        r.newLabel = value.toString();
        break;
    }
    fireTableCellUpdated(row, col);
  }

  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  private void fireTableColumnUpdated(int col) {
    fireTableChanged(new javax.swing.event.TableModelEvent(
            this, 0, Math.max(0, rows.size() - 1), col));
  }
}
