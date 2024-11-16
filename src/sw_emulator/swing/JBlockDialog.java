/**
 * @(#)JBlockHiDialog.java 2024/04/13
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

import java.text.ParseException;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import sw_emulator.software.MemoryDasm;

/**
 * Automatic label blocks of data 
 * 
 * @author ice
 */
public class JBlockDialog extends javax.swing.JDialog {
  /** Memory dasm */
  private MemoryDasm[] memories;   
  
    private static class HexFormatterFactory extends DefaultFormatterFactory { 
        @Override
        public JFormattedTextField.AbstractFormatter getDefaultFormatter() { 
           return new HexFormatter(); 
       } 
  } 

  private static class HexFormatter extends DefaultFormatter { 
      @Override
      public Object stringToValue(String text) throws ParseException { 
         try { 
            return Long.valueOf(text, 16); 
         } catch (NumberFormatException nfe) { 
            throw new ParseException(text,0); 
         } 
     } 

      @Override
     public String valueToString(Object value) throws ParseException { 
        if (value instanceof Long)
             return Long.toHexString( 
              ((Long)value).intValue()).toUpperCase();  
        else return Integer.toHexString( 
              ((Integer)value).intValue()).toUpperCase(); 
     } 
 } 
  
  
  /**
   * Creates new form JBlockDialog
   */
  public JBlockDialog(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
    
    JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor)jSpinnerStartAddr.getEditor(); 
    editor.getTextField().setFormatterFactory(new HexFormatterFactory());  
        
    editor = (JSpinner.DefaultEditor)jSpinnerEndAddr.getEditor(); 
    editor.getTextField().setFormatterFactory(new HexFormatterFactory());   
  }
  
  /**
   * Set memory dasm
   * 
   * @param memories the memories dasm to use
   * @param start starting addess
   * @param end ending address
   */
  public void setUp(MemoryDasm[] memories, int start, int end) {
    this.memories=memories; 

    jSpinnerStartAddr.setValue(start);
    jSpinnerEndAddr.setValue(end);            
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanelTitle = new javax.swing.JPanel();
    jLabelTitle = new javax.swing.JLabel();
    jLabelStartAddr = new javax.swing.JLabel();
    jLabelEndAddr = new javax.swing.JLabel();
    jSpinnerEndAddr = new javax.swing.JSpinner();
    jSpinnerStartAddr = new javax.swing.JSpinner();
    jLabelSize = new javax.swing.JLabel();
    jSpinnerSize = new javax.swing.JSpinner();
    jLabelPrefix = new javax.swing.JLabel();
    jLabelStart = new javax.swing.JLabel();
    jSpinnerStart = new javax.swing.JSpinner();
    jTextFieldPrefix = new javax.swing.JTextField();
    jSeparator1 = new javax.swing.JSeparator();
    jLabelDigit = new javax.swing.JLabel();
    jSpinnerDigit = new javax.swing.JSpinner();
    jCheckBoxUpper = new javax.swing.JCheckBox();
    jButtonApplyDigit = new javax.swing.JButton();
    jSeparator2 = new javax.swing.JSeparator();
    jButtonApplyMemory = new javax.swing.JButton();
    jPanelCancel = new javax.swing.JPanel();
    jButtonCancel = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

    jPanelTitle.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jLabelTitle.setText("Automatic blocks labeled");
    jPanelTitle.add(jLabelTitle);

    jLabelStartAddr.setText("Starting address:");

    jLabelEndAddr.setText("Ending address:");

    jSpinnerEndAddr.setModel(new javax.swing.SpinnerNumberModel(0, 0, 65535, 1));

    jSpinnerStartAddr.setModel(new javax.swing.SpinnerNumberModel(0, 0, 65535, 1));

    jLabelSize.setText("Block size:");

    jSpinnerSize.setModel(new javax.swing.SpinnerNumberModel(24, 1, 1024, 1));

    jLabelPrefix.setText("Prefix:");

    jLabelStart.setText("Start:");

    jSpinnerStart.setModel(new javax.swing.SpinnerNumberModel(0, 0, 255, 1));

    jLabelDigit.setText("Digit:");

    jSpinnerDigit.setModel(new javax.swing.SpinnerNumberModel(1, 1, 2, 1));
    jSpinnerDigit.setToolTipText("Min number of digits to use (can increase automatically)");

    jCheckBoxUpper.setText("Uppercase");

    jButtonApplyDigit.setText("Apply with digit");
    jButtonApplyDigit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonApplyDigitActionPerformed(evt);
      }
    });

    jButtonApplyMemory.setText("Apply with memory position");
    jButtonApplyMemory.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonApplyMemoryActionPerformed(evt);
      }
    });

    jPanelCancel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    jButtonCancel.setText("Cancel");
    jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCancelActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanelCancelLayout = new javax.swing.GroupLayout(jPanelCancel);
    jPanelCancel.setLayout(jPanelCancelLayout);
    jPanelCancelLayout.setHorizontalGroup(
      jPanelCancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
      .addGroup(jPanelCancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanelCancelLayout.createSequentialGroup()
          .addGap(0, 0, Short.MAX_VALUE)
          .addComponent(jButtonCancel)
          .addGap(0, 0, Short.MAX_VALUE)))
    );
    jPanelCancelLayout.setVerticalGroup(
      jPanelCancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 35, Short.MAX_VALUE)
      .addGroup(jPanelCancelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanelCancelLayout.createSequentialGroup()
          .addGap(0, 0, Short.MAX_VALUE)
          .addComponent(jButtonCancel)
          .addGap(0, 0, Short.MAX_VALUE)))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jPanelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jSeparator1)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelDigit, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSpinnerDigit, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                      .addComponent(jLabelSize, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addComponent(jLabelStartAddr))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                      .addComponent(jSpinnerStartAddr, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                      .addComponent(jSpinnerSize, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(34, 34, 34)
                    .addComponent(jLabelEndAddr, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSpinnerEndAddr, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabelStart, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSpinnerStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(31, 31, 31)
                    .addComponent(jCheckBoxUpper)))
                .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jSeparator2)
        .addContainerGap())
      .addGroup(layout.createSequentialGroup()
        .addGap(66, 66, 66)
        .addComponent(jButtonApplyDigit)
        .addGap(18, 18, 18)
        .addComponent(jButtonApplyMemory)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      .addComponent(jPanelCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jPanelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jSpinnerStartAddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabelStartAddr))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabelEndAddr)
            .addComponent(jSpinnerEndAddr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabelSize)
          .addComponent(jSpinnerSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(1, 1, 1)
        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(1, 1, 1)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabelPrefix)
          .addComponent(jTextFieldPrefix, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelDigit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jSpinnerDigit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jSpinnerStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelStart)
          .addComponent(jCheckBoxUpper))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButtonApplyDigit)
          .addComponent(jButtonApplyMemory))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jPanelCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 0, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
    setVisible(false);
  }//GEN-LAST:event_jButtonCancelActionPerformed

  private void jButtonApplyDigitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyDigitActionPerformed
    int start=(Integer)jSpinnerStartAddr.getValue();
    int end=(Integer)jSpinnerEndAddr.getValue();
    
    if (end<start) {
      JOptionPane.showMessageDialog(this, "Low end position must be after low start position");
      return;
    }
    

    int step=(int)jSpinnerSize.getValue();
    String prefix=jTextFieldPrefix.getText();
    boolean uppercase=jCheckBoxUpper.isSelected();
    int digit=(Integer)jSpinnerDigit.getValue();
    String label;
    int starting=(Integer)jSpinnerStart.getValue();
    
    // make the action
      int j=starting;
      for (int i=starting; i<=starting+end-start; i+=step, j++) {
        if (prefix!=null && !"".equals(prefix)) { 
          label=Integer.toHexString((int)j);
          if (label.length()==1 && digit==2) label="0"+label;
          if (uppercase) label=label.toUpperCase();
          else label=label.toLowerCase();
          label=prefix+label;        
          memories[(int)(start+i)].userLocation=label;
        }                 
      }
      
      setVisible(false);    
  }//GEN-LAST:event_jButtonApplyDigitActionPerformed

  private void jButtonApplyMemoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplyMemoryActionPerformed
    int start=(Integer)jSpinnerStartAddr.getValue();
    int end=(Integer)jSpinnerEndAddr.getValue();
    
    if (end<start) {
      JOptionPane.showMessageDialog(this, "Low end position must be after low start position");
      return;
    }
    

    int step=(int)jSpinnerSize.getValue();
    String prefix=jTextFieldPrefix.getText();
    String label;
    
    // make the action
      for (int i=start; i<=end; i+=step) {
        if (prefix!=null && !"".equals(prefix)) { 
          label=ShortToExe((int)i);
          label=prefix+label;        
          memories[(int)i].userLocation=label;
        }                 
      }
      
      setVisible(false);
  }//GEN-LAST:event_jButtonApplyMemoryActionPerformed

    /**
   * Convert a unsigned short (containing in a int) to Exe upper case 4 chars
   *
   * @param value the short value to convert
   * @return the exe string rapresentation of byte
   */
  protected static String ShortToExe(int value) {
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
      java.util.logging.Logger.getLogger(JBlockDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(JBlockDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(JBlockDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(JBlockDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        JBlockDialog dialog = new JBlockDialog(new javax.swing.JFrame(), true);
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
  private javax.swing.JButton jButtonApplyDigit;
  private javax.swing.JButton jButtonApplyMemory;
  private javax.swing.JButton jButtonCancel;
  private javax.swing.JCheckBox jCheckBoxUpper;
  private javax.swing.JLabel jLabelDigit;
  private javax.swing.JLabel jLabelEndAddr;
  private javax.swing.JLabel jLabelPrefix;
  private javax.swing.JLabel jLabelSize;
  private javax.swing.JLabel jLabelStart;
  private javax.swing.JLabel jLabelStartAddr;
  private javax.swing.JLabel jLabelTitle;
  private javax.swing.JPanel jPanelCancel;
  private javax.swing.JPanel jPanelTitle;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JSeparator jSeparator2;
  private javax.swing.JSpinner jSpinnerDigit;
  private javax.swing.JSpinner jSpinnerEndAddr;
  private javax.swing.JSpinner jSpinnerSize;
  private javax.swing.JSpinner jSpinnerStart;
  private javax.swing.JSpinner jSpinnerStartAddr;
  private javax.swing.JTextField jTextFieldPrefix;
  // End of variables declaration//GEN-END:variables
}