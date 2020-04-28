/**
 * @(#)C64MusDasm.java 2001/07/14
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

import sw_emulator.software.cpu.M6510Dasm;
import sw_emulator.math.Unsigned;
import java.util.Locale;

/**
 * Decode the voice music data as coded into 'mus' file used by sidplayer.
 * The generated code give so information about how a music is done.
 * It performs also a multy language comments.
 *
 * @author Ice
 * @version 1.00 14/07/2001
 */
public class C64MusDasm extends M6510Dasm {
  /**
   * Return the mnemonic assembler instruction rapresent by passed code bytes.
   * Here we give not assembler instruction, but music notation like in mus
   * file.
   *
   * @param buffer the buffer containg the data
   * @param pos the actual position in the buffer
   * @param pc the program counter value associated to the bytes being address
   *           by the <code>pos</code> in the buffer
   * @return a string menemonic rapresentation of instruction
   */
  @Override
  public String dasm(byte[] buffer, int pos, long pc) {
    String result="???";                  // result string
    int b1=Unsigned.done(buffer[pos]);    // first command byte
    int b2=Unsigned.done(buffer[pos+1]);  // second command byte

    // is this a music data command?
    if ((b1 & 0x03)==00) {             // yes this is music notation
      // analize duration byte:

      // look for TIE
      String tied="";
      if ((b1 & 0x40)==0x40) {
        tied="TIE";
      }

      // look for LENGTH
      String length="";
      switch (b1 & 0x1C) {
        case 0x08: length="WHOLE"; break;
        case 0x0C: length="HALF"; break;
        case 0x10: length="QUARTER"; break;
        case 0x14: length="EIGHTH"; break;
        case 0x18: length="16TH"; break;
        case 0x1C: length="32ND"; break;
        default:
          if ((b1 & 0x3C)==20) {
            length="64TH";
          } else {
              length="???";                 // unknown length
            }
      }

      // look for DOT
      String dot="";
      switch (b1 & 0xA0) {
        case 0xA0: dot="DOUBLE DOTTED"; break;
        case 0x20: dot="DOTTED"; break;
        case 0x00: dot="" /**"NONE"*/; break;
        default:
          dot="???";                        // unknown dot
      }

      result=length+" "+dot+" "+tied;       // compose the duration string

      // look for OTHER
      if (b1==0) {
        result="ABSOLUTE SET";
      } else {
          switch (b1 & 0xBF) {
            case 0x24:
              result="UTILITY-VOICE "+tied;
              break;
            case 0x04:
              result="UTILITY "+tied;
              break;
          }
        }

      // now look for note

      //look for octave
      char octave=' ';
      switch (b2 & 0x38) {
        case 0x38: octave='0'; break;
        case 0x30: octave='1'; break;
        case 0x28: octave='2'; break;
        case 0x20: octave='3'; break;
        case 0x18: octave='4'; break;
        case 0x10: octave='5'; break;
        case 0x08: octave='6'; break;
        case 0x00: octave='7'; break;
      }

      //look for note
      char note=' ';
      String type="";
      switch (b2 & 0x07) {
        case 0x01: note='C'; type="##"; break;
        case 0x02: note='D'; type="##"; break;
        case 0x03: note='E'; type="bb"; break;
        case 0x04: note='F'; type="##"; break;
        case 0x05: note='G'; type="##"; break;
        case 0x06: note='A'; type="bb"; break;
        case 0x07: note='B'; type="bb"; break;
      }

      // look for type
      switch (b2 & 0xC0) {
        case 0x80: type=""; break;
        case 0x40: type="#"; break;
        case 0xC0: type="b"; break;
        // default use double that is already setted
      }

      if ((b2 & 0x07)==0x00) {
        result="REST"+" "+result;
      } else {
          result=note+type+"-"+octave+"  "+result;
        }
    } else {
        switch (b1) {
          case 0x06:                        // look for tempo
            switch (b2) { 
              case 0x00: result="TEMPO: SET 56"; break;
              case 0xF8: result="TEMPO: SET 58"; break;
              case 0xF0: result="TEMPO: SET 60"; break;
              case 0xE8: result="TEMPO: SET 62"; break;
              case 0xE0: result="TEMPO: SET 64"; break;
              case 0xD8: result="TEMPO: SET 66"; break;
              case 0xD0: result="TEMPO: SET 69"; break;
              case 0xC8: result="TEMPO: SET 72"; break;
              case 0xC0: result="TEMPO: SET 75"; break;
              case 0xB8: result="TEMPO: SET 78"; break;
              case 0xB0: result="TEMPO: SET 81"; break;
              case 0xA8: result="TEMPO: SET 85"; break;
              case 0xA0: result="TEMPO: SET 90"; break;
              case 0x98: result="TEMPO: SET 94"; break;
              case 0x90: result="TEMPO: SET 100"; break;
              case 0x88: result="TEMPO: SET 105"; break;
              case 0x80: result="TEMPO: SET 112"; break;
              case 0x78: result="TEMPO: SET 120"; break;
              case 0x70: result="TEMPO: SET 128"; break;
              case 0x68: result="TEMPO: SET 138"; break;
              case 0x60: result="TEMPO: SET 150"; break;
              case 0x58: result="TEMPO: SET 163"; break;
              case 0x50: result="TEMPO: SET 180"; break;
              case 0x48: result="TEMPO: SET 200"; break;
              case 0x40: result="TEMPO: SET 225"; break;
              case 0x38: result="TEMPO: SET 257"; break;
              case 0x30: result="TEMPO: SET 300"; break;
              case 0x28: result="TEMPO: SET 360"; break;
              case 0x20: result="TEMPO: SET 450"; break;
              case 0x18: result="TEMPO: SET 600"; break;
              case 0x10: result="TEMPO: SET 900"; break;
              case 0x08: result="TEMPO: SET 1800"; break;
            }
            break;

          case 0x16:                        // look for utility tempo
            result="TEMPO: UTILITY SET "+b2;
            break;

          case 0x01:                        // look for volume
            switch (b2) {
              case 0x0E: result="VOLUME: SET 0"; break;
              case 0x1E: result="VOLUME: SET 1"; break;
              case 0x2E: result="VOLUME: SET 2"; break;
              case 0x3E: result="VOLUME: SET 3"; break;
              case 0x4E: result="VOLUME: SET 4"; break;
              case 0x5E: result="VOLUME: SET 5"; break;
              case 0x6E: result="VOLUME: SET 6"; break;
              case 0x7E: result="VOLUME: SET 7"; break;
              case 0x8E: result="VOLUME: SET 8"; break;
              case 0x9E: result="VOLUME: SET 9"; break;
              case 0xAE: result="VOLUME: SET 10"; break;
              case 0xBE: result="VOLUME: SET 11"; break;
              case 0xCE: result="VOLUME: SET 12"; break;
              case 0xDE: result="VOLUME: SET 13"; break;
              case 0xEE: result="VOLUME: SET 14"; break;
              case 0xFE: result="VOLUME: SET 15"; break;

              case 0x03: result="VOLUME: BUMP 0=UP"; break;
              case 0x0B: result="VOLUME: BUMP 1=DOWN"; break;

              case 0x0F: result="REPEAT: TAIL 1"; break;

              case 0x02: result="PHRASE: CALL 0"; break;
              case 0x12: result="PHRASE: CALL 1"; break;
              case 0x22: result="PHRASE: CALL 2"; break;
              case 0x32: result="PHRASE: CALL 3"; break;
              case 0x42: result="PHRASE: CALL 4"; break;
              case 0x52: result="PHRASE: CALL 5"; break;
              case 0x62: result="PHRASE: CALL 6"; break;
              case 0x72: result="PHRASE: CALL 7"; break;
              case 0x82: result="PHRASE: CALL 8"; break;
              case 0x92: result="PHRASE: CALL 9"; break;
              case 0xA2: result="PHRASE: CALL 10"; break;
              case 0xB2: result="PHRASE: CALL 11"; break;
              case 0xC2: result="PHRASE: CALL 12"; break;
              case 0xD2: result="PHRASE: CALL 13"; break;
              case 0xE2: result="PHRASE: CALL 14"; break;
              case 0xF2: result="PHRASE: CALL 15"; break;

              case 0x8B: result="PHRASE: CALL 16"; break;
              case 0x9B: result="PHRASE: CALL 17"; break;
              case 0xAB: result="PHRASE: CALL 18"; break;
              case 0xBB: result="PHRASE: CALL 19"; break;
              case 0xCB: result="PHRASE: CALL 20"; break;
              case 0xDB: result="PHRASE: CALL 21"; break;
              case 0xEB: result="PHRASE: CALL 22"; break;
              case 0xFB: result="PHRASE: CALL 23"; break; 

              case 0x06: result="PHRASE: DEFINE 0"; break;
              case 0x16: result="PHRASE: DEFINE 1"; break;
              case 0x26: result="PHRASE: DEFINE 2"; break;
              case 0x36: result="PHRASE: DEFINE 3"; break;
              case 0x46: result="PHRASE: DEFINE 4"; break;
              case 0x56: result="PHRASE: DEFINE 5"; break;
              case 0x66: result="PHRASE: DEFINE 6"; break;
              case 0x76: result="PHRASE: DEFINE 7"; break;
              case 0x86: result="PHRASE: DEFINE 8"; break;
              case 0x96: result="PHRASE: DEFINE 9"; break;
              case 0xA6: result="PHRASE: DEFINE 10"; break;
              case 0xB6: result="PHRASE: DEFINE 11"; break;
              case 0xC6: result="PHRASE: DEFINE 12"; break;
              case 0xD6: result="PHRASE: DEFINE 13"; break;
              case 0xE6: result="PHRASE: DEFINE 14"; break;
              case 0xF6: result="PHRASE: DEFINE 15"; break;

              case 0x83: result="PHRASE: DEFINE 16"; break;
              case 0x93: result="PHRASE: DEFINE 17"; break;
              case 0xA3: result="PHRASE: DEFINE 18"; break;
              case 0xB3: result="PHRASE: DEFINE 19"; break;
              case 0xC3: result="PHRASE: DEFINE 20"; break;
              case 0xD3: result="PHRASE: DEFINE 21"; break;
              case 0xE3: result="PHRASE: DEFINE 22"; break;
              case 0xF3: result="PHRASE: DEFINE 23"; break;

              case 0x2F: result="PHRASE: END 1"; break;

              case 0x04: result="ENVELOPE: ATTACK 0"; break;
              case 0x0C: result="ENVELOPE: ATTACK 1"; break;
              case 0x14: result="ENVELOPE: ATTACK 2"; break;
              case 0x1C: result="ENVELOPE: ATTACK 3"; break;
              case 0x24: result="ENVELOPE: ATTACK 4"; break;
              case 0x2C: result="ENVELOPE: ATTACK 5"; break;
              case 0x34: result="ENVELOPE: ATTACK 6"; break;
              case 0x3C: result="ENVELOPE: ATTACK 7"; break;
              case 0x44: result="ENVELOPE: ATTACK 8"; break;
              case 0x4C: result="ENVELOPE: ATTACK 9"; break;
              case 0x54: result="ENVELOPE: ATTACK 10"; break;
              case 0x5C: result="ENVELOPE: ATTACK 11"; break;
              case 0x64: result="ENVELOPE: ATTACK 12"; break;
              case 0x6C: result="ENVELOPE: ATTACK 13"; break;
              case 0x74: result="ENVELOPE: ATTACK 14"; break;
              case 0x7C: result="ENVELOPE: ATTACK 15" ; break;

              case 0x00: result="ENVELOPE: DECAY 0"; break;
              case 0x10: result="ENVELOPE: DECAY 1"; break;
              case 0x20: result="ENVELOPE: DECAY 2"; break;
              case 0x30: result="ENVELOPE: DECAY 3"; break;
              case 0x40: result="ENVELOPE: DECAY 4"; break;
              case 0x50: result="ENVELOPE: DECAY 5"; break;
              case 0x60: result="ENVELOPE: DECAY 6"; break;
              case 0x70: result="ENVELOPE: DECAY 7"; break;
              case 0x80: result="ENVELOPE: DECAY 8"; break;
              case 0x90: result="ENVELOPE: DECAY 9"; break;
              case 0xA0: result="ENVELOPE: DECAY 10"; break;
              case 0xB0: result="ENVELOPE: DECAY 11"; break;
              case 0xC0: result="ENVELOPE: DECAY 12"; break;
              case 0xD0: result="ENVELOPE: DECAY 13"; break;
              case 0xE0: result="ENVELOPE: DECAY 14"; break;
              case 0xF0: result="ENVELOPE: DECAY 15" ; break; 

              case 0x84: result="ENVELOPE: SUSTAIN 0"; break;
              case 0x8C: result="ENVELOPE: SUSTAIN 1"; break;
              case 0x94: result="ENVELOPE: SUSTAIN 2"; break;
              case 0x9C: result="ENVELOPE: SUSTAIN 3"; break;
              case 0xA4: result="ENVELOPE: SUSTAIN 4"; break;
              case 0xAC: result="ENVELOPE: SUSTAIN 5"; break;
              case 0xB4: result="ENVELOPE: SUSTAIN 6"; break;
              case 0xBC: result="ENVELOPE: SUSTAIN 7"; break;
              case 0xC4: result="ENVELOPE: SUSTAIN 8"; break;
              case 0xCC: result="ENVELOPE: SUSTAIN 9"; break;
              case 0xD4: result="ENVELOPE: SUSTAIN 10"; break;
              case 0xDC: result="ENVELOPE: SUSTAIN 11"; break;
              case 0xE4: result="ENVELOPE: SUSTAIN 12"; break;
              case 0xEC: result="ENVELOPE: SUSTAIN 13"; break;
              case 0xF4: result="ENVELOPE: SUSTAIN 14"; break;
              case 0xFC: result="ENVELOPE: SUSTAIN 15" ; break;

              case 0x08: result="ENVELOPE: RELEASE 0"; break;
              case 0x18: result="ENVELOPE: RELEASE 1"; break;
              case 0x28: result="ENVELOPE: RELEASE 2"; break;
              case 0x38: result="ENVELOPE: RELEASE 3"; break;
              case 0x48: result="ENVELOPE: RELEASE 4"; break;
              case 0x58: result="ENVELOPE: RELEASE 5"; break;
              case 0x68: result="ENVELOPE: RELEASE 6"; break;
              case 0x78: result="ENVELOPE: RELEASE 7"; break;
              case 0x88: result="ENVELOPE: RELEASE 8"; break;
              case 0x98: result="ENVELOPE: RELEASE 9"; break;
              case 0xA8: result="ENVELOPE: RELEASE 10"; break;
              case 0xB8: result="ENVELOPE: RELEASE 11"; break;
              case 0xC8: result="ENVELOPE: RELEASE 12"; break;
              case 0xD8: result="ENVELOPE: RELEASE 13"; break;
              case 0xE8: result="ENVELOPE: RELEASE 14"; break;
              case 0xF8: result="ENVELOPE: RELEASE 15" ; break;

              case 0x07: result="WAVEFORM: SET 0=N  (NOISE)"; break;
              case 0x27: result="WAVEFORM: SET 1=T  (TRIANGE)"; break;
              case 0x47: result="WAVEFORM: SET 2=S  (SAWTOOTH)"; break;
              case 0x67: result="WAVEFORM: SET 3=TS (TRIANGE+SAWTOOTH)"; break;
              case 0x87: result="WAVEFORM: SET 4=P  (PULSE)"; break;
              case 0xA7: result="WAVEFORM: SET 5=TP (TRIANGE+PULSE)"; break;
              case 0xC7: result="WAVEFORM: SET 6=SP (SAWTOOTH+PULSE)"; break;
              case 0xE7: result="WAVEFORM: SET 7=TSP (PULSE+TRIANGLE+SAWTOOTH)"; break;

              case 0x33: result="WAVEFORM: SYNC 0=NO"; break;
              case 0x3B: result="WAVEFORM: SYNC 1=YES"; break;
              case 0x23: result="WAVEFORM: RING MODE 0=NO"; break;
              case 0x2B: result="WAVEFORM: RING MODE 1=YES"; break;

              case 0x73: result="FREQ: POR & VIBRATO 0=NO"; break;
              case 0x7B: result="FREQ: POR & VIBRATO 1=YES"; break; 

              case 0x17: result="FILTER: MODE 0=N  (NORMAL)"; break;
              case 0x37: result="FILTER: MODE 1=L  (LOW PASS)"; break;
              case 0x57: result="FILTER: MODE 2=B  (BAND PASS)"; break;
              case 0x77: result="FILTER: MODE 3=LB"; break;
              case 0x97: result="FILTER: MODE 4=H  (HIGH PASS)"; break;
              case 0xB7: result="FILTER: MODE 5=LH"; break;
              case 0xD7: result="FILTER: MODE 6=BH"; break;
              case 0xF7: result="FILTER: MODE 7=LBH"; break;

              case 0x0A: result="FILTER: RESONANCE 0"; break;
              case 0x1A: result="FILTER: RESONANCE 1"; break;
              case 0x2A: result="FILTER: RESONANCE 2"; break;
              case 0x3A: result="FILTER: RESONANCE 3"; break;
              case 0x4A: result="FILTER: RESONANCE 4"; break;
              case 0x5A: result="FILTER: RESONANCE 5"; break;
              case 0x6A: result="FILTER: RESONANCE 6"; break;
              case 0x7A: result="FILTER: RESONANCE 7"; break;
              case 0x8A: result="FILTER: RESONANCE 8"; break;
              case 0x9A: result="FILTER: RESONANCE 9"; break;
              case 0xAA: result="FILTER: RESONANCE 10"; break;
              case 0xBA: result="FILTER: RESONANCE 11"; break;
              case 0xCA: result="FILTER: RESONANCE 12"; break;
              case 0xDA: result="FILTER: RESONANCE 13"; break;
              case 0xEA: result="FILTER: RESONANCE 14"; break;
              case 0xFA: result="FILTER: RESONANCE 15"; break;

              case 0x13: result="FILTER: THROUGH 0=NO"; break;
              case 0x1B: result="FILTER: THROUGH 0=YES"; break;

              case 0x43: result="FILTER: EXTERNAL 0=NO"; break;
              case 0x4B: result="FILTER: EXTERNAL 0=YES"; break;

              case 0x63: result="MODULATION: SOFTWARE LFO 0"; break;
              case 0x6B: result="MODULATION: SOFTWARE LFO 1"; break;

              case 0x01: result="MODULATION: LFO RATE UP 0"; break;
              case 0x09: result="MODULATION: LFO RATE UP 1"; break;
              case 0x11: result="MODULATION: LFO RATE UP 2"; break;
              case 0x19: result="MODULATION: LFO RATE UP 3"; break;
              case 0x21: result="MODULATION: LFO RATE UP 4"; break;
              case 0x29: result="MODULATION: LFO RATE UP 5"; break;
              case 0x31: result="MODULATION: LFO RATE UP 6"; break;
              case 0x39: result="MODULATION: LFO RATE UP 7"; break;
              case 0x41: result="MODULATION: LFO RATE UP 8"; break;
              case 0x49: result="MODULATION: LFO RATE UP 9"; break;
              case 0x51: result="MODULATION: LFO RATE UP 10"; break;
              case 0x59: result="MODULATION: LFO RATE UP 11"; break;
              case 0x61: result="MODULATION: LFO RATE UP 12"; break;
              case 0x69: result="MODULATION: LFO RATE UP 13"; break;
              case 0x71: result="MODULATION: LFO RATE UP 14"; break;
              case 0x79: result="MODULATION: LFO RATE UP 15"; break;
              case 0x81: result="MODULATION: LFO RATE UP 16"; break;
              case 0x89: result="MODULATION: LFO RATE UP 17"; break;
              case 0x91: result="MODULATION: LFO RATE UP 18"; break;
              case 0x99: result="MODULATION: LFO RATE UP 19"; break;
              case 0xA1: result="MODULATION: LFO RATE UP 20"; break;
              case 0xA9: result="MODULATION: LFO RATE UP 21"; break;
              case 0xB1: result="MODULATION: LFO RATE UP 22"; break;
              case 0xB9: result="MODULATION: LFO RATE UP 23"; break;
              case 0xC1: result="MODULATION: LFO RATE UP 24"; break;
              case 0xC9: result="MODULATION: LFO RATE UP 25"; break;
              case 0xD1: result="MODULATION: LFO RATE UP 26"; break;
              case 0xD9: result="MODULATION: LFO RATE UP 27"; break;
              case 0xE1: result="MODULATION: LFO RATE UP 28"; break;
              case 0xE9: result="MODULATION: LFO RATE UP 29"; break;
              case 0xF1: result="MODULATION: LFO RATE UP 30"; break;
              case 0xF9: result="MODULATION: LFO RATE UP 31"; break;

              case 0x05: result="MODULATION: LFO RATE DOWN 0"; break;
              case 0x0D: result="MODULATION: LFO RATE DOWN 1"; break;
              case 0x15: result="MODULATION: LFO RATE DOWN 2"; break;
              case 0x1D: result="MODULATION: LFO RATE DOWN 3"; break;
              case 0x25: result="MODULATION: LFO RATE DOWN 4"; break;
              case 0x2D: result="MODULATION: LFO RATE DOWN 5"; break;
              case 0x35: result="MODULATION: LFO RATE DOWN 6"; break;
              case 0x3D: result="MODULATION: LFO RATE DOWN 7"; break;
              case 0x45: result="MODULATION: LFO RATE DOWN 8"; break;
              case 0x4D: result="MODULATION: LFO RATE DOWN 9"; break;
              case 0x55: result="MODULATION: LFO RATE DOWN 10"; break;
              case 0x5D: result="MODULATION: LFO RATE DOWN 11"; break;
              case 0x65: result="MODULATION: LFO RATE DOWN 12"; break;
              case 0x6D: result="MODULATION: LFO RATE DOWN 13"; break;
              case 0x75: result="MODULATION: LFO RATE DOWN 14"; break;
              case 0x7D: result="MODULATION: LFO RATE DOWN 15"; break;
              case 0x85: result="MODULATION: LFO RATE DOWN 16"; break;
              case 0x8D: result="MODULATION: LFO RATE DOWN 17"; break;
              case 0x95: result="MODULATION: LFO RATE DOWN 18"; break;
              case 0x9D: result="MODULATION: LFO RATE DOWN 19"; break;
              case 0xA5: result="MODULATION: LFO RATE DOWN 20"; break;
              case 0xAD: result="MODULATION: LFO RATE DOWN 21"; break;
              case 0xB5: result="MODULATION: LFO RATE DOWN 22"; break;
              case 0xBD: result="MODULATION: LFO RATE DOWN 23"; break;
              case 0xC5: result="MODULATION: LFO RATE DOWN 24"; break;
              case 0xCD: result="MODULATION: LFO RATE DOWN 25"; break;
              case 0xD5: result="MODULATION: LFO RATE DOWN 26"; break;
              case 0xDD: result="MODULATION: LFO RATE DOWN 27"; break;
              case 0xE5: result="MODULATION: LFO RATE DOWN 28"; break;
              case 0xED: result="MODULATION: LFO RATE DOWN 29"; break;
              case 0xF5: result="MODULATION: LFO RATE DOWN 30"; break;
              case 0xFD: result="MODULATION: LFO RATE DOWN 31"; break;

              case 0x1F: result="MODULATION: SOURCE 0"; break;
              case 0x3F: result="MODULATION: SOURCE 1"; break;
              case 0x5F: result="MODULATION: SOURCE 2"; break;

              case 0x8F: result="MODULATION: DESTINATION 0"; break;
              case 0xAF: result="MODULATION: DESTINATION 1"; break;
              case 0xCF: result="MODULATION: DESTINATION 2"; break;
              case 0xFF: result="MODULATION: DESTINATION 3"; break;

              case 0x53: result="MISC: VOICE 3 OFF 0=NO"; break;
              case 0x5B: result="MISC: VOICE 3 OFF 0=YES"; break;

              case 0x4F: result="MISC: HALT 1"; break;
            }
            break;

          case 0x36:
            result="REPEAT: HEAD "+b2;
            break;

          case 0x26:
            result="ENVELOPE: RLS POINT "+b2;
            break;

          case 0x4E:
            result="ENVELOPE: HOLD TIME "+b2;
            break;

          case 0x56:
            result="WAVEFORM: PULSE SWEEP "+(byte)b2;
            break;

          case 0xC6:
            result="WAVEFORM: PULSE VIB DPT "+b2;
            if ((b2 & 0x80)!=0) result+= " ???";
            break;

          case 0xD6:
            result="WAVEFORM: PULSE VIB RAT "+b2;
            if ((b2 & 0x80)!=0) result+= " ???";
            break;

          case 0x76:
            result="FREQ: VIBRATO DEPTH "+b2;
            if ((b2 & 0x80)!=0) result+= " ???";
            break;

          case 0x86:
            result="FREQ: VIBRATO RATE "+b2;
            if ((b2 & 0x80)!=0) result+= " ???";
            break;

          case 0x96:
            result="FILTER: AUTO "+(byte)b2;
            break;

          case 0x0E:
            result="FILTER: CUTOFF "+b2;
            break;

          case 0x66:
            result="FILTER: SWEEP "+(byte)b2;
            break;

          case 0x6E:
            result="MODULATION: SCALE "+(byte)b2;
            if ((byte)b2>7 || (byte)b2<-7) result+= " ???";
            break;

          case 0xE6:
            result="MODULATION: MAX VALUE "+b2;
            break;

          case 0x1E:
            result="MISC: MEASURE # "+b2;
            break;

          case 0x5E:
            result="MISC: MEASURE # "+(b2+256);
            break;

          case 0x9E:
            result="MISC: MEASURE # "+(b2+512);
            break;

          case 0xDE:
            result="MISC: MEASURE # "+(b2+768);
            break;

          case 0xF6:
            result="MISC: UTL-VOICE SET "+b2;
            break;

          case 0x46:
            result="MISC: FLAG "+b2;
            break;

          case 0xB6:
            result="MISC: AUXILIARY "+b2;
            break;

          case 0xA6:
              if ((b2 & 0x01)==0x00) {
                // positive transpose  
                result="FREQ: TRANSPOSE +"+((7- (int)((b2 & 0x0E)>>1) )*12)+((int)((b2 & 0xF0)>>4));
              } else {
                // negative transpose  
                result="FREQ: TRANSPOSE -"+(((int)((b2 & 0x0E)>>1))*12)+((int)((b2 & 0xF0)>>4));
              }
            break;

          case 0x2E:
            result="FREQ: RELATIVE TPS "+ ((int)(3-(b2 &0x07))+ (int)((b2 & 0xF8)>>3));
            break;

          default:                          // look for all the other cases
            if ((b1 & 0x0F)==0x02) {
              result="WAVEFORM:PULSE WIDTH "+ ((int)((b1 & 0xF0)<<4)+b2);
            }

            if ((b1 & 0x03)==0x03) {
              result="FREQ: PORTAMENTO "+ ((int)((b1 & 0xFC)<<6)+b2);
            }

            if ((b1 & 0x1F)==0x1A) {
              result="FREQ: DETUNE "+(((int)((b1 & 0xE0)<<3)+b2)-2048);
            }

            if ((b1 & 0x1F)==0x0A) {
              result="FREQ: DETUNE "+((int)((b1 & 0xE0)<<3)+b2);
            }

            if ((b1 & 0x30)==0x30) {
              result="MISC: JIFFY LENGTH "+((int)((b1 & 0xC0)<<2)+b2-200);
            }
        }
      }
    this.pos+=2;
    this.pc+=2;
    return ShortToExe((int)pc)+"  "+ByteToExe(b1)+" "+ByteToExe(b2)+
           "       "+result;
  }

  /**
   * Comment and Disassemble a region of the buffer
   *
   * @param buffer the buffer containing the code
   * @param start the start position in buffer
   * @param end the end position in buffer
   * @param pc the programn counter for start position
   * @return a string rapresentation of disasemble with comment
   */
  public String cdasm(byte[] buffer, int start, int end, long pc) {
    StringBuilder result=new StringBuilder ("");            // resulting string
    String tmp;                  // local temp string
    String tmp2;                 // local temp string
    int pos=start;               // actual position in buffer

    this.pos=pos;
    this.pc=pc;
    while (pos<end | pos<start) { // verify also that don't circle in the buffer
      tmp=dasm(buffer);
      pos=this.pos;
      pc=this.pc;
      result.append(tmp).append("\n");
    }
    return result.toString();
  }
}
