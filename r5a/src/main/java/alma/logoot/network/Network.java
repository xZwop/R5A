package alma.logoot.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Network implements INetwork {

	public static Network instance = null;
	private IReceiveListener receiveListener;
	private NetworkServiceAsync service = GWT.create(NetworkService.class);
	private Integer id = new Integer(-1);

	public Network() {
		instance = this;
		initReceive();
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void send(String o) {
		// TODO Auto-generated method stub
		// Envoyer l'objet vers le serveur.
		System.out.println(o);
		service.send(o, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				System.out.println("Network.send.onSuccess");
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				System.out.println("Network.send.onFailure");
			}
		});
	}

	@Override
	public Integer connect() {
		// Initialiser le server

		// int id = (int) (Math.random() * 1000000);
		// System.out.println("Id generated : " + id);
		// return id;
		service.register(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				System.err
						.println("Network : Erreur enregistrement au serveur.");
			}

			@Override
			public void onSuccess(String result) {
				System.out.println("Network : Enregistrement au serveur."
						+ result);
				setId(Integer.parseInt(result));
			}
		});
		System.out.println("Reception id " + id);
		return id;
	}

	@Override
	public void addReceiverListener(IReceiveListener listener) {
		this.receiveListener = listener;
	}

	public native void initReceive() /*-{
		var source = new EventSource('getData');
		source.onmessage = function(event) {
      // alert(event.data);
			var text = event.data.replace(/<br>/g, '\n');
			var me = @alma.logoot.network.Network::instance;
			me.@alma.logoot.network.Network::receive(Ljava/lang/String;)(text);
		};
	}-*/;

	public void receive(String text) {
		System.out.println("Network reception des donnees.." + text);
//		text.replaceAll("br", "\n");
//		System.out.println("Network apres remplacement "+ text);
		receiveListener.receive(text);
	}

}
