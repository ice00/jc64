/**
 * @(#)OdysseyDasm.java 2024/04/17
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
package sw_emulator.software.machine;

import sw_emulator.software.cpu.I8048Dasm;
import static sw_emulator.software.cpu.I8048Dasm.A_CADR;
import static sw_emulator.software.cpu.I8048Dasm.A_REGA;
import static sw_emulator.software.cpu.I8048Dasm.A_REL;

/**
 * Comment the memory location of Odyssey/Videopack for the disassembler
 * It performs also a multy language comments.
 * 
 * @author ice
 */
public class OdysseyDasm extends I8048Dasm {
  // Available language
  public static final byte LANG_ENGLISH=1;
  public static final byte LANG_ITALIAN=2;
  
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
    byte language=option.commentLanguage;  
    switch (aType) {
      case A_CADR:
      case A_REL:
      case A_REGA:     
        // do not get comment if appropriate option is not selected  
        if ((int)addr<=0x40A && !option.commentOdysseyBiosRam) return "";
        
        switch (language) {
          case LANG_ITALIAN:
            switch ((int)addr) {
              case 0x00: return "Avvio a freddo";
              case 0x03: return "Interruzione esterna T0";
              case 0x07: return "Interruzione Timer/Orologio";
              case 0x09: return "Routine di interruzione VBlank 1";
              case 0x1A: return "Routine di interruzione VBlank 2 (Collisione & Orologio)";
              case 0x44: return "Routine di interruzione VBlank 3 (Melodia)";
              case 0x89: return "Codice di controllo per la copia da RAM a VDC durante VBlank";
              case 0xA3: return "Codice di copia";
              case 0xB0: return "Routine della tastiera";
              case 0xE7: return "Imposta l'accesso a VDC";
              case 0xEC: return "Imposta l'accesso alla RAM";
              case 0xF1: return "Reset";
              case 0x11C: return "Spegni il display";
              case 0x127: return "Accendi il display";
              case 0x132: return "Abilita la copia dei dati al prossimo VSYNC";
              case 0x13D: return "Ottieni un tasto premuto";
              case 0x14B: return "Traduzione carattere/colore";
              case 0x16B: return "Cancella tutti i caratteri";
              case 0x176: return "Attendi un'interruzione";
              case 0x17C: return "Visualizza caratteri BCD a 2 cifre";
              case 0x1A2: return "Avvia la melodia";
              case 0x1B0: return "Contatore su/giù";
              case 0x23A: return "Imposta i caratteri del punteggio quadruplo";
              case 0x261: return "Traduci e copia carattere e colore";
              case 0x26A: return "Test del bit";
              case 0x280: return "Cancella il bit";
              case 0x28A: return "Imposta il bit";
              case 0x293: return "[Sconosciuto]";
              case 0x2C3: return "Seleziona il gioco";
              case 0x300: return "Dati di frequenza";
              case 0x34A: return "Dati della melodia";
              case 0x376: return "Fine della routine di ingresso della tastiera";
              case 0x37E: return "Gestori di interruzioni varie per ROM nei bachi";
              case 0x38F: return "Leggi il joystick";
              case 0x3B1: return "[Sconosciuto]";
              case 0x3CF: return "[Sconosciuto]";
              case 0x3EA: return "Scrittura del carattere";
              
              case 0x400: return "Riavvio";
              case 0x402: return "Interruzione VBlank (Esterna)";
              case 0x404: return "Interruzione Timer/Orologio";
              case 0x406: return "Vettore Routine VBlank (se $402 = JMP $009)";
              case 0x408: return "Fine della Selezione del Gioco, A = Selezione";
              case 0x40A: return "Continuazione di VBlank (se $406 = JMP $01A)";
            }
          default:
            switch ((int)addr) {    
              case 0x00: return "Cold Boot";
              case 0x03: return "External T0 Interrupt";
              case 0x07: return "Timer/Clock Interrupt";
              case 0x09: return "VBlank Interrupt Routine 1";
              case 0x1A: return "VBlank Interrupt Routine 2 (Collision & Clock)";
              case 0x44: return "VBlank Interrupt Routine 3 (Tune)";
              case 0x89: return "RAM to VDC VBlank Copying Check Code";
              case 0xA3: return "Copying Code";
              case 0xB0: return "Keyboard Routine";
              case 0xE7: return "Set up VDC Access";
              case 0xEC: return "Set up RAM Access";
              case 0xF1: return "Reset";
              case 0x11C: return "Display Off";
              case 0x127: return "Display On";
              case 0x132: return "Enable Data Copy next VSYNC";
              case 0x13D: return "Get Keystroke";
              case 0x14B: return "Character/Colour Translation";
              case 0x16B: return "Clear all Characters";
              case 0x176: return "Wait for Interrupt";
              case 0x17C: return "Display 2 digit BCD Characters";
              case 0x1A2: return "Start Tune";
              case 0x1B0: return "Up/Down Counter";
              case 0x23A: return "Set up Quad Score Characters";
              case 0x261: return "Translate & Copy Character & Colour";
              case 0x26A: return "Bit test";
              case 0x280: return "Bit clear";
              case 0x28A: return "Bit set";
              case 0x293: return "[Unknown]";
              case 0x2C3: return "Select Game";
              case 0x300: return "Frequency Data";
              case 0x34A: return "Tune Data";
              case 0x376: return "Keyboard In routine (end)";
              case 0x37E: return "Miscellaneous Interrupt Handlers for Banked Roms";
              case 0x38F: return "Read Joystick";
              case 0x3B1: return "[Unknown]";
              case 0x3CF: return "[Unknown]";
              case 0x3EA: return "Character Write";
              
              case 0x400: return "Restart";
              case 0x402: return "VBlank (External) Interrupt";
              case 0x404: return "Timer/Clock Interrupt";
              case 0x406: return "VBlank Routine Vector (if $402 = JMP $009)";
              case 0x408: return "End of Select Game, A = Selection";
              case 0x40A: return "Continuation of VBlank (if $406 = JMP $01A)";
              }    
        }
    }
    return super.dcom(iType, aType, addr, value);
  }    
}
