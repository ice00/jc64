/**
 * @(#)cRSID.java 2023/03/19
 *
 * ICE Team free software group
 *
 * This file is part of C64 Java Software Emulator.
 * See README for copyright notice.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 */
package sw_emulator.software.sidid;


/**
 * Emulate the CPU of cRSID originl by Hermit
 * 
 * @author ice00
 */
public class CPU {  
  /** Reference to the containing c64 */
  C64 c64; 
  
  int PC;
  int A, SP;
  int X, Y, ST; //STATUS-flags: N V - B D I Z C
  int prevNMI;  //used for nmi leading edge     
  
  byte cycles;
  int samePage;
  int IR; //, ST, X, Y;
  //int A, SP, 
  int T;
  //int PC, 
  int addr, prevPC;
  
  public static final int N=0x80;
  public static final int V=0x40;
  public static final int B=0x10;
  public static final int D=0x08;
  public static final int I=0x04;
  public static final int Z=0x02;
  public static final int C=0x01;
  
  public static final short[] FlagSwitches={0x01,0x21,0x04,0x24,0x00,0x40,0x08,0x28};
  public static final short[] BranchFlags={0x80,0x40,0x01,0x02};
    
  /**
   * Construct the cpu
   * 
   * @param c64 the c64 reference
   */
  public CPU(C64 c64) {
    this.c64=c64;
  }

  /**
   * Init the cpu chip
   * 
   * @param mempos the starting position
   */
  public void initCPU(int mempos) {
    PC = mempos; 
    A = 0; 
    X = 0; 
    Y = 0; 
    ST = 0x04; 
    SP = 0xFF;
    prevNMI = 0;  
  }
  
  /**
   * Read a byte from address
   * 
   * @param address tjhe address
   * @return the value
   */
  private int rd(int address) {
    int value;
    value = c64.readMemC64(address);
    if (c64.realSIDmode) {
      if ( (c64.ramBank[1] & 3)!=0 ) {
        if (address==0xDC0D) { 
          c64.cia[0].acknowledgeCIAIRQ(); 
        } else if (address==0xDD0D) { 
                 c64.cia[1].acknowledgeCIAIRQ(); 
               }
      }
    }
    Memory.instance.setRead(address);
    return value;
  }
  
  /**
   * Write data to an address
   * 
   * @param address the address
   * @param data the data to wrote
   */
  private void wr(int address, int data) {  
    c64.writeMemC64(address, data);
    if ( c64.realSIDmode && (c64.ramBank[1] & 3)!=0 ) {
      if (address==0xD019) {  
        c64.vic.acknowledgeVICrasterIRQ(); 
      }  
    }
    Memory.instance.setWrite(address);
  }
  
  /**
   * Write data to an address (PSID-hack specific memory-write)
   * 
   * @param address the address
   * @param data the data to wrote
   */
  private void wr2(int address, int data) { 
    int tmp;
    c64.writeMemC64(address, data);
    if ( (c64.ramBank[1] & 3 )!=0) {
      if (c64.realSIDmode) {
        if (address==0xDC0D) c64.cia[0].writeCIAIRQmask(data );
        else if (address==0xDD0D) c64.cia[1].writeCIAIRQmask(data );
        else if (address==0xDD0C) c64.ioBankRD[address]=data; //mirror WR to RD (e.g. Wonderland_XIII_tune_1.sid)
        else if(address==0xD019 && (data&1)!=0) { //only writing 1 to $d019 bit0 would acknowledge
          c64.vic.acknowledgeVICrasterIRQ();
        }
      } else {
          switch (address) {
            case 0xDC05: 
            case 0xDC04:
              if ((c64.timerSource )!=0) { //dynamic cia-setting (Galway/Rubicon workaround)
                c64.frameCycles = ( (c64.ioBankWR[0xDC04] + (c64.ioBankWR[0xDC05]<<8)) ); //<< 4) / c64->sampleClockRatio;
              }
              break;
            case 0xDC08: 
              c64.ioBankRD[0xDC08] = data; 
              break; //refresh TOD-clock
            case 0xDC09: 
              c64.ioBankRD[0xDC09] = data; 
              break; //refresh TOD-clock
            case 0xD012: //dynamic VIC irq-rasterline setting (Microprose Soccer V1 workaround)
              if (c64.prevRasterLine>=0) { //was $d012 set before? (or set only once?)
                if (c64.ioBankWR[0xD012] != c64.prevRasterLine) {
                  tmp = c64.ioBankWR[0xD012] - c64.prevRasterLine;
                  if (tmp<0) tmp += c64.vic.rasterLines;
                  c64.frameCycleCnt = c64.frameCycles - tmp * c64.vic.rasterRowCycles;
                }
              }
              c64.prevRasterLine = c64.ioBankWR[0xD012];
              break;
           }
        }
    }
    Memory.instance.setWrite(address);
  }
  
  /**
   * Addressing mode immediate
   */
  private void addrModeImmediate () { 
    ++PC; 
    addr=PC; 
    cycles=2; 
    Memory.instance.setExecute(PC);
  }
 
  /** 
   * Addressing mode zero page 
   */
  private void addrModeZeropage () { 
    ++PC; 
    addr=rd(PC); 
    cycles=3; 
    Memory.instance.setExecuteMinus(PC);
    Memory.instance.setExecute(PC);
  } 
 
  /** 
   * Addressing mode absolute 
   */
  private void addrModeAbsolute () { 
    ++PC; 
    addr = rd(PC); 
    ++PC;
    addr += rd(PC)<<8; 
    cycles=4; 
    Memory.instance.setExecuteMinus(PC);
    Memory.instance.setExecute(PC);
  } 
 
  /** 
   * Addressing mode zero page X indexed
   * 
   * zp,x (with zeropage-wraparound of 6502)
   */
  private void addrModeZeropageXindexed () { 
    ++PC; 
    addr = (rd(PC) + X) & 0xFF; 
    cycles=4; 
    Memory.instance.setExecute(PC);
  } 
    
  /**
   * Addressign mode zero page Y indexed
   * 
   * zp,y (with zeropage-wraparound of 6502)
   */
  private void addrModeZeropageYindexed () { 
    ++PC; 
    addr = (rd(PC) + Y) & 0xFF; 
    cycles=4; 
    Memory.instance.setExecute(PC);
  } 
  
  /**
   * Addressing mode X indexed
   * 
   * abs,x (only STA is 5 cycles, others are 4 if page not crossed, RMW:7)
   */
  private void addrModeXindexed () { 
    ++PC; 
    addr = rd(PC) + X; 
    ++PC; 
    samePage = (addr <= 0xFF) ? 1:0; 
    addr += rd(PC)<<8; 
    cycles=5;
    Memory.instance.setExecuteMinus(PC);
    Memory.instance.setExecute(PC);
  }

  /**
   * Addressing mode Y indexed
   * 
   * abs,y (only STA is 5 cycles, others are 4 if page not crossed, RMW:7)
   */
  private void addrModeYindexed () {  
    ++PC; 
    addr = rd(PC) + Y; 
    ++PC; 
    samePage = (addr <= 0xFF) ? 1:0; 
    addr += rd(PC)<<8; 
    cycles=5;
    Memory.instance.setExecuteMinus(PC);
    Memory.instance.setExecute(PC);
  }

  /**
   * Addressing mode indirect Y indexed
   * 
   * (zp),y (only STA is 6 cycles, others are 5 if page not crossed, RMW:8)
   */
  private void addrModeIndirectYindexed() { 
    ++PC; 
    addr = rd(rd(PC)) + Y; 
    samePage = (addr <= 0xFF) ? 1:0; 
    addr += rd( (rd(PC)+1)&0xFF ) << 8; 
    cycles=6;
    Memory.instance.setExecute(PC);
  }

  /**
   * Addressing mode X indexed indirect
   * 
   * (zp,x)
   */
  private void addrModeXindexedIndirect() {
    ++PC; 
    addr = ( rd(rd(PC)+X)&0xFF ) + ( ( rd(rd(PC)+X+1)&0xFF ) << 8 ); 
    cycles=6;
    Memory.instance.setExecute(PC);
  }
  
  /**
   * Clear Carry-flag
   */
  private void clrC () {
    ST &= ~C; 
  } 
  
  /**
   * Set the Carry-flag
   * 
   * @param expr boolean value for the carry
   */
  private void setC (boolean expr) { 
    setC(expr ? 1:0);
  }
  
  /**
   * Set Carry-flag if expression is not zero
   * 
   * @param expr the expression to evaluate
   */
  private void setC (int expr) { 
    ST &= ~C; 
    ST |= (expr!=0) ? 1:0; 
  } 
  
  /**
   * Cleat the N/Z/C flags
   */
  private void clrNZC() {
    ST &= ~(N | Z | C);
  } 

  /**
   * Clear the N/V/Z/C flags
   */
  private void clrNVZC() {
    ST &= ~(N | V | Z | C);
  } 
  
  /**
   * Set Negative-flag and Zero-flag based on result in Accumulator
   */
  private void setNZbyA() { 
    ST &= ~(N|Z); 
    ST |= (((!(A!=0))?1:0)<<1) | (A&N); 
  } 
  
  /**
   * Set NZ flags
   */
  private void setNZbyT() { 
    T&=0xFF; 
    ST &= ~(N|Z); 
    ST |= (((!(T!=0))?1:0)<<1) | (T&N); 
  }
  
  /**
   * Set Negative-flag and Zero-flag based on result in X-register
   */
  private void setNZbyX() { 
    ST &= ~(N|Z); 
    ST |= (((!(X!=0))?1:0) <<1) | (X&N); 
  } 
  
  /**
   * Set Negative-flag and Zero-flag based on result in Y-register
   */
  private void setNZbyY() {
    ST &= ~(N|Z);
    ST |= (((!(Y!=0))?1:0)<<1) | (Y&N); 
  } 

  /**
   * Set Negative-flag and Zero-flag based on result at Memory-Address
   */
  private void setNZbyM() {
    ST &= ~(N | Z);
    ST |= ( ((!(rd(addr)!=0))?1:0) << 1) | (rd(addr) & N);
  } 

  /**
   * Set NZC after increase/addition
   */
  private void setNZCbyAdd() {
    ST &= ~(N | Z | C);
    ST |= (A & N) | ((A > 255)?1:0);
    A &= 0xFF;
    ST |= ((!(A!=0))?1:0) << 1;
  } 

  /**
   * Calculate V-flag from A and T (previous A) and input2 (Memory)
   *
   * @param M thr value to use
   */
  private void setVbyAdd(int M) {
    ST &= ~V;
    ST |= ((~(T ^ M)) & (T ^ A) & N) >> 1;
  } //
   
  /**
   * Set NZC and modify caller
   *
   * @param obj the object to test
   * @return the new value to give to object
   */
  private int setNZCbySub(int obj) {
    ST &= ~(N | Z | C);
    ST |= (obj & N) | ((obj >= 0) ? 1 : 0);
    obj &= 0xFF;
    ST |= (((!(obj != 0)) ? 1 : 0) << 1);

    return obj;
  }

  /**
   * Push a value into the stack
   *
   * @param value the value to push
   */
  private void push(int value) {
    c64.ramBank[0x100 + SP] = value;
    --SP;
    SP &= 0xFF;
  }

  /**
   * Pop a value from the stack
   *
   * @return the value
   */
  private int pop() {
    ++SP;
    SP &= 0xFF;
    return c64.ramBank[0x100 + SP];
  } 
  
   /**
    * The cpu emulation for sid/PRG playback (ToDo: cia/VIC-irq/nmi/RESET vectors, BCD-mode)    
    * 
    * @return the break type
    */
  public byte emulateCPU() { 
    // loadReg(); 
    prevPC = PC;
    IR = rd(PC);
    cycles = 2;
    samePage = 0; //'cycles': ensure smallest 6510 runtime (for implied/register instructions)
    
    Memory.instance.setExecute(PC);

/*System.err.println("PC="+Integer.toHexString(PC)+" "+
                   "IR="+Integer.toHexString(IR)+" "+
                   "A="+Integer.toHexString(A)+" "+
                   "X="+Integer.toHexString(X)+" "+
                   "Y="+Integer.toHexString(Y)+" "+
                   "SP="+Integer.toHexString(SP)+" "+
                   "ST="+Integer.toHexString(ST)
        );*/
    if ((IR & 1) != 0) {  //nybble2:  1/5/9/D:accu.instructions, 3/7/B/F:illegal opcodes
      switch ((IR & 0x1F) >> 1) { //value-forming to cause jump-table //PC wraparound not handled inside to save codespace
        case 0:
        case 1:
          addrModeXindexedIndirect();
          break; //(zp,x)
        case 2:
        case 3:
          addrModeZeropage();
          break;
        case 4:
        case 5:
          addrModeImmediate();
          break;
        case 6:
        case 7:
          addrModeAbsolute();
          break;
        case 8:
        case 9:
          addrModeIndirectYindexed();
          break; //(zp),y (5..6 cycles, 8 for R-M-W)
        case 0xA:
          addrModeZeropageXindexed();
          break; //zp,x
        case 0xB:
          if ((IR & 0xC0) != 0x80) {
            addrModeZeropageXindexed(); //zp,x for illegal opcodes
          } else {
              addrModeZeropageYindexed(); //zp,y for LAX/SAX illegal opcodes
            }
          break;
        case 0xC:
        case 0xD:
          addrModeYindexed();
          break;
        case 0xE:
          addrModeXindexed();
          break;
        case 0xF:
          if ((IR & 0xC0) != 0x80) {
            addrModeXindexed(); //abs,x for illegal opcodes
          } else {
              addrModeYindexed(); //abs,y for LAX/SAX illegal opcodes
            }
          break;
      }
      addr &= 0xFFFF;
      switch ((IR & 0xE0) >> 5) { //value-forming to cause gapless case-values and faster jump-table creation from switch-case
        case 0:
          if ((IR & 0x1F) != 0xB) { //ORA / SLO(ASO)=ASL+ORA
            if ((IR & 3) == 3) {
              clrNZC();
              setC(rd(addr) >= N);
              wr(addr, (rd(addr) << 1)&0xFF);
              cycles += 2;
            } //for SLO
            else {
              cycles -= samePage;
            }
            A |= rd(addr);
            setNZbyA(); //ORA
          } else {
              A &= rd(addr);
              setNZbyA();
             setC(A >= N);
            } //ANC (AND+Carry=bit7)
          break;
        case 1:
          if ((IR & 0x1F) != 0xB) { //AND / RLA (ROL+AND)
            if ((IR & 3) == 3) { //for RLA
              T = (rd(addr) << 1) + (ST & C);
              clrNZC();
              setC(T > 255);
              T &= 0xFF;
              wr(addr, T);
              cycles += 2;
            } else {
                cycles -= samePage;
              }
            A &= rd(addr);
            setNZbyA(); //AND
          } else {
              A &= rd(addr);
              setNZbyA();
              setC(A >= N);
            } //ANC (AND+Carry=bit7)
          break;

        case 2:
          if ((IR & 0x1F) != 0xB) { //EOR / SRE(LSE)=LSR+EOR
            if ((IR & 3) == 3) {
              clrNZC();
              setC(rd(addr) & 1);
              wr(addr, (rd(addr) >> 1)&0xFF);
              cycles += 2;
            } //for SRE
            else {
              cycles -= samePage;
            }
            A ^= rd(addr);
            setNZbyA(); //EOR
          } else {
              A &= rd(addr);
              setC(A & 1);
              A >>= 1;
              A &= 0xFF;
              setNZbyA();
            } //ALR(ASR)=(AND+LSR)
          break;

        case 3:
          if ((IR & 0x1F) != 0xB) { //RRA (ROR+ADC) / ADC
            if ((IR & 3) == 3) { //for RRA
              T = (rd(addr) >> 1) + ((ST & C) << 7);
              T&=0xFF;
              clrNZC();
              setC(T & 1);
              wr(addr, T);
              cycles += 2;
            } else {
                cycles -= samePage;
              }
            T = A;
            A += rd(addr) + (ST & C);
            
            //BCD?
            if ((ST & D) != 0 && (A & 0xF) > 9) {
              A += 0x10;
              A &= 0xF0;
            } 
            setNZCbyAdd();
            setVbyAdd(rd(addr)); //ADC
          } else { // ARR (AND+ROR, bit0 not going to C, but C and bit7 get exchanged.)
              A &= rd(addr);
              T += rd(addr) + (ST & C);              
              clrNVZC();
              setC(T > 255);
              setVbyAdd(rd(addr)); //V-flag set by intermediate ADC mechanism: (A&mem)+mem
              T = A;
              A = (A >> 1) + ((ST & C) << 7);
              A&=0xFF;
              setC(T >= N);
              setNZbyA();
            }
          break;

        case 4:
          switch (IR & 0x1F) {
            //XAA (TXA+AND), highly unstable on real 6502!
            case 0xB:
              A = X & rd(addr);
              setNZbyA();
              break;
            //TAS(SHS) (SP=A&X, mem=S&H} - unstable on real 6502
            case 0x1B:
              SP = A & X;
              wr(addr, (SP & ((addr >> 8) + 1))&0xFF);
              break;
            //STA / SAX (at times same as AHX/SHX/SHY) (illegal)  
            default:
              wr2(addr, (A & (((IR & 3) == 3) ? X : 0xFF)));
              break; 
          }
          break;

        case 5:
          //LDA / LAX (illegal, used by my 1 rasterline player) (LAX #imm is unstable on c64)
          if ((IR & 0x1F) != 0x1B) {
            A = rd(addr);
            if ((IR & 3) == 3) {
              X = A;
            }
          } else { //LAS(LAR)
              A = X = SP = rd(addr) & SP;
            } 
          setNZbyA();
          cycles -= samePage;
          break;

        case 6:
          // CMP / DCP(DEC+CMP)
          if ((IR & 0x1F) != 0xB) { 
            //DCP
            if ((IR & 3) == 3) {
              wr(addr, (rd(addr) - 1)&0xFF);
              cycles += 2;
            } 
            else {
              cycles -= samePage;
            }
            T = A - rd(addr);
          } else { //SBX(AXS)  //SBX (AXS) (CMP+DEX at the same time)
            X = T = (A & X) - rd(addr);
          } 
          T=setNZCbySub(T);
          break;

        case 7:
          //ISC(ISB)=INC+SBC / SBC
          if ((IR & 3) == 3 && (IR & 0x1F) != 0xB) {
            wr(addr, (rd(addr) + 1)&0xFF);
            cycles += 2;
          } 
          else {
            cycles -= samePage;
          }
          T = A;
          A -= rd(addr) + (!((ST & C)!=0)?1:0);
          A=setNZCbySub(A);
          setVbyAdd((~rd(addr)));
          break;
      }
    } else if ((IR & 2) != 0) {  //nybble2:  2:illegal/LDX, 6:A/X/INC/DEC, A:Accu-shift/reg.transfer/NOP, E:shift/X/INC/DEC
      switch (IR & 0x1F) { //Addressing modes
        case 2:
          addrModeImmediate();
          break;
        case 6:
          addrModeZeropage();
          break;
        case 0xE:
          addrModeAbsolute();
          break;
        case 0x16:
          if ((IR & 0xC0) != 0x80) {
            addrModeZeropageXindexed(); //zp,x
          } else {
            addrModeZeropageYindexed(); //zp,y
          }
          break;
        case 0x1E:
          if ((IR & 0xC0) != 0x80) {
            addrModeXindexed(); //abs,x
          } else {
            addrModeYindexed(); //abs,y
          }
          break;
      }
      addr &= 0xFFFF;

      switch ((IR & 0xE0) >> 5) {
        case 0:
          clrC();
        case 1:
          if ((IR & 0xF) == 0xA) {
            A = (A << 1) + (ST & C);
            A&=0xFF;
            setNZCbyAdd();
          } //ASL/ROL (Accu)
          else {
            T = (rd(addr) << 1) + (ST & C);
            T&=0xFF;
            setC(T > 255);
            setNZbyT();
            wr(addr, T);
            cycles += 2;
          } //RMW (Read-Write-Modify)
          break;
        case 2:
          clrC();
        case 3:
          if ((IR & 0xF) == 0xA) {
            T = A;
            A = (A >> 1) + ((ST & C) << 7);
            setC(T & 1);
            A &= 0xFF;
            setNZbyA();
          } //LSR/ROR (Accu)
          else {
            T = (rd(addr) >> 1) + ((ST & C) << 7);
            T&=0xFF;
            setC(rd(addr) & 1);
            setNZbyT();
            wr(addr, T);
            cycles += 2;
          } //memory (RMW)
          break;

        case 4:
          if ((IR & 4) != 0) {
            wr2(addr, X);
          } //STX
          else if ((IR & 0x10) != 0) {
            SP = X; //TXS
          } else {
            A = X;
            setNZbyA();
          } //TXA
          break;

        case 5:
          if ((IR & 0xF) != 0xA) {
            X = rd(addr);
            cycles -= samePage;
          } //LDX
          else if ((IR & 0x10) != 0) {
            X = SP; //TSX
          } else {
            X = A; //TAX
          }
          setNZbyX();
          break;

        case 6:
          //DEC
          if ((IR & 4) != 0) {
            wr(addr, (rd(addr) - 1)&0xFF);
            setNZbyM();
            cycles += 2;
          } 
          else { //DEX
            --X;
            X&=0xFF;
            setNZbyX();
          } 
          break;

        case 7:
          if ((IR & 4) != 0) {
            wr(addr, (rd(addr) + 1)&0xFF);
            setNZbyM();
            cycles += 2;
          } //INC/NOP
          break;
      }
    } else if ((IR & 0xC) == 8) {  //nybble2:  8:register/statusflag
      if ((IR & 0x10) != 0) {
        if (IR == 0x98) {
          A = Y;
          setNZbyA();
        } //TYA
        else { //CLC/SEC/CLI/SEI/CLV/CLD/SED
          if ((FlagSwitches[IR >> 5] & 0x20) != 0) {
            ST |= (FlagSwitches[IR >> 5] & 0xDF);
          } else {
              ST &= ~(FlagSwitches[IR >> 5] & 0xDF);
            }
        }
      } else {
        switch ((IR & 0xF0) >> 5) {
          case 0:
            push(ST);
            cycles = 3;
            break; //PHP
          case 1:
            ST = pop();
            cycles = 4;
            break; //PLP
          case 2:
            push(A);
            cycles = 3;
            break; //PHA
          case 3:
            A = pop();
            setNZbyA();
            cycles = 4;
            break; //PLA
          case 4:
            --Y;
            Y&=0xFF;
            setNZbyY();
            break; //DEY
          case 5:
            Y = A;
            setNZbyY();
            break; //TAY
          case 6:
            ++Y;
            Y&=0xFF;
            setNZbyY();
            break; //INY
          case 7:
            ++X;
            X&=0xFF;
            setNZbyX();
            break; //INX
        }
      }
    } else {  //nybble2:  0: control/branch/Y/compare  4: Y/compare  C:Y/compare/JMP

      if ((IR & 0x1F) == 0x10) { //BPL/BMI/BVC/BVS/BCC/BCS/BNE/BEQ  relative branch
        ++PC;
        Memory.instance.setExecute(PC);
        T = rd(PC);
        if ((T & 0x80) != 0) {
          T -= 0x100;
        }
        if ((IR & 0x20) != 0) {
          if ((ST & BranchFlags[IR >> 6])!=0) {
            PC += T;
            cycles = 3;
          }
        } else {
          if (!((ST & BranchFlags[IR >> 6])!=0)) {
            PC += T;
            cycles = 3;
          } //plus 1 cycle if page is crossed?
        }
      } else {  //nybble2:  0:Y/control/Y/compare  4:Y/compare  C:Y/compare/JMP
        switch (IR & 0x1F) { //Addressing modes
          case 0:
            addrModeImmediate();
            break; //imm. (or abs.low for JSR/BRK)
          case 4:
            addrModeZeropage();
            break;
          case 0xC:
            addrModeAbsolute();
            break;
          case 0x14:
            addrModeZeropageXindexed();
            break; //zp,x
          case 0x1C:
            addrModeXindexed();
            break; //abs,x
        }
        addr &= 0xFFFF;

        switch ((IR & 0xE0) >> 5) {

          case 0:
            if (!((IR & 4) != 0)) { //BRK / NOP-absolute/abs,x/zp/zp,x
              push((PC + 2 - 1) >> 8);
              push((PC + 2 - 1) & 0xFF);
              push(ST | B);
              ST |= I; //BRK
              PC = rd(0xFFFE) + (rd(0xFFFF) << 8) - 1;
              cycles = 7;
            } else if (IR == 0x1C) {
              cycles -= samePage; //NOP abs,x
            }
            break;

          case 1:
            if ((IR & 0xF) != 0) { //BIT / NOP-abs,x/zp,x
              if (!((IR & 0x10) != 0)) {
                ST &= 0x3D;
                ST |= (rd(addr) & 0xC0) | ( ((! ((A & rd(addr))!=0 )) ? 1:0) << 1);
              } //BIT
              else if (IR == 0x3C) {
                cycles -= samePage; //NOP abs,x
              }
            } else { //JSR              
              push((PC + 2 - 1) >> 8);
              push((PC + 2 - 1) & 0xFF);
              PC = rd(addr) + rd(addr + 1) * 256 - 1;
              Memory.instance.setExecute(addr);
              Memory.instance.setExecute(addr+1);
              cycles = 6;
            }
            break;

          case 2:
            if ((IR & 0xF) != 0) { //JMP / NOP-abs,x/zp/zp,x
              if (IR == 0x4C) { //JMP
                PC = addr - 1;
                cycles = 3;
                rd(addr + 1); //a read from jump-address highbyte is used in some tunes with jmp DD0C (e.g. Wonderland_XIII_tune_1.sid)
                //if (addr==prevPC) {storeReg(); c64->returned=1; return 0xFF;} //turn self-jump mainloop (after initC64) into idle time
              } else if (IR == 0x5C) {
                  cycles -= samePage; //NOP abs,x
                }
            } else { //RTI
                ST = pop();
                T = pop();
                PC = (pop() << 8) + T - 1;
                cycles = 6;
                if (c64.returned != 0 && SP >= 0xFF) {
                  ++PC;
                  ///storeReg();
                  return (byte)0xFE;
                }
              }
            break;

          case 3:
            if ((IR & 0xF) != 0) { //JMP() (indirect) / NOP-abs,x/zp/zp,x
              if (IR == 0x6C) { //JMP() (indirect)
                PC = rd((addr & 0xFF00) + ((addr + 1) & 0xFF)); //(with highbyte-wraparound bug)
                PC = (PC << 8) + rd(addr) - 1;
                cycles = 5;
              } else if (IR == 0x7C) {
                cycles -= samePage; //NOP abs,x
              }
            } else { //RTS
              if (SP >= 0xFF) {
                ///storeReg();
                c64.returned = 1;
                return (byte)0xFF;
              } //Init returns, provide idle-time between IRQs
              T = pop();
              PC = (pop() << 8) + T;
              cycles = 6;
            }
            break;

          case 4:
            if ((IR & 4) != 0) {
              wr2(addr, Y);
            } //STY / NOP #imm
            break;

          case 5:
            Y = rd(addr);
            setNZbyY();
            cycles -= samePage; //LDY
            break;

          case 6:
            if (!((IR & 0x10) != 0)) { //CPY / NOP abs,x/zp,x
              T = Y - rd(addr);
              T=setNZCbySub(T); //CPY
            } else if (IR == 0xDC) {
              cycles -= samePage; //NOP abs,x
            }
            break;

          case 7:
            if (!((IR & 0x10) != 0)) { //CPX / NOP abs,x/zp,x
              T = X - rd(addr);
              T=setNZCbySub(T); //CPX
            } else if (IR == 0xFC) {
              cycles -= samePage; //NOP abs,x
            }
            break;
        }
      }
    }

    ++PC; //PC&=0xFFFF;

    ///storeReg();
    if (!c64.realSIDmode) { //substitute KERNAL irq-return in PSID (e.g. Microprose Soccer)
      if ((c64.ramBank[1] & 3) > 1 && prevPC < 0xE000 && (PC == 0xEA31 || PC == 0xEA81 || PC == 0xEA7E)) {
        return (byte)0xFE;
      }
    }

    return cycles;
  }
    

  /**
   * handle entering into irq and nmi interrupt
   * 
   * @return if irq happened
   */
  public boolean handleCPUinterrupts () {
    if (c64.nmi > prevNMI) { //if irq and nmi at the same time, nmi is serviced first
      push(PC>>8); 
      push(PC&0xFF); 
      push(ST); 
      ST |= I;
      PC = c64.readMemC64(0xFFFA) + (c64.readMemC64(0xFFFB)<<8); //NMI-vector
      prevNMI = c64.nmi;
      return true;
    }
    else if ( (c64.irq!=0) && !((ST&I)!=0) ) {
           push(PC>>8); 
           push(PC&0xFF); 
           push(ST); 
           ST |= I;
           PC = c64.readMemC64(0xFFFE) + (c64.readMemC64(0xFFFF)<<8); //maskable irq-vector
           prevNMI = c64.nmi;           
           return true;
    }
    prevNMI = c64.nmi; //prepare for nmi edge-detection

    return false;
  }
}