package alma.logoot.ui.gwt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import alma.logoot.logootengine.LogootConf;
import alma.logoot.logootengine.LogootEngine;
import alma.logoot.logootengine.LogootIdContainer;
import alma.logoot.logootengine.LogootIdentifier;
import alma.logoot.logootengine.OpDelete;
import alma.logoot.logootengine.OpInsert;
import alma.logoot.logootengine.Operation;
import alma.logoot.logootengine.Patch;
import alma.logoot.logootengine.diff_match_patch;
import alma.logoot.logootengine.diff_match_patch.Diff;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class R5A implements EntryPoint {

	ArrayList<LogootIdContainer> idTable = new ArrayList<LogootIdContainer>();
	ArrayList<LogootIdContainer> idTableRec = new ArrayList<LogootIdContainer>();
	LogootIdentifier ID = new LogootIdentifier(0, 7, 0);

	final TextArea textArea = new TextArea();
	final FlexTable log = new FlexTable();
	final TextArea textReceiver = new TextArea();
	final TextBox identifier = new TextBox();
	final Button validIdentifier = new Button("Valider ID");
	
	String old = "";

	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final LogootServiceAsync logootService = GWT
			.create(LogootService.class);


	class ConnectCallBack implements AsyncCallback<Patch> {
		@Override
		public void onSuccess(Patch result) {
			System.out.println("Client : succes");
			receive(result);
			logootService.connect(new ConnectCallBack());
		}

		@Override
		public void onFailure(Throwable caught) {
			System.out.println("Client : fail " + GWT.getModuleBaseURL());
			// TODO : sleep 10.
			logootService.connect(new ConnectCallBack());
		}
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		initLog();
		initIdTable();
		
		logootService.connect(new ConnectCallBack());

		// Initialisation des textareas
		RootPanel.get("textAreaContainer").add(textArea);
		RootPanel.get("textReceiver").add(textReceiver);
		RootPanel.get("identifier").add(identifier);
		RootPanel.get("validIdentifier").add(validIdentifier);

		// Definition des hooks
		textArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				onEvent();
			}
		});
		textArea.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				onEvent();
			}
		});
		
		validIdentifier.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ID.setIdentifier(Integer.parseInt(identifier.getValue()));
			}
		});

	}

	/**
	 * Envoie un patch contenant les modifications faites sur le texte.
	 * 
	 * @param patch
	 *            Liste de opérations
	 */
	protected void send(Patch patch) {
		System.out.println("= send patch ==>" + patch);
		if(logootService==null){
			//TODO Create
		}
		logootService.share(patch, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Client : erreur send");
			}
			@Override
			public void onSuccess(Void result) {
				System.out.println("Client : ok send");
			}
		});
	}

	private void receive(Patch patch) {
		System.out.println("= receive patch with " + patch.size() + "=");
		for (Operation o : patch) {
			try {
				receive(o);
			} catch (Exception e) {
				// FIXME Comprendre et traiter cette exception
			}
		}
		if (!textArea.getText().equals(textReceiver.getText())) {
			textReceiver.addStyleName("error");
		} else {
			textReceiver.removeStyleName("error");
		}
	}

	@SuppressWarnings("unused")
	private void send(Operation op) {
		System.out.println("= send(" + op + ") =");
		receive(op);
	}

	private void receive(Operation op) {
		System.out.println("= receive(" + op + ") =");
		if (op.isIns()) {
			OpInsert o = (OpInsert) op;
			int index = -Collections.binarySearch(idTableRec, op.getPosition()) - 1;
			StringBuffer sb = new StringBuffer(textReceiver.getText());
			sb.insert(index - 1, o.getContent());
			textReceiver.setText(sb.toString());
			idTableRec.add(index, o.getPosition());

		} else {
			int index = Collections.binarySearch(idTableRec, op.getPosition());
			if (index > 0) {
				StringBuffer sb = new StringBuffer(textReceiver.getText());
				sb.deleteCharAt(index - 1);
				textReceiver.setText(sb.toString());
				idTableRec.remove(index);
			}
		}
	}

	private void initIdTable() {
		LogootIdContainer first = new LogootIdContainer();
		first.add(new LogootIdentifier(1, 0, 0));

		LogootIdContainer last = new LogootIdContainer();
		last.add(new LogootIdentifier(LogootConf.BASE, 0, 0));
		idTable.add(first);
		idTable.add(last);
		idTableRec.add(first);
		idTableRec.add(last);
	}

	int logRow = 1;

	private void initLog() {
		log.setText(0, 0, "Curseur");
		log.setText(0, 1, "Caractere");
		log.setText(0, 2, "Position");
		log.setText(0, 3, "Precedent");
		log.setText(0, 4, "Suivant");
		RootPanel.get("logTable").add(log);
		log.addStyleName("log");
	}

	private void log(int posCur, char car, LogootIdContainer pos,
			LogootIdContainer prec, LogootIdContainer suiv) {
		log.setText(logRow, 0, posCur + "");
		log.setText(logRow, 1, car + "");
		log.setText(logRow, 2, pos.toString());
		log.setText(logRow, 3, prec.toString());
		log.setText(logRow, 4, suiv.toString());
		++logRow;
	}

	/**
	 * 
	 */
	private void onEvent() {

		
		diff_match_patch diffEngine = new diff_match_patch();
		LinkedList<Diff> diff = diffEngine.diff_main(old, textArea.getText(),
				false);
		old = textArea.getText();

		int index = 0;
		alma.logoot.logootengine.Patch patch = new alma.logoot.logootengine.Patch();
		for (Diff d : diff) {
			if (d.operation == alma.logoot.logootengine.diff_match_patch.Operation.EQUAL) {
				index += d.text.length();
			} else if (d.operation == alma.logoot.logootengine.diff_match_patch.Operation.INSERT) {
				LogootIdContainer p = idTable.get(index);
				LogootIdContainer q = idTable.get(index + 1);
				ArrayList<LogootIdContainer> idList = LogootEngine
						.generateLineIdentier(p, q, d.text.length(), ID);
				// Mise a jour idTable
				idTable.addAll(index + 1, idList);
				// Creation opérations
				int i = 0;
				/** 
				 *  TODO : lic2 warning, may bug
				 */
				LogootIdContainer lic2 = p;
				for (LogootIdContainer lic : idList) {
					alma.logoot.logootengine.Operation op = new OpInsert(lic,	d.text.charAt(i));
					patch.add(op);
					log(index, d.text.charAt(i), lic, lic2, q);
					lic2 = lic;
					i++;
				}
				index += d.text.length();
			} else { // DELETE
				for (int i = 0; i < d.text.length(); i++) {
					LogootIdContainer position = idTable.get(index + 1);
					idTable.remove(position);
					alma.logoot.logootengine.Operation op = new OpDelete(position);
					patch.add(op);
				}
			}
		}
		if(patch.size()>0){
			send(patch);
		}

	}

}
