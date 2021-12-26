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
import java.security.Permission;
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
        res=tmpxCompile(input, output);
        break;  
      case ACME:
        res=acmeCompile(input, output);  
        break;   
      case CA65:
        res=ca65Compile(input, output);   
        break;
      case TASS64:
        res=tass64Compile(input, output);    
        break;
      case KICK:
        res=kickCompile(input, output);  
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
    PrintStream orgStream;
    PrintStream fileStream;
    
    orgStream = System.out;
    
    String result="No result obtained!!";
    String[] args=new String[4];
    
    args[0]=" ";
    args[1]=input.getAbsolutePath();
    args[2]="-o"+output.getAbsolutePath();
    if (option.dasmF3Comp) args[3]="-f3";
    else args[3]="-f1";
    
    try {
      fileStream = new PrintStream(option.tmpPath+File.separator+"tmp.tmp");
      System.setOut(fileStream);
      //System.setErr(fileStream);  
        
      Class cl = Class.forName("sw_emulator.software.asm.Dasm");
      Method mMain = cl.getMethod("run", new Class[]{String[].class});
      mMain.invoke(cl.newInstance(), new Object[]{args});
      
    } catch (Exception e) {
        System.err.println(e);
      }      
    System.setOut(orgStream);
    System.setErr(orgStream);
    
    try {
       result = new String(Files.readAllBytes(Paths.get(option.tmpPath+File.separator+"tmp.tmp")), StandardCharsets.UTF_8);
    } catch (Exception e) {
        System.err.println(e);
      }   
    
    return result;
  }
  
  /**
   * Compile the input file to the output file with Kick Assembler
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */ 
  public String kickCompile(File input, File output) {
    PrintStream orgStream = System.out;
    PrintStream fileStream;  
    
    String result="No result obtained!!";
      
    String[] args=new String[3];
    
       
    args[0]=input.getAbsolutePath();
    args[1]="-o";
    args[2]=output.getAbsolutePath();  
    
    try {
      fileStream = new PrintStream(option.tmpPath+File.separator+"tmp.tmp");
      System.setOut(fileStream);
      
     // install security manager to avoid System.exit() call from lib
     SecurityManager   previousSecurityManager = System.getSecurityManager();
     final SecurityManager securityManager  = new SecurityManager() {
        @Override public void checkPermission(final Permission permission) {
        if (permission.getName() != null && permission.getName().startsWith("exitVM")) {
          throw new SecurityException();
         }
      }
    };
    System.setSecurityManager(securityManager);

    try {
      kickass.KickAssembler.main(args);
    } catch (SecurityException e) {
      // Say hi to your favorite creator of closed source software that includes System.exit() in his code.
      } finally {
          System.setSecurityManager(previousSecurityManager);
        }   
    } catch (Exception e) {
       System.err.println(e);
    }
    
    System.setOut(orgStream);
    
    try {
       result = new String(Files.readAllBytes(Paths.get(option.tmpPath+File.separator+"tmp.tmp")), StandardCharsets.UTF_8);
       // remove the extra error message
       int pos=result.indexOf("org.ibex.nestedvm.Runtime$ExecutionException:");
       if (pos>0) result=result.substring(0, pos);
       if ("".equals(result)) result="Compilation done";
    } catch (Exception e) {
        System.err.println(e);
      }   
    
    return result;   
    
  }

  /**
   * Compile the input file to the output file with TmpX
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */  
    public String tmpxCompile(File input, File output) {
      return "TMPX native compiler not available. Sorry";
    }
    
  /**
   * Compile the input file to the output file with Acme
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */ 
  public String acmeCompile(File input, File output) {
    PrintStream orgStream;
    PrintStream fileStream;
    
    orgStream = System.out;
    
    String result="No result obtained!!";
    String[] args=new String[4];
    
    args[0]="";
    args[1]="-o";
    args[2]=output.getAbsolutePath();
    args[3]=input.getAbsolutePath();
    
    try {
      fileStream = new PrintStream(option.tmpPath+File.separator+"tmp.tmp");
      System.setOut(fileStream);
      System.setErr(fileStream);  
        
      Class cl = Class.forName("sw_emulator.software.asm.Acme");
      Method mMain = cl.getMethod("run", new Class[]{String[].class});
      mMain.invoke(cl.newInstance(), new Object[]{args});
      
    } catch (Exception e) {
        System.err.println(e);
      }      
    System.setOut(orgStream);
    System.setErr(orgStream);
    
    try {
       result = new String(Files.readAllBytes(Paths.get(option.tmpPath+File.separator+"tmp.tmp")), StandardCharsets.UTF_8);
       // remove the extra error message
       int pos=result.indexOf("org.ibex.nestedvm.Runtime$ExecutionException:");
       if (pos>0) result=result.substring(0, pos);
       if ("".equals(result)) result="Compilation done";
    } catch (Exception e) {
        System.err.println(e);
      }   
    
    return result;
  }    
  
  /**
   * Compile the input file to the output file with Tass64
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */ 
  public String tass64Compile(File input, File output) {
    PrintStream orgStream;
    PrintStream fileStream;
    
    orgStream = System.out;
    
    String result="No result obtained!!";
    String[] args=new String[3];
    
    args[0]="";
    args[1]="-o"+output.getAbsolutePath();
    args[2]=input.getAbsolutePath();
    
    try {
      fileStream = new PrintStream(option.tmpPath+File.separator+"tmp.tmp");
      System.setOut(fileStream);
      System.setErr(fileStream);  
        
      Class cl = Class.forName("sw_emulator.software.asm.Tass64");
      Method mMain = cl.getMethod("run", new Class[]{String[].class});
      mMain.invoke(cl.newInstance(), new Object[]{args});
      
    } catch (Exception e) {
        System.err.println(e);
      }      
    System.setOut(orgStream);
    System.setErr(orgStream);
    
    try {
       result = new String(Files.readAllBytes(Paths.get(option.tmpPath+File.separator+"tmp.tmp")), StandardCharsets.UTF_8);
       // remove the extra error message
       int pos=result.indexOf("org.ibex.nestedvm.Runtime$ExecutionException:");
       if (pos>0) result=result.substring(0, pos);
    } catch (Exception e) {
        System.err.println(e);
      }   
    
    return result;
  }  
  
  /**
   * Compile the input file to the output file with Ca65
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */ 
  public String ca65Compile(File input, File output) {
    PrintStream orgStream;
    PrintStream fileStream;
    
    orgStream = System.out;
    
    String result="No result obtained!!";
    String[] args=new String[3];
   
    args[0]="";
    args[1]=input.getAbsolutePath();    
    args[2]="-o"+output.getAbsolutePath();   
   
    try {
      fileStream = new PrintStream(option.tmpPath+File.separator+"tmp.tmp");
      System.setOut(fileStream);
      System.setErr(fileStream);  
        
      Class cl = Class.forName("sw_emulator.software.asm.Ca65");
      Method mMain = cl.getMethod("run", new Class[]{String[].class});
      mMain.invoke(cl.newInstance(), new Object[]{args});
      
    } catch (Exception e) {
        System.err.println(e);
      }      
    System.setOut(orgStream);
    System.setErr(orgStream);
    
    try {
       result = new String(Files.readAllBytes(Paths.get(option.tmpPath+File.separator+"tmp.tmp")), StandardCharsets.UTF_8);
       // remove the extra error message
       int pos=result.indexOf("org.ibex.nestedvm.Runtime$ExecutionException:");
       if (pos>0) result=result.substring(0, pos);
       if (pos==0) result="Compilation done";
    } catch (Exception e) {
        System.err.println(e);
      }   
    
    return result;
  }
}
