/**
 * @(#)DataTypejava 2020/10/18
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
package sw_emulator.software;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Detokenize Basic program into a readable string
 * 
 * @author stefano_tognon
 */
public class BasicDetokenize {     
  /** List with data to detokenize */
  ArrayList<Integer> list=new ArrayList();
  
  /**
   * Type of Basic to detokenize
   */
  public enum BasicType {
    NONE("None", false, false, false),
    
    BASIC_V2_0("Basic v2.0 (C64/VIC20/PET)", false, false, false) { 
      @Override
      protected void init() {
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }
    },
    
    BASIC_V3_5("Basic v3.5 (C16)",  false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_V3_5) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }   
    },
    
    BASIC_V4_0("Basic v4.0 (PET/CBM2)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_V4_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }    
    },
    
    BASIC_V7_0("Basic v7.0 (C128)", false, true,  true) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_V7_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_SIMON("Basic v2.0 with Simons' Basic (C64)", true,  false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_SIMON) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_ANDRE_FACHAT("Basic v2.0 with @Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_ANDRE_FACHAT) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_SPEECH("Basic v2.0 with Speech Basic v2.7 (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_SPEECH) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_FINAL_CART3("Basic v2.0 with Final Cartridge III (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_FINAL_CART3) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    }, 
    
    BASIC_ULTRABASIC("Basic v2.0 with Ultrabasic-64 (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_ULTRABASIC) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_GRAPHICS("Basic v2.0 with Graphics Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_GRAPHICS) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_WS("Basic v2.0 with WS Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_WS) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_PEGASUS("Basic v2.0 with Pegasus Basic v4.0 (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_PEGASUS) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_XBASIC("Basic v2.0 with Xbasic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_XBASIC) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_DRAGO("Basic v2.0 with Drago Basic v2.2 (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_DRAGO) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_REU("Basic v2.0 with REU-Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_REU) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_LIGHTNING("Basic v2.0 with Basic Lightning (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_LIGHTNING) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_MAGIC("Basic v2.0 with Magic Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_MAGIC) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_BLARG("Basic v2.0 with Blarg (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_BLARG) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_WS_FINAL("Basic v2.0 with WS Basic final (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_WS_FINAL) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_GAME("Basic v2.0 with Game Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_GAME) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_BASEX("Basic v2.0 with Basex (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_BASEX) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_SUPER("Basic v2.0 with Super Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_SUPER) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_EXPANDED("Basic v2.0 with Expanded Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_EXPANDED) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_SUPER_EXPANDER_CHIP("Basic v2.0 with Super Expander Chip (C64)", false, false, true) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_SUPER_EXPANDER_CHIP) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_WARSAW("Basic v2.0 with Warsaw Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_WARSAW) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_DBS("Basic v2.0 with Supergrafik 64 (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_DBS) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_KIPPER("Basic v2.0 with Kipper Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_KIPPER) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_BAILS("Basic v2.0 with Basic on Bails (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_BAILS) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_EVE("Basic v2.0 with Eve Basic (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_EVE) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_TOOL("Basic v2.0 with The Tool 64 (C64)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_TOOL) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_SUPER_EXPANDER("Basic v2.0 with Super Expander (VIC20)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_SUPER_EXPANDER) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_TURTLE("Basic v2.0 with Turtle Basic v1.0 (VIC20)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_TURTLE) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_EASY("Basic v2.0 with Easy Basic (VIC20)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_EASY) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_V4("Basic v2.0 with Basic v4.0 extension (VIC20)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_V4) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_V5("Basic v2.0 with Basic v4.5 extension (VIC20)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_V5) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_EXPANDED_V20("Basic v2.0 with Expanded Basic (VIC20)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_EXPANDED_V20) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_HANDY("Basic v2.0 with Handy Basic v1.0 (VIC20)", false, false, false) {
      @Override
      protected void init() {
        // copy back the Basic V2.0
        for (Object[] couple : BASIC_TOKENS_V2_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_HANDY) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    },
    
    BASIC_V8("Basic v8 Walrusoft' (C128)", false, true,  true) {
      @Override
      protected void init() {
        // copy back the Basic V7.0
        for (Object[] couple : BASIC_TOKENS_V7_0) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }

        for (Object[] couple : BASIC_TOKENS_V8) {
          hashBasic.put((Integer) couple[0], (String) couple[1]);
        }
      }         
    };
        
    
     
    /** Opcode 64 used */
    boolean op64;
    
    /** Opcode CE used */
    boolean opCE;
    
    /** Opdocde FE used*/
    boolean opFE;
    
    /** Name of the Basic */
    String name;
    
    /** Hashtable with opcode and tokens*/
    Hashtable<Integer, String> hashBasic=new Hashtable();

    private BasicType(String name, boolean op64, boolean opCE, boolean opFE) {
      this.name=name;
      this.op64 = op64;
      this.opCE = opCE;
      this.opFE = opFE;
      init();
    }  
    
    /**
     * Empty initialization
     */
    protected void init() {      
    }
    
    public String getName() {
      return name;
    }
    
    /**
     * Give the actual hashtable for basic tokens
     * 
     * @return the hash table with tokens
     */
    public Hashtable<Integer, String> getHashBasic() {
      return hashBasic;
    }
  }

  public BasicDetokenize() {
  }
   
  /** BASIC V2.0 tokens */
  private static final Object[][] BASIC_TOKENS_V2_0= {
    {0x80, "END"},      {0x81, "FOR"},     {0x82, "NEXT"},     {0x83, "DATA"},       
    {0x84, "INPUT#"},   {0x85, "INPUT"},   {0x86, "DIM"},      {0x87, "READ"},       
    {0x88, "LET"},      {0x89, "GOTO"},    {0x8A, "RUN"},      {0x8B, "IF"},       
    {0x8C, "RESTORE"},  {0x8D, "GOSUB"},   {0x8E, "RETURN"},   {0x8F, "REM"},
    {0x90, "STOP"},     {0x91, "ON"},      {0x92, "WAIT"},     {0x93, "LOAD"},     
    {0x94, "SAVE"},     {0x95, "VERIFY"},  {0x96, "DEF"},      {0x97, "POKE"}, 
    {0x98, "PRINT#"} ,  {0x99, "PRINT"},   {0x9A, "CONT"},     {0x9B, "LIST"},
    {0x9C, "CLR"},      {0x9D, "CMD"},     {0x9E, "SYS"},      {0x9F, "OPEN"},
    {0xA0, "CLOSE"},    {0xA1, "GET"},     {0xA2, "NEW"},      {0xA3, "TAB("},
    {0xA4, "TO"},       {0xA5, "FN"},      {0xA6, "SPC("},      {0xA7, "THEN"},
    {0xA8, "NOT"},      {0xA9, "STEP"},    {0xAA, "+"},        {0xAB, "-"}, 
    {0xAC, "*"},        {0xAD, "/"},       {0xAE, "^"},        {0xAF, "AND"},
    {0xB0, "OR"},       {0xB1, ">"},       {0xB2, "="},        {0xB3, "<"},
    {0xB4, "SGN"},      {0xB5, "INT"},     {0xB6, "ABS"},      {0xB7, "USR"},
    {0xB8, "FRE"},      {0xB9, "POS"},     {0xBA, "SQR"},      {0xBB, "RND"},
    {0xBC, "LOG"},      {0xBD, "EXP"},     {0xBE, "COS"},      {0xBF, "SIN"},
    {0xC0, "TAN"},      {0xC1, "ATN"},     {0xC2, "PEEK"},     {0xC3, "LEN"}, 
    {0xC4, "STR$"},     {0xC5, "VAL"},     {0xC6, "ASC"},      {0xC7, "CHR$"},
    {0xC8, "LEFT$"},    {0xC9, "RIGHT$"},  {0xCA, "MID$"},     {0xCB, "GO"}
  };  
  
  /** BASIC V3.5 tokens */
  private static final Object[][] BASIC_TOKENS_V3_5={
    {0xCC, "RGR"},      {0xCD, "RCLR"},    {0xCE, "RLUM"},     {0xCF, "JOY"}, 
    {0xD0, "RDOT"},     {0xD1, "DEC"},     {0xD2, "HEX$"},     {0xD3, "ERR$"},
    {0xD4, "INSTR"},    {0xD5, "ELSE"},    {0xD6, "RESUME"},   {0xD7, "TRAP"}, 
    {0xD8, "TRON"},     {0xD9, "TROFF"},   {0xDA, "SOUND"},    {0xDB, "VOL"},
    {0xDC, "AUTO"},     {0xDD, "PUDEF"},   {0xDE, "GRAPHICS"}, {0xDF, "PAINT"},
    {0xE0, "CHAR"},     {0xE1, "BOX"},     {0xE2, "CIRCLE"},   {0xE3, "GSHAPE"},
    {0xE4, "SSHAPE"},   {0xE5, "DRAW"},    {0xE6, "LOCATE"},   {0xE7, "COLOR"},
    {0xE8, "SCNCLR"},   {0xE9, "SCALE"},   {0xEA, "HELP"},     {0xEB, "DO"},
    {0xEC, "LOOP"},     {0xED, "EXIT"},    {0xEE, "DIRECTORY"},{0xEF, "DSAVE"},
    {0xF0, "DLOAD"},    {0xF1, "HEADER"},  {0xF2, "SCRATCH"},  {0xF3, "COLLECT"},
    {0xF4, "COPY"},     {0xF5, "RENAME"},  {0xF6, "BACKUP"},   {0xF7, "DELETE"},
    {0xF8, "RENUMBER"}, {0xF9, "KEY"},     {0xFA, "MONITOR"},  {0xFB, "USING"},
    {0xFC, "UNTIL"},    {0xFD, "WHILE"}
  };  
  
  /** BASIC V4.0 tokens */
  private static final Object[][] BASIC_TOKENS_V4_0={
    {0xCC, "CONCAT"},  {0xCD, "DOPEN"},    {0xCE, "DCLOSE"},   {0xCF, "RECORD"},
    {0xD0, "HEADER"},  {0xD1, "COLLECT"},  {0xD2, "BACKUP"},   {0xD3, "COPY"},
    {0xD4, "APPEND"},  {0xD5, "DSAVE"},    {0xD6, "DLOAD"},    {0xD7, "CATALOG"},
    {0xD8, "RENAME"},  {0xD9, "SCRATCH"},  {0xDA, "DIRECTORY"}    
  };
  
  /** BASIC V7.0 tokens */
  private static final Object[][] BASIC_TOKENS_V7_0={
    {0xCC, "RGR"},       {0xCD, "RCLR"},     {0xCE02, "POT"},     {0xCE03, "BUMP"},
    {0xCE04, "PEN"},     {0xCE05, "RSPPOS"}, {0xCE06, "RSPRITE"}, {0xCE07, "RSPCOLOR"},
    {0xCE08, "XOR"},     {0xCE09, "RWINDOW"},{0xCE0A, "POINTER"}, {0xCF, "JOY"},
    {0xD0, "RDOT"},      {0xD1, "DEC"},      {0xD2, "HEX$"},      {0xD3, "ERR$"},
    {0xD4, "INSTR"},     {0xD5, "ELSE"},     {0xD6, "RESUME"},    {0xD7, "TRAP"},
    {0xD8, "TRON"},      {0xD9, "TROFF"},    {0xDA, "SOUND"},     {0xDB, "VOL"},
    {0xDC, "AUTO"},      {0xDD, "PUDEF"},    {0xDE, "GRAPHICS"},  {0xDF, "PAINT"},
    {0xE0, "CHAR"},      {0xE1, "BOX"},      {0xE2, "CIRCLE"},    {0xE3, "GSHAPE"},
    {0xE4, "SSHAPE"},    {0xE5, "DRAW"},     {0xE6, "LOCATE"},    {0xE7, "COLOR"},
    {0xE8, "SCNCLR"},    {0xE9, "SCALE"},    {0xEA, "HELP"},      {0xEB, "DO"},
    {0xEC, "LOOP"},      {0xED, "EXIT"},     {0xEE, "DIRECTORY"}, {0xEF, "DSAVE"},
    {0xF0, "DLOAD"},     {0xF1, "HEADER"},   {0xF2, "SCRATCH"},   {0xF3, "COLLECT"},
    {0xF4, "COPY"},      {0xF5, "RENAME"},   {0xF6, "BACKUP"},    {0xF7, "DELETE"},
    {0xF8, "RENUMBER"},  {0xF9, "KEY"},      {0xFA, "MONITOR"},   {0xFB, "USING"},
    {0xFC, "UNTIL"},     {0xFD, "WHILE"},    {0xFE02, "BANK"},    {0xFE03, "FILTER"},
    {0xFE04, "PLAY"},    {0xFE05, "TEMPO"},  {0xFE06, "MOVSPR"},  {0xFE07, "SPRITE"},
    {0xFE08, "SPRCOLOR"},{0xFE09, "RREG"},   {0xFE0A, "ENVELOPE"},{0xFE0B, "SLEEP"},
    {0xFE0C, "CATALOG"}, {0xFE0D, "DOPEN"},  {0xFE0E, "APPEND"},  {0xFE0F, "DCLOSE"},
    {0xFE10, "BSAVE"},   {0xFE11, "BLOAD"},  {0xFE12, "RECORD"},  {0xFE13, "CONCAT"},
    {0xFE14, "DVERIFY"}, {0xFE15, "DCLEAR"}, {0xFE16, "SPRSAVE"}, {0xFE17, "COLLISION"},
    {0xFE18, "BEGIN"},   {0xFE19, "BEND"},   {0xFE1A, "WINDOW"},  {0xFE1B, "BOOT"},
    {0xFE1C, "WIDTH"},   {0xFE1D, "SPRDEF"}, {0xFE1E, "QUIT"},    {0xFE1F, "STASH"},
    {0xFE21, "FETCH"},   {0xFE23, "SWAP"},   {0xFE24, "OFF"},     {0xFE25, "FAST"},
    {0xFE26, "SLOW"}    
  };  
  
  /** BASIC Simon tokens */
  private static final Object[][] BASIC_TOKENS_SIMON={  
    {0x6400, ""},        {0x6401, "HIRES"},   {0x6402, "PLOT"},    {0x6403, "LINE"},   
    {0x6404, "BLOCK"},   {0x6405, "FCHR"},    {0x6406, "FCOL"},    {0x6407, "FILL"},
    {0x6408, "REC"},     {0x6409, "ROT"},     {0x640A, "DRAW"},    {0x640B, "CHAR"}, 
    {0x640C, "HI COL"},  {0x640D, "INV"},     {0x640E, "FRAC"},    {0x640F, "MOVE"},
    {0x6410, "PLACE"},   {0x6411, "UPB"},     {0x6412, "UPW"},     {0x6413, "LEFTW"}, 
    {0x6414, "LEFTB"},   {0x6415, "DOWNB"},   {0x6416, "DOWNW"},   {0x6417, "RIGHTB"}, 
    {0x6418, "RIGHTW"},  {0x6419, "MULTI"},   {0x641A, "COLOUR"},  {0x641B, "MMOB"}, 
    {0x641C, "BFLASH"},  {0x641D, "MOB SET"}, {0x641E, "MUSIC"},   {0x641F, "FLASH"}, 
    {0x6420, "REPAT"},   {0x6421, "PLAY"},    {0x6422, ">>"},      {0x6423, "CENTRE"}, 
    {0x6424, "ENVELOPE"},{0x6425, "CGOTO"},   {0x6426, "WAVE"},    {0x6427, "FETCH"}, 
    {0x6428, "AT("},     {0x6429, "UNTIL"},   {0x642A, ">>"},      {0x642B, ">>"}, 
    {0x642C, "USE"},     {0x642D, ">>"},      {0x642E, "GLOBAL"},  {0x642F, ">>"}, 
    {0x6430, "RESET"},   {0x6431, "PROC"},    {0x6432, "CALL"},    {0x6433, "EXEC"}, 
    {0x6434, "END PROC"},{0x6435, "EXIT"},    {0x6436, "END LOOP"},{0x6437, "ON KEY"}, 
    {0x6438, "DISABLE"}, {0x6439, "RESUME"},  {0x643A, "LOOP"},    {0x643B, "DELAY"}, 
    {0x643C, ">>"},      {0x643D, ">>"},      {0x643E, ">>"},      {0x643F, ">>"}, 
    {0x6440, "SECURE"},  {0x6441, "DISAPA"},  {0x6442, "CIRCLE"},  {0x6443, "ON ERROR"}, 
    {0x6444, "NO ERROR"},{0x6445, "LOCAL"},   {0x6446, "RCOMP"},   {0x6447, "ELSE"}, 
    {0x6448, "RETRACE"}, {0x6449, "TRACE"},   {0x644A, "DIR"},     {0x644B, "PAGE"}, 
    {0x644C, "DUMP"},    {0x644D, "FIND"},    {0x644E, "OPTION"},  {0x644F, "AUTO"}, 
    {0x6450, "OLD"},     {0x6451, "JOY"},     {0x6452, "MOD"},     {0x6453, "DIV"}, 
    {0x6454, ">>"},      {0x6455, "DUP"},     {0x6456, "INKEY"},   {0x6457, "INST"}, 
    {0x6458, "TEST"},    {0x6459, "LIN"},     {0x645A, "EXOR"},    {0x645B, "INSERT"}, 
    {0x645C, "POT"},     {0x645D, "PENX"},    {0x645E, ">>"},      {0x645F, "PENY"}, 
    {0x6460, "SOUND"},   {0x6461, "GRAPHICS"},{0x6462, "DESIGN"},  {0x6463, "RLOCMOB"}, 
    {0x6464, "CMOB"},    {0x6465, "BCKGNDS"}, {0x6466, "PAUSE"},   {0x6467, "NRM"}, 
    {0x6468, "MOB OFF"}, {0x6469, "OFF"},     {0x646A, "ANGL"},    {0x646B, "ARC"}, 
    {0x646C, "COLD"},    {0x646D, "SCRSV"},   {0x646E, "SCRLD"},   {0x646F, "TEXT"}, 
    {0x6470, "CSET"},    {0x6471, "VOL"},     {0x6472, "DISK"},    {0x6473, "HRDCPY"}, 
    {0x6474, "KEY"},     {0x6475, "PAINT"},   {0x6476, "LOW COL"}, {0x6477, "COPY"}, 
    {0x6478, "MERGE"},   {0x6479, "RENUMBER"},{0x647A, "MEM"},     {0x647B, "DETECT"}, 
    {0x647C, "CHECK"},   {0x647D, "DISPLAY"}, {0x647E, "ERR"},     {0x647F, "OUT"}
  };
  
  /** BASIC Andre Fachat tokens */
  private static final Object[][] BASIC_TOKENS_ANDRE_FACHAT={
    {0xCC, "TRACE"},     {0xCD, "DELETE"},    {0xCE, "AUTO"},     {0xCF, "OLD"},
    {0xD0, "DUMP"},      {0xD1, "FIND"},      {0xD2, "RENUMBER"}, {0xD3, "DLOAD"},
    {0xD4, "DSAVE"},     {0xD5, "DVERIFY"},   {0xD6, "DIRECTORY"},{0xD7, "CATALOG"},
    {0xD8, "SCRATCH"},   {0xD9, "COLLECT"},   {0xDA, "RENAME"},   {0xDB, "COPY"},
    {0xDC, "BACKUP"},    {0xDD, "DISK"},      {0xDE, "HEADER"},   {0xDF, "APPEND"},
    {0xE0, "MERGE"},     {0xE1, "MLOAD"},     {0xE2, "MVERIFY"},  {0xE3, "MSAVE"},
    {0xE4, "KEY"},       {0xE5, "BASIC"},     {0xE6, "RESET"},    {0xE7, "EXIT"},
    {0xE8, "ENTER"},     {0xE9, "DOKE"},      {0xEA, "SET"},      {0xEB, "HELP"},
    {0xEC, "SCREEN"},    {0xED, "LOMEM"},     {0xEE, "HIMEM"},    {0xEF, "COLOUR"},
    {0xF0, "TYPE"},      {0xF1, "TIME"},      {0xF2, "DEEK"},     {0xF3, "HEX$"},
    {0xF4, "BIN$"},      {0xF5, "OFF"},       {0xF6, "ALARM"}    
  };
  
  /** BASIC speech v2.7 tokens */
  private static final Object[][] BASIC_TOKENS_SPEECH={
    {0xCC, "RESET"},     {0xCD, "BASIC"},    {0xCE, "HELP"},     {0xCF, "KEY"},
    {0xD0, "HIMEM"},     {0xD1, "DISK"},     {0xD2, "DIR"},      {0xD3, "BLOAD"},
    {0xD4, "BSAVE"},     {0xD5, "MAP"},      {0xD6, "MEM"},      {0xD7, "PAUSE"},
    {0xD8, "BLOCK"},     {0xD9, "HEAR"},     {0xDA, "RECORD"},   {0xDB, "PLAY"},
    {0xDC, "VOLDER"},    {0xDD, "COLDEF"},   {0xDE, "HEX"},      {0xDF, "DEZ"},
    {0xE0, "SCREEN"},    {0xE1, "EXEC"},     {0xE2, "MON"},      {0xE3, "<-"},
    {0xE4, "FROM"},      {0xE5, "SPEED"},    {0xE6, "OFF"}          
  };
  
  /** BASIC Final Cartiridge III tokens */
  private static final Object[][] BASIC_TOKENS_FINAL_CART3={
    {0xCC, "OFF"},     {0xCD, "AUTO"},   {0xCE, "DEL"},      {0xCF, "RENUM"},
    {0xD0, "?ERROR?"}, {0xD1, "FIND"},   {0xD2, "OLD"},      {0xD3, "DLOAD"},
    {0xD4, "DVERIFY"}, {0xD5, "DSAVE"},  {0xD6, "APPEND"},   {0xD7, "DAPPEND"},
    {0xD8, "DOS"},     {0xD9, "KILL"},   {0xDA, "MOM"},      {0xDB, "PDIR"},
    {0xDC, "PLIST"},   {0xDD, "BAR"},    {0xDE, "DESKTOP"},  {0xDF, "DUMP"},
    {0xE0, "ARRAY"},   {0xE1, "MEM"},    {0xE2, "TRACE"},    {0xE3, "REPLACE"},
    {0xE4, "ORDER"},   {0xE5, "PACK"},   {0xE6, "UNPACK"},   {0xE7, "MREAD"},
    {0xE8, "MWRITE"}
  };
  
  /** BASIC Ultrabasic tokens */
  private static final Object[][] BASIC_TOKENS_ULTRABASIC={
    {0xCC, "DOT"},     {0xCD, "DRAW"},    {0xCE, "BOX"},      {0xCF, "TIC"}, 
    {0xD0, "COPY"},    {0xD1, "SPRITE"},  {0xD2, "OFF"},      {0xD3, "MODE"},
    {0xD4, "NORM"},    {0xD5, "GRAPH"},   {0xD6, "DUMP"},     {0xD7, "GREAD"}, 
    {0xD8, "CHAR"},    {0xD9, "PLACE"},   {0xDA, "MULTI"},    {0xDB, "HIRES"},
    {0xDC, "HEX"},     {0xDD, "BIT"},     {0xDE, "COLORS"},   {0xDF, "PIXEL"},
    {0xE0, "FILL"},    {0xE1, "CIRCLE"},  {0xE2, "BLOCK"},    {0xE3, "SDATA"},
    {0xE4, "VOL"},     {0xE5, "GEN"},     {0xE6, "SCROLL"},   {0xE7, "BCOLL"},
    {0xE8, "JOY"},     {0xE9, "PADDLE"},  {0xEA, "PEN"},      {0xEB, "SOUND"},
    {0xEC, "TUNE"},    {0xED, "TDATA"},   {0xEE, "SET"},      {0xEF, "TURNTO"},
    {0xF0, "TURN"},    {0xF1, "TUP"},     {0xF2, "TDOWN"},    {0xF3, "TCOLOR"},
    {0xF4, "TURTLE"},  {0xF5, "MOVE"},    {0xF6, "BYE"},      {0xF7, "ROTATE"},
    {0xF8, "TPOS"},    {0xF9, "CTR"},     {0xFA, "SCTR"},     {0xFB, "["},
    {0xFC, "]"},       {0xFD, "HARD"},    {0xFE, "EXIT"}
  };  
  
  /** BASIC Graphics tokens */
  private static final Object[][] BASIC_TOKENS_GRAPHICS={
    {0xCC, "BACKGROUND"}, {0xCD, "BORDER"},   {0xCE, "DIR"},     {0xCF, "DISK"}, 
    {0xD0, "FILL"},       {0xD1, "KEY"},      {0xD2, "CIRCLE"},  {0xD3, "PROCEDURE"},
    {0xD4, "DOT"},        {0xD5, "FIND"},     {0xD6, "CHANGE"},  {0xD7, "REN"}, 
    {0xD8, "ELSE"},       {0xD9, "COPY"},     {0xDA, "SCROLL"},  {0xDB, "ROLL"},
    {0xDC, "BOX"},        {0xDD, "SCALE"},    {0xDE, "DO"},      {0xDF, "LINE"},
    {0xE0, "SPRITE"},     {0xE1, "COLOR"},    {0xE2, "HIRES"},   {0xE3, "CLEAR"},
    {0xE4, "TEXT"},       {0xE5, "WINDOW"},   {0xE6, "OFF"},     {0xE7, "AT"},
    {0xE8, "SHAPE"},      {0xE9, "XYSIZE"},   {0xEA, "SPEED"},   {0xEB, "FROM"},
    {0xEC, "SETORIGIN"},  {0xED, "ANIMATE"},  {0xEE, "MULTI"},   {0xEF, "EZE"},
    {0xF0, "MOVE"},       {0xF1, "UNDER"},    {0xF2, "EDIT"},    {0xF3, "RESET"},
    {0xF4, "XPOS"},       {0xF5, "GPRINT"},   {0xF6, "VOICE"},   {0xF7, "ADSR"},
    {0xF8, "WAVE"},       {0xF9, "NE"},       {0xFA, "VOLUME"},  {0xFB, "PLAY"},
    {0xFC, "YPOS"},       {0xFD, "SOUND"},    {0xFE, "JOY"}
  };   
  
  /** BASIC WS tokens */
  private static final Object[][] BASIC_TOKENS_WS={
    {0xCC, "COPY"},   {0xCD, "OLD"},     {0xCE, "PORT"},     {0xCF, "DOKE"}, 
    {0xD0, "VPOKE"},  {0xD1, "FILL"},    {0xD2, "ERROR"},    {0xD3, "SEND"},
    {0xD4, "CALL"},   {0xD5, "BIT"},     {0xD6, "DIR"},      {0xD7, "BLOAD"}, 
    {0xD8, "BSAVE"},  {0xD9, "FIND"},    {0xDA, "SPEED"},    {0xDB, "PITCH"},
    {0xDC, "SAY"},    {0xDD, "FAST"},    {0xDE, "SLOW"},     {0xDF, "TALK"},
    {0xE0, "SHUTUP"}, {0xE1, "STASH"},   {0xE2, "FETCH"},    {0xE3, "SWAP"},
    {0xE4, "OFF"},    {0xE5, "SCREEN"},  {0xE6, "DEVICE"},   {0xE7, "OBJECT"},
    {0xE8, "VSTASH"}, {0xE9, "VFETCH"},  {0xEA, "QUIET"},    {0xEB, "COLOR"},
    {0xEC, "CLS"},    {0xED, "CURPOS"},  {0xEE, "MONITOR"},  {0xEF, "SUBEND"},
    {0xF0, "DO"},     {0xF1, "LOOP"},    {0xF2, "EXIT"},     {0xF3, "DEEK"},
    {0xF4, "RSC"},    {0xF5, "RSM"},     {0xF6, "DEC"},      {0xF7, "HEX$"},
    {0xF8, "HI"},     {0xF9, "LO"},      {0xFA, "DS$"},      {0xFB, "LINE"},
    {0xFC, "VPEEK"},  {0xFD, "ROW"},     {0xFE, "JOY"}
  };   
  
  /** BASIC Pegasus v4.0 tokens */
  private static final Object[][] BASIC_TOKENS_PEGASUS={
    {0xCC, "OFF"},      {0xCD, "ASC("},     {0xCE, "SIN("},     {0xCF, "COS("}, 
    {0xD0, "TAN("},     {0xD1, "ATN("},     {0xD2, "DEG("},     {0xD3, "RAD("},
    {0xD4, "FRAC("},    {0xD5, "MOD("},     {0xD6, "ROUND("},   {0xD7, "DEC("}, 
    {0xD8, "BIN("},     {0xD9, "DEEK("},    {0xDA, "INSTR("},   {0xDB, "JOY("},
    {0xDC, "POT("},     {0xDD, "SCREEN("},  {0xDE, "TEST("},    {0xDF, "USING("},
    {0xE0, "DS$"},      {0xE1, "HEX$"},     {0xE2, "BIN$"},     {0xE3, "SPACE$"},
    {0xE4, "UCASE$"},   {0xE5, "STRING$"},  {0xE6, "INPUT$"},   {0xE7, "TIME$"},
    {0xE8, "SPRITEX("}, {0xE9, "SPRITEY("}, {0xEA, "TURTLEX("}, {0xEB, "TURTLEY("},
    {0xEC, "TURTLEANG"}
  };
  
  /** BASIC Xbasic tokens */
  private static final Object[][] BASIC_TOKENS_XBASIC={
    {0xCC, "SPRAT"},   {0xCD, "BRDR"},    {0xCE, "SCREEN"},    {0xCF, "QUIT"}, 
    {0xD0, "SPRMULT"}, {0xD1, "MOVE"},    {0xD2, "SPRITE"},    {0xD3, "ASPRITE"},
    {0xD4, "DSPRITE"}, {0xD5, "SID"},     {0xD6, "ENVELOPE"},  {0xD7, "GATE"}, 
    {0xD8, "FRQ"},     {0xD9, "WAVE"},    {0xDA, "VOL"},       {0xDB, "FCUT"},
    {0xDC, "FMODE"},   {0xDD, "FILTER"},  {0xDE, "FRSN"},      {0xDF, "CSET"},
    {0xE0, "MULTI"},   {0xE1, "EXTND"},   {0xE2, "LOCALE"},    {0xE3, "CENTER"},
    {0xE4, "HIRES"},   {0xE5, "LINE"},    {0xE6, "HPRNT"},     {0xE7, "PLOT"},
    {0xE8, "TEXT"},    {0xE9, "CLEAR"},   {0xEA, "COLR"},      {0xEB, "STICK"},
    {0xEC, "BTN"}
  };  
  
  /** BASIC Drago v2.2 tokens */
  private static final Object[][] BASIC_TOKENS_DRAGO={
    {0xCC, "PUNKT"},   {0xCD, "LINIA"},    {0xCE, "RYSUJ"},    {0xCF, "PARAM"}, 
    {0xD0, "KUNTUR"},  {0xD1, "ANIM"},     {0xD2, "KOLOR"},    {0xD3, "PUWID"},
    {0xD4, "RYSELIP"}, {0xD5, "KOGUMA"},   {0xD6, "FIUT"},     {0xD7, "FIGURA"}, 
    {0xD8, "FIGUMA"}
  };  
  
  /** BASIC Reu tokens */
  private static final Object[][] BASIC_TOKENS_REU={
    {0xCC, "PUSH"},   {0xCD, "PULL"},    {0xCE, "FLIP"},    {0xCF, "REC"}, 
    {0xD0, "STASH"},  {0xD1, "FETCH"},   {0xD2, "SWAP"},    {0xD3, "REU"},
    {0xD4, "SIZE"},   {0xD5, "DIR"},     {0xD6, "@"},       {0xD7, "KILL"}, 
    {0xD8, "ROM"},    {0xD9, "RAM"},     {0xDA, "MOVE"}
  };  
  
  /** BASIC Lightning tokens */
  private static final Object[][] BASIC_TOKENS_LIGHTNING={
    {0xCC, "ELSE"},    {0xCD, "HEX$"},     {0xCE, "DDEK"},     {0xCF, "TRUE"}, 
    {0xD0, "IMPORT"},  {0xD1, "CFN"},      {0xD2, "SIZE"},     {0xD3, "FALSE"},
    {0xD4, "VER$"},    {0xD5, "LPX"},      {0xD6, "LPY"},      {0xD7, "COMMON%"}, 
    {0xD8, "CROW"},    {0xD9, "CCOL"},     {0xDA, "ATR"},      {0xDB, "INC"},
    {0xDC, "NUM"},     {0xDD, "ROW2"},     {0xDE, "COL2"},     {0xDF, "SPN2"},
    {0xE0, "HGT"},     {0xE1, "WID"},      {0xE2, "ROW"},      {0xE3, "COL"},
    {0xE4, "SPN"},     {0xE5, "TASK"},     {0xE6, "HALT"},     {0xE7, "REPEAT"},
    {0xE8, "UNTIL"},   {0xE9, "WHILE"},    {0xEA, "WEND"},     {0xEB, "CIF"},
    {0xEC, "CELSE"},   {0xED, "CEND"},     {0xEE, "LABEL"},    {0xEF, "DOKE"},
    {0xF0, "EXIT"},    {0xF1, "ALLOCATE"}, {0xF2, "DASABLE"},  {0xF3, "PULL"},
    {0xF4, "DLOAD"},   {0xF5, "DSAVE"},    {0xF6, "VAR"},      {0xF7, "LOCAL"},
    {0xF8, "PROCEND"}, {0xF9, "PROC"},     {0xFA, "CASEND"},   {0xFB, "OF"},
    {0xFC, "CASE"},    {0xFD, "RPT"},      {0xFE, "SETATR"}
  };
  
  /** BASIC Magic tokens */
  private static final Object[][] BASIC_TOKENS_MAGIC={
    {0xCC, "ASSEMBLER"}, {0xCD, "AUTO"},    {0xCE, "CDRIVE"},  {0xCF, "CAT"}, 
    {0xD0, "DAPPEND"},   {0xD1, "DELETE"},  {0xD2, "DEZ"},     {0xD3, "DIR"},
    {0xD4, "DLOAD"},     {0xD5, "DSAVE"},   {0xD6, "DVERIFY"}, {0xD7, "CONFIG"}, 
    {0xD8, "FIND"},      {0xD9, " "},       {0xDA, " "},       {0xDB, "HELP"},
    {0xDC, "HEX"},       {0xDD, "JUMP"},    {0xDE, "LLIST"},   {0xDF, "LPRINT"},
    {0xE0, "OFF"},       {0xE1, "OLD"},     {0xE2, "RENUM"},   {0xE3, "CRUN"},
    {0xE4, "SEND"},      {0xE5, "STATUS"},  {0xE6, "HIRES"},   {0xE7, "MULTI"},
    {0xE8, "CLEAR"},     {0xE9, "PLOT"},    {0xEA, "INVERT"},  {0xEB, "LINE"},
    {0xEC, "TEXT"},      {0xED, "GRAPHIK"}, {0xEE, "PAGE"},    {0xEF, "BOX"},
    {0xF0, "DRAW"},      {0xF1, "MIX"},     {0xF2, "COPY"},    {0xF3, "CIRCLE"},
    {0xF4, "GSAVE"},     {0xF5, "GLOAD"},   {0xF6, "FRAME"},   {0xF7, "HPRINT"},
    {0xF8, "VPRINT"},    {0xF9, "BLOCK"},   {0xFA, "FILL"},    {0xFB, " "},
    {0xFC, "REPLACE"},   {0xFD, "LRUN"}
  };  
  
  /** BASIC Blarg tokens */
  private static final Object[][] BASIC_TOKENS_BLARG={
    {0xE0, "PLOT"},      {0xE1, "LINE"},    {0xE2, "CIRCLE"},  {0xE3, "GRON"},
    {0xE4, "GROFF"},     {0xE5, "MODE"},    {0xE6, "ORIGIN"},  {0xE7, "cLEAR"},
    {0xE8, "BUFFER"},    {0xE9, "SWAP"},    {0xEA, "COLOr"}
  };  
  
  /** BASIC WS Final tokens */
  private static final Object[][] BASIC_TOKENS_WS_FINAL={
    {0xCC, "COPY"},    {0xCD, "BANJ"},    {0xCE, "OLD"},      {0xCF, "DOKE"}, 
    {0xD0, "DISPLAY"}, {0xD1, "FILL"},    {0xD2, "ERROR"},    {0xD3, "SEND"},
    {0xD4, "CALL"},    {0xD5, "BIT"},     {0xD6, "DIR"},      {0xD7, "BLOAD"}, 
    {0xD8, "BSAVE"},   {0xD9, "FIND"},    {0xDA, "SPEED"},    {0xDB, "PITCH"},
    {0xDC, "SAY"},     {0xDD, "FAST"},    {0xDE, "SLOW"},     {0xDF, "TALK"},
    {0xE0, "SHUTUP"},  {0xE1, "STASH"},   {0xE2, "FETCH"},    {0xE3, "SWAP"},
    {0xE4, "OFF"},     {0xE5, "MODE"},    {0xE6, "DEVICE"},   {0xE7, "OBJECT"},
    {0xE8, "VSTASH"},  {0xE9, "VFETCH"},  {0xEA, "LATCH"},    {0xEB, "COLOR"},
    {0xEC, "CLS"},     {0xED, "CURPOS"},  {0xEE, "MONITOR"},  {0xEF, "SUBEND"},
    {0xF0, "DO"},      {0xF1, "LOOP"},    {0xF2, "EXIT"},     {0xF3, "DEEK"},
    {0xF4, "COL"},     {0xF5, "RSM"},     {0xF6, "DEC"},      {0xF7, "HEX$"},
    {0xF8, "HI"},      {0xF9, "LO"},      {0xFA, "DS$"},      {0xFB, "LINE"},
    {0xFC, "BNK"},     {0xFD, "YPOS"},    {0xFE, "JOY"}
  };   
  
  /** BASIC Game tokens */
  private static final Object[][] BASIC_TOKENS_GAME={
    {0xCC, "WINDOW"},  {0xCD, "BFILE"},    {0xCE, "UPPER"},    {0xCF, "LOWER"},
    {0xD0, "CLS"},     {0xD1, "SCREEN"},   {0xD2, "PARSE"},    {0xD3, "PROC"},
    {0xD4, "ELSE"},    {0xD5, "SCRATCH"},  {0xD6, "REPLACE"},  {0xD7, "DEVICE"},
    {0xD8, "DIR"},     {0xD9, "REPEAT"},   {0xDA, "UNTIL"},    {0xDB, "DISK"},
    {0xDC, "FETCH#"},  {0xDD, "PUT#"},     {0xDE, "PROMPT"},   {0xDF, "POP"},
    {0xE0, "HELP"},    {0xE1, "EXIT"},     {0xE2, "DISABLE"},  {0xE3, "ENTER"},
    {0xE4, "RESET"},   {0xE5, "WARM"},     {0xE6, "NUM"},      {0xE7, "TYPE"},
    {0xE8, "TEXT$"}
  };  
  
  /** BASIC Basex tokens */
  private static final Object[][] BASIC_TOKENS_BASEX={
    {0xCC, "APPEND"}, {0xCD, "AUTO"},     {0xCE, "BAR"},     {0xCF, "CIRCLE"}, 
    {0xD0, "CLG"},    {0xD1, "CLS"},      {0xD2, "CSR"},     {0xD3, "DELETE"},
    {0xD4, "DISK"},   {0xD5, "DRAW"},     {0xD6, "EDGE"},    {0xD7, "ENVELOPE"}, 
    {0xD8, "FILL"},   {0xD9, "KEY"},      {0xDA, "MOB"},     {0xDB, "MODE"},
    {0xDC, "MOVE"},   {0xDD, "OLD"},      {0xDE, "PIC"},     {0xDF, "DUMP"},
    {0xE0, "PLOT"},   {0xE1, "RENUMBER"}, {0xE2, "REPEAT"},  {0xE3, "SCROLL"},
    {0xE4, "SOUND"},  {0xE5, "WHILE"},    {0xE6, "UNTIL"},   {0xE7, "VOICE"},
    {0xE8, "ASS"},    {0xE9, "DIS"},      {0xEA, "MEM"}
  };  
  
  /** BASIC Super tokens */
  private static final Object[][] BASIC_TOKENS_SUPER={
    {0xDB, "VOLUME"},
    {0xDC, "RESET"},  {0xDD, "MEM"},      {0xDE, "TRACE"},     {0xDF, "BASIC"},
    {0xE0, "RESUME"}, {0xE1, "LETTER"},   {0xE2, "HELP"},      {0xE3, "COKE"},
    {0xE4, "GROUND"}, {0xE5, "MATRIX"},   {0xE6, "DISPOSE"},   {0xE7, "PRINT@"},
    {0xE8, "HIMEM"},  {0xE9, "HARDCOPY"}, {0xEA, "INPUTFORM"}, {0xEB, "LOCK"},
    {0xEC, "SWAP"},   {0xED, "USING"},    {0xEE, "SEC"},       {0xEF, "ELSE"},
    {0xF0, "ERROR"},  {0xF1, "ROUND"},    {0xF2, "DEEK"},      {0xF3, "STRING$"},
    {0xF4, "POINT"},  {0xF5, "INSTR"},    {0xF6, "CEEK"},      {0xF7, "MIN"},
    {0xF8, "MAX"},    {0xF9, "VARPTR"},   {0xFA, "FRAC"},      {0xFB, "ODD"},
    {0xFC, "DEC"},    {0xFD, "HEX$"},     {0xFE, "EVAL"}
  };  
  
  /** BASIC Expanded tokens */
  private static final Object[][] BASIC_TOKENS_EXPANDED={
    {0xCC, "HIRES"},    {0xCD, "NORM"},     {0xCE, "GRAPH"},   {0xCF, "SET"},
    {0xD0, "LINE"},     {0xD1, "CIRCLE"},   {0xD2, "FILL"},    {0xD3, "MODE"},
    {0xD4, "CLS"},      {0xD5, "TEXT"},     {0xD6, "COLOR"},   {0xD7, "GSAVE"},
    {0xD8, "GLOAD"},    {0xD9, "INVERSE"},  {0xDA, "FRAME"},   {0xDB, "MOVE"},
    {0xDC, "USING"},    {0xDD, "RENUMBER"}, {0xDE, "DELETE"},  {0xDF, "BOX"},
    {0xE0, "MOBDEF"},   {0xE1, "SPRITE"},   {0xE2, "MOBSET"},  {0xE3, "MODSIZE"},
    {0xE4, "MOBCOLOR"}, {0xE5, "MOBMULTI"}, {0xE6, "MOBMOVE"}, {0xE7, "DOKE"},
    {0xE8, "ALLCLOSE"}, {0xE9, "OLD"},      {0xEA, "AUTO"},    {0xEB, "VOLUME"},
    {0xEC, "ENVELOPE"}, {0xED, "WAVE"},     {0xEE, "PLAY"},    {0xEF, "CASE ERROR"},
    {0xF0, "RESUME"},   {0xF1, "NO ERROR"}, {0xF2, "FIND"},    {0xF3, "INKEY"},
    {0xF4, "MERGE"},    {0xF5, "HARDCOPY"}   
  };    
  
   /** BASIC Super Expander Chip tokens */
  private static final Object[][] BASIC_TOKENS_SUPER_EXPANDER_CHIP={    
    {0xFE00, "KEY"},    {0xFE01, "COLOR"},  {0xFE02, "GRAPHIC"}, {0xFE03, "SCNCLR"},
    {0xFE04, "LOCATE"}, {0xFE05, "SCALE"},  {0xFE06, "BOX"},     {0xFE07, "CIRCLE"},
    {0xFE08, "CHAT"},   {0xFE09, "DRAW"},   {0xFE0A, "GSHAPE"},  {0xFE0B, "PAINT"},
    {0xFE0C, "SSHAPE"}, {0xFE0D, "TUNE"},   {0xFE0E, "FILTER"},  {0xFE0F, "SPRDEF"},
    {0xFE10, "TEMPO"},  {0xFE11, "MOVSPR"}, {0xFE12, "SPRCOL"},  {0xFE13, "SPRITE"},
    {0xFE14, "COLINT"}, {0xFE15, "SPSAV"},  {0xFE16, "RDUMP"},   {0xFE17, "RCLR"},
    {0xFE18, "RDOT"},   {0xFE19, "RGR"},    {0xFE1A, "RJOY"},    {0xFE1B, "RPEN"},
    {0xFE1C, "RPOT"},   {0xFE1D, "RSPCOL"}, {0xFE1E, "RSPPOS"},  {0xFE1F, "RSPR"}
  };  

  /** BASIC Warsaw tokens */
  private static final Object[][] BASIC_TOKENS_WARSAW={
    {0xDB, "HISAVE"},
    {0xDC, "SLINE"},  {0xDD, "MEM"},      {0xDE, "TRACE"},     {0xDF, "BEEP"},
    {0xE0, "RESUME"}, {0xE1, "LETTER"},   {0xE2, "HELP"},      {0xE3, "*****"},
    {0xE4, "GROUND"}, {0xE5, "REVERS"},   {0xE6, "DISPOSE"},   {0xE7, "PRINT@"},
    {0xE8, "HIMEM"},  {0xE9, "*****"},    {0xEA, "LINE"},      {0xEB, "PROC"},
    {0xEC, "AXIS"},   {0xED, "USING"},    {0xEE, "SEC"},       {0xEF, "ELSE"},
    {0xF0, "RROR"},   {0xF1, "ROUND"},    {0xF2, "****"},      {0xF3, "*******"},
    {0xF4, "*****"},  {0xF5, "*****"},    {0xF6, "POUND"},     {0xF7, "MIN"},
    {0xF8, "MAX"},    {0xF9, "******"},   {0xFA, "FRAC"},      {0xFB, "ODD"},
    {0xFC, "***"},    {0xFD, "HEEK"},     {0xFE, "EVAL"}
  };
  
  /** BASIC Data Becker Supergrafik tokens */
  private static final Object[][] BASIC_TOKENS_DBS={
    {0xD7, "DIRECTORY"}, 
    {0xD8, "SPOWER"},   {0xD9, "GCOMB"},   {0xDA, "DTASET"},    {0xDB, "MERGE"},
    {0xDC, "RENUM"},    {0xDD, "KEY"},     {0xDE, "TRANS"},     {0xDF, ""},
    {0xE0, "TUNE"},     {0xE1, "SOUND"},   {0xE2, "VOLUME="},   {0xE3, "FILTER"},
    {0xE4, "SREAD"},    {0xE5, "DEFINE"},  {0xE6, "SET"},       {0xE7, "SWAIT"},
    {0xE8, "SMODE"},    {0xE9, "GMODE"},   {0xEA, "GCLEAR"},    {0xEB, "GMOVE"},
    {0xEC, "PLOT"},     {0xED, "DRAW"},    {0xEE, "FILL"},      {0xEF, "FRAME"},
    {0xF0, "INVERS"},   {0xF1, "TEXT"},    {0xF2, "CIRCLE"},    {0xF3, "PADDLE"},
    {0xF4, "SCALE="},   {0xF5, "COLOR="},  {0xF6, "SCOL="},     {0xF7, "PCOL="},
    {0xF8, "GSAVE"},    {0xF9, "GLOAD"},   {0xFA, "HCOPY"},     {0xFB, "IRETURN"},
    {0xFC, "IF#"},      {0xFD, "PAINT"},   {0xFE, "EVAL"}
  };   
  
  /** BASIC Kipper tokens */
  private static final Object[][] BASIC_TOKENS_KIPPER={
    {0xE1, "IPCFG"},      {0xE2, "DHCP"},    {0xE3, "PING"},
    {0xE4, "MYIP"},       {0xE5, "NETMASK"}, {0xE6, "GSTEWAY"},    {0xE7, "DNS"},
    {0xE8, "TFTP"},       {0xE9, "TFGET"},   {0xEA, "TFPUT"},      {0xEB, "NETCAT"},
    {0xEC, "TCPCONNECT"}, {0xED, "POLL"},    {0xEE, "TCPLISTEN"},  {0xEF, "TCPSEND"},
    {0xF0, "TCPCLOSE"},   {0xF1, "TCPBLAT"}, {0xF2, "MAC"}
  };  
  
  /** BASIC Bails tokens */
  private static final Object[][] BASIC_TOKENS_BAILS={
    {0xE1, "IPCFG"},   {0xE2, "DHCP"},    {0xE3, "PING"},
    {0xE4, "MYIP"},    {0xE5, "NETMASK"}, {0xE6, "GATEWAY"},   {0xE7, "DNS"},
    {0xE8, "HOOK"},    {0xE9, "YIELD"},   {0xEA, "XSEND"},     {0xEB, "!"},
    {0xEC, "HTTPD"},   {0xED, "TYPE"},    {0xEE, "STATUS"},    {0xEF, "FLUSH"},
    {0xF0, "MAC"}
  };    
  
  /** BASIC Eve tokens */
  private static final Object[][] BASIC_TOKENS_EVE={
    {0xCC, "ELSE"},   {0xCD, "PAGE"},     {0xCE, "PAPER"},   {0xCF, "INK"}, 
    {0xD0, "LOCATE"}, {0xD1, "ERASE"},    {0xD2, "GRAPHIC"}, {0xD3, "SCALE"},
    {0xD4, "PEN"},    {0xD5, "POINT"},    {0xD6, "LINE"},    {0xD7, "PAINT"}, 
    {0xD8, "WRITE"},  {0xD9, "DRAW"},     {0xDA, "IMAGE"},   {0xDB, "SPRITE"},
    {0xDC, "SPRPIC"}, {0xDD, "SPRCOL"},   {0xDE, "SPRLOC"},  {0xDF, "SPRMULTI"},
    {0xE0, "TONE"},   {0xE1, "ENVELOPE"}, {0xE2, "WAVE"},    {0xE3, "VOL"},
    {0xE4, "FILTER"}, {0xE5, "DOS"},      {0xE6, "DVC"},     {0xE7, "DIR"},
    {0xE8, "CAT"},    {0xE9, "RECORD#"},  {0xEA, "SWAP"},    {0xEB, "EXIT"},
    {0xEC, "DO"},     {0xED, "LOOP"},     {0xEE, "WHILE"},   {0xEF, "UNTIL"},
    {0xF0, "CUR"},    {0xF1, "BIN$"},     {0xF2, "MAK$"},    {0xF3, "INPUT$"},
    {0xF4, "FMT$"},   {0xF5, "INFIX$"},   {0xF6, "INSTR"},   {0xF7, "DS$"},
    {0xF8, "DS"},     {0xF9, "SD"}, 
  }; 

    /** BASIC Tool tokens */
  private static final Object[][] BASIC_TOKENS_TOOL={
    {0xDB, " "},
    {0xDC, "SORT"},    {0xDD, "EXTRACT"}, {0xDE, "CARGET"},   {0xDF, " "},
    {0xE0, " "},       {0xE1, "SCREEN"},  {0xE2, "GRAPHIC"},  {0xE3, "TEXT"},
    {0xE4, "AUTO"},    {0xE5, "FIND"},    {0xE6, "DUMP"},     {0xE7, "ERROR"},
    {0xE8, "RENU"},    {0xE9, "DELETE"},  {0xEA, "PLOT"},     {0xEB, "POINT"},
    {0xEC, "DRAW"},    {0xED, "MOVE"},    {0xEE, "COLOR"},    {0xEF, "ELSE"},
    {0xF0, "DISPLAY"}, {0xF1, "TRACE"},   {0xF2, "OFF"},      {0xF3, "HCOPY"},
    {0xF4, "JOY"}
  };
  
  /** BASIC Super Expander tokens */
  private static final Object[][] BASIC_TOKENS_SUPER_EXPANDER={
    {0xCC, "KEY"},   {0xCD, "GRAPHIC"},  {0xCE, "SCNCLR"},   {0xCF, "CIRCLE"}, 
    {0xD0, "DRAW"},  {0xD1, "REGION"},   {0xD2, "COLOR"},    {0xD3, "POINT"},
    {0xD4, "SOUND"}, {0xD5, "CHAR"},     {0xD6, "PAINT"},    {0xD7, "RPOT"}, 
    {0xD8, "RPEN"},  {0xD9, "RSND"},     {0xDA, "RCOLOR"},   {0xDB, "RGB"},
    {0xDC, "RJOY"},  {0xDD, "RDOT"}
  };   
  
  /** BASIC Turtle tokens */
  private static final Object[][] BASIC_TOKENS_TURTLE={
    {0xCC, "GRAPHIC"},  {0xCD, "OLD"},    {0xCE, "TURN"},    {0xCF, "PEN"}, 
    {0xD0, "DRAW"},     {0xD1, "MOVE"},   {0xD2, "POINT"},   {0xD3, "KILL"},
    {0xD4, "WRITE"},    {0xD5, "REPEAT"}, {0xD6, "SCREEN"},  {0xD7, "DOKE"}, 
    {0xD8, "RELOC"},    {0xD9, "FILL"},   {0xDA, "RTIME"},   {0xDB, "BASE"},
    {0xDC, "PAUSE"},    {0xDD, "POP"},    {0xDE, "COLOR"},   {0xDF, "MERGE"},
    {0xE0, "CHAR"},     {0xE1, "TAKE"},   {0xE2, "SOUND"},   {0xE3, "VOL"},
    {0xE4, "PUT"},      {0xE5, "PLACE"},  {0xE6, "CLS"},     {0xE7, "ACCEPT"},
    {0xE8, "RESET"},    {0xE9, "GRAB"},   {0xEA, "RDOT"},    {0xEB, "PLR$"},
    {0xEC, "DEEK"},     {0xED, "JOY"}
  };  
  
  /** BASIC Easy tokens */
  private static final Object[][] BASIC_TOKENS_EASY={
    {0xCC, "DELETE"},  {0xCD, "OLD"},     {0xCE, "RENUMBER"},  {0xCF, "DUMP"}, 
    {0xD0, "MERGE"},   {0xD1, "PLOT"},    {0xD2, "TRACE"},     {0xD3, "KILL"},
    {0xD4, "HELP"},    {0xD5, "DLOAD"},   {0xD6, "DSAVE"},     {0xD7, "DVERIFY"}, 
    {0xD8, "APPEND"},  {0xD9, "SCREEN"},  {0xDA, "DIRECTORY"}, {0xDB, "KEY"},
    {0xDC, "SEND"},    {0xDD, "POP"},     {0xDE, "OFF"},       {0xDF, "POUT"},
    {0xE0, "HEADER"},  {0xE1, "FIND"},    {0xE2, "AUTO"},      {0xE3, "PPRINT"},
    {0xE4, "ACCEPT"},  {0xE5, "RESET"},   {0xE6, "SCRATCH"},   {0xE7, "COLOR"},
    {0xE8, "TAKE"},    {0xE9, "PAUSE"},   {0xEA, "BASE"},      {0xEB, "COPYCHR"},
    {0xEC, "CHAR"},    {0xED, "CLK"},     {0xEE, "CLS"},       {0xEF, "FILL"},
    {0xF0, "RETIME"},  {0xF1, "SOUND"},   {0xF2, "POFF"},      {0xF3, "PLIST"},
    {0xF4, "PUT"},     {0xF5, "VOLUME"},  {0xF6, "JOY"},       {0xF7, "MSB"},
    {0xF8, "LSB"},     {0xF9, "VECTOR"},  {0xFA, "RKEY"},      {0xFB, "DEC"},
    {0xFC, "HEX$"},    {0xFD, "GRAB"},    {0xFE, "DS$"}
  };  
  
  /** BASIC V4 tokens */
  private static final Object[][] BASIC_TOKENS_V4={
    {0xCC, "CONCAT"},  {0xCD, "DOPEN"},    {0xCE, "DCLOSE"},    {0xCF, "RECORD"}, 
    {0xD0, "HEADER"},  {0xD1, "COLLECT"},  {0xD2, "BACKUP"},    {0xD3, "COPY"},
    {0xD4, "APPEND"},  {0xD5, "DSAVE"},    {0xD6, "DLOAD"},     {0xD7, "CATALOG"}, 
    {0xD8, "RENAME"},  {0xD9, "SCRATCH"},  {0xDA, "DIRECTORY"}, {0xDB, "IEEE"},
    {0xDC, "SERIAL"},  {0xDD, "PARALLEL"}, {0xDE, "MONITOR"},   {0xDF, "MODEM"}
  };   
  
  /** BASIC V5 tokens */
  private static final Object[][] BASIC_TOKENS_V5={
    {0xCC, "CONCAT"},  {0xCD, "DOPEN"},   {0xCE, "DCLOSE"},    {0xCF, "RECORD"}, 
    {0xD0, "HEADER"},  {0xD1, "COLLECT"}, {0xD2, "BACKUP"},    {0xD3, "COPY"},
    {0xD4, "APPEND"},  {0xD5, "DSAVE"},   {0xD6, "DLOAD"},     {0xD7, "CATALOG"}, 
    {0xD8, "RENAME"},  {0xD9, "SCRATCH"}, {0xDA, "DIRECTORY"}, {0xDB, "DVERIFY"},
    {0xDC, "MONITOR"}, {0xDD, "REPEAT"},  {0xDE, "BELL"},      {0xDF, "COMMANDS"},
    {0xE0, "RENEW"},   {0xE1, "'"},       {0xE2, "KEY"},       {0xE3, "AUTO"},
    {0xE4, "OFF"},     {0xE5, ""},        {0xE6, "MERGE"},     {0xE7, "COLOR"},
    {0xE8, "MEM"},     {0xE9, "ENTER"},   {0xEA, "DELETE"},    {0xEB, "FIND"},
    {0xEC, "NUMBER"},  {0xED, "ELSE"},    {0xEE, "CALL"},      {0xEF, "GRAPHICS"},
    {0xF0, "ALPHA"},   {0xF1, "DMERGE"}
  };  
  
  /** BASIC Expanded tokens */
  private static final Object[][] BASIC_TOKENS_EXPANDED_V20={
    {0xCC, "RESET"},  {0xCD, "SOUND"},   {0xCE, "SLOW"},    {0xCF, "COM"}, 
    {0xD0, "MEM"},    {0xD1, "STAR("},   {0xD2, "KEY"},     {0xD3, "OFF"},
    {0xD4, "COL("},   {0xD5, "PLOT("},   {0xD6, "POP("},    {0xD7, "CHOL("}, 
    {0xD8, "CUROL("}, {0xD9, "BEEP("},   {0xDA, "PAUS("},   {0xDB, "MSAV("},
    {0xDC, "REG("},   {0xDD, "DPEK("},   {0xDE, "PDL"},     {0xDF, "JOY"},
    {0xE0, "DPOK"},   {0xE1, "DO"},      {0xE2, "UNTIL"},   {0xE3, "OLD"}
  };   
  
  /** BASIC Handy tokens */
  private static final Object[][] BASIC_TOKENS_HANDY={
    {0xCC, "MOVE"},   {0xCD, "POP"},     {0xCE, "ELSE"},    {0xCF, "VOLUME"}, 
    {0xD0, "PAUSE"},  {0xD1, "BASE"},    {0xD2, "RESET"},   {0xD3, "COPYCHR"},
    {0xD4, "COLOR"},  {0xD5, "SOUND"},   {0xD6, "FILL"},    {0xD7, "BEEP"}, 
    {0xD8, "PUT"},    {0xD9, "TAKE"},    {0xDA, "ACCEPT"},  {0xDB, "KILL"},
    {0xDC, "RTIME"},  {0xDD, "CLS"},     {0xDE, "OLD"},     {0xDF, "RKEY"},
    {0xE0, "JOY"},    {0xE1, "GRAB"}
  };  
  
  /** BASIC V8  Walrusoft tokens */
  private static final Object[][] BASIC_TOKENS_V8={
    {0xFE2F, "GROW"},    {0xFE30, "ARC"},     {0xFE31, "LINE"},       {0xFE32, "DRWMODA"},
    {0xFE33, "DRWMODB"}, {0xFE34, "PATTERN"}, {0xFE35, "BUFFER"},     {0xFE36, "STRUCT"},
    {0xFE37, "SDAT"},    {0xFE38, "TEXT"},    {0xFE39, "SCROLL"},     {0xFE3A, "WALRUS"},
    {0xFE3B, "FONT"},    {0xFE3C, "SCRDEF"},  {0xFE3D, "PIXEL"},      {0xFE3E, "MOUSE"},
    {0xFE3F, "DOT"},     {0xFE40, "SEND"},    {0xFE41, "HCOPY"},      {0xFE42, "LOGO"},
    {0xFE43, "CBRUSH"},  {0xFE44, "ANGLE"},   {0xFE45, "SCALE"},      {0xFE46, "ORIGIN"},
    {0xFE47, "VIEW"},    {0xFE48, "STORE"},   {0xFE49, "DISPLAY"},    {0xFE4A, "SCREEN"},
    {0xFE4B, "CLEAR"},   {0xFE4C, "MODE"},    {0xFE4D, "BRUSHPATRN"}, {0xFE4E, "SPHERE"},
    {0xFE4F, "CYLNDR"},  {0xFE50, "TOROID"},  {0xFE51, "SPOOL"},      {0xFE52, "DIR$"},
    {0xFE53, "SCLIP"},   {0xFE54, "STYLE"},   {0xFE55, "LSTRUCT"},    {0xFE56, "SSTRUCT"},
    {0xFE57, "PTR"},     {0xFE58, "FLASH"},   {0xFE59, "ZOOM"},       {0xFE5A, "MOVSPR"},    
  };  
  
  
  
  /** Cbm keys 0xA0..0xDF */
  private static final String CBMKEYS[] = {
    "SHIFT-SPACE", "CBM-K",       "CBM-I",   "CBM-T",   "CBM-@",   "CBM-G",   "CBM-+",   "CBM-M",
    "CBM-POUND",   "SHIFT-POUND", "CBM-N",   "CBM-Q",   "CBM-D",   "CBM-Z",   "CBM-S",   "CBM-P",
    "CBM-A",       "CBM-E",       "CBM-R",   "CBM-W",   "CBM-H",   "CBM-J",   "CBM-L",   "CBM-Y",
    "CBM-U",       "CBM-O",       "SHIFT-@", "CBM-F",   "CBM-C",   "CBM-X",   "CBM-V",   "CBM-B",
    "SHIFT-*",     "SHIFT-A",     "SHIFT-B", "SHIFT-C", "SHIFT-D", "SHIFT-E", "SHIFT-F", "SHIFT-G",
    "SHIFT-H",     "SHIFT-I",     "SHIFT-J", "SHIFT-K", "SHIFT-L", "SHIFT-M", "SHIFT-N", "SHIFT-O",
    "SHIFT-P",     "SHIFT-Q",     "SHIFT-R", "SHIFT-S", "SHIFT-T", "SHIFT-U", "SHIFT-V", "SHIFT-W",
    "SHIFT-X",     "SHIFT-Y",     "SHIFT-Z", "SHIFT-+", "CBM--",   "SHIFT--", "SHIFT-^", "CBM-*"
};
  
  
  
  /**
   * Decode the given char to printable string
   * 
   * @param val the val to decode
   * @param basicType the Basic type
   * @return the return string
   */
  private String decodeChar(int val, BasicType basicType) {
    switch (val) {
      case 0x03:
        return "{stop}";
      case 0x05:
        return "{white}";        
      case 0x0A:
      case 0x0D:  // CBM Carriage return
        return "\\n";
      case 0x0E:
        return "{lower case}";    
      case 0x11:
        return "{down}";
      case 0x12:
        return "{rvon}";
      case 0x13:
        return "{home}";  
      case 0x14:
        return "{del}";  
      case 0x1C:
        return "{red}";   
      case 0x1D:
        return "{right}";    
      case 0x1E:
        return "{green}"; 
      case 0x1F:
        return "{blue}";   
      case 0x40:
        return "@";
      case 0x5B:
        return "[";
      case 0x5C:
        return "\\";
      case 0x5D:
        return "]";
      case 0x5E:
        return "^";
      case 0x5F:
        return "_";
      case 0x85:
        return "{F1}";  
      case 0x86:
        return "{F3}";    
      case 0x87:
        return "{F5}";    
      case 0x88:
        return "{F7}";    
      case 0x89:
        return "{F2}";    
      case 0x8A:
        return "{F4}";    
      case 0x8B:
        return "{F6}";    
      case 0x8C:
        if (basicType==BasicType.BASIC_V3_5) return "{esc}";
        else return "{F8}";
      case 0x8D:
        return "{shift return}";      
      case 0x8E:
        return "{uppercase}";        
      case 0x90:
        return "{black}";    
      case 0x91:
        return "{up}";  
      case 0x93:
        return "{clr}";
      case 0x94:
        return "{inst}"; 
      case 0x95:
        return "{brown}";   
      case 0x96:
        if (basicType==BasicType.BASIC_V3_5) return "{yellow-green}";
        else return "{pink}";   
      case 0x97:
        if (basicType==BasicType.BASIC_V3_5) return "{pink}";
        else return "{dark grey}";   
      case 0x98:
        if (basicType==BasicType.BASIC_V3_5) return "{blue-green}";
        else return "{medium gray}";  
      case 0x99:
        if (basicType==BasicType.BASIC_V3_5) return "{light blue}"; 
        else return "{light green}"; 
      case 0x9A:
        if (basicType==BasicType.BASIC_V3_5) return "{dark blue}"; 
        else return "{light blue}"; 
      case 0x9B:
        if (basicType==BasicType.BASIC_V3_5) return "{light green}"; 
        else return "{light gray}";         
      case 0x9C:
        return "{purple}";  
      case 0x9d:
        return "{left}";
      case 0x9E:
        return "{yellow}";
      case 0x9F:
        return "{cyan}";
        
      case 0x7E:
      case 0xDE:
      case 0xFF:
        return ""+(char)(0x7E); // pi greco
    }
    
    if (val>=0xA0 && val<=0xE0) return "{"+CBMKEYS[val-0xA0]+"}";
    return ""+(char)val;
  }
  
  /**
   * Decode the token to the given BASIC, or the normal char
   * 
   * @param val the token to convert
   * @param prevVal the previous token (or 0)
   * @param basicType the type of BASIC
   * @param quote true if this is inside a quoted string
   * @return the converted token if present or the printable char
   */
  private String decodeToken(int val, int prevVal, BasicType basicType, boolean quote) {
    String res=""+decodeChar(val, basicType); // to modify for printable char
    
    if (quote) return ""+res;
    
    // skip if is first opcode is of some double bytes opcode
    if (val==0x64 && basicType.op64) return "";
    if (val==0xCE && basicType.opCE) return "";
    if (val==0xFE && basicType.opFE) return "";
    
    // use old opcode if it is of two bytes opcode
    if (prevVal==0x64 && basicType.op64) val=0x6400+val;
    if (prevVal==0xCE && basicType.opCE) val=0xCE00+val;
    if (prevVal==0xFE && basicType.opFE) val=0xFE00+val;
    
    Hashtable<Integer, String> hash=basicType.getHashBasic();       
    
    if (hash.containsKey(val)) return hash.get(val);
    return ""+res;
  }
  
  /**
   * Return a detokenized BASIC commands as readable string
   * 
   * @param basicType the type of BASIC to decode
   * @return the readable BASIC string
   */
  public String detokenizedCommand(BasicType basicType) {
    if (list==null || list.isEmpty()) return ""; // avoid bad usage 
    
    String result="";
    
    try {
      // address of next BASIC command
      int nextAddress=list.get(0)+list.get(1)*256;
            
      if (nextAddress==0) {
        list.clear();
        return "<- end of BASIC program ->";
      }
      
      // BASIC line of this instruction
      int line=list.get(2)+list.get(3)*256;
      
      result+=line+" ";

      int val;      
      int prevVal=0;
      boolean quote=false;  // true when we are quotating a string
      
      // process all bytes
      for (int i=4; i<list.size(); i++) {
        val=list.get(i);
        
        // ending of command
        if (val==0) break;
        
        if (val=='"') quote=!quote;
        result+=decodeToken(val, prevVal, basicType, quote);
        prevVal=val;
      }
      
    } catch (Exception e) {
        // avoid malformed given data that break the program
        System.err.println(e);
      }
    
    list.clear();
    return result;
  }
  
  /**
   * Clear actual list
   */
  public void clear() {
    list.clear();
  }
  
  /**
   * Add a value into the list
   * 
   * @param val the value to add
   */
  public void add(byte val) {
    list.add(val & 0xFF);
  }
  
  /**
   * Get the actual size of the list
   * 
   * @return the list size
   */
  public int size() {
    return list.size();
  }
  
  /**
   * True if the command flow is complete
   * 
   * @return true if it is completed
   */
  public boolean isComplete() {
    int size=list.size();
    
    if (size<=1) return false;
    
    if (size==2 && list.get(0)==0x00 && list.get(1)==0x00) return true;
    
    if (size<=4) return false;
    
    if (list.get(size-1)==0x00) return true;
    
    return false;
  }
}
