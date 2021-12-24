/*
 * @(#)JLabelsDialog.java 2021/06/15
 *
 * ICE Team free software group
 *
 * This file is part of JIIT64 Java Ice Team Tracker 64
 * See README for copyright notice.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 */
package sw_emulator.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import sw_emulator.software.MemoryDasm;
import sw_emulator.swing.main.Option;
import sw_emulator.swing.main.Project;
import sw_emulator.swing.table.ConstantCellEditor;
import sw_emulator.swing.table.DataTableModelLabels;

/**
 * Dialog for labels
 * 
 * @author ice
 */
public class JLabelsDialog extends javax.swing.JDialog {  
    /** Data model */
    DataTableModelLabels dataModel=new DataTableModelLabels();
    
    /** Constant cell editor */
    ConstantCellEditor constantCellEditor=new ConstantCellEditor(new JTextField());
    
    /** Memory table for selection of row  */
    JTable jTable;
    
    /** Preview area*/
    RSyntaxTextArea rSyntaxTextAreaDis;
    
    /** The project */
    Project project;
            

    /**
     * Creates new form JConstantDialog
     */
    public JLabelsDialog(java.awt.Frame parent, boolean modal, Option option) {
        super(parent, modal);       
        initComponents();
        Shared.framesList.add(this);        
        
        jTableLabels.addMouseListener(new java.awt.event.MouseAdapter() {
          MouseEvent last;  
            
          private Timer timer = new Timer(300, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              // timer has gone off, so treat as a single click
              singleClick();
              timer.stop();
            }
          });

	  @Override
	  public void mouseClicked(MouseEvent e) {
            last=e;
	    // Check if timer is running 
	    // to know if there was an earlier click
            if (timer.isRunning()) {
              // There was an earlier click so we'll treat it as a double click
              timer.stop();
              doubleClick(e);
            } else {
               // (Re)start the timer and wait for 2nd click      	
                timer.restart();
              }
	  }
          
          /**
           * Single click on table
           */ 
          protected void singleClick() {
	  }

          /**
           * Double click on table
           */
	  protected void doubleClick(MouseEvent evt) {
            int actual;  
         
            // get the address in hex format
            int addr=jTableLabels.getSelectedRow();
            
            if (addr<0) return;
            
            int address=dataModel.getData().get(addr).address;           

            // scan all lines for the memory location
            try {
              //scroll to that point
              ///jTable.scrollRectToVisible(jTable.getCellRect(address,0, true)); 
              Shared.scrollToCenter(jTable, address, 0);
        
              // select this row
              jTable.setRowSelectionInterval(address, address);
            } catch (Exception e) {
                System.err.println();  
              } 
            
            if (evt.isControlDown()) {
     
              int pos=0;        

              // scan all lines for the memory location
              try {
                String preview=rSyntaxTextAreaDis.getText();
                String lines[] = preview.split("\\r?\\n");
                for (String line: lines) {
                  actual=searchAddress(line.substring(0, Math.min(line.length(), option.maxLabelLength)));   
                  if (actual==address) {      
                    rSyntaxTextAreaDis.setCaretPosition(pos); 
                    // set preview in the find position  
                    java.awt.EventQueue.invokeLater(() -> {
                      rSyntaxTextAreaDis.requestFocusInWindow();
                    });
                    
                    break;
                  } else {
                      pos+=line.length()+1;
                    }
                }
              } catch (Exception e) {
                  System.err.println();  
                } 
            }
          }             
        });
    }
    
    /**
     * Search for a memory address (even as label) from the initial passed string
     * 
     * @param initial the initial buffer to search for
     * @return the address or -1 if not find
     */
    protected int searchAddress(String initial) {
      int addr=-1;
    
      try {
        // get the first word of the string
        String str=initial;
        str=str.contains(" ") ? str.split(" ")[0] : str; 
 
        if (str.length()==4) addr=Integer.decode("0x"+str);
        else {
          str=str.contains(":") ? str.split(":")[0] : str;  
          for (MemoryDasm memory : project.memory) {
            if (str.equals(memory.dasmLocation) || str.equals(memory.userLocation)) {
              addr=memory.address;
              break;      
            }    
          }  
        }
      } catch (Exception e)  {
          System.err.println(e);   
        }    
    
      return addr;
    }
    
    /**
     * Set up the dialog with the project to use 
     * 
     * @param data the memory data
     * @param jTable the table to updeate positon on click
     * @param rSyntaxTextAreaDis preview area
     * @param project the actual project
     */
    public void setUp(MemoryDasm[] data, JTable jTable, RSyntaxTextArea rSyntaxTextAreaDis, Project project) {
      dataModel.setData(data);
      this.jTable=jTable;
      this.rSyntaxTextAreaDis=rSyntaxTextAreaDis;
      this.project=project;
      dataModel.fireTableDataChanged();
    }         

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneTable = new javax.swing.JScrollPane();
        jTableLabels = new javax.swing.JTable();
        jTableLabels.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        jPanelDn = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Constants definitions");

        jTableLabels.setModel(dataModel);
        jTableLabels.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableLabels.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTableLabels.getColumnModel().getColumn(1).setPreferredWidth(10);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        jTableLabels.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        jScrollPaneTable.setViewportView(jTableLabels);

        getContentPane().add(jScrollPaneTable, java.awt.BorderLayout.CENTER);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanelDn.add(jButtonClose);

        getContentPane().add(jPanelDn, java.awt.BorderLayout.PAGE_END);

        setBounds(0, 0, 370, 830);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
      setVisible(false);
    }//GEN-LAST:event_jButtonCloseActionPerformed

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
            java.util.logging.Logger.getLogger(JLabelsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JLabelsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JLabelsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JLabelsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JLabelsDialog dialog = new JLabelsDialog(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JButton jButtonClose;
    private javax.swing.JPanel jPanelDn;
    private javax.swing.JScrollPane jScrollPaneTable;
    private javax.swing.JTable jTableLabels;
    // End of variables declaration//GEN-END:variables
}
