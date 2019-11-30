package sw_emulator.hardware.device;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class C64Form
             extends JFrame
             implements ActionListener 
{
 /**
  * A simple Frame to display the content of a TV object.
  *
  * @author Michele Caira
  * @version 1.0
  */
 
 /**
  * The reference to the TV object.
  */
 private Canvas tv;

 public C64Form()
 {
  setTitle("JC64");
  getContentPane().setLayout(new BorderLayout());
  Dimension d = getToolkit().getScreenSize();
  setBounds((d.width-320)/2,(d.height-200)/2,330,225);
  initComponents();
 }

 private void initComponents()
 {  
  MenuBar menuBar=new MenuBar();
  String[] file = {"Load",null,"Exit"};
  String[] options = {"320x200","480x300","640x400"};
  String[] info    = {"Authors","About"};
 
  menuBar.add(createC64Menu("File",file));  
  menuBar.add(createC64Menu("Options",options));
  menuBar.add(createC64Menu("?",info));
  setMenuBar(menuBar);		 
  }

 private Menu createC64Menu(String label,String[] items)
 {
  Menu menu=new Menu(label);
  for(int i=0;i<items.length;i++)
      {
       if(items[i]!=null)
	  {
          MenuItem item = new MenuItem(items[i]);
	  item.addActionListener(this);
	  menu.add(item);
	  }
       else menu.addSeparator();
       }
  return menu;     
 }

 public void actionPerformed(ActionEvent e)
 {
  String cmd=e.getActionCommand();
	 
  if(cmd.equals("Load"))
    {
     System.out.println("Sorry not implemented yet :(");
    }
  else
  if(cmd.equals("320x200"))
     {
      setSize(320,200);
      repaint();
      }
  else
  if(cmd.equals("480x300"))
    {
     setSize(480,300);
     repaint();
     }
  else
  if(cmd.equals("640x400"))
    {
     setSize(640,400);
     repaint();
     }
  else
  if(cmd.equals("Authors"))
    {
     System.out.println("Stefano Tognon  Ice00@libero.it     from Italy ");
     System.out.println("Michele Caira   (supporter!!) caira_mik@hotmail.com from Italy");
     }
  else
  if(cmd.equals("About"))
    {
     System.out.println("JC64 Emu...");
    }
  if(cmd.equals("Exit"))
    {
     setVisible(false);
     System.exit(0);
     } 
  }
	 

 public void setSize(int width,int height)
 {
  if(tv!=null)
     tv.setSize(width,height);
  super.setSize(width+10,height+25);
  }

 public void addTV(Canvas tv)
 {
  this.tv=tv;
  getContentPane().add(tv,BorderLayout.CENTER);
  }

 public void update(Graphics g)
 {
   if(tv!=null)
      tv.repaint();
   super.update(g);  // ancora utile?
  }

}//C64Form
