package alma.logoot.controller;

import alma.logoot.logootengine.FactoryLogootEngine;
import alma.logoot.logootengine.ILogootEngine;
import alma.logoot.network.FactoryNetwork;
import alma.logoot.network.INetwork;
import alma.logoot.network.IReceiveListener;
import alma.logoot.ui.FactoryUI;
import alma.logoot.ui.IChangeListener;
import alma.logoot.ui.IUI;

import com.google.gwt.core.client.EntryPoint;

public class Controller implements EntryPoint, IChangeListener,
		IReceiveListener {

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
		Integer id = network.connect();
		// On initalise le logootengine grace a l'id recupere sur le network.
		logootEngine = FactoryLogootEngine.getInstance();
		// logootEngine.setId(id);
	}

	@Override
	public void change(String text) {
		// TODO on set l'id du client a chaque saisie de caractere, ce qu'il ne
		// faudrais pas obligatoirement faire, il faut vériofier que le client
		// n'a ps l'id -1 sinon il ne faudrait pas remplacer.
		logootEngine.setId(network.getId());
		String patch = logootEngine.generatePatch(text);
		if (!patch.equals("[]"))
			network.send(patch);
	}

	@Override
	public void receive(String o) {
		System.out.println("Objet recu :" + o);
		System.out.println("Objet de type : " + o.getClass().getName());
		try {
			String text = logootEngine.deliver(o);
			ui.setText(text);
		} catch (ClassCastException e) {
			System.err
					.println("Error, failed to cast the received object into a collection of operations");
		}
	}
}
