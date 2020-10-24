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
import static sw_emulator.software.cpu.M6510Dasm.A_ABS;
import static sw_emulator.software.cpu.M6510Dasm.A_ABX;
import static sw_emulator.software.cpu.M6510Dasm.A_ABY;
import static sw_emulator.software.cpu.M6510Dasm.A_IDX;
import static sw_emulator.software.cpu.M6510Dasm.A_IDY;
import static sw_emulator.software.cpu.M6510Dasm.A_IND;
import static sw_emulator.software.cpu.M6510Dasm.A_REL;
import static sw_emulator.software.cpu.M6510Dasm.A_ZPG;
import static sw_emulator.software.cpu.M6510Dasm.A_ZPX;
import static sw_emulator.software.cpu.M6510Dasm.A_ZPY;

/**
 * Comment the memory location of Plus4 for the disassembler
 * It performs also a multy language comments.
 * 
 * @author ice
 */
public class CVic20Dasm extends M6510Dasm {
   // Available language
  public static final byte LANG_ENGLISH=1;
  public static final byte LANG_ITALIAN=2;

  /** Actual selected language (default=english) */
  public byte language=LANG_ENGLISH;
  
  /** Vic20: Comment zero page */
  public boolean commentVic20ZeroPage;
  
  /** Vic20: Comment stack area */
  public boolean commentVic20StackArea;
  
  /** Vic20: Comment 200 area */
  public boolean commentVic20_200Area;
  
  /** Vic20: Comment 300 area */
  public boolean commentVic20_300Area;
  
  /** Vic20: Comment 400 area */
  public boolean commentVic20_400Area;
  
  /** Vic20: Comment Vic */
  public boolean commentVic20Vic;
  
  /** Vic20: Comment Via 1 */
  public boolean commentVic20Via1;
  
  /** Vic20: Comment Via 2 */
  public boolean commentVic20Via2;
  
  /** Vic20: Comment user basic */
  public boolean commentVic20UserBasic;
  
  /** Vic20: Comment screen */
  public boolean commentVic20Screen;
  
  /** Vic20: Comment 8k expansion 1 */
  public boolean commentVic20_8kExp1;
  
  /** Vic20: Comment 8k expansion 2 */
  public boolean commentVic20_8kExp2;
  
  /** Vic20: Comment 8k expansion 3 */
  public boolean commentVic20_8kExp3;
  
  /** Vic20: Comment character */
  public boolean commentVic20Character;
  
  /** Vic20: Comment color */
  public boolean commentVic20Color;
  
  /** Vic20: Comment block 2 */
  public boolean commentVic20Block2;
  
  /** Vic20: Comment block 3 */
  public boolean commentVic20Block3;
  
  /** Vic20: Comment block 4 */
  public boolean commentVic20Block4;
  
  /** Vic20: Comment Basic rom */
  public boolean commentVic20BasicRom;
  
  /** Vic20: Comment Kernal rom */
  public boolean commentVic20KernalRom;
  
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
        if ((int)addr<=0xFF && !commentVic20ZeroPage) return "";
        if ((int)addr>=0x100 && (int)addr<=0x1FF && !commentVic20StackArea) return "";
        if ((int)addr>=0x200 && (int)addr<=0x2FF && !commentVic20_200Area) return "";
        if ((int)addr>=0x300 && (int)addr<=0x3FF && !commentVic20_300Area) return "";
        if ((int)addr>=0x400 && (int)addr<=0x7FF && !commentVic20_400Area) return "";
        if ((int)addr>=0x1000 && (int)addr<=0x1DFF && !commentVic20UserBasic) return "";
        if ((int)addr>=0x1E00 && (int)addr<=0x1FFF && !commentVic20Screen) return "";
        if ((int)addr>=0x2000 && (int)addr<=0x3FFF && !commentVic20_8kExp1) return "";
        if ((int)addr>=0x4000 && (int)addr<=0x5FFF && !commentVic20_8kExp2) return "";
        if ((int)addr>=0x6000 && (int)addr<=0x7FFF && !commentVic20_8kExp2) return "";
        if ((int)addr>=0x8000 && (int)addr<=0x8FFF && !commentVic20Character) return "";
        if ((int)addr>=0x9000 && (int)addr<=0x900F && !commentVic20Vic) return "";
        if ((int)addr>=0x9010 && (int)addr<=0x901F && !commentVic20Via1) return "";
        if ((int)addr>=0x9030 && (int)addr<=0x903F && !commentVic20Via2) return ""; 
        if ((int)addr>=0x9400 && (int)addr<=0x97FF && !commentVic20Color) return ""; 
        if ((int)addr>=0x9800 && (int)addr<=0x9BFF && !commentVic20Block2) return "";
        if ((int)addr>=0x9C00 && (int)addr<=0x9FFF && !commentVic20Block3) return "";
        if ((int)addr>=0xA000 && (int)addr<=0xBFFF && !commentVic20Block4) return "";
        if ((int)addr>=0xC000 && (int)addr<=0xDFFF && !commentVic20BasicRom) return "";
        if ((int)addr>=0xE000 && (int)addr<=0xFFFF && !commentVic20KernalRom) return "";          
        switch (language) {
          case LANG_ITALIAN:
            switch ((int)addr) {
              case 0x00: return "Salto per USR";
              case 0x01: 
              case 0x02: return "Vettore per USR";
              case 0x03: 
              case 0x04: return "Vettore virgola mobile-interor";
              case 0x05: 
              case 0x06: return "Vettore intero-virgola mobile";
              case 0x07: return "Cerca un carattere";
              case 0x08: return "Flag di virgolette di scansione";
              case 0x09: return "Salva colonna TAB";
              case 0x0A: return "0=LOAD, 1=VERIFY";
              case 0x0B: return "Puntatore del buffer di input/indice #";
              case 0x0C: return "Flag DIM predefinito";
              case 0x0D: return "Tipo: FF=stringa, 00=numero";
              case 0x0E: return "Tipo: 80=intero, 00=virgola mobile";
              case 0x0F: return "Scansione DATI/Citazione LIST/flag di memoria";
              case 0x10: return "Flag pedice /FNx";
              case 0x11: return "0 = INPUT;$40 = GET;$98 = READ";
              case 0x12: return "Segno ATN/flag di valutazione di confronto";
              case 0x13: return "Flag del prompt I/O corrente";
              case 0x14:
              case 0x15: return "Valore intero";
              case 0x16: return "Puntatore: stack di stringhe temporaneo";
              case 0x17: 
              case 0x18: return "Ultimo vettore di stringa temporanea";
              case 0x19:         
              case 0x1A:
              case 0x1B:                  
              case 0x1C:    
              case 0x1D:    
              case 0x1E:    
              case 0x1F:    
              case 0x20: 
              case 0x21: return "Stack per stringhe temporanee";              
              case 0x22: 
              case 0x23: 
              case 0x24:
              case 0x25: return "Area del puntatore di utilità";  
              case 0x26:
              case 0x27:
              case 0x28:
              case 0x29:
              case 0x2A: return "Area di prodotto per la moltiplicazione";
              case 0x2B: 
              case 0x2C: return "Puntatore: inizio di BASIC";
              case 0x2D: 
              case 0x2E: return "Puntatore: inizio delle variabili";
              case 0x2F: 
              case 0x30: return "Puntatore: inizio degli array";
              case 0x31: 
              case 0x32: return "Puntatore: fine degli array";
              case 0x33: 
              case 0x34: return "Puntatore: memoria stringa (spostandosi verso il basso)";
              case 0x35: 
              case 0x36: return "Puntatore a stringa di utilità";
              case 0x37: 
              case 0x38: return "Puntatore: limite di memoria";
              case 0x39: 
              case 0x3A: return "Numero di riga BASIC corrente";
              case 0x3B: 
              case 0x3C: return "Numero di riga BASIC precedente";
              case 0x3D: 
              case 0x3E: return "Puntatore: dichiarazione di base per CONT";
              case 0x3F: 
              case 0x40: return "Numero di riga DATI corrente";
              case 0x41: 
              case 0x42: return "Indirizzo DATA corrente"; 
              case 0x43: 
              case 0x44: return "Vettore di input";
              case 0x45: 
              case 0x46: return "Nome della variabile corrente";
              case 0x47: 
              case 0x48: return "Indirizzo variabile corrente";
              case 0x49: 
              case 0x4A: return "Puntatore variabile per FOR/NEXT";
              case 0x4B: 
              case 0x4C: return "Y-save; op-save; Puntatore salvataggio BASIC";
              case 0x4D: return "Accumulatore di simboli di confronto";
              case 0x4E:
              case 0x4F:
              case 0x50:
              case 0x51: 
              case 0x52: 
              case 0x53: return "Area di lavoro varie, puntatori, ecc";
              case 0x54: 
              case 0x55: 
              case 0x56: return "Salta il vettore per le funzioni";
              case 0x57:     
              case 0x58:
              case 0x59:      
              case 0x5A:      
              case 0x5B:      
              case 0x5C:
              case 0x5D: 
              case 0x5E:
              case 0x5F: 
              case 0x60: return "Area di lavoro numerica misc";   
              case 0x61: return "Accum #1: esponente";
              case 0x62:
              case 0x63: 
              case 0x64: 
              case 0x65: return "Accum #1: mantissa"; 
              case 0x66: return "Accum #1: segno";
              case 0x67: return "Puntatore della costante di valutazione della serie";
              case 0x68: return "Accum #1 hi-order (overflow)";
              case 0x69: 
              case 0x6A: 
              case 0x6B:
              case 0x6C: 
              case 0x6D: 
              case 0x6E: return "Accum #2: esponente, etc.";
              case 0x6F: return "Segno di confronto, Accum #1 contro #2"; 
              case 0x70: return "Accum #1 lo-order (arrotondamento)";
              case 0x71: 
              case 0x72: return "Lunghezza buffer cassetta / Puntatore serie";
              case 0x7A: 
              case 0x7B: return "Puntatore di base (all'interno della subroutine)";              
              case 0x8B: 
              case 0x8C:
              case 0x8D: 
              case 0x8E:
              case 0x8F: return "RND valore seme";
              case 0x90: return "Word di stato ST";
              case 0x91: return "Selettore a chiave PIA: flag STOP e RVS";
              case 0x92: return "Costante di tempo per il nastro";  
              case 0x93: return "Carica=0, Verifica=1";
              case 0x94: return "Uscita seriale: flag di caratteri differiti";
              case 0x95: return "Carattere differito seriale";
              case 0x96: return "EOT nastro ricevuto";
              case 0x97: return "Registrati salva";
              case 0x98: return "Quanti file aperti";
              case 0x99: return "Dispositivo di input (normalmente 0)";
              case 0x9A: return "Dispositivo di uscita (CMD), normalmente 3";
              case 0x9B: return "Parità dei caratteri del nastro";
              case 0x9C: return "Flag di byte ricevuti";
              case 0x9D: return "Diretto=$80/RUN=0 controllo dell'uscita"; 
              case 0x9E: return "Registro errori passaggio nastro 1/buffer carattere";
              case 0x9F: return "Registro errori passaggio 2 nastro corretto"; 
              case 0xA0: 
              case 0xA1:
              case 0xA2: return "Orologio Jiffy (HML)";
              case 0xA3: return "Conteggio bit seriale/flag EOI"; 
              case 0xA4: return "Conteggio ciclo";
              case 0xA5: return "Conto alla rovescia, scrittura su nastro/conteggio bit";
              case 0xA6: return "Puntatore: buffer del nastro";
              case 0xA7: return "Scrittura nastro ldr count/Read pass/inbit";
              case 0xA8: return "Scrittura nastro nuovo byte/Errore lettura/inbit";
              case 0xA9: return "Scrittura bit di partenza/Lettura bit errore/stbit";
              case 0xAA: return "Scansione nastro;Cnt;Ld;Fine/byte assy";
              case 0xAB: return "Scrivere lunghezza lead/checksum Rd/parità";
              case 0xAC:
              case 0xAD: return "Puntatore: buffer del nastro, scorrimento";
              case 0xAE: 
              case 0xAF: return "Indirizzi di fine nastro/Fine programma";
              case 0xB0:
              case 0xB1: return "Costanti di temporizzazione del nastro";
              case 0xB2:
              case 0xB3: return "Puntatore: inizio del buffer del nastro";              
              case 0xB4: return "Timer nastro (1=abilitato); bit cnt";
              case 0xB5: return "Nastro EOT/RS-232 bit successivo da inviare";
              case 0xB6: return "Lettura errore carattere/buffer outbyte";
              case 0xB7: return "# caratteri nel nome del filee";
              case 0xB8: return "File logico corrente";
              case 0xB9: return "Indirizzo secondario attuale";
              case 0xBA: return "Dispositivo corrente";
              case 0xBB: 
              case 0xBC: return "Puntatore: al nome del file";
              case 0xBD: return "Scrivi la parola di spostamento/Leggi il carattere di input";
              case 0xBE: return "# blocchi rimanenti da scrivere/leggere";
              case 0xBF: return "Buffer di parole seriali";
              case 0xC0: return "Blocco motore nastro";
              case 0xC1:
              case 0xC2: return "Indirizzi iniziali di I/O"; 
              case 0xC3:
              case 0xC4: return "Puntatore di configurazione KERNAL";
              case 0xC5: return "Tasto corrente premuto"; 
              case 0xC6: return "# caratteri nel buffer della tastiera";
              case 0xC7: return "Contrassegno inverso dello schermo";
              case 0xC8: return "Puntatore: fine riga per l'input";
              case 0xC9:
              case 0xCA: return "Log del cursore di input (riga, colonna)";
              case 0xCB: return "Quale chiave: 64 se nessuna chiave";
              case 0xCC: return "Abilita cursore (0 = cursore lampeggiante)";
              case 0xCD: return "Conto alla rovescia del tempo del cursore";
              case 0xCE: return "Carattere sotto il cursore";
              case 0xCF: return "Cursore in fase di lampeggio"; 
              case 0xD0: return "Input da schermo/da tastiera"; 
              case 0xD1:
              case 0xD2: return "Puntatore alla linea schermo";
              case 0xD3: return "Posizione del cursore sulla linea superiore";
              case 0xD4: return "0=cursore diretto, altrimenti programmato";
              case 0xD5: return "Lunghezza della riga dello schermo corrente";
              case 0xD6: return "Riga in cui risiede il cursore";
              case 0xD7: return "Ultimo inkey/checksum/buffer";
              case 0xD8: return "# di INSERT in sospeso"; 
              case 0xF1: return "Collegamento schermo fittizio";
              case 0xF2: return "Indicatore di riga sullo schermo";
              case 0xF3: 
              case 0xF4: return "Puntatore a colori dello schermo";   
              case 0xF5:
              case 0xF6: return "Puntatore da tastiera";
              case 0xF7: 
              case 0xF8: return "Puntatore RS-232 Rcv";
              case 0xF9: 
              case 0xFA: return "Puntatore RS-232 Tx"; 
              case 0xFB:
              case 0xFC:
              case 0xFD:
              case 0xFE: return "Sistema operativo spazio libero pagina zero";
              case 0xFF: return "Archiviazione di base";  
                
              case 0x281:
              case 0x282: return "Inizio della memoria per il sistema operativo";
              case 0x283:
              case 0x284: return "Parte superiore della memoria per il sistema operativo";   
              case 0x285: return "Flag timeout bus seriale";
              case 0x286: return "Codice colore corrente"; 
              case 0x287: return "Colore sotto il cursore";    
              case 0x288: return "Pagina di memoria dello schermo";                
              case 0x289: return "Dimensione massima del buffer della tastiera";
              case 0x28A: return "Ripetizione chiave (128 = ripeti tutte le chiavi)";
              case 0x28B: return "Ripetere il contatore di velocità";
              case 0x28C: return "Ripetere il contatore del ritardo";
              case 0x28D: return "Flag Shift/Control della tastiera";
              case 0x28E: return "Ultimo schema di spostamento della tastiera";
              case 0x28F:
              case 0x290: return "Puntatore: decodifica logica";
              case 0x291: return "Interruttore modalità cambio (0=abilitato, 128=bloccato)";
              case 0x292: return "Flag di scorrimento automatico verso il basso (0 = attivato, <> 0 = disattivato)";
              case 0x293: return "Registro di controllo RS-232";
              case 0x294: return "Registro di comando RS-232";
              case 0x295:
              case 0x296: return "Non standard (tempo bit / 2-100)";
              case 0x297: return "Rgistro di stato RS-232";
              case 0x298: return "Numero di bit da inviare";
              case 0x299: 
              case 0x29A: return "Baud rate (pieno) bit time";
              case 0x29B: return "Puntatore di ricezione RS-232";
              case 0x29C: return "Puntatore di ingresso RS-232";
              case 0x29D: return "Puntatore di trasmissione RS-232";
              case 0x29E: return "Puntatore di uscita RS-232";
              case 0x29F:
              case 0x2A0: return "Mantiene IRQ durante le operazioni su nastro";
           
              case 0x300:
              case 0x301: return "Error message linkCollegamento del messaggio di errore";
              case 0x302:
              case 0x303: return "Collegamento base per avviamento a caldo";
              case 0x304:
              case 0x305: return "Collegamento token Crunch Basic";
              case 0x306:
              case 0x307: return "Link ai token di stampa";
              case 0x308:
              case 0x309: return "Avvia un nuovo collegamento al codice di base";
              case 0x30A:
              case 0x30B: return "Ottieni il collegamento agli elementi aritmetici";
              case 0x30C: return "Memoria per registro 6502 .A";
              case 0x30D: return "Memoria per registro 6502 .X";
              case 0x30E: return "Memoria per registro 6502 .Y";
              case 0x30F: return "Memoria per registro 6502 .SP";
              case 0x314:
              case 0x315: return "Vettore di interrupt hardware (IRQ) [EABF]";
              case 0x316:
              case 0x317: return "Vettore di interruzione Break [FED2]";
              case 0x318:
              case 0x319: return "Vettore di interrupt NMI [FEAD]";
              case 0x31A:
              case 0x31B: return "Vettore OPEN [F40A]";
              case 0x31C:
              case 0x31D: return "Vettore CLOSE [F34A]";
              case 0x31E:
              case 0x31F: return "Vettore Set-input [F2C7]";
              case 0x320:
              case 0x321: return "Vettore Set-output [F309]";
              case 0x322:
              case 0x323: return "Vettore Restore l/O [F3F3]";
              case 0x324:
              case 0x325: return "Vettore INPUT [F20E]";
              case 0x326:
              case 0x327: return "Vettore Output [F27A]";
              case 0x328:
              case 0x329: return "Vettore Test-STOP [F770]";
              case 0x32A:
              case 0x32B: return "Vettore GET [F1F5]";
              case 0x32C:
              case 0x32D: return "Vettore Abort l/O [F3EF]";
              case 0x32E:
              case 0x32F: return "Vettore utente (BRK predefinito) [FED2]";
              case 0x330:
              case 0x331: return "Collegamento per caricare la RAM [F549]";
              case 0x332:
              case 0x333: return "Collegamento per scrivere la RAM [F685]";     
              
              case 0x9000: return "Vic: bit 0-6 centratura orizzontale, bit 7 imposta la scansione interlacciata";
              case 0x9001: return "Vic: centratura verticale";
              case 0x9002: return "Vic: i bit 0-6 impostano il numero di colonne, il bit 7 fa parte dell'indirizzo della matrice video";
              case 0x9003: return "Vic: i bit 1-6 impostano il numero di righe, il bit 0 imposta i caratteri 8x8 o 16x8";
              case 0x9004: return "Vic: Linea del fascio televisivo raster";
              case 0x9005: return "Vic: bit 0-3 inizio della memoria caratteri (predefinito = 0), bit 4-7 è il resto dell'indirizzo video (predefinito=F)";
              case 0x9006: return "Vic: posizione orizzontale della penna luminosa";
              case 0x9007: return "Vic: posizione verticale della penna luminosa";
              case 0x9008: return "Vic: valore digitalizzato del paddle X";
              case 0x9009: return "Vic: valore digitalizzato del paddle Y";
              case 0x900A: return "Vic: frequenza per l'oscillatore 1 (basso) (on: 128-255)";
              case 0x900B: return "Vic: frequenza per l'oscillatore 2 (medio) (on: 128-255)";
              case 0x900C: return "Vic: frequenza per l'oscillatore 3 (alto) (on: 128-255)";
              case 0x900D: return "Vic: frequenza sorgent rumore";
              case 0x900E: return "Vic: il bit 0-3 imposta il volume di tutto il suono, i bit 4-7 sono informazioni ausiliarie sul colore";
              case 0x900F: return "Vic: Registro del colore dello schermo e del bordo, bit 4-7 selezionare il colore di sfondo, bit 0-2 selezionare il colore del bordo, bit 3 seleziona la modalità invertita o normale";
              
              case 0x9110: return "Via #1: Porta B registro uscita";
              case 0x9111: return "Via #1: Porta A registero uscita";
              case 0x9112: return "Via #1: Registro direzioni dati B";
              case 0x9113: return "Via #1: Registro direzioni dati A";
              case 0x9114: return "Via #1: Timer 1 byte basso";
              case 0x9115: return "Via #1: Timer 1 byte alto e contatore";
              case 0x9116: return "Via #1: Timer 1 byte basso";
              case 0x9117: return "Via #1: Timer 1 byte alto";
              case 0x9118: return "Via #1: Timer 2 byte basso";
              case 0x9119: return "Via #1: Timer 2 byte alto";
              case 0x911A: return "Via #1: Registro a scorrimento";
              case 0x911B: return "Via #1: Registro di controllo ausiliario";
              case 0x911C: return "Via #1: Registro di controllo periferico";
              case 0x911D: return "Via #1: Registro flag di interruzione";
              case 0x911E: return "Via #1: Registro di abilitazione interrupt";
              case 0x911F: return "Via #1: Porta A (interruttore della cassetta Sense)";
              
              case 0x9120: return "Via #2: Porta B registro uscita";
              case 0x9121: return "Via #2: Porta A registro uscita";
              case 0x9122: return "Via #2: Registro direzioni dati B";
              case 0x9123: return "Via #2: Registro direzioni dati A";
              case 0x9124: return "Via #2: Timer 1 latch byte basso";
              case 0x9125: return "Via #2: Timer 1 latch byte alto";
              case 0x9126: return "Via #2: Timer 1 contatore di byte basso";
              case 0x9127: return "Via #2: Timer 1 contatore di byte alto";
              case 0x9128: return "Via #2: Timer 2 byte basso";
              case 0x9129: return "Via #2: Timer 2 byte alto";
              case 0x912A: return "Via #2: Registro a scorrimento";
              case 0x912B: return "Via #2: Registro di controllo ausiliario";
              case 0x912C: return "Via #2: Registro di controllo periferico";
              case 0x912D: return "Via #2: Registro flag di interruzione";
              case 0x912E: return "Via #2: Registro di abilitazione interrupt";
              case 0x912F: return "Via #2: Porat A registro usicita";              
              
              case 0xC000: 
              case 0xC001: return "Vettori ripartenza BASIC";
              case 0xC00C: 
              case 0xC00D: return "Vettori comandi BASIC";
              case 0xC052: 
              case 0xC053: return "Vettori funzioni BASIC";
              case 0xC080: 
              case 0xC081: return "Vettori operatori BASIC";
              case 0xC09E: return "Tabella delle parole chiave del comando BASIC";
              case 0xC129: return "Tabella delle parole chiave BASIC Misc.";
              case 0xC140: return "Tabella delle parole chiave dell'operatore BASIC";
              case 0xC14D: return "Tabella delle parole chiave della funzione BASIC";
              case 0xC19E: return "Tabella dei messaggi di errore";
              case 0xC328: 
              case 0xC329: return "Puntatori dei messaggi di errore";
              case 0xC364: return "Messaggi Misc.: ok";
              case 0xC369: return "Messaggi Misc.: errore";
              case 0xC38A: return "Trova la voce FOR/GOSUB nello Stack";
              case 0xC3B8: return "Spazio aperto in memoria";
              case 0xC3FB: return "Controlla la profondità dello stack";
              case 0xC408: return "Controlla la sovrapposizione della memoria";
              case 0xC435: return "Stampa errore ?OUT OF MEMORY";
              case 0xC437: return "Routine errore";
              case 0xC469: return "Break Entry";
              case 0xC474: return "Ripartenza BASIC";
              case 0xC480: return "Inserimento e identificazione della linea BASIC";
              case 0xC49C: return "Inserimento e identificazione della linea BASIC";
              case 0xC4A2: return "Inserisci testo BASIC";
              case 0xC533: return "Linee Rechain";
              case 0xC560: return "Riga di input nel buffer";
              case 0xC579: return "Tokenizza il buffer di input";
              case 0xC613: return "Cerca il numero di riga";
              case 0xC642: return "Esegue [new]";
              case 0xC65E: return "Esegue [clr]";
              case 0xC68E: return "Resetta TXTPTR";
              case 0xC69C: return "Esegue [list]";
              case 0xC717: return "Gestire il carattere LIST";
              case 0xC742: return "Esegue [for]";
              case 0xC7AE: return "BASIC avvio a caldo";
              case 0xC7C4: return "Controlla Fine programma";
              case 0xC7E1: return "Preparati a eseguire l'istruzione";
              case 0xC7ED: return "Eseguire la parola chiave BASIC";
              case 0xC81D: return "Esegue [restore]";
              case 0xC82C: return "Esegue [stop], [end], break";
              case 0xC857: return "Esegue [cont]";
              case 0xC871: return "Esegue [run]";
              case 0xC883: return "Esegue [gosub]";
              case 0xC8A0: return "Esegue [goto]";
              case 0xC8D2: return "Esegue [return]";
              case 0xC8F8: return "Esegue [data]";
              case 0xC906: return "Cerca istruzione/riga successiva";
              case 0xC928: return "Esegue [if]";
              case 0xC93B: return "Esegue [rem]";
              case 0xC94B: return "Esegue [on]";
              case 0xC96B: return "Recupera il numero di riga dal BASIC";
              case 0xC9A5: return "Esegue [let]";
              case 0xC9C4: return "Assegna numero intero";
              case 0xC9D6: return "Assegna virgola mobile";
              case 0xC9D9: return "Assegna stringa";
              case 0xC9E3: return "Assegna TI$";
              case 0xCA2C: return "Aggiungi cifra a FAC#1";
              case 0xCA80: return "Esegue [print]#";
              case 0xCA86: return "Esegue [cmd]";
              case 0xC99A: return "Stampa stringa dalla memoria";
              case 0xCAA0: return "Esegue [print]";
              case 0xCAB8: return "Variabile di output";
              case 0xCAD7: return "Uscita CR/LF";
              case 0xCAE8: return "Maneggia virgola, TAB(, SPC(";
              case 0xCB1E: return "Stringa di uscita";
              case 0xCB3B: return "Carattere formato uscita";
              case 0xCB4B: return "Gestisci dati non validi";
              case 0xCB7B: return "Esegue [get]";
              case 0xCBA5: return "Esegue [input#]";
              case 0xCBBF: return "Esegue [input]";
              case 0xCBEA: return "Leggere il buffer di ingresso";
              case 0xCBF9: return "Prompt di ingresso";
              case 0xCC06: return "Esegue [read]";
              case 0xCC35: return "Routine di lettura per scopi generali";
              case 0xCCFC: return "Messaggi di errore di ingresso";
              case 0xCD1E: return "Esegue [next]";
              case 0xCD61: return "Controllare il ciclo valido";
              case 0xCD8A: return "Conferma risultato";
              case 0xCD9E: return "Valuta l'espressione nel testo";
              case 0xCE8E: return "Valuta un singolo termine";
              case 0xCEA8: return "Constante - pi";
              case 0xCEAD: return "Continua l'espressione";
              case 0xCEF1: return "Espressione tra parentesi";
              case 0xCEF7: return "Conferma carattere ')'";
              case 0xCEFA: return "Conferma carattere '('";
              case 0xCEFD: return "Conferma carattere vigola";
              case 0xCF08: return "Scrive ?SYNTAX Error";
              case 0xCF0D: return "Impostare la funzione NOT";
              case 0xCF14: return "Identifica variabile riservata";
              case 0xCF28: return "Cerca la variabile";
              case 0xCF48: return "Converti TI in stringa ASCII";
              case 0xCFA7: return "Identifica il tipo di funzione";
              case 0xCFB1: return "Valuta la funzione stringa";
              case 0xCFD1: return "Valuta la funzione numerica";
              case 0xCFE6: return "Esegue [or], [and]";
              case 0xD016: return "Esegue <, =, >";
              case 0xD01B: return "Confronto numerico";
              case 0xD02E: return "Confronto stringa";
              case 0xD07E: return "Esegue [dim]";
              case 0xD08B: return "Identifica variabile";
              case 0xD0E7: return "Individua la variabile ordinaria";
              case 0xD11D: return "Crea nuova variabile";
              case 0xD128: return "Crea variabile";
              case 0xD194: return "Alloca lo spazio del puntatore della matrice";
              case 0xD1A5: return "Constante 32768 in Flpt";
              case 0xD1AA: return "FAC#1 da intero a (AC/YR)";
              case 0xD1B2: return "Valuta il testo per intero";
              case 0xD1B7: return "FAC#1 da intero positivo";
              case 0xD1D1: return "Ottieni parametri array";
              case 0xD218: return "Cerca vettore";
              case 0xD245: return "'?bad subscript error'";
              case 0xD248: return "'?illegal quantity error'";
              case 0xD261: return "Crea vettore";
              case 0xD30E: return "Trova elemento nel vettore";
              case 0xD34C: return "Numero di byte in pedice";
              case 0xD37D: return "Esegue [fre]";
              case 0xD391: return "Converte l'intero in (AC/YR) to virgola mobile";
              case 0xD39E: return "Esegue [pos]";
              case 0xD3A6: return "Conferma modalità programma";
              case 0xD3E1: return "Verifica sintassi di FN";
              case 0xD3F4: return "Esegue [fn]";
              case 0xD465: return "Esegue [str$]";
              case 0xD487: return "Imposta stringa";
              case 0xD4D5: return "Salva descrittore stringa";
              case 0xD4F4: return "Alloca spazio per la stringa";
              case 0xD526: return "Garbage Collection";
              case 0xD5BD: return "Cerca la prossima stringa";
              case 0xD606: return "Raccoglie la stringa";
              case 0xD63D: return "Concatena due stringhe";
              case 0xD67A: return "Memorizza la strinha in RAM alta";
              case 0xD6A3: return "Eseguire le operazioni di pulizia delle stringhe";
              case 0xD6DB: return "Pulisci stack descrittore";
              case 0xD6EC: return "Esegue [chr$]";
              case 0xD700: return "Esegue [left$]";
              case 0xD72C: return "Esegue [right$]";
              case 0xD737: return "Esegue [mid$]";
              case 0xD761: return "Parametri della stringa di pull";
              case 0xD77C: return "Esegue [len]";
              case 0xD782: return "Esci dalla modalità String";
              case 0xD78B: return "Esegue [asc]";
              case 0xD79B: return "Valuta il testo a 1 byte in XR";
              case 0xD7AD: return "Esegue [val]";
              case 0xD7B5: return "Converte stringa ASCII String in numero virgola mobile";
              case 0xD7EB: return "Ottieni parametri per POKE/WAIT";
              case 0xD7F7: return "Converte FAC#1 a intero in LINNUM";
              case 0xD80D: return "Esegue [peek]";
              case 0xD824: return "Esegue [poke]";
              case 0xD82D: return "Esegue [wait]";
              case 0xD849: return "Add 0.5 to FAC#1";
              case 0xD850: return "Esegue sottrazione";
              case 0xD862: return "Normalizza addizione";
              case 0xD867: return "Esegue addizione";
              case 0xD947: return "Complemento a 2 FAC#1";
              case 0xD97E: return "Scrive ?OVERFLOW Error";
              case 0xD983: return "Moltiplicazione per byte zero";
              case 0xD9BC: return "Tabella di costanti numeri a virgola mobile";
              case 0xD9EA: return "Esegue [log]";
              case 0xDA28: return "Esegue moltiplicazione";
              case 0xDA59: return "Moltiplica per un byte";
              case 0xDA8C: return "Carica FAC#2 dalla memoria";
              case 0xDAB7: return "Testare entrambi gli accumulatori";
              case 0xDAD4: return "Overflow/Underflow";
              case 0xDAE2: return "Moltiplica FAC#1 per 10";
              case 0xDAF9: return "Costante 10 in numero virgola mobile";
              case 0xDAFE: return "Divide FAC#1 per 10";
              case 0xDB07: return "Divide FAC#2 per numero virgola mobile in (AC/YR)";
              case 0xDB0F: return "Divide FAC#2 per FAC#1";
              case 0xDBA2: return "Carica FAC#1 dalla memoria";
              case 0xDBC7: return "Memorizza FAC#1 inmemoria";
              case 0xDBC2: return "Copia FAC#2 in FAC#1";
              case 0xDC0C: return "Copia FAC#1 in FAC#2";
              case 0xDC1B: return "Arrotonda FAC#1";
              case 0xDC2B: return "Controlla segno in FAC#1";
              case 0xDC39: return "Esegue [sgn]";
              case 0xDC58: return "Esegue [abs]";
              case 0xDC5B: return "Compara FAC#1 con la memoria";
              case 0xDC9B: return "Compara FAC#1 con intero";
              case 0xDCCC: return "Esegue [int]";
              case 0xDCF3: return "Converte stringa ASCII in un numero in FAC#1";
              case 0xDDB3: return "Costanti conversione stringa";
              case 0xDDC2: return "Scrive 'IN' e numero linea";
              case 0xDDDD: return "Convette FAC#1 in stringa ASCII";
              case 0xDE68: return "Converte TI in stringa";
              case 0xDF11: return "Tabelle di costanti";
              case 0xDF71: return "Esegue [sqr]";
              case 0xDF7B: return "Esegue elevamento a potenza ($)";
              case 0xDFB4: return "Nega FAC#1";
              case 0xDFBF: return "Tabella di costanti";
              case 0xDFED: return "Esegue [exp]";
              case 0xE040: return "Valutazione della serie";
              case 0xE08A: return "Costanti per RND";
              case 0xE094: return "Esegue [rnd]";
              case 0xE0F6: return "Gestire gli errori di I/O in BASIC";
              case 0xE109: return "Carattere di uscita";
              case 0xE10F: return "Carattere di ingresso";
              case 0xE115: return "Predisposizione per output";
              case 0xE11B: return "Predisposizione per input";
              case 0xE121: return "Ottieni un carattere";
              case 0xE127: return "Esegue [sys]";
              case 0xE153: return "Esegue [save]";
              case 0xE162: return "Esegue [verify/load]";
              case 0xE1BB: return "Esegue [open]";
              case 0xE1C4: return "Esegue [close]";
              case 0xE1D1: return "Ottiene paramatri per LOAD/SAVE";
              case 0xE1DB: return "Ottieni il parametro successivo di un byte";
              case 0xE203: return "Verificare i parametri predefiniti";
              case 0xE20B: return "Verificare per virgola";
              case 0xE216: return "Ottieni parametri per OPEN/CLOSE";
              case 0xE261: return "Esegue [cos]";
              case 0xE268: return "Esegue [sin]";
              case 0xE2B1: return "Esegue [tan]";
              case 0xE2DD: return "Tabella delle costanti trigonometriche: 1.570796327=pi/2";
              case 0xE2E2: return "Tabella delle costanti trigonometriche: 6.28318531=pi*2";
              case 0xE2E7: return "Tabella delle costanti trigonometriche: 0.25";
              case 0xE2EC: return "Tabella delle costanti trigonometriche: #05 (counter)";
              case 0xE2ED: return "Tabella delle costanti trigonometriche: -14.3813907";
              case 0xE2F2: return "Tabella delle costanti trigonometriche: 42.0077971";
              case 0xE2F7: return "Tabella delle costanti trigonometriche: -76.7041703";
              case 0xE2FC: return "Tabella delle costanti trigonometriche: 81.6052237";
              case 0xE301: return "Tabella delle costanti trigonometriche: -41.3417021";
              case 0xE306: return "Tabella delle costanti trigonometriche: 6.28318531";
              case 0xE30B: return "Esegue [atn]";
              case 0xE33B: return "Tabella delle costanti ATN: #0b	(counter)";
              case 0xE3EC: return "Tabella delle costanti ATN: -0.000684793912";
              case 0xE341: return "Tabella delle costanti ATN: 0.00485094216";
              case 0xE346: return "Tabella delle costanti ATN: -0.161117018";
              case 0xE34B: return "Tabella delle costanti ATN: 0.034209638";
              case 0xE350: return "Tabella delle costanti ATN: -0.0542791328";
              case 0xE355: return "Tabella delle costanti ATN: 0.0724571965";
              case 0xE35A: return "Tabella delle costanti ATN: -0.0898023954";
              case 0xE35F: return "Tabella delle costanti ATN: 0.110932413";
              case 0xE364: return "Tabella delle costanti ATN: -0.142839808";
              case 0xE369: return "Tabella delle costanti ATN: 0.19999912";
              case 0xE36E: return "Tabella delle costanti ATN: -0.333333316";
              case 0xE373: return "Tabella delle costanti ATN: 1.00";
              case 0xE378: return "BASIC Avviamento a freddo";
              case 0xE387: return "CHRGET per pagina zero";
              case 0xE39F: return "Seme RND per pagina zero 0.811635157";
              case 0xE3A4: return "Initializza BASIC RAM";
              case 0xE404: return "Messaggio di accensione in uscita";
              case 0xE429: return "Messaggio di accensione: ' bytes free'";
              case 0xE436: return "Messaggio di accensione: '**** cbm basic v2 ****'";
              case 0xE44F: return "Tabella di vettori BASIC (per 0300)";
              case 0xE45B: return "Inizializza i vettori";
              case 0xE467: return "BASIC Riavvio a caldo [RUNSTOP-RESTORE]";
              case 0xE47C: return "Byte inutilizzati per patch future";
              case 0xE4A0: return "Uscita seriale 1";
              case 0xE4A9: return "Uscita seriale 0";
              case 0xE4B2: return "Ottieni dati seriali e clock ingresso";
              case 0xE4BC: return "Ottieni la patch dell'indirizzo secondario per seriale LOAD/VERIFY";
              case 0xE4C1: return "Patch di caricamento riposizionata per serialeLOAD/VERIFY";
              case 0xE4CF: return "Patch di scrittura su nastro per CLOSE";
              case 0xE4DA: return "Non usato";
              case 0xE500: return "Restituire l'indirizzo di base I/O";
              case 0xE505: return "Ritorna organizzazione dello schermo";
              case 0xE50A: return "Leggi/Imposta la posizione X/Y del cursore";
              case 0xE518: return "Inizializza l'I/O";
              case 0xE55F: return "Pulisce lo schermo";
              case 0xE581: return "Cursore in posizione base";
              case 0xE587: return "Imposta i puntatori sullo schermo";
              case 0xE5B5: return "Imposta valori predefiniti I/O (Entrata inutilizzata)";
              case 0xE5BB: return "Imposta valori predefiniti I/O";
              case 0xE5CF: return "Ottieni carattere dal buffer della tastiera";
              case 0xE5E5: return "Input dalla tastiera";
              case 0xE64F: return "Input dallo schermo o dalla tastiera";
              case 0xE6B8: return "Test delle citazioni";
              case 0xE6C5: return "Imposta stampa schermo";
              case 0xEAEA: return "Avanza cursore";
              case 0xE719: return "Ritira il cursore";
              case 0xE72D: return "Torna alla riga precedente";
              case 0xE742: return "Uscita sullo schermo";
              case 0xE756: return "-unshifted characters-";
              case 0xE800: return "-shifted characters-";
              case 0xE8C3: return "Vai alla prossima riga";
              case 0xE8D8: return "Uscita";
              case 0xE8E8: return "Controllare il decremento della linea";
              case 0xE8FA: return "Controllare l'incremento della linea";
              case 0xE912: return "Setta il codice colore";
              case 0xE921: return "Tabella codice colore";
              case 0xE975: return "Scrolla lo schermo";
              case 0xE9EE: return "Apre uno spazio nellpo schermo";
              case 0xEA56: return "Muove una linea schermo";
              case 0xEA6E: return "Sincronizza trasferimento colore";
              case 0xEA7E: return "Imposta inizio riga";
              case 0xEA8D: return "Pulisce la linea di schermo";
              case 0xEAA1: return "Stampa sullo schermo";
              case 0xEAB2: return "Sincronizza puntatore colore";
              case 0xEABF: return "Punto di ingresso IRQ principale";
              case 0xEB1E: return "Scansione tastiera";
              case 0xEB71: return "Processa chiave immagine";
              case 0xEC46: return "Puntatori alle tabelle di decodifica della tastiera	";
              case 0xEC5E: return "Tabella di decodifica della tastierae - Unshifted";
              case 0xEC9F: return "Tabella di decodifica della tastiera - Shifted";
              case 0xECE0: return "Tabella di decodifica della tastiera - Commodore";
              case 0xED21: return "Controllo grafica/testo";
              case 0xED69: return "Tabella di decodifica della tastiera";
              case 0xEDA3: return "Tabella di decodifica della tastiera - controllo";
              case 0xEDE4: return "Tabella di configurazione del chip video";
              case 0xEDF4: return "Equivalente Shift-Run";
              case 0xEDFD: return "Indirizzi riga schermo byte bassi";
              case 0xEE14: return "Invia il comando TALK su Serial Bus";
              case 0xEE17: return "Invia il comando LISTEN su Serial Bus";
              case 0xEE49: return "Invia dati su bus seriale";
              case 0xEEB4: return "Segnala errori: #80 - device not present";
              case 0xEEB7: return "Segnala errori: #03 - write timeout";
              case 0xEEC0: return "Invia l'indirizzo secondario di LISTEN";
              case 0xEEC5: return "Pulisce ATN";
              case 0xEECE: return "Invia l'indirizzo secondario di TALK";
              case 0xEED3: return "Aspetta l'orologio";
              case 0xEEE4: return "Invia seriale differito";
              case 0xEEF6: return "Invia UNTALK su bus seriale";
              case 0xEF04: return "Invia UNLISTEN su bus seriale";
              case 0xEF19: return "Ricevi da bus seriale";
              case 0xEE84: return "Orologio seriale acceso";
              case 0xEF8D: return "Orologio seriale spento";
              case 0xEF96: return "Ritardo di 1 ms";
              case 0xEFA3: return "Invia RS-232";
              case 0xEFEE: return "Invia un nuovo byte RS-232";
              case 0xF016: return "Errore 'No DSR'";
              case 0xF019: return "Errore 'No CTS'";
              case 0xF021: return "Disabilita il timer";
              case 0xF027: return "Calcola il conteggio dei bit";
              case 0xF036: return "Riceve RS-232";
              case 0xF05B: return "Imposta per ricevere";
              case 0xF068: return "Elabora byte RS-232";
              case 0xF0BC: return "Invia a RS-232";
              case 0xF0ED: return "Invia al buffer RS-232";
              case 0xF116: return "Ingresso da RS-232";
              case 0xF14F: return "Ottieni da RS-232";
              case 0xF160: return "Bus seriale inattivo";
              case 0xF174: return "Tabella dei messaggi I/O del kernel: ' i/o error #'";
              case 0xF1DF: return "Tabella dei messaggi I/O del kernel: 'ok'";
              case 0xF1E2: return "Stampa messaggio se diretto";
              case 0xF1E6: return "Stampa messaggio";
              case 0xF1F5: return "Ottiene un byte";
              case 0xF20E: return "Immettere un byte";
              case 0xF250: return "Ottiene da Cassetta/Seriale/RS-232";
              case 0xF27A: return "Emetti un carattere";
              case 0xF2C7: return "Imposta dispositivo di input";
              case 0xF30E: return "Imposta dispositivo di output";
              case 0xF34A: return "Chiude file";
              case 0xF3CF: return "Cerca file";
              case 0xF3DF: return "Imposta i valori dei file";
              case 0xF3EF: return "Annulla tutti i file";
              case 0xF3F3: return "Ripristina l'I/O predefinito";
              case 0xF40A: return "Apei file";
              case 0xF495: return "Invia indirizzo secondario";
              case 0xF4C7: return "Apri RS-232";
              case 0xF542: return "Carica RAM dal dispositivo";
              case 0xF549: return "-load-";
              case 0xF55C: return "Carica file da bus seriale";
              case 0xF5CA: return "Carica file da nastro";
              case 0xF647: return "Stampa \"SEARCHING\"";
              case 0xF659: return "Stampa nome file";
              case 0xF66A: return "Stampa \"LOADING/VERIFYING\"";
              case 0xF675: return "Scrive la RAM nel dispositivo";
              case 0xF685: return "-save-";
              case 0xF692: return "Scrive nel bus seriale";
              case 0xF6F1: return "Scrive nella cassetta";
              case 0xF728: return "Scrive \"SAVING\"";
              case 0xF734: return "Incrementa l'orologio in tempo reale";
              case 0xF760: return "Leggi l'orologio in tempo reale";
              case 0xF767: return "Imposta orologio in tempo reale";
              case 0xF770: return "Verifica il tasto STOP";
              case 0xF77E: return "Messaggi di errore I/O in uscita: 'too many files'";
              case 0xF781: return "Messaggi di errore I/O in uscita: 'file open'";
              case 0xF784: return "Messaggi di errore I/O in uscita: 'file not open'";
              case 0xF787: return "Messaggi di errore I/O in uscita: 'file not found'";
              case 0xF78A: return "Messaggi di errore I/O in uscita: 'device not present'";
              case 0xF78D: return "Messaggi di errore I/O in uscita: 'not input file'";
              case 0xF790: return "Messaggi di errore I/O in uscita: 'not output file'";
              case 0xF793: return "Messaggi di errore I/O in uscita: 'missing filename'";
              case 0xF796: return "Messaggi di errore I/O in uscita: 'illegal device number'";              
              case 0xF7AF: return "Trova qualsiasi intestazione del nastro";
              case 0xF7E7: return "Scrivi intestazione nastro";
              case 0xF84D: return "Ottieni indirizzo buffer";
              case 0xF854: return "Imposta le statistiche del buffer/i puntatori finali";
              case 0xF867: return "Trova intestazione nastro specifica";
              case 0xF88A: return "Puntatore del nastro bump";
              case 0xF894: return "Stampa \"PRESS PLAY ON TAPE\"";
              case 0xF8AB: return "Controlla lo stato del nastro";
              case 0xF8B7: return "Stampa \"PRESS RECORD...\"";
              case 0xF8C0: return "Avvia la lettura del nastro";
              case 0xF8E3: return "Avvia scrittura su nastro";
              case 0xF8F4: return "Codice nastro comune";
              case 0xF94B: return "Controllare l'arresto del nastro";
              case 0xF95D: return "Imposta tempo di lettura";
              case 0xF98E: return "Leggi bit di nastro";
              case 0xFAAD: return "Memorizza i caratteri del nastro";
              case 0xFBB2: return "Reimposta puntatore nastro";
              case 0xFBDB: return "Nuova configurazione del personaggio";
              case 0xFBEA: return "Invia tono su nastro";
              case 0xFC06: return "Scrivi dati su nastro";
              case 0xFC95: return "Scrivi Tape Leader";
              case 0xFCCF: return "Ripristina IRQ normale";
              case 0xFCF6: return "Imposta vettore IRQ";
              case 0xFD08: return "Ferma il motore del nastro";
              case 0xFD11: return "Seleziona il puntatore di lettura/scrittura";
              case 0xFD1B: return "Puntatore di lettura/scrittura a sbalzo";
              case 0xFD22: return "Ingresso RESET all'accensione";
              case 0xFD3F: return "Verificare la presenza di una ROM";
              case 0xFD4D: return "Maschera ROM 'a0CBM'";
              case 0xFD52: return "Ripristina i vettori Kernal (at 0314)";
              case 0xFD57: return "Cambia vettori per l'utente";
              case 0xFD6D:
              case 0xFD6E: return "Vettori di ripristino del kernel";
              case 0xFD8D: return "Inizializza le costanti di sistema";
              case 0xFDF1:
              case 0xFDF2: return "Vettori IRQ per I/O su nastro";
              case 0xFDF9: return "Inizializza I/O";
              case 0xFE39: return "Abilita timer";
              case 0xFE49: return "Imposta nome file";
              case 0xFE50: return "Imposta i parametri del file logico";
              case 0xFE57: return "Ottieni parola di stato I/O";
              case 0xFE66: return "Controlla i messaggi del sistema operativo";
              case 0xFE6F: return "Imposta timeout IEEE";
              case 0xFE73: return "Imposta/leggi la parte superiore della memoria";
              case 0xFE82: return "Imposta/Leggi il fondo della memoria";
              case 0xFEA9: return "Voce di trasferimento NMI";
              case 0xFED2: return "Avvio a caldo di base [BRK]";
              case 0xFF56: return "Esci da Interruzione";
              case 0xFF5B: return "Tabella dei tempi RS-232";
              case 0xFF72: return "Immissione trasferimento IRQ";
              case 0xFF8A: return "Tabella di salto del kernel: ripristino dei vettori";
              case 0xFF8D: return "Tabella di salto del kernel: modifica dei vettori per l'utente";
              case 0xFF90: return "Tabella di salto del kernel: controlla i messaggi del sistema operativo";
              case 0xFF93: return "Tabella di salto del kernel: invia SA dopo l'ascolto";
              case 0xFF96: return "Tabella di salto del kernel: invia SA dopo la conversazione";
              case 0xFF99: return "Tabella di salto del kernel: imposta/Leggi RAM di sistema in alto";
              case 0xFF9C: return "Tabella di salto del kernel: imposta/legge la parte inferiore della RAM di sistema";
              case 0xFF9F: return "Tabella di salto del kernel: scansione della tastiera";
              case 0xFFA2: return "Tabella di salto del kernel: impostare il timeout in IEEE";
              case 0xFFA5: return "Tabella di salto del kernel: byte seriale handshake in ingresso";
              case 0xFFA8: return "Tabella di salto del kernel: thandshake seriale byte in uscita";
              case 0xFFAB: return "Tabella di salto del kernel: comando bus seriale UNTALK";
              case 0xFFAE: return "Tabella di salto del kernel: comando bus seriale UNLISTEN";
              case 0xFFB1: return "Tabella di salto del kernel: comando bus seriale LISTEN";
              case 0xFFB4: return "Tabella di salto del kernel: comando bus seriale TALK";
              case 0xFFB7: return "Tabella di salto del kernel: leggere la parola di stato I/O";
              case 0xFFBA: return "Tabella di salto del kernel: imposta i parametri del file logico";
              case 0xFFBD: return "Tabella di salto del kernel: imposta nome file";
              case 0xFFC0: return "Tabella di salto del kernel: vettore apertura [F40A]";
              case 0xFFC3: return "Tabella di salto del kernel: vettore chiusura [F34A]";
              case 0xFFC6: return "Tabella di salto del kernel: imposta input [F2C7]";
              case 0xFFC9: return "Tabella di salto del kernel: imposta output [F309]";
              case 0xFFCC: return "Tabella di salto del kernel: ripristina vettore I/O [F353]";
              case 0xFFCF: return "Tabella di salto del kernel: vettore input, chrin [F20E]";
              case 0xFFD2: return "Tabella di salto del kernel: vettore output, chrout [F27A]";
              case 0xFFD5: return "Tabella di salto del kernel: carica RAM dal dispositivo";
              case 0xFFD8: return "Tabella di salto del kernel: scrive RAM dal dispositivo";
              case 0xFFDB: return "Tabella di salto del kernel: imposta orologio in tempo reale";
              case 0xFFDE: return "Tabella di salto del kernel: leggi l'orologio in tempo reale";
              case 0xFFE1: return "Tabella di salto del kernel: vettore di arresto di prova [F770]";
              case 0xFFE4: return "Tabella di salto del kernel: ottieni dalla tastiera [F1F5]";
              case 0xFFE7: return "Tabella di salto del kernel: chiudi tutti i canali e i file [F3EF]";
              case 0xFFEA: return "Tabella di salto del kernel: incrementa l'orologio in tempo reale";
              case 0xFFED: return "Tabella di salto del kernel: torna organizzazione dello schermo";
              case 0xFFF0: return "Tabella di salto del kernel: leggi/imposta la posizione X/Y del cursore";
              case 0xFFF3: return "Tabella di salto del kernel: restituire l'indirizzo di base I/O";
              case 0xFFFA: return "NMI";
              case 0xFFFC: return "RESET";
              case 0xFFFE: return "IRQ";
            default:
                if ((addr>=0x73) && (addr<=0x8A)) return "CHRGET subroutine (get BASIC char)";  
                if ((addr>=0xD9) && (addr<=0xF0)) return "Screen line link table"; 
                if ((addr>=0x100) && (addr<=0x10A)) return "Floating to ASCII work area";
                if ((addr>=0x10B) && (addr<=0x013E)) return "Tape error log";
                if ((addr>=0x100) && (addr<=0x1FF)) return "Processor stack area";
                if ((addr>=0x200) && (addr<=0x258)) return "Basic input buffer";
                if ((addr>=0x259) && (addr<=0x262)) return "Logical file table";
                if ((addr>=0x263) && (addr<=0x26C)) return "Device # table";
                if ((addr>=0x26D) && (addr<=0x276)) return "Secondary Address table";
                if ((addr>=0x277) && (addr<=0x280)) return "Keyboard buffer";
                if ((addr>=0x2A1) && (addr<=0x2FF)) return "Program indirects";
                if ((addr>=0x33C) && (addr<=0x03FB)) return "Cassette buffer";
                if ((addr>=0x400) && (addr<=0x0FFF)) return "3K expansion RAM area";
                if ((addr>=0x1000) && (addr<=0x11FF)) return "User Basic area/Screen memory";
                if ((addr>=0x1200) && (addr<=0x1DFF)) return "User Basic area";
                if ((addr>=0x1E00) && (addr<=0x1FFF)) return "Screen memory";
                if ((addr>=0x2000) && (addr<=0x3FFF)) return "8K expansion RAM/ROM block 1";
                if ((addr>=0x4000) && (addr<=0x5FFF)) return "8K expansion RAM/ROM block 2";
                if ((addr>=0x6000) && (addr<=0x7FFF)) return "8K expansion RAM/ROM block 3";
                if ((addr>=0x8000) && (addr<=0x83FF)) return "4K Character generator ROM/Upper case and graphics";
                if ((addr>=0x8400) && (addr<=0x87FF)) return "4K Character generator ROM/Reversed upper case and graphics";
                if ((addr>=0x8800) && (addr<=0x8BFF)) return "4K Character generator ROM/Upper and lower case";
                if ((addr>=0x8C00) && (addr<=0x8FFF)) return "4K Character generator ROM/Reversed upper and lower case";
                if ((addr>=0x9400) && (addr<=0x95FF)) return "location of COLOR RAM with additional RAM at blk 1";
                if ((addr>=0x9600) && (addr<=0x97FF)) return "Normal location of COLOR RAM";
                if ((addr>=0x9800) && (addr<=0x9BFF)) return "I/O block 2";
                if ((addr>=0x9C00) && (addr<=0x9FFF)) return "I/O block 3";
                if ((addr>=0xA000) && (addr<=0xBFFF)) return "8K decoded block for expansion ROM";                
            }
          default:
            switch ((int)addr) {    
              case 0x00: return "Jump for USR";
              case 0x01: 
              case 0x02: return "Vector for USR";
              case 0x03: 
              case 0x04: return "Float-Fixed vector";
              case 0x05: 
              case 0x06: return "Fixed-Float vector";
              case 0x07: return "Search character";
              case 0x08: return "Scan-quotes flag";
              case 0x09: return "TAB column save";
              case 0x0A: return "0=LOAD, 1=VERIFY";
              case 0x0B: return "Input buffer pointer/# subscript";
              case 0x0C: return "Default DIM flag";
              case 0x0D: return "Type: FF=string, 00=numeric";
              case 0x0E: return "Type: 80=integer, 00=floating point";
              case 0x0F: return "DATA scan/LlST quote/memory flag";
              case 0x10: return "Subscript/FNx flag";
              case 0x11: return "0 = INPUT;$40 = GET;$98 = READ";
              case 0x12: return "ATN sign/Comparison eval flag";
              case 0x13: return "Current l/O prompt flag";
              case 0x14:
              case 0x15: return "Integer value";
              case 0x16: return "Pointer: temporary string stack";
              case 0x17: 
              case 0x18: return "Last temp string vector";
              case 0x19:         
              case 0x1A:
              case 0x1B:                  
              case 0x1C:    
              case 0x1D:    
              case 0x1E:    
              case 0x1F:    
              case 0x20: 
              case 0x21: return "Stack for temporary strings";              
              case 0x22: 
              case 0x23: 
              case 0x24:
              case 0x25: return "Utility pointer area";  
              case 0x26:
              case 0x27:
              case 0x28:
              case 0x29:
              case 0x2A: return "Product area for multiplication";
              case 0x2B: 
              case 0x2C: return "Pointer: Start of Basic";
              case 0x2D: 
              case 0x2E: return "Pointer: Start of Variables";
              case 0x2F: 
              case 0x30: return "Pointer: Start of Arrays";
              case 0x31: 
              case 0x32: return "Pointer: End of Arrays";
              case 0x33: 
              case 0x34: return "Pointer: String storage (moving down)";
              case 0x35: 
              case 0x36: return "Utility string pointer";
              case 0x37: 
              case 0x38: return "Pointer: Limit of memory";
              case 0x39: 
              case 0x3A: return "Current Basic line number";
              case 0x3B: 
              case 0x3C: return "Previous Basic line number";
              case 0x3D: 
              case 0x3E: return "Pointer: Basic statement for CONT";
              case 0x3F: 
              case 0x40: return "Current DATA line number";
              case 0x41: 
              case 0x42: return "Current DATA address"; 
              case 0x43: 
              case 0x44: return "Input vector";
              case 0x45: 
              case 0x46: return "Current variable name";
              case 0x47: 
              case 0x48: return "Current variable address";
              case 0x49: 
              case 0x4A: return "Variable pointer for FOR/NEXT";
              case 0x4B: 
              case 0x4C: return "Y-save; op-save; Basic pointer save";
              case 0x4D: return "Comparison symbol accumulator";
              case 0x4E:
              case 0x4F:
              case 0x50:
              case 0x51: 
              case 0x52: 
              case 0x53: return "Misc work area, pointers, etc";
              case 0x54: 
              case 0x55: 
              case 0x56: return "Jump vector for functions";
              case 0x57:     
              case 0x58:
              case 0x59:      
              case 0x5A:      
              case 0x5B:      
              case 0x5C:
              case 0x5D: 
              case 0x5E:
              case 0x5F: 
              case 0x60: return "Misc numeric work area";   
              case 0x61: return "Accum#1: Exponent";
              case 0x62:
              case 0x63: 
              case 0x64: 
              case 0x65: return "Accum#1: Mantissa"; 
              case 0x66: return "Accum#1: Sign";
              case 0x67: return "Series evaluation constant pointer";
              case 0x68: return "Accum#1 hi-order (overflow)";
              case 0x69: 
              case 0x6A: 
              case 0x6B:
              case 0x6C: 
              case 0x6D: 
              case 0x6E: return "Accum#2: Exponent, etc.";
              case 0x6F: return "Sign comparison, Acc#1 vs #2"; 
              case 0x70: return "Accum#1 lo-order (rounding)";
              case 0x71: 
              case 0x72: return "Cassette buffer length/Series pointer";
              case 0x7A: 
              case 0x7B: return "Basic pointer (within subroutine)";              
              case 0x8B: 
              case 0x8C:
              case 0x8D: 
              case 0x8E:
              case 0x8F: return "RND seed value";
              case 0x90: return "Status word ST";
              case 0x91: return "Keyswitch PIA: STOP and RVS flags";
              case 0x92: return "Timing constant for tape";  
              case 0x93: return "Load=0, Verify=1";
              case 0x94: return "Serial output: deferred char flag";
              case 0x95: return "Serial deferred character";
              case 0x96: return "Tape EOT received";
              case 0x97: return "Register save";
              case 0x98: return "How many open files";
              case 0x99: return "Input device (normally 0)";
              case 0x9A: return "Output (CMD) device, normally 3";
              case 0x9B: return "Tape character parity";
              case 0x9C: return "Byte-received flag";
              case 0x9D: return "Direct=$80/RUN=0 output control"; 
              case 0x9E: return "Tape Pass 1 error log/char buffer";
              case 0x9F: return "Tape Pass 2 error log corrected"; 
              case 0xA0: 
              case 0xA1:
              case 0xA2: return "Jiffy Clock (HML)";
              case 0xA3: return "Serial bit count/EOI flag"; 
              case 0xA4: return "Cycle count";
              case 0xA5: return "Countdown, tape write/bit count";
              case 0xA6: return "Pointer: tape buffer";
              case 0xA7: return "Tape Write ldr count/Read pass/inbit";
              case 0xA8: return "Tape Write new byte/Read error/inbit";
              case 0xA9: return "Write start bit/Read bit err/stbit";
              case 0xAA: return "Tape Scan;Cnt;Ld;End/byte assy";
              case 0xAB: return " Write lead length/Rd checksum/parity";
              case 0xAC:
              case 0xAD: return "Pointer: tape buffer, scrolling";
              case 0xAE: 
              case 0xAF: return "Tape end addresses/End of program";
              case 0xB0:
              case 0xB1: return "Tape timing constants";
              case 0xB2:
              case 0xB3: return "Pointer: start of tape buffer";              
              case 0xB4: return "Tape timer (1 =enable); bit cnt";
              case 0xB5: return "Tape EOT/RS-232 next bit to send";
              case 0xB6: return "Read character error/outbyte buffer";
              case 0xB7: return "# characters in file name";
              case 0xB8: return "Current logical file";
              case 0xB9: return "Current secondary address";
              case 0xBA: return "Current device";
              case 0xBB: 
              case 0xBC: return "Pointer: to file name";
              case 0xBD: return "Write shift word/Read input char";
              case 0xBE: return "# blocks remaining to Write/Read";
              case 0xBF: return "Serial word buffer";
              case 0xC0: return "Tape motor interlock";
              case 0xC1:
              case 0xC2: return "I/O start addresses"; 
              case 0xC3:
              case 0xC4: return "KERNAL setup pointer";
              case 0xC5: return "Current key pressed"; 
              case 0xC6: return "# chars in keyboard buffer";
              case 0xC7: return "Screen reverse flag";
              case 0xC8: return "Pointer: End-of-line for input";
              case 0xC9:
              case 0xCA: return "Input cursor log (row, column)";
              case 0xCB: return "Which key: 64 if no key";
              case 0xCC: return "Cursor enable (0=flash cursor)";
              case 0xCD: return "Cursor timing countdown";
              case 0xCE: return "Character under cursor";
              case 0xCF: return "Cursor in blink phase"; 
              case 0xD0: return "Input from screen/from keyboard"; 
              case 0xD1:
              case 0xD2: return "Pointer to screen line";
              case 0xD3: return "Position of cursor on above line";
              case 0xD4: return "0=direct cursor, else programmed";
              case 0xD5: return "Current screen line length";
              case 0xD6: return "Row where cursor lives";
              case 0xD7: return "Last inkey/checksum/buffer";
              case 0xD8: return "# of INSERTs outstanding"; 
              case 0xF1: return "Dummy screen link";
              case 0xF2: return "Screen row marker";
              case 0xF3: 
              case 0xF4: return "Screen color pointer";   
              case 0xF5:
              case 0xF6: return "Keyboard pointer";
              case 0xF7: 
              case 0xF8: return "RS-232 Rcv pointer";
              case 0xF9: 
              case 0xFA: return "RS-232 Tx pointer"; 
              case 0xFB:
              case 0xFC:
              case 0xFD:
              case 0xFE: return "Operating system free zero page space";
              case 0xFF: return "Basic storage";  
                
              case 0x281:
              case 0x282: return "Start of memory for op system";
              case 0x283:
              case 0x284: return "Top of memory for op system";   
              case 0x285: return "Serial bus timeout flag";
              case 0x286: return "Current color code"; 
              case 0x287: return "Color under cursor";    
              case 0x288: return "Screen memory page";                
              case 0x289: return "Max size of keyboard buffer";
              case 0x28A: return "Key repeat (128=repeat all keys)";
              case 0x28B: return "Repeat speed counter";
              case 0x28C: return "Repeat delay counter";
              case 0x28D: return "Keyboard Shift/Control flag";
              case 0x28E: return "Last keyboard shift pattern";
              case 0x28F:
              case 0x290: return "Pointer: decode logic";
              case 0x291: return "Shift mode switch (0=enabled, 128=locked)";
              case 0x292: return "Autoscrolldownflag (0=on, <>0=off)";
              case 0x293: return "RS-232 control register";
              case 0x294: return "RS-232 command register";
              case 0x295:
              case 0x296: return "Nonstandard (Bit time/2-100)";
              case 0x297: return "RS-232 status register";
              case 0x298: return "Number of bits to send";
              case 0x299: 
              case 0x29A: return "Baud rate (full) bit time";
              case 0x29B: return "RS-232 receive pointer";
              case 0x29C: return "RS-232 input pointer";
              case 0x29D: return "RS-232 transmit pointer";
              case 0x29E: return "RS-232 output pointer";
              case 0x29F:
              case 0x2A0: return "Holds IRQ during tape operations";
           
              case 0x300:
              case 0x301: return "Error message link";
              case 0x302:
              case 0x303: return "Basic warm start link";
              case 0x304:
              case 0x305: return "Crunch Basic tokens link";
              case 0x306:
              case 0x307: return "Print tokens link";
              case 0x308:
              case 0x309: return "Start new Basic code link";
              case 0x30A:
              case 0x30B: return "Get arithmetic element link";
              case 0x30C: return "Storage for 6502 .A register";
              case 0x30D: return "Storage for 6502 .X register";
              case 0x30E: return "Storage for 6502 .Y register";
              case 0x30F: return "Storage for 6502 .P register";
              case 0x314:
              case 0x315: return "Hardware (IRQ) interrupt vector [EABF]";
              case 0x316:
              case 0x317: return "Break interrupt vector [FED2]";
              case 0x318:
              case 0x319: return "NMI interrupt vector [FEAD]";
              case 0x31A:
              case 0x31B: return "OPEN vector [F40A]";
              case 0x31C:
              case 0x31D: return "CLOSE vector [F34A]";
              case 0x31E:
              case 0x31F: return "Set-input vector [F2C7]";
              case 0x320:
              case 0x321: return "Set-output vector [F309]";
              case 0x322:
              case 0x323: return "Restore l/O vector [F3F3]";
              case 0x324:
              case 0x325: return "INPUT vector [F20E]";
              case 0x326:
              case 0x327: return "Output vector [F27A]";
              case 0x328:
              case 0x329: return "Test-STOP vector [F770]";
              case 0x32A:
              case 0x32B: return "GET vector [F1F5]";
              case 0x32C:
              case 0x32D: return "Abort l/O vector [F3EF]";
              case 0x32E:
              case 0x32F: return "User vector (default BRK) [FED2]";
              case 0x330:
              case 0x331: return "Link to load RAM [F549]";
              case 0x332:
              case 0x333: return "Link to save RAM [F685]";     
              
              case 0x9000: return "Vic: bits 0-6 horizontal centering, bit 7 sets interlace scan";
              case 0x9001: return "Vic: vertical centering";
              case 0x9002: return "Vic: bits 0-6 set # of columns, bit 7 is part of video matrix address";
              case 0x9003: return "Vic: bits 1-6 set # of rows, bit 0 sets 8x8 or 16x8 chars";
              case 0x9004: return "Vic: TV raster beam line";
              case 0x9005: return "Vic: bits 0-3 start of character memory (default = 0), bits 4-7 is rest of video address (default= F)";
              case 0x9006: return "Vic: horizontal position of light pen";
              case 0x9007: return "Vic: vertical position of light pen";
              case 0x9008: return "Vic: digitized value of paddle X";
              case 0x9009: return "Vic: digitized value of paddle Y";
              case 0x900A: return "Vic: frequency for oscillator 1 (low) (on: 128-255)";
              case 0x900B: return "Vic: frequency for oscillator 2 (medium) (on: 128-255)";
              case 0x900C: return "Vic: frequency for oscillator 3 (high) (on: 128-255)";
              case 0x900D: return "Vic: frequency of noise source";
              case 0x900E: return "Vic: bit 0-3 sets volume of all sound, bits 4-7 are auxiliary color information";
              case 0x900F: return "Vic: Screen and border color register, bits 4-7 select background color, bits 0-2 select border color, bit 3 selects inverted or normal mode";
              
              case 0x9110: return "Via #1: Port B output register";
              case 0x9111: return "Via #1: Port A output register";
              case 0x9112: return "Via #1: Data direction register B";
              case 0x9113: return "Via #1: Data direction register A";
              case 0x9114: return "Via #1: Timer 1 low byte";
              case 0x9115: return "Via #1: Timer 1 high byte & counter";
              case 0x9116: return "Via #1: Timer 1 low byte";
              case 0x9117: return "Via #1: Timer 1 high byte";
              case 0x9118: return "Via #1: Timer 2 low byte";
              case 0x9119: return "Via #1: Timer 2 high byte";
              case 0x911A: return "Via #1: Shift register";
              case 0x911B: return "Via #1: Auxiliary control register";
              case 0x911C: return "Via #1: Peripheral control register";
              case 0x911D: return "Via #1: Interrupt flag register";
              case 0x911E: return "Via #1: Interrupt enable register";
              case 0x911F: return "Via #1: Port A (Sense cassette switch)";
              
              case 0x9120: return "Via #2: Port B output register";
              case 0x9121: return "Via #2: Port A output register";
              case 0x9122: return "Via #2: Data direction register B";
              case 0x9123: return "Via #2: Data direction register A";
              case 0x9124: return "Via #2: Timer 1 low byte latch";
              case 0x9125: return "Via #2: Timer 1 high byte latch";
              case 0x9126: return "Via #2: Timer 1 low byte counter";
              case 0x9127: return "Via #2: Timer 1 high byte counter";
              case 0x9128: return "Via #2: Timer 2 low byte";
              case 0x9129: return "Via #2: Timer 2 high byte";
              case 0x912A: return "Via #2: Shift register";
              case 0x912B: return "Via #2: Auxiliary control register";
              case 0x912C: return "Via #2: Peripheral control register";
              case 0x912D: return "Via #2: Interrupt flag register";
              case 0x912E: return "Via #2: Interrupt enable register";
              case 0x912F: return "Via #2: Port A output register";              
              
              case 0xC000: 
              case 0xC001: return "Basic Restart Vectors";
              case 0xC00C: 
              case 0xC00D: return "BASIC Command Vectors";
              case 0xC052: 
              case 0xC053: return "BASIC Function Vectors";
              case 0xC080: 
              case 0xC081: return "BASIC Operator Vectors	";
              case 0xC09E: return "BASIC Command Keyword Table";
              case 0xC129: return "BASIC Misc. Keyword Table";
              case 0xC140: return "BASIC Operator Keyword Table";
              case 0xC14D: return "BASIC Function Keyword Table";
              case 0xC19E: return "Error Message Table";
              case 0xC328: 
              case 0xC329: return "Error Message Pointers";
              case 0xC364: return "Misc. Messages: ok";
              case 0xC369: return "Misc. Messages: error";
              case 0xC38A: return "Find FOR/GOSUB Entry on Stack";
              case 0xC3B8: return "Open Space in Memory";
              case 0xC3FB: return "Check Stack Depth";
              case 0xC408: return "Check Memory Overlap";
              case 0xC435: return "Output ?OUT OF MEMORY Error";
              case 0xC437: return "Error Routine";
              case 0xC469: return "Break Entry";
              case 0xC474: return "Restart BASIC";
              case 0xC480: return "Input & Identify BASIC Line";
              case 0xC49C: return "Get Line Number & Tokenise Text";
              case 0xC4A2: return "Insert BASIC Text";
              case 0xC533: return "Rechain Lines";
              case 0xC560: return "Input Line Into Buffer";
              case 0xC579: return "Tokenise Input Buffer";
              case 0xC613: return "Search for Line Number";
              case 0xC642: return "Perform [new]";
              case 0xC65E: return "Perform [clr]";
              case 0xC68E: return "Reset TXTPTR";
              case 0xC69C: return "Perform [list]";
              case 0xC717: return "Handle LIST Character";
              case 0xC742: return "Perform [for]";
              case 0xC7AE: return "BASIC Warm Start";
              case 0xC7C4: return "Check End of Program";
              case 0xC7E1: return "Prepare to execute statement";
              case 0xC7ED: return "Perform BASIC Keyword";
              case 0xC81D: return "Perform [restore]";
              case 0xC82C: return "Perform [stop], [end], break";
              case 0xC857: return "Perform [cont]";
              case 0xC871: return "Perform [run]";
              case 0xC883: return "Perform [gosub]";
              case 0xC8A0: return "Perform [goto]";
              case 0xC8D2: return "Perform [return]";
              case 0xC8F8: return "Perform [data]";
              case 0xC906: return "Search for Next Statement/Line";
              case 0xC928: return "Perform [if]";
              case 0xC93B: return "Perform [rem]";
              case 0xC94B: return "Perform [on]";
              case 0xC96B: return "Fetch linnum From BASIC";
              case 0xC9A5: return "Perform [let]";
              case 0xC9C4: return "Assign Integer";
              case 0xC9D6: return "Assign Floating Point";
              case 0xC9D9: return "Assign String";
              case 0xC9E3: return "Assign TI$";
              case 0xCA2C: return "Add Digit to FAC#1";
              case 0xCA80: return "Perform [print]#";
              case 0xCA86: return "Perform [cmd]";
              case 0xC99A: return "Print String From Memory";
              case 0xCAA0: return "Perform [print]";
              case 0xCAB8: return "Output Variable";
              case 0xCAD7: return "Output CR/LF";
              case 0xCAE8: return "Handle comma, TAB(, SPC(";
              case 0xCB1E: return "Output String";
              case 0xCB3B: return "Output Format Character";
              case 0xCB4B: return "Handle Bad Data";
              case 0xCB7B: return "Perform [get]";
              case 0xCBA5: return "Perform [input#]";
              case 0xCBBF: return "Perform [input]";
              case 0xCBEA: return "Read Input Buffer";
              case 0xCBF9: return "Do Input Prompt";
              case 0xCC06: return "Perform [read]";
              case 0xCC35: return "General Purpose Read Routine";
              case 0xCCFC: return "Input Error Messages";
              case 0xCD1E: return "Perform [next]";
              case 0xCD61: return "Check Valid Loop";
              case 0xCD8A: return "Confirm Result";
              case 0xCD9E: return "Evaluate Expression in Text";
              case 0xCE8E: return "Evaluate Single Term";
              case 0xCEA8: return "Constant - pi";
              case 0xCEAD: return "Continue Expression";
              case 0xCEF1: return "Expression in Brackets";
              case 0xCEF7: return "Confirm Character ')'";
              case 0xCEFA: return "Confirm Character '('";
              case 0xCEFD: return "Confirm Character comma";
              case 0xCF08: return "Output ?SYNTAX Error";
              case 0xCF0D: return "Set up NOT Function";
              case 0xCF14: return "Identify Reserved Variable";
              case 0xCF28: return "Search for Variable";
              case 0xCF48: return "Convert TI to ASCII String";
              case 0xCFA7: return "Identify Function Type";
              case 0xCFB1: return "Evaluate String Function";
              case 0xCFD1: return "Evaluate Numeric Function";
              case 0xCFE6: return "Perform [or], [and]";
              case 0xD016: return "Perform <, =, >";
              case 0xD01B: return "Numeric Comparison";
              case 0xD02E: return "String Comparison";
              case 0xD07E: return "Perform [dim]";
              case 0xD08B: return "Identify Variable";
              case 0xD0E7: return "Locate Ordinary Variable";
              case 0xD11D: return "Create New Variable";
              case 0xD128: return "Create Variable";
              case 0xD194: return "Allocate Array Pointer Space";
              case 0xD1A5: return "Constant 32768 in Flpt";
              case 0xD1AA: return "FAC#1 to Integer in (AC/YR)";
              case 0xD1B2: return "Evaluate Text for Integer";
              case 0xD1B7: return "FAC#1 to Positive Integer";
              case 0xD1D1: return "Get Array Parameters";
              case 0xD218: return "Find Array";
              case 0xD245: return "'?bad subscript error'";
              case 0xD248: return "'?illegal quantity error'";
              case 0xD261: return "Create Array";
              case 0xD30E: return "Locate Element in Array";
              case 0xD34C: return "Number of Bytes in Subscript";
              case 0xD37D: return "Perform [fre]";
              case 0xD391: return "Convert Integer in (AC/YR) to Flpt";
              case 0xD39E: return "Perform [pos]";
              case 0xD3A6: return "Confirm Program Mode";
              case 0xD3E1: return "Check Syntax of FN";
              case 0xD3F4: return "Perform [fn]";
              case 0xD465: return "Perform [str$]";
              case 0xD487: return "Set Up String";
              case 0xD4D5: return "Save String Descriptor";
              case 0xD4F4: return "Allocate Space for String";
              case 0xD526: return "Garbage Collection";
              case 0xD5BD: return "Search for Next String";
              case 0xD606: return "Collect a String";
              case 0xD63D: return "Concatenate Two Strings";
              case 0xD67A: return "Store String in High RAM";
              case 0xD6A3: return "Perform String Housekeeping";
              case 0xD6DB: return "Clean Descriptor Stack";
              case 0xD6EC: return "Perform [chr$]";
              case 0xD700: return "Perform [left$]";
              case 0xD72C: return "Perform [right$]";
              case 0xD737: return "Perform [mid$]";
              case 0xD761: return "Pull sTring Parameters";
              case 0xD77C: return "Perform [len]";
              case 0xD782: return "Exit String Mode";
              case 0xD78B: return "Perform [asc]";
              case 0xD79B: return "Evaluate Text to 1 Byte in XR";
              case 0xD7AD: return "Perform [val]";
              case 0xD7B5: return "Convert ASCII String to Flpt";
              case 0xD7EB: return "Get parameters for POKE/WAIT";
              case 0xD7F7: return "Convert FAC#1 to Integer in LINNUM";
              case 0xD80D: return "Perform [peek]";
              case 0xD824: return "Perform [poke]";
              case 0xD82D: return "Perform [wait]";
              case 0xD849: return "Add 0.5 to FAC#1";
              case 0xD850: return "Perform Subtraction";
              case 0xD862: return "Normalise Addition";
              case 0xD867: return "Perform Addition";
              case 0xD947: return "2's Complement FAC#1";
              case 0xD97E: return "Output ?OVERFLOW Error";
              case 0xD983: return "Multiply by Zero Byte";
              case 0xD9BC: return "Table of Flpt Constants";
              case 0xD9EA: return "Perform [log]";
              case 0xDA28: return "Perform Multiply";
              case 0xDA59: return "Multiply by a Byte";
              case 0xDA8C: return "Load FAC#2 From Memory";
              case 0xDAB7: return "Test Both Accumulators";
              case 0xDAD4: return "Overflow/Underflow";
              case 0xDAE2: return "Multiply FAC#1 by 10";
              case 0xDAF9: return "Constant 10 in Flpt";
              case 0xDAFE: return "Divide FAC#1 by 10";
              case 0xDB07: return "Divide FAC#2 by Flpt at (AC/YR)";
              case 0xDB0F: return "Divide FAC#2 by FAC#1";
              case 0xDBA2: return "Load FAC#1 From Memory";
              case 0xDBC7: return "Store FAC#1 in Memory";
              case 0xDBC2: return "Copy FAC#2 into FAC#1";
              case 0xDC0C: return "Copy FAC#1 into FAC#2";
              case 0xDC1B: return "Round FAC#1";
              case 0xDC2B: return "Check Sign of FAC#1";
              case 0xDC39: return "Perform [sgn]";
              case 0xDC58: return "Perform [abs]";
              case 0xDC5B: return "Compare FAC#1 With Memory";
              case 0xDC9B: return "Convert FAC#1 to Integer";
              case 0xDCCC: return "Perform [int]";
              case 0xDCF3: return "Convert ASCII String to a Number in FAC#1";
              case 0xDDB3: return "String Conversion Constants";
              case 0xDDC2: return "Output 'IN' and Line Number";
              case 0xDDDD: return "Convert FAC#1 to ASCII String";
              case 0xDE68: return "Convert TI to String";
              case 0xDF11: return "Table of Constants";
              case 0xDF71: return "Perform [sqr]";
              case 0xDF7B: return "Perform power ($)";
              case 0xDFB4: return "Negate FAC#1";
              case 0xDFBF: return "Table of Constants";
              case 0xDFED: return "Perform [exp]";
              case 0xE040: return "Series Evaluation";
              case 0xE08A: return "Constants for RND";
              case 0xE094: return "Perform [rnd]";
              case 0xE0F6: return "Handle I/O Error in BASIC";
              case 0xE109: return "Output Character";
              case 0xE10F: return "Input Character";
              case 0xE115: return "Set Up For Output";
              case 0xE11B: return "Set Up For Input";
              case 0xE121: return "Get One Character";
              case 0xE127: return "Perform [sys]";
              case 0xE153: return "Perform [save]";
              case 0xE162: return "Perform [verify/load]";
              case 0xE1BB: return "Perform [open]";
              case 0xE1C4: return "Perform [close]";
              case 0xE1D1: return "Get Parameters For LOAD/SAVE";
              case 0xE1DB: return "Get Next One Byte Parameter";
              case 0xE203: return "Check Default Parameters";
              case 0xE20B: return "Check For Comma";
              case 0xE216: return "Get Parameters For OPEN/CLOSE";
              case 0xE261: return "Perform [cos]";
              case 0xE268: return "Perform [sin]";
              case 0xE2B1: return "Perform [tan]";
              case 0xE2DD: return "Table of Trig Constants: 1.570796327=pi/2";
              case 0xE2E2: return "Table of Trig Constants: 6.28318531=pi*2";
              case 0xE2E7: return "Table of Trig Constants: 0.25";
              case 0xE2EC: return "Table of Trig Constants: #05 (counter)";
              case 0xE2ED: return "Table of Trig Constants: -14.3813907";
              case 0xE2F2: return "Table of Trig Constants: 42.0077971";
              case 0xE2F7: return "Table of Trig Constants: -76.7041703";
              case 0xE2FC: return "Table of Trig Constants: 81.6052237";
              case 0xE301: return "Table of Trig Constants: -41.3417021";
              case 0xE306: return "Table of Trig Constants: 6.28318531";
              case 0xE30B: return "Perform [atn]";
              case 0xE33B: return "Table of ATN Constants: #0b	(counter)";
              case 0xE3EC: return "Table of ATN Constants: -0.000684793912";
              case 0xE341: return "Table of ATN Constants: 0.00485094216";
              case 0xE346: return "Table of ATN Constants: -0.161117018";
              case 0xE34B: return "Table of ATN Constants: 0.034209638";
              case 0xE350: return "Table of ATN Constants: -0.0542791328";
              case 0xE355: return "Table of ATN Constants: 0.0724571965";
              case 0xE35A: return "Table of ATN Constants: -0.0898023954";
              case 0xE35F: return "Table of ATN Constants: 0.110932413";
              case 0xE364: return "Table of ATN Constants: -0.142839808";
              case 0xE369: return "Table of ATN Constants: 0.19999912";
              case 0xE36E: return "Table of ATN Constants: -0.333333316";
              case 0xE373: return "Table of ATN Constants: 1.00";
              case 0xE378: return "BASIC Cold Start";
              case 0xE387: return "CHRGET For Zero-page";
              case 0xE39F: return "RND Seed For zero-page 0.811635157";
              case 0xE3A4: return "Initialize BASIC RAM";
              case 0xE404: return "Output Power-Up Message";
              case 0xE429: return "Power-Up Message: ' bytes free'";
              case 0xE436: return "Power-Up Message: '**** cbm basic v2 ****'";
              case 0xE44F: return "Table of BASIC Vectors (for 0300)";
              case 0xE45B: return "Initialize Vectors";
              case 0xE467: return "BASIC Warm Restart [RUNSTOP-RESTORE]";
              case 0xE47C: return "Unused Bytes For Future Patches";
              case 0xE4A0: return "Serial Output 1";
              case 0xE4A9: return "Serial Output 0";
              case 0xE4B2: return "Get Serial Data And Clock In";
              case 0xE4BC: return "Get Secondary Address patch for Serial LOAD/VERIFY";
              case 0xE4C1: return "Relocated Load patch for Serial LOAD/VERIFY";
              case 0xE4CF: return "Tape Write patch for CLOSE";
              case 0xE4DA: return "Unused";
              case 0xE500: return "Return I/O Base Address";
              case 0xE505: return "Return Screen Organization";
              case 0xE50A: return "Read/Set Cursor X/Y Position";
              case 0xE518: return "Initialize I/O";
              case 0xE55F: return "Clear Screen";
              case 0xE581: return "Home Cursor";
              case 0xE587: return "Set Screen Pointers";
              case 0xE5B5: return "Set I/O Defaults (Unused Entry)";
              case 0xE5BB: return "Set I/O Defaults";
              case 0xE5CF: return "Get Character From Keyboard Buffer";
              case 0xE5E5: return "Input From Keyboard";
              case 0xE64F: return "Input From Screen or Keyboard";
              case 0xE6B8: return "Quotes Test";
              case 0xE6C5: return "Set Up Screen Print";
              case 0xEAEA: return "Advance Cursor";
              case 0xE719: return "Retreat Cursor";
              case 0xE72D: return "Back on to Previous Line";
              case 0xE742: return "Output to Screen";
              case 0xE756: return "-unshifted characters-";
              case 0xE800: return "-shifted characters-";
              case 0xE8C3: return "Go to Next Line";
              case 0xE8D8: return "Output ";
              case 0xE8E8: return "Check Line Decrement";
              case 0xE8FA: return "Check Line Increment";
              case 0xE912: return "Set Colour Code";
              case 0xE921: return "Colour Code Table";
              case 0xE975: return "Scroll Screen";
              case 0xE9EE: return "Open A Space On The Screen";
              case 0xEA56: return "Move A Screen Line";
              case 0xEA6E: return "Syncronise Colour Transfer";
              case 0xEA7E: return "Set Start of Line";
              case 0xEA8D: return "Clear Screen Line";
              case 0xEAA1: return "Print To Screen";
              case 0xEAB2: return "Syncronise Colour Pointer";
              case 0xEABF: return "Main IRQ Entry Point";
              case 0xEB1E: return "Scan Keyboard";
              case 0xEB71: return "Process Key Image";
              case 0xEC46: return "Pointers to Keyboard decoding tables";
              case 0xEC5E: return "Keyboard Decoding Table - Unshifted";
              case 0xEC9F: return "Keyboard Decoding Table - Shifted";
              case 0xECE0: return "Keyboard Decoding Table - Commodore";
              case 0xED21: return "Graphics/Text Control";
              case 0xED69: return "Keyboard Decoding Table";
              case 0xEDA3: return "Keyboard Decoding Table - Control";
              case 0xEDE4: return "Video Chip Set Up Table";
              case 0xEDF4: return "Shift-Run Equivalent";
              case 0xEDFD: return "Low Byte Screen Line Addresses";
              case 0xEE14: return "Send TALK Command on Serial Bus";
              case 0xEE17: return "Send LISTEN Command on Serial Bus";
              case 0xEE49: return "Send Data On Serial Bus";
              case 0xEEB4: return "Flag Errors: #80 - device not present";
              case 0xEEB7: return "Flag Errors: #03 - write timeout";
              case 0xEEC0: return "Send LISTEN Secondary Address";
              case 0xEEC5: return "Clear ATN";
              case 0xEECE: return "Send TALK Secondary Address";
              case 0xEED3: return "Wait For Clock";
              case 0xEEE4: return "Send Serial Deferred";
              case 0xEEF6: return "Send UNTALK on Serial Bus";
              case 0xEF04: return "Send UNLISTEN on Serial Bus";
              case 0xEF19: return "Receive From Serial Bus";
              case 0xEE84: return "Serial Clock On";
              case 0xEF8D: return "Serial Clock Off";
              case 0xEF96: return "Delay 1 ms";
              case 0xEFA3: return "RS-232 Send";
              case 0xEFEE: return "Send New RS-232 Byte";
              case 0xF016: return "'No DSR' Error";
              case 0xF019: return "'No CTS' Error";
              case 0xF021: return "Disable Timer";
              case 0xF027: return "Compute Bit Count";
              case 0xF036: return "RS-232 Receive";
              case 0xF05B: return "Set Up To Receive";
              case 0xF068: return "Process RS-232 Byte";
              case 0xF0BC: return "Submit to RS-232";
              case 0xF0ED: return "Send to RS-232 Buffer";
              case 0xF116: return "Input From RS-232";
              case 0xF14F: return "Get From RS-232";
              case 0xF160: return "Serial Bus Idle";
              case 0xF174: return "Table of Kernal I/O Messages: ' i/o error #'";
              case 0xF1DF: return "Table of Kernal I/O Messages: 'ok'";
              case 0xF1E2: return "Print Message if Direct";
              case 0xF1E6: return "Print Message";
              case 0xF1F5: return "Get a byte";
              case 0xF20E: return "Input a byte";
              case 0xF250: return "Get From Tape / Serial / RS-232";
              case 0xF27A: return "Output One Character";
              case 0xF2C7: return "Set Input Device";
              case 0xF30E: return "Set Output Device";
              case 0xF34A: return "Close File";
              case 0xF3CF: return "Find File";
              case 0xF3DF: return "Set File values";
              case 0xF3EF: return "Abort All Files";
              case 0xF3F3: return "Restore Default I/O";
              case 0xF40A: return "Open File";
              case 0xF495: return "Send Secondary Address";
              case 0xF4C7: return "Open RS-232";
              case 0xF542: return "Load RAM From Device";
              case 0xF549: return "-load-";
              case 0xF55C: return "Load File From Serial Bus";
              case 0xF5CA: return "Load File From Tape";
              case 0xF647: return "Print \"SEARCHING\"";
              case 0xF659: return "Print Filename";
              case 0xF66A: return "Print \"LOADING/VERIFYING\"";
              case 0xF675: return "Save RAM To Device";
              case 0xF685: return "-save-";
              case 0xF692: return "Save to Serial Bus";
              case 0xF6F1: return "Save to Tape";
              case 0xF728: return "Print \"SAVING\"";
              case 0xF734: return "Increment Real-Time Clock";
              case 0xF760: return "Read Real-Time Clock";
              case 0xF767: return "Set Real-Time Clock";
              case 0xF770: return "Check STOP Key";
              case 0xF77E: return "Output I/O Error Messages: 'too many files'";
              case 0xF781: return "Output I/O Error Messages: 'file open'";
              case 0xF784: return "Output I/O Error Messages: 'file not open'";
              case 0xF787: return "Output I/O Error Messages: 'file not found'";
              case 0xF78A: return "Output I/O Error Messages: 'device not present'";
              case 0xF78D: return "Output I/O Error Messages: 'not input file'";
              case 0xF790: return "Output I/O Error Messages: 'not output file'";
              case 0xF793: return "Output I/O Error Messages: 'missing filename'";
              case 0xF796: return "Output I/O Error Messages: 'illegal device number'";              
              case 0xF7AF: return "Find Any Tape Header";
              case 0xF7E7: return "Write Tape Header";
              case 0xF84D: return "Get Buffer Address";
              case 0xF854: return "Set Buffer Stat/End Pointers";
              case 0xF867: return "Find Specific Tape Header";
              case 0xF88A: return "Bump Tape Pointer";
              case 0xF894: return "Print \"PRESS PLAY ON TAPE\"";
              case 0xF8AB: return "Check Tape Status";
              case 0xF8B7: return "Print \"PRESS RECORD...\"";
              case 0xF8C0: return "Initiate Tape Read";
              case 0xF8E3: return "Initiate Tape Write";
              case 0xF8F4: return "Common Tape Code";
              case 0xF94B: return "Check Tape Stop";
              case 0xF95D: return "Set Read Timing";
              case 0xF98E: return "Read Tape Bits";
              case 0xFAAD: return "Store Tape Characters";
              case 0xFBB2: return "Reset Tape Pointer";
              case 0xFBDB: return "New Character Setup";
              case 0xFBEA: return "Send Tone to Tape";
              case 0xFC06: return "Write Data to Tape";
              case 0xFC95: return "Write Tape Leader";
              case 0xFCCF: return "Restore Normal IRQ";
              case 0xFCF6: return "Set IRQ Vector";
              case 0xFD08: return "Kill Tape Motor";
              case 0xFD11: return "Check Read / Write Pointer";
              case 0xFD1B: return "Bump Read / Write Pointer";
              case 0xFD22: return "Power-Up RESET Entry";
              case 0xFD3F: return "Check For A-ROM";
              case 0xFD4D: return "ROM Mask 'a0CBM'";
              case 0xFD52: return "Restore Kernal Vectors (at 0314)";
              case 0xFD57: return "Change Vectors For User";
              case 0xFD6D:
              case 0xFD6E: return "Kernal Reset Vectors	";
              case 0xFD8D: return "Initialise System Constants";
              case 0xFDF1:
              case 0xFDF2: return "IRQ Vectors For Tape I/O";
              case 0xFDF9: return "Initialise I/O";
              case 0xFE39: return "Enable Timer";
              case 0xFE49: return "Set Filename";
              case 0xFE50: return "Set Logical File Parameters";
              case 0xFE57: return "Get I/O Status Word";
              case 0xFE66: return "Control OS Messages";
              case 0xFE6F: return "Set IEEE Timeout";
              case 0xFE73: return "Set / Read Top of Memory";
              case 0xFE82: return "Set / Read Bottom of Memory";
              case 0xFEA9: return "NMI Transfer Entry";
              case 0xFED2: return "Warm Start Basic [BRK]";
              case 0xFF56: return "Exit Interrupt";
              case 0xFF5B: return "RS-232 Timing Table";
              case 0xFF72: return "IRQ Transfer Entry";
              case 0xFF8A: return "Kernel Jump Table: Restore Vectors";
              case 0xFF8D: return "Kernel Jump Table: Change Vectors For User";
              case 0xFF90: return "Kernel Jump Table: Control OS Messages";
              case 0xFF93: return "Kernel Jump Table: Send SA After Listen";
              case 0xFF96: return "Kernel Jump Table: Send SA After Talk";
              case 0xFF99: return "Kernel Jump Table: Set/Read System RAM Top";
              case 0xFF9C: return "Kernel Jump Table: Set/Read System RAM Bottom";
              case 0xFF9F: return "Kernel Jump Table: Scan Keyboard";
              case 0xFFA2: return "Kernel Jump Table: Set Timeout In IEEE";
              case 0xFFA5: return "Kernel Jump Table: Handshake Serial Byte In";
              case 0xFFA8: return "Kernel Jump Table: Handshake Serial Byte Out";
              case 0xFFAB: return "Kernel Jump Table: Command Serial Bus UNTALK";
              case 0xFFAE: return "Kernel Jump Table: Command Serial Bus UNLISTEN";
              case 0xFFB1: return "Kernel Jump Table: Command Serial Bus LISTEN";
              case 0xFFB4: return "Kernel Jump Table: Command Serial Bus TALK";
              case 0xFFB7: return "Kernel Jump Table: Read I/O Status Word";
              case 0xFFBA: return "Kernel Jump Table: Set Logical File Parameters";
              case 0xFFBD: return "Kernel Jump Table: Set Filename";
              case 0xFFC0: return "Kernel Jump Table: Open Vector [F40A]";
              case 0xFFC3: return "Kernel Jump Table: Close Vector [F34A]";
              case 0xFFC6: return "Kernel Jump Table: Set Input [F2C7]";
              case 0xFFC9: return "Kernel Jump Table: Set Output [F309]";
              case 0xFFCC: return "Kernel Jump Table: Restore I/O Vector [F353]";
              case 0xFFCF: return "Kernel Jump Table: Input Vector, chrin [F20E]";
              case 0xFFD2: return "Kernel Jump Table: Output Vector, chrout [F27A]";
              case 0xFFD5: return "Kernel Jump Table: Load RAM From Device";
              case 0xFFD8: return "Kernel Jump Table: Save RAM To Device";
              case 0xFFDB: return "Kernel Jump Table: Set Real-Time Clock";
              case 0xFFDE: return "Kernel Jump Table: Read Real-Time Clock";
              case 0xFFE1: return "Kernel Jump Table: Test-Stop Vector [F770]";
              case 0xFFE4: return "Kernel Jump Table: Get From Keyboad [F1F5]";
              case 0xFFE7: return "Kernel Jump Table: Close All Channels And Files [F3EF]";
              case 0xFFEA: return "Kernel Jump Table: Increment Real-Time Clock";
              case 0xFFED: return "Kernel Jump Table: Return Screen Organization";
              case 0xFFF0: return "Kernel Jump Table: Read / Set Cursor X/Y Position";
              case 0xFFF3: return "Kernel Jump Table: Return I/O Base Address";
              case 0xFFFA: return "NMI";
              case 0xFFFC: return "RESET";
              case 0xFFFE: return "IRQ";
            default:
                if ((addr>=0x73) && (addr<=0x8A)) return "CHRGET subroutine (get BASIC char)";  
                if ((addr>=0xD9) && (addr<=0xF0)) return "Screen line link table"; 
                if ((addr>=0x100) && (addr<=0x10A)) return "Floating to ASCII work area";
                if ((addr>=0x10B) && (addr<=0x013E)) return "Tape error log";
                if ((addr>=0x100) && (addr<=0x1FF)) return "Processor stack area";
                if ((addr>=0x200) && (addr<=0x258)) return "Basic input buffer";
                if ((addr>=0x259) && (addr<=0x262)) return "Logical file table";
                if ((addr>=0x263) && (addr<=0x26C)) return "Device # table";
                if ((addr>=0x26D) && (addr<=0x276)) return "Secondary Address table";
                if ((addr>=0x277) && (addr<=0x280)) return "Keyboard buffer";
                if ((addr>=0x2A1) && (addr<=0x2FF)) return "Program indirects";
                if ((addr>=0x33C) && (addr<=0x03FB)) return "Cassette buffer";
                if ((addr>=0x400) && (addr<=0x0FFF)) return "3K expansion RAM area";
                if ((addr>=0x1000) && (addr<=0x11FF)) return "User Basic area/Screen memory";
                if ((addr>=0x1200) && (addr<=0x1DFF)) return "User Basic area";
                if ((addr>=0x1E00) && (addr<=0x1FFF)) return "Screen memory";
                if ((addr>=0x2000) && (addr<=0x3FFF)) return "8K expansion RAM/ROM block 1";
                if ((addr>=0x4000) && (addr<=0x5FFF)) return "8K expansion RAM/ROM block 2";
                if ((addr>=0x6000) && (addr<=0x7FFF)) return "8K expansion RAM/ROM block 3";
                if ((addr>=0x8000) && (addr<=0x83FF)) return "4K Character generator ROM/Upper case and graphics";
                if ((addr>=0x8400) && (addr<=0x87FF)) return "4K Character generator ROM/Reversed upper case and graphics";
                if ((addr>=0x8800) && (addr<=0x8BFF)) return "4K Character generator ROM/Upper and lower case";
                if ((addr>=0x8C00) && (addr<=0x8FFF)) return "4K Character generator ROM/Reversed upper and lower case";
                if ((addr>=0x9400) && (addr<=0x95FF)) return "location of COLOR RAM with additional RAM at blk 1";
                if ((addr>=0x9600) && (addr<=0x97FF)) return "Normal location of COLOR RAM";
                if ((addr>=0x9800) && (addr<=0x9BFF)) return "I/O block 2";
                if ((addr>=0x9C00) && (addr<=0x9FFF)) return "I/O block 3";
                if ((addr>=0xA000) && (addr<=0xBFFF)) return "8K decoded block for expansion ROM";
              }    
        }
  
    }
    return super.dcom(iType, aType, addr, value);
  }    
}
