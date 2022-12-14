/**
 * @(#)AtariDasm.java 2022/12/08
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
 * Comment the memory location of Atari for the disassembler
 * It performs also a multy language comments.
 *
 * @author ice
 */
public class AtariDasm extends M6510Dasm {
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
        if ((int)addr<=0xFF && !option.commentAtariZeroPage) return "";
        if ((int)addr>=0x100 && (int)addr<=0x1FF && !option.commentAtariStackArea) return "";
        if ((int)addr>=0x200 && (int)addr<=0x2FF && !option.commentAtari_200Area) return "";
        if ((int)addr>=0x300 && (int)addr<=0x3FF && !option.commentAtari_300Area) return "";
        if ((int)addr>=0x400 && (int)addr<=0x4FF && !option.commentAtari_400Area) return "";
        if ((int)addr>=0x500 && (int)addr<=0x5FF && !option.commentAtari_500Area) return "";
        if ((int)addr>=0x8000 && (int)addr<=0x9FFF && !option.commentAtariCartridgeB) return "";
        if ((int)addr>=0xA000 && (int)addr<=0xBFFF && !option.commentAtariCartridgeA) return "";
        if ((int)addr>=0xD000 && (int)addr<=0xD0FF && !option.commentAtariGtia) return "";
        if ((int)addr>=0xD200 && (int)addr<=0xD2FF && !option.commentAtariPokey) return "";
        if ((int)addr>=0xD300 && (int)addr<=0xD3FF && !option.commentAtariPia) return "";
        if ((int)addr>=0xD400 && (int)addr<=0xD4FF && !option.commentAtariAntic) return "";
        if ((int)addr>=0xD800 && (int)addr<=0xFFFF && !option.commentAtariKernalRom) return "";  
          
        switch (language) {
          case LANG_ITALIAN:
            switch ((int)addr) {
              case 0x0000:  
              case 0x0001: return "Timer VBLANK";
              case 0x0002:  
              case 0x0003: return "Vettore inizializzazione cassetta";
              case 0x0004:  
              case 0x0005: return "Puntatore RAM per il test della memoria all'avvio";
              case 0x0006: return "Registro temporaneo per dimensione RAM";
              case 0x0007: return "Registro dati per test RAM";
              case 0x0008: return "Flag partenza a caldo";
              case 0x0009: return "Indicatore successo avvio";
              case 0x000A:  
              case 0x000B: return "Vettore partenza per programmi disco (o non cartuccia)";
              case 0x000C:  
              case 0x000D: return "Indirizzo inizializzazione per avvio da disco";
              case 0x000E:  
              case 0x000F: return "Limite memoria alto per applicazioni e puntatore alla fine del programma BASIC";
              case 0x0010: return "Interruzioni POKEY";
              case 0x0011: return "Zero significa tasto BREAK premuto";
              case 0x0012:  
              case 0x0013:
              case 0x0014: return "Orologio interno in tempo reale";
              case 0x0015:
              case 0x0016: return "Registro indirizzo buffer indiretto";
              case 0x0017: return "Commando per vettore CIO";
              case 0x0018:
              case 0x0019: return "Puntatore gestore file su disco";
              case 0x001A:
              case 0x001B: return "Puntatore utilità per disco";
              case 0x001C: return "Timeout stampante";
              case 0x001D: return "Puntatore buffer stampante";
              case 0x001E: return "Dimensione buffer di stampa per un record del modo corrente";   
              case 0x001F: return "Registro temporaneo usanto dal gestore stampante";
              case 0x0020: return "Numero indice gestore";
              case 0x0021: return "Numero dispositivo o numero disco";
              case 0x0022: return "Codice commando settato dall'utente";
              case 0x0023: return "Stato dell'ultima azione IOCB ritornata dal dispositivo";
              case 0x0024:
              case 0x0025: return "Indirizzo buffer per trasferimento dati o indirizzo del nome file";
              case 0x0026:
              case 0x0027: return "Inserisce indirizzo byte della procedura settata dal sistema operativo";
              case 0x0028:
              case 0x0029: return "Contatore lunghezza buffer usato dalle operazioni PUT e GET";
              case 0x002A: return "Primo byte informazione ausiliaria";
              case 0x002B: return "Variabili lavoro CIO";
              case 0x002C:
              case 0x002D: return "Usato dai commandi BASIC NOTE e POINT per il trasferimento numeri settore del disco";
              case 0x002E: return "Il byte che sta vedendo letto nel settore";
              case 0x002F: return "Byte di riserva";
              case 0x0030: return "Memoria interna dello stato";
              case 0x0031: return "Checksum del frame di dati utilizzato da SIO";     
              case 0x0032:
              case 0x0033: return "Puntatore al buffer di dati";
              case 0x0034:
              case 0x0035: return "Byte successivo dopo la fine del buffer di dati SIO e DCB";
              case 0x0036: return "Numero di tentativi di frame di comando";
              case 0x0037: return "Numero di tentativi del dispositivo";
              case 0x0038: return "Flag di buffer dati pieno";
              case 0x0039: return "Flag di completamento per ricevi";
              case 0x003A: return "Flag di completamento per trasmetti";
              case 0x003B: return "Contrassegno checksum inviato";
              case 0x003C: return "Flag per no checksum dopo i dati";
              case 0x003D: return "Puntatore buffer cassetta";
              case 0x003E: return "Tipo di gap inter-record tra i record della cassetta";
              case 0x003F: return "Flag per fine file in cassetta";
              case 0x0040: return "Registro contenente numero segfnali acustici";
              case 0x0041: return "Flag di I/O rumore utilizzato da SIO per segnalare il segnale acustico udito durante l'I/O del disco e della cassetta";
              case 0x0042: return "Flag di regione di I/O critico";
              case 0x0043:
              case 0x0044: return "Puntatore del buffer della pagina zero al nome file dell'utente per l'I/O del disco";
              case 0x0045:     
              case 0x0046: return "Page zero drive pointer";
              case 0x0047:     
              case 0x0048: return "Puntatore unità pagina zero";
              case 0x0049: return "Numero di errore di I/O del disco";
              case 0x004A: return "Flag di richiesta di avvio della cassetta all'avvio a freddo";
              case 0x004B: return "Flag di avvio della cassetta";
              case 0x004C: return "Stato del display e registro della tastiera utilizzati dal gestore del display";
              case 0x004D: return "Timer e flag della modalità di attrazione";
              case 0x004E: return "Maschera di attrazione oscura";
              case 0x004F: return "Maschera di cambiamento di colore";
              case 0x0050: 
              case 0x0051: return "Registro temporaneo utilizzato dal gestore del display per spostare i dati da e verso lo schermo";
              case 0x0052: return "Colonna del margine sinistro del testo";
              case 0x0053: return "Margine destro della schermata di testo inizializzato a 39 ($ 27)";
              case 0x0054: return "Grafica corrente o riga del cursore dello schermo di testo";
              case 0x0055: 
              case 0x0056: return "Grafica corrente o colonna del cursore in modalità testo";
              case 0x0057: return "Modalità di visualizzazione/modalità schermo corrente";
              case 0x0058: 
              case 0x0059: return "L'indirizzo più basso della memoria dello schermo, corrispondente all'angolo in alto a sinistra dello schermo\"";
              case 0x005A: return "Riga del cursore grafica precedente";
              case 0x005B: 
              case 0x005C: return "Colonna del cursore grafica precedente";
              case 0x005D: return "Mantiene il valore del carattere sotto il cursore";
              case 0x005E: 
              case 0x005F: return "Mantiene la posizione di memoria della posizione corrente del cursore";
              case 0x0060: return "Punto (riga) a cui andranno DRAWTO e XIO 18 (FILL)";
              case 0x0061: 
              case 0x0062: return "Punto (colonna) a cui andranno DRAWTO e XIO 18 (FILL)";
              case 0x0063: return "Posizione del cursore sulla colonna in una riga logica";
              case 0x0064: 
              case 0x0065: return "Archiviazione temporanea utilizzata dal gestore di visualizzazione per l'indirizzo dell'elenco di visualizzazione";
              case 0x0066: 
              case 0x0067: return "Stoccaggio temporaneo per open e display handler";
              case 0x0068: 
              case 0x0069: return "Archiviazione temporanea per i dati sotto il cursore e la linea in movimento sullo schermo";  
              case 0x006A: return "Dimensione RAM";
              case 0x006B: return "Conteggio buffer";
              case 0x006C: 
              case 0x006D: return "Byte basso dell'editor (AM)";
              case 0x006E: return "Maschera di bit utilizzata nelle routine di mappatura dei bit dal gestore di visualizzazione del sistema operativo";
              case 0x006F: return "Giustificazione dei pixel";
              case 0x0070: return "Accumulatore di righe";
              case 0x0071: return "Accumulatore di colonna";
              case 0x0072: 
              case 0x0073: return "Controlla il tracciato dei punti di colonna";
              case 0x0074: 
              case 0x0075: return "Punto finale della linea da tracciare";
              case 0x0076: return "Riga delta";
              case 0x0077: 
              case 0x0078: return "Colonna delta";
              case 0x0079: return "Il valore di incremento o decremento della riga";
              case 0x007A: return "Il valore di incremento o decremento della colonna";
              case 0x007B: return "Controllo del cursore a schermo diviso";
              case 0x007C: return "Mantieni il carattere prima del controllo e la logica di spostamento";
              case 0x007D: return "Byte di archiviazione temporaneo utilizzato dal gestore di visualizzazione per il carattere sotto il cursore e il rilevamento di fine riga";
              case 0x007E: 
              case 0x007F: return "Numero di iterazioni necessarie per tracciare una linea";
              case 0x0080: 
              case 0x0081: return "Puntatore alla memoria insufficiente del BASIC";
              case 0x0082: 
              case 0x0083: return "Indirizzo iniziale della tabella dei nomi delle variabili";
              case 0x0084: 
              case 0x0085: return "Puntatore all'indirizzo finale della tabella dei nomi delle variabili più un byte";
              case 0x0086: 
              case 0x0087: return "Indirizzo per la tabella dei valori delle variabili";
              case 0x0088: 
              case 0x0089: return "L'indirizzo della tabella delle istruzioni";
              case 0x008A: 
              case 0x008B: return "Puntatore all'istruzione BASIC corrente";
              case 0x008C: 
              case 0x008D: return "L'indirizzo per la tabella delle stringhe e degli array e un puntatore alla fine del programma BASIC";
              case 0x008E: 
              case 0x008F: return "Indirizzo dello stack di runtime";
              case 0x0090: 
              case 0x0091: return "Puntatore all'inizio della memoria BASIC";
              
              case 0x00BA: 
              case 0x00BB: return "La riga in cui è stato interrotto un programma";
              case 0x00C3: return "Il numero del codice di errore che ha causato l'arresto o il TRAP";
              case 0x00C9: return "Il numero di colonne tra i punti TAB";
              case 0x00D2: 
              case 0x00D3: return "Riservato al BASIC o ad altri usi della cartuccia";
              case 0x00D4: 
              case 0x00D5: 
              case 0x00D6: 
              case 0x00D7:
              case 0x00D8: 
              case 0x00D9: return "Registro in virgola mobile zero";
              case 0x00DA: 
              case 0x00DB: 
              case 0x00DC: 
              case 0x00DD:
              case 0x00DE: 
              case 0x00DF: return "Registro extra in virgola mobile";
              case 0x00E0: 
              case 0x00E1: 
              case 0x00E2: 
              case 0x00E3:
              case 0x00E4: 
              case 0x00E5: return "Registro in virgola mobile uno";
              case 0x00E6: 
              case 0x00E7: 
              case 0x00E8: 
              case 0x00E9:
              case 0x00EA: 
              case 0x00EB: return "Registro in virgola mobile due";
              case 0x00EC: return "Registro di riserva in virgola mobile";
              case 0x00ED: return "Il valore di E (l'esponente)";
              case 0x00EE: return "Il segno del numero in virgola mobile";
              case 0x00EF: return "Il segno dell'esponente";
              case 0x00F0: return "Il primo indicatore di caratteri";
              case 0x00F1: return "Il numero di cifre a destra del decimale";
              case 0x00F2: return "Indice carattere (input corrente)";
              case 0x00F3: 
              case 0x00F4: return "Immettere il puntatore del buffer di testo ASCII";
              case 0x00F5:
              case 0x00F6:    
              case 0x00F7:
              case 0x00F8:    
              case 0x00F9:    
              case 0x00FA: return "Registro provvisorio";    
              case 0x00FB: return "Flag radianti/gradi";
              case 0x00FC:    
              case 0x00FD: return "Punta al numero in virgola mobile dell'utente";
              case 0x00FE:    
              case 0x00FF: return "Puntatore al secondo numero in virgola mobile dell'utente";
              
              case 0x0200:    
              case 0x0201: return "Vettore per NMI Display List Interrupts (DLI)";
              case 0x0202:    
              case 0x0203: return "Vettore di linea procedente seriale (periferico)";
              case 0x0204:    
              case 0x0205: return "Vettore di interrupt seriale (periferico)";
              case 0x0206:    
              case 0x0207: return "Vettore di istruzioni di interruzione del software per il 6502 BRK";
              case 0x0208:    
              case 0x0209: return "Vettore di interruzione della tastiera POKEY";
              case 0x020A:    
              case 0x020B: return "Il bus I/O seriale POKEY riceve il vettore di interrupt pronto per i dati";
              case 0x020C:    
              case 0x020D: return "I/O seriale POKEY trasmette il vettore di interrupt pronto";
              case 0x020E:    
              case 0x020F: return "Il bus seriale POKEY trasmette un vettore di interrupt completo";
              case 0x0210:    
              case 0x0211: return "POKEY timer 1 vettore di interruzione";
              case 0x0212:    
              case 0x0213: return "POKEY timer 2 vettori per AUDF2";
              case 0x0214:    
              case 0x0215: return "POKEY timer 4 vettori per AUDF4";
              case 0x0216:    
              case 0x0217: return "Il vettore immediato IRQ (generale)";
              case 0x0218:    
              case 0x0219: return "Timer sistema 1";
              case 0x021A:    
              case 0x021B: return "Timer sistema 2";
              case 0x021C:    
              case 0x021D: return "Timer sistema 3";
              case 0x021E:    
              case 0x021F: return "Timer sistema 4";
              case 0x0220:    
              case 0x0221: return "Timer sistema 5";
              case 0x0222:    
              case 0x0223: return "VBLANK registro immediato";
              case 0x0224:    
              case 0x0225: return "Registro differito VBLANK";
              case 0x0226:    
              case 0x0227: return "Timer di sistema 1 indirizzo di salto";
              case 0x0228:    
              case 0x0229: return "Timer di sistema 2 indirizzo di salto";
              case 0x022A: return "Timer di sistema flag 3";
              case 0x022B: return "Timer di ripetizione software, controllato dalla routine del dispositivo IRQ";
              case 0x022C: return "Timer di sistema flag 4";
              case 0x022D: return "Registro temporaneo utilizzato dal SETVBL";
              case 0x022E: return "Timer di sistema flag 5";
              case 0x022F: return "Abilita l'accesso diretto alla memoria (DMA)";
              case 0x0230:    
              case 0x0231: return "Indirizzo iniziale dell'elenco di visualizzazione";
              case 0x0232: return "Registro di controllo della porta seriale";
              case 0x0233: return "Byte di riserva (nessun utilizzo del sistema operativo)";             
              case 0x0234: return "Valore orizzontale penna ottica: ombra per  54284 ($D40C)";
              case 0x0235: return "Valore verticale penna ottica: ombra per 54285 ($D40D)";
              case 0x0236:    
              case 0x0237: return "Vettore di interruzione del tasto BREAK";
              case 0x0238: 
              case 0x0239: return "Byte di riserva";
              case 0x023A: return "Indirizzo CFB (Command Frame Buffer) a quattro byte per un dispositivo";
              case 0x023B: return "Il codice di comando del bus SIO";
              case 0x023C: return "Comando byte ausiliario uno";
              case 0x023D: return "Comando byte ausiliario due";
              case 0x023E: return "Registro RAM temporaneo per SIO";
              case 0x023F: return "Indicatore di errore SIO";
              case 0x0240: return "Flag del disco letti dal primo byte del file di avvio (settore uno) del disco";
              case 0x0241: return "Il numero di settori di avvio del disco letti dal primo record del disco";
              case 0x0242: 
              case 0x0243: return "L'indirizzo in cui verrà inserito il caricatore di avvio del disco";
              case 0x0244: return "Flag partenza a freddo";
              case 0x0245: return "Byte di riserva";
              case 0x0246: return "Registro del timeout del disco";
              case 0x026F: return "Registro di selezione prioritaria, shadow per 53275 ($D01B)";
              case 0x0270: return "Il valore del joystick paddle 0";
              case 0x0271: return "Il valore del joystick paddle 1";
              case 0x0272: return "Il valore del joystick paddle 2";
              case 0x0273: return "Il valore del joystick paddle 3";
              case 0x0274: return "Il valore del joystick paddle 4";
              case 0x0275: return "Il valore del joystick paddle 5";
              case 0x0276: return "Il valore del joystick paddle 6";
              case 0x0277: return "Il valore del joystick paddle 7";
              case 0x0278: return "Il valore del joystick 0";
              case 0x0279: return "Il valore del joystick 1";
              case 0x027A: return "Il valore del joystick 2";
              case 0x027B: return "Il valore del joystick 3";
              case 0x027C: return "Paddle trigger 0";
              case 0x027D: return "Paddle trigger 1";
              case 0x027E: return "Paddle trigger 2";
              case 0x027F: return "Paddle trigger 3";
              case 0x0280: return "Paddle trigger 4";
              case 0x0281: return "Paddle trigger 5";
              case 0x0282: return "Paddle trigger 6";
              case 0x0283: return "Paddle trigger 7";
              case 0x0284: return "Stick trigger 0";
              case 0x0285: return "Stick trigger 1";
              case 0x0286: return "Stick trigger 2";
              case 0x0287: return "Stick trigger 3";
              case 0x0288: return "Registro di stato della cassetta";
              case 0x0289: return "Memorizza la modalità di lettura o scrittura per il gestore di cassette";
              case 0x028A: return "Dimensioni del buffer del record di dati della cassetta";
              case 0x028B: 
              case 0x028C:    
              case 0x028D:   
              case 0x028E:    
              case 0x028F: return "Byte di riserva";
              case 0x0290: return "Riga del cursore della finestra di testo";
              case 0x0291:    
              case 0x0292: return "Colonna del cursore della finestra di testo";
              case 0x0293: return "Modo GRAPHICS per la finestra di testo nel split-screen corrente";
              case 0x0294:    
              case 0x0295: return "Indirizzo dell'angolo in alto a sinistra della finestra di testo";
              case 0x0296: return "Split-screen: riga del cursore grafica precedente";
              case 0x0297: 
              case 0x0298: return "Split-screen: colonna del cursore grafica precedente";
              case 0x0299: return "Split-screen: mantiene il valore del carattere sotto il cursore";
              case 0x029A: 
              case 0x029B: return "Split-screen: conserva la posizione di memoria della posizione corrente del cursore";
              case 0x029C: return "Registro temporaneo (gestore di visualizzazione)";
              case 0x029D:
              case 0x029E:    
              case 0x029F: return "Registro provvisorio";
              case 0x02A0: return "Maschera di posizione dei pixel";
              case 0x02A1: return "Memoria temporanea per la maschera di bit";              
              case 0x02A2: return "Flag per Escape";
              case 0x02B2:    
              case 0x02B3: 
              case 0x02B4:     
              case 0x02B5: return "Mappa di bit di inizio linea logica";
              case 0x02B6: return "Flag di caratteri inversi";
              case 0x02B7: return "Flag di riempimento a destra per il comando DRAW";
              case 0x02B8: return "Registro temporaneo per riga utilizzato da ROWCRS";
              case 0x02B9:     
              case 0x02BA: return "Registro temporaneo per la colonna utilizzata da COLCRS";
              case 0x02BB: return "Flag scorrimento";
              case 0x02BC: 
              case 0x02BD: return "Registro temporaneo utilizzato solo nel comando DRAW";
              case 0x02BE: return "Flag per i tasti shift e control";
              case 0x02BF: return "Flag per il numero di righe di testo disponibili per la stampa";
              case 0x02C0: return "Colore del player 0 e del missile 3 0";
              case 0x02C1: return "Colore del player 1 e del missile 3 1";
              case 0x02C2: return "Colore del player 2 e del missile 3 2";
              case 0x02C3: return "Colore del player 3 e del missile 3";
              case 0x02C4: return "Registro dei colori zero";
              case 0x02C5: return "Registro dei colori uno";
              case 0x02C6: return "Registro dei colori due";
              case 0x02C7: return "Registro dei colori tre";
              case 0x02C8: return "Registro dei colori per lo sfondo (BAK) e il bordo";
              case 0x02D9: return "Tasso di ritardo automatico";
              case 0x02DA: return "Il tasso di ripetizione";
              case 0x02DB: return "Clic sulla tastiera disabilita il registro";
              case 0x02DC: return "Aiuta la registrazione della chiave";
              case 0x02DE: return "Puntatore del buffer di stampa";
              case 0x02DF: return "Dimensioni del buffer di stampa";
              case 0x02E0: 
              case 0x02E1: return "Eseguire l'indirizzo letto dal settore del disco uno";
              case 0x02E2: 
              case 0x02E3: return "Indirizzo di inizializzazione letto dal disco";
              case 0x02E4: return "Dimensione RAM, solo byte alto";
              case 0x02E5:
              case 0x02E6: return "Puntatore all'inizio della memoria libera utilizzata sia dal BASIC che dal sistema operativo";
              case 0x02E7:
              case 0x02E8: return "Puntatore alla parte inferiore della memoria libera";
              case 0x02E9: return "Byte di riserva";
              case 0x02EA: return "Stato di errore del dispositivo e byte di stato del comando";
              case 0x02EB: return "Byte di stato del dispositivo";
              case 0x02EC: return "Valore massimo di timeout del dispositivo in secondi";
              case 0x02ED: return "Numero di byte nel buffer di output";
              case 0x02EE: return "Velocità di trasmissione della cassetta bassa";
              case 0x02EF: return "Velocità di trasmissione della cassetta alto";
              case 0x02F0: return "Flag di inibizione del cursore";
              case 0x02F1: return "Indicatore di ritardo tasto o contatore antirimbalzo tasto";
              case 0x02F2: return "Codice dei caratteri della tastiera precedente";
              case 0x02F3: return "Registro modalità carattere";
              case 0x02F4: return "Registro base caratteri";
              case 0x02F5: 
              case 0x02F6: 
              case 0x02F7: 
              case 0x02F8:    
              case 0x02F9: return "Byte di riserva";
              case 0x02FA: return "Valore del codice interno per l'ultimo carattere letto o scritto";
              case 0x02FB: return "Ultimo carattere ATASCII letto o scritto o il valore di un punto graficot";
              case 0x02FC: return "Internal hardware value for the last key pressed";
              case 0x02FD: return "Dati colore per l'area di riempimento nel comando XIO FILL";
              case 0x02FE: return "Flag schermo";
              case 0x02FF: return "Flag della schermata di visualizzazione di avvio/arresto";
              
              case 0x0300: return "ID del bus seriale del dispositivo";
              case 0x0301: return "Numero di unità del disco o del dispositivo";
              case 0x0302: return "Il numero dell'operazione del disco o del dispositivo";
              case 0x0303: return "Il codice di stato al ritorno all'utente";
              case 0x0304: 
              case 0x0305: return "Indirizzo del buffer di dati dell'origine o della destinazione dei dati da trasferire o delle informazioni sullo stato del dispositivo";
              case 0x0306: return "Il valore di timeout per il gestore in unità di un secondo";
              case 0x0307: return "Byte inutilizzato";
              case 0x0308: 
              case 0x0309: return "Il numero di byte trasferiti da o verso il buffer di dati";
              case 0x030A: 
              case 0x030B: return "Utilizzato per informazioni specifiche del dispositivo come il numero del settore del disco per l'operazione di lettura o scrittura";
              case 0x030C: 
              case 0x030D: return "Valore iniziale del timer della velocità di trasmissione";
              case 0x030E: return "Flag di correzione dell'aggiunta per i calcoli della velocità di trasmissione che coinvolgono i registri del timer";
              case 0x030F: return "Modalità cassetta quando impostata";
              case 0x0310: 
              case 0x0311: return "Valore finale del timer";
              case 0x0312: 
              case 0x0313: return "Registro di conservazione temporanea utilizzato da SIO";
              case 0x0314: return "Registro della custodia temporanea";
              case 0x0315: return "Ditto";
              case 0x0316: return "Salva la porta di ingresso dati seriale utilizzata per rilevare e aggiornare dopo l'arrivo di ogni bit";
              case 0x0317: return "Flag di timeout per la correzione della velocità di trasmissione";
              case 0x0318: return "Registro del puntatore dello stack SIO";
              case 0x0319: return "Titolare dello stato temporaneo per la posizione 48 ($30)";
              case 0x03E8: return "Flag per i tasti funzione";
              case 0x03E9: return "Flag di richiesta di avvio della cassetta partenza a freddo";
              case 0x03EA: return "Flag di avvio della cassetta";
              
              case 0x057E: return "LBUFF prefisso uno";
              case 0x057F: return "LBUFF prefisso due";
              case 0x05E0: return "Argomenti polinomiali";
              
              
                
              // Right cartridge B:
              case 0x9FFA: return "Cartuccia B: indirizzo iniziale basso";
              case 0x9FFB: return "Cartuccia B: indirizzo iniziale alto";
              case 0x9FFC: return "Cartuccia B: presente?";
              case 0x9FFD: return "Cartuccia B: byte di opzione";
              case 0x9FFE: return "Cartuccia B: indirizzo di inizializzazione basso";
              case 0x9FFF: return "Cartuccia B: indirizzo di inizializzazione alto";  
                
              // Left cartridge A:
              case 0xA8E2: return "Cartuccia A: mostra la revisione del BASIC";
              case 0xBFFA: return "Cartuccia A: indirizzo iniziale basso";
              case 0xBFFB: return "Cartuccia A: indirizzo iniziale alto";
              case 0xBFFC: return "Cartuccia A: presente?";
              case 0xBFFD: return "Cartuccia A: byte di opzione";
              case 0xBFFE: return "Cartuccia A: indirizzo di inizializzazione basso";
              case 0xBFFF: return "Cartuccia A: indirizzo di inizializzazione alto";              
                
              // gta
              case 0xD000: return "Collisione del missile 0 con il campo di gioco | Posizione orizzontale del player 0";
              case 0xD001: return "Collisione del missile 1 con il campo di gioco | Posizione orizzontale del player 1";
              case 0xD002: return "Collisione del missile 2 con il campo di gioco | Posizione orizzontale del player 2";
              case 0xD003: return "Collisione del missile 3 con il campo di gioco | Posizione orizzontale del player 3";
              case 0xD004: return "Collisione del Player 0 con il campo di gioco | Posizione orizzontale del missile 0";
              case 0xD005: return "Collisione del Player 1 con il campo di gioco | Posizione orizzontale del missile 1";
              case 0xD006: return "Collisione del Player 2 con il campo di gioco | Posizione orizzontale del missile 2";
              case 0xD007: return "Collisione del Player 3 con il campo di gioco | Posizione orizzontale del missile 3";
              case 0xD008: return "Collisione Missile 0 con Player | Player di dimensioni 0";
              case 0xD009: return "Collisione Missile 1 con Player | Player di dimensioni 1";
              case 0xD00A: return "Collisione Missile 2 con Player | Player di dimensioni 2";
              case 0xD00B: return "Collisione Missile 3 con Player | Player di dimensioni 3";
              case 0xD00C: return "Collisione Player 0 con Player x | Dimensione dei missili 0-3";
              case 0xD00D: return "Collision Player 1 con Player | Graphics Shape for Player 0";
              case 0xD00E: return "Collisione Player 2 con Player | Forma grafica per il giocatore 1";
              case 0xD00F: return "Collisione Player 3 con Player | Forma grafica per il giocatore 2";
              case 0xD010: return "Trigger 0 | Forma grafica per il giocatore 3";
              case 0xD011: return "Trigger 1 | Forma grafica per Missile 0-3";
              case 0xD012: return "Trigger Joystick 2 | Colore del giocatore e del missile 0";
              case 0xD013: return "Trigger Joystick 3 | Colore del giocatore e del missile 1";
              case 0xD014: return "Determina il sistema TV | Colore del giocatore e del missile 2";
              case 0xD015: return "Colore del giocatore e del missile 3";
              case 0xD016: return "Registro dei colori 0";
              case 0xD017: return "Registro dei colori 1";
              case 0xD018: return "Registro dei colori 2";
              case 0xD019: return "Registro dei colori 3";
              case 0xD01A: return "Registro dei colori di sfondo";
              case 0xD01B: return "Registro di selezione prioritaria";
              case 0xD01C: return "Ritardo verticale per PM";
              case 0xD01D: return "Controlla PM e Trigger";
              case 0xD01E: return "Cancella tutti i registri di collisione";
              case 0xD01F: return "Tasti consolle e altoparlante interno (400/800)";
                
              // pokey
              case 0xD200: return "Pot/paddle 0 | Frequenza del canale audio 1";
              case 0xD201: return "Pot/paddle 1 | Controllo del canale audio 1";
              case 0xD202: return "Pot/paddle 2 | Frequenza del canale audio 2";
              case 0xD203: return "Pot/paddle 3 | Controllo del canale audio 2";
              case 0xD204: return "Pot/paddle 4 | Frequenza del canale audio 3";
              case 0xD205: return "Pot/paddle 5 | Controllo del canale audio 3";
              case 0xD206: return "Pot/paddle 6 | Frequenza del canale audio 4";
              case 0xD207: return "Pot/paddle 7 | Controllo del canale audio 4";
              case 0xD208: return "Controllo audio | Stato del porto di Pot";
              case 0xD20A: return "Generatore di numeri casuali | Ripristino registro stato seriale";
              case 0xD20B: return "Inizia la sequenza di lettura POT";
              case 0xD20C: return "Non usato";
              case 0xD20D: return "Uscita dati porta seriale | Ingresso dati porta seriale";
              case 0xD20E: return "Attiva richiesta di interruzione | Stato della richiesta di interruzione";
              case 0xD20F: return "Controllo della porta seriale | Stato porta seriale";
              
              // pia
              case 0xD300: return "Porta del joystick A";
              case 0xD301: return "Porta del joystick B";
              case 0xD302: return "Controllo della porta A";
              case 0xD303: return "Controllo della porta B";
              
              // antic
              case 0xD400: return "Controllo dell'accesso diretto alla memoria (DMA)";
              case 0xD401: return "Visualizzazione del cursore e dei caratteri";
              case 0xD402: 
              case 0xD403: return "Indirizzo iniziale dell'elenco di visualizzazione";
              case 0xD404: return "Scorrimento fine orizzontale";
              case 0xD405: return "Scorrimento fine verticale";
              case 0xD406: return "Non usato";
              case 0xD407: return "Indirizzo database PM (MSB)";
              case 0xD408: return "Non usato";
              case 0xD409: return "Indirizzo del set di caratteri";
              case 0xD40A: return "Arresta la CPU fino al raggiungimento della fine della linea di scansione";
              case 0xD40B: return "Linea di scansione corrente/2";
              case 0xD40C: return "Posizione orizzontale delle penne ottiche";
              case 0xD40D: return "Posizione verticale delle penne ottiche";
              case 0xD40E: return "Abilita interruzione non mascherabile";
              case 0xD40F: return "Reset/Stato interruzione non mascherabile";
              
              // OS ROM
              case 0xD800: return "Conversione da ASCII a numero in virgola mobile";
              case 0xD8E6: return "Conversine da numero in virgola mobile ad ASCII";
              case 0xD9AA: return "Conversione da intero a numero in virgola mobile";
              case 0xD9D2: return "Conversione numero virgola mobile in intero";
              case 0xDA44: return "Cancella FR0 da 212 a 217 ($d$-$DB)";
              case 0xDA46: return "Azzera il numero in virgola mobile Clear da FR1, locazioni da 224 a 229 ($E0..$E5)";
              case 0xDA60: return "Routine sottrazione in virgola mobile, il valore in FR0 meno il valore in FR1";
              case 0xDA66: return "Routine addizione in virgola mobile; FR0 più FR1";
              case 0xDADB: return "Routine moltiplicazione in virgola mobile; FR0 per FR1";
              case 0xDB28: return "Routine divisione in virgola mobile; FR0 diviso FR1";
              case 0xDD40: return "Valutazione polinomiale numero in virgola mobile";
              case 0xDD89: return "Carica il numero FP in FR0 dai registri 6502 X,Y";
              case 0xDD8D: return "Carica il numero FP in FR0 dalla routine utente, usando FLPTR a 252 ($FC)";
              case 0xDD98: return "Caricare il numero FP in FR1 dai registri 6502 X,Y";
              case 0xDD9C: return "Caricare il numero FP in FR1 dal programma utente, utilizzando FLPTR";
              case 0xDDA7: return "Memorizza il numero FP nei registri 6502 X,Y da FR0";
              case 0xDDAB: return "Memorizza il numero FP da FR0, utilizzando FLPTR";
              case 0xDDB6: return "Spostare il numero FP da FR0 a FR1";
              case 0xDDC0: return "Esponenziale in base e numero birgola mobile";
              case 0xDDCC: return "Esponenziale in base numero virgola mobile";
              case 0xDDCD: return "Logaritmo naturale numero in virgola mobile";
              case 0xDED1: return "Logaritmo in base 10 numero in virgola mobile";
              case 0xE400: return "Editor di schermo (E:) tabella dei punti di ingresso";
              case 0xE410: return "Gestore display (schermo televisivo) (S:)";
              case 0xE420: return "Tabella di salto per il driver della tastiera K:";
              case 0xE430: return "Gestore della stampante (P:)";
              case 0xE440: return "Gestore cassette (C:)";
              case 0xE450: return "Vettore di inizializzazione del gestore del disco, inizializzato su 60906 ($EDEA)";
              case 0xE453: return "Voce del gestore del disco (interfaccia); controlla lo stato del disco. Inizializzato a 60912 ($EDF0)";
              
              case 0xE45C: return "Imposta timer di sistema e vettori di interruzione";
              case 0xE45F: return "Entrata dei calcoli VBLANK della prima fase";
              case 0xE462: return "Uscire dalla routine VBLANK";
              case 0xE465: return "Inizializzazione dell'utilità SIO, solo uso del sistema operativo";
              case 0xE468: return "Invia routine di abilitazione, solo uso del sistema operativo";
              case 0xE46B: return "Inizializzazione del gestore di interrupt, solo uso del sistema operativo";
              case 0xE46E: return "Inizializzazione dell'utility CIO, solo uso del sistema operativo";
              case 0xE471: return "Ingresso modalità lavagna";
              case 0xE474: return "Vettore partenza a caldo";
              case 0xE477: return "Vettore partenza a freddo";
              case 0xE47A: return "Legge i blocchi dal C:";
              case 0xE47D: return "Canali aperti per C:";
              case 0xE480: return "Vettore per il Self Test";
              case 0xE7AE: 
              case 0xE7D1: return "Routine VBLANK";     
              default:
                if ((addr>=0x100) && (addr<=0x1FF)) return "Stack CPU";  
                if ((addr>=0x247) && (addr<=0x26E)) return "Buffer della riga dei caratteri";  
                if ((addr>=0x2A3) && (addr<=0x2B1)) return "Mappa delle posizioni dei TAB stop";  
                if ((addr>=0x2C9) && (addr<=0x2DF)) return "Byte di riserva";
                if ((addr>=0x31A) && (addr<=0x33F)) return "Tabella degli indirizzi del gestore";
                if ((addr>=0x340) && (addr<=0x34F)) return "Blocco di controllo I/O (IOCB) zero";
                if ((addr>=0x350) && (addr<=0x35F)) return "Blocco di controllo I/O (IOCB) one";
                if ((addr>=0x360) && (addr<=0x36F)) return "Blocco di controllo I/O (IOCB) due";
                if ((addr>=0x370) && (addr<=0x37F)) return "Blocco di controllo I/O (IOCB) tre";
                if ((addr>=0x380) && (addr<=0x38F)) return "Blocco di controllo I/O (IOCB) quattro";
                if ((addr>=0x390) && (addr<=0x39F)) return "Blocco di controllo I/O (IOCB) cinque";
                if ((addr>=0x3A0) && (addr<=0x3AF)) return "Blocco di controllo I/O (IOCB) sei";
                if ((addr>=0x3B0) && (addr<=0x3BF)) return "Blocco di controllo I/O (IOCB) sette";
                if ((addr>=0x3C0) && (addr<=0x3E7)) return "Buffer stampante";
                if ((addr>=0x3FD) && (addr<=0x47F)) return "Biffer cassetta";
                if ((addr>=0x580) && (addr<=0x5E5)) return "Linea al buffer BASIC";
                if ((addr>=0x5E6) && (addr<=0x5FF)) return "Uso del blocco per appunti in virgola mobile";  
            }  
            
          case LANG_ENGLISH:
            switch ((int)addr) {
              case 0x0000:  
              case 0x0001: return "VBLANK timer";
              case 0x0002:  
              case 0x0003: return "Cassette initialization vector";
              case 0x0004:  
              case 0x0005: return "RAM pointer for the memory test used on powerup";
              case 0x0006: return "Temporary Register for RAM size";
              case 0x0007: return "RAM test data register";
              case 0x0008: return "Warmstart flag";
              case 0x0009: return "Boot flag success indicator";
              case 0x000A:  
              case 0x000B: return "Start vector for disk (or non-cartridge) software";
              case 0x000C:  
              case 0x000D: return "Initialization address for the disk boot";
              case 0x000E:  
              case 0x000F: return "Applications memory high limit and pointer to the end of your BASIC program";
              case 0x0010: return "POKEY interrupts";
              case 0x0011: return "Zero means the BREAK key is pressed";
              case 0x0012:  
              case 0x0013:
              case 0x0014: return "Internal realtime clock";
              case 0x0015:
              case 0x0016: return "Indirect buffer address register";
              case 0x0017: return "Command for CIO vector";
              case 0x0018:
              case 0x0019: return "Disk file manager pointer";
              case 0x001A:
              case 0x001B: return "The disk utilities pointer";
              case 0x001C: return "Printer timeout";
              case 0x001D: return "Print buffer pointer";
              case 0x001E: return "Print buffer size of printer record for current mode";   
              case 0x001F: return "Temporary register used by the printer handler";
              case 0x0020: return "Handler index number";
              case 0x0021: return "Device number or drive number";
              case 0x0022: return "Command code byte set by the user";
              case 0x0023: return "Status of the last IOCB action returned by the device";
              case 0x0024:
              case 0x0025: return "Buffer address for data transfer or the address of the file name";
              case 0x0026:
              case 0x0027: return "Put byte routine address set by the OS";
              case 0x0028:
              case 0x0029: return "Buffer length byte count used for PUT and GET operations";
              case 0x002A: return "Auxiliary information first byte";
              case 0x002B: return "CIO working variables";
              case 0x002C:
              case 0x002D: return "Used by BASIC NOTE and POINT commands for the transfer of disk sector numbers";
              case 0x002E: return "The byte being accessed within the sector";
              case 0x002F: return "Spare byte";
              case 0x0030: return "Internal status storage";
              case 0x0031: return "Data frame checksum used by SIO";     
              case 0x0032:
              case 0x0033: return "Pointer to the data buffer";
              case 0x0034:
              case 0x0035: return "Next byte past the end of the SIO and DCB data buffer";
              case 0x0036: return "Number of command frame retries";
              case 0x0037: return "Number of device retries";
              case 0x0038: return "Data buffer full flag";
              case 0x0039: return "Receive done flag";
              case 0x003A: return "Transmission done flag";
              case 0x003B: return "Checksum sent flag";
              case 0x003C: return "Flag for no checksum follows data";
              case 0x003D: return "Cassette buffer pointer";
              case 0x003E: return "Inter-record gap type between cassette records";
              case 0x003F: return "Cassette end of file flag";
              case 0x0040: return "Beep count retain register";
              case 0x0041: return "Noisy I/O flag used by SIO to signal the beeping heard during disk and cassette I/O";
              case 0x0042: return "Critical I/O region flag";
              case 0x0043:
              case 0x0044: return "Page zero buffer pointer to the user filename for disk I/O";
              case 0x0045:     
              case 0x0046: return "Page zero drive pointer";
              case 0x0047:     
              case 0x0048: return "Zero page sector buffer pointer";
              case 0x0049: return "Disk I/O error number";
              case 0x004A: return "Cassette boot request flag on coldstart";
              case 0x004B: return "Cassette boot flag";
              case 0x004C: return "Display status and keyboard register used by the display handler";
              case 0x004D: return "Attract mode timer and flag";
              case 0x004E: return "Dark attract mask";
              case 0x004F: return "Color shift mask";
              case 0x0050: 
              case 0x0051: return "Temporary register used by the display handler in moving data to and from screen";
              case 0x0052: return "Column of the left margin of text";
              case 0x0053: return "Right margin of the text screen initialized to 39 ($27)";
              case 0x0054: return "Current graphics or text screen cursor row";
              case 0x0055: 
              case 0x0056: return "Current graphics or text mode cursor column";
              case 0x0057: return "Display mode/current screen mode";
              case 0x0058: 
              case 0x0059: return "The lowest address of the screen memory, corresponding to the upper left corner of the screen";
              case 0x005A: return "Previous graphics cursor row";
              case 0x005B: 
              case 0x005C: return "Previous graphics cursor column";
              case 0x005D: return "Retains the value of the character under the cursor";
              case 0x005E: 
              case 0x005F: return "Retains the memory location of the current cursor location";
              case 0x0060: return "Point (row) to which DRAWTO and XIO 18 (FILL) will go";
              case 0x0061: 
              case 0x0062: return "Point (column) to which DRAWTO and XIO 18 (FILL) will go";
              case 0x0063: return "Position of the cursor at the column in a logical line";
              case 0x0064: 
              case 0x0065: return "Temporary storage used by the display handler for the Display List address";
              case 0x0066: 
              case 0x0067: return "Temporary storage for open and sisplay handler";
              case 0x0068: 
              case 0x0069: return "Temporary storage for data under the cursor and moving line on the screen";  
              case 0x006A: return "RAM size";
              case 0x006B: return "Buffer count";
              case 0x006C: 
              case 0x006D: return "Editor low byte (AM)";
              case 0x006E: return "Bit mask used in bit mapping routines by the OS display handler";
              case 0x006F: return "Pixel justification";
              case 0x0070: return "Row accumulator";
              case 0x0071: return "Column accumulator";
              case 0x0072: 
              case 0x0073: return "Controls column point plotting";
              case 0x0074: 
              case 0x0075: return "End point of the line to be drawn";
              case 0x0076: return "Delta row";
              case 0x0077: 
              case 0x0078: return "Delta column";
              case 0x0079: return "The row increment or decrement value";
              case 0x007A: return "The column increment or decrement value";
              case 0x007B: return "Split-screen cursor control";
              case 0x007C: return "Hold character before the control and shift logic";
              case 0x007D: return "Temporary storage byte used by the display handler for the character under the cursor and end of line detection";
              case 0x007E: 
              case 0x007F: return "Number of iterations required to draw a line";
              case 0x0080: 
              case 0x0081: return "Pointer to BASIC's low memory";
              case 0x0082: 
              case 0x0083: return "Beginning address of the variable name table";
              case 0x0084: 
              case 0x0085: return "Pointer to the ending address of the variable name table plus one byte";
              case 0x0086: 
              case 0x0087: return "Address for the variable value table";
              case 0x0088: 
              case 0x0089: return "The address of the statement table";
              case 0x008A: 
              case 0x008B: return "Current BASIC statement pointer";
              case 0x008C: 
              case 0x008D: return "The address for the string and array table and a pointer to the end of your BASIC program";
              case 0x008E: 
              case 0x008F: return "Address of the runtime stack";
              case 0x0090: 
              case 0x0091: return "Pointer to the top of BASIC memory";
              
              case 0x00BA: 
              case 0x00BB: return "The line where a program was stopped";
              case 0x00C3: return "The number of the error code that caused the stop or the TRAP";
              case 0x00C9: return "The number of columns between TAB stops";
              case 0x00D2: 
              case 0x00D3: return "Reserved for BASIC or other cartridge use";
              case 0x00D4: 
              case 0x00D5: 
              case 0x00D6: 
              case 0x00D7:
              case 0x00D8: 
              case 0x00D9: return "Floating point register zero";
              case 0x00DA: 
              case 0x00DB: 
              case 0x00DC: 
              case 0x00DD:
              case 0x00DE: 
              case 0x00DF: return "Floating point extra register";
              case 0x00E0: 
              case 0x00E1: 
              case 0x00E2: 
              case 0x00E3:
              case 0x00E4: 
              case 0x00E5: return "Floating point register one";
              case 0x00E6: 
              case 0x00E7: 
              case 0x00E8: 
              case 0x00E9:
              case 0x00EA: 
              case 0x00EB: return "Floating point register two";
              case 0x00EC: return "Floating point spare register";
              case 0x00ED: return "The value of E (the exponent)";
              case 0x00EE: return "The sign of the FP number";
              case 0x00EF: return "The sign of the exponent";
              case 0x00F0: return "The first character flag";
              case 0x00F1: return "The number of digits to the right of the decimal";
              case 0x00F2: return "Character (current input) index";
              case 0x00F3: 
              case 0x00F4: return "Input ASCII text buffer pointer";
              case 0x00F5:
              case 0x00F6:    
              case 0x00F7:
              case 0x00F8:    
              case 0x00F9:    
              case 0x00FA: return "Temporary register";    
              case 0x00FB: return "Radian/degree flag";
              case 0x00FC:    
              case 0x00FD: return "Points to the user's FP number";
              case 0x00FE:    
              case 0x00FF: return "Pointer to the user's second FP number";
              
              case 0x0200:    
              case 0x0201: return "Vector for NMI Display List Interrupts (DLI)";
              case 0x0202:    
              case 0x0203: return "Serial (peripheral) proceed line vector";
              case 0x0204:    
              case 0x0205: return "Serial (peripheral) interrupt vector";
              case 0x0206:    
              case 0x0207: return "Software break instruction vector for the 6502 BRK";
              case 0x0208:    
              case 0x0209: return "POKEY keyboard interrupt vector";
              case 0x020A:    
              case 0x020B: return "POKEY serial I/O bus receive data ready interrupt vector";
              case 0x020C:    
              case 0x020D: return "POKEY serial I/O transmit ready interrupt vector";
              case 0x020E:    
              case 0x020F: return "POKEY serial bus transmit complete interrupt vector";
              case 0x0210:    
              case 0x0211: return "POKEY timer one interrupt vector";
              case 0x0212:    
              case 0x0213: return "POKEY timer two vector for AUDF2";
              case 0x0214:    
              case 0x0215: return "POKEY timer four vector for AUDF4";
              case 0x0216:    
              case 0x0217: return "The IRQ immediate vector (general)";
              case 0x0218:    
              case 0x0219: return "System timer one value";
              case 0x021A:    
              case 0x021B: return "System timer two";
              case 0x021C:    
              case 0x021D: return "System timer three";
              case 0x021E:    
              case 0x021F: return "System timer four";
              case 0x0220:    
              case 0x0221: return "System timer five";
              case 0x0222:    
              case 0x0223: return "VBLANK immediate register";
              case 0x0224:    
              case 0x0225: return "VBLANK deferred register";
              case 0x0226:    
              case 0x0227: return "System timer one jump address";
              case 0x0228:    
              case 0x0229: return "System timer two jump address";
              case 0x022A: return "System timer three flag";
              case 0x022B: return "Software repeat timer, controlled by the IRQ device routine";
              case 0x022C: return "System timer four flag";
              case 0x022D: return "Temporary register used by the SETVBL";
              case 0x022E: return "System timer five flag";
              case 0x022F: return "Direct Memory Access (DMA) enable";
              case 0x0230:    
              case 0x0231: return "Starting address of the display list";
              case 0x0232: return "Serial port control register";
              case 0x0233: return "Spare byte (no OS use)";             
              case 0x0234: return "Light pen horizontal value shadow for 54284 ($D40C)";
              case 0x0235: return "Light pen vertical value: shadow for 54285 ($D40D)";
              case 0x0236:    
              case 0x0237: return "BREAK key interrupt vector";
              case 0x0238: 
              case 0x0239: return "Spare byte";
              case 0x023A: return "Four-byte command frame buffer (CFB) address for a device";
              case 0x023B: return "The SIO bus command code";
              case 0x023C: return "Command auxiliary byte one";
              case 0x023D: return "Command auxiliary byte two";
              case 0x023E: return "Temporary RAM register for SIO";
              case 0x023F: return "SIO error flag";
              case 0x0240: return "Disk flags read from the first byte of the boot file (sector one) of the disk";
              case 0x0241: return "The number of disk boot sectors read from the first disk record";
              case 0x0242: 
              case 0x0243: return "The address for where the disk boot loader will be put";
              case 0x0244: return "Coldstart flag";
              case 0x0245: return "Spare byte";
              case 0x0246: return "Disk time-out register";
              case 0x026F: return "Priority selection register, shadow for 53275 ($D01B)";
              case 0x0270: return "The value of paddle 0";
              case 0x0271: return "The value of paddle 1";
              case 0x0272: return "The value of paddle 2";
              case 0x0273: return "The value of paddle 3";
              case 0x0274: return "The value of paddle 4";
              case 0x0275: return "The value of paddle 5";
              case 0x0276: return "The value of paddle 6";
              case 0x0277: return "The value of paddle 7";
              case 0x0278: return "The value of joystick 0";
              case 0x0279: return "The value of joystick 1";
              case 0x027A: return "The value of joystick 2";
              case 0x027B: return "The value of joystick 3";
              case 0x027C: return "Paddle trigger 0";
              case 0x027D: return "Paddle trigger 1";
              case 0x027E: return "Paddle trigger 2";
              case 0x027F: return "Paddle trigger 3";
              case 0x0280: return "Paddle trigger 4";
              case 0x0281: return "Paddle trigger 5";
              case 0x0282: return "Paddle trigger 6";
              case 0x0283: return "Paddle trigger 7";
              case 0x0284: return "Stick trigger 0";
              case 0x0285: return "Stick trigger 1";
              case 0x0286: return "Stick trigger 2";
              case 0x0287: return "Stick trigger 3";
              case 0x0288: return "Cassette status register";
              case 0x0289: return "Store either the read or the write mode for the cassette handler";
              case 0x028A: return "Cassette data record buffer size";
              case 0x028B: 
              case 0x028C:    
              case 0x028D:   
              case 0x028E:    
              case 0x028F: return "Spare byte";
              case 0x0290: return "Text window cursor row";
              case 0x0291:    
              case 0x0292: return "Text window cursor column";
              case 0x0293: return "Current split-screen text window GRAPHICS mode";
              case 0x0294:    
              case 0x0295: return "Address of the upper left corner of the text window";
              case 0x0296: return "Split-screen: Previous graphics cursor row";
              case 0x0297: 
              case 0x0298: return "Split-screen: Previous graphics cursor column";
              case 0x0299: return "Split-screen: Retains the value of the character under the cursor";
              case 0x029A: 
              case 0x029B: return "Split-screen: Retains the memory location of the current cursor location";
              case 0x029C: return "Temporary register (display handler)";
              case 0x029D:
              case 0x029E:    
              case 0x029F: return "Temporary register";
              case 0x02A0: return "Pixel location mask";
              case 0x02A1: return "Temporary storage for the bit mask";              
              case 0x02A2: return "Escape flag";
              case 0x02B2:    
              case 0x02B3: 
              case 0x02B4:     
              case 0x02B5: return "Logical line start bit map";
              case 0x02B6: return "Inverse character flag";
              case 0x02B7: return "Right fill flag for the DRAW command";
              case 0x02B8: return "Temporary register for row used by ROWCRS";
              case 0x02B9:     
              case 0x02BA: return "Temporary register for column used by COLCRS";
              case 0x02BB: return "Scroll flag";
              case 0x02BC: 
              case 0x02BD: return "Temporary register used in the DRAW command only";
              case 0x02BE: return "Flag for the shift and control keys";
              case 0x02BF: return "Flag for the number of text rows available for printing";
              case 0x02C0: return "Color of player 0 and missile 0";
              case 0x02C1: return "Color of player 1 and missile 1";
              case 0x02C2: return "Color of player 2 and missile 2";
              case 0x02C3: return "Color of player 3 and missile 3";
              case 0x02C4: return "Color register zero";
              case 0x02C5: return "Color register one";
              case 0x02C6: return "Color register two";
              case 0x02C7: return "Color register three";
              case 0x02C8: return "Color register for the background (BAK) and border";
              case 0x02D9: return "Auto-delay rate";
              case 0x02DA: return "The rate of the repeat";
              case 0x02DB: return "Keyboard click disable register";
              case 0x02DC: return "Help key register";
              case 0x02DE: return "Print Buffer Pointer";
              case 0x02DF: return "Print buffer size";
              case 0x02E0: 
              case 0x02E1: return "Run address read from the disk sector one";
              case 0x02E2: 
              case 0x02E3: return "Initialization address read from the disk";
              case 0x02E4: return "RAM size, high byte only";
              case 0x02E5:
              case 0x02E6: return "Pointer to the top of free memory used by both BASIC and OS";
              case 0x02E7:
              case 0x02E8: return "Pointer to the bottom of free memory";
              case 0x02E9: return "Spare byte";
              case 0x02EA: return "Device error status and the command status byte";
              case 0x02EB: return "Device status byte";
              case 0x02EC: return "Maximum device time-out value in seconds";
              case 0x02ED: return "Number of bytes in output buffer";
              case 0x02EE: return "Cassette baud rate low";
              case 0x02EF: return "Cassette baud rate high";
              case 0x02F0: return "Cursor inhibit flag";
              case 0x02F1: return "Key delay flag or key debounce counter";
              case 0x02F2: return "Prior keyboard character code";
              case 0x02F3: return "Character Mode Register";
              case 0x02F4: return "Character Base Register";
              case 0x02F5: 
              case 0x02F6: 
              case 0x02F7: 
              case 0x02F8:    
              case 0x02F9: return "Spare byte";
              case 0x02FA: return "Internal code value for the most recent character read or written";
              case 0x02FB: return "Last ATASCII character read or written or the value of a graphics point";
              case 0x02FC: return "Internal hardware value for the last key pressed";
              case 0x02FD: return "Color data for the fill region in the XIO FILL command";
              case 0x02FE: return "Display flag";
              case 0x02FF: return "Start/stop display screen flag";
              
              case 0x0300: return "Device serial bus ID";
              case 0x0301: return "Disk or device unit number";
              case 0x0302: return "The number of the disk or device operation";
              case 0x0303: return "The status code upon return to user";
              case 0x0304: 
              case 0x0305: return "Data buffer address of the source or destination of the data to be transferred or the device status information";
              case 0x0306: return "The time-out value for the handler in one-second units";
              case 0x0307: return "Unused byte";
              case 0x0308: 
              case 0x0309: return "The number of bytes transferred to or from the data buffer";
              case 0x030A: 
              case 0x030B: return "Used for device specific information such as the disk sector number for the read or write operation";
              case 0x030C: 
              case 0x030D: return "Initial baud rate timer value";
              case 0x030E: return "Addition correction flag for the baud rate calculations involving the timer registers";
              case 0x030F: return "Cassette mode when set";
              case 0x0310: 
              case 0x0311: return "Final timer value";
              case 0x0312: 
              case 0x0313: return "Temporary storage register used by SIO";
              case 0x0314: return "Temporary storage register";
              case 0x0315: return "Ditto";
              case 0x0316: return "Save serial data-in port used to detect, and updated after, each bit arrival";
              case 0x0317: return "Time-out flag for baud rate correction";
              case 0x0318: return "SIO stack pointer register";
              case 0x0319: return "Temporary status holder for location 48 ($30)";
              case 0x03E8: return "Flag for function keys";
              case 0x03E9: return "Cassette boot request flag on coldstart";
              case 0x03EA: return "Cassette boot flag";
              
              case 0x057E: return "LBUFF prefix one";
              case 0x057F: return "LBUFF prefix two";
              case 0x05E0: return "Polynominal arguments";
              
              
                
              // Right cartridge B:
              case 0x9FFA: return "Cartridge B: start address low";
              case 0x9FFB: return "Cartridge B: start address high";
              case 0x9FFC: return "Cartridge B: present?";
              case 0x9FFD: return "Cartridge B: option byte";
              case 0x9FFE: return "Cartridge B: initialization address low";
              case 0x9FFF: return "Cartridge B: initialization address high";  
                
              // Left cartridge A:
              case 0xA8E2: return "Cartridge A: shows revision of BASIC";
              case 0xBFFA: return "Cartridge A: start address low";
              case 0xBFFB: return "Cartridge A: start address high";
              case 0xBFFC: return "Cartridge A: present?";
              case 0xBFFD: return "Cartridge A: option byte";
              case 0xBFFE: return "Cartridge A: initialization address low";
              case 0xBFFF: return "Cartridge A: initialization address high";              
                
              // gta
              case 0xD000: return "Missile 0 collision with playfield | Horizontal position of player 0";
              case 0xD001: return "Missile 1 collision with playfield | Horizontal position of player 1";
              case 0xD002: return "Missile 2 collision with playfield | Horizontal position of player 2";
              case 0xD003: return "Missile 3 collision with playfield | Horizontal position of player 3";
              case 0xD004: return "Player 0 collision with playfield | Horizontal position of missile 0";
              case 0xD005: return "Player 1 collision with playfield | Horizontal position of missile 1";
              case 0xD006: return "Player 2 collision with playfield | Horizontal position of missile 2";
              case 0xD007: return "Player 3 collision with playfield | Horizontal position of missile 3";
              case 0xD008: return "Collision Missile 0 with Player | Size Player 0";
              case 0xD009: return "Collision Missile 1 with Player | Size Player 1";
              case 0xD00A: return "Collision Missile 2 with Player | Size Player 2";
              case 0xD00B: return "Collision Missile 3 with Player | Size Player 3";
              case 0xD00C: return "Collision Player 0 with Player x | Size of Missiles 0-3";
              case 0xD00D: return "Collision Player 1 with Player | Graphics Shape for Player 0";
              case 0xD00E: return "Collision Player 2 with Player | Graphics Shape for Player 1";
              case 0xD00F: return "Collision Player 3 with Player | Graphics Shape for Player 2";
              case 0xD010: return "Trigger 0 | Graphics Shape for Player 3";
              case 0xD011: return "Trigger 1 | Graphics Shape for Missile 0-3";
              case 0xD012: return "Trigger Joystick 2 | Color of Player and Missile 0";
              case 0xD013: return "Trigger Joystick 3 | Color of Player and Missile 1";
              case 0xD014: return "Determine TV System | Color of Player and Missile 2";
              case 0xD015: return "Color of Player and Missile 3";
              case 0xD016: return "Color Register 0";
              case 0xD017: return "Color Register 1";
              case 0xD018: return "Color Register 2";
              case 0xD019: return "Color Register 3";
              case 0xD01A: return "Background Color Register";
              case 0xD01B: return "Priority Selection Register";
              case 0xD01C: return "Vertical Delay for PM";
              case 0xD01D: return "Controls PM and Triggers	";
              case 0xD01E: return "Clear all collision registers";
              case 0xD01F: return "Console keys and internal speaker (400/800)";
                
              // pokey
              case 0xD200: return "Pot/paddle 0 | Audio channel 1 frequency";
              case 0xD201: return "Pot/paddle 1 | Audio channel 1 control";
              case 0xD202: return "Pot/paddle 2 | Audio channel 2 frequency";
              case 0xD203: return "Pot/paddle 3 | Audio channel 2 control";
              case 0xD204: return "Pot/paddle 4 | Audio channel 3 frequency";
              case 0xD205: return "Pot/paddle 5 | Audio channel 3 control";
              case 0xD206: return "Pot/paddle 6 | Audio channel 4 frequency";
              case 0xD207: return "Pot/paddle 7 | Audio channel 4 control";
              case 0xD208: return "Audio control | Pot Port State";
              case 0xD20A: return "Random number generator | Serial status register reset";
              case 0xD20B: return "Start Pot reading sequence";
              case 0xD20C: return "Unused";
              case 0xD20D: return "Serial Port Data Output | Serial Port Data Input";
              case 0xD20E: return "Interrupt Request Enable | Interrupt Request Status";
              case 0xD20F: return "Serial Port Control | Serial Port Status";
              
              // pia
              case 0xD300: return "Joystick port A";
              case 0xD301: return "Joystick port B";
              case 0xD302: return "Port A Control";
              case 0xD303: return "Port B Control";
              
              // antic
              case 0xD400: return "Direct Memory Access (DMA) Control";
              case 0xD401: return "Cursor and character display";
              case 0xD402: 
              case 0xD403: return "Start Address of the Display List";
              case 0xD404: return "Horizontal Fine Scrolling";
              case 0xD405: return "Vertical Fine Scrolling";
              case 0xD406: return "Unused";
              case 0xD407: return "PM data base address (MSB)";
              case 0xD408: return "Unused";
              case 0xD409: return "Character set address";
              case 0xD40A: return "Stops CPU until end of scanline is reached";
              case 0xD40B: return "Current scanline/2";
              case 0xD40C: return "Horizontal position of lightpens";
              case 0xD40D: return "Vertical position of lightpens";
              case 0xD40E: return "Non Maskable Interrupt Enable";
              case 0xD40F: return "Non Maskable Interrupt Status/Reset";
              
              // OS ROM
              case 0xD800: return "ASCII to Floating Point (FP) conversion";
              case 0xD8E6: return "FP value to ASCII conversion";
              case 0xD9AA: return "Integer to FP conversion";
              case 0xD9D2: return "FP to integer conversion";
              case 0xDA44: return "Clear FR0 at 212 to 217 ($d$-$DB)";
              case 0xDA46: return "Clear the FP number from FR1, locations 224 to 229 ($E0 to $E5)";
              case 0xDA60: return "FP subtract routine, the value in FR0 minus the value in FR1";
              case 0xDA66: return "FP addition routine; FR0 plus FR1";
              case 0xDADB: return "FP multiplication routine; FR0 times FR1";
              case 0xDB28: return "FP division routine; FR0 divided by FR1";
              case 0xDD40: return "FP polynomial evaluation";
              case 0xDD89: return "Load the FP number into FR0 from the 6502 X,Y registers";
              case 0xDD8D: return "Load the FP number into FR0 from user routine, using FLPTR at 252 ($FC)";
              case 0xDD98: return "Load the FP number into FR1 from the 6502 X,Y registers";
              case 0xDD9C: return "Load the FP number into FR1 from user program, using FLPTR";
              case 0xDDA7: return "Store the FP number into the 6502 X,Y registers from FR0";
              case 0xDDAB: return "Store the FP number from FR0, using FLPTR";
              case 0xDDB6: return "Move the FP number from FR0 to FR1";
              case 0xDDC0: return "FP base e exponentiation";
              case 0xDDCC: return "FP base 10 exponentiation";
              case 0xDDCD: return "FP natural logarithm";
              case 0xDED1: return "FP base 10 logarithm";
              case 0xE400: return "Screen editor (E:) entry point table";
              case 0xE410: return "Display handler (television screen) (S:)";
              case 0xE420: return "Jump Table for Keyboard driver K:";
              case 0xE430: return "Printer handler (P:)";
              case 0xE440: return "Cassette handler (C:)";
              case 0xE450: return "Disk handler initialization vector, initialized to 60906 ($EDEA)";
              case 0xE453: return "Disk handler (interface) entry; checks the disk status. Initialized to 60912 ($EDF0)";
              
              case 0xE45C: return "Set system timers and interrupt vectors";
              case 0xE45F: return "Stage one VBLANK calculations entry";
              case 0xE462: return "Exit from the VBLANK routine";
              case 0xE465: return "SIO utility initialisation, OS use only";
              case 0xE468: return "Send enable routine, OS use only";
              case 0xE46B: return "Interrupt handler initialisation, OS use only";
              case 0xE46E: return "CIO utility initialisation, OS Use only";
              case 0xE471: return "Blackbaord Mode Entry";
              case 0xE474: return "Warm Start Vector";
              case 0xE477: return "Cold Start Vector";
              case 0xE47A: return "Reads block from C:";
              case 0xE47D: return "Opens channel for C:";
              case 0xE480: return "Vector for Self Test";
              case 0xE7AE: 
              case 0xE7D1: return "VBLANK routines";     
              default:
                if ((addr>=0x100) && (addr<=0x1FF)) return "CPU stack";  
                if ((addr>=0x247) && (addr<=0x26E)) return "Character line buffer";  
                if ((addr>=0x2A3) && (addr<=0x2B1)) return "Map of the TAB stop positions";  
                if ((addr>=0x2C9) && (addr<=0x2DF)) return "Spare byte";
                if ((addr>=0x31A) && (addr<=0x33F)) return "Handler Address Table";
                if ((addr>=0x340) && (addr<=0x34F)) return "I/O Control Block (IOCB) zero";
                if ((addr>=0x350) && (addr<=0x35F)) return "I/O Control Block (IOCB) one";
                if ((addr>=0x360) && (addr<=0x36F)) return "I/O Control Block (IOCB) two";
                if ((addr>=0x370) && (addr<=0x37F)) return "I/O Control Block (IOCB) three";
                if ((addr>=0x380) && (addr<=0x38F)) return "I/O Control Block (IOCB) four";
                if ((addr>=0x390) && (addr<=0x39F)) return "I/O Control Block (IOCB) five";
                if ((addr>=0x3A0) && (addr<=0x3AF)) return "I/O Control Block (IOCB) six";
                if ((addr>=0x3B0) && (addr<=0x3BF)) return "I/O Control Block (IOCB) seven";
                if ((addr>=0x3C0) && (addr<=0x3E7)) return "Printer buffer";
                if ((addr>=0x3FD) && (addr<=0x47F)) return "Cassette buffer";
                if ((addr>=0x580) && (addr<=0x5E5)) return "BASIC line Buffer";
                if ((addr>=0x5E6) && (addr<=0x5FF)) return "Floating Point scratch pad use";
            }   
            
        }  
    }
    
    return super.dcom(iType, aType, addr, value);    
  }  
}
