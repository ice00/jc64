/**
 * @(#)FileTypejava 2019/12/08
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

import sw_emulator.math.Unsigned;

  /** File type */  
  public enum FileType {
    UND {
        @Override
        public String getDescription(byte[] inB) {
          return "Unrecognized file";
        }
    },  // undefined  
    SID {
        @Override
        public String getDescription(byte[] inB) {
          int psidVersion;  // version of psid file
          int psidLAddr;    // psid load address
          int psidIAddr;    // psid init address
          int psidPAddr;    // psid play address
          int psidSong;     // number of songs
          int psidSSong;    // start song
    
          StringBuilder tmp=new StringBuilder();
          char first;
          if (inB[0]==0x52) first='R';
          else first='P';
          
          psidVersion=(int)inB[5];
          psidLAddr=Unsigned.done(inB[9])+Unsigned.done(inB[8])*256;
          psidIAddr=Unsigned.done(inB[11])+Unsigned.done(inB[10])*256;
          psidPAddr=Unsigned.done(inB[13])+Unsigned.done(inB[12])*256;
          psidSong=Unsigned.done(inB[15])+Unsigned.done(inB[14])*256;
          psidSSong=Unsigned.done(inB[17])+Unsigned.done(inB[16])*256;
                    
          tmp.append(first).append("SID file version ").append(psidVersion).append("\n");
          tmp.append("Load Address: ").append(Integer.toHexString(psidLAddr)).append("\n");
          tmp.append("Init Address: ").append(Integer.toHexString(psidIAddr)).append("\n");
          tmp.append("Play Address: ").append(Integer.toHexString(psidPAddr)).append("\n");
          
          tmp.append("name:      ");
          for (int i=0x16; i<0x36; i++) {
            if (inB[i]==0) break;
            tmp.append((char)inB[i]);
          }
          tmp.append("\n");
          
          tmp.append("author:    ");
          for (int i=0x36; i<0x56; i++) {
            if (inB[i]==0) break;
            tmp.append((char)inB[i]);
          }
          tmp.append("\n");
          
          tmp.append("copyright: ");
          for (int i=0x56; i<0x76; i++) {
            if (inB[i]==0) break;
            tmp.append((char)inB[i]);
          }
          tmp.append("\n");
         
          tmp.append("songs: ").append(psidSong).append(" (startsong: ")
             .append(psidSSong).append(")\n\n");
          return tmp.toString();
        }
    },  // PSID/RSID
    MUS {
        @Override
        public String getDescription(byte[] inB) {
          int v1Length;         // length of voice 1 data
          int v2Length;         // length of voice 2 data
          int v3Length;         // length of voice 3 data
            
          StringBuffer tmp=new StringBuffer("");
          
          v1Length=Unsigned.done(inB[2])+Unsigned.done(inB[3])*256;
          v2Length=Unsigned.done(inB[4])+Unsigned.done(inB[5])*256;
          v3Length=Unsigned.done(inB[6])+Unsigned.done(inB[7])*256;
          int pos=v1Length+v2Length+v3Length+8;

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
            } while ( !((c==0x0D) || (c==0x00) || (pos>inB.length)) );
            tmp.append('\n');
            if (c==0x00) break; // must exit of the lines are incomplete
          }

          tmp.append('\n');          
          return tmp.toString();
        }
    },  // MUS
    PRG {
        @Override
        public String getDescription(byte[] inB) {
          int start=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
          return "Starting address="+Integer.toHexString(start)+"\n";
        }
    };  // program
    
    /**
     * Get the description from the given data
     * 
     * @param inB the data (of the given type)
     * @return the description
     */
    public abstract String getDescription(byte[] inB);
    
    
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
    
    /**
     * Get the file type
     * 
     * @param inB the input data
     * @return the file type
     */
    public static FileType getFileType(byte[] inB) {
      if (isPSID(inB)) return SID;
      if (isMUS(inB)) return MUS;
      if (isPRG(inB)) return PRG;
      
      return UND;
    }
    
   /**
    * Determine if the input file is a PSID or RSID file
    *
    * @return true if the file is a PSID or RSID file
    */    
    private static boolean isPSID(byte[] inB) {
      int psidVersion;  // version of psid file
      int psidDOff;     // psid data offeset

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
      return true;
    }
    
  /**
   * Determine if the input file is a MUS/STR file
   *
   * @param inB the data
   * @return true if the file is a MUS file
   */
  private static boolean isMUS(byte[] inB) {
    int v1Length;         // length of voice 1 data
    int v2Length;         // length of voice 2 data
    int v3Length;         // length of voice 3 data

    v1Length=Unsigned.done(inB[2])+Unsigned.done(inB[3])*256;
    v2Length=Unsigned.done(inB[4])+Unsigned.done(inB[5])*256;
    v3Length=Unsigned.done(inB[6])+Unsigned.done(inB[7])*256;
    
    if (inB.length<v1Length+v2Length+v3Length) return false;

    // calculate pointer to voice data
    int ind1=8;
    int ind2=ind1+v1Length;
    int ind3=ind2+v2Length;

    return (inB[ind2-2]==0x01 &&
        inB[ind2-1]==0x4F &&
        inB[ind3-2]==0x01 &&
        inB[ind3-1]==0x4F &&
        inB[ind3+v3Length-2]==0x01 &&
        inB[ind3+v3Length-1]==0x4F);
    }

   /**
    * Determine if the input file is a PRG file
    * 
    * @param inB the data
    * @return true if the file is a PRG file
    */
   private static boolean isPRG(byte[] inB) {
     int start=Unsigned.done(inB[0])+Unsigned.done(inB[1])*256;
     return (inB.length<=65535+3-start); 
   }
   
  }  
