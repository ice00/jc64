/**
 * @(#)JASPanel 2024/04/19
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

import sw_emulator.software.Assembler;
import sw_emulator.swing.main.Option;

/**
 * A panel for AS assembler option 
 * 
 * @author ice
 */
public class JASPanel extends javax.swing.JPanel {
    /** Option file to use */
    Option option;
    
    /**
     * Creates new form JASPanel
     */
    public JASPanel() {
        initComponents();
    }
    
    /**
     * Set up the panel with the option 
     * 
     * @param option the option to use
     */
    public void setUp(Option option) {
      this.option=option;
    }      
    
     
  /**
   * Apply the option for AS
   */
  public void applyOptionAS() {
    // actually ASI e ASM points to the same visual control, so use only 1 of it
    
    switch (option.asiStarting) {
      case CPU_I:
        jRadioButtonASStarting.setSelected(true);   
        break;
    }
    
    switch (option.asiOrigin) {
      case ORG_H:
        jRadioButtonASOrigin.setSelected(true);  
        break;
    }      
    
    switch (option.asiLabel) {
      case NAME:
        jRadioButtonASLabelName.setSelected(true);
        break;
      case NAME_COLON:
        jRadioButtonASLabelNameColon.setSelected(true);  
        break;  
    }
    
    switch (option.asiComment) {
      case SEMICOLON:
        jRadioButtonASSemicolonComment.setSelected(true);
        break;               
    }    
    
    switch (option.asiByte) {
      case DB_BYTE:  
        jRadioButtonASDbByte.setSelected(true);
        break;      
    }
    
    switch (option.asiWord) {
      case DOT_WORD:  
        jRadioButtonASDwWord.setSelected(true);
        break;          
    } 
    
    switch (option.asiWordSwapped) {
      case MACRO6_WORD_SWAPPED:  
        jRadioButtonASMacroWordSwapped.setSelected(true);
        break;          
    }
    
    switch (option.asiTribyte) {
      case MACRO6_TRIBYTE:  
        jRadioButtonASMacroTribyte.setSelected(true);
        break;          
    }
    
    switch (option.asiLong) {
      case DD_LONG_H:  
        jRadioButtonASDdLong.setSelected(true);
        break;          
    }
    
    switch (option.asiAddress) {
      case DW_ADDR_H:  
        jRadioButtonASDwAddress.setSelected(true);
        break;          
    }
    
    switch (option.asiStackWord) {
      case MACRO5_STACKWORD:  
        jRadioButtonASMacroStackWord.setSelected(true);
        break;          
    }
    
    switch (option.dasmMonoSprite) {
      case BYTE_HEX:
        jRadioButtonASByteHexMonoSprite.setSelected(true);
        break;
      case BYTE_BIN:
        jRadioButtonASByteBinMonoSprite.setSelected(true);
        break;
      case MACRO6_HEX:
        jRadioButtonASMacroHexMonoSprite.setSelected(true);
        break;  
      case MACRO6_BIN:
        jRadioButtonASMacroBinMonoSprite.setSelected(true);
        break;  
    }
    
    switch (option.dasmMultiSprite) {
      case BYTE_HEX:
        jRadioButtonASByteHexMultiSprite.setSelected(true);
        break;
      case BYTE_BIN:
        jRadioButtonASByteBinMultiSprite.setSelected(true);
        break;
      case MACRO6_HEX:
        jRadioButtonASMacroHexMultiSprite.setSelected(true);
        break;  
      case MACRO6_BIN:
        jRadioButtonASMacroBinMultiSprite.setSelected(true);
        break;  
    }    
    
    switch (option.asiText) {
      case DB_BYTE_TEXT:  
        jRadioButtonASDbText.setSelected(true);
        break;          
    } 
    
    switch (option.asiNumText) {
      case DB_BYTE_NUMTEXT:  
        jRadioButtonASDbNumText.setSelected(true);
        break;          
    } 
    
    switch (option.asiZeroText) {
      case DB_BYTE_ZEROTEXT:  
        jRadioButtonASDbZeroText.setSelected(true);
        break;          
    }
    
    switch (option.asiHighText) {
      case DB_BYTE_HIGHTEXT:  
        jRadioButtonASDbHighText.setSelected(true);
        break;          
    }
    
    switch (option.asiShiftText) {
      case DB_BYTE_SHIFTTEXT:  
        jRadioButtonASDbShiftText.setSelected(true);
        break;          
    }
    
    switch (option.asiScreenText) {
      case DB_BYTE_SCREENTEXT:  
        jRadioButtonASDbScreenText.setSelected(true);
        break;          
    }  
    
    switch (option.asiPetasciiText) {
      case DB_BYTE_PETASCIITEXT:  
        jRadioButtonASDbPetasciiText.setSelected(true);
        break;          
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

    jLabelASLabelDeclaration = new javax.swing.JLabel();
    jRadioButtonASLabelName = new javax.swing.JRadioButton();
    jScrollPaneASByte = new javax.swing.JScrollPane();
    jTextPaneASLabelName = new javax.swing.JTextPane();
    jLabelASByteDeclaration = new javax.swing.JLabel();
    jRadioButtonASDbByte = new javax.swing.JRadioButton();
    jScrollPaneASWord = new javax.swing.JScrollPane();
    jTextPaneASDbByte = new javax.swing.JTextPane();
    jLabelASWordDeclaration = new javax.swing.JLabel();
    jRadioButtonASDwWord = new javax.swing.JRadioButton();
    jScrollPaneASDotWord = new javax.swing.JScrollPane();
    jTextPaneASDwWord = new javax.swing.JTextPane();
    jLabelASCommentDeclaration = new javax.swing.JLabel();
    jRadioButtonASSemicolonComment = new javax.swing.JRadioButton();
    jScrollPaneASSemicolonComment = new javax.swing.JScrollPane();
    jTextPaneASSemicolonComment = new javax.swing.JTextPane();
    jLabelASBlockCommentDeclaration = new javax.swing.JLabel();
    jRadioButtonASSemicolonBlockComment = new javax.swing.JRadioButton();
    jScrollPaneASSemicolonBlockComment1 = new javax.swing.JScrollPane();
    jTextPaneASSemicolonBlockComment1 = new javax.swing.JTextPane();
    jLabelASOriginDeclaration = new javax.swing.JLabel();
    jRadioButtonASOrigin = new javax.swing.JRadioButton();
    jScrollPaneASAsterixOrigin1 = new javax.swing.JScrollPane();
    jTextPaneASOrigin = new javax.swing.JTextPane();
    jLabelASStartingDeclaration = new javax.swing.JLabel();
    jRadioButtonASStarting = new javax.swing.JRadioButton();
    jScrollPaneASStarting = new javax.swing.JScrollPane();
    jTextPaneASStarting = new javax.swing.JTextPane();
    jLabelASMonoSpriteDeclaration = new javax.swing.JLabel();
    jLabelASMultiSpriteDeclaration = new javax.swing.JLabel();
    jRadioButtonASByteHexMonoSprite = new javax.swing.JRadioButton();
    jRadioButtonASByteHexMultiSprite = new javax.swing.JRadioButton();
    jScrollPaneASByteHexMultiSprite = new javax.swing.JScrollPane();
    TmpxPaneASByteHexMultiSprite = new javax.swing.JTextPane();
    jScrollPaneASByteHexMonoSprite = new javax.swing.JScrollPane();
    jTextPaneASByteHexMonoSprite = new javax.swing.JTextPane();
    jRadioButtonASByteBinMonoSprite = new javax.swing.JRadioButton();
    jRadioButtonASByteBinMultiSprite = new javax.swing.JRadioButton();
    jScrollPaneASByteBinMultiSprite = new javax.swing.JScrollPane();
    jTextPaneASByteBinMultiSprite = new javax.swing.JTextPane();
    jScrollPaneASByteBinMonoSprite = new javax.swing.JScrollPane();
    jTextPaneASByteBinMonoSprite = new javax.swing.JTextPane();
    jRadioButtonASMacroHexMonoSprite = new javax.swing.JRadioButton();
    jRadioButtonASMacroHexMultiSprite = new javax.swing.JRadioButton();
    jScrollPaneASMacroHexMultiSprite = new javax.swing.JScrollPane();
    jTextPaneASMacroHexMultiSprite = new javax.swing.JTextPane();
    jScrollPaneASMacroHexMonoSprite = new javax.swing.JScrollPane();
    jTextPaneASMacroHexMonoSprite = new javax.swing.JTextPane();
    jRadioButtonASMacroBinMonoSprite = new javax.swing.JRadioButton();
    jRadioButtonASMacroBinMultiSprite = new javax.swing.JRadioButton();
    jScrollPaneASMacroBinMultiSprite = new javax.swing.JScrollPane();
    jTextPaneASMacroBinMultiSprite = new javax.swing.JTextPane();
    jScrollPaneASMacroBinMonoSprite = new javax.swing.JScrollPane();
    jTextPaneASMacroBinMonoSprite = new javax.swing.JTextPane();
    jLabelASTribyteDeclaration = new javax.swing.JLabel();
    jRadioButtonASMacroTribyte = new javax.swing.JRadioButton();
    jScrollPaneASMacroTribyte = new javax.swing.JScrollPane();
    jTextPaneASMacroTribyte = new javax.swing.JTextPane();
    jLabelASLongDeclaration = new javax.swing.JLabel();
    jRadioButtonASDdLong = new javax.swing.JRadioButton();
    jScrollPaneASMacroLong = new javax.swing.JScrollPane();
    jTextPaneASMacroLong = new javax.swing.JTextPane();
    jLabelASWordSwappedDeclaration = new javax.swing.JLabel();
    jRadioButtonASMacroWordSwapped = new javax.swing.JRadioButton();
    jScrollPaneASMacroWordSwapped = new javax.swing.JScrollPane();
    jTextPaneASMacroWordSwapped = new javax.swing.JTextPane();
    jLabelASTextDeclaration = new javax.swing.JLabel();
    jRadioButtonASDbText = new javax.swing.JRadioButton();
    jScrollPaneASDotText = new javax.swing.JScrollPane();
    jTextPaneASDbText = new javax.swing.JTextPane();
    jLabelASNumTextDeclaration = new javax.swing.JLabel();
    jRadioButtonASDbNumText = new javax.swing.JRadioButton();
    jScrollPaneASDotTextNumText = new javax.swing.JScrollPane();
    jTextPaneASDbNumText = new javax.swing.JTextPane();
    jLabelASZeroTextDeclaration = new javax.swing.JLabel();
    jRadioButtonASDbZeroText = new javax.swing.JRadioButton();
    jScrollPaneASDotNullZeroText = new javax.swing.JScrollPane();
    jTextPaneASDbZeroText = new javax.swing.JTextPane();
    jLabelASAddressDeclaration = new javax.swing.JLabel();
    jRadioButtonASDwAddress = new javax.swing.JRadioButton();
    jScrollPaneASDotAddrAddress = new javax.swing.JScrollPane();
    jTextPaneASDwAddress = new javax.swing.JTextPane();
    jLabelTmpxASStackWordDeclaration = new javax.swing.JLabel();
    jRadioButtonASMacroStackWord = new javax.swing.JRadioButton();
    jScrollPaneASDotRtaStackWord = new javax.swing.JScrollPane();
    jTextPaneASMacroStackWord = new javax.swing.JTextPane();
    jLabelASHighTextDeclaration = new javax.swing.JLabel();
    jRadioButtonASDbHighText = new javax.swing.JRadioButton();
    jScrollPaneASDotShiftHighText = new javax.swing.JScrollPane();
    jTextPaneASDbHighText = new javax.swing.JTextPane();
    jLabelASShiftTextDeclaration = new javax.swing.JLabel();
    jRadioButtonASDbShiftText = new javax.swing.JRadioButton();
    jScrollPaneASDotShiftlShiftText = new javax.swing.JScrollPane();
    jTextPaneASDbShiftText = new javax.swing.JTextPane();
    jLabelASScreenTextDeclaration = new javax.swing.JLabel();
    jRadioButtonASDbScreenText = new javax.swing.JRadioButton();
    jScrollPaneASDotScreenText = new javax.swing.JScrollPane();
    jTextPaneDbScreenText = new javax.swing.JTextPane();
    jLabelASPetasciiTextDeclaration = new javax.swing.JLabel();
    jRadioButtonASDbPetasciiText = new javax.swing.JRadioButton();
    jScrollPaneASDotPetasciiText = new javax.swing.JScrollPane();
    jTextPaneASDbPetasciiText = new javax.swing.JTextPane();
    jRadioButtonASLabelNameColon = new javax.swing.JRadioButton();
    jScrollPaneASLabelNameColon = new javax.swing.JScrollPane();
    jTextPaneASLabelNameColon = new javax.swing.JTextPane();

    jLabelASLabelDeclaration.setText("Label:");

    jRadioButtonASLabelName.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASLabelNameItemStateChanged(evt);
      }
    });

    jScrollPaneASByte.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASByte.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASLabelName.setEditable(false);
    jTextPaneASLabelName.setContentType("text/html"); // NOI18N
    jTextPaneASLabelName.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n     <font color='black'>zzzz</font>\n    </p\n  </body>\n</html>\n");
    jScrollPaneASByte.setViewportView(jTextPaneASLabelName);

    jLabelASByteDeclaration.setText("Byte:");

    jRadioButtonASDbByte.setSelected(true);
    jRadioButtonASDbByte.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDbByteItemStateChanged(evt);
      }
    });

    jScrollPaneASWord.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASWord.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASDbByte.setEditable(false);
    jTextPaneASDbByte.setContentType("text/html"); // NOI18N
    jTextPaneASDbByte.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> db</b> <font color='red'>$xx</font> | <b> db</b> <font color='red'>xxh</font><br>\n     <b> db</b> <font color='blue'>dd</font> | <b> db</b> <font color='blue'>dd</font><br>\n     <b> db</b> <font color='green'>%nn</font> | <b> db</b> <font color='green'>nnb</font><br>\n     <b> db</b> <font color='purple'>'c'</font>  |  <b> db</b> <font color='purple'>'c'</font>\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASWord.setViewportView(jTextPaneASDbByte);

    jLabelASWordDeclaration.setText("Word:");

    jRadioButtonASDwWord.setSelected(true);
    jRadioButtonASDwWord.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDwWordItemStateChanged(evt);
      }
    });

    jScrollPaneASDotWord.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotWord.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASDwWord.setEditable(false);
    jTextPaneASDwWord.setContentType("text/html"); // NOI18N
    jTextPaneASDwWord.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> dw</b> <font color='red'>$xxyy</font> | <b> dw</b> <font color='red'>xxyyh</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASDotWord.setViewportView(jTextPaneASDwWord);

    jLabelASCommentDeclaration.setText("Comment:");

    jRadioButtonASSemicolonComment.setSelected(true);
    jRadioButtonASSemicolonComment.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASSemicolonCommentItemStateChanged(evt);
      }
    });

    jScrollPaneASSemicolonComment.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASSemicolonComment.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASSemicolonComment.setEditable(false);
    jTextPaneASSemicolonComment.setContentType("text/html"); // NOI18N
    jTextPaneASSemicolonComment.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n     <b>;</b> xxx\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASSemicolonComment.setViewportView(jTextPaneASSemicolonComment);

    jLabelASBlockCommentDeclaration.setText("Block Comment:");

    jRadioButtonASSemicolonBlockComment.setSelected(true);
    jRadioButtonASSemicolonBlockComment.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASSemicolonBlockCommentItemStateChanged(evt);
      }
    });

    jScrollPaneASSemicolonBlockComment1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASSemicolonBlockComment1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASSemicolonBlockComment1.setEditable(false);
    jTextPaneASSemicolonBlockComment1.setContentType("text/html"); // NOI18N
    jTextPaneASSemicolonBlockComment1.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n     <b>;</b> xxx\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASSemicolonBlockComment1.setViewportView(jTextPaneASSemicolonBlockComment1);

    jLabelASOriginDeclaration.setText("Origin:");

    jRadioButtonASOrigin.setSelected(true);
    jRadioButtonASOrigin.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASOriginItemStateChanged(evt);
      }
    });

    jScrollPaneASAsterixOrigin1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASAsterixOrigin1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASOrigin.setEditable(false);
    jTextPaneASOrigin.setContentType("text/html"); // NOI18N
    jTextPaneASOrigin.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n     <b>org</b> $xxyy | <b>org</b> xxyyh\n    </p>\n  </body>\n</html>\n\n");
    jScrollPaneASAsterixOrigin1.setViewportView(jTextPaneASOrigin);

    jLabelASStartingDeclaration.setText("Starting:");

    jRadioButtonASStarting.setSelected(true);
    jRadioButtonASStarting.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASStartingItemStateChanged(evt);
      }
    });

    jScrollPaneASStarting.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASStarting.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASStarting.setEditable(false);
    jTextPaneASStarting.setContentType("text/html"); // NOI18N
    jTextPaneASStarting.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n     <b>cpu</b> 6502 | <b> cpu</b> 8048\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASStarting.setViewportView(jTextPaneASStarting);

    jLabelASMonoSpriteDeclaration.setText("Monocolor sprite:");

    jLabelASMultiSpriteDeclaration.setText("Multicolor sprite:");

    jRadioButtonASByteHexMonoSprite.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASByteHexMonoSpriteItemStateChanged(evt);
      }
    });

    jRadioButtonASByteHexMultiSprite.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASByteHexMultiSpriteItemStateChanged(evt);
      }
    });

    jScrollPaneASByteHexMultiSprite.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASByteHexMultiSprite.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    TmpxPaneASByteHexMultiSprite.setEditable(false);
    TmpxPaneASByteHexMultiSprite.setContentType("text/html"); // NOI18N
    TmpxPaneASByteHexMultiSprite.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> [byte]</b> <font color='red'>$xx..</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASByteHexMultiSprite.setViewportView(TmpxPaneASByteHexMultiSprite);

    jScrollPaneASByteHexMonoSprite.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASByteHexMonoSprite.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASByteHexMonoSprite.setEditable(false);
    jTextPaneASByteHexMonoSprite.setContentType("text/html"); // NOI18N
    jTextPaneASByteHexMonoSprite.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> [byte]</b> <font color='red'>$xx..</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASByteHexMonoSprite.setViewportView(jTextPaneASByteHexMonoSprite);

    jRadioButtonASByteBinMonoSprite.setSelected(true);
    jRadioButtonASByteBinMonoSprite.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASByteBinMonoSpriteItemStateChanged(evt);
      }
    });

    jRadioButtonASByteBinMultiSprite.setSelected(true);
    jRadioButtonASByteBinMultiSprite.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASByteBinMultiSpriteItemStateChanged(evt);
      }
    });

    jScrollPaneASByteBinMultiSprite.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASByteBinMultiSprite.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASByteBinMultiSprite.setEditable(false);
    jTextPaneASByteBinMultiSprite.setContentType("text/html"); // NOI18N
    jTextPaneASByteBinMultiSprite.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>[byte]</b> <font color='red'>%b..</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASByteBinMultiSprite.setViewportView(jTextPaneASByteBinMultiSprite);

    jScrollPaneASByteBinMonoSprite.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASByteBinMonoSprite.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASByteBinMonoSprite.setEditable(false);
    jTextPaneASByteBinMonoSprite.setContentType("text/html"); // NOI18N
    jTextPaneASByteBinMonoSprite.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>[byte]</b> <font color='red'>%b..</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASByteBinMonoSprite.setViewportView(jTextPaneASByteBinMonoSprite);

    jRadioButtonASMacroHexMonoSprite.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASMacroHexMonoSpriteItemStateChanged(evt);
      }
    });

    jRadioButtonASMacroHexMultiSprite.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASMacroHexMultiSpriteItemStateChanged(evt);
      }
    });

    jScrollPaneASMacroHexMultiSprite.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASMacroHexMultiSprite.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASMacroHexMultiSprite.setEditable(false);
    jTextPaneASMacroHexMultiSprite.setContentType("text/html"); // NOI18N
    jTextPaneASMacroHexMultiSprite.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>[.mac] </b> <font color='red'>$xx..</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASMacroHexMultiSprite.setViewportView(jTextPaneASMacroHexMultiSprite);

    jScrollPaneASMacroHexMonoSprite.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASMacroHexMonoSprite.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASMacroHexMonoSprite.setEditable(false);
    jTextPaneASMacroHexMonoSprite.setContentType("text/html"); // NOI18N
    jTextPaneASMacroHexMonoSprite.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>[.mac] </b> <font color='red'>$xx..</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASMacroHexMonoSprite.setViewportView(jTextPaneASMacroHexMonoSprite);

    jRadioButtonASMacroBinMonoSprite.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASMacroBinMonoSpriteItemStateChanged(evt);
      }
    });

    jRadioButtonASMacroBinMultiSprite.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASMacroBinMultiSpriteItemStateChanged(evt);
      }
    });

    jScrollPaneASMacroBinMultiSprite.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASMacroBinMultiSprite.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASMacroBinMultiSprite.setEditable(false);
    jTextPaneASMacroBinMultiSprite.setContentType("text/html"); // NOI18N
    jTextPaneASMacroBinMultiSprite.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>[.mac] </b> <font color='red'>%b..</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASMacroBinMultiSprite.setViewportView(jTextPaneASMacroBinMultiSprite);

    jScrollPaneASMacroBinMonoSprite.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASMacroBinMonoSprite.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASMacroBinMonoSprite.setEditable(false);
    jTextPaneASMacroBinMonoSprite.setContentType("text/html"); // NOI18N
    jTextPaneASMacroBinMonoSprite.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>[.mac] </b> <font color='red'>%b..</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASMacroBinMonoSprite.setViewportView(jTextPaneASMacroBinMonoSprite);

    jLabelASTribyteDeclaration.setText("Tribyte:");

    jRadioButtonASMacroTribyte.setSelected(true);
    jRadioButtonASMacroTribyte.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASMacroTribyteItemStateChanged(evt);
      }
    });

    jScrollPaneASMacroTribyte.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASMacroTribyte.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASMacroTribyte.setEditable(false);
    jTextPaneASMacroTribyte.setContentType("text/html"); // NOI18N
    jTextPaneASMacroTribyte.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>[.mac]</b> <font color='red'>$xxyyzz</font> |<b> [.mac]</b> <font color='red'>xxyyzzh</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASMacroTribyte.setViewportView(jTextPaneASMacroTribyte);

    jLabelASLongDeclaration.setText("Long:");

    jRadioButtonASDdLong.setSelected(true);
    jRadioButtonASDdLong.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDdLongItemStateChanged(evt);
      }
    });

    jScrollPaneASMacroLong.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASMacroLong.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASMacroLong.setEditable(false);
    jTextPaneASMacroLong.setContentType("text/html"); // NOI18N
    jTextPaneASMacroLong.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>dd</b> <font color='red'>$xx..kk</font> | <b>dd</b> <font color='red'>xx..kkh</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASMacroLong.setViewportView(jTextPaneASMacroLong);

    jLabelASWordSwappedDeclaration.setText("Word Swapped:");

    jRadioButtonASMacroWordSwapped.setSelected(true);
    jRadioButtonASMacroWordSwapped.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASMacroWordSwappedItemStateChanged(evt);
      }
    });

    jScrollPaneASMacroWordSwapped.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASMacroWordSwapped.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASMacroWordSwapped.setEditable(false);
    jTextPaneASMacroWordSwapped.setContentType("text/html"); // NOI18N
    jTextPaneASMacroWordSwapped.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>[.mac]</b> <font color='red'>$yyxx</font> | <b>[.mac]</b> <font color='red'>yyxxh</font><br>\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASMacroWordSwapped.setViewportView(jTextPaneASMacroWordSwapped);

    jLabelASTextDeclaration.setText("Text:");

    jRadioButtonASDbText.setSelected(true);
    jRadioButtonASDbText.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDbTextItemStateChanged(evt);
      }
    });

    jScrollPaneASDotText.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotText.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASDbText.setEditable(false);
    jTextPaneASDbText.setContentType("text/html"); // NOI18N
    jTextPaneASDbText.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> db</b> <font color='red'>\"xxx\"</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASDotText.setViewportView(jTextPaneASDbText);

    jLabelASNumTextDeclaration.setText("Text #num chars:");

    jRadioButtonASDbNumText.setSelected(true);
    jRadioButtonASDbNumText.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDbNumTextItemStateChanged(evt);
      }
    });

    jScrollPaneASDotTextNumText.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotTextNumText.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASDbNumText.setEditable(false);
    jTextPaneASDbNumText.setContentType("text/html"); // NOI18N
    jTextPaneASDbNumText.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> db</b> <font color='red'>\"xxx\"</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jTextPaneASDbNumText.setPreferredSize(new java.awt.Dimension(66, 20));
    jScrollPaneASDotTextNumText.setViewportView(jTextPaneASDbNumText);

    jLabelASZeroTextDeclaration.setText("Text 0 terminated:");

    jRadioButtonASDbZeroText.setSelected(true);
    jRadioButtonASDbZeroText.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDbZeroTextItemStateChanged(evt);
      }
    });

    jScrollPaneASDotNullZeroText.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotNullZeroText.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASDbZeroText.setEditable(false);
    jTextPaneASDbZeroText.setContentType("text/html"); // NOI18N
    jTextPaneASDbZeroText.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> db</b> <font color='red'>\"xxx\"</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASDotNullZeroText.setViewportView(jTextPaneASDbZeroText);

    jLabelASAddressDeclaration.setText("Address:");

    jRadioButtonASDwAddress.setSelected(true);
    jRadioButtonASDwAddress.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDwAddressItemStateChanged(evt);
      }
    });

    jScrollPaneASDotAddrAddress.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotAddrAddress.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASDwAddress.setEditable(false);
    jTextPaneASDwAddress.setContentType("text/html"); // NOI18N
    jTextPaneASDwAddress.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> dw</b> <font color='red'>$xxyy</font>| <b> dw</b> <font color='red'>xxyyh</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASDotAddrAddress.setViewportView(jTextPaneASDwAddress);

    jLabelTmpxASStackWordDeclaration.setText("Stack Word:");

    jRadioButtonASMacroStackWord.setSelected(true);
    jRadioButtonASMacroStackWord.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASMacroStackWordItemStateChanged(evt);
      }
    });

    jScrollPaneASDotRtaStackWord.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotRtaStackWord.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASMacroStackWord.setEditable(false);
    jTextPaneASMacroStackWord.setContentType("text/html"); // NOI18N
    jTextPaneASMacroStackWord.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b>[.mac]</b> <font color='red'>$xxyy</font> | <b>[.mac]</b> <font color='red'>xxyyh</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASDotRtaStackWord.setViewportView(jTextPaneASMacroStackWord);

    jLabelASHighTextDeclaration.setText("Text '1' terminated:");

    jRadioButtonASDbHighText.setSelected(true);
    jRadioButtonASDbHighText.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDbHighTextItemStateChanged(evt);
      }
    });

    jScrollPaneASDotShiftHighText.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotShiftHighText.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASDbHighText.setEditable(false);
    jTextPaneASDbHighText.setContentType("text/html"); // NOI18N
    jTextPaneASDbHighText.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> db</b> <font color='red'>\"xxx\"</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jTextPaneASDbHighText.setPreferredSize(new java.awt.Dimension(66, 20));
    jScrollPaneASDotShiftHighText.setViewportView(jTextPaneASDbHighText);

    jLabelASShiftTextDeclaration.setText("Text left shifted:");

    jRadioButtonASDbShiftText.setSelected(true);
    jRadioButtonASDbShiftText.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDbShiftTextItemStateChanged(evt);
      }
    });

    jScrollPaneASDotShiftlShiftText.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotShiftlShiftText.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASDbShiftText.setEditable(false);
    jTextPaneASDbShiftText.setContentType("text/html"); // NOI18N
    jTextPaneASDbShiftText.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> db</b> <font color='red'>\"xxx\"</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jTextPaneASDbShiftText.setPreferredSize(new java.awt.Dimension(66, 20));
    jScrollPaneASDotShiftlShiftText.setViewportView(jTextPaneASDbShiftText);

    jLabelASScreenTextDeclaration.setText("Text to screen code:");

    jRadioButtonASDbScreenText.setSelected(true);
    jRadioButtonASDbScreenText.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDbScreenTextItemStateChanged(evt);
      }
    });

    jScrollPaneASDotScreenText.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotScreenText.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneDbScreenText.setEditable(false);
    jTextPaneDbScreenText.setContentType("text/html"); // NOI18N
    jTextPaneDbScreenText.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> db</b> <font color='red'>\"xxx\"</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASDotScreenText.setViewportView(jTextPaneDbScreenText);

    jLabelASPetasciiTextDeclaration.setText("Text to petascii code:");

    jRadioButtonASDbPetasciiText.setSelected(true);
    jRadioButtonASDbPetasciiText.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASDbPetasciiTextItemStateChanged(evt);
      }
    });

    jScrollPaneASDotPetasciiText.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASDotPetasciiText.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASDbPetasciiText.setEditable(false);
    jTextPaneASDbPetasciiText.setContentType("text/html"); // NOI18N
    jTextPaneASDbPetasciiText.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      <b> db</b> <font color='red'>\"xxx\"</font><br>\n\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASDotPetasciiText.setViewportView(jTextPaneASDbPetasciiText);

    jRadioButtonASLabelNameColon.setSelected(true);
    jRadioButtonASLabelNameColon.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jRadioButtonASLabelNameColonItemStateChanged(evt);
      }
    });

    jScrollPaneASLabelNameColon.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPaneASLabelNameColon.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

    jTextPaneASLabelNameColon.setEditable(false);
    jTextPaneASLabelNameColon.setContentType("text/html"); // NOI18N
    jTextPaneASLabelNameColon.setText("<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n     <font color='black'>zzzz</font><b>:</b>\n    </p>\n  </body>\n</html>\n");
    jScrollPaneASLabelNameColon.setViewportView(jTextPaneASLabelNameColon);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(12, 12, 12)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabelASPetasciiTextDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASScreenTextDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASShiftTextDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASHighTextDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASZeroTextDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASNumTextDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASTextDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASMultiSpriteDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASMonoSpriteDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelTmpxASStackWordDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASAddressDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASLongDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASTribyteDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASWordSwappedDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASWordDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASByteDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASLabelDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASBlockCommentDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASCommentDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASOriginDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelASStartingDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jRadioButtonASStarting)
          .addComponent(jRadioButtonASOrigin)
          .addComponent(jRadioButtonASSemicolonComment)
          .addComponent(jRadioButtonASSemicolonBlockComment)
          .addComponent(jRadioButtonASLabelName)
          .addComponent(jRadioButtonASDbByte)
          .addComponent(jRadioButtonASDwWord)
          .addComponent(jRadioButtonASMacroWordSwapped)
          .addComponent(jRadioButtonASMacroTribyte)
          .addComponent(jRadioButtonASDdLong)
          .addComponent(jRadioButtonASDwAddress)
          .addComponent(jRadioButtonASMacroStackWord)
          .addComponent(jRadioButtonASByteHexMonoSprite)
          .addComponent(jRadioButtonASByteHexMultiSprite)
          .addComponent(jRadioButtonASDbText)
          .addComponent(jRadioButtonASDbNumText)
          .addComponent(jRadioButtonASDbZeroText)
          .addComponent(jRadioButtonASDbHighText)
          .addComponent(jRadioButtonASDbShiftText)
          .addComponent(jRadioButtonASDbScreenText)
          .addComponent(jRadioButtonASDbPetasciiText))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPaneASStarting, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASAsterixOrigin1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASSemicolonComment, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASSemicolonBlockComment1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASDotText, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASDotTextNumText, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASDotNullZeroText, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASDotShiftHighText, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASDotShiftlShiftText, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASDotScreenText, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jScrollPaneASDotPetasciiText, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jScrollPaneASByte, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASWord, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASDotWord, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASMacroWordSwapped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASMacroTribyte, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASMacroLong, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASDotAddrAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASDotRtaStackWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASByteHexMonoSprite, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASByteHexMultiSprite, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jRadioButtonASLabelNameColon)
              .addComponent(jRadioButtonASByteBinMonoSprite)
              .addComponent(jRadioButtonASByteBinMultiSprite))
            .addGap(6, 6, 6)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jScrollPaneASByteBinMultiSprite, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
              .addComponent(jScrollPaneASByteBinMonoSprite, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
              .addComponent(jScrollPaneASLabelNameColon))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jRadioButtonASMacroHexMonoSprite)
              .addComponent(jRadioButtonASMacroHexMultiSprite))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jScrollPaneASMacroHexMonoSprite, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASMacroHexMultiSprite, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jRadioButtonASMacroBinMultiSprite)
              .addComponent(jRadioButtonASMacroBinMonoSprite))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jScrollPaneASMacroBinMultiSprite, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jScrollPaneASMacroBinMonoSprite, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addContainerGap(42, Short.MAX_VALUE))
    );

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabelASAddressDeclaration, jLabelASBlockCommentDeclaration, jLabelASByteDeclaration, jLabelASCommentDeclaration, jLabelASHighTextDeclaration, jLabelASLabelDeclaration, jLabelASLongDeclaration, jLabelASMonoSpriteDeclaration, jLabelASMultiSpriteDeclaration, jLabelASNumTextDeclaration, jLabelASOriginDeclaration, jLabelASPetasciiTextDeclaration, jLabelASScreenTextDeclaration, jLabelASShiftTextDeclaration, jLabelASStartingDeclaration, jLabelASTextDeclaration, jLabelASTribyteDeclaration, jLabelASWordDeclaration, jLabelASWordSwappedDeclaration, jLabelASZeroTextDeclaration, jLabelTmpxASStackWordDeclaration});

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPaneASAsterixOrigin1, jScrollPaneASByte, jScrollPaneASByteHexMonoSprite, jScrollPaneASByteHexMultiSprite, jScrollPaneASDotAddrAddress, jScrollPaneASDotNullZeroText, jScrollPaneASDotPetasciiText, jScrollPaneASDotRtaStackWord, jScrollPaneASDotScreenText, jScrollPaneASDotShiftHighText, jScrollPaneASDotShiftlShiftText, jScrollPaneASDotText, jScrollPaneASDotTextNumText, jScrollPaneASDotWord, jScrollPaneASMacroLong, jScrollPaneASMacroTribyte, jScrollPaneASMacroWordSwapped, jScrollPaneASSemicolonBlockComment1, jScrollPaneASSemicolonComment, jScrollPaneASStarting, jScrollPaneASWord});

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPaneASByteBinMonoSprite, jScrollPaneASByteBinMultiSprite, jScrollPaneASLabelNameColon});

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPaneASMacroHexMonoSprite, jScrollPaneASMacroHexMultiSprite});

    layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPaneASMacroBinMonoSprite, jScrollPaneASMacroBinMultiSprite});

    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(11, 11, 11)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASStartingDeclaration)
          .addComponent(jRadioButtonASStarting)
          .addComponent(jScrollPaneASStarting, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(11, 11, 11)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASOriginDeclaration)
          .addComponent(jRadioButtonASOrigin)
          .addComponent(jScrollPaneASAsterixOrigin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASCommentDeclaration)
          .addComponent(jRadioButtonASSemicolonComment)
          .addComponent(jScrollPaneASSemicolonComment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(12, 12, 12)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASBlockCommentDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jRadioButtonASSemicolonBlockComment)
          .addComponent(jScrollPaneASSemicolonBlockComment1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabelASLabelDeclaration)
              .addComponent(jRadioButtonASLabelName)
              .addComponent(jScrollPaneASByte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabelASByteDeclaration)
              .addComponent(jRadioButtonASDbByte)
              .addComponent(jScrollPaneASWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabelASWordDeclaration)
              .addComponent(jRadioButtonASDwWord)
              .addComponent(jScrollPaneASDotWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabelASWordSwappedDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jRadioButtonASMacroWordSwapped)
              .addComponent(jScrollPaneASMacroWordSwapped, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabelASTribyteDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jRadioButtonASMacroTribyte)
              .addComponent(jScrollPaneASMacroTribyte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(4, 4, 4)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabelASLongDeclaration, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jRadioButtonASDdLong)
              .addComponent(jScrollPaneASMacroLong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabelASAddressDeclaration)
              .addComponent(jRadioButtonASDwAddress)
              .addComponent(jScrollPaneASDotAddrAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
              .addComponent(jLabelTmpxASStackWordDeclaration)
              .addComponent(jRadioButtonASMacroStackWord)
              .addComponent(jScrollPaneASDotRtaStackWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
            .addComponent(jRadioButtonASLabelNameColon)
            .addComponent(jScrollPaneASLabelNameColon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASMonoSpriteDeclaration)
          .addComponent(jRadioButtonASByteHexMonoSprite)
          .addComponent(jScrollPaneASByteHexMonoSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jRadioButtonASByteBinMonoSprite)
          .addComponent(jScrollPaneASByteBinMonoSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jRadioButtonASMacroHexMonoSprite)
          .addComponent(jScrollPaneASMacroHexMonoSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jRadioButtonASMacroBinMonoSprite)
          .addComponent(jScrollPaneASMacroBinMonoSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASMultiSpriteDeclaration)
          .addComponent(jRadioButtonASByteHexMultiSprite)
          .addComponent(jScrollPaneASByteHexMultiSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jRadioButtonASByteBinMultiSprite)
          .addComponent(jScrollPaneASByteBinMultiSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jRadioButtonASMacroHexMultiSprite)
          .addComponent(jScrollPaneASMacroHexMultiSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jRadioButtonASMacroBinMultiSprite)
          .addComponent(jScrollPaneASMacroBinMultiSprite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASTextDeclaration)
          .addComponent(jRadioButtonASDbText)
          .addComponent(jScrollPaneASDotText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASNumTextDeclaration)
          .addComponent(jRadioButtonASDbNumText)
          .addComponent(jScrollPaneASDotTextNumText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASZeroTextDeclaration)
          .addComponent(jRadioButtonASDbZeroText)
          .addComponent(jScrollPaneASDotNullZeroText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASHighTextDeclaration)
          .addComponent(jRadioButtonASDbHighText)
          .addComponent(jScrollPaneASDotShiftHighText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASShiftTextDeclaration)
          .addComponent(jRadioButtonASDbShiftText)
          .addComponent(jScrollPaneASDotShiftlShiftText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASScreenTextDeclaration)
          .addComponent(jRadioButtonASDbScreenText)
          .addComponent(jScrollPaneASDotScreenText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
          .addComponent(jLabelASPetasciiTextDeclaration)
          .addComponent(jRadioButtonASDbPetasciiText)
          .addComponent(jScrollPaneASDotPetasciiText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(39, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonASLabelNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASLabelNameItemStateChanged
        option.asmLabel=Assembler.Label.NAME;
        option.asiLabel=Assembler.Label.NAME;
    }//GEN-LAST:event_jRadioButtonASLabelNameItemStateChanged

    private void jRadioButtonASDbByteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDbByteItemStateChanged
        option.asmByte=Assembler.Byte.DB_BYTE;
        option.asiByte=Assembler.Byte.DB_BYTE_H;
    }//GEN-LAST:event_jRadioButtonASDbByteItemStateChanged

    private void jRadioButtonASDwWordItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDwWordItemStateChanged
        option.asmWord=Assembler.Word.DW_WORD;
        option.asiWord=Assembler.Word.DW_WORD_H;        
    }//GEN-LAST:event_jRadioButtonASDwWordItemStateChanged

    private void jRadioButtonASSemicolonCommentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASSemicolonCommentItemStateChanged
        option.asmComment=Assembler.Comment.SEMICOLON;
        option.asiComment=Assembler.Comment.SEMICOLON;
    }//GEN-LAST:event_jRadioButtonASSemicolonCommentItemStateChanged

    private void jRadioButtonASSemicolonBlockCommentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASSemicolonBlockCommentItemStateChanged
        option.asmBlockComment=Assembler.BlockComment.SEMICOLON;
        option.asiBlockComment=Assembler.BlockComment.SEMICOLON;
    }//GEN-LAST:event_jRadioButtonASSemicolonBlockCommentItemStateChanged

    private void jRadioButtonASOriginItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASOriginItemStateChanged
        option.asmOrigin=Assembler.Origin.ORG;
        option.asiOrigin=Assembler.Origin.ORG_H;
    }//GEN-LAST:event_jRadioButtonASOriginItemStateChanged

    private void jRadioButtonASStartingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASStartingItemStateChanged
        option.asmStarting=Assembler.Starting.CPU_M;
        option.asiStarting=Assembler.Starting.CPU_I;
    }//GEN-LAST:event_jRadioButtonASStartingItemStateChanged

    private void jRadioButtonASByteHexMonoSpriteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASByteHexMonoSpriteItemStateChanged
        option.asmMonoSprite=Assembler.MonoSprite.BYTE_HEX;
        option.asiMonoSprite=Assembler.MonoSprite.BYTE_HEX;
    }//GEN-LAST:event_jRadioButtonASByteHexMonoSpriteItemStateChanged

    private void jRadioButtonASByteHexMultiSpriteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASByteHexMultiSpriteItemStateChanged
        option.asmMultiSprite=Assembler.MultiSprite.BYTE_HEX;
        option.asiMultiSprite=Assembler.MultiSprite.BYTE_HEX;
    }//GEN-LAST:event_jRadioButtonASByteHexMultiSpriteItemStateChanged

    private void jRadioButtonASByteBinMonoSpriteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASByteBinMonoSpriteItemStateChanged
        option.asmMonoSprite=Assembler.MonoSprite.BYTE_BIN;
        option.asiMonoSprite=Assembler.MonoSprite.BYTE_BIN;  
    }//GEN-LAST:event_jRadioButtonASByteBinMonoSpriteItemStateChanged

    private void jRadioButtonASByteBinMultiSpriteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASByteBinMultiSpriteItemStateChanged
        option.asmMultiSprite=Assembler.MultiSprite.BYTE_BIN;
        option.asiMultiSprite=Assembler.MultiSprite.BYTE_BIN;
    }//GEN-LAST:event_jRadioButtonASByteBinMultiSpriteItemStateChanged

    private void jRadioButtonASMacroHexMonoSpriteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASMacroHexMonoSpriteItemStateChanged
        option.asmMonoSprite=Assembler.MonoSprite.MACRO6_HEX;
        option.asiMonoSprite=Assembler.MonoSprite.MACRO6_HEX;
    }//GEN-LAST:event_jRadioButtonASMacroHexMonoSpriteItemStateChanged

    private void jRadioButtonASMacroHexMultiSpriteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASMacroHexMultiSpriteItemStateChanged
        option.asmMultiSprite=Assembler.MultiSprite.MACRO6_HEX;
        option.asiMultiSprite=Assembler.MultiSprite.MACRO6_HEX;
    }//GEN-LAST:event_jRadioButtonASMacroHexMultiSpriteItemStateChanged

    private void jRadioButtonASMacroBinMonoSpriteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASMacroBinMonoSpriteItemStateChanged
        option.asmMonoSprite=Assembler.MonoSprite.MACRO6_BIN;
        option.asiMonoSprite=Assembler.MonoSprite.MACRO6_BIN;
    }//GEN-LAST:event_jRadioButtonASMacroBinMonoSpriteItemStateChanged

    private void jRadioButtonASMacroBinMultiSpriteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASMacroBinMultiSpriteItemStateChanged
        option.asmMultiSprite=Assembler.MultiSprite.MACRO6_BIN;
        option.asiMultiSprite=Assembler.MultiSprite.MACRO6_BIN;
    }//GEN-LAST:event_jRadioButtonASMacroBinMultiSpriteItemStateChanged

    private void jRadioButtonASMacroTribyteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASMacroTribyteItemStateChanged
        option.asmTribyte=Assembler.Tribyte.MACRO6_TRIBYTE;
        option.asiTribyte=Assembler.Tribyte.MACRO6_TRIBYTE;
    }//GEN-LAST:event_jRadioButtonASMacroTribyteItemStateChanged

    private void jRadioButtonASDdLongItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDdLongItemStateChanged
        option.asmLong=Assembler.Long.DD_LONG;
        option.asiLong=Assembler.Long.DD_LONG_H;
    }//GEN-LAST:event_jRadioButtonASDdLongItemStateChanged

    private void jRadioButtonASMacroWordSwappedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASMacroWordSwappedItemStateChanged
        option.asmWordSwapped=Assembler.WordSwapped.MACRO6_WORD_SWAPPED;
        option.asiWordSwapped=Assembler.WordSwapped.MACRO6_WORD_SWAPPED;
    }//GEN-LAST:event_jRadioButtonASMacroWordSwappedItemStateChanged

    private void jRadioButtonASDbTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDbTextItemStateChanged
        option.asmText=Assembler.Text.DB_BYTE_TEXT;
        option.asiText=Assembler.Text.DB_BYTE_TEXT;
    }//GEN-LAST:event_jRadioButtonASDbTextItemStateChanged

    private void jRadioButtonASDbNumTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDbNumTextItemStateChanged
        option.asmNumText=Assembler.NumText.DB_BYTE_NUMTEXT;
        option.asiNumText=Assembler.NumText.DB_BYTE_NUMTEXT;
    }//GEN-LAST:event_jRadioButtonASDbNumTextItemStateChanged

    private void jRadioButtonASDbZeroTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDbZeroTextItemStateChanged
        option.asmZeroText=Assembler.ZeroText.DB_BYTE_ZEROTEXT;
        option.asiZeroText=Assembler.ZeroText.DB_BYTE_ZEROTEXT;
    }//GEN-LAST:event_jRadioButtonASDbZeroTextItemStateChanged

    private void jRadioButtonASDwAddressItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDwAddressItemStateChanged
        option.asmAddress=Assembler.Address.DW_ADDR;
        option.asiAddress=Assembler.Address.DW_ADDR_H;
    }//GEN-LAST:event_jRadioButtonASDwAddressItemStateChanged

    private void jRadioButtonASMacroStackWordItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASMacroStackWordItemStateChanged
        option.asmStackWord=Assembler.StackWord.MACRO5_STACKWORD;
        option.asiStackWord=Assembler.StackWord.MACRO5_STACKWORD;
    }//GEN-LAST:event_jRadioButtonASMacroStackWordItemStateChanged

    private void jRadioButtonASDbHighTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDbHighTextItemStateChanged
        option.asmHighText=Assembler.HighText.DB_BYTE_HIGHTEXT;
        option.asiHighText=Assembler.HighText.DB_BYTE_HIGHTEXT;
    }//GEN-LAST:event_jRadioButtonASDbHighTextItemStateChanged

    private void jRadioButtonASDbShiftTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDbShiftTextItemStateChanged
        option.asmShiftText=Assembler.ShiftText.DB_BYTE_SHIFTTEXT;
        option.asiShiftText=Assembler.ShiftText.DB_BYTE_SHIFTTEXT;
    }//GEN-LAST:event_jRadioButtonASDbShiftTextItemStateChanged

    private void jRadioButtonASDbScreenTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDbScreenTextItemStateChanged
        option.asmScreenText=Assembler.ScreenText.DB_BYTE_SCREENTEXT;
        option.asiScreenText=Assembler.ScreenText.DB_BYTE_SCREENTEXT;
    }//GEN-LAST:event_jRadioButtonASDbScreenTextItemStateChanged

    private void jRadioButtonASDbPetasciiTextItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASDbPetasciiTextItemStateChanged
        option.asmPetasciiText=Assembler.PetasciiText.DB_BYTE_PETASCIITEXT;
        option.asiPetasciiText=Assembler.PetasciiText.DB_BYTE_PETASCIITEXT;
    }//GEN-LAST:event_jRadioButtonASDbPetasciiTextItemStateChanged

    private void jRadioButtonASLabelNameColonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButtonASLabelNameColonItemStateChanged
        option.asmLabel=Assembler.Label.NAME_COLON;
        option.asiLabel=Assembler.Label.NAME_COLON;
    }//GEN-LAST:event_jRadioButtonASLabelNameColonItemStateChanged


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextPane TmpxPaneASByteHexMultiSprite;
  private javax.swing.JLabel jLabelASAddressDeclaration;
  private javax.swing.JLabel jLabelASBlockCommentDeclaration;
  private javax.swing.JLabel jLabelASByteDeclaration;
  private javax.swing.JLabel jLabelASCommentDeclaration;
  private javax.swing.JLabel jLabelASHighTextDeclaration;
  private javax.swing.JLabel jLabelASLabelDeclaration;
  private javax.swing.JLabel jLabelASLongDeclaration;
  private javax.swing.JLabel jLabelASMonoSpriteDeclaration;
  private javax.swing.JLabel jLabelASMultiSpriteDeclaration;
  private javax.swing.JLabel jLabelASNumTextDeclaration;
  private javax.swing.JLabel jLabelASOriginDeclaration;
  private javax.swing.JLabel jLabelASPetasciiTextDeclaration;
  private javax.swing.JLabel jLabelASScreenTextDeclaration;
  private javax.swing.JLabel jLabelASShiftTextDeclaration;
  private javax.swing.JLabel jLabelASStartingDeclaration;
  private javax.swing.JLabel jLabelASTextDeclaration;
  private javax.swing.JLabel jLabelASTribyteDeclaration;
  private javax.swing.JLabel jLabelASWordDeclaration;
  private javax.swing.JLabel jLabelASWordSwappedDeclaration;
  private javax.swing.JLabel jLabelASZeroTextDeclaration;
  private javax.swing.JLabel jLabelTmpxASStackWordDeclaration;
  private javax.swing.JRadioButton jRadioButtonASByteBinMonoSprite;
  private javax.swing.JRadioButton jRadioButtonASByteBinMultiSprite;
  private javax.swing.JRadioButton jRadioButtonASByteHexMonoSprite;
  private javax.swing.JRadioButton jRadioButtonASByteHexMultiSprite;
  private javax.swing.JRadioButton jRadioButtonASDbByte;
  private javax.swing.JRadioButton jRadioButtonASDbHighText;
  private javax.swing.JRadioButton jRadioButtonASDbNumText;
  private javax.swing.JRadioButton jRadioButtonASDbPetasciiText;
  private javax.swing.JRadioButton jRadioButtonASDbScreenText;
  private javax.swing.JRadioButton jRadioButtonASDbShiftText;
  private javax.swing.JRadioButton jRadioButtonASDbText;
  private javax.swing.JRadioButton jRadioButtonASDbZeroText;
  private javax.swing.JRadioButton jRadioButtonASDdLong;
  private javax.swing.JRadioButton jRadioButtonASDwAddress;
  private javax.swing.JRadioButton jRadioButtonASDwWord;
  private javax.swing.JRadioButton jRadioButtonASLabelName;
  private javax.swing.JRadioButton jRadioButtonASLabelNameColon;
  private javax.swing.JRadioButton jRadioButtonASMacroBinMonoSprite;
  private javax.swing.JRadioButton jRadioButtonASMacroBinMultiSprite;
  private javax.swing.JRadioButton jRadioButtonASMacroHexMonoSprite;
  private javax.swing.JRadioButton jRadioButtonASMacroHexMultiSprite;
  private javax.swing.JRadioButton jRadioButtonASMacroStackWord;
  private javax.swing.JRadioButton jRadioButtonASMacroTribyte;
  private javax.swing.JRadioButton jRadioButtonASMacroWordSwapped;
  private javax.swing.JRadioButton jRadioButtonASOrigin;
  private javax.swing.JRadioButton jRadioButtonASSemicolonBlockComment;
  private javax.swing.JRadioButton jRadioButtonASSemicolonComment;
  private javax.swing.JRadioButton jRadioButtonASStarting;
  private javax.swing.JScrollPane jScrollPaneASAsterixOrigin1;
  private javax.swing.JScrollPane jScrollPaneASByte;
  private javax.swing.JScrollPane jScrollPaneASByteBinMonoSprite;
  private javax.swing.JScrollPane jScrollPaneASByteBinMultiSprite;
  private javax.swing.JScrollPane jScrollPaneASByteHexMonoSprite;
  private javax.swing.JScrollPane jScrollPaneASByteHexMultiSprite;
  private javax.swing.JScrollPane jScrollPaneASDotAddrAddress;
  private javax.swing.JScrollPane jScrollPaneASDotNullZeroText;
  private javax.swing.JScrollPane jScrollPaneASDotPetasciiText;
  private javax.swing.JScrollPane jScrollPaneASDotRtaStackWord;
  private javax.swing.JScrollPane jScrollPaneASDotScreenText;
  private javax.swing.JScrollPane jScrollPaneASDotShiftHighText;
  private javax.swing.JScrollPane jScrollPaneASDotShiftlShiftText;
  private javax.swing.JScrollPane jScrollPaneASDotText;
  private javax.swing.JScrollPane jScrollPaneASDotTextNumText;
  private javax.swing.JScrollPane jScrollPaneASDotWord;
  private javax.swing.JScrollPane jScrollPaneASLabelNameColon;
  private javax.swing.JScrollPane jScrollPaneASMacroBinMonoSprite;
  private javax.swing.JScrollPane jScrollPaneASMacroBinMultiSprite;
  private javax.swing.JScrollPane jScrollPaneASMacroHexMonoSprite;
  private javax.swing.JScrollPane jScrollPaneASMacroHexMultiSprite;
  private javax.swing.JScrollPane jScrollPaneASMacroLong;
  private javax.swing.JScrollPane jScrollPaneASMacroTribyte;
  private javax.swing.JScrollPane jScrollPaneASMacroWordSwapped;
  private javax.swing.JScrollPane jScrollPaneASSemicolonBlockComment1;
  private javax.swing.JScrollPane jScrollPaneASSemicolonComment;
  private javax.swing.JScrollPane jScrollPaneASStarting;
  private javax.swing.JScrollPane jScrollPaneASWord;
  private javax.swing.JTextPane jTextPaneASByteBinMonoSprite;
  private javax.swing.JTextPane jTextPaneASByteBinMultiSprite;
  private javax.swing.JTextPane jTextPaneASByteHexMonoSprite;
  private javax.swing.JTextPane jTextPaneASDbByte;
  private javax.swing.JTextPane jTextPaneASDbHighText;
  private javax.swing.JTextPane jTextPaneASDbNumText;
  private javax.swing.JTextPane jTextPaneASDbPetasciiText;
  private javax.swing.JTextPane jTextPaneASDbShiftText;
  private javax.swing.JTextPane jTextPaneASDbText;
  private javax.swing.JTextPane jTextPaneASDbZeroText;
  private javax.swing.JTextPane jTextPaneASDwAddress;
  private javax.swing.JTextPane jTextPaneASDwWord;
  private javax.swing.JTextPane jTextPaneASLabelName;
  private javax.swing.JTextPane jTextPaneASLabelNameColon;
  private javax.swing.JTextPane jTextPaneASMacroBinMonoSprite;
  private javax.swing.JTextPane jTextPaneASMacroBinMultiSprite;
  private javax.swing.JTextPane jTextPaneASMacroHexMonoSprite;
  private javax.swing.JTextPane jTextPaneASMacroHexMultiSprite;
  private javax.swing.JTextPane jTextPaneASMacroLong;
  private javax.swing.JTextPane jTextPaneASMacroStackWord;
  private javax.swing.JTextPane jTextPaneASMacroTribyte;
  private javax.swing.JTextPane jTextPaneASMacroWordSwapped;
  private javax.swing.JTextPane jTextPaneASOrigin;
  private javax.swing.JTextPane jTextPaneASSemicolonBlockComment1;
  private javax.swing.JTextPane jTextPaneASSemicolonComment;
  private javax.swing.JTextPane jTextPaneASStarting;
  private javax.swing.JTextPane jTextPaneDbScreenText;
  // End of variables declaration//GEN-END:variables
}
