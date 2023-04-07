/**
 * @(#)C64.java 2023/03/19
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


class Output {
  int L;
  int R;
};

/**
 * C64 class of cRSID original by Hermit
 * 
 * @author ice00
 */
public class C64 {
  //platform-related:
  int sampleRate;
  //int bufferSize;
  boolean highQualitySID;
  char sidChipCount;
  char stereo;
  char playbackSpeed;
  //char Paused;
  //C64-machine related:
  int videoStandard; //0:NTSC, 1:PAL (based on the sid-header field)
  int cpuFrequency;
  int sampleClockRatio; //ratio of cpu-clock and samplerate
  int selectedSIDmodel;
  char mainVolume;
  //SID-file related:
  
  private PSID psid;
  //union {
  // cRSID_SIDheader*  SIDheader;
  // char*             SIDfileData;

  int attenuation;
  boolean realSIDmode;
  boolean psidDigiMode;
  int subTune;
  int loadAddress;
  int initAddress;
  int playAddress;
  int endAddress;
  int timerSource; //for current subtune, 0:VIC, 1:cia (as in sid-header)
  //PSID-playback related:
  //char SoundStarted;
  //char              CIAisSet; //for dynamic cia setting from player-routine (RealSID substitution)
  int frameCycles;
  int frameCycleCnt; //this is a substitution in PSID-mode for cia/VIC counters
  int prevRasterLine;
  int sampleCycleCnt;
  int overSampleCycleCnt;
  int tenthSecondCnt;
  int secondCnt;
  int playTime;
  boolean finished;
  char returned;
  char irq; //collected irq line from devices
  char nmi; //collected nmi line from devices
  
  int periodCounter=0;
  int sampleAddress=0;

  //Hardware-elements:
  CPU cpu;
  SID[] sid=new SID[4]; //CRSID_SIDCOUNT_MAX+1];
  CIA[] cia=new CIA[2]; //CRSID_CIACOUNT+1];
  VIC vic;
  //Overlapping system memories, which one is read/written in an address region depends on cpu-port bankselect-bits)
  //Address $00 and $01 - data-direction and data-register of port built into cpu (used as bank-selection) (overriding RAM on c64)
  int[] ramBank=new int[0x10100];  //$0000..$FFFF RAM (and RAM under IO/ROM/CPUport)
  int[] ioBankWR=new int[0x10100]; //$D000..$DFFF IO-RAM (registers) to write (VIC/SID/CIA/ColorRAM/IOexpansion)
  int[] ioBankRD=new int[0x10100]; //$D000..$DFFF IO-RAM (registers) to read from (VIC/SID/CIA/ColorRAM/IOexpansion)
  int[] romBanks=new int[0x10100]; //$1000..$1FFF/$9000..$9FFF (CHARGEN), $A000..$BFFF (BASIC), $E000..$FFFF (KERNAL)    
  
  short ROM_IRQreturnCode[] = {0xAD,0x0D,0xDC,0x68,0xA8,0x68,0xAA,0x68,0x40}; //CIA1-acknowledge irq-return
  short ROM_NMIstartCode[] = {0x78,0x6c,0x18,0x03,0x40}; //SEI and jmp($0318)
  short ROM_IRQBRKstartCode[] = { //Full irq-return (handling BRK with the same RAM vector as irq)
     0x48,0x8A,0x48,0x98,0x48,0xBA,0xBD,0x04,0x01,0x29,0x10,0xEA,0xEA,0xEA,0xEA,0xEA,0x6C,0x14,0x03
  };
  
  public static final int C64_PAL_CPUCLK=985248;
  public static final int C64_NTSC_CPUCLK=1022727;
  public static final int DEFAULT_SAMPLERATE=44100;

  public static final int C64_PAL_SCANLINES = 312;
  public static final int C64_NTSC_SCANLINES = 263;
  
  public static final int C64_PAL_SCANLINE_CYCLES = 63;
  public static final int C64_NTSC_SCANLINE_CYCLES = 65;
  
  public static final int CPUspeeds[] = { C64_NTSC_CPUCLK, C64_PAL_CPUCLK };
  public static final int ScanLines[] = { C64_NTSC_SCANLINES, C64_PAL_SCANLINES };
  public static final int ScanLineCycles[] = { C64_NTSC_SCANLINE_CYCLES, C64_PAL_SCANLINE_CYCLES };
  
  public static final int Attenuations[]={0,26,43,137}; //increase for 2SID (to 43) and 3SID (to 137)
  
  public static final int DIGI_VOLUME = 1200;
  
  public static final int OVERSAMPLING_RATIO=7;
  public static final int OVERSAMPLING_CYCLES = ((C64_PAL_CPUCLK/DEFAULT_SAMPLERATE)/OVERSAMPLING_RATIO);
 
 
  public C64(int samplerate) { //init a basic PAL c64 instance
    if (samplerate!=0) {
      sampleRate = samplerate;
    } else {
       sampleRate = samplerate = DEFAULT_SAMPLERATE;
      }
   
    sampleClockRatio = (C64_PAL_CPUCLK << 4) / samplerate; //shifting (multiplication) enhances sampleClockRatio precision
    attenuation = 26;
    sidChipCount = 1;

    //default c64 setup with only 1 sid and 2 CIAs and 1 VIC
    cpu=new CPU(this);
    sid[0]=new SID(this, 8580, PSID.CRSID_CHANNEL_BOTH, 0xD400); 
    cia[0]=new CIA(this, 0xDC00);
    cia[1]=new CIA(this, 0xDD00);
    vic=new VIC(this, 0xD000);
    
    setROMcontent();    
    initC64();
  }
  
  /**
   * set hardware-parameters (Models, SIDs) for playback of loaded SID-tune
   */
  void setC64(PSID psid) {  
    int sidModel;
    int sidChipCount;
    int sidChannel;
    
    this.psid=psid;
   
    videoStandard = (((psid.modelFormatStandard & 0x0C) >> 2) != 2) ? 1:0;
    if (sampleRate == 0) {
      sampleRate = 44100;
    }
    cpuFrequency = CPUspeeds[videoStandard];
    sampleClockRatio = (cpuFrequency << 4) / sampleRate; //shifting (multiplication) enhances sampleClockRatio precision

    vic.rasterLines = ScanLines[videoStandard];
    vic.rasterRowCycles = ScanLineCycles[videoStandard];
    frameCycles = vic.rasterLines * vic.rasterRowCycles; ///SampleRate / PAL_FRAMERATE; //1x speed tune with VIC Vertical-blank timing

    prevRasterLine = -1; //so if $d012 is set once only don't disturb frameCycleCnt

    sidModel = ((psid.modelFormatStandard & 0x30) >= 0x20) ? 8580 : 6581;
    
    sid[0].chipModel = (selectedSIDmodel!=0) ? selectedSIDmodel : sidModel;


    if (psid.version != PSID.CRSID_FILEVERSION_WEBSID) {

      sid[0].channel = PSID.CRSID_CHANNEL_LEFT;

      sidModel = psid.modelFormatStandard & 0xC0;
      if (sidModel!=0) {
        sidModel = (sidModel >= 0x80) ? 8580 : 6581;
      } else {
        sidModel = sid[0].chipModel;
      }
      if (selectedSIDmodel!=0) {
        sidModel = selectedSIDmodel;
      }
      sid[1]=new SID(this, sidModel, PSID.CRSID_CHANNEL_RIGHT, 0xD000 + psid.sid2baseAddress * 16);

      sidModel = psid.modelFormatStandardH & 0x03;
      if (sidModel!=0) {
        sidModel = (sidModel >= 0x02) ? 8580 : 6581;
      } else {
        sidModel = sid[0].chipModel;
      }
      if (selectedSIDmodel!=0) {
        sidModel = selectedSIDmodel;
      }
      sid[2]=new SID(this, sidModel, PSID.CRSID_CHANNEL_BOTH, 0xD000 + psid.sid3baseAddress * 16);

      sid[3]=new SID(this, sidModel, 0, 0); //ensure disabling SID4 in non-WebSID format

    } else {

      sid[0].channel = ((psid.modelFormatStandardH & 0x40)!=0) ? PSID.CRSID_CHANNEL_RIGHT : PSID.CRSID_CHANNEL_LEFT;
      if ((psid.modelFormatStandardH & 0x80)!=0) {
        sid[0].channel = PSID.CRSID_CHANNEL_BOTH; //my own proposal for 'middle' channel
      }
      sidModel = psid.sid2flagsL & 0x30;
      sidChannel = ((psid.sid2flagsL & 0x40)!=0) ? PSID.CRSID_CHANNEL_RIGHT : PSID.CRSID_CHANNEL_LEFT;
      if ((psid.sid2flagsL & 0x80)!=0) {
        sidChannel = PSID.CRSID_CHANNEL_BOTH;
      }
      if (sidModel!=0) {
        sidModel = (sidModel >= 0x20) ? 8580 : 6581;
      } else {
        sidModel = sid[0].chipModel;
      }
      if (selectedSIDmodel!=0) {
        sidModel = selectedSIDmodel;
      }
      sid[1]=new SID(this, sidModel, sidChannel, 0xD000 + psid.sid2baseAddress * 16);

      sidModel = psid.sid3flagsL & 0x30;
      sidChannel = ((psid.sid3flagsL & 0x40)!=0) ? PSID.CRSID_CHANNEL_RIGHT : PSID.CRSID_CHANNEL_LEFT;
      if ((psid.sid3flagsL & 0x80)!=0) {
        sidChannel = PSID.CRSID_CHANNEL_BOTH;
      }
      if (sidModel!=0) {
        sidModel = (sidModel >= 0x20) ? 8580 : 6581;
      } else {
        sidModel = sid[0].chipModel;
      }
      if (selectedSIDmodel!=0) {
        sidModel = selectedSIDmodel;
      }
      sid[2]=new SID(this, sidModel, sidChannel, 0xD000 + psid.sid3flagsH * 16);

      sidModel = psid.sid4flagsL & 0x30;
      sidChannel = ((psid.sid4flagsL & 0x40)!=0) ? PSID.CRSID_CHANNEL_RIGHT : PSID.CRSID_CHANNEL_LEFT;
      if ((psid.sid4flagsL & 0x80)!=0) {
        sidChannel = PSID.CRSID_CHANNEL_BOTH;
      }
      if (sidModel!=0) {
        sidModel = (sidModel >= 0x20) ? 8580 : 6581;
      } else {
        sidModel = sid[0].chipModel;
      }
      if (selectedSIDmodel!=0) {
        sidModel = selectedSIDmodel;
      }
      sid[3]=new SID(this, sidModel, sidChannel, 0xD000 + psid.sid4baseAddress * 16);
    }


    sidChipCount = 1 + 
                   ((sid[1].baseAddress > 0)?1:0) + 
                   ((sid[2].baseAddress > 0)?1:0) + 
                   ((sid[3].baseAddress > 0)?1:0);
    if (sidChipCount == 1) sid[0].channel = PSID.CRSID_CHANNEL_BOTH;
    attenuation = Attenuations[sidChipCount];
  }
  
  /**
   * C64 Reset
   */
  void initC64() { 
    sid[0].initChip();
    cia[0].initChip();
    cia[1].initChip();

    initMem();
    cpu.initCPU((readMemC64(0xFFFD) << 8) + readMemC64(0xFFFC));
    irq = nmi = 0;
    
    if (highQualitySID) {
      sid[0].nonFiltedSample = sid[0].filterInputSample = 0;
      sid[1].nonFiltedSample = sid[1].filterInputSample = 0;
      sid[2].nonFiltedSample = sid[2].filterInputSample = 0;
      sid[3].nonFiltedSample = sid[3].filterInputSample = 0;
      sid[0].prevNonFiltedSample = sid[0].prevFilterInputSample = 0;
      sid[1].prevNonFiltedSample = sid[1].prevFilterInputSample = 0;
      sid[2].prevNonFiltedSample = sid[2].prevFilterInputSample = 0;
      sid[3].prevNonFiltedSample = sid[3].prevFilterInputSample = 0;
    }
    sampleCycleCnt = overSampleCycleCnt = 0;
  }
  
  public Output emulateC64() {
    byte InstructionCycles;
    int HQsampleCount=0;
    int Tmp;
    
    Output output=new Output();
    SIDwavOutput sidWavOutput;

    //Cycle-based part of emulations:
    while (sampleCycleCnt <= sampleClockRatio) {

      if (!realSIDmode) {
        if (frameCycleCnt >= frameCycles) {
          frameCycleCnt -= frameCycles;
          if (finished) { //some tunes (e.g. Barbarian, A-Maze-Ing) doesn't always finish in 1 frame
            cpu.initCPU(playAddress); //(PSID docs say bank-register should always be set for each call's region)
            finished = false; //SampleCycleCnt=0; //PSID workaround for some tunes (e.g. Galdrumway):
            if (timerSource == 0) {
              ioBankRD[0xD019] = (byte)0x81; //always simulate to player-calls that VIC-irq happened
            } else {
              ioBankRD[0xDC0D] = (byte)0x83; //always simulate to player-calls that CIA TIMERA/TIMERB-irq happened
            }
          }
        }
        if (!finished) {
          InstructionCycles = cpu.emulateCPU();
          if ((InstructionCycles & 0xFF) >= 0xFE) {
            InstructionCycles = 6;
            finished = true;
          }
        } else {
           InstructionCycles = 7; //idle between player-calls
          }
        frameCycleCnt += InstructionCycles;
        ioBankRD[0xDC04] += InstructionCycles; //very simple CIA1 TimerA simulation for PSID (e.g. Delta-Mix_E-Load_loader)
      } else { //RealSID emulations:
        if (cpu.handleCPUinterrupts()) {
          finished = false;
          InstructionCycles = 7;
        } else if (!finished) {
          InstructionCycles = cpu.emulateCPU();
          if ((InstructionCycles & 0xFF) >= 0xFE) {
            InstructionCycles = 6;
            finished = true;
          }
        } else {
           InstructionCycles = 7; //idle between irq-calls
          }
        irq = nmi = 0; //prepare for collecting irq sources
        irq |= cia[0].emulateCIA(InstructionCycles);
        nmi |= cia[1].emulateCIA(InstructionCycles);
        irq |= vic.emulateVIC(InstructionCycles);
      }
      sampleCycleCnt += (InstructionCycles << 4);

      sid[0].emulateADSRs(InstructionCycles);
      if (sid[1].baseAddress != 0) {        
        sid[1].emulateADSRs(InstructionCycles);
      }
      if (sid[2].baseAddress != 0) {
        sid[2].emulateADSRs(InstructionCycles);      
      }
      if (sid[3].baseAddress != 0) {
        sid[3].emulateADSRs(InstructionCycles);      
      }
    }
    sampleCycleCnt -= sampleClockRatio;
    
    if (highQualitySID) { //oversampled waveform-generation
      HQsampleCount = 0;
      sid[0].nonFiltedSample = sid[0].filterInputSample = 0;
      sid[1].nonFiltedSample = sid[1].filterInputSample = 0;
      sid[2].nonFiltedSample = sid[2].filterInputSample = 0;
      sid[3].nonFiltedSample = sid[3].filterInputSample = 0;

      while (overSampleCycleCnt <= sampleClockRatio) {
        sidWavOutput = sid[0].emulateHQwaves(OVERSAMPLING_CYCLES);
        sid[0].nonFiltedSample += sidWavOutput.nonFilted; 
        sid[0].filterInputSample += sidWavOutput.filterInput;
        if (sid[1].baseAddress != 0) {
          sidWavOutput = sid[1].emulateHQwaves(OVERSAMPLING_CYCLES);
          sid[1].nonFiltedSample += sidWavOutput.nonFilted;
          sid[1].filterInputSample += sidWavOutput.filterInput;
        }
        if (sid[2].baseAddress != 0) {
          sidWavOutput = sid[2].emulateHQwaves(OVERSAMPLING_CYCLES);
          sid[2].nonFiltedSample += sidWavOutput.nonFilted;
          sid[2].filterInputSample += sidWavOutput.filterInput;
        }
        if (sid[3].baseAddress != 0) {
          sidWavOutput = sid[3].emulateHQwaves(OVERSAMPLING_CYCLES);
          sid[3].nonFiltedSample += sidWavOutput.nonFilted;
          sid[3].filterInputSample += sidWavOutput.filterInput;
        }
        ++HQsampleCount;
        overSampleCycleCnt += (OVERSAMPLING_CYCLES << 4);
      }
      overSampleCycleCnt -= sampleClockRatio;
    }


    //Samplerate-based part of emulations:
    if (!realSIDmode) { //some PSID tunes use CIA TOD-clock (e.g. Kawasaki Synthesizer Demo)
      --tenthSecondCnt;
      if (tenthSecondCnt <= 0) {
        tenthSecondCnt = sampleRate / 10;
        ++(ioBankRD[0xDC08]);
        if (ioBankRD[0xDC08] >= 10) {
          ioBankRD[0xDC08] = 0;
          ++(ioBankRD[0xDC09]);
          //if(c64->ioBankRD[0xDC09]%
        }
      }
    }
    
    if (secondCnt < sampleRate) ++secondCnt; 
    else { 
      secondCnt = 0; 
      if(playTime<3600) ++playTime; 
    }


    if (!highQualitySID) {
      if (stereo == 0 || sidChipCount == 1) {
        output.L = output.R = sid[0].emulateWaves();
        if (sid[1].baseAddress != 0) {
          output.L = output.R += sid[1].emulateWaves();
        }
        if (sid[2].baseAddress != 0) {
          output.L = output.R += sid[2].emulateWaves();
        }
        if (sid[3].baseAddress != 0) {
          output.L = output.R += sid[3].emulateWaves();
        }
      } else {
        Tmp = sid[0].emulateWaves();
        switch (sid[0].channel) {
          case PSID.CRSID_CHANNEL_LEFT:
            output.L = Tmp * 2;
            output.R = 0;
            break;
          case PSID.CRSID_CHANNEL_RIGHT:
            output.R = Tmp * 2;
            output.L = 0;
            break;
          default:
            output.L = output.R = Tmp;
            break;
        }
        if (sid[1].baseAddress != 0) {
          Tmp = sid[1].emulateWaves();
          switch (sid[1].channel) {
            case PSID.CRSID_CHANNEL_LEFT:
              output.L += Tmp * 2;
              break;
            case PSID.CRSID_CHANNEL_RIGHT:
              output.R += Tmp * 2;
              break;
            default:
              output.L += Tmp;
              output.R += Tmp;
              break;
          }
        }
        if (sid[2].baseAddress != 0) {
          Tmp = sid[2].emulateWaves();
          switch (sid[2].channel) {
            case PSID.CRSID_CHANNEL_LEFT:
              output.L += Tmp * 2;
              break;
            case PSID.CRSID_CHANNEL_RIGHT:
              output.R += Tmp * 2;
              break;
            default:
              output.L += Tmp;
              output.R += Tmp;
              break;
          }
        }
        if (sid[3].baseAddress != 0) {
          Tmp = sid[3].emulateWaves();
          switch (sid[3].channel) {
            case PSID.CRSID_CHANNEL_LEFT:
              output.L += Tmp * 2;
              break;
            case PSID.CRSID_CHANNEL_RIGHT:
              output.R += Tmp * 2;
              break;
            default:
              output.L += Tmp;
              output.R += Tmp;
              break;
          }
        }
      }
    } else { //SID output-stages and mono/stereo handling for High-Quality SID-emulation
      sid[0].nonFiltedSample /= HQsampleCount;
      sid[0].filterInputSample /= HQsampleCount;
      if (sid[1].baseAddress != 0) {
        sid[1].nonFiltedSample /= HQsampleCount;
        sid[1].filterInputSample /= HQsampleCount;
      }
      if (sid[2].baseAddress != 0) {
        sid[2].nonFiltedSample /= HQsampleCount;
        sid[2].filterInputSample /= HQsampleCount;
      }
      if (sid[3].baseAddress != 0) {
        sid[3].nonFiltedSample /= HQsampleCount;
        sid[3].filterInputSample /= HQsampleCount;
      }
      if (stereo == 0 || sidChipCount == 1) {
        output.L = output.R = sid[0].emulateSIDoutputStage();
        if (sid[1].baseAddress != 0) {
          output.L += sid[1].emulateSIDoutputStage();
        }
        if (sid[2].baseAddress != 0) {
          output.L += sid[2].emulateSIDoutputStage();
        }
        if (sid[3].baseAddress != 0) {
          output.L += sid[3].emulateSIDoutputStage();
        }
        output.R = output.L;
      } else {
        Tmp = sid[0].emulateSIDoutputStage();
        switch (sid[0].channel) {
          case PSID.CRSID_CHANNEL_LEFT:
            output.L = Tmp * 2;
            output.R = 0;
            break;
          case PSID.CRSID_CHANNEL_RIGHT:
            output.R = Tmp * 2;
            output.L = 0;
            break;
          default:
            output.L = output.R = Tmp;
            break;
        }
        if (sid[1].baseAddress != 0) {
          Tmp = sid[1].emulateSIDoutputStage();
          switch (sid[1].channel) {
            case PSID.CRSID_CHANNEL_LEFT:
              output.L += Tmp * 2;
              break;
            case PSID.CRSID_CHANNEL_RIGHT:
              output.R += Tmp * 2;
              break;
            default:
              output.L += Tmp;
              output.R += Tmp;
              break;
          }
        }
        if (sid[2].baseAddress != 0) {
          Tmp = sid[2].emulateSIDoutputStage();
          switch (sid[2].channel) {
            case PSID.CRSID_CHANNEL_LEFT:
              output.L += Tmp * 2;
              break;
            case PSID.CRSID_CHANNEL_RIGHT:
              output.R += Tmp * 2;
              break;
            default:
              output.L += Tmp;
              output.R += Tmp;
              break;
          }
        }
        if (sid[3].baseAddress != 0) {
          Tmp = sid[3].emulateSIDoutputStage();
          switch (sid[3].channel) {
            case PSID.CRSID_CHANNEL_LEFT:
              output.L += Tmp * 2;
              break;
            case PSID.CRSID_CHANNEL_RIGHT:
              output.R += Tmp * 2;
              break;
            default:
              output.L += Tmp;
              output.R += Tmp;
              break;
          }
        }
      }
    }


    //average level (for VU-meter)
    sid[0].level += ((Math.abs(sid[0].output) >> 4) - sid[0].level) / 1024;
    if (sid[1].baseAddress != 0) {
      sid[1].level += ((Math.abs(sid[1].output) >> 4) - sid[1].level) / 1024;
    }
    if (sid[2].baseAddress != 0) {
      sid[2].level += ((Math.abs(sid[2].output) >> 4) - sid[2].level) / 1024;
    }
    if (sid[3].baseAddress != 0)
      sid[3].level += ((Math.abs(sid[3].output) >> 4) - sid[3].level) / 1024;

  /*  output = sid[0].emulateWaves();
    if (sid[1].baseAddress != 0) {
      output += sid[1].emulateWaves();
    }
    if (sid[2].baseAddress != 0) {
      output += sid[2].emulateWaves();
    }
*/
    return output;
  }
  
  /**
   * Play digi
   * 
   * @return the output value
   */
  public int playPSIDdigi() {
    int shifts;
    int ratePeriod;
    
    boolean playbackEnabled = false;
    int nybbleCounter = 0;
    int repeatCounter = 0;    
    int output = 0;    

    if (ioBankWR[0xD41D]!=0) {
      playbackEnabled = (ioBankWR[0xD41D] >= 0xFE);
      periodCounter = 0;
      nybbleCounter = 0;
      sampleAddress = ioBankWR[0xD41E] + (ioBankWR[0xD41F] << 8);
      repeatCounter = ioBankWR[0xD43F];
    }
    ioBankWR[0xD41D] = 0;

    if (playbackEnabled) {
      ratePeriod = ioBankWR[0xD45D] + (ioBankWR[0xD45E] << 8);
      if (ratePeriod!=0) {
        periodCounter += cpuFrequency / ratePeriod;
      }
      if (periodCounter >= sampleRate) {
        periodCounter -= sampleRate;

        if (sampleAddress < ioBankWR[0xD43D] + (ioBankWR[0xD43E] << 8)) {
          if (nybbleCounter!=0) {
            shifts = ioBankWR[0xD47D]!=0 ? 4 : 0;
            ++sampleAddress;
          } else {
            shifts = ioBankWR[0xD47D]!=0 ? 0 : 4;
          }
          output = (((ramBank[sampleAddress] >> shifts) & 0xF) - 8) * DIGI_VOLUME; //* (c64->ioBankWR[0xD418]&0xF);
          nybbleCounter ^= 1;
        } else if (repeatCounter!=0) {
          sampleAddress = ioBankWR[0xD47F] + (ioBankWR[0xD47E] << 8);
          repeatCounter--;
        }

      }
    }

    return output;
  }

  /**
   * Fill KERNAL/BASIC-ROM areas with content needed for SID-playback
   */
  public void setROMcontent() { 
    int i;

    for (i = 0xA000; i < 0x10000; ++i) {
      romBanks[i] = 0x60; //RTS (at least return if some unsupported call is made to ROM)
    } //for (i=0; i<sizeof(KERNAL); ++i) romBanks[0xE000+i] = KERNAL[i];
    
    for (i = 0xEA31; i < 0xEA7E; ++i) {
      romBanks[i] = 0xEA; //NOP (full irq-return leading to simple irq-return without other tasks)
    }
    
    for (i = 0; i < 9; ++i) {
      romBanks[0xEA7E + i] = ROM_IRQreturnCode[i];
    }
    
    for (i = 0; i < 4; ++i) {
      romBanks[0xFE43 + i] = ROM_NMIstartCode[i];
    }
    
    for (i = 0; i < 19; ++i) {
      romBanks[0xFF48 + i] = ROM_IRQBRKstartCode[i];
    }

    romBanks[0xFFFB] = 0xFE;
    romBanks[0xFFFA] = 0x43; //ROM nmi-vector
    romBanks[0xFFFF] = 0xFF;
    romBanks[0xFFFE] = 0x48; //ROM irq-vector

    //copy KERNAL & BASIC ROM contents into the RAM under them? (So PSIDs that don't select bank correctly will work better.)
    for (i = 0xA000; i < 0x10000; ++i) {
      ramBank[i] = romBanks[i];
    }
  }

  /**
   * Set default values that normally KERNEL ensures after startup/reset (only SID-playback related)
   */
  public void initMem() { 
    int i;

    //data required by both PSID and RSID (according to HVSC SID_file_format.txt):
    writeMemC64(0x02A6, videoStandard); //$02A6 should be pre-set to: 0:NTSC / 1:PAL
    writeMemC64(0x0001, 0x37); //initialize bank-reg. (ROM-banks and IO enabled)

    //if (romBanks[0xE000]==0) { //wasn't a KERNAL-ROM loaded? (e.g. PSID)
    writeMemC64(0x00CB, 0x40); //Some tunes might check for keypress here (e.g. Master Blaster Intro)
    //if(realSIDmode) {
    writeMemC64(0x0315, 0xEA);
    writeMemC64(0x0314, 0x31); //IRQ
    writeMemC64(0x0319, 0xEA/*0xFE*/);
    writeMemC64(0x0318, 0x81/*0x47*/); //NMI
    //}

    for (i = 0xD000; i < 0xD7FF; ++i) {
      ioBankRD[i] = ioBankWR[i] = 0; //initialize the whole IO area for a known base-state
    }
    if (realSIDmode) {
      ioBankWR[0xD012] = 0x37;
      ioBankWR[0xD011] = 0x8B;
    } //else ioBankWR[0xD012] = 0;
    //IObankWR[0xD019] = 0; //PSID: rasterrow: any value <= $FF, irq:enable later if there is VIC-timingsource

    ioBankRD[0xDC00] = 0x10;
    ioBankRD[0xDC01] = 0xFF; //Imitate CIA1 keyboard/joy port, some tunes check if buttons are not pressed
    if (videoStandard!=0) {
      ioBankWR[0xDC04] = 0x24;
      ioBankWR[0xDC05] = 0x40;
    } //initialize CIAs
    else {
      ioBankWR[0xDC04] = 0x95;
      ioBankWR[0xDC05] = 0x42;
    }
    if (realSIDmode) {
      ioBankWR[0xDC0D] = 0x81; //Reset-default, but for PSID CIA1 TimerA irq should be enabled anyway if SID is CIA-timed
    }
    ioBankWR[0xDC0E] = 0x01; //some tunes (and PSID doc) expect already running CIA (Reset-default)
    ioBankWR[0xDC0F] = 0x00; //All counters other than CIA1 TimerA should be disabled and set to 0xFF for PSID:
    ioBankWR[0xDD00] = ioBankRD[0xDD00] = 0x03; //VICbank-selector default
    ioBankWR[0xDD04] = ioBankWR[0xDD05] = 0xFF; //IObankWR[0xDD0E] = IObank[0xDD0F] = 0x00;    
  }  
  
  /**
   * Write data in C64 memory
   * 
   * @param address the address of mempry
   * @param data the data to store
   */
  public void writeMemC64(int address, int data) {
    if (address < 0xD000 || 0xE000 <= address) {
      ramBank[address]=data;
      return;
    } else if ((ramBank[1] & 3)!=0) { //handle SID-mirrors! (CJ in the USA workaround (writing above $d420, except SID2/SID3/PSIDdigi))
      if (0xD420 <= address && address < 0xD800) { //CIA/VIC mirrors needed?
        if (!(psidDigiMode && (0xD418 <= address) && (address < 0xD500))
                && !(sid[1].baseAddress <= address && address < sid[1].baseAddress + 0x20)
                && !(sid[2].baseAddress <= address && address < sid[2].baseAddress + 0x20)
                && !(sid[3].baseAddress <= address && address < sid[3].baseAddress + 0x20)) {
          //write to $D400..D41F if not in SID2/SID3 address-space
          ioBankWR[0xD400 + (address & 0x1F)]=data;
          return; 
        } else {
            ioBankWR[address]=data;
            return;
          }
      } else {
        ioBankWR[address]=data;
        return;
      }
    }
    ramBank[address]=data;
  }
  
  /**
   * Read data from C64 memory
   * 
   * @param address the address of memory
   * @return the read data
   */
  public int readMemC64(int address) {
    if (address < 0xA000) {
      return  ramBank[address];
    } else if (0xD000 <= address && address < 0xE000 && ((ramBank[1] & 3)!=0)) {
      if (0xD400 <= address && address < 0xD419) {
        //emulate peculiar SID-read (e.g. Lift Off)
        return ioBankWR[address]; 
      }
      return  ioBankRD[address];
    } else if ((address < 0xC000 && (ramBank[1] & 3) == 3)
            || (0xE000 <= address && ((ramBank[1] & 2))!=0)) {
      return romBanks[address];
    }
    return  ramBank[address];
  }    
}
