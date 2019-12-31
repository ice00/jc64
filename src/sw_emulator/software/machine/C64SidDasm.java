/**
 * @(#)C64SidDasm.java 2001/05/05
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

/**
 * Comment the memory locations of C64 for the disassembler, but give the comment
 * for SID extended register, like in PSID file format
 * It performs also a multy language comments.
 *
 * @author Ice
 * @version 1.00 05/05/2001
 */
public class C64SidDasm extends C64Dasm {
  /**
   * Return a comment string for the passed instruction.
   * Here we comment only the SID extended register
   *
   * @param iType the type of instruction
   * @param aType the type of addressing used by the instruction
   * @param addr the address value (if needed by this istruction type)
   * @param value the operation value (if needed by this instruction)
   * @return a comment string
   */
  @Override
  public String dcom(int iType, int aType, long addr, long value) {
    switch (aType) {
      case A_ZPG:
      case A_ZPX:
      case A_ZPY:
      case A_ABS:
      case A_ABX:
      case A_ABY:
      case A_REL:
      case A_IND:
      case A_IDX:
      case A_IDY:
        switch (language) {
          case LANG_ITALIAN:
            switch ((int)addr) {
              case 0xD41D: return "START/STOP campionamento - toni Galway-Noise";
              case 0xD41E: return "Indirizzo basso ToneData (Galway-Noise) o SampleData";
              case 0xD41F: return "Indirizzo alto ToneData (Galway-Noise) o SampleData";

              case 0xD43D: return "Tonelength (Galway-Noise) o fine ind. basso SampleData";
              case 0xD43E: return "Volume del Noise (Galway-Noise) o fine ind. alto SampleData";
              case 0xD43F: return "Periodo di ogni ToneData o numero campioni da ripetere";

              case 0xD45D: return "Periodo per il valore 0 di ToneData o per byte bassi campioni";
              case 0xD45E: return "Periodo per byte alti campioni";
              case 0xD45F: return "Numero di byte da aggiungere dopo aver letto un nibble";

              case 0xD47D: return "Ordine dei campioni";
              case 0xD47E: return "Indirizzo basso campioni da ripetere";
              case 0xD47F: return "Indirizzo alto campioni da ripetere";
            }
          default:
            switch ((int)addr) {
              case 0xD41D: return "Sample START/STOP - Galway-Noise tones";
              case 0xD41E: return "ToneData address lowbyte (Galway-Noise) or SampleData";
              case 0xD41F: return "ToneData address highbyte (Galway-Noise) or SampleData";

              case 0xD43D: return "Tonelength (Galway-Noise) or SampleData end addr. low";
              case 0xD43E: return "Volume of Noise (Galway-Noise) or SampleData end addr. high";
              case 0xD43F: return "Period for each ToneData or Nr times of Repeat Cont. sample";

              case 0xD45D: return "Period for value 0 of ToneData or Period for samples lowbyte";
              case 0xD45E: return "Period for samples highbyte";
              case 0xD45F: return "Nr of bytes to add after Reading one nibble";

              case 0xD47D: return "Sampleorder";
              case 0xD47E: return "SampleData repeataddress low";
              case 0xD47F: return "SampleData repeataddress high";
            }
        }
      break;
    }
    return super.dcom(iType, aType, addr, value);
  }
}
