package client_package;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class Editor
{
     private JFrame frame;
     private JTextArea textArea;

     private CustomListener lis;

     
     
     // Constructor 
     public Editor(Client c)
     {
          super();
          /*try
          {
               // Set metl look and feel 
               UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
          
               // Set theme to ocean 
               MetalLookAndFeel.setCurrentTheme(new OceanTheme());
          } catch (Exception e)
          {
               e.printStackTrace();
          }*/

          lis = new CustomListener(c, this);

          //Frame
          frame = new JFrame("Editor");

          // Text component 
          textArea = new JTextArea();
          textArea.getDocument().addDocumentListener(lis);
          
          //REDO MENU BAR
          //Especially New, Open, Save
          //Function of New, Open, Save will depend on where
          //Document will be saved...
          //either in the server or one of the clients...

          // Create a menubar 
          JMenuBar mb = new JMenuBar();

          // Create amenu for menu 
          JMenu m1 = new JMenu("File");

          // Create menu items 
          JMenuItem mi1 = new JMenuItem("New");
          JMenuItem mi2 = new JMenuItem("Open");
          JMenuItem mi3 = new JMenuItem("Save");
          JMenuItem mi9 = new JMenuItem("Print");

          // Add action listener 
          mi1.addActionListener(lis);
          mi2.addActionListener(lis);
          mi3.addActionListener(lis);
          mi9.addActionListener(lis);

          m1.add(mi1);
          m1.add(mi2);
          m1.add(mi3);
          m1.add(mi9);

          // Create amenu for menu 
          JMenu m2 = new JMenu("Edit");

          // Create menu items 
          JMenuItem mi4 = new JMenuItem("Cut");
          JMenuItem mi5 = new JMenuItem("Copy");
          JMenuItem mi6 = new JMenuItem("Paste");

          // Add action listener 
          mi4.addActionListener(lis);
          mi5.addActionListener(lis);
          mi6.addActionListener(lis);

          m2.add(mi4);
          m2.add(mi5);
          m2.add(mi6);

          JMenuItem mc = new JMenuItem("Close");

          mc.addActionListener(lis);

          mb.add(m1);
          mb.add(m2);
          mb.add(mc);

          frame.setJMenuBar(mb);
          frame.add(textArea);
          frame.setSize(500, 500);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);
     }

     public void updateDoc(String com)
     {
          textArea.getDocument().removeDocumentListener(lis);
          
          ArrayList<String> check = RegexParser.matches("\\[([+|-])\\]\\[off(\\d+)\\]\\[len(\\d+)\\]\"(.*?)\"", com);
          /*
          for(int i = 1; i < check.size(); i++)
          {
               System.out.println(i + ": " + check.get(i));
          }
          */
          int offset = Integer.valueOf(check.get(2)).intValue();
          int length = Integer.valueOf(check.get(3)).intValue();
          String str = check.get(4);
          
          if(str.equals("newLine") && length == 1)
          {
               str = "\n";
          }
          
          try
          {
               if(check.get(1).equals("+"))
               {
                    textArea.getDocument().insertString(offset, str, null);
                    lis.ignoreEvent = true;
               }
               else if(check.get(1).equals("-"))
               {
                    textArea.getDocument().remove(offset, length);
                    lis.ignoreEvent = true;
               }
          } catch(BadLocationException e)
          {
               e.printStackTrace();
          }
          
          textArea.getDocument().addDocumentListener(lis);
     }
     
     /**
      * @return the frame
      */
     public JFrame getFrame()
     {
          return frame;
     }

     /**
      * @param frame the frame to set
      */
     public void setFrame(JFrame frame)
     {
          this.frame = frame;
     }

     /**
      * @return the textArea
      */
     public JTextArea getTextArea()
     {
          return textArea;
     }

     /**
      * @param textArea the textArea to set
      */
     public void setTextArea(JTextArea textArea)
     {
          this.textArea = textArea;
     }
}