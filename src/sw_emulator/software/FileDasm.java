/**
 * @(#)FileDasm.java 2001/05/05
 *
 * ICE Team Free Software Group
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

package sw_emulator.software;

import sw_emulator.software.memory.MemoryFlags;
import sw_emulator.software.machine.C64SidDasm;
import sw_emulator.software.machine.C64MusDasm;
import sw_emulator.software.machine.C64Dasm;
import sw_emulator.math.Unsigned;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Disassemble a given file in raw format (.prg) or in psid (.sid) format.
 * The action performed is simple: all the data is interpreted as code,
 * using one passed to the data. This make the result a simple way to figure
 * how the code is formed.
 * From this version, it is used the SIDLN memory state for better 
 * disassembling of prg/sid files.
 *
 * Some of the code is taken from libsidplay source
 *
 * @author Ice
 * @version 1.02 14/10/2003
 */
public class FileDasm {
  /** Complete name of the input file */
  public String inN;

  /** Complete name of the output file */
  public String outN;

  /** The input file stream */
  public BufferedInputStream inF;

  /** The output file stream */
  public BufferedOutputStream outF;
  
  /** The names of SIDLN output files*/
  public String[] names;

  /** The language to use */
  public byte lang=C64Dasm.LANG_ENGLISH;

  /** Contains the data of input file */
  public byte[] inB=new byte[0x20000];

  /** The output of disassembler */
  public String outB;

  /** Position in buffer of start of sid program */
  public int sidPos;

  /** PC value of start of sid program */
  public int sidPC;

  /** PC value of start of mus program */
  public int musPC;

  /** Mus file voice 1 address */
  public int ind1;

  /** Mus file voice 2 address */
  public int ind2;

  /** Mus file voice 3 address */
  public int ind3;

  /** Mus file txt address */
  public int txtA;
  
  /** The state of the memory */
  public MemoryFlags memory;

  /** CHR$ conversion table (0x01 = no output) */
  public static char CHRtab[] = {
    0x0, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0xd, 0x1, 0x1,
    0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1,
    0x20,0x21, 0x1,0x23,0x24,0x25,0x26,0x27,0x28,0x29,0x2a,0x2b,0x2c,0x2d,0x2e,0x2f,
    0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x3a,0x3b,0x3c,0x3d,0x3e,0x3f,
    0x40,0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x4a,0x4b,0x4c,0x4d,0x4e,0x4f,
    0x50,0x51,0x52,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5a,0x5b,0x24,0x5d,0x20,0x20,
    // alternative: CHR$(92=0x5c) => ISO Latin-1(0xa3)
    0x2d,0x23,0x7c,0x2d,0x2d,0x2d,0x2d,0x7c,0x7c,0x5c,0x5c,0x2f,0x5c,0x5c,0x2f,0x2f,
    0x5c,0x23,0x5f,0x23,0x7c,0x2f,0x58,0x4f,0x23,0x7c,0x23,0x2b,0x7c,0x7c,0x26,0x5c,
    // 0x80-0xFF
    0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1,
    0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1, 0x1,
    0x20,0x7c,0x23,0x2d,0x2d,0x7c,0x23,0x7c,0x23,0x2f,0x7c,0x7c,0x2f,0x5c,0x5c,0x2d,
    0x2f,0x2d,0x2d,0x7c,0x7c,0x7c,0x7c,0x2d,0x2d,0x2d,0x2f,0x5c,0x5c,0x2f,0x2f,0x23,
    0x2d,0x23,0x7c,0x2d,0x2d,0x2d,0x2d,0x7c,0x7c,0x5c,0x5c,0x2f,0x5c,0x5c,0x2f,0x2f,
    0x5c,0x23,0x5f,0x23,0x7c,0x2f,0x58,0x4f,0x23,0x7c,0x23,0x2b,0x7c,0x7c,0x26,0x5c,
    0x20,0x7c,0x23,0x2d,0x2d,0x7c,0x23,0x7c,0x23,0x2f,0x7c,0x7c,0x2f,0x5c,0x5c,0x2d,
    0x2f,0x2d,0x2d,0x7c,0x7c,0x7c,0x7c,0x2d,0x2d,0x2d,0x2f,0x5c,0x5c,0x2f,0x2f,0x23
  };

  public static void main(String[] args) {
    FileDasm fileDasm= new FileDasm(args);
  }

  /**
   * Construct the file disassembler with the passed parameters
   */
  public FileDasm(String[] args) {
    int result;  // result of file reading

    // look for good parameters
    if (!lookParameter(args)) {
      showHelp();
      return;
    }
    
    // create the memory state of this program
    memory=new MemoryFlags(names);

    // see if the file is present
    try {
      inF=new BufferedInputStream(new FileInputStream(inN));
    } catch (FileNotFoundException e) {
        System.out.println("Input file not found: abort"+e);
        return;
      }
      catch (SecurityException e1) {
        System.out.println("Security exception in open the input file: abort\n");
        return;
      }

    // read the file
    System.out.println("Read the input file...");
    try {
      result=inF.read(inB);
      inF.close();
    } catch (IOException e) {
        return;
      }

    // skip little file
    if (result<0x80) {
      System.out.println("The file is little: skip disassembling");
      return;
    }

    // see if the file can be created
    try {
      outF=new BufferedOutputStream(new FileOutputStream(outN));
    } catch (FileNotFoundException e) {
        System.out.println("Output file can not be created: abort: "+e);
        return;
      }
      catch (SecurityException e1) {
        System.out.println("Securety exception in creating the output file: abort\n");
        return;
      }

    // execute the disassembling of the file
    System.out.println("Disassemble the input file ...");
    if (isPSID()) {
      C64SidDasm sid=new C64SidDasm();
      sid.language=lang;
      outB=sid.cdasm(inB, sidPos, result, sidPC, memory);
    } else
       if (isMUS(result)) {
         C64MusDasm mus=new C64MusDasm();

         outB="VOICE 1 MUSIC DATA: \n\n";
         outB+=mus.cdasm(inB, ind1, ind2-1, ind1+musPC, null);

         outB+="\nVOICE 2 MUSIC DATA: \n\n";
         outB+=mus.cdasm(inB, ind2, ind3-1, ind2+musPC, null);

         outB+="\nVOICE 3 MUSIC DATA: \n\n";
         outB+=mus.cdasm(inB, ind3, txtA-1, ind3+musPC, null);
       } else {
           C64Dasm prg=new C64Dasm();
           prg.language=lang;
           int start=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;

           outB=prg.cdasm(inB, 2, result, start, memory);
         }

    // write output to the file
    System.out.println("Save the output file ...");
    try {
      for (int i=0; i<outB.length(); i++)
         outF.write((int)outB.charAt(i));         
      outF.flush();          
    } catch (IOException e) {
        System.out.println("Error writing the output file of " + outB.length()+ " bytes.");
      } 
  }

  /**
   * Show the help about program syntax
   */
  public void showHelp() {
    System.out.println("JC64Dis: dissassembler for pgr/sid/mus files (beta release 1)");
    System.out.println("by Ice Team Free Software Group 1999-2003\n");
    System.out.println("Usage: jc64dis [-xx] in_file out_file [RAWx.BIN ...]");
    System.out.println("where -xx is for the language to use: en (english)/it (italian)");
    System.out.println("      in_file is the input file");
    System.out.println("      out_file is the output file");
    System.out.println("      RAWx.bin are the SIDLN memory states file");
  }

  /**
   * Look for the passed paramether
   *
   * @param args the passed paramether
   * @return true if parameter are good formed
   */
  public boolean lookParameter(String[] args) {
    names=null;
  
    // are the parameters in the rigth number?
    if (args.length<2) {
      return false;
    }

    int pos=0;     // the position of the input file

    if (args[0].equals("-en")) {
      lang=C64Dasm.LANG_ENGLISH;
      pos=1;
    } else {
        if (args[0].equals("-it")) {
          lang=C64Dasm.LANG_ITALIAN;
          pos=1;
        } 
      }

    inN=new String(args[pos]);
    outN=new String(args[++pos]);
    
    if ((args.length-pos)==0) return true;
    
    // copy the names
    names=new String[args.length-pos-1];
    for (int i=++pos, j=0; i<args.length; i++, j++) {
      names[j]=args[i];
    }

    return true;
  }

  /**
   * Determine if the input file is a PSID or RSID file
   *
   * @return true if the file is a PSID or RSID file
   */
  public boolean isPSID() {
    int psidVersion;  // version of psid file
    int psidDOff;     // psid data offeset
    int psidLAddr;    // psid load address
    int psidIAddr;    // psid init address
    int psidPAddr;    // psid play address
    int psidSong;     // number of songs
    int psidSSong;    // start song

    // check header
    if (((inB[0]!='P') && (inB[0]!='R'))
        ||(inB[1]!='S')||(inB[2]!='I')||(inB[3]!='D')) return false;

    // check PSID version
    if ((inB[4]!='\0')|| (inB[5]!='\1' && inB[5]!='\2')) return false;
    psidVersion=(int)inB[5];

    // check PSID data offset
    if ((inB[6]!='\0')|| (inB[7]!=0x76 && inB[7]!=0x7C)) return false;
    psidDOff=(int)inB[7];
    if (psidVersion==2 && psidDOff==0x76) return false;
    if (psidVersion==1 && psidDOff==0x7C) return false;

    psidLAddr=Unsigned.done(inB[9])+Unsigned.done(inB[8])*256;
    psidIAddr=Unsigned.done(inB[11])+Unsigned.done(inB[10])*256;
    psidPAddr=Unsigned.done(inB[13])+Unsigned.done(inB[12])*256;
    psidSong=Unsigned.done(inB[15])+Unsigned.done(inB[14])*256;
    psidSSong=Unsigned.done(inB[17])+Unsigned.done(inB[16])*256;

    //write these data as header of output file
    try {
      StringBuffer tmp=new StringBuffer("");
      
      char first;
      if (inB[0]==0x52) first='R';
      else first='P';

      outF.write(new String(first+"SID file version "+psidVersion+"\n").getBytes());
      outF.write(new String("Load Address: "+Integer.toHexString(psidLAddr)+"\n").getBytes());
      outF.write(new String("Init Address: "+Integer.toHexString(psidIAddr)+"\n").getBytes());
      outF.write(new String("Play Address: "+Integer.toHexString(psidPAddr)+"\n").getBytes());
      for (int i=0x16; i<0x36; i++) {
        if (inB[i]==0) break;
        tmp.append((char)inB[i]);
      }
      outF.write(new String("name:      "+tmp+"\n").getBytes());
      tmp=new StringBuffer("");
      for (int i=0x36; i<0x56; i++) {
        if (inB[i]==0) break;
        tmp.append((char)inB[i]);
      }
      outF.write(new String("author:    "+tmp+"\n").getBytes());
      tmp=new StringBuffer("");
      for (int i=0x56; i<0x76; i++) {
        if (inB[i]==0) break;
        tmp.append((char)inB[i]);
      }
      outF.write(new String("copyright: "+tmp+"\n").getBytes());
      outF.write(new String("songs: "+psidSong+" (startsong: "+psidSSong+")\n\n").getBytes());
    } catch (IOException e) {
        System.out.println("Error writing output file: "+e);
      }

    //calculate address for disassembler
    if (psidLAddr==0) {
      sidPC=Unsigned.done(inB[psidDOff])+Unsigned.done(inB[psidDOff+1])*256;
      sidPos=psidDOff+2;
    } else {
        sidPos=psidDOff;
        sidPC=psidLAddr;
      }
    return true;
  }

  /**
   * Determine if the input file is a MUS/STR file
   *
   * @param length the length of the file
   * @return true if the file is a MUS file
   */
  public boolean isMUS(int length) {
    int v1Length;         // length of voice 1 data
    int v2Length;         // length of voice 2 data
    int v3Length;         // length of voice 3 data

    musPC=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
    v1Length=Unsigned.done(inB[2])+Unsigned.done(inB[3])*256;
    v2Length=Unsigned.done(inB[4])+Unsigned.done(inB[5])*256;
    v3Length=Unsigned.done(inB[6])+Unsigned.done(inB[7])*256;

    // calculate pointer to voice data
    ind1=8;
    ind2=ind1+v1Length;
    ind3=ind2+v2Length;

    if (inB[ind2-2]==0x01 &&
        inB[ind2-1]==0x4F &&
        inB[ind3-2]==0x01 &&
        inB[ind3-1]==0x4F &&
        inB[ind3+v3Length-2]==0x01 &&
        inB[ind3+v3Length-1]==0x4F) {
      // write songs information
      try {
        StringBuffer tmp=new StringBuffer("");
        int pos=v1Length+v2Length+v3Length+8;

        txtA=pos;

        for (int line=0; line<5; line++ ) {
          char c;
          char si=0;                       // count copied characters
          do {
            // ASCII CHR$ conversion
            c=CHRtab[Unsigned.done(inB[pos])];

            if ((c>=0x20) && (si<=31)) {
              tmp.append(c);                // copy to info string
            }

            // If character is 0x9d (left arrow key) then move back.
            if ((inB[pos]==0x9d) && (si>=0)) {
              si--;
            }
            pos++;
          } while ( !((c==0x0D) || (c==0x00) || (pos>length)) );
          tmp.append('\n');
        }

        tmp.append('\n');
        outF.write(new String(tmp+"\n").getBytes());
      } catch (IOException e) {
          System.out.println("Error writing output file: "+e);
        }
      return true;
    } else return false;
  }
}
