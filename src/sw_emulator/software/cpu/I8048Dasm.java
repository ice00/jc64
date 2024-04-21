/**
 * @(#)I8048Dasm.java 2024/04/11
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
import sw_emulator.software.MemoryDasm;
import sw_emulator.swing.main.Carets.Type;

/**
 * Disasseble the I8048 code instructions
 * This class implements the <code>disassembler</code> interface, so it must
 * disassemble one instruction and comment it.
 *
 * @author Ice
 * @version 1.00 11/04/2024
 */
public class I8048Dasm extends CpuDasm implements disassembler {

    public I8048Dasm() {
        defaultMode=false; // mode for Hex as h
    }        
    
  // legal instruction
  public static final byte M_ADD = 0;
public static final byte M_ADDC = 1;
public static final byte M_ANL = 2;
public static final byte M_ANLD = 3;
public static final byte M_CALL = 4;
public static final byte M_CLR = 5;
public static final byte M_CPL = 6;
public static final byte M_DA = 7;
public static final byte M_DEC = 8;
public static final byte M_DIS = 9;
public static final byte M_DJNZ = 10;
public static final byte M_EN = 11;
public static final byte M_ENT0 = 12;
public static final byte M_INC = 13;
public static final byte M_IN = 14;
public static final byte M_INS = 15;
public static final byte M_JB0 = 16;
public static final byte M_JB1 = 17;
public static final byte M_JB2 = 18;
public static final byte M_JB3 = 19;
public static final byte M_JB4 = 20;
public static final byte M_JB5 = 21;
public static final byte M_JB6 = 22;
public static final byte M_JB7 = 23;
public static final byte M_JC = 24;
public static final byte M_JF0 = 25;
public static final byte M_JF1 = 26;
public static final byte M_JMPP = 27;
public static final byte M_JMP = 28;
public static final byte M_JNC = 29;
public static final byte M_JNI = 30;
public static final byte M_JNT0 = 31;
public static final byte M_JNT1 = 32;
public static final byte M_JNZ = 33;
public static final byte M_JTF = 34;
public static final byte M_JT0 = 35;
public static final byte M_JT1 = 36;
public static final byte M_JZ = 37;
public static final byte M_MOVD = 38;
public static final byte M_MOVX = 39;
public static final byte M_MOVP3 = 40;
public static final byte M_MOVP = 41;
public static final byte M_MOV = 42;
public static final byte M_NOP = 43;
public static final byte M_ORL = 44;
public static final byte M_ORLD = 45;
public static final byte M_OUTL = 46;
public static final byte M_RETL = 47;
public static final byte M_RETR = 48;
public static final byte M_RET = 49;
public static final byte M_RL = 50;
public static final byte M_RLC = 51;
public static final byte M_RR = 52;
public static final byte M_RRC = 53;
public static final byte M_SEL = 54;
public static final byte M_STRT = 55;
public static final byte M_STOP = 56;
public static final byte M_SWAP = 57;
public static final byte M_XCHD = 58;
public static final byte M_XCH = 59;
public static final byte M_XRL = 60;  
  
  // no instruction
  public static final byte M_JAM=61;    
  
  // undocument instruction
  public static final byte M_ID1=62;    
  
  // addressing mode
  public static final byte A_NUL  =0;  // nothing else
  public static final byte A_ACC  =1;  // accumulator
  public static final byte A_ACCR =2;  // accumulator/register
  public static final byte A_ACCD =3;  // accumulator/data
  public static final byte A_ACCI =4;  // accumulator/immediate
  public static final byte A_BUSI =5;  // bus/immediate
  public static final byte A_PORI =6;  // port/immediate
  public static final byte A_PORA =7;  // port/accumulator
  public static final byte A_CADR =8;  // address
  public static final byte A_CAR  =9;  // carry
  public static final byte A_FLG0 =10; // flag 0
  public static final byte A_FLG1 =11; // flag 1
  public static final byte A_REG  =12; // register
  public static final byte A_INT  =13; // interrupt
  public static final byte A_TIM  =14; // timer/counter interrupt
  public static final byte A_ACCP =15; // accumulator/port
  public static final byte A_DREG =16; // data register
  public static final byte A_ACCB =17; // accumulator/bus
  public static final byte A_REL  =18; // relative
  public static final byte A_INDA =19; // indirect accumulator
  public static final byte A_APSW =20; // accumulator/psw
  public static final byte A_ACCT =21; // accumulator/timer
  public static final byte A_PSWA =22; // psw/accumulator
  public static final byte A_RACC =23; // register/accumulator
  public static final byte A_REGI =24; // register/immediate
  public static final byte A_DACC =25; // data register/accumulator
  public static final byte A_RDAI =26; // data register/immediate
  public static final byte A_TACC =27; // timer/accumulator
  public static final byte A_APOR =28; // accumulator/port
  public static final byte A_ACCA =29; // accumulator/accumulator
  public static final byte A_ACDA =30; // accumulator/data
  public static final byte A_BUSA =31; // bus/accumulator
  public static final byte A_MB0  =32; // MB0
  public static final byte A_MB1  =33; // MB1
  public static final byte A_RB0  =34; // SB0
  public static final byte A_RB1  =35; // SB1
  public static final byte A_TCNT =36; // timer
  public static final byte A_CNT  =37; // event counter
  public static final byte A_T    =38; // timer
  public static final byte A_CLK  =39; // clock
  public static final byte A_REGA =40; // reg/address

  /** Contains the mnemonics of instructions */
  public static final String[] mnemonics = {
        "ADD  ",
        "ADDC ",
        "ANL  ",
        "ANLD ",
        "CALL ",
        "CLR  ",
        "CPL  ",
        "DA   ",
        "DEC  ",
        "DIS  ",
        "DJNZ ",
        "EN   ",
        "ENT0 ",
        "INC  ",
        "IN   ",
        "INS  ",
        "JB0  ",
        "JB1  ",
        "JB2  ",
        "JB3  ",
        "JB4  ",
        "JB5  ",
        "JB6  ",
        "JB7  ",
        "JC   ",
        "JF0  ",
        "JF1  ",
        "JMPP ",
        "JMP  ",
        "JNC  ",
        "JNI  ",
        "JNT0 ",
        "JNT1 ",
        "JNZ  ",
        "JTF  ",
        "JT0  ",
        "JT1  ",
        "JZ   ",
        "MOVD ",
        "MOVX ",
        "MOVP3",
        "MOVP ",
        "MOV  ",
        "NOP  ",
        "ORL  ",
        "ORLD ",
        "OUTL ",
        "RETL ",
        "RETR ",
        "RET  ",
        "RL   ",
        "RLC  ",
        "RR   ",
        "RRC  ",
        "SEL  ",
        "STRT ",
        "STOP ",
        "SWAP ",
        "XCHD ",
        "XCH  ",
        "XRL  ",
        "???  ",
        "ID1  "
    };
  
  /** Contains the mnemonics reference for the instruction */
  public static final byte[] tableMnemonics={
    M_NOP,  M_ID1,  M_OUTL, M_ADD,  M_JMP,  M_EN,   M_JAM,  M_DEC,  // 0
    M_INS,  M_IN,   M_IN,   M_JAM,  M_MOVD, M_MOVD, M_MOVD, M_MOVD,
    M_INC,  M_INC,  M_JB0,  M_ADDC, M_CALL, M_DIS,  M_JTF,  M_INC,  // 10
    M_INC,  M_INC,  M_INC,  M_INC,  M_INC,  M_INC,  M_INC,  M_INC,
    M_XCH,  M_XCH,  M_JAM,  M_MOV,  M_JMP,  M_EN,   M_JNT0, M_CLR,  // 20
    M_XCH,  M_XCH,  M_XCH,  M_XCH,  M_XCH,  M_XCH,  M_XCH,  M_XCH,
    M_XCHD, M_XCHD, M_JB1,  M_JAM,  M_CALL, M_DIS,  M_JT0,  M_CPL,  // 30
    M_JAM,  M_OUTL, M_OUTL, M_JAM,  M_MOVD, M_MOVD, M_MOVD, M_MOVD,
    M_ORL,  M_ORL,  M_MOV,  M_ORL,  M_JMP,  M_STRT, M_JNT1, M_SWAP, // 40
    M_ORL,  M_ORL,  M_ORL,  M_ORL,  M_ORL,  M_ORL,  M_ORL,  M_ORL,
    M_ANL,  M_ANL,  M_JB2,  M_ANL,  M_CALL, M_STRT, M_JT1,  M_DA,   // 50
    M_ANL,  M_ANL,  M_ANL,  M_ANL,  M_ANL,  M_ANL,  M_ANL,  M_ANL,
    M_ADD,  M_ADD,  M_MOV,  M_JAM,  M_JMP,  M_STOP, M_JAM,  M_RRC,  // 60
    M_ADD,  M_ADD,  M_ADD,  M_ADD,  M_ADD,  M_ADD,  M_ADD,  M_ADD,
    M_ADDC, M_ADDC, M_JB3,  M_JAM,  M_CALL, M_ENT0, M_JF1,  M_RR,   // 70
    M_ADDC, M_ADDC, M_ADDC, M_ADDC, M_ADDC, M_ADDC, M_ADDC, M_ADDC,
    M_MOVX, M_MOVX, M_JAM,  M_RET,  M_JMP,  M_CLR,  M_JNI,  M_JAM,  // 80
    M_ORL,  M_ORL,  M_ORL,  M_JAM,  M_ORLD, M_ORLD, M_ORLD, M_ORLD,
    M_MOVX, M_MOVX, M_JB4,  M_RETR, M_CALL, M_CPL,  M_JNZ,  M_CLR,  // 90
    M_ANL,  M_ANL,  M_ANL,  M_JAM,  M_ANLD, M_ANLD, M_ANLD, M_ANLD,
    M_MOV,  M_MOV,  M_JAM,  M_MOVP, M_JMP,  M_CLR,  M_JAM,  M_CPL,  // A0
    M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,
    M_MOV,  M_MOV,  M_JB5,  M_JMPP, M_CALL, M_CPL,  M_JF0,  M_JAM,  // B0
    M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,
    M_JAM,  M_JAM,  M_JAM,  M_JAM,  M_JMP,  M_SEL,  M_JZ,   M_MOV,  // C0
    M_DEC,  M_DEC,  M_DEC,  M_DEC,  M_DEC,  M_DEC,  M_DEC,  M_DEC,
    M_XRL,  M_XRL,  M_JB6,  M_XRL,  M_CALL, M_SEL,  M_JAM,  M_MOV,  // D0
    M_XRL,  M_XRL,  M_XRL,  M_XRL,  M_XRL,  M_XRL,  M_XRL,  M_XRL,
    M_JAM,  M_JAM,  M_JAM,  M_MOVP3,M_JMP,  M_SEL,  M_JNC,  M_RL,   // E0
    M_DJNZ, M_DJNZ, M_DJNZ, M_DJNZ, M_DJNZ, M_DJNZ, M_DJNZ, M_DJNZ,
    M_MOV,  M_MOV,  M_JB7,  M_JAM,  M_CALL, M_SEL,  M_JC,   M_RLC,  // F0
    M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV,  M_MOV
  };        
  
  /** Contains the modes for the instruction */
  public static final byte[] tableModes={  
    A_NUL,  A_NUL,  A_BUSA, A_ACCI, A_CADR, A_INT,  A_NUL,  A_ACC,   // 0
    A_ACCB, A_ACCP, A_ACCP, A_NUL,  A_APOR, A_APOR, A_APOR, A_APOR,
    A_DREG, A_DREG, A_REL,  A_ACCI, A_CADR, A_INT,  A_REL,  A_ACC,   // 10
    A_REG,  A_REG,  A_REG,  A_REG,  A_REG,  A_REG,  A_REG,  A_REG,
    A_ACCD, A_ACCD, A_NUL,  A_ACCI, A_CADR, A_TIM,  A_REL,  A_ACC,   // 20
    A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR,
    A_ACCD, A_ACCD, A_REL,  A_NUL,  A_CADR, A_TIM,  A_REL,  A_ACC,   // 30
    A_NUL,  A_PORA, A_PORA, A_NUL,  A_PORA, A_PORA, A_PORA, A_PORA,
    A_ACDA, A_ACDA, A_ACCT, A_ACCI, A_CADR, A_CNT,  A_REL,  A_ACC,   // 40
    A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR,
    A_ACCD, A_ACCD, A_REL,  A_ACCI, A_CADR, A_T,    A_REL,  A_ACC,   // 50
    A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR,  
    A_ACCD, A_ACCD, A_TACC, A_NUL,  A_CADR, A_TCNT, A_NUL,  A_ACC,   // 60
    A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR,
    A_ACCD, A_ACCD, A_REL,  A_NUL,  A_CADR, A_CLK,  A_REL,  A_ACC,   // 70 
    A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR,
    A_ACDA, A_ACDA, A_NUL,  A_NUL,  A_CADR, A_FLG0, A_REL,  A_NUL,   // 80
    A_BUSI, A_PORI, A_PORI, A_NUL,  A_PORA, A_PORA, A_PORA, A_PORA,
    A_DACC, A_DACC, A_REL,  A_NUL,  A_CADR, A_FLG0, A_REL,  A_CAR,   // 90
    A_BUSI, A_PORI, A_PORI, A_NUL,  A_PORA, A_PORA, A_PORA, A_PORA,
    A_DACC, A_DACC, A_NUL,  A_ACCA, A_CADR, A_FLG1, A_NUL,  A_CAR,   // A0
    A_RACC, A_RACC, A_RACC, A_RACC, A_RACC, A_RACC, A_RACC, A_RACC,  
    A_RDAI, A_RDAI, A_REL,  A_INDA, A_CADR, A_FLG1, A_REL,  A_NUL,   // B0
    A_REGI, A_REGI, A_REGI, A_REGI, A_REGI, A_REGI, A_REGI, A_REGI,
    A_NUL,  A_NUL,  A_NUL,  A_NUL,  A_CADR, A_RB0,  A_REL,  A_APSW,  // C0
    A_REG,  A_REG,  A_REG,  A_REG,  A_REG,  A_REG,  A_REG,  A_REG,
    A_ACCD, A_ACCD, A_REL,  A_ACCI, A_CADR, A_RB1,  A_NUL,  A_PSWA,  // D0
    A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR,
    A_NUL,  A_NUL,  A_NUL,  A_ACCA, A_CADR, A_MB0,  A_REL,  A_ACC,   // E0
    A_REGA, A_REGA, A_REGA, A_REGA, A_REGA, A_REGA, A_REGA, A_REGA,
    A_ACCD, A_ACCD, A_REL,  A_NUL,  A_CADR, A_MB1,  A_REL,  A_ACC,   // F0
    A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR, A_ACCR
  };

  /** Contains the bytes used for the instruction */
  public static final byte[] tableSize={
    1, 1, 1, 2, 2, 1, 1, 1,      // 00 - 07
    1, 1, 1, 1, 1, 1, 1, 1,      // 08 - 0f
    1, 1, 2, 2, 2, 1, 2, 1,      // 10 - 17
    1, 1, 1, 1, 1, 1, 1, 1,      // 18 - 1f
    1, 1, 1, 2, 2, 1, 2, 1,      // 20 - 27
    1, 1, 1, 1, 1, 1, 1, 1,      // 28 - 2f
    1, 1, 2, 1, 2, 1, 2, 1,      // 30 - 37
    1, 1, 1, 1, 1, 1, 1, 1,      // 38 - 3f
    1, 1, 1, 2, 2, 1, 2, 1,      // 40 - 47
    1, 1, 1, 1, 1, 1, 1, 1,      // 48 - 4f
    1, 1, 2, 2, 2, 1, 2, 1,      // 50 - 57
    1, 1, 1, 1, 1, 1, 1, 1,      // 58 - 5f
    1, 1, 1, 1, 2, 1, 1, 1,      // 60 - 67
    1, 1, 1, 1, 1, 1, 1, 1,      // 68 - 6f
    1, 1, 2, 1, 2, 1, 2, 1,      // 70 - 77
    1, 1, 1, 1, 1, 1, 1, 1,      // 78 - 7f
    1, 1, 1, 1, 2, 1, 2, 1,      // 80 - 87
    2, 2, 2, 1, 1, 1, 1, 1,      // 88 - 8f
    1, 1, 2, 1, 2, 1, 2, 1,      // 90 - 97
    2, 2, 2, 1, 1, 1, 1, 1,      // 98 - 9f
    1, 1, 1, 1, 2, 1, 1, 1,      // a0 - a7
    1, 1, 1, 1, 1, 1, 1, 1,      // a8 - af
    2, 2, 2, 1, 2, 1, 2, 1,      // b0 - b7
    2, 2, 2, 2, 2, 2, 2, 2,      // b8 - bf
    1, 1, 1, 1, 2, 1, 2, 1,      // c0 - c7
    1, 1, 1, 1, 1, 1, 1, 1,      // c8 - cf
    1, 1, 2, 2, 2, 1, 1, 1,      // d0 - d7
    1, 1, 1, 1, 1, 1, 1, 1,      // d8 - df
    1, 1, 1, 1, 2, 1, 2, 1,      // e0 - e7
    2, 2, 2, 2, 2, 2, 2, 2,      // e8 - ef
    1, 1, 2, 1, 2, 1, 2, 1,      // f0 - f7
    1, 1, 1, 1, 1, 1, 1, 1       // f8 - ff 
  };

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
    int pStart;                // start position for caret
    String result="";          // result disassemble string
    int op=Unsigned.done(buffer[pos++]); // instruction opcode

    iType=(int)tableMnemonics[op];   // store the type for creating comment             
    
    if (upperCase) result=mnemonics[iType];
    else result=mnemonics[iType].toLowerCase();
      
    String nn=getSpacesTabsOp();
    result+=nn;

    aType=tableModes[op];
    switch (aType) {
      case A_NUL:     // nothing
        pc++;
        break;
      case A_ACC:     // accumulator
        result+="A";
        pc++;
        break;
      case A_CAR:     // carry
        result+="C";
        pc++;
        break;  
      case A_INT:     // interrupt
        result+="I";
        pc++;
        break;   
      case A_TIM:     // timer/counter interrupt
        result+="TCNTI";
        pc++;
        break;          
      case A_TCNT:     // timer
        result+="TCNT";
        pc++;
        break; 
      case A_CNT:      // event counter
        result+="CNT";
        pc++;
        break;     
      case A_T:       // timer
        result+="T";
        pc++;
        break;        
      case A_FLG0:    // flag 0
        result+="F0";
        pc++;
        break;
      case A_FLG1:   // flag 1
        result+="F1";
        pc++;
        break;
      case A_INDA:   // indirect accumulator
        result+="@A";
        pc++;
        break;        
      case A_ACCR:    // accumulator/register
        result+="A,R"+(op&0x07);
        pc++;
        break;
      case A_RACC:    // register/accumulator
        result+="R"+(op&0x07)+",A";
        pc++;
        break;        
      case A_ACCD:    // accumulator/data
        result+="A,@R"+(op&0x01);
        pc++;
        break;   
      case A_ACCP:    // accumulator/port
        result+="A,P"+(op&0x03);
        pc++;
        break;    
      case A_APSW:    // accumulator/psw
        result+="A,PSW";
        pc++;
        break; 
      case A_PSWA:    // psw/accumulator
        result+="PSW,A";
        pc++;
        break;           
      case A_ACCT:    // accumulator/timer
        result+="A,T";
        pc++;
        break;          
      case A_TACC:    // timer/accumulator
        result+="T,A";
        pc++;
        break;          
      case A_ACCI:    // accumulator/immediate
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+="A,#"+getLabelImm(pc+1, value);
        setLabelPlus(pc,1);
        
        pc+=2;
        break;      
      case A_RDAI:    // register/immediate
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+="@R"+(op&0x01)+",#"+getLabelImm(pc+1, value);
        setLabelPlus(pc,1);
        
        pc+=2;
        break;         
      case A_BUSI:    // bus/immediate
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+="BUS,#"+getLabelImm(pc+1, value);
        setLabelPlus(pc,1);
        
        pc+=2;
        break;        
      case A_PORI:    // port/immediate
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+="P"+(op&0x03)+",#"+getLabelImm(pc+1, value);
        setLabelPlus(pc,1);
        
        pc+=2;
        break;  
      case A_REGI:    // register/immediate
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+="R"+(op&0x03)+",#"+getLabelImm(pc+1, value);
        setLabelPlus(pc,1);
        
        pc+=2;
        break;          
      case A_PORA:    // port/accumulator
        result+="P"+(op&0x07)+",A";
        pc++;
        break;  
      case A_APOR:    // accumulator/port
        result+="A,P"+(op&0x07);
        pc++;
        break;         
      case A_REG:    // register
        result+="R"+(op&0x07);
        pc++;
        break; 
      case A_DREG:    // data register
        result+="@R"+(op&0x01);
        pc++;
        break;           
      case A_ACDA:    // accumulator/data register
        result+="A,@R"+(op&0x01);
        pc++;
        break;         
      case A_DACC:    // data register/accumulator
        result+="@R"+(op&0x01)+",A";
        pc++;
        break;          
      case A_ACCB:     // accumulator/bus
        result+="A,BUS";
        pc++;
        break;  
      case A_BUSA:     // accumulator/bus
        result+="BUS,A";
        pc++;
        break;         
      case A_ACCA:     // accumulator/accumulator
        result+="A,@A";
        pc++;
        break;          
      case A_REL:     // relative
        if (pos<buffer.length) addr=(pc & 0xFF00)+(buffer[pos++] & 0xFF);
        else addr=-1;   
        
        result+=getLabel(addr);
        setLabel(addr);
        setLabelPlus(pc,1);
        
        pc+=2;
        break;   
      case A_CADR:  // address
        if (pos<buffer.length) addr=((op & 0xE0)<<3)+(buffer[pos++] & 0xFF);
        else addr=-1;   
        
        result+=getLabel(addr);
        setLabel(addr);
        setLabelPlus(pc,1);
        pc+=2;
        break;       
      case A_REGA:    // register address
        if (pos<buffer.length) addr=(pc & 0xFF00)+(buffer[pos++] & 0xFF);
        else addr=-1;    
          
        result+="R"+(op&0x07)+","+getLabel(addr);
        setLabel(addr);
        setLabelPlus(pc,1);
        pc+=2;
        break;         
      case A_MB0:    // MB0
        result+="MB0";
        pc++;
        break;        
      case A_MB1:    // MB1
        result+="MB1";
        pc++;
        break;     
      case A_RB0:    // RB0
        result+="RB0";
        pc++;
        break;        
      case A_RB1:    // RB1
        result+="RB1";
        pc++;
        break;     
      case A_CLK:    // clock
        result+="CLK";
        pc++;
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
  @Override
  public String cdasm(byte[] buffer, int start, int end, long pc) {    
    String tmp;                  // local temp string
    String tmp2;                 // local temp string
    MemoryDasm mem;              // memory dasm
    MemoryDasm memRel;           // memory related
    MemoryDasm memRel2;          // memory related of second kind
    int actualOffset;            // actual offset for caret action
            
    int pos=start;               // actual position in buffer
    boolean isCode=true;         // true if we are decoding an instruction
    boolean wasGarbage=false;    // true if we were decoding garbage
        
    result.setLength(0);
 //   result.append(addConstants());
    
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
          
          actualOffset=assembler.getCarets().getOffset();                               // rember actual offset
          assembler.getCarets().setOffset(result.length()+actualOffset+17);             // use new offset
          tmp=dasm(buffer);                                                             // this is an instruction
          assembler.getCarets().setOffset(actualOffset);                                // set old offset     
               
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
          
          // always add a carriage return after a RET, RETL, RETR or JMP
          if (iType==M_JMP || iType==M_RET || iType==M_RETL || iType==M_RETR) result.append("\n");    
          
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
            assembler.putValue(result, mem, memRel, memRel2, memory[mem.relatedAddressBase], memory[mem.relatedAddressDest]); 
            
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
  @Override
  public String csdasm(byte[] buffer, int start, int end, long pc) {
    String tmp;                  // local temp string
    String tmp2;                 // local temp string
    MemoryDasm mem;              // memory dasm
    MemoryDasm memRel;           // memory related
    MemoryDasm memRel2;          // memory related of second kind
    int actualOffset;            // actual offset
    int pos=start;               // actual position in buffer
    boolean isCode=true;         // true if we are decoding an instruction
    boolean wasGarbage=false;    // true if we were decoding garbage
    int pStart;
         
    result.setLength(0);
  //  result.append(addConstants());
    
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
                              
          pStart=result.length();
          result.append(getInstrSpacesTabs(mem));
          
          // this is an instruction         
          actualOffset=assembler.getCarets().getOffset();                               // rember actual offset
          assembler.getCarets().setOffset(result.length()+actualOffset);                // use new offset
          tmp=dasm(buffer);                                                             // this is an instruction
          assembler.getCarets().setOffset(actualOffset);                                // set old offset   
                  
          result.append(tmp).append(getInstrCSpacesTabs(tmp.length()));
          assembler.getCarets().add(pStart, result.length(), mem, Type.INSTR);
          
          tmp2=dcom();   
          
          // if there is a user comment, then use it
          assembler.setComment(result, mem);
          
          // always add a carriage return after a RET, RETL, RETR or JMP
          if (iType==M_JMP || iType==M_RET || iType==M_RETL || iType==M_RETR) result.append("\n");            
          
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
            assembler.putValue(result, mem, memRel, memRel2, memory[mem.relatedAddressBase], memory[mem.relatedAddressDest]);            
            
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
        case M_JAM: return "Illegal instruction";
        case M_ID1: return "Undocument instruction";
        default: return "";
    }
  }

  /**
   * Return a comment string for the last instruction
   *
   * @return a comment string
   */
  public String dcom() {
    return dcom(iType, aType, addr, value);
  }  
}
