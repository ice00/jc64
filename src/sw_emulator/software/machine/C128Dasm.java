/**
 * @(#)C128Dasm.java 2020/10/11
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

/**
 * Comment the memory location of C128 for the disassembler
 * It performs also a multy language comments.
 * 
 * @author ice
 */
public class C128Dasm extends M6510Dasm {
  // Available language
  public static final byte LANG_ENGLISH=1;
  public static final byte LANG_ITALIAN=2;
  
  public byte language;///

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
        // do not get comment if appropriate option is not selected  
        switch (language) {                       
          case LANG_ITALIAN:
            switch ((int)addr) {
              case 0x00: return "Direzione registro I/o dati del 6510";
              case 0x01: return "Porta I/o dati del 6510";
              case 0x02: return "Il token \"SEARCH\" cerca, numero di banco, salta all'indirizzo SYS";
              case 0x03: 
              case 0x04: return "Indirizzo SYS, PC registro MLM";
              case 0x05: return "I registri SYS e MLM salvano SR";
              case 0x06: return "I registri SYS e MLM salvano AC";
              case 0x07: return "I registri SYS e MLM salvano XR";
              case 0x08: return "I registri SYS e MLM salvano YR";
              case 0x09: return "I registri SYS e MLM salvano SP";
              case 0x0A: return "Flag di virgolette di scansione";
              case 0x0B: return "Salva colonna TAB";
              case 0x0C: return "Flag: 0=LOAD, 1=VERIFY";
              case 0x0D: return "Puntatore del buffer di input/numero di pedici";
              case 0x0E: return "Flag DIM predefinito";
              case 0x0F: return "Tipo dato: FF=stringa, 00=numerico";
              case 0x10: return "tipo dato: 80=intero, 00=virgola mobile";
              case 0x11: return "Scansione DATI/Citazione LISTA/flag di memoria";
              case 0x12: return "Flag pedice/FNxx";
              case 0x13: return "Flag: 0=INPUT, $40=GET, $98=READ";
              case 0x14: return "Segno ATN/Flag di valutazione di confronto";
              case 0x15: return "Flag del prompt I/O corrente";
              case 0x16: 
              case 0x17: return "Valore intero";
              case 0x18: return "Puntatore: stack di stringhe temporaneo";
              case 0x19: return "Ultimo indirizzo stringa temporanea";  
              case 0x1B: return "Stack per stringhe temporanee";
              case 0x24:
              case 0x25: 
              case 0x26: 
              case 0x27: return "Area del puntatore di utilità";  
              case 0x28: 
              case 0x29:         
              case 0x2A:
              case 0x2B:                  
              case 0x2C: return "Area di prodotto per la moltiplicazione";  
              case 0x2D:  
              case 0x2E: return "Puntatore: Inizio del BASIC (banco 0) [1C01]";   
              case 0x2F:    
              case 0x30: return "Puntatore: inizio delle variabili (banco 1) [0400]";
              case 0x31: 
              case 0x32: return "Puntatore: inizio degli array";
              case 0x33: 
              case 0x34: return "Puntatore: fine degli array";
              case 0x35: 
              case 0x36: return "Memorizzazione della stringa del puntatore (spostandosi verso il basso)";
              case 0x37: 
              case 0x38: return "Puntatore a stringa di utilità";
              case 0x39: 
              case 0x3A: return "Puntatore: limite di memoria (banco 1) [FF00]";              
              case 0x3B:
              case 0x3C: return "Numero di riga BASIC corrente";
              case 0x3D:
              case 0x3E: return "Puntatore di testo: punto di riferimento BASIC (chrget)";    
              case 0x3F: 
              case 0x40: return "Puntatore di utilità";  
              case 0x41: 
              case 0x42: return "Numero di riga DATI corrente"; 
              case 0x43: 
              case 0x44: return "Indirizzo DATA corrente";
              case 0x45: 
              case 0x46: return "Vettore di input";
              case 0x47: 
              case 0x48: return "Nome della variabile corrente";
              case 0x49: 
              case 0x4A: return "Indirizzo variabile corrente";
              case 0x4B:
              case 0x4C: return "Puntatore variabile per FOR/NEXT";
              case 0x4D: 
              case 0x4E: return "Salva Y, Salva operando, salva puntatore BASIC";
              case 0x4F: return "Accumulatore di simboli di confronto"; 
              case 0x50: 
              case 0x51:
              case 0x52:
              case 0x53:
              case 0x54:
              case 0x55: return "Area di lavoro varie, puntatori e così via";
              case 0x56:
              case 0x57:
              case 0x58: return "Vettore salto per le funzioni";
              case 0x60:
              case 0x61: 
              case 0x62: return "Indirizzo MLM 0";
              case 0x63: return "Indirizzo MLM 1/Accumulatore #1 esponente";
              case 0x64: 
              case 0x65: return "Indirizzo MLM 1/Accumulatore #1 mantissa";
              case 0x66:
              case 0x67: return "Indirizzo MLM 2/Accumulatore #1 mantissa"; 
              case 0x68: return "Indirizzo MLM 2/Accumulatore #1 segno";   
              case 0x69: return "Puntatore della costante di valutazione della serie";
              case 0x6A:
              case 0x6B:
              case 0x6C:
              case 0x6D:
              case 0x6E:
              case 0x6F: return "Esponente dell'accumulatore #2 e così via";
              case 0x70: return "Comparazione segno accumulatore #1 con #2";
              case 0x71: return "Accumulatore #1 in ordine inferiore (arrotondamento)";
              case 0x72:                   
              case 0x73: return "Lunghezza buffer cassetta/Puntatore serie";
              case 0x74: 
              case 0x75: return "Incremento automatico del numero di riga";
              case 0x76: return "Flag grafico: FF = Grafica allocata, 00 = Non allocata";
              case 0x77: return "Numero sorgente colore";
              case 0x78: 
              case 0x79: return "Contatori temporanei";
              case 0x7A:  
              case 0x7B:  
              case 0x7C: return "Descrittore DS$";
              case 0x7D: 
              case 0x7E: return "Puntatore allo pseudo-stack BASIC";
              case 0x7F: return "Flag: 0=modo diretto";
              case 0x80:
              case 0x81: return "DOS, flag di lavoro USING";
              case 0x82: return "Salva il puntatore dello stack per gli errori";              
              case 0x83: return "Sorgente colore grafico";
              case 0x84: return "Multicolore 1 (1)";
              case 0x85: return "Multicolore 2 (2)";
              case 0x86: return "Colore grafico in primo piano (13)";
              case 0x87: 
              case 0x88: return "Fattori di scala grafici X";
              case 0x89: 
              case 0x8A: return "Fattori di scala grafici Y";              
              case 0x8B: return "Smetti di dipingere se non stesso sfondo/colore";
              case 0x8C:
              case 0x8D:
              case 0x8E:
              case 0x8F: return "Valori grafici di lavoro";
              case 0x90: return "Parola di stato ST";
              case 0x91: return "Selettore a chiave 1A: flag STOP e RVS"; 
              case 0x92: return "Costante di tempo per il nastro ($80)";  
              case 0x93: return "Valore del lavoro, monitor, LOAD/SAVE: 0=LOAD, 1=VERIFY";
              case 0x94: return "Uscita seriale, flag di carattere differito";
              case 0x95: return "Carattere differito seriale";
              case 0x96: return "Valore di lavoro della cassetta";
              case 0x97: return "Registro salvataggio";
              case 0x98: return "Quanti file aperti";
              case 0x99: return "Dispositivo di input, normalmente 0";
              case 0x9A: return "Dispositivo CMD di uscita, normalmente 3";
              case 0x9B: 
              case 0x9C: return "Parità nastro, flag di ricezione in uscita";
              case 0x9D: return "Messaggi I/O: 192=tutti, 128=comandi, 64=errori, 0=zero";
              case 0x9E:
              case 0x9F: return "Puntatori di errore del nastro";
              case 0xA0: 
              case 0xA1:
              case 0xA2: return "Orologio Jiffy HML";
              case 0xA3: 
              case 0xA4: return "Area dati temporanea";
              case 0xA5: 
              case 0xA6: return "Byte di lavoro I/O (nastro)";
              case 0xA7: return "Memorizzazione bit di ingresso RS-232, conteggio cortocircuito cassetta";
              case 0xA8: return "Conteggio bit RS-232, errore di lettura cassetta";
              case 0xA9: return "Flag RS-232 per controllo bit di avvio, zero lettura cassetta";
              case 0xAA: return "Buffer byte RS-232, modalità lettura cassetta";
              case 0xAB: return "Archiviazione con parità RS-232, Cnt breve cassetta";
              case 0xAC: 
              case 0xAD: return "Puntatore per il buffer del nastro e lo scorrimento dello schermo";
              case 0xAE:
              case 0xAF: return "Indirizzo di fine nastro/Fine programma";
              case 0xB0: 
              case 0xB1: return "Costanti di temporizzazione del nastro";
              case 0xB2:
              case 0xB3: return "Puntatore: inizio del buffer del nastro";
              case 0xB4: return "Conteggio bit RS-232";
              case 0xB5: return "Bit successivo RS-232 da inviare";
              case 0xB6: return "Buffer byte RS-232";
              case 0xB7: return "Numero di caratteri nel nome del file";
              case 0xB8: return "File logico corrente";
              case 0xB9: return "Indirizzo secondario attuale";
              case 0xBA: return "Dispositivo corrente"; 
              case 0xBB: 
              case 0xBC: return "Puntatore al nome del file";  
              case 0xBD: return "Buffer di parità RS-232 TRNS"; 
              case 0xBE: return "Conteggio blocchi lettura cassetta"; 
              case 0xBF: return "Buffer di parole seriale";
              case 0xC0: return "Interruttore manuale/controllato della cassetta (aggiornato durante IRQ)";
              case 0xC1:
              case 0xC2: return "Indirizzo iniziale I/O";
              case 0xC3:
              case 0xC4: return "Tempo di caricamento cassetta (2 byte)";
              case 0xC5: return "Dati di lettura/scrittura su nastro";
              case 0xC6: 
              case 0xC7: return "Banchi: dati I / O, nome file";
              case 0xC8: 
              case 0xC9: return "Indirizzi del buffer di ingresso RS-232  [0c00]";
              case 0xCA: 
              case 0xCB: return "Indirizzi del buffer di uscita RS-232 [0d00]";
              case 0xCC: 
              case 0xCD: return "Puntatore di decodifica della tastiera (banco 15) [fa80]";
              case 0xCE: 
              case 0xCF: return "Stampa puntatore di lavoro stringa"; 
              case 0xD0: return "Numero di caratteri nel buffer della tastiera";
              case 0xD1: return "Numero di caratteri programmati in attesa";
              case 0xD2: return "Indice dei caratteri chiave programmati";
              case 0xD3: return "Flag di spostamento dei tasti";   
              case 0xD4: return "Codice chiave corrente: 88=nessuna chiave";
              case 0xD5: return "Codice chiave precedente: 88=nessuna chiave";
              case 0xD6: return "Input da schermo/da tastiera";
              case 0xD7: return "40/80 colonne: 0=schermo a 40 colonne";
              case 0xD8: return "Codice della modalità grafica";   
              case 0xD9: return "Base di caratteri: 0=ROM, 4=RAM";
              case 0xDA: return "Puntatori per MOVLIN/Temporanea per For TAB & rutine LINE WRAP";
              case 0xDB: return "Un altro posto temporaneo per salvare un reg.";
              case 0xDC:
              case 0xDD:
              case 0xDE:
              case 0xDF: return "Area di lavoro di Misc Editor";
              case 0xE0:
              case 0xE1: return "Puntatore alla riga/cursore dello schermo";
              case 0xE2:    
              case 0xE3: return "Puntatore della linea di colore";
              case 0xE4: return "Margine inferiore dello schermo corrente";
              case 0xE5: return "Margine superiore dello schermo corrente";
              case 0xE6: return "Margine sinistro della schermata corrente";
              case 0xE7: return "Margine destro dello schermo corrente";
              case 0xE8:    
              case 0xE9: return "Log del cursore di input (riga, colonna)"; 
              case 0xEA: return "Fine riga per il puntatore di input";
              case 0xEB: return "Riga in cui risiede il cursore";
              case 0xEC: return "Posizione del cursore sulla riga dello schermo";
              case 0xED: return "Righe massime dello schermo (24)";
              case 0xEE: return "Colonne massime dello schermo (39)";
              case 0xEF: return "Carattere I/O corrente";
              case 0xF0: return "Carattere precedente stampato";
              case 0xF1: return "Colore del carattere";
              case 0xF2: return "Salvataggio temporaneo del colore";
              case 0xF3: return "Contrassegno inverso dello schermo";
              case 0xF4: return "Flag di virgolette (Editor), 0=cursore diretto, altrimenti programmato";
              case 0xF5: return "Numero di INSERT in sospeso";
              case 0xF6: return "255=inserimento automatico abilitato";
              case 0xF7: return "Blocco modalità testo (SHFT-C =): 0=abilitato, 128=disabilitato";
              case 0xF8: return "Scorrimento: 0=abilitato, 128=disabilitato";
              case 0xF9: return "Campanello (CTRL-G): 0=abilitato, 128=disabilitato";
              case 0xFA:
              case 0xFB:
              case 0xFC: 
              case 0xFD:
              case 0xFE: return "Non usato";
              case 0xFF: return "Basic Scratch";
              
              case 0x110: return "Contatore loop DOS";
              case 0x111: return "Nome file DOS 1 lunghezza";
              case 0x112: return "Unità disco DOS 1";
              case 0x113: return "Nome file DOS 2 lunghezza"; 
              case 0x114: return "Unità disco DOS 2";  
              case 0x115: return "Nome file DOS 2 indirizzo"; 
              case 0x116:
              case 0x117: return "Indirizzo iniziale BLOAD/BSAVE";  
              case 0x118:
              case 0x119: return "BLOAD/BSAVE Indirizzo finale";  
              case 0x11B: return "Indirizzo logico DOS [00]";                 
              case 0x11C: return "Indirizzo fisico DOS [08]";
              case 0x11D: return "Indirizzo secondario DOS [6F]";
              case 0x11E: return "Lunghezza record DOS";
              case 0x120: return "ID disco DOS";
              case 0x122: return "DOS DSK ID FLG SPACE Udato da PRINT USING";
              case 0x123: return "Puntatore per inizio N.";
              case 0x124: return "Puntatore per fine N.";
              case 0x125: return "Segno del dollaro";
              case 0x126: return "Flag virgola, PLAY: VOXTUM flag";
              case 0x127: return "Contatore";
              case 0x128: return "Segno eponente";
              case 0x129: return "Puntatore all'esponente";
              case 0x12A: return "Numero di cifre prima della virgola decimale";
              case 0x12B: return "Flag giustifica";
              case 0x12C: return "N. di posizioni prima della virgola decimale (campo)";
              case 0x12D: return "Numero di posizioni dopo il punto decimale (campo)";
              case 0x12E: return "+/- Flag (Campo)";
              case 0x12F: return "Flag esponente (campo)";
              case 0x130: return "Interruttore";
              case 0x131: return "Contatore di caratteri (campo)";
              case 0x132: return "Segno numero";
              case 0x133: return "Blank/Star Flag";
              case 0x134: return "Puntatore all'inizio del campo";
              case 0x135: return "Lunghezza del formato";
              case 0x136: return "Puntatore a fine campo";
              case 0x2FC:
              case 0x2FD: return "Function Execute Hook";
              case 0x300:
              case 0x301: return "Error Message Link [4D3F]";
              case 0x302:
              case 0x303: return "BASIC Warm Start Link	[4DC6]";
              case 0x304:
              case 0x305: return "Crunch BASIC Tokens Link [430D]";
              case 0x306:
              case 0x307: return "Print Tokens Link [5151]";
              case 0x308:
              case 0x309: return "Start New BASIC Code Link [4AA2]";
              case 0x30A:
              case 0x30B: return "Get Arithmetic Element Link [78DA]";
              case 0x30C:
              case 0x30D: return "Crunch FE Hook [4321]";
              case 0x30E: 
              case 0x30F: return "List FE Hook [51CD]";
              case 0x310:
              case 0x311: return "Execute FE Hook [4BA9]";
              case 0x312:
              case 0x313: return "Unused";
              case 0x314:
              case 0x315: return "IRQ Vector [FA65]";
              case 0x316:
              case 0x317: return "Break Interrupt Vector [B003]";
              case 0x318:
              case 0x319: return "NMI Interrupt Vector[FA40]";
              case 0x31A:
              case 0x31B: return "OPEN Vector [EFBD]";
              case 0x31C:
              case 0x31D: return "CLOSE Vector [F188]";
              case 0x31E:
              case 0x31F: return "Set Input Vector [F106]";
              case 0x320:
              case 0x321: return "Set Output Vector [F14C]";
              case 0x322:
              case 0x323: return "Restore I/O Vector [F226]";
              case 0x324:
              case 0x325: return "Input Vector [EF06]";
              case 0x326:
              case 0x327: return "Output Vector	[EF79]";
              case 0x328:
              case 0x329: return "Test STOP Key [F66E]";
              case 0x32A:
              case 0x32B: return "GET Vector [EEEB]";
              case 0x32C:
              case 0x32D: return "Abort I/O Vector [F222]";
              case 0x32E:
              case 0x32F: return "Machine Lang Monitor Link [B006]";
              case 0x330:
              case 0x331: return "LOAD Link [F26C]";
              case 0x332:
              case 0x333: return "SAVE Link [F54E]";
              case 0x334:
              case 0x335: return "Print Control Code Link [C7B9]";
              case 0x336:
              case 0x337: return "Print High ASCII Code Link [C805]";
              case 0x338:
              case 0x339: return "Print ESC Sequence Link [C9C1]";
              case 0x33A:
              case 0x33B: return "Keyscan Link [C5E1]";
              case 0x33C:
              case 0x33D: return "Store Key [C6AD]";
              case 0x33E:
              case 0x33F: return "Pointer to KBD Decoding Table: Unshifted [FA80/FD29]";
              case 0x340:
              case 0x341: return "Pointer to KBD Decoding Table: Shifted [FAD9/FD82]";
              case 0x342:
              case 0x343: return "Pointer to KBD Decoding Table: Commodore [FB32/FDDB]";
              case 0x344:
              case 0x345: return "Pointer to KBD Decoding Table: Control [FB8B/FE34] 1)";
              case 0x346:
              case 0x347: return "Pointer to KBD Decoding Table: Alt [FA80/FD29]";
              case 0x348:
              case 0x349: return "Pointer to KBD Decoding Table: Ascii/DIN [FB4E/FD29]";              
              case 0x35E:
              case 0x35F: 
              case 0x360:
              case 0x362: return "Bitmap Of Line Wraps";
              case 0x386: return "CHRGOT Entry";
              case 0x3D2:
              case 0x3D3: 
              case 0x3D4: return "Numeric Constant For BASIC";
              case 0x3D5: return "Current Bank For SYS, POKE, PEEK";
              case 0x3D6: 
              case 0x3D7:
              case 0x3D8: 
              case 0x3D9: return "INSTR Work Values";
              case 0x3DA: return "Bank Pointer For String/Number CONVERT RTN";
              case 0x3DB: 
              case 0x3DC:
              case 0x3DE: return "Sprite: Work bytes for SSHAPE";
              case 0x3DF: return "FAC#1 Overflow";
              case 0x3E0: 
              case 0x3E1: return "Sprite: Work bytes for SPRSAV";
              case 0x3E2: return "Graphic Foreground/ Background Color Nybbles";
              case 0x3E3: return "Graphic Foreground/ Multicolor 1 Color Nybbles";
              case 0xA00:
              case 0xA01: return "Vector to Restart System (BASIC Warm) [4003]";
              case 0xA02: return "KERNAL Warm/Cold Init'n Status Byte";
              case 0xA03: return "PAL/NTSC System Flag";
              case 0xA04: return "Flags RESET vs. NMI Status for init'n rtns";
              case 0xA05: 
              case 0xA06: return "Ptr to Bottom of Avail. Memory in System Bank";
              case 0xA07: 
              case 0xA08: return "Ptr to Top of Available Memory in System Bank";
              case 0xA09: return "Tape Handler preserves IRQ Indirect here";
              case 0xA0B: return "TOD Sense during tape operations";
              case 0xA0C: return "CIA 1 Interrupt Log";
              case 0xA0D: return "CIA 1 Timer Enabled";
              case 0xA0F: return "RS-232 Enables";
              case 0xA10: return "RS-232 Control Register";
              case 0xA11: return "RS-232 Command Register";
              case 0xA12: return "RS-232 User Baud Rate";
              case 0xA14: return "RS-232 Status Register";
              case 0xA15: return "RS-232 Number of Bits To Send";
              case 0xA16: return "RS-232 Baud Rate Full Bit Time (Created by OPEN)";
              case 0xA18: return "RS-232 Receive Pointer";
              case 0xA19: return "RS-232 Input Pointer";
              case 0xA1A: return "RS-232 Transmit Pointer";
              case 0xA1B: return "RS-232 Send Pointer";
              case 0xA1C: return "Fast Serial Internal/External Flag";
              case 0xA1D: return "Decrementing Jiffie Register";
              case 0xA1E:
              case 0xA1F: return "Sleep Countdown, FFFF = disable";
              case 0xA20: return "Keyboard Buffer Size (10)";
              case 0xA21: return "Screen Freeze Flag";
              case 0xA22: return "Key Repeat: 128 = all, 64 = none";
              case 0xA23: return "Key Repeat Timing";
              case 0xA24: return "Key Repeat Pause";
              case 0xA25: return "Graphics / Text Toggle Latch";
              case 0xA26: return "40-Col Cursor Mode/VIC Cursor Mode (Blinking, Solid)";
              case 0xA27: return "VIC Cursor Disable";
              case 0xA28: return "VIC Cursor Blink Counter";
              case 0xA29: return "VIC Cursor Character Before Blink";
              case 0xA2A: return "40-Col Blink Values/VIC Cursor Color Before Blink";
              case 0xA2B: return "80-Col Cursor Mode/VDC Cursor Mode (when enabled)";
              case 0xA2C: return "40-Col Video $D018 Image/VIC Text Screen/Character Base Pointer";
              case 0xA2D: return "VIC Bit-Map Base Pointer";
              case 0xA2E: return "80-Col Pages-Screen/VDC Text Screen Base ";
              case 0xA2F: return "80-Col Pages-Screen/Color/VDC Attribute Base";
              case 0xA30: return "Temporary Pointer to Last Line For LOOP4";
              case 0xA31: return "Temporary For 80-Col Routines";
              case 0xA32: return "Temporary For 80-Col Routines";
              case 0xA33: return "VDC Cursor Color Before Blink";
              case 0xA34: return "VIC Split Screen Raster Value";
              case 0xA35: return "Save .X During Bank Operations";
              case 0xA36: return "Counter for PAL Systems (Jiffie adjustment)";
              case 0xA37: return "Save System Speed During Tape and Serial Ops";
              case 0xA38: return "Save Sprite Enables During Tape and Serial Ops";
              case 0xA39: return "Save Blanking Status During Tape Ops";
              case 0xA3A: return "Flag set by user to resrv full control of VIC";
              case 0xA3B: return "Hi byte:SA Of VIC Scrn (Use W/VMI to move scrn)";
              case 0xA3C: 
              case 0xA3D: return "8563 Block Fill";
              case 0xA80: return "Compare Buffer (32 bytes)";
              case 0xAA0:
              case 0xAA1: return "MLM";
              case 0xAAB: return "ASM/DIS";
              case 0xAAC: return "For Assembler";
              case 0xAAF: 
              case 0xAB0: return "Byte Temp used all over";
              case 0xAB1: return "Byte Temp for Assembler";
              case 0xAB2: return "Save .X here during Indirect Subroutine Calls";
              case 0xAB3: return "Direction Indicator For 'TRANSFER'";
              case 0xAB4: 
              case 0xAB5: return "Parse Number Conversion";
              case 0xAB7: return "Parse Number Conversion";
              case 0xAC0: return "PAT Counter/Current Function Key ROM Bank Being Polled";
              case 0xAC1:
              case 0xAC2:
              case 0xAC3:
              case 0xAC4: return "ROM Physical Address Table (IDS OF LOGGED-IN CARDS)";
              case 0xAC5: return "Flag: KBD/Reserved For Foreign Screen Editors";
              case 0x1131: return "Current X Position";
              case 0x1133: return "Current Y Position";
              case 0x1135: return "X-Coordinate Destination";
              case 0x1137: return "Y-Coordinate Destination";
              case 0x1139: return "Line Drawing Variables";
              case 0x1149: return "Sign Of Angle";
              case 0x114A: return "Sine Of Value Of Angle";
              case 0x114C: return "Cosine of Value of Angle";
              case 0x114E: return "Temps For Angle Distance Routines";
              case 0x1150: return "CIRCLE Center, X Coordinate, BOX POINT 1 X-Coord.";
              case 0x1152: return "CIRCLE Center, Y Coordinate, BOX POINT 1 Y-Coord.";
              case 0x1153: return "String Len";
              case 0x1154: return "X Radius, BOX Rotation Angle";
              case 0x1155: return "String Pos'n Counter";
              case 0x1156: return "Y Radius";
              case 0x1157: return "New String or Bit Map Byte";
              case 0x1158: return "CIRCLE Rotation Angle/Placeholder";
              case 0x1159: return "SHAPE Column Length";
              case 0x115A: return "BOX: Length of a side";
              case 0x115B: return "SHAPE Row Length";
              case 0x115C: return "Arc Angle Start";  
              case 0x115D: return "Temp For Column Length";
              case 0x115E: return "Arc Angle End, Char's Col. Counter";
              case 0x115F: return "Save SHAPE String Descriptor";
              case 0x1160: return "X Radius * COS(Rotation Angle)";
              case 0x1161: return "Bit Index Into Byte";
              case 0x1162: return "Y Radius * SIN(Rotation Angle)";
              case 0x1164: return "X Radius * SIN(Rotation Angle)";
              case 0x1166: return "Y Radius * COS(Rotation Angle)";
              case 0x1168: return "HIGH BYTE: ADDR OF CHARROM For 'CHAR' CMD.";
              case 0x1169: return "Temp For GSHAPE";
              case 0x116A: return "SCALE Mode Flag";
              case 0x116B: return "Double Width Flag";
              case 0x116C: return "Box Fill Flag";
              case 0x116D: return "Temp For Bit Mask";
              case 0x116F: return "Trace Mode: FF = on";
              case 0x1170:
              case 0x1171:
              case 0x1172: 
              case 0x1173: return "Renumbering Pointers";
              case 0x1174:
              case 0x1175:
              case 0x1176: return "Directory Work Pointers/Graphic Temp Storage"; 
              case 0x1177: return "Directory Work Pointers";  
              case 0x117A:
              case 0x117B: return "Float-fixed Vector [849F]";
              case 0x117C:
              case 0x117D: return "Fixed-float Vector [793C]";
              case 0x11E6: return "Sprite X-High Positions";
              case 0x11E7:
              case 0x11E8: return "Sprite Bumb Masks (sprite - backgnd)";
              case 0x11E9:
              case 0x11EA: return "Light Pen Values, X and Y";
              case 0x11EB: return "CHRGEN ROM Page, Text Mode [D8]";
              case 0x11EC: return "CHRGEN ROM Page, Graphics Mode [D0]";
              case 0x11ED: return "Secondary Address For RECORD";
              case 0x1200:
              case 0x1201: return "Previous BASIC Line";
              case 0x1202:
              case 0x1203: return "Pointer: BASIC Statement for CONTINUE";
              case 0x1204: return "PRINT USING Fill Symbol";
              case 0x1205: return "PRINT USING Comma Symbol";
              case 0x1206: return "PRINT USING D.P. Symbol";
              case 0x1207: return "Print Using Monetary Symbol";
              case 0x1208: return "Used by Error Trapping Routine - Last Err No";
              case 0x1209:
              case 0x120A: return "Line # of Last Error. FFFF if No Error";
              case 0x120B:
              case 0x120C: return "TRAP Address, FFFF=none";
              case 0x120D: return "Hold Trap # of Tempor.";
              case 0x1210:
              case 0x1211: return "End of Basic, Bank 0";
              case 0x1212: 
              case 0x1213: return "Basic Program Limit [FF00]";
              case 0x1214: 
              case 0x1215: 
              case 0x1216: 
              case 0x1217: return "DO Work Pointers";
              case 0x1218: 
              case 0x1219:
              case 0x121A: return "USR Program Jump [7D28]";
              case 0x121B: 
              case 0x121C: 
              case 0x121D: 
              case 0x121E:
              case 0x121F: return "RND Seed Value";
              case 0x1220: return "Degrees Per CIRCLE Segment";
              case 0x1221: return "'Cold' or 'Warm' Reset Status";
              case 0x1222: return "Sound Tempo";
              case 0x1223:
              case 0x1224: return "Remaining Note Length LO/HI, Voice 1";
              case 0x1225:
              case 0x1226: return "Remaining Note Length LO/HI, Voice 2";
              case 0x1227:
              case 0x1228: return "Remaining Note Length LO/HI, Voice 3";
              case 0x1229:
              case 0x122A: return "Note Length LO/HI";
              case 0x122B: return "Octave";
              case 0x122C: return "Flag: 01 = Sharp, FF = Flat";
              case 0x122D:
              case 0x122E: return "Pitch";
              case 0x122F: return "Music Sequencer (Voice Number)";
              case 0x1230: return "Wave";
              case 0x1233: return "Flag: Play Dotted Note";
              case 0x1234: 
              case 0x1235: 
              case 0x1236:
              case 0x1237: return "Note Image";
              case 0x1271:
              case 0x1272:
              case 0x1273:
              case 0x1274: return "Note: xx, xx, volume";
              case 0x1275: return "Previous Volume Image";
              case 0x1276:
              case 0x1277:
              case 0x1278: return "Collision IRQ Task Table";
              case 0x127F: return "Collision Mask";
              case 0x1280: return "Collision Work Value";
              case 0x1281: return "SOUND Voice";
              case 0x1282: return "SOUND Time LO";
              case 0x1285: 
              case 0x1286:
              case 0x1287: return "SOUND Time HI";
              case 0x1288:
              case 0x1289:
              case 0x128A: return "SOUND Max LO";
              case 0x128B: return "SOUND Max HI";
              case 0x128E: return "SOUND Min LO";
              case 0x1291: return "SOUND Min HI";
              case 0x1294: return "SOUND Direction";
              case 0x1297: return "SOUND Step LO";
              case 0x129A: return "SOUND Step HI";
              case 0x129B: return "SOUND Freq LO";
              case 0x12A0: return "SOUND Freq HI";
              case 0x12A3: return "Temp Time LO";
              case 0x12A4: return "Temp Time HI";
              case 0x12A5: return "Temp Max LO";
              case 0x12A6: return "Temp Max HI";
              case 0x12A7: return "Temp Min LO";
              case 0x12A8: return "Temp Min HI";
              case 0x12A9: return "Temp Direction";
              case 0x12AA: return "Temp Step LO";
              case 0x12AB: return "Temp Step HI";
              case 0x12AC: return "Temp Freq LO";
              case 0x12AD: return "Temp Freq HI";
              case 0x12AE: return "Temp Pulse LO";
              case 0x12AF: return "Temp Pulse HI";
              case 0x12B0: return "Temp Waveform";
              case 0x12B1: 
              case 0x12B2: return "PEN/POT Work Values";
              case 0x12B7: 
              case 0x12FA: 
              case 0x12FB: return "Used BY SPRDEF & SAVSPR";
              case 0x12FC: return "Sprite Number/Used BY SPRDEF & SAVSPR";
              case 0x12FD: return "Used by BASIC IRQ to block all but one IRQ call";
              
              case 0x4000:
              case 0x4001:
              case 0x4002: return "COLD ENTRY";
              case 0x4003:
              case 0x4004:
              case 0x4005: return "WARM ENTRY";
              case 0x4006:
              case 0x4007:
              case 0x4008: return "IRQ ENTRY";
              case 0x4009: return "Basic Restart";
              case 0x4023: return "Basic Cold Start";
              case 0x4045: return "Set-Up Basic Constants";
              case 0x4112: return "Chime";
              case 0x417A: return "Set Preconfig Registers";
              case 0x4189: return "Registers For $D501";
              case 0x418D: return "Init Sprite Movement Tabs";
              case 0x419B: return "Print Startup Message";
              case 0x41BB: return "Startup Message";
              case 0x4251: return "Set Basic Links";
              case 0x4267: return "Basic Links for $0300";
              case 0x4279: return "Chrget For $0380";
              case 0x42CE: return "Get From ($50) Bank1";
              case 0x42D3: return "Get From ($3f) Bank1";
              case 0x42D8: return "Get From ($52) Bank1";
              case 0x42DD: return "Get From ($5c) Bank0";
              case 0x42E2: return "Get From ($5c) Bank1";
              case 0x42E7: return "Get From ($66) Bank1";
              case 0x42EC: return "Get From ($61) Bank0";
              case 0x4271: return "Get From ($70) Bank0";
              case 0x42F6: return "Get From ($70) Bank1";
              case 0x42FB: return "Get From ($50) Bank1";
              case 0x4300: return "Get From ($61) Bank1";
              case 0x4305: return "Get From ($24) Bank0";
              case 0x430A: return "Crunch Tokens";
              case 0x43CC: return "Move Down Input Buffer";
              case 0x43E2: return "Check Keyword Match";
              case 0x4417: return "Keywords - Non prefixed";
              case 0x4609: return "Keywords - Prefix FE";
              case 0x46C9: return "Keywords - Prefix CE";
              case 0x46FC: return "Action Vectors";
              case 0x47D8: return "Function Vectors";
              case 0x4828: return "Defunct Vectors";
              case 0x4846: return "Unimplemented Commands";
              case 0x484B: return "Error Messages";
              case 0x4A82: return "Find Message";
              case 0x4A9F: return "Start New Basic Code";
              case 0x4B34: return "Update Continue Pointer";
              case 0x4B3F: return "Execute/Trace Statement";
              case 0x4BCB: return "Perform [stop]";
              case 0x4BCD: return "Perform [end]";
              case 0x4BF7: return "Setup FN Reference";
              case 0x4C86: return "Evaluate <or>";
              case 0x4C89: return "Evaluate <and>";
              case 0x4CB6: return "Evaluate <compare>";
              case 0x4D2A: return "Print 'ready'";
              case 0x4D2D: return "'ready.'";
              case 0x4D37: return "Error or Ready";
              case 0x4DCA: return "Print 'out of memory'";
              case 0x4D3C: return "Error";
              case 0x4DAF: return "Break Entry";
              case 0x4DC3: return "Ready For Basic";
              case 0x4DE2: return "Handle New Line";
              case 0x4FAF: return "Rechain Lines";
              case 0x4F82: return "Reset End-of-Basic";
              case 0x4F93: return "Receive Input Line";
              case 0x4FAA: return "Search B-Stack For Match";
              case 0x4FFE: return "Move B-Stack Down";
              case 0x5017: return "Check Memory Space";
              case 0x5047: return "Copy B-Stack Pointer";
              case 0x5050: return "Set B-Stack Pointer";
              case 0x5059: return "Move B-Stack Up";
              case 0x5064: return "Find Basic Line";
              case 0x50A0: return "Get Fixed Pt Number";
              case 0x50E2: return "Perform [list]";
              case 0x5123: return "List Subroutine";
              case 0x51D6: return "Perform [new]";
              case 0x51F3: return "Set Up Run";
              case 0x51F8: return "Perform [clr]";
              case 0x5238: return "Clear Stack & Work Area";
              case 0x5250: return "Pudef Characters";
              case 0x5254: return "Back Up Text Pointer";
              case 0x5262: return "Perform [return]";
              case 0x528F: return "Perform [data/bend]";
              case 0x529D: return "Perform [rem]";
              case 0x52A2: return "Scan To Next Statement";
              case 0x52A5: return "Scan To Next Line";
              case 0x52C5: return "Perform [if]";
              case 0x5320: return "Search/Skip Begin/Bend";
              case 0x537C: return "Skip String Constant";
              case 0x5391: return "Perform [else]";
              case 0x53A3: return "Perform [on]";
              case 0x53C6: return "Perform [let]";
              case 0x54F6: return "Check String Location";
              case 0x553A: return "Perform [print#]";
              case 0x5540: return "Perform [cmd]";
              case 0x555A: return "Perform [print]";
              case 0x5600: return "Print Format Char";
              case 0x5607: return "-print '<cursor right>'-";
              case 0x5604: return "-print space-";
              case 0x560A: return "-print '?'-";
              case 0x5612: return "Perform [get]";                           
              case 0x5635: return "Getkey";
              case 0x5648: return "Perform [input#]";
              case 0x5662: return "Perform [input]";
              case 0x569C: return "Prompt & Input";
              case 0x56A9: return "Perform [read]";
              case 0x57F4: return "Perform [next]";
              case 0x587B: return "Perform [dim]";
              case 0x5885: return "Perform [sys]";
              case 0x58B4: return "Perform [tron]";
              case 0x58B7: return "Perform [troff]";
              case 0x58BD: return "Perform [rreg]";
              case 0x5901: return "Assign <mid$>";
              case 0x5975: return "Perform [auto]";
              case 0x5986: return "Perform [help]";
              case 0x59AC: return "Insert Help Marker";
              case 0x59CF: return "Perform [gosub]";
              case 0x59DB: return "Perform [goto]";
              case 0x5A15: return "Undef'd Statement";
              case 0x5A1D: return "Put Sub To B-Stack";
              case 0x5A3D: return "Perform [go]";
              case 0x5A60: return "Perform [cont]";
              case 0x5A9B: return "Perform [run]";
              case 0x5ACA: return "Perform [restore]";
              case 0x5AF0: return "Keywords To Renumber";
              case 0x5AF8: return "Perform [renumber]";
              case 0x5BAE: return "Renumber-Continued";
              case 0x5BFB: return "Renumber Scan";
              case 0x5D19: return "Convert Line Number";
              case 0x5D68: return "Get Renumber Start";
              case 0x5D75: return "Count Off Lines";
              case 0x5D89: return "Add Renumber Inc";
              case 0x5D99: return "Scan Ahead";
              case 0x5DA7: return "Set Up Block Move";
              case 0x5DC6: return "Block Move Down";
              case 0x5DDF: return "Block Move Up";
              case 0x5DEE: return "Check Block Limit";
              case 0x5DF9: return "Perform [for]";
              case 0x5E87: return "Perform [delete]";
              case 0x5EFB: return "Get Line Number Range";
              case 0x5F34: return "Perform [pudef]";
              case 0x5F4D: return "Perform [trap]";
              case 0x5F62: return "Perform [resume]";
              case 0x5FB7: return "Reinstate Trap Point";
              case 0x5FD8: return "Syntax Exit";
              case 0x5FDB: return "Print 'can't resume'";
              case 0x5FE0: return "Perform [do]";
              case 0x6039: return "Perform [exit]";
              case 0x608A: return "Perform [loop]";
              case 0x60B4: return "Print 'loop not found'";
              case 0x60B7: return "Print 'loop without do'";
              case 0x60DB: return "Eval While/Until Argument";
              case 0x60E1: return "Define Programmed Key";
              case 0x610A: return "Perform [key]";
              case 0x619D: return "'+chr$('";
              case 0x61A8: return "Perform [paint]";
              case 0x627C: return "Check Painting Split";
              case 0x62B7: return "Perform [box]";
              case 0x63F5: return "Authors";
              case 0x642B: return "Perform [sshape]";
              case 0x658D: return "Perform [gshape]";
              case 0x668E: return "Perform [circle]";
              case 0x6750: return "Draw Circle";
              case 0x6797: return "Perform [draw]";
              case 0x67D7: return "Perform [char]";
              case 0x6955: return "Perform [locate]";
              case 0x6960: return "Perform [scale]";
              case 0x69D8: return "Scale Factor Constants";
              case 0x69e2: return "Perform [color]";
              case 0x6A4C: return "Color Codes";
              case 0x6A5C: return "Log Current Colors";
              case 0x6A79: return "Perform [scnclr]";
              case 0x6B06: return "Fill Memory Page";
              case 0x6B17: return "Set Screen Color";
              case 0x6B5A: return "Perform [graphic]";
              case 0x6BC9: return "Perform [bank]";
              case 0x6BD7: return "Perform [sleep]";
              case 0x6C09: return "Multiply Sleep Time";
              case 0x6C2D: return "Perform [wait]";
              case 0x6C4F: return "Perform [sprite]";
              case 0x6CB3: return "Bit Masks";
              case 0x6CC6: return "Perform [movspr]";
              case 0x6DD9: return "Sprite Motion Table Offsets";
              case 0x6DE1: return "Perform [play]";
              case 0x6E02: return "Analyze Play Character";
              case 0x6E66: return "-Filter";
              case 0x6E9D: return "-Voice";
              case 0x6EA8: return "-Octave";
              case 0x6EB2: return "Set SID Sound";
              case 0x6EDD: return "-Volume";
              case 0x6EFD: return "Play Error";
              case 0x6F03: return "Dotted Note";
              case 0x6F07: return "Note Length Char";
              case 0x6F1E: return "Note A-G";
              case 0x6F52: return "...votxum...";
              case 0x6F69: return "Sharp";
              case 0x6F6C: return "Flat";
              case 0x6F78: return "Rest";
              case 0x6FD7: return "Perform [tempo]";
              case 0x6FE4: return "Voice Times Two	";
              case 0x6FE7: return "Length Characters";
              case 0x6FEC: return "Command Characters	";
              case 0x6FF2: return "Indexes for Note Symbols	";
              case 0x6FF9: return "Pitch LO -- NTSC";
              case 0x7005: return "Pitch HI -- NTSC";
              case 0x7011: return "Envelope A/D Patterns";
              case 0x702F: return "Chime Seq";
              case 0x7039: return "SID Voice Offsets";
              case 0x703C: return "SID Volume Scale";
              case 0x7046: return "Perform [filter]";
              case 0x70C1: return "Perform [envelope]";
              case 0x7164: return "Perform [collision]";
              case 0x7190: return "Perform [sprcolor]";
              case 0x71B6: return "Perform [width]";
              case 0x71C5: return "Perform [vol]";
              case 0x71EC: return "Perform [sound]";
              case 0x72CC: return "Perform [window]";
              case 0x7335: return "Perform [boot]";
              case 0x7372: return "Perform [sprdef]";
              case 0x7452: return "-Perform [home]";
              case 0x7497: return "-Perform [1234]";
              case 0x74F4: return "-Perform []";
              case 0x74FE: return "-Perform [shift-return]";
              case 0x7506: return "-Perform [m]";
              case 0x751C: return "-Perform [y]";
              case 0x751F: return "-Perform [x]";
              case 0x7530: return "-Perform [clr]";
              case 0x753F: return "-Perform [right]";
              case 0x7542: return "-Perform [left]";
              case 0x7564: return "-Perform [up]";
              case 0x7567: return "-Perform [down]";
              case 0x7576: return "-Perform [a]";
              case 0x7581: return "-Perform [return]";
              case 0x7588: return "Perform [c]";
              case 0x767F: return "SPRDEF Commands";
              case 0x7691: return "SPRDEF Command Vectors HI/LO";
              case 0x76B5: return "SPRDEF Color Commands";
              case 0x76EC: return "Perform [sprsav]";
              case 0x77B3: return "Perform [fast]";
              case 0x77C4: return "Perform [slow]";
              case 0x77D7: return "Type Match Check";
              case 0x77DA: return "Confirm Numeric";
              case 0x77DD: return "Confirm String";
              case 0x77E7: return "Print 'type mismatch'";
              case 0x77EA: return "Print 'formula too complex'";
              case 0x77EF: return "Evaluate Expression";
              case 0x78D7: return "Evaluate Item";
              case 0x78FE: return "Constant PI";
              case 0x7903: return "Continue Expression";
              case 0x7930: return "Evaluate <equal>";
              case 0x793C: return "Fixed-Float";
              case 0x7950: return "Eval Within Parens";
              case 0x7956: return "-Check Right Parenthesis";
              case 0x7959: return "-Check Left Parenthesis";
              case 0x795C: return "-Check Comma";
              case 0x796C: return "Syntax Error";
              case 0x7978: return "Search For Variable";
              case 0x7A85: return "Unpack RAM1 to FAC#1";
              case 0x7AAF: return "Locate Variable";
              case 0x7B3C: return "Check Alphabetic";
              case 0x7B46: return "Create Variable";
              case 0x7CAB: return "Set Up Array";
              case 0x7D25: return "Print 'bad subscript'";
              case 0x7D28: return "Print 'illegal quantity'";
              case 0x7E3E: return "Compute Array Size";
              case 0x7E71: return "Array Pointer Subroutine";
              case 0x7E82: return "Set Bank patch for [char]";
              case 0x7E88: return "Set Bank patch for Screen Print Link";
              case 0x7E8E: return "Patch for Renumber Scan";
              case 0x7E94: return "Patch for [delete]";
              case 0x7EA6: return "Patch for Note A-G";
              case 0x7EB9: return "Pitch LO -- PAL";
              case 0x7EC5: return "Pitch HI -- PAL";
              case 0x7ED1: return "String Stack patch for Error";
              case 0x7ED9: return "Unused";
              case 0x7FC0: return "Copyright Banner";
              case 0x7FFC: return "Checksum (?)";
              
              case 0x8000: return "Evaluate <fre>";
              case 0x8020: return "Decrypt Message";
              case 0x803A: return "-Free in Bank 1";
              case 0x804A: return "Evaluate <val>";
              case 0x8052: return "String to Float";
              case 0x8076: return "Evaluate <dec>";
              case 0x80C5: return "Evaluate <peek>";
              case 0x80E5: return "Perform [poke]";
              case 0x80F6: return "Evaluate <err$>";
              case 0x8139: return "Swap x With y";
              case 0x8142: return "Evaluate <hex$>";
              case 0x816B: return "Byte to Hex";
              case 0x8182: return "Evaluate <rgr>";
              case 0x818C: return "Get Graphics Mode";
              case 0x819B: return "Evaluate <rclr>";
              case 0x81F3: return "CRTC Color Codes";
              case 0x8203: return "Evaluate <joy>";
              case 0x8242: return "Joystick Values";
              case 0x824D: return "Evaluate <pot>";
              case 0x82AE: return "Evaluate <pen>";
              case 0x82FA: return "Evaluate <pointer>";
              case 0x831E: return "Evaluate <rsprite>";
              case 0x835B: return "VIC Sprite Register Numbers	";
              case 0x8361: return "Evaluate <rspcolor>";
              case 0x837C: return "Evaluate <bump>";
              case 0x8397: return "Evaluate <rspos>";
              case 0x83E1: return "Evaluate <xor>";
              case 0x8407: return "Evaluate <rwindow>";
              case 0x8434: return "Evaluate <rnd>";
              case 0x8490: return "Rnd Multiplier";
              case 0x849A: return "Value 32768";
              case 0x849F: return "Float-Fixed Unsigned";
              case 0x84A7: return "Evaluate Fixed Number";
              case 0x84AD: return "Float-Fixed Signed";
              case 0x84C9: return "Float (.y, .a)";
              case 0x84D0: return "Evaluate <pos>";
              case 0x84D4: return "Byte To Float";
              case 0x84D9: return "Check Direct";
              case 0x84DD: return "Print 'illegal direct'";
              case 0x84E0: return "Print 'undef'd function'";
              case 0x84E5: return "Set Up 16 Bit Fix-Float";
              case 0x84F0: return "Check Direct";
              case 0x84F5: return "Print 'direct mode only'";
              case 0x84FA: return "Perform [def]";
              case 0x8528: return "Check FN Syntax";
              case 0x853B: return "Perform [fn]";
              case 0x85AE: return "Evaluate <str$>";
              case 0x85BF: return "Evaluate <chr$>";
              case 0x85D6: return "Evaluate <left$>";
              case 0x860A: return "Evaluate <right$>";
              case 0x861C: return "Evaluate <mid$>";
              case 0x864D: return "Pull String Parameters";
              case 0x8668: return "Evaluate <len>";
              case 0x866E: return "Exit String Mode";
              case 0x8677: return "Evaluate <asc>";
              case 0x8688: return "Calc String Vector";
              case 0x869A: return "Set Up String";
              case 0x874E: return "Build String to Memory";
              case 0x877B: return "Evaluate String";
              case 0x87E0: return "Clean Descriptor Stack";
              case 0x87F1: return "Input Byte Parameter";
              case 0x87F4: return "-Eval Byte Parameter";
              case 0x8803: return "Params For Poke/Wait";
              case 0x880F: return "Input Next Float/Fixed Value";
              case 0x8812: return "-Input Float/Fixed Value";
              case 0x8815: return "Float/Fixed";
              case 0x882E: return "Subtract From Memory";
              case 0x8831: return "Evaluate <subtract>";
              case 0x8845: return "Add Memory";
              case 0x8848: return "Evaluate <add>";
              case 0x88D6: return "Zero Both Accuymulators";
              case 0x8917: return "Trim FAC#1 Left";
              case 0x8926: return "Negate FAC#1";
              case 0x894E: return "Round Up FAC#1";
              case 0x895D: return "Print 'overflow'";
              case 0x899C: return "Log Series: 1.00";
              case 0x89A1: return "Log Series: #03 (counter)";
              case 0x89A2: return "Log Series: 0.434255942";
              case 0x89A7: return "Log Series: 0.57658454";
              case 0x89AC: return "Log Series: 0.961800759";
              case 0x89B1: return "Log Series: 2.885390073";
              case 0x89B6: return "Log Series: 0.707106781 SQR(0.5)";
              case 0x89BB: return "Log Series: 1.41421356 SRQ(2)";
              case 0x89C0: return "Log Series: -0.5";
              case 0x89C5: return "Log Series: 0.693147181 LOG(2)";
              case 0x89CA: return "Evaluate <log>";                            
              case 0x8A0E: return "Add 0.5";
              case 0x8A12: return "Add Memory At A/Y";
              case 0x8A18: return "Subtract Memory At A/Y";
              case 0x8A1E: return "Divide By Memory";
              case 0x8A24: return "Multiply By Memory";
              case 0x8A27: return "Evaluate <multiply>";
              case 0x8A89: return "Unpack ROM to FAC#2";
              case 0x8AB4: return "Unpack RAM1 to FAC#2";
              case 0x8AEC: return "Test Both Accumulators";
              case 0x8B09: return "Overflow/Underflow";
              case 0x8B17: return "Multiply By 10";
              case 0x8B2E: return "+10";
              case 0x8B33: return "Print 'division by zero'";
              case 0x8B38: return "Divide By 10";
              case 0x8B49: return "Divide Into Memory";
              case 0x8B4C: return "Evaluate <divide>";
              case 0x8BD4: return "Unpack ROM to FAC#1";
              case 0x8BF9: return "Pack FAC#1 to $5e";
              case 0x8BFC: return "Pack FAC#1 to $59";
              case 0x8C00: return "Pack FAC#1 to RAM1";
              case 0x8C28: return "FAC#2 to FAC#1";
              case 0x8C38: return "FAC#1 to FAC#2";
              case 0x8C47: return "Round FAC#1";
              case 0x8C57: return "Get Sign";
              case 0x8C65: return "Evaluate <sgn>";
              case 0x8C68: return "Byte Fixed-Float";
              case 0x8C75: return "Fixed-Float";
              case 0x8C84: return "Evaluate <abs>";
              case 0x8C87: return "Compare FAC#1 to Memory";
              case 0x8CC7: return "Float-Fixed";
              case 0x8CFB: return "Evaluate <int>";
              case 0x8D22: return "String to FAC#1";
              case 0x8DB0: return "Get Ascii Digit";
              case 0x8E17: return "String Conversion Constants: 99999999.9";
              case 0x8E1C: return "String Conversion Constants: 999999999";
              case 0x8E21: return "String Conversion Constants: 1000000000";
              case 0x8E26: return "Print 'in...'";
              case 0x8E32: return "Print Integer";
              case 0x8E42: return "Float to Ascii";
              case 0x8F76: return "+0.5";
              case 0x8F7B: return "Decimal Constants";
              case 0x8F9F: return "TI Constants";
              case 0x8FB7: return "Evaluate <sqr>";
              case 0x8FBE: return "Raise to Memory Power";
              case 0x8FC1: return "Evaluate <power>";
              case 0x8FFA: return "Evaluate <negate>";
              case 0x9005: return "Exp Series: 1.44269504 (1/LOG to base 2 e)";
              case 0x900A: return "Exp Series: #07 (counter)";
              case 0x900B: return "Exp Series: 2.149875 E-5";
              case 0x9010: return "Exp Series: 1.435231 E-4";
              case 0x9015: return "Exp Series: 1.342263 E-3";
              case 0x901A: return "Exp Series: 9.6414017 E-3";
              case 0x901F: return "Exp Series: 5.550513 E-2";
              case 0x9024: return "Exp Series: 2.402263 E-4";
              case 0x9029: return "Exp Series: 6.931471 E-1";
              case 0x902E: return "Exp Series: 1.00";
              case 0x9033: return "Evaluate <exp>";
              case 0x90D0: return "I/O Error Message";
              case 0x90D8: return "Basic 'open'";
              case 0x90DF: return "Basic 'chrout'";
              case 0x90E5: return "Basic 'input'";
              case 0x90EB: return "Redirect Output";
              case 0x90FD: return "Redirect Input";
              case 0x9112: return "Perform [save]";
              case 0x9129: return "Perform [verify]";
              case 0x912C: return "Perform [load]";
              case 0x918D: return "Perform [open]";
              case 0x919A: return "Perform [close]";
              case 0x91AE: return "Get Load/Save Parameters";
              case 0x91DD: return "Get Next Byte Value";
              case 0x91E3: return "Get Character or Abort";
              case 0x91EB: return "Move to Next Parameter";
              case 0x91F6: return "Get Open/Close Parameters";
              case 0x9243: return "Release I/O String";
              case 0x9251: return "Call 'status'";
              case 0x9257: return "Call 'setlfs'";
              case 0x925D: return "Call 'setnam'";
              case 0x9263: return "Call 'getin'";
              case 0x9269: return "Call 'chrout'";
              case 0x926F: return "Call 'clrchn'";
              case 0x9275: return "Call 'close'";
              case 0x927B: return "Call 'clall'";
              case 0x9281: return "Print Following Text";
              case 0x9287: return "Set Load/Save Bank";
              case 0x928D: return "Call 'plot'";
              case 0x9293: return "Call 'test stop'";
              case 0x9299: return "Make Room For String";
              case 0x92EA: return "Garbage Collection";
              case 0x9409: return "Evaluate <cos>";
              case 0x9410: return "Evaluate <sin>";
              case 0x9459: return "Evaluate <tan>";
              case 0x9485: return "Trig Series: 1.570796327 pi/2";
              case 0x948A: return "Trig Series: 6.28318531 pi*2";
              case 0x948F: return "Trig Series: 0.25";
              case 0x9494: return "Trig Series: #05 (counter)";
              case 0x9495: return "Trig Series: -14.3813907";
              case 0x949A: return "Trig Series: 42.0077971";
              case 0x949F: return "Trig Series: -76.7041703";
              case 0x94A4: return "Trig Series: 81.6052237";
              case 0x94A9: return "Trig Series: -41.3417021";
              case 0x94AE: return "Trig Series: 6.28318531";
              case 0x94B3: return "Evaluate <atn>";
              case 0x94E3: return "ATN Series: #0b (counter)";
              case 0x94E4: return "ATN Series: -0.000684793912";
              case 0x94E9: return "ATN Series:  0.00485094216";
              case 0x94EE: return "ATN Series: -0.161117018";
              case 0x94F3: return "ATN Series: 0.034209638";
              case 0x94F8: return "ATN Series: -0.0542791328";
              case 0x94FD: return "ATN Series: 0.0724571965";
              case 0x9502: return "ATN Series: -0.0898023954";
              case 0x9507: return "ATN Series: 0.110932413";
              case 0x950C: return "ATN Series: -0.142839808";
              case 0x9511: return "ATN Series: 0.19999912";
              case 0x9516: return "ATN Series: -0.333333316";
              case 0x951B: return "ATN Series: 1.00";
              case 0x9520: return "Print Using";
              case 0x99C1: return "Evaluate <instr>";
              case 0x9B0C: return "Evaluate <rdot>";
              case 0x9B30: return "Draw Line";
              case 0x9BFB: return "Plot Pixel";
              case 0x9C49: return "Examine Pixel";
              case 0x9C70: return "Set Hi-Res Color Cell";
              case 0x9CCA: return "Video Matrix Lines Hi";
              case 0x9CE3: return "Position Pixel";
              case 0x9D1C: return "Bit Masks	";
              case 0x9D24: return "Calc Hi-Res Row/Column";                            
              case 0x9D67: return "Calculate Graphics Coordinates"; 
              case 0x9D6D: return "Add Graphics Coordinate"; 
              case 0x9D7C: return "Subtract Graphics Coordinate"; 
              case 0x9D8F: return "Read Current X Position to A/Y"; 
              case 0x9DF2: return "Restore Pixel Cursor";
              case 0x9E06: return "Check Optional Float/Fixed Parameter"; 
              case 0x9E1C: return "Input Optional Byte Parameter/-Check Byte Parameter in List"; 
              case 0x9E2F: return "Parse Graphics Command"; 
              case 0x9E32: return "Get Color Source Param"; 
              case 0x9F25: return "Multicolor Pixel Masks"; 
              case 0x9F29: return "Conv Words Hi"; 
              case 0x9F3D: return "Conv Words Lo";
              case 0x9F4F: return "Allocate 9K Graphics Area for graphic/sprdef";
              case 0xA022: return "Move Basic to $1c01";
              case 0xA07E: return "Perform [catalog/directory]";
              case 0xA11D: return "Perform [dopen]";
              case 0xA134: return "Perform [append]";
              case 0xA157: return "Find Spare SA";
              case 0xA16F: return "Perform [dclose]";
              case 0xA18C: return "Perform [dsave]";
              case 0xA1A4: return "Perform [dverify]";
              case 0xA1A7: return "Perform [dload]";
              case 0xA1C8: return "Perform [bsave]";
              case 0xA218: return "Perform [bload]";
              case 0xA267: return "Perform [header]";
              case 0xA2A1: return "Perform [scratch]";
              case 0xA2D7: return "Perform [record]";
              case 0xA322: return "Perform [dclear]";
              case 0xA32F: return "Perform [collect]";
              case 0xA346: return "Perform [copy]";
              case 0xA362: return "Perform [concat]";
              case 0xA36E: return "Perform [rename]";
              case 0xA37C: return "Perform [backup]";
              case 0xA3B8: return "Default DOS Disk Unit (U8 D0)";
              case 0xA3BC: return "DOS Logical Address";
              case 0xA3BD: return "DOS Physical Address";
              case 0xA3BE: return "DOS Secondary Address";
              case 0xA3BF: return "Parse DOS Commands";
              case 0xA5E7: return "Print 'missing file name'";
              case 0xA5EA: return "Print 'illegal device number'";
              case 0xA5ED: return "Print 'string too long'";
              case 0xA627: return "DOS Command Masks	";
              case 0xA667: return "Set DOS Parameters";
              case 0xA7E1: return "Print 'are you sure'";
              case 0xA7E8: return "'are you sure?'";
              case 0xA80D: return "Release String";
              case 0xA82A: return "'key 0,'";
              case 0xA845: return "Set Bank 15";
              case 0xA84D: return "IRQ Work";
              case 0xAA1F: return "Perform [stash]";
              case 0xAA24: return "Perform [fetch]";
              case 0xAA29: return "Perform [swap]";
              case 0xAA6E: return "Patch for Print Using";
              case 0xAA81: return "Unused";
              case 0xAE64: return "Encrypted Message";
              
              case 0xAF00:
              case 0xAF01:
              case 0xAF02: return "Convert F.P. to Integer";
              case 0xAF03:
              case 0xAF04:
              case 0xAF05: return "Convert Integer to F.P.";
              case 0xAF06:
              case 0xAF07:
              case 0xAF08: return "Convert F.P. to ASCII String";
              case 0xAF09:
              case 0xAF0A:
              case 0xAF0B: return "Convert ASCII String to F.P.";     
              case 0xAF0C:
              case 0xAF0D:
              case 0xAF0E: return "Convert F.P. to an Address";   
              case 0xAF0F:
              case 0xAF10:
              case 0xAF11: return "Convert Address to F.P.";   
              case 0xAF12:
              case 0xAF13:
              case 0xAF14: return "MEM - FACC";
              case 0xAF15:
              case 0xAF16:
              case 0xAF17: return "ARG - FACC";
              case 0xAF18:
              case 0xAF19:
              case 0xAF1A: return "MEM + FACC";              
              case 0xAF1B:
              case 0xAF1C:
              case 0xAF1D: return "ARG - FACC";
              case 0xAF1E:
              case 0xAF1F:
              case 0xAF20: return "MEM * FACC";
              case 0xAF21:
              case 0xAF22: 
              case 0xAF23: return "ARG * FACC";
              case 0xAF24:
              case 0xAF25:
              case 0xAF26: return "MEM / FACC";
              case 0xAF27:
              case 0xAF28:
              case 0xAF29: return "ARG / FACC";
              case 0xAF2A: 
              case 0xAF2B:
              case 0xAF2C: return "Compute Natural LOG Of FACC";   
              case 0xAF2D:
              case 0xAF2E:     
              case 0xAF2F: return "Perform BASIC INT On FACC";     
              case 0xAF30:
              case 0xAF31:     
              case 0xAF32: return "Compute Square Root OF FACC";
              case 0xAF33:
              case 0xAF34:     
              case 0xAF35: return "Negate FACC";
              case 0xAF36:
              case 0xAF37:     
              case 0xAF38: return "Raise ARG to The Mem Power";   
              case 0xAF39:
              case 0xAF3A:     
              case 0xAF3B: return "Raise ARG to The FACC Power"; 
              case 0xAF3C:
              case 0xAF3D:     
              case 0xAF3E: return "Compute EXP Of FACC"; 
              case 0xAF3F:
              case 0xAF40:     
              case 0xAF41: return "Compute COS Of FACC";   
              case 0xAF42:
              case 0xAF43:     
              case 0xAF44: return "Compute SIN Of FACC";
              case 0xAF45:
              case 0xAF46:     
              case 0xAF47: return "Compute TAN Of FACC";
              case 0xAF48:
              case 0xAF49:     
              case 0xAF4A: return "Compute ATN Of FACC";
              case 0xAF4B:
              case 0xAF4C:     
              case 0xAF4D: return "Round FACC";   
              case 0xAF4E:
              case 0xAF4F:     
              case 0xAF50: return "Absolute Value Of FACC";
              case 0xAF51:
              case 0xAF52:     
              case 0xAF53: return "Test Sign Of FACC";              
              case 0xAF54:
              case 0xAF55:     
              case 0xAF56: return "Compare FACC With Memory";
              case 0xAF57:
              case 0xAF58:     
              case 0xAF59: return "Generate Random F.P. Number";     
              case 0xAF5A:
              case 0xAF5B:     
              case 0xAF5C: return "Move RAM MEM to ARG";      
              case 0xAF5D:
              case 0xAF5E:     
              case 0xAF5F: return "Move ROM MEM to ARG"; 
              case 0xAF60:
              case 0xAF61:     
              case 0xAF62: return "Move RAM MEM to FACC"; 
              case 0xAF63:
              case 0xAF64:     
              case 0xAF65: return "Move ROM MEM to FACC";               
              case 0xAF66:
              case 0xAF67:     
              case 0xAF68: return "Move FACC to MEM";               
              case 0xAF69:
              case 0xAF6A:     
              case 0xAF6B: return "Move ARG to FACC";    
              case 0xAF6C:
              case 0xAF6D:     
              case 0xAF6E: return "Move FACC to ARG";
              case 0xAFA8: return "Unused";
              
              case 0xB000:
              case 0xB001:     
              case 0xB002: return "MONITOR Call Entry";
              case 0xB003:
              case 0xB004:     
              case 0xB005: return "MONITOR Break Entry";
              case 0xB006:
              case 0xB007:     
              case 0xB008: return "MONITOR Command Parser Entry";
              case 0xB009: return "Print 'break'";
              case 0xB00C: return "'break'";
              case 0xB021: return "Print 'call' entry";
              case 0xB03A: return "Print 'monitor'";
              case 0xB03D: return "'monitor'";
              case 0xB050: return "Perform [r]/Print 'pc sr ac xr yr sp'";
              case 0xB053: return "'pc sr ac xr yr sp'";
              case 0xB08D: return "Get Command";
              case 0xB0BC: return "Error";
              case 0xB0BF: return "'?'";
              case 0xB0E3: return "Perform [x]";
              case 0xB0E6: return "Commands";
              case 0xB0FC: return "Vectors";
              case 0xB11A: return "Read Banked Memory";
              case 0xB12A: return "Write Banked Memory";
              case 0xB13D: return "Compare Banked Memory";
              case 0xB152: return "Perform [m]";
              case 0xB194: return "Perform [:]";
              case 0xB1AB: return "Perform [>]";
              case 0xB1C9: return "Print 'esc-o-up'";
              case 0xB1CC: return "'esc-o-up'";
              case 0xB1D6: return "Perform [g]";
              case 0xB1DF: return "Perform [j]";
              case 0xB1E8: return "Display Memory";
              case 0xB20B: return "Print ':<rvs-on>'";
              case 0xB20E: return "':<rvs-on>'";
              case 0xB231: return "Perform [c]";
              case 0xB234: return "Perform [t]";
              case 0xB2C3: return "Add 1 to Op 3";
              case 0xB2C6: return "Do Next Address";
              case 0xB2CE: return "Perform [h]";
              case 0xB337: return "Perform [lsv]";
              case 0xB3C4: return "Print 'error'";
              case 0xB3DB: return "Perform [f]";
              case 0xB406: return "Perform [a]";
              case 0xB533: return "Print 'space<esc-q>'";
              case 0xB57C: return "Check 2 A-Matches";
              case 0xB57F: return "Check A-Match";
              case 0xB58B: return "Try Next Op Code";
              case 0xB599: return "Perform [d]";
              case 0xB5AE: return "Print '<cr><esc-q>'";
              case 0xB5B1: return "'<cr><esc-q>'";
              case 0xB5D4: return "Display Instruction";
              case 0xB5F2: return "Print '<3 spaces>'";
              case 0xB5F5: return "'<3 spaces>'";
              case 0xB659: return "Classify Op Code";
              case 0xB6A1: return "Get Mnemonic Char";
              case 0xB6C3: return "Mode Tables";
              case 0xB715: return "Mode Characters";
              case 0xB721: return "Compacted Mnemonics";
              case 0xB7A5: return "Input Parameter";
              case 0xB7CE: return "Read Value";
              case 0xB88A: return "Number Bases";
              case 0xB88E: return "Base Bits";
              case 0xB892: return "Display 5-Digit Address";
              case 0xB8A5: return "Display 2-digit Byte";
              case 0xB8A8: return "Print Space";
              case 0xB8AD: return "Print Cursor-Up";
              case 0xB8B4: return "New Line";
              case 0xB8B9: return "Blank New Line";
              case 0xB8C2: return "Output 2-Digit Byte";
              case 0xB8D2: return "Byte to 2 Ascii";
              case 0xB8E7: return "Get Input Char";
              case 0xB8E9: return "Get Character";
              case 0xB901: return "Copy Add0 to Add2";
              case 0xB90E: return "Calculate Add2 - Add0";
              case 0xB922: return "Subtract Add0";
              case 0xB93C: return "Subtract Add1";
              case 0xB950: return "Increment Pointer";
              case 0xB960: return "Decrement Pointer";
              case 0xB874: return "Copy to Register Area";
              case 0xB983: return "Calculate Step/Area";
              case 0xB9B1: return "Perform [$+&%]";
              case 0xBA07: return "Convert to Decimal";
              case 0xBA47: return "Transfer Address";
              case 0xBA5D: return "Output Address";
              case 0xBA90: return "Perform [@]";
              case 0xBB72: return "Unused";
              case 0xBFC0: return "Copyright Banner";
              case 0xBFFC: return "Checksum (?)";
              
              case 0xC000:
              case 0xC001:     
              case 0xC002: return "Initialize Editor & Screen";              
              case 0xC003:
              case 0xC004:     
              case 0xC005: return "Display Charac in .A, Color";        
              case 0xC006:
              case 0xC007:     
              case 0xC008: return "Get Key From IRQ Buffer";
              case 0xC009:
              case 0xC00A:     
              case 0xC00B: return "Into A";
              case 0xC00C:
              case 0xC00D:     
              case 0xC00E: return "Print Character In .A";
              case 0xC00F:
              case 0xC010:     
              case 0xC011: return "Get # of Scrn Rows, Cols Into X & Y";    
              case 0xC012:
              case 0xC013:     
              case 0xC014: return "Scan Keyboard Subroutine"; 
              case 0xC015:
              case 0xC016:     
              case 0xC017: return "Handle Repeat Key & Store Decoded Key"; 
              case 0xC018:
              case 0xC019:     
              case 0xC01A: return "Read Or Set CRSR Position In X, Y"; 
              case 0xC01B:
              case 0xC01C:     
              case 0xC01D: return "Move 8563 Cursor Subroutine";
              case 0xC01E:
              case 0xC01F:     
              case 0xC020: return "Execute ESC Function using chr in .A";
              case 0xC021:
              case 0xC022:     
              case 0xC023: return "Redefine A Programmable Func'n Key";     
              case 0xC024:
              case 0xC025:     
              case 0xC026: return "IRQ Entry";   
              case 0xC027:
              case 0xC028:     
              case 0xC029: return "Initialize 80-Column Character Set";   
              case 0xC02A:
              case 0xC02B:     
              case 0xC02C: return "Swap Editor Locals (in 40/80 change)";
              case 0xC02D:
              case 0xC02E:     
              case 0xC02F: return "Set Top-Left or Bot-Right of Window";
              case 0xC033: return "Screen Address Low";
              case 0xC04C: return "Screen Address High";
              case 0xC065: return "I/O Link Vectors";
              case 0xC06F: return "Keyboard Shift Vectors";
              case 0xC07B: return "Initialize Screen";
              case 0xC142: return "Reset Window";
              case 0xC150: return "Home Cursor";
              case 0xC156: return "Goto Left Border";
              case 0xC15C: return "Set Up New Line";
              case 0xC17C: return "Do Screen Color";
              case 0xC194: return "(IRQ) Split Screen";
              case 0xC234: return "Get a Key";
              case 0xC258: return "Screen Line Editor";
              case 0xC29B: return "Input From Screen";
              case 0xC2BC: return "Read Screen Char";
              case 0xC2FF: return "Check For Quotes";
              case 0xC30C: return "Wrap Up Screen Print";
              case 0xC320: return "Ascii to Screen Code";
              case 0xC33E: return "Check Cursor Range";
              case 0xC363: return "Do New Line";
              case 0xC37C: return "Insert a Line";
              case 0xC3A6: return "Scroll Screen";
              case 0xC3DC: return "Delete a Line";
              case 0xC40D: return "Move Screen Line";
              case 0xC4A5: return "Clear a Line";
              case 0xC53C: return "Set 80-Column Counter to 1";
              case 0xC53E: return "Set 80-Column Counter";
              case 0xC55D: return "Keyboard Scan Subroutine";
              case 0xC651: return "Key Pickup & Repeat";
              case 0xC6DD: return "Keycodes for Programmed Keys";
              case 0xC6E7: return "Flash 40 Column Cursor";
              case 0xC72D: return "Print to Screen";
              case 0xC77D: return "Esc-o (escape)";
              case 0xC78C: return "Control Characters";
              case 0xC79A: return "Control Character Vectors";
              case 0xC7B6: return "Print Control Char";
              case 0xC802: return "Print Hi-Bit Char";
              case 0xC854: return "Chr$(29) Cursor Right";
              case 0xC85A: return "Chr$(17) Cursor Down";
              case 0xC875: return "Chr$(157) Cursor Left";
              case 0xC880: return "Chr$(14) Text Mode";
              case 0xC8A6: return "Chr$(11) Lock";
              case 0xC8AC: return "Chr$(12) Unlock";
              case 0xC8B3: return "Chr$(19) Home";
              case 0xC8BF: return "Chr$(146) Clear Rvs Mode";
              case 0xC8C2: return "Chr$(18) Reverse";
              case 0xC8C7: return "Chr$(2) Underline On";
              case 0xC8CE: return "Chr$(130) Underline Off";
              case 0xC8D5: return "Chr$(15) Flash On";
              case 0xC8DC: return "Chr$(143) Flash Off";
              case 0xC8E3: return "Open Screen Space";
              case 0xC91B: return "Chr$(20) Delete";
              case 0xC932: return "Restore Cursor";
              case 0xC94F: return "Chr$(9) Tab";
              case 0xC961: return "Chr$(24) Tab Toggle";
              case 0xC96C: return "Find Tab Column";
              case 0xC980: return "Esc-z Clear All Tabs";
              case 0xC983: return "Esc-y Set Default Tabs";
              case 0xC98E: return "Chr$(7) Bell";
              case 0xC9B1: return "Chr$(10) Linefeed";
              case 0xC9BE: return "Analyze Esc Sequence";
              case 0xC9DE: return "Esc Sequence Vectors";
              case 0xCA14: return "Esc-t Top";
              case 0xCA16: return "Esc-b Bottom";
              case 0xCA1B: return "Set Window Part";
              case 0xCA24: return "Exit Window";
              case 0xC052: return "Esc-d Delete Line";
              case 0xC076: return "Esc-q Erase End";
              case 0xC08B: return "Esc-p Erase Begin";
              case 0xCA9F: return "Esc-@ Clear Remainder of Screen";
              case 0xCABC: return "Esc-v Scroll Up";
              case 0xCACA: return "Esc-w Scroll Down";
              case 0xCAE2: return "Esc-l Scroll On";
              case 0xCAE5: return "Esc-m Scroll Off";
              case 0xCAEA: return "Esc-c Cancel Auto Insert";
              case 0xCAED: return "Esc-a Auto Insert";
              case 0xCAF2: return "Esc-s Block Cursor";
              case 0xCAFE: return "Esc-u Underline Cursor";
              case 0xCB0B: return "Esc-e Cursor Non Flash";
              case 0xCB21: return "Esc-f Cursor Flash";
              case 0xCB37: return "Esc-g Bell Enable";
              case 0xCB3A: return "Esc-h Bell Disable";
              case 0xCB3F: return "Esc-r Screen Reverse";
              case 0xCB48: return "Esc-n Screen Normal";
              case 0xCB52: return "Esc-k End-of-Line";
              case 0xCB58: return "Get Screen Char/Color";
              case 0xCB74: return "Check Screen Line of Location";
              case 0xCB81: return "Extend/Trim Screen Line";
              case 0xCB9F: return "Set Up Line Masks";
              case 0xCBB1: return "Esc-j Start-of-Line";
              case 0xCBC3: return "Find End-of-Line";
              case 0xCBED: return "Move Cursor Right";
              case 0xCC00: return "Move Cursor Left";
              case 0xCC1E: return "Save Cursor";
              case 0xCC27: return "Print Space";
              case 0xCC2F: return "Print Character";
              case 0xCC32: return "Print Fill Color";
              case 0xCC34: return "Put Char to Screen";
              case 0xCC5B: return "Get Rows/Columns";
              case 0xCC6A: return "Read/Set Cursor";
              case 0xCCA2: return "Define Function Key";
              case 0xCD2D: return "Esc-x Switch 40/80 Col";
              case 0xCD57: return "Position 80-col Cursor";
              case 0xCD6F: return "Set Screen Color";
              case 0xCD9F: return "Turn Cursor On";
              case 0xCDCA: return "Set CRTC Register 31";
              case 0xCDCC: return "Set CRTC Register";
              case 0xCDD8: return "Read CRTC Register 31";
              case 0xCDDA: return "Read CRTC Register";
              case 0xCDE6: return "Set CRTC to Screen Address";
              case 0xCDF9: return "Set CRTC to Color Address";
              case 0xCE0C: return "Set Up 80 Column Char Set";
              case 0xCE4C: return "Ascii Color Codes";
              case 0xCE5C: return "System Color Codes";
              case 0xCE6C: return "Bit Masks";
              case 0xCE74: return "40-col Init Values ($e0)";
              case 0xCE8E: return "80-col Init Values ($0a40)";
              case 0xCE8A: return "Prog Key Lengths";
              case 0xCEB2: return "Prog Key Definitions";
              case 0xCEF5: return "Unused";
              
              case 0xD000: return "Position X sprite 0";
              case 0xD001: return "Position Y sprite 0";
              case 0xD002: return "Position X sprite 1";
              case 0xD003: return "Position Y sprite 1";
              case 0xD004: return "Position X sprite 2";
              case 0xD005: return "Position Y sprite 2";
              case 0xD006: return "Position X sprite 3";
              case 0xD007: return "Position Y sprite 3";
              case 0xD008: return "Position X sprite 4";
              case 0xD009: return "Position Y sprite 4";
              case 0xD00A: return "Position X sprite 5";
              case 0xD00B: return "Position Y sprite 5";
              case 0xD00C: return "Position X sprite 6";
              case 0xD00D: return "Position Y sprite 6";
              case 0xD00E: return "Position X sprite 7";
              case 0xD00F: return "Position Y sprite 7";
              case 0xD010: return "Position X MSB sprites 0..7";
              case 0xD011: return "VIC control register";
              case 0xD012: return "Reading/Writing IRQ balance value";
              case 0xD013: return "Positin X of optic pencil \"latch\"";
              case 0xD014: return "Positin Y of optic pencil \"latch\"";
              case 0xD015: return "Sprites Abilitator";
              case 0xD016: return "VIC control register";
              case 0xD017: return "(2X) vertical expansion (Y) sprite 0..7";
              case 0xD018: return "VIC memory control register";
              case 0xD019: return "Interrupt indicator register";
              case 0xD01A: return "IRQ mask register";
              case 0xD01B: return "Sprite-background screen priority";
              case 0xD01C: return "Set multicolor mode for sprite 0..7";
              case 0xD01D: return "(2X) horizontal expansion (X) sprite 0..7";
              case 0xD01E: return "Animations contact";
              case 0xD01F: return "Animation/background contact";
              case 0xD020: return "Border color";
              case 0xD021: return "Background 0 color";
              case 0xD022: return "Background 1 color";
              case 0xD023: return "Background 2 color";
              case 0xD024: return "Background 3 color";
              case 0xD025: return "Multicolor animation 0 register";
              case 0xD026: return "Multicolor animation 1 register";
              case 0xD027: return "Color sprite 0";
              case 0xD028: return "Color sprite 1";
              case 0xD029: return "Color sprite 2";
              case 0xD02A: return "Color sprite 3";
              case 0xD02B: return "Color sprite 4";
              case 0xD02C: return "Color sprite 5";
              case 0xD02D: return "Color sprite 6";
              case 0xD02E: return "Color sprite 7";

              case 0xD400: return "Voice 1: Frequency control (lo byte)";
              case 0xD401: return "Voice 1: Frequency control (hi byte)";
              case 0xD402: return "Voice 1: Wave form pulsation amplitude (lo byte)";
              case 0xD403: return "Voice 1: Wave form pulsation amplitude (hi byte)";
              case 0xD404: return "Voice 1: Control registers";
              case 0xD405: return "Generator 1: Attack/Decay";
              case 0xD406: return "Generator 1: Sustain/Release";
              case 0xD407: return "Voice 2: Frequency control (lo byte)";
              case 0xD408: return "Voice 2: Frequency control (hi byte)";
              case 0xD409: return "Voice 2: Wave form pulsation amplitude (lo byte)";
              case 0xD40A: return "Voice 2: Wave form pulsation amplitude (hi byte)";
              case 0xD40B: return "Voice 2: Control registers";
              case 0xD40C: return "Generator 2: Attack/Decay";
              case 0xD40D: return "Generator 2: Sustain/Release";
              case 0xD40E: return "Voice 3: Frequency control (lo byte)";
              case 0xD40F: return "Voice 3: Frequency control (hi byte)";
              case 0xD410: return "Voice 3: Wave form pulsation amplitude (lo byte)";
              case 0xD411: return "Voice 3: Wave form pulsation amplitude (hi byte)";
              case 0xD412: return "Voice 3: Control registers";
              case 0xD413: return "Generator 3: Attack/Decay";
              case 0xD414: return "Generator 3: Sustain/Release";
              case 0xD415: return "Filter cut frequency: lo byte (bit 2-0)";
              case 0xD416: return "Filter cut frequency: hi byte";
              case 0xD417: return "Filter resonance control/voice input control";
              case 0xD418: return "Select volume and filter mode";
              case 0xD419: return "Analog/digital converter: Paddle 1";
              case 0xD41A: return "Analog/digital converter: Paddle 2";
              case 0xD41B: return "Random numbers generator oscillator 3";
              case 0xD41C: return "Generator output";
              
              case 0xD500: return "Configuration Register (CR)";
              case 0xD501: return "Preconfiguration Registers";
              case 0xD505: return "Modality register";
              case 0xD506: return "RAM Configuration Register (RCR)";
              case 0xD507: return "Zero Page Pointer Lo";
              case 0xD508: return "Zero Page Pointer Hi";
              case 0xD509: return "Stack Page Pointer Lo";
              case 0xD50A: return "Stack Page Pointer Hi";
              case 0xD50B: return "MMU Version Register";
              
              case 0xD600: return "VDC 8563: Horizontal Total";
              case 0xD601: return "VDC 8563: Horizontal Displayed";
              case 0xD602: return "VDC 8563: Horizontal Sync Position";
              case 0xD603: return "VDC 8563: Vert/Horiz. Sync Width";
              case 0xD604: return "VDC 8563: Vertical Total";
              case 0xD605: return "VDC 8563: Vertical Total Fine Adju";
              case 0xD606: return "VDC 8563: Vertical Displayed";
              case 0xD607: return "VDC 8563: Vertical Sync Position";
              case 0xD608: return "VDC 8563: Interlace Mode";
              case 0xD609: return "VDC 8563: Character Total Vertical";
              case 0xD60A: return "VDC 8563: Cursor Mode/ Start Scan";
              case 0xD60B: return "VDC 8563: Cursor End Scan";
              case 0xD60C: return "VDC 8563: Display Start Adrs (Hi)";
              case 0xD60D: return "VDC 8563: Display Start Adrs (Lo)";
              case 0xD60E: return "VDC 8563: Cursor Position (Hi)";
              case 0xD60F: return "VDC 8563: Cursor Position (Lo)";
              case 0xD610: return "VDC 8563: Light Pen Veritcal";
              case 0xD611: return "VDC 8563: Light Pen Horizontal";
              case 0xD612: return "VDC 8563: Update Address (Hi)";
              case 0xD613: return "VDC 8563: Update Address (Lo)";
              case 0xD614: return "VDC 8563: Attribute Start Adrs (Hi)";
              case 0xD615: return "VDC 8563: Attribute Start Adrs (Lo)";
              case 0xD616: return "VDC 8563: Hz Chr Pxl Ttl/IChar Spc ";
              case 0xD617: return "VDC 8563: Vert. Character Pxl Spc";
              case 0xD618: return "VDC 8563: Block/Rvs Scr/V. Scroll";
              case 0xD619: return "VDC 8563: Diff. Mode Sw/H. Scroll";
              case 0xD61A: return "VDC 8563: ForeGround/BackGround Col";
              case 0xD61B: return "VDC 8563: Row/Adrs. Incremen";
              case 0xD61C: return "VDC 8563: Character Set Addrs/Ram";
              case 0xD61D: return "VDC 8563: Underline Scan Line";
              case 0xD61E: return "VDC 8563: Word Count (-1)";
              case 0xD61F: return "VDC 8563: Data";
              case 0xD620: return "VDC 8563: Block Copy Source (hi)";
              case 0xD621: return "VDC 8563: Block Copy Source (lo)";
              case 0xD622: return "VDC 8563: Display Enable Begin";
              case 0xD623: return "VDC 8563: Display Enable End";
              case 0xD624: return "VDC 8563: DRAM Refresh Rate";
              
              case 0xDC00: return "Data port A #1: keyboard, joystick, paddle, optical pencil";
              case 0xDC01: return "Data port B #1: keyboard, joystick, paddle";
              case 0xDC02: return "Data direction register port A #1";
              case 0xDC03: return "Data direction register port B #1";
              case 0xDC04: return "Timer A #1: Lo Byte";
              case 0xDC05: return "Timer A #1: Hi Byte";
              case 0xDC06: return "Timer B #1: Lo Byte";
              case 0xDC07: return "Timer B #1: Hi Byte";
              case 0xDC08: return "Day time clock #1: 1/10 second";
              case 0xDC09: return "Day time clock #1: Second";
              case 0xDC0A: return "Day time clock #1: Minutes";
              case 0xDC0B: return "Day time clock #1: Hour+[indicator AM/PM]";
              case 0xDC0C: return "Serial I/O data buffer synchronous #1";
              case 0xDC0D: return "Interrupt control register CIA #1";
              case 0xDC0E: return "Control register A of CIA #1";
              case 0xDC0F: return "Control register B of CIA #1";

              case 0xDD00: return "Data port A #2: serial bus, RS-232, VIC memory";
              case 0xDD01: return "Data port B #2: user port, RS-232";
              case 0xDD02: return "Data direction register port A #2";
              case 0xDD03: return "Data direction register port A #2";
              case 0xDD04: return "Timer A #2: Lo Byte";
              case 0xDD05: return "Timer A #2: Hi Byte";
              case 0xDD06: return "Timer B #2: Lo Byte";
              case 0xDD07: return "Timer B #2: HI Byte";
              case 0xDD08: return "Day time clock #2: 1/10 second";
              case 0xDD09: return "Day time clock #2: seconds";
              case 0xDD0A: return "Day time clock #2: minutes";
              case 0xDD0B: return "Day time clock #2: Hour+[indicator AM/PM]";
              case 0xDD0C: return "Serial I/O data buffer synchronous #2";
              case 0xDD0D: return "Interrupt control register CIA #2";
              case 0xDD0E: return "Control register A of CIA #2";
              case 0xDD0F: return "Control register B of CIA #2";
              
              case 0xDF00: return "8726 DMA Controller: STATUS";
              case 0xDF01: return "8726 DMA Controller: COMMAND";
              case 0xDF02: return "8726 DMA Controller: HOST ADDRESS LOW";
              case 0xDF03: return "8726 DMA Controller: HOST ADDRESS HIGH";
              case 0xDF04: return "8726 DMA Controller: EXPANSION ADDRESS LOW";
              case 0xDF05: return "8726 DMA Controller: EXPANSION ADDRESS HIGH";
              case 0xDF06: return "8726 DMA Controller: EXPANSION BANK";
              case 0xDF07: return "8726 DMA Controller: TRANSFER LENGTH LOW";
              case 0xDF08: return "8726 DMA Controller: TRANSFER LENGTH HIGH";
              case 0xDF09: return "8726 DMA Controller: INTERRUPT MASK REGISTER";
              case 0xDF0A: return "8726 DMA Controller: VERSION, MAXIMUM MEMORY";
              
              case 0xE000: return "Reset Code";
              case 0xE04B: return "MMU Set Up Bytes";
              case 0xE056: return "-restor-";
              case 0xE05B: return "-vector-";
              case 0xE073: return "Vectors to $0314";
              case 0xE093: return "-ramtas-";
              case 0xE0CD: return "Move Code For High RAM Banks";
              case 0xE105: return "RAM Bank Masks";
              case 0xE109: return "-init-";
              case 0xE1DC: return "Set Up CRTC Registers";
              case 0xE1F0: return "Check Special Reset";
              case 0xE242: return "Reset to 64/128";
              case 0xE24B: return "Switch to 64 Mode";
              case 0xE263: return "Code to $02";
              case 0xE26B: return "Scan All ROMs";
              case 0xE2BC: return "ROM Addresses High";
              case 0xE2C0: return "ROM Banks";
              case 0xE2C4: return "Print 'cbm' Mask";
              case 0xE2C7: return "VIC 8564 Set Up";
              case 0xE2F8: return "CRTC 8563 Set Up Pairs";
              case 0xE33B: return "-talk-";
              case 0xE33E: return "-listen-";
              case 0xE38C: return "Send Data On Serial Bus";
              case 0xE439: return "-acptr-";
              case 0xE4D2: return "-second-";
              case 0xE4E0: return "-tksa-";
              case 0xE503: return "ciout- Print Serial";
              case 0xE515: return "-untlk-";
              case 0xE526: return "-unlsn-";
              case 0xE535: return "Reset ATN";
              case 0xE545: return "Set Clock High";
              case 0xE54E: return "Set Clock Low";
              case 0xE557: return "Set Data High";
              case 0xE560: return "Set Data Low";
              case 0xE569: return "Read Serial Lines";
              case 0xE573: return "Stabilize Timing";
              case 0xE59F: return "Restore Timing";
              case 0xE5BC: return "Prepare For Response";
              case 0xE5C3: return "Fast Disk Off";
              case 0xE5D6: return "Fast Disk On";
              case 0xE5FB: return "Fast Disk On/Off";
              case 0xE5FF: return "(NMI) Transmit RS-232";
              case 0xE64A: return "RS-232 Handshake";
              case 0xE68E: return "Set RS-232 Bit Count";
              case 0xE69D: return "(NMI) RS-232 Receive";
              case 0xE75F: return "Send To RS-232";
              case 0xE795: return "Connect RS-232 Input";
              case 0xE7CE: return "Get From RS-232";
              case 0xE7EC: return "Interlock RS-232/Serial";
              case 0xE805: return "(NMI) RS-232 Control I/O";
              case 0xE850: return "RS-232 Timing Table -- NTSC";
              case 0xE864: return "RS-232 Timing Table -- PAL";
              case 0xE878: return "(NMI) RS-232 Receive Timing";
              case 0xE8A9: return "(NMI) RS-232 Transmit Timing";
              case 0xE8D0: return "Find Any Tape Header";
              case 0xE919: return "Write Tape Header";
              case 0xE980: return "Get Buffer Address";
              case 0xE987: return "Get Tape Buffer Start & End Addrs";
              case 0xE99A: return "Find Specific Header";
              case 0xE9BE: return "Bump Tape Pointer";
              case 0xE9C8: return "Print 'press play on tape'";
              case 0xE9DF: return "Check Tape Status";
              case 0xE9E9: return "Print 'press record ...'";
              case 0xE9F2: return "Initiate Tape Read";
              case 0xEA15: return "Initiate Tape Write";
              case 0xEA26: return "Common Tape Code";
              case 0xEA7D: return "Wait For Tape";
              case 0xEA8F: return "Check Tape Stop";
              case 0xEAA1: return "Set Read Timing";
              case 0xEAEB: return "(IRQ) Read Tape Bits";
              case 0xEC1F: return "Store Tape Chars";
              case 0xED51: return "Reset Pointer";
              case 0xED5A: return "New Char Set Up";
              case 0xED69: return "Write Transition to Tape";
              case 0xED8B: return "Write Data to Tape";
              case 0xED90: return "(IRQ) Tape Write";
              case 0xEE2E: return "(IRQ) Tape Leader";
              case 0xEE57: return "Wind Up Tape I/O";
              case 0xEE9B: return "Switch IRQ Vector";
              case 0xEEA8: return "IRQ Vectors";
              case 0xEEB0: return "Kill Tape Motor";
              case 0xEEB7: return "Check End Address";
              case 0xEEC1: return "Bump Address";
              case 0xEEC8: return "(IRQ) Clear Break";
              case 0xEED0: return "Control Tape Motor";
              case 0xEEEB: return "-getin-";
              case 0xEF06: return "-chrin-";
              case 0xEF48: return "Get Char From Tape";
              case 0xEF79: return "-chrout-";
              case 0xEFBD: return "-open-";
              case 0xF0B0: return "Set CIA to RS-232";
              case 0xF0CB: return "Check Serial Open";
              case 0xF106: return "-chkin-";
              case 0xF14C: return "-chkout-";
              case 0xF188: return "-close-";
              case 0xF1E4: return "Delete File";
              case 0xF202: return "Search For File";
              case 0xF212: return "Set File Parameters";
              case 0xF222: return "-clall-";
              case 0xF226: return "-clrchn-";
              case 0xF23D: return "Clear I/O Path";
              case 0xF265: return "-load-";
              case 0xF27B: return "Serial Load";
              case 0xF32A: return "Tape Load";
              case 0xF3A1: return "Disk Load";
              case 0xF3EA: return "Burst Load";
              case 0xF48C: return "Close Off Serial";
              case 0xF4BA: return "Get Serial Byte";
              case 0xF4C5: return "Receive Serial Byte";
              case 0xF503: return "Toggle Clock Line";
              case 0xF50C: return "Print 'u0' Disk Reset";
              case 0xF50F: return "Print 'searching'";
              case 0xF521: return "Send File Name";
              case 0xF533: return "Print 'loading'";
              case 0xF53E: return "-save-";
              case 0xF5B5: return "Terminate Serial Input";
              case 0xF5BC: return "Print 'saving'";
              case 0xF5C8: return "Save to Tape";
              case 0xF5F8: return "-udtim-";
              case 0xF63D: return "Watch For RUN or Shift";
              case 0xF65E: return "-rdtim-";
              case 0xF665: return "-settim-";
              case 0xF66E: return "-stop-";
              case 0xF67C: return "Print 'too many files'";
              case 0xF67F: return "Print 'file open'";
              case 0xF682: return "Print 'file not open'";
              case 0xF685: return "Print 'file not found'";
              case 0xF688: return "Print 'device not present'";
              case 0xF68B: return "Print 'not input file'";
              case 0xF68E: return "Print 'not output file'";
              case 0xF691: return "Print 'missing file name'";
              case 0xF694: return "Print 'illegal device no'";
              case 0xF697: return "Error #0";
              case 0xF6B0: return "Messages";
              case 0xF71E: return "Print If Direct";
              case 0xF722: return "Print I/O Message";
              case 0xF731: return "-setnam-";
              case 0xF738: return "-setlfs-";
              case 0xF73F: return "Set Load/Save Bank";
              case 0xF744: return "-rdst-";
              case 0xF757: return "Set Status Bit";
              case 0xF75C: return "-setmsg-";
              case 0xF75F: return "Set Serial Timeout";
              case 0xF763: return "-memtop-";
              case 0xF772: return "-membot-";
              case 0xF781: return "-iobase-";
              case 0xF786: return "Search For SA";
              case 0xF79D: return "Search & Set Up File";
              case 0xF7A5: return "Trigger DMA";
              case 0xF7AE: return "Get Char From Memory";
              case 0xF7BC: return "Store Loaded Byte";
              case 0xF7C9: return "Read Byte to be Saved";
              case 0xF7D0: return "Get Char From Memory Bank";
              case 0xF7DA: return "Store Char to Memory Bank";
              case 0xF7E3: return "Compare Char With Memory Bank";
              case 0xF7F0: return "MMU Bank Configuration Values";
              case 0xF800: return "Subroutines to $02a2-$02fb";
              case 0xF85A: return "DMA Code to $03f0";
              case 0xF867: return "Check Auto Start ROM";
              case 0xF890: return "Check For Boot Disk";
              case 0xF908: return "Print 'booting'";
              case 0xF92C: return "Print '...'";
              case 0xF98B: return "Wind Up Disk Boot";
              case 0xF9B3: return "Read Next Boot Block";
              case 0xF9FB: return "To 2-Digit Decimal";
              case 0xFA08: return "Block Read Command String";
              case 0xFa17: return "Print a Message";
              case 0xFA40: return "NMI Sequence";
              case 0xFA65: return "(IRQ) Normal Entry";
              case 0xFA80: return "Keyboard Matrix Un-Shifted";
              case 0xFAD9: return "Keyboard Matrix Shifted";
              case 0xFB32: return "Keyboard Matrix C-Key";
              case 0xFB8B: return "Keyboard Matrix Control";
              case 0xFBE4: return "Keyboard Matrix Caps-Loc";
              case 0xFC62: return "Patch for Set Up CRTC Registers";
              case 0xFC6F: return "Unused";
              case 0xFC87: return "Init KBD Translation Tables";
              case 0xFCC6: return "DIN Keyboard patch for Key Pickup";
              case 0xFD29: return "DIN Keyboard Matrix Un-Shifted";
              case 0xFD82: return "DIN Keyboard Matrix Shifted";
              case 0xFDDB: return "DIN Keyboard Matrix C-Key";
              case 0xFE34: return "DIN Keyboard Shift Vectors";
              case 0xFE8C: return "Unused";
              case 0xFEFF: return "Patch byte";
              case 0xFEF0: return "MMU Configuration Register";
              case 0xFF01: return "MMU LCR: Bank 0";
              case 0xFF02: return "MMU LCR: Bank 1";
              case 0xFF03: return "MMU LCR: Bank 14";
              case 0xFF04: return "MMU LCR: Bank 14 Over RAM 1";
              case 0xFF05: return "NMI Transfer Entry";
              case 0xFF17: return "IRQ Transfer Entry";
              case 0xFF33: return "Return From Interrupt";
              case 0xFF3D: return "Reset Transfer Entry";           
              case 0xFF47:
              case 0xFF48:     
              case 0xFF49: return "Set up Fast Serial Port for I/O";
              case 0xFF4A:
              case 0xFF4B:     
              case 0xFF4C: return "Close All Logical Files for a device";  
              case 0xFF4D:
              case 0xFF4E:     
              case 0xFF4F: return "Reconfigure System as a C64 (no return)";
              case 0xFF50:
              case 0xFF51:     
              case 0xFF52: return "Initiate DMA Request to External RAM";
              case 0xFF53:
              case 0xFF54:     
              case 0xFF55: return "Boot Load Program From Disk";
              case 0xFF56:
              case 0xFF57:     
              case 0xFF58: return "Call All Function Cards' Cold Start";
              case 0xFF59:
              case 0xFF5A:     
              case 0xFF5B: return "Search Tables For Given LA";
              case 0xFF5C:
              case 0xFF5D:     
              case 0xFF5E: return "Search Tables For Given SA";
              case 0xFF5F:
              case 0xFF60:     
              case 0xFF61: return "Switch Between 40 and 80 Columns (Editor)";
              case 0xFF62:
              case 0xFF63:     
              case 0xFF64: return "Init 80-Col Character RAM (Editor)";
              case 0xFF65:
              case 0xFF66:     
              case 0xFF67: return "Program Function Key (Editor)";
              case 0xFF68:
              case 0xFF69:     
              case 0xFF6A: return "SET Bank For I/O Operations";
              case 0xFF6B:
              case 0xFF6C:     
              case 0xFF6D: return "Lookup MMU Data For Given Bank";
              case 0xFF6E:
              case 0xFF6F:     
              case 0xFF70: return "JSR to Any Bank, RTS to Calling Bank";
              case 0xFF71:
              case 0xFF72:     
              case 0xFF73: return "JMP to Any Bank";
              case 0xFF74:
              case 0xFF75:     
              case 0xFF76: return "LDA (FETVEC),Y FROM Any Bank";
              case 0xFF77:
              case 0xFF78:     
              case 0xFF79: return "STA (STAVEC),Y to Any Bank";
              case 0xFF7A:
              case 0xFF7B:     
              case 0xFF7C: return "CMP (CMPVEC),Y to Any Bank";
              case 0xFF7D:
              case 0xFF7E:     
              case 0xFF7F: return "Print Immediate Utility";
              case 0xFF80: return "Release Number Of KERNAL";
              case 0xFF81:     
              case 0xFF82: 
              case 0xFF83: return "Init Editor & Display";
              case 0xFF84:     
              case 0xFF85: 
              case 0xFF86: return "Init I/O Devices (ports, timers, etc.)";
              case 0xFF87:     
              case 0xFF88: 
              case 0xFF89: return "Initialize RAM And Buffers For System";
              case 0xFF8A:     
              case 0xFF8B: 
              case 0xFF8C: return "Restore Vectors to Initial System";
              case 0xFF8D:     
              case 0xFF8E: 
              case 0xFF8F: return "Change Vectors For USER";
              case 0xFF90:     
              case 0xFF91: 
              case 0xFF92: return "Control O.S. Message";
              case 0xFF93:     
              case 0xFF94: 
              case 0xFF95: return "Send SA After LISTEN";
              case 0xFF96:     
              case 0xFF97: 
              case 0xFF98: return "Send SA After TALK";
              case 0xFF99:     
              case 0xFF9A: 
              case 0xFF9B: return "Set/Read Top Of System RAM";
              case 0xFF9C:     
              case 0xFF9D: 
              case 0xFF9E: return "Set/Read Bottom Of System RAM";
              case 0xFF9F:     
              case 0xFFA0: 
              case 0xFFA1: return "Scan Keyboard (Editor)";
              case 0xFFA2:     
              case 0xFFA3: 
              case 0xFFA4: return "Set Timeout In IEEE (reserved)";
              case 0xFFA5:     
              case 0xFFA6: 
              case 0xFFA7: return "Handshake Serial Byte In";
              case 0xFFA8:     
              case 0xFFA9: 
              case 0xFFAA: return "Handshake Serial Byte Out";
              case 0xFFAB:     
              case 0xFFAC: 
              case 0xFFAD: return "Send UNTALK Out Serial";
              case 0xFFAE:     
              case 0xFFAF: 
              case 0xFFB0: return "Send UNLISTEN Out Serial";
              case 0xFFB1:     
              case 0xFFB2: 
              case 0xFFB3: return "Send LISTEN Out Serial";
              case 0xFFB4:     
              case 0xFFB5: 
              case 0xFFB6: return "Send TALK Out Serial";
              case 0xFFB7:     
              case 0xFFB8: 
              case 0xFFB9: return "Return I/O Status Byte";
              case 0xFFBA:     
              case 0xFFBB: 
              case 0xFFBC: return "Set LA, FA, SA";
              case 0xFFBD:     
              case 0xFFBE: 
              case 0xFFBF: return "Set Length And File Name Address";
              case 0xFFC0:     
              case 0xFFC1: 
              case 0xFFC2: return "OPEN Logical File";
              case 0xFFC3:     
              case 0xFFC4: 
              case 0xFFC5: return "CLOSE Logical File";
              case 0xFFC6:     
              case 0xFFC7: 
              case 0xFFC8: return "Set Channel In";
              case 0xFFC9:     
              case 0xFFCA: 
              case 0xFFCB: return "Set Channel Out";
              case 0xFFCC:     
              case 0xFFCD: 
              case 0xFFCE: return "Restore Default I/O Channel";
              case 0xFFCF:     
              case 0xFFD0: 
              case 0xFFD1: return "INPUT From Channel";
              case 0xFFD2:     
              case 0xFFD3: 
              case 0xFFD4: return "OUTPUT To Channel";
              case 0xFFD5:     
              case 0xFFD6: 
              case 0xFFD7: return "LOAD From File";
              case 0xFFD8:     
              case 0xFFD9: 
              case 0xFFDA: return "SAVE to File";
              case 0xFFDB:     
              case 0xFFDC: 
              case 0xFFDD: return "Set Internal Clock";
              case 0xFFDE:     
              case 0xFFDF: 
              case 0xFFE0: return "Read Internal Clock";
              case 0xFFE1:     
              case 0xFFE2: 
              case 0xFFE3: return "Scan STOP Key";
              case 0xFFE4:     
              case 0xFFE5: 
              case 0xFFE6: return "Read Buffered Data";
              case 0xFFE7:     
              case 0xFFE8: 
              case 0xFFE9: return "Close All Files And Channels";
              case 0xFFEA:     
              case 0xFFEB: 
              case 0xFFEC: return "Increment Internal Clock";
              case 0xFFED:     
              case 0xFFEE: 
              case 0xFFEF: return "Return Screen Window Size (Editor)";
              case 0xFFF0:     
              case 0xFFF1: 
              case 0xFFF2: return "Read/Set X,Y Cursor Coord (Editor)";
              case 0xFFF3:     
              case 0xFFF4: 
              case 0xFFF5: return "Return I/O Base";
              case 0xFFF8: 
              case 0xFFF9: return "Operating System Vector (RAM1)";    
              case 0xFFFA: 
              case 0xFFFB: return "Processor NMI Vector";
              case 0xFFFC:     
              case 0xFFFD: return "Processor RESET Vector";
              case 0xFFFE: 
              case 0xFFFF: return "Processor IRQ/BRK Vector";
              
              
              default:  
                if ((addr>=0x19) && (addr<=0x23)) return "Stack for temporary strings";
                if ((addr>=0x59) && (addr<=0x62)) return "Miscellaneous numeric work area";
                if ((addr>=0x100) && (addr<=0x10F)) return "Tape Read Errors, Area to build filename in (16 bytes)";
                if ((addr>=0x100) && (addr<=0x1FF)) return "System Stack";
                if ((addr>=0x200) && (addr<=0x2A1)) return "BASIC & Monitor input buffer";
                if ((addr>=0x2A2) && (addr<=0x2AE)) return "Bank Peek Subroutine (Kernal RAM)";
                if ((addr>=0x2AF) && (addr<=0x2BD)) return "Bank Poke Subroutine";
                if ((addr>=0x2BE) && (addr<=0x2CC)) return "Bank Compare Subroutine";
                if ((addr>=0x2CD) && (addr<=0x2E2)) return "JSR to Another Bank";
                if ((addr>=0x2E3) && (addr<=0x2FB)) return "JMP to Another Bank";
                if ((addr>=0x34A) && (addr<=0x353)) return "IRQ Keyboard Buffer (10 Bytes)  FF = No key";
                if ((addr>=0x354) && (addr<=0x35D)) return "Bitmap Of TAB Stops (10 Bytes)";
                if ((addr>=0x362) && (addr<=0x36B)) return "Logical File Number Table";
                if ((addr>=0x36C) && (addr<=0x375)) return "Device Number Table";
                if ((addr>=0x376) && (addr<=0x37F)) return "Secondary Addresse Table";
                if ((addr>=0x380) && (addr<=0x39E)) return "CHRGET Subroutine";
                if ((addr>=0x39F) && (addr<=0x3AA)) return "Fetch From RAM Bank 0";
                if ((addr>=0x3AB) && (addr<=0x3B6)) return "Fetch From RAM Bank 1";
                if ((addr>=0x3B7) && (addr<=0x3BF)) return "Index1 Indirect Fetch From RAM Bank 1";
                if ((addr>=0x3C0) && (addr<=0x3C8)) return "Index2 Indirect Fetch From RAM Bank 0";
                if ((addr>=0x3C9) && (addr<=0x3D1)) return "Txtptr Fetch From RAM Bank 0";
                if ((addr>=0x3F0) && (addr<=0x3F6)) return "DMA Link Code";
                if ((addr>=0x400) && (addr<=0x7E7)) return "VIC 40-Column Text Screen";
                if ((addr>=0x7E8) && (addr<=0x7fF)) return "Sprite Identity Pointers For Text Mode";
                if ((addr>=0x800) && (addr<=0x9FF)) return "BASIC Pseudo Stack (gosub and loop addresses and commands)";
                if ((addr>=0xA40) && (addr<=0xA5A)) return "40/80 Pointer Swap (to E0-FA)";
                if ((addr>=0xA60) && (addr<=0xA6D)) return "40/80 Data Swap (0354-0361)";
                if ((addr>=0xB00) && (addr<=0xBBF)) return "Cassette Buffer";
                if ((addr>=0xC00) && (addr<=0xDFF)) return "RS-232 Input, Output Buffers";
                if ((addr>=0xE00) && (addr<=0xFFF)) return "System Sprites (56-63)";
                if ((addr>=0x1000) && (addr<=0x1009)) return "Programmed Key Lenghts";
                if ((addr>=0x100A) && (addr<=0x10FF)) return "Programmed Key Definitions";
                if ((addr>=0x1100) && (addr<=0x1130)) return "DOS Command Staging Area";
                if ((addr>=0x1131) && (addr<=0x116E)) return "Graphics Work Area";
                if ((addr>=0x1178) && (addr<=0x1197)) return "Graphics Index";
                if ((addr>=0x117E) && (addr<=0x11D5)) return "Sprite Motion Tables (8 x 11 bytes)";
                if ((addr>=0x11D6) && (addr<=0x11E5)) return "Sprite X/Y Positions";
                if ((addr>=0x11EE) && (addr<=0x11FF)) return "Unused";
                if ((addr>=0x1239) && (addr<=0x123E)) return "Current Envelope Pattern";
                if ((addr>=0x123F) && (addr<=0x1270)) return "AD(SR) Pattern";
                if ((addr>=0x1249) && (addr<=0x1252)) return "(AD)SR Pattern";
                if ((addr>=0x1253) && (addr<=0x125C)) return "Waveform Pattern";
                if ((addr>=0x125D) && (addr<=0x1266)) return "Pulse Width Lo Pattern";
                if ((addr>=0x1267) && (addr<=0x1270)) return "Pulse Width Hi Pattern";
                if ((addr>=0x1279) && (addr<=0x127E)) return "Collision IRQ Address Tables";
                if ((addr>=0x1300) && (addr<=0x17FF)) return "Application Program Area";
                if ((addr>=0x1800) && (addr<=0x1BFF)) return "Application Program Area/Reserved for Key Functions";
                if ((addr>=0x1C00) && (addr<=0x1FF7)) return "Video Color Matrix For Graphics Mode";
                if ((addr>=0x1FF8) && (addr<=0x1FFF)) return "Sprite Identity Pointers For Graphics Mode";
                if ((addr>=0x2000) && (addr<=0x3FFF)) return "Screen Memory For Graphics Mode";
                if ((addr>=0xD800) && (addr<=0xDBFF)) return "Color RAM (Nybbles)";              
            }     
          default:
            switch ((int)addr) {
              case 0x00: return "6510 Data I/O direction register";
              case 0x01: return "6510 Data I/O port";
              case 0x02: return "Token 'SEARCH' looks for, Bank Number, Jump to SYS Address";
              case 0x03: 
              case 0x04: return "SYS address, MLM register PC";
              case 0x05: return "SYS and MLM register save SR ";
              case 0x06: return "SYS and MLM register save AC";
              case 0x07: return "SYS and MLM register save XR";
              case 0x08: return "SYS and MLM register save YR";
              case 0x09: return "SYS and MLM register save SP";
              case 0x0A: return "Scan-quotes flag";
              case 0x0B: return "TAB column save";
              case 0x0C: return "Flag: 0=LOAD, 1=VERIFY";
              case 0x0D: return "Input buffer pointer/number of subscripts";
              case 0x0E: return "Default DIM flag";
              case 0x0F: return "Data Type: FF=string, 00=numeric";
              case 0x10: return "Data Type: 80=integer, 00=floating point";
              case 0x11: return "DATA scan/LIST quote/memory flag";
              case 0x12: return "Subscript/FNxx flag";
              case 0x13: return "Flag: 0=INPUT, $40=GET, $98=READ";
              case 0x14: return "ATN sign / Comparison evaluation flag";
              case 0x15: return "Current I/O prompt flag";
              case 0x16: 
              case 0x17: return "Integer value";
              case 0x18: return "Pointer: temporary string stack";
              case 0x19: return "Last Temp String Address";  
              case 0x1B: return "Stack For Temp Strings";
              case 0x24:
              case 0x25: 
              case 0x26: 
              case 0x27: return "Utility pointer area";  
              case 0x28: 
              case 0x29:         
              case 0x2A:
              case 0x2B:                  
              case 0x2C: return "Product area for multiplication";  
              case 0x2D:  
              case 0x2E: return "Pointer: Start-of-BASIC (bank 0) [1C01]";   
              case 0x2F:    
              case 0x30: return "Pointer: Start-of-variables (bank 1) [0400]";
              case 0x31: 
              case 0x32: return "Pointer: Start-of-arrays";
              case 0x33: 
              case 0x34: return "Pointer: End-of-arrays";
              case 0x35: 
              case 0x36: return "Pointer string-storage (moving down)";
              case 0x37: 
              case 0x38: return "Utility string pointer";
              case 0x39: 
              case 0x3A: return "Pointer: Limit-of-memory (bank 1) [FF00]";              
              case 0x3B:
              case 0x3C: return "Current BASIC line number";
              case 0x3D:
              case 0x3E: return "Textpointer: BASIC work point (chrget)";    
              case 0x3F: 
              case 0x40: return "Utility Pointer";  
              case 0x41: 
              case 0x42: return "Current DATA line number"; 
              case 0x43: 
              case 0x44: return "Current DATA address";
              case 0x45: 
              case 0x46: return "Input vector";
              case 0x47: 
              case 0x48: return "Current variable name";
              case 0x49: 
              case 0x4A: return "Current variable address";
              case 0x4B:
              case 0x4C: return "Variable pointer for FOR/NEXT";
              case 0x4D: 
              case 0x4E: return "Y-save, op-save, BASIC pointer save";
              case 0x4F: return "Comparison symbol accumulator"; 
              case 0x50: 
              case 0x51:
              case 0x52:
              case 0x53:
              case 0x54:
              case 0x55: return "Miscellaneous work area, pointers, and so on";
              case 0x56:
              case 0x57:
              case 0x58: return "Jump vector for functions";
              case 0x60:
              case 0x61: 
              case 0x62: return "MLM Address 0";
              case 0x63: return "MLM Address 1/Accum #1 exponent";
              case 0x64: 
              case 0x65: return "MLM Address 1/Accum #1 mantissa";
              case 0x66:
              case 0x67: return "MLM Address 2/Accum #1 mantissa"; 
              case 0x68: return "MLM Address 2/Accum #1 sign";   
              case 0x69: return "Series evaluation constant pointer";
              case 0x6A:
              case 0x6B:
              case 0x6C:
              case 0x6D:
              case 0x6E:
              case 0x6F: return "Accum #2 exponent, and so on";
              case 0x70: return "Sign comparison Acc #1 versus #2";
              case 0x71: return "Accum #1 lo-order (rounding)";
              case 0x72:                   
              case 0x73: return "Cassette buffer len / Series pointer";
              case 0x74: 
              case 0x75: return "Auto line number increment";
              case 0x76: return "Graphics flag: FF = Graphics allocated, 00 = Not allocated";
              case 0x77: return "Color source number";
              case 0x78: 
              case 0x79: return "Temporary counters";
              case 0x7A:  
              case 0x7B:  
              case 0x7C: return "DS$ descriptor";
              case 0x7D: 
              case 0x7E: return "BASIC pseudo-stack pointer";
              case 0x7F: return "Flag: 0=direct mode";
              case 0x80:
              case 0x81: return "DOS, USING work flags";
              case 0x82: return "Stack pointer save for errors";              
              case 0x83: return "Graphic color source";
              case 0x84: return "Multicolor 1 (1)";
              case 0x85: return "Multicolor 2 (2)";
              case 0x86: return "Graphic foreground color (13)";
              case 0x87: 
              case 0x88: return "Graphic scale factors X";
              case 0x89: 
              case 0x8A: return "Graphic scale factors Y";              
              case 0x8B: return "Stoppaint if not Background/Not same color";
              case 0x8C:
              case 0x8D:
              case 0x8E:
              case 0x8F: return "Graphic work values";
              case 0x90: return "Status word ST";
              case 0x91: return "Keyswitch 1A: STOP and RVS flags"; 
              case 0x92: return "Timing constant for tape ($80)";  
              case 0x93: return "Work value, monitor, LOAD/SAVE: 0=LOAD, 1=VERIFY";
              case 0x94: return "Serial output, deferred character flag";
              case 0x95: return "Serial deferred character";
              case 0x96: return "Cassette work value";
              case 0x97: return "Register save";
              case 0x98: return "How many open files";
              case 0x99: return "Input device, normally 0";
              case 0x9A: return "Output CMD device, normally 3";
              case 0x9B: 
              case 0x9C: return "Tape parity, output-received flag";
              case 0x9D: return "I/O messages: 192=all, 128=commands, 64=errors, 0=nil";
              case 0x9E:
              case 0x9F: return "Tape error pointers";
              case 0xA0: 
              case 0xA1:
              case 0xA2: return "Jiffy clock HML";
              case 0xA3: 
              case 0xA4: return "Temp data area";
              case 0xA5: 
              case 0xA6: return "I/O work bytes (tape)";
              case 0xA7: return "RS-232 Input Bit Storage, Cassette Short Count";
              case 0xA8: return "RS-232 Bit Count In, Cassette Read Error";
              case 0xA9: return "RS-232 Flag For Start Bit Check, Cassette Reading Zeroes";
              case 0xAA: return "RS-232 Byte Buffer, Cassette Read Mode";
              case 0xAB: return "RS-232 Parity Storage, Cassette Short Cnt";
              case 0xAC: 
              case 0xAD: return "Pointer for tape buffer and screen scrolling";
              case 0xAE:
              case 0xAF: return "Tape end address / End of program";
              case 0xB0: 
              case 0xB1: return "Tape timing constants";
              case 0xB2:
              case 0xB3: return "Pointer: Start of tape buffer";
              case 0xB4: return "RS-232 Bit Count";
              case 0xB5: return "RS-232 Next Bit To Be Sent";
              case 0xB6: return "RS-232 Byte Buffer";
              case 0xB7: return "Number of characters in file name";
              case 0xB8: return "Current logical file";
              case 0xB9: return "Current secondary address";
              case 0xBA: return "Current device"; 
              case 0xBB: 
              case 0xBC: return "Pointer to file name";  
              case 0xBD: return "RS-232 TRNS Parity Buffer"; 
              case 0xBE: return "Cassette Read Block Count"; 
              case 0xBF: return "Serial Word Buffer";
              case 0xC0: return "Cassette Manual/Cntrolled Switch (Updated during IRQ)";
              case 0xC1:
              case 0xC2: return "I/O Start Address";
              case 0xC3:
              case 0xC4: return "Cassette LOAD Temps (2 bytes)";
              case 0xC5: return "Tape Read/Write Data";
              case 0xC6: 
              case 0xC7: return "BANKS: I/O data, filename";
              case 0xC8: 
              case 0xC9: return "RS-232 input buffer addresses  [0c00]";
              case 0xCA: 
              case 0xCB: return "RS-232 output buffer addresses [0d00]";
              case 0xCC: 
              case 0xCD: return "Keyboard decode pointer (bank 15) [fa80]";
              case 0xCE: 
              case 0xCF: return "Print string work pointer"; 
              case 0xD0: return "Number of characters in keyboard buffer";
              case 0xD1: return "Number of programmed chars waiting";
              case 0xD2: return "Programmed key character index";
              case 0xD3: return "Key shift flag";   
              case 0xD4: return "Current key code: 88=no key";
              case 0xD5: return "Previous key code: 88=no key";
              case 0xD6: return "Input from screen/from keyboard";
              case 0xD7: return "40/80 columns: 0=40 column screen";
              case 0xD8: return "Graphics mode code";   
              case 0xD9: return "Character base: 0=ROM, 4=RAM";
              case 0xDA: return "Pointers For MOVLIN/Temporary For TAB & LINE WRAP Routines";
              case 0xDB: return "Another Temporary Place To Save A Reg.";
              case 0xDC:
              case 0xDD:
              case 0xDE:
              case 0xDF: return "Misc Editor work area";
              case 0xE0:
              case 0xE1: return "Pointer to screen line/cursor";
              case 0xE2:    
              case 0xE3: return "Color line pointer";
              case 0xE4: return "Current screen bottom margin";
              case 0xE5: return "Current screen top margin";
              case 0xE6: return "Current screen left margin";
              case 0xE7: return "Current screen right margin";
              case 0xE8:    
              case 0xE9: return "Input cursor log (row, column)"; 
              case 0xEA: return "End-of-line for input pointer";
              case 0xEB: return "Row where cursor lives";
              case 0xEC: return "Position of cursor on screen line";
              case 0xED: return "Maximum screen lines (24)";
              case 0xEE: return "Maximum screen columns (39)";
              case 0xEF: return "Current I/O character";
              case 0xF0: return "Previous character printed";
              case 0xF1: return "Character color";
              case 0xF2: return "Temporary color save";
              case 0xF3: return "Screen reverse flag";
              case 0xF4: return "Quotes flag (Editor), 0=direct cursor, else programmed";
              case 0xF5: return "Number of INSERTs outstanding";
              case 0xF6: return "255 = Auto Insert enabled";
              case 0xF7: return "Text mode lockout (SHFT-C=): 0=enabled, 128=disabled";
              case 0xF8: return "Scrolling: 0 = enabled, 128 = disabled";
              case 0xF9: return "Bell (CTRL-G):  0= enable, 128 = disable";
              case 0xFA:
              case 0xFB:
              case 0xFC: 
              case 0xFD:
              case 0xFE: return "Not used";
              case 0xFF: return "Basic Scratch";
              
              case 0x110: return "DOS Loop Counter";
              case 0x111: return "DOS Filename 1 Len";
              case 0x112: return "DOS Disk Drive 1";
              case 0x113: return "DOS Filename 2 Len"; 
              case 0x114: return "DOS Disk Drive 2";  
              case 0x115: return "DOS Filename 2 Addr"; 
              case 0x116:
              case 0x117: return "BLOAD/BSAVE Starting Address";  
              case 0x118:
              case 0x119: return "BLOAD/BSAVE Ending Address";  
              case 0x11B: return "DOS Logical Addr [00]";                 
              case 0x11C: return "DOS Phys Addr [08]";
              case 0x11D: return "DOS Sec. Addr [6F]";
              case 0x11E: return "DOS Record Length";
              case 0x120: return "DOS Disk ID";
              case 0x122: return "DOS DSK ID FLG SPACE Used by PRINT USING";
              case 0x123: return "Pointer to Begin. NO.";
              case 0x124: return "Pointer to End NO.";
              case 0x125: return "Dollar Flag";
              case 0x126: return "Comma Flag, PLAY: VOXTUM flag";
              case 0x127: return "Counter";
              case 0x128: return "Sign Exponent";
              case 0x129: return "Pointer to Exponent";
              case 0x12A: return "# Of Digits Before Decimal Point";
              case 0x12B: return "Justify Flag";
              case 0x12C: return "# Of Pos Before Decimal Point (Field)";
              case 0x12D: return "# Of Pos After Decimal Point (Field)";
              case 0x12E: return "+/- Flag (Field)";
              case 0x12F: return "Exponent Flag (Field)";
              case 0x130: return "Switch";
              case 0x131: return "Char Counter (Field)";
              case 0x132: return "Sign No";
              case 0x133: return "Blank/Star Flag";
              case 0x134: return "Pointer to Begin of Field";
              case 0x135: return "Length of Format";
              case 0x136: return "Pointer to End Of Field";
              case 0x2FC:
              case 0x2FD: return "Function Execute Hook";
              case 0x300:
              case 0x301: return "Error Message Link [4D3F]";
              case 0x302:
              case 0x303: return "BASIC Warm Start Link	[4DC6]";
              case 0x304:
              case 0x305: return "Crunch BASIC Tokens Link [430D]";
              case 0x306:
              case 0x307: return "Print Tokens Link [5151]";
              case 0x308:
              case 0x309: return "Start New BASIC Code Link [4AA2]";
              case 0x30A:
              case 0x30B: return "Get Arithmetic Element Link [78DA]";
              case 0x30C:
              case 0x30D: return "Crunch FE Hook [4321]";
              case 0x30E: 
              case 0x30F: return "List FE Hook [51CD]";
              case 0x310:
              case 0x311: return "Execute FE Hook [4BA9]";
              case 0x312:
              case 0x313: return "Unused";
              case 0x314:
              case 0x315: return "IRQ Vector [FA65]";
              case 0x316:
              case 0x317: return "Break Interrupt Vector [B003]";
              case 0x318:
              case 0x319: return "NMI Interrupt Vector[FA40]";
              case 0x31A:
              case 0x31B: return "OPEN Vector [EFBD]";
              case 0x31C:
              case 0x31D: return "CLOSE Vector [F188]";
              case 0x31E:
              case 0x31F: return "Set Input Vector [F106]";
              case 0x320:
              case 0x321: return "Set Output Vector [F14C]";
              case 0x322:
              case 0x323: return "Restore I/O Vector [F226]";
              case 0x324:
              case 0x325: return "Input Vector [EF06]";
              case 0x326:
              case 0x327: return "Output Vector	[EF79]";
              case 0x328:
              case 0x329: return "Test STOP Key [F66E]";
              case 0x32A:
              case 0x32B: return "GET Vector [EEEB]";
              case 0x32C:
              case 0x32D: return "Abort I/O Vector [F222]";
              case 0x32E:
              case 0x32F: return "Machine Lang Monitor Link [B006]";
              case 0x330:
              case 0x331: return "LOAD Link [F26C]";
              case 0x332:
              case 0x333: return "SAVE Link [F54E]";
              case 0x334:
              case 0x335: return "Print Control Code Link [C7B9]";
              case 0x336:
              case 0x337: return "Print High ASCII Code Link [C805]";
              case 0x338:
              case 0x339: return "Print ESC Sequence Link [C9C1]";
              case 0x33A:
              case 0x33B: return "Keyscan Link [C5E1]";
              case 0x33C:
              case 0x33D: return "Store Key [C6AD]";
              case 0x33E:
              case 0x33F: return "Pointer to KBD Decoding Table: Unshifted [FA80/FD29]";
              case 0x340:
              case 0x341: return "Pointer to KBD Decoding Table: Shifted [FAD9/FD82]";
              case 0x342:
              case 0x343: return "Pointer to KBD Decoding Table: Commodore [FB32/FDDB]";
              case 0x344:
              case 0x345: return "Pointer to KBD Decoding Table: Control [FB8B/FE34] 1)";
              case 0x346:
              case 0x347: return "Pointer to KBD Decoding Table: Alt [FA80/FD29]";
              case 0x348:
              case 0x349: return "Pointer to KBD Decoding Table: Ascii/DIN [FB4E/FD29]";              
              case 0x35E:
              case 0x35F: 
              case 0x360:
              case 0x362: return "Bitmap Of Line Wraps";
              case 0x386: return "CHRGOT Entry";
              case 0x3D2:
              case 0x3D3: 
              case 0x3D4: return "Numeric Constant For BASIC";
              case 0x3D5: return "Current Bank For SYS, POKE, PEEK";
              case 0x3D6: 
              case 0x3D7:
              case 0x3D8: 
              case 0x3D9: return "INSTR Work Values";
              case 0x3DA: return "Bank Pointer For String/Number CONVERT RTN";
              case 0x3DB: 
              case 0x3DC:
              case 0x3DE: return "Sprite: Work bytes for SSHAPE";
              case 0x3DF: return "FAC#1 Overflow";
              case 0x3E0: 
              case 0x3E1: return "Sprite: Work bytes for SPRSAV";
              case 0x3E2: return "Graphic Foreground/ Background Color Nybbles";
              case 0x3E3: return "Graphic Foreground/ Multicolor 1 Color Nybbles";
              case 0xA00:
              case 0xA01: return "Vector to Restart System (BASIC Warm) [4003]";
              case 0xA02: return "KERNAL Warm/Cold Init'n Status Byte";
              case 0xA03: return "PAL/NTSC System Flag";
              case 0xA04: return "Flags RESET vs. NMI Status for init'n rtns";
              case 0xA05: 
              case 0xA06: return "Ptr to Bottom of Avail. Memory in System Bank";
              case 0xA07: 
              case 0xA08: return "Ptr to Top of Available Memory in System Bank";
              case 0xA09: return "Tape Handler preserves IRQ Indirect here";
              case 0xA0B: return "TOD Sense during tape operations";
              case 0xA0C: return "CIA 1 Interrupt Log";
              case 0xA0D: return "CIA 1 Timer Enabled";
              case 0xA0F: return "RS-232 Enables";
              case 0xA10: return "RS-232 Control Register";
              case 0xA11: return "RS-232 Command Register";
              case 0xA12: return "RS-232 User Baud Rate";
              case 0xA14: return "RS-232 Status Register";
              case 0xA15: return "RS-232 Number of Bits To Send";
              case 0xA16: return "RS-232 Baud Rate Full Bit Time (Created by OPEN)";
              case 0xA18: return "RS-232 Receive Pointer";
              case 0xA19: return "RS-232 Input Pointer";
              case 0xA1A: return "RS-232 Transmit Pointer";
              case 0xA1B: return "RS-232 Send Pointer";
              case 0xA1C: return "Fast Serial Internal/External Flag";
              case 0xA1D: return "Decrementing Jiffie Register";
              case 0xA1E:
              case 0xA1F: return "Sleep Countdown, FFFF = disable";
              case 0xA20: return "Keyboard Buffer Size (10)";
              case 0xA21: return "Screen Freeze Flag";
              case 0xA22: return "Key Repeat: 128 = all, 64 = none";
              case 0xA23: return "Key Repeat Timing";
              case 0xA24: return "Key Repeat Pause";
              case 0xA25: return "Graphics / Text Toggle Latch";
              case 0xA26: return "40-Col Cursor Mode/VIC Cursor Mode (Blinking, Solid)";
              case 0xA27: return "VIC Cursor Disable";
              case 0xA28: return "VIC Cursor Blink Counter";
              case 0xA29: return "VIC Cursor Character Before Blink";
              case 0xA2A: return "40-Col Blink Values/VIC Cursor Color Before Blink";
              case 0xA2B: return "80-Col Cursor Mode/VDC Cursor Mode (when enabled)";
              case 0xA2C: return "40-Col Video $D018 Image/VIC Text Screen/Character Base Pointer";
              case 0xA2D: return "VIC Bit-Map Base Pointer";
              case 0xA2E: return "80-Col Pages-Screen/VDC Text Screen Base ";
              case 0xA2F: return "80-Col Pages-Screen/Color/VDC Attribute Base";
              case 0xA30: return "Temporary Pointer to Last Line For LOOP4";
              case 0xA31: return "Temporary For 80-Col Routines";
              case 0xA32: return "Temporary For 80-Col Routines";
              case 0xA33: return "VDC Cursor Color Before Blink";
              case 0xA34: return "VIC Split Screen Raster Value";
              case 0xA35: return "Save .X During Bank Operations";
              case 0xA36: return "Counter for PAL Systems (Jiffie adjustment)";
              case 0xA37: return "Save System Speed During Tape and Serial Ops";
              case 0xA38: return "Save Sprite Enables During Tape and Serial Ops";
              case 0xA39: return "Save Blanking Status During Tape Ops";
              case 0xA3A: return "Flag set by user to resrv full control of VIC";
              case 0xA3B: return "Hi byte:SA Of VIC Scrn (Use W/VMI to move scrn)";
              case 0xA3C: 
              case 0xA3D: return "8563 Block Fill";
              case 0xA80: return "Compare Buffer (32 bytes)";
              case 0xAA0:
              case 0xAA1: return "MLM";
              case 0xAAB: return "ASM/DIS";
              case 0xAAC: return "For Assembler";
              case 0xAAF: 
              case 0xAB0: return "Byte Temp used all over";
              case 0xAB1: return "Byte Temp for Assembler";
              case 0xAB2: return "Save .X here during Indirect Subroutine Calls";
              case 0xAB3: return "Direction Indicator For 'TRANSFER'";
              case 0xAB4: 
              case 0xAB5: return "Parse Number Conversion";
              case 0xAB7: return "Parse Number Conversion";
              case 0xAC0: return "PAT Counter/Current Function Key ROM Bank Being Polled";
              case 0xAC1:
              case 0xAC2:
              case 0xAC3:
              case 0xAC4: return "ROM Physical Address Table (IDS OF LOGGED-IN CARDS)";
              case 0xAC5: return "Flag: KBD/Reserved For Foreign Screen Editors";
              case 0x1131: return "Current X Position";
              case 0x1133: return "Current Y Position";
              case 0x1135: return "X-Coordinate Destination";
              case 0x1137: return "Y-Coordinate Destination";
              case 0x1139: return "Line Drawing Variables";
              case 0x1149: return "Sign Of Angle";
              case 0x114A: return "Sine Of Value Of Angle";
              case 0x114C: return "Cosine of Value of Angle";
              case 0x114E: return "Temps For Angle Distance Routines";
              case 0x1150: return "CIRCLE Center, X Coordinate, BOX POINT 1 X-Coord.";
              case 0x1152: return "CIRCLE Center, Y Coordinate, BOX POINT 1 Y-Coord.";
              case 0x1153: return "String Len";
              case 0x1154: return "X Radius, BOX Rotation Angle";
              case 0x1155: return "String Pos'n Counter";
              case 0x1156: return "Y Radius";
              case 0x1157: return "New String or Bit Map Byte";
              case 0x1158: return "CIRCLE Rotation Angle/Placeholder";
              case 0x1159: return "SHAPE Column Length";
              case 0x115A: return "BOX: Length of a side";
              case 0x115B: return "SHAPE Row Length";
              case 0x115C: return "Arc Angle Start";  
              case 0x115D: return "Temp For Column Length";
              case 0x115E: return "Arc Angle End, Char's Col. Counter";
              case 0x115F: return "Save SHAPE String Descriptor";
              case 0x1160: return "X Radius * COS(Rotation Angle)";
              case 0x1161: return "Bit Index Into Byte";
              case 0x1162: return "Y Radius * SIN(Rotation Angle)";
              case 0x1164: return "X Radius * SIN(Rotation Angle)";
              case 0x1166: return "Y Radius * COS(Rotation Angle)";
              case 0x1168: return "HIGH BYTE: ADDR OF CHARROM For 'CHAR' CMD.";
              case 0x1169: return "Temp For GSHAPE";
              case 0x116A: return "SCALE Mode Flag";
              case 0x116B: return "Double Width Flag";
              case 0x116C: return "Box Fill Flag";
              case 0x116D: return "Temp For Bit Mask";
              case 0x116F: return "Trace Mode: FF = on";
              case 0x1170:
              case 0x1171:
              case 0x1172: 
              case 0x1173: return "Renumbering Pointers";
              case 0x1174:
              case 0x1175:
              case 0x1176: return "Directory Work Pointers/Graphic Temp Storage"; 
              case 0x1177: return "Directory Work Pointers";  
              case 0x117A:
              case 0x117B: return "Float-fixed Vector [849F]";
              case 0x117C:
              case 0x117D: return "Fixed-float Vector [793C]";
              case 0x11E6: return "Sprite X-High Positions";
              case 0x11E7:
              case 0x11E8: return "Sprite Bumb Masks (sprite - backgnd)";
              case 0x11E9:
              case 0x11EA: return "Light Pen Values, X and Y";
              case 0x11EB: return "CHRGEN ROM Page, Text Mode [D8]";
              case 0x11EC: return "CHRGEN ROM Page, Graphics Mode [D0]";
              case 0x11ED: return "Secondary Address For RECORD";
              case 0x1200:
              case 0x1201: return "Previous BASIC Line";
              case 0x1202:
              case 0x1203: return "Pointer: BASIC Statement for CONTINUE";
              case 0x1204: return "PRINT USING Fill Symbol";
              case 0x1205: return "PRINT USING Comma Symbol";
              case 0x1206: return "PRINT USING D.P. Symbol";
              case 0x1207: return "Print Using Monetary Symbol";
              case 0x1208: return "Used by Error Trapping Routine - Last Err No";
              case 0x1209:
              case 0x120A: return "Line # of Last Error. FFFF if No Error";
              case 0x120B:
              case 0x120C: return "TRAP Address, FFFF=none";
              case 0x120D: return "Hold Trap # of Tempor.";
              case 0x1210:
              case 0x1211: return "End of Basic, Bank 0";
              case 0x1212: 
              case 0x1213: return "Basic Program Limit [FF00]";
              case 0x1214: 
              case 0x1215: 
              case 0x1216: 
              case 0x1217: return "DO Work Pointers";
              case 0x1218: 
              case 0x1219:
              case 0x121A: return "USR Program Jump [7D28]";
              case 0x121B: 
              case 0x121C: 
              case 0x121D: 
              case 0x121E:
              case 0x121F: return "RND Seed Value";
              case 0x1220: return "Degrees Per CIRCLE Segment";
              case 0x1221: return "'Cold' or 'Warm' Reset Status";
              case 0x1222: return "Sound Tempo";
              case 0x1223:
              case 0x1224: return "Remaining Note Length LO/HI, Voice 1";
              case 0x1225:
              case 0x1226: return "Remaining Note Length LO/HI, Voice 2";
              case 0x1227:
              case 0x1228: return "Remaining Note Length LO/HI, Voice 3";
              case 0x1229:
              case 0x122A: return "Note Length LO/HI";
              case 0x122B: return "Octave";
              case 0x122C: return "Flag: 01 = Sharp, FF = Flat";
              case 0x122D:
              case 0x122E: return "Pitch";
              case 0x122F: return "Music Sequencer (Voice Number)";
              case 0x1230: return "Wave";
              case 0x1233: return "Flag: Play Dotted Note";
              case 0x1234: 
              case 0x1235: 
              case 0x1236:
              case 0x1237: return "Note Image";
              case 0x1271:
              case 0x1272:
              case 0x1273:
              case 0x1274: return "Note: xx, xx, volume";
              case 0x1275: return "Previous Volume Image";
              case 0x1276:
              case 0x1277:
              case 0x1278: return "Collision IRQ Task Table";
              case 0x127F: return "Collision Mask";
              case 0x1280: return "Collision Work Value";
              case 0x1281: return "SOUND Voice";
              case 0x1282: return "SOUND Time LO";
              case 0x1285: 
              case 0x1286:
              case 0x1287: return "SOUND Time HI";
              case 0x1288:
              case 0x1289:
              case 0x128A: return "SOUND Max LO";
              case 0x128B: return "SOUND Max HI";
              case 0x128E: return "SOUND Min LO";
              case 0x1291: return "SOUND Min HI";
              case 0x1294: return "SOUND Direction";
              case 0x1297: return "SOUND Step LO";
              case 0x129A: return "SOUND Step HI";
              case 0x129B: return "SOUND Freq LO";
              case 0x12A0: return "SOUND Freq HI";
              case 0x12A3: return "Temp Time LO";
              case 0x12A4: return "Temp Time HI";
              case 0x12A5: return "Temp Max LO";
              case 0x12A6: return "Temp Max HI";
              case 0x12A7: return "Temp Min LO";
              case 0x12A8: return "Temp Min HI";
              case 0x12A9: return "Temp Direction";
              case 0x12AA: return "Temp Step LO";
              case 0x12AB: return "Temp Step HI";
              case 0x12AC: return "Temp Freq LO";
              case 0x12AD: return "Temp Freq HI";
              case 0x12AE: return "Temp Pulse LO";
              case 0x12AF: return "Temp Pulse HI";
              case 0x12B0: return "Temp Waveform";
              case 0x12B1: 
              case 0x12B2: return "PEN/POT Work Values";
              case 0x12B7: 
              case 0x12FA: 
              case 0x12FB: return "Used BY SPRDEF & SAVSPR";
              case 0x12FC: return "Sprite Number/Used BY SPRDEF & SAVSPR";
              case 0x12FD: return "Used by BASIC IRQ to block all but one IRQ call";
              
              case 0x4000:
              case 0x4001:
              case 0x4002: return "COLD ENTRY";
              case 0x4003:
              case 0x4004:
              case 0x4005: return "WARM ENTRY";
              case 0x4006:
              case 0x4007:
              case 0x4008: return "IRQ ENTRY";
              case 0x4009: return "Basic Restart";
              case 0x4023: return "Basic Cold Start";
              case 0x4045: return "Set-Up Basic Constants";
              case 0x4112: return "Chime";
              case 0x417A: return "Set Preconfig Registers";
              case 0x4189: return "Registers For $D501";
              case 0x418D: return "Init Sprite Movement Tabs";
              case 0x419B: return "Print Startup Message";
              case 0x41BB: return "Startup Message";
              case 0x4251: return "Set Basic Links";
              case 0x4267: return "Basic Links for $0300";
              case 0x4279: return "Chrget For $0380";
              case 0x42CE: return "Get From ($50) Bank1";
              case 0x42D3: return "Get From ($3f) Bank1";
              case 0x42D8: return "Get From ($52) Bank1";
              case 0x42DD: return "Get From ($5c) Bank0";
              case 0x42E2: return "Get From ($5c) Bank1";
              case 0x42E7: return "Get From ($66) Bank1";
              case 0x42EC: return "Get From ($61) Bank0";
              case 0x4271: return "Get From ($70) Bank0";
              case 0x42F6: return "Get From ($70) Bank1";
              case 0x42FB: return "Get From ($50) Bank1";
              case 0x4300: return "Get From ($61) Bank1";
              case 0x4305: return "Get From ($24) Bank0";
              case 0x430A: return "Crunch Tokens";
              case 0x43CC: return "Move Down Input Buffer";
              case 0x43E2: return "Check Keyword Match";
              case 0x4417: return "Keywords - Non prefixed";
              case 0x4609: return "Keywords - Prefix FE";
              case 0x46C9: return "Keywords - Prefix CE";
              case 0x46FC: return "Action Vectors";
              case 0x47D8: return "Function Vectors";
              case 0x4828: return "Defunct Vectors";
              case 0x4846: return "Unimplemented Commands";
              case 0x484B: return "Error Messages";
              case 0x4A82: return "Find Message";
              case 0x4A9F: return "Start New Basic Code";
              case 0x4B34: return "Update Continue Pointer";
              case 0x4B3F: return "Execute/Trace Statement";
              case 0x4BCB: return "Perform [stop]";
              case 0x4BCD: return "Perform [end]";
              case 0x4BF7: return "Setup FN Reference";
              case 0x4C86: return "Evaluate <or>";
              case 0x4C89: return "Evaluate <and>";
              case 0x4CB6: return "Evaluate <compare>";
              case 0x4D2A: return "Print 'ready'";
              case 0x4D2D: return "'ready.'";
              case 0x4D37: return "Error or Ready";
              case 0x4DCA: return "Print 'out of memory'";
              case 0x4D3C: return "Error";
              case 0x4DAF: return "Break Entry";
              case 0x4DC3: return "Ready For Basic";
              case 0x4DE2: return "Handle New Line";
              case 0x4FAF: return "Rechain Lines";
              case 0x4F82: return "Reset End-of-Basic";
              case 0x4F93: return "Receive Input Line";
              case 0x4FAA: return "Search B-Stack For Match";
              case 0x4FFE: return "Move B-Stack Down";
              case 0x5017: return "Check Memory Space";
              case 0x5047: return "Copy B-Stack Pointer";
              case 0x5050: return "Set B-Stack Pointer";
              case 0x5059: return "Move B-Stack Up";
              case 0x5064: return "Find Basic Line";
              case 0x50A0: return "Get Fixed Pt Number";
              case 0x50E2: return "Perform [list]";
              case 0x5123: return "List Subroutine";
              case 0x51D6: return "Perform [new]";
              case 0x51F3: return "Set Up Run";
              case 0x51F8: return "Perform [clr]";
              case 0x5238: return "Clear Stack & Work Area";
              case 0x5250: return "Pudef Characters";
              case 0x5254: return "Back Up Text Pointer";
              case 0x5262: return "Perform [return]";
              case 0x528F: return "Perform [data/bend]";
              case 0x529D: return "Perform [rem]";
              case 0x52A2: return "Scan To Next Statement";
              case 0x52A5: return "Scan To Next Line";
              case 0x52C5: return "Perform [if]";
              case 0x5320: return "Search/Skip Begin/Bend";
              case 0x537C: return "Skip String Constant";
              case 0x5391: return "Perform [else]";
              case 0x53A3: return "Perform [on]";
              case 0x53C6: return "Perform [let]";
              case 0x54F6: return "Check String Location";
              case 0x553A: return "Perform [print#]";
              case 0x5540: return "Perform [cmd]";
              case 0x555A: return "Perform [print]";
              case 0x5600: return "Print Format Char";
              case 0x5607: return "-print '<cursor right>'-";
              case 0x5604: return "-print space-";
              case 0x560A: return "-print '?'-";
              case 0x5612: return "Perform [get]";                           
              case 0x5635: return "Getkey";
              case 0x5648: return "Perform [input#]";
              case 0x5662: return "Perform [input]";
              case 0x569C: return "Prompt & Input";
              case 0x56A9: return "Perform [read]";
              case 0x57F4: return "Perform [next]";
              case 0x587B: return "Perform [dim]";
              case 0x5885: return "Perform [sys]";
              case 0x58B4: return "Perform [tron]";
              case 0x58B7: return "Perform [troff]";
              case 0x58BD: return "Perform [rreg]";
              case 0x5901: return "Assign <mid$>";
              case 0x5975: return "Perform [auto]";
              case 0x5986: return "Perform [help]";
              case 0x59AC: return "Insert Help Marker";
              case 0x59CF: return "Perform [gosub]";
              case 0x59DB: return "Perform [goto]";
              case 0x5A15: return "Undef'd Statement";
              case 0x5A1D: return "Put Sub To B-Stack";
              case 0x5A3D: return "Perform [go]";
              case 0x5A60: return "Perform [cont]";
              case 0x5A9B: return "Perform [run]";
              case 0x5ACA: return "Perform [restore]";
              case 0x5AF0: return "Keywords To Renumber";
              case 0x5AF8: return "Perform [renumber]";
              case 0x5BAE: return "Renumber-Continued";
              case 0x5BFB: return "Renumber Scan";
              case 0x5D19: return "Convert Line Number";
              case 0x5D68: return "Get Renumber Start";
              case 0x5D75: return "Count Off Lines";
              case 0x5D89: return "Add Renumber Inc";
              case 0x5D99: return "Scan Ahead";
              case 0x5DA7: return "Set Up Block Move";
              case 0x5DC6: return "Block Move Down";
              case 0x5DDF: return "Block Move Up";
              case 0x5DEE: return "Check Block Limit";
              case 0x5DF9: return "Perform [for]";
              case 0x5E87: return "Perform [delete]";
              case 0x5EFB: return "Get Line Number Range";
              case 0x5F34: return "Perform [pudef]";
              case 0x5F4D: return "Perform [trap]";
              case 0x5F62: return "Perform [resume]";
              case 0x5FB7: return "Reinstate Trap Point";
              case 0x5FD8: return "Syntax Exit";
              case 0x5FDB: return "Print 'can't resume'";
              case 0x5FE0: return "Perform [do]";
              case 0x6039: return "Perform [exit]";
              case 0x608A: return "Perform [loop]";
              case 0x60B4: return "Print 'loop not found'";
              case 0x60B7: return "Print 'loop without do'";
              case 0x60DB: return "Eval While/Until Argument";
              case 0x60E1: return "Define Programmed Key";
              case 0x610A: return "Perform [key]";
              case 0x619D: return "'+chr$('";
              case 0x61A8: return "Perform [paint]";
              case 0x627C: return "Check Painting Split";
              case 0x62B7: return "Perform [box]";
              case 0x63F5: return "Authors";
              case 0x642B: return "Perform [sshape]";
              case 0x658D: return "Perform [gshape]";
              case 0x668E: return "Perform [circle]";
              case 0x6750: return "Draw Circle";
              case 0x6797: return "Perform [draw]";
              case 0x67D7: return "Perform [char]";
              case 0x6955: return "Perform [locate]";
              case 0x6960: return "Perform [scale]";
              case 0x69D8: return "Scale Factor Constants";
              case 0x69e2: return "Perform [color]";
              case 0x6A4C: return "Color Codes";
              case 0x6A5C: return "Log Current Colors";
              case 0x6A79: return "Perform [scnclr]";
              case 0x6B06: return "Fill Memory Page";
              case 0x6B17: return "Set Screen Color";
              case 0x6B5A: return "Perform [graphic]";
              case 0x6BC9: return "Perform [bank]";
              case 0x6BD7: return "Perform [sleep]";
              case 0x6C09: return "Multiply Sleep Time";
              case 0x6C2D: return "Perform [wait]";
              case 0x6C4F: return "Perform [sprite]";
              case 0x6CB3: return "Bit Masks";
              case 0x6CC6: return "Perform [movspr]";
              case 0x6DD9: return "Sprite Motion Table Offsets";
              case 0x6DE1: return "Perform [play]";
              case 0x6E02: return "Analyze Play Character";
              case 0x6E66: return "-Filter";
              case 0x6E9D: return "-Voice";
              case 0x6EA8: return "-Octave";
              case 0x6EB2: return "Set SID Sound";
              case 0x6EDD: return "-Volume";
              case 0x6EFD: return "Play Error";
              case 0x6F03: return "Dotted Note";
              case 0x6F07: return "Note Length Char";
              case 0x6F1E: return "Note A-G";
              case 0x6F52: return "...votxum...";
              case 0x6F69: return "Sharp";
              case 0x6F6C: return "Flat";
              case 0x6F78: return "Rest";
              case 0x6FD7: return "Perform [tempo]";
              case 0x6FE4: return "Voice Times Two	";
              case 0x6FE7: return "Length Characters";
              case 0x6FEC: return "Command Characters	";
              case 0x6FF2: return "Indexes for Note Symbols	";
              case 0x6FF9: return "Pitch LO -- NTSC";
              case 0x7005: return "Pitch HI -- NTSC";
              case 0x7011: return "Envelope A/D Patterns";
              case 0x702F: return "Chime Seq";
              case 0x7039: return "SID Voice Offsets";
              case 0x703C: return "SID Volume Scale";
              case 0x7046: return "Perform [filter]";
              case 0x70C1: return "Perform [envelope]";
              case 0x7164: return "Perform [collision]";
              case 0x7190: return "Perform [sprcolor]";
              case 0x71B6: return "Perform [width]";
              case 0x71C5: return "Perform [vol]";
              case 0x71EC: return "Perform [sound]";
              case 0x72CC: return "Perform [window]";
              case 0x7335: return "Perform [boot]";
              case 0x7372: return "Perform [sprdef]";
              case 0x7452: return "-Perform [home]";
              case 0x7497: return "-Perform [1234]";
              case 0x74F4: return "-Perform []";
              case 0x74FE: return "-Perform [shift-return]";
              case 0x7506: return "-Perform [m]";
              case 0x751C: return "-Perform [y]";
              case 0x751F: return "-Perform [x]";
              case 0x7530: return "-Perform [clr]";
              case 0x753F: return "-Perform [right]";
              case 0x7542: return "-Perform [left]";
              case 0x7564: return "-Perform [up]";
              case 0x7567: return "-Perform [down]";
              case 0x7576: return "-Perform [a]";
              case 0x7581: return "-Perform [return]";
              case 0x7588: return "Perform [c]";
              case 0x767F: return "SPRDEF Commands";
              case 0x7691: return "SPRDEF Command Vectors HI/LO";
              case 0x76B5: return "SPRDEF Color Commands";
              case 0x76EC: return "Perform [sprsav]";
              case 0x77B3: return "Perform [fast]";
              case 0x77C4: return "Perform [slow]";
              case 0x77D7: return "Type Match Check";
              case 0x77DA: return "Confirm Numeric";
              case 0x77DD: return "Confirm String";
              case 0x77E7: return "Print 'type mismatch'";
              case 0x77EA: return "Print 'formula too complex'";
              case 0x77EF: return "Evaluate Expression";
              case 0x78D7: return "Evaluate Item";
              case 0x78FE: return "Constant PI";
              case 0x7903: return "Continue Expression";
              case 0x7930: return "Evaluate <equal>";
              case 0x793C: return "Fixed-Float";
              case 0x7950: return "Eval Within Parens";
              case 0x7956: return "-Check Right Parenthesis";
              case 0x7959: return "-Check Left Parenthesis";
              case 0x795C: return "-Check Comma";
              case 0x796C: return "Syntax Error";
              case 0x7978: return "Search For Variable";
              case 0x7A85: return "Unpack RAM1 to FAC#1";
              case 0x7AAF: return "Locate Variable";
              case 0x7B3C: return "Check Alphabetic";
              case 0x7B46: return "Create Variable";
              case 0x7CAB: return "Set Up Array";
              case 0x7D25: return "Print 'bad subscript'";
              case 0x7D28: return "Print 'illegal quantity'";
              case 0x7E3E: return "Compute Array Size";
              case 0x7E71: return "Array Pointer Subroutine";
              case 0x7E82: return "Set Bank patch for [char]";
              case 0x7E88: return "Set Bank patch for Screen Print Link";
              case 0x7E8E: return "Patch for Renumber Scan";
              case 0x7E94: return "Patch for [delete]";
              case 0x7EA6: return "Patch for Note A-G";
              case 0x7EB9: return "Pitch LO -- PAL";
              case 0x7EC5: return "Pitch HI -- PAL";
              case 0x7ED1: return "String Stack patch for Error";
              case 0x7ED9: return "Unused";
              case 0x7FC0: return "Copyright Banner";
              case 0x7FFC: return "Checksum (?)";
              
              case 0x8000: return "Evaluate <fre>";
              case 0x8020: return "Decrypt Message";
              case 0x803A: return "-Free in Bank 1";
              case 0x804A: return "Evaluate <val>";
              case 0x8052: return "String to Float";
              case 0x8076: return "Evaluate <dec>";
              case 0x80C5: return "Evaluate <peek>";
              case 0x80E5: return "Perform [poke]";
              case 0x80F6: return "Evaluate <err$>";
              case 0x8139: return "Swap x With y";
              case 0x8142: return "Evaluate <hex$>";
              case 0x816B: return "Byte to Hex";
              case 0x8182: return "Evaluate <rgr>";
              case 0x818C: return "Get Graphics Mode";
              case 0x819B: return "Evaluate <rclr>";
              case 0x81F3: return "CRTC Color Codes";
              case 0x8203: return "Evaluate <joy>";
              case 0x8242: return "Joystick Values";
              case 0x824D: return "Evaluate <pot>";
              case 0x82AE: return "Evaluate <pen>";
              case 0x82FA: return "Evaluate <pointer>";
              case 0x831E: return "Evaluate <rsprite>";
              case 0x835B: return "VIC Sprite Register Numbers	";
              case 0x8361: return "Evaluate <rspcolor>";
              case 0x837C: return "Evaluate <bump>";
              case 0x8397: return "Evaluate <rspos>";
              case 0x83E1: return "Evaluate <xor>";
              case 0x8407: return "Evaluate <rwindow>";
              case 0x8434: return "Evaluate <rnd>";
              case 0x8490: return "Rnd Multiplier";
              case 0x849A: return "Value 32768";
              case 0x849F: return "Float-Fixed Unsigned";
              case 0x84A7: return "Evaluate Fixed Number";
              case 0x84AD: return "Float-Fixed Signed";
              case 0x84C9: return "Float (.y, .a)";
              case 0x84D0: return "Evaluate <pos>";
              case 0x84D4: return "Byte To Float";
              case 0x84D9: return "Check Direct";
              case 0x84DD: return "Print 'illegal direct'";
              case 0x84E0: return "Print 'undef'd function'";
              case 0x84E5: return "Set Up 16 Bit Fix-Float";
              case 0x84F0: return "Check Direct";
              case 0x84F5: return "Print 'direct mode only'";
              case 0x84FA: return "Perform [def]";
              case 0x8528: return "Check FN Syntax";
              case 0x853B: return "Perform [fn]";
              case 0x85AE: return "Evaluate <str$>";
              case 0x85BF: return "Evaluate <chr$>";
              case 0x85D6: return "Evaluate <left$>";
              case 0x860A: return "Evaluate <right$>";
              case 0x861C: return "Evaluate <mid$>";
              case 0x864D: return "Pull String Parameters";
              case 0x8668: return "Evaluate <len>";
              case 0x866E: return "Exit String Mode";
              case 0x8677: return "Evaluate <asc>";
              case 0x8688: return "Calc String Vector";
              case 0x869A: return "Set Up String";
              case 0x874E: return "Build String to Memory";
              case 0x877B: return "Evaluate String";
              case 0x87E0: return "Clean Descriptor Stack";
              case 0x87F1: return "Input Byte Parameter";
              case 0x87F4: return "-Eval Byte Parameter";
              case 0x8803: return "Params For Poke/Wait";
              case 0x880F: return "Input Next Float/Fixed Value";
              case 0x8812: return "-Input Float/Fixed Value";
              case 0x8815: return "Float/Fixed";
              case 0x882E: return "Subtract From Memory";
              case 0x8831: return "Evaluate <subtract>";
              case 0x8845: return "Add Memory";
              case 0x8848: return "Evaluate <add>";
              case 0x88D6: return "Zero Both Accuymulators";
              case 0x8917: return "Trim FAC#1 Left";
              case 0x8926: return "Negate FAC#1";
              case 0x894E: return "Round Up FAC#1";
              case 0x895D: return "Print 'overflow'";
              case 0x899C: return "Log Series: 1.00";
              case 0x89A1: return "Log Series: #03 (counter)";
              case 0x89A2: return "Log Series: 0.434255942";
              case 0x89A7: return "Log Series: 0.57658454";
              case 0x89AC: return "Log Series: 0.961800759";
              case 0x89B1: return "Log Series: 2.885390073";
              case 0x89B6: return "Log Series: 0.707106781 SQR(0.5)";
              case 0x89BB: return "Log Series: 1.41421356 SRQ(2)";
              case 0x89C0: return "Log Series: -0.5";
              case 0x89C5: return "Log Series: 0.693147181 LOG(2)";
              case 0x89CA: return "Evaluate <log>";                            
              case 0x8A0E: return "Add 0.5";
              case 0x8A12: return "Add Memory At A/Y";
              case 0x8A18: return "Subtract Memory At A/Y";
              case 0x8A1E: return "Divide By Memory";
              case 0x8A24: return "Multiply By Memory";
              case 0x8A27: return "Evaluate <multiply>";
              case 0x8A89: return "Unpack ROM to FAC#2";
              case 0x8AB4: return "Unpack RAM1 to FAC#2";
              case 0x8AEC: return "Test Both Accumulators";
              case 0x8B09: return "Overflow/Underflow";
              case 0x8B17: return "Multiply By 10";
              case 0x8B2E: return "+10";
              case 0x8B33: return "Print 'division by zero'";
              case 0x8B38: return "Divide By 10";
              case 0x8B49: return "Divide Into Memory";
              case 0x8B4C: return "Evaluate <divide>";
              case 0x8BD4: return "Unpack ROM to FAC#1";
              case 0x8BF9: return "Pack FAC#1 to $5e";
              case 0x8BFC: return "Pack FAC#1 to $59";
              case 0x8C00: return "Pack FAC#1 to RAM1";
              case 0x8C28: return "FAC#2 to FAC#1";
              case 0x8C38: return "FAC#1 to FAC#2";
              case 0x8C47: return "Round FAC#1";
              case 0x8C57: return "Get Sign";
              case 0x8C65: return "Evaluate <sgn>";
              case 0x8C68: return "Byte Fixed-Float";
              case 0x8C75: return "Fixed-Float";
              case 0x8C84: return "Evaluate <abs>";
              case 0x8C87: return "Compare FAC#1 to Memory";
              case 0x8CC7: return "Float-Fixed";
              case 0x8CFB: return "Evaluate <int>";
              case 0x8D22: return "String to FAC#1";
              case 0x8DB0: return "Get Ascii Digit";
              case 0x8E17: return "String Conversion Constants: 99999999.9";
              case 0x8E1C: return "String Conversion Constants: 999999999";
              case 0x8E21: return "String Conversion Constants: 1000000000";
              case 0x8E26: return "Print 'in...'";
              case 0x8E32: return "Print Integer";
              case 0x8E42: return "Float to Ascii";
              case 0x8F76: return "+0.5";
              case 0x8F7B: return "Decimal Constants";
              case 0x8F9F: return "TI Constants";
              case 0x8FB7: return "Evaluate <sqr>";
              case 0x8FBE: return "Raise to Memory Power";
              case 0x8FC1: return "Evaluate <power>";
              case 0x8FFA: return "Evaluate <negate>";
              case 0x9005: return "Exp Series: 1.44269504 (1/LOG to base 2 e)";
              case 0x900A: return "Exp Series: #07 (counter)";
              case 0x900B: return "Exp Series: 2.149875 E-5";
              case 0x9010: return "Exp Series: 1.435231 E-4";
              case 0x9015: return "Exp Series: 1.342263 E-3";
              case 0x901A: return "Exp Series: 9.6414017 E-3";
              case 0x901F: return "Exp Series: 5.550513 E-2";
              case 0x9024: return "Exp Series: 2.402263 E-4";
              case 0x9029: return "Exp Series: 6.931471 E-1";
              case 0x902E: return "Exp Series: 1.00";
              case 0x9033: return "Evaluate <exp>";
              case 0x90D0: return "I/O Error Message";
              case 0x90D8: return "Basic 'open'";
              case 0x90DF: return "Basic 'chrout'";
              case 0x90E5: return "Basic 'input'";
              case 0x90EB: return "Redirect Output";
              case 0x90FD: return "Redirect Input";
              case 0x9112: return "Perform [save]";
              case 0x9129: return "Perform [verify]";
              case 0x912C: return "Perform [load]";
              case 0x918D: return "Perform [open]";
              case 0x919A: return "Perform [close]";
              case 0x91AE: return "Get Load/Save Parameters";
              case 0x91DD: return "Get Next Byte Value";
              case 0x91E3: return "Get Character or Abort";
              case 0x91EB: return "Move to Next Parameter";
              case 0x91F6: return "Get Open/Close Parameters";
              case 0x9243: return "Release I/O String";
              case 0x9251: return "Call 'status'";
              case 0x9257: return "Call 'setlfs'";
              case 0x925D: return "Call 'setnam'";
              case 0x9263: return "Call 'getin'";
              case 0x9269: return "Call 'chrout'";
              case 0x926F: return "Call 'clrchn'";
              case 0x9275: return "Call 'close'";
              case 0x927B: return "Call 'clall'";
              case 0x9281: return "Print Following Text";
              case 0x9287: return "Set Load/Save Bank";
              case 0x928D: return "Call 'plot'";
              case 0x9293: return "Call 'test stop'";
              case 0x9299: return "Make Room For String";
              case 0x92EA: return "Garbage Collection";
              case 0x9409: return "Evaluate <cos>";
              case 0x9410: return "Evaluate <sin>";
              case 0x9459: return "Evaluate <tan>";
              case 0x9485: return "Trig Series: 1.570796327 pi/2";
              case 0x948A: return "Trig Series: 6.28318531 pi*2";
              case 0x948F: return "Trig Series: 0.25";
              case 0x9494: return "Trig Series: #05 (counter)";
              case 0x9495: return "Trig Series: -14.3813907";
              case 0x949A: return "Trig Series: 42.0077971";
              case 0x949F: return "Trig Series: -76.7041703";
              case 0x94A4: return "Trig Series: 81.6052237";
              case 0x94A9: return "Trig Series: -41.3417021";
              case 0x94AE: return "Trig Series: 6.28318531";
              case 0x94B3: return "Evaluate <atn>";
              case 0x94E3: return "ATN Series: #0b (counter)";
              case 0x94E4: return "ATN Series: -0.000684793912";
              case 0x94E9: return "ATN Series:  0.00485094216";
              case 0x94EE: return "ATN Series: -0.161117018";
              case 0x94F3: return "ATN Series: 0.034209638";
              case 0x94F8: return "ATN Series: -0.0542791328";
              case 0x94FD: return "ATN Series: 0.0724571965";
              case 0x9502: return "ATN Series: -0.0898023954";
              case 0x9507: return "ATN Series: 0.110932413";
              case 0x950C: return "ATN Series: -0.142839808";
              case 0x9511: return "ATN Series: 0.19999912";
              case 0x9516: return "ATN Series: -0.333333316";
              case 0x951B: return "ATN Series: 1.00";
              case 0x9520: return "Print Using";
              case 0x99C1: return "Evaluate <instr>";
              case 0x9B0C: return "Evaluate <rdot>";
              case 0x9B30: return "Draw Line";
              case 0x9BFB: return "Plot Pixel";
              case 0x9C49: return "Examine Pixel";
              case 0x9C70: return "Set Hi-Res Color Cell";
              case 0x9CCA: return "Video Matrix Lines Hi";
              case 0x9CE3: return "Position Pixel";
              case 0x9D1C: return "Bit Masks	";
              case 0x9D24: return "Calc Hi-Res Row/Column";                            
              case 0x9D67: return "Calculate Graphics Coordinates"; 
              case 0x9D6D: return "Add Graphics Coordinate"; 
              case 0x9D7C: return "Subtract Graphics Coordinate"; 
              case 0x9D8F: return "Read Current X Position to A/Y"; 
              case 0x9DF2: return "Restore Pixel Cursor";
              case 0x9E06: return "Check Optional Float/Fixed Parameter"; 
              case 0x9E1C: return "Input Optional Byte Parameter/-Check Byte Parameter in List"; 
              case 0x9E2F: return "Parse Graphics Command"; 
              case 0x9E32: return "Get Color Source Param"; 
              case 0x9F25: return "Multicolor Pixel Masks"; 
              case 0x9F29: return "Conv Words Hi"; 
              case 0x9F3D: return "Conv Words Lo";
              case 0x9F4F: return "Allocate 9K Graphics Area for graphic/sprdef";
              case 0xA022: return "Move Basic to $1c01";
              case 0xA07E: return "Perform [catalog/directory]";
              case 0xA11D: return "Perform [dopen]";
              case 0xA134: return "Perform [append]";
              case 0xA157: return "Find Spare SA";
              case 0xA16F: return "Perform [dclose]";
              case 0xA18C: return "Perform [dsave]";
              case 0xA1A4: return "Perform [dverify]";
              case 0xA1A7: return "Perform [dload]";
              case 0xA1C8: return "Perform [bsave]";
              case 0xA218: return "Perform [bload]";
              case 0xA267: return "Perform [header]";
              case 0xA2A1: return "Perform [scratch]";
              case 0xA2D7: return "Perform [record]";
              case 0xA322: return "Perform [dclear]";
              case 0xA32F: return "Perform [collect]";
              case 0xA346: return "Perform [copy]";
              case 0xA362: return "Perform [concat]";
              case 0xA36E: return "Perform [rename]";
              case 0xA37C: return "Perform [backup]";
              case 0xA3B8: return "Default DOS Disk Unit (U8 D0)";
              case 0xA3BC: return "DOS Logical Address";
              case 0xA3BD: return "DOS Physical Address";
              case 0xA3BE: return "DOS Secondary Address";
              case 0xA3BF: return "Parse DOS Commands";
              case 0xA5E7: return "Print 'missing file name'";
              case 0xA5EA: return "Print 'illegal device number'";
              case 0xA5ED: return "Print 'string too long'";
              case 0xA627: return "DOS Command Masks	";
              case 0xA667: return "Set DOS Parameters";
              case 0xA7E1: return "Print 'are you sure'";
              case 0xA7E8: return "'are you sure?'";
              case 0xA80D: return "Release String";
              case 0xA82A: return "'key 0,'";
              case 0xA845: return "Set Bank 15";
              case 0xA84D: return "IRQ Work";
              case 0xAA1F: return "Perform [stash]";
              case 0xAA24: return "Perform [fetch]";
              case 0xAA29: return "Perform [swap]";
              case 0xAA6E: return "Patch for Print Using";
              case 0xAA81: return "Unused";
              case 0xAE64: return "Encrypted Message";
              
              case 0xAF00:
              case 0xAF01:
              case 0xAF02: return "Convert F.P. to Integer";
              case 0xAF03:
              case 0xAF04:
              case 0xAF05: return "Convert Integer to F.P.";
              case 0xAF06:
              case 0xAF07:
              case 0xAF08: return "Convert F.P. to ASCII String";
              case 0xAF09:
              case 0xAF0A:
              case 0xAF0B: return "Convert ASCII String to F.P.";     
              case 0xAF0C:
              case 0xAF0D:
              case 0xAF0E: return "Convert F.P. to an Address";   
              case 0xAF0F:
              case 0xAF10:
              case 0xAF11: return "Convert Address to F.P.";   
              case 0xAF12:
              case 0xAF13:
              case 0xAF14: return "MEM - FACC";
              case 0xAF15:
              case 0xAF16:
              case 0xAF17: return "ARG - FACC";
              case 0xAF18:
              case 0xAF19:
              case 0xAF1A: return "MEM + FACC";              
              case 0xAF1B:
              case 0xAF1C:
              case 0xAF1D: return "ARG - FACC";
              case 0xAF1E:
              case 0xAF1F:
              case 0xAF20: return "MEM * FACC";
              case 0xAF21:
              case 0xAF22: 
              case 0xAF23: return "ARG * FACC";
              case 0xAF24:
              case 0xAF25:
              case 0xAF26: return "MEM / FACC";
              case 0xAF27:
              case 0xAF28:
              case 0xAF29: return "ARG / FACC";
              case 0xAF2A: 
              case 0xAF2B:
              case 0xAF2C: return "Compute Natural LOG Of FACC";   
              case 0xAF2D:
              case 0xAF2E:     
              case 0xAF2F: return "Perform BASIC INT On FACC";     
              case 0xAF30:
              case 0xAF31:     
              case 0xAF32: return "Compute Square Root OF FACC";
              case 0xAF33:
              case 0xAF34:     
              case 0xAF35: return "Negate FACC";
              case 0xAF36:
              case 0xAF37:     
              case 0xAF38: return "Raise ARG to The Mem Power";   
              case 0xAF39:
              case 0xAF3A:     
              case 0xAF3B: return "Raise ARG to The FACC Power"; 
              case 0xAF3C:
              case 0xAF3D:     
              case 0xAF3E: return "Compute EXP Of FACC"; 
              case 0xAF3F:
              case 0xAF40:     
              case 0xAF41: return "Compute COS Of FACC";   
              case 0xAF42:
              case 0xAF43:     
              case 0xAF44: return "Compute SIN Of FACC";
              case 0xAF45:
              case 0xAF46:     
              case 0xAF47: return "Compute TAN Of FACC";
              case 0xAF48:
              case 0xAF49:     
              case 0xAF4A: return "Compute ATN Of FACC";
              case 0xAF4B:
              case 0xAF4C:     
              case 0xAF4D: return "Round FACC";   
              case 0xAF4E:
              case 0xAF4F:     
              case 0xAF50: return "Absolute Value Of FACC";
              case 0xAF51:
              case 0xAF52:     
              case 0xAF53: return "Test Sign Of FACC";              
              case 0xAF54:
              case 0xAF55:     
              case 0xAF56: return "Compare FACC With Memory";
              case 0xAF57:
              case 0xAF58:     
              case 0xAF59: return "Generate Random F.P. Number";     
              case 0xAF5A:
              case 0xAF5B:     
              case 0xAF5C: return "Move RAM MEM to ARG";      
              case 0xAF5D:
              case 0xAF5E:     
              case 0xAF5F: return "Move ROM MEM to ARG"; 
              case 0xAF60:
              case 0xAF61:     
              case 0xAF62: return "Move RAM MEM to FACC"; 
              case 0xAF63:
              case 0xAF64:     
              case 0xAF65: return "Move ROM MEM to FACC";               
              case 0xAF66:
              case 0xAF67:     
              case 0xAF68: return "Move FACC to MEM";               
              case 0xAF69:
              case 0xAF6A:     
              case 0xAF6B: return "Move ARG to FACC";    
              case 0xAF6C:
              case 0xAF6D:     
              case 0xAF6E: return "Move FACC to ARG";
              case 0xAFA8: return "Unused";
              
              case 0xB000:
              case 0xB001:     
              case 0xB002: return "MONITOR Call Entry";
              case 0xB003:
              case 0xB004:     
              case 0xB005: return "MONITOR Break Entry";
              case 0xB006:
              case 0xB007:     
              case 0xB008: return "MONITOR Command Parser Entry";
              case 0xB009: return "Print 'break'";
              case 0xB00C: return "'break'";
              case 0xB021: return "Print 'call' entry";
              case 0xB03A: return "Print 'monitor'";
              case 0xB03D: return "'monitor'";
              case 0xB050: return "Perform [r]/Print 'pc sr ac xr yr sp'";
              case 0xB053: return "'pc sr ac xr yr sp'";
              case 0xB08D: return "Get Command";
              case 0xB0BC: return "Error";
              case 0xB0BF: return "'?'";
              case 0xB0E3: return "Perform [x]";
              case 0xB0E6: return "Commands";
              case 0xB0FC: return "Vectors";
              case 0xB11A: return "Read Banked Memory";
              case 0xB12A: return "Write Banked Memory";
              case 0xB13D: return "Compare Banked Memory";
              case 0xB152: return "Perform [m]";
              case 0xB194: return "Perform [:]";
              case 0xB1AB: return "Perform [>]";
              case 0xB1C9: return "Print 'esc-o-up'";
              case 0xB1CC: return "'esc-o-up'";
              case 0xB1D6: return "Perform [g]";
              case 0xB1DF: return "Perform [j]";
              case 0xB1E8: return "Display Memory";
              case 0xB20B: return "Print ':<rvs-on>'";
              case 0xB20E: return "':<rvs-on>'";
              case 0xB231: return "Perform [c]";
              case 0xB234: return "Perform [t]";
              case 0xB2C3: return "Add 1 to Op 3";
              case 0xB2C6: return "Do Next Address";
              case 0xB2CE: return "Perform [h]";
              case 0xB337: return "Perform [lsv]";
              case 0xB3C4: return "Print 'error'";
              case 0xB3DB: return "Perform [f]";
              case 0xB406: return "Perform [a]";
              case 0xB533: return "Print 'space<esc-q>'";
              case 0xB57C: return "Check 2 A-Matches";
              case 0xB57F: return "Check A-Match";
              case 0xB58B: return "Try Next Op Code";
              case 0xB599: return "Perform [d]";
              case 0xB5AE: return "Print '<cr><esc-q>'";
              case 0xB5B1: return "'<cr><esc-q>'";
              case 0xB5D4: return "Display Instruction";
              case 0xB5F2: return "Print '<3 spaces>'";
              case 0xB5F5: return "'<3 spaces>'";
              case 0xB659: return "Classify Op Code";
              case 0xB6A1: return "Get Mnemonic Char";
              case 0xB6C3: return "Mode Tables";
              case 0xB715: return "Mode Characters";
              case 0xB721: return "Compacted Mnemonics";
              case 0xB7A5: return "Input Parameter";
              case 0xB7CE: return "Read Value";
              case 0xB88A: return "Number Bases";
              case 0xB88E: return "Base Bits";
              case 0xB892: return "Display 5-Digit Address";
              case 0xB8A5: return "Display 2-digit Byte";
              case 0xB8A8: return "Print Space";
              case 0xB8AD: return "Print Cursor-Up";
              case 0xB8B4: return "New Line";
              case 0xB8B9: return "Blank New Line";
              case 0xB8C2: return "Output 2-Digit Byte";
              case 0xB8D2: return "Byte to 2 Ascii";
              case 0xB8E7: return "Get Input Char";
              case 0xB8E9: return "Get Character";
              case 0xB901: return "Copy Add0 to Add2";
              case 0xB90E: return "Calculate Add2 - Add0";
              case 0xB922: return "Subtract Add0";
              case 0xB93C: return "Subtract Add1";
              case 0xB950: return "Increment Pointer";
              case 0xB960: return "Decrement Pointer";
              case 0xB874: return "Copy to Register Area";
              case 0xB983: return "Calculate Step/Area";
              case 0xB9B1: return "Perform [$+&%]";
              case 0xBA07: return "Convert to Decimal";
              case 0xBA47: return "Transfer Address";
              case 0xBA5D: return "Output Address";
              case 0xBA90: return "Perform [@]";
              case 0xBB72: return "Unused";
              case 0xBFC0: return "Copyright Banner";
              case 0xBFFC: return "Checksum (?)";
              
              case 0xC000:
              case 0xC001:     
              case 0xC002: return "Initialize Editor & Screen";              
              case 0xC003:
              case 0xC004:     
              case 0xC005: return "Display Charac in .A, Color";        
              case 0xC006:
              case 0xC007:     
              case 0xC008: return "Get Key From IRQ Buffer";
              case 0xC009:
              case 0xC00A:     
              case 0xC00B: return "Into A";
              case 0xC00C:
              case 0xC00D:     
              case 0xC00E: return "Print Character In .A";
              case 0xC00F:
              case 0xC010:     
              case 0xC011: return "Get # of Scrn Rows, Cols Into X & Y";    
              case 0xC012:
              case 0xC013:     
              case 0xC014: return "Scan Keyboard Subroutine"; 
              case 0xC015:
              case 0xC016:     
              case 0xC017: return "Handle Repeat Key & Store Decoded Key"; 
              case 0xC018:
              case 0xC019:     
              case 0xC01A: return "Read Or Set CRSR Position In X, Y"; 
              case 0xC01B:
              case 0xC01C:     
              case 0xC01D: return "Move 8563 Cursor Subroutine";
              case 0xC01E:
              case 0xC01F:     
              case 0xC020: return "Execute ESC Function using chr in .A";
              case 0xC021:
              case 0xC022:     
              case 0xC023: return "Redefine A Programmable Func'n Key";     
              case 0xC024:
              case 0xC025:     
              case 0xC026: return "IRQ Entry";   
              case 0xC027:
              case 0xC028:     
              case 0xC029: return "Initialize 80-Column Character Set";   
              case 0xC02A:
              case 0xC02B:     
              case 0xC02C: return "Swap Editor Locals (in 40/80 change)";
              case 0xC02D:
              case 0xC02E:     
              case 0xC02F: return "Set Top-Left or Bot-Right of Window";
              case 0xC033: return "Screen Address Low";
              case 0xC04C: return "Screen Address High";
              case 0xC065: return "I/O Link Vectors";
              case 0xC06F: return "Keyboard Shift Vectors";
              case 0xC07B: return "Initialize Screen";
              case 0xC142: return "Reset Window";
              case 0xC150: return "Home Cursor";
              case 0xC156: return "Goto Left Border";
              case 0xC15C: return "Set Up New Line";
              case 0xC17C: return "Do Screen Color";
              case 0xC194: return "(IRQ) Split Screen";
              case 0xC234: return "Get a Key";
              case 0xC258: return "Screen Line Editor";
              case 0xC29B: return "Input From Screen";
              case 0xC2BC: return "Read Screen Char";
              case 0xC2FF: return "Check For Quotes";
              case 0xC30C: return "Wrap Up Screen Print";
              case 0xC320: return "Ascii to Screen Code";
              case 0xC33E: return "Check Cursor Range";
              case 0xC363: return "Do New Line";
              case 0xC37C: return "Insert a Line";
              case 0xC3A6: return "Scroll Screen";
              case 0xC3DC: return "Delete a Line";
              case 0xC40D: return "Move Screen Line";
              case 0xC4A5: return "Clear a Line";
              case 0xC53C: return "Set 80-Column Counter to 1";
              case 0xC53E: return "Set 80-Column Counter";
              case 0xC55D: return "Keyboard Scan Subroutine";
              case 0xC651: return "Key Pickup & Repeat";
              case 0xC6DD: return "Keycodes for Programmed Keys";
              case 0xC6E7: return "Flash 40 Column Cursor";
              case 0xC72D: return "Print to Screen";
              case 0xC77D: return "Esc-o (escape)";
              case 0xC78C: return "Control Characters";
              case 0xC79A: return "Control Character Vectors";
              case 0xC7B6: return "Print Control Char";
              case 0xC802: return "Print Hi-Bit Char";
              case 0xC854: return "Chr$(29) Cursor Right";
              case 0xC85A: return "Chr$(17) Cursor Down";
              case 0xC875: return "Chr$(157) Cursor Left";
              case 0xC880: return "Chr$(14) Text Mode";
              case 0xC8A6: return "Chr$(11) Lock";
              case 0xC8AC: return "Chr$(12) Unlock";
              case 0xC8B3: return "Chr$(19) Home";
              case 0xC8BF: return "Chr$(146) Clear Rvs Mode";
              case 0xC8C2: return "Chr$(18) Reverse";
              case 0xC8C7: return "Chr$(2) Underline On";
              case 0xC8CE: return "Chr$(130) Underline Off";
              case 0xC8D5: return "Chr$(15) Flash On";
              case 0xC8DC: return "Chr$(143) Flash Off";
              case 0xC8E3: return "Open Screen Space";
              case 0xC91B: return "Chr$(20) Delete";
              case 0xC932: return "Restore Cursor";
              case 0xC94F: return "Chr$(9) Tab";
              case 0xC961: return "Chr$(24) Tab Toggle";
              case 0xC96C: return "Find Tab Column";
              case 0xC980: return "Esc-z Clear All Tabs";
              case 0xC983: return "Esc-y Set Default Tabs";
              case 0xC98E: return "Chr$(7) Bell";
              case 0xC9B1: return "Chr$(10) Linefeed";
              case 0xC9BE: return "Analyze Esc Sequence";
              case 0xC9DE: return "Esc Sequence Vectors";
              case 0xCA14: return "Esc-t Top";
              case 0xCA16: return "Esc-b Bottom";
              case 0xCA1B: return "Set Window Part";
              case 0xCA24: return "Exit Window";
              case 0xC052: return "Esc-d Delete Line";
              case 0xC076: return "Esc-q Erase End";
              case 0xC08B: return "Esc-p Erase Begin";
              case 0xCA9F: return "Esc-@ Clear Remainder of Screen";
              case 0xCABC: return "Esc-v Scroll Up";
              case 0xCACA: return "Esc-w Scroll Down";
              case 0xCAE2: return "Esc-l Scroll On";
              case 0xCAE5: return "Esc-m Scroll Off";
              case 0xCAEA: return "Esc-c Cancel Auto Insert";
              case 0xCAED: return "Esc-a Auto Insert";
              case 0xCAF2: return "Esc-s Block Cursor";
              case 0xCAFE: return "Esc-u Underline Cursor";
              case 0xCB0B: return "Esc-e Cursor Non Flash";
              case 0xCB21: return "Esc-f Cursor Flash";
              case 0xCB37: return "Esc-g Bell Enable";
              case 0xCB3A: return "Esc-h Bell Disable";
              case 0xCB3F: return "Esc-r Screen Reverse";
              case 0xCB48: return "Esc-n Screen Normal";
              case 0xCB52: return "Esc-k End-of-Line";
              case 0xCB58: return "Get Screen Char/Color";
              case 0xCB74: return "Check Screen Line of Location";
              case 0xCB81: return "Extend/Trim Screen Line";
              case 0xCB9F: return "Set Up Line Masks";
              case 0xCBB1: return "Esc-j Start-of-Line";
              case 0xCBC3: return "Find End-of-Line";
              case 0xCBED: return "Move Cursor Right";
              case 0xCC00: return "Move Cursor Left";
              case 0xCC1E: return "Save Cursor";
              case 0xCC27: return "Print Space";
              case 0xCC2F: return "Print Character";
              case 0xCC32: return "Print Fill Color";
              case 0xCC34: return "Put Char to Screen";
              case 0xCC5B: return "Get Rows/Columns";
              case 0xCC6A: return "Read/Set Cursor";
              case 0xCCA2: return "Define Function Key";
              case 0xCD2D: return "Esc-x Switch 40/80 Col";
              case 0xCD57: return "Position 80-col Cursor";
              case 0xCD6F: return "Set Screen Color";
              case 0xCD9F: return "Turn Cursor On";
              case 0xCDCA: return "Set CRTC Register 31";
              case 0xCDCC: return "Set CRTC Register";
              case 0xCDD8: return "Read CRTC Register 31";
              case 0xCDDA: return "Read CRTC Register";
              case 0xCDE6: return "Set CRTC to Screen Address";
              case 0xCDF9: return "Set CRTC to Color Address";
              case 0xCE0C: return "Set Up 80 Column Char Set";
              case 0xCE4C: return "Ascii Color Codes";
              case 0xCE5C: return "System Color Codes";
              case 0xCE6C: return "Bit Masks";
              case 0xCE74: return "40-col Init Values ($e0)";
              case 0xCE8E: return "80-col Init Values ($0a40)";
              case 0xCE8A: return "Prog Key Lengths";
              case 0xCEB2: return "Prog Key Definitions";
              case 0xCEF5: return "Unused";
              
              case 0xD000: return "Position X sprite 0";
              case 0xD001: return "Position Y sprite 0";
              case 0xD002: return "Position X sprite 1";
              case 0xD003: return "Position Y sprite 1";
              case 0xD004: return "Position X sprite 2";
              case 0xD005: return "Position Y sprite 2";
              case 0xD006: return "Position X sprite 3";
              case 0xD007: return "Position Y sprite 3";
              case 0xD008: return "Position X sprite 4";
              case 0xD009: return "Position Y sprite 4";
              case 0xD00A: return "Position X sprite 5";
              case 0xD00B: return "Position Y sprite 5";
              case 0xD00C: return "Position X sprite 6";
              case 0xD00D: return "Position Y sprite 6";
              case 0xD00E: return "Position X sprite 7";
              case 0xD00F: return "Position Y sprite 7";
              case 0xD010: return "Position X MSB sprites 0..7";
              case 0xD011: return "VIC control register";
              case 0xD012: return "Reading/Writing IRQ balance value";
              case 0xD013: return "Positin X of optic pencil \"latch\"";
              case 0xD014: return "Positin Y of optic pencil \"latch\"";
              case 0xD015: return "Sprites Abilitator";
              case 0xD016: return "VIC control register";
              case 0xD017: return "(2X) vertical expansion (Y) sprite 0..7";
              case 0xD018: return "VIC memory control register";
              case 0xD019: return "Interrupt indicator register";
              case 0xD01A: return "IRQ mask register";
              case 0xD01B: return "Sprite-background screen priority";
              case 0xD01C: return "Set multicolor mode for sprite 0..7";
              case 0xD01D: return "(2X) horizontal expansion (X) sprite 0..7";
              case 0xD01E: return "Animations contact";
              case 0xD01F: return "Animation/background contact";
              case 0xD020: return "Border color";
              case 0xD021: return "Background 0 color";
              case 0xD022: return "Background 1 color";
              case 0xD023: return "Background 2 color";
              case 0xD024: return "Background 3 color";
              case 0xD025: return "Multicolor animation 0 register";
              case 0xD026: return "Multicolor animation 1 register";
              case 0xD027: return "Color sprite 0";
              case 0xD028: return "Color sprite 1";
              case 0xD029: return "Color sprite 2";
              case 0xD02A: return "Color sprite 3";
              case 0xD02B: return "Color sprite 4";
              case 0xD02C: return "Color sprite 5";
              case 0xD02D: return "Color sprite 6";
              case 0xD02E: return "Color sprite 7";

              case 0xD400: return "Voice 1: Frequency control (lo byte)";
              case 0xD401: return "Voice 1: Frequency control (hi byte)";
              case 0xD402: return "Voice 1: Wave form pulsation amplitude (lo byte)";
              case 0xD403: return "Voice 1: Wave form pulsation amplitude (hi byte)";
              case 0xD404: return "Voice 1: Control registers";
              case 0xD405: return "Generator 1: Attack/Decay";
              case 0xD406: return "Generator 1: Sustain/Release";
              case 0xD407: return "Voice 2: Frequency control (lo byte)";
              case 0xD408: return "Voice 2: Frequency control (hi byte)";
              case 0xD409: return "Voice 2: Wave form pulsation amplitude (lo byte)";
              case 0xD40A: return "Voice 2: Wave form pulsation amplitude (hi byte)";
              case 0xD40B: return "Voice 2: Control registers";
              case 0xD40C: return "Generator 2: Attack/Decay";
              case 0xD40D: return "Generator 2: Sustain/Release";
              case 0xD40E: return "Voice 3: Frequency control (lo byte)";
              case 0xD40F: return "Voice 3: Frequency control (hi byte)";
              case 0xD410: return "Voice 3: Wave form pulsation amplitude (lo byte)";
              case 0xD411: return "Voice 3: Wave form pulsation amplitude (hi byte)";
              case 0xD412: return "Voice 3: Control registers";
              case 0xD413: return "Generator 3: Attack/Decay";
              case 0xD414: return "Generator 3: Sustain/Release";
              case 0xD415: return "Filter cut frequency: lo byte (bit 2-0)";
              case 0xD416: return "Filter cut frequency: hi byte";
              case 0xD417: return "Filter resonance control/voice input control";
              case 0xD418: return "Select volume and filter mode";
              case 0xD419: return "Analog/digital converter: Paddle 1";
              case 0xD41A: return "Analog/digital converter: Paddle 2";
              case 0xD41B: return "Random numbers generator oscillator 3";
              case 0xD41C: return "Generator output";
              
              case 0xD500: return "Configuration Register (CR)";
              case 0xD501: return "Preconfiguration Registers";
              case 0xD505: return "Modality register";
              case 0xD506: return "RAM Configuration Register (RCR)";
              case 0xD507: return "Zero Page Pointer Lo";
              case 0xD508: return "Zero Page Pointer Hi";
              case 0xD509: return "Stack Page Pointer Lo";
              case 0xD50A: return "Stack Page Pointer Hi";
              case 0xD50B: return "MMU Version Register";
              
              case 0xD600: return "VDC 8563: Horizontal Total";
              case 0xD601: return "VDC 8563: Horizontal Displayed";
              case 0xD602: return "VDC 8563: Horizontal Sync Position";
              case 0xD603: return "VDC 8563: Vert/Horiz. Sync Width";
              case 0xD604: return "VDC 8563: Vertical Total";
              case 0xD605: return "VDC 8563: Vertical Total Fine Adju";
              case 0xD606: return "VDC 8563: Vertical Displayed";
              case 0xD607: return "VDC 8563: Vertical Sync Position";
              case 0xD608: return "VDC 8563: Interlace Mode";
              case 0xD609: return "VDC 8563: Character Total Vertical";
              case 0xD60A: return "VDC 8563: Cursor Mode/ Start Scan";
              case 0xD60B: return "VDC 8563: Cursor End Scan";
              case 0xD60C: return "VDC 8563: Display Start Adrs (Hi)";
              case 0xD60D: return "VDC 8563: Display Start Adrs (Lo)";
              case 0xD60E: return "VDC 8563: Cursor Position (Hi)";
              case 0xD60F: return "VDC 8563: Cursor Position (Lo)";
              case 0xD610: return "VDC 8563: Light Pen Veritcal";
              case 0xD611: return "VDC 8563: Light Pen Horizontal";
              case 0xD612: return "VDC 8563: Update Address (Hi)";
              case 0xD613: return "VDC 8563: Update Address (Lo)";
              case 0xD614: return "VDC 8563: Attribute Start Adrs (Hi)";
              case 0xD615: return "VDC 8563: Attribute Start Adrs (Lo)";
              case 0xD616: return "VDC 8563: Hz Chr Pxl Ttl/IChar Spc ";
              case 0xD617: return "VDC 8563: Vert. Character Pxl Spc";
              case 0xD618: return "VDC 8563: Block/Rvs Scr/V. Scroll";
              case 0xD619: return "VDC 8563: Diff. Mode Sw/H. Scroll";
              case 0xD61A: return "VDC 8563: ForeGround/BackGround Col";
              case 0xD61B: return "VDC 8563: Row/Adrs. Incremen";
              case 0xD61C: return "VDC 8563: Character Set Addrs/Ram";
              case 0xD61D: return "VDC 8563: Underline Scan Line";
              case 0xD61E: return "VDC 8563: Word Count (-1)";
              case 0xD61F: return "VDC 8563: Data";
              case 0xD620: return "VDC 8563: Block Copy Source (hi)";
              case 0xD621: return "VDC 8563: Block Copy Source (lo)";
              case 0xD622: return "VDC 8563: Display Enable Begin";
              case 0xD623: return "VDC 8563: Display Enable End";
              case 0xD624: return "VDC 8563: DRAM Refresh Rate";
              
              case 0xDC00: return "Data port A #1: keyboard, joystick, paddle, optical pencil";
              case 0xDC01: return "Data port B #1: keyboard, joystick, paddle";
              case 0xDC02: return "Data direction register port A #1";
              case 0xDC03: return "Data direction register port B #1";
              case 0xDC04: return "Timer A #1: Lo Byte";
              case 0xDC05: return "Timer A #1: Hi Byte";
              case 0xDC06: return "Timer B #1: Lo Byte";
              case 0xDC07: return "Timer B #1: Hi Byte";
              case 0xDC08: return "Day time clock #1: 1/10 second";
              case 0xDC09: return "Day time clock #1: Second";
              case 0xDC0A: return "Day time clock #1: Minutes";
              case 0xDC0B: return "Day time clock #1: Hour+[indicator AM/PM]";
              case 0xDC0C: return "Serial I/O data buffer synchronous #1";
              case 0xDC0D: return "Interrupt control register CIA #1";
              case 0xDC0E: return "Control register A of CIA #1";
              case 0xDC0F: return "Control register B of CIA #1";

              case 0xDD00: return "Data port A #2: serial bus, RS-232, VIC memory";
              case 0xDD01: return "Data port B #2: user port, RS-232";
              case 0xDD02: return "Data direction register port A #2";
              case 0xDD03: return "Data direction register port A #2";
              case 0xDD04: return "Timer A #2: Lo Byte";
              case 0xDD05: return "Timer A #2: Hi Byte";
              case 0xDD06: return "Timer B #2: Lo Byte";
              case 0xDD07: return "Timer B #2: HI Byte";
              case 0xDD08: return "Day time clock #2: 1/10 second";
              case 0xDD09: return "Day time clock #2: seconds";
              case 0xDD0A: return "Day time clock #2: minutes";
              case 0xDD0B: return "Day time clock #2: Hour+[indicator AM/PM]";
              case 0xDD0C: return "Serial I/O data buffer synchronous #2";
              case 0xDD0D: return "Interrupt control register CIA #2";
              case 0xDD0E: return "Control register A of CIA #2";
              case 0xDD0F: return "Control register B of CIA #2";
              
              case 0xDF00: return "8726 DMA Controller: STATUS";
              case 0xDF01: return "8726 DMA Controller: COMMAND";
              case 0xDF02: return "8726 DMA Controller: HOST ADDRESS LOW";
              case 0xDF03: return "8726 DMA Controller: HOST ADDRESS HIGH";
              case 0xDF04: return "8726 DMA Controller: EXPANSION ADDRESS LOW";
              case 0xDF05: return "8726 DMA Controller: EXPANSION ADDRESS HIGH";
              case 0xDF06: return "8726 DMA Controller: EXPANSION BANK";
              case 0xDF07: return "8726 DMA Controller: TRANSFER LENGTH LOW";
              case 0xDF08: return "8726 DMA Controller: TRANSFER LENGTH HIGH";
              case 0xDF09: return "8726 DMA Controller: INTERRUPT MASK REGISTER";
              case 0xDF0A: return "8726 DMA Controller: VERSION, MAXIMUM MEMORY";
              
              case 0xE000: return "Reset Code";
              case 0xE04B: return "MMU Set Up Bytes";
              case 0xE056: return "-restor-";
              case 0xE05B: return "-vector-";
              case 0xE073: return "Vectors to $0314";
              case 0xE093: return "-ramtas-";
              case 0xE0CD: return "Move Code For High RAM Banks";
              case 0xE105: return "RAM Bank Masks";
              case 0xE109: return "-init-";
              case 0xE1DC: return "Set Up CRTC Registers";
              case 0xE1F0: return "Check Special Reset";
              case 0xE242: return "Reset to 64/128";
              case 0xE24B: return "Switch to 64 Mode";
              case 0xE263: return "Code to $02";
              case 0xE26B: return "Scan All ROMs";
              case 0xE2BC: return "ROM Addresses High";
              case 0xE2C0: return "ROM Banks";
              case 0xE2C4: return "Print 'cbm' Mask";
              case 0xE2C7: return "VIC 8564 Set Up";
              case 0xE2F8: return "CRTC 8563 Set Up Pairs";
              case 0xE33B: return "-talk-";
              case 0xE33E: return "-listen-";
              case 0xE38C: return "Send Data On Serial Bus";
              case 0xE439: return "-acptr-";
              case 0xE4D2: return "-second-";
              case 0xE4E0: return "-tksa-";
              case 0xE503: return "ciout- Print Serial";
              case 0xE515: return "-untlk-";
              case 0xE526: return "-unlsn-";
              case 0xE535: return "Reset ATN";
              case 0xE545: return "Set Clock High";
              case 0xE54E: return "Set Clock Low";
              case 0xE557: return "Set Data High";
              case 0xE560: return "Set Data Low";
              case 0xE569: return "Read Serial Lines";
              case 0xE573: return "Stabilize Timing";
              case 0xE59F: return "Restore Timing";
              case 0xE5BC: return "Prepare For Response";
              case 0xE5C3: return "Fast Disk Off";
              case 0xE5D6: return "Fast Disk On";
              case 0xE5FB: return "Fast Disk On/Off";
              case 0xE5FF: return "(NMI) Transmit RS-232";
              case 0xE64A: return "RS-232 Handshake";
              case 0xE68E: return "Set RS-232 Bit Count";
              case 0xE69D: return "(NMI) RS-232 Receive";
              case 0xE75F: return "Send To RS-232";
              case 0xE795: return "Connect RS-232 Input";
              case 0xE7CE: return "Get From RS-232";
              case 0xE7EC: return "Interlock RS-232/Serial";
              case 0xE805: return "(NMI) RS-232 Control I/O";
              case 0xE850: return "RS-232 Timing Table -- NTSC";
              case 0xE864: return "RS-232 Timing Table -- PAL";
              case 0xE878: return "(NMI) RS-232 Receive Timing";
              case 0xE8A9: return "(NMI) RS-232 Transmit Timing";
              case 0xE8D0: return "Find Any Tape Header";
              case 0xE919: return "Write Tape Header";
              case 0xE980: return "Get Buffer Address";
              case 0xE987: return "Get Tape Buffer Start & End Addrs";
              case 0xE99A: return "Find Specific Header";
              case 0xE9BE: return "Bump Tape Pointer";
              case 0xE9C8: return "Print 'press play on tape'";
              case 0xE9DF: return "Check Tape Status";
              case 0xE9E9: return "Print 'press record ...'";
              case 0xE9F2: return "Initiate Tape Read";
              case 0xEA15: return "Initiate Tape Write";
              case 0xEA26: return "Common Tape Code";
              case 0xEA7D: return "Wait For Tape";
              case 0xEA8F: return "Check Tape Stop";
              case 0xEAA1: return "Set Read Timing";
              case 0xEAEB: return "(IRQ) Read Tape Bits";
              case 0xEC1F: return "Store Tape Chars";
              case 0xED51: return "Reset Pointer";
              case 0xED5A: return "New Char Set Up";
              case 0xED69: return "Write Transition to Tape";
              case 0xED8B: return "Write Data to Tape";
              case 0xED90: return "(IRQ) Tape Write";
              case 0xEE2E: return "(IRQ) Tape Leader";
              case 0xEE57: return "Wind Up Tape I/O";
              case 0xEE9B: return "Switch IRQ Vector";
              case 0xEEA8: return "IRQ Vectors";
              case 0xEEB0: return "Kill Tape Motor";
              case 0xEEB7: return "Check End Address";
              case 0xEEC1: return "Bump Address";
              case 0xEEC8: return "(IRQ) Clear Break";
              case 0xEED0: return "Control Tape Motor";
              case 0xEEEB: return "-getin-";
              case 0xEF06: return "-chrin-";
              case 0xEF48: return "Get Char From Tape";
              case 0xEF79: return "-chrout-";
              case 0xEFBD: return "-open-";
              case 0xF0B0: return "Set CIA to RS-232";
              case 0xF0CB: return "Check Serial Open";
              case 0xF106: return "-chkin-";
              case 0xF14C: return "-chkout-";
              case 0xF188: return "-close-";
              case 0xF1E4: return "Delete File";
              case 0xF202: return "Search For File";
              case 0xF212: return "Set File Parameters";
              case 0xF222: return "-clall-";
              case 0xF226: return "-clrchn-";
              case 0xF23D: return "Clear I/O Path";
              case 0xF265: return "-load-";
              case 0xF27B: return "Serial Load";
              case 0xF32A: return "Tape Load";
              case 0xF3A1: return "Disk Load";
              case 0xF3EA: return "Burst Load";
              case 0xF48C: return "Close Off Serial";
              case 0xF4BA: return "Get Serial Byte";
              case 0xF4C5: return "Receive Serial Byte";
              case 0xF503: return "Toggle Clock Line";
              case 0xF50C: return "Print 'u0' Disk Reset";
              case 0xF50F: return "Print 'searching'";
              case 0xF521: return "Send File Name";
              case 0xF533: return "Print 'loading'";
              case 0xF53E: return "-save-";
              case 0xF5B5: return "Terminate Serial Input";
              case 0xF5BC: return "Print 'saving'";
              case 0xF5C8: return "Save to Tape";
              case 0xF5F8: return "-udtim-";
              case 0xF63D: return "Watch For RUN or Shift";
              case 0xF65E: return "-rdtim-";
              case 0xF665: return "-settim-";
              case 0xF66E: return "-stop-";
              case 0xF67C: return "Print 'too many files'";
              case 0xF67F: return "Print 'file open'";
              case 0xF682: return "Print 'file not open'";
              case 0xF685: return "Print 'file not found'";
              case 0xF688: return "Print 'device not present'";
              case 0xF68B: return "Print 'not input file'";
              case 0xF68E: return "Print 'not output file'";
              case 0xF691: return "Print 'missing file name'";
              case 0xF694: return "Print 'illegal device no'";
              case 0xF697: return "Error #0";
              case 0xF6B0: return "Messages";
              case 0xF71E: return "Print If Direct";
              case 0xF722: return "Print I/O Message";
              case 0xF731: return "-setnam-";
              case 0xF738: return "-setlfs-";
              case 0xF73F: return "Set Load/Save Bank";
              case 0xF744: return "-rdst-";
              case 0xF757: return "Set Status Bit";
              case 0xF75C: return "-setmsg-";
              case 0xF75F: return "Set Serial Timeout";
              case 0xF763: return "-memtop-";
              case 0xF772: return "-membot-";
              case 0xF781: return "-iobase-";
              case 0xF786: return "Search For SA";
              case 0xF79D: return "Search & Set Up File";
              case 0xF7A5: return "Trigger DMA";
              case 0xF7AE: return "Get Char From Memory";
              case 0xF7BC: return "Store Loaded Byte";
              case 0xF7C9: return "Read Byte to be Saved";
              case 0xF7D0: return "Get Char From Memory Bank";
              case 0xF7DA: return "Store Char to Memory Bank";
              case 0xF7E3: return "Compare Char With Memory Bank";
              case 0xF7F0: return "MMU Bank Configuration Values";
              case 0xF800: return "Subroutines to $02a2-$02fb";
              case 0xF85A: return "DMA Code to $03f0";
              case 0xF867: return "Check Auto Start ROM";
              case 0xF890: return "Check For Boot Disk";
              case 0xF908: return "Print 'booting'";
              case 0xF92C: return "Print '...'";
              case 0xF98B: return "Wind Up Disk Boot";
              case 0xF9B3: return "Read Next Boot Block";
              case 0xF9FB: return "To 2-Digit Decimal";
              case 0xFA08: return "Block Read Command String";
              case 0xFa17: return "Print a Message";
              case 0xFA40: return "NMI Sequence";
              case 0xFA65: return "(IRQ) Normal Entry";
              case 0xFA80: return "Keyboard Matrix Un-Shifted";
              case 0xFAD9: return "Keyboard Matrix Shifted";
              case 0xFB32: return "Keyboard Matrix C-Key";
              case 0xFB8B: return "Keyboard Matrix Control";
              case 0xFBE4: return "Keyboard Matrix Caps-Loc";
              case 0xFC62: return "Patch for Set Up CRTC Registers";
              case 0xFC6F: return "Unused";
              case 0xFC87: return "Init KBD Translation Tables";
              case 0xFCC6: return "DIN Keyboard patch for Key Pickup";
              case 0xFD29: return "DIN Keyboard Matrix Un-Shifted";
              case 0xFD82: return "DIN Keyboard Matrix Shifted";
              case 0xFDDB: return "DIN Keyboard Matrix C-Key";
              case 0xFE34: return "DIN Keyboard Shift Vectors";
              case 0xFE8C: return "Unused";
              case 0xFEFF: return "Patch byte";
              case 0xFEF0: return "MMU Configuration Register";
              case 0xFF01: return "MMU LCR: Bank 0";
              case 0xFF02: return "MMU LCR: Bank 1";
              case 0xFF03: return "MMU LCR: Bank 14";
              case 0xFF04: return "MMU LCR: Bank 14 Over RAM 1";
              case 0xFF05: return "NMI Transfer Entry";
              case 0xFF17: return "IRQ Transfer Entry";
              case 0xFF33: return "Return From Interrupt";
              case 0xFF3D: return "Reset Transfer Entry";           
              case 0xFF47:
              case 0xFF48:     
              case 0xFF49: return "Set up Fast Serial Port for I/O";
              case 0xFF4A:
              case 0xFF4B:     
              case 0xFF4C: return "Close All Logical Files for a device";  
              case 0xFF4D:
              case 0xFF4E:     
              case 0xFF4F: return "Reconfigure System as a C64 (no return)";
              case 0xFF50:
              case 0xFF51:     
              case 0xFF52: return "Initiate DMA Request to External RAM";
              case 0xFF53:
              case 0xFF54:     
              case 0xFF55: return "Boot Load Program From Disk";
              case 0xFF56:
              case 0xFF57:     
              case 0xFF58: return "Call All Function Cards' Cold Start";
              case 0xFF59:
              case 0xFF5A:     
              case 0xFF5B: return "Search Tables For Given LA";
              case 0xFF5C:
              case 0xFF5D:     
              case 0xFF5E: return "Search Tables For Given SA";
              case 0xFF5F:
              case 0xFF60:     
              case 0xFF61: return "Switch Between 40 and 80 Columns (Editor)";
              case 0xFF62:
              case 0xFF63:     
              case 0xFF64: return "Init 80-Col Character RAM (Editor)";
              case 0xFF65:
              case 0xFF66:     
              case 0xFF67: return "Program Function Key (Editor)";
              case 0xFF68:
              case 0xFF69:     
              case 0xFF6A: return "SET Bank For I/O Operations";
              case 0xFF6B:
              case 0xFF6C:     
              case 0xFF6D: return "Lookup MMU Data For Given Bank";
              case 0xFF6E:
              case 0xFF6F:     
              case 0xFF70: return "JSR to Any Bank, RTS to Calling Bank";
              case 0xFF71:
              case 0xFF72:     
              case 0xFF73: return "JMP to Any Bank";
              case 0xFF74:
              case 0xFF75:     
              case 0xFF76: return "LDA (FETVEC),Y FROM Any Bank";
              case 0xFF77:
              case 0xFF78:     
              case 0xFF79: return "STA (STAVEC),Y to Any Bank";
              case 0xFF7A:
              case 0xFF7B:     
              case 0xFF7C: return "CMP (CMPVEC),Y to Any Bank";
              case 0xFF7D:
              case 0xFF7E:     
              case 0xFF7F: return "Print Immediate Utility";
              case 0xFF80: return "Release Number Of KERNAL";
              case 0xFF81:     
              case 0xFF82: 
              case 0xFF83: return "Init Editor & Display";
              case 0xFF84:     
              case 0xFF85: 
              case 0xFF86: return "Init I/O Devices (ports, timers, etc.)";
              case 0xFF87:     
              case 0xFF88: 
              case 0xFF89: return "Initialize RAM And Buffers For System";
              case 0xFF8A:     
              case 0xFF8B: 
              case 0xFF8C: return "Restore Vectors to Initial System";
              case 0xFF8D:     
              case 0xFF8E: 
              case 0xFF8F: return "Change Vectors For USER";
              case 0xFF90:     
              case 0xFF91: 
              case 0xFF92: return "Control O.S. Message";
              case 0xFF93:     
              case 0xFF94: 
              case 0xFF95: return "Send SA After LISTEN";
              case 0xFF96:     
              case 0xFF97: 
              case 0xFF98: return "Send SA After TALK";
              case 0xFF99:     
              case 0xFF9A: 
              case 0xFF9B: return "Set/Read Top Of System RAM";
              case 0xFF9C:     
              case 0xFF9D: 
              case 0xFF9E: return "Set/Read Bottom Of System RAM";
              case 0xFF9F:     
              case 0xFFA0: 
              case 0xFFA1: return "Scan Keyboard (Editor)";
              case 0xFFA2:     
              case 0xFFA3: 
              case 0xFFA4: return "Set Timeout In IEEE (reserved)";
              case 0xFFA5:     
              case 0xFFA6: 
              case 0xFFA7: return "Handshake Serial Byte In";
              case 0xFFA8:     
              case 0xFFA9: 
              case 0xFFAA: return "Handshake Serial Byte Out";
              case 0xFFAB:     
              case 0xFFAC: 
              case 0xFFAD: return "Send UNTALK Out Serial";
              case 0xFFAE:     
              case 0xFFAF: 
              case 0xFFB0: return "Send UNLISTEN Out Serial";
              case 0xFFB1:     
              case 0xFFB2: 
              case 0xFFB3: return "Send LISTEN Out Serial";
              case 0xFFB4:     
              case 0xFFB5: 
              case 0xFFB6: return "Send TALK Out Serial";
              case 0xFFB7:     
              case 0xFFB8: 
              case 0xFFB9: return "Return I/O Status Byte";
              case 0xFFBA:     
              case 0xFFBB: 
              case 0xFFBC: return "Set LA, FA, SA";
              case 0xFFBD:     
              case 0xFFBE: 
              case 0xFFBF: return "Set Length And File Name Address";
              case 0xFFC0:     
              case 0xFFC1: 
              case 0xFFC2: return "OPEN Logical File";
              case 0xFFC3:     
              case 0xFFC4: 
              case 0xFFC5: return "CLOSE Logical File";
              case 0xFFC6:     
              case 0xFFC7: 
              case 0xFFC8: return "Set Channel In";
              case 0xFFC9:     
              case 0xFFCA: 
              case 0xFFCB: return "Set Channel Out";
              case 0xFFCC:     
              case 0xFFCD: 
              case 0xFFCE: return "Restore Default I/O Channel";
              case 0xFFCF:     
              case 0xFFD0: 
              case 0xFFD1: return "INPUT From Channel";
              case 0xFFD2:     
              case 0xFFD3: 
              case 0xFFD4: return "OUTPUT To Channel";
              case 0xFFD5:     
              case 0xFFD6: 
              case 0xFFD7: return "LOAD From File";
              case 0xFFD8:     
              case 0xFFD9: 
              case 0xFFDA: return "SAVE to File";
              case 0xFFDB:     
              case 0xFFDC: 
              case 0xFFDD: return "Set Internal Clock";
              case 0xFFDE:     
              case 0xFFDF: 
              case 0xFFE0: return "Read Internal Clock";
              case 0xFFE1:     
              case 0xFFE2: 
              case 0xFFE3: return "Scan STOP Key";
              case 0xFFE4:     
              case 0xFFE5: 
              case 0xFFE6: return "Read Buffered Data";
              case 0xFFE7:     
              case 0xFFE8: 
              case 0xFFE9: return "Close All Files And Channels";
              case 0xFFEA:     
              case 0xFFEB: 
              case 0xFFEC: return "Increment Internal Clock";
              case 0xFFED:     
              case 0xFFEE: 
              case 0xFFEF: return "Return Screen Window Size (Editor)";
              case 0xFFF0:     
              case 0xFFF1: 
              case 0xFFF2: return "Read/Set X,Y Cursor Coord (Editor)";
              case 0xFFF3:     
              case 0xFFF4: 
              case 0xFFF5: return "Return I/O Base";
              case 0xFFF8: 
              case 0xFFF9: return "Operating System Vector (RAM1)";    
              case 0xFFFA: 
              case 0xFFFB: return "Processor NMI Vector";
              case 0xFFFC:     
              case 0xFFFD: return "Processor RESET Vector";
              case 0xFFFE: 
              case 0xFFFF: return "Processor IRQ/BRK Vector";
              
              
              default:  
                if ((addr>=0x19) && (addr<=0x23)) return "Stack for temporary strings";
                if ((addr>=0x59) && (addr<=0x62)) return "Miscellaneous numeric work area";
                if ((addr>=0x100) && (addr<=0x10F)) return "Tape Read Errors, Area to build filename in (16 bytes)";
                if ((addr>=0x100) && (addr<=0x1FF)) return "System Stack";
                if ((addr>=0x200) && (addr<=0x2A1)) return "BASIC & Monitor input buffer";
                if ((addr>=0x2A2) && (addr<=0x2AE)) return "Bank Peek Subroutine (Kernal RAM)";
                if ((addr>=0x2AF) && (addr<=0x2BD)) return "Bank Poke Subroutine";
                if ((addr>=0x2BE) && (addr<=0x2CC)) return "Bank Compare Subroutine";
                if ((addr>=0x2CD) && (addr<=0x2E2)) return "JSR to Another Bank";
                if ((addr>=0x2E3) && (addr<=0x2FB)) return "JMP to Another Bank";
                if ((addr>=0x34A) && (addr<=0x353)) return "IRQ Keyboard Buffer (10 Bytes)  FF = No key";
                if ((addr>=0x354) && (addr<=0x35D)) return "Bitmap Of TAB Stops (10 Bytes)";
                if ((addr>=0x362) && (addr<=0x36B)) return "Logical File Number Table";
                if ((addr>=0x36C) && (addr<=0x375)) return "Device Number Table";
                if ((addr>=0x376) && (addr<=0x37F)) return "Secondary Addresse Table";
                if ((addr>=0x380) && (addr<=0x39E)) return "CHRGET Subroutine";
                if ((addr>=0x39F) && (addr<=0x3AA)) return "Fetch From RAM Bank 0";
                if ((addr>=0x3AB) && (addr<=0x3B6)) return "Fetch From RAM Bank 1";
                if ((addr>=0x3B7) && (addr<=0x3BF)) return "Index1 Indirect Fetch From RAM Bank 1";
                if ((addr>=0x3C0) && (addr<=0x3C8)) return "Index2 Indirect Fetch From RAM Bank 0";
                if ((addr>=0x3C9) && (addr<=0x3D1)) return "Txtptr Fetch From RAM Bank 0";
                if ((addr>=0x3F0) && (addr<=0x3F6)) return "DMA Link Code";
                if ((addr>=0x400) && (addr<=0x7E7)) return "VIC 40-Column Text Screen";
                if ((addr>=0x7E8) && (addr<=0x7fF)) return "Sprite Identity Pointers For Text Mode";
                if ((addr>=0x800) && (addr<=0x9FF)) return "BASIC Pseudo Stack (gosub and loop addresses and commands)";
                if ((addr>=0xA40) && (addr<=0xA5A)) return "40/80 Pointer Swap (to E0-FA)";
                if ((addr>=0xA60) && (addr<=0xA6D)) return "40/80 Data Swap (0354-0361)";
                if ((addr>=0xB00) && (addr<=0xBBF)) return "Cassette Buffer";
                if ((addr>=0xC00) && (addr<=0xDFF)) return "RS-232 Input, Output Buffers";
                if ((addr>=0xE00) && (addr<=0xFFF)) return "System Sprites (56-63)";
                if ((addr>=0x1000) && (addr<=0x1009)) return "Programmed Key Lenghts";
                if ((addr>=0x100A) && (addr<=0x10FF)) return "Programmed Key Definitions";
                if ((addr>=0x1100) && (addr<=0x1130)) return "DOS Command Staging Area";
                if ((addr>=0x1131) && (addr<=0x116E)) return "Graphics Work Area";
                if ((addr>=0x1178) && (addr<=0x1197)) return "Graphics Index";
                if ((addr>=0x117E) && (addr<=0x11D5)) return "Sprite Motion Tables (8 x 11 bytes)";
                if ((addr>=0x11D6) && (addr<=0x11E5)) return "Sprite X/Y Positions";
                if ((addr>=0x11EE) && (addr<=0x11FF)) return "Unused";
                if ((addr>=0x1239) && (addr<=0x123E)) return "Current Envelope Pattern";
                if ((addr>=0x123F) && (addr<=0x1270)) return "AD(SR) Pattern";
                if ((addr>=0x1249) && (addr<=0x1252)) return "(AD)SR Pattern";
                if ((addr>=0x1253) && (addr<=0x125C)) return "Waveform Pattern";
                if ((addr>=0x125D) && (addr<=0x1266)) return "Pulse Width Lo Pattern";
                if ((addr>=0x1267) && (addr<=0x1270)) return "Pulse Width Hi Pattern";
                if ((addr>=0x1279) && (addr<=0x127E)) return "Collision IRQ Address Tables";
                if ((addr>=0x1300) && (addr<=0x17FF)) return "Application Program Area";
                if ((addr>=0x1800) && (addr<=0x1BFF)) return "Application Program Area/Reserved for Key Functions";
                if ((addr>=0x1C00) && (addr<=0x1FF7)) return "Video Color Matrix For Graphics Mode";
                if ((addr>=0x1FF8) && (addr<=0x1FFF)) return "Sprite Identity Pointers For Graphics Mode";
                if ((addr>=0x2000) && (addr<=0x3FFF)) return "Screen Memory For Graphics Mode";
                if ((addr>=0xD800) && (addr<=0xDBFF)) return "Color RAM (Nybbles)";
            }      
      }
    }  
    return super.dcom(iType, aType, addr, value);       
  }
}
