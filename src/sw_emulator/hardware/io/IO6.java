/**
 * @(#)IO6.java 1999/10/24
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

package sw_emulator.hardware.io;

/**
 * This class manage a 6 bits bidirectional I/O port.
 * For each port there's two methods for set or get the value of port:
 * <code>setPx</code> and <code>getPx</code>. These methods provide also a
 * system for having default ouput where a port is defined for input, but the
 * output line is also connected to pull-up or pull-down resistors. So a class
 * that use it, may define the variables <code>defaultPx</code> to be of value 0
 * or 1.
 * The <code>setPx_</code> and <code>getPx_</code> methods are to be used by the
 * chip that have the I/O port, unlike the other are to be used for external
 * chips that are connected to the ports.
 * Note that these methods are visible only for class in this package.
 *
 * Even if the port are bits oriented, this class is thinked for giving support
 * of packed bits: <code>portDir</code> and <code>portData</code> are provided
 * for memorize the bits in two registers.
 * Also the <code>remember</code> variables is used to remember that a output
 * port is changed, so connected chips may be advised.
 *
 * In more details, an I/O bit port is like this:
 * <pre>
 *                      Dir                      pull-up
 *                       |                          |
 *   Write Line ----- DataOut bit buffer -----      |
 *                    | ___                   |     |
 *                    | Dir                   |----------output/input line
 *                    |  |                    |
 *   Read  Line ----- DataIn bit buffer  -----
 *
 *
 *
 * </pre>
 * So, this table showes what is reading/writing from internal chip side (left
 * in draw) and from external chip side:
 *
 * <pre>
 *           Dir=output
 *  internal write x:           DataOut bit buffer=x
 *  external:                   read the x stored in DataOut bit buffer
 *
 *  external write y:           DataIn bit buffer=y (this is for remember)
 *  internal:                   read the (DataIn & DataOut) value
 *
 *           Dir=input
 *  external write y:           DataIn bit buffer=y
 *  internal:                   read the y stored in DataIn buffer
 *
 *  internal write x:           DataIn bit buffer=x
 *  external:                   read the pull-up value (not x)
 * </pre>
 * 
 * Note. A bit can be non connected, so in this case there is a fall back time
 * as internal capacitors store a 1 value that than goes to 0 in that time.
 *
 * @author Ice
 * @version 1.01 27/05/2000
 */
public class IO6 {
  /** I/O Port direction register: 0=input, 1=output */
  public int portDir;

  /** I/O Port data register for output */
  public int portDataOut;

  /* I/O Port data register for input */
  public int portDataIn=0xff;
  
  /** Clock fall back time for capacitors of open ports */
  public int fallBackTime=0;

  /* A 1 bit indicated that a output port have change it's value */
  public int remember=0;

  /** Default output value of port 0 if it is set to input */
  public int defaultP0=1;
  
  /** Default output value of port 1 if it is set to input */
  public int defaultP1=1;

  /** Default output value of port 2 if it is set to input */
  public int defaultP2=1;

  /** Default output value of port 3 if it is set to input */
  public int defaultP3=1;

  /** Default output value of port 4 if it is set to input */
  public int defaultP4=1;

  /** Default output value of port 5 if it is set to input */
  public int defaultP5=0;
  
  /** True if the bit of port 0 is open */
  public boolean isOpenP0=false;

  /** True if the bit of port 1 is open */
  public boolean isOpenP1=false;

  /** True if the bit of port 2 is open */
  public boolean isOpenP2=false;

  /** True if the bit of port 3 is open */
  public boolean isOpenP3=false;

  /** True if the bit of port 4 is open */
  public boolean isOpenP4=false;  
  
  /** True if the bit of port 5 is open */
  public boolean isOpenP5=false;  

  /**
   * Set the value of input port 0
   * The value is store in input buffer
   *
   * @param bit the bit value of input port
   */
  public void setP0(int bit) {
    if (bit==0) portDataIn&=~0x01;
    else portDataIn|=0x01;
  }

  /**
   * Set the value of input port 1
   * The value is store in input buffer
   *
   * @param bit the bit value of input port
   */
  public void setP1(int bit) {
    if (bit==0) portDataIn&=~0x02;
    else portDataIn|=0x02;
  }

  /**
   * Set the value of input port 2
   * The value is store in input buffer
   *
   * @param bit the bit value of input port
   */
  public void setP2(int bit) {
    if (bit==0) portDataIn&=~0x04;
    else portDataIn|=0x04;
  }

  /**
   * Set the value of input port 3
   * The value is store in input buffer
   *
   * @param bit the bit value of input port
   */
  public void setP3(int bit) {
    if (bit==0) portDataIn&=~0x08;
    else portDataIn|=0x08;
  }

  /**
   * Set the value of input port 4
   * The value is store in input buffer
   *
   * @param bit the bit value of input port
   */
  public void setP4(int bit) {
    if (bit==0) portDataIn&=~0x10;
    else portDataIn|=0x10;
  }

  /**
   * Set the value of input port 5
   * The value is store in input buffer
   *
   * @param bit the bit value of input port
   */
  public void setP5(int bit) {
    if (bit==0) portDataIn&=~0x20;
    else portDataIn|=0x20;
  }

  /**
   * Get the value of output port 0
   * If the port if not of output we return a default value
   *
   * @return the 0/1 bit value
   */
  public int getP0() {
    if ((portDir & 0x01)!=0)       // output port ?
      return (portDataOut & 0x01);
      else return defaultP0;
  }

  /**
   * Get the value of output port 1
   * If the port if not of output we return a default value
   *
   * @return the 0/1 bit value
   */
  public int getP1() {
    if ((portDir & 0x02)!=0)       // output port ?
      return (portDataOut & 0x02)>>1;
      else return defaultP1;
  }

  /**
   * Get the value of output port 2
   * If the port if not of output we return a default value
   *
   * @return the 0/1 bit value
   */
  public int getP2() {
    if ((portDir & 0x04)!=0)       // output port ?
      return (portDataOut & 0x04)>>2;
      else return defaultP2;
  }

  /**
   * Get the value of output port 3
   * If the port if not of output we return a default value
   *
   * @return the 0/1 bit value
   */
  public int getP3() {
    if ((portDir & 0x08)!=0)       // output port ?
      return (portDataOut & 0x08)>>3;
      else return defaultP3;
  }

  /**
   * Get the value of output port 4
   * If the port if not of output we return a default value
   *
   * @return the 0/1 bit value
   */
  public int getP4() {
    if ((portDir & 0x10)!=0)       // output port ?
      return (portDataOut & 0x10)>>4;
      else return defaultP4;
  }

  /**
   * Get the value of output port 5
   * If the port if not of output we return a default value
   *
   * @return the 0/1 bit value
   */
  public int getP5() {
    if ((portDir & 0x20)!=0)       // output port ?
      return (portDataOut & 0x20)>>5;
      else return defaultP5;
  }

  /**
   * Set the value of output port 0 (by internal operation)
   * If the value is different from the precedent, we remember this occurence.
   *
   * @param value the value to give to bit (it is just correctly shifted)
   */
  protected void setP0_(int value) {
    if ((portDir & 0x01)!=0) { // output port ?
      if (((portDataOut & 0x01)^value)!=0) remember|=0x01;
    }
    if (value!=0) portDataOut|=value;
    else portDataOut&=~0x01;
  }

  /**
   * Set the value of output port 1 (by internal operation)
   * If the value is different from the precedent, we remember this occurence.
   *
   * @param value the value to give to bit (it is just correctly shifted)
   */
  protected void setP1_(int value) {
    if ((portDir & 0x02)!=0) { // output port ?
      if (((portDataOut & 0x02)^value)!=0) remember|=0x02;
    }
    if (value!=0) portDataOut|=value;
    else portDataOut&=~0x02;
  }

  /**
   * Set the value of output port 2 (by internal operation)
   * If the value is different from the precedent, we remember this occurence.
   *
   * @param value the value to give to bit (it is just correctly shifted)
   */
  protected void setP2_(int value) {
    if ((portDir & 0x04)!=0) { // output port ?
      if (((portDataOut & 0x04)^value)!=0) remember|=0x04;
    }
    if (value!=0) portDataOut|=value;
    else portDataOut&=~0x04;
  }

  /**
   * Set the value of output port 3 (by internal operation)
   * If the value is different from the precedent, we remember this occurence.
   *
   * @param value the value to give to bit (it is just correctly shifted)
   */
  protected void setP3_(int value) {
    if ((portDir & 0x08)!=0) { // output port ?
      if (((portDataOut & 0x08)^value)!=0) remember|=0x08;
    }
    if (value!=0) portDataOut|=value;
    else portDataOut&=~0x08;
  }

  /**
   * Set the value of output port 4 (by internal operation)
   * If the value is different from the precedent, we remember this occurence.
   *
   * @param value the value to give to bit (it is just correctly shifted)
   */
  protected void setP4_(int value) {
    if ((portDir & 0x10)!=0) { // output port ?
      if (((portDataOut & 0x10)^value)!=0) remember|=0x10;
    }
    if (value!=0) portDataOut|=value;
    else portDataOut&=~0x10;
  }

  /**
   * Set the value of output port 5 (by internal operation)
   * If the value is different from the precedent, we remember this occurence.
   *
   * @param value the value to give to bit (it is just correctly shifted)
   */
  protected void setP5_(int value) {
    if ((portDir & 0x20)!=0) { // output port ?
      if (((portDataOut & 0x20)^value)!=0) remember|=0x20;
    }
    if (value!=0) portDataOut|=value;
    else portDataOut&=~0x20;
  }

  /**
   * Get the value of input port 0 (by internal operation)
   *
   * @return the 0/1 bit correctly shifted
   */
  protected int getP0_() {
    if ((portDir & 0x01)==0)   // port of input ?
      return (portDataIn & defaultP0 & 0x01);
    else return (portDataIn & portDataOut & 0x01);
  }

  /**
   * Get the value of input port 1 (by internal operation)
   *
   * @return the 0/1 bit correctly shifted
   */
  protected int getP1_() {
    if ((portDir & 0x02)==0)   // port of input ?
      return (portDataIn & (defaultP1<<1) & 0x02);
    else return (portDataIn & portDataOut & 0x02);
  }

  /**
   * Get the value of input port 2 (by internal operation)
   *
   * @return the 0/1 bit correctly shifted
   */
  protected int getP2_() {
    if ((portDir & 0x04)==0)   // port of input ?
      return (portDataIn & (defaultP2<<2) & 0x04);
    else return (portDataIn & portDataOut & 0x04);
  }

  /**
   * Get the value of input port 3 (by internal operation)
   *
   * @return the 0/1 bit correctly shifted
   */
  protected int getP3_() {   
    if ((portDir & 0x08)==0)   // port of input ?
      return (portDataIn & (defaultP3<<3) & 0x08);
    else return (portDataIn & portDataOut & 0x08);
  }

  /**
   * Get the value of input port 4 (by internal operation)
   *
   * @return the 0/1 bit correctly shifted
   */
  protected int getP4_() {
    if ((portDir & 0x10)==0)   // port of input ?
      return (portDataIn & (defaultP4<<4) & 0x10);
    else return (portDataIn & portDataOut & 0x10);
  }

  /**
   * Get the value of input port 5 (by internal operation)
   *
   * @return the 0/1 bit correctly shifted
   */
  protected int getP5_() {
    if ((portDir & 0x20)==0)   // port of input ?
      return (portDataIn & (defaultP5<<5) & 0x20);
    else return (portDataIn & portDataOut & 0x20);
  }
}
