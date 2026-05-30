/**
 * @(#)JAgentProgressDialog 2026/05/30
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

import javax.swing.SwingUtilities;

/**
 * Dialog for automatic ai agent
 * 
 * @author ice
 */
public class JAgentProgressDialog extends javax.swing.JDialog {
  
  // ---------------------------------------------------------------------------
  // State
  // ---------------------------------------------------------------------------

  private final int     maxSteps;
  private volatile int  currentStep = 0;
  private volatile boolean cancelled = false;
  private volatile boolean finished  = false;
  
  private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JAgentProgressDialog.class.getName());

  /**
   * Construct dialog
   * 
   * @param parent     parent frame (dialog will be centered over it)
   * @param maxSteps  expected number of agent steps (tool calls);
   *                  use DisassemblerAgent.estimateSteps(startAddr, endAddr)
   */
  public JAgentProgressDialog(java.awt.Frame parent, int maxSteps) {
    super(parent, true);
    this.maxSteps = Math.max(1, maxSteps);
    initComponents();
  }
  
  // ---------------------------------------------------------------------------
  // Public API  –  called from the agent background thread (thread-safe)
  // ---------------------------------------------------------------------------

  /**
   * Advances the progress bar by one step and appends a message to the log.
   * Safe to call from any thread.
   *
   * @param message short description of the current step (tool call name)
   */
  public void step(String message) {
    currentStep = Math.min(currentStep + 1, maxSteps);
    final int step = currentStep;
    SwingUtilities.invokeLater(() -> {
      jProgressBar.setValue(step);
      int pct = (int) ((step * 100L) / maxSteps);
      jLabelSteps.setText("Step " + step + " / " + maxSteps);
      jLabelPercent.setText(pct + "%");
      appendLog(message);
    });
  }

  /**
   * Appends a message to the log without advancing the progress bar.
   * Safe to call from any thread.
   * 
   * @param message the message to log
   */
  public void log(String message) {
    SwingUtilities.invokeLater(() -> appendLog(message));
  }
  
    /**
   * Appends a message to the log without advancing the progress bar.
   * Safe to call from any thread.
   * 
   * @param message the message to log
   */
  public void logAction(String message) {
    SwingUtilities.invokeLater(() -> appendActionLog(message));
  }

  /**
   * Marks the progress bar as complete and closes the dialog.
   * Safe to call from any thread.
   *
   * @param finalMessage  message shown before closing (e.g. "Done – 42 labels set")
   */
  public void finish(String finalMessage) {
    finished = true;
    SwingUtilities.invokeLater(() -> {
      jProgressBar.setValue(maxSteps);
      jLabelSteps.setText("Step " + maxSteps + " / " + maxSteps);
      jLabelPercent.setText("100%");
      appendLog(finalMessage);
     
      jButtonCancel.removeActionListener(jButtonCancel.getActionListeners()[0]);
      jButtonCancel.addActionListener(e -> dispose());
      // Auto-close after 1 second so the user sees the final state
      javax.swing.Timer t = new javax.swing.Timer(1000, e -> dispose());
      t.setRepeats(false);
      t.start();
    });
  }

  /**
   * Returns true if the user clicked Cancel.
   * The agent should check this flag periodically and stop if true.
   */
  public boolean isCancelled() { return cancelled; }

  /**
   * Returns true if the agent has finished (successfully or with error).
   */
  public boolean isFinished() { return finished; }

    // ---------------------------------------------------------------------------
  // Private helpers
  // ---------------------------------------------------------------------------

  /**
   * Cancel action
   */
  private void onCancel() {
    cancelled = true;
    jButtonCancel.setEnabled(false);
    jButtonCancel.setText("Cancelling…");
    appendLog("Cancellation requested – waiting for current step to finish…");
  }

  /**
   * Append log to text area
   * 
   * @param message the message to append
   */
  private void appendLog(String message) {
    if (message == null || message.isBlank()) return;
    jTextAreaLog.append(message + "\n");
    // Auto-scroll to bottom
    jTextAreaLog.setCaretPosition(jTextAreaLog.getDocument().getLength());
  }
  
  /**
   * Append action log to text area
   * 
   * @param message tje message to append
   */
  private void appendActionLog(String message) {
    if (message == null || message.isBlank()) return;
    jTextAreaLogAction.append(message + "\n");
    // Auto-scroll to bottom
    jTextAreaLogAction.setCaretPosition(jTextAreaLogAction.getDocument().getLength());
  }
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jPanelUp = new javax.swing.JPanel();
    jProgressBar = new javax.swing.JProgressBar(0, this.maxSteps);
    jLabelSteps = new javax.swing.JLabel();
    jLabelPercent = new javax.swing.JLabel();
    jPanelCn = new javax.swing.JPanel();
    jScrollPaneLog = new javax.swing.JScrollPane();
    jTextAreaLog = new javax.swing.JTextArea();
    jScrollPaneAction = new javax.swing.JScrollPane();
    jTextAreaLogAction = new javax.swing.JTextArea();
    jPanel1Dn = new javax.swing.JPanel();
    jButtonCancel = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setTitle("AI Agent – analyzing…");
    setResizable(false);

    jPanelUp.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 4, 8));
    jPanelUp.setLayout(new java.awt.GridBagLayout());

    jProgressBar.setPreferredSize(new java.awt.Dimension(420, 20));
    jProgressBar.setRequestFocusEnabled(false);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    jPanelUp.add(jProgressBar, gridBagConstraints);

    jLabelSteps.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    jLabelSteps.setText("Step 0 / " + this.maxSteps);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    jPanelUp.add(jLabelSteps, gridBagConstraints);

    jLabelPercent.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
    jLabelPercent.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jLabelPercent.setText("0%");
    jLabelPercent.setToolTipText("");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    jPanelUp.add(jLabelPercent, gridBagConstraints);

    getContentPane().add(jPanelUp, java.awt.BorderLayout.NORTH);

    jPanelCn.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
    jPanelCn.setLayout(new java.awt.GridLayout(2, 0));

    jScrollPaneLog.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Log"));

    jTextAreaLog.setColumns(20);
    jTextAreaLog.setRows(5);
    jTextAreaLog.setPreferredSize(new java.awt.Dimension(640, 480));
    jScrollPaneLog.setViewportView(jTextAreaLog);

    jPanelCn.add(jScrollPaneLog);

    jScrollPaneAction.setBorder(javax.swing.BorderFactory.createTitledBorder("Action Log"));

    jTextAreaLogAction.setColumns(20);
    jTextAreaLogAction.setRows(5);
    jScrollPaneAction.setViewportView(jTextAreaLogAction);

    jPanelCn.add(jScrollPaneAction);

    getContentPane().add(jPanelCn, java.awt.BorderLayout.CENTER);

    jPanel1Dn.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

    jButtonCancel.setText("Cancel");
    jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCancelActionPerformed(evt);
      }
    });
    jPanel1Dn.add(jButtonCancel);

    getContentPane().add(jPanel1Dn, java.awt.BorderLayout.PAGE_END);

    setSize(new java.awt.Dimension(811, 631));
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
    onCancel();
  }//GEN-LAST:event_jButtonCancelActionPerformed

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
    } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
      logger.log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        JAgentProgressDialog dialog = new JAgentProgressDialog(new javax.swing.JFrame(), 55);
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
  private javax.swing.JButton jButtonCancel;
  private javax.swing.JLabel jLabelPercent;
  private javax.swing.JLabel jLabelSteps;
  private javax.swing.JPanel jPanel1Dn;
  private javax.swing.JPanel jPanelCn;
  private javax.swing.JPanel jPanelUp;
  private javax.swing.JProgressBar jProgressBar;
  private javax.swing.JScrollPane jScrollPaneAction;
  private javax.swing.JScrollPane jScrollPaneLog;
  private javax.swing.JTextArea jTextAreaLog;
  private javax.swing.JTextArea jTextAreaLogAction;
  // End of variables declaration//GEN-END:variables
}
