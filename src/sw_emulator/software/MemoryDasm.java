/**
 * @(#)memoryState.java 2019/12/21
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
package sw_emulator.software;

import java.util.Objects;
import sw_emulator.swing.main.DataType;

/**
 * Memory cell for the disassembler
 * 
 * @author ice
 */
public class MemoryDasm implements Cloneable {
  /** Address of memory (0..$FFFF) */  
  public int address;  
  
  /** Memory comment from dasm disassemble */
  public String dasmComment;
  
  /** Memory comment from user (if not null it surclass the dasm one) */  
  public String userComment;
  
  /** Memory block comment from user */
  public String userBlockComment;
  
  /** Location defined by dasm disassemble */
  public String dasmLocation;
  
  /** Location defined by user */
  public String userLocation;
  
  /** True if the memory area is inside the read data to disassemble */
  public boolean isInside;
  
  /** True if this is code */
  public boolean isCode;
  
  /** True if this is data */
  public boolean isData;
  
  /** True if this is garbage */
  public boolean isGarbage;
  
  /** If inside it store the copy of value of memory */
  public byte copy;
  
  /** Constant index to use if applicable into the kind of data */
  public byte index=-1;
  
  /** Related address (#< or #>)*/
  public int related=-1;
  
  /** Type of relation for related < or > */
  public char type=' ';
  
  /** Type of data */
  public DataType dataType=DataType.NONE;

    /**
     * Test if a given object is equal to this
     * 
     * @param o the object to test
     * @return true if it is equal
     */
    @Override
  public boolean equals(Object o) {
    if (!(o instanceof MemoryDasm))  return false;
    MemoryDasm d=(MemoryDasm) o;
    
    if (this.address != d.address) return false;
    if (!Objects.equals(this.dasmComment, d.dasmComment)) return false;
    if (!Objects.equals(this.userComment, d.userComment)) return false;
    if (!Objects.equals(this.userBlockComment, d.userBlockComment)) return false;
    if (!Objects.equals(this.dasmLocation, d.dasmLocation)) return false;
    if (!Objects.equals(this.userLocation, d.userLocation)) return false;
    if (this.isInside != d.isInside) return false;
    if (this.isCode != d.isCode) return false;
    if (this.isData != d.isData) return false;
    if (this.isGarbage != d.isGarbage) return false;
    if (this.copy != d.copy) return false;
    if (this.related != d.related) return false;
    if (this.type != d.type) return false;
    if (this.dataType != d.dataType) return false;
    if (this.index != d.index) return false;
    
    return true;
  }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.address;
        hash = 53 * hash + Objects.hashCode(this.dasmComment);
        hash = 53 * hash + Objects.hashCode(this.userComment);
        hash = 53 * hash + Objects.hashCode(this.userBlockComment);
        hash = 53 * hash + Objects.hashCode(this.dasmLocation);
        hash = 53 * hash + Objects.hashCode(this.userLocation);
        hash = 53 * hash + (this.isInside ? 1 : 0);
        hash = 53 * hash + (this.isCode ? 1 : 0);
        hash = 53 * hash + (this.isData ? 1 : 0);
        hash = 53 * hash + (this.isGarbage ? 1 : 0);
        hash = 53 * hash + this.copy;
        hash = 53 * hash + this.related;
        hash = 53 * hash + this.type;
        hash = 53 * hash + this.index;
        hash = 53 * hash + Objects.hashCode(this.dataType);
        return hash;
    }

 @Override
  public MemoryDasm clone() {
    MemoryDasm m = new MemoryDasm();
    
    m.address=this.address;
    m.copy=this.copy;
    m.dasmComment=this.dasmComment;
    m.dasmLocation=this.dasmLocation;
    m.isCode=this.isCode;
    m.isData=this.isData;
    m.isGarbage=this.isGarbage;    
    m.isInside=this.isInside;
    m.related=this.related;
    m.type=this.type;
    m.userBlockComment=this.userBlockComment;
    m.userComment=this.userComment;
    m.userLocation=this.userLocation;
    m.dataType=this.dataType;
    m.index=this.index;
          
    return m;    
  }  
  
}
