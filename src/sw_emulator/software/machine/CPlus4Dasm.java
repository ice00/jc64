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
public class CPlus4Dasm extends M6510Dasm {
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
        if ((int)addr<=0xFF && !option.commentPlus4ZeroPage) return "";
        if ((int)addr>=0x100 && (int)addr<=0x1FF && !option.commentPlus4StackArea) return "";
        if ((int)addr>=0x200 && (int)addr<=0x2FF && !option.commentPlus4_200Area) return "";
        if ((int)addr>=0x300 && (int)addr<=0x3FF && !option.commentPlus4_300Area) return "";
        if ((int)addr>=0x400 && (int)addr<=0x4FF && !option.commentPlus4_400Area) return "";
        if ((int)addr>=0x500 && (int)addr<=0x5FF && !option.commentPlus4_500Area) return "";
        if ((int)addr>=0x600 && (int)addr<=0x6FF && !option.commentPlus4_600Area) return "";
        if ((int)addr>=0x700 && (int)addr<=0x7FF && !option.commentPlus4_700Area) return "";
        if ((int)addr>=0x800 && (int)addr<=0xBFF && !option.commentPlus4ColorArea) return "";
        if ((int)addr>=0xC00 && (int)addr<=0xCFF && !option.commentPlus4VideoArea) return "";
        if ((int)addr>=0x1000 && (int)addr<=0x17FF && !option.commentPlus4BasicRamP) return "";
        if ((int)addr>=0x1800 && (int)addr<=0x1BFF && !option.commentPlus4Luminance) return "";
        if ((int)addr>=0x1C00 && (int)addr<=0x1FFF && !option.commentPlus4ColorBitmap) return ""; 
        if ((int)addr>=0x2000 && (int)addr<=0x3FFF && !option.commentPlus4GraphicData) return "";
        if ((int)addr>=0x4000 && (int)addr<=0x7FFF && !option.commentPlus4BasicRamN) return "";
        if ((int)addr>=0x8000 && (int)addr<=0xBFFF && !option.commentPlus4BasicRom) return "";
        if ((int)addr>=0xC000 && (int)addr<=0xCFFF && !option.commentPlus4BasicExt) return "";
        if ((int)addr>=0xD000 && (int)addr<=0xDFFF && !option.commentPlus4Caracter) return "";
        if ((int)addr>=0xFD00 && (int)addr<=0xFD0F && !option.commentPlus4Acia) return "";
        if ((int)addr>=0xFD10 && (int)addr<=0xFD1F && !option.commentPlus4_6529B_1) return "";
        if ((int)addr>=0xFD30 && (int)addr<=0xFD3F && !option.commentPlus4_6529B_2) return ""; 
        if ((int)addr>=0xFF00 && (int)addr<=0xFF1F && !option.commentPlus4Ted) return ""; 
        if ((int)addr>=0xFF20 && (int)addr<=0xFFFF && !option.commentPlus4Kernal) return "";
        switch (language) {                       
          case LANG_ITALIAN:
            switch ((int)addr) {           
              case 0x00: return "7501 registro di direzione dati su chip";
              case 0x01: return "7501 registro di ingresso/uscita a 8 bit su chip";
              case 0x02: return "La \"ricerca\" del token cerca (stack di runtime)";
              case 0x03: 
              case 0x04: 
              case 0x05: 
              case 0x06: return "Temp (rinumerazione)";
              case 0x07: return "Carattere di ricerca";
              case 0x08: return "Flag: cerca le virgolette alla fine della stringa";
              case 0x09: return "Colonna dello schermo dall'ultimo TAB";
              case 0x0A: return "Flag: 0=carica 1=verifica";
              case 0x0B: return "Puntatore del buffer di input/n. di sottoscrizioni";
              case 0x0C: return "Flag: Dimensione matrice predefinita";
              case 0x0D: return "Tipo di dati: $FF=stringa, $00=numerico";
              case 0x0E: return "Tipo di dati: $80=numero intero, $00=virgola mobile";
              case 0x0F: return "Flag: scansione DATA/quota LIST/garbage collection";
              case 0x10: return "Flag: riferimento pedice/funzione utente coll";
              case 0x11: return "Flag: $00=INPUT, $43=GET, $98=READ";
              case 0x12: return "Contrassegna TAN siqn/risultato del confronto";
              case 0x13: return "Flag: prompt INPUT";
              case 0x14:
              case 0x15: return "Temp: valore intero";
              case 0x16: return "Puntatore: stack di stringhe temporaneo";
              case 0x17: 
              case 0x18: return "Indirizzo dell'ultima stringa temporanea";
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
              case 0x2B: 
              case 0x2C: return "Puntatore: inizio del testo BASIC";
              case 0x2D: 
              case 0x2E: return "Puntatore: inizio delle variabili BASIC";
              case 0x2F: 
              case 0x30: return "Puntatore: inizio degli array BASIC";
              case 0x31: 
              case 0x32: return "Puntatore: fine degli array BASIC (+1)";
              case 0x33: 
              case 0x34: return "Puntatore: fondo della memoria delle stringhe";
              case 0x35: 
              case 0x36: return "Puntatore a stringa di utilità";
              case 0x37: 
              case 0x38: return "Puntatore: indirizzo più alto utilizzato dal BASIC";
              case 0x39: 
              case 0x3A: return "Numero di riga BASIC corrente";              
              case 0x3F: 
              case 0x40: return "Numero di riga DATI corrente";
              case 0x41: 
              case 0x42: return "Puntatore: indirizzo dell'elemento DATI corrente"; 
              case 0x43: 
              case 0x44: return "Vettore: routine INPUT";
              case 0x45: 
              case 0x46: return "Nome della variabile BASIC corrente";
              case 0x47: 
              case 0x48: return "Puntatore: dati della variabile BASIC corrente";
              case 0x49: 
              case 0x4A: return "Puntatore: variabile indice per FOR/NEXT";
              case 0x61: return "Accumulatore a virgola mobile n. 1: esponente";   
              case 0x62:
              case 0x63: 
              case 0x64: 
              case 0x65: return "Accumulatore a virgola mobile n. 1: mantissa"; 
              case 0x66: return "Accumulatore a virgola mobile n. 1: segno";
              case 0x67: return "Puntatore: costante di valutazione della serie";
              case 0x68: return "Accumulatore a virgola mobile n. 1: cifra di overflow";              
              case 0x69: return "Accumulatore a virgola mobile n. 2: esponente";
              case 0x6A: 
              case 0x6B:
              case 0x6C: 
              case 0x6D: return "Accumulatore a virgola mobile n. 2: mantissa";
              case 0x6E: return "Accumulatore a virgola mobile n. 2: segno";
              case 0x6F: return "Risultato del confronto dei segni: accum. # 1 contro # 2"; 
              case 0x70: return "Accumulatore a virgola mobile n. 1: ordine basso (arrotondamento)";
              case 0x71: 
              case 0x72: return "Puntatore: buffer della cassetta";
              case 0x73:
              case 0x74: return "Valore di incremento per auto (0=off)";
              case 0x75: return "Contrassegna se vengono assegnate 10.000 assunzioni";
              case 0x78: return "Utilizzato come temp Eor carichi indiretti";
              case 0x79: 
              case 0x7A: 
              case 0x7B: return "Descrittore per DSS"; 
              case 0x7C:
              case 0x7D: return "Inizio dello stack del tempo di esecuzione";
              case 0x7E:
              case 0x7F: return "Utilizzato temporaneamente dalla musica (tono e volume)";
              case 0x83: return "Modalità grafica corrente";
              case 0x84: return "Colore corrente selezionato";
              case 0x85: return "Multi colore 1";
              case 0x86: return "Colore di primo piano";
              case 0x87: return "Numero massimo di colonne";
              case 0x88: return "Numero massimo di righe";
              case 0x89: return "Impostazione Paint-left";
              case 0x8A: return "Impostazione Paint-Right";              
              case 0x8B: return "Smetti di dipingere se non BG (non dello stesso colore)";
              case 0x90: return "Parola di stato I/O del kernel: ST";
              case 0x91: return "Flag: tasto STOP/tasto RVS"; 
              case 0x92: return "Temporaneo";  
              case 0x93: return "Flag: 0=caricamento, 1=verifica";
              case 0x94: return "Flag: bus seriale - caratteri di uscita bufferizzati";
              case 0x95: return "Carattere bufferizzato per bus seriale";
              case 0x96: return "Temporaneo per Basin";
              case 0x97: return "numero di file/indice aperti nella tabella dei file";
              case 0x98: return "Dispositivo di input predefinito (0)";
              case 0x99: return "Dispositivo di output predefinito (CMD) (3)";
              case 0x9A: return "Flag: $80=modalità diretta $00=programma";
              case 0x9B: return "Registro errori passaggio 1 nastro";
              case 0x9C: return "Registro errori passaggio 2 nastro";
              case 0x9F:          
              case 0xA0: 
              case 0xA1:
              case 0xA2: return "Area dati temporanea";
              case 0xA3: 
              case 0xA4: 
              case 0xA5: return "Orologio jiffy in tempo reale (circa) 1/60 sec";
              case 0xA6: return "Utilizzo del bus seriale (EOI in uscita)";
              case 0xA7: return "Byte da scrivere/leggere su/off nastro";
              case 0xA8: return "Temp utilizzato dalla routine seriale";
              case 0xAB: return "Lunghezza del nome file corrente";
              case 0xAC: return "Numero logico corrente";
              case 0xAD: return "Indirizzo secondario attuale";
              case 0xAE: return "Numero del dispositivo corrente";
              case 0xAF: 
              case 0xB0: return "Puntatore: nome del file corrente";
              case 0xB2:
              case 0xB3: return "Indirizzo iniziale I / O";
              case 0xB4: 
              case 0xB5: return "Caricare la base della ram";
              case 0xB6: 
              case 0xB7: return "Puntatore base alla base della cassetta";
              case 0xBA: 
              case 0xBB: return "Puntatore ai dati per le scritture su nastro";  
              case 0xBC: 
              case 0xBD: return "Puntatore alla stringa immediata per i primm"; 
              case 0xBE: 
              case 0xBF: return "Puntatore a byte da recuperare in bank fetc";
              case 0xC0: 
              case 0xC1: return "Temp per lo scorrimento";
              case 0xC2: return "Flag del campo RVS attivato";
              case 0xC4: return "Posizione X all'inizio";
              case 0xC6: return "Flag: modalità shift per la stampa";
              case 0xC7: return "caso 0xC7: ritorna \"Screen reverse flag\";";
              case 0xC8:
              case 0xC9: return "Puntatore: indirizzo della riga dello schermo corrente";
              case 0xCA: return "Colonna del cursore sulla riga corrente";
              case 0xCB: return "Flag: editor in modalità quote, $00=no";
              case 0xCC: return "Uso temporaneo dell'editor";
              case 0xCD: return "Numero di riga fisica corrente del cursore";
              case 0xCE: return "Area dati temporanea";
              case 0xCF: return "Flag: modalità di inserimento,>0=numero INST"; 
              case 0xE9: return "Tabella dei collegamenti della riga dello schermo /temporaneo dell'editor"; 
              case 0xEA:
              case 0xEB: return "IP a colori dell'editor dello schermo";
              case 0xEC:
              case 0xED: return "Tabella di scansione chiave indiretta";
              case 0xEF: return "Indice alla coda della tastiera";
              case 0xF0: return "Impostazione pausa";
              case 0xF1:
              case 0xF2: return "Monitora lo spazio di archiviazione ZP";
              case 0xF5: return "Temp per il calcolo del checksum";
              case 0xF7: return "Quale passaggio stiamo facendo nella stringa";
              case 0xF8: return "Tipo di blocco";
              case 0xF9: return "(B.7=1)=> per scrittura, (B.6=1)=> per lettura";
              case 0xFA: return "Salva registro X per un test rapido di tasto stop"; 
              case 0xFB: return "Configurazione banca corrente";
              case 0xFC: return "Carattere da inviare per una x-on(RS232)";
              case 0xFD: return "Carattere da inviare per un x-off (RS232)";
              case 0xFE: return "Uso temporaneo dell'editor";
              
              case 0x110: return "Posizioni temporanee per salvataggio/ripristino A";
              case 0x111: return "Posizioni temporanee per salvataggio/ripristino Y";
              case 0x112: return "Posizioni temporanee per salvataggio/ripristino X";
              case 0x25D: return "Contatore loop DOS";
              case 0x26F: return "Unità disco DOS 1";
              case 0x270:
              case 0x271: return "Indirizzo nome file DOS 1";
              case 0x272: return "Lunghezza del nome file DOS 2";
              case 0x273: return "Unità disco DOS 2";
              case 0x274:
              case 0x275: return "Indirizzo nome file DOS 2";
              case 0x276: return "Indirizzo logico DOS";
              case 0x277: return "Indirizzo fisico DOS";
              case 0x278: return "Indirizzo secondario DOS";
              case 0x279: 
              case 0x27A: return "Identificatore del disco DOS";
              case 0x27B: return "Flag DOS DID";
              case 0x27C: return "Buffer della stringa di output DOS";
              case 0x2AD:
              case 0x2AE: return "Posizione x corrente";
              case 0x2AF:
              case 0x2B0: return "Posizione y corrente";
              case 0x2B1:
              case 0x2B2: return "Coordinata X destinazione";
              case 0x2B3:
              case 0x2B4: return "Coordinata Y destinazione";
              case 0x2C5: return "Segno di angolo";
              case 0x2C6:
              case 0x2C7: return "Seno del valore dell'angolo";
              case 0x2C8:
              case 0x2C9: return "Coseno del valore dell'angolo";
              case 0x2CA:
              case 0x2CB: return "Temporanei per routine di distanza angolare";
              case 0x2CC: return "Segnaposto";
              case 0x2CD: return "Puntatore per l'inizio di no.";
              case 0x2CE: return "Puntatore per la fine di no.";
              case 0x2CF: return "Segno del dollaro";
              case 0x2D0: return "Segno della virgola";
              case 0x2D1: return "Contatore";
              case 0x2D2: return "Esponente del segno";
              case 0x2D3: return "Puntatore all'esponente";
              case 0x2D4: return "Numero di cifre prima del punto decimale";
              case 0x2D5: return "Giustifica bandiera";
              case 0x2D6: return "Numero di posizione prima del punto decimale (campo)";
              case 0x2D7: return "Numero di posizione dopo il punto decimale (campo)";
              case 0x2D8: return "Segni +/- (campo)";
              case 0x2D9: return "Segno esponente (campo)";
              case 0x2DA: return "contatore colonna interruttore/caratteri";
              case 0x2DB: return "Contatore di caratteri (campo)/contatore di righe di caratteri";
              case 0x2DC: return "Segno n.";
              case 0x2DD: return "Bandiera vuota /a stella";
              case 0x2DE: return "Puntatore all'inizio del campo";
              case 0x2DF: 
              case 0x2E0: return "Lunghezza del formato";
              case 0x2F1:
              case 0x2F3: return "Puntatore alla routine: converte il virgola mobile in intero";
              case 0x2F4:
              case 0x2F5: return "Puntatore alla routine: converte l'intero in virgola mobile";
              case 0x2FE:
              case 0x2FF: return "Vettore per utenti di cartucce funzionali";
              case 0x300:
              case 0x301: return "Errore indiretto (errore di output in .X)";
              case 0x302:
              case 0x303: return "Principale indiretto (loop diretto di sistema)";
              case 0x304:
              case 0x305: return "Crunch indiretto (routine di tokenizzazione)";
              case 0x306:
              case 0x307: return "Elenco indiretto (elenco caratteri)";
              case 0x308:
              case 0x309: return "Indiretto andato (invio del carattere)";
              case 0x30A:
              case 0x30B: return "Valutazione indiretta (valutazione del simbolo)";
              case 0x30C: 
              case 0x30D: return "Escape token crunch";
              case 0x314:
              case 0x315: return "Verrore Ram IRQ";
              case 0x316:
              case 0x317: return "Vettore RAM dell'istruzione BRK";
              case 0x318:
              case 0x319: return "Indiretti per il codice";
              case 0x330:
              case 0x331: return "Savesp";
              case 0x3F3:
              case 0x3F4: return "Lunghezza dei dati da scrivere su nastro";
              case 0x3F5:
              case 0x3F6: return "Lunghezza dei dati da leggere dal nastro";
              case 0x4A2:
              case 0x4A3:    
              case 0x4A4: return "Costante numerica per Basic";
              case 0x4E7: return "Stampa utilizzando il simbolo di riempimento [spazio]";
              case 0x4E8: return "Stampa utilizzando il simbolo della virgola [;]";
              case 0x4E9: return "Stampa utilizzando il simbolo del punto [.]";
              case 0x4EA: return "Stampa utilizzando il simbolo monetario [$]";
              case 0x4EB:
              case 0x4EC:    
              case 0x4ED:    
              case 0x4EE: return "Temp per l'istruzione";
              case 0x4EF: return "Ultimo numero di errore";
              case 0x4F0:    
              case 0x4F1: return "Numero riga dell'ultimo errore";
              case 0x4F2:    
              case 0x4F3: return "Linea per andare all'errore";
              case 0x4F4: return "Tieni il numero trap temporaneamente";
              case 0x4FC:    
              case 0x4FD: return "Tabella dei jiffies in sospeso (complemento a 2)";   
              case 0x508: return "stato di avvio \"freddo\" o \"caldo\"";
              case 0x531: 
              case 0x532: return "Inizio della memoria [1000]";
              case 0x533:
              case 0x534: return "Ritorna \\ \"Inizio della memoria [1000]\";";
              case 0x535: return "Flag di timeout IEEE";
              case 0x536: return "Fine file raggiunta=1, 0 altrimenti";
              case 0x537: return "# di caratteri rimasti nel buffer (per R & W)";
              case 0x538: return "# di caratteri validi totali nel buffer (R)";
              case 0x539: return "Puntatore al carattere successivo nel buffer (per R & W)";
              case 0x53A: return "Contiene il tipo di file di classe corrente";
              case 0x53B: return "Byte attributo attivo";
              case 0x53C: return "Carattere lampeggiante";
              case 0x53D: return "Libero";
              case 0x53E: return "OC Posizione base dello schermo (in alto) [0C]";
              case 0x540: return "Flag di ripetizione dei tasti";
              case 0x543: return "Shift flag byte";
              case 0x544: return "Schema dell'ultimo pattern";
              case 0x545:
              case 0x546: return "Indiretta per l'impostazione tabella della tastiera";
              case 0x547: return "shift, C="; 
              case 0x548: return "Flag di scorrimento automatico verso il basso (0 = attivato, 0 <> disattivato)";
              case 0x54B: return "Monitoraggio dell'archiviazione non pagina zero";
              case 0x55B: return "Utilizzato da varie routine di monitoraggio";
              case 0x55D: return "Utilizzato per tasti programmabili";
              case 0x5E7: return "Temp per la scrittura dei dati kennedy";
              case 0x5E8: return "Seleziona per leggere o scrivere kennedy";
              case 0x5E9: return "numero dispositivo kennedy";
              case 0x5EA: return "Rennedy presente = $ ff, altrimenti = $ 00";
              case 0x5EB: return "Temp per il tipo di apertura per kennedy";
              case 0x5EC: 
              case 0x5ED:
              case 0x5EE: 
              case 0x5EF: return "Tabella degli indirizzi fisici";
              case 0x5F0: 
              case 0x5F1: return "Indirizzo di salto lungo";
              case 0x5F2: return "Accumulatore per salto lungo";
              case 0x5F3: return "Registro x salto lungo";
              case 0x5F4: return "Registro di stato del salto lungo";
              case 0x7B0: return "Byte da scrivere su nastro";
              case 0x7B1: return "Temp per il calcolo della parità";
              case 0x7B2: 
              case 0x7B3: return "Temp per l'header di scrittura";
              case 0x5B5: return "Indice locale per la routine READBYTE:";
              case 0x5B6: return "Puntatore nello stack degli errori";
              case 0x5B7: return "Numero di errori di primo passaggio";
              case 0x7BE: return "Marcatore di pila per il recupero della chiave di arresto";
              case 0x7BF: return "Marcatore di pila per il recupero della chiave a tendina";
              case 0x7C0:
              case 0x7C1:
              case 0x7C2:
              case 0x7C3: return "I parametri sono passati a RDBLOK";
              case 0x7C4: return "Salvataggio delle statistiche temporanee per RDBLOK";
              case 0x7C5: return "Numero Consec shorts da trovare nell'intestazione";
              case 0x7C6: return "Numero errori irreversibili nel conto alla rovescia RD";
              case 0x7C7: return "Temp per il comando Verifica";
              case 0x7C8:
              case 0x7C9:    
              case 0x7CA:    
              case 0x7CB: return "Pipe temp per T1";
              case 0x7CC: return "Lettura propagazione errore";
              case 0x7CD: return "Carattere utente da inviare";
              case 0x7CE: return "0=vuoto; 1=pieno";
              case 0x7CF: return "Carattere di sistema da inviare";
              case 0x7D0: return "0=vuoto; 1=pieno";
              case 0x7D1: return "Puntatore all'inizio della coda di input";
              case 0x7D2: return "Puntatore in fondo alla coda di input";
              case 0x7D3: return "Numero di caratteri nella coda di input";
              case 0x7D4: return "Stato temporaneo per ACIA";
              case 0x7D5: return "Temp per routine di input";
              case 0x7D6: return "FLG per la pausa locale";
              case 0x7D7: return "FLG per pausa remota";
              case 0x7D8: return "FLG per indicare la presenza di ACIA";
              case 0x7E5: return "Parte inferiore dello schermo (0 ... 24)";
              case 0x7E6: return "Parte superiore dello schermo";
              case 0x7E7: return "Schermo sinistro (0 ... 39)";
              case 0x7E8: return "Schermo a destra";
              case 0x7E9: return "Negativo = scorrere fuori";
              case 0x7EA: return "Modalità di inserimento: FF=on, 00=off";
              case 0x7F2: return "Registra per il comando SYS";
              case 0x7F6: return "Indice di scansione delle chiavi";
              case 0x7F7: return "Flag per disabilitare la pausa CTRL-S";
              case 0x7F8: return "MSB per il monitoraggio recupera dalla ROM=0; RAM=1";
              case 0x7F9: return "MSB per tabella colori/limite in RAM=0; ROM=1";
              case 0x7FA: return "Maschera ROM per schermo diviso";
              case 0x7FB: return "Maschera base VM per schermo diviso";
              case 0x7FC: return "Semaforo blocco motore per cassetta";
              case 0x7FD: return "tod PAL";    
              case 0xFCF1: 
              case 0xFCF2:
              case 0xFCF3: return "Routine IRQ da JMP a cartuccia";
              case 0xFCF4: 
              case 0xFCF5:
              case 0xFCF6: return "Routine da JMP a PHOENIX";
              case 0xFCF7: 
              case 0xFCF8:
              case 0xFCF9: return "Routine da JMP a LONG FETCH";
              case 0xFCFA:   
              case 0xFCFB:
              case 0xFCFC: return "Routine da JMP a LONG JUMP";    
              case 0xFCFD:   
              case 0xFCFE:
              case 0xFCFF: return "Routine da JMP a LONG IRQ";                               
              case 0xFD00: return "6551 ACIA: porta DATA";
              case 0xFD01: return "6551 ACIA: porta STATUS";              
              case 0xFD02: return "6551 ACIA: porta COMMAND";
              case 0xFD03: return "6551 ACIA: porta CONTROL";
              case 0xFD04:
              case 0xFD08:
              case 0xFD0C: return "6551 ACIA: copia";
              case 0xFD10: return "6529B #1: porta utente PIO (P0-P7)";
              case 0xFD30: return "6529B #2: tastiera PIO Connettore per matrice di tastiera";
              case 0xFF00: return "TED: Timer 1 basso";
              case 0xFF01: return "TED: Timer 1 alto";
              case 0xFF02: return "TED: Timer 2 basso";
              case 0xFF03: return "TED: Timer 2 alto";
              case 0xFF04: return "TED: Timer 3 basso";
              case 0xFF05: return "TED: Timer 3 alto";
              case 0xFF06: return "TED: Test, ECM, BMM, Blank, Rows, Y2, Y1, Y0";
              case 0xFF07: return "TED: RVS off PAL, Freeze, MCM, Columns, X2, X1, X0";
              case 0xFF08: return "TED: Blocco tastiera";
              case 0xFF09: return "TED: Interrupt";
              case 0xFF0A: return "TED: Interrupt mask";
              case 0xFF0B: return "TED: RC7, RC6, RC5, RC4, RC3, RC2, RC1, RC0";
              case 0xFF0C: return "TED: C9, CUR8";
              case 0xFF0D: return "TED: CUR7, CUR6, CUR5, CUR4, CUR3, CUR2, CUR1, CUR0";
              case 0xFF0E: return "TED: Voce #1 frequenza, bits 0-7";
              case 0xFF0F: return "TED: Voce #2 frequenza, bits 0-7";
              case 0xFF10: return "TED: Voce #2 frequenza, bits 8 & 9";
              case 0xFF11: return "TED: Bits 0-3: controllo volume";
              case 0xFF12: return "TED: Bit 0-1 : voce #1 frequenza, bits 8 & 9";
              case 0xFF13: return "TED: Bit 0 Stato dell'orologio";
              case 0xFF14: return "TED: Bits 3-7 Matrice video/indirizzo base memoria colore";
              case 0xFF15: return "TED: Registro dei colori di sfondo";
              case 0xFF16: return "TED: Registro dei colori #1";
              case 0xFF17: return "TED: Registro dei colori #2";
              case 0xFF18: return "TED: Registro dei colori #3";
              case 0xFF19: return "TED: Registro dei colori #4";
              case 0xFF1A: return "TED: Ricarica bit map alto";
              case 0xFF1B: return "TED: Ricarica della bit map basso";
              case 0xFF1C: return "TED: Bit 0: Bit di linea verticale 8";
              case 0xFF1D: return "TED: Bits 0-7: Bit di linea verticale 0-7";
              case 0xFF1E: return "TED: Posizione orizzontale";
              case 0xFF1F: return "TED: Lampeggiante, indirizzo secondario verticale";
              case 0xFF3E: return "Selettore ROM";
              case 0xFF3F: return "Selettore RAM";
              case 0xFF49:   
              case 0xFF4A:
              case 0xFF4B: return "JMP to define function key routine"; 
              case 0xFF4C:   
              case 0xFF4D:
              case 0xFF4E: return "JMP to PRINT routine";   
              case 0xFF4F:   
              case 0xFF50:
              case 0xFF51: return "JMP to PRIMM routine";
              case 0xFF52:   
              case 0xFF53:
              case 0xFF54: return "JMP to ENTRY routine";              
              case 0xFF80: return "Release of Kernel: (MSB: 0=NTSC ; 1=PAL)";
              case 0xFF81: return "Initialize screen editor";
              case 0xFF84: return "Initialize I/O devices";
              case 0xFF87: return "Ram test";
              case 0xFF8A: return "Restore vectors to initial values";
              case 0xFF8D: return "Change vectors for user";
              case 0xFF90: return "Control O.S. messages";
              case 0xFF93: return "Send SA after LISTEN";
              case 0xFF96: return "Send SA after TALK";
              case 0xFF99: return "Set/Read top of memory";
              case 0xFF9C: return "Set/Read bottom of memory";
              case 0xFF9F: return "Scan keyboard";
              case 0xFFA2: return "Set timeout in DMA disk";
              case 0xFFA5: return "Handshake serial bus or DMA disk byte in";
              case 0xFFA8: return "Handshake serial bus or DMA disk byte out";
              case 0xFFAB: return "Send UNTALK out serial bus or DMA disk";
              case 0xFFAE: return "Send UNLISTEN out serial bus or DMA disk";
              case 0xFFB1: return "Send LISTEN out serial bus or DMA disk";
              case 0xFFB4: return "Send TALK out serial bus or DMA disk";
              case 0xFFB7: return "Return I/O STATUS byte";
              case 0xFFBA: return "Set LA, FA, SA";
              case 0xFFBD: return "Set length and FN address";
              case 0xFFC0: return "Open logical file";
              case 0xFFC3: return "Close logical file";
              case 0xFFC6: return "Open channel in";
              case 0xFFC9: return "open channel out";
              case 0xFFCC: return "Close I/O channels";
              case 0xFFCF: return "Input from channel";
              case 0xFFD2: return "output to channel";
              case 0xFFD5: return "Load from file";
              case 0xFFD8: return "Save to file";
              case 0xFFDB: return "Set internal clock";
              case 0xFFDE: return "Read internal clock";
              case 0xFFE1: return "Scan STOP key";
              case 0xFFE4: return "Get character from queue";
              case 0xFFE7: return "Close all files";
              case 0xFFEA: return "Increment clock";
              case 0xFFED: return "Screen org.";
              case 0xFFF0: return "Read/Set X,Y coord of cursor";
              case 0xFFF3: return "Return location of start of I/O";
            default:
                if ((addr>=0xD0) && (addr<=0xD7)) return "Area for use by speech software"; 
                if ((addr>=0xD8) && (addr<=0xE8)) return "Area for use by application software";   
                if ((addr>=0x113) && (addr<=0x122)) return "Color/luminance table in RAM";
                if ((addr>=0x124) && (addr<=0x1FF)) return "System stack";
                if ((addr>=0x200) && (addr<=0x258)) return "Basic/monitor input buffer";
                if ((addr>=0x259) && (addr<=0x25C)) return "Basic storage";
                if ((addr>=0x25E) && (addr<=0x26D)) return "Area for filename";
                if ((addr>=0x27D) && (addr<=0x2AC)) return "Area used to build DOS string";
                if ((addr>=0x333) && (addr<=0x3F2)) return "Cassette tape buffer";
                if ((addr>=0x3F7) && (addr<=0x436)) return "RS-232 input queue";
                if ((addr>=0x494) && (addr<=0x4A1)) return "Shared ROM fetch sub";
                if ((addr>=0x4A5) && (addr<=0x4AF)) return "Txtptr";
                if ((addr>=0x4B0) && (addr<=0x4BA)) return "Index & Index1";
                if ((addr>=0x4BB) && (addr<=0x4C5)) return "Index2";
                if ((addr>=0x4C6) && (addr<=0x4D0)) return "Strng1";
                if ((addr>=0x4D1) && (addr<=0x4DB)) return "Lowtr";
                if ((addr>=0x4DC) && (addr<=0x4E6)) return "Facmo";
                if ((addr>=0x509) && (addr<=0x512)) return "Logical file numbers";
                if ((addr>=0x513) && (addr<=0x51C)) return "Primary device numbers";
                if ((addr>=0x51D) && (addr<=0x526)) return "Secondary addresses";
                if ((addr>=0x527) && (addr<=0x530)) return "IRQ keyboard buffer";   
                if ((addr>=0x55F) && (addr<=0x556)) return "Table of P.F. lengths";  
                if ((addr>=0x567) && (addr<=0x5E6)) return "P.F. Key storage area";  
                if ((addr>=0x5F5) && (addr<=0x65D)) return "RAM areas for banking";  
                if ((addr>=0x65E) && (addr<=0x6EB)) return "RAM area for speech";  
                if ((addr>=0x6EC) && (addr<=0x7AF)) return "BASIC run-time stack";  
                if ((addr>=0x7B8) && (addr<=0x7BD)) return "Time constant";
                if ((addr>=0x7D9) && (addr<=0x7E4)) return "Indirect routine downloaded";
                if ((addr>=0x800) && (addr<=0xBFF)) return "Color memory (Text)";
                if ((addr>=0xC00) && (addr<=0xCFF)) return "Video matrix (Text)";
                if ((addr>=0x1000) && (addr<=0x17FF)) return "BASIC RAM (without graphics)";
                if ((addr>=0x1800) && (addr<=0x1BFF)) return "Luminance for bit map screen";
                if ((addr>=0x1C00) && (addr<=0x1FFF)) return "Color for bit map";
                if ((addr>=0x2000) && (addr<=0x3FFF)) return "Graphics screen data";
                if ((addr>=0x4000) && (addr<=0x7FFF)) return "BASIC RAM (with graphics)";
                if ((addr>=0x8000) && (addr<=0xBFFF)) return "BASIC ROM";
                if ((addr>=0xC000) && (addr<=0xCFFF)) return "BASIC Expansion";
                if ((addr>=0xD000) && (addr<=0xD7FF)) return "Character table";
                if ((addr>=0xFD00) && (addr<=0xFD0F)) return "6551 ACIA";
                if ((addr>=0xFD10) && (addr<=0xFD1F)) return "6529B #1";
                if ((addr>=0xFD30) && (addr<=0xFD3F)) return "6529B #2";
            }     
          default:
            switch ((int)addr) {                  
              case 0x00: return "7501 on-chip data-direction register";
              case 0x01: return "7501 on-chip 8-bit Input/Output register";
              case 0x02: return "Token 'search' looks for (run-time stack)";
              case 0x03: 
              case 0x04: 
              case 0x05: 
              case 0x06: return "Temp (renumber)";
              case 0x07: return "Search character";
              case 0x08: return "Flag: scan for quote at end of string";
              case 0x09: return "Screen column from last TAB";
              case 0x0A: return "Flag: 0=load 1=verify";
              case 0x0B: return "Input buffer pointer/No. of subsctipts";
              case 0x0C: return "Flag: Default Array DIMension";
              case 0x0D: return "Data type: $FF=string, $00=numeric";
              case 0x0E: return "Data type: $80=integer, $00=floating";
              case 0x0F: return "Flag: DATA scan/LIST quote/garbage coll";
              case 0x10: return "Flag: subscript ref/user function coll";
              case 0x11: return "Flag: $00=INPUT, $43=GET, $98=READ";
              case 0x12: return "Flag TAN siqn/comparison result";
              case 0x13: return "Flag: INPUT prompt";
              case 0x14:
              case 0x15: return "Temp: integer value";
              case 0x16: return "Pointer: temporary string stack";
              case 0x17: 
              case 0x18: return "Last temp string address";
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
              case 0x2B: 
              case 0x2C: return "Pointer: start of BASIC text";
              case 0x2D: 
              case 0x2E: return "Pointer: start of BASIC variables";
              case 0x2F: 
              case 0x30: return "Pointer: start of BASIC arrays";
              case 0x31: 
              case 0x32: return "Pointer: end of BASIC arrays (+1)";
              case 0x33: 
              case 0x34: return "Pointer: bottom of string storage";
              case 0x35: 
              case 0x36: return "Utility string pointer";
              case 0x37: 
              case 0x38: return "Pointer: highest address used by BASIC";
              case 0x39: 
              case 0x3A: return "Current BASIC line number";              
              case 0x3F: 
              case 0x40: return "Current DATA line number";
              case 0x41: 
              case 0x42: return "Pointer: Current DATA item address"; 
              case 0x43: 
              case 0x44: return "Vector: INPUT routine";
              case 0x45: 
              case 0x46: return "Current BASIC variable name";
              case 0x47: 
              case 0x48: return "Pointer: Current BASIC variable data";
              case 0x49: 
              case 0x4A: return "Pointer: Index variable for FOR/NEXT";
              case 0x61: return "Floating-point accumulator #1: exponent";   
              case 0x62:
              case 0x63: 
              case 0x64: 
              case 0x65: return "Floating accum. #1: mantissa"; 
              case 0x66: return "Floating accum. #1: sign";
              case 0x67: return "Pointer: series evaluation constant";
              case 0x68: return "Floating accum. #1: overflow digit";              
              case 0x69: return "Floating-point accumulator #2: exponent";
              case 0x6A: 
              case 0x6B:
              case 0x6C: 
              case 0x6D: return "Floating accum. #2: mantissa";
              case 0x6E: return "Floating accum. #2: sign";
              case 0x6F: return "Sign comparison result: accum. #1 vs #2"; 
              case 0x70: return "Floating accum. #1. low-order (rounding)";
              case 0x71: 
              case 0x72: return "Pointer: cassette buffer";
              case 0x73:
              case 0x74: return "Increment value for auto (0=off)";
              case 0x75: return "Flag if 10K hires allocated";
              case 0x78: return "Used as temp Eor indirect loads";
              case 0x79: 
              case 0x7A: 
              case 0x7B: return "Descriptor for DSS"; 
              case 0x7C:
              case 0x7D: return "Top of run time stack";
              case 0x7E:
              case 0x7F: return "Temps used by music (tone & volume)";
              case 0x83: return "Current graphic mode";
              case 0x84: return "Current color selected";
              case 0x85: return "Multicolor 1";
              case 0x86: return "Foreground color";
              case 0x87: return "Maximum # of columns";
              case 0x88: return "Maximum # of rows";
              case 0x89: return "Paint-left flag";
              case 0x8A: return "Paint-Right flag";              
              case 0x8B: return "Stop paint if not BG (Not same Color)";
              case 0x90: return "Kernal I/O status word: ST";
              case 0x91: return "Flag: STOP key/RVS key"; 
              case 0x92: return "Temp";  
              case 0x93: return "Flag: 0=load, 1=verify";
              case 0x94: return "Flag: serial bus - output char buffered";
              case 0x95: return "Buffered character for serial bus";
              case 0x96: return "Temp for basin";
              case 0x97: return "# of open files/index to file table";
              case 0x98: return "Default input device (0)";
              case 0x99: return "Default output (CMD) device (3)";
              case 0x9A: return "Flag: $80=direct mode  $00=program";
              case 0x9B: return "Tape pass 1 error log";
              case 0x9C: return "Tape pass 2 error log";
              case 0x9F:          
              case 0xA0: 
              case 0xA1:
              case 0xA2: return "Temp data area";
              case 0xA3: 
              case 0xA4: 
              case 0xA5: return "Real-time jiffy clock (approx) 1/60 sec";
              case 0xA6: return "Serial bus usage (EOI on output)";
              case 0xA7: return "Byte to be written/read on/off tape";
              case 0xA8: return "Temp used by serial routine";
              case 0xAB: return "Length of current file name";
              case 0xAC: return "Current logical file number";
              case 0xAD: return "Current secondary address";
              case 0xAE: return "Current device number";
              case 0xAF: 
              case 0xB0: return "Pointer: current file name";
              case 0xB2:
              case 0xB3: return "I/O start address";
              case 0xB4: 
              case 0xB5: return "Load ram base";
              case 0xB6: 
              case 0xB7: return "Base pointer to cassette base";
              case 0xBA: 
              case 0xBB: return "Pointer to data for tape writes";  
              case 0xBC: 
              case 0xBD: return "Pointer to immediate string for primms"; 
              case 0xBE: 
              case 0xBF: return "Pointer to byte to be fetched in bank fetc";
              case 0xC0: 
              case 0xC1: return "Temp for scrolling";
              case 0xC2: return "RVS field flag on";
              case 0xC4: return "X position at start";
              case 0xC6: return "Flag: shift mode for print";
              case 0xC7: return "case 0xC7: return \"Screen reverse flag\";";
              case 0xC8:
              case 0xC9: return "Pointer: current screen line address";
              case 0xCA: return "Cursor column on current line";
              case 0xCB: return "Flag: editor in quote mode, $00=no";
              case 0xCC: return "Editor temp use";
              case 0xCD: return "Current cursor physical line number";
              case 0xCE: return "Temp data area";
              case 0xCF: return "Flag: insert mode, >0 = # INSTs"; 
              case 0xE9: return "Screen line link table/editor temps"; 
              case 0xEA:
              case 0xEB: return "Screen editor color IP";
              case 0xEC:
              case 0xED: return "Key scan table indirect";
              case 0xEF: return "Index to keyboard queue";
              case 0xF0: return "Pause flag";
              case 0xF1:
              case 0xF2: return "Monitor ZP storage";
              case 0xF5: return "Temp for checksum calculation";
              case 0xF7: return "Which pass we are doing str";
              case 0xF8: return "Type of block";
              case 0xF9: return "(B.7 = 1)=> for wr, (B.6 = 1)=> for rd";
              case 0xFA: return "Save xreg for quick stopkey test"; 
              case 0xFB: return "Current bank configuration";
              case 0xFC: return "Char to send for a x-on (RS232)";
              case 0xFD: return "Char to send for a x-off (RS232)";
              case 0xFE: return "Editor temporary use";
              
              case 0x110: return "Temp Locations for save/restore A";
              case 0x111: return "Temp Locations for save/restore Y";
              case 0x112: return "Temp Locations for save/restore X";
              case 0x25D: return "DOS loop counter";
              case 0x26F: return "DOS disk drive 1";
              case 0x270:
              case 0x271: return "DOS filename 1 addr";
              case 0x272: return "DOS filename 2 length";
              case 0x273: return "DOS disk drive 2";
              case 0x274:
              case 0x275: return "DOS filename 2 addr";
              case 0x276: return "DOS logical address";
              case 0x277: return "DOS phys addr";
              case 0x278: return "DOS secordary address";
              case 0x279: 
              case 0x27A: return "DOS disk identifier";
              case 0x27B: return "DOS DID flag";
              case 0x27C: return "DOS output string buffer";
              case 0x2AD:
              case 0x2AE: return "Current x position";
              case 0x2AF:
              case 0x2B0: return "Current y position";
              case 0x2B1:
              case 0x2B2: return "X coordinate destination";
              case 0x2B3:
              case 0x2B4: return "Y coordinate destination";
              case 0x2C5: return "Sign of angle";
              case 0x2C6:
              case 0x2C7: return "Sine of value of angle";
              case 0x2C8:
              case 0x2C9: return "Cosine of value of angle";
              case 0x2CA:
              case 0x2CB: return "Temps for angle distance routines";
              case 0x2CC: return "Placeholder";
              case 0x2CD: return "Pointer to begin no.";
              case 0x2CE: return "Pointer to end no.";
              case 0x2CF: return "Dollar flag";
              case 0x2D0: return "Comma flag";
              case 0x2D1: return "Counter";
              case 0x2D2: return "Sign exponent";
              case 0x2D3: return "Pointer to exponent";
              case 0x2D4: return "# of digits before decimal point";
              case 0x2D5: return "Justify flag";
              case 0x2D6: return "# of pos before decimal point (field)";
              case 0x2D7: return "# of pos after decimal point (field)";
              case 0x2D8: return "+/- flag (field)";
              case 0x2D9: return "Exponent flag (field)";
              case 0x2DA: return "Switch/Characters column counter";
              case 0x2DB: return "Char counter (field)/Characters row counter";
              case 0x2DC: return "Sign no.";
              case 0x2DD: return "Blank/star flag";
              case 0x2DE: return "Pointer to beginning of field";
              case 0x2DF: 
              case 0x2E0: return "Length of format";
              case 0x2F1:
              case 0x2F3: return "Ptr to routine: convert float to integer";
              case 0x2F4:
              case 0x2F5: return "Ptr to routine: convert integer to float";
              case 0x2FE:
              case 0x2FF: return "Vector for function cartridge users";
              case 0x300:
              case 0x301: return "Indirect Error (Output Error in .X)";
              case 0x302:
              case 0x303: return "Indirect Main (System Direct Loop)";
              case 0x304:
              case 0x305: return "Indirect Crunch (Tokenization Routine)";
              case 0x306:
              case 0x307: return "Indirect List (Char List)";
              case 0x308:
              case 0x309: return "Indirect Gone (Character Dispatch)";
              case 0x30A:
              case 0x30B: return "Indirect Eval (Symbol Evaluation)";
              case 0x30C: 
              case 0x30D: return "Escape token crunch";
              case 0x314:
              case 0x315: return "IRQ Ram Vector";
              case 0x316:
              case 0x317: return "BRK Instr RAM Vector";
              case 0x318:
              case 0x319: return "Indirects for Code";
              case 0x330:
              case 0x331: return "Savesp";
              case 0x3F3:
              case 0x3F4: return "Length of data to be written to tape";
              case 0x3F5:
              case 0x3F6: return "Length of data to be read from tape";
              case 0x4A2:
              case 0x4A3:    
              case 0x4A4: return "Numeric constant for Basic";
              case 0x4E7: return "Print using fill symbol [space]";
              case 0x4E8: return "Print using comma symbol [;]";
              case 0x4E9: return "Print using D.P. symbol  [.]";
              case 0x4EA: return "Print using monetary symbol [$]";
              case 0x4EB:
              case 0x4EC:    
              case 0x4ED:    
              case 0x4EE: return "Temp for instr";
              case 0x4EF: return "Last error number";
              case 0x4F0:    
              case 0x4F1: return "Line # of last error";
              case 0x4F2:    
              case 0x4F3: return "Line to go on error";
              case 0x4F4: return "Hold trap no. temporarily";
              case 0x4FC:    
              case 0x4FD: return "Table of pending jiffies (2's comp)";   
              case 0x508: return "'cold' or 'warm' start status";
              case 0x531: 
              case 0x532: return "Start of memory [1000]";
              case 0x533:
              case 0x534: return "return \"Start of memory [1000]\";";
              case 0x535: return "IEEE timeout flag";
              case 0x536: return "File end reached = 1, 0 otherwise";
              case 0x537: return "# of chars left in buffer (for R & W)";
              case 0x538: return "# of total valid chars in buffer (R)";
              case 0x539: return "Ptr to next char in buffer (for R & W)";
              case 0x53A: return "Contains type of current cass file";
              case 0x53B: return "Active attribute byte";
              case 0x53C: return "Character flash flag";
              case 0x53D: return "Free";
              case 0x53E: return "OC Base location of screen (top) [0C]";
              case 0x540: return "Key repeat flag";
              case 0x543: return "Shift flag byte";
              case 0x544: return "Last shift pattern";
              case 0x545:
              case 0x546: return "Indirect for keyboard table setup";
              case 0x547: return "shift, C="; 
              case 0x548: return "Auto scroll down flag (0=on,0<>off)";
              case 0x54B: return "Monitor non-zpage storage";
              case 0x55B: return "Used by various monitor routines";
              case 0x55D: return "Used for programmable keys";
              case 0x5E7: return "Temp for data write to kennedy";
              case 0x5E8: return "Select for kennedy read or write";
              case 0x5E9: return "Kennedy's dev #";
              case 0x5EA: return "Rennedy present = $ff, else = $00";
              case 0x5EB: return "Temp for type of open for kennedy";
              case 0x5EC: 
              case 0x5ED:
              case 0x5EE: 
              case 0x5EF: return "Physical Address Table";
              case 0x5F0: 
              case 0x5F1: return "Long jump address";
              case 0x5F2: return "Long jump accumulator";
              case 0x5F3: return "Long jump x register";
              case 0x5F4: return "Long jump status register";
              case 0x7B0: return "Byte to be written on tape";
              case 0x7B1: return "Temp for parity calc";
              case 0x7B2: 
              case 0x7B3: return "Temp for write-header";
              case 0x5B5: return "Local index for READBYTE routine: ";
              case 0x5B6: return "Pointer into the error stack";
              case 0x5B7: return "Number of first pass errors";
              case 0x7BE: return "Stack marker for stopkey recover";
              case 0x7BF: return "Stack marker for dropkey recover";
              case 0x7C0:
              case 0x7C1:
              case 0x7C2:
              case 0x7C3: return "Params passed to RDBLOK";
              case 0x7C4: return "Temp stat save for RDBLOK";
              case 0x7C5: return "# consec shorts to find in header";
              case 0x7C6: return "# Errors fatal in RD countdown";
              case 0x7C7: return "Temp for Verify command";
              case 0x7C8:
              case 0x7C9:    
              case 0x7CA:    
              case 0x7CB: return "Pipe temp for T1";
              case 0x7CC: return "Read error propagate";
              case 0x7CD: return "User chracter to send";
              case 0x7CE: return "0 = empty ; 1 = full";
              case 0x7CF: return "System character to send";
              case 0x7D0: return "0 = empty ; 1 = full";
              case 0x7D1: return "Pntr to front of input queue";
              case 0x7D2: return "Pntr to rear of input queue";
              case 0x7D3: return "# of chars in input queue";
              case 0x7D4: return "Temp status for ACIA";
              case 0x7D5: return "Temp for input routine";
              case 0x7D6: return "FLG for local pause";
              case 0x7D7: return "FLG for remote pause";
              case 0x7D8: return "FLG to indicate presence of ACIA";
              case 0x7E5: return "Screen bottom (0...24)";
              case 0x7E6: return "Screen top";
              case 0x7E7: return "Screen left (0...39)";
              case 0x7E8: return "Screen right";
              case 0x7E9: return "Negative = scroll out";
              case 0x7EA: return "Insert mode: FF = on, 00 = off";
              case 0x7F2: return "Registers for SYS command";
              case 0x7F6: return "Key scan index";
              case 0x7F7: return "Flag to disable CTRL-S pause";
              case 0x7F8: return "MSB for monitor fetches from ROM=0;RAM=1";
              case 0x7F9: return "MSB for color/lim table in RAM=0;ROM=1";
              case 0x7FA: return "ROM mask for split screen";
              case 0x7FB: return "VM base mask for split screen";
              case 0x7FC: return "Motor lock semaphore for cassette";
              case 0x7FD: return "PAL tod";    
              case 0xFCF1: 
              case 0xFCF2:
              case 0xFCF3: return "JMP to cartridge IRQ routine";
              case 0xFCF4: 
              case 0xFCF5:
              case 0xFCF6: return "JMP to PHOENIX routine";
              case 0xFCF7: 
              case 0xFCF8:
              case 0xFCF9: return "JMP to LONG FETCH routine";
              case 0xFCFA:   
              case 0xFCFB:
              case 0xFCFC: return "JMP to LONG JUMP routine";    
              case 0xFCFD:   
              case 0xFCFE:
              case 0xFCFF: return "JMP to LONG IRQ routine";                               
              case 0xFD00: return "6551 ACIA: DATA port";
              case 0xFD01: return "6551 ACIA: STATUS port";              
              case 0xFD02: return "6551 ACIA: COMMAND port";
              case 0xFD03: return "6551 ACIA: CONTROL port";
              case 0xFD04:
              case 0xFD08:
              case 0xFD0C: return "6551 ACIA: copy";
              case 0xFD10: return "6529B #1: User Port PIO (P0-P7)";
              case 0xFD30: return "6529B #2: Keyboard PIO  Keyboard Matrix Connector";
              case 0xFF00: return "TED: Timer 1 low";
              case 0xFF01: return "TED: Timer 1 high";
              case 0xFF02: return "TED: Timer 2 low";
              case 0xFF03: return "TED: Timer 2 high";
              case 0xFF04: return "TED: Timer 3 low";
              case 0xFF05: return "TED: Timer 3 high";
              case 0xFF06: return "TED: Test, ECM, BMM, Blank, Rows, Y2, Y1, Y0";
              case 0xFF07: return "TED: RVS off PAL, Freeze, MCM, Columns, X2, X1, X0";
              case 0xFF08: return "TED: Keyboard Latch";
              case 0xFF09: return "TED: Interrupt";
              case 0xFF0A: return "TED: Interrupt mask";
              case 0xFF0B: return "TED: RC7, RC6, RC5, RC4, RC3, RC2, RC1, RC0";
              case 0xFF0C: return "TED: C9, CUR8";
              case 0xFF0D: return "TED: CUR7, CUR6, CUR5, CUR4, CUR3, CUR2, CUR1, CUR0";
              case 0xFF0E: return "TED: Voice #1 frequency, bits 0-7";
              case 0xFF0F: return "TED: Voice #2 frequency, bits 0-7";
              case 0xFF10: return "TED: Voice #2 frequency, bits 8 & 9";
              case 0xFF11: return "TED: Bits 0-3 : Volume control";
              case 0xFF12: return "TED: Bit 0-1 : Voice #1 frequency, bits 8 & 9";
              case 0xFF13: return "TED: Bit 0 Clock status";
              case 0xFF14: return "TED: Bits 3-7 Video matrix/color memory base address";
              case 0xFF15: return "TED: Background color register";
              case 0xFF16: return "TED: Color register #1";
              case 0xFF17: return "TED: Color register #2";
              case 0xFF18: return "TED: Color registes #3";
              case 0xFF19: return "TED: Color registes #4";
              case 0xFF1A: return "TED: Bit map reload high";
              case 0xFF1B: return "TED: Bit map reload low";
              case 0xFF1C: return "TED: Bit 0 : Vertical line bit 8";
              case 0xFF1D: return "TED: Bits 0-7 : Vertical line bits 0-7";
              case 0xFF1E: return "TED: Horizontal position";
              case 0xFF1F: return "TED: Blink, vertical sub address";
              case 0xFF3E: return "ROM select";
              case 0xFF3F: return "RAM select";
              case 0xFF49:   
              case 0xFF4A:
              case 0xFF4B: return "JMP to define function key routine"; 
              case 0xFF4C:   
              case 0xFF4D:
              case 0xFF4E: return "JMP to PRINT routine";   
              case 0xFF4F:   
              case 0xFF50:
              case 0xFF51: return "JMP to PRIMM routine";
              case 0xFF52:   
              case 0xFF53:
              case 0xFF54: return "JMP to ENTRY routine";              
              case 0xFF80: return "Release of Kernel: (MSB: 0=NTSC ; 1=PAL)";
              case 0xFF81: return "Initialize screen editor";
              case 0xFF84: return "Initialize I/O devices";
              case 0xFF87: return "Ram test";
              case 0xFF8A: return "Restore vectors to initial values";
              case 0xFF8D: return "Change vectors for user";
              case 0xFF90: return "Control O.S. messages";
              case 0xFF93: return "Send SA after LISTEN";
              case 0xFF96: return "Send SA after TALK";
              case 0xFF99: return "Set/Read top of memory";
              case 0xFF9C: return "Set/Read bottom of memory";
              case 0xFF9F: return "Scan keyboard";
              case 0xFFA2: return "Set timeout in DMA disk";
              case 0xFFA5: return "Handshake serial bus or DMA disk byte in";
              case 0xFFA8: return "Handshake serial bus or DMA disk byte out";
              case 0xFFAB: return "Send UNTALK out serial bus or DMA disk";
              case 0xFFAE: return "Send UNLISTEN out serial bus or DMA disk";
              case 0xFFB1: return "Send LISTEN out serial bus or DMA disk";
              case 0xFFB4: return "Send TALK out serial bus or DMA disk";
              case 0xFFB7: return "Return I/O STATUS byte";
              case 0xFFBA: return "Set LA, FA, SA";
              case 0xFFBD: return "Set length and FN address";
              case 0xFFC0: return "Open logical file";
              case 0xFFC3: return "Close logical file";
              case 0xFFC6: return "Open channel in";
              case 0xFFC9: return "open channel out";
              case 0xFFCC: return "Close I/O channels";
              case 0xFFCF: return "Input from channel";
              case 0xFFD2: return "output to channel";
              case 0xFFD5: return "Load from file";
              case 0xFFD8: return "Save to file";
              case 0xFFDB: return "Set internal clock";
              case 0xFFDE: return "Read internal clock";
              case 0xFFE1: return "Scan STOP key";
              case 0xFFE4: return "Get character from queue";
              case 0xFFE7: return "Close all files";
              case 0xFFEA: return "Increment clock";
              case 0xFFED: return "Screen org.";
              case 0xFFF0: return "Read/Set X,Y coord of cursor";
              case 0xFFF3: return "Return location of start of I/O";
            default:
                if ((addr>=0xD0) && (addr<=0xD7)) return "Area for use by speech software"; 
                if ((addr>=0xD8) && (addr<=0xE8)) return "Area for use by application software";   
                if ((addr>=0x113) && (addr<=0x122)) return "Color/luminance table in RAM";
                if ((addr>=0x124) && (addr<=0x1FF)) return "System stack";
                if ((addr>=0x200) && (addr<=0x258)) return "Basic/monitor input buffer";
                if ((addr>=0x259) && (addr<=0x25C)) return "Basic storage";
                if ((addr>=0x25E) && (addr<=0x26D)) return "Area for filename";
                if ((addr>=0x27D) && (addr<=0x2AC)) return "Area used to build DOS string";
                if ((addr>=0x333) && (addr<=0x3F2)) return "Cassette tape buffer";
                if ((addr>=0x3F7) && (addr<=0x436)) return "RS-232 input queue";
                if ((addr>=0x494) && (addr<=0x4A1)) return "Shared ROM fetch sub";
                if ((addr>=0x4A5) && (addr<=0x4AF)) return "Txtptr";
                if ((addr>=0x4B0) && (addr<=0x4BA)) return "Index & Index1";
                if ((addr>=0x4BB) && (addr<=0x4C5)) return "Index2";
                if ((addr>=0x4C6) && (addr<=0x4D0)) return "Strng1";
                if ((addr>=0x4D1) && (addr<=0x4DB)) return "Lowtr";
                if ((addr>=0x4DC) && (addr<=0x4E6)) return "Facmo";
                if ((addr>=0x509) && (addr<=0x512)) return "Logical file numbers";
                if ((addr>=0x513) && (addr<=0x51C)) return "Primary device numbers";
                if ((addr>=0x51D) && (addr<=0x526)) return "Secondary addresses";
                if ((addr>=0x527) && (addr<=0x530)) return "IRQ keyboard buffer";   
                if ((addr>=0x55F) && (addr<=0x556)) return "Table of P.F. lengths";  
                if ((addr>=0x567) && (addr<=0x5E6)) return "P.F. Key storage area";  
                if ((addr>=0x5F5) && (addr<=0x65D)) return "RAM areas for banking";  
                if ((addr>=0x65E) && (addr<=0x6EB)) return "RAM area for speech";  
                if ((addr>=0x6EC) && (addr<=0x7AF)) return "BASIC run-time stack";  
                if ((addr>=0x7B8) && (addr<=0x7BD)) return "Time constant";
                if ((addr>=0x7D9) && (addr<=0x7E4)) return "Indirect routine downloaded";
                if ((addr>=0x800) && (addr<=0xBFF)) return "Color memory (Text)";
                if ((addr>=0xC00) && (addr<=0xCFF)) return "Video matrix (Text)";
                if ((addr>=0x1000) && (addr<=0x17FF)) return "BASIC RAM (without graphics)";
                if ((addr>=0x1800) && (addr<=0x1BFF)) return "Luminance for bit map screen";
                if ((addr>=0x1C00) && (addr<=0x1FFF)) return "Color for bit map";
                if ((addr>=0x2000) && (addr<=0x3FFF)) return "Graphics screen data";
                if ((addr>=0x4000) && (addr<=0x7FFF)) return "BASIC RAM (with graphics)";
                if ((addr>=0x8000) && (addr<=0xBFFF)) return "BASIC ROM";
                if ((addr>=0xC000) && (addr<=0xCFFF)) return "BASIC Expansion";
                if ((addr>=0xD000) && (addr<=0xD7FF)) return "Character table";
                if ((addr>=0xFD00) && (addr<=0xFD0F)) return "6551 ACIA";
                if ((addr>=0xFD10) && (addr<=0xFD1F)) return "6529B #1";
                if ((addr>=0xFD30) && (addr<=0xFD3F)) return "6529B #2";
            }
        }
    }  
    return super.dcom(iType, aType, addr, value);
  }             
}
