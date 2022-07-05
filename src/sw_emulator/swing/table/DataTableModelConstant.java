/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sw_emulator.swing.table;

import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import sw_emulator.swing.Shared;
import sw_emulator.swing.main.Constant;

/**
 * DataTableModel for constant table
 * 
 * @author ice
 */
public class DataTableModelConstant extends AbstractTableModel {
  /** Constant with value */  
  Constant constant;
    
  public enum COLUMNS {
    ID("ID", String.class),
    T0("Table 0", String.class),
    T1("Table 1", String.class),
    T2("Table 2", String.class),
    T3("Table 3", String.class),
    T4("Table 4", String.class),
    T5("Table 5", String.class),
    T6("Table 6", String.class),
    T7("Table 7", String.class),
    T8("Table 8", String.class),
    T9("Table 9", String.class),
    T10("Table !", String.class),
    T11("Table \"", String.class),
    T12("Table £", String.class),
    T13("Table $", String.class),
    T14("Table %", String.class),
    T15("Table &", String.class),    
    T16("Table /", String.class),    
    T17("Table (", String.class),    
    T18("Table )", String.class),    
    T19("Table =", String.class),    
    ;
      
    String columnsTip;
    Class type;
    
    COLUMNS(String tip, Class type) {
      columnsTip=tip;  
      this.type=type;
    }
    
  };  
  
  public static COLUMNS[] columns=COLUMNS.values(); 

  /**
   * Constructor 
   * 
   * @param constant the constant to use
   */
  public DataTableModelConstant(Constant constant) {
    this.constant = constant;
  }
  
  /**
   * Get the number of rows
   * 
   * @return the number of rows
   */
  @Override
  public int getRowCount() {
    return Constant.ROWS;
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
  
  @Override
  public Class<?> getColumnClass(int i) {
    return columns[i].type;
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
      switch (column) {
          case 11: return "T!";
          case 12: return "T\"";
          case 13: return "T£";
          case 14: return "T$";
          case 15: return "T%";
          case 16: return "T&";
          case 17: return "T/";
          case 18: return "T(";
          case 19: return "T)";
          case 20: return "T=";
          default: return columns[column].name();
      }  
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
    switch (columns[columnIndex]) {
      case ID:
        if (rowIndex<Constant.MIN_ROWS) return Shared.ByteToExe(rowIndex);
        else return Shared.ShortToExe(rowIndex);
      case T0:
        return constant.table[0][rowIndex];
      case T1:
        return constant.table[1][rowIndex];  
      case T2:
        return constant.table[2][rowIndex];  
      case T3:
        return constant.table[3][rowIndex];  
      case T4:
        return constant.table[4][rowIndex];  
      case T5:
        return constant.table[5][rowIndex];  
      case T6:
        return constant.table[6][rowIndex];  
      case T7:
        return constant.table[7][rowIndex];  
      case T8:
        return constant.table[8][rowIndex];  
      case T9:
        return constant.table[9][rowIndex];  
      case T10:
        return constant.table[10][rowIndex];        
      case T11:
        return constant.table[11][rowIndex];        
      case T12:
        return constant.table[12][rowIndex];        
      case T13:
        return constant.table[13][rowIndex];          
      case T14:
        return constant.table[14][rowIndex];          
      case T15:
        return constant.table[15][rowIndex];                    
      case T16:
        return constant.table[16][rowIndex];                    
      case T17:
        return constant.table[17][rowIndex];                    
      case T18:
        return constant.table[18][rowIndex];                      
      case T19:
        return constant.table[19][rowIndex];                    
    }  
    
    return "";
  }
 
  /**
   * Set the value at the given position
   * 
   * @param aValue the value to save
   * @param rowIndex the row position
   * @param columnIndex the column position
   */  
  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    switch (columns[columnIndex]) {
      case T0:
        constant.table[0][rowIndex]=(String)aValue;  
        break;  
      case T1:
        constant.table[1][rowIndex]=(String)aValue;  
        break;
      case T2:
        constant.table[2][rowIndex]=(String)aValue;  
        break;  
      case T3:
        constant.table[3][rowIndex]=(String)aValue;  
        break;    
      case T4:
        constant.table[4][rowIndex]=(String)aValue;  
        break;   
      case T5:
        constant.table[5][rowIndex]=(String)aValue;  
        break;  
      case T6:
        constant.table[6][rowIndex]=(String)aValue;  
        break;   
      case T7:
        constant.table[7][rowIndex]=(String)aValue;  
        break;  
      case T8:
        constant.table[8][rowIndex]=(String)aValue;  
        break;  
      case T9:
        constant.table[9][rowIndex]=(String)aValue;  
        break;
      case T10:
        constant.table[10][rowIndex]=(String)aValue;  
        break;      
      case T11:
        constant.table[11][rowIndex]=(String)aValue;  
        break;      
      case T12:
        constant.table[12][rowIndex]=(String)aValue;  
        break;        
      case T13:
        constant.table[13][rowIndex]=(String)aValue;  
        break;
      case T14:
        constant.table[14][rowIndex]=(String)aValue;  
        break;           
      case T15:
        constant.table[15][rowIndex]=(String)aValue;  
        break;
      case T16:
        constant.table[16][rowIndex]=(String)aValue;  
        break;  
      case T17:
        constant.table[17][rowIndex]=(String)aValue;  
        break;    
      case T18:
        constant.table[18][rowIndex]=(String)aValue;  
        break;        
      case T19:
        constant.table[19][rowIndex]=(String)aValue;  
        break;        
        
    }
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    switch (columns[columnIndex]) {  
      case ID: return false;
      default: return true;  
    }
  }    
}
