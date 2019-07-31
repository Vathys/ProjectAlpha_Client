package client_package;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

public class MyDocumentListener implements DocumentListener{

	
	@Override
	public void insertUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		printChanges(e, "insert");
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		printChanges(e, "remove");
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
	public void printChanges(DocumentEvent e, String action)
	{
		DocumentEvent.ElementChange lineChange = e.getChange(e.getDocument().getDefaultRootElement());
		int startOffset = e.getOffset();
	    int endOffset = startOffset + e.getLength();
	    int[] offsetRange = new int[] {startOffset, endOffset};
	    Element[] addedLines = null;
	    if(lineChange != null)
	    {
	   addedLines  = lineChange.getChildrenAdded();
	    }
	    if(addedLines != null)
	    {
		if(action.equals("insert"))
		{
		System.out.println("Offset: " + e.getOffset());
		System.out.println("Change: " + addedLines.toString());
		}
		else
		{
			System.out.println();
		}
	    }
	}
}
