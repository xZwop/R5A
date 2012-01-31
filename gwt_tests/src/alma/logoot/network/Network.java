package alma.logoot.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Network implements INetwork {

	private IReceiveListener receiveListener;
	private NetworkServiceAsync service = GWT.create(NetworkService.class);
	// Le network peut être unique - Dans ce cas la ce n'est pas le controlleur qui va le crééer, mais il doit s'y connecter en connaissant son addresse.
	// Il peut aussi être multiple - Un par client. 
	
	@Override
	public void send(Object o) {
		// TODO Auto-generated method stub
		// Envoyer l'objet vers le serveur.
		service.send(o, null);
	}

	@Override
	public int connect() {
		// TODO Auto-generated method stub.
		// Initialiser le server
		int id = (int) (Math.random()*1000000);
		System.out.println("Id generated : "+id);
		return id;
	}

	@Override
	public void addReceiverListener(IReceiveListener listener) {
		this.receiveListener = listener;
	}
	
	private void invokeWaiting(){
		service.waitForChange(new AsyncCallback<Object>() {
			
			@Override
			public void onSuccess(Object result) {
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
