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
import sw_emulator.software.Assembler;
import sw_emulator.software.Assembler.Name;
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
  public static final File OPTION_FILE=new File(System.getProperty("user.home")+File.separator+".jc64dis");
    
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
      option.maxByteAggregate = in.readInt();
      
      option.psidInitSongsLabel = in.readUTF();
      option.psidPlaySoundsLabel = in.readUTF();
      option.sidFreqLoLabel = in.readUTF();
      option.sidFreqHiLabel = in.readUTF();
      
      // 0.8     
      option.theme = in.readInt();
      option.lafName = in.readUTF();
      option.flatLaf = in.readUTF();
      
      // 0.9
      option.numInstrSpaces = in.readInt();
      option.numInstrTabs = in.readInt();
      option.numDataSpaces = in.readInt();
      option.numDataTabs = in.readInt();
      option.labelOnSepLine = in.readBoolean();
      
      option.maxWordAggregate = in.readInt();
      option.maxTribyteAggregate = in.readInt();
      option.maxLongAggregate = in.readInt();
      
      option.dasmF3Comp = in.readBoolean();
      option.dasmStarting = Assembler.Starting.valueOf(in.readUTF());
      option.dasmOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.dasmLabel = Assembler.Label.valueOf(in.readUTF());
      option.dasmComment = Assembler.Comment.valueOf(in.readUTF());
      option.dasmBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.dasmByte = Assembler.Byte.valueOf(in.readUTF());
      option.dasmWord = Assembler.Word.valueOf(in.readUTF());
      
      option.dasmMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.dasmMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());
      
      option.tmpxStarting = Assembler.Starting.valueOf(in.readUTF());
      option.tmpxOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.tmpxLabel = Assembler.Label.valueOf(in.readUTF());
      option.tmpxComment = Assembler.Comment.valueOf(in.readUTF());
      option.tmpxBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.tmpxByte = Assembler.Byte.valueOf(in.readUTF());
      option.tmpxWord = Assembler.Word.valueOf(in.readUTF());   
      
      option.ca65Starting = Assembler.Starting.valueOf(in.readUTF());
      option.ca65Origin = Assembler.Origin.valueOf(in.readUTF());
      option.ca65Label = Assembler.Label.valueOf(in.readUTF());
      option.ca65Comment = Assembler.Comment.valueOf(in.readUTF());
      option.ca65BlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.ca65Byte = Assembler.Byte.valueOf(in.readUTF());
      option.ca65Word = Assembler.Word.valueOf(in.readUTF());     
      
      option.ca65MonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.ca65MultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());
 
      option.acmeStarting = Assembler.Starting.valueOf(in.readUTF());
      option.acmeOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.acmeLabel = Assembler.Label.valueOf(in.readUTF());
      option.acmeComment = Assembler.Comment.valueOf(in.readUTF());
      option.acmeBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.acmeByte = Assembler.Byte.valueOf(in.readUTF());
      option.acmeWord = Assembler.Word.valueOf(in.readUTF());  
      
      option.acmeMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.acmeMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());
            
      option.kickStarting = Assembler.Starting.valueOf(in.readUTF());
      option.kickOrigin = Assembler.Origin.valueOf(in.readUTF());
      option.kickLabel = Assembler.Label.valueOf(in.readUTF());
      option.kickComment = Assembler.Comment.valueOf(in.readUTF());
      option.kickBlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.kickByte = Assembler.Byte.valueOf(in.readUTF());
      option.kickWord = Assembler.Word.valueOf(in.readUTF());    
      
      option.kickMonoSprite = Assembler.MonoSprite.valueOf(in.readUTF());
      option.kickMultiSprite = Assembler.MultiSprite.valueOf(in.readUTF());
            
      option.tass64Starting = Assembler.Starting.valueOf(in.readUTF());
      option.tass64Origin = Assembler.Origin.valueOf(in.readUTF());
      option.tass64Label = Assembler.Label.valueOf(in.readUTF());
      option.tass64Comment = Assembler.Comment.valueOf(in.readUTF());
      option.tass64BlockComment = Assembler.BlockComment.valueOf(in.readUTF());
      option.tass64Byte = Assembler.Byte.valueOf(in.readUTF());
      option.tass64Word = Assembler.Word.valueOf(in.readUTF());
      
      
      option.commentC64ZeroPage = in.readBoolean();
      option.commentC64StackArea = in.readBoolean();
      option.commentC64_200Area = in.readBoolean();
      option.commentC64_300Area = in.readBoolean();
      option.commentC64ScreenArea = in.readBoolean();
      option.commentC64BasicFreeArea = in.readBoolean();
      option.commentC64BasicRom = in.readBoolean();
      option.commentC64FreeRam = in.readBoolean();
      option.commentC64VicII = in.readBoolean();
      option.commentC64Sid = in.readBoolean();
      option.commentC64ColorArea = in.readBoolean();          
      option.commentC64Cia1 = in.readBoolean();
      option.commentC64Cia2 = in.readBoolean();
      option.commentC64KernalRom = in.readBoolean(); 
      
      option.commentC128ZeroPage = in.readBoolean();
      option.commentC128StackArea = in.readBoolean();
      option.commentC128_200Area = in.readBoolean(); 
      option.commentC128_300Area = in.readBoolean(); 
      option.commentC128ScreenArea = in.readBoolean(); 
      
      option.commentC128AppProgArea = in.readBoolean();  
      option.commentC128BasicRom = in.readBoolean();  
      option.commentC128Cia1 = in.readBoolean();
      option.commentC128Cia2 = in.readBoolean();
      option.commentC128Color = in.readBoolean();
      option.commentC128DMA = in.readBoolean();  
      option.commentC128KernalRom = in.readBoolean();
      option.commentC128MMU = in.readBoolean();
      option.commentC128ScreenMem = in.readBoolean();
      option.commentC128UserBasic = in.readBoolean();
      option.commentC128VDC = in.readBoolean();
      option.commentC128VideoColor = in.readBoolean();  
      option.commentC128VicII = in.readBoolean();
      option.commentC128Sid = in.readBoolean();
      
      option.commentC1541ZeroPage = in.readBoolean();
      option.commentC1541StackArea = in.readBoolean();
      option.commentC1541_200Area = in.readBoolean();  
      option.commentC1541Buffer0 = in.readBoolean();  
      option.commentC1541Buffer1 = in.readBoolean();  
      option.commentC1541Buffer2 = in.readBoolean();  
      option.commentC1541Buffer3 = in.readBoolean();  
      option.commentC1541Buffer4 = in.readBoolean();  
      option.commentC1541Via1 = in.readBoolean();  
      option.commentC1541Via2 = in.readBoolean();  
      option.commentC1541Kernal = in.readBoolean();  
      
      option.commentPlus4ZeroPage = in.readBoolean(); 
      option.commentPlus4StackArea = in.readBoolean(); 
      option.commentPlus4_200Area = in.readBoolean(); 
      option.commentPlus4_300Area = in.readBoolean(); 
      option.commentPlus4_400Area = in.readBoolean(); 
      option.commentPlus4_500Area = in.readBoolean(); 
      option.commentPlus4_600Area = in.readBoolean(); 
      option.commentPlus4_700Area = in.readBoolean(); 
      option.commentPlus4ColorArea = in.readBoolean(); 
      option.commentPlus4VideoArea = in.readBoolean(); 
      option.commentPlus4BasicRamP = in.readBoolean(); 
      option.commentPlus4BasicRamN = in.readBoolean(); 
      option.commentPlus4Luminance = in.readBoolean(); 
      option.commentPlus4ColorBitmap = in.readBoolean(); 
      option.commentPlus4GraphicData = in.readBoolean(); 
      option.commentPlus4BasicRom = in.readBoolean(); 
      option.commentPlus4BasicExt = in.readBoolean(); 
      option.commentPlus4Caracter = in.readBoolean(); 
      option.commentPlus4Acia = in.readBoolean(); 
      option.commentPlus4_6529B_1   = in.readBoolean(); 
      option.commentPlus4_6529B_2 = in.readBoolean(); 
      option.commentPlus4Ted = in.readBoolean(); 
      option.commentPlus4Kernal = in.readBoolean();     
      
      option.commentVic20ZeroPage = in.readBoolean();
      option.commentVic20StackArea = in.readBoolean();
      option.commentVic20_200Area = in.readBoolean();
      option.commentVic20_300Area = in.readBoolean();
      option.commentVic20_400Area = in.readBoolean();
      option.commentVic20Vic = in.readBoolean();
      option.commentVic20Via1 = in.readBoolean();
      option.commentVic20Via2 = in.readBoolean();
      option.commentVic20UserBasic = in.readBoolean();
      option.commentVic20Screen = in.readBoolean();
      option.commentVic20_8kExp1 = in.readBoolean();
      option.commentVic20_8kExp2 = in.readBoolean();
      option.commentVic20_8kExp3 = in.readBoolean();
      option.commentVic20Character = in.readBoolean();
      option.commentVic20Color = in.readBoolean();
      option.commentVic20Block2 = in.readBoolean();
      option.commentVic20Block3 = in.readBoolean();
      option.commentVic20Block4 = in.readBoolean();
      option.commentVic20BasicRom = in.readBoolean();
      option.commentVic20KernalRom = in.readBoolean();
                  
      option.assembler = Name.valueOf(in.readUTF());
              
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
      out.writeInt(option.maxByteAggregate);
      
      out.writeUTF(option.psidInitSongsLabel);
      out.writeUTF(option.psidPlaySoundsLabel);
      out.writeUTF(option.sidFreqLoLabel);
      out.writeUTF(option.sidFreqHiLabel);
      
      // 0.8
      out.writeInt(option.theme);
      out.writeUTF(option.lafName);
      out.writeUTF(option.flatLaf);
      
      // 0.9
      out.writeInt(option.numInstrSpaces);
      out.writeInt(option.numInstrTabs);
      out.writeInt(option.numDataSpaces);
      out.writeInt(option.numDataTabs);
      out.writeBoolean(option.labelOnSepLine);
      
      out.writeInt(option.maxWordAggregate);
      out.writeInt(option.maxTribyteAggregate);
      out.writeInt(option.maxLongAggregate);
      
      out.writeBoolean(option.dasmF3Comp);
      out.writeUTF(option.dasmStarting.name());
      out.writeUTF(option.dasmOrigin.name());
      out.writeUTF(option.dasmLabel.name());
      out.writeUTF(option.dasmComment.name());
      out.writeUTF(option.dasmBlockComment.name());
      out.writeUTF(option.dasmByte.name());
      out.writeUTF(option.dasmWord.name());
      
      out.writeUTF(option.dasmMonoSprite.name());
      out.writeUTF(option.dasmMultiSprite.name());
      
      out.writeUTF(option.tmpxStarting.name());
      out.writeUTF(option.tmpxOrigin.name());
      out.writeUTF(option.tmpxLabel.name());
      out.writeUTF(option.tmpxComment.name());
      out.writeUTF(option.tmpxBlockComment.name());
      out.writeUTF(option.tmpxByte.name());
      out.writeUTF(option.tmpxWord.name());    
      
      out.writeUTF(option.ca65Starting.name());
      out.writeUTF(option.ca65Origin.name());
      out.writeUTF(option.ca65Label.name());
      out.writeUTF(option.ca65Comment.name());
      out.writeUTF(option.ca65BlockComment.name());
      out.writeUTF(option.ca65Byte.name());
      out.writeUTF(option.ca65Word.name());  
      
      out.writeUTF(option.ca65MonoSprite.name());
      out.writeUTF(option.ca65MultiSprite.name());
      
      out.writeUTF(option.acmeStarting.name());
      out.writeUTF(option.acmeOrigin.name());
      out.writeUTF(option.acmeLabel.name());
      out.writeUTF(option.acmeComment.name());
      out.writeUTF(option.acmeBlockComment.name());
      out.writeUTF(option.acmeByte.name());
      out.writeUTF(option.acmeWord.name());   
      
      out.writeUTF(option.acmeMonoSprite.name());
      out.writeUTF(option.acmeMultiSprite.name());
      
      out.writeUTF(option.kickStarting.name());
      out.writeUTF(option.kickOrigin.name());
      out.writeUTF(option.kickLabel.name());
      out.writeUTF(option.kickComment.name());
      out.writeUTF(option.kickBlockComment.name());
      out.writeUTF(option.kickByte.name());
      out.writeUTF(option.kickWord.name());     
      
      out.writeUTF(option.kickMonoSprite.name());
      out.writeUTF(option.kickMultiSprite.name());
      
      out.writeUTF(option.tass64Starting.name());
      out.writeUTF(option.tass64Origin.name());
      out.writeUTF(option.tass64Label.name());
      out.writeUTF(option.tass64Comment.name());
      out.writeUTF(option.tass64BlockComment.name());
      out.writeUTF(option.tass64Byte.name());
      out.writeUTF(option.tass64Word.name());     
      
      
      out.writeBoolean(option.commentC64ZeroPage);
      out.writeBoolean(option.commentC64StackArea);
      out.writeBoolean(option.commentC64_200Area);
      out.writeBoolean(option.commentC64_300Area);
      out.writeBoolean(option.commentC64ScreenArea);
      out.writeBoolean(option.commentC64BasicFreeArea);
      out.writeBoolean(option.commentC64BasicRom);
      out.writeBoolean(option.commentC64FreeRam);
      out.writeBoolean(option.commentC64VicII);
      out.writeBoolean(option.commentC64Sid);
      out.writeBoolean(option.commentC64ColorArea);          
      out.writeBoolean(option.commentC64Cia1);
      out.writeBoolean(option.commentC64Cia2);
      out.writeBoolean(option.commentC64KernalRom);
      
      out.writeBoolean(option.commentC128ZeroPage);
      out.writeBoolean(option.commentC128StackArea);
      out.writeBoolean(option.commentC128_200Area);
      out.writeBoolean(option.commentC128_300Area);
      out.writeBoolean(option.commentC128ScreenArea);
      out.writeBoolean(option.commentC128AppProgArea);  
      out.writeBoolean(option.commentC128BasicRom);  
      out.writeBoolean(option.commentC128Cia1);
      out.writeBoolean(option.commentC128Cia2);
      out.writeBoolean(option.commentC128Color);
      out.writeBoolean(option.commentC128DMA);  
      out.writeBoolean(option.commentC128KernalRom);
      out.writeBoolean(option.commentC128MMU);
      out.writeBoolean(option.commentC128ScreenMem);
      out.writeBoolean(option.commentC128UserBasic);
      out.writeBoolean(option.commentC128VDC);
      out.writeBoolean(option.commentC128VideoColor);
      out.writeBoolean(option.commentC128VicII);
      out.writeBoolean(option.commentC128Sid);
      
      out.writeBoolean(option.commentC1541ZeroPage);
      out.writeBoolean(option.commentC1541StackArea);
      out.writeBoolean(option.commentC1541_200Area);
      out.writeBoolean(option.commentC1541Buffer0);
      out.writeBoolean(option.commentC1541Buffer1);
      out.writeBoolean(option.commentC1541Buffer2);
      out.writeBoolean(option.commentC1541Buffer3);
      out.writeBoolean(option.commentC1541Buffer4);
      out.writeBoolean(option.commentC1541Via1);
      out.writeBoolean(option.commentC1541Via2);
      out.writeBoolean(option.commentC1541Kernal);
      
      out.writeBoolean(option.commentPlus4ZeroPage);
      out.writeBoolean(option.commentPlus4StackArea);
      out.writeBoolean(option.commentPlus4_200Area);
      out.writeBoolean(option.commentPlus4_300Area);
      out.writeBoolean(option.commentPlus4_400Area);
      out.writeBoolean(option.commentPlus4_500Area);
      out.writeBoolean(option.commentPlus4_600Area);
      out.writeBoolean(option.commentPlus4_700Area);
      out.writeBoolean(option.commentPlus4ColorArea);
      out.writeBoolean(option.commentPlus4VideoArea);
      out.writeBoolean(option.commentPlus4BasicRamP);
      out.writeBoolean(option.commentPlus4BasicRamN);
      out.writeBoolean(option.commentPlus4Luminance);
      out.writeBoolean(option.commentPlus4ColorBitmap);
      out.writeBoolean(option.commentPlus4GraphicData);
      out.writeBoolean(option.commentPlus4BasicRom);
      out.writeBoolean(option.commentPlus4BasicExt);
      out.writeBoolean(option.commentPlus4Caracter);
      out.writeBoolean(option.commentPlus4Acia);
      out.writeBoolean(option.commentPlus4_6529B_1); 
      out.writeBoolean(option.commentPlus4_6529B_2);
      out.writeBoolean(option.commentPlus4Ted);
      out.writeBoolean(option.commentPlus4Kernal);
      
      out.writeBoolean(option.commentVic20ZeroPage);
      out.writeBoolean(option.commentVic20StackArea);
      out.writeBoolean(option.commentVic20_200Area);
      out.writeBoolean(option.commentVic20_300Area);
      out.writeBoolean(option.commentVic20_400Area);
      out.writeBoolean(option.commentVic20Vic);
      out.writeBoolean(option.commentVic20Via1);
      out.writeBoolean(option.commentVic20Via2);
      out.writeBoolean(option.commentVic20UserBasic);
      out.writeBoolean(option.commentVic20Screen);
      out.writeBoolean(option.commentVic20_8kExp1);
      out.writeBoolean(option.commentVic20_8kExp2);
      out.writeBoolean(option.commentVic20_8kExp3);
      out.writeBoolean(option.commentVic20Character);
      out.writeBoolean(option.commentVic20Color);
      out.writeBoolean(option.commentVic20Block2);
      out.writeBoolean(option.commentVic20Block3);
      out.writeBoolean(option.commentVic20Block4);
      out.writeBoolean(option.commentVic20BasicRom);
      out.writeBoolean(option.commentVic20KernalRom); 
      
      out.writeUTF(option.assembler.name());
      
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
      project.targetType=TargetType.valueOf(in.readUTF());  // version 1
      
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
        
        if (version>0) {
            project.memory[i].isGarbage=in.readBoolean();
            project.memory[i].dataType=DataType.valueOf(in.readUTF());
        } // version 1
        
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
      out.writeUTF(project.targetType.name());  // version 1
      
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
          out.writeBoolean(memory.isGarbage);    // version 1
          out.writeUTF(memory.dataType.name());  // version 1
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
