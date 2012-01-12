package alma.logoot.client;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import alma.logoot.shared.LogootEngine;
import alma.logoot.shared.LogootIdContainer;
import alma.logoot.shared.LogootIdentifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dev.javac.asm.CollectAnnotationData;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class R5A implements EntryPoint {
	
	LinkedList<LogootIdContainer> idTable=new LinkedList<LogootIdContainer>();
	LogootIdentifier ID=new LogootIdentifier(0,1547,0);
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		LogootIdContainer first=new LogootIdContainer();
		first.add(new LogootIdentifier(0, 0, 0));
		
		LogootIdContainer last=new LogootIdContainer();
		last.add(new LogootIdentifier(99, 0, 0));
		idTable.add(first);
		idTable.add(last);
		
		
		
		final TextArea textArea = new TextArea();
		final TextArea log=new TextArea();
		RootPanel.get("textAreaContainer").add(textArea);
		RootPanel.get("logContainer").add(log);
		LogootIdContainer id=new LogootIdContainer();
		id.add(new LogootIdentifier(15, 1, 1));
		id.add(new LogootIdentifier(5, 1, 2));
		textArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				// Création identifiantLogoot
				// Send	
				int pos=textArea.getCursorPos();
				LogootIdContainer p=idTable.get(pos-1);
				LogootIdContainer q=idTable.get(pos);
				ArrayList<LogootIdContainer> idList= LogootEngine.generateLineIdentier(p, q, 1, ID);
				idTable.add(pos,idList.get(0));
				log.setText(pos+"-"+((char)event.getNativeKeyCode())+"-"+idList.get(0)+"entre ("
							+p+"-"+q+")\n"+log.getText());		
			}
		});
	}
		
		
}
