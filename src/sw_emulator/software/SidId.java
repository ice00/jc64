/*
 * @(#)SidId.java 2023/06/12
 *
 * ICE Team free software group
 *
 * This file is part of JIIT64 Java Ice Team Tracker 64
 * See README for copyright notice.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 */
package sw_emulator.software;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Store the information about a SID Id signature (patterns)
 */
class SidIdRecord {
  String name;
  ArrayList<int[]> list;
}

/**
 * SidId from XSidplay2
 * 
 * @author ice00
 */
public class SidId {
  static final int END = -1;
  static final int ANY = -2;
  static final int AND = -3;
  static final int NAME = -4;  
  static final int MAX_SIGSIZE = 4096;
  
  /** Instance of the class as singleton */
  public static final SidId instance=new SidId();
  
  /** Version of the algorithm */
  public static final String VERSION = "SIDId V1.09 by Cadaver (C) 2012";
  
  /** list of Sid IDs */
  ArrayList<SidIdRecord> sidIdList=new ArrayList();
    
  /** Last players identified */
  String lastPlayers;  
  
  /**
   * Private constructor
   */
  private SidId() {      
  }
  
  /**
   * Get the number of players recognized
   *
   * @return the number of players recognized
   */ 
  public int getNumberOfPlayers() {
    return sidIdList.size();
  }

  /**
   * Get the number of patterns used
   *
   * @return the number of patterns used
   */
  public int getNumberOfPatterns() {
    int n = 0;

    for (SidIdRecord rec : sidIdList) {
      n += rec.list.size();
    }
    return n;
  }
  
  /**
   * Read the patterns id file
   *
   * @param name the name (with path) of the file to read
   * @return true if read is ok
   */
  public boolean readConfig(String name) throws Exception {
    //String tokenstr;
    String[] tokens;
    String line;
    int[] temp;
    int sigsize = 0;

    BufferedReader in;
    try {
      in = new BufferedReader(new FileReader(name));
      sidIdList.clear();    // clear the list as we can read a config more time

      while (in.ready()) {
        int len;
        
        temp=new int[MAX_SIGSIZE];
        line = in.readLine();
        tokens = line.split(" ");
        for (String tokenstr : tokens) {
          len = tokenstr.length();

          if (len > 0) {
            int token = NAME;  // suppose this is a NAME declaration
            switch (tokenstr) {
              case "??":
                token = ANY;
                break;
              case "end":  
              case "END":
                token = END;
                break;
              case "and":  
              case "AND":
                token = AND;
                break;
            }
            if ((len == 2) && (isHex(tokenstr.charAt(0)) && (isHex(tokenstr.charAt(1))))) {
              token = getHex(tokenstr.charAt(0)) * 16 + getHex(tokenstr.charAt(1));
            }

            switch (token) {
              // name declaration
              case NAME: 
                SidIdRecord newid = new SidIdRecord();
                newid.list=new ArrayList();
                newid.name = tokenstr;///strdup(tokenstr);

                sidIdList.add(newid);
                sigsize = 0;
                break;

              case END:
                if (sigsize >= MAX_SIGSIZE) {
                  throw new Exception("Maximum signature size exceeded!\n");
                }
                temp[sigsize++] = END;
                if (sigsize > 1) {
                  int c;

                  int[] newbytes = new int[sigsize];
                  if (newbytes==null) {
                    throw new Exception("Out of memory!\n");
                  }

                  for (c = 0; c < sigsize; c++) {
                    newbytes[c] = temp[c];
                  }

                  if (sidIdList.isEmpty()) {
                    throw new Exception("No playername defined before signature!\n");
                  }

                  sidIdList.get(sidIdList.size()-1).list.add(newbytes);
                }
                sigsize = 0;
                break;

              default:
                if (sigsize >= MAX_SIGSIZE) {
                  throw new Exception("Maximum signature size exceeded!\n");
                }
                temp[sigsize++] = token;
                break;
            }
          } else {
              break;
            }
        }
      }
      in.close();
    } catch (Exception e) { 
        System.err.println(e);
        return false;
      }

    return true;
  }
  
  /**
   * Identify the IDs of the given buffer
   * 
   * @param buffer the buffed with the data to identify
   * @param length length of the buffer
   * @return the identified engines as string
   */
  public String identifyBuffer(int[] buffer, int length) {
    lastPlayers="";
    // scan all the engines
    for (SidIdRecord list : sidIdList) {
      for (int[] bytes: list.list) {
        if (identifyBytes(bytes, buffer, length)) {
          lastPlayers+=list.name+" ";
          break;
        }
      }
    }
  
    return lastPlayers;
  }  
  
  /**
   * Identify the bytes of ID and buffer according to the pattern rules
   * 
   * @param bytes the ID bytes pattern
   * @param buffer the SID file buffer
   * @param length length of the buffer
   * @return true if ID is matching 
   */
  public boolean identifyBytes(int[] bytes, int[] buffer, int length) {
    int c = 0, d = 0, rc = 0, rd = 0;

    while (c < length) {
      if (d == rd) {
        if (buffer[c] == bytes[d]) {
          rc = c+1;
          d++;
        }
        c++;
      } else {
          if (bytes[d] == END) return true;
          if (bytes[d] == AND) {
            d++;
            while (c < length) {
              if (buffer[c] == bytes[d]) {
                rc = c+1;
                rd = d;
                break;
              }
              c++;
            }
            if (c >= length) return false;
          }
          if ((bytes[d] != ANY) && (buffer[c] != bytes[d])) {
            c = rc;
            d = rd;
          } else {
              c++;
              d++;
            }
        }
    }
    if (bytes[d] == END) return true;
    return false;
  }
  
  /**
   * Determine if the passed char is an exe number
   * 
   * @param c the char to test
   * @return true if the char is an hex number
   */
  private boolean isHex(char c) {
    if ((c >= '0') && (c <= '9')) return true;
    if ((c >= 'a') && (c <= 'f')) return true;
    if ((c >= 'A') && (c <= 'F')) return true;
    return false;
  }

  /**
   * Get the decimal number of the passed exe number char
   * 
   * @param c the char to use
   * @return the decimal number of the hex char passed
   */
  private int getHex(char c) {
    if ((c >= '0') && (c <= '9')) return c - '0';
    if ((c >= 'a') && (c <= 'f')) return c - 'a' + 10;
    if ((c >= 'A') && (c <= 'F')) return c - 'A' + 10;
    return -1;
  }  
}
