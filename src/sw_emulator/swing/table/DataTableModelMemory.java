/**
 * @(#)DataTableModelMemory.java 2019/12/21
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

import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import sw_emulator.software.MemoryDasm;

/**
 * DataTableModel for memory dasm
 * 
 * @author ice
 */
public class DataTableModelMemory extends AbstractTableModel {    
  /** Table data */  
  MemoryDasm[] data;  
  
  public enum COLUMNS {
    ID("Memory location", Integer.class),
    DC("Dasm comment", Boolean.class),
    UC("User comment", Boolean.class),
    DL("Dasm location", Boolean.class),
    UL("User location", Boolean.class),
    UB("User block comment", Boolean.class);
      
    String columnsTip;
    Class type;
    
    COLUMNS(String tip, Class type) {
      columnsTip=tip;  
      this.type=type;
    }
    
    };  
  
  public static COLUMNS[] columns=COLUMNS.values();   
  
  /**
   * Set the memory data to use
   * 
   * @param data the memory data
   */
  public void setData(MemoryDasm[] data) {
    this.data=data;  
  }
  
  /**
   * Get the actual data in table 
   * 
   * @return  the data
   */
  public MemoryDasm[] getData() {
    return data;  
  }
    
  /**
   * Get the number of columns
   * 
   * @return the number of columns
   */
  @Override
  public int getColumnCount() {
    return columns.length;
  }
  
  /**
   * Get the number of rows
   * 
   * @return the number of rows
   */
  @Override
  public int getRowCount() {
    if (data!=null) return data.length;
    else return 0;
  }

  @Override
  public Class<?> getColumnClass(int i) {
    return columns[i].type;
  } 
  
  /**
   * Get the value at the given position
   * 
   * @param rowIndex the row index
   * @param columnIndex the column index
   * @return the value in cell
   */
  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    MemoryDasm memory=data[rowIndex];
      
    switch (columns[columnIndex]) {
        case ID:
          return ShortToExe(memory.address);            
        case DC:
          return memory.dasmComment!=null;            
        case UC:
          return memory.userComment!=null;  
        case DL:
          return memory.dasmLocation!=null;            
        case UL:
          return memory.userLocation!=null;      
        case UB:
          return memory.userBlockComment!=null;                  
    }  
    return "";
  }

  /**
   * Get the name of the column
   * 
   * @param column the column index
   * @return the column name
   * @Override
   */    
  @Override
  public String getColumnName(int column) {
      return columns[column].name();
  }
  
  /**
   * Convert a unsigned short (containing in a int) to Exe upper case 4 chars
   *
   * @param value the short value to convert
   * @return the exe string rapresentation of byte
   */
  protected String ShortToExe(int value) {
    int tmp=value;

    if (value<0) return "????";
    
    String ret=Integer.toHexString(tmp);
    int len=ret.length();
    switch (len) {
      case 1:
        ret="000"+ret;
        break;
     case 2:
        ret="00"+ret;
        break;
     case 3:
        ret="0"+ret;
        break;
    }
    return ret.toUpperCase(Locale.ENGLISH);
  }
}
