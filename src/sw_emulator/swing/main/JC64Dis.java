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

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import javax.swing.SwingUtilities;
import sw_emulator.swing.JDisassemblerFrame;

/**
 * Java C64 disassembler with graphics
 *
 * @author ice
 */
public class JC64Dis {

  private JDisassemblerFrame jMainFrame;

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
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    String base = System.getProperty("user.dir");
    String confSoundPath = base + File.separator + "conf" + File.separator + "sound.properties";

    if (isNativeImage()) {
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

    // debug TO REMOVE
    System.out.println("Running in native image: " + isNativeImage());
    System.out.println("javax.sound.config.file=" + System.getProperty("javax.sound.config.file"));
    System.out.println("java.home=" + System.getProperty("java.home"));

    new JC64Dis();
  }
}
