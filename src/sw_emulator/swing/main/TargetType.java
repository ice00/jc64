/**
 * @(#)FileTypejava 2020/10/10
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
package sw_emulator.swing.main;

import sw_emulator.software.cpu.CpuDasm;
import sw_emulator.software.cpu.M6510Dasm;
import sw_emulator.software.cpu.Z80Dasm;
import sw_emulator.software.machine.AtariDasm;
import sw_emulator.software.machine.C128Dasm;
import sw_emulator.software.machine.C1541Dasm;
import sw_emulator.software.machine.C64Dasm;
import sw_emulator.software.machine.CPlus4Dasm;
import sw_emulator.software.machine.CVic20Dasm;
import sw_emulator.software.machine.OdysseyDasm;
import static sw_emulator.swing.main.CpuFamily.I8048;
import static sw_emulator.swing.main.CpuFamily.M6502;
import static sw_emulator.swing.main.CpuFamily.Z80;

/**
 * Target machine type for disassembly
 * 
 * @author ice
 */
 public enum TargetType {
   C64(M6502),
   C1541(M6502),
   C128(M6502),
   VIC20(M6502),
   PLUS4(M6502),
   C128Z(Z80),
   ATARI(M6502),
   ODYSSEY(I8048);         
   
   /** Cpu fmaily of this target */
   public CpuFamily cpuFamily;

    /**
     * Construct the cpu family     
     */
    private TargetType(CpuFamily cpuFamily) {
        this.cpuFamily = cpuFamily;
    }
   
   
   
   /**
    * Get the appropriate disassembler for the target type
    * 
    * @return the disassembler to use
    */
   public CpuDasm getDasm() {
     switch (this) {
      case C64:
        return new C64Dasm();  
      case C1541:
        return new C1541Dasm();     
      case C128:
        return new C128Dasm();  
      case VIC20:
        return new CVic20Dasm(); 
      case PLUS4:
        return new CPlus4Dasm();  
      case C128Z:
        return new Z80Dasm();
      case ATARI:
        return new AtariDasm();  
      case ODYSSEY:
        return new OdysseyDasm();
      default:  
        return new M6510Dasm();
     }    
   }
}    

