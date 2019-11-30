/**
 * @(#)C64Dasm.java 1999/08/21
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
 * Comment the memory location of C64 for the disassembler
 * It performs also a multy language comments.
 *
 * @author Ice
 * @version 1.00 21/08/1999
 */
public class C64Dasm extends M6510Dasm {
  // Available language
  public static final byte LANG_ENGLISH=1;
  public static final byte LANG_ITALIAN=2;

  /**
   * Actual selected language (default=english)
   */
  public byte language=LANG_ENGLISH;

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
              case 0x01: return  "Registro I/O 6510";
              case 0x03:
              case 0x04: return  "Vettore salti: Conversione reale-intero";
              case 0x05:
              case 0x06: return "Vettore salti: Conversione intero-reale";
              case 0x07: return "Carattere di ricerca";
              case 0x08: return "Flag: Cerca le virgolette alla fine di una stringa";
              case 0x09: return "Colonna di schermo dopo l'ultimo TAB";
              case 0x0A: return "Flag: 0=LOAD, 1=VERIFY";
              case 0x0B: return "Puntatore buffer di input/numero indici";
              case 0x0C: return "Flag: dimensione default per DIM";
              case 0x0D: return "Tipo di dato: case 0xFF=Stringa, case 0x00=Numerico";
              case 0x0E: return "Tipo di dato: case 0x80=Intero, case 0x00=Reale";
              case 0x0F: return "Flag per DATA/LIST";
              case 0x10: return "Flag: Riferimento indice/Chiamata di funzione utente";
              case 0x11: return "Flag: case 0x00=INPUT, case 0x40=GET, case 0x98=READ";
              case 0x12: return "Flag: Simbolo TAN/Risultato di un confronto";
              case 0x13: return "Flag: richista di INPUT";
              case 0x14:
              case 0x15: return "Transiente: Valore intero";
              case 0x16: return "Puntatore: Stack stringhe transienti";
              case 0x17:
              case 0x18: return "Ultimo indirizzo stringhe transienti";
              case 0x19:
              case 0x20:
              case 0x21: return "Stack stringhe transienti";
              case 0x22:
              case 0x23:
              case 0x24:
              case 0x25: return "Area puntatori programmi di utilit�";
              case 0x26:
              case 0x27:
              case 0x28:
              case 0x29:
              case 0x2A: return "Prodotto di moltiplicazione reale";
              case 0x2B:
              case 0x2C: return "Puntatore: inizio del programma BASIC";
              case 0x2D:
              case 0x2E: return "Puntatore: inizio variabili del BASIC";
              case 0x2F:
              case 0x30: return "Puntatore: inzio degli array del BASIC";
              case 0x31:
              case 0x32: return "Puntatore: fine degli array del BASIC";
              case 0x33:
              case 0x34: return "Puntatore: inizio delle stringhe del BASIC";
              case 0x35:
              case 0x36: return "Puntatore: stringhe per programmi ausiliari";
              case 0x37:
              case 0x38: return "Puntatore: fine della memoria BASIC";
              case 0x39:
              case 0x3A: return "Numero di linea corrente del BASIC";
              case 0x3B:
              case 0x3C: return "Numero di linea precedente del BASIC";
              case 0x3D:
              case 0x3E: return "Puntatore: istruzione BASIC per CONT";
              case 0x3F:
              case 0x40: return "Numero di linea DATA corrente";
              case 0x41:
              case 0x42: return "Puntatore: indirizzo elemento corrente di DATA";
              case 0x43:
              case 0x44: return "Vettore: routine di INPUT";
              case 0x45:
              case 0x46: return "Nome variabile corrente del BASIC";
              case 0x47:
              case 0x48: return "Puntatore: dato variabile corrente del BASIC";
              case 0x49:
              case 0x4A: return "Puntatore: variabile per il FOR..NEXT";
              case 0x4B:
              case 0x4C: return "Puntatore BASIC per la scratch area";
              case 0x4D: return "Accumulatore per la comparazione dei simboli";
              case 0x54:
              case 0x55:
              case 0x56: return "Vettore di fusione jump";
              case 0x61: return "Accumulatore floating point #1: Esponente";
              case 0x62:
              case 0x63:
              case 0x64:
              case 0x65: return "Accumulatore floating point #1: Mantissa";
              case 0x66: return "Accumulatore floating point #1: Segno";
              case 0x67: return "Puntatore: costante di valutazione delle serie";
              case 0x68: return "Accumulatore floating point #1: Cifra di overflow";
              case 0x69: return "Accumulatore floating point #2: Esponente";
              case 0x6A:
              case 0x6B:
              case 0x6C:
              case 0x6D: return "Accumulatore floating point #2: Mantissa";
              case 0x6E: return "Accumulatore floating point #2: Segno";
              case 0x6F: return "Risultato confronto segno di #1 con #2";
              case 0x70: return "Byte basso #1 (arrotondamento)";
              case 0x71:
              case 0x72: return "Puntatore: buffer cassetta";
              case 0x8B: case 0x8C:
              case 0x8D: case 0x8E:
              case 0x8F: return "Valore reale del seme di RND";
              case 0x90: return "Statusbyte ST dell'I/O KERNAL";
              case 0x91: return "Flag: tasto STOP/ tasto RVS";
              case 0x92: return "Costante (timeout) di misura del tempo per nastro";
              case 0x93: return "Flag: 0=LOAD, 1=VERIFY";
              case 0x94: return "Flag: Bus seriale - Carattere bufferizzato in out";
              case 0x95: return "Carattere bufferizzato per bus seriale";
              case 0x96: return "Numero (EOT) di sincronismo cassetta";
              case 0x97: return "Memoria di registro";
              case 0x98: return "Numero file aperti/Indice della tabella dei file";
              case 0x99: return "Dispositivo di input (default=0)";
              case 0x9A: return "Dispositivo di output (CMD=3)";
              case 0x9B: return "Parità carattere nastro";
              case 0x9C: return "Flag: Ricevuto byte da nastro";
              case 0x9D: return "Flag: 80=modo diretto 00=modo programma";
              case 0x9E: return "Registro errore passo 1 del nastro";
              case 0x9F: return "Registro errore passo 2 del nastro";
              case 0xA0:
              case 0xA1:
              case 0xA2: return "Clock in tempo reale HMS (1/60 sec)";
              case 0xA3: return "Contatore seriale di Bit: Flag EOI";
              case 0xA4: return "Ciclo del contatore";
              case 0xA5: return "Contatore a ritroso sincronismo Write cassetta";
              case 0xA6: return "Puntatore: Buffer di I/O del nastro";
              case 0xA7: return "Bit di input RS-232/Cassetta transiente";
              case 0xA8: return "Contatore bit input RS-232/Cassetta transiente";
              case 0xA9: return "Indicatore RS-232: Controllo dei bit di partenza";
              case 0xAA: return "Buffer per byte input RS-232/Cassetta transiente";
              case 0xAB: return "Parità input RS-232/Contatore corto cassetta";
              case 0xAC:
              case 0xAD: return "Puntatore: Buffer nastro/Scorrimento schermo";
              case 0xAE:
              case 0xAF: return "Indirizzi di fine nastro/Fine programma";
              case 0xB0:
              case 0xB1: return "Costanti di misura del tempo per nastro";
              case 0xB2:
              case 0xB3: return "Puntatore: inizio buffer per nastro";
              case 0xB4: return "Contatore bit output RS-232/Temporizzatore nastro";
              case 0xB5: return "Prossimo bit RS-232 da mandare/EOT del nastro";
              case 0xB6: return "Buffer del byte di output dell'RS-232";
              case 0xB7: return "Lunghezza del nome file corrente";
              case 0xB8: return "Numero file logico corrente";
              case 0xB9: return "Indirizzo secondario corrente";
              case 0xBA: return "Numero del dispositivo corrente";
              case 0xBB:
              case 0xBC: return "Puntatore: nome del file corrente";
              case 0xBD: return "Parità output dell'RS-232/Cassetta transiente";
              case 0xBE: return "Contatore blocco Read/Write cassetta";
              case 0xBF: return "Buffer parola seriale";
              case 0xC0: return "Arresto motore del nastro";
              case 0xC1:
              case 0xC2: return "Indirizzo partenza dell'I/O";
              case 0xC3:
              case 0xC4: return "Carico nastro transiente";
              case 0xC5: return "Tasto corrente premuto: CHRcase 0x(n) 0=nessun tasto";
              case 0xC6: return "Numero di caratteri nel buffer tastiera";
              case 0xC7: return "Flag: Stampa caratteri inversi: 1=si 0=non usato";
              case 0xC8: return "Puntatore: fine linea logica per INPUT";
              case 0xC9:
              case 0xCA: return "Posizione (X,Y) del cursore";
              case 0xCB: return "Flag: stampa i caratteri con SHIFT abassato";
              case 0xCC: return "Abilitatore lampeggio: 0=lampeggio";
              case 0xCD: return "Timer: conto alla rovescia per cursore bistabile";
              case 0xCE: return "Carattere sotto il cursore";
              case 0xCF: return "Flag: Ultima impostazione cursore (lampeggio/fisso)";
              case 0xD0: return "Flag: INPUT o GET da tastiera";
              case 0xD1:
              case 0xD2: return "Puntatore: indirizzo linea di schermo corrente";
              case 0xD3: return "Colonna del cursore sulla linea corrente";
              case 0xD4: return "Flag: Editor modo \"quote\", case 0x00=NO";
              case 0xD5: return "Lunghezza linea di schermo fisica";
              case 0xD6: return "Numero linea dove il cursore risiede";
              case 0xD7: return "Ultimo tasto/checksum/buffer";
              case 0xD8: return "Flag: Modo Inserimento";
              case 0xF1: return "Linee di schermo non vere";
              case 0xF2: return "Label delle linee di schermo";
              case 0xF3:
              case 0xF4: return "Puntatore: locazione corrente RAM colore schermo";
              case 0xF5:
              case 0xF6: return "Vettore: Tavola di decodificazione della tastiera";
              case 0xF7:
              case 0xF8: return "Puntatore al buffer di input dell'RS-232";
              case 0xF9:
              case 0xFA: return "Puntatore al buffer di output dell'RS-232";
              case 0xFB:
              case 0xFE: return "Libera Pagina 0 per progammi Utente";
              case 0xFF: return "Area dati transiente del BASIC";
              case 0x281:
              case 0x282: return "Puntatore: Base della memoria per Sistema Operativo";
              case 0x283:
              case 0x284: return "Puntatore: Cima della memoria per Sistema Operativo";
              case 0x285: return "Flag KERNAL: supero Tempo dell'IEEE";
              case 0x286: return "Codice colore del carattere corrente";
              case 0x287: return "Colore di fondo sotto il cursore";
              case 0x288: return "Cima della memoria schermo (pagina)";
              case 0x289: return "Misura del buffer della tastiera";
              case 0x28A: return "Flag: ripete il tasto battuto, case 0x80=tutti i tasti";
              case 0x28B: return "Ripete il contatore velocità";
              case 0x28C: return "Ripete il contatore ritardo";
              case 0x28D: return "Flag: Tasto SHIFT/CTRL/Commodore";
              case 0x28E: return "Ultima configurazione SHIFT della tastiera";
              case 0x28F:
              case 0x290: return "Vettore: Preparazione tabella tastiera";
              case 0x291: return "Flag: modo SHIFT: 00=Disabilita case 0x80=abilita";
              case 0x292: return "Flag: scorrimento automatico verso il basso: 0=ON";
              case 0x293: return "RS-232: Immagine del registro di controllo del 6551";
              case 0x294: return "RS-232: Immagine del registro di comando del 6551";
              case 0x295:
              case 0x296: return "BPS RS-232 USA non standard (Tempo/2-100)";
              case 0x297: return "RS-232: Immagine del registro di stato del 6551";
              case 0x298: return "Numero di bit dell'RS-232 rimasti da inviare";
              case 0x299:
              case 0x29A: return "Baud Rate dell'RS-232";
              case 0x29B: return "Indice RS-232 per termine buffer input";
              case 0x29C: return "Inizio del buffer di input RS-232 (pagina)";
              case 0x29D: return "Inizio del buffer di output RS-232 (pagina)";
              case 0x29E: return "Indice RS-232 per termine buffer output";
              case 0x29F:
              case 0x2A0: return "Contiene il vettore IRQ durante l'I/O del nastro";
              case 0x2A1: return "Abilita l'RS-232";
              case 0x2A2: return "Lettura di TOD durante I/O cassetta";
              case 0x2A3: return "Memorizzazione transiente per lettura cassetta";
              case 0x2A4: return "Indicatore di IRQ transiente per lettura cassetta";
              case 0x2A5: return "Transiente per indice di linea";
              case 0x2A6: return "Indicatore PAL/NTSC, 0=NTSC, 1=PAL";
              case 0x300:
              case 0x301: return "Vettore: Stampa i messaggi di errore del BASIC";
              case 0x302:
              case 0x303: return "Vettore: Partenza a caldo del BASIC";
              case 0x304:
              case 0x305: return "Vettore: Testo BASIC \"tokenizzato\"";
              case 0x306:
              case 0x307: return "Vettore: Lista del testo BASIC";
              case 0x308:
              case 0x309: return "Vettore: Invio caratteri BASIC";
              case 0x30A:
              case 0x30B: return "Vettore: Valutazione \"token\" del BASIC";
              case 0x30C: return "Memorizzazione del registro A del 6502";
              case 0x30D: return "Memorizzazione del registro X del 6502";
              case 0x30E: return "Memorizzazione del registro Y del 6502";
              case 0x30F: return "Memorizzazione del registro P del 6502";
              case 0x310: return "Istruzione di salto della funzione USR";
              case 0x311:
              case 0x312: return "Byte basso/alto dell'istruzione di USR";
              case 0x313: return "Non usato";
              case 0x314:
              case 0x315: return "Vettore: Hardware Interrupt (IRQ)";
              case 0x316:
              case 0x317: return "Vettore: Break Interrupt";
              case 0x318:
              case 0x319: return "Vettore: Non maskerable Interrupt (NMI)";
              case 0x31A:
              case 0x31B: return "Vettore routine OPEN del KERNAL";
              case 0x31C:
              case 0x31D: return "Vettore routine CLOSE del KERNAL";
              case 0x31E:
              case 0x31F: return "Vettore routine CHKIN del KERNAL";
              case 0x320:
              case 0x321: return "Vettore routine CHKOUT del KERNAL";
              case 0x322:
              case 0x323: return "Vettore routine CLRCHN del KERNAL";
              case 0x324:
              case 0x325: return "Vettore routine CHRIN del KERNAL";
              case 0x326:
              case 0x327: return "Vettore routine CHROUT del KERNAL";
              case 0x328:
              case 0x329: return "Vettore routine STOP del KERNAL";
              case 0x32A:
              case 0x32B: return "Vettore routine GETIN del KERNAL";
              case 0x32C:
              case 0x32D: return "Vettore routine CLALL del KERNAL";
              case 0x32E:
              case 0x32F: return "Vettore definito dall'utente";
              case 0x330:
              case 0x331: return "Vettore routine LOAD del KERNAL";
              case 0x332:
              case 0x333: return "Vettore routine SAVE del KERNAL";
              case 0xA000: return "Vettore Routine: imposta vet. Basic e stampa inizio";
              case 0xA002: return "Vettore Routine: chiude canali, partenza a caldo";
              case 0xA435: return "Routine: Stampa errore (OUT OF MEMORY)";
              case 0xA437: return "Routine: Stampa i messaggi di errore del BASIC";
              case 0xA43A: return "Routine: Default stampa messaggi errore BASIC";
              case 0xA469: return "Routine: Stampa stringa (YA),+ 'IN'+numero linea";
              case 0xA474: return "Routine: Stampa READY,partenza a caldo";
              case 0xA483: return "Routine: Partenza a caldo del BASIC";
              case 0xA560: return "Routine: Legge 89 caratteri di input di comando BASIC";
              case 0xA579: return "Routine: Testo BASIC \"tokenizzato\" (da case 0x0304)";
              case 0xA57C: return "Routine: Testo BASIC \"tokenizzato\"";
              case 0xA642: return "Routine: Istruzione NEW";
              case 0xA65E: return "Routine: Istruzione CLR";
              case 0xA69C: return "Routine: Istruzione LIST";
              case 0xA71A: return "Lista del testo BASIC";
              case 0xA742: return "Routine: Istruzione FOR";
              case 0xA7E4: return "Invio caratteri BASIC";
              case 0xA81D: return "Routine: Istruzione RESTORE";
              case 0xA82F: return "Routine: Istruzione STOP";
              case 0xA831: return "Routine: Istruzione END";
              case 0xA857: return "Routine: Istruzione CONT";
              case 0xA871: return "Routine: Istruzione RUN";
              case 0xA883: return "Routine: Istruzione GOSUB";
              case 0xA8A0: return "Routine: Istruzione GOTO";
              case 0xA8D2: return "Routine: Istruzione RETURN";
              case 0xA8F8: return "Routine: Istruzione DATA";
              case 0xA906: return "Routine: Cerca caratere ':' sulla stringa (posizione)";
              case 0xA909: return "Routine: Cerca caratere 0 sulla stringa (posizione)";
              case 0xA928: return "Routine: Istruzione IF";
              case 0xA93B: return "Routine: Istruzione REM";
              case 0xA94B: return "Routine: Istruzione ON";
              case 0xA9A5: return "Routine: Istruzione LET";
              case 0xAA80: return "Routine: Istruzione PRINT#";
              case 0xAA86: return "Routine: Istruzione CMD";
              case 0xAAA0: return "Routine: Istruzione PRINT";
              case 0xAACA: return "Routine: Mette un terminatore (0) sulla stringa letta";
              case 0xAAD7: return "Routine: Stampa effetto return e/o avanzamento";
              case 0xAB42: return "Routine: Immette tasto DX nel canale+ errori e char";
              case 0xAB45: return "Routine: Immette '?' nel canale+ errori e char";
              case 0xAB47: return "Routine: Immette un carat. nel canale+ errori e char";
              case 0xAB7B: return "Routine: Istruzione GET";
              case 0xABA5: return "Routine: Istruzione INPUT#";
              case 0xABBF: return "Routine: Istruzione INPUT";
              case 0xAC06: return "Routine: Istruzione READ";
              case 0xAD1E: return "Routine: Istruzione NEXT";
              case 0xAD8D: return "Routine: Verifica se dato (A) di tipo Numerico+ errore";
              case 0xAD8F: return "Routine: Verifica se dato (A) di tipo Stringa+ errore";
              case 0xAE86: return "Routine: Valutazione \"token\" del BASIC";
              case 0xAEF7: return "Routine: Verifica se c'è ')' nel char corrente (Syntax)";
              case 0xAEFA: return "Routine: Verifica se c'è '(' nel char corrente (Syntax)";
              case 0xAEFD: return "Routine: Verifica se c'è ',' nel char corrente (Syntax)";
              case 0xAEFF: return "Routine: Verifica se A è nel char corrente (Syntax)";
              case 0xAF08: return "Routine: Stampa errore (SYNTAX)";
              case 0xB081: return "Routine: Istruzione DIM";
              case 0xB08B: return "Routine: Verifica se c'è una variabile";
              case 0xB090: return "Routine: Verifica se c'è una variabile per DIM";
              case 0xB092: return "Routine: Verifica se c'è una variabile (A=par)";
              case 0xB113: return "Routine: Verifica se il carattere in A è da 'A'..'Z'";
              case 0xB1AA: return "Routine: Conversione Floating Point -> Intero";
              case 0xB245: return "Routine: Stampa errore (BAD SUBSCRIPT)";
              case 0xB248: return "Routine: Stampa errore (ILLEGAL QUANTITY)";
              case 0xB37D: return "Routine: Funzione FRE";
              case 0xB391: return "Routine: Conversione Intero->Floating Point";
              case 0xB39E: return "Routine: Funzione POS";
              case 0xB3B3: return "Routine: Istruzione DEF";
              case 0xB465: return "Routine: Funzione STRcase 0x";
              case 0xB6EC: return "Routine: Funzione CHRcase 0x";
              case 0xB700: return "Routine: Funzione LEFTcase 0x";
              case 0xB72C: return "Routine: Funzione RIGTHcase 0x";
              case 0xB737: return "Routine: Funzione MODcase 0x";
              case 0xB77C: return "Routine: Funzione LEN";
              case 0xB78B: return "Routine: Funzione ASC";
              case 0xB7AD: return "Routine: Funzione VAL";
              case 0xB80D: return "Routine: Funzione PEEK";
              case 0xB824: return "Routine: Istruzione POKE";
              case 0xB82D: return "Routine: Istruzione WAIT";
              case 0xB97E: return "Routine: Stampa errore (OVERFLOW)";
              case 0xB9EA: return "Routine: Funzione LOG";
              case 0xBB8A: return "Routine: Stampa DIVISION BY ZERO";
              case 0xBC2B: return "Routine: Determina se numero =0 (ZF) o +/- (CF)";
              case 0xBC39: return "Routine: Funzione SGN";
              case 0xBC58: return "Routine: Funzione ABS";
              case 0xBCCC: return "Routine: Funzione INT";
              case 0xBDC2: return "Routine: Stampa 'IN' + linea basic";
              case 0xBDCD: return "Routine: Stampa stringa corrispondente intero (AX)";
              case 0xBDDA: return "Routine: Stampa stringa (YA=pointer)";
              case 0xBF71: return "Routine: Funzione SQR";
              case 0xBFED: return "Routine: Funzione EXP";

              case 0xD000: return "Posizione X sprite 0";
              case 0xD001: return "Posizione Y sprite 0";
              case 0xD002: return "Posizione X sprite 1";
              case 0xD003: return "Posizione Y sprite 1";
              case 0xD004: return "Posizione X sprite 2";
              case 0xD005: return "Posizione Y sprite 2";
              case 0xD006: return "Posizione X sprite 3";
              case 0xD007: return "Posizione Y sprite 3";
              case 0xD008: return "Posizione X sprite 4";
              case 0xD009: return "Posizione Y sprite 4";
              case 0xD00A: return "Posizione X sprite 5";
              case 0xD00B: return "Posizione Y sprite 5";
              case 0xD00C: return "Posizione X sprite 6";
              case 0xD00D: return "Posizione Y sprite 6";
              case 0xD00E: return "Posizione X sprite 7";
              case 0xD00F: return "Posizione Y sprite 7";
              case 0xD010: return "Posizione X MSB sprites 0..7";
              case 0xD011: return "Registro di controllo del VIC";
              case 0xD012: return "Lettura/Scrittura del valore di quadro per IRQ";
              case 0xD013: return "Posizione X del \"latch\" penna ottica";
              case 0xD014: return "Posizione Y del \"latch\" penna ottica";
              case 0xD015: return "Abilitatore Sprites";
              case 0xD016: return "Registro di controllo del VIC";
              case 0xD017: return "Espansione (2X) verticale (Y) sprite 0..7";
              case 0xD018: return "Registro di controllo della memoria del VIC";
              case 0xD019: return "Registro indicatore di interruzione";
              case 0xD01A: return "Registro maschera IRQ";
              case 0xD01B: return "Priorit� di schermo sprite-fondo";
              case 0xD01C: return "Seleziona il modo multicolore per sprite 0..7";
              case 0xD01D: return "Espansione (2X) orrizzontale (X) sprite 0..7";
              case 0xD01E: return "Scoperta contatto fra due animazioni";
              case 0xD01F: return "Scoperta di contatto animazione-fondo";
              case 0xD020: return "Colore del bordo";
              case 0xD021: return "Colore di fondo 0";
              case 0xD022: return "Colore di fondo 1";
              case 0xD023: return "Colore di fondo 2";
              case 0xD024: return "Colore di fondo 3";
              case 0xD025: return "Registro 0 animazione multicolore";
              case 0xD026: return "Registro 1 animazione multicolore";
              case 0xD027: return "Colore sprite 0";
              case 0xD028: return "Colore sprite 1";
              case 0xD029: return "Colore sprite 2";
              case 0xD02A: return "Colore sprite 3";
              case 0xD02B: return "Colore sprite 4";
              case 0xD02C: return "Colore sprite 5";
              case 0xD02D: return "Colore sprite 6";
              case 0xD02E: return "Colore sprite 7";

              case 0xD400: return "Voce 1: Controllo frequenza (byte basso)";
              case 0xD401: return "Voce 1: Controllo frequenza (byte alto)";
              case 0xD402: return "Voce 1: Ampiezza forma d'onda pulsazione (byte basso)";
              case 0xD403: return "Voce 1: Ampiezza forma d'onda pulsazione (semibyte alto)";
              case 0xD404: return "Voce 1: Registri di controllo";
              case 0xD405: return "Generatore 1 invilutto: Attack/Decay";
              case 0xD406: return "Generatore 1 invilutto: Sustain/Release";
              case 0xD407: return "Voce 2: Controllo frequenza (byte basso)";
              case 0xD408: return "Voce 2: Controllo frequenza (byte alto)";
              case 0xD409: return "Voce 2: Ampiezza forma d'onda pulsazione (byte basso)";
              case 0xD40A: return "Voce 2: Ampiezza forma d'onda pulsazione (semibyte alto)";
              case 0xD40B: return "Voce 2: Registri di controllo";
              case 0xD40C: return "Generatore 2 inviluppo: Attack/Decay";
              case 0xD40D: return "Generatore 2 inviluppo: Sustain/Release";
              case 0xD40E: return "Voce 3: Controllo frequenza (byte basso)";
              case 0xD40F: return "Voce 3: Controllo frequenza (byte alto)";
              case 0xD410: return "Voce 3: Ampiezza forma d'onda pulsazione (byte basso)";
              case 0xD411: return "Voce 3: Ampiezza forma d'onda pulsazione (semibyte alto)";
              case 0xD412: return "Voce 3: Registri di controllo";
              case 0xD413: return "Generatore 3 inviluppo: Attack/Decay";
              case 0xD414: return "Generatore 3 inviluppo: Sustain/Release";
              case 0xD415: return "Frequenza di taglio del filtro: semibyte basso (bit 2-0)";
              case 0xD416: return "Frequenza di taglio del filtro: semibyte alto";
              case 0xD417: return "Controllo risonanza filtro/Controllo ingresso voce";
              case 0xD418: return "Seleziona modo e volume del filtro";
              case 0xD419: return "Convertitore analogico/digitale: Paddle 1";
              case 0xD41A: return "Convertitore analogico/digitale: Paddle 2";
              case 0xD41B: return "Generatore numeri casuali oscillatore 3";
              case 0xD41C: return "Uscita generatore dell'inviluppo";

              case 0xDC00: return "Porta dati A #1: tastiera, joystick, paddle, penna ottica";
              case 0xDC01: return "Porta dati B #1: tastiera, joystick, paddle";
              case 0xDC02: return "Registro direzione dati porta A #1";
              case 0xDC03: return "Registro direzione dati porta B #1";
              case 0xDC04: return "Timer A #1: Byte basso";
              case 0xDC05: return "Timer A #1: Byte alto";
              case 0xDC06: return "Timer B #1: Byte basso";
              case 0xDC07: return "Timer B #1: Byte alto";
              case 0xDC08: return "Clock tempo del giorno #1: Decimi di secondo";
              case 0xDC09: return "Clock tempo del giorno #1: Secondi";
              case 0xDC0A: return "Clock tempo del giorno #1: Minuti";
              case 0xDC0B: return "Clock tempo del giorno #1: Ore+[indicatore AM/PM]";
              case 0xDC0C: return "Buffer dati I/O seriale sincrono #1";
              case 0xDC0D: return "Registro controllo interruzioni CIA #1";
              case 0xDC0E: return "Registro A di controllo del CIA #1";
              case 0xDC0F: return "Registro B di controllo del CIA #1";

              case 0xDD00: return "Porta dati A #2: bus seriale, RS-232, memoria VIC";
              case 0xDD01: return "Porta dati B #2: porta utente, RS-232";
              case 0xDD02: return "Registro direzione dati porta A #2";
              case 0xDD03: return "Registro direzione dati porta B #2";
              case 0xDD04: return "Timer A #2: Byte basso";
              case 0xDD05: return "Timer A #2: Byte alto";
              case 0xDD06: return "Timer B #2: Byte basso";
              case 0xDD07: return "Timer B #2: Byte alto";
              case 0xDD08: return "Clock tempo del giorno #2: Decimi di secondo";
              case 0xDD09: return "Clock tempo del giorno #2: Secondi";
              case 0xDD0A: return "Clock tempo del giorno #2: Minuti";
              case 0xDD0B: return "Clock tempo del giorno #2: Ore+[indicatore AM/PM]";
              case 0xDD0C: return "Buffer dati I/O seriale sincrono #2";
              case 0xDD0D: return "Registro controllo interruzioni CIA #2";
              case 0xDD0E: return "Registro A di controllo del CIA #2";
              case 0xDD0F: return "Registro B di controllo del CIA #2";

              case 0xE097: return "Routine: Funzione RND";
              case 0xE10C: return "Routine: Immette un carat. nel canale + errori BASIC";
              case 0xE112: return "Routine: Accetta un carat. dal canale+ errori BASIC";
              case 0xE118: return "Routine: Apre il canale di output + errori BASIC";
              case 0xE11E: return "Routine: Apre il canale di input + errori BASIC";
              case 0xE124: return "Routine: Prende il carat. dal buffer tast.+ errori BASIC";
              case 0xE12A: return "Routine: Istruzione SYS";
              case 0xE147: return "Routine: Salva stato corrente 6502";
              case 0xE156: return "Routine: Istruzione SAVE";
              case 0xE165: return "Routine: Istruzione VERIFY";
              case 0xE168: return "Routine: Istruzione LOAD";
              case 0xE1C7: return "Routine: Istruzione CLOSE";
              case 0xE1D4: return "Routine: Istruzione OPEN assumendo Datasette=default";
              case 0xE1DD: return "Routine: Istruzione OPEN";
              case 0xE206: return "Routine: Legge prossimo carattere, se=0 => PLA,PLA";
              case 0xE20E: return "Routine: Verifica se c'� ',' e legge prossimo (Syntax)";
              case 0xE211: return "Routine: Legge prossimo carattere, se =0 =>syntax error";
              case 0xE264: return "Routine: Funzione COS";
              case 0xE26B: return "Routine: Funzione SIN";
              case 0xE2B4: return "Routine: Funzione TAN";
              case 0xE30E: return "Routine: Funzione ATN";
              case 0xE37B: return "Routine (di case 0xA002): chiude canali, partenza a caldo";
              case 0xE386: return "Routine: Stampa READY+ partenza a caldo (da case 0x300)";
              case 0xE38B: return "Routine: Stampa i messaggi di errore del BASIC";
              case 0xE394: return "Routine (di case 0xA000): imposta vet. Basic e stampa inizio";
              case 0xE3A2: return "Routine che viene copiata in case 0x73..case 0x8A";
              case 0xE3BF: return "Routine: imposta istruz. USR e memoria per BASIC";
              case 0xE422: return "Routine: Stampa messaggi iniziali + set della memoria";
              case 0xE453: return "Routine: imposta vettori per BASIC (case 0x300..case 0x309)";
              case 0xE4AD: return "Routine: Apre il canale di output + errori";
              case 0xE4D3: return "Routine: Aggiorna controllo bit partenza/parit�";
              case 0xE4DA: return "Routine: Pone il colore al carattere corrente";
              case 0xE4E0: return "Routine: Attende alcuni secondi o pressione tasto";
              case 0xE500: return "Routine IOBASE del KERNAL";
              case 0xE505: return "Routine SCREEN del KERNAL";
              case 0xE50A: return "Routine PLOT del KERNAL";
              case 0xE518: return "Routine: Predispone editor/cancella schermo";
              case 0xE544: return "Routine: Cancella schermo";
              case 0xE566: return "Routine: Riporta cursore in altro a sinistra";
              case 0xE56C: return "Routine: Goto X=linea, Y=col. del cursore";
              case 0xE5A0: return "Routine: Setta registri VIC6567 e dispositivi";
              case 0xE5BA: return "Routine: Legge carattere dal buffer tastiera";
              case 0xE632: return "Routine: Preleva un carattere dalla tastiera (CRT-Video)";
              case 0xE68A: return "Routine: Attiva/Disattiva Editor modo \"quote\"";
              case 0xE716: return "Routine: Invia un carattere sullo schermo";
              case 0xE87C: return "Routine: Aggiorna schermo come per RETURN";
              case 0xE891: return "Routine: Aggiorna schermo/editor perch� premuto RETURN";
              case 0xE8CD: return "Routine: Cerca (e cambia) codici Ascii colori";
              case 0xE8EA: return "Routine: Scorrimento schermo verso l'alto per nuova riga";
              case 0xE9F0: return "Routine: Calcola indirizzo di linea schermo corrente";
              case 0xE9FF: return "Routine: Stampa messaggi iniziali + set della memoria";
              case 0xEA13: return "Routine: Stampa carattere sul cursore (con parametri)";
              case 0xEA24: return "Routine: Calcola puntatore locazione RAM colore";
              case 0xEA31: return "Default hardware interrupt (IRQ)";
              case 0xEA87: return "Routine SCNKEY del KERNAL";
              case 0xEC44: return "Routine: Controlla passaggio negativo/maiuscolo/SHIFT";
              case 0xED09: return "Routine TALK del KERNAL";
              case 0xED0C: return "Routine LISTEN del KERNAL";
              case 0xED40: return "Routine: Invia un byte nel bus seriale";
              case 0xEDB9: return "Routine SECOND del KERNAL";
              case 0xEDC7: return "Routine TKSA del KERNAL";
              case 0xEDDD: return "Routine CIOUT del KERNAL";
              case 0xEDEF: return "Routine UNTLK del KERNAL";
              case 0xEDFE: return "Routine UNLSN del KERNAL";
              case 0xEE13: return "Routine ACPTR del KERNAL";
              case 0xEE85: return "Routine: Azzera uscita pulsazione clock bus seriale";
              case 0xEE8E: return "Routine: Setta uscita pulsazione clock bus seriale";
              case 0xEE97: return "Routine: Azzera uscita dati bus seriale";
              case 0xEEA0: return "Routine: Setta uscita dati bus seriale";
              case 0xEEA9: return "Routine: Legge nel C ingresso dati bus seriale";
              case 0xEEB3: return "Routine: Ritardo di 931 cicli di clock";
              case 0xF00D: return "Routine: Immagine registro di stato 6551 = case 0x40";
              case 0xF086: return "Routine: prende carattere dal RS-232";
              case 0xF0A4: return "Routine: Controlla per disabilitazione RS-232";
              case 0xF12B: case 0xF12C:
              case 0xF12D: case 0xF12E:
              case 0xF12F: return "Routine: Stampa stringa relativa all'I/O";
              case 0xF13E: return "Routine GETIN del KERNAL";
              case 0xF14E: return "Routine: Prende carattere dal RS-232/Memoria registro";
              case 0xF157: return "Routine CHRIN del KERNAL";
              case 0xF199: return "Routine: Preleva carattere dal nastro";
              case 0xF1AD: return "Routine: Preleva carattere dal bus seriale";
              case 0xF1B8: return "Routine: Prende char dal RS-232/Immagine registro";
              case 0xF1CA: return "Routine CHROUT del KERNAL";
              case 0xF1DD: return "Routine: Invia un carattere su nastro (usa buffer)";
              case 0xF20E: return "Routine CHKIN del KERNAL";
              case 0xF250: return "Routine CHKOUT del KERNAL";
              case 0xF291: return "Routine CLOSE del KERNAL";
              case 0xF2EE: return "Routine: Indirizza dispositivo/Riorganizza tabella";
              case 0xF2F1: return "Routine: Riorganizza tabella file per chiusura";
              case 0xF2F2: return "Routine: Riorganizza tabella file per chiusura (A=par.)";
              case 0xF30F: return "Routine: Cerca indice tabella (X) del file (X=n�file)";
              case 0xF314: return "Routine: Cerca indice tabella (X) del file (A=n�file)";
              case 0xF31F: return "Routine: Rende il file all'indirizzo X attivo";
              case 0xF32F: return "Routine CLALL del KERNAL";
              case 0xF333: return "Routine CLRCHN del KERNAL";
              case 0xF34A: return "Routine OPEN del KERNAL";
              case 0xF3D5: return "Routine: Invia il nome del file sul bus seriale";
              case 0xF47D: return "Routine: Fissa Cima memoria per Sistema Operativo (C=0)";
              case 0xF49E: return "Routine LOAD del KERNAL";
              case 0xF4A5: return "Default routine LOAD del KERNAL";
              case 0xF5AF: return "Routine: Stampa SEARCHING FOR nome file";
              case 0xF5C1: return "Routine: Stampa nome file corrente";
              case 0xF5D2: return "Routine: Stampa stringa LOADING/VERIFING";
              case 0xF5DD: return "Routine SAVE del KERNAL";
              case 0xF5ED: return "Default routine SAVE del KERNAL";
              case 0xF633: return "Routine: Indirizza il dispositivo con il comando (C=1)";
              case 0xF642: return "Routine: Indirizza il dispositivo con il comando";
              case 0xF654: return "Routine: Imposta il bus seriale a non-ricevente";
              case 0xF68F: return "Routine: Stampa SAVING nome file";
              case 0xF69B: return "Routine UDTIM del KERNAL";
              case 0xF6BC: return "Routine: Verifica pressione tasti STOP/RVS";
              case 0xF6DD: return "Routine RDTIM del KERNAL";
              case 0xF6EA: return "Routine SETTIM del KERNAL";
              case 0xF6FB: return "Routine: Chiude tutti canali I/O e Stampa I/O ERROR 1";
              case 0xF6FE: return "Routine: Chiude tutti canali I/O e Stampa I/O ERROR 2";
              case 0xF701: return "Routine: Chiude tutti canali I/O e Stampa I/O ERROR 3";
              case 0xF704: return "Routine: Chiude tutti canali I/O e Stampa I/O ERROR 4";
              case 0xF707: return "Routine: Chiude tutti canali I/O e Stampa I/O ERROR 5";
              case 0xF70A: return "Routine: Chiude tutti canali I/O e Stampa I/O ERROR 6";
              case 0xF70D: return "Routine: Chiude tutti canali I/O e Stampa I/O ERROR 7";
              case 0xF710: return "Routine: Chiude tutti canali I/O e Stampa I/O ERROR 8";
              case 0xF713: return "Routine: Chiude tutti canali I/O e Stampa I/O ERROR 9";
              case 0xF72C: return "Routine: Cerca/verifica valido file intestazione nastro";
              case 0xF76A: return "Routine: Scrive Header per il file (indirizzi+nome)";
              case 0xF7D0: return "Routine: Carica in XY il puntatore inizio buffer nastro";
              case 0xF7EA: return "Routine: Confronta nome file trovato con quello cercato";
              case 0xF7D7: return "Routine: Prepara indirizzi partenza I/O, fine nastro";
              case 0xF80D: return "Routine: Carica in XY puntatore corrente buffer nastro";
              case 0xF817: return "Routine: Attesa (stampa) di PRESS PLAY cassetta";
              case 0xF82E: return "Routine: Lettura interruttore cassetta (Z=presente)";
              case 0xF838: return "Routine: Attesa (stampa) di PRESS RECORD & PLAY cassetta";
              case 0xF841: return "Routine: Legge blocco dati da nastro";
              case 0xF84A: return "Routine: Legge blocco dati da nastro (no indirizzi)";
              case 0xF864: return "Routine: Scrive blocco dati su nastro+ inizializzazione";
              case 0xF867: return "Routine: Scrive blocco dati su nastro (tipo #14)";
              case 0xF86B: return "Routine: Scrive blocco dati su nastro";
              case 0xF8D0: return "Routine: Verifica per spegnere motore (e normali IRQ)";
              case 0xF92C: return "Routine IRQ cassetta per LOAD";
              case 0xFB8E: return "Routine: Setta ^Buffer nastro a ^Ind. partenza I/O";
              case 0xFB97: return "Routine: Inizializza/Azzera variabili per cassetta";
              case 0xFBA6: return "Routine: Predispone TimerB #1 in base al bit di out";
              case 0xFBAD: return "Routine: Attiva Timer B #1(176c) e inverte uscita tape";
              case 0xFBAF: return "Routine: Attiva Timer B #1(A=par) e inverte uscita tape";
              case 0xFBB1: return "Routine: Attiva Timer B #1(AX=par) e inverte uscita tape";
              case 0xFC57: return "Routine: Cambia vettore IRQ a FC6A";
              case 0xFC93: return "Routine: Spegne motore, disabilita int. CIA #1";
              case 0xFCB8: return "Routine: Spegne motore, disab. int. CIA #1, esce int.";
              case 0xFCBD: return "Routine: Cambia vettore IRQ per l'I/O";
              case 0xFCCA: return "Routine: Spegne motore registratore";
              case 0xFCD1: return "Routine: Controlla se ^Buffer nastro=ind. fine nastro";
              case 0xFCDB: return "Routine: Avanza punt. Buffer nastro/Scor. schermo";
              case 0xFCE2: return "Routine: Startup";
              case 0xFD02: return "Routine: Cerca presenza cartuccia";
              case 0xFD15: return "Routine RESTOR del KERNAL";
              case 0xFD1A: return "Routine VECTOR del KERNAL";
              case 0xFD50: return "Routine RAMTAS del KERNAL";
              case 0xFDAE: return "Routine IOINIT del KERNAL";
              case 0xFFDD: return "Routine: Predispone Timer A #1 a 16,7msec, Int. e PB6";
              case 0xFDF9: return "Routine SETNAME del KERNAL";
              case 0xFE00: return "Routine SETLFS del KERNAL";
              case 0xFE07: return "Routine READST del KERNAL";
              case 0xFE18: return "Routine SETMSG del KERNAL";
              case 0xFE1C: return "Routine: Cambia (attiva) stato ST dell'I/O KERNAL";
              case 0xFE21: return "Routine SETTMO del KERNAL";
              case 0xFE25: return "Routine MEMTOP del KERNAL";
              case 0xFE27: return "Routine: XY= Cima della memoria per Sistema Operativo";
              case 0xFE2D: return "Routine: Fissa Cima memoria per Sistema Operativo";
              case 0xFE34: return "Routine MEMBOT del KERNAL";
              case 0xFE43: return "Routine: Non Maskerable Interrupt (NMI)";
              case 0xFE47: return "Default Non maskerable Interrupt (NMI)";
              case 0xFEBC: return "Ripristina registri processore e esce da interrupt";
              case 0xFEC2:
              case 0xFEC3:
              case 0xFEC4: return "Costanti per sistema NTSC";
              case 0xFF2E: return "Routine: Aggiorna Baud Rate dell'RS-232";
              case 0xFF43: return "Routine: Esegue NMI Interrupt Routine (^return in stack)";
              case 0xFF48: return "Masckerable Interrupt (IRQ) Routine";
              case 0xFF5B: return "Routine CINT del KERNAL";
              case 0xFF6E: return "Routine: Abilita Int. Timer #1 e uscita PB6";
              case 0xFF81: return "Routine: Inizializza l'editor di schermo";
              case 0xFF84: return "Routine: Inizializza l'I/O";
              case 0xFF87: return "Routine: Inizializza RAM/ crea buffer nastro/schermo case 0x0400";
              case 0xFF8A: return "Routine: Ripristina il vettore di default di I/O";
              case 0xFF8D: return "Routine: Legge/Imposta il vettore di I/O";
              case 0xFF90: return "Routine: Controlla i messaggi del KERNAL";
              case 0xFF93: return "Routine: Invia l'indirizzo secondario dopo RICEZIONE";
              case 0xFF96: return "Routine: Invia l'indirizzo secondario dopo TRASMISSIONE";
              case 0xFF99: return "Routine: Legge/Imposta la cima della memoria";
              case 0xFF9C: return "Routine: Legge/Imposta la base della memoria";
              case 0xFF9F: return "Routine: Fa la scansione della tastiera";
              case 0xFFA2: return "Routine: Imposta il supero tempo sul bus seriale";
              case 0xFFA5: return "Routine: Accetta un byte dalla porta seriale";
              case 0xFFA8: return "Routine: Trasferisce un byte alla porta seriale";
              case 0xFFAB: return "Routine: Imposta il bus seriale a NON-TRASMITTENTE";
              case 0xFFAE: return "Routine: Imposta il bus seriale a NON-RICEVENTE";
              case 0xFFB1: return "Routine: Dispone a RICEVERE i dispositivi sul bus seriale";
              case 0xFFB4: return "Routine: Imposta a TRASMITTENTE il dispositivo bus seriale";
              case 0xFFB7: return "Routine: Legge la parola di stato I/O";
              case 0xFFBA: return "Routine: Imposta indirizzi primario, secondario, logico";
              case 0xFFBD: return "Routine: Imposta il nome del file";
              case 0xFFC0: return "Routine: Apre un file logico";
              case 0xFFC3: return "Routine: Chiude un file logico specifico";
              case 0xFFC6: return "Routine: Apre il canale di input";
              case 0xFFC9: return "Routine: Apre il canale di output";
              case 0xFFCC: return "Routine: Chiude i canali di input e di output";
              case 0xFFCF: return "Routine: Accetta un carattere dal canale";
              case 0xFFD2: return "Routine: Immette un carattere nel canale";
              case 0xFFD5: return "Routine: Carica la RAM da un dispositivo";
              case 0xFFD8: return "Routine: Salva la RAM su un dispositivo";
              case 0xFFDB: return "Routine: Imposta il clock del tempo";
              case 0xFFDE: return "Routine: Legge il Clock del tempo";
              case 0xFFE1: return "Routine: Termina la scansione della tastiera";
              case 0xFFE4: return "Routine: Prende il carattere dal buffer tastiera";
              case 0xFFE7: return "Routine: Chiude tutti i canali ed i files";
              case 0xFFEA: return "Routine: Incrementa il clock del tempo";
              case 0xFFED: return "Routine: Ritorna il sistema di coordinate (X,Y) schermo";
              case 0xFFF0: return "Routine: Legge/Imposta la posizione (X,Y) del cursore";
              case 0xFFF3: return "Routine: Ritorna l'indirizzo di base degli I/O";
              case 0xFFFA: return "Non Maskerable Interrupt (NMI) vector";
              case 0xFFFC: return "Startup vector";
              case 0xFFFE: return "Masckerable Interrupt (IRQ) vector";
              default:
                if ((addr>=0x4E) && (addr<=0x53)) return "Scratch area";
                if ((addr>=0x57) && (addr<=0x60)) return "Scratch per operazioni numeriche";
                if ((addr>=0x73) && (addr<=0x8A)) return "CHRGET (immette un carattere) subroutine";
                if ((addr>=0xD9) && (addr<=0xF0)) return "Tavola delle linea di schermo/Editor transiente";
                if ((addr>=0x100) && (addr<=0x10A)) return "CPU stack/Errore nastro/area conversione floating";
                if ((addr>=0x10B) && (addr<=0x13E)) return "CPU stack/Errore di nastro";
                if ((addr>=0x13F) && (addr<=0x1FF)) return "CPU stack";
                if ((addr>=0x200) && (addr<=0x258)) return "Buffer di INPUT del BASIC";
                if ((addr>=0x259) && (addr<=0x262)) return "Tabella KERNAL: Numero file logici attivi";
                if ((addr>=0x263) && (addr<=0x26C)) return "Tabella KERNAL: Numero dispositivo per ogni file";
                if ((addr>=0x26D) && (addr<=0x276)) return "Tabella KERNAL: Indirizzo secondario di ogni file";
                if ((addr>=0x277) && (addr<=0x280)) return "Coda del buffer della tastiera (FIFO)";
                if ((addr>=0x2A7) && (addr<=0x2FF)) return "Non usati";
                if ((addr>=0x334) && (addr<=0x33B)) return "Non usato";
                if ((addr>=0x33C) && (addr<=0x3FB)) return "Buffer di I/O del nastro";
                if ((addr>=0x3FC) && (addr<=0x3FF)) return "Non usato";
                if ((addr>=0x400) && (addr<=0x7E7)) return "Matrice video (25*40)";
                if ((addr>=0x7E8) && (addr<=0x7FF)) return "Puntatore ai dati animazione";
                if ((addr>=0x800) && (addr<=0x7FFF)) return "Spazio normale programmi BASIC";
                if ((addr>=0x8000) && (addr<=0x9FFF)) return "Spazio normale programmi BASIC/ROM cartuccia";
                if ((addr>=0xA000) && (addr<=0xBFFF)) return "ROM BASIC";
                if ((addr>=0xD500) && (addr<=0xD7FF)) return "IMMAGINI del SID";
                if ((addr>=0xD800) && (addr<=0xDBFF)) return "RAM colore";
                if ((addr>=0xE4EC) && (addr<=0xE4FE)) return "Costanti per sistema PAL";
                break;
            }
          default:
            switch ((int)addr) {
              case 0x01: return "6510 I/O register";
              case 0x03:
              case 0x04: return "Jump Vector: real-integer conversion";
              case 0x05:
              case 0x06: return "Jump Vector: integer-real conversion";
              case 0x07: return "Search char";
              case 0x08: return "Flag: search the quotation marks at the end of one string";
              case 0x09: return "Screen column after last TAB";
              case 0x0A: return "Flag: 0=LOAD, 1=VERIFY";
              case 0x0B: return "Buffer pointer of input/number index";
              case 0x0C: return "Flag: default dimension for DIM";
              case 0x0D: return "Data type: case 0xFF=Stringa, case 0x00=Numerico";
              case 0x0E: return "Data type: case 0x80=Intero, case 0x00=Reale";
              case 0x0F: return "Flag for DATA/LIST";
              case 0x10: return "Flag: Index/Call reference of user function";
              case 0x11: return "Flag: case 0x00=INPUT, case 0x40=GET, case 0x98=READ";
              case 0x12: return "Flag: TAN/Result symbol of one comparison";
              case 0x13: return "Flag: INPUT request";
              case 0x14:
              case 0x15: return "Transient: integer value";
              case 0x16: return "Pointer: transient strings stack";
              case 0x17:
              case 0x18: return "Last transient strings address";
              case 0x19:
              case 0x20:
              case 0x21: return "Transient strings stack";
              case 0x22:
              case 0x23:
              case 0x24:
              case 0x25: return "Utility programs pointers area";
              case 0x26:
              case 0x27:
              case 0x28:
              case 0x29:
              case 0x2A: return "Real product";
              case 0x2B:
              case 0x2C: return "Pointer: BASIC starting programs";
              case 0x2D:
              case 0x2E: return "Pointer: BASIC starting variables";
              case 0x2F:
              case 0x30: return "Pointer: BASIC starting arrays";
              case 0x31:
              case 0x32: return "Pointer: BASIC ending arrays";
              case 0x33:
              case 0x34: return "Pointer: BASIC starting strings";
              case 0x35:
              case 0x36: return "Pointer: strings for auxiliari programs";
              case 0x37:
              case 0x38: return "Pointer: BASIC ending memory";
              case 0x39:
              case 0x3A: return "BASIC current line number";
              case 0x3B:
              case 0x3C: return "BASIC precedent line number";
              case 0x3D:
              case 0x3E: return "Pointer: BASIC instruction for CONT";
              case 0x3F:
              case 0x40: return "DATA current line number";
              case 0x41:
              case 0x42: return "Pointer: DATA current element address";
              case 0x43:
              case 0x44: return "Vector: INPUT routine";
              case 0x45:
              case 0x46: return "BASIC current variable name";
              case 0x47:
              case 0x48: return "Pointer: BASIC current variable data";
              case 0x49:
              case 0x4A: return "Pointer: variable for the FOR..NEXT";
              case 0x4B:
              case 0x4C: return "Scratch area BASIC pointer";
              case 0x4D: return "Accumulator for the simbols compare";
              case 0x54:
              case 0x55:
              case 0x56: return "Vector of jump fusion";
              case 0x61: return "Floating point accumulator #1: Exponent";
              case 0x62:
              case 0x63:
              case 0x64:
              case 0x65: return "Floating point accumulator #1: Mantissa";  //?
              case 0x66: return "Floating point accumulator #1: Sign";
              case 0x67: return "Pointer: costant of series valutation";
              case 0x68: return "Floating point accumulator #1: Overflow numeral";
              case 0x69: return "Floating point accumulator #2: Exponent";
              case 0x6A:
              case 0x6B:
              case 0x6C:
              case 0x6D: return "Floating point accumulator #2: Mantissa"; //?
              case 0x6E: return "Floating point accumulator #2: Sign";
              case 0x6F: return "Sign comparison result of #1 with #2";
              case 0x70: return "Lo Byte #1 (rounding)";
              case 0x71:
              case 0x72: return "Pointer: cassette buffer";
              case 0x8B: case 0x8C:
              case 0x8D: case 0x8E:
              case 0x8F: return "Real value of the RND seed";
              case 0x90: return "Statusbyte ST of I/O KERNAL";
              case 0x91: return "Flag: key STOP/ key RVS";
              case 0x92: return "Constant (timeout) of time misure for tape";
              case 0x93: return "Flag: 0=LOAD, 1=VERIFY";
              case 0x94: return "Flag: Serail Bus - Bufferized char in out";
              case 0x95: return "Bufferized cahr for serial bus";
              case 0x96: return "Number (EOT) of cassette sincronism";
              case 0x97: return "Memory of register";
              case 0x98: return "Open files number/Index of files table";
              case 0x99: return "Input device (default=0)";
              case 0x9A: return "Output device (CMD=3)";
              case 0x9B: return "Parity tape char";
              case 0x9C: return "Flag: Byte received from tape";
              case 0x9D: return "Flag: 80=direct mode 00=program mode";
              case 0x9E: return "Tape pass 1 error register";
              case 0x9F: return "Tape pass 2 error register";
              case 0xA0:
              case 0xA1:
              case 0xA2: return "Real time clock HMS (1/60 sec)";
              case 0xA3: return "Serial counter of bits: Flag EOI";
              case 0xA4: return "Cicly of counter";
              case 0xA5: return "Bashful counter of Write cassette sincronization";
              case 0xA6: return "Pointer: I/O Buffer of tape";
              case 0xA7: return "Input bit of RS-232/Transient cassette";
              case 0xA8: return "RS-232 input bits counter/Transient cassette";
              case 0xA9: return "RS-232 indicator: Control of starting bit";
              case 0xAA: return "Buffer for RS-232 input byte/Transient cassette";
              case 0xAB: return "RS-232 input parity/Cassette short counter";
              case 0xAC:
              case 0xAD: return "Pointer: Tape buffer/Screen scrolling";
              case 0xAE:
              case 0xAF: return "Addresses of Tape end/Program end";
              case 0xB0:
              case 0xB1: return "Constant of tape time misure";
              case 0xB2:
              case 0xB3: return "Pointer: starting tape buffer";
              case 0xB4: return "RS-232 output bits counter/Tape timer";
              case 0xB5: return "Next RS-232 bit to send/tape EOT";
              case 0xB6: return "Buffer of RS-232 output byte";
              case 0xB7: return "Length of current file name";
              case 0xB8: return "Current logical file number";
              case 0xB9: return "Current secondary address";
              case 0xBA: return "Current device number";
              case 0xBB:
              case 0xBC: return "Pointer: current file name";
              case 0xBD: return "RS-232 output parity/Transient cassette";
              case 0xBE: return "Read/Write cassette block counter";
              case 0xBF: return "Serial word Buffer";
              case 0xC0: return "Stop motor of tape";
              case 0xC1:
              case 0xC2: return "I/O starting address";
              case 0xC3:
              case 0xC4: return "Transient tape load";
              case 0xC5: return "Key current handled: CHRcase 0x(n) 0=no key";
              case 0xC6: return "Number of char in keyboard buffer";
              case 0xC7: return "Flag: Write inverse chars: 1=yes 0=not used";
              case 0xC8: return "Pointer: end of logical line for INPUT";
              case 0xC9:
              case 0xCA: return "Position (X,Y) of cursor";
              case 0xCB: return "Flag: write chars with SHIFT pressed";
              case 0xCC: return "Flash state: 0=flashing";
              case 0xCD: return "Timer: countdown for cursor changing";
              case 0xCE: return "Char under the cursor";
              case 0xCF: return "Flag: Last cursore state (Flash/fixed)";
              case 0xD0: return "Flag: INPUT or GET from keyboard";
              case 0xD1:
              case 0xD2: return "Pointer: current screen line address";
              case 0xD3: return "Column of cursor on the current line";
              case 0xD4: return "Flag: Editor mode \"quotation\", case 0x00=NO";
              case 0xD5: return "Fisical screen line length";
              case 0xD6: return "Cursor line number";
              case 0xD7: return "Last key/checksum/buffer";
              case 0xD8: return "Flag: Insert mode";
              case 0xF1: return "Screen lines not thru";
              case 0xF2: return "Label of screen lines";
              case 0xF3:
              case 0xF4: return "Pointer: screen color RAM current location";
              case 0xF5:
              case 0xF6: return "Vector: keyboard decode table";
              case 0xF7:
              case 0xF8: return "RS-232 input buffer pointer";
              case 0xF9:
              case 0xFA: return "RS-232 output buffer pointer";
              case 0xFB:
              case 0xFE: return "Free 0 page for user program";
              case 0xFF: return "Transient data area of BASIC";
              case 0x281:
              case 0x282: return "Pointer: Memory base for Operative System";
              case 0x283:
              case 0x284: return "Pointer: Memory top for  Operative System";
              case 0x285: return "Flag KERNAL: IEEE time exceed";
              case 0x286: return "Current char color code";
              case 0x287: return "Background color under the cursor";
              case 0x288: return "Top of memory screen (page)";
              case 0x289: return "Keyboard buffer misure";
              case 0x28A: return "Flag: repeat pressed key, case 0x80=all keys";
              case 0x28B: return "Repeat velocity counter";
              case 0x28C: return "Repeat delay counter";
              case 0x28D: return "Flag: Key SHIFT/CTRL/Commodore";
              case 0x28E: return "Last keyboard configuration";
              case 0x28F:
              case 0x290: return "Vector: keyboard table preparation";
              case 0x291: return "Flag: mode SHIFT: 00=Disable case 0x80=Enable";
              case 0x292: return "Flag: automatic shift to bottom: 0=ON";
              case 0x293: return "RS-232: Control register image of 6551";
              case 0x294: return "RS-232: Command register image of 6551";
              case 0x295:
              case 0x296: return "BPS RS-232 USA not standard (Time/2-100)";
              case 0x297: return "RS-232: State register image of 6551";
              case 0x298: return "RS-232 bits number stayed from varied";
              case 0x299:
              case 0x29A: return "RS-232 Baud Rate";
              case 0x29B: return "RS-232 index for input buffer end";
              case 0x29C: return "RS-232 input buffer beginning (page)";
              case 0x29D: return "RS-232 output buffer beginning (page)";
              case 0x29E: return "RS-232 index for output buffer ending";
              case 0x29F:
              case 0x2A0: return "Contain IRQ vector during tape I/O";
              case 0x2A1: return "Enable the RS-232";
              case 0x2A2: return "TOD reading during I/O cassette";
              case 0x2A3: return "Transient memorization for cassette reading";
              case 0x2A4: return "Indicator of transient IRQ for cassette reading";
              case 0x2A5: return "Transient for line index";
              case 0x2A6: return "Indicator PAL/NTSC, 0=NTSC, 1=PAL";
              case 0x300:
              case 0x301: return "Vector: Write BASIC error messages";
              case 0x302:
              case 0x303: return "Vector: BASIC start up";
              case 0x304:
              case 0x305: return "Vector: BASIC text\"tokenized\"";
              case 0x306:
              case 0x307: return "Vector: BASIC text list";
              case 0x308:
              case 0x309: return "Vector: BASIC chars sending";
              case 0x30A:
              case 0x30B: return "Vector: BASIC \"token\" valutation";
              case 0x30C: return "6502 A register memorization";
              case 0x30D: return "6502 X register memorization";
              case 0x30E: return "6502 Y register memorization";
              case 0x30F: return "6502 P register memorization";
              case 0x310: return "Jump instruction of USB function";
              case 0x311:
              case 0x312: return "Lo/Hi Byte of USB instruction";
              case 0x313: return "Not used";
              case 0x314:
              case 0x315: return "Vector: Hardware Interrupt (IRQ)";
              case 0x316:
              case 0x317: return "Vector: Break Interrupt";
              case 0x318:
              case 0x319: return "Vector: Not maskerable Interrupt (NMI)";
              case 0x31A:
              case 0x31B: return "KERNAL OPEN routine vector";
              case 0x31C:
              case 0x31D: return "KERNAL CLOSE routine vector";
              case 0x31E:
              case 0x31F: return "KERNAL CHKIN routine vector";
              case 0x320:
              case 0x321: return "KERNAL CHKOUT routine vector";
              case 0x322:
              case 0x323: return "KERNAL CLRCHN routine vector";
              case 0x324:
              case 0x325: return "KERNAL CHRIN routine vector";
              case 0x326:
              case 0x327: return "KERNAL CHROUT routine vector";
              case 0x328:
              case 0x329: return "KERNAL STOP routine vector";
              case 0x32A:
              case 0x32B: return "KERNAL GETIN routine vector";
              case 0x32C:
              case 0x32D: return "KERNAL CLALL routine vector";
              case 0x32E:
              case 0x32F: return "User defined vector";
              case 0x330:
              case 0x331: return "KERNAL LOAD routine vector";
              case 0x332:
              case 0x333: return "KERNAL SAVE routine vector";

              case 0xA000: return "Routine Vector: set BASIC vector and start write";
              case 0xA002: return "Routine Vector: close canal, start up";
              case 0xA435: return "Routine: Write error (OUT OF MEMORY)";
              case 0xA437: return "Routine: Write BASIC error messages";
              case 0xA43A: return "Routine: Default write BASIC error message";
              case 0xA469: return "Routine: Write string (YA),+ 'IN'+line number";
              case 0xA474: return "Routine: Write READY, start up";
              case 0xA483: return "Routine: BASIC start up";
              case 0xA560: return "Routine: Read 89 input chars of BASIC command";
              case 0xA579: return "Routine: BASIC Text \"tokenized\" (from case 0x0304)";
              case 0xA57C: return "Routine: BASIC Text \"tokenized\"";
              case 0xA642: return "Routine: NEW instruction";
              case 0xA65E: return "Routine: CLR instruction";
              case 0xA69C: return "Routine: LIST instruction";
              case 0xA71A: return "BASIC text line";
              case 0xA742: return "Routine: FOR instruction";
              case 0xA7E4: return "Send BASIC chars";
              case 0xA81D: return "Routine: RESTORE instruction";
              case 0xA82F: return "Routine: STOP instruction";
              case 0xA831: return "Routine: END instruction";
              case 0xA857: return "Routine: CONT instruction";
              case 0xA871: return "Routine: RUN instruction";
              case 0xA883: return "Routine: GOSUB instruction";
              case 0xA8A0: return "Routine: GOTO instruction";
              case 0xA8D2: return "Routine: RETURN instruction";
              case 0xA8F8: return "Routine: DATA instruction";
              case 0xA906: return "Routine: Search ':' char in string (position)";
              case 0xA909: return "Routine: Search 0 char in string (position)";
              case 0xA928: return "Routine: IF instruction";
              case 0xA93B: return "Routine: REM instruction";
              case 0xA94B: return "Routine: ON instruction";
              case 0xA9A5: return "Routine: LET instruction";
              case 0xAA80: return "Routine: PRINT# instruction";
              case 0xAA86: return "Routine: CMD instruction";
              case 0xAAA0: return "Routine: PRINT instruction";
              case 0xAACA: return "Routine: Put a (0) ending to the readed string";
              case 0xAAD7: return "Routine: Write return effect and/or advancement";
              case 0xAB42: return "Routine: Introduces DX key in canal+ errors and char";
              case 0xAB45: return "Routine: Introduces '?' in canal+ errors and char";
              case 0xAB47: return "Routine: Introduces a char in canal+ errors and char";
              case 0xAB7B: return "Routine: GET instruction";
              case 0xABA5: return "Routine: INPUT# instruction";
              case 0xABBF: return "Routine: INPUT instruction";
              case 0xAC06: return "Routine: READ instruction";
              case 0xAD1E: return "Routine: NEXT instruction";
              case 0xAD8D: return "Routine: Verify if (A) data of Numerical type + error";
              case 0xAD8F: return "Routine: Verify if (A) data of String type + error";
              case 0xAE86: return "Routine: BASIC \"token\" evaluation";
              case 0xAEF7: return "Routine: Verify if there's ')' in current char (Syntax)";
              case 0xAEFA: return "Routine: Verify if there's '(' in current char (Syntax)";
              case 0xAEFD: return "Routine: Verify if there's ',' in current char (Syntax)";
              case 0xAEFF: return "Routine: Verify if A is in current char (Syntax)";
              case 0xAF08: return "Routine: Write (SYNTAX) error";
              case 0xB081: return "Routine: DIM instruction";
              case 0xB08B: return "Routine: Verify if there's a variable";
              case 0xB090: return "Routine: Verify if there's a variable for DIM";
              case 0xB092: return "Routine: Verify if there's a variable (A=par)";
              case 0xB113: return "Routine: Verify if the char in A is in 'A'..'Z'";
              case 0xB1AA: return "Routine: Floating Point -> Integer conversion";
              case 0xB245: return "Routine: Write (BAD SUBSCRIPT) error";
              case 0xB248: return "Routine: Write (ILLEGAL QUANTITY) error";
              case 0xB37D: return "Routine: FRE function";
              case 0xB391: return "Routine: Intger->Floating Point conversion";
              case 0xB39E: return "Routine: POS function";
              case 0xB3B3: return "Routine: DEF instruction";
              case 0xB465: return "Routine: STRcase 0x function";
              case 0xB6EC: return "Routine: CHRcase 0x function";
              case 0xB700: return "Routine: LEFTcase 0x function";
              case 0xB72C: return "Routine: RIGTHcase 0x function";
              case 0xB737: return "Routine: MODcase 0x function";
              case 0xB77C: return "Routine: LEN function";
              case 0xB78B: return "Routine: ASC function";
              case 0xB7AD: return "Routine: VAL function";
              case 0xB80D: return "Routine: PEEK function";
              case 0xB824: return "Routine: POKE instruction";
              case 0xB82D: return "Routine: WAIT instruction";
              case 0xB97E: return "Routine: Write (OVERFLOW) error";
              case 0xB9EA: return "Routine: LOG function";
              case 0xBB8A: return "Routine: Write DIVISION BY ZERO";
              case 0xBC2B: return "Routine: Determine if number =0 (ZF) o +/- (CF)";
              case 0xBC39: return "Routine: SGN function";
              case 0xBC58: return "Routine: ABS function";
              case 0xBCCC: return "Routine: INT function";
              case 0xBDC2: return "Routine: Write 'IN' + basic line";
              case 0xBDCD: return "Routine: Write (AX) integer correspondenting string";
              case 0xBDDA: return "Routine: Write (YA=pointer) string";
              case 0xBF71: return "Routine: SQR function";
              case 0xBFED: return "Routine: EXP function";

              case 0xD000: return "Positin X sprite 0";
              case 0xD001: return "Positin Y sprite 0";
              case 0xD002: return "Positin X sprite 1";
              case 0xD003: return "Positin Y sprite 1";
              case 0xD004: return "Positin X sprite 2";
              case 0xD005: return "Positin Y sprite 2";
              case 0xD006: return "Positin X sprite 3";
              case 0xD007: return "Positin Y sprite 3";
              case 0xD008: return "Positin X sprite 4";
              case 0xD009: return "Positin Y sprite 4";
              case 0xD00A: return "Positin X sprite 5";
              case 0xD00B: return "Positin Y sprite 5";
              case 0xD00C: return "Positin X sprite 6";
              case 0xD00D: return "Positin Y sprite 6";
              case 0xD00E: return "Positin X sprite 7";
              case 0xD00F: return "Positin Y sprite 7";
              case 0xD010: return "Positin X MSB sprites 0..7";
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
              case 0xD025: return "Multicolor aniamtion 0 register";
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

              case 0xE097: return "Routine: RND function";
              case 0xE10C: return "Routine: Send char in canal + BASIC errors";
              case 0xE112: return "Routine: Receive a char from canal+ BASIC errors";
              case 0xE118: return "Routine: Open the output canal + BASIC errors";
              case 0xE11E: return "Routine: Open the input canal + BASIC errors";
              case 0xE124: return "Routine: Take the char from keyboard buffer + BASIC errors";
              case 0xE12A: return "Routine: SYS instruction";
              case 0xE147: return "Routine: Save the current 6502 state";
              case 0xE156: return "Routine: SAVE instruction";
              case 0xE165: return "Routine: VERIFY instruction";
              case 0xE168: return "Routine: LOAD instruction";
              case 0xE1C7: return "Routine: CLOSE instruction";
              case 0xE1D4: return "Routine: OPEN instruction with Datasette=default";
              case 0xE1DD: return "Routine: OPEN instruction";
              case 0xE206: return "Routine: Read next char, if=0 => PLA,PLA";
              case 0xE20E: return "Routine: Verify if there's ',' and read next (Syntax)";
              case 0xE211: return "Routine: Read next char, if =0 =>syntax error";
              case 0xE264: return "Routine: COS function";
              case 0xE26B: return "Routine: SIN function";
              case 0xE2B4: return "Routine: TAN function";
              case 0xE30E: return "Routine: ATN function";
              case 0xE37B: return "Routine (from case 0xA002): close canals, start up";
              case 0xE386: return "Routine: Write READY+ start up (from case 0x300)";
              case 0xE38B: return "Routine: Write BASIC error messages";
              case 0xE394: return "Routine (from case 0xA000): set BASIC vectors and write starting";
              case 0xE3A2: return "Routine copied in case 0x73..0x8A";
              case 0xE3BF: return "Routine: Set USR instruction and memory for BASIC";
              case 0xE422: return "Routine: Write starting messages + set of memory";
              case 0xE453: return "Routine: Set BASIC vectors (case 0x300..case 0x309)";
              case 0xE4AD: return "Routine: Open output canal + errors";
              case 0xE4D3: return "Routine: Update starting/parity control bit";
              case 0xE4DA: return "Routine: Give color to actual char";
              case 0xE4E0: return "Routine: Wait some seconds or key pressed";
              case 0xE500: return "Routine IOBASE of KERNAL";
              case 0xE505: return "Routine SCREEN of KERNAL";
              case 0xE50A: return "Routine PLOT of KERNAL";
              case 0xE518: return "Routine: Predispose editor/screen delete";
              case 0xE544: return "Routine: Screen delete";
              case 0xE566: return "Routine: Transfer the cursor at top left";
              case 0xE56C: return "Routine: Goto X=line, Y=col. of cursore";
              case 0xE5A0: return "Routine: Set VIC6567 registers and devices";
              case 0xE5BA: return "Routine: Read char from keyboard buffer";
              case 0xE632: return "Routine: Withdraw a char from keyboard (CRT-Video)";
              case 0xE68A: return "Routine: Enable/Disable Editor mode \"quota\"";
              case 0xE716: return "Routine: Send a char to the screen";
              case 0xE87C: return "Routine: Update screen as for RETURN";
              case 0xE891: return "Routine: Update screen/editor becouse RETURN is pressed";
              case 0xE8CD: return "Routine: Search (and change) Ascii color codes";
              case 0xE8EA: return "Routine: Screen shifting throw top for new line";
              case 0xE9F0: return "Routine: Calculate the address of current screen line";
              case 0xE9FF: return "Routine: Write starting message + set of memory";
              case 0xEA13: return "Routine: Write char to the cursor (with parameters)";
              case 0xEA24: return "Routine: Calculate pointer to color RAM location";
              case 0xEA31: return "Default hardware interrupt (IRQ)";
              case 0xEA87: return "Routine SCNKEY of KERNAL";
              case 0xEC44: return "Routine: Control switching negative/uppercase/SHIFT";
              case 0xED09: return "Routine TALK of KERNAL";
              case 0xED0C: return "Routine LISTEN of KERNAL";
              case 0xED40: return "Routine: Send a byte to serial bus";
              case 0xEDB9: return "Routine SECOND of KERNAL";
              case 0xEDC7: return "Routine TKSA of KERNAL";
              case 0xEDDD: return "Routine CIOUT of KERNAL";
              case 0xEDEF: return "Routine UNTLK of KERNAL";
              case 0xEDFE: return "Routine UNLSN of KERNAL";
              case 0xEE13: return "Routine ACPTR of KERNAL";
              case 0xEE85: return "Routine: Reset serial bus clock pulse output";
              case 0xEE8E: return "Routine: Set serial bus clock pulse output";
              case 0xEE97: return "Routine: Reset serial bus data output";
              case 0xEEA0: return "Routine: Set serial bus data output";
              case 0xEEA9: return "Routine: Read in C serial bus data input";
              case 0xEEB3: return "Routine: 931 clock cicle delay";
              case 0xF00D: return "Routine: 6551 state register image = case 0x40";
              case 0xF086: return "Routine: Take char from RS-232";
              case 0xF0A4: return "Routine: control for RS-232 disabilitation";
              case 0xF12B: case 0xF12C:
              case 0xF12D: case 0xF12E:
              case 0xF12F: return "Routine: Write strings of I/O";
              case 0xF13E: return "Routine GETIN of KERNAL";
              case 0xF14E: return "Routine: Take char from RS-232/Register memory";
              case 0xF157: return "Routine CHRIN of KERNAL";
              case 0xF199: return "Routine: Withdraws char from tape";
              case 0xF1AD: return "Routine: Withdraws char from serial bus";
              case 0xF1B8: return "Routine: Take char from RS-232/Register image";
              case 0xF1CA: return "Routine CHROUT of KERNAL";
              case 0xF1DD: return "Routine: Send a char to tape (use buffer)";
              case 0xF20E: return "Routine CHKIN of KERNAL";
              case 0xF250: return "Routine CHKOUT of KERNAL";
              case 0xF291: return "Routine CLOSE of KERNAL";
              case 0xF2EE: return "Routine: device addresses/Table reorganizes";
              case 0xF2F1: return "Routine: Table reorganizes for file closing";
              case 0xF2F2: return "Routine: Table reorganizes for file closing (A=par.)";
              case 0xF30F: return "Routine: Searches table index (X) of file (X=n�file)";
              case 0xF314: return "Routine: Searches table index (X) of file (A=n�file)";
              case 0xF31F: return "Routine: Makes the file at X addrress active";
              case 0xF32F: return "Routine CLALL of KERNAL";
              case 0xF333: return "Routine CLRCHN of KERNAL";
              case 0xF34A: return "Routine OPEN of KERNAL";
              case 0xF3D5: return "Routine: Sends file name on serial bus";
              case 0xF47D: return "Routine: Fixes top memori of Operative System (C=0)";
              case 0xF49E: return "Routine LOAD of KERNAL";
              case 0xF4A5: return "Default routine LOAD of KERNAL";
              case 0xF5AF: return "Routine: Write SEARCHING FOR file name";
              case 0xF5C1: return "Routine: Write current file name";
              case 0xF5D2: return "Routine: Write LOADING/VERIFING string";
              case 0xF5DD: return "Routine SAVE of KERNAL";
              case 0xF5ED: return "Default routine SAVE of KERNAL";
              case 0xF633: return "Routine: Addresses the device with the command (C=1)";
              case 0xF642: return "Routine: Addresses the device with the command";
              case 0xF654: return "Routine: Sets the serial bus to  not-receiving";
              case 0xF68F: return "Routine: Write SAVING file name";
              case 0xF69B: return "Routine UDTIM of KERNAL";
              case 0xF6BC: return "Routine: Verifies pressure STOP/RVS keys";
              case 0xF6DD: return "Routine RDTIM of KERNAL";
              case 0xF6EA: return "Routine SETTIM of KERNAL";
              case 0xF6FB: return "Routine: Close all I/O canals and Write I/O ERROR 1";
              case 0xF6FE: return "Routine: Close all I/O canals and Write I/O ERROR 2";
              case 0xF701: return "Routine: Close all I/O canals and Write I/O ERROR 3";
              case 0xF704: return "Routine: Close all I/O canals and Write I/O ERROR 4";
              case 0xF707: return "Routine: Close all I/O canals and Write I/O ERROR 5";
              case 0xF70A: return "Routine: Close all I/O canals and Write I/O ERROR 6";
              case 0xF70D: return "Routine: Close all I/O canals and Write I/O ERROR 7";
              case 0xF710: return "Routine: Close all I/O canals and Write I/O ERROR 8";
              case 0xF713: return "Routine: Close all I/O canals and Write I/O ERROR 9";
              case 0xF72C: return "Routine: Search/verify valid tape header file";
              case 0xF76A: return "Routine: Writes the Header for the file (address+name)";
              case 0xF7D0: return "Routine: Loads in XY the pointer of tape buffer start";
              case 0xF7EA: return "Routine: Compares name found file with that searched";
              case 0xF7D7: return "Routine: Prepares I/O starting addresses, tape end";
              case 0xF80D: return "Routine: Loads in XY the current tape buffer pointer";
              case 0xF817: return "Routine: Wait (write) of cassette PRESS PLAY";
              case 0xF82E: return "Routine: Reads cassette switch (Z=present)";
              case 0xF838: return "Routine: Wait (write) of cassette PRESS RECORD & PLAY";
              case 0xF841: return "Routine: Reads blocks data from tape";
              case 0xF84A: return "Routine: Reads blocks data from tape (no addresses)";
              case 0xF864: return "Routine: Writes blocks data from tape+ initialization";
              case 0xF867: return "Routine: Writes blocks data from tape (type #14)";
              case 0xF86B: return "Routine: Writes blocks data from tape";
              case 0xF8D0: return "Routine: Verifies for switch off motor (and normal IRQ)";
              case 0xF92C: return "Routine IRQ cassette for LOAD";
              case 0xFB8E: return "Routine: Sets tape ^Buffer at ^I/O starting address";
              case 0xFB97: return "Routine: Initialize/reset variables for cassette";
              case 0xFBA6: return "Routine: Sets TimerB #1 in base of out bit";
              case 0xFBAD: return "Routine: Starts Timer B #1(176c) and reverses tape output";
              case 0xFBAF: return "Routine: Starts Timer B #1(A=par) and  reverses tape output";
              case 0xFBB1: return "Routine: Starts Timer B #1(AX=par) and reverses tape output";
              case 0xFC57: return "Routine: Changes IRQ vector to FC6A";
              case 0xFC93: return "Routine: Stop motor, disable int. CIA #1";
              case 0xFCB8: return "Routine: Stop motor, disable int. CIA #1, int. goes out";
              case 0xFCBD: return "Routine: Change IRQ vector for I/O";
              case 0xFCCA: return "Routine: Stop cassette motor";
              case 0xFCD1: return "Routine: Controls if tape ^Buffer =tape ending address";
              case 0xFCDB: return "Routine: Advances ^ tape Buffer/Screen shifting";
              case 0xFCE2: return "Routine: Startup";
              case 0xFD02: return "Routine: Search for cartridge presence";
              case 0xFD15: return "Routine RESTOR of KERNAL";
              case 0xFD1A: return "Routine VECTOR of KERNAL";
              case 0xFD50: return "Routine RAMTAS of KERNAL";
              case 0xFDAE: return "Routine IOINIT of KERNAL";
              case 0xFFDD: return "Routine: Set Timer A #1 at 16,7msec, Int. e PB6";
              case 0xFDF9: return "Routine SETNAME of KERNAL";
              case 0xFE00: return "Routine SETLFS of KERNAL";
              case 0xFE07: return "Routine READST of KERNAL";
              case 0xFE18: return "Routine SETMSG of KERNAL";
              case 0xFE1C: return "Routine: Set (change) ST state of I/O KERNAL";
              case 0xFE21: return "Routine SETTMO of KERNAL";
              case 0xFE25: return "Routine MEMTOP of KERNAL";
              case 0xFE27: return "Routine: XY= top of Operative System memory";
              case 0xFE2D: return "Routine: Sets top of Operative System memory";
              case 0xFE34: return "Routine MEMBOT of KERNAL";
              case 0xFE43: return "Routine: Non Maskerable Interrupt (NMI)";
              case 0xFE47: return "Default Non maskerable Interrupt (NMI)";
              case 0xFEBC: return "Restores cpu registers and goes out from interrupt";
              case 0xFEC2:
              case 0xFEC3:
              case 0xFEC4: return "NTSC system costants";
              case 0xFF2E: return "Routine: Update RS-232 Baud Rate";
              case 0xFF43: return "Routine: Execute NMI Interrupt Routine (^return in stack)";
              case 0xFF48: return "Masckerable Interrupt (IRQ) Routine";
              case 0xFF5B: return "Routine CINT of KERNAL";
              case 0xFF6E: return "Routine: Set Int. Timer #1 and PB6 output";
              case 0xFF81: return "Routine: Initialize screen editor";
              case 0xFF84: return "Routine: I/O initialization";
              case 0xFF87: return "Routine: Initializes RAM/ creates tape buffer /Screen case 0x0400";
              case 0xFF8A: return "Routine: Reset default I/O vector";
              case 0xFF8D: return "Routine: Read/Set I/O vector";
              case 0xFF90: return "Routine: Control KERNAL messages";
              case 0xFF93: return "Routine: Send secondary address after Receive";
              case 0xFF96: return "Routine: Send secondary address after Trasmit";
              case 0xFF99: return "Routine: Read/Set top memory";
              case 0xFF9C: return "Routine: Read/Set base mamory";
              case 0xFF9F: return "Routine: Done the keyboard scan";
              case 0xFFA2: return "Routine: Set the time out of serial bus";
              case 0xFFA5: return "Routine: Acept a byte from serial port";
              case 0xFFA8: return "Routine: Send a byte to serial port";
              case 0xFFAB: return "Routine: Set serial bus to Not-trasmit";
              case 0xFFAE: return "Routine: Set serial bus to Not-Receive";
              case 0xFFB1: return "Routine: Set to Receive the devices to the serial bus";
              case 0xFFB4: return "Routine: Set to Trasmit the serial bus device";
              case 0xFFB7: return "Routine: Read the I/O state word";
              case 0xFFBA: return "Routine: Set primary, secondary and logical addresses";
              case 0xFFBD: return "Routine: Set file name";
              case 0xFFC0: return "Routine: Open a logical file";
              case 0xFFC3: return "Routine: Close a specified logical file";
              case 0xFFC6: return "Routine: Open an input canal";
              case 0xFFC9: return "Routine: Open an output canal";
              case 0xFFCC: return "Routine: Close the input and output channel";
              case 0xFFCF: return "Routine: Acept a char in the channel";
              case 0xFFD2: return "Routine: Send a char in the channel";
              case 0xFFD5: return "Routine: Load the Ram from a device";
              case 0xFFD8: return "Routine: Save the Ram to a device";
              case 0xFFDB: return "Routine: Set time clock";
              case 0xFFDE: return "Routine: Read time clock";
              case 0xFFE1: return "Routine: Terminate the keyboard scan";
              case 0xFFE4: return "Routine: Take the char from keyboard buffer";
              case 0xFFE7: return "Routine: Close all canals and files";
              case 0xFFEA: return "Routine: Increment time clock";
              case 0xFFED: return "Routine: Return (X,Y) screen coordinate";
              case 0xFFF0: return "Routine: Read/Set (X,Y) cursor position";
              case 0xFFF3: return "Routine: Return the base address of I/O";
              case 0xFFFA: return "Not Maskerable Interrupt (NMI) vector";
              case 0xFFFC: return "Startup vector";
              case 0xFFFE: return "Masckerable Interrupt (IRQ) vector";
              default:
                if ((addr>=0x4E) && (addr<=0x53)) return "Scratch area";
                if ((addr>=0x57) && (addr<=0x60)) return "Scratch for numeric operation";
                if ((addr>=0x73) && (addr<=0x8A)) return "CHRGET (Introduce a char) subroutine";
                if ((addr>=0xD9) && (addr<=0xF0)) return "Table of screen line/Transient editor";
                if ((addr>=0x100) && (addr<=0x10A)) return "CPU stack/Tape error/Floating conversion area";
                if ((addr>=0x10B) && (addr<=0x13E)) return "CPU stack/Tape error";
                if ((addr>=0x13F) && (addr<=0x1FF)) return "CPU stack";
                if ((addr>=0x200) && (addr<=0x258)) return "INPUT buffer of BASIC";
                if ((addr>=0x259) && (addr<=0x262)) return "KERNAL table: Number of active logical files";
                if ((addr>=0x263) && (addr<=0x26C)) return "KERNAL table: Number of device for each files";
                if ((addr>=0x26D) && (addr<=0x276)) return "KERNAL table: Secondary address for each files";
                if ((addr>=0x277) && (addr<=0x280)) return "Keyboard buffer queue (FIFO)";
                if ((addr>=0x2A7) && (addr<=0x2FF)) return "Not used";
                if ((addr>=0x334) && (addr<=0x33B)) return "Not used";
                if ((addr>=0x33C) && (addr<=0x3FB)) return "Tape I/O buffer";
                if ((addr>=0x3FC) && (addr<=0x3FF)) return "Not used";
                if ((addr>=0x400) && (addr<=0x7E7)) return "Video matrix (25*40)";
                if ((addr>=0x7E8) && (addr<=0x7FF)) return "Pointer to data sprites";
                if ((addr>=0x800) && (addr<=0x7FFF)) return "Normal space for BASIC programs";
                if ((addr>=0x8000) && (addr<=0x9FFF)) return "Normal space for BASIC programs/ROM cartridge";
                if ((addr>=0xA000) && (addr<=0xBFFF)) return "BASIC ROM";
                if ((addr>=0xD500) && (addr<=0xD7FF)) return "SID images";
                if ((addr>=0xD800) && (addr<=0xDBFF)) return "Color RAM";
                if ((addr>=0xE4EC) && (addr<=0xE4FE)) return "PAL system costant";
                break;
            }
        }
      break;
    }
    return super.dcom(iType, aType, addr, value);
  }
}
