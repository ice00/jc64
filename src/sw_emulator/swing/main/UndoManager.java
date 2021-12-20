/*
 * @(#)UndoManager.java 2021/12/20
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
package sw_emulator.swing.main;

import com.rits.cloning.Cloner;
import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Undo functionality
 * 
 * @author ice
 */
public class UndoManager {
   /** Max capacity of undo */  
   int maxCapacity=9;   
   
   
   /** The deque for undo of project */
   ArrayDeque<KeyProject> dequeProject=new ArrayDeque(maxCapacity); 
   
   /**
   * Clear all the undo
   */
  public void clear() {
    dequeProject.clear();
  }
  
  /**
   * Resize the max capacity of undo
   * 
   * @param capacity the new capacity
   */
  public void resize(int capacity) {
    maxCapacity=capacity;
    while (dequeProject.size()>maxCapacity) dequeProject.pollLast();
  }
  
  /**
   * Store the actual value for undo action according with the given type
   * 
   * @param key the key to use
   * @param project the project to store   
   */
  public void store(String key, Project project) {
    Project copy;
    KeyProject keyProject=new KeyProject();
    Cloner cloner=new Cloner();  
    

    copy=cloner.deepClone(project);
    keyProject.key=key;
    keyProject.project=copy;
        
        
    dequeProject.addFirst(keyProject);
    
    // resize deque if needed
    while (dequeProject.size()>maxCapacity) dequeProject.pollLast();                 
  }
  
  /**
   * Retrieve the value for undo action according with the given type
   * 
   * @param key the key to search
   * @return the saved project (or null)
   */
  public Project retrieve(String key) {   
    if (dequeProject.isEmpty()) return null;  
    
    KeyProject keyProject;
    
    Iterator<KeyProject> iter=dequeProject.iterator();
    while (iter.hasNext()) {
      keyProject=iter.next();
      if (keyProject.key.equals(key)) return keyProject.project;
    }   
        
    return null;
  }  
  
  
  /**
   * Get all elements 
   * 
   * @return all elements iterator
   */
  public Iterator<KeyProject> getAll() {
    return dequeProject.descendingIterator();
  }
}
