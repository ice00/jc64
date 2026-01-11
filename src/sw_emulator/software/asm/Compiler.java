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
import java.io.FileWriter;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Permission;
import java.util.Set;
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
   * @param option the option to use 
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
    if (option==null) return "Internal error: no option selected";
    
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
      case GLASS:
        res=glassCompile(input, output);
        break;
      case AS:
        res=asCompile(input, output);
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
   * Compile the input file to the output file with KickAssembler
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */ 
  public String kickCompile(File input, File output) {
    String[] args = new String[3];
    args[0] = input.getAbsolutePath();
    args[1] = "-o";
    args[2] = output.getAbsolutePath();
    
    Set<String> kickAssemblerPackages = Set.of(
      "kickass",
      "sw_emulator.software.asm"
    );

    
    String result = SafeLibraryExecutor.executeInIsolatedClassLoader(
        "kickass.KickAssembler", args, kickAssemblerPackages, option
    );
    
    // Se l'esecuzione è andata bene, leggi l'output dal file temporaneo
    if (result.equals("Compilation done") || result.equals("Compilation done (exit prevented)")) {
        return SafeLibraryExecutor.getCompilationOutput(option);
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
    
    File tmp=new File(output.getAbsolutePath()+".tmp");
    
    orgStream = System.out;
    
    String result="No result obtained!!";
    String[] args=new String[3];
   
    args[0]="";
    args[1]=input.getAbsolutePath();    
    args[2]="-o"+tmp.getAbsolutePath();   
   
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
    
    
    try {
      FileWriter myWriter = new FileWriter(option.tmpPath+File.separator+"c64-asm.cfg");
      myWriter.write(
        "FEATURES {\n" +
        "    STARTADDRESS: default = $0801;\n" +
        "}\n" +
        "SYMBOLS {\n" +
        "}\n" +
        "MEMORY {\n" +
        "    ZP:      file = \"\", start = $0002,  size = $00FE,      define = yes;\n" +
        "    MAIN:     file = %O, start = %S,     size = $D000 - %S;\n" +
        "}\n" +
        "SEGMENTS {\n" +
        "    ZEROPAGE: load = ZP,       type = zp,  optional = yes;\n" +
        "    EXEHDR:   load = MAIN,     type = ro,  optional = yes;\n" +
        "    CODE:     load = MAIN,     type = rw;\n" +
        "    RODATA:   load = MAIN,     type = ro,  optional = yes;\n" +
        "    DATA:     load = MAIN,     type = rw,  optional = yes;\n" +
        "    BSS:      load = MAIN,     type = bss, optional = yes, define = yes;\n" +
        "}"
      );
      myWriter.close();
    } catch (Exception e) {
        System.err.println(e);
      } 
        
    args=new String[4];
    args[0]="";
    args[1]="-o"+output.getAbsolutePath(); 
    args[2]="-C"+option.tmpPath+File.separator+"c64-asm.cfg";
    args[3]=tmp.getAbsolutePath();    
  
   
    try {
      fileStream = new PrintStream(option.tmpPath+File.separator+"tmp.tmp");
      System.setOut(fileStream);
      System.setErr(fileStream);  
        
      Class cl = Class.forName("sw_emulator.software.asm.Ld65");
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
  
  /**
   * Compile the input file to the output file with Glass
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */ 
  public String glassCompile(File input, File output) {
    String[] args = new String[2];
    args[0] = input.getAbsolutePath();
    args[1] = output.getAbsolutePath();
    
    Set<String> glassPackages = Set.of("nl.grauw.glass");
    
    String result = SafeLibraryExecutor.executeInIsolatedClassLoader(
        "nl.grauw.glass.Assembler", args, glassPackages, option
    );
    
    // Se l'esecuzione è andata bene, leggi l'output dal file temporaneo
    if (result.equals("Compilation done") || result.equals("Compilation done (exit prevented)")) {
        return SafeLibraryExecutor.getCompilationOutput(option);
    }
    
    return result;
}
  
  /**
   * Compile the input file to the output file with AS (Macro assembler)
   * 
   * @param input the input file 
   * @param output the output file
   * @return the io message from the appplication
   */ 
  public String asCompile(File input, File output) {
    PrintStream orgStream;
    PrintStream errStream;
    PrintStream fileStream;
        
    orgStream = System.out;
    errStream = System.err;
    
    
    String result="No result obtained!!";
    String[] args=new String[2];
   
    args[0]=" ";
    args[1]=input.getAbsolutePath();
    
    String[] args2=new String[2];
   
    args2[0]=" ";
    args2[1]=option.tmpPath+File.separator+"input.p";

   
    try {
      fileStream = new PrintStream(option.tmpPath+File.separator+"tmp.tmp");
      System.setOut(fileStream);
      System.setErr(fileStream);
        
      Class cl = Class.forName("sw_emulator.software.asm.Asl");
      Method mMain = cl.getMethod("run", new Class[]{String[].class});
      mMain.invoke(cl.newInstance(), new Object[]{args});
      
      fileStream.close();
      
    } catch (Exception e) {
        System.err.println(e);      
      }    
    
    System.setOut(orgStream);
    System.setErr(errStream);
    
    try {
       result = new String(Files.readAllBytes(Paths.get(option.tmpPath+File.separator+"tmp.tmp")), StandardCharsets.UTF_8);
       // remove the extra error message
       int pos=result.indexOf("org.ibex.nestedvm.Runtime$ExecutionException:");
       if (pos>0) result=result.substring(0, pos);
       if (pos==0) result="Compilation done";
    } catch (Exception e) {
        System.err.println(e);
      }   
    
    String result2="";               
    
    try {
      fileStream = new PrintStream(option.tmpPath+File.separator+"tmp.tmp");
      System.setOut(fileStream);
      System.setErr(fileStream);
             
      Class cl = Class.forName("sw_emulator.software.asm.P2bin");
      Method mMain = cl.getMethod("run", new Class[]{String[].class});
      mMain.invoke(cl.newInstance(), new Object[]{args2});
      
      
      fileStream.close();
      
    } catch (Exception e) {
        System.err.println(e);      
      }    
    
    System.setOut(orgStream);
    System.setErr(errStream);
    
    try {
       result2 = new String(Files.readAllBytes(Paths.get(option.tmpPath+File.separator+"tmp.tmp")), StandardCharsets.UTF_8);
       // remove the extra error message
       int pos=result2.indexOf("org.ibex.nestedvm.Runtime$ExecutionException:");
       if (pos>0) result2=result2.substring(0, pos);
       if (pos==0) result2="Linking done";
    } catch (Exception e) {
        System.err.println(e);
      }   

    return result+"\n"+result2;    
  }
}
