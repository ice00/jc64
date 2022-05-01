/**
 * @(#)JHexDialog.java 2019/12/29
 *
 * ICE Team free software group
 *
 * This file is part of JIIT64 Java Ice Team Tracker 64
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
package sw_emulator.swing;

import java.awt.Font;
import java.awt.Point;
import java.io.File;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import sw_emulator.software.MemoryDasm;
import sw_emulator.swing.table.MonoTableCellRenderer;
import sw_emulator.swing.table.MultiTableCellRenderer;

/**
 * Show dialog with vary hex view of data
 * 
 * @author ice
 */
public class JHexDialog extends javax.swing.JDialog {
    
  /** C64 font */  
  Font c64Font;
  
  /** C64 bold font */  
  Font c64BoldFont;
  
  /** C64 plain font */  
  Font c64PlainFont; 
  
  /** Memory dasm to use */
  MemoryDasm[] memory;
  
  /** Start address of area to show */
  int start;
  
  /** End address of area to show */
  int end;
  
  /** Mono renderer */
  MonoTableCellRenderer rendererMono=new MonoTableCellRenderer();
  
  /** Multi renderer */
  MultiTableCellRenderer rendererMulti=new MultiTableCellRenderer();
    
    /**
     * Creates new form JViewDialog
     */
    public JHexDialog(java.awt.Frame parent, boolean modal) {
      super(parent, modal);
      Shared.framesList.add(this);
        
      try {
          c64Font = Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/sw_emulator/swing/C64_Pro_Mono-STYLE.ttf").getFile()));
          c64BoldFont = c64Font.deriveFont(Font.BOLD, 12);
          c64PlainFont = c64Font.deriveFont(Font.PLAIN, 12);  
      } catch (Exception e) {
            e.printStackTrace();
        }            
      initComponents();
        
      /// let to scroll the tables together
      jScrollPaneAddr.getViewport().addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        Point p = jScrollPaneAddr.getViewport().getViewPosition();
        jScrollPaneHex.getViewport().setViewPosition(p);
        jScrollPaneAddr.getViewport().setViewPosition(p);
        jScrollPaneText.getViewport().setViewPosition(p);
        }
      });
      
      jScrollPaneHex.getViewport().addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        Point p = jScrollPaneHex.getViewport().getViewPosition();
        jScrollPaneHex.getViewport().setViewPosition(p);
        jScrollPaneAddr.getViewport().setViewPosition(p);
        jScrollPaneText.getViewport().setViewPosition(p);
        }
      });
      
      jScrollPaneText.getViewport().addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        Point p = jScrollPaneText.getViewport().getViewPosition();
        jScrollPaneHex.getViewport().setViewPosition(p);
        jScrollPaneAddr.getViewport().setViewPosition(p);
        jScrollPaneText.getViewport().setViewPosition(p);
        }
      });
      
      
      
      /// let to scroll the tables together
      jScrollPaneAddrC.getViewport().addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        Point p = jScrollPaneAddrC.getViewport().getViewPosition();
        jScrollPaneMono.getViewport().setViewPosition(p);
        jScrollPaneAddrC.getViewport().setViewPosition(p);
        jScrollPaneMulti.getViewport().setViewPosition(p);
        }
      });
      
      jScrollPaneMono.getViewport().addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        Point p = jScrollPaneMono.getViewport().getViewPosition();
        jScrollPaneMono.getViewport().setViewPosition(p);
        jScrollPaneAddrC.getViewport().setViewPosition(p);
        jScrollPaneMulti.getViewport().setViewPosition(p);
        }
      });
      
      jScrollPaneMulti.getViewport().addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        Point p = jScrollPaneMulti.getViewport().getViewPosition();
        jScrollPaneMono.getViewport().setViewPosition(p);
        jScrollPaneAddrC.getViewport().setViewPosition(p);
        jScrollPaneMulti.getViewport().setViewPosition(p);
        }
      });
      
      
      // select the same rows in tables
      jTableAddr.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int index = jTableAddr.getSelectedRow();
            if (index > -1) {                   
              if (jTableHex.getModel().getRowCount()>=index)  jTableHex.getSelectionModel().setSelectionInterval(index, index);
              if (jTableText.getModel().getRowCount()>=index) jTableText.getSelectionModel().setSelectionInterval(index, index);
                
              Shared.scrollToCenter(jTableAddrC, index*8, 0);
              if (jTableAddrC.getModel().getRowCount()>=index*8) jTableAddrC.getSelectionModel().setSelectionInterval(index*8, index*8+7);       
            }
        }
      });
      
      jTableHex.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int index = jTableHex.getSelectedRow();
            if (index > -1) {                   
              if (jTableAddr.getModel().getRowCount()>=index)  jTableAddr.getSelectionModel().setSelectionInterval(index, index);
              if (jTableText.getModel().getRowCount()>=index)  jTableText.getSelectionModel().setSelectionInterval(index, index);
                
              Shared.scrollToCenter(jTableAddrC, index*8, 0);
              if (jTableAddrC.getModel().getRowCount()>=index*8) jTableAddrC.getSelectionModel().setSelectionInterval(index*8, index*8+7);
            }
        }
      });
      
      jTableText.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int index = jTableText.getSelectedRow();
            if (index > -1) {                   
              if (jTableHex.getModel().getRowCount()>=index)  jTableHex.getSelectionModel().setSelectionInterval(index, index);
              if (jTableAddr.getModel().getRowCount()>=index) jTableAddr.getSelectionModel().setSelectionInterval(index, index);
                
              Shared.scrollToCenter(jTableAddrC, index*8, 0);
              if (jTableAddrC.getModel().getRowCount()>=index/8)jTableAddrC.getSelectionModel().setSelectionInterval(index*8, index*8+7);
            }
        }
      });
      
      rendererMono.setup(j16ColorPanelBackground.getSelectedColor(), j16ColorPanelForeground.getSelectedColor());
      rendererMulti.setup(j16ColorPanelBackground.getSelectedColor(), j16ColorPanelForeground.getSelectedColor(),
                          j16ColorPanelCommon1.getSelectedColor(), j16ColorPanelCommon2.getSelectedColor());
      
      
      
      // set selection for blocks rows
      jTableAddrC.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int index = jTableAddrC.getSelectedRow();
            if (index > -1) {              
               if (jTableAddrC.getModel().getRowCount()>=index/8*8) jTableAddrC.setRowSelectionInterval(index/8*8, (index+8)/8*8-1);  
               jTableAddrC.repaint();
               
               Shared.scrollToCenter(jTableAddr, index/8, 0);
               if (jTableAddr.getModel().getRowCount()>=index/8) jTableAddr.getSelectionModel().setSelectionInterval(index/8, index/8);
               if (jTableHex.getModel().getRowCount()>=index/8)  jTableHex.getSelectionModel().setSelectionInterval(index/8, index/8);
               if (jTableText.getModel().getRowCount()>=index/8) jTableText.getSelectionModel().setSelectionInterval(index/8, index/8);               
            }
        }
    });
    }
    
    /**
     * Set up the hex dialog
     * 
     * @param memory the memory to use
     * @param start the start address
     * @param end  the end address
     */
    public void setUp(MemoryDasm[] memory, int start, int end) {
      this.memory=memory;
      this.start=start;

       // be sure end is multiply of 8 from start
       end=((end-start)/8)*8+start;
       this.end=end;
       
       calculateTables();
    }
    
    /**
     * Show the values in table
     */
    private void calculateTables() {
      DefaultTableModel dmAddr = (DefaultTableModel) jTableAddr.getModel();
      dmAddr.getDataVector().removeAllElements();
      
      DefaultTableModel dmHex = (DefaultTableModel) jTableHex.getModel();
      dmHex.getDataVector().removeAllElements();
      
      DefaultTableModel dmText = (DefaultTableModel) jTableText.getModel();
      dmText.getDataVector().removeAllElements();  
      
      DefaultTableModel dmAddrC = (DefaultTableModel) jTableAddrC.getModel();
      dmAddrC.getDataVector().removeAllElements();
      
      DefaultTableModel dmMono = (DefaultTableModel) jTableMono.getModel();
      dmMono.getDataVector().removeAllElements();
      
      DefaultTableModel dmMulti = (DefaultTableModel) jTableMulti.getModel();
      dmMulti.getDataVector().removeAllElements();
      
      revalidate();        
              
      // insert the table with address  
      for (int i=start; i<end; i+=8) {
        dmAddr.insertRow((i-start)/8, new Object[] {Shared.ShortToExe(i)});
      }
      
      dmAddr.fireTableDataChanged();
      
      
      // insert hex values
      for (int i=start; i<end; i+=8) {          
        dmHex.insertRow((i-start)/8, new Object[] {
           Shared.ByteToExe(memory[i].copy & 0xFF),
           Shared.ByteToExe(memory[i+1].copy & 0xFF),
           Shared.ByteToExe(memory[i+2].copy & 0xFF),
           Shared.ByteToExe(memory[i+3].copy & 0xFF),
           Shared.ByteToExe(memory[i+4].copy & 0xFF),
           Shared.ByteToExe(memory[i+5].copy & 0xFF),
           Shared.ByteToExe(memory[i+6].copy & 0xFF),
           Shared.ByteToExe(memory[i+7].copy & 0xFF)
         });
      }
      
      dmHex.fireTableDataChanged();
      
      int offset;
      if (jRadioButtonUpper.isSelected()) offset=0xee00;
      else offset=0xef00;
        
      // insert text values
      for (int i=start; i<end; i+=8) {          
        dmText.insertRow((i-start)/8, new Object[] {
           (char)(offset+(memory[i].copy & 0xFF)),
           (char)(offset+(memory[i+1].copy & 0xFF)),
           (char)(offset+(memory[i+2].copy & 0xFF)),
           (char)(offset+(memory[i+3].copy & 0xFF)),
           (char)(offset+(memory[i+4].copy & 0xFF)),
           (char)(offset+(memory[i+5].copy & 0xFF)),
           (char)(offset+(memory[i+6].copy & 0xFF)),
           (char)(offset+(memory[i+7].copy & 0xFF))
         });
      }    
      
      dmText.fireTableDataChanged();
      
      
      // insert the table with address of chars
      for (int i=start; i<end; i++) {
        dmAddrC.insertRow((i-start), new Object[] {Shared.ShortToExe(i)});
      }
      
      dmAddrC.fireTableDataChanged();
      
      // insert text values
      int pos;
      for (int i=start; i<end; i+=8) {          
        pos=(i-start); 
        dmMono.insertRow(pos, new Object[] {
           (memory[i].copy & 0x80)>>7,
           (memory[i].copy & 0x40)>>6,
           (memory[i].copy & 0x20)>>5,
           (memory[i].copy & 0x10)>>4,
           (memory[i].copy & 0x08)>>3,
           (memory[i].copy & 0x04)>>2,
           (memory[i].copy & 0x02)>>1,
           (memory[i].copy & 0x01)
         });
         dmMono.insertRow(pos+1, new Object[] {
           (memory[i+1].copy & 0x80)>>7,
           (memory[i+1].copy & 0x40)>>6,
           (memory[i+1].copy & 0x20)>>5,
           (memory[i+1].copy & 0x10)>>4,
           (memory[i+1].copy & 0x08)>>3,
           (memory[i+1].copy & 0x04)>>2,
           (memory[i+1].copy & 0x02)>>1,
           (memory[i+1].copy & 0x01)
         });
         dmMono.insertRow(pos+2, new Object[] {
           (memory[i+2].copy & 0x80)>>7,
           (memory[i+2].copy & 0x40)>>6,
           (memory[i+2].copy & 0x20)>>5,
           (memory[i+2].copy & 0x10)>>4,
           (memory[i+2].copy & 0x08)>>3,
           (memory[i+2].copy & 0x04)>>2,
           (memory[i+2].copy & 0x02)>>1,
           (memory[i+2].copy & 0x01)
         });
         dmMono.insertRow(pos+3, new Object[] {
           (memory[i+3].copy & 0x80)>>7,
           (memory[i+3].copy & 0x40)>>6,
           (memory[i+3].copy & 0x20)>>5,
           (memory[i+3].copy & 0x10)>>4,
           (memory[i+3].copy & 0x08)>>3,
           (memory[i+3].copy & 0x04)>>2,
           (memory[i+3].copy & 0x02)>>1,
           (memory[i+3].copy & 0x01)
         });
         dmMono.insertRow(pos+4, new Object[] {
           (memory[i+4].copy & 0x80)>>7,
           (memory[i+4].copy & 0x40)>>6,
           (memory[i+4].copy & 0x20)>>5,
           (memory[i+4].copy & 0x10)>>4,
           (memory[i+4].copy & 0x08)>>3,
           (memory[i+4].copy & 0x04)>>2,
           (memory[i+4].copy & 0x02)>>1,
           (memory[i+4].copy & 0x01)
         });
         dmMono.insertRow(pos+5, new Object[] {
           (memory[i+5].copy & 0x80)>>7,
           (memory[i+5].copy & 0x40)>>6,
           (memory[i+5].copy & 0x20)>>5,
           (memory[i+5].copy & 0x10)>>4,
           (memory[i+5].copy & 0x08)>>3,
           (memory[i+5].copy & 0x04)>>2,
           (memory[i+5].copy & 0x02)>>1,
           (memory[i+5].copy & 0x01)
         });
         dmMono.insertRow(pos+6, new Object[] {           
           (memory[i+6].copy & 0x80)>>7,
           (memory[i+6].copy & 0x40)>>6,
           (memory[i+6].copy & 0x20)>>5,
           (memory[i+6].copy & 0x10)>>4,
           (memory[i+6].copy & 0x08)>>3,
           (memory[i+6].copy & 0x04)>>2,
           (memory[i+6].copy & 0x02)>>1,
           (memory[i+6].copy & 0x01)
         });
         dmMono.insertRow(pos+7, new Object[] {
           (memory[i+7].copy & 0x80)>>7,
           (memory[i+7].copy & 0x40)>>6,
           (memory[i+7].copy & 0x20)>>5,
           (memory[i+7].copy & 0x10)>>4,
           (memory[i+7].copy & 0x08)>>3,
           (memory[i+7].copy & 0x04)>>2,
           (memory[i+7].copy & 0x02)>>1,
           (memory[i+7].copy & 0x01)
         });
      }
      
      dmMono.fireTableDataChanged();
      
      for (int i=start; i<end; i+=8) {          
        pos=(i-start); 
        dmMulti.insertRow(pos, new Object[] {
           (memory[i].copy & 0xc0)>>6, 
           (memory[i].copy & 0x30)>>4,
           (memory[i].copy & 0x0c)>>2,
           (memory[i].copy & 0x03)
         });
         dmMulti.insertRow(pos+1, new Object[] {
           (memory[i+1].copy & 0xc0)>>6, 
           (memory[i+1].copy & 0x30)>>4,
           (memory[i+1].copy & 0x0c)>>2,
           (memory[i+1].copy & 0x03)
         });
         dmMulti.insertRow(pos+2, new Object[] {
           (memory[i+2].copy & 0xc0)>>6, 
           (memory[i+2].copy & 0x30)>>4,
           (memory[i+2].copy & 0x0c)>>2,
           (memory[i+2].copy & 0x03)
         });
         dmMulti.insertRow(pos+3, new Object[] {
           (memory[i+3].copy & 0xc0)>>6, 
           (memory[i+3].copy & 0x30)>>4,
           (memory[i+3].copy & 0x0c)>>2,
           (memory[i+3].copy & 0x03)
         });
         dmMulti.insertRow(pos+4, new Object[] {
           (memory[i+4].copy & 0xc0)>>6, 
           (memory[i+4].copy & 0x30)>>4,
           (memory[i+4].copy & 0x0c)>>2,
           (memory[i+4].copy & 0x03)
         });
         dmMulti.insertRow(pos+5, new Object[] {
           (memory[i+5].copy & 0xc0)>>6, 
           (memory[i+5].copy & 0x30)>>4,
           (memory[i+5].copy & 0x0c)>>2,
           (memory[i+5].copy & 0x03)
         });
         dmMulti.insertRow(pos+6, new Object[] {
           (memory[i+6].copy & 0xc0)>>6, 
           (memory[i+6].copy & 0x30)>>4,
           (memory[i+6].copy & 0x0c)>>2,
           (memory[i+6].copy & 0x03)
         });
         dmMulti.insertRow(pos+7, new Object[] {
           (memory[i+7].copy & 0xc0)>>6, 
           (memory[i+7].copy & 0x30)>>4,
           (memory[i+7].copy & 0x0c)>>2,
           (memory[i+7].copy & 0x03)
         });
      }
      
      dmMulti.fireTableDataChanged();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupType = new javax.swing.ButtonGroup();
        jPanelDn = new javax.swing.JPanel();
        jPanelUp = new javax.swing.JPanel();
        jPanelColor = new javax.swing.JPanel();
        jLabelBackground = new javax.swing.JLabel();
        j16ColorPanelBackground = new sw_emulator.swing.J16ColorPanel();
        j16ColorPanelForeground = new sw_emulator.swing.J16ColorPanel();
        jLabelForeground = new javax.swing.JLabel();
        j16ColorPanelCommon1 = new sw_emulator.swing.J16ColorPanel();
        jLabelCommon1 = new javax.swing.JLabel();
        j16ColorPanelCommon2 = new sw_emulator.swing.J16ColorPanel();
        jLabelCommon4 = new javax.swing.JLabel();
        jRadioButtonUpper = new javax.swing.JRadioButton();
        jRadioButtonLower = new javax.swing.JRadioButton();
        jLabelHex = new javax.swing.JLabel();
        jLabelText = new javax.swing.JLabel();
        jLabelChar = new javax.swing.JLabel();
        jLabelMultiChar = new javax.swing.JLabel();
        jPanelTables = new javax.swing.JPanel();
        jScrollPaneAddr = new javax.swing.JScrollPane();
        jTableAddr = new javax.swing.JTable();
        jScrollPaneHex = new javax.swing.JScrollPane();
        jTableHex = new javax.swing.JTable();
        jScrollPaneText = new javax.swing.JScrollPane();
        jTableText = new javax.swing.JTable();
        jScrollPaneMono = new javax.swing.JScrollPane();
        jTableMono = new javax.swing.JTable();
        jTableMono.setDefaultRenderer(Integer.class, rendererMono);
        jScrollPaneMulti = new javax.swing.JScrollPane();
        jTableMulti = new javax.swing.JTable();
        jTableMulti.setDefaultRenderer(Integer.class, rendererMulti);
        jScrollPaneAddrC = new javax.swing.JScrollPane();
        jTableAddrC = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Hex/Text/Char view");
        setPreferredSize(new java.awt.Dimension(1024, 803));
        getContentPane().add(jPanelDn, java.awt.BorderLayout.SOUTH);

        jPanelUp.setName(""); // NOI18N
        jPanelUp.setPreferredSize(new java.awt.Dimension(468, 85));

        jPanelColor.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelBackground.setText("Background:");

        j16ColorPanelBackground.setSelectedIndex(1);
        j16ColorPanelBackground.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j16ColorPanelBackgroundMousePressed(evt);
            }
        });

        j16ColorPanelForeground.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j16ColorPanelForegroundMousePressed(evt);
            }
        });

        jLabelForeground.setText("Foreground:");

        j16ColorPanelCommon1.setSelectedIndex(2);
        j16ColorPanelCommon1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j16ColorPanelCommon1MousePressed(evt);
            }
        });

        jLabelCommon1.setText("Common1:");

        j16ColorPanelCommon2.setSelectedIndex(3);
        j16ColorPanelCommon2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                j16ColorPanelCommon2MousePressed(evt);
            }
        });

        jLabelCommon4.setText("Common2:");

        buttonGroupType.add(jRadioButtonUpper);
        jRadioButtonUpper.setSelected(true);
        jRadioButtonUpper.setText("Upper");
        jRadioButtonUpper.setToolTipText("Select upper c64 text");
        jRadioButtonUpper.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonUpperItemStateChanged(evt);
            }
        });

        buttonGroupType.add(jRadioButtonLower);
        jRadioButtonLower.setText("Lower");
        jRadioButtonLower.setToolTipText("Select lower C64 text");
        jRadioButtonLower.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLowerItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanelColorLayout = new javax.swing.GroupLayout(jPanelColor);
        jPanelColor.setLayout(jPanelColorLayout);
        jPanelColorLayout.setHorizontalGroup(
            jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelColorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonUpper)
                    .addComponent(jRadioButtonLower))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(j16ColorPanelBackground, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBackground))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelForeground)
                    .addComponent(j16ColorPanelForeground, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCommon1)
                    .addComponent(j16ColorPanelCommon1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelCommon4)
                    .addComponent(j16ColorPanelCommon2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );
        jPanelColorLayout.setVerticalGroup(
            jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelColorLayout.createSequentialGroup()
                .addGroup(jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelColorLayout.createSequentialGroup()
                        .addGroup(jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelColorLayout.createSequentialGroup()
                                .addComponent(jLabelCommon4)
                                .addGap(9, 9, 9)
                                .addComponent(j16ColorPanelCommon2, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelColorLayout.createSequentialGroup()
                                .addComponent(jLabelCommon1)
                                .addGap(9, 9, 9)
                                .addComponent(j16ColorPanelCommon1, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelColorLayout.createSequentialGroup()
                                .addGroup(jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabelBackground)
                                    .addComponent(jLabelForeground))
                                .addGap(9, 9, 9)
                                .addGroup(jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(j16ColorPanelBackground, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                                    .addComponent(j16ColorPanelForeground, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelColorLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jRadioButtonUpper)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButtonLower)))
                .addContainerGap())
        );

        jLabelHex.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelHex.setText("Hex");

        jLabelText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelText.setText("Text");

        jLabelChar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelChar.setText("Char definition");

        jLabelMultiChar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelMultiChar.setText("Multicolor char definition");

        javax.swing.GroupLayout jPanelUpLayout = new javax.swing.GroupLayout(jPanelUp);
        jPanelUp.setLayout(jPanelUpLayout);
        jPanelUpLayout.setHorizontalGroup(
            jPanelUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelUpLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(jLabelHex, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelText, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(108, 108, 108)
                .addComponent(jLabelChar, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabelMultiChar, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanelUpLayout.setVerticalGroup(
            jPanelUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelUpLayout.createSequentialGroup()
                .addComponent(jPanelColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelUpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelHex, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelText)
                    .addComponent(jLabelChar)
                    .addComponent(jLabelMultiChar, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanelUp, java.awt.BorderLayout.NORTH);

        jPanelTables.setPreferredSize(new java.awt.Dimension(682, 300));

        jScrollPaneAddr.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTableAddr.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Addr."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableAddr.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneAddr.setViewportView(jTableAddr);

        jScrollPaneHex.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTableHex.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "0", "1", "2", "3", "4", "5", "6", "7"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableHex.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneHex.setViewportView(jTableHex);

        jScrollPaneText.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTableText.setFont(c64PlainFont);
        jTableText.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "0", "1", "2", "3", "4", "5", "6", "7"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableText.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneText.setViewportView(jTableText);

        jScrollPaneMono.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTableMono.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "0", "1", "2", "3", "4", "5", "6", "7"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMono.setRowSelectionAllowed(false);
        jScrollPaneMono.setViewportView(jTableMono);

        jScrollPaneMulti.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTableMulti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "0-1", "1-2", "2-3", "3-4"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableMulti.setRowSelectionAllowed(false);
        jScrollPaneMulti.setViewportView(jTableMulti);

        jScrollPaneAddrC.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jTableAddrC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Addr."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableAddrC.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPaneAddrC.setViewportView(jTableAddrC);

        javax.swing.GroupLayout jPanelTablesLayout = new javax.swing.GroupLayout(jPanelTables);
        jPanelTables.setLayout(jPanelTablesLayout);
        jPanelTablesLayout.setHorizontalGroup(
            jPanelTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTablesLayout.createSequentialGroup()
                .addComponent(jScrollPaneAddr, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneHex, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneText, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneAddrC, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneMono, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneMulti, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanelTablesLayout.setVerticalGroup(
            jPanelTablesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneAddr, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
            .addComponent(jScrollPaneHex, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
            .addComponent(jScrollPaneText, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
            .addComponent(jScrollPaneMono, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
            .addComponent(jScrollPaneMulti, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
            .addComponent(jScrollPaneAddrC, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelTables, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void j16ColorPanelBackgroundMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j16ColorPanelBackgroundMousePressed
      notifyColor();
    }//GEN-LAST:event_j16ColorPanelBackgroundMousePressed

    private void j16ColorPanelForegroundMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j16ColorPanelForegroundMousePressed
      notifyColor();
    }//GEN-LAST:event_j16ColorPanelForegroundMousePressed

    private void j16ColorPanelCommon1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j16ColorPanelCommon1MousePressed
      notifyColor();
    }//GEN-LAST:event_j16ColorPanelCommon1MousePressed

    private void j16ColorPanelCommon2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_j16ColorPanelCommon2MousePressed
      notifyColor();
    }//GEN-LAST:event_j16ColorPanelCommon2MousePressed

    private void jRadioButtonUpperItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonUpperItemStateChanged
      calculateTables();
    }//GEN-LAST:event_jRadioButtonUpperItemStateChanged

    private void jRadioButtonLowerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonLowerItemStateChanged
      calculateTables();
    }//GEN-LAST:event_jRadioButtonLowerItemStateChanged

    private void notifyColor() {
      rendererMono.setup(j16ColorPanelBackground.getSelectedColor(), j16ColorPanelForeground.getSelectedColor());
      rendererMulti.setup(j16ColorPanelBackground.getSelectedColor(), j16ColorPanelForeground.getSelectedColor(),
                          j16ColorPanelCommon1.getSelectedColor(), j16ColorPanelCommon2.getSelectedColor());  
      DefaultTableModel dmMono = (DefaultTableModel) jTableMono.getModel();
      dmMono.fireTableDataChanged();
      
      DefaultTableModel dmMulti = (DefaultTableModel) jTableMulti.getModel();
      dmMulti.fireTableDataChanged();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JHexDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JHexDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JHexDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JHexDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JHexDialog dialog = new JHexDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupType;
    private sw_emulator.swing.J16ColorPanel j16ColorPanelBackground;
    private sw_emulator.swing.J16ColorPanel j16ColorPanelCommon1;
    private sw_emulator.swing.J16ColorPanel j16ColorPanelCommon2;
    private sw_emulator.swing.J16ColorPanel j16ColorPanelForeground;
    private javax.swing.JLabel jLabelBackground;
    private javax.swing.JLabel jLabelChar;
    private javax.swing.JLabel jLabelCommon1;
    private javax.swing.JLabel jLabelCommon4;
    private javax.swing.JLabel jLabelForeground;
    private javax.swing.JLabel jLabelHex;
    private javax.swing.JLabel jLabelMultiChar;
    private javax.swing.JLabel jLabelText;
    private javax.swing.JPanel jPanelColor;
    private javax.swing.JPanel jPanelDn;
    private javax.swing.JPanel jPanelTables;
    private javax.swing.JPanel jPanelUp;
    private javax.swing.JRadioButton jRadioButtonLower;
    private javax.swing.JRadioButton jRadioButtonUpper;
    private javax.swing.JScrollPane jScrollPaneAddr;
    private javax.swing.JScrollPane jScrollPaneAddrC;
    private javax.swing.JScrollPane jScrollPaneHex;
    private javax.swing.JScrollPane jScrollPaneMono;
    private javax.swing.JScrollPane jScrollPaneMulti;
    private javax.swing.JScrollPane jScrollPaneText;
    private javax.swing.JTable jTableAddr;
    private javax.swing.JTable jTableAddrC;
    private javax.swing.JTable jTableHex;
    private javax.swing.JTable jTableMono;
    private javax.swing.JTable jTableMulti;
    private javax.swing.JTable jTableText;
    // End of variables declaration//GEN-END:variables
}
