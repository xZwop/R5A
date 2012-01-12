package alma.logoot.client;


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import alma.logoot.shared.LogootEngine;
import alma.logoot.shared.LogootIdContainer;
import alma.logoot.shared.LogootIdentifier;
import alma.logoot.shared.OpInsert;
import alma.logoot.shared.Operation;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dev.javac.asm.CollectAnnotationData;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class R5A implements EntryPoint {
	
	LinkedList<LogootIdContainer> idTable=new LinkedList<LogootIdContainer>();
	LinkedList<LogootIdContainer> idTableRec=new LinkedList<LogootIdContainer>();
	LogootIdentifier ID=new LogootIdentifier(0,1547,0);
	
	final TextArea textArea = new TextArea();
	final FlexTable log = new FlexTable();
	//final TextArea log=new TextArea();
	final TextArea textReceiver=new TextArea();
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		initLog();
		initIdTable();
		
		// Init textAreas
		RootPanel.get("textAreaContainer").add(textArea);
		RootPanel.get("textReceiver").add(textReceiver);
		
		//RootPanel.get("logContainer").add(log);
		LogootIdContainer id=new LogootIdContainer();
		id.add(new LogootIdentifier(15, 1, 1));
		id.add(new LogootIdentifier(5, 1, 2));
		textArea.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				System.out.println("onKeypress");
				int pos=textArea.getCursorPos();
				LogootIdContainer p=idTable.get(pos);
				LogootIdContainer q=idTable.get(pos+1);
				ArrayList<LogootIdContainer> idList= LogootEngine.generateLineIdentier(p, q, 1, ID);
				char content=(char) event.getUnicodeCharCode();
				Operation op=new OpInsert(idList.get(0), content);
				idTable.add(pos+1,idList.get(0));
				log(pos,content,idList.get(0),p,q);
				send(op);
			}
		});
		/*textArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				// Création identifiantLogoot
				// Send	
				int pos=textArea.getCursorPos();
				LogootIdContainer p=idTable.get(pos-1);
				LogootIdContainer q=idTable.get(pos);
				ArrayList<LogootIdContainer> idList= LogootEngine.generateLineIdentier(p, q, 1, ID);
				System.out.println(event.getNativeEvent().getString());
				char content=(char) event.getNativeKeyCode();
				System.out.println("="+content+"=");
				Operation op=new OpInsert(idList.get(0), content);
				send(op);
				idTable.add(pos,idList.get(0));
				log(pos,content,idList.get(0),p,q);
//				log.setText(pos+"-"+((char)event.getNativeKeyCode())+"-"+idList.get(0)+"entre ("
//							+p+"-"+q+")\n"+log.getText());		
			}
		});*/
	}
	
	private void initIdTable() {
		LogootIdContainer first=new LogootIdContainer();
		first.add(new LogootIdentifier(0, 0, 0));
		
		LogootIdContainer last=new LogootIdContainer();
		last.add(new LogootIdentifier(99, 0, 0));
		idTable.add(first);
		idTable.add(last);
		idTableRec.add(first);
		idTableRec.add(last);		
	}

	int logRow=1;
	private void initLog() {
		log.setText(0, 0, "Curseur");
		log.setText(0, 1, "Caractere");
		log.setText(0, 2, "Position");
		log.setText(0, 3, "Precedent");
		log.setText(0, 4, "Suivant");
		RootPanel.get("logTable").add(log);
		log.addStyleName("log");
	}
	
	private void log(int posCur,char car,
			LogootIdContainer pos,
			LogootIdContainer prec,
			LogootIdContainer suiv){
		log.setText(logRow, 0, posCur+"");
		log.setText(logRow, 1, car+"");
		log.setText(logRow, 2, pos.toString());
		log.setText(logRow, 3, prec.toString());
		log.setText(logRow, 4, suiv.toString());
		++logRow;
	}

	private void send(Operation op) {
		receive(op);
	}

	private void receive(Operation op) {
		if(op.isIns()){
			OpInsert o=(OpInsert)op;
			int index=0;
			for(LogootIdContainer pos:idTableRec){
				if(pos.compareTo(o.getPosition())>0){
					idTableRec.add(index, o.getPosition());
					StringBuffer sb= new StringBuffer(textReceiver.getText());
					sb.insert(index-1, o.getContent());
					textReceiver.setText(sb.toString());
				}
				index++;
			}
		}
	}
		
		
}
