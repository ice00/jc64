/**
 * @(#)FileCartridge.java 2000/06/30
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

package sw_emulator.software.cartridge;

import sw_emulator.hardware.cartridge.Cartridge;
import sw_emulator.hardware.cartridge.GameCartridge;
import sw_emulator.hardware.io.CartridgeIO;
import sw_emulator.hardware.bus.Bus;
import sw_emulator.util.Monitor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Determine the cartridge type from a file.
 * Actually only CCS cartridge file format is supported.
 *
 * @author Ice
 * @version 1.00 30/06/2000
 */
public class FileCartridge {
  // negative value means error
  public static final int E_FNF = -2;  // file not found
  public static final int E_SE  = -3;  // security exception
  public static final int E_FTB = -4;  // file too big
  public static final int E_IO  = -5;  // i/o error
  public static final int E_LBR = -6;  // less byte read
  public static final int E_ICS = -7;  // invalid cartridge signature
  public static final int E_NCV = -8;  // not supported cartridge version
  public static final int E_IHL = -9;  // invalid header lenght
  public static final int E_UCT = -10; // unknown cartridge type
  public static final int E_ANS = -11; // actually not supported
  public static final int E_ISD = -12; // invalid signal data
  public static final int E_CNF = -13; // chip not found
  public static final int E_CNE = -14; // chip not expected

  /**
   * CCS Cartridge signature: "C64 CARTRIDGE   "
   */
  public static final byte[] SIGNATURE={0x43, 0x36, 0x34, 0x20, 0x43, 0x41,
                                        0x52, 0x54, 0x52, 0x49, 0x44, 0x47,
                                        0x45, 0x20, 0x20, 0x20};

  /**
   * Name of the fine
   */
  protected String fileName;

  /**
   * The name of the cartridge
   */
  protected String cartName;

  /**
   * Image of the file
   */
  protected byte[] fileImage;

  /**
   * Roml data buffer
   */
  protected byte[] roml=new byte[8192];

  /**
   * Romh data buffer
   */
  protected byte[] romh=new byte[8192];

  /**
   * A monitor where synchronizer with clock
   */
  protected Monitor clock;

  /**
   * The external bus
   */
  protected Bus bus;

  /**
   * The cartridge IO
   */
  protected CartridgeIO io;

  /**
   * Construct a file cartridge
   *
   * @param io the cartridge expansion port io
   * @param clock a monitor where synchronized with a clock
   * @param bus the external bus
   */
  public FileCartridge(CartridgeIO io, Monitor clock, Bus bus) {
    this.io=io;
    this.clock=clock;
    this.bus=bus;
  }

  /**
   * Set the name of file to use for cartridge images
   *
   * @param fileName name of the file
   */
  public void setFileName(String fileName) {
    this.fileName=fileName;
  }

  /**
   * Read the cartridge image file
   *
   * @return the error code
   */
  public int readFile() {
    int result;           // result of reading
    FileInputStream file; // the file

    try {
      file=new FileInputStream(fileName);
    } catch (FileNotFoundException e) {
        return E_FNF;
      }
      catch (SecurityException e1) {
        return E_SE;
      }

    try {
      if (file.available()>32*1024) return E_FTB;

      fileImage=new byte[file.available()];

      result=file.read(fileImage);

      if (result!=fileImage.length) return E_LBR;
    } catch (IOException e) {
        return E_IO;
      }
    return 0;
  }

  /**
   * Determine the type of cartridge
   *
   * return the cartridge type
   */
  public int determineCart() {
    int i;            //cycle variables
    int exrom;        // exrom signal
    int game;         // game signal
    int offs;         // offset of chip image

    // look for cartridge signature
    for (i=0; i<16; i++)
      if (fileImage[i]!=SIGNATURE[i])
        return E_ICS;                        // invalid cartridge signature

    // look for cartridge version
    if (fileImage[0x14]*256+fileImage[0x15]!=0x100)
      return E_NCV;                          // not supported cartridge version

    // look for correct header lenght
    if ((fileImage[0x10]!=0)||
        (fileImage[0x11]!=0)||
        (fileImage[0x12]!=0)||
        (fileImage[0x13]!=0x40)) ///20 (?)
      return E_IHL;                          // invalid header length

    cartName="";                             // reset the name

    // get the cartridge name
    for (i=0x20; i<0x40; i++) {
      cartName+=(char)fileImage[i];
    }

    // get exrom and game signal
    exrom=fileImage[0x18];
    game=fileImage[0x19];

    // look for valid signals
    if (!((game==0 || game==1) &&
        (exrom==0 || exrom==1)))
      return E_ISD;                          // invalid signal data

    // determine cartridge type
    switch (fileImage[0x16]*256+fileImage[0x17]) {
      case 0x00:                             // normal cartridge

        offs=0x40;                           // offeset of chip image
        if (((char)fileImage[offs+0]!='C')||
            ((char)fileImage[offs+1]!='H')||
            ((char)fileImage[offs+2]!='I')||
            ((char)fileImage[offs+3]!='P'))
          return E_CNF;                      // chip not found

        switch (game<<1+exrom) {
          case 00:

            //look for ROM chip
            if (fileImage[offs+8]*256+fileImage[offs+9]!=0)
              return E_CNE;                  // RAM chip not expected

            //... not yet implemented
            // now we suppose 16Kb expected!!!

            // copy first ROM (8000h)
            for (i=0; i<8192; i++) {
              roml[i]=fileImage[i+offs+0x10];
            }

            // copy second ROM (A000h)
            for (i=0; i<8192; i++) {
              romh[i]=fileImage[i+offs+0x10+8192];
            }

            break;
        }



        break;
      case 0x01:                           // action replay
        return E_ANS;                      // actually not supported

      case 0x02:                           // KCS power cartridge
        return E_ANS;                      // actually not supported

      default:
        return E_UCT;                      // unknown cartridge type
    }
    return 0;
  }

  /**
   * Get the cartridge correctly initilized
   *
   * @param type the type of cartridge to return
   */
  public Cartridge getCartridge(int type) {
    type=0; // debug

    switch (type) {
      case 00:

        return new GameCartridge(io, clock, bus, roml, romh);
      default:
        return new Cartridge(io, clock, bus);
    }
  }
}