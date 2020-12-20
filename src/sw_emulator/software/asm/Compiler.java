/**
 * @(#)compiler 2020/12/19
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
package sw_emulator.software.asm;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import sw_emulator.swing.main.Option;

/**
 * Compiler: call real compilers 
 * All compiler are ported into Java via NestedVM unless a Java version is available
 * 
 * @author ice
 */
public class Compiler {
    
  /** Option */  
  Option option;  
  
  /**
   * Set the compiler to use 
   * 
   * @param name the name of compiler
   */
  public void setOption(Option option) {
    this.option=option; 
  }
  
  /**
   * Compile the input file to the output file
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */
  public String compile(File input, File output) {
    if (option==null) return "Internal erro: no option selected";
    
    String res="";  
      
    switch (option.assembler) {
      case DASM:
        res=dasmCompile(input, output);
        break;
      case TMPX:
        break;  
      case CA65:
        break;
      case TASS64:
        break;
      case KICK:
        break;  
    }
    
    return res;  
  }
  
  /**
   * Compile the input file to the output file with Dasm
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */ 
  public String dasmCompile(File input, File output) {
    PrintStream orgStream   = null;
    PrintStream fileStream  = null;
    
    orgStream = System.out;
    
    String result="No result obtained!!";
    String[] args=new String[3];
    
    args[0]=" ";
    args[1]=input.getAbsolutePath();
    args[2]="-o"+output.getAbsolutePath();
    
    try {
      fileStream = new PrintStream("/tmp/tmp.tmp");
      System.setOut(fileStream);
      //System.setErr(fileStream);  
        
      Class cl = Class.forName("sw_emulator.software.asm.Dasm");
      Method mMain = cl.getMethod("run", new Class[]{String[].class});
      mMain.invoke(cl.newInstance(), new Object[]{args});
      
    } catch (Exception e) {
        System.err.println(e);
      }      
    System.setOut(orgStream);
    //System.setErr(orgStream);
    
    try {
       result = new String(Files.readAllBytes(Paths.get("/tmp/tmp.tmp")), StandardCharsets.UTF_8);
    } catch (Exception e) {
        System.err.println(e);
      }   
    
    return result;
  }
}
