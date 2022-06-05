/**
 * @(#)Z80Dasm.java 2022/02/03
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
 * Disasseble the Z80 code instructions
 * This class implements the </code>disassembler</code> interface, so it must
 * disassemble one instruction and comment it.
 *
 * @author ice
 */
public class Z80Dasm extends CpuDasm implements disassembler {
    
  // extra table  
  public static final byte T_CB =-1;
  public static final byte T_DD =-2;
  public static final byte T_ED =-3;
  public static final byte T_FD =-4;  
  public static final byte T_DDCB=-5;
  public static final byte T_FDCB=-6;
    
   // legal instruction
  public static final byte M_ADC =0; 
  public static final byte M_ADD =1; 
  public static final byte M_AND =2; 
  public static final byte M_BIT =3;
  public static final byte M_CALL=4;
  public static final byte M_CCF =5;
  public static final byte M_CP  =6;
  public static final byte M_CPD =7;
  public static final byte M_CPDR=8;
  public static final byte M_CPI =9;
  public static final byte M_CPIR=10;
  public static final byte M_CPL =11;
  public static final byte M_DAA =12;
  public static final byte M_DEC =13;
  public static final byte M_DI  =14;
  public static final byte M_DJNZ=15;
  public static final byte M_EI  =16;
  public static final byte M_EX  =17;
  public static final byte M_EXX =18;
  public static final byte M_HALT=19;
  public static final byte M_IM  =20;
  public static final byte M_IN  =21;
  public static final byte M_INC =22;
  public static final byte M_IND =23;
  public static final byte M_INDR=24;
  public static final byte M_INI =25;
  public static final byte M_INIR=26;
  public static final byte M_JP  =27;
  public static final byte M_JR  =28;
  public static final byte M_LD  =29;
  public static final byte M_LDD =30;
  public static final byte M_LDDR=31;
  public static final byte M_LDI =32;
  public static final byte M_LDIR=33;
  public static final byte M_NEG =34;
  public static final byte M_NOP =35;
  public static final byte M_OR  =36;
  public static final byte M_OTDR=37;
  public static final byte M_OTIR=38;
  public static final byte M_OUT =39;
  public static final byte M_OUTD=40;
  public static final byte M_OUTI=41;
  public static final byte M_POP =42;
  public static final byte M_PUSH=43;
  public static final byte M_RES =44;
  public static final byte M_RET =45;
  public static final byte M_RETI=46;
  public static final byte M_RETN=47;
  public static final byte M_RL  =48;
  public static final byte M_RLA =49;
  public static final byte M_RLC =50;
  public static final byte M_RLCA=51;
  public static final byte M_RLD =52;
  public static final byte M_RR  =53;
  public static final byte M_RRA =54;
  public static final byte M_RRC =55;
  public static final byte M_RRCA=56;
  public static final byte M_RRD =57;
  public static final byte M_RST =58;
  public static final byte M_SBC =59;
  public static final byte M_SCF =60;
  public static final byte M_SET =61;
  public static final byte M_SLA =62;
  public static final byte M_SLI =63;
  public static final byte M_SRA =64;
  public static final byte M_SRL =65;
  public static final byte M_STOP=66;
  public static final byte M_SUB =67;
  public static final byte M_XOR =68;
  // extra
  public static final byte M_NUL =69;
  public static final byte M_SLL =70;
  
  // addressing mode
  public static final int A_NUL    =0;   // nothing else
  public static final int A_REG_A  =1;   // register A
  public static final int A_REG_B  =2;   // register B
  public static final int A_REG_C  =3;   // register C
  public static final int A_REG_D  =4;   // register D
  public static final int A_REG_E  =5;   // register E
  public static final int A_REG_H  =6;   // register H
  public static final int A_REG_L  =7;   // register L
  public static final int A_BC_NN  =8;   // BC absolute nn
  public static final int A_DE_NN  =9;   // DE absolute nn
  public static final int A_HL_NN  =10;  // HL absolute nn
  public static final int A_SP_NN  =11;  // SP absolute nn
  public static final int A_IX_NN  =12;  // IX absolute nn
  public static final int A_IY_NN  =13;  // IY absolute nn
  public static final int A__BC_A  =14;  // (BC) indirect A
  public static final int A__DE_A  =15;  // (DE) indirect A
  public static final int A__HL_A  =16;  // (HL) indirect A
  public static final int A__IXN_A =17;  // (IX+N) indirect A
  public static final int A__IYN_A =18;  // (IY+N) indirect A
  public static final int A__NN_A  =19;  // (NN) indirect A
  public static final int A_REG_BC =20;  // registers BC
  public static final int A_REG_DE =21;  // registers DE
  public static final int A_REG_HL =22;  // registers HL
  public static final int A_REG_SP =23;  // registers SP
  public static final int A_A_N    =24;  // A reg with N
  public static final int A_B_N    =25;  // B reg with N
  public static final int A_C_N    =26;  // C reg with N
  public static final int A_D_N    =27;  // D reg with N
  public static final int A_E_N    =28;  // E reg with N
  public static final int A_H_N    =29;  // H reg with N
  public static final int A_L_N    =30;  // L reg with N
  public static final int A_AF_AF  =31;  // AF with shadow AF'
  public static final int A_HL_BC  =32;  // HL reg BC
  public static final int A_HL_DE  =33;  // HL reg DE
  public static final int A_HL_HL  =34;  // HL reg HL
  public static final int A_HL_SP  =35;  // HL reg SP
  public static final int A_IX_BC  =36;  // IX reg BC
  public static final int A_IX_DE  =37;  // IX reg DE
  public static final int A_IX_HL  =38;  // IX reg HL
  public static final int A_IX_SP  =39;  // IX reg SP
  public static final int A_IY_BC  =40;  // IY reg BC
  public static final int A_IY_DE  =41;  // IY reg DE
  public static final int A_IY_HL  =42;  // IY reg HL
  public static final int A_IY_SP  =43;  // IY reg SP 
  public static final int A_A__BC  =44;  // A indirect (BC)
  public static final int A_A__DE  =45;  // A indirect (DE)
  public static final int A_A__HL  =46;  // A indirect (HL)
  public static final int A_A__IXN =47;  // A indirect (IX+N)
  public static final int A_A__IYN =48;  // A indirect (IY+N)
  public static final int A_REL    =49;  // relative
  public static final int A_REL_NZ =50;  // relative NZ
  public static final int A_REL_Z  =51;  // relative Z
  public static final int A_REL_NC =52;  // relative NC
  public static final int A_REL_C  =53;  // relative C  
  public static final int A__NN_BC =54;  // (NN) ind absolute BC
  public static final int A__NN_DE =55;  // (NN) ind absolute DE
  public static final int A__NN_HL =56;  // (NN) absolute HL
  public static final int A__NN_SP =57;  // (NN) ind absolute SP
  public static final int A__NN_IX =58;  // (NN) ind absolute IX
  public static final int A__NN_IY =59;  // (NN) ind absolute IY
  public static final int A_BC__NN =60;  // BC ind absolute (NN)
  public static final int A_DE__NN =61;  // DE ind absolute (NN)
  public static final int A_HL__NN =62;  // HL ind absolute (NN)
  public static final int A_SP__NN =63;  // SP ind absolute (NN)
  public static final int A_IX__NN =64;  // IX ind absolute (NN)
  public static final int A_IY__NN =65;  // IY ind absolute (NN)
  public static final int A__HL    =66;  // ind (HL)
  public static final int A__HL_N  =67;  // ind (HL) imm N
  public static final int A_A__NN  =68;  // A ind (NN)
  public static final int A_A_A    =69;  // A reg A
  public static final int A_A_B    =70;  // A reg B
  public static final int A_A_C    =71;  // A reg C
  public static final int A_A_D    =72;  // A reg D
  public static final int A_A_E    =73;  // A reg E
  public static final int A_A_H    =74;  // A reg A
  public static final int A_A_L    =75;  // A reg L
  public static final int A_A_I    =76;  // A reg I
  public static final int A_A_R    =77;  // A reg R
  public static final int A_B_A    =78;  // B reg A
  public static final int A_B_B    =79;  // B reg B
  public static final int A_B_C    =80;  // B reg C
  public static final int A_B_D    =81;  // B reg D
  public static final int A_B_E    =82;  // B reg E
  public static final int A_B_H    =83;  // B reg A
  public static final int A_B_L    =84;  // B reg L
  public static final int A_C_A    =85;  // C reg A
  public static final int A_C_B    =86;  // C reg B
  public static final int A_C_C    =87;  // C reg C
  public static final int A_C_D    =88;  // C reg D
  public static final int A_C_E    =89;  // C reg E
  public static final int A_C_H    =90;  // C reg A
  public static final int A_C_L    =91;  // C reg L
  public static final int A_D_A    =92;  // D reg A
  public static final int A_D_B    =93;  // D reg B
  public static final int A_D_C    =94;  // D reg C
  public static final int A_D_D    =95;  // D reg D
  public static final int A_D_E    =96;  // D reg E
  public static final int A_D_H    =97;  // D reg A
  public static final int A_D_L    =98;  // D reg L
  public static final int A_E_A    =99;  // E reg A
  public static final int A_E_B    =100; // E reg B
  public static final int A_E_C    =101; // E reg C
  public static final int A_E_D    =102; // E reg D
  public static final int A_E_E    =103; // E reg E
  public static final int A_E_H    =104; // E reg A
  public static final int A_E_L    =105; // E reg L
  public static final int A_H_A    =106; // H reg A
  public static final int A_H_B    =107; // H reg B
  public static final int A_H_C    =108; // H reg C
  public static final int A_H_D    =109; // H reg D
  public static final int A_H_E    =110; // H reg E
  public static final int A_H_H    =111; // H reg A
  public static final int A_H_L    =112; // H reg L  
  public static final int A_L_A    =113; // L reg A
  public static final int A_L_B    =114; // L reg B
  public static final int A_L_C    =115; // L reg C
  public static final int A_L_D    =116; // L reg D
  public static final int A_L_E    =117; // L reg E
  public static final int A_L_H    =118; // L reg A
  public static final int A_L_L    =119; // L reg L 
  public static final int A_I_A    =120; // I reg A
  public static final int A_R_A    =121; // R reg A
  public static final int A_B__HL  =122; // B indirect (HL)
  public static final int A_B__IXN =123; // B indirect (IX+N)
  public static final int A_B__IYN =124; // B indirect (IY+N)
  public static final int A_C__HL  =125; // C indirect (HL)
  public static final int A_C__IXN =126; // C indirect (IX+N)
  public static final int A_C__IYN =127; // C indirect (IY+N)  
  public static final int A_D__HL  =128; // D indirect (HL)
  public static final int A_D__IXN =129; // D indirect (IX+N)
  public static final int A_D__IYN =130; // D indirect (IY+N)  
  public static final int A_E__HL  =131; // E indirect (HL)
  public static final int A_E__IXN =132; // E indirect (IX+N)
  public static final int A_E__IYN =133; // E indirect (IY+N)  
  public static final int A_H__HL  =134; // H indirect (HL)
  public static final int A_H__IXN =135; // H indirect (IX+N)
  public static final int A_H__IYN =136; // H indirect (IY+N)
  public static final int A_L__HL  =137; // L indirect (HL)
  public static final int A_L__IXN =138; // L indirect (IX+N)
  public static final int A_L__IYN =139; // L indirect (IY+N)
  public static final int A__HL_B  =140; // (HL) indirect B
  public static final int A__HL_C  =141; // (HL) indirect C
  public static final int A__HL_D  =142; // (HL) indirect D  
  public static final int A__HL_E  =143; // (HL) indirect E
  public static final int A__HL_H  =144; // (HL) indirect H
  public static final int A__HL_I  =145; // (HL) indirect I
  public static final int A__HL_L  =146; // (HL) indirect L
  public static final int A_00     =147; // 00h
  public static final int A_08     =148; // 08h
  public static final int A_10     =149; // 10h
  public static final int A_18     =150; // 18h
  public static final int A_20     =151; // 20h
  public static final int A_28     =152; // 28h
  public static final int A_30     =153; // 30h
  public static final int A_38     =154; // 38h
  public static final int A_NZ     =155; // NZ cond
  public static final int A_Z      =156; // Z cond
  public static final int A_NC     =157; // NC cond
  public static final int A_C      =158; // C cond
  public static final int A_PO     =159; // PO cond
  public static final int A_P      =160; // P cond
  public static final int A_PE     =161; // PE cond
  public static final int A_M      =162; // PE cond
  public static final int A_N      =163; // immediate N
  public static final int A_NN     =164; // absolute NN
  public static final int A_REG_AF =165; // reg AF
  public static final int A__N_A   =166; // (N) immediate A
  public static final int A_A__N   =167; // A immediate (N) 
  public static final int A_SP_HL  =168; // SP reg HL
  public static final int A_DE_HL  =169; // DE reg HL
  public static final int A__SP_HL =170; // (SP) ind  HL
  public static final int A_NZ_NN  =171; // NZ cond NN
  public static final int A_Z_NN   =172; // Z cond NN
  public static final int A_NC_NN  =173; // NC cond NN
  public static final int A_C_NN   =174; // C cond NN
  public static final int A_PO_NN  =175; // PO cond NN
  public static final int A_P_NN   =176; // P cond NN
  public static final int A_PE_NN  =177; // PE cond NN
  public static final int A_M_NN   =178; // PE cond NN
  public static final int A_A__C   =179; // A reg ind (C)
  public static final int A_B__C   =180; // B reg ind (C)
  public static final int A_C__C   =181; // C reg ind (C)
  public static final int A_D__C   =182; // D reg ind (C)
  public static final int A_E__C   =183; // E reg ind (C)
  public static final int A_H__C   =184; // H reg ind (C)
  public static final int A_L__C   =185; // L reg ind (C)
  public static final int A___C    =186; // ind (C)
  public static final int A__C_A   =187; // ind C reg A 
  public static final int A__C_B   =188; // ind C reg B
  public static final int A__C_C   =189; // ind C reg C
  public static final int A__C_D   =190; // ind C reg D
  public static final int A__C_E   =191; // ind C reg E
  public static final int A__C_H   =192; // ind C reg H
  public static final int A__C_L   =193; // ind C reg L
  public static final int A___C_0  =194; // ind C 0
  public static final int A_0      =195; // 0
  public static final int A_1      =196; // 1
  public static final int A_2      =197; // 2
  public static final int A_0_A    =198; // 0 reg A
  public static final int A_0_B    =199; // 0 reg B
  public static final int A_0_C    =200; // 0 reg C
  public static final int A_0_D    =201; // 0 reg D
  public static final int A_0_E    =202; // 0 reg E
  public static final int A_0_H    =203; // 0 reg H
  public static final int A_0_L    =204; // 0 reg L
  public static final int A_0__HL  =205; // 0 ind (HL)
  public static final int A_1_A    =206; // 1 reg A
  public static final int A_1_B    =207; // 1 reg B
  public static final int A_1_C    =208; // 1 reg C
  public static final int A_1_D    =209; // 1 reg D
  public static final int A_1_E    =210; // 1 reg E
  public static final int A_1_H    =211; // 1 reg H
  public static final int A_1_L    =212; // 1 reg L
  public static final int A_1__HL  =213; // 1 ind (HL)
  public static final int A_2_A    =214; // 2 reg A
  public static final int A_2_B    =215; // 2 reg B
  public static final int A_2_C    =216; // 2 reg C
  public static final int A_2_D    =217; // 2 reg D
  public static final int A_2_E    =218; // 2 reg E
  public static final int A_2_H    =219; // 2 reg H
  public static final int A_2_L    =220; // 2 reg L
  public static final int A_2__HL  =221; // 2 ind (HL)
  public static final int A_3_A    =222; // 3 reg A
  public static final int A_3_B    =223; // 3 reg B
  public static final int A_3_C    =224; // 3 reg C
  public static final int A_3_D    =225; // 3 reg D
  public static final int A_3_E    =226; // 3 reg E
  public static final int A_3_H    =227; // 3 reg H
  public static final int A_3_L    =228; // 3 reg L
  public static final int A_3__HL  =229; // 3 ind (HL)
  public static final int A_4_A    =230; // 4 reg A
  public static final int A_4_B    =231; // 4 reg B
  public static final int A_4_C    =232; // 4 reg C
  public static final int A_4_D    =233; // 4 reg D
  public static final int A_4_E    =234; // 4 reg E
  public static final int A_4_H    =235; // 4 reg H
  public static final int A_4_L    =236; // 4 reg L
  public static final int A_4__HL  =237; // 4 ind (HL)
  public static final int A_5_A    =238; // 5 reg A
  public static final int A_5_B    =239; // 5 reg B
  public static final int A_5_C    =240; // 5 reg C
  public static final int A_5_D    =241; // 5 reg D
  public static final int A_5_E    =242; // 5 reg E
  public static final int A_5_H    =243; // 5 reg H
  public static final int A_5_L    =244; // 5 reg L
  public static final int A_5__HL  =245; // 5 ind (HL)
  public static final int A_6_A    =246; // 6 reg A
  public static final int A_6_B    =247; // 6 reg B
  public static final int A_6_C    =248; // 6 reg C
  public static final int A_6_D    =249; // 6 reg D
  public static final int A_6_E    =250; // 6 reg E
  public static final int A_6_H    =251; // 6 reg H
  public static final int A_6_L    =252; // 6 reg L
  public static final int A_6__HL  =253; // 6 ind (HL)
  public static final int A_7_A    =254; // 7 reg A
  public static final int A_7_B    =255; // 7 reg B
  public static final int A_7_C    =256; // 7 reg C
  public static final int A_7_D    =257; // 7 reg D
  public static final int A_7_E    =258; // 7 reg E
  public static final int A_7_H    =259; // 7 reg H
  public static final int A_7_L    =260; // 7 reg L
  public static final int A_7__HL  =261; // 7 ind (HL)
  public static final int A_REG_IX =262; // reg IX
  public static final int A_REG_IY =263; // reg IY
  public static final int A_REG_IXH=264; // reg IXH
  public static final int A_REG_IXL=265; // reg IXL
  public static final int A_REG_IYH=266; // reg IYH
  public static final int A_REG_IYL=267; //reg IYL
  public static final int A_IX_IX  =268; // IX reg IX
  public static final int A_IY_IY  =269; // IY reg IY
  public static final int A__IX_N  =270; // ind (IX+N)
  public static final int A__IY_N  =271; // ind (IX+N)
  public static final int A__IX_N_A=272; // ind (IX+N),A
  public static final int A__IX_N_B=273; // ind (IX+N),B
  public static final int A__IX_N_C=274; // ind (IX+N),C
  public static final int A__IX_N_D=275; // ind (IX+N),D
  public static final int A__IX_N_E=276; // ind (IX+N),E
  public static final int A__IX_N_H=277; // ind (IX+N),H
  public static final int A__IX_N_L=278; // ind (IX+N),L
  public static final int A__IY_N_A=279; // ind (IY+N),A
  public static final int A__IY_N_B=280; // ind (IY+N),B
  public static final int A__IY_N_C=281; // ind (IY+N),C
  public static final int A__IY_N_D=282; // ind (IY+N),D
  public static final int A__IY_N_E=283; // ind (IY+N),E
  public static final int A__IY_N_H=284; // ind (IY+N),H
  public static final int A__IY_N_L=285; // ind (IY+N),L
  public static final int A_SP_IX  =286; // SP reg IX
  public static final int A_SP_IY  =287; // SP reg IY
  public static final int A__IX    =288; // ind (IX)
  public static final int A__IY    =289; // ind (IY)
  public static final int A__SP_IX =290; // (SP) ind  IX
  public static final int A__SP_IY =291; // (SP) ind  IY
  public static final int A_IXH_A = 292; // IXH reg A
  public static final int A_IXH_B = 293; // IXH reg B
  public static final int A_IXH_C = 294; // IXH reg C
  public static final int A_IXH_D = 295; // IXH reg D
  public static final int A_IXH_E = 296; // IXH reg E
  public static final int A_IXH_H = 297; // IXH reg H
  public static final int A_IXH_L = 298; // IXH reg L
  public static final int A_IYH_A = 299; // IYH reg A
  public static final int A_IYH_B = 300; // IYH reg B
  public static final int A_IYH_C = 301; // IYH reg C
  public static final int A_IYH_D = 302; // IYH reg D
  public static final int A_IYH_E = 303; // IYH reg E
  public static final int A_IYH_H = 304; // IYH reg H
  public static final int A_IYH_L = 305; // IYH reg L
  public static final int A_A_IXH = 306; // A reg IXH
  public static final int A_B_IXH = 307; // B reg IXH
  public static final int A_C_IXH = 308; // C reg IXH
  public static final int A_D_IXH = 309; // D reg IXH
  public static final int A_E_IXH = 310; // E reg IXH
  public static final int A_A_IXL = 311; // A reg IXL
  public static final int A_B_IXL = 312; // B reg IXL
  public static final int A_C_IXL = 313; // C reg IXL
  public static final int A_D_IXL = 314; // D reg IXL
  public static final int A_E_IXL = 315; // E reg IXL  
  public static final int A_IXL_A = 316; // IXL reg A
  public static final int A_IXL_B = 317; // IXL reg B
  public static final int A_IXL_C = 318; // IXL reg C
  public static final int A_IXL_D = 319; // IXL reg D
  public static final int A_IXL_E = 320; // IXL reg E
  public static final int A_IXL_L = 321; // IXL reg L
  public static final int A_IXH_IXH=322; // IXH reg IXH
  public static final int A_IXH_IXL=323; // IXH reg IXL
  public static final int A_IXL_IXH=324; // IXL reg IXH
  public static final int A_IXL_IXL=325; // IXL reg IXL
  public static final int A__IX_N_N=326; // ind (IX+N),N
  public static final int A__IY_N_N=327; // ind (IX+N),N
  public static final int A_C__IX_N=328; // C ind (IX+N)
  public static final int A_D__IX_N=329; // D ind (IX+N)
  public static final int A_E__IX_N=330; // E ind (IX+N)
  public static final int A_H__IX_N=331; // H ind (IX+N)
  public static final int A_L__IX_N=332; // L ind (IX+N)
  public static final int A_IYL_A = 333; // IYL reg A
  public static final int A_IYL_B = 334; // IYL reg B
  public static final int A_IYL_C = 335; // IYL reg C
  public static final int A_IYL_D = 336; // IYL reg D
  public static final int A_IYL_E = 337; // IYL reg E
  public static final int A_IYL_L = 338; // IYL reg L
  public static final int A_A_IYH = 339; // A reg IYH
  public static final int A_B_IYH = 340; // B reg IYH
  public static final int A_C_IYH = 341; // C reg IYH
  public static final int A_D_IYH = 342; // D reg IYH
  public static final int A_E_IYH = 343; // E reg IYH
  public static final int A_A_IYL = 344; // A reg IYL
  public static final int A_B_IYL = 345; // B reg IYL
  public static final int A_C_IYL = 346; // C reg IYL
  public static final int A_D_IYL = 347; // D reg IYL
  public static final int A_E_IYL = 348; // E reg IYL
  public static final int A_IYH_IYH=349; // IYH reg IYH
  public static final int A_IYH_IYL=350; // IYH reg IYL
  public static final int A_IYL_IYH=351; // IYL reg IYH
  public static final int A_IYL_IYL=352; // IYL reg IYL
  public static final int A_A__IY_N=353; // A ind (IY+N)
  public static final int A_B__IY_N=354; // B ind (IY+N)
  public static final int A_C__IY_N=355; // C ind (IY+N)
  public static final int A_D__IY_N=356; // D ind (IY+N)
  public static final int A_E__IY_N=357; // E ind (IY+N)
  public static final int A_H__IY_N=358; // H ind (IY+N)
  public static final int A_L__IY_N=359; // L ind (IY+N)
  public static final int A_IXH_N  =360; // IXH,N
  public static final int A_IXL_N  =361; // IXL,N
  public static final int A_IYH_N  =362; // IYH,N
  public static final int A_IYL_N  =363; // IYL,N
  public static final int A_0__IX_N=364; // 0,(IX+N) 
  public static final int A_1__IX_N=365; // 1,(IX+N)
  public static final int A_2__IX_N=366; // 2,(IX+N)
  public static final int A_3__IX_N=367; // 3,(IX+N)
  public static final int A_4__IX_N=368; // 4,(IX+N)
  public static final int A_5__IX_N=369; // 5,(IX+N)
  public static final int A_6__IX_N=370; // 6,(IX+N)
  public static final int A_7__IX_N=371; // 7,(IX+N)
  public static final int A_0__IY_N=372; // 0,(IY+N) 
  public static final int A_1__IY_N=373; // 1,(IY+N)
  public static final int A_2__IY_N=374; // 2,(IY+N)
  public static final int A_3__IY_N=375; // 3,(IY+N)
  public static final int A_4__IY_N=376; // 4,(IY+N)
  public static final int A_5__IY_N=377; // 5,(Iy+N)
  public static final int A_6__IY_N=378; // 6,(IY+N)
  public static final int A_7__IY_N=379; // 7,(IY+N)
  
  public static final int A_0__IX_N_A=380; // 0,(IX+N),A
  public static final int A_0__IX_N_B=381; // 0,(IX+N),B
  public static final int A_0__IX_N_C=382; // 0,(IX+N),C
  public static final int A_0__IX_N_D=383; // 0,(IX+N),D
  public static final int A_0__IX_N_E=384; // 0,(IX+N),E
  public static final int A_0__IX_N_H=385; // 0,(IX+N),H
  public static final int A_0__IX_N_L=386; // 0,(IX+N),L
  public static final int A_1__IX_N_A=387; // 1,(IX+N),A
  public static final int A_1__IX_N_B=388; // 1,(IX+N),B
  public static final int A_1__IX_N_C=389; // 1,(IX+N),C
  public static final int A_1__IX_N_D=390; // 1,(IX+N),D
  public static final int A_1__IX_N_E=391; // 1,(IX+N),E
  public static final int A_1__IX_N_H=392; // 1,(IX+N),H
  public static final int A_1__IX_N_L=393; // 1,(IX+N),L
  public static final int A_2__IX_N_A=394; // 2,(IX+N),A
  public static final int A_2__IX_N_B=395; // 2,(IX+N),B
  public static final int A_2__IX_N_C=396; // 2,(IX+N),C
  public static final int A_2__IX_N_D=397; // 2,(IX+N),D
  public static final int A_2__IX_N_E=398; // 2,(IX+N),E
  public static final int A_2__IX_N_H=399; // 2,(IX+N),H
  public static final int A_2__IX_N_L=400; // 2,(IX+N),L
  public static final int A_3__IX_N_A=401; // 3,(IX+N),A
  public static final int A_3__IX_N_B=402; // 3,(IX+N),B
  public static final int A_3__IX_N_C=403; // 3,(IX+N),C
  public static final int A_3__IX_N_D=404; // 3,(IX+N),D
  public static final int A_3__IX_N_E=405; // 3,(IX+N),E
  public static final int A_3__IX_N_H=406; // 3,(IX+N),H
  public static final int A_3__IX_N_L=407; // 3,(IX+N),L
  public static final int A_4__IX_N_A=408; // 4,(IX+N),A
  public static final int A_4__IX_N_B=409; // 4,(IX+N),B
  public static final int A_4__IX_N_C=410; // 4,(IX+N),C
  public static final int A_4__IX_N_D=411; // 4,(IX+N),D
  public static final int A_4__IX_N_E=412; // 4,(IX+N),E
  public static final int A_4__IX_N_H=413; // 4,(IX+N),H
  public static final int A_4__IX_N_L=414; // 4,(IX+N),L
  public static final int A_5__IX_N_A=415; // 5,(IX+N),A
  public static final int A_5__IX_N_B=416; // 5,(IX+N),B
  public static final int A_5__IX_N_C=417; // 5,(IX+N),C
  public static final int A_5__IX_N_D=418; // 5,(IX+N),D
  public static final int A_5__IX_N_E=419; // 5,(IX+N),E
  public static final int A_5__IX_N_H=420; // 5,(IX+N),H
  public static final int A_5__IX_N_L=421; // 5,(IX+N),L
  public static final int A_6__IX_N_A=422; // 6,(IX+N),A
  public static final int A_6__IX_N_B=423; // 6,(IX+N),B
  public static final int A_6__IX_N_C=424; // 6,(IX+N),C
  public static final int A_6__IX_N_D=425; // 6,(IX+N),D
  public static final int A_6__IX_N_E=426; // 6,(IX+N),E
  public static final int A_6__IX_N_H=427; // 6,(IX+N),H
  public static final int A_6__IX_N_L=428; // 6,(IX+N),L
  public static final int A_7__IX_N_A=429; // 7,(IX+N),A
  public static final int A_7__IX_N_B=430; // 7,(IX+N),B
  public static final int A_7__IX_N_C=431; // 7,(IX+N),C
  public static final int A_7__IX_N_D=432; // 7,(IX+N),D
  public static final int A_7__IX_N_E=433; // 7,(IX+N),E
  public static final int A_7__IX_N_H=434; // 7,(IX+N),H
  public static final int A_7__IX_N_L=435; // 7,(IX+N),L
  public static final int A_0__IY_N_A=436; // 0,(IY+N),A
  public static final int A_0__IY_N_B=437; // 0,(IY+N),B
  public static final int A_0__IY_N_C=438; // 0,(IY+N),C
  public static final int A_0__IY_N_D=439; // 0,(IY+N),D
  public static final int A_0__IY_N_E=440; // 0,(IY+N),E
  public static final int A_0__IY_N_H=441; // 0,(IY+N),H
  public static final int A_0__IY_N_L=442; // 0,(IY+N),L
  public static final int A_1__IY_N_A=443; // 1,(IY+N),A
  public static final int A_1__IY_N_B=444; // 1,(IY+N),B
  public static final int A_1__IY_N_C=445; // 1,(IY+N),C
  public static final int A_1__IY_N_D=446; // 1,(IY+N),D
  public static final int A_1__IY_N_E=447; // 1,(IY+N),E
  public static final int A_1__IY_N_H=448; // 1,(IY+N),H
  public static final int A_1__IY_N_L=449; // 1,(IY+N),L
  public static final int A_2__IY_N_A=450; // 2,(IY+N),A
  public static final int A_2__IY_N_B=451; // 2,(IY+N),B
  public static final int A_2__IY_N_C=452; // 2,(IY+N),C
  public static final int A_2__IY_N_D=453; // 2,(IY+N),D
  public static final int A_2__IY_N_E=454; // 2,(IY+N),E
  public static final int A_2__IY_N_H=455; // 2,(IY+N),H
  public static final int A_2__IY_N_L=456; // 2,(IY+N),L
  public static final int A_3__IY_N_A=457; // 3,(IY+N),A
  public static final int A_3__IY_N_B=458; // 3,(IY+N),B
  public static final int A_3__IY_N_C=459; // 3,(IY+N),C
  public static final int A_3__IY_N_D=460; // 3,(IY+N),D
  public static final int A_3__IY_N_E=461; // 3,(IY+N),E
  public static final int A_3__IY_N_H=462; // 3,(IY+N),H
  public static final int A_3__IY_N_L=463; // 3,(IY+N),L
  public static final int A_4__IY_N_A=464; // 4,(IY+N),A
  public static final int A_4__IY_N_B=465; // 4,(IY+N),B
  public static final int A_4__IY_N_C=466; // 4,(IY+N),C
  public static final int A_4__IY_N_D=467; // 4,(IY+N),D
  public static final int A_4__IY_N_E=468; // 4,(IY+N),E
  public static final int A_4__IY_N_H=469; // 4,(IY+N),H
  public static final int A_4__IY_N_L=470; // 4,(IY+N),L
  public static final int A_5__IY_N_A=471; // 5,(IY+N),A
  public static final int A_5__IY_N_B=472; // 5,(IY+N),B
  public static final int A_5__IY_N_C=473; // 5,(IY+N),C
  public static final int A_5__IY_N_D=474; // 5,(IY+N),D
  public static final int A_5__IY_N_E=475; // 5,(IY+N),E
  public static final int A_5__IY_N_H=476; // 5,(IY+N),H
  public static final int A_5__IY_N_L=477; // 5,(IY+N),L
  public static final int A_6__IY_N_A=478; // 6,(IY+N),A
  public static final int A_6__IY_N_B=479; // 6,(IY+N),B
  public static final int A_6__IY_N_C=480; // 6,(IY+N),C
  public static final int A_6__IY_N_D=481; // 6,(IY+N),D
  public static final int A_6__IY_N_E=482; // 6,(IY+N),E
  public static final int A_6__IY_N_H=483; // 6,(IY+N),H
  public static final int A_6__IY_N_L=484; // 6,(IY+N),L
  public static final int A_7__IY_N_A=485; // 7,(IY+N),A
  public static final int A_7__IY_N_B=486; // 7,(IY+N),B
  public static final int A_7__IY_N_C=487; // 7,(IY+N),C
  public static final int A_7__IY_N_D=488; // 7,(IY+N),D
  public static final int A_7__IY_N_E=489; // 7,(IY+N),E
  public static final int A_7__IY_N_H=490; // 7,(IY+N),H
  public static final int A_7__IY_N_L=491; // 7,(IY+N),L
  
  /** Contains the mnemonics of instructions */
  public static final String[] mnemonics={
    // legal instruction first:
    "ADC",
    "ADD", 
    "AND", 
    "BIT", 
    "CALL",
    "CCF", 
    "CP",  
    "CPD", 
    "CPDR",
    "CPI", 
    "CPIR",
    "CPL", 
    "DAA", 
    "DEC", 
    "DI",  
    "DJNZ",
    "EI",  
    "EX",  
    "EXX", 
    "HALT",
    "IM",  
    "IN",  
    "INC", 
    "IND", 
    "INDR",
    "INI", 
    "INIR",
    "JP", 
    "JR",  
    "LD", 
    "LDD", 
    "LDDR",
    "LDI", 
    "LDIR",
    "NEG", 
    "NOP", 
    "OR", 
    "OTDR",
    "OTIR",
    "OUT", 
    "OUTD",
    "OUTI",
    "POP", 
    "PUSH",
    "RES", 
    "RET", 
    "RETI",
    "RETN",
    "RL",              
    "RLA", 
    "RLC", 
    "RLCA",
    "RLD", 
    "RR",  
    "RRA", 
    "RRC", 
    "RRCA",
    "RRD", 
    "RST", 
    "SBC", 
    "SCF", 
    "SET", 
    "SLA", 
    "SLI", 
    "SRA", 
    "SRL", 
    "STOP",
    "SUB", 
    "XOR",
    
    "???",
    "SLL"
  };    
  
  
  /** Contains the mnemonics reference for the instruction */
  public static final byte[] tableMnemonics={
    M_NOP,  M_LD,  M_LD,  M_INC, M_INC,  M_DEC, M_LD,  M_RLCA,   // 00
    M_EX,   M_ADD, M_LD,  M_DEC, M_INC,  M_DEC, M_LD,  M_RRCA,
    M_DJNZ, M_LD,  M_LD,  M_INC, M_INC,  M_DEC, M_LD,  M_RLA, 
    M_JR,   M_ADD, M_LD,  M_DEC, M_INC,  M_DEC, M_LD,  M_RRA, 
    M_JR,   M_LD,  M_LD,  M_INC, M_INC,  M_DEC, M_LD,  M_DAA,    // 20
    M_JR,   M_ADD, M_LD,  M_DEC, M_INC,  M_DEC, M_LD,  M_CPL, 
    M_JR,   M_LD,  M_LD,  M_INC, M_INC,  M_DEC, M_LD,  M_SCF,
    M_JR,   M_ADD, M_LD,  M_DEC, M_INC,  M_DEC, M_LD,  M_CCF,
    M_LD,   M_LD,  M_LD,  M_LD,  M_LD,   M_LD,  M_LD,  M_LD,    // 40
    M_LD,   M_LD,  M_LD,  M_LD,  M_LD,   M_LD,  M_LD,  M_LD,
    M_LD,   M_LD,  M_LD,  M_LD,  M_LD,   M_LD,  M_LD,  M_LD,    
    M_LD,   M_LD,  M_LD,  M_LD,  M_LD,   M_LD,  M_LD,  M_LD,
    M_LD,   M_LD,  M_LD,  M_LD,  M_LD,   M_LD,  M_LD,  M_LD,    // 60
    M_LD,   M_LD,  M_LD,  M_LD,  M_LD,   M_LD,  M_LD,  M_LD,
    M_LD,   M_LD,  M_LD,  M_LD,  M_LD,   M_LD,  M_HALT,M_LD,
    M_LD,   M_LD,  M_LD,  M_LD,  M_LD,   M_LD,  M_LD,  M_LD,
    M_ADD,  M_ADD, M_ADD, M_ADD, M_ADD,  M_ADD, M_ADD, M_ADD,   // 80
    M_ADC,  M_ADC, M_ADC, M_ADC, M_ADC,  M_ADC, M_ADC, M_ADC, 
    M_SUB,  M_SUB, M_SUB, M_SUB, M_SUB,  M_SUB, M_SUB, M_SUB, 
    M_SBC,  M_SBC, M_SBC, M_SBC, M_SBC,  M_SBC, M_SBC, M_SBC,
    M_AND,  M_AND, M_AND, M_AND, M_AND,  M_AND, M_AND, M_AND,   // A0
    M_XOR,  M_XOR, M_XOR, M_XOR, M_XOR,  M_XOR, M_XOR, M_XOR, 
    M_OR,   M_OR,  M_OR,  M_OR,  M_OR,   M_OR,  M_OR,  M_OR,
    M_CP,   M_CP,  M_CP,  M_CP,  M_CP,   M_CP,  M_CP,  M_CP,
    M_RET,  M_POP, M_JP,  M_JP,  M_CALL, M_PUSH,M_ADD, M_RST,   // C0
    M_RET,  M_RET, M_JP,  T_CB,  M_CALL, M_CALL,M_ADC, M_RST,
    M_RET,  M_POP, M_JP,  M_OUT, M_CALL, M_PUSH,M_SUB, M_RST,
    M_RET,  M_EXX, M_JP,  M_IN,  M_CALL, T_DD,  M_SBC, M_RST,
    M_RET,  M_POP, M_JP,  M_EX,  M_CALL, M_PUSH,M_AND, M_RST,   // E0
    M_RET,  M_JP,  M_JP,  M_EX,  M_CALL, T_ED,  M_XOR, M_RST,
    M_RET,  M_POP, M_JP,  M_DI,  M_CALL, M_PUSH,M_OR,  M_RST,
    M_RET,  M_LD,  M_JP,  M_EI,  M_CALL, T_FD,  M_CP,  M_RST    
  };
  
  /** Contains the modes for the instruction */
  public static final int[] tableModes={
    A_NUL,   A_BC_NN, A__BC_A, A_REG_BC, A_REG_B, A_REG_B, A_B_N,  A_NUL,  // 00
    A_AF_AF, A_HL_BC, A_A__BC, A_REG_BC, A_REG_C, A_REG_C, A_C_N,  A_NUL,    
    A_REL,   A_DE_NN, A__DE_A, A_REG_DE, A_REG_D, A_REG_D, A_D_N,  A_NUL, 
    A_REL,   A_HL_DE, A_A__DE, A_REG_DE, A_REG_E, A_REG_E, A_E_N,  A_NUL,
    A_REL_NZ,A_HL_NN, A__NN_HL,A_REG_HL, A_REG_H, A_REG_H, A_H_N,  A_NUL,  // 20
    A_REL_Z, A_HL_HL, A_HL__NN,A_REG_HL, A_REG_L, A_REG_L, A_L_N,  A_NUL,
    A_REL_NC,A_SP_NN, A__NN_A, A_REG_SP, A__HL,   A__HL,   A__HL_N,A_NUL, 
    A_REL_C, A_HL_SP, A_A__NN, A_REG_SP, A_REG_A, A_REG_A, A_A_N,  A_NUL,
    A_B_B,   A_B_C,   A_B_D,   A_B_E,    A_B_H,   A_B_L,   A_B__HL, A_B_A, // 40
    A_C_D,   A_C_C,   A_C_D,   A_C_E,    A_C_H,   A_C_L,   A_C__HL, A_C_A,
    A_D_B,   A_D_C,   A_D_D,   A_D_E,    A_D_H,   A_D_L,   A_D__HL, A_D_A,
    A_E_B,   A_E_C,   A_E_D,   A_E_E,    A_E_H,   A_E_L,   A_E__HL, A_E_A,
    A_H_B,   A_H_C,   A_H_D,   A_H_E,    A_H_H,   A_H_L,   A_H__HL, A_H_A, // 60
    A_L_B,   A_L_C,   A_L_D,   A_L_E,    A_L_H,   A_L_L,   A_L__HL, A_L_A,
    A__HL_B, A__HL_C, A__HL_D, A__HL_E,  A__HL_H, A__HL_L, A_NUL,   A__HL_A,
    A_A_B,   A_A_C,   A_A_D,   A_A_E,    A_A_H,   A_A_L,   A_A__HL, A_A_A,
    A_A_B,   A_A_C,   A_A_D,   A_A_E,    A_A_H,   A_A_L,   A_A__HL, A_A_A, // 80    
    A_A_B,   A_A_C,   A_A_D,   A_A_E,    A_A_H,   A_A_L,   A_A__HL, A_A_A,
    A_REG_B, A_REG_C, A_REG_D, A_REG_E,  A_REG_H, A_REG_L, A__HL,   A_REG_A,
    A_A_B,   A_A_C,   A_A_D,   A_A_E,    A_A_H,   A_A_L,   A_A__HL, A_A_A,
    A_REG_B, A_REG_C, A_REG_D, A_REG_E,  A_REG_H, A_REG_L, A__HL,   A_REG_A,//A0
    A_REG_B, A_REG_C, A_REG_D, A_REG_E,  A_REG_H, A_REG_L, A__HL,   A_REG_A,
    A_REG_B, A_REG_C, A_REG_D, A_REG_E,  A_REG_H, A_REG_L, A__HL,   A_REG_A,
    A_REG_B, A_REG_C, A_REG_D, A_REG_E,  A_REG_H, A_REG_L, A__HL,   A_REG_A,
    A_NZ,    A_REG_BC,A_NZ_NN, A_NN,     A_NZ_NN, A_REG_BC,A_A_N,   A_00,  // C0 
    A_Z,     A_NUL,   A_Z_NN,  0,        A_Z_NN,  A_NN,    A_A_N,   A_08,
    A_NC,    A_REG_DE,A_NC_NN, A__N_A,   A_NC_NN, A_REG_DE,A_N,     A_10,
    A_C,     A_NUL,   A_C_NN,  A_A__N,   A_C_NN,  0,       A_A_N,   A_18,
    A_PO,    A_REG_HL,A_PO_NN, A__SP_HL, A_PO_NN, A_REG_HL,A_N,     A_20,   //E0
    A_PE,    A__HL,   A_PE_NN, A_DE_HL,  A_PE_NN, 0,       A_N,     A_28,
    A_P,     A_REG_AF,A_P_NN,  A_NUL,    A_P_NN,  A_REG_AF,A_N,     A_30,
    A_M,     A_SP_HL, A_M_NN,  A_NUL,    A_M_NN,  0,       A_N,     A_38
  };
  
  /** Contains the bytes used for the instruction */
  public static final byte[] tableSize={
    1, 3, 1, 1, 1, 1, 2, 1,     // 00
    1, 1, 1, 1, 1, 1, 2, 1,
    2, 3, 1, 1, 1, 1, 2, 1, 
    2, 1, 1, 1, 1, 1, 2, 1,
    2, 3, 3, 1, 1, 1, 2, 1,     // 20  
    2, 1, 3, 1, 1, 1, 2, 1, 
    2, 3, 3, 1, 1, 1, 2, 1, 
    2, 1, 3, 1, 1, 1, 2, 1,
    1, 1, 1, 1, 1, 1, 1, 1,     // 40
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,    
    1, 1, 1, 1, 1, 1, 1, 1,     // 60
    1, 1, 1, 1, 1, 1, 1, 1, 
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,     // 80
    1, 1, 1, 1, 1, 1, 1, 1, 
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,     // A0
    1, 1, 1, 1, 1, 1, 1, 1,  
    1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1,   
    1, 1, 3, 3, 3, 1, 2, 1,     // C0
    1, 1, 3, 0, 3, 3, 2, 1,
    1, 1, 3, 2, 3, 1, 2, 1,
    1, 1, 3, 2, 3, 0, 2, 1,
    1, 1, 3, 1, 3, 1, 2, 1,     // E0
    1, 1, 3, 1, 3, 0, 2, 1,
    1, 1, 3, 1, 3, 1, 2, 1,
    1, 1, 3, 1, 3, 0, 2, 1
  };
  
  /** Contains the mnemonics reference for the instruction */
  public static final byte[] tableMnemonicsED={
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,  // 00
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,  // 20
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_IN,  M_OUT, M_SBC, M_LD,  M_NEG,M_RETN, M_IM,  M_LD,  // 40
    M_IN,  M_OUT, M_ADC, M_LD,  M_NEG,M_RETI, M_IM,  M_LD,
    M_IN,  M_OUT, M_SBC, M_LD,  M_NEG,M_RETN, M_IM,  M_LD,
    M_IN,  M_OUT, M_ADC, M_LD,  M_NEG,M_RETN, M_IM,  M_LD,
    M_IN,  M_OUT, M_SBC, M_LD,  M_NEG,M_RETN, M_IM,  M_RRD, // 60
    M_IN,  M_OUT, M_ADC, M_LD,  M_NEG,M_RETN, M_IM,  M_RLD,
    M_IN,  M_OUT, M_SBC, M_LD,  M_NEG,M_RETN, M_IM,  M_NUL,    
    M_IN,  M_OUT, M_ADC, M_LD,  M_NEG,M_RETN, M_IM,  M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,  // 80
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_LDI, M_CPI, M_INI, M_OUTI,M_NUL,M_NUL, M_NUL, M_NUL,  // A0
    M_LDD, M_CPD, M_IND, M_OUTD,M_NUL,M_NUL, M_NUL, M_NUL,
    M_LDIR,M_CPIR,M_INIR,M_OTIR,M_NUL,M_NUL, M_NUL, M_NUL,
    M_LDDR,M_CPDR,M_INDR,M_OTDR,M_NUL,M_NUL, M_NUL, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, // C0
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, // E0 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL
  };
  
  /** Contains the mnemonics reference for the instruction */
  public static final int[] tableModesED={
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,  // 00
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,  // 20
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_B__C,A__C_A,A_HL_BC,A__NN_BC,A_NUL, A_NUL, A_0,  A_I_A,  // 40
    A_C__C,A__C_C,A_HL_BC,A_BC__NN,A_NUL, A_NUL, A_0,  A_R_A,
    A_D__C,A__C_D,A_HL_DE,A__NN_DE,A_NUL, A_NUL, A_1,  A_A_I,
    A_E__C,A__C_E,A_HL_DE,A_DE__NN,A_NUL, A_NUL, A_2,  A_A_R,
    A_H__C,A__C_H,A_HL_HL,A__NN_HL,A_NUL, A_NUL, A_0,  A_NUL,  // 60
    A_L__C,A__C_L,A_HL_HL,A_HL__NN,A_NUL, A_NUL, A_0,  A_NUL, 
    A___C, A___C_0,A_HL_SP,A__NN_SP,A_NUL,A_NUL, A_1,  A_NUL,
    A_A__C,A__C_A,A_HL_SP,A_SP__NN, A_NUL,A_NUL, A_2,  A_NUL,
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,  // 80
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,  // A0
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, // C0
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, // E0 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL,
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL, 
    A_NUL, A_NUL, A_NUL,  A_NUL,   A_NUL, A_NUL, A_NUL, A_NUL
  };
  
  /** Contains the bytes used for the instruction */
  public static final byte[] tableSizeED={
    2, 2, 2, 2, 2, 2, 2, 2,     // 00
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // 20  
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 4, 2, 2, 2, 2,     // 40
    2, 2, 2, 4, 2, 2, 2, 2,
    2, 2, 2, 4, 2, 2, 2, 2,
    2, 2, 2, 4, 2, 2, 2, 2,    
    2, 2, 2, 4, 2, 2, 2, 2,     // 60
    2, 2, 2, 4, 2, 2, 2, 2, 
    2, 2, 2, 4, 2, 2, 2, 2,
    2, 2, 2, 4, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // 80
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // A0
    2, 2, 2, 2, 2, 2, 2, 2,  
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,   
    2, 2, 2, 2, 2, 2, 2, 2,     // C0
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // E0
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2
  };
  
  
  /** Contains the mnemonics reference for the instruction */
  public static final byte[] tableMnemonicsCB={
    M_RLC, M_RLC, M_RLC, M_RLC, M_RLC, M_RLC, M_RLC, M_RLC,   // 00
    M_RRC, M_RRC, M_RRC, M_RRC, M_RRC, M_RRC, M_RRC, M_RRC,
    M_RL,  M_RL,  M_RL,  M_RL,  M_RL,  M_RL,  M_RL,  M_RL,
    M_RR,  M_RR,  M_RR,  M_RR,  M_RR,  M_RR, M_RR,  M_RR,
    M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, M_SLA,   // 20
    M_SRA, M_SRA, M_SRA, M_SRA, M_SRA, M_SRA, M_SRA, M_SRA,
    M_SLL, M_SLL, M_SLL, M_SLL, M_SLL, M_SLL, M_SLL, M_SLL,
    M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, 
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,    // 40
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,    
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,    // 60
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,    // 80
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,    // A0
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,    // C0
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, 
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, 
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, 
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,    // E0
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, 
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, 
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET
  };
  
  /** Contains the mnemonics reference for the instruction */
  public static final int[] tableModesCB={
    A_REG_B, A_REG_C, A_REG_D, A_REG_E, A_REG_H, A_REG_L, A__HL,  A_REG_A, // 00
    A_REG_B, A_REG_C, A_REG_D, A_REG_E, A_REG_H, A_REG_L, A__HL,  A_REG_A,
    A_REG_B, A_REG_C, A_REG_D, A_REG_E, A_REG_H, A_REG_L, A__HL,  A_REG_A,
    A_REG_B, A_REG_C, A_REG_D, A_REG_E, A_REG_H, A_REG_L, A__HL,  A_REG_A,
    A_REG_B, A_REG_C, A_REG_D, A_REG_E, A_REG_H, A_REG_L, A__HL,  A_REG_A, // 20
    A_REG_B, A_REG_C, A_REG_D, A_REG_E, A_REG_H, A_REG_L, A__HL,  A_REG_A,
    A_REG_B, A_REG_C, A_REG_D, A_REG_E, A_REG_H, A_REG_L, A__HL,  A_REG_A,
    A_REG_B, A_REG_C, A_REG_D, A_REG_E, A_REG_H, A_REG_L, A__HL,  A_REG_A,
    A_0_B,   A_0_C,   A_0_D,   A_0_E,   A_0_H,   A_0_L,   A_0__HL,A_0_A,   // 40 
    A_1_B,   A_1_C,   A_1_D,   A_1_E,   A_1_H,   A_1_L,   A_1__HL,A_1_A,
    A_2_B,   A_2_C,   A_2_D,   A_2_E,   A_2_H,   A_2_L,   A_2__HL,A_2_A,
    A_3_B,   A_3_C,   A_3_D,   A_3_E,   A_3_H,   A_3_L,   A_3__HL,A_3_A,
    A_4_B,   A_4_C,   A_4_D,   A_4_E,   A_4_H,   A_4_L,   A_4__HL,A_4_A,   // 60
    A_5_B,   A_5_C,   A_5_D,   A_5_E,   A_5_H,   A_5_L,   A_5__HL,A_5_A,
    A_6_B,   A_6_C,   A_6_D,   A_6_E,   A_6_H,   A_6_L,   A_6__HL,A_6_A,
    A_7_B,   A_7_C,   A_7_D,   A_7_E,   A_7_H,   A_7_L,   A_7__HL,A_7_A,
    A_0_B,   A_0_C,   A_0_D,   A_0_E,   A_0_H,   A_0_L,   A_0__HL,A_0_A,   // 80 
    A_1_B,   A_1_C,   A_1_D,   A_1_E,   A_1_H,   A_1_L,   A_1__HL,A_1_A,
    A_2_B,   A_2_C,   A_2_D,   A_2_E,   A_2_H,   A_2_L,   A_2__HL,A_2_A,
    A_3_B,   A_3_C,   A_3_D,   A_3_E,   A_3_H,   A_3_L,   A_3__HL,A_3_A,
    A_4_B,   A_4_C,   A_4_D,   A_4_E,   A_4_H,   A_4_L,   A_4__HL,A_4_A,   // A0
    A_5_B,   A_5_C,   A_5_D,   A_5_E,   A_5_H,   A_5_L,   A_5__HL,A_5_A,
    A_6_B,   A_6_C,   A_6_D,   A_6_E,   A_6_H,   A_6_L,   A_6__HL,A_6_A,
    A_7_B,   A_7_C,   A_7_D,   A_7_E,   A_7_H,   A_7_L,   A_7__HL,A_7_A,
    A_0_B,   A_0_C,   A_0_D,   A_0_E,   A_0_H,   A_0_L,   A_0__HL,A_0_A,   // C0 
    A_1_B,   A_1_C,   A_1_D,   A_1_E,   A_1_H,   A_1_L,   A_1__HL,A_1_A,
    A_2_B,   A_2_C,   A_2_D,   A_2_E,   A_2_H,   A_2_L,   A_2__HL,A_2_A,
    A_3_B,   A_3_C,   A_3_D,   A_3_E,   A_3_H,   A_3_L,   A_3__HL,A_3_A,
    A_4_B,   A_4_C,   A_4_D,   A_4_E,   A_4_H,   A_4_L,   A_4__HL,A_4_A,   // E0
    A_5_B,   A_5_C,   A_5_D,   A_5_E,   A_5_H,   A_5_L,   A_5__HL,A_5_A,
    A_6_B,   A_6_C,   A_6_D,   A_6_E,   A_6_H,   A_6_L,   A_6__HL,A_6_A,
    A_7_B,   A_7_C,   A_7_D,   A_7_E,   A_7_H,   A_7_L,   A_7__HL,A_7_A    
  };
  
  
  /** Contains the bytes used for the instruction */
  public static final byte[] tableSizeCB={
    2, 2, 2, 2, 2, 2, 2, 2,     // 00
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // 20  
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // 40
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,    
    2, 2, 2, 2, 2, 2, 2, 2,     // 60
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // 80
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // A0
    2, 2, 2, 2, 2, 2, 2, 2,  
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,   
    2, 2, 2, 2, 2, 2, 2, 2,     // C0
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // E0
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2
  };  
  
  /** Contains the mnemonics reference for the instruction */
  public static final byte[] tableMnemonicsDD={
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,  // 00
    M_NUL, M_ADD, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_ADD, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_LD,  M_LD,  M_INC, M_INC, M_DEC, M_LD, M_NUL,  // 20
    M_NUL, M_ADD, M_LD,  M_DEC, M_INC, M_DEC, M_LD, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_INC, M_DEC, M_LD, M_NUL, 
    M_NUL, M_ADD, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD, M_NUL,  // 40
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD, M_LD,
    M_LD,  M_LD,  M_LD,  M_LD, M_LD,  M_LD,  M_LD,  M_LD, // 60
    M_LD,  M_LD,  M_LD,  M_LD, M_LD,  M_LD,  M_LD,  M_LD,
    M_LD,  M_LD,  M_LD,  M_LD, M_LD,  M_LD,  M_NUL, M_LD,    
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD,  M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_ADD, M_ADD, M_ADD, M_NUL,  // 80
    M_NUL, M_NUL, M_NUL, M_NUL, M_ADC, M_ADC, M_ADC, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_SUB, M_SUB, M_SUB, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_SBC, M_SBC, M_SBC, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_AND, M_AND, M_AND, M_NUL,  // A0
    M_NUL, M_NUL, M_NUL, M_NUL, M_XOR, M_XOR, M_XOR, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_OR,  M_OR,  M_OR, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_CP,  M_CP,  M_CP, M_CP,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, // C0
    M_NUL, M_NUL, M_NUL, T_DDCB,M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_POP, M_NUL, M_EX,  M_NUL, M_PUSH,M_NUL, M_NUL, // E0 
    M_NUL, M_JP,  M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_LD,  M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL
  };
  
  /** Contains the mnemonics reference for the instruction */
  public static final int[] tableModesDD={
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL,  // 00
    A_NUL,  A_IX_BC,A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL, 
    A_NUL,  A_IX_DE,A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL, 
    A_NUL,  A_IX_NN,A__NN_IX,A_REG_IX,A_REG_IXH,A_REG_IXH,A_IXH_N,A_NUL,  // 20
    A_NUL,  A_IX_IX,A_IX__NN,A_REG_IX,A_REG_IXL,A_REG_IXL,A_IXL_N,A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A__IX_N,  A__IX_N,  A__IX_N_N, A_NUL, 
    A_NUL,  A_IX_SP,A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL,     
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_B_IXH,  A_B_IXL,  A_B__IXN, A_NUL,  // 40
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_C_IXH,  A_C_IXL,  A_C__IXN, A_NUL,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_D_IXH,  A_D_IXL,  A_D__IXN, A_NUL,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_E_IXH,  A_E_IXL,  A_E__IXN, A_NUL,    
    A_IXH_B,A_IXH_C,A_IXH_D,A_IXH_E, A_IXH_IXH, A_IXH_L, A_H__IX_N, A_IXH_A,//60
    A_IXL_B,A_IXL_C,A_IXL_D,A_IXL_E, A_IXL_IXH, A_IXL_L, A_L__IX_N, A_IXL_A,            
    A__IX_N_B,A__IX_N_C,A__IX_N_D,A__IX_N_E,A__IX_N_H,A__IX_N_L, A_NUL,A__IX_N_A,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_A_IXH,   A_A_IXL,  A_A__IXN, A_NUL,    
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_A_IXH,   A_A_IXL,  A_A__IXN, A_NUL,  // 80    
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_A_IXH,   A_A_IXL,  A_A__IXN, A_NUL,    
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IXH, A_REG_IXL, A__IX_N, A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_A_IXH,   A_A_IXL,  A_A__IXN, A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IXH, A_REG_IXL, A__IX_N, A_NUL,  // A0
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IXH, A_REG_IXL, A__IX_N, A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IXH, A_REG_IXL, A__IX_N, A_NUL,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IXH, A_REG_IXL, A__IX_N, A_NUL,   
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL, // C0
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL,
    A_NUL, A_REG_IX,A_NUL,   A__SP_IX,A_REG_IX,  A_NUL,   A_NUL,     A_NUL, // E0 
    A_NUL, A__IX,   A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL,
    A_NUL, A_NUL,   A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL, 
    A_NUL, A_SP_IX, A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL
  };
  
  /** Contains the bytes used for the instruction */
  public static final byte[] tableSizeDD={
    2, 2, 2, 2, 2, 2, 2, 2,     // 00
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 4, 4, 2, 2, 2, 3, 2,     // 20  
    2, 2, 4, 2, 2, 2, 3, 2, 
    2, 2, 2, 2, 3, 3, 4, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 3, 2,     // 40
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,    
    2, 2, 2, 2, 2, 2, 3, 2,     // 60
    2, 2, 2, 2, 2, 2, 3, 2, 
    3, 3, 3, 3, 3, 3, 2, 3,
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,     // 80
    2, 2, 2, 2, 2, 2, 3, 2, 
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,     // A0
    2, 2, 2, 2, 2, 2, 3, 2,  
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,   
    2, 2, 2, 2, 2, 2, 2, 2,     // C0
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // E0
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2
  };
  
  /** Contains the mnemonics reference for the instruction */
  public static final byte[] tableMnemonicsFD={
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,  // 00
    M_NUL, M_ADD, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_ADD, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_LD,  M_LD,  M_INC, M_INC, M_DEC, M_LD, M_NUL,  // 20
    M_NUL, M_ADD, M_LD,  M_DEC, M_INC, M_DEC, M_LD, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_INC, M_DEC, M_LD, M_NUL, 
    M_NUL, M_ADD, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD, M_NUL,  // 40
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD, M_LD,
    M_LD,  M_LD,  M_LD,  M_LD, M_LD,  M_LD,  M_LD,  M_LD, // 60
    M_LD,  M_LD,  M_LD,  M_LD, M_LD,  M_LD,  M_LD,  M_LD,
    M_LD,  M_LD,  M_LD,  M_LD, M_LD,  M_LD,  M_NUL, M_LD,    
    M_NUL, M_NUL, M_NUL, M_NUL, M_LD,  M_LD,  M_LD,  M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_ADD, M_ADD, M_ADD, M_NUL,  // 80
    M_NUL, M_NUL, M_NUL, M_NUL, M_ADC, M_ADC, M_ADC, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_SUB, M_SUB, M_SUB, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_SBC, M_SBC, M_SBC, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_AND, M_AND, M_AND, M_NUL,  // A0
    M_NUL, M_NUL, M_NUL, M_NUL, M_XOR, M_XOR, M_XOR, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_OR,  M_OR,  M_OR, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_CP,  M_CP,  M_CP, M_CP,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, // C0
    M_NUL, M_NUL, M_NUL, T_FDCB,M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_POP, M_NUL, M_EX,  M_NUL, M_PUSH,M_NUL, M_NUL, // E0 
    M_NUL, M_JP,  M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL,
    M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, 
    M_NUL, M_LD,  M_NUL, M_NUL, M_NUL, M_NUL, M_NUL, M_NUL
  };
  
  /** Contains the mnemonics reference for the instruction */
  public static final int[] tableModesFD={
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL,  // 00
    A_NUL,  A_IY_BC,A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL, 
    A_NUL,  A_IY_DE,A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL, 
    A_NUL,  A_IY_NN,A__NN_IY,A_REG_IY,A_REG_IYH,A_REG_IYH,A_IYH_N, A_NUL,  // 20
    A_NUL,  A_IY_IY,A_IY__NN,A_REG_IY,A_REG_IYL,A_REG_IYL,A_IYL_N, A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A__IY_N,  A__IY_N,  A__IY_N_N, A_NUL, 
    A_NUL,  A_IY_SP,A_NUL,   A_NUL,   A_NUL,    A_NUL,    A_NUL,    A_NUL,     
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_B_IYH,  A_B_IYL,  A_B__IY_N, A_NUL,  // 40
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_C_IYH,  A_C_IYL,  A_C__IY_N, A_NUL,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_D_IYH,  A_D_IYL,  A_D__IY_N, A_NUL,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_E_IYH,  A_E_IYL,  A_E__IY_N, A_NUL,    
    A_IYH_B,A_IYH_C,A_IYH_D,A_IYH_E, A_IYH_IYH, A_IYH_L, A_H__IY_N, A_IYH_A,//60
    A_IYL_B,A_IYL_C,A_IYL_D,A_IYL_E, A_IYL_IYH, A_IYL_L, A_L__IY_N, A_IYL_A,            
    A__IY_N_B,A__IY_N_C,A__IY_N_D,A__IY_N_E,A__IY_N_H,A__IY_N_L, A_NUL,A__IY_N_A,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_A_IYH,   A_A_IYL,  A_A__IY_N,A_NUL,    
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_A_IYH,   A_A_IYL,  A_A__IY_N,A_NUL,  // 80    
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_A_IYH,   A_A_IYL,  A_A__IY_N,A_NUL,    
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IYH, A_REG_IYL, A__IY_N, A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_A_IYH,   A_A_IYL,  A_A__IY_N, A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IYH, A_REG_IYL, A__IY_N, A_NUL,  // A0
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IYH, A_REG_IYL, A__IY_N, A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IYH, A_REG_IYL, A__IY_N, A_NUL,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_REG_IYH, A_REG_IYL, A__IY_N, A_NUL,   
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL, // C0
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL,
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL, 
    A_NUL,  A_NUL,  A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL,
    A_NUL, A_REG_IY,A_NUL,   A__SP_IY,A_REG_IY,  A_NUL,   A_NUL,     A_NUL, // E0 
    A_NUL, A__IY,   A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL,
    A_NUL, A_NUL,   A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL, 
    A_NUL, A_SP_IY, A_NUL,   A_NUL,   A_NUL,     A_NUL,    A_NUL,    A_NUL
  };
  
  /** Contains the bytes used for the instruction */
  public static final byte[] tableSizeFD={
    2, 2, 2, 2, 2, 2, 2, 2,     // 00
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 4, 4, 2, 2, 2, 3, 2,     // 20  
    2, 2, 4, 2, 2, 2, 3, 2, 
    2, 2, 2, 2, 3, 3, 4, 2, 
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 3, 2,     // 40
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,    
    2, 2, 2, 2, 2, 2, 3, 2,     // 60
    2, 2, 2, 2, 2, 2, 3, 2, 
    3, 3, 3, 3, 3, 3, 2, 3,
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,     // 80
    2, 2, 2, 2, 2, 2, 3, 2, 
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,     // A0
    2, 2, 2, 2, 2, 2, 3, 2,  
    2, 2, 2, 2, 2, 2, 3, 2,
    2, 2, 2, 2, 2, 2, 3, 2,   
    2, 2, 2, 2, 2, 2, 2, 2,     // C0
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,     // E0
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2,
    2, 2, 2, 2, 2, 2, 2, 2
  };
  
  
  /** Contains the mnemonics reference for the instruction */
  public static final byte[] tableMnemonicsDDCB={
    M_RLC, M_RLC, M_RLC, M_RLC, M_RLC, M_RLC, M_RLC, M_RLC,  // 00
    M_RRC, M_RRC, M_RRC, M_RRC, M_RRC, M_RRC, M_RRC, M_RRC,
    M_RL,  M_RL,  M_RL,  M_RL,   M_RL,  M_RL,  M_RL,  M_RL, 
    M_RR,  M_RR,  M_RR,  M_RR,  M_RR,  M_RR,  M_RR,  M_RR,
    M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, // 20
    M_SRA, M_SRA, M_SRA, M_SRA, M_SRA, M_SRA, M_SRA, M_SRA,  
    M_SLL, M_SLL, M_SLL, M_SLL, M_SLL, M_SLL, M_SLL, M_SLL,
    M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, 
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, // 40
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, // 60
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, 
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,  // 80
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, // A0
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, // C0
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, // E0 
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET
  };
  
  /** Contains the mnemonics reference for the instruction */
  public static final int[] tableModesDDCB={
    A__IX_N_B,  A__IX_N_C,  A__IX_N_D,  A__IX_N_E,  A__IX_N_H,  A__IX_N_L,  A__IX_N,   A__IX_N_A, // 00
    A__IX_N_B,  A__IX_N_C,  A__IX_N_D,  A__IX_N_E,  A__IX_N_H,  A__IX_N_L,  A__IX_N,   A__IX_N_A,
    A__IX_N_B,  A__IX_N_C,  A__IX_N_D,  A__IX_N_E,  A__IX_N_H,  A__IX_N_L,  A__IX_N,   A__IX_N_A,
    A__IX_N_B,  A__IX_N_C,  A__IX_N_D,  A__IX_N_E,  A__IX_N_H,  A__IX_N_L,  A__IX_N,   A__IX_N_A,
    A__IX_N_B,  A__IX_N_C,  A__IX_N_D,  A__IX_N_E,  A__IX_N_H,  A__IX_N_L,  A__IX_N,   A__IX_N_A, // 20
    A__IX_N_B,  A__IX_N_C,  A__IX_N_D,  A__IX_N_E,  A__IX_N_H,  A__IX_N_L,  A__IX_N,   A__IX_N_A,
    A__IX_N_B,  A__IX_N_C,  A__IX_N_D,  A__IX_N_E,  A__IX_N_H,  A__IX_N_L,  A__IX_N,   A__IX_N_A,
    A__IX_N_B,  A__IX_N_C,  A__IX_N_D,  A__IX_N_E,  A__IX_N_H,  A__IX_N_L,  A__IX_N,   A__IX_N_A,
    A_0__IX_N,  A_0__IX_N,  A_0__IX_N,  A_0__IX_N,  A_0__IX_N,  A_0__IX_N,  A_0__IX_N, A_0__IX_N, // 40
    A_1__IX_N,  A_1__IX_N,  A_1__IX_N,  A_1__IX_N,  A_1__IX_N,  A_1__IX_N,  A_1__IX_N, A_1__IX_N,
    A_2__IX_N,  A_2__IX_N,  A_2__IX_N,  A_2__IX_N,  A_2__IX_N,  A_2__IX_N,  A_2__IX_N, A_2__IX_N,
    A_3__IX_N,  A_3__IX_N,  A_3__IX_N,  A_3__IX_N,  A_3__IX_N,  A_3__IX_N,  A_3__IX_N, A_3__IX_N,
    A_4__IX_N,  A_4__IX_N,  A_4__IX_N,  A_4__IX_N,  A_4__IX_N,  A_4__IX_N,  A_4__IX_N, A_4__IX_N, // 60
    A_5__IX_N,  A_5__IX_N,  A_5__IX_N,  A_5__IX_N,  A_5__IX_N,  A_5__IX_N,  A_5__IX_N, A_5__IX_N,
    A_6__IX_N,  A_6__IX_N,  A_6__IX_N,  A_6__IX_N,  A_6__IX_N,  A_6__IX_N,  A_6__IX_N, A_6__IX_N,
    A_7__IX_N,  A_7__IX_N,  A_7__IX_N,  A_7__IX_N,  A_7__IX_N,  A_7__IX_N,  A_7__IX_N, A_7__IX_N,
    A_0__IX_N_B,A_0__IX_N_C,A_0__IX_N_D,A_0__IX_N_E,A_0__IX_N_H,A_0__IX_N_L,A_0__IX_N, A_0__IX_N_A,// 80
    A_1__IX_N_B,A_1__IX_N_C,A_1__IX_N_D,A_1__IX_N_E,A_1__IX_N_H,A_1__IX_N_L,A_1__IX_N, A_1__IX_N_A,
    A_2__IX_N_B,A_2__IX_N_C,A_2__IX_N_D,A_2__IX_N_E,A_2__IX_N_H,A_2__IX_N_L,A_2__IX_N, A_2__IX_N_A,
    A_3__IX_N_B,A_3__IX_N_C,A_3__IX_N_D,A_3__IX_N_E,A_3__IX_N_H,A_3__IX_N_L,A_3__IX_N, A_3__IX_N_A,
    A_4__IX_N_B,A_4__IX_N_C,A_4__IX_N_D,A_4__IX_N_E,A_4__IX_N_H,A_4__IX_N_L,A_4__IX_N, A_4__IX_N_A, // A0
    A_5__IX_N_B,A_5__IX_N_C,A_5__IX_N_D,A_5__IX_N_E,A_5__IX_N_H,A_5__IX_N_L,A_5__IX_N, A_5__IX_N_A,
    A_6__IX_N_B,A_6__IX_N_C,A_6__IX_N_D,A_6__IX_N_E,A_6__IX_N_H,A_6__IX_N_L,A_6__IX_N, A_6__IX_N_A,
    A_7__IX_N_B,A_7__IX_N_C,A_7__IX_N_D,A_7__IX_N_E,A_7__IX_N_H,A_7__IX_N_L,A_7__IX_N, A_7__IX_N_A,
    A_0__IX_N_B,A_0__IX_N_C,A_0__IX_N_D,A_0__IX_N_E,A_0__IX_N_H,A_0__IX_N_L,A_0__IX_N, A_0__IX_N_A, // C0
    A_1__IX_N_B,A_1__IX_N_C,A_1__IX_N_D,A_1__IX_N_E,A_1__IX_N_H,A_1__IX_N_L,A_1__IX_N, A_1__IX_N_A,
    A_2__IX_N_B,A_2__IX_N_C,A_2__IX_N_D,A_2__IX_N_E,A_2__IX_N_H,A_2__IX_N_L,A_2__IX_N, A_2__IX_N_A,
    A_3__IX_N_B,A_3__IX_N_C,A_3__IX_N_D,A_3__IX_N_E,A_3__IX_N_H,A_3__IX_N_L,A_3__IX_N, A_3__IX_N_A,
    A_4__IX_N_B,A_4__IX_N_C,A_4__IX_N_D,A_4__IX_N_E,A_4__IX_N_H,A_4__IX_N_L,A_4__IX_N, A_4__IX_N_A, // E0
    A_5__IX_N_B,A_5__IX_N_C,A_5__IX_N_D,A_5__IX_N_E,A_5__IX_N_H,A_5__IX_N_L,A_5__IX_N, A_5__IX_N_A,
    A_6__IX_N_B,A_6__IX_N_C,A_6__IX_N_D,A_6__IX_N_E,A_6__IX_N_H,A_6__IX_N_L,A_6__IX_N, A_6__IX_N_A,
    A_7__IX_N_B,A_7__IX_N_C,A_7__IX_N_D,A_7__IX_N_E,A_7__IX_N_H,A_7__IX_N_L,A_7__IX_N, A_7__IX_N_A
  };
  
    /** Contains the bytes used for the instruction */
  public static final byte[] tableSizeDDCB={
    4, 4, 4, 4, 4, 4, 4, 4,     // 00
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // 20  
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // 40
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,    
    4, 4, 4, 4, 4, 4, 4, 4,     // 60
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // 80
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // A0
    4, 4, 4, 4, 4, 4, 4, 4,  
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,   
    4, 4, 4, 4, 4, 4, 4, 4,     // C0
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // E0
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 2
  };
  
  /** Contains the mnemonics reference for the instruction */
  public static final byte[] tableMnemonicsFDCB={
    M_RLC, M_RLC, M_RLC, M_RLC, M_RLC, M_RLC, M_RLC, M_RLC,  // 00
    M_RRC, M_RRC, M_RRC, M_RRC, M_RRC, M_RRC, M_RRC, M_RRC,
    M_RL,  M_RL,  M_RL,  M_RL,   M_RL,  M_RL,  M_RL,  M_RL, 
    M_RR,  M_RR,  M_RR,  M_RR,  M_RR,  M_RR,  M_RR,  M_RR,
    M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, M_SLA, // 20
    M_SRA, M_SRA, M_SRA, M_SRA, M_SRA, M_SRA, M_SRA, M_SRA,  
    M_SLL, M_SLL, M_SLL, M_SLL, M_SLL, M_SLL, M_SLL, M_SLL,
    M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, M_SRL, 
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, // 40
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, // 60
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, 
    M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT, M_BIT,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,  // 80
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, // A0
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES, M_RES,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, // C0
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, // E0 
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET,
    M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET, M_SET
  };
  
  /** Contains the mnemonics reference for the instruction */
  public static final int[] tableModesFDCB={
    A__IY_N_B,  A__IY_N_C,  A__IY_N_D,  A__IY_N_E,  A__IY_N_H,  A__IY_N_L,  A__IY_N,   A__IY_N_A, // 00
    A__IY_N_B,  A__IY_N_C,  A__IY_N_D,  A__IY_N_E,  A__IY_N_H,  A__IY_N_L,  A__IY_N,   A__IY_N_A,
    A__IY_N_B,  A__IY_N_C,  A__IY_N_D,  A__IY_N_E,  A__IY_N_H,  A__IY_N_L,  A__IY_N,   A__IY_N_A,
    A__IY_N_B,  A__IY_N_C,  A__IY_N_D,  A__IY_N_E,  A__IY_N_H,  A__IY_N_L,  A__IY_N,   A__IY_N_A,
    A__IY_N_B,  A__IY_N_C,  A__IY_N_D,  A__IY_N_E,  A__IY_N_H,  A__IY_N_L,  A__IY_N,   A__IY_N_A, // 20
    A__IY_N_B,  A__IY_N_C,  A__IY_N_D,  A__IY_N_E,  A__IY_N_H,  A__IY_N_L,  A__IY_N,   A__IY_N_A,
    A__IY_N_B,  A__IY_N_C,  A__IY_N_D,  A__IY_N_E,  A__IY_N_H,  A__IY_N_L,  A__IY_N,   A__IY_N_A,
    A__IY_N_B,  A__IY_N_C,  A__IY_N_D,  A__IY_N_E,  A__IY_N_H,  A__IY_N_L,  A__IY_N,   A__IY_N_A,
    A_0__IY_N,  A_0__IY_N,  A_0__IY_N,  A_0__IY_N,  A_0__IY_N,  A_0__IY_N,  A_0__IY_N, A_0__IY_N, // 40
    A_1__IY_N,  A_1__IY_N,  A_1__IY_N,  A_1__IY_N,  A_1__IY_N,  A_1__IY_N,  A_1__IY_N, A_1__IY_N,
    A_2__IY_N,  A_2__IY_N,  A_2__IY_N,  A_2__IY_N,  A_2__IY_N,  A_2__IY_N,  A_2__IY_N, A_2__IY_N,
    A_3__IY_N,  A_3__IY_N,  A_3__IY_N,  A_3__IY_N,  A_3__IY_N,  A_3__IY_N,  A_3__IY_N, A_3__IY_N,
    A_4__IY_N,  A_4__IY_N,  A_4__IY_N,  A_4__IY_N,  A_4__IY_N,  A_4__IY_N,  A_4__IY_N, A_4__IY_N, // 60
    A_5__IY_N,  A_5__IY_N,  A_5__IY_N,  A_5__IY_N,  A_5__IY_N,  A_5__IY_N,  A_5__IY_N, A_5__IY_N,
    A_6__IY_N,  A_6__IY_N,  A_6__IY_N,  A_6__IY_N,  A_6__IY_N,  A_6__IY_N,  A_6__IY_N, A_6__IY_N,
    A_7__IY_N,  A_7__IY_N,  A_7__IY_N,  A_7__IY_N,  A_7__IY_N,  A_7__IY_N,  A_7__IY_N, A_7__IY_N,
    A_0__IY_N_B,A_0__IY_N_C,A_0__IY_N_D,A_0__IY_N_E,A_0__IY_N_H,A_0__IY_N_L,A_0__IY_N, A_0__IY_N_A,// 80
    A_1__IY_N_B,A_1__IY_N_C,A_1__IY_N_D,A_1__IY_N_E,A_1__IY_N_H,A_1__IY_N_L,A_1__IY_N, A_1__IY_N_A,
    A_2__IY_N_B,A_2__IY_N_C,A_2__IY_N_D,A_2__IY_N_E,A_2__IY_N_H,A_2__IY_N_L,A_2__IY_N, A_2__IY_N_A,
    A_3__IY_N_B,A_3__IY_N_C,A_3__IY_N_D,A_3__IY_N_E,A_3__IY_N_H,A_3__IY_N_L,A_3__IY_N, A_3__IY_N_A,
    A_4__IY_N_B,A_4__IY_N_C,A_4__IY_N_D,A_4__IY_N_E,A_4__IY_N_H,A_4__IY_N_L,A_4__IY_N, A_4__IY_N_A, // A0
    A_5__IY_N_B,A_5__IY_N_C,A_5__IY_N_D,A_5__IY_N_E,A_5__IY_N_H,A_5__IY_N_L,A_5__IY_N, A_5__IY_N_A,
    A_6__IY_N_B,A_6__IY_N_C,A_6__IY_N_D,A_6__IY_N_E,A_6__IY_N_H,A_6__IY_N_L,A_6__IY_N, A_6__IY_N_A,
    A_7__IY_N_B,A_7__IY_N_C,A_7__IY_N_D,A_7__IY_N_E,A_7__IY_N_H,A_7__IY_N_L,A_7__IY_N, A_7__IY_N_A,
    A_0__IY_N_B,A_0__IY_N_C,A_0__IY_N_D,A_0__IY_N_E,A_0__IY_N_H,A_0__IY_N_L,A_0__IY_N, A_0__IY_N_A, // C0
    A_1__IY_N_B,A_1__IY_N_C,A_1__IY_N_D,A_1__IY_N_E,A_1__IY_N_H,A_1__IY_N_L,A_1__IY_N, A_1__IY_N_A,
    A_2__IY_N_B,A_2__IY_N_C,A_2__IY_N_D,A_2__IY_N_E,A_2__IY_N_H,A_2__IY_N_L,A_2__IY_N, A_2__IY_N_A,
    A_3__IY_N_B,A_3__IY_N_C,A_3__IY_N_D,A_3__IY_N_E,A_3__IY_N_H,A_3__IY_N_L,A_3__IY_N, A_3__IY_N_A,
    A_4__IY_N_B,A_4__IY_N_C,A_4__IY_N_D,A_4__IY_N_E,A_4__IY_N_H,A_4__IY_N_L,A_4__IY_N, A_4__IY_N_A, // E0
    A_5__IY_N_B,A_5__IY_N_C,A_5__IY_N_D,A_5__IY_N_E,A_5__IY_N_H,A_5__IY_N_L,A_5__IY_N, A_5__IY_N_A,
    A_6__IY_N_B,A_6__IY_N_C,A_6__IY_N_D,A_6__IY_N_E,A_6__IY_N_H,A_6__IY_N_L,A_6__IY_N, A_6__IY_N_A,
    A_7__IY_N_B,A_7__IY_N_C,A_7__IY_N_D,A_7__IY_N_E,A_7__IY_N_H,A_7__IY_N_L,A_7__IY_N, A_7__IY_N_A
  };
  
  /** Contains the bytes used for the instruction */
  public static final byte[] tableSizeFDCB={
    4, 4, 4, 4, 4, 4, 4, 4,     // 00
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // 20  
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // 40
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,    
    4, 4, 4, 4, 4, 4, 4, 4,     // 60
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // 80
    4, 4, 4, 4, 4, 4, 4, 4, 
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // A0
    4, 4, 4, 4, 4, 4, 4, 4,  
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,   
    4, 4, 4, 4, 4, 4, 4, 4,     // C0
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,     // E0
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 4,
    4, 4, 4, 4, 4, 4, 4, 2
  };
  
  @Override
  public String dasm(byte[] buffer, int pos, long pc) {
    String result="";          // result disassemble string
    int steps=0;
    
    int op=Unsigned.done(buffer[pos++]); // instruction opcode

    iType=(int)tableMnemonics[op];   // store the type for creating comment
    
    switch (iType) {
      case T_CB:
        op=Unsigned.done(buffer[pos++]);  
        iType=(int)tableMnemonicsCB[op];  
        aType=tableModesCB[op];
        steps=tableSizeCB[op];
        break;
      case T_DD: 
        op=Unsigned.done(buffer[pos++]);  
        iType=(int)tableMnemonicsDD[op];  
        aType=tableModesDD[op];
        steps=tableSizeDD[op];
        
        if (iType==T_DDCB) {
          // there are an extra table  
          op=Unsigned.done(buffer[pos+1]);  
          pos++;
          iType=(int)tableMnemonicsDDCB[op];  
          aType=tableModesDDCB[op];
          steps=tableSizeDDCB[op];          
        }
        break;      
      case T_ED:
        op=Unsigned.done(buffer[pos++]);  
        iType=(int)tableMnemonicsED[op];  
        aType=tableModesED[op];
        steps=tableSizeED[op];
        break;
      case T_FD:
        op=Unsigned.done(buffer[pos++]);  
        iType=(int)tableMnemonicsFD[op];  
        aType=tableModesFD[op];
        steps=tableSizeFD[op];
        
        if (iType==T_FDCB) {
          op=Unsigned.done(buffer[pos+1]);  
          pos++;
          iType=(int)tableMnemonicsFDCB[op];  
          aType=tableModesFDCB[op];
          steps=tableSizeFDCB[op];  
        }
        break;
      default:
        aType=tableModes[op];  
        steps=tableSize[op];
        break;
    }        

        
    if (upperCase) result=mnemonics[iType];
    else result=mnemonics[iType].toLowerCase();  
    
    switch (result.length()) {
        case 2:
          result+="   ";
          break;
        case 3: 
          result+="  ";
          break;
        case 4:
          result+=" ";
          break;
    }          
    
    switch (aType) {
      case A_NUL:     // nothing
        break;
      case A_REG_A:    // register A  
        result+=(upperCase? "A": "a");
        break;  
      case A_REG_B:    // register B
        result+=(upperCase? "B": "b");
        break;    
      case A_REG_C:    // register C
        result+=(upperCase? "C": "c");
        break;         
      case A_REG_D:    // register D
        result+=(upperCase? "D": "d");
        break;  
      case A_REG_E:    // register E
        result+=(upperCase? "E": "e");
        break;   
      case A_REG_H:    // register H
        result+=(upperCase? "H": "h");
        break;        
      case A_BC_NN:    // BC absolute nn
        this.pos=pos;  
        result+=getRegXXNN(buffer, (upperCase? "BC": "bc"));
        pos=this.pos;
        break;
      case A_DE_NN:    // DE absolute nn
        this.pos=pos;  
        result+=getRegXXNN(buffer, (upperCase? "DE": "de"));
        pos=this.pos;
        break;    
      case A_HL_NN:    // HL absolute nn
        this.pos=pos;  
        result+=getRegXXNN(buffer, (upperCase? "HL": "hl"));
        pos=this.pos;
        break;        
      case A_SP_NN:    // SP absolute nn
        this.pos=pos;  
        result+=getRegXXNN(buffer, (upperCase? "SP": "sp"));
        pos=this.pos;
        break; 
       case A_IX_NN:    // IX absolute nn
        this.pos=pos;   
        result+=getRegXXNN(buffer, (upperCase? "IX": "ix"));
        pos=this.pos;
        break;
      case A_IY_NN:    // IY absolute nn
        this.pos=pos;  
        result+=getRegXXNN(buffer, (upperCase? "IY": "iy"));
        pos=this.pos;
        break;           
      case A__BC_A:    // (BC) indirect A
        result+=(upperCase? "(BC),A": "(bc),a");
        break;
      case A__DE_A:    // (DE) indirect A
        result+=(upperCase? "(DE),A": "(de),a");
        break;  
      case A__HL_A:    // (HL) indirect A
        result+=(upperCase? "(HL),A": "(hl),a");
        break;     
     case A__HL_B:    // (HL) indirect B
        result+=(upperCase? "(HL),B": "(hl),b");  
        break;
     case A__HL_C:    // (HL) indirect C
        result+=(upperCase? "(HL),C": "(hl),c");  
        break;   
     case A__HL_D:    // (HL) indirect D
        result+=(upperCase? "(HL),D": "(hl),d");  
        break;   
     case A__HL_E:    // (HL) indirect E
        result+=(upperCase? "(HL),E": "(hl),e");  
        break;   
     case A__HL_H:    // (HL) indirect H
        result+=(upperCase? "(HL),H": "(hl),h");  
        break;  
     case A__HL_L:    // (HL) indirect L
        result+=(upperCase? "(HL),L": "(hl),l");  
        break;           
      case A__IXN_A:   // (IX+N) indirect A
        if (pos<buffer.length) addr=Unsigned.done(buffer[pos++]);
        else addr=-1; 
        
        result+=(upperCase? "(IX+)": "(ix+")+getLabelZero(addr)+(upperCase? "),A": "),a"); 
        break;  
      case A__IYN_A:   // (IY+N) indirect A
        if (pos<buffer.length) addr=Unsigned.done(buffer[pos++]);
        else addr=-1; 
        
        result+=(upperCase? "(IY+)": "(iy+")+getLabelZero(addr)+(upperCase? "),A": "),a"); 
        break;  
      case A__NN_A:    // (NN) indirect A  
        if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        else addr=-1;
        pos++;  
        
        result+="("+getLabel(addr)+(upperCase? "),A": "),a"); 
        
        setLabel(addr);
        setLabelPlus(pc,1);
        setLabelPlus(pc,2);  
        break; 
      case A_REG_BC:   // registers BC
        result+=(upperCase? "BC": "bc");   
        break;   
      case A_REG_DE:   // registers DE
        result+=(upperCase? "DE": "de");   
        break;          
      case A_REG_HL:   // registers HL
        result+=(upperCase? "HL": "hl");   
        break;  
      case A_REG_SP:   // registers SP
        result+=(upperCase? "SP": "sp");   
        break;   
      case A_REG_AF:    // register AF  
        result+=(upperCase? "AF": "af");
        break;   
      case A_A_N:     // A reg with N 
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "A": "a"));
        pos=this.pos;
        break;  
      case A_B_N:     // B reg with N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "B": "b"));  
        pos=this.pos;
        break;  
      case A_C_N:     // C reg with N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "C": "c"));
        pos=this.pos;
        break;      
      case A_D_N:     // D reg with N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "D": "d"));
        pos=this.pos;
        break;        
      case A_E_N:     // E reg with N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "E": "e"));
        pos=this.pos;
        break;   
      case A_H_N:     // H reg with N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "H": "h"));
        pos=this.pos;
        break;   
      case A_L_N:     // L reg with N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "L": "l"));
        pos=this.pos;
        break;           
      case A_IXH_N: // IXH,N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "IXH": "ixh"));
        pos=this.pos;   
        break;
      case A_IXL_N: // IXL,N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "IXL": "ixl"));
        pos=this.pos;          
        break;        
      case A_IYH_N: // IYH,N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "IYH": "iyh"));
        pos=this.pos;   
        break;  
      case A_IYL_N: // IYL,N
        this.pos=pos;  
        result+=getRegXN(buffer, (upperCase? "IYL": "iyl"));
        pos=this.pos;          
        break;                
      case A_AF_AF: 
        result+=(upperCase? "AF,AF'": "af,af'");  
        break; 
      case A_REL:       // relative  
        if (pos<buffer.length) addr=pc+buffer[pos++]+2;
        else addr=-1; 
        
        result+=getLabel(addr);
        setLabel(addr);  
        break;
      case A_REL_NZ:    // relative NZ  
        if (pos<buffer.length) addr=pc+buffer[pos++]+2;
        else addr=-1; 
        
        result+=(upperCase? "NZ,": "nz,")+getLabel(addr);
        setLabel(addr);          
        break;
      case A_REL_Z:    // relative Z  
        if (pos<buffer.length) addr=pc+buffer[pos++]+2;
        else addr=-1; 
        
        result+=(upperCase? "Z,": "z,")+getLabel(addr);
        setLabel(addr);          
        break;
      case A_REL_NC:    // relative NC  
        if (pos<buffer.length) addr=pc+buffer[pos++]+2;
        else addr=-1; 
        
        result+=(upperCase? "NC,": "nc,")+getLabel(addr);
        setLabel(addr);          
        break;    
      case A_REL_C:    // relative C  
        if (pos<buffer.length) addr=pc+buffer[pos++]+2;
        else addr=-1; 
        
        result+=(upperCase? "C,": "c,")+getLabel(addr);
        setLabel(addr);          
        break; 
      case A_IXH_A:     // IXH reg A  
        result+=(upperCase? "IXH,A": "ixh,a");  
        break;  
      case A_IXH_B:     // IXH reg B  
        result+=(upperCase? "IXH,B": "ixh,b");  
        break;     
      case A_IXH_C:     // IXH reg C  
        result+=(upperCase? "IXH,C": "ixh,c");  
        break;    
      case A_IXH_D:     // IXH reg D  
        result+=(upperCase? "IXH,D": "ixh,d");  
        break;    
      case A_IXH_E:     // IXH reg E  
        result+=(upperCase? "IXH,E": "ixh,e");  
        break;    
      case A_IXH_H:     // IXH reg H  
        result+=(upperCase? "IXH,H": "ixh,h");  
        break;   
      case A_IXH_L:     // IXH reg L  
        result+=(upperCase? "IXH,L": "ixh,l");  
        break;    
      case A_IYH_A:     // IYH reg A  
        result+=(upperCase? "IYH,A": "iyh,a");  
        break;  
      case A_IYH_B:     // IYH reg B  
        result+=(upperCase? "IYH,B": "iyh,b");  
        break;     
      case A_IYH_C:     // IYH reg C  
        result+=(upperCase? "IYH,C": "iyh,c");  
        break;    
      case A_IYH_D:     // IYH reg D  
        result+=(upperCase? "IYH,D": "iyh,d");  
        break;    
      case A_IYH_E:     // IYH reg E  
        result+=(upperCase? "IYH,E": "iyh,e");  
        break;    
      case A_IYH_H:     // IYH reg H  
        result+=(upperCase? "IYH,H": "iyh,h");  
        break;   
      case A_IYH_L:     // IYH reg L  
        result+=(upperCase? "IYH,L": "iyh,l");  
        break;          
      case A_A_IXH:     // A reg IXH 
        result+=(upperCase? "A,IXH": "a,ixh");  
        break;  
      case A_B_IXH:     // B reg IXH 
        result+=(upperCase? "B,IXH": "b,ixh");  
        break;    
      case A_C_IXH:     // C reg IXH 
        result+=(upperCase? "C,IXH": "c,ixh");  
        break;    
      case A_D_IXH:     // D reg IXH 
        result+=(upperCase? "D,IXH": "d,ixh");  
        break;    
      case A_E_IXH:     // E reg IXH 
        result+=(upperCase? "E,IXH": "e,ixh");  
        break;    
      case A_A_IXL:     // A reg IXL 
        result+=(upperCase? "A,IXL": "a,ixl");  
        break;  
      case A_B_IXL:     // B reg IXL 
        result+=(upperCase? "B,IXL": "b,ixl");  
        break;    
      case A_C_IXL:     // C reg IXL 
        result+=(upperCase? "C,IXL": "c,ixl");  
        break;    
      case A_D_IXL:     // D reg IXL 
        result+=(upperCase? "D,IXL": "d,ixl");  
        break;    
      case A_E_IXL:     // E reg IXL 
        result+=(upperCase? "E,IXL": "e,ixl");  
        break;
      case A_A_IYH:     // A reg IYH 
        result+=(upperCase? "A,IYH": "a,iyh");  
        break;  
      case A_B_IYH:     // B reg IYH 
        result+=(upperCase? "B,IYH": "b,iyh");  
        break;    
      case A_C_IYH:     // C reg IYH 
        result+=(upperCase? "C,IYH": "c,iyh");  
        break;    
      case A_D_IYH:     // D reg IYH 
        result+=(upperCase? "D,IYH": "d,iyh");  
        break;    
      case A_E_IYH:     // E reg IYH 
        result+=(upperCase? "E,IYH": "e,iyh");  
        break;    
      case A_A_IYL:     // A reg IYL 
        result+=(upperCase? "A,IYL": "a,iyl");  
        break;  
      case A_B_IYL:     // B reg IYL 
        result+=(upperCase? "B,IYL": "b,iyl");  
        break;    
      case A_C_IYL:     // C reg IYL 
        result+=(upperCase? "C,IYL": "c,iyl");  
        break;    
      case A_D_IYL:     // D reg IYL 
        result+=(upperCase? "D,IYL": "d,iyl");  
        break;    
      case A_E_IYL:     // E reg IYL 
        result+=(upperCase? "E,IYL": "e,iyl");  
        break;        
      case A_IXL_A:     // IXL reg A
        result+=(upperCase? "IXL, A": "ixl, a");  
        break;    
      case A_IXL_B:     // IXL reg B
        result+=(upperCase? "IXL, B": "ixl, b");  
        break;    
      case A_IXL_C:     // IXL reg C
        result+=(upperCase? "IXL, C": "ixl, c");  
        break;    
      case A_IXL_D:     // IXL reg D
        result+=(upperCase? "IXL, D": "ixl, d");  
        break;    
      case A_IXL_E:     // IXL reg E
        result+=(upperCase? "IXL, E": "ixl, e");  
        break;    
      case A_IXL_L:     // IXL reg L
        result+=(upperCase? "IXL, L": "ixl, l");  
        break;       
      case A_IYL_A:     // IXL reg A
        result+=(upperCase? "IYL, A": "iyl, a");  
        break;    
      case A_IYL_B:     // IXL reg B
        result+=(upperCase? "IYL, B": "iyl, b");  
        break;    
      case A_IYL_C:     // IXL reg C
        result+=(upperCase? "IYL, C": "iyl, c");  
        break;    
      case A_IYL_D:     // IXL reg D
        result+=(upperCase? "IYL, D": "iyl, d");  
        break;    
      case A_IYL_E:     // IXL reg E
        result+=(upperCase? "IYL, E": "iyl, e");  
        break;    
      case A_IYL_L:     // IXL reg L
        result+=(upperCase? "IYL, L": "iyl, l");  
        break;            
      case A_IXH_IXH:     // IXH reg IXH
        result+=(upperCase? "IXH, IXH": "ixh, ixh");  
        break;  
      case A_IXH_IXL:     // IXH reg IXL
        result+=(upperCase? "IXH, IXL": "ixh, ixl");  
        break;    
      case A_IXL_IXH:     // IXL reg IXH
        result+=(upperCase? "IXL, IXH": "ixl, ixh");  
        break;          
      case A_IXL_IXL:     // IXL reg IXL
        result+=(upperCase? "IXL, IXL": "ixl, ixl");  
        break;
      case A_IYH_IYH:     // IYH reg IYH
        result+=(upperCase? "IYH, IYH": "iyh, iyh");  
        break;  
      case A_IYH_IYL:     // IYH reg IYL
        result+=(upperCase? "IYH, IYL": "iyh, iyl");  
        break;    
      case A_IYL_IYH:     // IYL reg IYH
        result+=(upperCase? "IYL, IYH": "iyl, iyh");  
        break;          
      case A_IYL_IYL:     // IYL reg IYL
        result+=(upperCase? "IYL, IYL": "iyl, iyl");  
        break;  
      case A_HL_BC:     // HL reg BC
        result+=(upperCase? "HL,BC": "hl,bc");
        break;  
      case A_HL_DE:     // HL reg DE
        result+=(upperCase? "HL,DE": "hl,de");
        break;  
      case A_HL_HL:     // HL reg HL
        result+=(upperCase? "HL,HL": "hl,hl");
        break;  
      case A_HL_SP:     // HL reg SP
        result+=(upperCase? "HL,SP": "hl,sp");
        break;    
      case A_IX_BC:     // IX reg BC
        result+=(upperCase? "IX,BC": "ix,bc");
        break;  
      case A_IX_DE:     // IX reg DE
        result+=(upperCase? "IX,DE": "ix,de");
        break;  
      case A_IX_HL:     // IX reg HL
        result+=(upperCase? "IX,HL": "ix,hl");
        break;  
      case A_IX_SP:     // IX reg SP
        result+=(upperCase? "IX,SP": "ix,sp");
        break;        
      case A_IY_BC:     // IY reg BC
        result+=(upperCase? "IY,BC": "iy,bc");
        break;  
      case A_IY_DE:     // IY reg DE
        result+=(upperCase? "IY,DE": "iy,de");
        break;  
      case A_IY_HL:     // IY reg HL
        result+=(upperCase? "IY,HL": "iy,hl");
        break;  
      case A_IY_SP:     // IY reg SP
        result+=(upperCase? "IY,SP": "iy,sp");
        break;
      case A_SP_HL:     // SP reg HL 
        result+=(upperCase? "SP,HL": "sp,hl");
        break;   
      case A_DE_HL:     // DE reg HL 
        result+=(upperCase? "DE,HL": "de,hl");
        break;   
      case A_IX_IX:     // IX reg IX
        result+=(upperCase? "IX,IX": "ix,ix");  
        break;  
      case A_IY_IY:     // IY reg IY
        result+=(upperCase? "IY,IY": "iy,iy");  
        break;  
      case A_SP_IX:     // SP reg IX
        result+=(upperCase? "SP,IX": "sp,ix");  
        break;  
      case A_SP_IY:     // SP reg IY
        result+=(upperCase? "SP,IY": "sp,iy");  
        break;     
      case A__SP_HL:    // (SP) ind  HL  
        result+=(upperCase? "(SP),HL": "(sp),hl");  
        break;  
      case A__SP_IX:    // (SP) ind  IX  
        result+=(upperCase? "(SP),IX": "(sp),ix");  
        break;          
      case A__SP_IY:    // (SP) ind  IY  
        result+=(upperCase? "(SP),IY": "(sp),iy");  
        break;             
      case A_A__BC:     // A indirect (BC)  
        result+=(upperCase? "A,(BC)": "a,(bc)");  
        break;  
      case A_A__DE:     // A indirect (DE)  
        result+=(upperCase? "A,(DE)": "a,(de)");  
        break;        
      case A_A__HL:     // A indirect (HL)  
        result+=(upperCase? "A,(HL)": "a,(hl)");  
        break;        
      case A_A__IXN:    // A indirect (IX+N)   
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "A": "a"), (upperCase? "IX": "ix"));   
        pos=this.pos;
        break;  
      case A_A__IYN:    // A indirect (IY+N)
      case A_A__IY_N:    
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "A": "a"), (upperCase? "IY": "iy"));  
        pos=this.pos;
        break;         
      case A_B__HL:    // B indirect (HL)  
        result+=(upperCase? "B,(HL)": "b,(hl)");  
        break;        
      case A_B__IXN:    // B indirect (IX+N)   
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "B": "b"), (upperCase? "IX": "ix")); 
        pos=this.pos;
        break;  
      case A_B__IYN:    // B indirect (IY+N)  
      case A_B__IY_N:
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "B": "b"), (upperCase? "IY": "iy"));  
        pos=this.pos;
        break; 
      case A_C__HL:     // C indirect (HL)  
        result+=(upperCase? "C,(HL)": "c,(hl)");  
        break;        
      case A_C__IXN:    // C indirect (IX+N)  
      case A_C__IX_N:    
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "C": "c"), (upperCase? "IX": "ix")); 
        pos=this.pos;
        break;  
      case A_C__IYN:    // C indirect (IY+N) 
      case A_C__IY_N:    
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "C": "c"), (upperCase? "IY": "iy")); 
        pos=this.pos;
        break; 
      case A_D__HL:     // D indirect (HL)  
        result+=(upperCase? "D,(HL)": "d,(hl)");  
        break;        
      case A_D__IXN:    // D indirect (IX+N)  
      case A_D__IX_N:    
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "D": "d"), (upperCase? "IX": "ix"));  
        pos=this.pos;
        break;  
      case A_D__IYN:    // D indirect (IY+N)
      case A_D__IY_N:    
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "D": "d"), (upperCase? "IY": "iy")); 
        pos=this.pos;
        break; 
      case A_E__HL:     // E indirect (HL)  
        result+=(upperCase? "E,(HL)": "e,(hl)");  
        break;        
      case A_E__IXN:    // E indirect (IX+N)  
      case A_E__IX_N:    
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "E": "e"), (upperCase? "IX": "ix")); 
        pos=this.pos;
        break;  
      case A_E__IYN:    // E indirect (IY+N)  
      case A_E__IY_N:     
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "E": "e"), (upperCase? "IY": "iy")); 
        pos=this.pos;
        break;        
      case A_H__HL:     // H indirect (HL)  
        result+=(upperCase? "H,(HL)": "h,(hl)");  
        break;        
      case A_H__IXN:    // H indirect (IX+N) 
      case A_H__IX_N:    
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "H": "h"), (upperCase? "IX": "ix")); 
        pos=this.pos;
        break;  
      case A_H__IYN:    // H indirect (IY+N)  
      case A_H__IY_N:
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "H": "h"), (upperCase? "IY": "iy"));  
        pos=this.pos;
        break;       
      case A_L__HL:     // L indirect (HL)  
        result+=(upperCase? "L,(HL)": "l,(hl)");  
        break;        
      case A_L__IXN:    // L indirect (IX+N)  
      case A_L__IX_N:   
        this.pos=pos;  
        result+=getRefXIndXXN(buffer, (upperCase? "L": "l"), (upperCase? "IX": "ix"));  
        pos=this.pos;
        break;  
      case A_L__IYN:    // L indirect (IY+N)
      case A_L__IY_N:    
        this.pos=pos;   
        result+=getRefXIndXXN(buffer, (upperCase? "L": "l"), (upperCase? "IY": "iy"));
        pos=this.pos;
        break;                                                                             
     case A_0__IX_N:  // 0 ind (IX+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "0", (upperCase? "IX": "ix"));
        pos=this.pos;
        break;       
     case A_0__IY_N:  // 0 ind (IY+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "0", (upperCase? "IY": "iy"));
        pos=this.pos;
        break;    
     case A_1__IX_N:  // 1 ind (IX+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "1", (upperCase? "IX": "ix"));
        pos=this.pos;
        break;       
     case A_1__IY_N:  // 1 ind (IY+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "1", (upperCase? "IY": "iy"));
        pos=this.pos;
        break;            
     case A_2__IX_N:  // 2 ind (IX+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "2", (upperCase? "IX": "ix"));
        pos=this.pos;
        break;       
     case A_2__IY_N:  // 2 ind (IY+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "0", (upperCase? "IY": "iy"));
        pos=this.pos;
        break;    
     case A_3__IX_N:  // 3 ind (IX+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "3", (upperCase? "IX": "ix"));
        pos=this.pos;
        break;       
     case A_3__IY_N:  // 3 ind (IY+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "3", (upperCase? "IY": "iy"));
        pos=this.pos;
        break;         
     case A_4__IX_N:  // 4 ind (IX+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "4", (upperCase? "IX": "ix"));
        pos=this.pos;
        break;       
     case A_4__IY_N:  // 4 ind (IY+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "4", (upperCase? "IY": "iy"));
        pos=this.pos;
        break;         
     case A_5__IX_N:  // 5 ind (IX+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "5", (upperCase? "IX": "ix"));
        pos=this.pos;
        break;       
     case A_5__IY_N:  // 5 ind (IY+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "5", (upperCase? "IY": "iy"));
        pos=this.pos;
        break;     
     case A_6__IX_N:  // 6 ind (IX+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "6", (upperCase? "IX": "ix"));
        pos=this.pos;
        break;       
     case A_6__IY_N:  // 6 ind (IY+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "6", (upperCase? "IY": "iy"));
        pos=this.pos;
        break;     
     case A_7__IX_N:  // 7 ind (IX+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "7", (upperCase? "IX": "ix"));
        pos=this.pos;
        break;       
     case A_7__IY_N:  // 7 ind (IY+N)
        this.pos=pos;   
        result+=getRefXInd2XXN(buffer, "7", (upperCase? "IY": "iy"));
        pos=this.pos;
        break;             
      case A__NN_BC:   // (NN) ind absolute BC 
        this.pos=pos;  
        result+=getIndNNregX(buffer, (upperCase? "BC": "bc"));
        pos=this.pos;
        break;   
      case A__NN_DE:   // (NN) ind absolute DE 
        this.pos=pos;  
        result+=getIndNNregX(buffer, (upperCase? "DE": "de"));
        pos=this.pos;
        break;     
      case A__NN_HL:   // (NN) ind absolute HL 
        this.pos=pos;  
        result+=getIndNNregX(buffer, (upperCase? "HL": "hl"));
        pos=this.pos;
        break;          
      case A__NN_SP:   // (NN) ind absolute SP
        this.pos=pos;  
        result+=getIndNNregX(buffer, (upperCase? "SP": "sp"));
        pos=this.pos;
        break;    
      case A__NN_IX:   // (NN) absolute IX
        this.pos=pos;  
        result+=getIndNNregX(buffer, (upperCase? "IX": "ix"));
        pos=this.pos;
        break; 
      case A__NN_IY:   // (NN) absolute IY
        this.pos=pos;  
        result+=getIndNNregX(buffer, (upperCase? "IY": "iy"));
        pos=this.pos;
        break;    
      case A_BC__NN:   // BC ind absolute (NN) 
        this.pos=pos;  
        result+=getRegXXIndNN(buffer, (upperCase? "BC": "bc"));  
        pos=this.pos;
        break; 
      case A_DE__NN:   // DE ind absolute (NN) 
        this.pos=pos;  
        result+=getRegXXIndNN(buffer, (upperCase? "DE": "de")); 
        pos=this.pos;
        break;   
      case A_HL__NN:   // HL ind absolute (NN) 
        this.pos=pos;  
        result+=getRegXXIndNN(buffer, (upperCase? "HL": "hl")); 
        pos=this.pos;
        break;           
      case A_SP__NN:   // SP ind absolute (NN) 
        this.pos=pos;  
        result+=getRegXXIndNN(buffer, (upperCase? "SP": "sp")); 
        pos=this.pos;
        break;     
      case A_IX__NN:   // IX ind absolute (NN) 
        this.pos=pos;  
        result+=getRegXXIndNN(buffer, (upperCase? "IX": "ix")); 
        pos=this.pos;
        break;        
      case A_IY__NN:   // IY ind absolute (NN) 
        this.pos=pos;  
        result+=getRegXXIndNN(buffer, (upperCase? "IY": "iy")); 
        pos=this.pos;
        break;          
      case A__HL:     // ind (HL)  
        result+=(upperCase? "(HL)": "(hl)");  
        break;
      case A__IX:     // ind (IX)    
         result+=(upperCase? "(IX)": "(ix)");  
        break;        
      case A__IY:     // ind (IY)    
         result+=(upperCase? "(IY)": "(iy)");  
        break;          
      case A__HL_N:    // ind (HL) imm N 
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+=(upperCase? "(HL),": "(hl), ")+getLabelImm(pc+1, value);           
        break; 
      case A_A__NN:     // A ind (NN) 
        if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        else addr=-1;
        pos++;  
        
        result+=(upperCase? "A,(": "a,(")+getLabel(addr)+")";
        
        setLabel(addr);
        setLabelPlus(pc,1);
        setLabelPlus(pc,2);  
        break; 
      case A__IX_N_N: // ind (IX+N),N   
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;  
        
        result+="("+(upperCase? "IX": "ix")+"+"+getLabelImm(pc+1, value)+"),";
        
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+=getLabelImm(pc+2, value);                  
        break;  
      case A__IY_N_N: // ind (IY+N),N   
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;  
        
        result+="("+(upperCase? "IY": "iy")+"+"+getLabelImm(pc+1, value)+"),";
        
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+=getLabelImm(pc+2, value);                  
        break;                
      case A_A_A:    // A reg A
        result+=(upperCase? "A,A": "a,a");
        break;
      case A_A_B:    // A reg B
        result+=(upperCase? "A,B": "a,b");
        break;  
      case A_A_C:    // A reg C
        result+=(upperCase? "A,C": "a,c");
        break;  
      case A_A_D:    // A reg D
        result+=(upperCase? "A,D": "a,d");
        break;  
      case A_A_E:    // A reg E
        result+=(upperCase? "A,E": "a,e");
        break;  
      case A_A_H:    // A reg H
        result+=(upperCase? "A,H": "a,h");
        break;  
      case A_A_L:    // A reg L
        result+=(upperCase? "A,L": "a,l");
        break;   
      case A_A_I:    // A reg I
        result+=(upperCase? "A,I": "a,i");
        break;  
      case A_A_R:    // A reg R
        result+=(upperCase? "A,R": "a,r");
        break;  
      case A_B_A:    // B reg A
        result+=(upperCase? "B,A": "b,a");
        break;
      case A_B_B:    // B reg B
        result+=(upperCase? "B,B": "b,b");
        break;  
      case A_B_C:    // B reg C
        result+=(upperCase? "B,C": "b,c");
        break;  
      case A_B_D:    // B reg D
        result+=(upperCase? "B,D": "b,d");
        break;  
      case A_B_E:    // B reg E
        result+=(upperCase? "B,E": "b,e");
        break;  
      case A_B_H:    // B reg H
        result+=(upperCase? "B,H": "b,h");
        break;  
      case A_B_L:    // B reg L
        result+=(upperCase? "B,L": "b,l");
        break;   
      case A_C_A:    // C reg A
        result+=(upperCase? "C,A": "c,a");
        break;
      case A_C_B:    // C reg B
        result+=(upperCase? "C,B": "c,b");
        break;  
      case A_C_C:    // C reg C
        result+=(upperCase? "C,C": "c,c");
        break;  
      case A_C_D:    // C reg D
        result+=(upperCase? "C,D": "c,d");
        break;  
      case A_C_E:    // C reg E
        result+=(upperCase? "C,E": "c,e");
        break;  
      case A_C_H:    // C reg H
        result+=(upperCase? "C,H": "c,h");
        break;  
      case A_C_L:    // C reg L
        result+=(upperCase? "C,L": "c,l");
        break;
      case A_D_A:    // C reg A
        result+=(upperCase? "D,A": "d,a");
        break;
      case A_D_B:    // C reg B
        result+=(upperCase? "D,B": "d,b");
        break;  
      case A_D_C:    // C reg C
        result+=(upperCase? "D,C": "d,c");
        break;  
      case A_D_D:    // C reg D
        result+=(upperCase? "D,D": "d,d");
        break;  
      case A_D_E:    // C reg E
        result+=(upperCase? "D,E": "d,e");
        break;  
      case A_D_H:    // C reg H
        result+=(upperCase? "D,H": "d,h");
        break;  
      case A_D_L:    // C reg L
        result+=(upperCase? "D,L": "d,l");
        break;  
      case A_E_A:    // E reg A
        result+=(upperCase? "E,A": "e,a");
        break;
      case A_E_B:    // E reg B
        result+=(upperCase? "E,B": "e,b");
        break;  
      case A_E_C:    // E reg C
        result+=(upperCase? "E,C": "e,c");
        break;  
      case A_E_D:    // E reg D
        result+=(upperCase? "E,D": "e,d");
        break;  
      case A_E_E:    // E reg E
        result+=(upperCase? "E,E": "e,e");
        break;  
      case A_E_H:    // E reg H
        result+=(upperCase? "E,H": "e,h");
        break;  
      case A_E_L:    // E reg L
        result+=(upperCase? "E,L": "e,l");
        break;  
      case A_H_A:    // E reg A
        result+=(upperCase? "H,A": "h,a");
        break;
      case A_H_B:    // H reg B
        result+=(upperCase? "H,B": "h,b");
        break;  
      case A_H_C:    // H reg C
        result+=(upperCase? "H,C": "h,c");
        break;  
      case A_H_D:    // H reg D
        result+=(upperCase? "H,D": "h,d");
        break;  
      case A_H_E:    // H reg E
        result+=(upperCase? "H,E": "h,e");
        break;  
      case A_H_H:    // H reg H
        result+=(upperCase? "H,H": "h,h");
        break;  
      case A_H_L:    // H reg L
        result+=(upperCase? "H,L": "h,l");
        break;   
      case A_L_A:    // L reg A
        result+=(upperCase? "L,A": "l,a");
        break;
      case A_L_B:    // L reg B
        result+=(upperCase? "L,B": "l,b");
        break;  
      case A_L_C:    // L reg C
        result+=(upperCase? "L,C": "l,c");
        break;  
      case A_L_D:    // L reg D
        result+=(upperCase? "L,D": "l,d");
        break;  
      case A_L_E:    // L reg E
        result+=(upperCase? "L,E": "l,e");
        break;  
      case A_L_H:    // L reg H
        result+=(upperCase? "L,H": "l,h");
        break;  
      case A_L_L:    // L reg L
        result+=(upperCase? "L,L": "l,l");
        break; 
      case A_I_A:    // I reg A
        result+=(upperCase? "I,A": "i,a");
        break;
     case A_R_A:    // R reg A
        result+=(upperCase? "R,A": "r,a");
        break;  
     case A_00:     // 00h   
        result+="$00";
        break; 
     case A_08:     // 08h   
        result+="$08";
        break;    
     case A_10:     // 10h   
        result+="$10";
        break;      
     case A_18:     // 18h   
        result+="$18";
        break; 
     case A_20:     // 20h   
        result+="$20";
        break; 
     case A_28:     // 28h   
        result+="$28";
        break;       
      case A_30:    // 30h   
        result+="$30";
        break; 
     case A_38:     // 38h   
        result+="$38";
        break;  
     case A_Z:      // Z cond
        result+="Z";
        break;  
     case A_NZ:      // NZ cond
        result+="NZ";
        break;        
     case A_NC:      // NC cond
        result+="NC";
        break; 
     case A_C:      // C cond
        result+="C";
        break;         
     case A_PO:      // PO cond
        result+="PO";
        break;   
     case A_P:      // P cond
        result+="P";
        break; 
     case A_PE:      // PE cond
        result+="PE";
        break; 
     case A_M:      // M cond
        result+="M";
        break;    
     case A_N:
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+=getLabelImm(pc+1, value); 
        break; 
     case A_NN:    // absolute NN
        if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
        else addr=-1;                       
        pos++; 
        
        result+=getLabel(addr);
        setLabel(addr);
        setLabelPlus(pc,1);
        setLabelPlus(pc,2);      
        break;
     case A__N_A:  // (N) immediate A 
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+="("+getLabelImm(pc+1, value)+"),"+(upperCase? "A": "a"); 
        break; 
     case A_A__N:  // A immediate (N)
        if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
        else value=0;
        
        result+=(upperCase? "A": "a")+",("+getLabelImm(pc+1, value)+")";
        break; 
     case A_NZ_NN:   // NZ cond NN
        this.pos=pos; 
        result+=getRegXXNN(buffer,(upperCase? "NZ": "nz")); 
        pos=this.pos;
        break;        
     case A_Z_NN:   // Z cond NN
        this.pos=pos;
        result+=getRegXXNN(buffer,(upperCase? "Z": "z")); 
        pos=this.pos;
        break;
     case A_NC_NN:  // NC cond NN
        this.pos=pos; 
        result+=getRegXXNN(buffer,(upperCase? "NC": "nc")); 
        pos=this.pos;
        break;
     case A_C_NN:   // C cond NN
        this.pos=pos; 
        result+=getRegXXNN(buffer,(upperCase? "C": "C"));  
        pos=this.pos;
        break;
     case A_PO_NN:  // PO cond NN
        this.pos=pos; 
        result+=getRegXXNN(buffer,(upperCase? "PO": "po"));
        pos=this.pos;
        break;
     case A_P_NN:   // P cond NN
        this.pos=pos; 
        result+=getRegXXNN(buffer,(upperCase? "P": "p")); 
        pos=this.pos;
        break;
     case A_PE_NN:  // PE cond NN
        this.pos=pos;  
        result+=getRegXXNN(buffer,(upperCase? "PE": "pe")); 
        pos=this.pos;
        break; 
     case A_M_NN:   // M cond NN   
        this.pos=pos; 
        result+=getRegXXNN(buffer,(upperCase? "M": "m"));  
        pos=this.pos;
        break;
     case A_A__C:   // A reg ind (C)
        result+=(upperCase? "A,(C)": "a,(c)"); 
        break;
     case A_B__C:   // B reg ind (C)
        result+=(upperCase? "B,(C)": "b,(c)"); 
        break;   
     case A_C__C:   // C reg ind (C)
        result+=(upperCase? "C,(C)": "c,(c)"); 
        break;   
     case A_D__C:   // D reg ind (C)
        result+=(upperCase? "D,(C)": "d,(c)"); 
        break;   
     case A_E__C:   // E reg ind (C)
        result+=(upperCase? "E,(C)": "e,(c)"); 
        break;   
     case A_H__C:   // H reg ind (C)
        result+=(upperCase? "H,(C)": "h,(c)"); 
        break;   
     case A_L__C:   // L reg ind (C)
        result+=(upperCase? "L,(C)": "l,(c)"); 
        break;   
     case A___C:   // ind (C)
        result+=(upperCase? "(C)": "(c)"); 
        break;   
     case A__C_A:   // ind (C) reg A
        result+=(upperCase? "(C),A": "(c),a"); 
        break;  
     case A__C_B:   // ind (C) reg B
        result+=(upperCase? "(C),B": "(c),b"); 
        break;    
     case A__C_C:   // ind (C) reg C
        result+=(upperCase? "(C),C": "(c),c"); 
        break;     
     case A__C_D:   // ind (C) reg D
        result+=(upperCase? "(C),D": "(c),d"); 
        break;    
     case A__C_E:   // ind (C) reg E
        result+=(upperCase? "(C),E": "(c),e"); 
        break;     
     case A__C_H:   // ind (C) reg H
        result+=(upperCase? "(C),H": "(c),h"); 
        break;    
     case A__C_L:   // ind (C) reg L
        result+=(upperCase? "(C),L": "(c),l"); 
        break;    
     case A___C_0:  // ind C 0 
        result+=(upperCase? "(C),0": "(c),0"); 
        break;  
     case A_0:      // 0 
        result+="0"; 
        break; 
     case A_1:      // 1
        result+="1"; 
        break;    
     case A_2:      // 2
        result+="2"; 
        break;    
     case A_0_A:    // 0 reg A
        result+="0,"+(upperCase? "A": "a"); 
        break; 
     case A_0_B:    // 0 reg B
        result+="0,"+(upperCase? "B": "b"); 
        break;    
     case A_0_C:    // 0 reg C
        result+="0,"+(upperCase? "C": "c"); 
        break;    
     case A_0_D:    // 0 reg D
        result+="0,"+(upperCase? "D": "d"); 
        break;    
     case A_0_E:    // 0 reg E
        result+="0,"+(upperCase? "E": "e"); 
        break;    
     case A_0_H:    // 0 reg H
        result+="0,"+(upperCase? "H": "h"); 
        break;    
     case A_0_L:    // 0 reg L
        result+="0,"+(upperCase? "L": "l"); 
        break;    
     case A_0__HL:   // 0 ind (HL)
        result+="0,"+(upperCase? "(HL)": "(hl)"); 
        break;
     case A_1_A:    // 1 reg A
        result+="1,"+(upperCase? "A": "a"); 
        break; 
     case A_1_B:    // 1 reg B
        result+="1,"+(upperCase? "B": "b"); 
        break;    
     case A_1_C:    // 1 reg C
        result+="1,"+(upperCase? "C": "c"); 
        break;    
     case A_1_D:    // 1 reg D
        result+="1,"+(upperCase? "D": "d"); 
        break;    
     case A_1_E:    // 1 reg E
        result+="1,"+(upperCase? "E": "e"); 
        break;    
     case A_1_H:    // 1 reg H
        result+="1,"+(upperCase? "H": "h"); 
        break;    
     case A_1_L:    // 1 reg L
        result+="1,"+(upperCase? "L": "l"); 
        break;    
     case A_1__HL:   // 1 ind (HL)
        result+="1,"+(upperCase? "(HL)": "(hl)"); 
        break;   
     case A_2_A:    // 2 reg A
        result+="2,"+(upperCase? "A": "a"); 
        break; 
     case A_2_B:    // 2 reg B
        result+="2,"+(upperCase? "B": "b"); 
        break;    
     case A_2_C:    // 2 reg C
        result+="2,"+(upperCase? "C": "c"); 
        break;    
     case A_2_D:    // 2 reg D
        result+="2,"+(upperCase? "D": "d"); 
        break;    
     case A_2_E:    // 2 reg E
        result+="2,"+(upperCase? "E": "e"); 
        break;    
     case A_2_H:    // 2 reg H
        result+="2,"+(upperCase? "H": "h"); 
        break;    
     case A_2_L:    // 2 reg L
        result+="2,"+(upperCase? "L": "l"); 
        break;    
     case A_2__HL:   // 2 ind (HL)
        result+="2,"+(upperCase? "(HL)": "(hl)"); 
        break;        
     case A_3_A:    // 3 reg A
        result+="3,"+(upperCase? "A": "a"); 
        break; 
     case A_3_B:    // 3 reg B
        result+="3,"+(upperCase? "B": "b"); 
        break;    
     case A_3_C:    // 3 reg C
        result+="3,"+(upperCase? "C": "c"); 
        break;    
     case A_3_D:    // 3 reg D
        result+="3,"+(upperCase? "D": "d"); 
        break;    
     case A_3_E:    // 3 reg E
        result+="3,"+(upperCase? "E": "e"); 
        break;    
     case A_3_H:    // 3 reg H
        result+="3,"+(upperCase? "H": "h"); 
        break;    
     case A_3_L:    // 3 reg L
        result+="3,"+(upperCase? "L": "l"); 
        break;    
     case A_3__HL:   // 3 ind (HL)
        result+="3,"+(upperCase? "(HL)": "(hl)"); 
        break;       
     case A_4_A:    // 4 reg A
        result+="4,"+(upperCase? "A": "a"); 
        break; 
     case A_4_B:    // 4 reg B
        result+="4,"+(upperCase? "B": "b"); 
        break;    
     case A_4_C:    // 4 reg C
        result+="4,"+(upperCase? "C": "c"); 
        break;    
     case A_4_D:    // 4 reg D
        result+="4,"+(upperCase? "D": "d"); 
        break;    
     case A_4_E:    // 4 reg E
        result+="4,"+(upperCase? "E": "e"); 
        break;    
     case A_4_H:    // 4 reg H
        result+="4,"+(upperCase? "H": "h"); 
        break;    
     case A_4_L:    // 4 reg L
        result+="4,"+(upperCase? "L": "l"); 
        break;    
     case A_4__HL:   // 4 ind (HL)
        result+="4,"+(upperCase? "(HL)": "(hl)"); 
        break;   
     case A_5_A:    // 5 reg A
        result+="5,"+(upperCase? "A": "a"); 
        break; 
     case A_5_B:    // 5 reg B
        result+="5,"+(upperCase? "B": "b"); 
        break;    
     case A_5_C:    // 5 reg C
        result+="5,"+(upperCase? "C": "c"); 
        break;    
     case A_5_D:    // 5 reg D
        result+="5,"+(upperCase? "D": "d"); 
        break;    
     case A_5_E:    // 5 reg E
        result+="5,"+(upperCase? "E": "e"); 
        break;    
     case A_5_H:    // 5 reg H
        result+="5,"+(upperCase? "H": "h"); 
        break;    
     case A_5_L:    // 5 reg L
        result+="5,"+(upperCase? "L": "l"); 
        break;    
     case A_5__HL:   // 5 ind (HL)
        result+="5,"+(upperCase? "(HL)": "(hl)"); 
        break;   
     case A_6_A:    // 6 reg A
        result+="6,"+(upperCase? "A": "a"); 
        break; 
     case A_6_B:    // 6 reg B
        result+="6,"+(upperCase? "B": "b"); 
        break;    
     case A_6_C:    // 6 reg C
        result+="6,"+(upperCase? "C": "c"); 
        break;    
     case A_6_D:    // 6 reg D
        result+="6,"+(upperCase? "D": "d"); 
        break;    
     case A_6_E:    // 6 reg E
        result+="6,"+(upperCase? "E": "e"); 
        break;    
     case A_6_H:    // 6 reg H
        result+="6,"+(upperCase? "H": "h"); 
        break;    
     case A_6_L:    // 6 reg L
        result+="6,"+(upperCase? "L": "l"); 
        break;    
     case A_6__HL:   // 6 ind (HL)
        result+="6,"+(upperCase? "(HL)": "(hl)"); 
        break;  
     case A_7_A:    // 7 reg A
        result+="7,"+(upperCase? "A": "a"); 
        break; 
     case A_7_B:    // 7 reg B
        result+="7,"+(upperCase? "B": "b"); 
        break;    
     case A_7_C:    // 7 reg C
        result+="7,"+(upperCase? "C": "c"); 
        break;    
     case A_7_D:    // 7 reg D
        result+="7,"+(upperCase? "D": "d"); 
        break;    
     case A_7_E:    // 7 reg E
        result+="7,"+(upperCase? "E": "e"); 
        break;    
     case A_7_H:    // 7 reg H
        result+="7,"+(upperCase? "H": "h"); 
        break;    
     case A_7_L:    // 7 reg L
        result+="7,"+(upperCase? "L": "l"); 
        break;    
     case A_7__HL:   // 7 ind (HL)
        result+="7,"+(upperCase? "(HL)": "(hl)"); 
        break;  
     case  A_REG_IX: // reg IX
        result+=(upperCase? "IX": "ix");  
        break; 
     case A_REG_IY:   // reg IY
         result+=(upperCase? "IY": "iy");  
         break;
     case A_REG_IXH:  // reg IXH
         result+=(upperCase? "IXH": "ixh");  
         break;
     case A_REG_IXL:  // reg IXL
         result+=(upperCase? "IXL": "ixl");  
         break;
     case A_REG_IYH:  // reg IYH
         result+=(upperCase? "IYH": "iyh");  
         break;
     case A_REG_IYL:   //reg IYL      
         result+=(upperCase? "IYL": "iyl");  
         break;
     case A__IX_N:    // ind (IX+N)   
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IX": "ix"));
         pos=this.pos;
         break;
     case A__IY_N:    // ind (IY+N)
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IY": "iy"));
         pos=this.pos;
         break;
     case A__IX_N_A:    // ind (IX+N),A
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IX": "ix"), (upperCase? "A": "a"));
         pos=this.pos;
         break;    
     case A__IX_N_B:    // ind (IX+N),B
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IX": "ix"), (upperCase? "B": "b"));
         pos=this.pos;
         break;       
     case A__IX_N_C:    // ind (IX+N),C
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IX": "ix"), (upperCase? "C": "c"));
         pos=this.pos;
         break;          
     case A__IX_N_D:    // ind (IX+N),D
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IX": "ix"), (upperCase? "D": "d"));
         pos=this.pos;
         break;           
     case A__IX_N_E:    // ind (IX+N),E
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IX": "ix"), (upperCase? "E": "e"));
         pos=this.pos;
         break;          
     case A__IX_N_H:    // ind (IX+N),H
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IX": "ix"), (upperCase? "H": "h"));
         pos=this.pos;
         break;          
     case A__IX_N_L:    // ind (IX+N),L
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IX": "ix"), (upperCase? "L": "l"));
         pos=this.pos;
         break;   
     case A__IY_N_A:    // ind (IY+N),A
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IY": "iy"), (upperCase? "A": "a"));
         pos=this.pos;
         break;    
     case A__IY_N_B:    // ind (IY+N),B
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IY": "iy"), (upperCase? "B": "b"));
         pos=this.pos;
         break;       
     case A__IY_N_C:    // ind (IY+N),C
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IY": "iy"), (upperCase? "C": "c"));
         pos=this.pos;
         break;          
     case A__IY_N_D:    // ind (IY+N),D
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IY": "iy"), (upperCase? "D": "d"));
         pos=this.pos;
         break;           
     case A__IY_N_E:    // ind (IY+N),E
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IY": "iy"), (upperCase? "E": "e"));
         pos=this.pos;
         break;          
     case A__IY_N_H:    // ind (IY+N),H
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IY": "iy"), (upperCase? "H": "h"));
         pos=this.pos;
         break;          
     case A__IY_N_L:    // ind (IY+N),L
         this.pos=pos;
         result+=getRegIndXN(buffer, (upperCase? "IY": "iy"), (upperCase? "L": "l"));
         pos=this.pos;
         break;           
     case A_0__IX_N_A: // 0,(IX+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IX": "ix"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_0__IX_N_B: // 0,(IX+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IX": "ix"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_0__IX_N_C: // 0,(IX+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IX": "ix"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_0__IX_N_D: // 0,(IX+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IX": "ix"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_0__IX_N_E: // 0,(IX+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IX": "ix"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_0__IX_N_H: // 0,(IX+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IX": "ix"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_0__IX_N_L: // 0,(IX+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IX": "ix"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;    
     case A_1__IX_N_A: // 1,(IX+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IX": "ix"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_1__IX_N_B: // 1,(IX+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IX": "ix"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_1__IX_N_C: // 1,(IX+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IX": "ix"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_1__IX_N_D: // 1,(IX+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IX": "ix"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_1__IX_N_E: // 1,(IX+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IX": "ix"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_1__IX_N_H: // 1,(IX+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IX": "ix"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_1__IX_N_L: // 1,(IX+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IX": "ix"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;  
    case A_2__IX_N_A: // 2,(IX+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IX": "ix"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_2__IX_N_B: // 2,(IX+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IX": "ix"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_2__IX_N_C: // 2,(IX+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IX": "ix"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_2__IX_N_D: // 2,(IX+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IX": "ix"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_2__IX_N_E: // 2,(IX+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IX": "ix"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_2__IX_N_H: // 2,(IX+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IX": "ix"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_2__IX_N_L: // 2,(IX+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IX": "ix"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;           
    case A_3__IX_N_A: // 3,(IX+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IX": "ix"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_3__IX_N_B: // 3,(IX+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IX": "ix"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_3__IX_N_C: // 3,(IX+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IX": "ix"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_3__IX_N_D: // 3,(IX+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IX": "ix"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_3__IX_N_E: // 3,(IX+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IX": "ix"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_3__IX_N_H: // 3,(IX+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IX": "ix"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_3__IX_N_L: // 3,(IX+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IX": "ix"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;           
    case A_4__IX_N_A: // 4,(IX+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IX": "ix"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_4__IX_N_B: // 4,(IX+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IX": "ix"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_4__IX_N_C: // 4,(IX+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IX": "ix"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_4__IX_N_D: // 4,(IX+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IX": "ix"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_4__IX_N_E: // 4,(IX+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IX": "ix"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_4__IX_N_H: // 4,(IX+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IX": "ix"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_4__IX_N_L: // 4,(IX+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IX": "ix"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;           
     case A_5__IX_N_A: // 5,(IX+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IX": "ix"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_5__IX_N_B: // 5,(IX+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IX": "ix"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_5__IX_N_C: // 5,(IX+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IX": "ix"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_5__IX_N_D: // 5,(IX+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IX": "ix"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_5__IX_N_E: // 5,(IX+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IX": "ix"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_5__IX_N_H: // 5,(IX+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IX": "ix"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_5__IX_N_L: // 5,(IX+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IX": "ix"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;      
     case A_6__IX_N_A: // 6,(IX+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IX": "ix"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_6__IX_N_B: // 6,(IX+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IX": "ix"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_6__IX_N_C: // 6,(IX+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IX": "ix"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_6__IX_N_D: // 6,(IX+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IX": "ix"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_6__IX_N_E: // 6,(IX+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IX": "ix"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_6__IX_N_H: // 6,(IX+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IX": "ix"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_6__IX_N_L: // 6,(IX+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IX": "ix"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;     
     case A_7__IX_N_A: // 7,(IX+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IX": "ix"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_7__IX_N_B: // 7,(IX+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IX": "ix"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_7__IX_N_C: // 7,(IX+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IX": "ix"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_7__IX_N_D: // 7,(IX+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IX": "ix"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_7__IX_N_E: // 7,(IX+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IX": "ix"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_7__IX_N_H: // 7,(IX+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IX": "ix"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_7__IX_N_L: // 7,(IX+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IX": "ix"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;             
     case A_0__IY_N_A: // 0,(IY+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IY": "iy"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_0__IY_N_B: // 0,(IY+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IY": "iy"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_0__IY_N_C: // 0,(IY+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IY": "iy"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_0__IY_N_D: // 0,(IY+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IY": "iy"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_0__IY_N_E: // 0,(IY+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IY": "iy"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_0__IY_N_H: // 0,(IY+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IY": "iy"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_0__IY_N_L: // 0,(IY+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "0", (upperCase? "IY": "iy"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;    
     case A_1__IY_N_A: // 1,(IY+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IY": "iy"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_1__IY_N_B: // 1,(IY+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IY": "iy"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_1__IY_N_C: // 1,(IY+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IY": "iy"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_1__IY_N_D: // 1,(IY+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IY": "iy"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_1__IY_N_E: // 1,(IY+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IY": "iy"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_1__IY_N_H: // 1,(IY+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IY": "iy"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_1__IY_N_L: // 1,(IY+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "1", (upperCase? "IY": "iy"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;  
    case A_2__IY_N_A: // 2,(IY+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IY": "iy"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_2__IY_N_B: // 2,(IY+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IY": "iy"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_2__IY_N_C: // 2,(IY+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IY": "iy"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_2__IY_N_D: // 2,(IY+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IY": "iy"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_2__IY_N_E: // 2,(IY+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IY": "iy"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_2__IY_N_H: // 2,(IY+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IY": "iy"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_2__IY_N_L: // 2,(IY+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "2", (upperCase? "IY": "iy"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;           
     case A_3__IY_N_A: // 3,(IY+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IY": "iy"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_3__IY_N_B: // 3,(IY+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IY": "iy"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_3__IY_N_C: // 3,(IY+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IY": "iy"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_3__IY_N_D: // 3,(IY+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IY": "iy"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_3__IY_N_E: // 3,(IY+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IY": "iy"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_3__IY_N_H: // 3,(IY+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IY": "iy"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_3__IY_N_L: // 3,(IY+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "3", (upperCase? "IY": "iy"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;           
     case A_4__IY_N_A: // 4,(IY+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IY": "iy"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_4__IY_N_B: // 4,(IY+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IY": "iy"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_4__IY_N_C: // 4,(IY+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IY": "iy"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_4__IY_N_D: // 4,(IY+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IY": "iy"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_4__IY_N_E: // 4,(IY+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IY": "iy"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_4__IY_N_H: // 4,(IY+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IY": "iy"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_4__IY_N_L: // 4,(IY+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "4", (upperCase? "IY": "iy"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;           
     case A_5__IY_N_A: // 5,(IY+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IY": "iy"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_5__IY_N_B: // 5,(IY+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IY": "iy"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_5__IY_N_C: // 5,(IY+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IY": "iy"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_5__IY_N_D: // 5,(IY+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IY": "iy"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_5__IY_N_E: // 5,(IY+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IY": "iy"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_5__IY_N_H: // 5,(IY+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IY": "iy"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_5__IY_N_L: // 5,(IY+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "5", (upperCase? "IY": "iy"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;      
     case A_6__IY_N_A: // 6,(IY+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IY": "iy"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_6__IY_N_B: // 6,(IY+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IY": "iy"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_6__IY_N_C: // 6,(IY+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IY": "iy"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_6__IY_N_D: // 6,(IY+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IY": "iy"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_6__IY_N_E: // 6,(IY+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IY": "iy"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_6__IY_N_H: // 6,(IY+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IY": "iy"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_6__IY_N_L: // 6,(IY+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "6", (upperCase? "IY": "iy"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;     
     case A_7__IY_N_A: // 7,(IY+N),A
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IY": "iy"), (upperCase? "A": "a"));
         pos=this.pos;
         break;
     case A_7__IY_N_B: // 7,(IY+N),B
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IY": "iy"), (upperCase? "B": "b"));
         pos=this.pos;                  
         break;
     case A_7__IY_N_C: // 7,(IY+N),C
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IY": "iy"), (upperCase? "C": "c"));
         pos=this.pos;                  
         break;    
     case A_7__IY_N_D: // 7,(IY+N),D
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IY": "iy"), (upperCase? "D": "d"));
         pos=this.pos;                  
         break;    
     case A_7__IY_N_E: // 7,(IY+N),E
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IY": "iy"), (upperCase? "E": "e"));
         pos=this.pos;                  
         break;   
     case A_7__IY_N_H: // 7,(IY+N),H
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IY": "iy"), (upperCase? "H": "h"));
         pos=this.pos;                  
         break;   
     case A_7__IY_N_L: // 7,(IY+N),L
         this.pos=pos;
         result+=getReg2IndXN(buffer, "7", (upperCase? "IY": "iy"), (upperCase? "L": "l"));
         pos=this.pos;                  
         break;         
    }    
    
    // add eventaul relative address of instructions
    switch (steps) {
        case 4:
          setLabelPlus(pc,3);      
        case 3:
          setLabelPlus(pc,2);
        case 2:
          setLabelPlus(pc,1);
    }
    
    this.pc=pc+steps;
    this.pos=pos;  
    
    return result;
  }  

  @Override
  public String dcom(int iType, int aType, long addr, long value) {
    return "";
  }
  
  /**
   * Get the instruction register indirect over byte
   * 
   * @param buffer the buffer to use
   * @param reg the reg to use
   * @return the instruction
   */
  private String getRegIndXN(byte[] buffer, String reg) {
    if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
    else value=-1; 
        
    return "("+reg+"+"+getLabelImm(pc+1, value)+")";  
  }
  
  /**
   * Get the instruction register 2 indirect over byte
   * 
   * @param buffer the buffer to use
   * @param reg the reg to use
   * @param reg2 the reg2 to use
   * @param reg3 the reg3 to use
   * @return the instruction
   */
  private String getReg2IndXN(byte[] buffer, String reg, String reg2, String reg3) {
    if (pos<buffer.length) value=Unsigned.done(buffer[pos-1]);
    else value=-1; 
    pos++;    
    return reg+"("+reg2+"+"+getLabelImm(pc+1, value)+"),"+reg3;  
  }
      
  /**
   * Get the instruction register indirect over byte
   * 
   * @param buffer the buffer to use
   * @param reg the reg to use
   * @param reg2 the reg2 to use
   * @return the instruction
   */
  private String getRegIndXN(byte[] buffer, String reg, String reg2) {
    if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
    else value=-1; 
    
    return "("+reg+"+"+getLabelImm(pc+1, value)+"),"+reg2;  
  }
  
  /**
   * Get the instruction register over byte
   * 
   * @param buffer the buffer to use
   * @param reg the reg to use
   * @return the instruction
   */
  private String getRegXN(byte[] buffer, String reg) {
    if (pos<buffer.length) value=Unsigned.done(buffer[pos++]);
    else value=-1; 
        
    return reg+","+getLabelImm(pc+1, value);  
  }
  
  /**
   * Get the instruction registers over word
   * 
   * @param buffer the buffer to use
   * @param reg the regs to use
   * @return the instruction
   */
  private String getRegXXNN(byte[] buffer, String reg) {
    if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
    else addr=-1;
    pos++;    
                      
    setLabel(addr);
    //setLabelPlus(pc,1);
    //setLabelPlus(pc,2);  
    return reg+","+getLabel(addr);   
  }
  
    /**
   * Get the instruction registers ind over word
   * 
   * @param buffer the buffer to use
   * @param reg the regs to use
   * @return the instruction
   */
  private String getRegXXIndNN(byte[] buffer, String reg) {
    if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
    else addr=-1;
    pos++;    
      
    setLabel(addr);
    //setLabelPlus(pc,1);
    //setLabelPlus(pc,2);
    return reg+",("+getLabel(addr)+")";
   }     
  
  /**
   * Get the instruction word over register
   * 
   * @param buffer the bufffer to use
   * @param reg the reg to use
   * @return the instruction
   */
  private String getNNregX(byte[] buffer, String reg) {
    if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
    else addr=-1;
    pos++;  
        
    setLabel(addr);
    //setLabelPlus(pc,1);
    //setLabelPlus(pc,2);
    return getLabel(addr)+","+reg;    
  }
  
  /**
   * Get the instruction indirect word over register
   * 
   * @param buffer the bufffer to use
   * @param reg the reg to use
   * @return the instruction
   */
  private String getIndNNregX(byte[] buffer, String reg) {
    if (pos<buffer.length-1) addr=((Unsigned.done(buffer[pos+1])<<8) | Unsigned.done(buffer[pos++]));
    else addr=-1;
    pos++;  
        
    setLabel(addr);
    //setLabelPlus(pc,1);
    //setLabelPlus(pc,2);
    return "("+getLabel(addr)+"),"+reg;    
  }
  
  /**
   * Get the instruction 
   * 
   * @param buffer the bufffer to use
   * @param reg the reg to use
   * @param reg2 the reg ind to use
   * @return the instruction 
   */
  private String getRefXIndXXN(byte[] buffer, String reg, String reg2) {
    if (pos<buffer.length) addr=Unsigned.done(buffer[pos++]);
    else addr=-1; 
       
    return reg+",("+reg2+"+"+getLabelZero(addr)+")";  
  }
  
  /**
   * Get the instruction 
   * 
   * @param buffer the bufffer to use
   * @param reg the reg to use
   * @param reg2 the reg ind to use
   * @return the instruction 
   */
  private String getRefXInd2XXN(byte[] buffer, String reg, String reg2) {
    if (pos<buffer.length) addr=Unsigned.done(buffer[pos-1]);
    else addr=-1; 
    
    pos++;
       
    return reg+",("+reg2+"+"+getLabelZero(addr)+")";  
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
    int actualOffset;            // actual offset
    int pos=start;               // actual position in buffer
    boolean isCode=true;         // true if we are decoding an instruction
    boolean wasGarbage=false;    // true if we were decoding garbage
        
    result.setLength(0);
    //result.append(addConstants());
    
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
          actualOffset=assembler.getCarets().getOffset();                               // rember actual offset
          assembler.getCarets().setOffset(result.length()+actualOffset+21);             // use new offset
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
          if (this.pc-pc==4) {
            if (pos+3<buffer.length) tmp2+=" "+ByteToExe(Unsigned.done(buffer[pos+1]))+
                                           " "+ByteToExe(Unsigned.done(buffer[pos+2]))+
                                           " "+ByteToExe(Unsigned.done(buffer[pos+3]));
            else tmp2+=" ???????";
          }          
          
          for (int i=tmp2.length(); i<21; i++) // insert spaces
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
          if (iType==M_RET || iType==M_RETI || iType==M_RETN) result.append("\n");    
          
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
    //result.append(addConstants());
    
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
          
          // this is an instruction
          actualOffset=assembler.getCarets().getOffset();                               // rember actual offset
          assembler.getCarets().setOffset(result.length()+actualOffset);                // use new offset
          tmp=dasm(buffer);                                                             // this is an instruction
          assembler.getCarets().setOffset(actualOffset);                                // set old offset   
  
          result.append(getInstrSpacesTabs(mem)).append(tmp).append(getInstrCSpacesTabs(tmp.length()));
          assembler.getCarets().add(pStart, result.length(), mem, Type.INSTR);
          
          tmp2=dcom();   
          
          // if there is a user comment, then use it
          assembler.setComment(result, mem);
          
          // always add a carriage return after a RTS, RTI or JMP
          if (iType==M_RET || iType==M_RETI || iType==M_RETN) result.append("\n");        
          
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
   * Return a comment string for the last instruction
   *
   * @return a comment string
   */
  public String dcom() {
    switch (iType) {
      case M_SLL:  
        return "Undocument instruction";  
    }
    return "";
  }
}
