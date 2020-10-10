/**
 * @(#)C64Dasm.java 2020/10/05
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
 * Comment the memory location of a 1541 drive for the disassembler
 * It performs also a multy language comments.
 *
 * @author Ice
 * @version 1.00 05/10/2020
 */
public class D1541Dasm extends M6510Dasm {
  // Available language
  public static final byte LANG_ENGLISH=1;
  public static final byte LANG_ITALIAN=2;

  /** Actual selected language (default=english) */
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
              case 0x00: return "Buffer #0 registro di stato e comando";
              case 0x01: return "Buffer #1 registro di stato e comando";
              case 0x02: return "Buffer #2 registro di stato e comando";
              case 0x03: return "Buffer #3 registro di stato e comando";
              case 0x04: return "Buffer #4 registro di stato e comando";
              case 0x05: return "Non usato (Registro di stato e commando del non esistente buffer #5)";
              case 0x06: 
              case 0x07: return "Buffer #0 registro di traccia e settore";
              case 0x08: 
              case 0x09: return "Buffer #1 registro di traccia e settore";
              case 0x0A: 
              case 0x0B: return "Buffer #2 registro di traccia e settore";
              case 0x0C: 
              case 0x0D: return "Buffer #3 registro di traccia e settore";
              case 0x0E: 
              case 0x0F: return "Buffer #4 registro di traccia e settore";
              case 0x10: 
              case 0x11: return "Non usato (Registro di traccia e settore del non esistente buffer #5";
              case 0x12: 
              case 0x13: return "Unità #0 settore ID intestazione atteso";
              case 0x14:
              case 0x15: return "Non usato (Settore ID intestazione atteso della non esistente unità #1)";
              case 0x16: 
              case 0x17: return "ID intestazione dall'ultimo settore letto da disco";
              case 0x18: 
              case 0x19: return "Numero di traccia e settore dall'intestazione dell'ultimo settore letto da disco";              	
              case 0x1A: return "Checksum dalla intestazione dell'ultimo settore letto da disco";
              case 0x1B: return "Non usato";
              case 0x1C: return "Unità #0 indicatore cambio disco";
              case 0x1D: return "Non usato (Indicatore cambio disco della non esistente unità #1)";
              case 0x1E: return "Stato precedente della fotocellula protezione scritttura dell'unità #0 (nel bit #4)";
              case 0x1F: return "Non usato (Stato precedente della fotocellula protezione scritttura della cellula non esistente dell'unità #1)";
              case 0x20: return "Unità #0 registro comandi interno del controller del disco";
              case 0x21: return "Non usato (registro comandi interno del controller del disco della non esistente unità #1)";
              case 0x22: return "Unità #0 numero corrente di traccia";
              case 0x23: return "Commutatore velocità di comunicazione bus seriale";
              case 0x24:
              case 0x25: 
              case 0x26:
              case 0x27:
              case 0x28:
              case 0x29:
              case 0x2A: 
              case 0x2B: return "Intestazione dell'ultima o successiva lettura del settore da scrivere su disco, nel formato codificato GCR";
              case 0x2C: return "Non usato";
              case 0x2D: return "Non usato";
              case 0x2E: 
              case 0x2F: return "Puntatore al byte corrente nel buffer durante codifica/decodifica GCR";
              case 0x30: 
              case 0x31: return "Puntatore all'inizio del buffer corrente";
              case 0x32: 
              case 0x33: return "Puntatore ai registri di settore e traccia del buffer corrente";
              case 0x34: return "Contatore GCR-byte durante codifica/decodifica GRC";
              case 0x35: return "Non usato";
              case 0x36: return "Contatore byte durante codifica/decodifica GCR";
              case 0x37: return "Non usato";
              case 0x38: return "Byte della firma del blocco dati dell'ultima lettura del settore dal disco";
              case 0x39: return "Valore atteso per la firma del blocco. Default: $08";
              case 0x3A: return "Somma di controllo calcolata dei dati nel buffer";
              case 0x3B: return "Non usato";
              case 0x3C: return "Non usato";
              case 0x3D: return "Numero di unità corrente del controller del disco";
              case 0x3E: return "Numero di unità precedente del controller del disco. Valori";
              case 0x3F: return "Numero di buffer corrente del controller del disco";
              case 0x40: return "Numero di traccia corrente";
              case 0x41: return "Numero di buffer da cercare";
              case 0x42: return "Numero di tracce da spostare durante la ricerca"; 
              case 0x43: return "Numero di settori sulla traccia corrente";
              case 0x44: return "Densita dati (nei bits #5-#6). Registro temporaneo per comandi nel buffer";
              case 0x45: return "Registro dei comandi del buffer del controller del disco";
              case 0x46: return "Non usato";
              case 0x47: return "Valore atteso del byte della firma dell'intestazione del blocco dati. Default: $07";
              case 0x48: return "Contatore del ritardo rotazione su/giù del motore";
              case 0x49: return "Valore originale del puntatore allo stack prima dell'esecuzione dell'interrupt del controller del disco";
              case 0x4A: return "Numero di mezze traccie da spostare durante la ricerca";
              case 0x4B: return "Riprova il contatore per la lettura dell'intestazione del settore. Area temporanea durante la ricerca";
              case 0x4C: return "Sulla traccia corrente, la distanza del settore, che corrisponde al settore di un buffer ed è più vicina all'ultima letta dal disco";
              case 0x4D: return "Sulla traccia corrente, il settore che si trova a due settori di distanza dall'ultimo letto dal disco";
              case 0x4E:
              case 0x4F: return "Puntatore all'inizio del buffer ausiliario durante la codifica GCR (byte scambiati)";
              case 0x50: return "Indicatore dei dati del buffer attualmente presenti in forma codificata GCR";
              case 0x51: return "Numero traccia corrente durante la formattazione";
              case 0x52:
              case 0x53:
              case 0x54:
              case 0x55: return "Area temporanea per byte dati durante la codifica/decodifica GRC";
              case 0x56: 
              case 0x57:     
              case 0x58:
              case 0x59:      
              case 0x5A:      
              case 0x5B:      
              case 0x5C:
              case 0x5D: return "Area temporanea per nybbles e byte GCR durante la codifica/decosifica GCR";
              case 0x5E: return "Numero di mezze traccie per accelerare/decelerare durante la ricerca accelerata";
              case 0x5F: return "Fattore di accellerazione/decelerazione durante la ricerca acellerata";
              case 0x60: return "Contatore del ritardo dopo la ricerca (in modo che la testa smetta di vibrare), contatore a metà traccia per l'accelerazione/decelerazione durante la ricerca accelerata";   
              case 0x61: return "Contatore metà traccia per la massima velocità durante la ricerca accelerata";
              case 0x62:
              case 0x63: return "Puntatore alla routine successiva da eseguire durante la ricerca"; 
              case 0x64: return "Limite di distanza inferiore della ricerca accelerata, in mezze traccie. Default: $C8";
              case 0x65:  
              case 0x66: return "Rutine puntatore per partenza a freddo (comando \"UI\"). Default: $EB22.";
              case 0x67: return "Sconosciuto";
              case 0x68: return "Interruttore di inizializzazione automatica del disco. Default $00";
              case 0x69: return "Soft interleave (distanza, in settori, per allocare il settore successivo per i file). Default: $0A";
              case 0x6A: return "Numero di tentativi sui comandi del disco. Default: $05";
              case 0x6B:
              case 0x6C: return "Puntatore alla tabella comandi per \"Ux\". Default: $FFEA";
              case 0x6D: 
              case 0x6E: return "Puntatore temporaneo per operazioni BAM";
              case 0x6F: 
              case 0x70: return "Puntatore temporaneo per varie operazioni";
              case 0x71: 
              case 0x72: 
              case 0x73: 
              case 0x74: return "Sconosciuto";
              case 0x75:
              case 0x76: return "Puntatore al byte corrente durante il test della memoria all'avvio. Indirizzo di esecuzione del comando corrente Ux";
              case 0x77: return "Indicatore comando da accettaer LISTEN  nel bus seriale (Numero dispositivo o $20)";
              case 0x78: return "Indicatore comando da accettare TALK nel bus seriale (Numero dispositivo o $40)";
              case 0x79: return "Indicatore comando LISTEN nel bus seriale";
              case 0x7A: return "Indicatore comando TALK nel bus seriale";
              case 0x7B: return "Sconosciuto";
              case 0x7C: return "Indicatore arrivo ATN nel bus seriale";
              case 0x7D: return "Indicatore comando End";
              case 0x7E: return "Numero traccia del precedente file aperto (Usato quando di apre \"*\")";
              case 0x7F: return "Unità corrente. Default: $00";
              case 0x80:
              case 0x81: return "Traccia e numero settori per varie operazioni";
              case 0x82: return "Numero canale corrente";
              case 0x83: return "Indirizzo secondario corrente (solo bit #0-#3)";
              case 0x84: return "Indirizzo secondario corrente";
              case 0x85: return "Byte letti dal serial bus. Dati letti dal buffer o scritti nel buffer";
              case 0x86:
              case 0x87: return "Puntatore al byte corrente nel buffer della directory";
              case 0x88:
              case 0x89: return "Puntatore al byte e indirizzo esecuzione del comando utente \"&\"";
              case 0x8A: return "Sconosciuto";
              case 0x8B: 
              case 0x8C:
              case 0x8D: return "Area temporane per la divisione intera. (Utilizzato per calcolare il settore laterale dei file relativi)";
              case 0x8E: 
              case 0x8F: 
              case 0x90: 
              case 0x91: 
              case 0x92: 
              case 0x93: return "Sconosciuto";
              case 0x94: 
              case 0x95: return "Puntatore alla voce della rubrica corrente";
              case 0x96: 
              case 0x97: return "Non usato";
              case 0x98: return "Contatore di bit durante l'ingresso/uscita del bus seriale";
              case 0x99: 
              case 0x9A: return "Puntatore al buffer #0. Default: $0300";
              case 0x9B: 
              case 0x9C: return "Puntatore al buffer #1. Default: $0400";
              case 0x9D: 
              case 0x9E: return "Puntatore al buffer #2. Default: $0500";
              case 0x9F: 
              case 0xA0: return "Puntatore al buffer #3. Default: $0600";
              case 0xA1:
              case 0xA2: return "Puntatore al buffer #4. Default: $0700";
              case 0xA3: 
              case 0xA4: return "Puntatore al buffer di input. Default: $0200";
              case 0xA5: 
              case 0xA6: return "Puntatore al buffer di messaggio di errore. Default: $02D5";
              case 0xA7: 
              case 0xA8: 
              case 0xA9: 
              case 0xAA: 
              case 0xAB: 
              case 0xAC:
              case 0xAD: return "Numero di buffer primario assegnato ai canali";
              case 0xAE:
              case 0xAF: 
              case 0xB0:
              case 0xB1: 
              case 0xB2:
              case 0xB3: 
              case 0xB4: return "Numero di buffer secondario assegnato ai canali";
              case 0xB5: 
              case 0xB6: 
              case 0xB7: 
              case 0xB8:
              case 0xB9:
              case 0xBA: return "Lunghezza del file assegnato al canale, low byte. Per file relativi, numero di records, low byte";
              case 0xBB:
              case 0xBC: 
              case 0xBD: 
              case 0xBE: 
              case 0xBF: 
              case 0xC0: return "Lunghezza del file assegnato al canale, high byte. Per file relativi, numero di records, high byte";
              case 0xC1:
              case 0xC2: 
              case 0xC3:
              case 0xC4: 
              case 0xC5: 
              case 0xC6: return "Offset del byte corrente nel buffer assegnato ai canali";
              case 0xC7:
              case 0xC8:
              case 0xC9:
              case 0xCA:
              case 0xCB:
              case 0xCC: return "lunghezza disco del file relativo assegnato ai canali";
              case 0xCD: 
              case 0xCE: 
              case 0xCF: 
              case 0xD0: 
              case 0xD1:
              case 0xD2: return "Numero di buffer contenente il settore laterale del file relativo assegnato ai canali";
              case 0xD3: return "Contatore virgola durante il recupero dei numeri di unità dal comando";
              case 0xD4: return "Offset del byte corrente nel record del file relativo";
              case 0xD5: return "Numero del settore laterale appartenente al record del file relativo corrente";
              case 0xD6: return "Offset del numero di traccia e del settore del record di file relativo corrente nel settore laterale";
              case 0xD7: return "Offset del record nel settore dei dati del file relativo";
              case 0xD8: 
              case 0xD9:
              case 0xDA:
              case 0xDB:
              case 0xDC: return "Numero di settore della voce di directory dei file"; 
              case 0xDD:
              case 0xDE:
              case 0xDF:    
              case 0xE0: 
              case 0xE1: return "Offset della voce della directory del file";   
              case 0xE2:
              case 0xE3:
              case 0xE4:
              case 0xE5:
              case 0xE6: return "Numero di unità di file";
              case 0xE7:
              case 0xE8:
              case 0xE9:    
              case 0xEA:    
              case 0xEB: return "Tipo di file e contrassegni dei file";
              case 0xEC:
              case 0xED:
              case 0xEE:    
              case 0xEF:    
              case 0xF0:
              case 0xF1: return "Numero di unità, tipo di file e flag dei file assegnati ai canali";
              case 0xF2: 
              case 0xF3:
              case 0xF4: 
              case 0xF5:
              case 0xF6: 
              case 0xF7: return "Flag Input/output dei canali";
              case 0xF8: return "Indicatore fine file del canale corrente";
              case 0xF9: return "Numero buffer corrente";
              case 0xFA: 
              case 0xFB:
              case 0xFE: return "Sconosciuto";
              case 0xFF: return "Unità #0 indicatore errore input/output BAM";
              
              case 0x100: return "Indicatore errore input/output BAM della non esistente unità #1";
              case 0x101: return "Unità #0 codice versione BAM (Byte al offset #$02 nel sector 18;00) Atteso: $41,A";
              case 0x102: return "Codice versione BAM della non esistente unità #1";
              
              case 0x22A: return "Numero comando DOS";
              case 0x23E: 
              case 0x23F:
              case 0x240: 
              case 0x241: 
              case 0x242: return "Area temporanea per il prossimo byte che deve essere scritto nei buffer #0-#4 del serial bus";
              case 0x243: return "Area temporanea per il prossimo byte che deve essere scritto dal buffer messaggi errore al serial bus";
              case 0x244: 
              case 0x245: 
              case 0x246:
              case 0x247: 
              case 0x248: return "Offset dell'ultimo byte di dati nei buffer #0-#4";
              case 0x249: return "Offset dell'ultimo byte di dati nel buffer dei messaggi di errore";
              case 0x24A: return "Tipo di file del file corrente";
              case 0x24B: return "Lunghezza del nome del file corrente";
              case 0x24C: return "Area temporanea per indirizzo secondario";
              case 0x24D: return "Area temporanea per il comando del controller del disco";
              case 0x24E: return "Numero di settori sulla traccia corrente";
              case 0x24F:
              case 0x250: return "Registro di allocazione del buffer. Default: $FFE0";
              case 0x251: return "Unità #0 indicatore di cambio BAM";
              case 0x252: return "Indicatore di cambio BAM della unità non esistente unit #1";
              case 0x253: return "Indicatore di file trovato durante la ricerca di un nome file nella directory";
              case 0x254: return "Indicatore di directory dei canali LOAD";
              case 0x255: return "Indicatore di fine comando";
              case 0x256: return "Registro di assegnazione dei canali";
              case 0x257: return "Area temporanea per numero di canale";
              case 0x258: return "Registra la lunghezza del file relativo corrente";
              case 0x259:
              case 0x25A: return "Traccia e numero di settore del primo settore laterale del file relativo corrente";
              case 0x25B:
              case 0x25C:
              case 0x25D:
              case 0x25E:    
              case 0x25F: return "Comandi originali del controller del disco dei buffer #0-#4";
              case 0x260:    
              case 0x261:
              case 0x262:    
              case 0x263:    
              case 0x264:        
              case 0x265: return "Numero di settore della voce di directory dei file specificati nel comando";
              case 0x266:
              case 0x267:    
              case 0x268:    
              case 0x269:    
              case 0x26A:                      
              case 0x26B: return "Offset della voce di directory dei file specificati nel comando";
              case 0x26C: return "Interruttore per la visualizzazione dei messaggi di avviso dei file relativi";
              case 0x26D: return "Unità #0 bit LED";
              case 0x26E: return "Numero di unità del file aperto in precedenza (utilizzato durante l'apertura \"*\")";
              case 0x26F: return "Numero di settore del file aperto in precedenza (utilizzato durante l'apertura \"*\")";
              case 0x270: return "Area temporanea per numero di canale";
              case 0x271: return "Non usato";
              case 0x272: 
              case 0x273: return "Numero di riga BASIC per le voci inviate all'host durante il caricamento di \"$\"";
              case 0x274: return "Lunghezza del comando";
              case 0x275: return "Primo carattere di comando. Carattere da cercare nel buffer di input"; 
              case 0x276: return "Offset del primo carattere dopo il nome del file nel comando";
              case 0x277: return "Area temporanea per numero di virgole in comando";
              case 0x278: return "Numero di virgole o numeri di unità in comando";
              case 0x279: return "Numero di virgole prima del segno di equazione nel comando. Numero del file corrente nel comando durante la ricerca dei file specificati nel comando";
              case 0x27A: return "Scostamento del carattere prima dei due punti in comando";
              case 0x27B: 
              case 0x27C:
              case 0x27D:    
              case 0x27E:    
              case 0x27F: return "Offset dei nomi di file nel comando";
              case 0x280:
              case 0x281:
              case 0x282:
              case 0x283:
              case 0x284: return "Traccia il numero di file specificato nel comando";   
              case 0x285:
              case 0x286:    
              case 0x287:     
              case 0x288:     
              case 0x289: return "Numero di settore di file specificato nel comando. Per i comandi B-x, byte inferiore dei parametri";
              case 0x28A: return "Numero di caratteri jolly trovati nel nome file corrente";
              case 0x28B: return "Flag di sintassi dei comandi";
              case 0x28C: return "Numero di unità da elaborare durante la lettura della directory";
              case 0x28D: return "Numero di unità corrente durante la lettura della directory";
              case 0x28E: return "Numero di unità precedente durante la lettura della directory";
              case 0x28F: return "Indicatore per continuare a cercare nella directory";
              case 0x290: return "Numero di settore della directory corrente";
              case 0x291: return "Numero di settore della directory da leggere";
              case 0x292: return "Offset della voce della directory corrente nel settore della directory";
              case 0x293: return "Indicatore di fine directory";
              case 0x294: return "Offset della voce della directory corrente nel settore della directory";
              case 0x295: return "Numero di voci di directory rimanenti nel settore della directory meno 1";
              case 0x296: return "Tipo di file del file cercato nella directory";
              case 0x297: return "Modalità di apertura dei file";
              case 0x298: return "Interruttore di visualizzazione dei messaggi per gli errori del disco";
              case 0x299: return "Offset del byte corrente nella tabella di ricerca halftrack durante il tentativo di ripetere le operazioni del disco su mezze traccie adiacenti";
              case 0x29A: return "Direzione di ricerca del semitraccia originale durante il tentativo di eseguire nuovamente le operazioni del disco su mezze traccie adiacenti";
              case 0x29B: return "Unit #0 numero di traccia della voce BAM corrente";
              case 0x29C: return "Numero di traccia della voce BAM corrente della unità non esistente #1";
              case 0x29D:
              case 0x29E: return "Unit #0 numeri traccia di due voci BAM memorizzate nella cache";
              case 0x29F:
              case 0x2A0: return "Numeri traccia di due voci BAM memorizzate nella cache della unità non esistente #1";
              case 0x2A1:
              case 0x2A2:
              case 0x2A3:
              case 0x2A4:
              case 0x2A5:
              case 0x2A6:    
              case 0x2A7:
              case 0x2A8: return "Unità #0 due voci BAM memorizzate nella cache";
              case 0x2A9:
              case 0x2AA:
              case 0x2AB:    
              case 0x2AC:    
              case 0x2AD:
              case 0x2AE:
              case 0x2AF:    
              case 0x2B0: return "Due voci BAM memorizzate nella cache della unità non esistente #1";
              case 0x2F9: return "Aggiornamento del disco al cambio di BAM. Diverse operazioni relative a BAM prevedono valori diversi qui";
              case 0x2FA: return "Unità #0 numero di blocchi liberi, byte basso";
              case 0x2FB: return "Numero di blocchi liberi, byte basso, della unità non esistente #1";
              case 0x2FC: return "Unità #0 numero di blocchi liberi, byte alto";
              case 0x2FD: return "Numero di blocchi liberi, byte alto, della unità non esistente #1";
              case 0x2FE: return "Unità #0 Direzione di ricerca mezze traccie adiacente";
              case 0x2FF: return "Direzione di ricerca mezze traccie adiacente di unità non esistente #1";
              
              case 0x1800: return "Port B #1 bus seriale";
              case 0x1801: return "Port A #1 leggere per riconoscere l'interrupt generato da ATN IN che diventa alto";
              case 0x1802: return "Port B #1 registro direzioni dati. Default: $1A, %00011010";
              case 0x1803: return "Port A #1 registro direzioni dati. Default: $FF, %11111111.";
              case 0x1804: 
              case 0x1805: return "Timer #1 byte basso lettura o byte alto scrittura per avviare il timer o riavviare il timer in caso di underflow";
              case 0x1806: 
              case 0x1807: return "Timer latch #1. Valore iniziale lettura/scrittura del timer da/a qui";
              case 0x1808:
              case 0x1809:
              case 0x180A: return "Non usato";
              case 0x180B: return "Registro controllo timer #1";
              case 0x180C: return "Non usato";
              case 0x180D: return "Registro stato interruzione #1";
              case 0x180E: return "Registro controllo interruzione #1";
              case 0x180F: return "Non usato";
              
              case 0x1C00: return "Port B #2";
              case 0x1C01: return "Port A #2 ultimo byte di dati letto da o da scrivere successivamente su disco";
              case 0x1C02: return "Port B #2 registro direzione dati";
              case 0x1C03: return "Port A #2 registro direzioni dati";
              case 0x1C04: 
              case 0x1C05: return "Timer #2. Byte basso lettura o byte alto scrittura per avviare il timer o riavviare il timer in caso di underflow";
              case 0x1C06: 
              case 0x1C07: return "Timer latch #2. Valore iniziale lettura/scrittura del timer da/a qui";
              case 0x1C08:
              case 0x1C09:
              case 0x1C0A: return "Non usato";
              case 0x1C0B: return "Registro controllo timer #2";
              case 0x1C0C: return "Registro controllo ausiliario #2";
              case 0x1C0D: return "Registro stato interruzione #2";
              case 0x1C0E: return "Registro controllo interruzione #2";
              case 0x1C0F: return "Non usato";                
              
              case 0xC100: return "Accende il LED per il drive corrente";
              case 0xC118: return "Accende il LED";
              case 0xC123: return "Pulisce stati di errore";
              case 0xC12C: return "Prepara per il lampeggio  del LED dopo un errore";
              case 0xC146: return "Interpreta un comando dal computer";
              case 0xC194: return "Prepara un messaggio di errore dopo l'esecuzione di un comando";              
              case 0xC1BD: return "Cancella il buffer di ingresso";
              case 0xC1C8: return "Messaggio di errore in uscita (traccia e settore 0)";
              case 0xC1D1: return "Verifica la linea di ingresso";
              case 0xC1E5: return "Verifica ':' sulla linea di ingresso";
              case 0xC1EE: return "Verifica linea di ingresso";
              case 0xC268: return "Cerca un carattere nel buffer in ingresso";
              case 0xC2B3: return "Verifica lunghezza linea";
              case 0xC2DC: return "Pulisce gli stati per comando in ingresso";
              case 0xC312: return "Preserva il numero dispositivo";
              case 0xC33C: return "Ricerca per il numero dispositivo";
              case 0xC368: return "Ottiene il numero dispositivo";
              case 0xC38F: return "Inverti il numero dispositivo";
              case 0xC398: return "Verifica il tipo file dato";
              case 0xC3BD: return "Verifica il numero dispositivo dato";
              case 0xC3CA: return "Verifica il numero dispositivo";
              case 0xC440: return "Impostazioni per verifica dispositivo";
              case 0xC44f: return "Ricerca per file nella directory";
              case 0xC63D: return "Testa e inizializza il dispositivo";
              case 0xC66E: return "Nome del file nel buffer directory";
              case 0xC688: return "Copia il nome file nel buffer di lavoro";
              case 0xC6A6: return "Cerca la fine del nome nel comando";
              case 0xC7AC: return "Pulisce il buffer di uscita della directory";
              case 0xC7B7: return "Crea intestazione con nome del file";
              case 0xC806: return "Stampa 'blocks free.'";
              case 0xC817: return "'Blocks free.'";
              case 0xC823: return "Esegue [S] - Scratch command";
              case 0xC87D: return "Cancella file";
              case 0xC8B6: return "Cancella entrata directory";
              case 0xC8C1: return "Esegue [D] - Backup command (Non usato)";
              case 0xC8C6: return "Fomatta il disco";
              case 0xC8F0: return "Esegua [C] - Copy command";
              case 0xCA88: return "Esegue [R] - Rename command";
              case 0xCACC: return "Verifica se il file è presente";
              case 0xCAF8: return "Esegue [M] - Memory command";
              case 0xCB20: return "M-R memoria letta";
              case 0xCB50: return "M-W memoria scritta";
              case 0xCB5C: return "Esegue [U] - User command";
              case 0xCB84: return "Apre canale di accesso diretto, numero";
              case 0xCC1B: return "Esegue [B] - Block/Buffer command";
              case 0xCC5D: return "Comando di blocco \"AFRWEP\"";
              case 0xCC63: return "Vettore comando di blocco";
              case 0xCC6F: return "Ottieni parametri dal comando di blocco";
              case 0xCCF2: return "Valori decimali  1, 10, 100";
              case 0xCCF5: return "B-F blocchi liberi";
              case 0xCD03: return "B-A allocazione blocco";
              case 0xCD36: return "Legge il blocco nel buffer";
              case 0xCD3c: return "Ottiene il byte dal buffer";
              case 0xCD42: return "Legge il blocco da disco";
              case 0xCD56: return "B-R blocco letto";
              case 0xCD5F: return "U1, blocco letto senza cambiare puntatore al buffer";
              case 0xCD73: return "B-W blocco scritto";
              case 0xCD97: return "U2, blocco scritto senza cambiare puntatore al buffer";
              case 0xCDA3: return "B-E blocco eseguito";
              case 0xCDBD: return "B-P puntatore blocco";
              case 0xCDD2: return "Canale aperto";
              case 0xCDF2: return "Verifica il numero buffer e il canale aperto";
              case 0xCE0E: return "Setta il puntatore per file REL";
              case 0xCE6E: return "Divide per 254";
              case 0xCE71: return "Divide per 120";
              case 0xCED9: return "Cancella spazio di archiviazione";
              case 0xCEE2: return "Scorre a sinistra registro di 3-byte due volte";
              case 0xCEE5: return "Scorre a sinistra registro di 3-byte una volta";
              case 0xCEED: return "Somma registri di 3-byte";
              case 0xCF8C: return "Cambio buffer";
              case 0xCF9B: return "Scrive i dati nel buffer";
              case 0xCFF1: return "Scrive il dato nel buffer";
              case 0xD005: return "Esegue [I] - Initalise command";
              case 0xD00e: return "Legge BAM da disco";
              case 0xD042: return "Legge BAM";
              case 0xD075: return "Calcola i blocchi liberi";
              case 0xD0C3: return "Legge i blocchi";
              case 0xD0C7: return "Scrive i blocchi";
              case 0xD0EB: return "Apre i canali per leggere";
              case 0xD107: return "Apre i canali per scrivere";
              case 0xD125: return "Verifica per tipo file REL";
              case 0xD12f: return "Ottiene numeri canale e buffer";
              case 0xD137: return "Ottiene un bute dal buffer";
              case 0xD156: return "Ottiene un byte e legge il prossimo blocco";
              case 0xD19D: return "Scrive byte nel buffer e blocco";
              case 0xD1C6: return "Incrementa il puntatore nel buffer";
              case 0xD1D3: return "Ottiene in numero dispositivo";
              case 0xD1DF: return "Cerca il canale e buffer di scrittura";
              case 0xD1E2: return "Cerca il canale e buffer di scrittura";
              case 0xD227: return "Chiude il canale";
              case 0xD25A: return "Buffer libero";
              case 0xD28E: return "Cerca buffer";
              case 0xD307: return "Chiude tutti i canali";
              case 0xD313: return "Chiude tutti i canali e gli altri dispositivi";
              case 0xD37F: return "Cerca canale e alloca";
              case 0xD39B: return "Ottiene il byte per uscita";
              case 0xD44D: return "Legge prossimo blocco";
              case 0xD460: return "Legge blocco";
              case 0xD464: return "Scrive blocco";
              case 0xD475: return "Alloca il budffee e legge il blocco";
              case 0xD486: return "Alloca un nuovo blocco";
              case 0xD48D: return "Scrive un blocco directory";
              case 0xD4C8: return "Setta il puntatore al buffer";
              case 0xD4DA: return "Chiude il canale interno";
              case 0xD4E8: return "Setta il puntatore al buffer";
              case 0xD4F6: return "Ottiene un byte dal buffer";
              case 0xD506: return "Verifica numeri traccia e settore";
              case 0xD552: return "Ottiene traccia e settori per il lavoro corrente";
              case 0xD55F: return "Verifica per validi numeri settore e traccia";
              case 0xD572: return "Errore DOS mismatch";
              case 0xD586: return "Legge blocco";
              case 0xD58A: return "Scrive blocco";
              case 0xD599: return "Verifica esecuzione";
              case 0xD5C6: return "Altri tentativi aggiuntivi per lettura con errori";
              case 0xD676: return "Muove la testa di mezza traccia";
              case 0xD693: return "Muove la testa di una traccia dentro e fuori";
              case 0xD6A6: return "Tenta l'esecuzione comando molteplici volte";
              case 0xD6D0: return "Trasmetti i parametri al controller del disco";
              case 0xD6E4: return "Inserisci il file nella directory";
              case 0xD7B4: return "Comando OPEN, indirizzo secondario 15";
              case 0xD7C7: return "-Check '*' Last file";
              case 0xD7F3: return "-Check '$' Directory";
              case 0xD815: return "-Check '#' Channel";
              case 0xD8F5: return "Apre il file con sovrascrittura (@)";
              case 0xD9A0: return "Apre il file per la lettura";
              case 0xD9E3: return "Apre il file per la scrittura";
              case 0xDA09: return "Verifica il tipo file e modello controllo";
              case 0xDA2A: return "Preparazione per accodamento";
              case 0xDA55: return "Apre la directory";
              case 0xDAC0: return "Routine Chiusura";
              case 0xDB02: return "Chiusura file";
              case 0xDB62: return "Scrive l'ultimo blocco";
              case 0xDBA5: return "Voce directory";
              case 0xDC46: return "Legge blocco, alloca buffer";
              case 0xDCB6: return "Puntatore Reset";
              case 0xDCDA: return "Costruisce un nuovo blocco";
              case 0xDD8D: return "Scittura byte nel blocco del settore laterale";
              case 0xDD95: return "Manipola le impostazioni";
              case 0xDDAB: return "Codice comando verifica per scrittura";
              case 0xDDF1: return "Scrive un blocco di un file REL";
              case 0xDDFD: return "Scrivi byte della prossima traccia";
              case 0xDE0C: return "Ottieni numeri di traccia e settore seguenti";
              case 0xDE19: return "Segui la traccia per l'ultimo blocco";
              case 0xDE2B: return "Puntatore del buffer a zero";
              case 0xDE3B: return "Ottiene traccia e settore";
              case 0xDE50: return "Scrive (?)";
              case 0xDE57: return "R";
              case 0xDE5E: return "W";
              case 0xDE65: return "R";
              case 0xDE73: return "R";
              case 0xDE95: return "Ottieni la traccia e il settore seguenti dal buffer";
              case 0xDEA5: return "Copia il contenuto del buffer";
              case 0xDEC1: return "Cancella buffer Y";
              case 0xDED2: return "Ottieni il numero del settore laterale";
              case 0xDEDC: return "Imposta il puntatore del buffer sul settore laterale";
              case 0xDEE9: return "Puntatore buffer per settore laterale";
              case 0xDEF8: return "Ottieni il settore laterale e il puntatore del buffer";
              case 0xDF1B: return "Leggi il settore laterale";
              case 0xDF21: return "Scrivi settore laterale";
              case 0xDF45: return "Imposta il puntatore del buffer nel settore laterale";
              case 0xDF4C: return "Calcola il numero di blocchi in un file REL";
              case 0xDF66: return "Verifica il settore laterale nel buffer";
              case 0xDF93: return "Ottieni numero di buffer";
              case 0xDFD0: return "Ottieni il record successivo nel file REL";
              case 0xE03C: return "Scrivi blocco e leggi il blocco successivo";
              case 0xE07C: return "Scrivi un byte in un record";
              case 0xE0AB: return "Scrivi byte nel file REL";
              case 0xE0F3: return "Riempi il record con 0";
              case 0xE105: return "Scrivi il numero del buffer nella tabella";
              case 0xE120: return "Ottieni byte dal file REL";
              case 0xE1CB: return "Ottieni l'ultimo settore laterale";
              case 0xE207: return "Eseguire [P] - Comando di posizione";
              case 0xE2E2: return "Dividi i blocchi di dati in record";
              case 0xE304: return "Imposta il puntatore al record successivo";
              case 0xE31C: return "Espandi il settore laterale";
              case 0xE44E: return "Scrivi settore laterale e assegna nuovo";
              case 0xE4fC: return "Tabella dei messaggi di errore: 00, Ok";                            
              case 0xE500: return "Tabella dei messaggi di errore: 20,21,22,23,24,27, Errore lettura";
              case 0xE50B: return "Tabella dei messaggi di errore: 52,	File troppo grande";
              case 0xE517: return "Tabella dei messaggi di errore: 50,	Record non presente";
              case 0xE522: return "Tabella dei messaggi di errore: 51,	Overflow in record";
              case 0xE52F: return "Tabella dei messaggi di errore: 25,28, Errore scrittura";
              case 0xE533: return "Tabella dei messaggi di errore: 26,	Protezione scrittura attiva";
              case 0xE540: return "Tabella dei messaggi di errore: 29,	Mancata corrispondenza dell'ID disco";
              case 0xE546: return "Tabella dei messaggi di errore: 30,31,32,33,34, Errore di sintassi";
              case 0xE552: return "Tabella dei messaggi di errore: 60,	Scrivi file aperto";
              case 0xE556: return "Tabella dei messaggi di errore: 63,	File esiste";
              case 0xE55F: return "Tabella dei messaggi di errore: 64,	Tipo di file non corrispondente";
              case 0xE567: return "Tabella dei messaggi di errore: 65,	No blocco";
              case 0xE570: return "Tabella dei messaggi di errore: 66,67, Traccia o settore illegale";
              case 0xE589: return "Tabella dei messaggi di errore: 61,	File non aperto";
              case 0xE58D: return "Tabella dei messaggi di errore: 39,62, File non trovato";
              case 0xE592: return "Tabella dei messaggi di errore: 01,	File ricreati";
              case 0xE59F: return "Tabella dei messaggi di errore: 70,	Nessun canale";
              case 0xE5AA: return "Tabella dei messaggi di errore: 71,	Errore disco";
              case 0xE5AF: return "Tabella dei messaggi di errore: 72,	Disco pieno";
              case 0xE5B6: return "Tabella dei messaggi di errore: 73,	Cbm dos v2.6 1541";
              case 0xE5C8: return "Tabella dei messaggi di errore: 74,	Lettore non pronto";
              case 0xE5D5: return "Parole indicizzate: 09 Errore";
              case 0xE5DB: return "Parole indicizzate: 0A Scrittura";
              case 0xE5E1: return "Parole indicizzate: 03 File";
              case 0xE5E6: return "Parole indicizzate: 04 Aperto";
              case 0xE5EB: return "Parole indicizzate: 05 Non corrispondente";
              case 0xE5F4: return "Parole indicizzate: 06 Nont";
              case 0xE5F8: return "Parole indicizzate: 07 Trovato";
              case 0xE5FE: return "Parole indicizzate: 08 Disco";
              case 0xE603: return "Parole indicizzate: 0B Record";
              case 0xE60A: return "Prepara il numero e il messaggio di errore";
              case 0xE645: return "Stampa il messaggio di errore nel buffer degli errori";
              case 0xE680: return "TALK";
              case 0xE688: return "LISTEN";
              case 0xE69B: return "Converte BIN in 2-Ascii (buffer dei messaggi di errore)";
              case 0xE6AB: return "Converte BCD in 2-Ascii (buffer dei messaggi di errore)";
              case 0xE6BC: return "Scrive OK nel buffer";
              case 0xE6C1: return "Stampa l'errore sulla traccia 00,00 nel buffer degli errori";
              case 0xE6C7: return "Stampa l'errore sulla traccia corrente nel buffer degli errori";
              case 0xE706: return "Scrive la stringa del messaggio di errore nel buffer";
              case 0xE754: return "Ottieni carattere e buffer";
              case 0xE767: return "Ottieni un carattere del messaggio di errore";
              case 0xE775: return "Puntatore di incremento";
              case 0xE77F: return "Subroutine fittizia";
              case 0xE780: return "Verifica avvio automatico - rimosso";
              case 0xE7A3: return "Esegui [&] - File USR esegui comando";
              case 0xE84B: return "Genera checksum";
              case 0xE853: return "Routine IRQ per bus seriale";
              case 0xE85B: return "Assistenza per il bus seriale";
              case 0xE909: return "Spedice dati";
              case 0xE99C: return "DATA OUT basso";
              case 0xE9A5: return "DATA OUT alto";
              case 0xE9AE: return "CLOCK OUT alto";
              case 0xE9B7: return "CLOCK OUT basso";
              case 0xE9C0: return "Legge prota IEEE";
              case 0xE9C9: return "Ottieni byte di dati dal bus";
              case 0xE9F2: return "Accetta byte con EOI";
              case 0xEA2E: return "Accetta i dati dal bus seriale";
              case 0xEA59: return "Testa per ATN";
              case 0xEA6E: return "LED lampeggiante per difetti hardware, autotest";
              case 0xEAA0: return "Routine di RESET all'accensione";
              case 0xEBFF: return "Aspetta il ciclo";
              case 0xEC9E: return "Carica cartella";
              case 0xED59: return "Trasmetti linea directory";
              case 0xED67: return "Ottieni elemento dal buffer";
              case 0xED84: return "Esegue [V] - coamndo Validazione";
              case 0xEDE5: return "Allocare blocchi di file in BAM";
              case 0xEE0D: return "Eseguire [N] - Nuovo comando (Formatta)";
              case 0xEEB7: return "Crea BAM";
              case 0xEEF4: return "Scrive BAM se necessario";
              case 0xEF3A: return "Imposta il puntatore del buffer per BAM";
              case 0xEF4D: return "Ottieni il numero di blocchi gratuiti per dir";
              case 0xEF5C: return "Contrassegna il blocco come libero";
              case 0xEF88: return "Imposta flag per cambio BAM";
              case 0xEF90: return "Contrassegna il blocco come allocato";
              case 0xEFCF: return "Cancella bit per settore nella voce BAM";
              case 0xEFE9: return "Potenza di 2";
              case 0xEFF1: return "Scrivi BAM dopo la modifica";
              case 0xF005: return "Cancella buffer BAM";
              case 0xF0D1: return "Cancella BAM";
              case 0xF10F: return "Ottieni il numero di buffer per BAM";
              case 0xF119: return "Numero di buffer per BAM";
              case 0xF11E: return "Trova e assegna blocchi liberi";
              case 0xF1A9: return "Trova settore libero e alloca";
              case 0xF1FA: return "Trova settori liberi nella traccia corrente";
              case 0xF220: return "Verify number of free blocks in BAM";
              case 0xF24B: return "Stabilisci il numero di settori per traccia";
              case 0xF258: return "Subroutine fittizia";
              case 0xF259: return "Inizializza il controller del disco";
              case 0xF2B0: return "Routine IRQ per controller del disco";
              case 0xF2F9: return "Trasporto della testina";
              case 0xF36E: return "Eseguire il programma nel buffer";
              case 0xF37C: return "Bump, trova la traccia 1 (vai alla fermata)";
              case 0xF393: return "Inizializza il puntatore nel buffer";
              case 0xF3b2: return "Leggi l'intestazione del blocco, verifica l'ID";
              case 0xF410: return "Conserva l'intestazione del blocco";
              case 0xF418: return "Lavoro restituisce valore 01 (OK) in coda";
              case 0xF41B: return "Lavoro restituisce il valore 0B (READ ERROR) nella coda";
              case 0xF41E: return "Lavoro Restituisce il valore 09 (READ ERROR) in coda";
              case 0xF423: return "Ottimizzazione del lavoro";
              case 0xF4CA: return "Verificare ulteriormente il codice di comando";
              case 0xF4D1: return "Leggi settore";
              case 0xF50A: return "Trova l'inizio del blocco dati";
              case 0xF510: return "Leggi l'intestazione del blocco";
              case 0xF556: return "Attendi SYNC";
              case 0xF56E: return "Verificare ulteriormente il codice di comando";
              case 0xF575: return "Scrivi il blocco dati su disco";
              case 0xF5E9: return "Calcola la parità per il buffer di dati";
              case 0xF5F2: return "Converti il buffer dei dati GCR in binario";
              case 0xF691: return "Verificare ulteriormente il codice di comando";
              case 0xF698: return "Confronta i dati scritti con i dati su disco";
              case 0xF6CA: return "Codice di comando per il settore di ricerca";
              case 0xF6D0: return "Converti 4 byte binari in 5 byte GCR";
              case 0xF77F: return "Tabella nybble GCR (5 bit)";
              case 0xF78F: return "Converte 260 byte in codice di gruppo da 325 byte";
              case 0xF7E6: return "Converti 5 byte GCR in 4 byte binari";
              case 0xF8A0: return "Tabella di conversione da GCR a binario - high nybble $ FF significa non valido";
              case 0xF8C0: return "Tabella di conversione da GCR a binario: nybble basso $ FF significa non valido";
              case 0xF8E0: return "Decodifica 69 byte GCR";
              case 0xF934: return "Converti l'intestazione del blocco in codice GCR";
              case 0xF969: return "Controller disco di immissione errore";
              case 0xF97E: return "Accendere il motore di azionamento";
              case 0xF98F: return "Spegnere il motore di azionamento";
              case 0xF99C: return "Controller del disco del loop di lavoro";
              case 0xFA05: return "Passa alla traccia successiva";
              case 0xFA1C: return "Calcola il numero di passi della testina";
              case 0xFA3B: return "Spostare il motore passo-passo a breve distanza";
              case 0xFA4E: return "Carico testina";
              case 0xFA7B: return "Prepara un movimento veloce della testina";
              case 0xFA97: return "Movimento veloce della testina";
              case 0xFAA5: return "Prepara un movimento lento della testina";
              case 0xFAC7: return "formattazione";
              case 0xFDA3: return "Scrivi SYNC 10240 volte, cancella traccia";
              case 0xFDC3: return "Lettura/scrittura ($621/$622) volte";
              case 0xFDD3: return "Contatore di tentativi di formattazione";
              case 0xFDF5: return "Copia i dati dal buffer di overflow";
              case 0xFE00: return "Passa alla lettura";
              case 0xFE0E: return "Scrivi $55 10240 volte";
              case 0xFE30: return "Converti l'intestazione nel buffer 0 in codice GCR";
              case 0xFE67: return "Routine interruzione";
              case 0xFE85: return "Traccia directory";
              case 0xFE86: return "Inizio del bam";
              case 0xFE87: return "Lunghezza del Bam per traccia";
              case 0xFE88: return "Fine del Bam";
              case 0xFE89: return "Tabella parole comandi";
              case 0xFE95: return "Byte basso indirizzo comando";
              case 0xFEA1: return "Byte alto indirizzo comando";
              case 0xFEAD: return "Byte per controllo sintassi";
              case 0xFEB2: return "Metodi controllo file \"RWAM\"";
              case 0xFEB6: return "Tipo File \"DSPUL\"";
              case 0xFEBB: return "Nomi dei tipi file: prima lettera \"DSPUR\"";
              case 0xFEC0: return "Nomi dei tipi file: seconda lettera \"EERSE\"";
              case 0xFEC5: return "Nomi dei tipi file: terza lettera \"LQGRL\"";
              case 0xFECA: return "Errore valore bit LED";
              case 0xFECD: return "Machere per bit comando";
              case 0xFED1: return "Numero di settori per traccia";
              case 0xFED5: return "Costanti per formato disco";
              case 0xFED6: return "4 track ranges";
              case 0xFED7: return "Number of tracks";
              case 0xFED8: return "Tracks on which sector numbers change";
              case 0xFEDB: return "Control bytes for head postion";
              case 0xFEE6: return "ROM checksum";
              case 0xFEE7: return "From UI command $EB22, to reset";
              case 0xFEEA: return "Patch for diagnostic routine from $EA7A";
              case 0xFEF3: return "Delay loop for serial bus in 1541 mode, from $E97D";
              case 0xFEFB: return "Patch for data output to serial bus, from $E980";
              case 0xFF01: return "U9 vector, switch 1540/1541";
              case 0xFF10: return "Patch for reset routine, from $EAA4";
              case 0xFF20: return "Patch for listen to serial bus, from $E9DC";
              case 0xFF2F: return "Non usato";					
              case 0xFFE6: return "Formatta [C8C6]";
              case 0xFFE8: return "Spegni motore [F98F]";
              case 0xFFEA: return "UA, U1 [CD5F]";
              case 0xFFEC: return "UB, U2 [CD97]";
              case 0xFFEE: return "UC, U3 [0500]";
              case 0xFFF0: return "UD, U4 [0503]";
              case 0xFFF2: return "UE, U5 [0506]";
              case 0xFFF4: return "UF, U6 [0509]";
              case 0xFFF6: return "UG, U7 [050C]";
              case 0xFFF8: return "UH, U8 [050F]";
              case 0xFFFA: return "UI, U9 [FF01]";
              case 0xFFFC: return "RESET [EAA0]";
              case 0xFFFE: return "IRQ   [FE67]";              
              
              default:
                if ((addr>=0x0103) && (addr<=0x0145)) return "Stack attuale del processore";  
                if ((addr>=0x0146) && (addr<=0x01B9)) return "Non usato"; 
                if ((addr>=0x01BA) && (addr<=0x01FF)) return "Buffer ausiliario per codifica/decodifica GCR";                 
                if ((addr>=0x0200) && (addr<=0x0229)) return "Buffer di input: 42 bytes (Usati per accettare i comandi dall'host)";      
                if ((addr>=0x022B) && (addr<=0x023D)) return "Numero canale assegnato agli indirizzi secondari"; 
                if ((addr>=0x02B1) && (addr<=0x02CB)) return "Buffer usato per costuire l'attuale voce (linea BASIC) mentre esegue LOAD \"$\""; 
                if ((addr>=0x02CC) && (addr<=0x02D4)) return "Non usato";  
                if ((addr>=0x02D5) && (addr<=0x02F8)) return "Buffer messaggio di errore";  
                if ((addr>=0x0300) && (addr<=0x03FF)) return "Buffer #0";
                if ((addr>=0x0400) && (addr<=0x04FF)) return "Buffer #1";
                if ((addr>=0x0500) && (addr<=0x05FF)) return "Buffer #2";
                if ((addr>=0x0600) && (addr<=0x06FF)) return "Buffer #3";
                if ((addr>=0x0700) && (addr<=0x07FF)) return "Buffer #4";
                break;
            }
          default:
            switch ((int)addr) {
              case 0x00: return "Buffer #0 command and status registers";
              case 0x01: return "Buffer #1 command and status register";
              case 0x02: return "Buffer #2 command and status register";
              case 0x03: return "Buffer #3 command and status register";
              case 0x04: return "Buffer #4 command and status register";
              case 0x05: return "Unused (Command and status register of not existing buffer #5)";
              case 0x06: 
              case 0x07: return "Buffer #0 track and sector register";
              case 0x08: 
              case 0x09: return "Buffer #1 track and sector register";
              case 0x0A: 
              case 0x0B: return "Buffer #2 track and sector register";
              case 0x0C: 
              case 0x0D: return "Buffer #3 track and sector register";
              case 0x0E: 
              case 0x0F: return "Buffer #4 track and sector register";
              case 0x10: 
              case 0x11: return "Unused (Track and sector register of not existing buffer #5";
              case 0x12: 
              case 0x13: return "Unit #0 expected sector header ID";
              case 0x14:
              case 0x15: return "Unused (Expected sector header ID of not existing unit #1)";
              case 0x16: 
              case 0x17: return "Header ID from header of sector last read from disk";
              case 0x18: 
              case 0x19: return "Track and sector number from header of sector last read from disk";              	
              case 0x1A: return "Header checksum from header of sector last read from disk";
              case 0x1B: return "Unused";
              case 0x1C: return "Unit #0 disk change indicator";
              case 0x1D: return "Unused (Disk change indicator of not existing unit #1)";
              case 0x1E: return "Previous status of unit #0 write protect photocell (in bit #4)";
              case 0x1F: return "Unused (Previous status of write protect photocell of not existing unit #1)";
              case 0x20: return "Unit #0 disk controller internal command register";
              case 0x21: return "Unused (Disk controller internal command register of not existing unit #1)";
              case 0x22: return "Unit #0 current track number";
              case 0x23: return "Serial bus communication speed switch";
              case 0x24:
              case 0x25: 
              case 0x26:
              case 0x27:
              case 0x28:
              case 0x29:
              case 0x2A: 
              case 0x2B: return "Header of sector last read from or next to be written onto disk, in GCR-encoded form";
              case 0x2C: return "Unused";
              case 0x2D: return "Unused";
              case 0x2E: 
              case 0x2F: return "Pointer to current byte in buffer during GCR-encoding/decoding";
              case 0x30: 
              case 0x31: return "Pointer to beginning of current buffer";
              case 0x32: 
              case 0x33: return "Pointer to track and sector registers of current buffer";
              case 0x34: return "GCR-byte counter during GCR-encoding/decoding";
              case 0x35: return "Unused";
              case 0x36: return "Byte counter during GCR-encoding/decoding";
              case 0x37: return "Unused";
              case 0x38: return "Data block signature byte of sector last read from disk";
              case 0x39: return "Expected value of sector header signature byte. Default: $08";
              case 0x3A: return "Computed checksum of data in buffer";
              case 0x3B: return "Unused";
              case 0x3C: return "Unused";
              case 0x3D: return "Disk controller current unit number";
              case 0x3E: return "Disk controller previous unit number. Values";
              case 0x3F: return "Disk controller current buffer number";
              case 0x40: return "Current track number";
              case 0x41: return "Buffer number that needs seeking";
              case 0x42: return "Number of tracks to move during seeking"; 
              case 0x43: return "Number of sectors on current track";
              case 0x44: return "Data density (in bits #5-#6). Temporary register for buffer command";
              case 0x45: return "Disk controller buffer command register";
              case 0x46: return "Unused";
              case 0x47: return "Expected value of data block header signature byte. Default: $07";
              case 0x48: return "Motor spin up/down delay counter";
              case 0x49: return "Original value of stack pointer before execution of disk controller interrupt";
              case 0x4A: return "Number of halftracks to move during seeking";
              case 0x4B: return "Retry counter for reading sector header. Temporary area during seeking";
              case 0x4C: return "On current track, the distance of the sector, that matches the sector of a buffer and is nearest to the one last read from disk";
              case 0x4D: return "On current track, the sector that is two sectors away from the one last read from disk";
              case 0x4E:
              case 0x4F: return "Pointer to beginning of auxiliary buffer during GCR-encoding (byte swapped)";
              case 0x50: return "Indicator of buffer data currently being in GCR-encoded form";
              case 0x51: return "Current track number during formatting";
              case 0x52:
              case 0x53:
              case 0x54:
              case 0x55: return "Temporary area for data bytes during GCR-encoding/decoding";
              case 0x56: 
              case 0x57:     
              case 0x58:
              case 0x59:      
              case 0x5A:      
              case 0x5B:      
              case 0x5C:
              case 0x5D: return "Temporary area for data nybbles and GCR bytes during GCR-encoding/decoding";
              case 0x5E: return "Number of halftracks to accelerate/decelerate through during accelerated seeking";
              case 0x5F: return "Acceleration/deceleration factor during accelerated seeking";
              case 0x60: return "Delay counter after seeking (So that head stops vibrating), halftrack counter for acceleration/deceleration during accelerated seeking";   
              case 0x61: return "Halftrack counter for full speed during accelerated seeking";
              case 0x62:
              case 0x63: return "Pointer to routine next to be executed during seeking"; 
              case 0x64: return "Lower distance limit of accelerated seeking, in halftracks. Default: $C8";
              case 0x65:  
              case 0x66: return "Pointer to warm reset (\"UI\" command) routine. Default: $EB22.";
              case 0x67: return "Unknown";
              case 0x68: return "Automatic disk initialization switch. Default $00";
              case 0x69: return "Soft interleave (Distance, in sectors, for allocating the next sector for files). Default: $0A";
              case 0x6A: return "Number of retries on disk commands. Default: $05";
              case 0x6B:
              case 0x6C: return "Pointer to \"Ux\" user command pointer table. Default: $FFEA";
              case 0x6D: 
              case 0x6E: return "Temporary pointer for BAM operations";
              case 0x6F: 
              case 0x70: return "Temporary pointer for various operations";
              case 0x71: 
              case 0x72: 
              case 0x73: 
              case 0x74: return "Unknown";
              case 0x75:
              case 0x76: return "Pointer to current byte during memory test upon startup. Execution address of current Ux user command";
              case 0x77: return "Serial bus LISTEN command to accept (Device number OR $20)";
              case 0x78: return "Serial bus TALK command to accept (Device number OR $40)";
              case 0x79: return "Serial bus LISTEN command indicator";
              case 0x7A: return "Serial bus TALK command indicator";
              case 0x7B: return "Unknown";
              case 0x7C: return "Serial bus ATN arrival indicator";
              case 0x7D: return "End of command indicator";
              case 0x7E: return "Track number of previously opened file (Used when opening \"*\")";
              case 0x7F: return "Current unit. Default: $00";
              case 0x80:
              case 0x81: return "Track and sector number for various operations";
              case 0x82: return "Current channel number";
              case 0x83: return "Current secondary address (only bits #0-#3)";
              case 0x84: return "Current secondary address";
              case 0x85: return "Data byte read from serial bus. Data byte read from buffer or to be written into buffer";
              case 0x86:
              case 0x87: return "Pointer to current byte in directory buffer";
              case 0x88:
              case 0x89: return "Pointer to current byte or execution address of user code during \"&\" command";
              case 0x8A: return "Unknown";
              case 0x8B: 
              case 0x8C:
              case 0x8D: return "Temporary area for integer division. (Used to compute the side sector of relative files)";
              case 0x8E: 
              case 0x8F: 
              case 0x90: 
              case 0x91: 
              case 0x92: 
              case 0x93: return "Unknown";
              case 0x94: 
              case 0x95: return "Pointer to current directory entry";
              case 0x96: 
              case 0x97: return "Unused";
              case 0x98: return "Bit counter during serial bus input/output";
              case 0x99: 
              case 0x9A: return "Pointer to buffer #0. Default: $0300";
              case 0x9B: 
              case 0x9C: return "Pointer to buffer #1. Default: $0400";
              case 0x9D: 
              case 0x9E: return "Pointer to buffer #2. Default: $0500";
              case 0x9F: 
              case 0xA0: return "Pointer to buffer #3. Default: $0600";
              case 0xA1:
              case 0xA2: return "Pointer to buffer #4. Default: $0700";
              case 0xA3: 
              case 0xA4: return "Pointer to input buffer. Default: $0200";
              case 0xA5: 
              case 0xA6: return "Pointer to error message buffer. Default: $02D5";
              case 0xA7: 
              case 0xA8: 
              case 0xA9: 
              case 0xAA: 
              case 0xAB: 
              case 0xAC:
              case 0xAD: return "Primary buffer number assigned to channels";
              case 0xAE:
              case 0xAF: 
              case 0xB0:
              case 0xB1: 
              case 0xB2:
              case 0xB3: 
              case 0xB4: return "Secondary buffer number assigned to channels";
              case 0xB5: 
              case 0xB6: 
              case 0xB7: 
              case 0xB8:
              case 0xB9:
              case 0xBA: return "Length of file assigned to channels, low byte. For relative files, number of records, low byte";
              case 0xBB:
              case 0xBC: 
              case 0xBD: 
              case 0xBE: 
              case 0xBF: 
              case 0xC0: return "Length of file assigned to channels, high byte. For relative files, number of records, high byte";
              case 0xC1:
              case 0xC2: 
              case 0xC3:
              case 0xC4: 
              case 0xC5: 
              case 0xC6: return "Offset of current byte in buffer assigned to channels";
              case 0xC7:
              case 0xC8:
              case 0xC9:
              case 0xCA:
              case 0xCB:
              case 0xCC: return "Record length of relative file assigned to channels";
              case 0xCD: 
              case 0xCE: 
              case 0xCF: 
              case 0xD0: 
              case 0xD1:
              case 0xD2: return "Buffer number holding side sector of relative file assigned to channels";
              case 0xD3: return "Comma counter during fetching unit numbers from command";
              case 0xD4: return "Offset of current byte in relative file record";
              case 0xD5: return "Side sector number belonging to current relative file record";
              case 0xD6: return "Offset of track and sector number of current relative file record in side sector";
              case 0xD7: return "Offset of record in relative file data sector";
              case 0xD8: 
              case 0xD9:
              case 0xDA:
              case 0xDB:
              case 0xDC: return "Sector number of directory entry of files"; 
              case 0xDD:
              case 0xDE:
              case 0xDF:    
              case 0xE0: 
              case 0xE1: return "Offset of directory entry of file";   
              case 0xE2:
              case 0xE3:
              case 0xE4:
              case 0xE5:
              case 0xE6: return "Unit number of files";
              case 0xE7:
              case 0xE8:
              case 0xE9:    
              case 0xEA:    
              case 0xEB: return "File type and flags of files";
              case 0xEC:
              case 0xED:
              case 0xEE:    
              case 0xEF:    
              case 0xF0:
              case 0xF1: return "Unit number, file type and flags of files assigned to channels";
              case 0xF2: 
              case 0xF3:
              case 0xF4: 
              case 0xF5:
              case 0xF6: 
              case 0xF7: return "Input/output flags of channels";
              case 0xF8: return "End of file indicator of current channel";
              case 0xF9: return "Current buffer number";
              case 0xFA: 
              case 0xFB:
              case 0xFE: return "Unknown";
              case 0xFF: return "Unit #0 BAM input/output error indicator";
              
              case 0x100: return "BAM input/output error indicator of not existing unit #1";
              case 0x101: return "Unit #0 BAM version code (Byte at offset #$02 in sector 18;00) Expected: $41,A";
              case 0x102: return "BAM version code of not existing unit #1";
              
              case 0x22A: return "DOS command number";
              case 0x23E: 
              case 0x23F:
              case 0x240: 
              case 0x241: 
              case 0x242: return "Temporary area of next data byte to be written from buffers #0-#4 to serial bus";
              case 0x243: return "Temporary area of next data byte to be written from error message buffer to serial bus";
              case 0x244: 
              case 0x245: 
              case 0x246:
              case 0x247: 
              case 0x248: return "Offset of last data byte in buffers #0-#4";
              case 0x249: return "Offset of last data byte in error message buffer";
              case 0x24A: return "File type of current file";
              case 0x24B: return "Length of name of current file";
              case 0x24C: return "Temporary area for secondary address";
              case 0x24D: return "Temporary area for disk controller command";
              case 0x24E: return "Number of sectors on current track";
              case 0x24F:
              case 0x250: return "Buffer allocation register. Default: $FFE0";
              case 0x251: return "Unit #0 BAM change indicator";
              case 0x252: return "BAM change indicator of not existing unit #1";
              case 0x253: return "File found indicator during searching for a file name in directory.";
              case 0x254: return "LOAD channel directory indicator";
              case 0x255: return "End of command indicator";
              case 0x256: return "Channel allocation register";
              case 0x257: return "Temporary area for channel number";
              case 0x258: return "Record length of current relative file";
              case 0x259:
              case 0x25A: return "Track and sector number of first side sector of current relative file";
              case 0x25B:
              case 0x25C:
              case 0x25D:
              case 0x25E:    
              case 0x25F: return "Original disk controller commands of buffers #0-#4";
              case 0x260:    
              case 0x261:
              case 0x262:    
              case 0x263:    
              case 0x264:        
              case 0x265: return "Sector number of directory entry of files specified in command";
              case 0x266:
              case 0x267:    
              case 0x268:    
              case 0x269:    
              case 0x26A:                      
              case 0x26B: return "Offset of directory entry of files specified in command";
              case 0x26C: return "Switch for displaying warning messages of relative files";
              case 0x26D: return "Unit #0 LED bit";
              case 0x26E: return "Unit number of previously opened file (Used when opening \"*\")";
              case 0x26F: return "Sector number of previously opened file (Used when opening \"*\")";
              case 0x270: return "Temporary area for channel number";
              case 0x271: return "Unused";
              case 0x272:
              case 0x273: return "BASIC line number for entries sent to host during LOAD'ing \"$\"";
              case 0x274: return "Length of command";
              case 0x275: return "First character of command. Character to search for in input buffer"; 
              case 0x276: return "Offset of first character after file name in command";
              case 0x277: return "Temporary area for number of commas in command";
              case 0x278: return "Number of commas or unit numbers in command";
              case 0x279: return "Number of commas before equation mark in command. Number of current file in command during searching for files specified in command";
              case 0x27A: return "Offset of character before colon in command";
              case 0x27B: 
              case 0x27C:
              case 0x27D:    
              case 0x27E:    
              case 0x27F: return "Offset of file names in command";
              case 0x280:
              case 0x281:
              case 0x282:
              case 0x283:
              case 0x284: return "Track number of files specified in command";   
              case 0x285:
              case 0x286:    
              case 0x287:     
              case 0x288:     
              case 0x289: return "Sector number of files specified in command. For B-x commands, lower byte of parameters";
              case 0x28A: return "Number of wildcards found in current file name";
              case 0x28B: return "Command syntax flags";
              case 0x28C: return "Number of units to process during reading the directory";
              case 0x28D: return "Current unit number during reading the directory";
              case 0x28E: return "Previous unit number during reading the directory";
              case 0x28F: return "Indicator to keep searching in the directory";
              case 0x290: return "Current directory sector number";
              case 0x291: return "Directory sector number to read";
              case 0x292: return "Offset of current directory entry in directory sector";
              case 0x293: return "End of directory indicator";
              case 0x294: return "Offset of current directory entry in directory sector";
              case 0x295: return "Number of remaining directory entries in directory sector minus 1";
              case 0x296: return "File type of file being searched in directory";
              case 0x297: return "File open mode";
              case 0x298: return "Message display switch for disk errors";
              case 0x299: return "Offset of current byte in halftrack seek table during retrying disk operations on adjacent halftracks";
              case 0x29A: return "Direction of seeking back to original halftrack during retrying disk operations on adjacent halftracks";
              case 0x29B: return "Unit #0 track number of current BAM entry";
              case 0x29C: return "Track number of current BAM entry of not existing unit #1";
              case 0x29D:
              case 0x29E: return "Unit #0 track numbers of two cached BAM entries";
              case 0x29F:
              case 0x2A0: return "Track numbers of two cached BAM entries of not existing unit #1";
              case 0x2A1:
              case 0x2A2:
              case 0x2A3:
              case 0x2A4:
              case 0x2A5:
              case 0x2A6:    
              case 0x2A7:
              case 0x2A8: return "Unit #0 two cached BAM entries";
              case 0x2A9:
              case 0x2AA:
              case 0x2AB:    
              case 0x2AC:    
              case 0x2AD:
              case 0x2AE:
              case 0x2AF:    
              case 0x2B0: return "Two cached BAM entries of not existing unit #1";
              case 0x2F9: return "Disk update upon BAM change switch. Different BAM-related operations expect different values here";
              case 0x2FA: return "Unit #0 number of free blocks, low byte";
              case 0x2FB: return "Number of free blocks, low byte, of not existing unit #1";
              case 0x2FC: return "Unit #0 number of free blocks, high byte";
              case 0x2FD: return "Number of free blocks, high byte, of not existing unit #1";
              case 0x2FE: return "Unit #0 direction of seeking of adjacent halftrack";
              case 0x2FF: return "Direction of seeking of adjacent halftrack of not existing unit #1";
              
              case 0x1800: return "Port B #1, serial bus";
              case 0x1801: return "Port A #1. Read to acknowledge interrupt generated by ATN IN going high";
              case 0x1802: return "Port B #1 data direction register. Default: $1A, %00011010";
              case 0x1803: return "Port A #1 data direction register. Default: $FF, %11111111.";
              case 0x1804: 
              case 0x1805: return "Timer #1. Read low byte or write high byte to start timer or restart timer upon underflow";
              case 0x1806: 
              case 0x1807: return "Timer latch #1. Read/write starting value of timer from/to here";
              case 0x1808:
              case 0x1809:
              case 0x180A: return "Unused";
              case 0x180B: return "Timer control register #1";
              case 0x180C: return "Unused";
              case 0x180D: return "Interrupt status register #1";
              case 0x180E: return "Interrupt control register #1";
              case 0x180F: return "Unused";
              
              case 0x1C00: return "Port B #2";
              case 0x1C01: return "Port A #2. Data byte last read from or to be next written onto disk";
              case 0x1C02: return "Port B #2 data direction register";
              case 0x1C03: return "Port A #2 data direction register";
              case 0x1C04: 
              case 0x1C05: return "Timer #2. Read low byte or write high byte to start timer or restart timer upon underflow";
              case 0x1C06: 
              case 0x1C07: return "Timer latch #2. Read/write starting value of timer from/to here";
              case 0x1C08:
              case 0x1C09:
              case 0x1C0A: return "Unused";
              case 0x1C0B: return "Timer control register #2";
              case 0x1C0C: return "Auxiliary control register #2";
              case 0x1C0D: return "Interrupt status register #2";
              case 0x1C0E: return "Interrupt control register #2";
              case 0x1C0F: return "Unused";        
              
              case 0xC100: return "Turn LED on for current drive";
              case 0xC118: return "Turn LED on";
              case 0xC123: return "Clear error flags";
              case 0xC12C: return "Prepare for LED flash after error";
              case 0xC146: return "Interpret command from computer";
              case 0xC194: return "Prepare error msg after executing command";              
              case 0xC1BD: return "Erase input buffer";
              case 0xC1C8: return "Output error msg (track and sector 0)";
              case 0xC1D1: return "Check input line";
              case 0xC1E5: return "Check ':' on input line";
              case 0xC1EE: return "Check input line";
              case 0xC268: return "Search character in input buffer";
              case 0xC2B3: return "Check line length";
              case 0xC2DC: return "Clear flags for command input";
              case 0xC312: return "Preserve drive number";
              case 0xC33C: return "Search for drive number";
              case 0xC368: return "Get drive number";
              case 0xC38F: return "Reverse drive number";
              case 0xC398: return "Check given file type";
              case 0xC3BD: return "Check given drive number";
              case 0xC3CA: return "Verify drive number";
              case 0xC440: return "Flags for drive check";
              case 0xC44f: return "Search for file in directory";
              case 0xC63D: return "Test and initalise drive";
              case 0xC66E: return "Name of file in directory buffer";
              case 0xC688: return "Copy filename to work buffer";
              case 0xC6A6: return "Search for end of name in command";
              case 0xC7AC: return "Clear Directory Output Buffer";
              case 0xC7B7: return "Create header with disk name";
              case 0xC806: return "Print 'blocks free.'";
              case 0xC817: return "'Blocks free.'";
              case 0xC823: return "Perform [S] - Scratch command";
              case 0xC87D: return "Erase file";
              case 0xC8B6: return "Erase dir entry";
              case 0xC8C1: return "Perform [D] - Backup command (Unused)";
              case 0xC8C6: return "Format disk";
              case 0xC8F0: return "Perform [C] - Copy command";
              case 0xCA88: return "Perform [R] - Rename command";
              case 0xCACC: return "Check if file present";
              case 0xCAF8: return "Perform [M] - Memory command";
              case 0xCB20: return "M-R memory read";
              case 0xCB50: return "M-W memory write";
              case 0xCB5C: return "Perform [U] - User command";
              case 0xCB84: return "Open direct access channel, number";
              case 0xCC1B: return "Perform [B] - Block/Buffer command";
              case 0xCC5D: return "Block commands \"AFRWEP\"";
              case 0xCC63: return "Block command vectors";
              case 0xCC6F: return "Get parameters form block commands";
              case 0xCCF2: return "Decimal values  1, 10, 100";
              case 0xCCF5: return "B-F block free";
              case 0xCD03: return "B-A block allocate";
              case 0xCD36: return "Read block to buffer";
              case 0xCD3c: return "Get byte from buffer";
              case 0xCD42: return "Read block from disk";
              case 0xCD56: return "B-R block read";
              case 0xCD5F: return "U1, Block read without changing buffer pointer";
              case 0xCD73: return "B-W block write";
              case 0xCD97: return "U2, Block write without changing buffer pointer";
              case 0xCDA3: return "B-E block execute";
              case 0xCDBD: return "B-P block pointer";
              case 0xCDD2: return "Open channel";
              case 0xCDF2: return "Check buffer number and open channel";
              case 0xCE0E: return "Set pointer for REL file";
              case 0xCE6E: return "Divide by 254";
              case 0xCE71: return "Divide by 120";
              case 0xCED9: return "Erase work storage";
              case 0xCEE2: return "Left shift 3-byte register twice";
              case 0xCEE5: return "Left shift 3-byte register once";
              case 0xCEED: return "Add 3-byte registers";
              case 0xCF8C: return "Change buffer";
              case 0xCF9B: return "Write data in buffer";
              case 0xCFF1: return "Write data byte in buffer";
              case 0xD005: return "Perform [I] - Initalise command";
              case 0xD00e: return "Read BAM from disk";
              case 0xD042: return "Load BAM";
              case 0xD075: return "Calculate blocks free";
              case 0xD0C3: return "Read block";
              case 0xD0C7: return "Write block";
              case 0xD0EB: return "Open channel for reading";
              case 0xD107: return "Open channel for writing";
              case 0xD125: return "Check for file type REL";
              case 0xD12f: return "Get buffer and channel numbers";
              case 0xD137: return "Get a byte from buffer";
              case 0xD156: return "Get byte and read next block";
              case 0xD19D: return "Write byte in buffer and block";
              case 0xD1C6: return "Increment buffer pointer";
              case 0xD1D3: return "Get drive number";
              case 0xD1DF: return "Find write channel and buffer";
              case 0xD1E2: return "Find read channel and buffer";
              case 0xD227: return "Close channel";
              case 0xD25A: return "Free buffer";
              case 0xD28E: return "Find buffer";
              case 0xD307: return "Close all channels";
              case 0xD313: return "Close all channels of other drives";
              case 0xD37F: return "Find channel and allocate";
              case 0xD39B: return "Get byte for output";
              case 0xD44D: return "Read next block";
              case 0xD460: return "Read block";
              case 0xD464: return "Write block";
              case 0xD475: return "Allocate buffer and read block";
              case 0xD486: return "Allocate new block";
              case 0xD48D: return "Write dir block";
              case 0xD4C8: return "Set buffer pointer";
              case 0xD4DA: return "Close internal channel";
              case 0xD4E8: return "Set buffer pointer";
              case 0xD4F6: return "Get byte from buffer";
              case 0xD506: return "Check track and sector numbers";
              case 0xD552: return "Get track and sector numbers for current job";
              case 0xD55F: return "Check for vaild track and sector numbers";
              case 0xD572: return "DOS mismatch error";
              case 0xD586: return "Read block";
              case 0xD58A: return "Write block";
              case 0xD599: return "Verify execution";
              case 0xD5C6: return "Additional attempts for read errors";
              case 0xD676: return "Move head by half a track";
              case 0xD693: return "Move head one track in or out";
              case 0xD6A6: return "Attempt command execution multiple times";
              case 0xD6D0: return "Transmit param to disk controller";
              case 0xD6E4: return "Enter file in dir";
              case 0xD7B4: return "OPEN command, secondary addr 15";
              case 0xD7C7: return "-Check '*' Last file";
              case 0xD7F3: return "-Check '$' Directory";
              case 0xD815: return "-Check '#' Channel";
              case 0xD8F5: return "Open a file with overwriting (@)";
              case 0xD9A0: return "Open file for reading";
              case 0xD9E3: return "Open file for writing";
              case 0xDA09: return "Check file type and control mode";
              case 0xDA2A: return "Preparation for append";
              case 0xDA55: return "Open directory";
              case 0xDAC0: return "Close routine";
              case 0xDB02: return "Close file";
              case 0xDB62: return "Write last block";
              case 0xDBA5: return "Directory entry";
              case 0xDC46: return "Read block, allocate buffer";
              case 0xDCB6: return "Reset pointer";
              case 0xDCDA: return "Construct a new block";
              case 0xDD8D: return "Write byte in side-sector block";
              case 0xDD95: return "Manipulate flags";
              case 0xDDAB: return "Verify command code for writing";
              case 0xDDF1: return "Write a block of a REL file";
              case 0xDDFD: return "Write bytes for following track";
              case 0xDE0C: return "Get following track and sector numbers";
              case 0xDE19: return "Following track for last block";
              case 0xDE2B: return "buffer pointer to zero";
              case 0xDE3B: return "Get track and sector";
              case 0xDE50: return "Write (?)";
              case 0xDE57: return "R";
              case 0xDE5E: return "W";
              case 0xDE65: return "R";
              case 0xDE73: return "R";
              case 0xDE95: return "Get following track and sector from buffer";
              case 0xDEA5: return "Copy buffer contents";
              case 0xDEC1: return "Erase buffer Y";
              case 0xDED2: return "Get side-sector number";
              case 0xDEDC: return "Set buffer pointer to side-sector";
              case 0xDEE9: return "Buffer pointer for side-sector";
              case 0xDEF8: return "Get side sector and buffer pointer";
              case 0xDF1B: return "Read side-sector";
              case 0xDF21: return "Write side-sector";
              case 0xDF45: return "Set buffer pointer in side-sector";
              case 0xDF4C: return "Calculate number of blocks in a REL file";
              case 0xDF66: return "Verify side-sector in buffer";
              case 0xDF93: return "Get buffer number";
              case 0xDFD0: return "Get next record iin REL file";
              case 0xE03C: return "Write block and read next block";
              case 0xE07C: return "Write a byte in a record";
              case 0xE0AB: return "Write byte in REL file";
              case 0xE0F3: return "Fill record with 0s";
              case 0xE105: return "Write buffer number in table";
              case 0xE120: return "Get byte from REL file";
              case 0xE1CB: return "Get last side-sector";
              case 0xE207: return "Perform [P] - Position command";
              case 0xE2E2: return "Divide data blocks into records";
              case 0xE304: return "Set pointer to next record";
              case 0xE31C: return "Expand side-sector";
              case 0xE44E: return "Write side-sector and allocate new";
              case 0xE4fC: return "Table of error messages: 00, Ok";                            
              case 0xE500: return "Table of error messages: 20,21,22,23,24,27, Read error";
              case 0xE50B: return "Table of error messages: 52,	File too large";
              case 0xE517: return "Table of error messages: 50,	Record not present";
              case 0xE522: return "Table of error messages: 51,	Overflow in record";
              case 0xE52F: return "Table of error messages: 25,28, Write error";
              case 0xE533: return "Table of error messages: 26,	Write protect on";
              case 0xE540: return "Table of error messages: 29,	Disk id mismatch";
              case 0xE546: return "Table of error messages: 30,31,32,33,34, Syntax error";
              case 0xE552: return "Table of error messages: 60,	Write file open";
              case 0xE556: return "Table of error messages: 63,	File exists";
              case 0xE55F: return "Table of error messages: 64,	File type mismatch";
              case 0xE567: return "Table of error messages: 65,	No block";
              case 0xE570: return "Table of error messages: 66,67, Illegal track or sector";
              case 0xE589: return "Table of error messages: 61,	File not open";
              case 0xE58D: return "Table of error messages: 39,62, File not found";
              case 0xE592: return "Table of error messages: 01,	Files scratched";
              case 0xE59F: return "Table of error messages: 70,	No channel";
              case 0xE5AA: return "Table of error messages: 71,	Dir error";
              case 0xE5AF: return "Table of error messages: 72,	Disk full";
              case 0xE5B6: return "Table of error messages: 73,	Cbm dos v2.6 1541";
              case 0xE5C8: return "Table of error messages: 74,	Drive not ready";
              case 0xE5D5: return "Indexed words: 09 Error";
              case 0xE5DB: return "Indexed words: 0A Write";
              case 0xE5E1: return "Indexed words: 03 File";
              case 0xE5E6: return "Indexed words: 04 Open";
              case 0xE5EB: return "Indexed words: 05 Mismatch";
              case 0xE5F4: return "Indexed words: 06 Not";
              case 0xE5F8: return "Indexed words: 07 Found";
              case 0xE5FE: return "Indexed words: 08 Disk";
              case 0xE603: return "Indexed words: 0B Record";
              case 0xE60A: return "Prepare error number and message";
              case 0xE645: return "Print error message into error buffer";
              case 0xE680: return "TALK";
              case 0xE688: return "LISTEN";
              case 0xE69B: return "Convert BIN to 2-Ascii (error message buffer)";
              case 0xE6AB: return "Convert BCD to 2-Ascii (error message buffer)";
              case 0xE6BC: return "Write OK in buffer";
              case 0xE6C1: return "Print error on track 00,00 to error buffer";
              case 0xE6C7: return "Print error on current track to error buffer";
              case 0xE706: return "Write error message string to buffer";
              case 0xE754: return "Get character and in buffer";
              case 0xE767: return "Get a char of the error message";
              case 0xE775: return "Increment pointer";
              case 0xE77F: return "Dummy subroutine";
              case 0xE780: return "Check for auto start - removed";
              case 0xE7A3: return "Perform [&] - USR file execute command";
              case 0xE84B: return "Generate checksum";
              case 0xE853: return "IRQ routine for serial bus";
              case 0xE85B: return "Service the serial bus";
              case 0xE909: return "Send data";
              case 0xE99C: return "DATA OUT lo";
              case 0xE9A5: return "DATA OUT hi";
              case 0xE9AE: return "CLOCK OUT hi";
              case 0xE9B7: return "CLOCK OUT lo";
              case 0xE9C0: return "Read IEEE port";
              case 0xE9C9: return "Get data byte from bus";
              case 0xE9F2: return "Accept byte with EOI";
              case 0xEA2E: return "Accept data from serial bus";
              case 0xEA59: return "Test for ATN";
              case 0xEA6E: return "Flash LED for hardware defects, self-test";
              case 0xEAA0: return "Power-up RESET routine";
              case 0xEBFF: return "Wait loop";
              case 0xEC9E: return "Load dir";
              case 0xED59: return "Transmit dir line";
              case 0xED67: return "Get byte from buffer";
              case 0xED84: return "Perform [V] - Validate command";
              case 0xEDE5: return "Allocate file blocks in BAM";
              case 0xEE0D: return "Perform [N] - New (Format) command";
              case 0xEEB7: return "Create BAM";
              case 0xEEF4: return "Write BAM if needed";
              case 0xEF3A: return "Set buffer pointer for BAM";
              case 0xEF4D: return "Get number of free blocks for dir";
              case 0xEF5C: return "Mark block as free";
              case 0xEF88: return "Set flag for BAM changed";
              case 0xEF90: return "Mark block as allocated";
              case 0xEFCF: return "Erase bit for sector in BAM entry";
              case 0xEFE9: return "Powers of 2";
              case 0xEFF1: return "Write BAM after change";
              case 0xF005: return "Erase BAM buffer";
              case 0xF0D1: return "Crear BAM";
              case 0xF10F: return "Get buffer number for BAM";
              case 0xF119: return "Buffer number for BAM";
              case 0xF11E: return "Find and allocate free block";
              case 0xF1A9: return "Find free sector and allocate";
              case 0xF1FA: return "Find free sectors in current track";
              case 0xF220: return "Verify number of free blocks in BAM";
              case 0xF24B: return "Establish number of sectors per track";
              case 0xF258: return "Dummy subroutine";
              case 0xF259: return "Initialise disk controller";
              case 0xF2B0: return "IRQ routine for disk controller";
              case 0xF2F9: return "Head transport";
              case 0xF36E: return "Execute program in buffer";
              case 0xF37C: return "Bump, find track 1 (head at stop)";
              case 0xF393: return "Initialise pointer in buffer";
              case 0xF3b2: return "Read block header, verify ID";
              case 0xF410: return "Preserve block header";
              case 0xF418: return "Work Return value 01 (OK) into queue";
              case 0xF41B: return "Work Return value 0B (READ ERROR) into queue";
              case 0xF41E: return "Work Return value 09 (READ ERROR) into queue";
              case 0xF423: return "Job optimisation";
              case 0xF4CA: return "Test command code further";
              case 0xF4D1: return "Read sector";
              case 0xF50A: return "Find start of data block";
              case 0xF510: return "Read block header";
              case 0xF556: return "Wait for SYNC";
              case 0xF56E: return "Test command code further";
              case 0xF575: return "Write data block to disk";
              case 0xF5E9: return "Calculate parity for data buffer";
              case 0xF5F2: return "Convert buffer of GCR data into binary";
              case 0xF691: return "Test command code further";
              case 0xF698: return "Compare written data with data on disk";
              case 0xF6CA: return "Command code for find sector";
              case 0xF6D0: return "Convert 4 binary bytes to 5 GCR bytes";
              case 0xF77F: return "GCR (5-bit) nybble table";
              case 0xF78F: return "Convert 260 bytes to 325 bytes group code";
              case 0xF7E6: return "Convert 5 GCR bytes to 4 binary bytes";
              case 0xF8A0: return "Conversion table GCR to binary - high nybble $FF means invalid";
              case 0xF8C0: return "Conversion table GCR to binary - low nybble $FF means invalid";
              case 0xF8E0: return "Decode 69 GCR bytes";
              case 0xF934: return "Convert block header to GCR code";
              case 0xF969: return "Error entry disk controller";
              case 0xF97E: return "Turn drive motor on";
              case 0xF98F: return "Turn drive motor off";
              case 0xF99C: return "Job loop disk controller";
              case 0xFA05: return "Move head to next track";
              case 0xFA1C: return "Calculate number of head steps";
              case 0xFA3B: return "Move stepper motor short distance";
              case 0xFA4E: return "Load head";
              case 0xFA7B: return "Prepare fast head movement";
              case 0xFA97: return "Fast head movement";
              case 0xFAA5: return "Prepare slow head movement";
              case 0xFAC7: return "Formatting";
              case 0xFDA3: return "Write SYNC 10240 times, erase track";
              case 0xFDC3: return "Read/write ($621/$622) times";
              case 0xFDD3: return "Attempt counter for formatting";
              case 0xFDF5: return "Copy data from overflow buffer";
              case 0xFE00: return "Switch to reading";
              case 0xFE0E: return "Write $55 10240 times";
              case 0xFE30: return "Convert header in buffer 0 to GCR code";
              case 0xFE67: return "Interrupt routine";
              case 0xFE85: return "Directory track";
              case 0xFE86: return "Start of bam";
              case 0xFE87: return "Length of bam per track";
              case 0xFE88: return "End of bam";
              case 0xFE89: return "Table of command words";
              case 0xFE95: return "Low  byte of command addresses";
              case 0xFEA1: return "High byte of command addresses";
              case 0xFEAD: return "Bytes for syntax check";
              case 0xFEB2: return "File control methods \"RWAM\"";
              case 0xFEB6: return "File types \"DSPUL\"";
              case 0xFEBB: return "Names of file types: 1st letter \"DSPUR\"";
              case 0xFEC0: return "Names of file types: 2nd letter \"EERSE\"";
              case 0xFEC5: return "Names of file types: 3rd letter \"LQGRL\"";
              case 0xFECA: return "Error LED bit value";
              case 0xFECD: return "Masks for bit command";
              case 0xFED1: return "Number of sectors per track";
              case 0xFED5: return "Constands for disk format";
              case 0xFED6: return "4 track ranges";
              case 0xFED7: return "Number of tracks";
              case 0xFED8: return "Tracks on which sector numbers change";
              case 0xFEDB: return "Control bytes for head postion";
              case 0xFEE6: return "ROM checksum";
              case 0xFEE7: return "From UI command $EB22, to reset";
              case 0xFEEA: return "Patch for diagnostic routine from $EA7A";
              case 0xFEF3: return "Delay loop for serial bus in 1541 mode, from $E97D";
              case 0xFEFB: return "Patch for data output to serial bus, from $E980";
              case 0xFF01: return "U9 vector, switch 1540/1541";
              case 0xFF10: return "Patch for reset routine, from $EAA4";
              case 0xFF20: return "Patch for listen to serial bus, from $E9DC";
              case 0xFF2F: return "Unused";					
              case 0xFFE6: return "Format	[C8C6]";
              case 0xFFE8: return "Turn motor off [F98F]";
              case 0xFFEA: return "UA, U1	[CD5F]";
              case 0xFFEC: return "UB, U2	[CD97]";
              case 0xFFEE: return "UC, U3	[0500]";
              case 0xFFF0: return "UD, U4	[0503]";
              case 0xFFF2: return "UE, U5	[0506]";
              case 0xFFF4: return "UF, U6	[0509]";
              case 0xFFF6: return "UG, U7	[050C]";
              case 0xFFF8: return "UH, U8	[050F]";
              case 0xFFFA: return "UI, U9	[FF01]";
              case 0xFFFC: return "RESET [EAA0]";
              case 0xFFFE: return "IRQ   [FE67]";

              default:
                if ((addr>=0x0103) && (addr<=0x0145)) return "Actual processor stack";  
                if ((addr>=0x0146) && (addr<=0x01B9)) return "Unused"; 
                if ((addr>=0x01BA) && (addr<=0x01FF)) return "Auxiliary buffer for GCR-encoding/decoding";                 
                if ((addr>=0x0200) && (addr<=0x0229)) return "Input buffer: 42 bytes (Used for accepting commands from host)";      
                if ((addr>=0x022B) && (addr<=0x023D)) return "Channel number assigned to secondary addresses"; 
                if ((addr>=0x02B1) && (addr<=0x02CB)) return "Buffer for constructing current entry (BASIC line) while LOAD'ing \"$\""; 
                if ((addr>=0x02CC) && (addr<=0x02D4)) return "Unused";  
                if ((addr>=0x02D5) && (addr<=0x02F8)) return "Error message buffer";  
                if ((addr>=0x0300) && (addr<=0x03FF)) return "Buffer #0";
                if ((addr>=0x0400) && (addr<=0x04FF)) return "Buffer #1";
                if ((addr>=0x0500) && (addr<=0x05FF)) return "Buffer #2";
                if ((addr>=0x0600) && (addr<=0x06FF)) return "Buffer #3";
                if ((addr>=0x0700) && (addr<=0x07FF)) return "Buffer #4";                
                break;
            }
        }
      break;
    }
    return super.dcom(iType, aType, addr, value);
  }
}
