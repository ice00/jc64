/**
 * @(#)ProjectFileView.java 2023/06/04
 *
 * ICE Team free software group
 *
 * This file is part of JIIT64 Java Ice Team Tracker 64
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
package sw_emulator.swing;

import java.io.File;
import java.util.Hashtable;
import javax.swing.Icon;
import javax.swing.filechooser.FileView;
import sw_emulator.swing.main.FileManager;
import sw_emulator.swing.main.Option;

/**
 * Add custom icon based onto % of project state
 * We cache the already reas files to speed up, that means that the perc. is 
 * the inital one, and is not update in real time
 * 
 * @author ice
 */
public class ProjectFileView extends FileView {  
  Icon icon0, icon25, icon50, icon75, icon100;
  Hashtable<String, Integer> hashFile=new Hashtable();  // remember already read files
  Option option;
  
  
  public ProjectFileView(Option option) {
    this.option=option;
    icon0=new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/I0.png"));
    icon25=new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/I25.png"));
    icon50=new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/I50.png"));
    icon75=new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/I75.png"));
    icon100=new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/icons/mini/I100.png"));
  } 

  @Override
  public Icon getIcon(File f) {
    if (!option.chooserPerc) return super.getIcon(f);     
    if (f.isDirectory()) return super.getIcon(f);    
    if (!f.getName().toLowerCase().endsWith(".dis")) return super.getIcon(f);
    
    int perc;
    
    String key=f.getAbsolutePath();
    
    if (hashFile.containsKey(key)) perc=hashFile.get(key);
    else {
      perc=FileManager.instance.readPercProjectFile(f);
      hashFile.put(key, perc);
    }
    
    if (perc<0) return super.getIcon(f);     
    if (perc<20) return icon0;
    if (perc<40) return icon25;
    if (perc<60) return icon50;
    if (perc<90) return icon75;
    return icon100;
  }
  
}
