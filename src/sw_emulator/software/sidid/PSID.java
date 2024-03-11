/**
 * @(#)PSID64.java 2023/03/19
 *
 * ICE Team free software group
 *
 * This file is part of C64 Java Software Emulator.
 * See README for copyright notice.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 */
package sw_emulator.software.sidid;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * PSID file of cSID original by Hermit
 * 
 * @author ice
 */
public class PSID {
  String magicString;                   //$00 - "PSID" or "RSID" (RSID must provide Reset-circumstances & CIA/VIC-interrupts)
  int versionH00;                       //$04
  int version;                          //$05 - 1 for PSID v1, 2..4 for PSID v2..4 or RSID v2..4 (3/4 has 2SID/3SID support), 0x4E for 4SID (WebSID-format)
  int headerSizeH00;                    //$06
  int headerSize;                       //$07 - $76 for v1, $7C for v2..4, with WebSID-format: $7E for 2SID, $80 for 3SID, $82 for 4SID (depends on number of SIDs)
  int loadAddressH, loadAddressL;       //$08 - if 0 it's a PRG and its loadaddress is used (RSID: 0, PRG-loadaddress>=$07E8)
  int initAddressH, initAddressL;       //$0A - if 0 it's taken from load-address (but should be set) (RSID: don't point to ROM, 0 if BASICflag set)
  int playAddressH, playAddressL;       //$0C - if 0 play-routine-call is set by the initializer (always true for RSID)
  int subtuneAmountH00;                 //$0E
  int subtuneAmount;                    //$0F - 1..256
  int defaultSubtuneH00;                //$10
  int defaultSubtune;                   //$11 - 1..256 (optional, defaults to 1)
  int[] subtuneTimeSources=new int[4];  //$12 - 0:Vsync / 1:CIA1 (for PSID) (LSB is subtune1, MSB above 32) , always 0 for RSID
  String title;                         //$16 - strings are using 1252 codepage
  String author;                        //$36
  String releaseInfo;                   //$56
  //SID v2 additions:                              (if SID2/SID3 model is set to unknown, they're set to the same model as SID1)
  int modelFormatStandardH;             //$76 - bit9&8/7&6/5&4: SID3/2/1 model (00:?,01:6581,10:8580,11:both) (4SID:bit6=SID1-channel), bit3&2:videoStandard..
  int modelFormatStandard;              //$77 ..(01:PAL,10:NTSC,11:both), bit1:(0:C64,1:PlaySIDsamples/RSID_BASICflag), bit0:(0:builtin-player,1:MUS)
  int relocStartPage;                   //$78 - v2NG specific, if 0 the SID doesn't write outside its data-range, if $FF there's no place for driver
  int relocFreePages;                   //$79 - size of area from relocStartPage for driver-relocation (RSID: must not contain ROM or 0..$3FF)
 
  int sid2baseAddress;                  //$7A - (SID2BASE-$d000)/16 //SIDv3-relevant, only $42..$FE values are valid ($d420..$DFE0), else no SID2
  int sid2flagsH;                       //$7A: address of SID2 in WebSID-format too (same format as sid2baseAddress in HVSC format)
 
  int sid3baseAddress;                  //$7B - (SID3BASE-$d000)/16 //SIDv4-relevant, only $42..$FE values are valid ($d420..$DFE0), else no SID3
  int sid2flagsL;                       //$7B: flags for WebSID-format, bit6: output-channel (0(default):left, 1:right, ?:both?), bit5..4:SIDmodel(00:setting,01:6581,10:8580,11:both)
                                        //   my own (implemented in SID-Wizard too) proposal for channel-info: bit7 should be 'middle' channel-flag (overriding bit6 left/right)
 
  //WebSID-format (with 4 and more SIDs -support) additional fields: for each extra SID there's an 'nSIDflags' byte-pair
  int sid3flagsH, sid3flagsL; //$7C,$7D: the same address/flag-layout for SID3 as with SID2
  int sid4flagsH; 
  int sid4baseAddress; //$7E
  int sid4flagsL; //$7F: the same address/flag-layout for SID4 as with SID2
   //... repeated for more SIDs, and end the list with $00,$00 (this determines the amount of SIDs)
 
  
  public static final int CRSID_CHANNEL_LEFT=1;
  public static final int CRSID_CHANNEL_RIGHT=2;
  public static final int CRSID_CHANNEL_BOTH=3;  
  
  public static final int CRSID_SIDCOUNT_MAX=4;
  public static final int CRSID_CIACOUNT=2;
  public static final int CRSID_FILEVERSION_WEBSID=0x4E;
  
  public static final int CRSID_FILESIZE_MAX=1000000;
 
  public static final String MagicStringPSID="PSID";
  
  public C64 init(int samplerate, int buflen) {
    C64 c64=new C64(samplerate);
    
    c64.highQualitySID=true; 
    c64.stereo=0; 
    c64.selectedSIDmodel=0; 
    c64.playbackSpeed=1; //default model and mode selections
    c64.mainVolume=255;
     //if ( cRSID_initSound (C64, samplerate,buflen) == NULL) return NULL;

    return c64;  
  }
  
  public void processSIDfile(C64 c64, byte[] filedata, int filesize) {
    int i;
    int sidDataOffset;

        
    magicString=""+(char)filedata[0]+(char)filedata[1]+(char)filedata[2]+(char)filedata[3];          
    versionH00=filedata[4] & 0xFF;
    version=filedata[5] & 0xFF;
    headerSizeH00=filedata[6] & 0xFF;
    headerSize=filedata[7] & 0xFF;
    loadAddressH=filedata[8] & 0xFF;
    loadAddressL=filedata[9] & 0xFF;
    initAddressH=filedata[0xA] & 0xFF;
    initAddressL=filedata[0xB] & 0xFF;
    playAddressH=filedata[0xC] & 0xFF;
    playAddressL=filedata[0xD] & 0xFF;
    subtuneAmountH00=filedata[0xE] & 0xFF;
    subtuneAmount=filedata[0xF] & 0xFF;
    defaultSubtuneH00=filedata[0x10] & 0xFF;
    defaultSubtune=filedata[0x11] & 0xFF;
    subtuneTimeSources[0]=filedata[0x12] & 0xFF;
    subtuneTimeSources[1]=filedata[0x13] & 0xFF;
    subtuneTimeSources[2]=filedata[0x14] & 0xFF;
    subtuneTimeSources[3]=filedata[0x15] & 0xFF;
   
    title="";
    for (i=0x16; i<0x16+32; i++) {
      title+=(char)filedata[i];
    }
    
    author="";
    for (i=0x36; i<0x36+32; i++) {
      author+=(char)filedata[i];
    }
    
    releaseInfo="";
    for (i=0x56; i<0x56+32; i++) {
      releaseInfo+=(char)filedata[i];
    }
    
    modelFormatStandardH=filedata[0x76] & 0xFF;
    modelFormatStandard=filedata[0x77] & 0xFF;
    relocStartPage=filedata[0x78] & 0xFF;
    relocFreePages=filedata[0x79] & 0xFF;
    sid2baseAddress=filedata[0x7A] & 0xFF;
    sid2flagsH=filedata[0x7A] & 0xFF;
    sid3baseAddress=filedata[0x7B] & 0xFF;
    sid2flagsL=filedata[0x7B] & 0xFF;
    sid3flagsH=filedata[0x7C] & 0xFF;
    sid3flagsL=filedata[0x7D] & 0xFF;
    sid4flagsH=filedata[0x7E] & 0xFF;
    sid4baseAddress=filedata[0x7E] & 0xFF;
    sid4flagsL=filedata[0x7F] & 0xFF;
    
    for (i = 0x0000; i < 0xA000; ++i) {
      c64.ramBank[i] = 0; //fresh start (maybe some bugged SIDs want 0 at certain RAM-locations)
    }
    for (i = 0xC000; i < 0xD000; ++i) {
      c64.ramBank[i] = 0;
    }

    if (magicString.charAt(0) != 'P' && magicString.charAt(0) != 'R') {
      return;
    }
    for (i = 1; i <MagicStringPSID.length() - 1; ++i) {
      if (magicString.charAt(i) != MagicStringPSID.charAt(i)) {
        return;
      }
    }
    c64.realSIDmode = (magicString.charAt(0) == 'R');

    if (loadAddressH == 0 && loadAddressH == 0) { //load-address taken from first 2 bytes of the C64 PRG
      c64.loadAddress = ((filedata[headerSize + 1] & 0xff) << 8) + ((filedata[headerSize + 0]&0xFF));
      sidDataOffset = headerSize + 2;
    } else { //load-adress taken from SID-header
      c64.loadAddress = (loadAddressH << 8) + (loadAddressL);
      sidDataOffset = headerSize;
    }

    for (i = sidDataOffset; i < filesize; ++i) {
      c64.ramBank[c64.loadAddress + (i - sidDataOffset)] = filedata[i] &0xFF;
    }

    i = c64.loadAddress + (filesize - sidDataOffset);
    c64.endAddress = (i < 0x10000) ? i : 0xFFFF;

    c64.psidDigiMode = (!c64.realSIDmode && ((modelFormatStandard & 2)!=0));
  }
  
  /**
   * Load the sid file 
   * 
   * @param filename the file to read
   * @return the file or null
   */
  byte[] loadSIDfile (String filename) {
    byte[] data;
            
    try {
      data=Files.readAllBytes(Path.of(filename));
    } catch (IOException ex) {
        System.err.println(ex);
        data=null;
    }
            
    return data;
  }
  
  /**
   * Load and process the sid file
   * 
   * @param c64 the emulator
   * @param filename the file name
   */
  public void loadSIDtune(C64 c64, String filename) {
    byte[] SIDfileData=null; //use memset?

    SIDfileData=loadSIDfile(filename);   

    processSIDfile(c64, SIDfileData, SIDfileData.length );
  }
  
  /**
   * Get the max tune into the SID
   * 
   * @return the max tune number
   */
  public int getMaxTune() {
      return subtuneAmount;
  }
}

