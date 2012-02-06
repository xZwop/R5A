package alma.logoot.network;

import java.util.Collection;

import alma.logoot.logootengine.IOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Network implements INetwork {

	private IReceiveListener receiveListener;
	private NetworkServiceAsync service = GWT.create(NetworkService.class);

	// Le network peut �tre unique - Dans ce cas la ce n'est pas le
	// controlleur qui va le cr��er, mais il doit s'y connecter en
	// connaissant son addresse.
	// Il peut aussi �tre multiple - Un par client.

	public Network() {
		initReceive();
	}

	@Override
	public void send(Collection<IOperation> o) {
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
			this.@alma.logoot.network.Network::receive(Ljava/util/Collection;)(event.data);
		};
	}-*/;

	public void receive(Collection<IOperation> text) {
		System.out.println("Network reception des donnees..");
		// Gson gson = new Gson();
		// Collection<IOperation> patch = gson.fromJson(text, Collection.class);
		receiveListener.receive(text);
	}

	private void invokeWaiting() {
		service.waitForChange(new AsyncCallback<Collection<IOperation>>() {

			@Override
			public void onSuccess(Collection<IOperation> result) {
				receiveListener.receive(result);
				invokeWaiting();
			}

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Failure callback invokeWaiting");
				invokeWaiting();
			}
		});
	}

}
