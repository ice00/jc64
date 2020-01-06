/**
 * @(#)JPanelOption.java 2019/12/01
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

import javax.swing.JOptionPane;
import sw_emulator.software.cpu.M6510Dasm;
import sw_emulator.software.machine.C64Dasm;
import sw_emulator.swing.main.FileManager;
import sw_emulator.swing.main.Option;

/**
 * A dialog for option
 * 
 * @author ice
 */
public class JOptionDialog extends javax.swing.JDialog {
    /** Option to use */
    Option option=new Option();
    
    /**
     * Creates new form JOptionDialog
     */
    public JOptionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupOpcodeFormatting = new javax.swing.ButtonGroup();
        buttonGroupIllegalOpcodeStyle = new javax.swing.ButtonGroup();
        buttonGroupLanguage = new javax.swing.ButtonGroup();
        buttonGroupCodeData = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPaneOption = new javax.swing.JTabbedPane();
        jPanelPreview = new javax.swing.JPanel();
        jLabelIllegalOpcodeStyle = new javax.swing.JLabel();
        jRadioButtonStyle1 = new javax.swing.JRadioButton();
        jRadioButtonStyle2 = new javax.swing.JRadioButton();
        jRadioButtonStyle3 = new javax.swing.JRadioButton();
        jLabelLanguage = new javax.swing.JLabel();
        jRadioButtonLangEnglish = new javax.swing.JRadioButton();
        jRadioButtonLangItalian = new javax.swing.JRadioButton();
        jCheckBoxEraseDComm = new javax.swing.JCheckBox();
        jCheckBoxOpcodeFormattingPreview = new javax.swing.JCheckBox();
        jCheckBoxUndefinedCode = new javax.swing.JCheckBox();
        jSpinnerMaxLength = new javax.swing.JSpinner();
        jLabelMaxLength = new javax.swing.JLabel();
        jLabelAggregate = new javax.swing.JLabel();
        jSpinnerMaxAggregate = new javax.swing.JSpinner();
        jLabelPSIDinitsong = new javax.swing.JLabel();
        jTextFieldInitSongs = new javax.swing.JTextField();
        jLabelPSIDplaysound = new javax.swing.JLabel();
        jTextFieldPlaySound = new javax.swing.JTextField();
        jLabelSIDfreqLo = new javax.swing.JLabel();
        jTextFieldSidFreqLo = new javax.swing.JTextField();
        jLabelSIDfreqHi = new javax.swing.JLabel();
        jTextFieldSidFreqHi = new javax.swing.JTextField();
        jCheckBoxOpcodeFormattingSource = new javax.swing.JCheckBox();
        jCheckBoxErasePlus = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jButtonLoad = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabelIllegalOpcodeStyle.setText("Illegal opcode style:");

        buttonGroupIllegalOpcodeStyle.add(jRadioButtonStyle1);
        jRadioButtonStyle1.setSelected(true);
        jRadioButtonStyle1.setText("ANE, ISB, SHY, SHS");
        jRadioButtonStyle1.setToolTipText("Mode use by John west and Marko M\"akel\"a");
        jRadioButtonStyle1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonStyle1ItemStateChanged(evt);
            }
        });

        buttonGroupIllegalOpcodeStyle.add(jRadioButtonStyle2);
        jRadioButtonStyle2.setText("AXA, ISC, SYH, SSH");
        jRadioButtonStyle2.setToolTipText("Mode use by Juergen Buchmueller");
        jRadioButtonStyle2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonStyle2ItemStateChanged(evt);
            }
        });

        buttonGroupIllegalOpcodeStyle.add(jRadioButtonStyle3);
        jRadioButtonStyle3.setText("XAA, INS, SAY, TAS");
        jRadioButtonStyle3.setToolTipText("Mode use by Adam Vardy");
        jRadioButtonStyle3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonStyle3ItemStateChanged(evt);
            }
        });

        jLabelLanguage.setText("Language of comments:");

        buttonGroupLanguage.add(jRadioButtonLangEnglish);
        jRadioButtonLangEnglish.setSelected(true);
        jRadioButtonLangEnglish.setText("English");
        jRadioButtonLangEnglish.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLangEnglishItemStateChanged(evt);
            }
        });

        buttonGroupLanguage.add(jRadioButtonLangItalian);
        jRadioButtonLangItalian.setText("Italian");
        jRadioButtonLangItalian.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButtonLangItalianItemStateChanged(evt);
            }
        });

        jCheckBoxEraseDComm.setSelected(true);
        jCheckBoxEraseDComm.setText("Erase DASM automatic comment when mark a memory location as Data");
        jCheckBoxEraseDComm.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxEraseDCommItemStateChanged(evt);
            }
        });

        jCheckBoxOpcodeFormattingPreview.setSelected(true);
        jCheckBoxOpcodeFormattingPreview.setText("Opcode formatting as Upper case (NOP, JMP, ...) insteas of Lower case (nop, jmp, ...) for preview");
        jCheckBoxOpcodeFormattingPreview.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxOpcodeFormattingPreviewItemStateChanged(evt);
            }
        });

        jCheckBoxUndefinedCode.setSelected(true);
        jCheckBoxUndefinedCode.setText("Undefined code/data used as code instead of data");
        jCheckBoxUndefinedCode.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxUndefinedCodeItemStateChanged(evt);
            }
        });

        jSpinnerMaxLength.setModel(new javax.swing.SpinnerNumberModel(25, 5, 40, 1));
        jSpinnerMaxLength.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerMaxLengthStateChanged(evt);
            }
        });

        jLabelMaxLength.setText("Max lenght of label (some assembler has a limit):");

        jLabelAggregate.setText("Aggregate up to X values on a data row:");

        jSpinnerMaxAggregate.setModel(new javax.swing.SpinnerNumberModel(8, 2, 8, 1));
        jSpinnerMaxAggregate.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerMaxAggregateStateChanged(evt);
            }
        });

        jLabelPSIDinitsong.setText("PSID init songs label:");

        jTextFieldInitSongs.setText("initSongs");
        jTextFieldInitSongs.setToolTipText("Label to use for the PSID starting init routine");
        jTextFieldInitSongs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldInitSongsFocusLost(evt);
            }
        });

        jLabelPSIDplaysound.setText("PSID play sound label:");

        jTextFieldPlaySound.setText("playSound");
        jTextFieldPlaySound.setToolTipText("Label to use for PSID play sound routine");
        jTextFieldPlaySound.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldPlaySoundFocusLost(evt);
            }
        });

        jLabelSIDfreqLo.setText("SID frequency table low label:");

        jTextFieldSidFreqLo.setText("frequencyLo");
        jTextFieldSidFreqLo.setToolTipText("Label to use for PSID play sound routine");
        jTextFieldSidFreqLo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSidFreqLoFocusLost(evt);
            }
        });

        jLabelSIDfreqHi.setText("SID frequency table high label:");

        jTextFieldSidFreqHi.setText("frequencyHi");
        jTextFieldSidFreqHi.setToolTipText("Label to use for PSID play sound routine");
        jTextFieldSidFreqHi.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSidFreqHiFocusLost(evt);
            }
        });

        jCheckBoxOpcodeFormattingSource.setText("Opcode formatting as Upper case (NOP, JMP, ...) insteas of Lower case (nop, jmp, ...) for source");
        jCheckBoxOpcodeFormattingSource.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxOpcodeFormattingSourceItemStateChanged(evt);
            }
        });

        jCheckBoxErasePlus.setSelected(true);
        jCheckBoxErasePlus.setText("Erase relative + address when mark a memory location as Data");
        jCheckBoxErasePlus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxErasePlusItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanelPreviewLayout = new javax.swing.GroupLayout(jPanelPreview);
        jPanelPreview.setLayout(jPanelPreviewLayout);
        jPanelPreviewLayout.setHorizontalGroup(
            jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPreviewLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPreviewLayout.createSequentialGroup()
                        .addComponent(jLabelLanguage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(292, 292, 292))
                    .addGroup(jPanelPreviewLayout.createSequentialGroup()
                        .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelPreviewLayout.createSequentialGroup()
                                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelSIDfreqHi, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanelPreviewLayout.createSequentialGroup()
                                        .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabelPSIDplaysound, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabelAggregate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabelMaxLength, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabelPSIDinitsong, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabelSIDfreqLo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextFieldPlaySound, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldInitSongs, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jSpinnerMaxLength)
                                                .addComponent(jSpinnerMaxAggregate, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jTextFieldSidFreqLo, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextFieldSidFreqHi, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(134, 134, 134))
                            .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelPreviewLayout.createSequentialGroup()
                                    .addComponent(jLabelIllegalOpcodeStyle, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanelPreviewLayout.createSequentialGroup()
                                            .addComponent(jRadioButtonStyle1)
                                            .addGap(47, 47, 47)
                                            .addComponent(jRadioButtonStyle2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jRadioButtonStyle3))
                                        .addGroup(jPanelPreviewLayout.createSequentialGroup()
                                            .addComponent(jRadioButtonLangEnglish, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jRadioButtonLangItalian))))
                                .addComponent(jCheckBoxOpcodeFormattingPreview, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBoxOpcodeFormattingSource, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBoxEraseDComm, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBoxErasePlus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jCheckBoxUndefinedCode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap(46, Short.MAX_VALUE))))
        );
        jPanelPreviewLayout.setVerticalGroup(
            jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPreviewLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelIllegalOpcodeStyle)
                    .addComponent(jRadioButtonStyle1)
                    .addComponent(jRadioButtonStyle2)
                    .addComponent(jRadioButtonStyle3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelLanguage)
                    .addComponent(jRadioButtonLangEnglish)
                    .addComponent(jRadioButtonLangItalian))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxOpcodeFormattingPreview)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxOpcodeFormattingSource)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxEraseDComm)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxErasePlus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxUndefinedCode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerMaxLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMaxLength))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelAggregate)
                    .addComponent(jSpinnerMaxAggregate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPSIDinitsong)
                    .addComponent(jTextFieldInitSongs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPSIDplaysound)
                    .addComponent(jTextFieldPlaySound, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSIDfreqLo)
                    .addComponent(jTextFieldSidFreqLo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSIDfreqHi)
                    .addComponent(jTextFieldSidFreqHi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(114, Short.MAX_VALUE))
        );

        jTabbedPaneOption.addTab("Option", jPanelPreview);

        jPanel2.add(jTabbedPaneOption, java.awt.BorderLayout.CENTER);

        jButtonLoad.setText("Load");
        jButtonLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoadActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonLoad);

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonSave);

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanel3.add(jButtonClose);

        jPanel2.add(jPanel3, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 817, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoadActionPerformed
      if(!FileManager.instance.readOptionFile(FileManager.optionFile, option)) {
        JOptionPane.showMessageDialog(this, "Error reading the option file", "Loading error", JOptionPane.ERROR_MESSAGE);
      } else applyOption();
    }//GEN-LAST:event_jButtonLoadActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
       if (!FileManager.instance.writeOptionFile(FileManager.optionFile, option)) {
        JOptionPane.showMessageDialog(this, "Error rwriting the option file", "Saving error", JOptionPane.ERROR_MESSAGE);
      } else JOptionPane.showMessageDialog(this, "Saving done", "Saving result", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
      setVisible(false);
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jTextFieldSidFreqHiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSidFreqHiFocusLost
        String txt=jTextFieldSidFreqHi.getText();
        txt=txt.replaceAll(" ", "");
        if ("".equals(txt)) option.sidFreqHiLabel="frequencyHi";
        else option.sidFreqHiLabel=txt;
    }//GEN-LAST:event_jTextFieldSidFreqHiFocusLost

    private void jTextFieldSidFreqLoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSidFreqLoFocusLost
        String txt=jTextFieldSidFreqLo.getText();
        txt=txt.replaceAll(" ", "");
        if ("".equals(txt)) option.sidFreqLoLabel="frequencyLo";
        else option.sidFreqLoLabel=txt;
    }//GEN-LAST:event_jTextFieldSidFreqLoFocusLost

    private void jTextFieldPlaySoundFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldPlaySoundFocusLost
        String txt=jTextFieldPlaySound.getText();
        txt=txt.replaceAll(" ", "");
        if ("".equals(txt)) option.psidPlaySoundsLabel="playSound";
        else option.psidPlaySoundsLabel=txt;
    }//GEN-LAST:event_jTextFieldPlaySoundFocusLost

    private void jTextFieldInitSongsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldInitSongsFocusLost
        String txt=jTextFieldInitSongs.getText();
        txt=txt.replaceAll(" ", "");
        if ("".equals(txt)) option.psidInitSongsLabel="initSongs";
        else option.psidInitSongsLabel=txt;
    }//GEN-LAST:event_jTextFieldInitSongsFocusLost

    private void jSpinnerMaxAggregateStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerMaxAggregateStateChanged
        option.maxAggregate=(Integer)jSpinnerMaxAggregate.getValue();
    }//GEN-LAST:event_jSpinnerMaxAggregateStateChanged

    private void jSpinnerMaxLengthStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerMaxLengthStateChanged
        option.maxLabelLength=(Integer)jSpinnerMaxLength.getValue();
    }//GEN-LAST:event_jSpinnerMaxLengthStateChanged

    private void jCheckBoxUndefinedCodeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxUndefinedCodeItemStateChanged
        option.useAsCode=jCheckBoxUndefinedCode.isSelected();
    }//GEN-LAST:event_jCheckBoxUndefinedCodeItemStateChanged

    private void jCheckBoxOpcodeFormattingPreviewItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxOpcodeFormattingPreviewItemStateChanged
        option.opcodeUpperCasePreview=jCheckBoxOpcodeFormattingPreview.isSelected();
    }//GEN-LAST:event_jCheckBoxOpcodeFormattingPreviewItemStateChanged

    private void jCheckBoxEraseDCommItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxEraseDCommItemStateChanged
        option.eraseDComm=jCheckBoxEraseDComm.isSelected();
    }//GEN-LAST:event_jCheckBoxEraseDCommItemStateChanged

    private void jRadioButtonLangItalianItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonLangItalianItemStateChanged
        option.commentLanguage=C64Dasm.LANG_ITALIAN;
    }//GEN-LAST:event_jRadioButtonLangItalianItemStateChanged

    private void jRadioButtonLangEnglishItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonLangEnglishItemStateChanged
        option.commentLanguage=C64Dasm.LANG_ENGLISH;
    }//GEN-LAST:event_jRadioButtonLangEnglishItemStateChanged

    private void jRadioButtonStyle3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonStyle3ItemStateChanged
        option.illegalOpcodeMode=M6510Dasm.MODE3;
    }//GEN-LAST:event_jRadioButtonStyle3ItemStateChanged

    private void jRadioButtonStyle2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonStyle2ItemStateChanged
        option.illegalOpcodeMode=M6510Dasm.MODE2;
    }//GEN-LAST:event_jRadioButtonStyle2ItemStateChanged

    private void jRadioButtonStyle1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonStyle1ItemStateChanged
        option.illegalOpcodeMode=M6510Dasm.MODE1;
    }//GEN-LAST:event_jRadioButtonStyle1ItemStateChanged

    private void jCheckBoxOpcodeFormattingSourceItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxOpcodeFormattingSourceItemStateChanged
       option.opcodeUpperCaseSource=jCheckBoxOpcodeFormattingSource.isSelected();     
    }//GEN-LAST:event_jCheckBoxOpcodeFormattingSourceItemStateChanged

    private void jCheckBoxErasePlusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxErasePlusItemStateChanged
      option.erasePlus=jCheckBoxErasePlus.isSelected();
    }//GEN-LAST:event_jCheckBoxErasePlusItemStateChanged

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
            java.util.logging.Logger.getLogger(JOptionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JOptionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JOptionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JOptionDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JOptionDialog dialog = new JOptionDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.ButtonGroup buttonGroupCodeData;
    private javax.swing.ButtonGroup buttonGroupIllegalOpcodeStyle;
    private javax.swing.ButtonGroup buttonGroupLanguage;
    private javax.swing.ButtonGroup buttonGroupOpcodeFormatting;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonLoad;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxEraseDComm;
    private javax.swing.JCheckBox jCheckBoxErasePlus;
    private javax.swing.JCheckBox jCheckBoxOpcodeFormattingPreview;
    private javax.swing.JCheckBox jCheckBoxOpcodeFormattingSource;
    private javax.swing.JCheckBox jCheckBoxUndefinedCode;
    private javax.swing.JLabel jLabelAggregate;
    private javax.swing.JLabel jLabelIllegalOpcodeStyle;
    private javax.swing.JLabel jLabelLanguage;
    private javax.swing.JLabel jLabelMaxLength;
    private javax.swing.JLabel jLabelPSIDinitsong;
    private javax.swing.JLabel jLabelPSIDplaysound;
    private javax.swing.JLabel jLabelSIDfreqHi;
    private javax.swing.JLabel jLabelSIDfreqLo;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelPreview;
    private javax.swing.JRadioButton jRadioButtonLangEnglish;
    private javax.swing.JRadioButton jRadioButtonLangItalian;
    private javax.swing.JRadioButton jRadioButtonStyle1;
    private javax.swing.JRadioButton jRadioButtonStyle2;
    private javax.swing.JRadioButton jRadioButtonStyle3;
    private javax.swing.JSpinner jSpinnerMaxAggregate;
    private javax.swing.JSpinner jSpinnerMaxLength;
    private javax.swing.JTabbedPane jTabbedPaneOption;
    private javax.swing.JTextField jTextFieldInitSongs;
    private javax.swing.JTextField jTextFieldPlaySound;
    private javax.swing.JTextField jTextFieldSidFreqHi;
    private javax.swing.JTextField jTextFieldSidFreqLo;
    // End of variables declaration//GEN-END:variables

    /**
     * Use the passed option container 
     * 
     * @param option the option to use
     */
    public void useOption(Option option) {
      this.option=option;
      applyOption();
    }

    /**
     * Apply option to graphical container
     */
    private void applyOption() {
      jRadioButtonLangEnglish.setSelected(option.commentLanguage==C64Dasm.LANG_ENGLISH);
      jRadioButtonLangItalian.setSelected(option.commentLanguage==C64Dasm.LANG_ITALIAN);
      jRadioButtonStyle1.setSelected((option.illegalOpcodeMode==M6510Dasm.MODE1));
      jRadioButtonStyle2.setSelected((option.illegalOpcodeMode==M6510Dasm.MODE2));
      jRadioButtonStyle3.setSelected((option.illegalOpcodeMode==M6510Dasm.MODE3));
      jCheckBoxOpcodeFormattingPreview.setSelected(option.opcodeUpperCasePreview);      
      jCheckBoxOpcodeFormattingSource.setSelected(option.opcodeUpperCaseSource);      
      jCheckBoxUndefinedCode.setSelected(option.useAsCode);
      jCheckBoxEraseDComm.setSelected(option.eraseDComm);
      jCheckBoxErasePlus.setSelected(option.erasePlus);
      jTextFieldInitSongs.setText(option.psidInitSongsLabel);
      jTextFieldPlaySound.setText(option.psidPlaySoundsLabel);
      jTextFieldSidFreqLo.setText(option.sidFreqLoLabel);
      jTextFieldSidFreqHi.setText(option.sidFreqHiLabel);
      jSpinnerMaxLength.setValue(option.maxLabelLength);
      jSpinnerMaxAggregate.setValue(option.maxAggregate);
    }
}
