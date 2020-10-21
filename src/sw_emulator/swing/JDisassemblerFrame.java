/**
 * @(#)JFrameDisassembler.java 2019/12/01
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

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;
import sw_emulator.software.Disassembly;
import sw_emulator.software.MemoryDasm;
import sw_emulator.software.memory.memoryState;
import sw_emulator.swing.main.DataType;
import sw_emulator.swing.main.FileManager;
import sw_emulator.swing.main.MPR;
import sw_emulator.swing.main.Option;
import sw_emulator.swing.main.Project;
import sw_emulator.swing.main.userAction;
import static sw_emulator.swing.main.userAction.SOURCE_FINDD;
import sw_emulator.swing.table.DataTableModelMemory;
import sw_emulator.swing.table.MemoryTableCellRenderer;

/**
 * Main frame for C64 disassembler
 * 
 * @author ice
 */
public class JDisassemblerFrame extends javax.swing.JFrame implements userAction {
  /** Option to use */
  Option option=new Option();
  
  /** Project to use */
  Project project;
  
  /** Project file name */
  File projectFile;
  
  /** Last saved project values */
  Project savedProject;
  
  /** Data table for memory */
  DataTableModelMemory dataTableModelMemory=new DataTableModelMemory();
  
  /** Disassembly engine  */
  Disassembly disassembly=new Disassembly();
  
  /** Option dialog */
  JOptionDialog jOptionDialog=new JOptionDialog(this, true);
  
  /** Project dialog */
  JProjectDialog jProjectDialog=new JProjectDialog(this, true);
  
  /** Project chooser file dialog*/
  JFileChooser projectChooserFile=new JFileChooser();
  
  /** Project merge file dialog*/
  JFileChooser projectMergeFile=new JFileChooser();  
  
  /** Export as file chooser */
  JFileChooser exportAsChooserFile=new JFileChooser();
  
  /** Load MPR as file chooser */
  JFileChooser optionMPRLoadChooserFile=new JFileChooser();
  
  /** Save MPR as file chooser */
  JFileChooser optionMPRSaveChooserFile=new JFileChooser();
  
  /** Memory cell renderer for table */
  MemoryTableCellRenderer memoryTableCellRenderer=new MemoryTableCellRenderer();
  
  /** License dialog */
  JLicenseDialog jLicenseDialog=new JLicenseDialog(this, true);
  
  /** Credit dialog */
  JCreditsDialog jCreditsDialog=new JCreditsDialog(this, true);
  
  /** About dialog */
  JAboutDialog jAboutDialog=new JAboutDialog(this, true);
  
  /** Help dialog */
  JHelpFrame jHelpFrame=new JHelpFrame();
  
  /** Find dialog for source */
  FindDialog findDialogSource;
  
  /** Find dialog for disassembly */
  FindDialog findDialogDis;
  
  
    /**
     * Creates new form JFrameDisassembler
     */
    public JDisassemblerFrame() {        
        initComponents();
        Shared.framesList.add(this);
        Shared.framesList.add(projectChooserFile);
        Shared.framesList.add(projectMergeFile);
        Shared.framesList.add(exportAsChooserFile);
        Shared.framesList.add(optionMPRLoadChooserFile);
        Shared.framesList.add(optionMPRSaveChooserFile);
        Shared.framesList.add(findDialogDis);
        Shared.framesList.add(findDialogSource);
        findDialogDis.setSearchString(" ");
        findDialogSource.setSearchString(" ");
        
        FileManager.instance.readOptionFile(FileManager.optionFile, option);
        
        if (option.getLafName().equals("SYNTH")) Option.useLookAndFeel(option.getFlatLaf());
        else Option.useLookAndFeel(option.getLafName(), option.getMethalTheme());
        
        jOptionDialog.useOption(option);
        
        projectChooserFile.addChoosableFileFilter(new FileNameExtensionFilter("JC64Dis (*.dis)", "dis"));
        projectMergeFile.addChoosableFileFilter(new FileNameExtensionFilter("JC64Dis (*.dis)", "dis"));
        exportAsChooserFile.addChoosableFileFilter(new FileNameExtensionFilter("Source (*.txt)","txt"));
        optionMPRLoadChooserFile.addChoosableFileFilter(new FileNameExtensionFilter("PRG C64 program (prg, bin)", "prg", "bin"));
        optionMPRLoadChooserFile.setMultiSelectionEnabled(true);
        optionMPRLoadChooserFile.setDialogTitle("Select all PRG to include into the MPR");    
        optionMPRSaveChooserFile.addChoosableFileFilter(new FileNameExtensionFilter("Multi PRG C64 program (mpr)", "mpr"));
        optionMPRSaveChooserFile.setDialogTitle("Select the MPR file to save");          
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenuData = new javax.swing.JPopupMenu();
        jMenuItemByteHex = new javax.swing.JMenuItem();
        jMenuItemByteDec = new javax.swing.JMenuItem();
        jMenuItemByteBin = new javax.swing.JMenuItem();
        jMenuItemByteChar = new javax.swing.JMenuItem();
        jSeparatorByte = new javax.swing.JPopupMenu.Separator();
        jMenuItemWord = new javax.swing.JMenuItem();
        jMenuItemWordSwapped = new javax.swing.JMenuItem();
        jSeparatorWord = new javax.swing.JPopupMenu.Separator();
        jMenuItemTribyte = new javax.swing.JMenuItem();
        jMenuItemLong = new javax.swing.JMenuItem();
        jSeparatorPopUpMenu0 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAddress = new javax.swing.JMenuItem();
        jMenuItemStackWord = new javax.swing.JMenuItem();
        jSeparatorPopUpMenu1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSpriteMono = new javax.swing.JMenuItem();
        jMenuItemSpriteMulti = new javax.swing.JMenuItem();
        jSeparatorSprite = new javax.swing.JPopupMenu.Separator();
        jMenuItemText = new javax.swing.JMenuItem();
        jMenuItemNumText = new javax.swing.JMenuItem();
        jMenuItemTextZero = new javax.swing.JMenuItem();
        jMenuItemtextHighOne = new javax.swing.JMenuItem();
        jMenuItemtextShifted = new javax.swing.JMenuItem();
        jMenuItemTextScreen = new javax.swing.JMenuItem();
        jMenuItemTextPetascii = new javax.swing.JMenuItem();
        jToolBar = new javax.swing.JToolBar();
        jButtonNewProject = new javax.swing.JButton();
        jButtonOpenProject = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();
        jButtonSaveProject = new javax.swing.JButton();
        jButtonSaveProjectAs = new javax.swing.JButton();
        jButtonMPR = new javax.swing.JButton();
        jButtonMerge = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        jSeparatorButton1 = new javax.swing.JToolBar.Separator();
        jButtonClearDMem = new javax.swing.JButton();
        jButtonClearUMem = new javax.swing.JButton();
        jButtonClearDLabel = new javax.swing.JButton();
        jButtonAddUserComm = new javax.swing.JButton();
        jButtonAddUserBlock = new javax.swing.JButton();
        jButtonAddUserLabel = new javax.swing.JButton();
        jButtonAddUserLabelOp = new javax.swing.JButton();
        jButtonMarkCode = new javax.swing.JButton();
        jButtonMarkData = new javax.swing.JButton();
        jButtonMarkGarbage = new javax.swing.JButton();
        jButtonMarkPlus = new javax.swing.JButton();
        jButtonMarkMinus = new javax.swing.JButton();
        jButtonMarkLow = new javax.swing.JButton();
        jButtonMarkMax = new javax.swing.JButton();
        jSeparatorButton3 = new javax.swing.JToolBar.Separator();
        jButtonConfigure = new javax.swing.JButton();
        jButtonSIDLD = new javax.swing.JButton();
        jButtonViewProject = new javax.swing.JButton();
        jSeparatorButton2 = new javax.swing.JToolBar.Separator();
        jButtonFindMem = new javax.swing.JButton();
        jButtonDisassemble = new javax.swing.JButton();
        jButtonFindDis = new javax.swing.JButton();
        jButtonExportAsDiss = new javax.swing.JButton();
        jButtonFindSource = new javax.swing.JButton();
        jButtonExportAsSource = new javax.swing.JButton();
        jSplitPaneExternal = new javax.swing.JSplitPane();
        jSplitPaneInternal = new javax.swing.JSplitPane();
        jScrollPaneLeft = new javax.swing.JScrollPane();
        rSyntaxTextAreaDis = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();
        jScrollPaneRight = new javax.swing.JScrollPane();
        rSyntaxTextAreaSource = new org.fife.ui.rsyntaxtextarea.RSyntaxTextArea();
        jScrollPaneMemory = new javax.swing.JScrollPane();
        jTableMemory = new javax.swing.JTable() {

            //Implement table cell tool tips.
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(colIndex);

                try {
                    MemoryDasm memory=dataTableModelMemory.getData()[rowIndex];
                    switch (dataTableModelMemory.columns[realColumnIndex]) {
                        case ID:
                        if (!memory.isInside) tip="Memory outside of the program";
                        else if(memory.isCode) tip="Memory marked as code";
                        else if (memory.isData) tip="Memory marked as data";
                        else tip="Memory not marked as code or data";
                        break;
                        case DC:
                        if ((Boolean)getValueAt(rowIndex, colIndex)) tip=memory.dasmComment;
                        break;
                        case UC:
                        if ((Boolean)getValueAt(rowIndex, colIndex)) tip=memory.userComment;
                        break;
                        case DL:
                        if ((Boolean)getValueAt(rowIndex, colIndex)) tip=memory.dasmLocation;
                        break;
                        case UL:
                        if ((Boolean)getValueAt(rowIndex, colIndex)) tip=memory.userLocation;
                        break;
                        case UB:
                        if ((Boolean)getValueAt(rowIndex, colIndex)) tip="<html>"+memory.userBlockComment.replace("\n", "<br>")+"</html>";
                        break;
                        case RE:
                        if (memory.type!=' ') {
                            MemoryDasm mem=dataTableModelMemory.getData()[memory.related];
                            if (memory.type=='+') {
                                if (mem.userLocation!=null && !"".equals(mem.userLocation)) tip=mem.userLocation+"+"+(memory.address-memory.related);
                                else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) tip=mem.dasmLocation+"+"+(memory.address-memory.related);
                                else tip="$"+ShortToExe(mem.address)+"+"+(memory.address-memory.related);
                            } else {
                                if (mem.userLocation!=null && !"".equals(mem.userLocation)) tip="#"+memory.type+mem.userLocation;
                                else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) tip="#"+memory.type+mem.dasmLocation;
                                else tip="#"+memory.type+"$"+ShortToExe(mem.address);
                            }
                        }
                        break;
                    }
                } catch (RuntimeException e1) {
                    //catch null pointer exception if mouse is over an empty line
                }
                return tip;
            }

        };
        jMenuBar = new javax.swing.JMenuBar();
        jMenuMerge = new javax.swing.JMenu();
        jMenuItemNewProject = new javax.swing.JMenuItem();
        jSeparatorProject1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemOpenProject = new javax.swing.JMenuItem();
        jMenuItemCloseProject = new javax.swing.JMenuItem();
        jSeparatorProject2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSaveProject = new javax.swing.JMenuItem();
        jMenuItemSaveAsProject = new javax.swing.JMenuItem();
        jSeparatorProject3 = new javax.swing.JPopupMenu.Separator();
        jMenuItemMPR = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuMemory = new javax.swing.JMenu();
        jMenuItemClearDMem = new javax.swing.JMenuItem();
        jMenuItemClearUMem = new javax.swing.JMenuItem();
        jMenuItemClearDLabel = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAddComment = new javax.swing.JMenuItem();
        jMenuItemAddBlock = new javax.swing.JMenuItem();
        jMenuItemUserLabel = new javax.swing.JMenuItem();
        jMenuItemUserLabelOp = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItemMarkCode = new javax.swing.JMenuItem();
        jMenuItemMarkData = new javax.swing.JMenuItem();
        jSubMenu = new javax.swing.JMenu();
        jMenuItemByteHex1 = new javax.swing.JMenuItem();
        jMenuItemByteDec1 = new javax.swing.JMenuItem();
        jMenuItemByteBin1 = new javax.swing.JMenuItem();
        jMenuItemByteChar1 = new javax.swing.JMenuItem();
        jSeparatorByte1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemWord1 = new javax.swing.JMenuItem();
        jMenuItemWordSwapped1 = new javax.swing.JMenuItem();
        jSeparatorWord1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemTribyte1 = new javax.swing.JMenuItem();
        jMenuItemLong1 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItemAddress1 = new javax.swing.JMenuItem();
        jMenuItemStackWord1 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItemSpriteMono1 = new javax.swing.JMenuItem();
        jMenuItemSpriteMulti1 = new javax.swing.JMenuItem();
        jSeparatorSprite1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemText1 = new javax.swing.JMenuItem();
        jMenuItemNumText1 = new javax.swing.JMenuItem();
        jMenuItemTextZero1 = new javax.swing.JMenuItem();
        jMenuItemtextHighOne1 = new javax.swing.JMenuItem();
        jMenuItemtextShifted1 = new javax.swing.JMenuItem();
        jMenuItemTextScreen1 = new javax.swing.JMenuItem();
        jMenuItemTextPetascii1 = new javax.swing.JMenuItem();
        jMenuItemMarkGarbage = new javax.swing.JMenuItem();
        jMenuItemPlus = new javax.swing.JMenuItem();
        jMenuItemMinus = new javax.swing.JMenuItem();
        jMenuItemMemLow = new javax.swing.JMenuItem();
        jMenuItemMemHigh = new javax.swing.JMenuItem();
        jMenuOption = new javax.swing.JMenu();
        jMenuItemConfigure = new javax.swing.JMenuItem();
        jMenuItemSIDLD = new javax.swing.JMenuItem();
        jSeparatorOption = new javax.swing.JPopupMenu.Separator();
        jMenuItemViewProject = new javax.swing.JMenuItem();
        jMenuSource = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemDiss = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemFindDis = new javax.swing.JMenuItem();
        jMenuItemDissSaveAs = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemFindSource = new javax.swing.JMenuItem();
        jMenuItemSourceSaveAs = new javax.swing.JMenuItem();
        jMenuHelpContents = new javax.swing.JMenu();
        jMenuItemContents = new javax.swing.JMenuItem();
        jSeparatorHelp1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemLicense = new javax.swing.JMenuItem();
        jMenuItemCredits = new javax.swing.JMenuItem();
        jMenuItemAbout = new javax.swing.JMenuItem();

        jMenuItemByteHex.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/B.png"))); // NOI18N
        jMenuItemByteHex.setText("(B) Mark data as Byte (HEX)");
        jMenuItemByteHex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemByteHexActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemByteHex);

        jMenuItemByteDec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/D.png"))); // NOI18N
        jMenuItemByteDec.setText("(D) Mark data as Byte (DEC)");
        jMenuItemByteDec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemByteDecActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemByteDec);

        jMenuItemByteBin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/Y.png"))); // NOI18N
        jMenuItemByteBin.setText("(Y) Mark data as Byte (BIN)");
        jMenuItemByteBin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemByteBinActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemByteBin);

        jMenuItemByteChar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/R.png"))); // NOI18N
        jMenuItemByteChar.setText("(R) Mark data as Byte (CHAR)");
        jMenuItemByteChar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemByteCharActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemByteChar);
        jPopupMenuData.add(jSeparatorByte);

        jMenuItemWord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/W.png"))); // NOI18N
        jMenuItemWord.setText("(W) Mark data as Word");
        jMenuItemWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemWordActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemWord);

        jMenuItemWordSwapped.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/P.png"))); // NOI18N
        jMenuItemWordSwapped.setText("(P) Mark data as Word Swapped");
        jMenuItemWordSwapped.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemWordSwappedActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemWordSwapped);
        jPopupMenuData.add(jSeparatorWord);

        jMenuItemTribyte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/E.png"))); // NOI18N
        jMenuItemTribyte.setText("(E) Mark data as Tribyte");
        jMenuItemTribyte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTribyteActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemTribyte);

        jMenuItemLong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/L.png"))); // NOI18N
        jMenuItemLong.setText("(L) Mark data as Long");
        jMenuItemLong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemLongActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemLong);
        jPopupMenuData.add(jSeparatorPopUpMenu0);

        jMenuItemAddress.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/A.png"))); // NOI18N
        jMenuItemAddress.setText("(A) Mark data as Address");
        jMenuItemAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAddressActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemAddress);

        jMenuItemStackWord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/S.png"))); // NOI18N
        jMenuItemStackWord.setText("(S) Mark data as Stack Word");
        jMenuItemStackWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemStackWordActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemStackWord);
        jPopupMenuData.add(jSeparatorPopUpMenu1);

        jMenuItemSpriteMono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/B.png"))); // NOI18N
        jMenuItemSpriteMono.setText("() Mark data as Monocromatic Sprite definitions");
        jMenuItemSpriteMono.setToolTipText("");
        jMenuItemSpriteMono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSpriteMonoActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemSpriteMono);

        jMenuItemSpriteMulti.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/B.png"))); // NOI18N
        jMenuItemSpriteMulti.setText("() Mark data as Multicolor Sprite definitions");
        jMenuItemSpriteMulti.setToolTipText("");
        jMenuItemSpriteMulti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSpriteMultiActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemSpriteMulti);
        jPopupMenuData.add(jSeparatorSprite);

        jMenuItemText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/T.png"))); // NOI18N
        jMenuItemText.setText("(T) Mark data as Text");
        jMenuItemText.setToolTipText("");
        jMenuItemText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTextActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemText);

        jMenuItemNumText.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/N.png"))); // NOI18N
        jMenuItemNumText.setText("(N) Mark data as Text with # chars before");
        jMenuItemNumText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNumTextActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemNumText);

        jMenuItemTextZero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/Z.png"))); // NOI18N
        jMenuItemTextZero.setText("(Z) Mark data as text terminated with 0");
        jMenuItemTextZero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTextZeroActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemTextZero);

        jMenuItemtextHighOne.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/M.png"))); // NOI18N
        jMenuItemtextHighOne.setText("(M) Mark data as Text with high bit 1");
        jMenuItemtextHighOne.setToolTipText("");
        jMenuItemtextHighOne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemtextHighOneActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemtextHighOne);

        jMenuItemtextShifted.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/H.png"))); // NOI18N
        jMenuItemtextShifted.setText("(H) Mark data as Text shifted and high bit 1");
        jMenuItemtextShifted.setToolTipText("");
        jMenuItemtextShifted.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemtextShiftedActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemtextShifted);

        jMenuItemTextScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/C.png"))); // NOI18N
        jMenuItemTextScreen.setText("(C) Mark data as Text converted to screen code");
        jMenuItemTextScreen.setToolTipText("");
        jMenuItemTextScreen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTextScreenActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemTextScreen);

        jMenuItemTextPetascii.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/I.png"))); // NOI18N
        jMenuItemTextPetascii.setText("(I) Mark data as Text converted to petAscii code");
        jMenuItemTextPetascii.setToolTipText("");
        jMenuItemTextPetascii.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemTextPetasciiActionPerformed(evt);
            }
        });
        jPopupMenuData.add(jMenuItemTextPetascii);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("JC64Dis");
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);

        jButtonNewProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/filenew.png"))); // NOI18N
        jButtonNewProject.setToolTipText("New project");
        jButtonNewProject.setFocusable(false);
        jButtonNewProject.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNewProject.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewProjectActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonNewProject);

        jButtonOpenProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/fileopen.png"))); // NOI18N
        jButtonOpenProject.setToolTipText("Open project");
        jButtonOpenProject.setFocusable(false);
        jButtonOpenProject.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonOpenProject.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonOpenProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenProjectActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonOpenProject);

        jButtonClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/close.png"))); // NOI18N
        jButtonClose.setToolTipText("Close the project");
        jButtonClose.setFocusable(false);
        jButtonClose.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClose.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonClose);

        jButtonSaveProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/filesave.png"))); // NOI18N
        jButtonSaveProject.setToolTipText("Save project");
        jButtonSaveProject.setFocusable(false);
        jButtonSaveProject.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSaveProject.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSaveProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveProjectActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonSaveProject);

        jButtonSaveProjectAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/filesaveas.png"))); // NOI18N
        jButtonSaveProjectAs.setToolTipText("Save project as");
        jButtonSaveProjectAs.setFocusable(false);
        jButtonSaveProjectAs.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSaveProjectAs.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSaveProjectAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveProjectAsActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonSaveProjectAs);

        jButtonMPR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/create.png"))); // NOI18N
        jButtonMPR.setToolTipText("Create a MRP archive");
        jButtonMPR.setFocusable(false);
        jButtonMPR.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMPR.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMPR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMPRActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMPR);

        jButtonMerge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/merge.png"))); // NOI18N
        jButtonMerge.setToolTipText("Collaborative merge");
        jButtonMerge.setFocusable(false);
        jButtonMerge.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMerge.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMerge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMergeActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMerge);

        jButtonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/exit.png"))); // NOI18N
        jButtonExit.setToolTipText("Exit application");
        jButtonExit.setFocusable(false);
        jButtonExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExitActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonExit);
        jToolBar.add(jSeparatorButton1);

        jButtonClearDMem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/eraser.png"))); // NOI18N
        jButtonClearDMem.setToolTipText("Erase dasm automatic comment");
        jButtonClearDMem.setFocusable(false);
        jButtonClearDMem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClearDMem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonClearDMem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearDMemActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonClearDMem);

        jButtonClearUMem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/eraser1.png"))); // NOI18N
        jButtonClearUMem.setToolTipText("Erase user comment");
        jButtonClearUMem.setFocusable(false);
        jButtonClearUMem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClearUMem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonClearUMem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearUMemActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonClearUMem);

        jButtonClearDLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/eraser2.png"))); // NOI18N
        jButtonClearDLabel.setToolTipText("Erase dasm automatic label");
        jButtonClearDLabel.setFocusable(false);
        jButtonClearDLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonClearDLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonClearDLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonClearDLabelActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonClearDLabel);

        jButtonAddUserComm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/comm.png"))); // NOI18N
        jButtonAddUserComm.setToolTipText("Add user comment");
        jButtonAddUserComm.setFocusable(false);
        jButtonAddUserComm.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAddUserComm.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddUserComm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUserCommActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonAddUserComm);

        jButtonAddUserBlock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/block.png"))); // NOI18N
        jButtonAddUserBlock.setToolTipText("Add a block user comment");
        jButtonAddUserBlock.setFocusable(false);
        jButtonAddUserBlock.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAddUserBlock.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddUserBlock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUserBlockActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonAddUserBlock);

        jButtonAddUserLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mem2.png"))); // NOI18N
        jButtonAddUserLabel.setToolTipText("Add user label");
        jButtonAddUserLabel.setFocusable(false);
        jButtonAddUserLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAddUserLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddUserLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUserLabelActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonAddUserLabel);

        jButtonAddUserLabelOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mem3.png"))); // NOI18N
        jButtonAddUserLabelOp.setToolTipText("Add user label on next word address");
        jButtonAddUserLabelOp.setFocusable(false);
        jButtonAddUserLabelOp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAddUserLabelOp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAddUserLabelOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddUserLabelOpActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonAddUserLabelOp);

        jButtonMarkCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/code.png"))); // NOI18N
        jButtonMarkCode.setToolTipText("Mark the selected addresses as code");
        jButtonMarkCode.setFocusable(false);
        jButtonMarkCode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMarkCode.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMarkCode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonMarkCodeMouseEntered(evt);
            }
        });
        jButtonMarkCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMarkCodeActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMarkCode);

        jButtonMarkData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/data.png"))); // NOI18N
        jButtonMarkData.setToolTipText("Mark the selected addresses as data");
        jButtonMarkData.setFocusable(false);
        jButtonMarkData.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMarkData.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMarkData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonMarkDataMouseEntered(evt);
            }
        });
        jButtonMarkData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMarkDataActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMarkData);

        jButtonMarkGarbage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/garb.png"))); // NOI18N
        jButtonMarkGarbage.setToolTipText("Mark the selected addresses as garbage");
        jButtonMarkGarbage.setFocusable(false);
        jButtonMarkGarbage.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMarkGarbage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMarkGarbage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonMarkGarbageMouseEntered(evt);
            }
        });
        jButtonMarkGarbage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMarkGarbageActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMarkGarbage);

        jButtonMarkPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/plus.png"))); // NOI18N
        jButtonMarkPlus.setToolTipText("Mark the selected addresses as +");
        jButtonMarkPlus.setFocusable(false);
        jButtonMarkPlus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMarkPlus.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMarkPlus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMarkPlusActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMarkPlus);

        jButtonMarkMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/minus.png"))); // NOI18N
        jButtonMarkMinus.setToolTipText("Mark the selected addresses as -");
        jButtonMarkMinus.setFocusable(false);
        jButtonMarkMinus.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMarkMinus.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMarkMinus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMarkMinusActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMarkMinus);

        jButtonMarkLow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/min.png"))); // NOI18N
        jButtonMarkLow.setToolTipText("Assign the selected address as #<");
        jButtonMarkLow.setFocusable(false);
        jButtonMarkLow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMarkLow.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMarkLow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMarkLowActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMarkLow);

        jButtonMarkMax.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/max.png"))); // NOI18N
        jButtonMarkMax.setToolTipText("Assign the selected address as #>");
        jButtonMarkMax.setFocusable(false);
        jButtonMarkMax.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonMarkMax.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonMarkMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMarkMaxActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonMarkMax);
        jToolBar.add(jSeparatorButton3);

        jButtonConfigure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/configure.png"))); // NOI18N
        jButtonConfigure.setToolTipText("Set general option");
        jButtonConfigure.setFocusable(false);
        jButtonConfigure.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonConfigure.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonConfigure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConfigureActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonConfigure);

        jButtonSIDLD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mem.png"))); // NOI18N
        jButtonSIDLD.setToolTipText("Apply SIDLD flags to memory");
        jButtonSIDLD.setFocusable(false);
        jButtonSIDLD.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSIDLD.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSIDLD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSIDLDActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonSIDLD);

        jButtonViewProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/view_detailed.png"))); // NOI18N
        jButtonViewProject.setToolTipText("View project");
        jButtonViewProject.setFocusable(false);
        jButtonViewProject.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonViewProject.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonViewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewProjectActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonViewProject);
        jToolBar.add(jSeparatorButton2);

        jButtonFindMem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/finda.png"))); // NOI18N
        jButtonFindMem.setToolTipText("Find a memory address");
        jButtonFindMem.setFocusable(false);
        jButtonFindMem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonFindMem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonFindMem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFindMemActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonFindMem);

        jButtonDisassemble.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/exec.png"))); // NOI18N
        jButtonDisassemble.setToolTipText("Disassemble");
        jButtonDisassemble.setFocusable(false);
        jButtonDisassemble.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDisassemble.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDisassemble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDisassembleActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonDisassemble);

        jButtonFindDis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/findd.png"))); // NOI18N
        jButtonFindDis.setToolTipText("Find a text in preview");
        jButtonFindDis.setFocusable(false);
        jButtonFindDis.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonFindDis.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonFindDis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFindDisActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonFindDis);

        jButtonExportAsDiss.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/exportas1.png"))); // NOI18N
        jButtonExportAsDiss.setToolTipText("Save preview file");
        jButtonExportAsDiss.setFocusable(false);
        jButtonExportAsDiss.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExportAsDiss.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExportAsDiss.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportAsDissActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonExportAsDiss);

        jButtonFindSource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/finds.png"))); // NOI18N
        jButtonFindSource.setToolTipText("Find a text in source");
        jButtonFindSource.setFocusable(false);
        jButtonFindSource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonFindSource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonFindSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFindSourceActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonFindSource);

        jButtonExportAsSource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/exportas2.png"))); // NOI18N
        jButtonExportAsSource.setToolTipText("Save source file");
        jButtonExportAsSource.setFocusable(false);
        jButtonExportAsSource.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExportAsSource.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExportAsSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonExportAsSourceActionPerformed(evt);
            }
        });
        jToolBar.add(jButtonExportAsSource);

        jSplitPaneExternal.setToolTipText("");

        jSplitPaneInternal.setResizeWeight(0.5);
        jSplitPaneInternal.setToolTipText("");

        rSyntaxTextAreaDis.setEditable(false);
        rSyntaxTextAreaDis.setColumns(20);
        rSyntaxTextAreaDis.setRows(5);
        rSyntaxTextAreaDis.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        rSyntaxTextAreaDis.setSyntaxEditingStyle("text/asm6502");
        findDialogDis=new FindDialog(this, new SearchListener() {
            @Override
            public void searchEvent(SearchEvent e) {
                SearchEvent.Type type = e.getType();
                SearchContext context = e.getSearchContext();
                SearchResult result;

                switch (type) {
                    default: // Prevent FindBugs warning later
                    case MARK_ALL:
                    result = SearchEngine.markAll(rSyntaxTextAreaDis, context);
                    break;
                    case FIND:
                    result = SearchEngine.find(rSyntaxTextAreaDis, context);
                    if (!result.wasFound()) {
                        UIManager.getLookAndFeel().provideErrorFeedback(rSyntaxTextAreaDis);
                    }
                    break;
                }

            }

            @Override
            public String getSelectedText() {
                return rSyntaxTextAreaDis.getSelectedText();
            }
        }
    );

    rSyntaxTextAreaDis.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK),
        new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                execute(SOURCE_FINDD);
            }
        }
    );
    rSyntaxTextAreaDis.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            rSyntaxTextAreaDisMouseReleased(evt);
        }
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            rSyntaxTextAreaDisMouseClicked(evt);
        }
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            rSyntaxTextAreaDisMouseEntered(evt);
        }
    });
    jScrollPaneLeft.setViewportView(rSyntaxTextAreaDis);

    jSplitPaneInternal.setLeftComponent(jScrollPaneLeft);

    rSyntaxTextAreaSource.setEditable(false);
    rSyntaxTextAreaSource.setColumns(20);
    rSyntaxTextAreaSource.setRows(5);
    rSyntaxTextAreaSource.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
    rSyntaxTextAreaSource.setSyntaxEditingStyle("text/asm6502");
    findDialogSource=new FindDialog(this, new SearchListener() {
        @Override
        public void searchEvent(SearchEvent e) {
            SearchEvent.Type type = e.getType();
            SearchContext context = e.getSearchContext();
            SearchResult result;

            switch (type) {
                default: // Prevent FindBugs warning later
                case MARK_ALL:
                result = SearchEngine.markAll(rSyntaxTextAreaSource, context);
                break;
                case FIND:
                result = SearchEngine.find(rSyntaxTextAreaSource, context);
                if (!result.wasFound()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(rSyntaxTextAreaSource);
                }
                break;
            }

        }

        @Override
        public String getSelectedText() {
            return rSyntaxTextAreaSource.getSelectedText();
        }
    }
    );

    rSyntaxTextAreaSource.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK),
        new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                execute(SOURCE_FINDS);
            }
        }
    );
    rSyntaxTextAreaSource.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            rSyntaxTextAreaSourceMouseReleased(evt);
        }
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            rSyntaxTextAreaSourceMouseClicked(evt);
        }
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            rSyntaxTextAreaSourceMouseEntered(evt);
        }
    });
    jScrollPaneRight.setViewportView(rSyntaxTextAreaSource);

    jSplitPaneInternal.setRightComponent(jScrollPaneRight);

    jSplitPaneExternal.setRightComponent(jSplitPaneInternal);

    jScrollPaneMemory.setPreferredSize(new java.awt.Dimension(170, 403));

    jTableMemory.setModel(dataTableModelMemory);
    jTableMemory.setDefaultRenderer(Integer.class, memoryTableCellRenderer);
    jTableMemory.getColumnModel().getColumn(0).setPreferredWidth(310);

    InputMap im = this.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    ActionMap am = this.getRootPane().getActionMap();

    //add custom action
    im.put(KeyStroke.getKeyStroke("control F"), "save");
    am.put("save", new AbstractAction(){
        @Override
        public void actionPerformed(ActionEvent ae) {
            execute(SOURCE_FINDA);
        }
    });

    ((InputMap)UIManager.get("Table.ancestorInputMap")).put(KeyStroke.getKeyStroke("control F"), "none");

    jTableMemory.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTableMemoryMouseClicked(evt);
        }
    });
    jScrollPaneMemory.setViewportView(jTableMemory);

    jSplitPaneExternal.setLeftComponent(jScrollPaneMemory);

    jMenuBar.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            jMenuBarMouseEntered(evt);
        }
    });

    jMenuMerge.setText("File");

    jMenuItemNewProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemNewProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/filenew.png"))); // NOI18N
    jMenuItemNewProject.setMnemonic('n');
    jMenuItemNewProject.setText("New Project");
    jMenuItemNewProject.setToolTipText("");
    jMenuItemNewProject.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemNewProjectActionPerformed(evt);
        }
    });
    jMenuMerge.add(jMenuItemNewProject);
    jMenuMerge.add(jSeparatorProject1);

    jMenuItemOpenProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemOpenProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/fileopen.png"))); // NOI18N
    jMenuItemOpenProject.setMnemonic('o');
    jMenuItemOpenProject.setText("Open Project");
    jMenuItemOpenProject.setToolTipText("");
    jMenuItemOpenProject.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemOpenProjectActionPerformed(evt);
        }
    });
    jMenuMerge.add(jMenuItemOpenProject);

    jMenuItemCloseProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemCloseProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/close.png"))); // NOI18N
    jMenuItemCloseProject.setMnemonic('c');
    jMenuItemCloseProject.setText("Close Project");
    jMenuItemCloseProject.setToolTipText("");
    jMenuItemCloseProject.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemCloseProjectActionPerformed(evt);
        }
    });
    jMenuMerge.add(jMenuItemCloseProject);
    jMenuMerge.add(jSeparatorProject2);

    jMenuItemSaveProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemSaveProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/filesave.png"))); // NOI18N
    jMenuItemSaveProject.setMnemonic('s');
    jMenuItemSaveProject.setText("Save Project");
    jMenuItemSaveProject.setToolTipText("");
    jMenuItemSaveProject.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemSaveProjectActionPerformed(evt);
        }
    });
    jMenuMerge.add(jMenuItemSaveProject);

    jMenuItemSaveAsProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemSaveAsProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/filesaveas.png"))); // NOI18N
    jMenuItemSaveAsProject.setMnemonic('v');
    jMenuItemSaveAsProject.setText("Save Project As");
    jMenuItemSaveAsProject.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemSaveAsProjectActionPerformed(evt);
        }
    });
    jMenuMerge.add(jMenuItemSaveAsProject);
    jMenuMerge.add(jSeparatorProject3);

    jMenuItemMPR.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemMPR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/create.png"))); // NOI18N
    jMenuItemMPR.setText("Create a MPR archive");
    jMenuItemMPR.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemMPRActionPerformed(evt);
        }
    });
    jMenuMerge.add(jMenuItemMPR);

    jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/merge.png"))); // NOI18N
    jMenuItem2.setMnemonic('r');
    jMenuItem2.setText("Collaborative merge");
    jMenuItem2.setToolTipText("");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem2ActionPerformed(evt);
        }
    });
    jMenuMerge.add(jMenuItem2);

    jMenuItemExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/exit.png"))); // NOI18N
    jMenuItemExit.setMnemonic('x');
    jMenuItemExit.setText("Exit");
    jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemExitActionPerformed(evt);
        }
    });
    jMenuMerge.add(jMenuItemExit);

    jMenuBar.add(jMenuMerge);

    jMenuMemory.setText("Memory");

    jMenuItemClearDMem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemClearDMem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/eraser.png"))); // NOI18N
    jMenuItemClearDMem.setMnemonic('a');
    jMenuItemClearDMem.setText("Clear dasm automatic comment");
    jMenuItemClearDMem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemClearDMemActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemClearDMem);

    jMenuItemClearUMem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemClearUMem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/eraser1.png"))); // NOI18N
    jMenuItemClearUMem.setMnemonic('m');
    jMenuItemClearUMem.setText("Clear user comment");
    jMenuItemClearUMem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemClearUMemActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemClearUMem);

    jMenuItemClearDLabel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemClearDLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/eraser2.png"))); // NOI18N
    jMenuItemClearDLabel.setMnemonic('r');
    jMenuItemClearDLabel.setText("Clear dasm automatic label");
    jMenuItemClearDLabel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemClearDLabelActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemClearDLabel);
    jMenuMemory.add(jSeparator4);

    jMenuItemAddComment.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemAddComment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/comm.png"))); // NOI18N
    jMenuItemAddComment.setMnemonic('u');
    jMenuItemAddComment.setText("Add user comment");
    jMenuItemAddComment.setToolTipText("");
    jMenuItemAddComment.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemAddCommentActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemAddComment);

    jMenuItemAddBlock.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemAddBlock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/block.png"))); // NOI18N
    jMenuItemAddBlock.setMnemonic('b');
    jMenuItemAddBlock.setText("Add user block comment");
    jMenuItemAddBlock.setToolTipText("");
    jMenuItemAddBlock.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemAddBlockActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemAddBlock);

    jMenuItemUserLabel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemUserLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/mem2.png"))); // NOI18N
    jMenuItemUserLabel.setMnemonic('l');
    jMenuItemUserLabel.setText("Add user label");
    jMenuItemUserLabel.setToolTipText("");
    jMenuItemUserLabel.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemUserLabelActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemUserLabel);

    jMenuItemUserLabelOp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemUserLabelOp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/mem3.png"))); // NOI18N
    jMenuItemUserLabelOp.setMnemonic('n');
    jMenuItemUserLabelOp.setText("Add user label on next address");
    jMenuItemUserLabelOp.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemUserLabelOpActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemUserLabelOp);
    jMenuMemory.add(jSeparator3);

    jMenuItemMarkCode.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemMarkCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/code.png"))); // NOI18N
    jMenuItemMarkCode.setMnemonic('c');
    jMenuItemMarkCode.setText("Mark as code");
    jMenuItemMarkCode.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemMarkCodeActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemMarkCode);

    jMenuItemMarkData.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemMarkData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/data.png"))); // NOI18N
    jMenuItemMarkData.setMnemonic('d');
    jMenuItemMarkData.setText("Mark as data");
    jMenuItemMarkData.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemMarkDataActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemMarkData);

    jSubMenu.setText("(more specific data selection)");

    jMenuItemByteHex1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemByteHex1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/B.png"))); // NOI18N
    jMenuItemByteHex1.setMnemonic('b');
    jMenuItemByteHex1.setText("(B) Mark data as Byte (HEX)");
    jMenuItemByteHex1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemByteHex1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemByteHex1);

    jMenuItemByteDec1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemByteDec1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/D.png"))); // NOI18N
    jMenuItemByteDec1.setMnemonic('d');
    jMenuItemByteDec1.setText("(D) Mark data as Byte (DEC)");
    jMenuItemByteDec1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemByteDec1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemByteDec1);

    jMenuItemByteBin1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemByteBin1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/Y.png"))); // NOI18N
    jMenuItemByteBin1.setMnemonic('y');
    jMenuItemByteBin1.setText("(Y) Mark data as Byte (BIN)");
    jMenuItemByteBin1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemByteBin1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemByteBin1);

    jMenuItemByteChar1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemByteChar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/R.png"))); // NOI18N
    jMenuItemByteChar1.setMnemonic('r');
    jMenuItemByteChar1.setText("(R) Mark data as Byte (CHAR)");
    jMenuItemByteChar1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemByteChar1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemByteChar1);
    jSubMenu.add(jSeparatorByte1);

    jMenuItemWord1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemWord1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/W.png"))); // NOI18N
    jMenuItemWord1.setMnemonic('w');
    jMenuItemWord1.setText("(W) Mark data as Word");
    jMenuItemWord1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemWord1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemWord1);

    jMenuItemWordSwapped1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemWordSwapped1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/P.png"))); // NOI18N
    jMenuItemWordSwapped1.setMnemonic('p');
    jMenuItemWordSwapped1.setText("(P) Mark data as Word Swapped");
    jMenuItemWordSwapped1.setToolTipText("");
    jMenuItemWordSwapped1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemWordSwapped1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemWordSwapped1);
    jSubMenu.add(jSeparatorWord1);

    jMenuItemTribyte1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/E.png"))); // NOI18N
    jMenuItemTribyte1.setText("(E) Mark data as Tribyte");
    jMenuItemTribyte1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemTribyte1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemTribyte1);

    jMenuItemLong1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemLong1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/L.png"))); // NOI18N
    jMenuItemLong1.setMnemonic('l');
    jMenuItemLong1.setText("(L) Mark data as Long");
    jMenuItemLong1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemLong1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemLong1);
    jSubMenu.add(jSeparator5);

    jMenuItemAddress1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemAddress1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/A.png"))); // NOI18N
    jMenuItemAddress1.setMnemonic('a');
    jMenuItemAddress1.setText("(A) Mark data as Address");
    jMenuItemAddress1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemAddress1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemAddress1);

    jMenuItemStackWord1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemStackWord1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/S.png"))); // NOI18N
    jMenuItemStackWord1.setMnemonic('s');
    jMenuItemStackWord1.setText("(S) Mark data as Stack Work");
    jMenuItemStackWord1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemStackWord1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemStackWord1);
    jSubMenu.add(jSeparator6);

    jMenuItemSpriteMono1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/B.png"))); // NOI18N
    jMenuItemSpriteMono1.setText("() Mark data as Monocromatic Sprite definitions");
    jMenuItemSpriteMono1.setToolTipText("");
    jMenuItemSpriteMono1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemSpriteMono1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemSpriteMono1);

    jMenuItemSpriteMulti1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/B.png"))); // NOI18N
    jMenuItemSpriteMulti1.setText("() Mark data as Multicolor Sprite definitions");
    jMenuItemSpriteMulti1.setToolTipText("");
    jMenuItemSpriteMulti1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemSpriteMulti1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemSpriteMulti1);
    jSubMenu.add(jSeparatorSprite1);

    jMenuItemText1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemText1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/T.png"))); // NOI18N
    jMenuItemText1.setMnemonic('t');
    jMenuItemText1.setText("(T) Mark data as Text");
    jMenuItemText1.setToolTipText("");
    jMenuItemText1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemText1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemText1);

    jMenuItemNumText1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemNumText1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/N.png"))); // NOI18N
    jMenuItemNumText1.setMnemonic('n');
    jMenuItemNumText1.setText("(N) Mark data as Text with # chars before");
    jMenuItemNumText1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemNumText1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemNumText1);

    jMenuItemTextZero1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemTextZero1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/Z.png"))); // NOI18N
    jMenuItemTextZero1.setMnemonic('z');
    jMenuItemTextZero1.setText("(Z) Mark data as text terminated with 0");
    jMenuItemTextZero1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemTextZero1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemTextZero1);

    jMenuItemtextHighOne1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemtextHighOne1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/M.png"))); // NOI18N
    jMenuItemtextHighOne1.setMnemonic('m');
    jMenuItemtextHighOne1.setText("(M) Mark data as Text with high bit 1");
    jMenuItemtextHighOne1.setToolTipText("");
    jMenuItemtextHighOne1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemtextHighOne1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemtextHighOne1);

    jMenuItemtextShifted1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemtextShifted1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/H.png"))); // NOI18N
    jMenuItemtextShifted1.setMnemonic('h');
    jMenuItemtextShifted1.setText("(H) Mark data as Text shifted and high bit 1");
    jMenuItemtextShifted1.setToolTipText("");
    jMenuItemtextShifted1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemtextShifted1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemtextShifted1);

    jMenuItemTextScreen1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemTextScreen1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/C.png"))); // NOI18N
    jMenuItemTextScreen1.setMnemonic('c');
    jMenuItemTextScreen1.setText("(C) Mark data as Text converted to screen code");
    jMenuItemTextScreen1.setToolTipText("");
    jMenuItemTextScreen1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemTextScreen1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemTextScreen1);

    jMenuItemTextPetascii1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.ALT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemTextPetascii1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/I.png"))); // NOI18N
    jMenuItemTextPetascii1.setMnemonic('i');
    jMenuItemTextPetascii1.setText("(I) Mark data as Text converted to petAscii code");
    jMenuItemTextPetascii1.setToolTipText("");
    jMenuItemTextPetascii1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemTextPetascii1ActionPerformed(evt);
        }
    });
    jSubMenu.add(jMenuItemTextPetascii1);

    jMenuMemory.add(jSubMenu);

    jMenuItemMarkGarbage.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemMarkGarbage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/garb.png"))); // NOI18N
    jMenuItemMarkGarbage.setMnemonic('g');
    jMenuItemMarkGarbage.setText("Mark as garbage");
    jMenuItemMarkGarbage.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemMarkGarbageActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemMarkGarbage);

    jMenuItemPlus.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ADD, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/plus.png"))); // NOI18N
    jMenuItemPlus.setText("Assign the selected address as +");
    jMenuItemPlus.setToolTipText("");
    jMenuItemPlus.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemPlusActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemPlus);

    jMenuItemMinus.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SUBTRACT, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/minus.png"))); // NOI18N
    jMenuItemMinus.setText("Assign the selected address as -");
    jMenuItemMinus.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemMinusActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemMinus);

    jMenuItemMemLow.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_LESS, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemMemLow.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/min.png"))); // NOI18N
    jMenuItemMemLow.setText("Assign the selected address as #<");
    jMenuItemMemLow.setToolTipText("");
    jMenuItemMemLow.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemMemLowActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemMemLow);

    jMenuItemMemHigh.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_GREATER, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemMemHigh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/max.png"))); // NOI18N
    jMenuItemMemHigh.setText("Assign the selected address as #>");
    jMenuItemMemHigh.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemMemHighActionPerformed(evt);
        }
    });
    jMenuMemory.add(jMenuItemMemHigh);

    jMenuBar.add(jMenuMemory);

    jMenuOption.setText("Option");
    jMenuOption.setToolTipText("");

    jMenuItemConfigure.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemConfigure.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/configure.png"))); // NOI18N
    jMenuItemConfigure.setMnemonic('o');
    jMenuItemConfigure.setText("General Option");
    jMenuItemConfigure.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemConfigureActionPerformed(evt);
        }
    });
    jMenuOption.add(jMenuItemConfigure);

    jMenuItemSIDLD.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemSIDLD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/mem.png"))); // NOI18N
    jMenuItemSIDLD.setMnemonic('e');
    jMenuItemSIDLD.setText("Apply SIDLD flags to memory");
    jMenuItemSIDLD.setToolTipText("");
    jMenuItemSIDLD.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemSIDLDActionPerformed(evt);
        }
    });
    jMenuOption.add(jMenuItemSIDLD);
    jMenuOption.add(jSeparatorOption);

    jMenuItemViewProject.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemViewProject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/view_detailed.png"))); // NOI18N
    jMenuItemViewProject.setMnemonic('j');
    jMenuItemViewProject.setText("View Project");
    jMenuItemViewProject.setToolTipText("");
    jMenuItemViewProject.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemViewProjectActionPerformed(evt);
        }
    });
    jMenuOption.add(jMenuItemViewProject);

    jMenuBar.add(jMenuOption);

    jMenuSource.setText("Source");

    jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/finda.png"))); // NOI18N
    jMenuItem1.setText("Find memory address");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem1ActionPerformed(evt);
        }
    });
    jMenuSource.add(jMenuItem1);

    jMenuItemDiss.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemDiss.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/exec.png"))); // NOI18N
    jMenuItemDiss.setMnemonic('d');
    jMenuItemDiss.setText("Disassemble");
    jMenuItemDiss.setToolTipText("");
    jMenuItemDiss.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemDissActionPerformed(evt);
        }
    });
    jMenuSource.add(jMenuItemDiss);
    jMenuSource.add(jSeparator1);

    jMenuItemFindDis.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemFindDis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/findd.png"))); // NOI18N
    jMenuItemFindDis.setText("Find text in preview");
    jMenuItemFindDis.setToolTipText("");
    jMenuItemFindDis.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemFindDisActionPerformed(evt);
        }
    });
    jMenuSource.add(jMenuItemFindDis);

    jMenuItemDissSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemDissSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/exportas1.png"))); // NOI18N
    jMenuItemDissSaveAs.setMnemonic('p');
    jMenuItemDissSaveAs.setText("Export As of preview");
    jMenuItemDissSaveAs.setToolTipText("");
    jMenuItemDissSaveAs.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemDissSaveAsActionPerformed(evt);
        }
    });
    jMenuSource.add(jMenuItemDissSaveAs);
    jMenuSource.add(jSeparator2);

    jMenuItemFindSource.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemFindSource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/finds.png"))); // NOI18N
    jMenuItemFindSource.setText("Find text in source");
    jMenuItemFindSource.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemFindSourceActionPerformed(evt);
        }
    });
    jMenuSource.add(jMenuItemFindSource);

    jMenuItemSourceSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemSourceSaveAs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/exportas2.png"))); // NOI18N
    jMenuItemSourceSaveAs.setMnemonic('s');
    jMenuItemSourceSaveAs.setText("Export As of source");
    jMenuItemSourceSaveAs.setToolTipText("");
    jMenuItemSourceSaveAs.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemSourceSaveAsActionPerformed(evt);
        }
    });
    jMenuSource.add(jMenuItemSourceSaveAs);

    jMenuBar.add(jMenuSource);

    jMenuHelpContents.setText("Help");

    jMenuItemContents.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemContents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/help_index.png"))); // NOI18N
    jMenuItemContents.setMnemonic('h');
    jMenuItemContents.setText("Help contents");
    jMenuItemContents.setToolTipText("");
    jMenuItemContents.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemContentsActionPerformed(evt);
        }
    });
    jMenuHelpContents.add(jMenuItemContents);
    jMenuHelpContents.add(jSeparatorHelp1);

    jMenuItemLicense.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemLicense.setMnemonic('l');
    jMenuItemLicense.setText("License");
    jMenuItemLicense.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemLicenseActionPerformed(evt);
        }
    });
    jMenuHelpContents.add(jMenuItemLicense);

    jMenuItemCredits.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemCredits.setMnemonic('d');
    jMenuItemCredits.setText("Credits");
    jMenuItemCredits.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemCreditsActionPerformed(evt);
        }
    });
    jMenuHelpContents.add(jMenuItemCredits);

    jMenuItemAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
    jMenuItemAbout.setMnemonic('a');
    jMenuItemAbout.setText("About");
    jMenuItemAbout.setToolTipText("");
    jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItemAboutActionPerformed(evt);
        }
    });
    jMenuHelpContents.add(jMenuItemAbout);

    jMenuBar.add(jMenuHelpContents);

    setJMenuBar(jMenuBar);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jSplitPaneExternal)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSplitPaneExternal, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
      execute(APP_EXIT);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    private void jMenuItemNewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNewProjectActionPerformed
      execute(PROJ_NEW);
    }//GEN-LAST:event_jMenuItemNewProjectActionPerformed

    private void jMenuItemOpenProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenProjectActionPerformed
     execute(PROJ_OPEN);
    }//GEN-LAST:event_jMenuItemOpenProjectActionPerformed

    private void jMenuItemCloseProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCloseProjectActionPerformed
      execute(PROJ_CLOSE);
    }//GEN-LAST:event_jMenuItemCloseProjectActionPerformed

    private void jMenuItemSaveProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveProjectActionPerformed
      execute(PROJ_SAVE);
    }//GEN-LAST:event_jMenuItemSaveProjectActionPerformed

    private void jMenuItemSaveAsProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAsProjectActionPerformed
      execute(PROJ_SAVEAS);
    }//GEN-LAST:event_jMenuItemSaveAsProjectActionPerformed

    private void jButtonNewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewProjectActionPerformed
      execute(PROJ_NEW);
    }//GEN-LAST:event_jButtonNewProjectActionPerformed

    private void jButtonOpenProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenProjectActionPerformed
      execute(PROJ_OPEN);
    }//GEN-LAST:event_jButtonOpenProjectActionPerformed

    private void jButtonSaveProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveProjectActionPerformed
      execute(PROJ_SAVE);
    }//GEN-LAST:event_jButtonSaveProjectActionPerformed

    private void jButtonSaveProjectAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveProjectAsActionPerformed
      execute(PROJ_SAVEAS);
    }//GEN-LAST:event_jButtonSaveProjectAsActionPerformed

    private void jButtonConfigureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConfigureActionPerformed
      execute(OPTION_CONFIGURE);
    }//GEN-LAST:event_jButtonConfigureActionPerformed

    private void jMenuItemConfigureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConfigureActionPerformed
      execute(OPTION_CONFIGURE);
    }//GEN-LAST:event_jMenuItemConfigureActionPerformed

    private void jButtonDisassembleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDisassembleActionPerformed
      execute(SOURCE_DISASS);
    }//GEN-LAST:event_jButtonDisassembleActionPerformed

    private void jMenuItemDissActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDissActionPerformed
      execute(SOURCE_DISASS);
    }//GEN-LAST:event_jMenuItemDissActionPerformed

    private void jMenuItemViewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemViewProjectActionPerformed
      execute(OPTION_VIEWPRJ); 
    }//GEN-LAST:event_jMenuItemViewProjectActionPerformed

    private void jMenuItemDissSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDissSaveAsActionPerformed
      execute(SOURCE_EXPASDIS); 
    }//GEN-LAST:event_jMenuItemDissSaveAsActionPerformed

    private void jButtonExportAsDissActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportAsDissActionPerformed
      execute(SOURCE_EXPASDIS); 
    }//GEN-LAST:event_jButtonExportAsDissActionPerformed

    private void jButtonExportAsSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportAsSourceActionPerformed
      execute(SOURCE_EXPASSOURCE); 
    }//GEN-LAST:event_jButtonExportAsSourceActionPerformed

    private void jMenuItemSourceSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSourceSaveAsActionPerformed
      execute(SOURCE_EXPASSOURCE);
    }//GEN-LAST:event_jMenuItemSourceSaveAsActionPerformed

    private void jButtonViewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewProjectActionPerformed
      execute(OPTION_VIEWPRJ); 
    }//GEN-LAST:event_jButtonViewProjectActionPerformed

    private void jButtonExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExitActionPerformed
      execute(APP_EXIT); 
    }//GEN-LAST:event_jButtonExitActionPerformed

    private void jMenuItemClearDMemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClearDMemActionPerformed
      execute(MEM_CLEARDCOM);
    }//GEN-LAST:event_jMenuItemClearDMemActionPerformed

    private void jButtonClearDMemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearDMemActionPerformed
       execute(MEM_CLEARDCOM);
    }//GEN-LAST:event_jButtonClearDMemActionPerformed

    private void jButtonClearUMemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearUMemActionPerformed
      execute(MEM_CLEARUCOM);
    }//GEN-LAST:event_jButtonClearUMemActionPerformed

    private void jMenuItemClearUMemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClearUMemActionPerformed
      execute(MEM_CLEARUCOM);
    }//GEN-LAST:event_jMenuItemClearUMemActionPerformed

    private void jButtonMarkCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMarkCodeActionPerformed
      execute(MEM_MARKCODE);
    }//GEN-LAST:event_jButtonMarkCodeActionPerformed

    private void jButtonMarkDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMarkDataActionPerformed
      execute(MEM_MARKDATA);
    }//GEN-LAST:event_jButtonMarkDataActionPerformed

    private void jMenuItemMarkCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMarkCodeActionPerformed
      execute(MEM_MARKCODE);
    }//GEN-LAST:event_jMenuItemMarkCodeActionPerformed

    private void jMenuItemMarkDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMarkDataActionPerformed
      execute(MEM_MARKDATA);  
    }//GEN-LAST:event_jMenuItemMarkDataActionPerformed

    private void jButtonSIDLDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSIDLDActionPerformed
      execute(OPTION_SIDLD);
    }//GEN-LAST:event_jButtonSIDLDActionPerformed

    private void jMenuItemSIDLDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSIDLDActionPerformed
      execute(OPTION_SIDLD);
    }//GEN-LAST:event_jMenuItemSIDLDActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
      execute(PROJ_CLOSE);
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jMenuItemContentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemContentsActionPerformed
      execute(HELP_CONTENTS);
    }//GEN-LAST:event_jMenuItemContentsActionPerformed

    private void jMenuItemLicenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLicenseActionPerformed
      execute(HELP_LICENSE);
    }//GEN-LAST:event_jMenuItemLicenseActionPerformed

    private void jMenuItemCreditsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCreditsActionPerformed
      execute(HELP_CREDITS);
    }//GEN-LAST:event_jMenuItemCreditsActionPerformed

    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
      execute(HELP_ABOUT);
    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    private void rSyntaxTextAreaDisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSyntaxTextAreaDisMouseClicked
      try {
        // get starting position of clicked point  
        int pos=Utilities.getRowStart(rSyntaxTextAreaDis, rSyntaxTextAreaDis.getCaretPosition());
       
        int addr=searchAddress(rSyntaxTextAreaDis.getDocument().getText(pos,option.maxLabelLength));
        
        if (addr==-1) return;
                
        //scroll to that point
        jTableMemory.scrollRectToVisible(jTableMemory.getCellRect(addr,0, true)); 
        
        // select this row
        jTableMemory.setRowSelectionInterval(addr, addr);
      } catch (Exception e) {
          System.err.println(e);
      }
    }//GEN-LAST:event_rSyntaxTextAreaDisMouseClicked

    private void jMenuItemFindDisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindDisActionPerformed
      execute(SOURCE_FINDD);
    }//GEN-LAST:event_jMenuItemFindDisActionPerformed

    private void jMenuItemFindSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemFindSourceActionPerformed
      execute(SOURCE_FINDS);
    }//GEN-LAST:event_jMenuItemFindSourceActionPerformed

    private void jButtonFindDisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFindDisActionPerformed
      execute(SOURCE_FINDD);
    }//GEN-LAST:event_jButtonFindDisActionPerformed

    private void jButtonFindSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFindSourceActionPerformed
      execute(SOURCE_FINDS);
    }//GEN-LAST:event_jButtonFindSourceActionPerformed

    private void jButtonAddUserCommActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUserCommActionPerformed
      execute(MEM_ADDCOMM);
    }//GEN-LAST:event_jButtonAddUserCommActionPerformed

    private void jMenuItemAddCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddCommentActionPerformed
      execute(MEM_ADDCOMM);
    }//GEN-LAST:event_jMenuItemAddCommentActionPerformed

    private void jButtonAddUserLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUserLabelActionPerformed
      execute(MEM_ADDLABEL);
    }//GEN-LAST:event_jButtonAddUserLabelActionPerformed

    private void jMenuItemUserLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUserLabelActionPerformed
      execute(MEM_ADDLABEL);
    }//GEN-LAST:event_jMenuItemUserLabelActionPerformed

    private void jButtonAddUserBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUserBlockActionPerformed
      execute(MEM_ADDBLOCK);
    }//GEN-LAST:event_jButtonAddUserBlockActionPerformed

    private void jMenuItemAddBlockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddBlockActionPerformed
      execute(MEM_ADDBLOCK);
    }//GEN-LAST:event_jMenuItemAddBlockActionPerformed

    private void rSyntaxTextAreaSourceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSyntaxTextAreaSourceMouseClicked
      try {
        // get starting position of clicked point  
        int pos=Utilities.getRowStart(rSyntaxTextAreaSource, rSyntaxTextAreaSource.getCaretPosition());
        
        int addr=searchAddress(rSyntaxTextAreaSource.getDocument().getText(pos,option.maxLabelLength));
        
        if (addr==-1) return;
                
        //scroll to that point
        jTableMemory.scrollRectToVisible(jTableMemory.getCellRect(addr,0, true)); 
        
        // select this row
        jTableMemory.setRowSelectionInterval(addr, addr);
      } catch (Exception e) {
          System.err.println(e);
      }
    }//GEN-LAST:event_rSyntaxTextAreaSourceMouseClicked

    private void jMenuItemClearDLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemClearDLabelActionPerformed
      execute(MEM_CLEARDLABEL);  
    }//GEN-LAST:event_jMenuItemClearDLabelActionPerformed

    private void jButtonClearDLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearDLabelActionPerformed
      execute(MEM_CLEARDLABEL);  
    }//GEN-LAST:event_jButtonClearDLabelActionPerformed

    private void jButtonMarkLowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMarkLowActionPerformed
      execute(MEM_LOW);
    }//GEN-LAST:event_jButtonMarkLowActionPerformed

    private void jButtonMarkMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMarkMaxActionPerformed
      execute(MEM_HIGH);
    }//GEN-LAST:event_jButtonMarkMaxActionPerformed

    private void jMenuItemMemLowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMemLowActionPerformed
      execute(MEM_LOW);
    }//GEN-LAST:event_jMenuItemMemLowActionPerformed

    private void jMenuItemMemHighActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMemHighActionPerformed
      execute(MEM_HIGH);
    }//GEN-LAST:event_jMenuItemMemHighActionPerformed

    private void jButtonFindMemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFindMemActionPerformed
      execute(SOURCE_FINDA);
    }//GEN-LAST:event_jButtonFindMemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
      execute(SOURCE_FINDA);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButtonMarkPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMarkPlusActionPerformed
      execute(MEM_PLUS);
    }//GEN-LAST:event_jButtonMarkPlusActionPerformed

    private void jMenuItemPlusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemPlusActionPerformed
      execute(MEM_PLUS);
    }//GEN-LAST:event_jMenuItemPlusActionPerformed

    private void jButtonMarkMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMarkMinusActionPerformed
      execute(MEM_MINUS);
    }//GEN-LAST:event_jButtonMarkMinusActionPerformed

    private void jMenuItemMinusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMinusActionPerformed
      execute(MEM_MINUS);
    }//GEN-LAST:event_jMenuItemMinusActionPerformed

    private void jMenuItemMPRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMPRActionPerformed
      execute(OPTION_MPR);
    }//GEN-LAST:event_jMenuItemMPRActionPerformed

    private void jButtonMPRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMPRActionPerformed
      execute(OPTION_MPR);
    }//GEN-LAST:event_jButtonMPRActionPerformed

    private void jButtonAddUserLabelOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddUserLabelOpActionPerformed
      execute(MEM_ADDLABELOP);
    }//GEN-LAST:event_jButtonAddUserLabelOpActionPerformed

    private void jMenuItemUserLabelOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUserLabelOpActionPerformed
      execute(MEM_ADDLABELOP);
    }//GEN-LAST:event_jMenuItemUserLabelOpActionPerformed

    private void jButtonMarkGarbageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMarkGarbageActionPerformed
      execute(MEM_MARKGARB);
    }//GEN-LAST:event_jButtonMarkGarbageActionPerformed

    private void jMenuItemMarkGarbageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemMarkGarbageActionPerformed
      execute(MEM_MARKGARB);
    }//GEN-LAST:event_jMenuItemMarkGarbageActionPerformed

    private void rSyntaxTextAreaDisMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSyntaxTextAreaDisMouseReleased
      String selected=rSyntaxTextAreaDis.getSelectedText();
      
      int actual;
      
      int min=0xffff+1;  // min find address
      int max=-1;        // max find address
      
      // avoid no selected text
      if (selected==null) return;
      
      try {
        String lines[] = selected.split("\\r?\\n");
        for (String line: lines) {
          actual=searchAddress(line.substring(0, Math.min(line.length(), option.maxLabelLength)));  
          if (actual==-1) continue;
        
          if (actual<min) min=actual;
          if (actual>max) max=actual;
        }
      
        // if max is not -1 we find a range
        if (max==-1) return;
      
        //scroll to that point
        jTableMemory.scrollRectToVisible(jTableMemory.getCellRect(min,0, true)); 
        
        // select this rows
        jTableMemory.setRowSelectionInterval(min, max);
      } catch (Exception e) {
          System.err.println(e);;
        }  
    }//GEN-LAST:event_rSyntaxTextAreaDisMouseReleased

    private void rSyntaxTextAreaSourceMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSyntaxTextAreaSourceMouseReleased
      String selected=rSyntaxTextAreaSource.getSelectedText();
      
      int actual;
      
      int min=0xffff+1;  // min find address
      int max=-1;        // max find address
      
      // avoid no selected text
      if (selected==null) return;
      
      try {
        String lines[] = selected.split("\\r?\\n");
        for (String line: lines) {
          actual=searchAddress(line.substring(0, Math.min(line.length(), option.maxLabelLength)));  
          if (actual==-1) continue;
        
          if (actual<min) min=actual;
          if (actual>max) max=actual;
        }
      
        // if max is not -1 we find a range
        if (max==-1) return;
      
        //scroll to that point
        jTableMemory.scrollRectToVisible(jTableMemory.getCellRect(min,0, true)); 
        
        // select this rows
        jTableMemory.setRowSelectionInterval(min, max);
      } catch (Exception e) {
          System.err.println(e);;
        }  
    }//GEN-LAST:event_rSyntaxTextAreaSourceMouseReleased

    private void jTableMemoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMemoryMouseClicked
      if (evt.getClickCount() == 2) {  
        int actual;  
        
        // get the address in hex format
        int addr=jTableMemory.getSelectedRow();
        int pos=0;        

        // scan all lines for the memory location
        try {
          String preview=rSyntaxTextAreaDis.getText();
          String lines[] = preview.split("\\r?\\n");
          for (String line: lines) {
            actual=searchAddress(line.substring(0, Math.min(line.length(), option.maxLabelLength)));   
            if (actual==addr) {      
              // set preview in the find position  
              rSyntaxTextAreaDis.setCaretPosition(pos);
              rSyntaxTextAreaDis.requestFocusInWindow();
              break;
            } else {
                pos+=line.length()+1;
              }
          }
        } catch (Exception e) {
            System.err.println();  
          }  
      }
    }//GEN-LAST:event_jTableMemoryMouseClicked

    private void jButtonMergeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMergeActionPerformed
      execute(PROJ_MERGE);
    }//GEN-LAST:event_jButtonMergeActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
      execute(PROJ_MERGE);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButtonMarkDataMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMarkDataMouseEntered
      jPopupMenuData.show((JComponent)evt.getSource(), 0, ((JComponent)evt.getSource()).getHeight());
    }//GEN-LAST:event_jButtonMarkDataMouseEntered

    private void jButtonMarkCodeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMarkCodeMouseEntered
      if (jPopupMenuData.isShowing()) jPopupMenuData.setVisible(false);
    }//GEN-LAST:event_jButtonMarkCodeMouseEntered

    private void jButtonMarkGarbageMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonMarkGarbageMouseEntered
      if (jPopupMenuData.isShowing()) jPopupMenuData.setVisible(false);
    }//GEN-LAST:event_jButtonMarkGarbageMouseEntered

    private void jMenuBarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuBarMouseEntered
      if (jPopupMenuData.isShowing()) jPopupMenuData.setVisible(false);
    }//GEN-LAST:event_jMenuBarMouseEntered

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
      if (jPopupMenuData.isShowing()) jPopupMenuData.setVisible(false);
    }//GEN-LAST:event_formMouseEntered

    private void rSyntaxTextAreaDisMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSyntaxTextAreaDisMouseEntered
      if (jPopupMenuData.isShowing()) jPopupMenuData.setVisible(false);
    }//GEN-LAST:event_rSyntaxTextAreaDisMouseEntered

    private void rSyntaxTextAreaSourceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSyntaxTextAreaSourceMouseEntered
      if (jPopupMenuData.isShowing()) jPopupMenuData.setVisible(false);
    }//GEN-LAST:event_rSyntaxTextAreaSourceMouseEntered

    private void jMenuItemByteHexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemByteHexActionPerformed
      execute(MEM_MARKDATA_B);
    }//GEN-LAST:event_jMenuItemByteHexActionPerformed

    private void jMenuItemWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemWordActionPerformed
      execute(MEM_MARKDATA_W);     
    }//GEN-LAST:event_jMenuItemWordActionPerformed

    private void jMenuItemLongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLongActionPerformed
      execute(MEM_MARKDATA_L); 
    }//GEN-LAST:event_jMenuItemLongActionPerformed

    private void jMenuItemAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddressActionPerformed
      execute(MEM_MARKDATA_A);
    }//GEN-LAST:event_jMenuItemAddressActionPerformed

    private void jMenuItemStackWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemStackWordActionPerformed
      execute(MEM_MARKDATA_S);
    }//GEN-LAST:event_jMenuItemStackWordActionPerformed

    private void jMenuItemTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTextActionPerformed
      execute(MEM_MARKDATA_T);
    }//GEN-LAST:event_jMenuItemTextActionPerformed

    private void jMenuItemNumTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNumTextActionPerformed
      execute(MEM_MARKDATA_N);
    }//GEN-LAST:event_jMenuItemNumTextActionPerformed

    private void jMenuItemTextZeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTextZeroActionPerformed
      execute(MEM_MARKDATA_Z);
    }//GEN-LAST:event_jMenuItemTextZeroActionPerformed

    private void jMenuItemtextHighOneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemtextHighOneActionPerformed
      execute(MEM_MARKDATA_M);
    }//GEN-LAST:event_jMenuItemtextHighOneActionPerformed

    private void jMenuItemtextShiftedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemtextShiftedActionPerformed
      execute(MEM_MARKDATA_H);
    }//GEN-LAST:event_jMenuItemtextShiftedActionPerformed

    private void jMenuItemTextScreenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTextScreenActionPerformed
      execute(MEM_MARKDATA_C); 
    }//GEN-LAST:event_jMenuItemTextScreenActionPerformed

    private void jMenuItemByteHex1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemByteHex1ActionPerformed
      execute(MEM_MARKDATA_B);
    }//GEN-LAST:event_jMenuItemByteHex1ActionPerformed

    private void jMenuItemWord1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemWord1ActionPerformed
      execute(MEM_MARKDATA_W);      
    }//GEN-LAST:event_jMenuItemWord1ActionPerformed

    private void jMenuItemLong1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemLong1ActionPerformed
      execute(MEM_MARKDATA_L);
    }//GEN-LAST:event_jMenuItemLong1ActionPerformed

    private void jMenuItemAddress1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAddress1ActionPerformed
      execute(MEM_MARKDATA_A);       
    }//GEN-LAST:event_jMenuItemAddress1ActionPerformed

    private void jMenuItemStackWord1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemStackWord1ActionPerformed
      execute(MEM_MARKDATA_S);
    }//GEN-LAST:event_jMenuItemStackWord1ActionPerformed

    private void jMenuItemText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemText1ActionPerformed
      execute(MEM_MARKDATA_T);
    }//GEN-LAST:event_jMenuItemText1ActionPerformed

    private void jMenuItemNumText1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemNumText1ActionPerformed
      execute(MEM_MARKDATA_N);
    }//GEN-LAST:event_jMenuItemNumText1ActionPerformed

    private void jMenuItemTextZero1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTextZero1ActionPerformed
      execute(MEM_MARKDATA_Z);
    }//GEN-LAST:event_jMenuItemTextZero1ActionPerformed

    private void jMenuItemtextHighOne1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemtextHighOne1ActionPerformed
      execute(MEM_MARKDATA_M);
    }//GEN-LAST:event_jMenuItemtextHighOne1ActionPerformed

    private void jMenuItemtextShifted1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemtextShifted1ActionPerformed
      execute(MEM_MARKDATA_H);
    }//GEN-LAST:event_jMenuItemtextShifted1ActionPerformed

    private void jMenuItemTextScreen1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTextScreen1ActionPerformed
      execute(MEM_MARKDATA_C);
    }//GEN-LAST:event_jMenuItemTextScreen1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      execute(APP_EXIT);
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItemWordSwappedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemWordSwappedActionPerformed
      execute(MEM_MARKDATA_P);  
    }//GEN-LAST:event_jMenuItemWordSwappedActionPerformed

    private void jMenuItemWordSwapped1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemWordSwapped1ActionPerformed
      execute(MEM_MARKDATA_P);  
    }//GEN-LAST:event_jMenuItemWordSwapped1ActionPerformed

    private void jMenuItemByteDecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemByteDecActionPerformed
      execute(MEM_MARKDATA_D);
    }//GEN-LAST:event_jMenuItemByteDecActionPerformed

    private void jMenuItemByteBinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemByteBinActionPerformed
      execute(MEM_MARKDATA_Y);  
    }//GEN-LAST:event_jMenuItemByteBinActionPerformed

    private void jMenuItemByteCharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemByteCharActionPerformed
      execute(MEM_MARKDATA_R);  
    }//GEN-LAST:event_jMenuItemByteCharActionPerformed

    private void jMenuItemByteDec1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemByteDec1ActionPerformed
      execute(MEM_MARKDATA_D); 
    }//GEN-LAST:event_jMenuItemByteDec1ActionPerformed

    private void jMenuItemByteBin1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemByteBin1ActionPerformed
      execute(MEM_MARKDATA_Y);  
    }//GEN-LAST:event_jMenuItemByteBin1ActionPerformed

    private void jMenuItemByteChar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemByteChar1ActionPerformed
      execute(MEM_MARKDATA_R);  
    }//GEN-LAST:event_jMenuItemByteChar1ActionPerformed

    private void jMenuItemSpriteMonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSpriteMonoActionPerformed
        
    }//GEN-LAST:event_jMenuItemSpriteMonoActionPerformed

    private void jMenuItemSpriteMultiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSpriteMultiActionPerformed
       
    }//GEN-LAST:event_jMenuItemSpriteMultiActionPerformed

    private void jMenuItemTextPetascii1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTextPetascii1ActionPerformed
       execute(MEM_MARKDATA_I);  
    }//GEN-LAST:event_jMenuItemTextPetascii1ActionPerformed

    private void jMenuItemTextPetasciiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTextPetasciiActionPerformed
      execute(MEM_MARKDATA_I); 
    }//GEN-LAST:event_jMenuItemTextPetasciiActionPerformed

    private void jMenuItemTribyteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTribyteActionPerformed
      execute(MEM_MARKDATA_E);  
    }//GEN-LAST:event_jMenuItemTribyteActionPerformed

    private void jMenuItemTribyte1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemTribyte1ActionPerformed
      execute(MEM_MARKDATA_E); 
    }//GEN-LAST:event_jMenuItemTribyte1ActionPerformed

    private void jMenuItemSpriteMono1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSpriteMono1ActionPerformed
       
    }//GEN-LAST:event_jMenuItemSpriteMono1ActionPerformed

    private void jMenuItemSpriteMulti1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSpriteMulti1ActionPerformed
      
    }//GEN-LAST:event_jMenuItemSpriteMulti1ActionPerformed

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
            java.util.logging.Logger.getLogger(JDisassemblerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDisassemblerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDisassemblerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDisassemblerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        Option.installLook();
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JDisassemblerFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAddUserBlock;
    private javax.swing.JButton jButtonAddUserComm;
    private javax.swing.JButton jButtonAddUserLabel;
    private javax.swing.JButton jButtonAddUserLabelOp;
    private javax.swing.JButton jButtonClearDLabel;
    private javax.swing.JButton jButtonClearDMem;
    private javax.swing.JButton jButtonClearUMem;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonConfigure;
    private javax.swing.JButton jButtonDisassemble;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonExportAsDiss;
    private javax.swing.JButton jButtonExportAsSource;
    private javax.swing.JButton jButtonFindDis;
    private javax.swing.JButton jButtonFindMem;
    private javax.swing.JButton jButtonFindSource;
    private javax.swing.JButton jButtonMPR;
    private javax.swing.JButton jButtonMarkCode;
    private javax.swing.JButton jButtonMarkData;
    private javax.swing.JButton jButtonMarkGarbage;
    private javax.swing.JButton jButtonMarkLow;
    private javax.swing.JButton jButtonMarkMax;
    private javax.swing.JButton jButtonMarkMinus;
    private javax.swing.JButton jButtonMarkPlus;
    private javax.swing.JButton jButtonMerge;
    private javax.swing.JButton jButtonNewProject;
    private javax.swing.JButton jButtonOpenProject;
    private javax.swing.JButton jButtonSIDLD;
    private javax.swing.JButton jButtonSaveProject;
    private javax.swing.JButton jButtonSaveProjectAs;
    private javax.swing.JButton jButtonViewProject;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuHelpContents;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemAddBlock;
    private javax.swing.JMenuItem jMenuItemAddComment;
    private javax.swing.JMenuItem jMenuItemAddress;
    private javax.swing.JMenuItem jMenuItemAddress1;
    private javax.swing.JMenuItem jMenuItemByteBin;
    private javax.swing.JMenuItem jMenuItemByteBin1;
    private javax.swing.JMenuItem jMenuItemByteChar;
    private javax.swing.JMenuItem jMenuItemByteChar1;
    private javax.swing.JMenuItem jMenuItemByteDec;
    private javax.swing.JMenuItem jMenuItemByteDec1;
    private javax.swing.JMenuItem jMenuItemByteHex;
    private javax.swing.JMenuItem jMenuItemByteHex1;
    private javax.swing.JMenuItem jMenuItemClearDLabel;
    private javax.swing.JMenuItem jMenuItemClearDMem;
    private javax.swing.JMenuItem jMenuItemClearUMem;
    private javax.swing.JMenuItem jMenuItemCloseProject;
    private javax.swing.JMenuItem jMenuItemConfigure;
    private javax.swing.JMenuItem jMenuItemContents;
    private javax.swing.JMenuItem jMenuItemCredits;
    private javax.swing.JMenuItem jMenuItemDiss;
    private javax.swing.JMenuItem jMenuItemDissSaveAs;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemFindDis;
    private javax.swing.JMenuItem jMenuItemFindSource;
    private javax.swing.JMenuItem jMenuItemLicense;
    private javax.swing.JMenuItem jMenuItemLong;
    private javax.swing.JMenuItem jMenuItemLong1;
    private javax.swing.JMenuItem jMenuItemMPR;
    private javax.swing.JMenuItem jMenuItemMarkCode;
    private javax.swing.JMenuItem jMenuItemMarkData;
    private javax.swing.JMenuItem jMenuItemMarkGarbage;
    private javax.swing.JMenuItem jMenuItemMemHigh;
    private javax.swing.JMenuItem jMenuItemMemLow;
    private javax.swing.JMenuItem jMenuItemMinus;
    private javax.swing.JMenuItem jMenuItemNewProject;
    private javax.swing.JMenuItem jMenuItemNumText;
    private javax.swing.JMenuItem jMenuItemNumText1;
    private javax.swing.JMenuItem jMenuItemOpenProject;
    private javax.swing.JMenuItem jMenuItemPlus;
    private javax.swing.JMenuItem jMenuItemSIDLD;
    private javax.swing.JMenuItem jMenuItemSaveAsProject;
    private javax.swing.JMenuItem jMenuItemSaveProject;
    private javax.swing.JMenuItem jMenuItemSourceSaveAs;
    private javax.swing.JMenuItem jMenuItemSpriteMono;
    private javax.swing.JMenuItem jMenuItemSpriteMono1;
    private javax.swing.JMenuItem jMenuItemSpriteMulti;
    private javax.swing.JMenuItem jMenuItemSpriteMulti1;
    private javax.swing.JMenuItem jMenuItemStackWord;
    private javax.swing.JMenuItem jMenuItemStackWord1;
    private javax.swing.JMenuItem jMenuItemText;
    private javax.swing.JMenuItem jMenuItemText1;
    private javax.swing.JMenuItem jMenuItemTextPetascii;
    private javax.swing.JMenuItem jMenuItemTextPetascii1;
    private javax.swing.JMenuItem jMenuItemTextScreen;
    private javax.swing.JMenuItem jMenuItemTextScreen1;
    private javax.swing.JMenuItem jMenuItemTextZero;
    private javax.swing.JMenuItem jMenuItemTextZero1;
    private javax.swing.JMenuItem jMenuItemTribyte;
    private javax.swing.JMenuItem jMenuItemTribyte1;
    private javax.swing.JMenuItem jMenuItemUserLabel;
    private javax.swing.JMenuItem jMenuItemUserLabelOp;
    private javax.swing.JMenuItem jMenuItemViewProject;
    private javax.swing.JMenuItem jMenuItemWord;
    private javax.swing.JMenuItem jMenuItemWord1;
    private javax.swing.JMenuItem jMenuItemWordSwapped;
    private javax.swing.JMenuItem jMenuItemWordSwapped1;
    private javax.swing.JMenuItem jMenuItemtextHighOne;
    private javax.swing.JMenuItem jMenuItemtextHighOne1;
    private javax.swing.JMenuItem jMenuItemtextShifted;
    private javax.swing.JMenuItem jMenuItemtextShifted1;
    private javax.swing.JMenu jMenuMemory;
    private javax.swing.JMenu jMenuMerge;
    private javax.swing.JMenu jMenuOption;
    private javax.swing.JMenu jMenuSource;
    private javax.swing.JPopupMenu jPopupMenuData;
    private javax.swing.JScrollPane jScrollPaneLeft;
    private javax.swing.JScrollPane jScrollPaneMemory;
    private javax.swing.JScrollPane jScrollPaneRight;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparatorButton1;
    private javax.swing.JToolBar.Separator jSeparatorButton2;
    private javax.swing.JToolBar.Separator jSeparatorButton3;
    private javax.swing.JPopupMenu.Separator jSeparatorByte;
    private javax.swing.JPopupMenu.Separator jSeparatorByte1;
    private javax.swing.JPopupMenu.Separator jSeparatorHelp1;
    private javax.swing.JPopupMenu.Separator jSeparatorOption;
    private javax.swing.JPopupMenu.Separator jSeparatorPopUpMenu0;
    private javax.swing.JPopupMenu.Separator jSeparatorPopUpMenu1;
    private javax.swing.JPopupMenu.Separator jSeparatorProject1;
    private javax.swing.JPopupMenu.Separator jSeparatorProject2;
    private javax.swing.JPopupMenu.Separator jSeparatorProject3;
    private javax.swing.JPopupMenu.Separator jSeparatorSprite;
    private javax.swing.JPopupMenu.Separator jSeparatorSprite1;
    private javax.swing.JPopupMenu.Separator jSeparatorWord;
    private javax.swing.JPopupMenu.Separator jSeparatorWord1;
    private javax.swing.JSplitPane jSplitPaneExternal;
    private javax.swing.JSplitPane jSplitPaneInternal;
    private javax.swing.JMenu jSubMenu;
    private javax.swing.JTable jTableMemory;
    private javax.swing.JToolBar jToolBar;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea rSyntaxTextAreaDis;
    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea rSyntaxTextAreaSource;
    // End of variables declaration//GEN-END:variables

  @Override
  public void execute(int type) {
    switch (type) {
      case PROJ_NEW:
        projectNew();
        break;
      case PROJ_CLOSE:  
        projectClose(); 
        break;  
      case PROJ_OPEN:
        projectOpen();
        break;
      case PROJ_SAVE:
        projectSave(); 
        break;        
      case PROJ_SAVEAS:
        projectSaveAs();
        break;        
      case PROJ_MERGE:
        projectMerge();
        break;       
      case OPTION_CONFIGURE:
        jOptionDialog.setVisible(true);
        break;
      case OPTION_VIEWPRJ:
        projectView();
        break;   
      case OPTION_SIDLD:
        optionSIDLD();  
        break;        
      case OPTION_MPR:
        optionMPR();
        break;
      case SOURCE_DISASS:
        disassembly();
        break;            
      case SOURCE_EXPASDIS:
        exportAs(rSyntaxTextAreaDis.getText());  
        break;        
      case SOURCE_EXPASSOURCE:
        exportAs(rSyntaxTextAreaSource.getText());    
        break;
      case SOURCE_FINDA:
        findAddress();
        break;   
      case SOURCE_FINDD:
        findDialogDis.setVisible(true);
        break;
      case SOURCE_FINDS:
        findDialogSource.setVisible(true);
        break;        
      case APP_EXIT:
        exit();
        break;        
      case MEM_CLEARDCOM:
        clearDasmComment();
        break;
       case MEM_CLEARUCOM:
        clearUserComment();
        break;    
       case MEM_ADDCOMM:
         addComment();
         break;
       case MEM_ADDLABEL:
         addLabel();
         break;      
       case MEM_ADDLABELOP:
         addLabelOp();
         break;                   
       case MEM_MARKCODE:
         markAsCode();  
         break;
       case MEM_MARKDATA:
         markAsData(DataType.NONE);  
         break;
       case MEM_MARKDATA_B:
         markAsData(DataType.BYTE_HEX);      
         break;      
       case MEM_MARKDATA_D:
         markAsData(DataType.BYTE_DEC);      
         break;           
       case MEM_MARKDATA_Y:
         markAsData(DataType.BYTE_BIN);      
         break; 
       case MEM_MARKDATA_R:
         markAsData(DataType.BYTE_CHAR);      
         break;          
       case MEM_MARKDATA_W:  
         markAsData(DataType.WORD);  
         break;  
       case MEM_MARKDATA_P:  
         markAsData(DataType.SWAPPED);  
         break;      
       case MEM_MARKDATA_E:  
         markAsData(DataType.TRIBYTE);   
         break;         
       case MEM_MARKDATA_L:  
         markAsData(DataType.LONG);   
         break;          
       case MEM_MARKDATA_A:  
         markAsData(DataType.ADDRESS);     
         break;         
       case MEM_MARKDATA_S:  
         markAsData(DataType.STACK);      
         break;           
       case MEM_MARKDATA_T: 
         markAsData(DataType.TEXT);  
         break;           
       case MEM_MARKDATA_N:  
         markAsData(DataType.NUM_TEXT);    
         break;           
       case MEM_MARKDATA_Z: 
         markAsData(DataType.ZERO_TEXT);   
         break;   
       case MEM_MARKDATA_M:  
         markAsData(DataType.HIGH_TEXT);   
         break;            
       case MEM_MARKDATA_H:  
         markAsData(DataType.SHIFT_TEXT);   
         break;           
       case MEM_MARKDATA_C:  
         markAsData(DataType.SCREEN_TEXT);  
         break;  
       case MEM_MARKDATA_I:  
         markAsData(DataType.PETASCII_TEXT);  
         break;           
       case MEM_MARKGARB:
         markAsGarbage();  
         break;        
       case MEM_ADDBLOCK:
         addBlock();
         break;
       case MEM_CLEARDLABEL:
         clearDLabel();  
         break;
       case MEM_LOW:
         memLow();  
         break;
       case MEM_HIGH:
         memHigh();  
         break;
       case MEM_PLUS:
         memPlus();  
         break;
       case MEM_MINUS:
         memMinus();  
         break;         
         
       case HELP_CONTENTS: 
         jHelpFrame.setVisible(true);
         break;
       case HELP_LICENSE:
         jLicenseDialog.setVisible(true);  
         break;
       case HELP_CREDITS:
         jCreditsDialog.setVisible(true);
         break;  
       case HELP_ABOUT:
         jAboutDialog.setVisible(true);
         break;
    }
        
  }
  
  /**
   * Project new user action
   */
  private void projectNew() {
    if (project != null && !project.equals(savedProject)) {
      JOptionPane.showMessageDialog(this, "Project not saved. Close it, then create a new one.", "Information", JOptionPane.WARNING_MESSAGE);
    } else {
        project=new Project();
        savedProject=project.clone();
        projectFile=null;
        jProjectDialog.setUp(project);            
        jProjectDialog.setVisible(true);
        
        if (project.file==null || "".equals(project.file)) {
          project=null;
          savedProject=null;          
        } else {
          dataTableModelMemory.setData(project.memory);
          dataTableModelMemory.fireTableDataChanged();
          execute(SOURCE_DISASS);
        }
      }    
  }
  
  /**
   * Project close user action
   */
  private void projectClose() {
    if (project == null) return;
    
    if (!project.equals(savedProject)) {
      int res=JOptionPane.showConfirmDialog(this, "Project not saved. Close it anywere?", "Information", JOptionPane.YES_NO_OPTION);
      if (res!=JOptionPane.YES_OPTION) return;              
    }       
  
    project=null;
    savedProject=null;
    projectFile=null;
    rSyntaxTextAreaSource.setText("");
    rSyntaxTextAreaDis.setText("");
    dataTableModelMemory.setData(null);
    dataTableModelMemory.fireTableDataChanged();
  }
  
  /**
   * Project open user action
   */
  private void projectOpen() {
    if (project != null && !project.equals(savedProject)) {
      JOptionPane.showMessageDialog(this, "Project not saved. Close it, then create a new one.", "Information", JOptionPane.WARNING_MESSAGE);
    } else {
        int retVal=projectChooserFile.showOpenDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
          projectFile=projectChooserFile.getSelectedFile();
          project=new Project();
          if (!FileManager.instance.readProjectFile(projectFile , project)) {
              JOptionPane.showMessageDialog(this, "Error reading project file", "Error", JOptionPane.ERROR_MESSAGE);
          } else {
              JOptionPane.showMessageDialog(this, "File read", "Information", JOptionPane.INFORMATION_MESSAGE);
              execute(SOURCE_DISASS);
            }
            savedProject=project.clone();
            dataTableModelMemory.setData(project.memory);
            dataTableModelMemory.fireTableDataChanged();
          }
       }         
  }
  
  /**
   * Project save user action
   */
  private void projectSave() {
    if (project==null) {
      JOptionPane.showMessageDialog(this, "There is nothing to save", "Information", JOptionPane.INFORMATION_MESSAGE);  
    } else {
        if (projectFile==null) execute (PROJ_SAVEAS);
        else {
          if (!FileManager.instance.writeProjectFile(projectFile , project)) {
            JOptionPane.showMessageDialog(this, "Error writing project file", "Error", JOptionPane.ERROR_MESSAGE);
          } else {
              JOptionPane.showMessageDialog(this, "File saved", "Information", JOptionPane.INFORMATION_MESSAGE);
              savedProject=project.clone();
            }  
        }
      }      
  } 
  
  /**
   * Project save as user action
   */
  private void projectSaveAs() {
    if (project==null) {
      JOptionPane.showMessageDialog(this, "There is nothing to save", "Information", JOptionPane.INFORMATION_MESSAGE);  
    } else {
        int retVal=projectChooserFile.showSaveDialog(this);
        if (retVal == JFileChooser.APPROVE_OPTION) {
          projectFile=projectChooserFile.getSelectedFile();
          if (!FileManager.instance.writeProjectFile(projectFile , project)) {
            JOptionPane.showMessageDialog(this, "Error writing project file", "Error", JOptionPane.ERROR_MESSAGE);
          } else {
              JOptionPane.showMessageDialog(this, "File saved", "Information", JOptionPane.INFORMATION_MESSAGE);
              savedProject=project.clone();
            }
        }
     }      
  }
  
  /**
   * Project view user action
   */
  private void projectView() {
    if (project==null) {
      JOptionPane.showMessageDialog(this, "No project are actually being used.", "Warning", JOptionPane.WARNING_MESSAGE);   
    }  else {
         jProjectDialog.setUp(project);
         jProjectDialog.setVisible(true);
         execute(SOURCE_DISASS);
        }      
  }

  /**
   * Export as of the select text
   * 
   * @param text the text to export
   */
  private void exportAs(String text) {
     File file; 
      
     if (text==null || "".equals(text)) {
          JOptionPane.showMessageDialog(this, "There is nothing to save", "Information", JOptionPane.INFORMATION_MESSAGE);  
        } else {
            int retVal=exportAsChooserFile.showSaveDialog(this);
            if (retVal == JFileChooser.APPROVE_OPTION) {
              file=exportAsChooserFile.getSelectedFile();
              if (!FileManager.instance.writeTxtFile(file , text)) {
                JOptionPane.showMessageDialog(this, "Error writing txt file", "Error", JOptionPane.ERROR_MESSAGE);
              } else {
                  JOptionPane.showMessageDialog(this, "File saved", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            }
          }   
  }
 
  /**
   * Exit from program
   */
  private void exit() {
    if (project != null && !project.equals(savedProject)) {
      int res=JOptionPane.showConfirmDialog(this, "Project not saved. Exit anywere?", "Information", JOptionPane.YES_NO_OPTION);
      if (res==JOptionPane.YES_OPTION) {      
        System.exit(0);
      }
    } else System.exit(0);
  }
  
  /**
   * Clear the dasm label by adding a user null label
   */
  private void clearDasmComment() {
    MemoryDasm mem;   
      
    int rows[]=jTableMemory.getSelectedRows();
        
    for (int i=0; i<rows.length; i++) {
      mem= project.memory[rows[i]];
      if (mem.dasmComment!=null && mem.userComment==null) mem.userComment="";
    }
    
    dataTableModelMemory.fireTableDataChanged();
    jTableMemory.clearSelection();
    for (int i=0; i<rows.length; i++) {
      jTableMemory.addRowSelectionInterval(rows[i], rows[i]);  
    }
  }
  
  /**
   * Clear the dasm label by adding a user null label
   */
  private void clearUserComment() {
    MemoryDasm mem;   
      
    int rows[]=jTableMemory.getSelectedRows();
        
    for (int i=0; i<rows.length; i++) {
      mem= project.memory[rows[i]];
      if (mem.userComment!=null) mem.userComment=null;
    }
    
    dataTableModelMemory.fireTableDataChanged();
    jTableMemory.clearSelection();
    for (int i=0; i<rows.length; i++) {
      jTableMemory.addRowSelectionInterval(rows[i], rows[i]);  
    }
  } 
  
  /**
   * Mark user selection as code
   */
  private void markAsCode() {
    MemoryDasm mem;   
      
    int rows[]=jTableMemory.getSelectedRows();
        
    for (int i=0; i<rows.length; i++) {
      mem= project.memory[rows[i]];
      mem.isCode=true;
      mem.isData=false;
      mem.isGarbage=false;
      mem.dataType=DataType.NONE;
    }
    
    dataTableModelMemory.fireTableDataChanged();  
    jTableMemory.clearSelection();
    for (int i=0; i<rows.length; i++) {
      jTableMemory.addRowSelectionInterval(rows[i], rows[i]);  
    }
  }
  
  /**
   * Mark user selection as data
   * 
   * @param dataType the type of date
   */
  private void markAsData(DataType dataType) {
    MemoryDasm mem;   
      
    int rows[]=jTableMemory.getSelectedRows();
        
    for (int i=0; i<rows.length; i++) {
      mem= project.memory[rows[i]];
      mem.isData=true;
      mem.isCode=false;
      mem.isGarbage=false;
      mem.dataType=dataType;
      if (option.eraseDComm) mem.dasmComment=null;
      if (option.erasePlus && mem.type=='+') {
        mem.related=-1;
        mem.type=' ';
      }
    }
    
    dataTableModelMemory.fireTableDataChanged();  
    jTableMemory.clearSelection();
    for (int i=0; i<rows.length; i++) {
      jTableMemory.addRowSelectionInterval(rows[i], rows[i]);  
    }
  }

  /**
   * Mark user selection as garbage
   */
  private void markAsGarbage() {
    MemoryDasm mem;   
      
    int rows[]=jTableMemory.getSelectedRows();
        
    for (int i=0; i<rows.length; i++) {
      mem= project.memory[rows[i]];
      mem.isData=false;
      mem.isCode=false;
      mem.isGarbage=true;
      mem.dasmLocation=null;
      if (option.eraseDComm) mem.dasmComment=null;
      if (option.erasePlus && mem.type=='+') {
        mem.related=-1;
        mem.type=' ';
      }
    }
    
    dataTableModelMemory.fireTableDataChanged();  
    jTableMemory.clearSelection();
    for (int i=0; i<rows.length; i++) {
      jTableMemory.addRowSelectionInterval(rows[i], rows[i]);  
    }
  }  
  
  /**
   * Apply SIDLD flags to memory
   */
  private void optionSIDLD() {
    if (project==null) {
      JOptionPane.showMessageDialog(this, "No project are actually being used.", "Warning", JOptionPane.WARNING_MESSAGE);   
    }  else {  
         int res=JOptionPane.showConfirmDialog(this, "Confirm to apply SIDLD memory flags to code/data in table?", "Information", JOptionPane.YES_NO_OPTION);
         if (res == JFileChooser.APPROVE_OPTION) {
             
           for (int i=0; i<project.memoryFlags.length; i++) {        
               project.memory[i].isData = (project.memoryFlags[i] & 
                       (memoryState.MEM_READ | memoryState.MEM_READ_FIRST |
                       memoryState.MEM_WRITE | memoryState.MEM_WRITE_FIRST |
                       memoryState.MEM_SAMPLE)) !=0;
      
               project.memory[i].isCode = (project.memoryFlags[i] & 
                       (memoryState.MEM_EXECUTE | memoryState.MEM_EXECUTE_FIRST)) !=0;                                             
           }
           JOptionPane.showMessageDialog(this, "Operation done.", "Info", JOptionPane.INFORMATION_MESSAGE);  
         }                  
       }  
  }
  
  /**
   * MPR create option
   */
  private void optionMPR() {
    JOptionPane.showMessageDialog(this,"A MPR is a group of PRG files saved together\n"+
                                       "Selelect all PRG the files with a multi selection (use CTRL+click) in the next dialog\n"+
                                       "Then you had to choose the output MPR file name in the last dialog", "Create a multi PRG archive", JOptionPane.INFORMATION_MESSAGE);
      
    optionMPRLoadChooserFile.showOpenDialog(this);
    File[] files = optionMPRLoadChooserFile.getSelectedFiles();  
    if (files.length==0) {
      JOptionPane.showMessageDialog(this,"Aborting creation due to not files selected", "Warning", JOptionPane.WARNING_MESSAGE);
      return;
    }
    
    MPR mpr=new MPR();
    if (!mpr.setElements(files)) {
      JOptionPane.showMessageDialog(this,"I/O error in reading the files", "Error", JOptionPane.ERROR_MESSAGE);  
      return;
    }
    
    if (optionMPRSaveChooserFile.showSaveDialog(this)==JFileChooser.APPROVE_OPTION) {
       if (!mpr.saveFile(optionMPRSaveChooserFile.getSelectedFile())) {
         JOptionPane.showMessageDialog(this,"Error saving the file", "Error", JOptionPane.ERROR_MESSAGE);  
         return;
       }
    } else {
        JOptionPane.showMessageDialog(this,"No file selected", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
      }
    
   JOptionPane.showMessageDialog(this, mpr.getDescription(), "Information on saved file", JOptionPane.INFORMATION_MESSAGE);
  }
  
  /**
   * Disassembly the memory
   */
  private void disassembly() {
    if (project==null) {
      disassembly.source="";
      disassembly.disassembly="";
    } else {
        disassembly.dissassembly(project.fileType, project.inB, option, project.memory, project.mpr, project.targetType, false);
        disassembly.dissassembly(project.fileType, project.inB, option, project.memory, project.mpr, project.targetType, true);
      }  
    int lineS=0;
    int lineD=0;
    try {
      lineS=rSyntaxTextAreaSource.getLineOfOffset(rSyntaxTextAreaSource.getCaretPosition());        
      lineD=rSyntaxTextAreaDis.getLineOfOffset(rSyntaxTextAreaDis.getCaretPosition());        
    } catch (BadLocationException ex) {
        System.err.println(ex);
    }
    rSyntaxTextAreaSource.setText(disassembly.source);    
    try {
      rSyntaxTextAreaSource.setCaretPosition(rSyntaxTextAreaSource.getDocument()
                        .getDefaultRootElement().getElement(lineS)
                        .getStartOffset());
      rSyntaxTextAreaSource.requestFocusInWindow();
    } catch (Exception ex) {
        System.err.println(ex);
    }
    
    rSyntaxTextAreaDis.setText(disassembly.disassembly);
    try {
      rSyntaxTextAreaDis.setCaretPosition(rSyntaxTextAreaDis.getDocument()
                        .getDefaultRootElement().getElement(lineD)
                        .getStartOffset());
      rSyntaxTextAreaDis.requestFocusInWindow();
    } catch (Exception ex) {
        System.err.println(ex);
    }       
    
    memoryTableCellRenderer.setDisassembly(disassembly);      
  }

  /**
   * Add a user label to the selected memory address
   */
  private void addComment() {
    int row=jTableMemory.getSelectedRow();
    if (row<0) {
      JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;
    }
      
    MemoryDasm mem= project.memory[row];
    String comment=JOptionPane.showInputDialog(this, "Insert the comment for the selected memory location", mem.userComment);
    
    System.err.println("###"+project.memory[0].userComment+"###"+savedProject.memory[0].userComment);
    
    if (comment!=null) mem.userComment=comment;  
    
      System.err.println("###"+project.memory[0].userComment+"###"+savedProject.memory[0].userComment);
    
    dataTableModelMemory.fireTableDataChanged(); 
    jTableMemory.setRowSelectionInterval(row, row); 
  }

  /**
   * Add a user label to the selected memory address
   */
  private void addLabel() {
    int row=jTableMemory.getSelectedRow();
    if (row<0) {
      JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;
    }
    
    MemoryDasm mem= project.memory[row];
    String initial="";
    if (mem.userLocation!=null) initial=mem.userLocation;
    else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) initial=mem.dasmLocation;
    
    String label=JOptionPane.showInputDialog(this, "Insert the label for the selected memory location", initial);  
    if (label!=null) {
      if ("".equals(label)) {
        JOptionPane.showMessageDialog(this, "User label erased", "Information", JOptionPane.INFORMATION_MESSAGE);   
        mem.userLocation=null;
        return;
      }  
      
      if (label.contains(" ")) {
        JOptionPane.showMessageDialog(this, "Label must not contain spaces", "Error", JOptionPane.ERROR_MESSAGE);   
        return;
      }
      
      if (label.length()>option.maxLabelLength) {
        JOptionPane.showMessageDialog(this, "Label too long. Max allowed="+option.maxLabelLength, "Error", JOptionPane.ERROR_MESSAGE);     
        return;
      }
        
      if (label.length()<6) {
        JOptionPane.showMessageDialog(this, "Label too short. Min allowed=6", "Error", JOptionPane.ERROR_MESSAGE);     
        return;
      }    
            
      // see if the label is already defined
      for (MemoryDasm memory : project.memory) {
        if (label.equals(memory.dasmLocation) || label.equals(memory.userLocation)) {
          JOptionPane.showMessageDialog(this, "This label is already used into the source", "Error", JOptionPane.ERROR_MESSAGE);  
          return;
       }
      }
      
      mem.userLocation=label;
      dataTableModelMemory.fireTableDataChanged(); 
      jTableMemory.setRowSelectionInterval(row, row); 
    }
  }
  
  /**
   * Add a user label to the next word address of selected memory address
   */
  private void addLabelOp() {
    int row=jTableMemory.getSelectedRow();
    if (row<0) {
      JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;
    }
    
    // avoid to read over the end
    if (row>=0xFFFE) {
      JOptionPane.showMessageDialog(this, "Address locate over $FFFF boundary", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;        
    }
    
    // avoid to use not defined bytes
    if (!project.memory[row+1].isInside ||!project.memory[row+2].isInside) {
      JOptionPane.showMessageDialog(this, "Address is incomplete. Skip action", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;         
    }
    
    // get next address
    MemoryDasm mem= project.memory[(project.memory[row+2].copy & 0xFF)*256+(project.memory[row+1].copy & 0xFF)];
    
    String initial="";
    if (mem.userLocation!=null) initial=mem.userLocation;
    else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) initial=mem.dasmLocation;
    
    String label=JOptionPane.showInputDialog(this, "Insert the label for the address of operation", initial);  
    if (label!=null) {
      if ("".equals(label)) {
        JOptionPane.showMessageDialog(this, "User label erased", "Information", JOptionPane.INFORMATION_MESSAGE);   
        mem.userLocation=null;
        return;
      }  
      
      if (label.contains(" ")) {
        JOptionPane.showMessageDialog(this, "Label must not contain spaces", "Error", JOptionPane.ERROR_MESSAGE);   
        return;
      }
      
      if (label.length()>option.maxLabelLength) {
        JOptionPane.showMessageDialog(this, "Label too long. Max allowed="+option.maxLabelLength, "Error", JOptionPane.ERROR_MESSAGE);     
        return;
      }
        
      if (label.length()<6) {
        JOptionPane.showMessageDialog(this, "Label too short. Min allowed=6", "Error", JOptionPane.ERROR_MESSAGE);     
        return;
      }    
            
      // see if the label is already defined
      for (MemoryDasm memory : project.memory) {
        if (label.equals(memory.dasmLocation) || label.equals(memory.userLocation)) {
          JOptionPane.showMessageDialog(this, "This label is already used into the source", "Error", JOptionPane.ERROR_MESSAGE);  
          return;
       }
      }
      
      mem.userLocation=label;
      dataTableModelMemory.fireTableDataChanged(); 
      jTableMemory.setRowSelectionInterval(row, row); 
    }
  }

  /**
   * Add a block for comment
   */
  private void addBlock() {
    int row=jTableMemory.getSelectedRow();
    if (row<0) {
      JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;
    }
      
    MemoryDasm mem= project.memory[row];
    JTextArea area=new JTextArea(20,20);
    area.setText(mem.userBlockComment);
    area.setFont(new Font("monospaced", Font.PLAIN, 12));

    JScrollPane scrollPane = new JScrollPane(area);
    
    if (JOptionPane.showConfirmDialog(null, scrollPane, "Add a multi-lines block comment", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
      mem.userBlockComment=area.getText();
      if ("".equals(mem.userBlockComment)) mem.userBlockComment=null;
      dataTableModelMemory.fireTableDataChanged();  
      jTableMemory.setRowSelectionInterval(row, row); 
    }              
  }

  /**
   * Clear dasm label (can be regenerated next time)
   */
  private void clearDLabel() {
    MemoryDasm mem;   
      
    int rows[]=jTableMemory.getSelectedRows();
        
    for (int i=0; i<rows.length; i++) {
      mem= project.memory[rows[i]];
      if (mem.dasmLocation!=null) mem.dasmLocation=null;
    }
    
    dataTableModelMemory.fireTableDataChanged();    
    jTableMemory.clearSelection();
    for (int i=0; i<rows.length; i++) {
      jTableMemory.addRowSelectionInterval(rows[i], rows[i]);  
    }
  }  

  /**
   * Assign a reference to memory as #<
   */
  private void memLow() {
    int row=jTableMemory.getSelectedRow();
    if (row<0) {
      JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;
    }
    
    Vector cols=new Vector();
    cols.add("ADDR");
    cols.add("DASM");
    cols.add("USER");
        
    Vector rows=new Vector();
    Vector data;
    
    int value=project.memory[row].copy & 0xFF;
    
    for (MemoryDasm memory : project.memory) {    
      if ((memory.address & 0xFF)==value) {
        if (!memory.isInside && memory.userLocation==null) continue;
          
        data=new Vector();
          
        data.add(ShortToExe(memory.address));
        data.add(memory.dasmLocation);
        data.add(memory.userLocation);
          
        rows.add(data);
      }
    }

    JTable table = new JTable(rows, cols);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    if (JOptionPane.showConfirmDialog(null, new JScrollPane(table), 
            "Select the address to use as #<", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
        
      int rowS=table.getSelectedRow();
      if (rowS<0) {
        if (project.memory[row].type=='>' || project.memory[row].type=='<') {
          if (JOptionPane.showConfirmDialog(this, "Did you want to delete the current address association?", "No selection were done, so:", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            project.memory[row].type=' ';
            project.memory[row].related=-1;
          }
        } else JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
        return;
      } else {         
          project.memory[row].related=Integer.parseInt((String)table.getValueAt(rowS, 0),16);          
          project.memory[row].type='<';
        }
       
      dataTableModelMemory.fireTableDataChanged();      
      jTableMemory.setRowSelectionInterval(row, row); 
    }       
  }

  /**
   * Assign a reference to memory as #>
   */
  private void memHigh() {
    int row=jTableMemory.getSelectedRow();
    if (row<0) {
      JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;
    }
    
    Vector cols=new Vector();
    cols.add("ADDR");
    cols.add("DASM");
    cols.add("USER");
        
    Vector rows=new Vector();
    Vector data;
    
    int value=project.memory[row].copy & 0xFF;
    
    for (MemoryDasm memory : project.memory) {      
      if (((memory.address>>8) & 0xFF)==value) {
        if (!memory.isInside && memory.userLocation==null) continue;  
          
        data=new Vector();
          
        data.add(ShortToExe(memory.address));
        data.add(memory.dasmLocation);
        data.add(memory.userLocation);
          
        rows.add(data);
      }
    }
    
    JTable table = new JTable(rows, cols);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    if (JOptionPane.showConfirmDialog(null, new JScrollPane(table), 
           "Select the address to use as #>", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
        
       int rowS=table.getSelectedRow();
       if (rowS<0) {
         if (project.memory[row].type=='>' || project.memory[row].type=='<') {
            if (JOptionPane.showConfirmDialog(this, "Did you want to delete the current address association?", "No selection were done, so:", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
              project.memory[row].type=' ';
              project.memory[row].related=-1;
            }
         } else JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
         return;
       } else {         
           project.memory[row].related=Integer.parseInt((String)table.getValueAt(rowS, 0),16);          
           project.memory[row].type='>';
         }
       
       dataTableModelMemory.fireTableDataChanged();
       jTableMemory.setRowSelectionInterval(row, row);
    }   
  }
 
  /**
   * Find a memory address
   */
  private void findAddress() {
    String addr=JOptionPane.showInputDialog(this, "Search and go to a given HEX memory address");
    if (addr==null) return;
    
    try {
      int pos=Integer.parseInt(addr,16);
      if (pos<0 || pos>0xFFFF) return;
    
      jTableMemory.getSelectionModel().setSelectionInterval(pos, pos);
      jTableMemory.scrollRectToVisible(new Rectangle(jTableMemory.getCellRect(pos, 0, true)));
    } catch (Exception e) {
      }
  }

  /**
   * Mark the memroy as address +
   */
  private void memPlus() {
    int row=jTableMemory.getSelectedRow();
    if (row<0) {
      JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;
    }
    
    Vector cols=new Vector();
    cols.add("ADDR");
    cols.add("DASM");
    cols.add("USER");
        
    Vector rows=new Vector();
    Vector data;
    
    int value=project.memory[row].copy & 0xFF;
    
    MemoryDasm mem=project.memory[row];
    MemoryDasm memory;
    
    int addr;
    for (int i=1; i<32; i++) {
      addr=mem.address-i;
      if (i<0) continue;
      
      memory=project.memory[addr];
      
      data=new Vector();
          
      data.add(ShortToExe(memory.address));
      data.add(memory.dasmLocation);
      data.add(memory.userLocation);
          
      rows.add(data);
    }
    
    JTable table = new JTable(rows, cols);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
    
    if (JOptionPane.showConfirmDialog(null, new JScrollPane(table), 
           "Select the address to use as + in table", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
        
      int rowS=table.getSelectedRow();
      if (rowS<0) {
        if (project.memory[row].type=='+') {
          if (JOptionPane.showConfirmDialog(this, "Did you want to delete the current address association?", "No selection were done, so:", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            project.memory[row].type=' ';
            project.memory[row].related=-1;
          }
        } else JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
        return;
      } else {         
           mem.related=Integer.parseInt((String)table.getValueAt(rowS, 0),16);          
           mem.type='+';
        }
       
      dataTableModelMemory.fireTableDataChanged();
      jTableMemory.setRowSelectionInterval(row, row);
    }
  }
  
  /**
   * Collaborative merge of another user contribution
   */
  private void projectMerge() {
    if (project == null) {
      JOptionPane.showMessageDialog(this, "You must be inside a project to merge it with another!", "Information", JOptionPane.WARNING_MESSAGE);
      return;
    } 
 
    int retVal=projectMergeFile.showOpenDialog(this);
    if (retVal == JFileChooser.APPROVE_OPTION) {
       File mergeFile=projectMergeFile.getSelectedFile();
       Project mergeProject=new Project();
       if (!FileManager.instance.readProjectFile(mergeFile , mergeProject)) {
         JOptionPane.showMessageDialog(this, "Error reading project file", "Error", JOptionPane.ERROR_MESSAGE);
         return;  
       } 
                   
       // test that the projects are of the same
       if ((Arrays.hashCode(mergeProject.inB) != Arrays.hashCode(project.inB)) ||
           (mergeProject.mpr.hashCode() != project.hashCode())) {
         JOptionPane.showMessageDialog(this, "Hashcode of the projects are different: they are not of the same source", "Error", JOptionPane.ERROR_MESSAGE);
         return;            
       }
       
       
       // take name/description from secondary only if not present in primary
       if (project.name==null || "".equals(project.name)) project.name=mergeProject.name;       
       if (project.description==null || "".equals(project.description)) project.description=mergeProject.description;
       
       
       MemoryDasm memProject;
       MemoryDasm memMerge;
       
       // scan all memory locations
       for (int i=0; i<project.memory.length; i++) {
          memProject=project.memory[i];
          memMerge=mergeProject.memory[i];
          
          // apply secondary information if primary are not defined
          if (memProject.userBlockComment==null || "".equals(memProject.userBlockComment)) memProject.userBlockComment=memMerge.userBlockComment;
          if (memProject.userComment==null || "".equals(memProject.userComment)) memProject.userComment=memMerge.userComment;
          if (memProject.userLocation==null || "".equals(memProject.userLocation)) memProject.userLocation=memMerge.userLocation;
          
          if (memProject.isInside) {
            if (!memProject.isCode && !memProject.isData && !memProject.isGarbage) {
              memProject.isCode=memMerge.isCode;
              memProject.isData=memMerge.isData;
              memProject.isGarbage=memMerge.isGarbage;
            }  
            
            if (memProject.related!=-1) {
              memProject.related=memMerge.related;
              memProject.type=memMerge.type;
            }
            
            if (memProject.dataType!=DataType.NONE) memProject.dataType=memMerge.dataType;
          }
       }
          
       dataTableModelMemory.fireTableDataChanged();
    }
           
  }
  
/**
   * Mark the memroy as address -
   */
  private void memMinus() {
    int row=jTableMemory.getSelectedRow();
    if (row<0) {
      JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
      return;
    }
    
    Vector cols=new Vector();
    cols.add("ADDR");
    cols.add("DASM");
    cols.add("USER");
        
    Vector rows=new Vector();
    Vector data;
    
    int value=project.memory[row].copy & 0xFF;
    
    MemoryDasm mem=project.memory[row];
    MemoryDasm memory;
    
    int addr;
    for (int i=1; i<32; i++) {
      addr=mem.address+i;
      if (i<0) continue;
      
      memory=project.memory[addr];
      
      data=new Vector();
          
      data.add(ShortToExe(memory.address));
      data.add(memory.dasmLocation);
      data.add(memory.userLocation);
          
      rows.add(data);
    }
    
    JTable table = new JTable(rows, cols);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
    
    if (JOptionPane.showConfirmDialog(null, new JScrollPane(table), 
           "Select the address to use as - in table", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION) {
        
      int rowS=table.getSelectedRow();
      if (rowS<0) {
        if (project.memory[row].type=='-') {
          if (JOptionPane.showConfirmDialog(this, "Did you want to delete the current address association?", "No selection were done, so:", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            project.memory[row].type=' ';
            project.memory[row].related=-1;
          }
        } else JOptionPane.showMessageDialog(this, "No row selected", "Warning", JOptionPane.WARNING_MESSAGE);  
        return;
      } else {         
           mem.related=Integer.parseInt((String)table.getValueAt(rowS, 0),16);          
           mem.type='-';
        }
       
      dataTableModelMemory.fireTableDataChanged();
      jTableMemory.setRowSelectionInterval(row, row);
    }
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
}
