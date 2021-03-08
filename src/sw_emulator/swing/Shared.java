/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sw_emulator.swing;

import java.util.ArrayList;

/**
 * Contains all shared object accessible by the application
 * 
 * @author ice
 */
public class Shared {
  /** List of used frames. Each frame must register here */  
  public static ArrayList framesList=new ArrayList();   
  
  /** Version of the application */
  public static final String VERSION="1.1";
}
