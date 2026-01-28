/**
 * @(#)JC64Dis.java 2019/12/01
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import sw_emulator.swing.JDisassemblerFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 * Java C64 disassembler with graphics
 *
 * @author ice
 */
public class JC64Dis {

  private JDisassemblerFrame jMainFrame;
  private static JWindow nativeSplash = null;
  private static boolean usingNativeSplash = false;

  /**
   * Main program that display the disassembler 
   */
  public JC64Dis() {
    Option option = new Option();

    // read option file even if it will be reload again in JDisassemblerFrame
    FileManager.instance.readOptionFile(FileManager.OPTION_FILE, option);

    if (option.getLafName().equals("SYNTH")) {
      Option.useLookAndFeel(option.getFlatLaf());
    } else {
      Option.useLookAndFeel(option.getLafName(), option.getMethalTheme());
    }

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        jMainFrame = new JDisassemblerFrame();

        // close splash once main window is shown
        closeSplash();
                jMainFrame.setVisible(true);
      }
    });
  }

  /**
   * Give true if we run the program as native image
   *
   * @return true on native image execution
   */
  private static boolean isNativeImage() {
    try {
      Class<?> imageInfo = Class.forName("org.graalvm.nativeimage.ImageInfo");
      java.lang.reflect.Method inImageCode = imageInfo.getMethod("inImageCode");
      Object res = inImageCode.invoke(null);
      return Boolean.TRUE.equals(res);
    } catch (Throwable ignored) {
      return false;
    }
  }

  /**
   * Endsure audio is provided in native image
   */
  private static void ensureAudioProviders() {
    try {
      try {
        Class<?> p1 = Class.forName("com.sun.media.sound.PortMixerProvider");
        Object prov1 = p1.getDeclaredConstructor().newInstance();
        // javax.sound.sampled.spi.MixerProvider mp = (javax.sound.sampled.spi.MixerProvider) prov1;
      } catch (Throwable ignored) {
      }

      try {
        Class<?> p2 = Class.forName("com.sun.media.sound.DirectAudioDeviceProvider");
        Object prov2 = p2.getDeclaredConstructor().newInstance();
      } catch (Throwable ignored) {
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  private static void showSplash() {
    // macOS native-image does NOT support java.awt.SplashScreen
   /* 
    if (System.getProperty("os.name").toLowerCase().contains("mac")) {
        showSoftwareSplash();
        return;
    }

    SplashScreen ss = null;
    try {
        ss = SplashScreen.getSplashScreen();
    } catch (Throwable ignored) {
    }

    if (ss != null) return;
*/
    showSoftwareSplash();
  }

  private static void showSoftwareSplash() {
    try {
        InputStream is = JC64Dis.class.getResourceAsStream("/sw_emulator/swing/images/splash.png");
        if (is == null) {
            System.err.println("Splash image not found in native image");
            return;
        }

        BufferedImage img = ImageIO.read(is);
        if (img == null) {
            return;
        }

        JWindow w = new JWindow();
        w.getContentPane().add(new JLabel(new ImageIcon(img)));
        w.pack();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle screen = ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        int x = screen.x + (screen.width - w.getWidth()) / 2;
        int y = screen.y + (screen.height - w.getHeight()) / 2;
        w.setLocation(x, y);

        nativeSplash = w;
        usingNativeSplash = true;

        SwingUtilities.invokeLater(() -> w.setVisible(true));

    } catch (Throwable t) {
        t.printStackTrace();
    }
  }


  /**
   * Close the sofware splash screen
   */
  private static void closeSplash() {
    if (usingNativeSplash && nativeSplash != null) {
      nativeSplash.setVisible(false);
      nativeSplash.dispose();
      nativeSplash = null;
      usingNativeSplash = false;
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    String base = System.getProperty("user.dir");
    String confSoundPath = base + File.separator + "conf" + File.separator + "sound.properties";

    showSplash();

    if (isNativeImage()) {
      ensureAudioProviders();
      System.setProperty("javax.sound.config.file", confSoundPath);

      try {
        File f = new File(confSoundPath);
        if (f.exists() && f.isFile()) {
          Properties p = new Properties();
          try (FileInputStream fis = new FileInputStream(f)) {
            p.load(fis);
          }
          for (String name : p.stringPropertyNames()) {
            if (System.getProperty(name) == null) {
              System.setProperty(name, p.getProperty(name));
            }
          }
        }
      } catch (Throwable t) {
        t.printStackTrace();
      }
    }

    System.out.println("Mixer info:");
    for (javax.sound.sampled.Mixer.Info mi : javax.sound.sampled.AudioSystem.getMixerInfo()) {
      System.out.println(" - " + mi.getName() + " / " + mi.getDescription() + " / " + mi.getVendor());
    }
    System.out.println("Service files present:");
    try {
      java.util.Enumeration<java.net.URL> e = Thread.currentThread().getContextClassLoader().getResources("META-INF/services/javax.sound.sampled.MixerProvider");
      while (e.hasMoreElements()) {
        System.out.println(" - " + e.nextElement());
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    // debug TO REMOVE
    System.out.println("Running in native image: " + isNativeImage());
    System.out.println("javax.sound.config.file=" + System.getProperty("javax.sound.config.file"));
    System.out.println("java.home=" + System.getProperty("java.home"));

    new JC64Dis();
  }
}
