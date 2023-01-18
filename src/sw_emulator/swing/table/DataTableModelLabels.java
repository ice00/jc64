/**
 * @(#)DataTableModelLabels.java 2021/06/14
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
import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import sw_emulator.software.MemoryDasm;

/**
 * DataTableModel for labels
 * 
 * @author ice
 */
public class DataTableModelLabels extends AbstractTableModel {    

    
  /** Table data */  
  MemoryDasm[] data;  
  
  /** Parsed data */
  ArrayList<MemoryDasm> data2=new ArrayList();

  public DataTableModelLabels() {
  }
  
  public enum COLUMNS {
    ID("Memory location", Integer.class),
    DL("Dasm location", String.class),
    UL("User location", String.class);
      
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
    
    data2.clear();
    for (MemoryDasm mem:data) {
      if ((mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) || (mem.userLocation!=null && !"".equals(mem.userLocation))) data2.add(mem);
    }
  }
  
  /**
   * Get the actual data in table 
   * 
   * @return  the data
   */
  public ArrayList<MemoryDasm> getData() {
    return data2;  
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
    return data2.size();    
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
    MemoryDasm memory=data2.get(rowIndex);
      
    switch (columns[columnIndex]) {
        case ID:
          return ShortToExe(memory.address);   
        case DL:
          return memory.dasmLocation;            
        case UL:            
          MemoryDasm  mem;
          if (memory.type=='+' || memory.type=='-' || memory.type=='^' || memory.type=='\\') {
            
            if (memory.type=='+') {
              mem=data[memory.related];
              if (mem.userLocation!=null && !"".equals(mem.userLocation)) return mem.userLocation+"+"+(memory.address-memory.related); 
              else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) return mem.dasmLocation+"+"+(memory.address-memory.related);
                   else return "$"+ShortToExe(mem.address)+"+"+(memory.address-memory.related);
            } 
            if (memory.type=='-') {
               mem=data[memory.related];
               if (mem.userLocation!=null && !"".equals(mem.userLocation)) return mem.userLocation+(memory.address-memory.related); 
                  else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation))return mem.dasmLocation+(memory.address-memory.related);
                       else return "$"+ShortToExe(mem.address)+(memory.address-memory.related);                 
            }   
            if (memory.type=='^' || memory.type=='\\') {
              mem=data[(memory.related>>16) & 0xFFFF];  
              if (mem.userLocation!=null && !"".equals(mem.userLocation)) return mem.userLocation+"+"+(memory.address-((memory.related>>16) & 0xFFFF)); 
              else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) return mem.dasmLocation+"+"+(memory.address-((memory.related>>16) & 0xFFFF));
                   else return "$"+ShortToExe(mem.address)+"+"+(memory.address-((memory.related>>16) & 0xFFFF));
            }             
          }   
          return memory.userLocation;                       
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
  
  /**
   * Convert a byte (containing in a int) to Exe upper case 2 chars
   *
   * @param value the short value to convert
   * @return the exe string rapresentation of byte
   */
  protected String ByteToExe(byte value) {
    int tmp=value & 0xFF;
    
    String ret=Integer.toHexString(tmp);
    int len=ret.length();
    switch (len) {
      case 1:
        ret="0"+ret;
        break;
    }
    return ret.toUpperCase(Locale.ENGLISH);
  }
}
