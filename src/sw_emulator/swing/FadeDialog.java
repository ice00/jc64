/**
 * @(#)FadeDialog.java 2023/10/17
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
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package sw_emulator.swing;

import java.awt.BorderLayout;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import sw_emulator.software.sidid.CRSID;
import sw_emulator.swing.main.FileManager;
import sw_emulator.swing.main.Option;

/**
 * Fade dialog for easter eggs
 */
public class FadeDialog extends JDialog {
  private float rate = 0.15f;
  private float alfa = 0;
  private JLabel label;

  private Timer timerIn=null;
  private Timer timerOut=null;  
  private CRSID crsid;

    
  /**
   * Return the easter egg if it is activated
   * 
   * @return the egg or null
   */
  private EasterEgg getEgg() {
    EasterEgg[] list= EasterEgg.values();
    
    for (EasterEgg egg : list) {
      if (egg.isNow()) return egg;
    }
    
    return null;
  }
  
  /**
   * Construct the dialog 
   * 
   * @param option the option to use
   * @param parent the parent grame
   */
  FadeDialog(Option option, JFrame parent) {
    EasterEgg egg=getEgg();
    
    if (egg==null) return;
      
    super.setLocationRelativeTo(parent);
    super.setLocation(0, 0);
    setAlwaysOnTop(true);
    
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setUndecorated(true); //opacity supported for undecorated JDialogs
    
    getContentPane().add(new ContentPane(), BorderLayout.CENTER);

    label.addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labelMouseClicked(evt);
      }
    });
    label.addKeyListener(new java.awt.event.KeyAdapter() {
      @Override
      public void keyPressed(java.awt.event.KeyEvent evt) {
        labelKeyPressed(evt);
      }
    });
    
    label.setIcon(new javax.swing.ImageIcon(getClass().getResource(egg.image))); // NOI18N

    pack();
    setOpacity(0);
    setVisible(true);
    
    crsid=new CRSID();
    crsid.init(44100);

    byte[] data;
    
    try {      
      InputStream in=getClass().getResourceAsStream(egg.sid);
      data = in.readAllBytes();
                 
      File file=new File(option.tmpPath+File.separator+"SID.sid");     
    
      // write the file in temporary path
      FileManager.instance.writeFile(file, data);
      crsid.playSIDfile(file.getAbsolutePath(), egg.tune);
    
    } catch (Exception ex) {
        System.err.println(ex);
    }
    

    timerIn = new Timer(100, e -> fadeIn());
    timerIn.setInitialDelay(100);
    timerIn.start();
  }

  /**
   * Fade in of image
   */
  private void fadeIn() {
    alfa += rate;
    if (alfa > 1) {
      alfa = 1;
      timerIn.stop();
    }

    setOpacity(alfa);
  }

  /**
   * Fade out of image
   */
  private void fadeOut() {
    alfa -= rate;
    if (alfa < 0) {
      alfa = 0;
      timerOut.stop();
      setVisible(false);
      
    }

    setOpacity(alfa);
  }

  /**
   * Manage a key press action by closing the easter egg
   * 
   * @param evt the event
   */
  private void labelKeyPressed(java.awt.event.KeyEvent evt) {
    close();
  }

  /**
   * Manage a mouse click by closing the easter egg
   * 
   * @param evt the event
   */
  private void labelMouseClicked(java.awt.event.MouseEvent evt) {
    close();
  }

  /**
   * Close the easter egg
   */
  private void close() {
    timerOut = new Timer(100, e -> fadeOut());
    timerOut.setInitialDelay(100);
    timerOut.start();
    crsid.stopPlaying();
  }

  /**
   * Panel with image to show
   */
  class ContentPane extends JPanel {

    ContentPane() {
      //setPreferredSize(new Dimension(200, 100));
      setLayout(new BorderLayout());

      label = new JLabel("");
      //label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sw_emulator/swing/images/hw.png"))); // NOI18N
      add(label, BorderLayout.CENTER);
    }

  }

  public static void main(String[] args) {
    new FadeDialog(new Option(), null);
  }
}

/**
 * Easter eggs events with images and tunes 
 */
enum EasterEgg{  
  HALLOWEEN(31, 10, "/sw_emulator/swing/images/hw.png", "/sw_emulator/swing/images/Driller_Hub_Style.sid", 1),
  CHRISTMAS(25, 12, "/sw_emulator/swing/images/ch.png", "/sw_emulator/swing/images/Silent_Night.sid", 1),
  EASTER("/sw_emulator/swing/images/ea.png","/sw_emulator/swing/images/Turtle.sid",  1);
  
  private final int day;
  private final int month;  
  
  public String image;
  public String sid;
  public int tune;
  
  /**
   * Init Easter egg with day/month and image
   * 
   * @param day the day to activate
   * @param month the month to activate
   * @param image the image path
   * @param sid the sid path
   * @param tune tune number
   */
  private EasterEgg(int day, int month, String image, String sid, int tune) {
    this.day = day;
    this.month = month;
    this.image = image;
    this.sid = sid;
    this.tune=tune;
  }
  
  /**
   * Init Easter egg with Easter date and image
   * 
   * @param image the image path
   * @param sid the sid path
   * @param tune tune number
   */
  private EasterEgg(String image, String sid, int tune) {
    Calendar cal=findHolyDay(Year.now().getValue());
    
    this.day=cal.get(Calendar.DAY_OF_MONTH);
    this.month=cal.get(Calendar.MONTH)+1;
    this.image=image;
    this.sid = sid;
    this.tune=tune;
  }
  
  /*
   * Compute the day of the year that Easter falls on. Step names E1 E2 etc.,
   * are direct references to Knuth, Vol 1, p 155. 
   */
  static final Calendar findHolyDay(int year) {
    int golden, century, x, z, d, epact, n;

    golden = (year % 19) + 1; /* E1: metonic cycle */
    century = (year / 100) + 1; /* E2: e.g. 1984 was in 20th C */
    x = (3 * century / 4) - 12; /* E3: leap year correction */
    z = ((8 * century + 5) / 25) - 5; /* E3: sync with moon's orbit */
    d = (5 * year / 4) - x - 10;
    epact = (11 * golden + 20 + z - x) % 30; /* E5: epact */
    if ((epact == 25 && golden > 11) || epact == 24)
      epact++;
    n = 44 - epact;
    n += 30 * (n < 21 ? 1 : 0); /* E6: */
    n += 7 - ((d + n) % 7);
    if (n > 31) /* E7: */
      return new GregorianCalendar(year, 4 - 1, n - 31); /* April */
    else
      return new GregorianCalendar(year, 3 - 1, n); /* March */
  }
  
  /**
   * True if the date is now
   * 
   * @return true if this is the event
   */
  public boolean isNow() {
    Calendar today = Calendar.getInstance();
    
    return today.get(Calendar.DAY_OF_MONTH)==day && today.get(Calendar.MONTH)+1==month;
  }
}