/**
 * @(#)JDialogProject.java 2019/12/03
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
package sw_emulator.swing;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import sw_emulator.software.memory.MemoryFlags;
import sw_emulator.swing.main.FileManager;
import sw_emulator.swing.main.Project;

/**
 * Dialog for project
 * 
 * @author ice
 */
public class JProjectDialog extends javax.swing.JDialog {
    /** File chooser */
    JFileChooser fileChooser=new JFileChooser();    
    
     /** File chooser for memory flags */
    JFileChooser memFileChooser=new JFileChooser();    
    
    /** The project to use (create an emty one not used) */
    Project project=new Project();
    
    /**
     * Creates new form JDialogProject
     */
    public JProjectDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PSID/RSID tune", "sid"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("MUS tune", "mus"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PRG C64 program", "prg", "bin"));
    }
    
    /**
     * Set up the dialog with the project to use 
     * 
     * @param project the project to use 
     */
    public void setUp(Project project) {
      this.project=project;  
      
      jTextFieldProjectName.setText(project.name);
      jTextFieldInputFile.setText(project.file);
      jTextAreaDescr.setText(project.description);
      if (project.fileType!=null) {
        switch (project.fileType) {
          case PRG:
            jRadioButtonPRG.setSelected(true);
            break;
          case SID:
            jRadioButtonSID.setSelected(true);
            break;
          case MUS:
            jRadioButtonMUS.setSelected(true);
            break;
        }
      } else {
          jRadioButtonPRG.setSelected(false);
          jRadioButtonSID.setSelected(false);
          jRadioButtonMUS.setSelected(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupFileType = new javax.swing.ButtonGroup();
        jPanelCenter = new javax.swing.JPanel();
        jLabelProjectName = new javax.swing.JLabel();
        jTextFieldProjectName = new javax.swing.JTextField();
        jLabelInputFile = new javax.swing.JLabel();
        jTextFieldInputFile = new javax.swing.JTextField();
        jButtonSelect = new javax.swing.JButton();
        jLabelFileType = new javax.swing.JLabel();
        jRadioButtonSID = new javax.swing.JRadioButton();
        jRadioButtonMUS = new javax.swing.JRadioButton();
        jRadioButtonPRG = new javax.swing.JRadioButton();
        jLabelFileDes = new javax.swing.JLabel();
        jScrollPaneDescr = new javax.swing.JScrollPane();
        jTextAreaDescr = new javax.swing.JTextArea();
        jLabelSidLd = new javax.swing.JLabel();
        jButtonClear = new javax.swing.JButton();
        jButtonAddNext = new javax.swing.JButton();
        jButtonInit = new javax.swing.JButton();
        jPanelDn = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Project");
        setResizable(false);

        jPanelCenter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelProjectName.setText("Project Name:");

        jLabelInputFile.setText("File to disassemblate:");

        jButtonSelect.setText("Select");
        jButtonSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectActionPerformed(evt);
            }
        });

        jLabelFileType.setText("File Type:");

        buttonGroupFileType.add(jRadioButtonSID);
        jRadioButtonSID.setText("SID");
        jRadioButtonSID.setEnabled(false);

        buttonGroupFileType.add(jRadioButtonMUS);
        jRadioButtonMUS.setText("MUS");
        jRadioButtonMUS.setEnabled(false);

        buttonGroupFileType.add(jRadioButtonPRG);
        jRadioButtonPRG.setText("PRG");
        jRadioButtonPRG.setEnabled(false);

        jLabelFileDes.setText("File description:");

        jTextAreaDescr.setEditable(false);
        jTextAreaDescr.setColumns(20);
        jTextAreaDescr.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextAreaDescr.setRows(5);
        jScrollPaneDescr.setViewportView(jTextAreaDescr);

        jLabelSidLd.setText("SIDLD memory flag: ");

        jButtonClear.setText("Clear");
        jButtonClear.setToolTipText("Clear the memory flag as of all undefined");
        jButtonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearActionPerformed(evt);
            }
        });

        jButtonAddNext.setText("Add next");
        jButtonAddNext.setToolTipText("Add next SIDLD file to memory flag");
        jButtonAddNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddNextActionPerformed(evt);
            }
        });

        jButtonInit.setText("Init");
        jButtonInit.setToolTipText("Init the memory flag as all of executable code");
        jButtonInit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonInitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCenterLayout = new javax.swing.GroupLayout(jPanelCenter);
        jPanelCenter.setLayout(jPanelCenterLayout);
        jPanelCenterLayout.setHorizontalGroup(
            jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneDescr)
                    .addGroup(jPanelCenterLayout.createSequentialGroup()
                        .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabelInputFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelProjectName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabelFileType, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelFileDes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCenterLayout.createSequentialGroup()
                                .addComponent(jRadioButtonSID)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonMUS)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRadioButtonPRG)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanelCenterLayout.createSequentialGroup()
                                .addComponent(jTextFieldInputFile, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonSelect))
                            .addComponent(jTextFieldProjectName)))
                    .addGroup(jPanelCenterLayout.createSequentialGroup()
                        .addComponent(jLabelSidLd, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonClear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonInit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAddNext)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCenterLayout.setVerticalGroup(
            jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCenterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelProjectName)
                    .addComponent(jTextFieldProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelInputFile)
                    .addComponent(jTextFieldInputFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSelect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCenterLayout.createSequentialGroup()
                        .addComponent(jLabelFileType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelFileDes))
                    .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jRadioButtonSID)
                        .addComponent(jRadioButtonMUS)
                        .addComponent(jRadioButtonPRG)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneDescr, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonClear)
                    .addComponent(jLabelSidLd)
                    .addComponent(jButtonAddNext)
                    .addComponent(jButtonInit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jPanelDn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanelDn.add(jButtonClose);

        getContentPane().add(jPanelDn, java.awt.BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectActionPerformed
      int retValue=fileChooser.showOpenDialog(this);
      
      if (retValue==JFileChooser.APPROVE_OPTION) {
        project.file=fileChooser.getSelectedFile().getAbsolutePath();
        jTextFieldInputFile.setText(project.file);        
        
        // go to read the file
        try {
          project.setData(FileManager.instance.readFile(project.file));
          jTextAreaDescr.setText(project.description);
          switch (project.fileType) {
            case SID:   
              jRadioButtonSID.setSelected(true);
              break;
            case MUS:
              jRadioButtonMUS.setSelected(true);
              break;
            case PRG:
              jRadioButtonPRG.setSelected(true);
              break;  
            case UND:
              jRadioButtonSID.setSelected(false);  
              jRadioButtonMUS.setSelected(false);  
              jRadioButtonPRG.setSelected(false);  
              break;
          }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch(IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
          }
        
        project.fileType=project.fileType.getFileType(project.inB);
        
      } 
    }//GEN-LAST:event_jButtonSelectActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
       project.name=jTextFieldProjectName.getText();
       
       if (project.file==null || "".equals(project.file)) {
         if (JOptionPane.showConfirmDialog(this, "No file inserted. Closing will erase all in project. Do you want to close anywere?", "Warning", JOptionPane.WARNING_MESSAGE)==JOptionPane.OK_OPTION) setVisible(false);
         else return;
       }
        
       setVisible(false);
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
      // clear memory flags
      project.memoryFlags=new byte[0xffff];        
    }//GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonAddNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddNextActionPerformed
      if (project.memoryFlags==null) project.memoryFlags=new byte[0xffff]; 
          
      int retValue=memFileChooser.showOpenDialog(this);
      
      if (retValue==JFileChooser.APPROVE_OPTION) {
        String[] file=new String[1];
        file[1]=memFileChooser.getSelectedFile().getAbsolutePath();
        MemoryFlags memoryFlags=new MemoryFlags(file);
        project.memoryFlags=memoryFlags.orMemory(memoryFlags.getMemoryState(0, 0xFFFF), project.memoryFlags);       
      }  
    }//GEN-LAST:event_jButtonAddNextActionPerformed

    private void jButtonInitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonInitActionPerformed
      // ser as executable
      MemoryFlags memoryFlags=new MemoryFlags((String[])null);
      project.memoryFlags=memoryFlags.getMemoryState(0, 0xFFFF);
    }//GEN-LAST:event_jButtonInitActionPerformed

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
            java.util.logging.Logger.getLogger(JProjectDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JProjectDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JProjectDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JProjectDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JProjectDialog dialog = new JProjectDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroupFileType;
    private javax.swing.JButton jButtonAddNext;
    private javax.swing.JButton jButtonClear;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonInit;
    private javax.swing.JButton jButtonSelect;
    private javax.swing.JLabel jLabelFileDes;
    private javax.swing.JLabel jLabelFileType;
    private javax.swing.JLabel jLabelInputFile;
    private javax.swing.JLabel jLabelProjectName;
    private javax.swing.JLabel jLabelSidLd;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelDn;
    private javax.swing.JRadioButton jRadioButtonMUS;
    private javax.swing.JRadioButton jRadioButtonPRG;
    private javax.swing.JRadioButton jRadioButtonSID;
    private javax.swing.JScrollPane jScrollPaneDescr;
    private javax.swing.JTextArea jTextAreaDescr;
    private javax.swing.JTextField jTextFieldInputFile;
    private javax.swing.JTextField jTextFieldProjectName;
    // End of variables declaration//GEN-END:variables
}
