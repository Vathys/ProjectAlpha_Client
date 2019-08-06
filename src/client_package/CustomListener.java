package client_package;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.BadLocationException;

public class CustomListener implements ActionListener, DocumentListener, KeyListener
{
     private Client client;
     private Editor parent;
     public boolean ignoreEvent;

     public CustomListener(Client c, Editor e)
     {
          client = c;
          parent = e;
     }

     @Override
     public void actionPerformed(ActionEvent e)
     {
          String s = e.getActionCommand();

          if (s.equals("Cut"))
          {
               parent.getTextArea().cut();
          } else if (s.equals("Copy"))
          {
               parent.getTextArea().copy();
          } else if (s.equals("Paste"))
          {
               parent.getTextArea().paste();
          } else if (s.equals("Save"))
          {
               // Create an object of JFileChooser class 
               JFileChooser j = new JFileChooser("f:");

               // Invoke the showsSaveDialog function to show the save dialog 
               int r = j.showSaveDialog(null);

               if (r == JFileChooser.APPROVE_OPTION)
               {

                    // Set the label to the path of the selected directory 
                    File fi = new File(j.getSelectedFile().getAbsolutePath());

                    try
                    {
                         // Create a file writer 
                         FileWriter wr = new FileWriter(fi, false);

                         // Create buffered writer to write 
                         BufferedWriter w = new BufferedWriter(wr);

                         // Write 
                         w.write(parent.getTextArea().getText());

                         w.flush();
                         w.close();
                    } catch (Exception evt)
                    {
                         JOptionPane.showMessageDialog(parent.getFrame(), evt.getMessage());
                    }
               }
               // If the user cancelled the operation 
               else
                    JOptionPane.showMessageDialog(parent.getFrame(), "the user cancelled the operation");
          } else if (s.equals("Print"))
          {
               try
               {
                    // print the file 
                    parent.getTextArea().print();
               } catch (Exception evt)
               {
                    JOptionPane.showMessageDialog(parent.getFrame(), evt.getMessage());
               }
          } else if (s.equals("Open"))
          {
               // Create an object of JFileChooser class 
               JFileChooser j = new JFileChooser("f:");

               // Invoke the showsOpenDialog function to show the save dialog 
               int r = j.showOpenDialog(null);

               // If the user selects a file 
               if (r == JFileChooser.APPROVE_OPTION)
               {
                    // Set the label to the path of the selected directory 
                    File fi = new File(j.getSelectedFile().getAbsolutePath());

                    try
                    {
                         // String 
                         String s1 = "", sl = "";

                         // File reader 
                         FileReader fr = new FileReader(fi);

                         // Buffered reader 
                         BufferedReader br = new BufferedReader(fr);

                         // Initilize sl 
                         sl = br.readLine();

                         // Take the input from the file 
                         while ((s1 = br.readLine()) != null)
                         {
                              sl = sl + "\n" + s1;
                         }

                         // Set the text 
                         parent.getTextArea().setText(sl);

                         br.close();
                    } catch (Exception evt)
                    {
                         JOptionPane.showMessageDialog(parent.getFrame(), evt.getMessage());
                    }
               }
               // If the user cancelled the operation 
               else
                    JOptionPane.showMessageDialog(parent.getFrame(), "the user cancelled the operation");
          } else if (s.equals("New"))
          {
               parent.getTextArea().setText("");
          } else if (s.equals("Close"))
          {
               parent.getFrame().setVisible(false);
          } else if (s.equals("+"))
          {
               Font font = parent.getTextArea().getFont();
               float size = font.getSize() + 1.0f;
               parent.getTextArea().setFont(font.deriveFont(size));
               System.out.println("+");
          } else if (s.equals("-"))
          {
               Font font = parent.getTextArea().getFont();
               float size = font.getSize() - 1.0f;
               parent.getTextArea().setFont(font.deriveFont(size));
          }

     }

     @Override
     public void insertUpdate(DocumentEvent e)
     {
          String val;
          try
          {
               val = e.getDocument().getText(e.getOffset(), e.getLength());
               send(e, val);
          } catch (BadLocationException e1)
          {
               e1.printStackTrace();
          }
     }

     @Override
     public void removeUpdate(DocumentEvent e)
     {
          send(e, "");
     }

     @Override
     public void changedUpdate(DocumentEvent e)
     {
     }

     public void send(DocumentEvent e, String val)
     {
          String msg = "";

          if (e.getType().equals(EventType.INSERT))
          {
               msg += "[+]";
          } else if (e.getType().equals(EventType.REMOVE))
          {
               msg += "[-]";
               val = "";
          }

          msg += "[off" + e.getOffset() + "]";
          msg += "[len" + e.getLength() + "]";

          if (val.equals("\n"))
          {
               msg += "\"" + "newLine" + "\"";
          } else
          {
               msg += "\"" + val + "\"";
          }

          client.send(msg);
          //System.out.println(msg);
     }

     boolean control_pressed = false;

     @Override
     public void keyPressed(KeyEvent e)
     {

          if ((e.getKeyCode() == KeyEvent.VK_EQUALS) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
          {
               Font font = parent.getTextArea().getFont();
               float size = font.getSize() + 1.0f;
               parent.getTextArea().setFont(font.deriveFont(size));
          } else if (e.getKeyCode() == KeyEvent.VK_MINUS && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
          {
               Font font = parent.getTextArea().getFont();
               float size = font.getSize() - 1.0f;
               parent.getTextArea().setFont(font.deriveFont(size));
          }

     }

     @Override
     public void keyReleased(KeyEvent e)
     {
          // TODO Auto-generated method stub
          if (e.getKeyCode() == KeyEvent.VK_CONTROL)
          {
               //    control_pressed = false;
          }

     }

     @Override
     public void keyTyped(KeyEvent e)
     {
          // TODO Auto-generated method stub

     }
}
