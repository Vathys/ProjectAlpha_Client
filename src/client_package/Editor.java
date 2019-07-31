package client_package;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class Editor
{
     /* Current protocol
      * 
      * {EventType Offset Length StringValue}
      * 
      * EventType -> [+ (EventType.INSERT) OR - (EventType.REMOVE)]
      * Offset -> [off, #]
      * Length -> [len, #]
      * StringValue -> "val"
      * 
      * Example:
      * 
      * {[+][off12][len1]"d"}
      **/
     
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