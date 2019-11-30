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

import sw_emulator.software.cpu.disassembler;
import sw_emulator.software.memory.memoryState;
import sw_emulator.math.Unsigned;
import java.lang.Integer;
import java.lang.String;
import java.util.Locale;

/**
 * Disasseble the M6510 code instructions
 * This class implements the </code>disassembler</code> interface, so it must
 * disassemble one instruction and comment it.
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

  /**
   * Contains the mnemonics of instructions
   */
  public String[] mnemonics={
    // legal instruction first:
    new String("ADC"),
    new String("AND"),
    new String("ASL"),
    new String("BCC"),
    new String("BCS"),
    new String("BEQ"),
    new String("BIT"),
    new String("BMI"),
    new String("BNE"),
    new String("BPL"),
    new String("BRK"),
    new String("BVC"),
    new String("BVS"),
    new String("CLC"),
    new String("CLD"),
    new String("CLI"),
    new String("CLV"),
    new String("CMP"),
    new String("CPX"),
    new String("CPY"),
    new String("DEC"),
    new String("DEX"),
    new String("DEY"),
    new String("EOR"),
    new String("INC"),
    new String("INX"),
    new String("INY"),
    new String("JMP"),
    new String("JSR"),
    new String("LDA"),
    new String("LDX"),
    new String("LDY"),
    new String("LSR"),
    new String("NOP"),
    new String("ORA"),
    new String("PHA"),
    new String("PHP"),
    new String("PLA"),
    new String("PLP"),
    new String("ROL"),
    new String("ROR"),
    new String("RTI"),
    new String("RTS"),
    new String("SBC"),
    new String("SEC"),
    new String("SED"),
    new String("SEI"),
    new String("STA"),
    new String("STX"),
    new String("STY"),
    new String("TAX"),
    new String("TAY"),
    new String("TSX"),
    new String("TXA"),
    new String("TXS"),
    new String("TYA"),
    // undocument instruction
    new String("ANC"),
    new String("ANE"),
    new String("ARR"),
    new String("ASR"),
    new String("DCP"),
    new String("ISB"),
    new String("JAM"),
    new String("LAS"),
    new String("LAX"),
    new String("LXA"),
    new String("NOOP"),
    new String("NOOP"),
    new String("NOOP"),
    new String("RLA"),
    new String("RRA"),
    new String("SAX"),
    new String("SBX"),
    new String("SHA"),
    new String("SHX"),
    new String("SHY"),
    new String("SHS"),
    new String("SLO"),
    new String("SRE"),
    new String("USBC")
  };

  /**
   * Contains the mnemonics reference for the instruction
   */
  public byte[] tableMnemonics={
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

  public byte[] tableModes={
    A_IMP, A_IDX, A_NUL, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,
    A_IMP, A_IMM, A_ACC, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_ABS, A_IDX, A_NUL, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,
    A_IMP, A_IMM, A_ACC, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_IMP, A_IDX, A_NUL, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,
    A_IMP, A_IMM, A_ACC, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_IMP, A_IDX, A_NUL, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,
    A_IMP, A_IMM, A_ACC, A_IMM, A_IND, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_IMM, A_IDX, A_IMM, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,
    A_IMP, A_IMM, A_IMP, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPY, A_ZPY,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABY, A_ABY,
    A_IMM, A_IDX, A_IMM, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,
    A_IMP, A_IMM, A_IMP, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPY, A_ZPY,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABY, A_ABY,
    A_IMM, A_IDX, A_IMM, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,
    A_IMP, A_IMM, A_IMP, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX,
    A_IMM, A_IDX, A_IMM, A_IDX, A_ZPG, A_ZPG, A_ZPG, A_ZPG,
    A_IMP, A_IMM, A_IMP, A_IMM, A_ABS, A_ABS, A_ABS, A_ABS,
    A_REL, A_IDY, A_NUL, A_IDY, A_ZPX, A_ZPX, A_ZPX, A_ZPX,
    A_IMP, A_ABY, A_IMP, A_ABY, A_ABX, A_ABX, A_ABX, A_ABX
  };

  /**
   * Type of instruction (used to create comment)
   */
  public int iType=M_JAM;

  /**
   * Type of addressing used by instruction (used to create comment)
   */
  public int aType=A_NUL;

  /**
   * Value of address (used to create comment)
   */
  public long addr=0;

  /**
   * Value of operation (used to create comment)
   */
  public long value=0;

  /**
   * Last position pointer in buffer
   */
  public int pos=0;

  /**
   * Last program counter value
   */
  public long pc=0;

  /**
   * Return the mnemonic assembler instruction rapresent by passed code bytes.
   *
   * @param buffer the buffer containg the data
   * @param pos the actual position in the buffer
   * @param pc the program counter value associated to the bytes being address
   *           by the <code>pos</code> in the buffer
   * @return a string menemonic rapresentation of instruction
   */
  public String dasm(byte[] buffer, int pos, long pc) {
    String result=new String();          // result disassemble string
    int op=Unsigned.done(buffer[pos++]); // instruction opcode

    iType=(int)tableMnemonics[op];   // store the type for creating comment
    result=mnemonics[iType];
    if (result.length()==3) result+=" ";
    result+=" ";

    switch (tableModes[op]) {
      case A_NUL:     // nothing
        aType=A_NUL;
        pc++;
        break;
      case A_ACC:     // accumulator
        aType=A_ACC;
        result+="A";
        pc++;
        break;
      case A_IMP:     // implicit
        aType=A_IMP;
        pc++;
        break;
      case A_IMM:     // immediate
        aType=A_IMM;
        value=Unsigned.done(buffer[pos++]);
        result+="#$"+ByteToExe((int)value);
        pc+=2;
        break;
      case A_ZPG:     // zero page
        aType=A_ZPG;
        addr=Unsigned.done(buffer[pos++]);
        result+="$"+ByteToExe((int)addr);
        pc+=2;
        break;
      case A_ZPX:     // zero page x
        aType=A_ZPX;
        addr=Unsigned.done(buffer[pos++]);
        result+="$"+ByteToExe((int)addr)+",X";
        pc+=2;
        break;
      case A_ZPY:     // zero page y
        aType=A_ZPY;
        addr=Unsigned.done(buffer[pos++]);
        result+="$"+ByteToExe((int)addr)+",Y";
        pc+=2;
        break;
      case A_ABS:     // absolute
        aType=A_ABS;
        addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        pos++;
        result+="$"+ShortToExe((int)addr);
        pc+=3;
        break;
      case A_ABX:     // absolute x
        aType=A_ABX;
        addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        pos++;
        result+="$"+ShortToExe((int)addr)+",X";
        pc+=3;
        break;
      case A_ABY:     // absolute y
        aType=A_ABS;
        addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        pos++;
        result+="$"+ShortToExe((int)addr)+",Y";
        pc+=3;
        break;
      case A_REL:     // relative
        aType=A_REL;
        addr=pc+buffer[pos++]+2;
        result+="$"+ShortToExe((int)addr);
        pc+=2;
        break;
      case A_IND:     // indirect
        aType=A_IND;
        addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        pos++;
        result+="($"+ShortToExe((int)addr)+")";
        pc+=3;
        break;
      case A_IDX:     // indirect x
        aType=A_IDX;
        addr=Unsigned.done(buffer[pos++]);
        result+="($"+ByteToExe((int)addr)+",X)";
        pc+=2;
        break;
      case A_IDY:     // indirect y
        aType=A_IDY;
        addr=Unsigned.done(buffer[pos++]);
        result+="($"+ByteToExe((int)addr)+"),Y";
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
   * @param memory the memory state (SIDLN flags)   
   * @return a string rapresentation of disasemble with comment
   */
  public String cdasm(byte[] buffer, int start, int end, long pc, memoryState memory) {
    StringBuffer result=new StringBuffer ("");            // resulting string
    String tmp;                  // local temp string
    String tmp2;                 // local temp string
    int pos=start;               // actual position in buffer
    boolean wasCode=true;        // true if we were decoding an instruction
    boolean isCode=true;         // true if we are decoding an instruction
    
    byte[] state=memory.getMemoryState(0, 65535);

    this.pos=pos;;
    this.pc=pc;
    while (pos<end | pos<start) { // verify also that don't circle in the buffer
      if (wasCode) {    
        if ((state[(int)pc] & 
          (memoryState.MEM_READ | memoryState.MEM_READ_FIRST |
           memoryState.MEM_WRITE | memoryState.MEM_WRITE_FIRST |
           memoryState.MEM_SAMPLE)) ==0 ) {
          isCode=true;    
        } else {
            isCode=false;
          }
      } else {
          if ((state[(int)pc] & 
             (memoryState.MEM_EXECUTE | memoryState.MEM_EXECUTE_FIRST)) ==0 ) {
            isCode=false;               
          } else {
               isCode=true;
            }                     
        }    
        
        if (isCode) {
          // this is an instruction   
          tmp=dasm(buffer);
          tmp2=ShortToExe((int)pc)+"  "+ByteToExe(Unsigned.done(buffer[pos]));
          if (this.pc-pc==2) tmp2+=" "+ByteToExe(Unsigned.done(buffer[pos+1]));
          if (this.pc-pc==3) tmp2+=" "+ByteToExe(Unsigned.done(buffer[pos+1]))+
                                   " "+ByteToExe(Unsigned.done(buffer[pos+2]));
          for (int i=tmp2.length(); i<17; i++) // insert spaces
            tmp2+=" ";
          tmp=tmp2+tmp;
          tmp2="";
          for (int i=tmp.length(); i<34; i++) // insert spaces
            tmp2+=" ";
          result.append(tmp).append(tmp2).append(dcom()).append("\n");
          pos=this.pos;
          pc=this.pc;
          wasCode=true;
        } else {
            // this is a data declaration
            tmp2=ShortToExe((int)pc)+"  .byte $"+ByteToExe(Unsigned.done(buffer[pos]));
            pos++;
            pc++;
            if ((state[(int)pc] & (memoryState.MEM_EXECUTE | memoryState.MEM_EXECUTE_FIRST)) ==0 ) {
              tmp2+=", $"+ByteToExe(Unsigned.done(buffer[pos]));
              pos++;
              pc++;       
              if ((state[(int)pc] & (memoryState.MEM_EXECUTE | memoryState.MEM_EXECUTE_FIRST)) ==0 ) {
                tmp2+=", $"+ByteToExe(Unsigned.done(buffer[pos]));
                pos++;
                pc++;       
                if ((state[(int)pc] & (memoryState.MEM_EXECUTE | memoryState.MEM_EXECUTE_FIRST)) ==0 ) {
                  tmp2+=", $"+ByteToExe(Unsigned.done(buffer[pos]));
                  pos++;
                  pc++;                                          
                }                                    
              }                                    
            }                      
            result.append(tmp2).append("\n");
            
            this.pos=pos;
            this.pc=pc;            
            wasCode=false;
          }         
    } 
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

    String ret=Integer.toHexString(tmp);
    int len=ret.length();
    if (len==1) ret="000"+ret;
    else if (len==2) ret="00"+ret;
         else if (len==3) ret="0"+ret;
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
        mnemonics[M_SLO]="ASO";
        mnemonics[M_SHA]="AXA";
        mnemonics[M_SHS]="TAS";
        mnemonics[M_SHX]="XAS";
        mnemonics[M_SHY]="SAY";
        mnemonics[M_SRE]="LSE";
    }
  }
}
