/*
 * jMemorize - Learning made easy (and fun) - A Leitner flashcards tool
 * Copyright(C) 2004-2006 Riad Djemili
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 1, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package sw_emulator.swing.main;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * A simple data structure to store recent items (e.g. recent file in a menu or
 * recent search text in a search dialog).
 * 
 * @author djemili
 */
public class RecentItems {   
  public final static String RECENT_ITEM_STRING = "recent.item."; 
    
  private int m_maxItems=9;
  private Preferences m_prefNode=Preferences.userRoot().node(this.getClass().getName());

  private List<String> m_items=new ArrayList<String>();
    
  public RecentItems() {      
     loadFromPreferences();
  }
    
  public void push(String item) {
    loadFromPreferences(); // force load as multiple instances can use those preferences 

    m_items.remove(item);
    m_items.add(0, item);
        
    if (m_items.size() > m_maxItems) {
      m_items.remove(m_items.size() - 1);
    }    
        
    update();
  }
    
  public void remove(String item) {
    m_items.remove(item);
    update();
  }
    
  public String get(int index) {
    return (String)m_items.get(index);
  }
    
  public List<String> getItems() {
    return m_items;
  }
    
  public int size() {
    return m_items.size();
  }
  
  public void reload() {
    loadFromPreferences();  
  }
    
  private void update() {
    storeToPreferences();
  }    
    
  private void loadFromPreferences() {
    m_items.clear();
      
    // load recent files from properties
    for (int i = 0; i < m_maxItems; i++) {
      String val = m_prefNode.get(RECENT_ITEM_STRING+i, ""); //$NON-NLS-1$

      if (!val.equals("")) m_items.add(val);
      else break;
          
    }
  }
    
  private void storeToPreferences() {
    for (int i = 0; i < m_maxItems; i++) {
      if (i < m_items.size()) m_prefNode.put(RECENT_ITEM_STRING+i, (String)m_items.get(i));
      else m_prefNode.remove(RECENT_ITEM_STRING+i);          
    }
  }
}