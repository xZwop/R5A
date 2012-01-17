package alma.logoot.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import alma.logoot.shared.LogootConf;
import alma.logoot.shared.LogootEngine;
import alma.logoot.shared.LogootIdContainer;
import alma.logoot.shared.LogootIdentifier;
import alma.logoot.shared.OpDelete;
import alma.logoot.shared.OpInsert;
import alma.logoot.shared.Operation;
import alma.logoot.shared.Patch;
import alma.logoot.shared.diff_match_patch;
import alma.logoot.shared.diff_match_patch.Diff;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

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

	String old = "";

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		initLog();
		initIdTable();

		// Initialisation des textareas
		RootPanel.get("textAreaContainer").add(textArea);
		RootPanel.get("textReceiver").add(textReceiver);

		// Definition des hooks
		textArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				System.out.println("=======Event KeyUp=========");
				onEvent();
			}
		});
		textArea.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				System.out.println("=======Event Change=========");
				onEvent();
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
		System.out.println("= send patch =\n=>" + patch);
		receive(patch);
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

			// int index = 0;
			// for (LogootIdContainer pos : idTableRec) {
			// if (pos.compareTo(o.getPosition()) > 0) {
			// idTableRec.add(index, o.getPosition());
			// StringBuffer sb = new StringBuffer(textReceiver.getText());
			// sb.insert(index - 1, o.getContent());
			// textReceiver.setText(sb.toString());
			// }
			// index++;
			// }
		} else {
			int index = Collections.binarySearch(idTableRec, op.getPosition());
			if (index > 0) {
				StringBuffer sb = new StringBuffer(textReceiver.getText());
				sb.deleteCharAt(index - 1);
				textReceiver.setText(sb.toString());
				idTableRec.remove(index);
			}
			// int index = idTableRec.indexOf(op.getPosition());
			// idTableRec.remove(index);
			// StringBuffer sb = new StringBuffer(textReceiver.getText());
			// sb.deleteCharAt(index - 1);
			// textReceiver.setText(sb.toString());
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
		System.out.println("Diff : " + diff);
		old = textArea.getText();

		int index = 0;
		alma.logoot.shared.Patch patch = new alma.logoot.shared.Patch();
		for (Diff d : diff) {
			if (d.operation == alma.logoot.shared.diff_match_patch.Operation.EQUAL) {
				index += d.text.length();
			} else if (d.operation == alma.logoot.shared.diff_match_patch.Operation.INSERT) {
				LogootIdContainer p = idTable.get(index);
				LogootIdContainer q = idTable.get(index + 1);
				ArrayList<LogootIdContainer> idList = LogootEngine
						.generateLineIdentier(p, q, d.text.length(), ID);
				// Mise a jour idTable
				idTable.addAll(index + 1, idList);
				// Creation opérations
				int i = 0;
				for (LogootIdContainer lic : idList) {
					alma.logoot.shared.Operation op = new OpInsert(lic,
							d.text.charAt(i));
					patch.add(op);
					log(index, d.text.charAt(i), lic, p, q);
					i++;
				}
				index += d.text.length();
			} else { // DELETE
				for (int i = 0; i < d.text.length(); i++) {
					LogootIdContainer position = idTable.get(index + 1);
					idTable.remove(position);
					alma.logoot.shared.Operation op = new OpDelete(position);
					patch.add(op);
				}
			}
		}
		if(patch.size()>0){
			send(patch);
		}

		if (!textArea.getText().equals(textReceiver.getText())) {
			textReceiver.addStyleName("error");
		} else {
			textReceiver.removeStyleName("error");
		}
	}

}
