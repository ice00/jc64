/**
 * @(#)Option.java 2019/12/01
 *
 * ICE Team free software group
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
package sw_emulator.swing.main;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoDeepOceanIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkHardIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkMediumIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkSoftIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatHiberbeeDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatHighContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatMonocaiIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatVuesionIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatDraculaContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedLightContrastIJTheme;
import java.awt.Component;
import java.util.Iterator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import sw_emulator.software.cpu.M6510Dasm;
import sw_emulator.software.machine.C64Dasm;

import javax.swing.plaf.metal.MetalLookAndFeel;
import sw_emulator.swing.plaf.metal.AquaTheme;
import sw_emulator.swing.plaf.metal.CharcoalTheme;
import sw_emulator.swing.plaf.metal.ContrastTheme;
import sw_emulator.swing.plaf.metal.EmeraldTheme;
import sw_emulator.swing.plaf.metal.RubyTheme;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.plaf.metal.DefaultMetalTheme;
import sw_emulator.software.Assembler;
import sw_emulator.software.Assembler.Name;
import sw_emulator.swing.Shared;


/**
 * Store all the options about the program
 * 
 * @author ice
 */
public class Option {
  // loook and feel options
    
  /** Macintosh Look and Feel */  
  public static final String LAF_MAC     ="com.sun.java.swing.plaf.mac.MacLookAndFeel";
  
  /** Metal Look and Feel */
  public static final String LAF_METAL   ="javax.swing.plaf.metal.MetalLookAndFeel";
  
  /** Motif Look and Feel */
  public static final String LAF_MOTIF   ="com.sun.java.swing.plaf.motif.MotifLookAndFeel";
  
  /** Windows Look and Feel */
  public static final String LAF_WINDOWS ="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    
  /** Windows Look and Feel */
  public static final String LAF_CWINDOWS ="com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";  
  
  /** GTK Look and Feel */
  public static final String LAF_GTK     ="com.sun.java.swing.plaf.gtk.GTKLookAndFeel";

  /** Nimbus Look and Feel */
  ///public static final String LAF_NIMBUS  ="com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
  public static final String LAF_NIMBUS  ="javax.swing.plaf.nimbus.NimbusLookAndFeel";
  
  /** Synth based Look and Feel */
  public static final String LAF_SYNTH  ="SYNTH"; 
  
  /** Flat laf look & feel */
  public static final FlatLaf[] LAF_SYNTH_FLAT = {
     new FlatLightLaf(),
     new FlatDarkLaf(),
     new FlatIntelliJLaf(),
     new FlatDarculaLaf(),
      
     new FlatArcIJTheme(),
     new FlatArcOrangeIJTheme(),
     new FlatCyanLightIJTheme(),
     new FlatDarkFlatIJTheme(),
     new FlatDarkPurpleIJTheme(),
     new FlatDraculaIJTheme(),
     new FlatGradiantoDarkFuchsiaIJTheme(),
     new FlatGradiantoDeepOceanIJTheme(),
     new FlatGradiantoMidnightBlueIJTheme(),
     new FlatGrayIJTheme(),
     new FlatGruvboxDarkHardIJTheme(),
     new FlatGruvboxDarkMediumIJTheme(),
     new FlatGruvboxDarkSoftIJTheme(),
     new FlatHiberbeeDarkIJTheme(),
     new FlatHighContrastIJTheme(),
     new FlatLightFlatIJTheme(),
     new FlatMaterialDesignDarkIJTheme(),
     new FlatMonocaiIJTheme(),
     new FlatNordIJTheme(),
     new FlatOneDarkIJTheme(),
     new FlatSolarizedDarkIJTheme(),
     new FlatSolarizedLightIJTheme(),
     new FlatSpacegrayIJTheme(),
     new FlatVuesionIJTheme(),
     new FlatArcDarkIJTheme(),
     new FlatArcDarkContrastIJTheme(),
     new FlatAtomOneDarkIJTheme(),
     new FlatAtomOneDarkContrastIJTheme(),
     new FlatAtomOneLightIJTheme(),
     new FlatAtomOneLightContrastIJTheme(),    
     new FlatDraculaContrastIJTheme(),
     new FlatGitHubIJTheme(),
     new FlatGitHubContrastIJTheme(),
     new FlatLightOwlIJTheme(),
     new FlatLightOwlContrastIJTheme(),
     new FlatMaterialDarkerIJTheme(),
     new FlatMaterialDarkerContrastIJTheme(),
     new FlatMaterialDeepOceanIJTheme(),
     new FlatMaterialDeepOceanContrastIJTheme(),
     new FlatMaterialLighterIJTheme(),
     new FlatMaterialLighterContrastIJTheme(),
     new FlatMaterialOceanicIJTheme(),     
     new FlatMaterialOceanicContrastIJTheme(),
     new FlatMaterialPalenightIJTheme(),
     new FlatMaterialPalenightContrastIJTheme(),
     new FlatMonokaiProIJTheme(),
     new FlatMonokaiProContrastIJTheme(),
     new FlatNightOwlIJTheme(),
     new FlatNightOwlContrastIJTheme(),
     new FlatSolarizedDarkContrastIJTheme(),
     new FlatSolarizedLightContrastIJTheme()
  };
  
  /** Null theme */
  public static final int THEME_NULL     = -1;  
  
  /** Ocean theme */
  public static final int THEME_OCEAN    = 0;
  
  /** Steel theme */
  public static final int THEME_STEEL    = 1;
  
  /** Aqua theme */
  public static final int THEME_AQUA     = 2;
  
  /** Charcoal theme */
  public static final int THEME_CHARCOAL = 3;
  
  /** Contrast theme */
  public static final int THEME_CONTRAST = 4;
  
  /** Emerald theme */
  public static final int THEME_EMERALD  = 5;
  
  /** Ruby theme */
  public static final int THEME_RUBY     = 6;

  /**
   * Install look & feel
   */
  public static void installLook()  {   
    for (FlatLaf laf : LAF_SYNTH_FLAT) {
      FlatLaf.install(laf);
    }
  }  
    
  /** Look and feel name */
  protected String lafName=LAF_METAL;
  
  /** Theme (-1 no theme)*/
  protected int theme=0;

  /** theme for flat laf*/
  protected String flatLaf="FlatLaf Light";

    
  /** Mode of the illegal opcode  */
  public byte illegalOpcodeMode = M6510Dasm.MODE1;
        
  /** Uper case for opcode in preview */
  public boolean opcodeUpperCasePreview = true;
  
  /** Uper case for opcode in source */
  public boolean opcodeUpperCaseSource = false;  
  
  /** Language for comment  */
  public byte commentLanguage = C64Dasm.LANG_ENGLISH;
  
  /** Use as code (not data) if memory is undefined */
  public boolean useAsCode = true;
  
  /** Erase Dasm comment when convering to data*/
  public boolean eraseDComm = true;
  
  /** Erase Dasm plus addresses when convering to data*/
  public boolean erasePlus = true;
  
  /** Max length of a label */
  public int maxLabelLength=25;
    
  /** Max byte aggregate for data row */
  public int maxByteAggregate=8;
  
  /** Max word aggregate for data row */
  public int maxWordAggregate=4;  
  
  /** Max word swapped aggregate for data row */
  public int maxSwappedAggregate=4;
  
  /** Max text aggregate for data row */
  public int maxTextAggregate=32;
  
  /** Max tribyte aggregate for data row */
  public int maxTribyteAggregate=3;   
  
  /** Max long aggregate for data row */
  public int maxLongAggregate=2; 
  
  /** Max address aggregate for data row */
  public int maxAddressAggregate=4;   
  
  /** Max stack word aggregate for data row */
  public int maxStackWordAggregate=4;  
  
  /** Temporary path */
  public String tmpPath="";
  
  
  /** Psid init song label to use as user defined */
  public String psidInitSongsLabel = "initSongs";
  
  /** Psid play sounds label to use as user definied */
  public String psidPlaySoundsLabel = "playSound";
  
  /** Sid low frequency table label */
  public String sidFreqLoLabel = "frequencyLo";
  
  /** Sid high frequency table label */
  public String sidFreqHiLabel = "frequencyHi";
  
  /** Number of spaces in starting row of instruction */
  public int numInstrSpaces=6;
  
  /** Number of tabs in starting row of instrucyion */
  public int numInstrTabs=0;
  
    /** Number of spaces in starting row of data */
  public int numDataSpaces=6;
  
  /** Number of space in starting row of data */
  public int numDataTabs=0;
  
  /** A label is on a sepatate line from instruction */
  public boolean labelOnSepLine=true;
  
  /** Click on UC column edit user comment */
  public boolean clickUcEdit=false;
  
  /** Click on UB column edit user global comment */
  public boolean clickUbEdit=false;
  
  /** Click on UL column edit user label */
  public boolean clickUlEdit=false;
  
  /** Click on DC column erase dasm comment */
  public boolean clickDcErase=false;
  
  /** Click on DL column erase dasm lable */
  public boolean clickDlErase=false;
  
  
  // comments C64
  
  /** Comment zero page area */
  public boolean commentC64ZeroPage=true;
  
  /** Comment stack area */
  public boolean commentC64StackArea=true;
  
  /** Comment 200 area */
  public boolean commentC64_200Area=true;
  
  /** Comment 300 area */
  public boolean commentC64_300Area=true;
  
  /** Comment screen area */
  public boolean commentC64ScreenArea=true; 
  
  /** Comment basic free area */
  public boolean commentC64BasicFreeArea=false;
  
  /** Comment basic rom area */
  public boolean commentC64BasicRom=true;
  
  /** Comment free ram area */
  public boolean commentC64FreeRam=false;
  
  /** Comment Vic II chip */
  public boolean commentC64VicII=true;
  
  /** Comment Sid chip */
  public boolean commentC64Sid=true; 
  
  /** Comment color area */
  public boolean commentC64ColorArea=true; 
  
  /** Comment Cia 1 chip */
  public boolean commentC64Cia1=true;  
  
  /** Comment Cia 2 chip */
  public boolean commentC64Cia2=true;
  
  /** Comment Kernal Rom */
  public boolean commentC64KernalRom=true; 
  
  
  // comments C128
  
  /** C128: Comment zero page area */
  public boolean commentC128ZeroPage=true;
  
  /** C128: Comment stack area */
  public boolean commentC128StackArea=true;
  
  /** C128: Comment 200 area */
  public boolean commentC128_200Area=true;
  
  /** C128: Comment 300 area */
  public boolean commentC128_300Area=true;
  
  /** C128: Comment screen area */
  public boolean commentC128ScreenArea=true; 
  
  /** C128: Comment application program area*/
  public boolean commentC128AppProgArea;
  
  /** C128: Comment Basic rom */
  public boolean commentC128BasicRom;
  
  /** C128: Comment CIA 1 */
  public boolean commentC128Cia1;

  /** C128: Comment CIA 2*/
  public boolean commentC128Cia2;
  
  /** C128: Comment Color */
  public boolean commentC128Color;

  /** C128: Comment DMA */
  public boolean commentC128DMA;
  
  /** C128: Comment Kernal rom */
  public boolean commentC128KernalRom;

  /** C128: Comment MMU */
  public boolean commentC128MMU;

  /** C128: Comment screen memory */
  public boolean commentC128ScreenMem;
  
  /** C128: Comment user basic area */
  public boolean commentC128UserBasic;
  
  /** C128: Comment VDC */
  public boolean commentC128VDC;
  
  /** C128: Comment video color mem */
  public boolean commentC128VideoColor;
  
  /** C128: Comment Vic II */
  public boolean commentC128VicII;
  
  /** C128: Comment SID */
  public boolean commentC128Sid;
  
  // comments C1541
  
  /** C1541: Comment zero page area */
  public boolean commentC1541ZeroPage=true;
  
  /** C1541: Comment stack area */
  public boolean commentC1541StackArea=true;
  
  /** C1541: Comment 200 area */
  public boolean commentC1541_200Area=true;
  
  /** C1541: Comment VIA 1 */
  public boolean commentC1541Via1=true;
  
  /** C1541: Comment VIA 2 */
  public boolean commentC1541Via2=true;
  
  /** C1541: Comment Kernal */
  public boolean commentC1541Kernal=true;
  
  /** C1541: Comment buffer 0 */
  public boolean commentC1541Buffer0=false;
  
  /** C1541: Comment buffer 1 */
  public boolean commentC1541Buffer1=false;
  
  /** C1541: Comment buffer 2 */
  public boolean commentC1541Buffer2=false;
  
  /** C1541: Comment buffer 3 */
  public boolean commentC1541Buffer3=false;
  
  /** C1541: Comment buffer 4 */
  public boolean commentC1541Buffer4=false;
  
  
  /** Plus4: Comment zero page */
  public boolean commentPlus4ZeroPage=true;
  
  /** Plus4: Comment stack area */
  public boolean commentPlus4StackArea=true;
  
  /** Plus4: Comment 200 area */
  public boolean commentPlus4_200Area=true;
  
  /** Plus4: Comment 300 area */
  public boolean commentPlus4_300Area=true;
  
  /** Plus4: Comment 400 area */
  public boolean commentPlus4_400Area=true;
  
  /** Plus4: Comment 500 area */
  public boolean commentPlus4_500Area=true;
  
  /** Plus4: Comment 600 area */
  public boolean commentPlus4_600Area=true;
    
  /** Plus4: Comment 700 area */
  public boolean commentPlus4_700Area=true;
  
  /** Plus4: Comment color area */
  public boolean commentPlus4ColorArea=false;
  
  /** Plus4: Comment video area */
  public boolean commentPlus4VideoArea=false;
  
  /** Plus4: Comment BASIC RAM without graphics) */
  public boolean commentPlus4BasicRamP=false;
    
  /** Plus4: Comment BASIC RAM with graphics) */
  public boolean commentPlus4BasicRamN=false; 
  
  /** Plus4: Comment luminance */
  public boolean commentPlus4Luminance=false;
  
  /** Plus4: Comment colore luminance */
  public boolean commentPlus4ColorBitmap=false;
  
  /** Plus4: Comment graphic data */
  public boolean commentPlus4GraphicData=false;
  
  /** Plus4: Comment BASIC rom */
  public boolean commentPlus4BasicRom=false;
  
  /** Plus4: Comment BASIC extension */
  public boolean commentPlus4BasicExt=false;
  
  /** Plus4: Comment caracter */
  public boolean commentPlus4Caracter=false;
  
  /** Plus4: Comment Acia */
  public boolean commentPlus4Acia=true;
  
  /** Plus4: Comment 6529B 1 */
  public boolean commentPlus4_6529B_1=true;
  
  /** Plus4: Comment 6529B 2 */
  public boolean commentPlus4_6529B_2=true;
  
  /** Plus4: Comment Ted */
  public boolean commentPlus4Ted=true;
  
  /** Plus4: Comment Kernal */
  public boolean commentPlus4Kernal=true;
  
  /** Vic20: Comment zero page */
  public boolean commentVic20ZeroPage=true;
  
  /** Vic20: Comment stack area */
  public boolean commentVic20StackArea=true;
  
  /** Vic20: Comment 200 area */
  public boolean commentVic20_200Area=true;
  
  /** Vic20: Comment 300 area */
  public boolean commentVic20_300Area=true;
  
  /** Vic20: Comment 400 area */
  public boolean commentVic20_400Area=true;
  
  /** Vic20: Comment Vic */
  public boolean commentVic20Vic=true;
  
  /** Vic20: Comment Via 1 */
  public boolean commentVic20Via1=true;
  
  /** Vic20: Comment Via 2 */
  public boolean commentVic20Via2=true;
  
  /** Vic20: Comment user basic */
  public boolean commentVic20UserBasic=false;;
  
  /** Vic20: Comment screen */
  public boolean commentVic20Screen=true;
  
  /** Vic20: Comment 8k expansion 1 */
  public boolean commentVic20_8kExp1=false;;
  
  /** Vic20: Comment 8k expansion 2 */
  public boolean commentVic20_8kExp2=false;;
  
  /** Vic20: Comment 8k expansion 3 */
  public boolean commentVic20_8kExp3=false;;
  
  /** Vic20: Comment character */
  public boolean commentVic20Character=true;
  
  /** Vic20: Comment color */
  public boolean commentVic20Color=true;
  
  /** Vic20: Comment block 2 */
  public boolean commentVic20Block2=false;;
  
  /** Vic20: Comment block 3 */
  public boolean commentVic20Block3=false;;
  
  /** Vic20: Comment block 4 */
  public boolean commentVic20Block4=false;;
  
  /** Vic20: Comment Basic rom */
  public boolean commentVic20BasicRom=true;
  
  /** Vic20: Comment Kernal rom */
  public boolean commentVic20KernalRom=true;
  
  
  // assembler
  
  /** Assember to use */
  public Name assembler=Name.DASM;
      
  
  /** DASM: compile source with f3 option */
  public boolean dasmF3Comp=false;
  
  /** DASM: starting declaration */
  public Assembler.Starting dasmStarting=Assembler.Starting.PROC; 
  
  /** DASM: origin declaration */
  public Assembler.Origin dasmOrigin=Assembler.Origin.DOT_ORG;    
  
  /** DASM: label declaration */
  public Assembler.Label dasmLabel=Assembler.Label.NAME_COLON;    
    
  /** DASM: comment declaration */
  public Assembler.Comment dasmComment=Assembler.Comment.SEMICOLON; 
  
  /** DASM: block comment declaration */
  public Assembler.BlockComment dasmBlockComment=Assembler.BlockComment.SEMICOLON; 
  
  /** DASM: byte declaration */
  public Assembler.Byte dasmByte=Assembler.Byte.DOT_BYTE;
  
  /** DASM: word declaration */
  public Assembler.Word dasmWord=Assembler.Word.DOT_WORD;
  
  /** DASM: word swapped declaration */
  public Assembler.WordSwapped dasmWordSwapped=Assembler.WordSwapped.DC_DOT_S_WORD_SWAPPED;
  
  /** DASM: tribyte declaration */
  public Assembler.Tribyte dasmTribyte=Assembler.Tribyte.MACRO_TRIBYTE;
  
  /** DASM: long declaration */
  public Assembler.Long dasmLong=Assembler.Long.DOT_LONG; 
  
  /** DASM: address declaration */
  public Assembler.Address dasmAddress=Assembler.Address.DOT_WORD_ADDR;
  
  /** DASM: stack words declaration */
  public Assembler.StackWord dasmStackWord=Assembler.StackWord.MACRO_STACKWORD;
  
  /** DASM: mono color sprite declaration */
  public Assembler.MonoSprite dasmMonoSprite=Assembler.MonoSprite.MACRO_BIN;
   
  /** DASM: multi color sprite declaration */
  public Assembler.MultiSprite dasmMultiSprite=Assembler.MultiSprite.MACRO_BIN;
  
  /** DASM: text declaration */
  public Assembler.Text dasmText=Assembler.Text.DOT_BYTE_TEXT;
  
  /** DASM: text with num chars declaration */
  public Assembler.NumText dasmNumText=Assembler.NumText.DOT_BYTE_NUMTEXT;
  
  /** DASM: text null terminated declaration */
  public Assembler.ZeroText dasmZeroText=Assembler.ZeroText.DOT_BYTE_ZEROTEXT;
  
  /** DASM: text terminated with high bit 1 declaration */
  public Assembler.HighText dasmHighText=Assembler.HighText.DOT_BYTE_HIGHTEXT;
  
  /** DASM: text left shifted declaration */
  public Assembler.ShiftText dasmShiftText=Assembler.ShiftText.DOT_BYTE_SHIFTTEXT;
  
  /** DASM: text to screen code declaration */
  public Assembler.ScreenText dasmScreenText=Assembler.ScreenText.DOT_BYTE_SCREENTEXT; 
  
  /** DASM: text to petascii code declaration */
  public Assembler.PetasciiText dasmPetasciiText=Assembler.PetasciiText.DOT_BYTE_PETASCIITEXT; 
  
  
  /** TMPX: starting declaration */
  public Assembler.Starting tmpxStarting=Assembler.Starting.FAKE; 
  
  /** TMPx: origin declaration */
  public Assembler.Origin tmpxOrigin=Assembler.Origin.ASTERISK; 
  
  /** TMPx: label declaration */
  public Assembler.Label tmpxLabel=Assembler.Label.NAME; 
  
  /** TMPx: comment declaration */
  public Assembler.Comment tmpxComment=Assembler.Comment.SEMICOLON; 
  
  /** TMPX: block comment declaration */
  public Assembler.BlockComment tmpxBlockComment=Assembler.BlockComment.SEMICOLON;  
  
  /** TMPX: byte declaration */
  public Assembler.Byte tmpxByte=Assembler.Byte.DOT_BYTE;
  
  /** TMPX: word declaration */
  public Assembler.Word tmpxWord=Assembler.Word.DOT_WORD;
  
  /** TMPX: word swapped declaration */
  public Assembler.WordSwapped tmpxWordSwapped=Assembler.WordSwapped.MACRO4_WORD_SWAPPED; 
  
  /** TMPX: tribyte declaration */
  public Assembler.Tribyte tmpxTribyte=Assembler.Tribyte.MACRO4_TRIBYTE; 
  
  /** TMPX: long declaration type */
  public Assembler.Long tmpxLong=Assembler.Long.MACRO4_LONG; 
  
  /** TMPX: address declaration */
  public Assembler.Address tmpxAddress=Assembler.Address.DOT_ADDR_ADDR;
  
  /** TMPX: stack word declaration */
  public Assembler.StackWord tmpxStackWord=Assembler.StackWord.DOT_RTA_STACKWORD; 
  
  /** TMPx: mono color sprite declaration type */
  public Assembler.MonoSprite tmpxMonoSprite=Assembler.MonoSprite.MACRO4_BIN;
   
  /** TMPx: multi color sprite declaration type */
  public Assembler.MultiSprite tmpxMultiSprite=Assembler.MultiSprite.MACRO4_BIN;
  
  /** TMPx: text declaration type */
  public Assembler.Text tmpxText=Assembler.Text.DOT_TEXT;
  
  /** TMPx: text with num chars declaration */
  public Assembler.NumText tmpxNumText=Assembler.NumText.DOT_PTEXT_NUMTEXT;
  
  /** TMPx: text null temrianted declaration */
  public Assembler.ZeroText tmpxZeroText=Assembler.ZeroText.DOT_NULL_ZEROTEXT;
  
  /** TMPX: text terminated with high bit 1 declaration */
  public Assembler.HighText tmpxHighText=Assembler.HighText.DOT_SHIFT_HIGHTEXT;
  
  /** TMPX: text left shifted declaration */
  public Assembler.ShiftText tmpxShiftText=Assembler.ShiftText.DOT_SHIFTL_SHIFTTEXT;
  
  /** TMPx: text to screen code declaration type */
  public Assembler.ScreenText tmpxScreenText=Assembler.ScreenText.DOT_SCREEN_SCREENTEXT;  
  
  /** TMPX: text to petascii code declaration */
  public Assembler.PetasciiText tmpxPetasciiText=Assembler.PetasciiText.DOT_TEXT_PETASCIITEXT;
  
  
  /** CA65: starting declaration */
  public Assembler.Starting ca65Starting=Assembler.Starting.DOT_SETCPU; 
  
  /** CA65: origin declaration */
  public Assembler.Origin ca65Origin=Assembler.Origin.DOT_ORG;
  
  /** CA65: label declaration */
  public Assembler.Label ca65Label=Assembler.Label.NAME_COLON; 
  
   /** CA65: comment declaration */
  public Assembler.Comment ca65Comment=Assembler.Comment.SEMICOLON; 
  
  /** CA65: block comment declaration */
  public Assembler.BlockComment ca65BlockComment=Assembler.BlockComment.SEMICOLON;  
  
  /** CA65: byte declaration */
  public Assembler.Byte ca65Byte=Assembler.Byte.DOT_BYTE;
  
  /** CA65: word declaration */
  public Assembler.Word ca65Word=Assembler.Word.DOT_WORD;
  
  /** CA65: word swapped declaration */
  public Assembler.WordSwapped ca65WordSwapped=Assembler.WordSwapped.DOT_DTYB;
  
  /** CA65: tribyte declaration */
  public Assembler.Tribyte ca65Tribyte=Assembler.Tribyte.MACRO3_TRIBYTE;  
  
  /** CA65: long declaration */
  public Assembler.Long ca65Long=Assembler.Long.DOT_DWORD_LONG; 
  
  /** CA65: address declaration */
  public Assembler.Address ca65Address=Assembler.Address.DOT_ADDR_ADDR;
  
  /** CA65: stack word declaration */
  public Assembler.StackWord ca65StackWord=Assembler.StackWord.MACRO3_STACKWORD; 
  
  /** CA65: mono color sprite*/
  public Assembler.MonoSprite ca65MonoSprite=Assembler.MonoSprite.MACRO3_BIN;
   
  /** CA65: multi color sprite */
  public Assembler.MultiSprite ca65MultiSprite=Assembler.MultiSprite.MACRO3_BIN;
  
  /** CA65: text declaration */
  public Assembler.Text ca65Text=Assembler.Text.DOT_BYTE_TEXT;
  
  /** CA65: text with num chars declaration */
  public Assembler.NumText ca65NumText=Assembler.NumText.DOT_BYTE_NUMTEXT;
  
  /** CA65: text null temrianted declaration */
  public Assembler.ZeroText ca65ZeroText=Assembler.ZeroText.DOT_ASCIIZ_ZEROTEXT;
  
  /** CA65: text terminated with high bit 1 declaration */
  public Assembler.HighText ca65HighText=Assembler.HighText.DOT_BYTE_HIGHTEXT;
  
  /** CA65: text left shifted declaration */
  public Assembler.ShiftText ca65ShiftText=Assembler.ShiftText.DOT_BYTE_SHIFTTEXT;
  
  /** CA65: text to screen code declaration */
  public Assembler.ScreenText ca65ScreenText=Assembler.ScreenText.DOT_BYTE_SCREENTEXT;
  
  /** CA65: text to petascii code declaration */
  public Assembler.PetasciiText ca65PetasciiText=Assembler.PetasciiText.DOT_BYTE_PETASCIITEXT;
  
  
  /** ACME: starting declaration */
  public Assembler.Starting acmeStarting=Assembler.Starting.MARK_CPU; 
  
  /** ACME: origin declaration */
  public Assembler.Origin acmeOrigin=Assembler.Origin.ASTERISK;
  
  /** ACME: label declaration */
  public Assembler.Label acmeLabel=Assembler.Label.NAME_COLON; 
  
  /** ACME: comment declaration */
  public Assembler.Comment acmeComment=Assembler.Comment.SEMICOLON; 
  
  /** ACME: block comment declaration */
  public Assembler.BlockComment acmeBlockComment=Assembler.BlockComment.SEMICOLON; 
  
  /** ACME: byte declaration */
  public Assembler.Byte acmeByte=Assembler.Byte.MARK_BYTE;
  
  /** ACME: word declaration */
  public Assembler.Word acmeWord=Assembler.Word.MARK_WORD;  
  
  /** ACME: word swapped declaration */
  public Assembler.WordSwapped acmeWordSwapped=Assembler.WordSwapped.MACRO2_WORD_SWAPPED; 
  
  /** ACME: tribyte declaration */
  public Assembler.Tribyte acmeTribyte=Assembler.Tribyte.MARK_TWENTYFOUR_TRIBYTE; 
  
  /** ACME: long declaration */
  public Assembler.Long acmeLong=Assembler.Long.MARK_THIRTYTWO_LONG; 
  
  /** ACME: address declaration */
  public Assembler.Address acmeAddress=Assembler.Address.MARK_WORD_ADDR;
  
  /** ACME: stack word declaration */
  public Assembler.StackWord acmeStackWord=Assembler.StackWord.MACRO2_STACKWORD;
  
  /** ACME: mono color sprite*/
  public Assembler.MonoSprite acmeMonoSprite=Assembler.MonoSprite.MACRO2_BIN;
   
  /** ACME: multi color sprite */
  public Assembler.MultiSprite acmeMultiSprite=Assembler.MultiSprite.MACRO2_BIN;
  
  /** ACME: text declaration */
  public Assembler.Text acmeText=Assembler.Text.MARK_TEXT;
  
  /** ACME: text with num chars declaration */
  public Assembler.NumText acmeNumText=Assembler.NumText.MARK_TEXT_NUMTEXT;
  
  /** ACME: text null temrianted declaration */
  public Assembler.ZeroText acmeZeroText=Assembler.ZeroText.MARK_TEXT_ZEROTEXT;
  
  /** ACME: text terminated with high bit 1 declaration */
  public Assembler.HighText acmeHighText=Assembler.HighText.MARK_TEXT_HIGHTEXT;
  
  /** ACME: text left shift declaration */
  public Assembler.ShiftText acmeShiftText=Assembler.ShiftText.MARK_TEXT_SHIFTTEXT; 
  
  /** ACME: text to screen code declaration */
  public Assembler.ScreenText acmeScreenText=Assembler.ScreenText.MARK_SCR_SCREENTEXT;
  
  /** ACME: text to petascii code declaration */
  public Assembler.PetasciiText acmePetasciiText=Assembler.PetasciiText.MARK_PET_PETASCIITEXT;
  
  
  /** KickAssembler: colon macro */
  public boolean kickColonMacro=false;
  
  /** KickAssembler: starting declaration */
  public Assembler.Starting kickStarting=Assembler.Starting.DOT_CPU_UND; 
  
  /** KickAssembler: kick declaration */
  public Assembler.Origin kickOrigin=Assembler.Origin.ASTERISK;
  
  /** KickAssembler: label declaration */
  public Assembler.Label kickLabel=Assembler.Label.NAME_COLON; 
  
  /** KickAssembler: comment declaration */
  public Assembler.Comment kickComment=Assembler.Comment.DOUBLE_BAR; 
  
  /** KickAssembler: block comment declaration */
  public Assembler.BlockComment kickBlockComment=Assembler.BlockComment.DOUBLE_BAR; 
  
  /** KickAssembler: byte declaration */
  public Assembler.Byte kickByte=Assembler.Byte.DOT_BYTE;
  
  /** KickAssembler: word declaration */
  public Assembler.Word kickWord=Assembler.Word.DOT_WORD;  
  
  /** KickAssembler: word swapped declaration */
  public Assembler.WordSwapped kickWordSwapped=Assembler.WordSwapped.MACRO1_WORD_SWAPPED;    
  
  /** kickAssembler: tribyte declaration */
  public Assembler.Tribyte kickTribyte=Assembler.Tribyte.MACRO1_TRIBYTE; 
  
  /** Kick: long declaration */
  public Assembler.Long kickLong=Assembler.Long.DOT_DWORD_LONG; 
  
  /** KickAssembler: address declaration */
  public Assembler.Address kickAddress=Assembler.Address.DOT_WORD_ADDR; 
  
  /** KickAssembler: stack word declaration */
  public Assembler.StackWord kickStackWord=Assembler.StackWord.MACRO1_STACKWORD;  
  
  /** KickAssembler: mono color sprite*/
  public Assembler.MonoSprite kickMonoSprite=Assembler.MonoSprite.MACRO1_BIN;
   
  /** KickAsembler: multi color sprite */
  public Assembler.MultiSprite kickMultiSprite=Assembler.MultiSprite.MACRO1_BIN;  
    
  /** KickAsembler: text declaration type */
  public Assembler.Text kickText=Assembler.Text.DOT_TEXT;
  
  /** KickAsembler: text with num chars declaration */
  public Assembler.NumText kickNumText=Assembler.NumText.DOT_TEXT_NUMTEXT;
  
  /** KickAsembler: text null temrianted declaration */
  public Assembler.ZeroText kickZeroText=Assembler.ZeroText.DOT_TEXT_ZEROTEXT;
  
  /** KickAsembler: text terminated with high bit 1 declaration */
  public Assembler.HighText kickHighText=Assembler.HighText.DOT_TEXT_HIGHTEXT;
  
  /** KickAsembler: text left shifted declaration */
  public Assembler.ShiftText kickShiftText=Assembler.ShiftText.DOT_TEXT_SHIFTTEXT; 
  
  /** KickAsembler: text to screen code declaration */
  public Assembler.ScreenText kickScreenText=Assembler.ScreenText.DOT_SCREEN_SCREENTEXT;
  
  /** KickAsembler: text to petascii code declaration */
  public Assembler.PetasciiText kickPetasciiText=Assembler.PetasciiText.DOT_TEXT_PETASCIITEXT;
  
  
  /** 64Tass: starting declaration */
  public Assembler.Starting tass64Starting=Assembler.Starting.DOT_CPU_A; 
    
  /** 64Tass: origin declaration */
  public Assembler.Origin tass64Origin=Assembler.Origin.ASTERISK; 
    
  /** KickAssembler: label declaration */
  public Assembler.Label tass64Label=Assembler.Label.NAME_COLON;
  
  /** 64Tass: comment declaration */
  public Assembler.Comment tass64Comment=Assembler.Comment.SEMICOLON; 
  
  /** 64Tass: block comment declaration */
  public Assembler.BlockComment tass64BlockComment=Assembler.BlockComment.SEMICOLON;
  
  /** 64Tass: byte declaration */
  public Assembler.Byte tass64Byte=Assembler.Byte.DOT_BYTE;
  
  /** 64Tass: word declaration */
  public Assembler.Word tass64Word=Assembler.Word.DOT_WORD;
  
  /** 64Tass: word swapped declaration */
  public Assembler.WordSwapped tass64WordSwapped=Assembler.WordSwapped.MACRO4_WORD_SWAPPED;
  
  /** 64Tass: tribyte declaration */
  public Assembler.Tribyte tass64Tribyte=Assembler.Tribyte.DOT_LONG_TRIBYTE; 
  
  /** 64Tass: long declaration */
  public Assembler.Long tass64Long=Assembler.Long.DOT_DWORD_LONG; 
  
  /** 64Tass: Address declaration */
  public Assembler.Address tass64Address=Assembler.Address.DOT_ADDR_ADDR;
  
  /** 64Tass: Stack Word declaration */
  public Assembler.StackWord tass64StackWord=Assembler.StackWord.DOT_RTA_STACKWORD;  
  
  /** 64Tass: mono color sprite*/
  public Assembler.MonoSprite tass64MonoSprite=Assembler.MonoSprite.MACRO4_BIN;
   
  /** 64Tass: multi color sprite */
  public Assembler.MultiSprite tass64MultiSprite=Assembler.MultiSprite.MACRO4_BIN;
  
  /** 64Tass: text declaration type */
  public Assembler.Text tass64Text=Assembler.Text.DOT_TEXT;
  
  /** 64Tass: text with num chars declaration */
  public Assembler.NumText tass64NumText=Assembler.NumText.DOT_PTEXT_NUMTEXT;
  
  /** 64Tass: text null temrianted declaration */
  public Assembler.ZeroText tass64ZeroText=Assembler.ZeroText.DOT_NULL_ZEROTEXT;
  
  /** 64Tass: text terminated with high bit 1 declaration */
  public Assembler.HighText tass64HighText=Assembler.HighText.DOT_SHIFT_HIGHTEXT;
  
  /** 64Tass: text left shifted declaration */
  public Assembler.ShiftText tass64ShiftText=Assembler.ShiftText.DOT_SHIFTL_SHIFTTEXT; 
  
  /** 64Tass: text to screen code declaration */
  public Assembler.ScreenText tass64ScreenText=Assembler.ScreenText.DOT_TEXT_SCREENTEXT;
  
  /** 64Tass: text to petascii code declaration */
  public Assembler.PetasciiText tass64PetasciiText=Assembler.PetasciiText.DOT_TEXT_PETASCIITEXT;
  
  
  /**
   * Get the flat laf theme
   * 
   * @return the flat laf theme
   */
  public String getFlatLaf() {
    return flatLaf;
  }

 /**  
  * Set the flat laf theme 
  * 
  * @param flatLaf the flat laf theme
  */
 public void setFlatLaf(String flatLaf) {
   this.flatLaf = flatLaf;
 }
  
  /**
   * Get the look and feel name
   * 
   * @return the look and feel name
   */
  public String getLafName() {
    return lafName;
  }

  /**
   * Set the look and feel name
   * 
   * @param lafName thje look and feel name
   */
  public void setLafName(String lafName) {
    this.lafName = lafName;
  }
  
  /**
   * Get the methal theme
   * 
   * @return the methal theme
   */
  public MetalTheme getMethalTheme() {
    switch (theme) {
      case THEME_OCEAN:
        return new OceanTheme();
      case THEME_STEEL:
        return new DefaultMetalTheme();
      case THEME_AQUA:
        return new AquaTheme();
      case THEME_CHARCOAL:
        return new CharcoalTheme(); 
      case THEME_CONTRAST:
        return new ContrastTheme();   
      case THEME_EMERALD:
        return new EmeraldTheme();
      case THEME_RUBY:
        return new RubyTheme();   
    }  
    return null;
  }
  
  /**
   * Get the theme number (-1 = none)
   * 
   * @return the theme number 
   */
  public int getTheme() {
    return theme;
  }

  /**
   * Set the theme number (-1 none)
   * 
   * @param theme
   */
  public void setTheme(int theme) {
    this.theme = theme;
  }

  /**
   * Use the given look and feel
   * 
   * @param lafName the look and feel name
   * @param theme additional theme to set
   */
  public static void useLookAndFeel(String lafName, MetalTheme theme) {
    try {
      if (theme!=null) MetalLookAndFeel.setCurrentTheme(theme);
      UIManager.setLookAndFeel(lafName);
      Iterator iter=Shared.framesList.iterator();
      while (iter.hasNext()) {
        SwingUtilities.updateComponentTreeUI((Component)iter.next());  
      }  
    } catch (Exception e) {
        System.out.println(e.toString());
      }    
  }  
  
  /**
   * Use the given flat laf theme
   * 
   * @param lafName the flat laf name
   */
  public static void useLookAndFeel(String lafName) {
     try { 
       for (FlatLaf laf: Option.LAF_SYNTH_FLAT) {
         if (laf.getName().equals(lafName)) {
             UIManager.setLookAndFeel(laf);
             
             Iterator iter=Shared.framesList.iterator();
             while (iter.hasNext()) {
               SwingUtilities.updateComponentTreeUI((Component)iter.next());  
             }  
             return;
         }  
       }
     } catch (Exception e) {
          System.out.println(e.toString());
       }      
  }
}
