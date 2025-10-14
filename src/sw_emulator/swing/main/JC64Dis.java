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
        Option option=new Option();
        
        // read option file even if it will be reload again in JDisassemblerFrame
        FileManager.instance.readOptionFile(FileManager.OPTION_FILE, option);
        
        if (option.getLafName().equals("SYNTH")) Option.useLookAndFeel(option.getFlatLaf());
        else Option.useLookAndFeel(option.getLafName(), option.getMethalTheme());  
                
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          jMainFrame=new JDisassemblerFrame();
          jMainFrame.setVisible(true);
        }
      });  
    }
       
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {    
      
    /**  
      // Log all focus messages.
      final Logger rootLogger = Logger.getLogger("");
      rootLogger.setLevel(Level.ALL);
      final ConsoleHandler consoleHandler = new ConsoleHandler();
      consoleHandler.setLevel(Level.ALL);
      // Because there are a lot of focus messages, make them
      // a little easier to read by logging only the message.
      consoleHandler.setFormatter(new Formatter() {
        @Override
        public String format(LogRecord record) {
          return "FOCUS: " + record.getMessage() + '\n';
        }
      });
      final Logger logger = Logger.getLogger("java.awt.focus.Component");
      logger.setLevel(Level.ALL);
      logger.setUseParentHandlers(false);
      logger.addHandler(consoleHandler);
    */  
      new JC64Dis();
    }  
}
