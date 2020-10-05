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
              case 0x8D: return "Temporary area for integer division. (Used to compute the side sector of relative files.)";
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
              case 0x1803: return "Port A data direction register. Default: $FF, %11111111.";
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
              case 0x1C03: return "Port A #2 data direction registe";
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
