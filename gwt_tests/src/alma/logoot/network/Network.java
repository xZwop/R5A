package alma.logoot.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Network implements INetwork {

	
	public static Network instance = null;
	private IReceiveListener receiveListener;
	private NetworkServiceAsync service = GWT.create(NetworkService.class);

	public Network() {
		instance = this;
		initReceive();
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
	public int connect() {
		// TODO Auto-generated method stub.
		// Initialiser le server
		int id = (int) (Math.random() * 1000000);
		System.out.println("Id generated : " + id);
		return id;
	}

	@Override
	public void addReceiverListener(IReceiveListener listener) {
		this.receiveListener = listener;
	}

	public native void initReceive() /*-{
		var source = new EventSource('GetData');
		source.onmessage = function(event) {
			alert(event.data);
			var me = @alma.logoot.network.Network::instance;
			me.@alma.logoot.network.Network::receive(Ljava/lang/String;)(event.data);
		};
	}-*/;

	public void receive(String text) {
		System.out.println("Network reception des donnees.."+text);
		receiveListener.receive(text);
	}
	
}
