/**
 * @(#)JCreditsDialog.java 2019/12/29
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

/**
 * Credits dialog
 * 
 * @author  ice
 */
public class JCreditsDialog extends javax.swing.JDialog {

    /** Creates new form JCreditsDialog */
    public JCreditsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Shared.framesList.add(this);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelDn = new javax.swing.JPanel();
        jButtonClose = new javax.swing.JButton();
        jScrollPane = new javax.swing.JScrollPane();
        jTextPaneCredits = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Credits");
        setResizable(false);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanelDn.add(jButtonClose);

        getContentPane().add(jPanelDn, java.awt.BorderLayout.PAGE_END);

        jTextPaneCredits.setContentType("text/html"); // NOI18N
        jTextPaneCredits.setText("<html>\n  <head>\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n     <b>Code:</b><br>\n     Stefano Tognon (Ice00)\n     <br><br>\n     <b>External syntax highlight library:</b><br>\n     Robert Futrell\n     <br><br>\n<b>Flat look & feel:</b><br>\nFormDev Software GmbH\n  <br><br>\n<b>Beta testing:</b><br>\nBacchus/Fairligh<br>\nChris Abbott \n  <br><br>\n<b>C64 font:</b><br>\nhttps://style64.org/c64-truetype\n    </p>\n  </body>\n</html>\n");
        jScrollPane.setViewportView(jTextPaneCredits);

        getContentPane().add(jScrollPane, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(416, 365));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
  setVisible(false);
}//GEN-LAST:event_jButtonCloseActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JCreditsDialog dialog = new JCreditsDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
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
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextPane jTextPaneCredits;
    // End of variables declaration//GEN-END:variables

}
