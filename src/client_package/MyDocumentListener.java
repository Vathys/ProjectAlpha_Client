package client_package;

import java.util.ArrayList;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

public class MyDocumentListener implements DocumentListener
{
     Element[] addedLines = null;
     Element[] removedLines = null;
     
     @Override
     public void insertUpdate(DocumentEvent e)
     {
          System.out.println("INSERT_UPDATE");
          printChanges(e, "insert");

     }

     @Override
     public void removeUpdate(DocumentEvent e)
     {
          System.out.println("REMOVE_UPDATE");
          printChanges(e, "remove");
     }

     @Override
     public void changedUpdate(DocumentEvent e)
     {
          System.out.println("CHANGED_UPDATE");
          printChanges(e, "changed");
     }

     public void printChanges(DocumentEvent e, String action)
     {
          DocumentEvent.ElementChange lineChange = e.getChange(e.getDocument().getDefaultRootElement());
          System.out.println(lineChange);
          
          if (lineChange != null)
          {
               addedLines = lineChange.getChildrenAdded();
               removedLines = lineChange.getChildrenRemoved();
          }
          if (addedLines != null)
          {
               System.out.println(action);
               System.out.println("Offset: " + e.getOffset());
               
               for(Element el : addedLines)
               {
                    try
                    {
                         System.out.print(e.getDocument().getText(el.getStartOffset(), el.getEndOffset() - el.getStartOffset()));
                    } catch (BadLocationException e1)
                    {
                         e1.printStackTrace();
                    }
               }
          }
          if (removedLines != null)
          {
               for(Element el : removedLines)
               {
                    try
                    {
                         System.out.print(e.getDocument().getText(el.getStartOffset(), el.getElementCount()));
                    } catch (BadLocationException e1)
                    {
                         e1.printStackTrace();
                    }
               }
          }
     }
}
