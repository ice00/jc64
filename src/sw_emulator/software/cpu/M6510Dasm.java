/**
 * @(#)M6510Dasm.java 1999/08/20
 *
 * ICE Team Free Software Group
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

package sw_emulator.software.cpu;

import sw_emulator.math.Unsigned;
import java.util.Locale;
import sw_emulator.software.Assembler;
import sw_emulator.software.MemoryDasm;
import sw_emulator.swing.main.Constant;
import sw_emulator.swing.main.Option;

/**
 * Disasseble the M6510 code instructions
 * This class implements the </code>disassembler</code> interface, so it must
 * disassemble one instruction and comment it.
 * 
 * Note that the instruction comment are provided only for giving information
 * about undocument instruction.
 * Also some methods are provided for changing the name of undocument
 * instructions, because their names are not standard.
 * Finally, bytes declaration is performed using SIDLN memory states.
 *
 * @author Ice
 * @version 1.02 16/10/2003
 */
public class M6510Dasm implements disassembler {
  // mode of undocument command are called
  public static final byte MODE1=1; // mode use by John west and Marko M"akel"a
  public static final byte MODE2=2; // mode use by Juergen Buchmueller
  public static final byte MODE3=3; // mode use by Adam Vardy

  // legal instruction
  public static final byte M_ADC=0;
  public static final byte M_AND=1;
  public static final byte M_ASL=2;
  public static final byte M_BCC=3;
  public static final byte M_BCS=4;
  public static final byte M_BEQ=5;
  public static final byte M_BIT=6;
  public static final byte M_BMI=7;
  public static final byte M_BNE=8;
  public static final byte M_BPL=9;
  public static final byte M_BRK=10;
  public static final byte M_BVC=11;
  public static final byte M_BVS=12;
  public static final byte M_CLC=13;
  public static final byte M_CLD=14;
  public static final byte M_CLI=15;
  public static final byte M_CLV=16;
  public static final byte M_CMP=17;
  public static final byte M_CPX=18;
  public static final byte M_CPY=19;
  public static final byte M_DEC=20;
  public static final byte M_DEX=21;
  public static final byte M_DEY=22;
  public static final byte M_EOR=23;
  public static final byte M_INC=24;
  public static final byte M_INX=25;
  public static final byte M_INY=26;
  public static final byte M_JMP=27;
  public static final byte M_JSR=28;
  public static final byte M_LDA=29;
  public static final byte M_LDX=30;
  public static final byte M_LDY=31;
  public static final byte M_LSR=32;
  public static final byte M_NOP=33;
  public static final byte M_ORA=34;
  public static final byte M_PHA=35;
  public static final byte M_PHP=36;
  public static final byte M_PLA=37;
  public static final byte M_PLP=38;
  public static final byte M_ROL=39;
  public static final byte M_ROR=40;
  public static final byte M_RTI=41;
  public static final byte M_RTS=42;
  public static final byte M_SBC=43;
  public static final byte M_SEC=44;
  public static final byte M_SED=45;
  public static final byte M_SEI=46;
  public static final byte M_STA=47;
  public static final byte M_STX=48;
  public static final byte M_STY=49;
  public static final byte M_TAX=50;
  public static final byte M_TAY=51;
  public static final byte M_TSX=52;
  public static final byte M_TXA=53;
  public static final byte M_TXS=54;
  public static final byte M_TYA=55;
  // undocument instruction
  public static final byte M_ANC=56;
  public static final byte M_ANE=57;
  public static final byte M_ARR=58;
  public static final byte M_ASR=59;
  public static final byte M_DCP=60;
  public static final byte M_ISB=61;
  public static final byte M_JAM=62;
  public static final byte M_LAS=63;
  public static final byte M_LAX=64;
  public static final byte M_LXA=65;
  public static final byte M_NOP0=66;
  public static final byte M_NOP1=67;
  public static final byte M_NOP2=68;
  public static final byte M_RLA=69;
  public static final byte M_RRA=70;
  public static final byte M_SAX=71;
  public static final byte M_SBX=72;
  public static final byte M_SHA=73;
  public static final byte M_SHX=74;
  public static final byte M_SHY=75;
  public static final byte M_SHS=76;
  public static final byte M_SLO=77;
  public static final byte M_SRE=78;
  public static final byte M_USBC=79;

  // addressing mode
  public static final byte A_NUL=0;  // nothing else
  public static final byte A_ACC=1;  // accumulator
  public static final byte A_IMP=2;  // implicit
  public static final byte A_IMM=3;  // immediate
  public static final byte A_ZPG=4;  // zero page
  public static final byte A_ZPX=5;  // zero page x
  public static final byte A_ZPY=6;  // zero page y
  public static final byte A_ABS=7;  // absolute
  public static final byte A_ABX=8;  // absolute x
  public static final byte A_ABY=9;  // absolute y
  public static final byte A_REL=10; // relative
  public static final byte A_IND=11; // indirect
  public static final byte A_IDX=12; // indirect x
  public static final byte A_IDY=13; // indirect y

  /** Actual case to use for text */
  public boolean upperCase=true;
  
  /** Contains the mnemonics of instructions */
  public static final String[] mnemonics={
    // legal instruction first:
    "ADC", 
    "AND",
    "ASL",
    "BCC",
    "BCS",
    "BEQ",
    "BIT",
    "BMI",
    "BNE",
    "BPL",
    "BRK",
    "BVC",
    "BVS",
    "CLC",
    "CLD",
    "CLI",
    "CLV",
    "CMP",
    "CPX",
    "CPY",
    "DEC",
    "DEX",
    "DEY",
    "EOR",
    "INC",
    "INX",
    "INY",
    "JMP",
    "JSR",
    "LDA",
    "LDX",
    "LDY",
    "LSR",
    "NOP",
    "ORA",
    "PHA",
    "PHP",
    "PLA",
    "PLP",
    "ROL",
    "ROR",
    "RTI",
    "RTS",
    "SBC",
    "SEC",
    "SED",
    "SEI",
    "STA",
    "STX",
    "STY",
    "TAX",
    "TAY",
    "TSX",
    "TXA",
    "TXS",
    "TYA",
    // undocument instruction
    "ANC",
    "ANE",
    "ARR",
    "ASR",
    "DCP",
    "ISB",
    "JAM",
    "LAS",
    "LAX",
    "LXA",
    "NOOP",
    "NOOP",
    "NOOP",
    "RLA",
    "RRA",
    "SAX",
    "SBX",
    "SHA",
    "SHX",
    "SHY",
    "SHS",
    "SLO",
    "SRE",
    "USBC"
  };

  /** Contains the mnemonics reference for the instruction */
  public static final byte[] tableMnemonics={
    M_BRK, M_ORA, M_JAM, M_SLO, M_NOP1,M_ORA, M_ASL, M_SLO,  // 00
    M_PHP, M_ORA, M_ASL, M_ANC, M_NOP2,M_ORA, M_ASL, M_SLO,
    M_BPL, M_ORA, M_JAM, M_SLO, M_NOP1,M_ORA, M_ASL, M_SLO,
    M_CLC, M_ORA, M_NOP0,M_SLO, M_NOP2,M_ORA, M_ASL, M_SLO,
    M_JSR, M_AND, M_JAM, M_RLA, M_BIT, M_AND, M_ROL, M_RLA,  // 20
    M_PLP, M_AND, M_ROL, M_ANC, M_BIT, M_AND, M_ROL, M_RLA,
    M_BMI, M_AND, M_JAM, M_RLA, M_NOP1,M_AND, M_ROL, M_RLA,
    M_SEC, M_AND, M_NOP0,M_RLA, M_NOP2,M_AND, M_ROL, M_RLA,
    M_RTI, M_EOR, M_JAM, M_SRE, M_NOP1,M_EOR, M_LSR, M_SRE,  // 40
    M_PHA, M_EOR, M_LSR, M_ASR, M_JMP, M_EOR, M_LSR, M_SRE,
    M_BVC, M_EOR, M_JAM, M_SRE, M_NOP1,M_EOR, M_LSR, M_SRE,
    M_CLI, M_EOR, M_NOP0,M_SRE, M_NOP2,M_EOR, M_LSR, M_SRE,
    M_RTS, M_ADC, M_JAM, M_RRA, M_NOP1,M_ADC, M_ROR, M_RRA,  // 60
    M_PLA, M_ADC, M_ROR, M_ARR, M_JMP, M_ADC, M_ROR, M_RRA,
    M_BVS, M_ADC, M_JAM, M_RRA, M_NOP1,M_ADC, M_ROR, M_RRA,
    M_SEI, M_ADC, M_NOP0,M_RRA, M_NOP2,M_ADC, M_ROR, M_RRA,
    M_NOP1,M_STA, M_NOP1,M_SAX, M_STY, M_STA, M_STX, M_SAX,  // 80
    M_DEY, M_NOP1,M_TXA, M_ANE, M_STY, M_STA, M_STX, M_SAX,
    M_BCC, M_STA, M_JAM, M_SHA, M_STY, M_STA, M_STX, M_SAX,
    M_TYA, M_STA, M_TXS, M_SHS, M_SHY, M_STA, M_SHX, M_SHA,
    M_LDY, M_LDA, M_LDX, M_LAX, M_LDY, M_LDA, M_LDX, M_LAX,  // A0
    M_TAY, M_LDA, M_TAX, M_LXA, M_LDY, M_LDA, M_LDX, M_LAX,
    M_BCS, M_LDA, M_JAM, M_LAX, M_LDY, M_LDA, M_LDX, M_LAX,
    M_CLV, M_LDA, M_TSX, M_LAS, M_LDY, M_LDA, M_LDX, M_LAX,
    M_CPY, M_CMP, M_NOP1,M_DCP, M_CPY, M_CMP, M_DEC, M_DCP,  // C0
    M_INY, M_CMP, M_DEX, M_SBX, M_CPY, M_CMP, M_DEC, M_DCP,
    M_BNE, M_CMP, M_JAM, M_DCP, M_NOP1,M_CMP, M_DEC, M_DCP,
    M_CLD, M_CMP, M_NOP0,M_DCP, M_NOP2,M_CMP, M_DEC, M_DCP,
    M_CPX, M_SBC, M_NOP1,M_ISB, M_CPX, M_SBC, M_INC, M_ISB,  // E0
    M_INX, M_SBC, M_NOP, M_USBC,M_CPX, M_SBC, M_INC, M_ISB,
    M_BEQ, M_SBC, M_JAM, M_ISB, M_NOP1,M_SBC, M_INC, M_ISB,
    M_SED, M_SBC, M_NOP0,M_ISB, M_NOP2,M_SBC, M_INC, M_ISB
  };

  /** Contains the modes for the instruction */
  public static final byte[] tableModes={
    A_IMP, A_IDX, A_NUL, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,  // 00
    A_IMP, A_IMM, A_ACC, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_ABS, A_IDX, A_NUL, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,  // 20
    A_IMP, A_IMM, A_ACC, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_IMP, A_IDX, A_NUL, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,  // 40
    A_IMP, A_IMM, A_ACC, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_IMP, A_IDX, A_NUL, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,  // 60
    A_IMP, A_IMM, A_ACC, A_IMM, A_IND, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_IMM, A_IDX, A_IMM, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,  // 80
    A_IMP, A_IMM, A_IMP, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPY, A_ZPY,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABY, A_ABY,
    A_IMM, A_IDX, A_IMM, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,  // A0
    A_IMP, A_IMM, A_IMP, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPY, A_ZPY,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABY, A_ABY,
    A_IMM, A_IDX, A_IMM, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,  // C0
    A_IMP, A_IMM, A_IMP, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_IMM, A_IDX, A_IMM, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,  // E0
    A_IMP, A_IMM, A_IMP, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX
  };
    
  /** Contains the bytes used for the instruction */
  public static final byte[] tableSize={
    1, 2, 1, 2, 2, 2, 2, 2, // 00
    1, 2, 1, 2, 3, 3, 3, 3, 
    2, 2, 1, 2, 2, 2, 2, 2, 
    1, 3, 1, 3, 3, 3, 3, 3,
    3, 2, 1, 2, 2, 2, 2, 2, // 20
    1, 2, 1, 2, 3, 3, 3, 3, 
    2, 2, 1, 2, 2, 2, 2, 2,
    1, 3, 1, 3, 3, 3, 3, 3,
    1, 2, 1, 2, 2, 2, 2, 2, // 40
    1, 2, 1, 2, 3, 3, 3, 3,
    2, 2, 1, 2, 2, 2, 2, 2,
    1, 3, 1, 3, 3, 3, 3, 3,
    1, 2, 1, 2, 2, 2, 2, 2, // 60
    1, 2, 1, 1, 3, 3, 3, 3,
    2, 2, 1, 2, 2, 2, 2, 2,
    1, 3, 1, 3, 3, 3, 3, 3,
    2, 2, 2, 2, 2, 2, 2, 2, // 80
    1, 2, 1, 2, 3, 3, 3, 3,
    2, 2, 1, 2, 2, 2, 2, 2,
    1, 3, 1, 3, 3, 3, 3, 3,
    2, 2, 2, 2, 2, 2, 2, 2, // A0
    1, 2, 1, 2, 3, 3, 3, 3,
    2, 2, 1, 2, 2, 2, 2, 2,
    1, 3, 1, 3, 3, 3, 3, 3,
    2, 2, 2, 2, 2, 2, 2, 2, // C0
    1, 2, 1, 2, 3, 3, 3, 3,
    2, 2, 1, 2, 2, 2, 2, 2,
    1, 3, 1, 3, 3, 3, 3, 3,
    2, 2, 2, 2, 2, 2, 2, 2, // E0
    1, 2, 1, 2, 3, 3, 3, 3,
    2, 2, 1, 2, 2, 2, 2, 2,
    1, 3, 1, 3, 3, 3, 3, 3    
  };

  /** Type of instruction (used to create comment) */
  protected int iType=M_JAM;

  /** Type of addressing used by instruction (used to create comment) */
  protected int aType=A_NUL;

  /** Value of address (used to create comment) */
  protected long addr=0;

  /** Value of operation (used to create comment) */
  protected long value=0;

  /** Last position pointer in buffer */
  protected int pos=0;

  /** Last program counter value */
  protected long pc=0;
  
  /** Memory dasm to use */
  MemoryDasm[] memory;
  
  /** Assembler manager */
  protected Assembler assembler=new Assembler();
  
  /** Option to use */
  protected Option option;    
  
  /** Constant to use */
  protected Constant constant;  
  
  /** String builder global to reduce GC call */
  private final StringBuilder result=new StringBuilder ("");     
  
  /**
   * Set the memory dasm to use
   * 
   * @param memory the memory dasm
   */
  public void setMemory(MemoryDasm[] memory) {
    this.memory=memory;  
  }
  
  /**
   * Set the constant to use
   * 
   * @param constant the constants
   */
  public void setConstant(Constant constant) {
    this.constant=constant;  
  }
  
  /**
   * Set the option to use
   * 
   * @param option the option to use
   * @param assembler the assembler to use
   */
  public void setOption(Option option, Assembler assembler) {
    this.option=option;
    this.assembler=assembler;        
  }

  /**
   * Return the mnemonic assembler instruction rapresent by passed code bytes.
   *
   * @param buffer the buffer containg the data
   * @param pos the actual position in the buffer
   * @param pc the program counter value associated to the bytes being address
   *           by the <code>pos</code> in the buffer
   * @return a string menemonic rapresentation of instruction
   */
  @Override
  public String dasm(byte[] buffer, int pos, long pc) {
    String result="";          // result disassemble string
    int op=Unsigned.done(buffer[pos++]); // instruction opcode

    iType=(int)tableMnemonics[op];   // store the type for creating comment
    
    if (upperCase) result=mnemonics[iType];
    else result=mnemonics[iType].toLowerCase();
        
    if (result.length()==3) result+=" ";
    result+=" ";

    switch (tableModes[op]) {
      case A_NUL:     // nothing
        aType=A_NUL;
        pc++;
        break;
      case A_ACC:     // accumulator
        aType=A_ACC;
        ///result+="A";
        pc++;
        break;
      case A_IMP:     // implicit
        aType=A_IMP;
        pc++;
        break;
      case A_IMM:     // immediate
        aType=A_IMM;
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+="#"+getLabelImm(pc+1, value);
        pc+=2;
        break;
      case A_ZPG:     // zero page
        aType=A_ZPG;
        if (pos<buffer.length) addr=Unsigned.done(buffer[pos++]);
        else addr=-1;
        result+=getLabelZero(addr);
        pc+=2;
        break;
      case A_ZPX:     // zero page x
        aType=A_ZPX;
        if (pos<buffer.length) addr=Unsigned.done(buffer[pos++]);
        else addr=-1;
        result+=getLabelZero(addr)+(upperCase? ",X": ",x");
        pc+=2;
        break;
      case A_ZPY:     // zero page y
        aType=A_ZPY;
        if (pos<buffer.length) addr=Unsigned.done(buffer[pos++]);
        else addr=-1;
        result+=getLabelZero(addr)+(upperCase? ",Y": ",y");
        
        pc+=2;
        break;
      case A_ABS:     // absolute
        aType=A_ABS;
        if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        else addr=-1;
        
        result+=getLabel(addr);
        setLabel(addr);
        setLabelPlus(pc,1);
        setLabelPlus(pc,2);
        //setLabelMinus(pc,1);
        //setLabelMinus(pc,2);        
        
        pos++;
        pc+=3;    
        break;
      case A_ABX:     // absolute x
        aType=A_ABX;
        if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        else addr=-1;
        
        result+=getLabel(addr)+(upperCase? ",X": ",x");
        setLabel(addr);
        setLabelPlus(pc,1);
        setLabelPlus(pc,2);
        //setLabelMinus(pc,1);
        //setLabelMinus(pc,2); 
        
        pos++;        
        pc+=3;
        break;
      case A_ABY:     // absolute y
        aType=A_ABS;
        if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        else addr=-1;
        pos++;
       
        result+=getLabel(addr)+(upperCase? ",Y": ",y");
        setLabel(addr);
        setLabelPlus(pc,1);
        setLabelPlus(pc,2);
        //setLabelMinus(pc,1);
        //setLabelMinus(pc,2); 
        
        pc+=3;
        break;
      case A_REL:     // relative
        aType=A_REL;
        if (pos<buffer.length) addr=pc+buffer[pos++]+2;
        else addr=-1;   
        
        result+=getLabel(addr);
        setLabel(addr);
        
        pc+=2;
        break;
      case A_IND:     // indirect
        aType=A_IND;
        if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        else addr=-1;
        pos++;
        result+="("+getLabel(addr)+")";
        setLabel(addr);
        pc+=3;
        break;
      case A_IDX:     // indirect x
        aType=A_IDX;
        if (pos<buffer.length) addr=Unsigned.done(buffer[pos++]);
        else addr=-1;
        result+="("+getLabelZero(addr)+(upperCase? ",X)": ",x)");
        pc+=2;
        break;
      case A_IDY:     // indirect y
        aType=A_IDY;
        if (pos<buffer.length) addr=Unsigned.done(buffer[pos++]);
        else addr=-1;
        result+="("+getLabelZero(addr)+(upperCase? "),Y": "),y");
        pc+=2;
        break;
    }
    this.pc=pc;
    this.pos=pos;   
    return result;
  }

  /**
   * Return the mnemonic assembler instruction rapresent by passed code bytes,
   * using last position an program counter.
   *
   * @param buffer the buffer containg the data 
   * @return a string menemonic rapresentation of instruction
   */
  public String dasm(byte[] buffer) {
    return dasm(buffer, pos, pc);
  }

  /**
   * Comment and Disassemble a region of the buffer
   *
   * @param buffer the buffer containing the code
   * @param start the start position in buffer
   * @param end the end position in buffer
   * @param pc the programn counter for start position 
   * @return a string rapresentation of disassemble with comment
   */
  public String cdasm(byte[] buffer, int start, int end, long pc) {    
    String tmp;                  // local temp string
    String tmp2;                 // local temp string
    MemoryDasm mem;              // memory dasm
    MemoryDasm memRel;           // memory related
    MemoryDasm memRel2;          // memory related of second kind
    int pos=start;               // actual position in buffer
    boolean isCode=true;         // true if we are decoding an instruction
    boolean wasGarbage=false;    // true if we were decoding garbage
        
    result.setLength(0);
    result.append(addConstants());
    
    this.pos=pos;
    this.pc=pc;
    while (pos<=end | pos<start) { // verify also that don't circle in the buffer        
      mem=memory[(int)pc];
      isCode=((mem.isCode || (!mem.isData && option.useAsCode)) && !mem.isGarbage);
        
        if (isCode) {    
          assembler.flush(result);
          
          // must put the org if we start from an garbage area
          if (wasGarbage) {
            wasGarbage=false;
            assembler.setOrg(result, (int)pc);
          }
            
          // add block if user declare it
          if (mem.userBlockComment!=null && !"".equals(mem.userBlockComment)) {  
            assembler.setBlockComment(result, mem);
          }   
            
          // add the label if it was declared by dasm or user           
          //if (mem.userLocation!=null && !"".equals(mem.userLocation)) result.append(mem.userLocation).append(":\n");
          //else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) result.append(mem.dasmLocation).append(":\n");
          if ((mem.userLocation!=null && !"".equals(mem.userLocation)) || 
             (mem.dasmLocation!=null && !"".equals(mem.dasmLocation))) {
            assembler.setLabel(result, mem);
            result.append("\n");
          }  
          
          // this is an instruction
          tmp=dasm(buffer); 
          tmp2=ShortToExe((int)pc)+"  "+ByteToExe(Unsigned.done(buffer[pos]));
          if (this.pc-pc==2) {
            if (pos+1<buffer.length) tmp2+=" "+ByteToExe(Unsigned.done(buffer[pos+1]));
            else tmp2+=" ??";
          }
          if (this.pc-pc==3) {
            if (pos+2<buffer.length) tmp2+=" "+ByteToExe(Unsigned.done(buffer[pos+1]))+
                                           " "+ByteToExe(Unsigned.done(buffer[pos+2]));
            else tmp2+=" ?????";
          }    
          for (int i=tmp2.length(); i<17; i++) // insert spaces
            tmp2+=" ";
          tmp=tmp2+tmp;
          tmp2="";
          for (int i=tmp.length(); i<43; i++) // insert spaces
            tmp2+=" ";
          result.append(tmp).append(tmp2);
          
          tmp2=dcom();   
          
          // if there is a user comment, then use it
          if (mem.userComment!=null) result.append(" ").append(mem.userComment).append("\n"); 
          else result.append(" ").append(tmp2).append("\n");  
          
          // always add a carriage return after a RTS, RTI or JMP
          if (iType==M_JMP || iType==M_RTS || iType==M_RTI) result.append("\n");    
          
          if (pc>=0) {
            // rememeber this dasm automatic comment  
            if (!"".equals(tmp2)) mem.dasmComment=tmp2;
            else mem.dasmComment=null;
          }         
          
          pos=this.pos;
          pc=this.pc;
        } else 
            if (mem.isGarbage) {
              assembler.flush(result);
              wasGarbage=true;
              pos++;
              pc++;              
            
              this.pos=pos;
              this.pc=pc; 
            } 
          else {    
            // must put the org if we start from an garbage area
            if (wasGarbage) {
              wasGarbage=false;
              assembler.setOrg(result, (int)pc);
            }            
            
            memRel=mem.related!=-1 ? memory[mem.related & 0xFFFF]: null;
            if (memRel!=null) memRel2=memRel.related!=-1 ? memory[memRel.related & 0xFFFF]: null;
            else memRel2=null;
            assembler.putValue(result, mem, memRel, memRel2); 
            
            pos++;
            pc++;
            
            this.pos=pos;
            this.pc=pc;            
          }  
        
    } 
    assembler.flush(result);
    return result.toString();
  }
  
  /**
   * Comment and Disassemble a region of the buffer as source
   *
   * @param buffer the buffer containing the code
   * @param start the start position in buffer
   * @param end the end position in buffer
   * @param pc the programn counter for start position 
   * @return a string rapresentation of disasemble with comment
   */
  public String csdasm(byte[] buffer, int start, int end, long pc) {
    String tmp;                  // local temp string
    String tmp2;                 // local temp string
    MemoryDasm mem;              // memory dasm
    MemoryDasm memRel;           // memory related
    MemoryDasm memRel2;          // memory related of second kind
    int pos=start;               // actual position in buffer
    boolean isCode=true;         // true if we are decoding an instruction
    boolean wasGarbage=false;    // true if we were decoding garbage
         
    result.setLength(0);
    result.append(addConstants());
    
    this.pos=pos;
    this.pc=pc;
    while (pos<=end | pos<start) { // verify also that don't circle in the buffer        
      mem=memory[(int)pc];
      isCode=((mem.isCode || (!mem.isData && option.useAsCode)) && !mem.isGarbage);
        
        if (isCode) {        
          assembler.flush(result);
          
          // must put the org if we start from an garbage area
          if (wasGarbage) {
            wasGarbage=false;
            assembler.setOrg(result, (int)pc);
          }             
          
          // add block if user declare it    
          if (mem.userBlockComment!=null && !"".equals(mem.userBlockComment)) {  
            assembler.setBlockComment(result, mem);
          }          
              
          if ((mem.userLocation!=null && !"".equals(mem.userLocation)) || 
             (mem.dasmLocation!=null && !"".equals(mem.dasmLocation))) {
            assembler.setLabel(result, mem);
            if (option.labelOnSepLine) result.append("\n");
          }  
          
          // this is an instruction
          tmp=dasm(buffer); 
  
          result.append(getInstrSpacesTabs(mem)).append(tmp).append(getInstrCSpacesTabs(tmp.length()));
          
          tmp2=dcom();   
          
          // if there is a user comment, then use it
          assembler.setComment(result, mem);
          
          // always add a carriage return after a RTS, RTI or JMP
          if (iType==M_JMP || iType==M_RTS || iType==M_RTI) result.append("\n");          
          
          if (pc>=0) {
            // rememeber this dasm automatic comment  
            if (!"".equals(tmp2)) mem.dasmComment=tmp2;
            else mem.dasmComment=null;
          }         
          
          pos=this.pos;
          pc=this.pc;
        } else if (mem.isGarbage) {
              assembler.flush(result);
              wasGarbage=true;
              pos++;
              pc++;
                          
              this.pos=pos;
              this.pc=pc; 
            } 
          else { 
            // must put the org if we start from an garbage area
            if (wasGarbage) {                
              wasGarbage=false;
              assembler.setOrg(result, (int)pc);
            }   
            
            memRel=mem.related!=-1 ? memory[mem.related & 0xFFFF]: null;
            if (memRel!=null) memRel2=memRel.related!=-1 ? memory[memRel.related & 0xFFFF]: null;
            else memRel2=null;
            assembler.putValue(result, mem, memRel, memRel2);            
            
            pos++;
            pc++;
            
            this.pos=pos;
            this.pc=pc;            
          }  
        
    } 
    assembler.flush(result);
    return result.toString();
  }  

  /**
   * Return a comment string for the passed instruction
   *
   * @param iType the type of instruction
   * @param aType the type of addressing used by the instruction
   * @param addr the address value (if needed by this istruction type)
   * @param value the operation value (if needed by this instruction)
   * @return a comment string
   */
  @Override
  public String dcom(int iType, int aType, long addr, long value) {
    switch (iType) {
      case M_SLO:
      case M_NOP0:
      case M_NOP1:
      case M_NOP2:
      case M_RLA:
      case M_SRE:
      case M_RRA:
      case M_SAX:
      case M_LAX:
      case M_DCP:
      case M_ISB:
      case M_USBC:
        return "Undocument command";
      case M_ANC:
      case M_ASR:
      case M_ARR:
      case M_ANE:
      case M_SHA:
      case M_SHS:
      case M_SHY:
      case M_SHX:
      case M_SBX:
      case M_LXA:
      case M_LAS:
        return "Unusual operation";
      case M_JAM:
        return "Illegal instruction";
    }
    return "";
  }

  /**
   * Return a comment string for the last instruction
   *
   * @return a comment string
   */
  public String dcom() {
    return dcom(iType, aType, addr, value);
  }

  /**
   * Convert a unsigned byte (containing in a int) to Exe upper case 2 chars
   *
   * @param value the byte value to convert
   * @return the exe string rapresentation of byte
   */
  protected String ByteToExe(int value) {
    int tmp=value;
    
    if (value<0) return "??";
    
    String ret=Integer.toHexString(tmp);
    if (ret.length()==1) ret="0"+ret;
    return ret.toUpperCase(Locale.ENGLISH);
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
   * Set the mode of using mnemonics in the disassembler
   * Available modes are <code>MODE1</code>, </code>MODE2</code>,
   * <code>MODE3</code>.
   *
   * @param mode the type of mode to use
   */
  public void setMode(byte mode) {
    switch (mode) {
      case MODE1:
        mnemonics[M_ANE]="ANE";
        mnemonics[M_ASR]="ASR";
        mnemonics[M_DCP]="DCP";
        mnemonics[M_ISB]="ISB";
        mnemonics[M_JAM]="JAM";
        mnemonics[M_LAS]="LAS";
        mnemonics[M_LXA]="LXA";
        mnemonics[M_NOP0]="NOOP";
        mnemonics[M_NOP1]="NOOP";
        mnemonics[M_NOP2]="NOOP";
        mnemonics[M_SAX]="SAX";
        mnemonics[M_SBX]="SBX";
        mnemonics[M_SHA]="SHA";
        mnemonics[M_SHX]="SHX";
        mnemonics[M_SHY]="SHY";
        mnemonics[M_SHS]="SHS";
        mnemonics[M_SLO]="SLO";
        mnemonics[M_SRE]="SRE";
        break;
      case MODE2:
        mnemonics[M_ANE]="AXA";
        mnemonics[M_ASR]="ASR";
        mnemonics[M_DCP]="DCP";
        mnemonics[M_ISB]="ISC";
        mnemonics[M_JAM]="JAM";
        mnemonics[M_LAS]="AST";
        mnemonics[M_LXA]="LXA";
        mnemonics[M_NOP0]="NOP";
        mnemonics[M_NOP1]="DOP";
        mnemonics[M_NOP2]="TOP";
        mnemonics[M_SAX]="SAX";
        mnemonics[M_SBX]="ASX";
        mnemonics[M_SHA]="SAH";
        mnemonics[M_SHX]="SXH";
        mnemonics[M_SHY]="SYH";
        mnemonics[M_SHS]="SSH";
        mnemonics[M_SLO]="SLO";
        mnemonics[M_SRE]="SRE";
        break;
      case MODE3:
        mnemonics[M_ANE]="XAA";
        mnemonics[M_ASR]="ALR";
        mnemonics[M_DCP]="DCM";
        mnemonics[M_ISB]="INS";
        mnemonics[M_JAM]="HLT";
        mnemonics[M_LAS]="LAS";
        mnemonics[M_LXA]="OAL";
        mnemonics[M_NOP0]="NOP";
        mnemonics[M_NOP1]="SKB";
        mnemonics[M_NOP2]="SKW";
        mnemonics[M_SAX]="AXS";
        mnemonics[M_SBX]="SAX";  // name conflict with other modes
        mnemonics[M_SHA]="AXA";        
        mnemonics[M_SHX]="XAS";
        mnemonics[M_SHY]="SAY";        
        mnemonics[M_SHS]="TAS";
        mnemonics[M_SLO]="ASO";        
        mnemonics[M_SRE]="LSE";
    }
  }
  
  /**
   * Get the label of immediate value
   * 
   * @param addr the address of the value
   * @param value in that location
   * @return the label of location
   */
  private String getLabelImm(long addr, long value) {
    if (addr<0 || addr>0xffff) return "$??"; 
    
    char type=memory[(int)addr].type;
    
    // this is a data declaration            
    if (type=='<' || type=='>' || type=='^') {    
      MemoryDasm memRel;
      // the byte is a reference
      if (type=='^') memRel=memory[memory[(int)addr].related & 0xFFFF];   
      else memRel=memory[memory[(int)addr].related];   
              
      if (memRel.userLocation!=null && !"".equals(memRel.userLocation)) return memory[(int)addr].type+memRel.userLocation;
      else if (memRel.dasmLocation!=null && !"".equals(memRel.dasmLocation)) return memory[(int)addr].type+memRel.dasmLocation;
           else return memory[(int)addr].type+"$"+ShortToExe(memRel.address);
    } else {        
        if (memory[(int)addr].index!=-1) {
          String res=constant.table[memory[(int)addr].index][(int)value];  
          if (res!=null && !"".equals(res)) return res;
        }            
        return "$"+ByteToExe((int)value);
      }
  }
  
  
  /**
   * Get the label or memory location ($) of zero page
   * 
   * @param addr the address of the label
   * @return the label or memory location ($)
   */
  private String getLabelZero(long addr) {
    if (addr<0 || addr>0xffff) return "$??";
      
    MemoryDasm mem=memory[(int)addr];
    
    if (mem.type=='+') {
      /// this is a memory in table label
      int pos=mem.address-mem.related;
      MemoryDasm mem2=memory[mem.related];
      if (mem2.userLocation!=null && !"".equals(mem2.userLocation)) return mem2.userLocation+"+"+pos;
      if (mem2.dasmLocation!=null && !"".equals(mem2.dasmLocation)) return mem2.dasmLocation+"+"+pos;
      return "$"+ByteToExe((int)mem.related)+"+"+pos;  
    }
    
    if (mem.type=='^') {
      /// this is a memory in table label
      int rel=mem.related>>16;
      int pos=mem.address-rel;
      MemoryDasm mem2=memory[rel];
      if (mem2.userLocation!=null && !"".equals(mem2.userLocation)) return mem2.userLocation+"+"+pos;
      if (mem2.dasmLocation!=null && !"".equals(mem2.dasmLocation)) return mem2.dasmLocation+"+"+pos;
      return "$"+ByteToExe(rel)+"+"+pos;  
    }    
    
    if (mem.type=='-') {
      /// this is a memory in table label
      int pos=mem.address-mem.related;
      MemoryDasm mem2=memory[mem.related];
      if (mem2.userLocation!=null && !"".equals(mem2.userLocation)) return mem2.userLocation+pos;
      if (mem2.dasmLocation!=null && !"".equals(mem2.dasmLocation)) return mem2.dasmLocation+pos;
      return "$"+ByteToExe((int)mem.related)+pos;  
    }     
     
    if (mem.userLocation!=null && !"".equals(mem.userLocation)) return mem.userLocation;
    if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) return mem.dasmLocation;
    return "$"+ByteToExe((int)addr);        
  } 

  /**
   * Get the label or memory location ($)
   * 
   * @param addr the address of the label
   * @return the label or memory location ($)
   */
  private String getLabel(long addr) {
    if (addr<0 || addr>0xffff) return "$????";  
      
    MemoryDasm mem=memory[(int)addr];
    
    try {    
        if (mem.type=='+') {
          /// this is a memory in table label
          int pos=mem.address-mem.related;
          MemoryDasm mem2=memory[mem.related];
          if (mem2.userLocation!=null && !"".equals(mem2.userLocation)) return mem2.userLocation+"+"+pos;
          if (mem2.dasmLocation!=null && !"".equals(mem2.dasmLocation)) return mem2.dasmLocation+"+"+pos;
          return "$"+ShortToExe((int)mem.related)+"+"+pos;  
        }

        if (mem.type=='^') {
          /// this is a memory in table label
          int rel=(mem.related>>16)&0xFFFF;
          int pos=mem.address-rel;
          MemoryDasm mem2=memory[rel];
          if (mem2.userLocation!=null && !"".equals(mem2.userLocation)) return mem2.userLocation+"+"+pos;
          if (mem2.dasmLocation!=null && !"".equals(mem2.dasmLocation)) return mem2.dasmLocation+"+"+pos;
          return "$"+ShortToExe(rel)+"+"+pos;  
        }    

        if (mem.type=='-') {
          /// this is a memory in table label
          int pos=mem.address-mem.related;
          MemoryDasm mem2=memory[mem.related];
          if (mem2.userLocation!=null && !"".equals(mem2.userLocation)) return mem2.userLocation+pos;
          if (mem2.dasmLocation!=null && !"".equals(mem2.dasmLocation)) return mem2.dasmLocation+pos;
          return "$"+ShortToExe((int)mem.related)+pos;  
        } 
    } catch (Exception e) {
        return "$xxxx";
      }
     
    if (mem.userLocation!=null && !"".equals(mem.userLocation)) return mem.userLocation;
    if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) return mem.dasmLocation;
    return "$"+ShortToExe((int)addr);        
  } 
  
  /**
   * Set the dasm label
   * 
   * @param addr the address to add as label
   */
  private void setLabel(long addr) {
    if (addr<0 || addr>0xffff) return;
    
    MemoryDasm mem=memory[(int)addr];
           
    if (mem.isInside && !mem.isGarbage) {
      if (mem.type=='+' || mem.type=='-') memory[mem.related].dasmLocation="W"+ShortToExe(mem.related);  
      else if (mem.type=='^') memory[mem.related & 0xFFFF].dasmLocation="W"+ShortToExe(mem.related & 0xFFFF);  
        
      mem.dasmLocation="W"+ShortToExe((int)addr);
    }
  }
  
  /**
   * Set the label for plus relative address if this is the case
   * 
   * @param addr tha address where to point
   * @param offset the offset to add
   */
  private void setLabelPlus(long addr, int offset) {
    if (addr<0 || addr+offset>0xffff) return;
    
    MemoryDasm mem=memory[(int)addr+offset];
    
    if (mem.isInside) {
      // set as relative + unless it is already set (even by user)
      if (mem.dasmLocation!=null && (mem.type!='+') && (mem.type!='-')) {
        mem.type='+';
        mem.related=(int)addr;
        setLabel(addr);
      } 
    }
  }
  
  /**
   * Set the label for minus relative address if this is the case
   * 
   * @param addr tha address where to point
   * @param offset the offset to sub
   */
  private void setLabelMinus(long addr, int offset) {
    if (addr<0 || addr+offset>0xffff) return;
    
    MemoryDasm mem=memory[(int)addr-offset];
    
    if (mem.isInside && mem.type!='+') {
      if (mem.dasmLocation!=null) {
        mem.type='-';
        mem.related=(int)addr;
        setLabel(addr);
      } 
    }
  }  
  
  /**
   * Add constants to the source
   * 
   * @return the constants
   */
  private String addConstants() {
    String label;  
    String tmp;
      
    StringBuilder result=new StringBuilder();
    
    for (MemoryDasm mem : memory) {
      if (mem.isInside && !mem.isGarbage) continue;
      
      // for garbage inside, only if there is a user label makes it outs
      if (mem.isGarbage && mem.userLocation!=null && !"".equals(mem.userLocation)) {
        
        // look for block comment
        if (mem.userBlockComment!=null && !"".equals(mem.userBlockComment)) {  
          assembler.setBlockComment(result, mem);
        } 
        
        label=mem.userLocation;
        
        if (option.assembler==Assembler.Name.KICK) {
          if (mem.address<=0xFF) tmp=".label "+label+" = $"+ByteToExe(mem.address);  
          else tmp=".label "+label+" = $"+ShortToExe(mem.address);  
        } else {
          if (mem.address<=0xFF) tmp=label+" = $"+ByteToExe(mem.address);  
          else tmp=label+" = $"+ShortToExe(mem.address);
        }
        
        result.append(tmp).append(getInstrCSpacesTabs(tmp.length()));          
          
        assembler.setComment(result, mem);                    
        
        continue;
      } 
      
      // look for block comment
      if (mem.userBlockComment!=null && !"".equals(mem.userBlockComment)) {  
        assembler.setBlockComment(result, mem);
      }   
      
      // look for constant
      label=null;
      if (mem.userLocation!=null && !"".equals(mem.userLocation)) label=mem.userLocation;
      else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) label=mem.dasmLocation;
      
      if (label!=null) {
        if (option.assembler==Assembler.Name.KICK) {
          if (mem.address<=0xFF) tmp=".label "+label+" = $"+ByteToExe(mem.address);  
          else tmp=".label "+label+" = $"+ShortToExe(mem.address);  
        } else {
          if (mem.address<=0xFF) tmp=label+" = $"+ByteToExe(mem.address);  
          else tmp=label+" = $"+ShortToExe(mem.address);
        }
        
        result.append(tmp).append(getInstrCSpacesTabs(tmp.length()));          
          
        assembler.setComment(result, mem);                             
      }
    }
    return result.append("\n").toString();
  }
  
  private static final String SPACES="                                                                               "; 
  private static final String TABS="\t\t\t\t\t\t\t\t\t\t";
  
  /**
   * Return spaces/tabs to use in start of instruction
   * 
   * @param mem the memory of this line
   * @return the spaces/tabs
   */
  private String getInstrSpacesTabs(MemoryDasm mem) {
    if (!option.labelOnSepLine) {
      int num=0;  
      if (mem.userLocation!=null && !"".equals(mem.userLocation)) num=mem.userLocation.length()+1;
      else if (mem.dasmLocation!=null && !"".equals(mem.dasmLocation)) num=mem.dasmLocation.length()+1;
      return SPACES.substring(0, (option.maxLabelLength-num<0 ? 1: option.maxLabelLength-num))+SPACES.substring(0, option.numInstrSpaces)+TABS.substring(0, option.numInstrTabs);
    } else return SPACES.substring(0, option.numInstrSpaces)+TABS.substring(0, option.numInstrTabs);
  }    
  
  /**
   * Return spaces/tabs to use in comment after instruction
   * 
   * @param mem the memory of this line
   * @return the spaces/tabs
   */
  private String getInstrCSpacesTabs(int skip) {
    return SPACES.substring(0, (option.numInstrCSpaces-skip<0 ? 1:option.numInstrCSpaces-skip))+TABS.substring(0, option.numInstrCTabs);
  }  
     
}
