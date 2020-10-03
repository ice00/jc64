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
import java.util.logging.Level;
import java.util.logging.Logger;
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
  public boolean  opcodeUpperCasePreview = true;
  
  /** Uper case for opcode in source */
  public boolean  opcodeUpperCaseSource = false;  
  
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
    
  /** Max aggregate for data row */
  public int maxAggregate=8;
  
  
  /** Psid init song label to use as user defined */
  public String psidInitSongsLabel = "initSongs";
  
  /** Psid play sounds label to use as user definied */
  public String psidPlaySoundsLabel = "playSound";
  
  /** Sid low frequency table label */
  public String sidFreqLoLabel = "frequencyLo";
  
  /** Sid high frequency table label */
  public String sidFreqHiLabel = "frequencyHi";
  
  // comments
  
  /** Comment zero page area */
  public boolean commentZeroPage=true;
  
  /** Comment stack area */
  public boolean commentStackArea=true;
  
  /** Comment 200 area */
  public boolean comment200Area=true;
  
  /** Comment 300 area */
  public boolean comment300Area=true;
  
  /** Comment screen area */
  public boolean commentScreenArea=true; 
  
  /** Comment basic free area */
  public boolean commentBasicFreeArea=false;
  
  /** Comment basic rom area */
  public boolean commentBasicRom=true;
  
  /** Comment free ram area */
  public boolean commentFreeRam=false;
  
  /** Comment Vic II chip */
  public boolean commentVicII=true;
  
  /** Comment Sid chip */
  public boolean commentSid=true; 
  
  /** Comment color area */
  public boolean commentColorArea=true; 
  
  /** Comment Cia 1 chip */
  public boolean commentCia1=true;  
  
  /** Comment Cia 2 chip */
  public boolean commentCia2=true;
  
  /** Comment Kernal Rom */
  public boolean commentKernalRom=true; 
  
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
