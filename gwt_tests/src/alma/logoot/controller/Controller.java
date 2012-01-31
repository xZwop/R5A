package alma.logoot.controller;

import java.util.Collection;

import alma.logoot.logootengine.FactoryLogootEngine;
import alma.logoot.logootengine.ILogootEngine;
import alma.logoot.logootengine.IOperation;
import alma.logoot.network.FactoryNetwork;
import alma.logoot.network.INetwork;
import alma.logoot.network.IReceiveListener;
import alma.logoot.ui.FactoryUI;
import alma.logoot.ui.IChangeListener;
import alma.logoot.ui.IUI;

import com.google.gwt.core.client.EntryPoint;

public class Controller implements EntryPoint, IChangeListener, IReceiveListener {

	IUI ui;
	INetwork network;
	ILogootEngine logootEngine;

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// On initialise la vue.
		ui = FactoryUI.getInstance();
		ui.addChangeListener(this);
		// On initialise le network.
		network = FactoryNetwork.getInstance();
		network.addReceiverListener(this);
		// -> Recuperer l'id du client en appelant le network
		int id = network.connect();
		// On initalise le logootengine grace a l'id recupere sur le network.
		logootEngine = FactoryLogootEngine.getInstance();
		logootEngine.setId(id);
	}

	@Override
	public void change(String text) {
		network.send(logootEngine.generatePatch(text));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void receive(String o) { 
		try {
			String text = logootEngine.deliver((String) o);
			ui.setText(text);
		} catch (ClassCastException e) {
			System.err.println("Error, failed to cast the received object into a collection of operations");
		}
	}
}
