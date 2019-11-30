/**
 * @(#)UserEvent.java 2000/05/02
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

package sw_emulator.software.device;

import sw_emulator.hardware.device.Keyboard;
import sw_emulator.hardware.io.Joystick;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


public class UserEvent 
       implements KeyListener
{
 /**
  * A bridge between Java & JC64 IO:
  * This class converts an user input to a signal for the emulator.
  *
  * @author Michele Caira caira_mik@hotmail.com
  * @version 1.0
  */


 /**
  * A reference to a C64 keyboard.
  */
 protected Keyboard keyboard;

 /**
  * Java still doesn't allow to controll PC-joysticks,
  * so we must emulate it by the keyboard. 
  */
 protected Joystick joystick1;
 protected Joystick joystick2;

 
 private int JOY1_UP;
 private int JOY1_DOWN;
 private int JOY1_LEFT;
 private int JOY1_RIGHT;
 private int JOY1_FIRE;

 private int JOY2_UP;
 private int JOY2_DOWN;
 private int JOY2_LEFT;
 private int JOY2_RIGHT;
 private int JOY2_FIRE;


 /**
  * @param keyboard The C64 keyboard we wanna emulate.
  */
 public UserEvent(Keyboard keyboard)
 {
  this.keyboard=keyboard;
  }


 /**
  * Connect joystick1 to the keyboard.
  *
  * @param joystick1 The reference to the C64 joystick in port 1.
  */
 public final void connectJoystick1(Joystick joystick1)
 {
  this.joystick1=joystick1;
  setJoystick1DefaultKeySet();
  }

  /**
  * Connect joystick2 to the keyboard.
  *
  * @param joystick2 The reference to the C64 joystick in port 2.
  */
  public final void connectJoystick2(Joystick joystick2)
  {
   this.joystick2=joystick2;
   setJoystick2DefaultKeySet();
  }

 /** 
  * Set a keyset for emulating joystick1. 
  *
  * @param keyset1 The user keyset.
  */
 public void setJoystick1KeySet(int[] keyset1)
 {
  JOY1_UP   = keyset1[0];
  JOY1_DOWN = keyset1[1];
  JOY1_LEFT = keyset1[2];
  JOY1_RIGHT= keyset1[3];
  JOY1_FIRE = keyset1[4];
  }

 /** 
  * Set a keyset for emulating joystick2. 
  *
  * @param keyset2 The user keyset.
  */
 public final void setJoystick2KeySet(int[] keyset2)
 {
  JOY2_UP   = keyset2[0];
  JOY2_DOWN = keyset2[1];
  JOY2_LEFT = keyset2[2];
  JOY2_RIGHT= keyset2[3];
  JOY2_FIRE = keyset2[4];
  }

 /** 
  * Set a standard keyset for emulating joystick1. 
  */
 public final void setJoystick1DefaultKeySet()
 {
  JOY1_UP   = KeyEvent.VK_NUMPAD8;
  JOY1_DOWN = KeyEvent.VK_NUMPAD2;
  JOY1_LEFT = KeyEvent.VK_NUMPAD4;
  JOY1_RIGHT= KeyEvent.VK_NUMPAD6;
  JOY1_FIRE = KeyEvent.VK_SHIFT;
  }

 /** 
  * Set a standard keyset for emulating joystick2. 
  */
 public final void setJoystick2DefaultKeySet()
 {
  JOY2_UP   = KeyEvent.VK_W;
  JOY2_DOWN = KeyEvent.VK_S;
  JOY2_LEFT = KeyEvent.VK_A;
  JOY2_RIGHT= KeyEvent.VK_D;
  JOY2_FIRE = KeyEvent.VK_F;
  }

 public void keyPressed(KeyEvent e)
 {
  int keycode=e.getKeyCode();
  switch(keycode)
        {
        case KeyEvent.VK_A:
              keyboard.pressKey(1,2);
              break;
         case KeyEvent.VK_B:
              keyboard.pressKey(0,0); //
              break;
         case KeyEvent.VK_C:
              keyboard.pressKey(2,4); 
              break;
         case KeyEvent.VK_D:
              keyboard.pressKey(2,2);
              break;
         case KeyEvent.VK_E:
              keyboard.pressKey(1,6);
              break;
         case KeyEvent.VK_F:
              keyboard.pressKey(2,5);
              break;
         case KeyEvent.VK_G:
              keyboard.pressKey(3,2); 
              break;
         case KeyEvent.VK_H:
              keyboard.pressKey(3,5);
              break;
         case KeyEvent.VK_I:
              keyboard.pressKey(4,1);
              break;
         case KeyEvent.VK_J:
              keyboard.pressKey(4,2);
              break;
         case KeyEvent.VK_K:
              keyboard.pressKey(4,5);
              break;
         case KeyEvent.VK_L:
              keyboard.pressKey(5,2);
              break;
         case KeyEvent.VK_M:
              keyboard.pressKey(4,4);
              break;
         case KeyEvent.VK_N:
              keyboard.pressKey(4,7);
              break;
         case KeyEvent.VK_O:
              keyboard.pressKey(1,6);
              break;
         case KeyEvent.VK_P:
              keyboard.pressKey(5,1); 
              break;
         case KeyEvent.VK_Q:
              keyboard.pressKey(0,0); //
              break;
         case KeyEvent.VK_R:
              keyboard.pressKey(2,1);
              break;
         case KeyEvent.VK_S:
              keyboard.pressKey(1,5);
              break;
         case KeyEvent.VK_T:
              keyboard.pressKey(2,6);
              break;
         case KeyEvent.VK_U:
              keyboard.pressKey(3,6);
              break;
         case KeyEvent.VK_V:
              keyboard.pressKey(3,7);
              break;
         case KeyEvent.VK_W:
              keyboard.pressKey(1,1);
              break;
         case KeyEvent.VK_X:
              keyboard.pressKey(2,7);
              break;
         case KeyEvent.VK_Y:
              keyboard.pressKey(3,1);
              break;
         case KeyEvent.VK_Z:
              keyboard.pressKey(1,4);
              break;

       
         case KeyEvent.VK_0:
              keyboard.pressKey(4,3);
              break;
         case KeyEvent.VK_1:
              keyboard.pressKey(0,0);
              break;
         case KeyEvent.VK_2:
              keyboard.pressKey(0,3);
              break;
         case KeyEvent.VK_3:
              keyboard.pressKey(1,0); 
              break;
         case KeyEvent.VK_4:
              keyboard.pressKey(1,3);
              break;
         case KeyEvent.VK_5:
              keyboard.pressKey(2,0);
              break;
         case KeyEvent.VK_6:
              keyboard.pressKey(2,3);
              break;
         case KeyEvent.VK_7:
              keyboard.pressKey(3,0); 
              break;
         case KeyEvent.VK_8:
              keyboard.pressKey(3,3);
              break;
         case KeyEvent.VK_9:
              keyboard.pressKey(4,0);
              break;

         case KeyEvent.VK_F1:
         case KeyEvent.VK_F2:
              keyboard.pressKey(7,4);
              break;
         case KeyEvent.VK_F3:
         case KeyEvent.VK_F4:
              keyboard.pressKey(7,5);
              break;
         case KeyEvent.VK_F5:
         case KeyEvent.VK_F6:
              keyboard.pressKey(7,6); 
              break;
         case KeyEvent.VK_F7:
         case KeyEvent.VK_F8:     
              keyboard.pressKey(7,3);
              break;

         case KeyEvent.VK_EQUALS:
              keyboard.pressKey(6,5);
              break;
         case KeyEvent.VK_ADD:
              keyboard.pressKey(5,0);
              break;
       //  case KeyEvent.VK_SEPARATOR:
         case KeyEvent.VK_SUBTRACT:
              keyboard.pressKey(5,7);
              break;
         case KeyEvent.VK_DIVIDE:
         case KeyEvent.VK_SLASH:
              break;
         case KeyEvent.VK_MULTIPLY:
     //    case KeyEvent.VK_ASTERICS:
              keyboard.pressKey(6,1);
              break;

         case KeyEvent.VK_CANCEL:
              keyboard.pressKey(7,0);
              break;
         case KeyEvent.VK_SPACE:
              keyboard.pressKey(0,4);
              break;
         case KeyEvent.VK_ENTER:
              keyboard.pressKey(7,1);
              break;
         case KeyEvent.VK_COMMA:
              keyboard.pressKey(5,7);
              break;         
         }
 }

 public void keyTyped(KeyEvent e)
 {
  int keycode=e.getKeyCode();
  /**
   * Joystick1 menage.
   */
  if(joystick1!=null)
     {
      if(keycode==JOY1_UP)
         {
          joystick1.setJoy0();
          }
      else
      if(keycode==JOY1_UP)
        {
         joystick1.setJoy1();
         }
      else
      if(keycode==JOY1_LEFT)
        {
         joystick1.setJoy2();
         }
      else
      if(keycode==JOY1_RIGHT)
        {
         joystick1.setJoy3();
         }
      else
      if(keycode==JOY1_FIRE)
        {
         joystick1.setJoyBut();
         }
      }

  /**
   * Joystick2 menage.
   */
  if(joystick2!=null)
     {
     if(keycode==JOY2_UP)
         {
          joystick2.setJoy0();
          }
      else
      if(keycode==JOY2_UP)
        {
         joystick2.setJoy1();
         }
      else
      if(keycode==JOY2_LEFT)
        {
         joystick2.setJoy2();
         }
      else
      if(keycode==JOY2_RIGHT)
        {
         joystick2.setJoy3();
         }
      else
      if(keycode==JOY2_FIRE)
        {
         joystick2.setJoyBut();
         }
      }
  }

 public void keyReleased(KeyEvent e)
 {
  int keycode=e.getKeyCode();
  switch(keycode)
        {

         case KeyEvent.VK_A:
              keyboard.releaseKey(1,2);
              break;
         case KeyEvent.VK_B:
              keyboard.releaseKey(0,0); //
              break;
         case KeyEvent.VK_C:
              keyboard.releaseKey(2,4); 
              break;
         case KeyEvent.VK_D:
              keyboard.releaseKey(2,2);
              break;
         case KeyEvent.VK_E:
              keyboard.releaseKey(1,6);
              break;
         case KeyEvent.VK_F:
              keyboard.releaseKey(2,5);
              break;
         case KeyEvent.VK_G:
              keyboard.releaseKey(3,2); 
              break;
         case KeyEvent.VK_H:
              keyboard.releaseKey(3,5);
              break;
         case KeyEvent.VK_I:
              keyboard.releaseKey(4,1);
              break;
         case KeyEvent.VK_J:
              keyboard.releaseKey(4,2);
              break;
         case KeyEvent.VK_K:
              keyboard.releaseKey(4,5);
              break;
         case KeyEvent.VK_L:
              keyboard.releaseKey(5,2);
              break;
         case KeyEvent.VK_M:
              keyboard.releaseKey(4,4);
              break;
         case KeyEvent.VK_N:
              keyboard.releaseKey(4,7);
              break;
         case KeyEvent.VK_O:
              keyboard.releaseKey(1,6);
              break;
         case KeyEvent.VK_P:
              keyboard.releaseKey(5,1); 
              break;
         case KeyEvent.VK_Q:
              keyboard.releaseKey(0,0);  //
              break;
         case KeyEvent.VK_R:
              keyboard.releaseKey(2,1);
              break;
         case KeyEvent.VK_S:
              keyboard.releaseKey(1,5);
              break;
         case KeyEvent.VK_T:
              keyboard.releaseKey(2,6);
              break;
         case KeyEvent.VK_U:
              keyboard.releaseKey(3,6);
              break;
         case KeyEvent.VK_V:
              keyboard.releaseKey(3,7);
              break;
         case KeyEvent.VK_W:
              keyboard.releaseKey(1,1);
              break;
         case KeyEvent.VK_X:
              keyboard.releaseKey(2,7);
              break;
         case KeyEvent.VK_Y:
              keyboard.releaseKey(3,1);
              break;
         case KeyEvent.VK_Z:
              keyboard.releaseKey(1,4);
              break;

       
         case KeyEvent.VK_0:
              keyboard.releaseKey(4,3);
              break;
         case KeyEvent.VK_1:
              keyboard.releaseKey(0,0);
              break;
         case KeyEvent.VK_2:
              keyboard.releaseKey(0,3);
              break;
         case KeyEvent.VK_3:
              keyboard.releaseKey(1,0); 
              break;
         case KeyEvent.VK_4:
              keyboard.releaseKey(1,3);
              break;
         case KeyEvent.VK_5:
              keyboard.releaseKey(2,0);
              break;
         case KeyEvent.VK_6:
              keyboard.releaseKey(2,3);
              break;
         case KeyEvent.VK_7:
              keyboard.releaseKey(3,0); 
              break;
         case KeyEvent.VK_8:
              keyboard.releaseKey(3,3);
              break;
         case KeyEvent.VK_9:
              keyboard.releaseKey(4,0);
              break;

         case KeyEvent.VK_F1:
         case KeyEvent.VK_F2:
              keyboard.releaseKey(7,4);
              break;
         case KeyEvent.VK_F3:
         case KeyEvent.VK_F4:
              keyboard.releaseKey(7,5);
              break;
         case KeyEvent.VK_F5:
         case KeyEvent.VK_F6:
              keyboard.releaseKey(7,6);
              break;
         case KeyEvent.VK_F7:
         case KeyEvent.VK_F8:
              keyboard.releaseKey(7,3);     
              break;          

         case KeyEvent.VK_EQUALS:
              keyboard.releaseKey(6,5);
              break;
         case KeyEvent.VK_ADD:
              keyboard.releaseKey(5,0);
              break;
        // case KeyEvent.VK_SEPARATOR:
         case KeyEvent.VK_SUBTRACT:
              keyboard.releaseKey(5,7);
              break;
         case KeyEvent.VK_DIVIDE:
         case KeyEvent.VK_SLASH:
              break;
         case KeyEvent.VK_MULTIPLY:
        // case KeyEvent.VK_ASTERICS:
              keyboard.releaseKey(6,1);
              break;

         case KeyEvent.VK_CANCEL:
              keyboard.releaseKey(7,0);
              break;
         case KeyEvent.VK_SPACE:
              keyboard.releaseKey(0,4);
              break;
         case KeyEvent.VK_ENTER:
              keyboard.releaseKey(7,1);
              break;
         case KeyEvent.VK_COMMA:
              keyboard.releaseKey(5,7);
              break;
         }
  
   /**
   * Joystick1 menage.
   */

  if(joystick1!=null)
     {
      if(keycode==JOY1_UP)
         {
          joystick1.resetJoy0();
          }
      else
      if(keycode==JOY1_UP)
        {
         joystick1.resetJoy1();
         }
      else
      if(keycode==JOY1_LEFT)
        {
         joystick1.resetJoy2();
         }
      else
      if(keycode==JOY1_RIGHT)
        {
         joystick1.resetJoy3();
         }
      else
      if(keycode==JOY1_FIRE)
        {
         joystick1.resetJoyBut();
         }
      }

  /**
   * Joystick2 menage.
   */

  if(joystick2!=null)
     {
     if(keycode==JOY2_UP)
         {
          joystick2.resetJoy0();
          }
      else
      if(keycode==JOY2_UP)
        {
         joystick2.resetJoy1();
         }
      else
      if(keycode==JOY2_LEFT)
        {
         joystick2.resetJoy2();
         }
      else
      if(keycode==JOY2_RIGHT)
        {
         joystick2.resetJoy3();
         }
      else
      if(keycode==JOY2_FIRE)
        {
         joystick2.resetJoyBut();
         }
      }

  }

}//UserEvent     
