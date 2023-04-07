/**
 * @(#)cRSID.java 2023/03/19
 *
 * ICE Team free software group
 *
 * This file is part of JIIT64 Java Ice Team Tracker 64
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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * CRSID class of CRSID original by Hermit
 * 
 * @author ice00
 */
public class CRSID extends Thread {
  C64 c64;
  
  public static final char PowersOf2[] = {0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80};    
  
  //private final int BUFFER_SIZE = 16384;
  private final int SAMPLE_RATE = 44100;
  private final int SAMPLE_SIZE_IN_BITS = 16;
  private final int CHANNELS = 2;
  private final boolean SIGNED = true;
  private final boolean BIG_ENDIAN = false;
  
  private boolean playing=false;
  private boolean paused=false;

  //AudioFormat format;
  //DataLine.Info info;
  SourceDataLine line;
  
  
  
  public CRSID() {
    init(44100);
    start();
  }
  
  
  public void init(int samplerate) {
    c64=new C64(samplerate);

    c64.highQualitySID=true; 
    c64.stereo=0; 
    c64.selectedSIDmodel=0;
    c64.playbackSpeed=1; //default model and mode selections
    c64.mainVolume=255;

    //if ( cRSID_initSound (C64, samplerate,buflen) == NULL) return NULL;
  }
  
  public void initSIDtune(PSID psid, int subtune) { //subtune: 1..255
    int InitTimeout;
    
    if (subtune == 0) {
      subtune = 1;
    } else if (subtune > psid.subtuneAmount) {
              subtune = psid.subtuneAmount;
           }
    c64.subTune = subtune;
    c64.secondCnt = c64.playTime = /*c64.Paused =*/ 0;
    
    Memory.instance.clear();

    c64.setC64(psid);
    c64.initC64();
    //cRSID_writeMemC64(C64,0xD418,0xF); //set C64 hardware and initC64 (reset) it

    //determine initC64-address:
    c64.initAddress = ((psid.initAddressH) << 8) + (psid.initAddressL); //get info from BASIC-startupcode for some tunes
    if (c64.ramBank[1] == 0x37) { //are there SIDs with routine under IO area? some PSIDs don't set bank-registers themselves
      if ((0xA000 <= c64.initAddress && c64.initAddress < 0xC000)
              || (c64.loadAddress < 0xC000 && c64.endAddress >= 0xA000)) {
        c64.ramBank[1] = 0x36;
      } else if (c64.initAddress >= 0xE000 || c64.endAddress >= 0xE000) {
        c64.ramBank[1] = 0x35;
      }
    }
    c64.cpu.initCPU(c64.initAddress); //prepare initC64-routine call
    c64.cpu.A = subtune - 1;

    if (!c64.realSIDmode) {
      //call initC64-routine:
      for (InitTimeout = 10000000; InitTimeout > 0; InitTimeout--) {
        if ((c64.cpu.emulateCPU()&0xFF) >= 0xFE) {
          break;
        }
      } //give error when timed out?
    }

    //determine timing-source, if CIA, replace frameCycles previouisly set to VIC-timing
    if (subtune > 32) {
      c64.timerSource = psid.subtuneTimeSources[0] & 0x80; //subtunes above 32 should use subtune32's timing
    } else {
      c64.timerSource = psid.subtuneTimeSources[(32 - subtune) >> 3] & PowersOf2[(subtune - 1) & 7];
    }
    if (c64.timerSource!=0 || c64.ioBankWR[0xDC05] != 0x40 || c64.ioBankWR[0xDC04] != 0x24) { //CIA1-timing (probably multispeed tune)
      c64.frameCycles = ((c64.ioBankWR[0xDC04] + (c64.ioBankWR[0xDC05] << 8))); //<< 4) / C64->ClockRatio;
      c64.timerSource = 1; //if initC64-routine changed DC04 or DC05, assume CIA-timing
    }
    //determine playaddress:
    c64.playAddress = (psid.playAddressH << 8) + psid.playAddressL;
    if (c64.playAddress!=0) { //normal play-address called with JSR
      if (c64.ramBank[1] == 0x37) { //are there SIDs with routine under IO area?
        if (0xA000 <= c64.playAddress && c64.playAddress < 0xC000) {
          c64.ramBank[1] = 0x36;
        }
      } else if (c64.playAddress >= 0xE000) {
        c64.ramBank[1] = 0x35; //player under KERNAL (e.g. Crystal Kingdom Dizzy)
      }
    } else { //IRQ-playaddress for multispeed-tunes set by initC64-routine (some tunes turn off KERNAL ROM but doesn't set irq-vector!)
      c64.playAddress = (c64.ramBank[1] & 3) < 2 ? c64.readMemC64(0xFFFE) + (c64.readMemC64(0xFFFF) << 8) //for PSID
              : c64.readMemC64(0x314) + (c64.readMemC64( 0x315) << 8);
      if (c64.playAddress == 0) { //if 0, still try with RSID-mode fallback
        c64.cpu.initCPU(c64.playAddress); //point CPU to play-routine
        c64.finished = true;
        c64.returned = 1;
        return;
      }
    }

    if (!c64.realSIDmode) {  //prepare (PSID) play-routine playback:
      c64.cpu.initCPU(c64.playAddress); //point CPU to play-routine
      c64.frameCycleCnt = 0;
      c64.finished = true;
      c64.sampleCycleCnt = 0; //C64->CIAisSet=0;
    } else {
        c64.finished = false;
        c64.returned = 0;
    }
  }
  
  /**
   * Genewrate a sample
   * 
   * @return the stereo sample
   */
  public Output generateSample() { //call this from custom buffer-filler
    Output output;
    int PSIDdigi;

    output = c64.emulateC64();
    
    if (c64.psidDigiMode) {
      PSIDdigi = c64.playPSIDdigi();
      output.L += PSIDdigi;
      output.R += PSIDdigi;
    }
    if (output.L >= 32767) {
      output.L = 32767;
    } else if (output.L <= -32768) {
      output.L = -32768; //saturation logic on overflow
    }
    if (output.R >= 32767) {
      output.R = 32767;
    } else if (output.R <= -32768) {
      output.R = -32768; //saturation logic on overflow
    }
    return output;
  }
  
  /**
   * Genrate the sound from emulation
   * 
   * @param buf the buffer to fill with sound
   */
  public void generateSound(byte[] buf) {

    Output output=null;

    for (int i = 0; i < buf.length; i += 4) {
      for (int j = 0; j < c64.playbackSpeed; ++j) {
        output = generateSample();
      }
      output.L = output.L * c64.mainVolume / 256;
      output.R = output.R * c64.mainVolume / 256;

      buf[i + 0] = (byte)(output.L & 0xFF);
      buf[i + 1] = (byte)(output.L >> 8);
      buf[i + 2] = (byte)(output.R & 0xFF);
      buf[i + 3] = (byte)(output.R >> 8);
    }
  }
  
  /**
   * Play the given sid file 
   * 
   * @param filename the file to play
   * @param subtune the tune to play
   */
  public void playSIDfile(String filename, int subtune) {
    
    PSID psid=new PSID();
    psid.loadSIDtune(c64,filename);

    initSIDtune(psid, subtune);    
    
    playing = true;
    paused = false;
    c64.playbackSpeed=1;    
  }
  
  @Override
  public void run() {
    try {
     while(true) {  
      byte[] buffer = new byte[/*Shared.option.getBufferSize()*/ 16384 * CHANNELS];
      
      c64.highQualitySID=true; //Shared.option.isCrsidHighQuality();

      AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
      line = (SourceDataLine) AudioSystem.getLine(info);
      line.open(format);
      line.start();
      
      while (playing) {
        if (paused) {
          Thread.sleep(100);
          continue;
        }
        generateSound(buffer);                
        line.write(buffer, 0, buffer.length);
      }
      
      while (!playing) {
          Thread.sleep(100);
          continue;
        }   
     }  
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (line != null) {
        line.drain();
        line.close();
      }
    }
  }
  
  /**
   * Fast play (or if already fast, goews normal
   */ 
  public synchronized void fPlay() {
    if (c64.playbackSpeed>1) c64.playbackSpeed=1;
    else c64.playbackSpeed = 4;
  }
  
  /**
   * Pause the play of tune
   */
  public synchronized void pausePlaying() {
    //c64.Paused^=1; 
    //if(c64.Paused) paused=true; 
    //else pause=false; 
    paused=!paused;
  }
   
  /**
   * Stop the play of tune
   */ 
  public synchronized void stopPlaying() {
    playing = false;
  }
  
  /**
   * Played time in second
   * 
   * @return the played time
   */
  public int time() {
    return c64.playTime;
  }
  
  /**
   * Set the voice to mute/unmute
   * 
   * @param voice the voice 
   * @param mute the mute state
   */
  public void setVoiceMute(int voice, boolean mute) {
    switch (voice) {
      case 0:
        c64.sid[0].muteVoice1=mute;
        c64.sid[1].muteVoice1=mute;
        c64.sid[2].muteVoice1=mute;
        c64.sid[3].muteVoice1=mute;
        break;
      case 1:
        c64.sid[0].muteVoice2=mute;
        c64.sid[1].muteVoice2=mute;
        c64.sid[2].muteVoice2=mute;
        c64.sid[3].muteVoice2=mute;
        break;          
      case 2:
        c64.sid[0].muteVoice3=mute;
        c64.sid[1].muteVoice3=mute;
        c64.sid[2].muteVoice3=mute;
        c64.sid[3].muteVoice3=mute;
        break  ;
    }
  }
  
  
  public static void main(String args[]) {  
    CRSID cr=new CRSID();
    
    cr.init(44100);
    cr.playSIDfile(args[0], 1);
           // "/home/ice/hvsids/MUSICIANS/B/Bjerregaard_Johannes/Sweet.sid",1);
            
            //args[0], 1); //"/home/ice/hvsids/MUSICIANS/B/Bjerregaard_Johannes/Sweet.sid", 1);

  } 
}