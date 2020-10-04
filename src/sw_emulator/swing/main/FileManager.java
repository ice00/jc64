/**
 * @(#)FileManager.java 2019/12/01
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import sw_emulator.software.MemoryDasm;

/**
 * Manage the files of disassembler
 * 
 * @author ice
 */
public class FileManager {
  /** Public access to file manager */
  public static final FileManager instance=new FileManager();
    
  /** File to use for option*/
  public static final File optionFile=new File(System.getProperty("user.home")+File.separator+".jc64dis");
    
  /**
   * Singleton contructor
   */
  private FileManager() {
  }
    
  /**
   * Read option file
   * If file is not present, operation is ok
   * 
   * @param file the file to use
   * @param option the option to fill
   * @return true if operation is ok
   */
  public boolean readOptionFile(File file, Option option) {
    try {      
      DataInputStream in=new DataInputStream(
                         new BufferedInputStream(
                         new FileInputStream(file))); 
     
      option.opcodeUpperCasePreview = in.readBoolean();
      option.opcodeUpperCaseSource = in.readBoolean();
      option.illegalOpcodeMode = in.readByte();
      option.commentLanguage = in.readByte();     
      option.useAsCode = in.readBoolean();
      option.eraseDComm = in.readBoolean();
      option.erasePlus = in.readBoolean();
      option.maxLabelLength = in.readInt();
      option.maxAggregate = in.readInt();
      
      option.psidInitSongsLabel = in.readUTF();
      option.psidPlaySoundsLabel = in.readUTF();
      option.sidFreqLoLabel = in.readUTF();
      option.sidFreqHiLabel = in.readUTF();
      
      // 0.8     
      option.theme = in.readInt();
      option.lafName = in.readUTF();
      option.flatLaf = in.readUTF();
      
      // 0.9
      option.commentZeroPage = in.readBoolean();
      option.commentStackArea = in.readBoolean();
      option.comment200Area = in.readBoolean();
      option.comment300Area = in.readBoolean();
      option.commentScreenArea = in.readBoolean();
      option.commentBasicFreeArea = in.readBoolean();
      option.commentBasicRom = in.readBoolean();
      option.commentFreeRam = in.readBoolean();
      option.commentVicII = in.readBoolean();
      option.commentSid = in.readBoolean();
      option.commentColorArea = in.readBoolean();          
      option.commentCia1 = in.readBoolean();
      option.commentCia2 = in.readBoolean();
      option.commentKernalRom = in.readBoolean();                      
    } catch (FileNotFoundException e) {
         return true; 
    } catch (Exception e) {
        System.err.println(e);
        return false;
      }  
    return true;
  }
  
  /**
   * Write the option output file
   * 
   * @param file the file to write
   * @param option the option to write
   * @return true if operation is ok
   */
  public boolean writeOptionFile(File file, Option option) {
    try {      
      DataOutputStream out=new DataOutputStream(
                           new BufferedOutputStream(
                           new FileOutputStream(file))); 
      
    
      out.writeBoolean(option.opcodeUpperCasePreview);
      out.writeBoolean(option.opcodeUpperCaseSource);
      out.writeByte(option.illegalOpcodeMode);
      out.writeByte(option.commentLanguage);
      out.writeBoolean(option.useAsCode);
      out.writeBoolean(option.eraseDComm);
      out.writeBoolean(option.erasePlus);
      out.writeInt(option.maxLabelLength);
      out.writeInt(option.maxAggregate);
      
      out.writeUTF(option.psidInitSongsLabel);
      out.writeUTF(option.psidPlaySoundsLabel);
      out.writeUTF(option.sidFreqLoLabel);
      out.writeUTF(option.sidFreqHiLabel);
      
      // 0.8
      out.writeInt(option.theme);
      out.writeUTF(option.lafName);
      out.writeUTF(option.flatLaf);
      
      // 0.9
      out.writeBoolean(option.commentZeroPage);
      out.writeBoolean(option.commentStackArea);
      out.writeBoolean(option.comment200Area);
      out.writeBoolean(option.comment300Area);
      out.writeBoolean(option.commentScreenArea);
      out.writeBoolean(option.commentBasicFreeArea);
      out.writeBoolean(option.commentBasicRom);
      out.writeBoolean(option.commentFreeRam);
      out.writeBoolean(option.commentVicII);
      out.writeBoolean(option.commentSid);
      out.writeBoolean(option.commentColorArea);          
      out.writeBoolean(option.commentCia1);
      out.writeBoolean(option.commentCia2);
      out.writeBoolean(option.commentKernalRom);
      
      out.flush();
      out.close();
    } catch (Exception e) {
        System.err.println(e);
        return false;
      }  
    return true;
  }    
  
  /**
   * Read the project from file 
   * 
   * @param file the file to read
   * @param project the project to fill from file
   * @return true if operation is ok
   */
  public boolean readProjectFile(File file, Project project) {
    try {      
      DataInputStream in=new DataInputStream(
                         new BufferedInputStream(
                         new FileInputStream(file)));  
      
      byte version=in.readByte();
    
      project.name=in.readUTF();
      project.file=in.readUTF();
      project.description=in.readUTF();
      project.fileType=FileType.valueOf(in.readUTF());
      
      int size=in.readInt();      
      project.inB=new byte[size];
      in.read(project.inB);
      
      size=in.readInt();
      project.memoryFlags=new byte[size];
      in.read(project.memoryFlags);
      
      size=in.readInt();
      project.memory=new MemoryDasm[size];
      for (int i=0; i<project.memory.length; i++) {
        project.memory[i]=new MemoryDasm();  
        project.memory[i].address=in.readInt();
        
        if (in.readBoolean()) project.memory[i].dasmComment=in.readUTF();
        else project.memory[i].dasmComment=null;
        
        if (in.readBoolean()) project.memory[i].userComment=in.readUTF();
        else project.memory[i].userComment=null;
        
        if (in.readBoolean()) project.memory[i].userBlockComment=in.readUTF();
        else project.memory[i].userBlockComment=null;
        
        if (in.readBoolean()) project.memory[i].dasmLocation=in.readUTF();
        else project.memory[i].dasmLocation=null;
        
        if (in.readBoolean()) project.memory[i].userLocation=in.readUTF();
        else project.memory[i].userLocation=null;
        
        project.memory[i].isInside=in.readBoolean();
        project.memory[i].isCode=in.readBoolean();
        project.memory[i].isData=in.readBoolean();
        if (version>0) project.memory[i].isGarbage=in.readBoolean(); // version 1
        project.memory[i].copy=in.readByte();
        project.memory[i].related=in.readInt();
        project.memory[i].type=in.readChar();
        
        if (project.fileType==FileType.MPR) {
          project.mpr=new MPR();
          project.mpr.getElements(project.inB);
        }
      }
      
    } catch (Exception e) {
        System.err.println(e);
        return false;
      }  
      
    return true;  
  }
  
  /**
   * Write the project output file
   * 
   * @param file the file to write
   * @param project the project to write
   * @return true if operation is ok
   */
  public boolean writeProjectFile(File file, Project project) {
    try {      
      DataOutputStream out=new DataOutputStream(
                           new BufferedOutputStream(
                           new FileOutputStream(file))); 
      
      out.writeByte(project.ACTUAL_VERSION);
      
      out.writeUTF(project.name);
      out.writeUTF(project.file);
      out.writeUTF(project.description);
      out.writeUTF(project.fileType.name());
      
      out.writeInt(project.inB.length);
      out.write(project.inB);
      
      out.writeInt(project.memoryFlags.length);
      out.write(project.memoryFlags);
      
      out.writeInt(project.memory.length);
      for (MemoryDasm memory : project.memory) {
          out.writeInt(memory.address);
          
          if (memory.dasmComment!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.dasmComment);
          } else {
              out.writeBoolean(false);
            }
          
          if (memory.userComment!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.userComment);
          } else {
              out.writeBoolean(false);
            }
          
          if (memory.userBlockComment!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.userBlockComment);
          } else {
              out.writeBoolean(false);
            }         
          
          if (memory.dasmLocation!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.dasmLocation);
          } else {
              out.writeBoolean(false);
            }
          
          if (memory.userLocation!=null) {
              out.writeBoolean(true);
              out.writeUTF(memory.userLocation);
          } else {
              out.writeBoolean(false);
            }      
          
          out.writeBoolean(memory.isInside);
          out.writeBoolean(memory.isCode);
          out.writeBoolean(memory.isData);
          out.writeBoolean(memory.isGarbage);  // version 1
          out.writeByte(memory.copy);
          out.writeInt(memory.related);
          out.writeChar(memory.type);
      }  
      
      out.flush();
      out.close();
    } catch (Exception e) {
        System.err.println(e);
        return false;
      }  
    return true;
  }  
  
  /**
   * Read the file to disassemble
   * 
   * @param inN the input name
   * @return the buffer with data
   * @throws java.io.FileNotFoundException
   * @throws IOException
   * @throws SecurityException
   */
  public byte[] readFile(String inN) throws FileNotFoundException, IOException, SecurityException {
    byte[] inB = new byte[66000];      
    int size;
    BufferedInputStream inF;  
      
    // see if the file is present
    inF=new BufferedInputStream(new FileInputStream(inN));
                 
    // read the file
     size=inF.read(inB);
     inF.close();
     
     if (size<=0) return null;
     
     // resize to the read size of read byte
     byte[] res=new byte[size];
     System.arraycopy(inB, 0, res, 0, size);
     return res;
  }

  /**
   * Wtyie the given text to file
   * 
   * @param file the file to use
   * @param text the text to write
   * @return true if operation is ok
   */
    public boolean writeTxtFile(File file, String text) {
      try (
        FileWriter writer=new FileWriter(file);
        BufferedWriter bw=new BufferedWriter(writer)) {
        bw.write(text);
      } catch (IOException e) {
          System.err.println(e);
          return false;
        }
      return true;  
    }
}
