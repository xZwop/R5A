package alma.logoot.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Network implements INetwork {

	private IReceiveListener receiveListener;
	private NetworkServiceAsync service = GWT.create(NetworkService.class);
	// Le network peut �tre unique - Dans ce cas la ce n'est pas le controlleur qui va le cr��er, mais il doit s'y connecter en connaissant son addresse.
	// Il peut aussi �tre multiple - Un par client. 
	
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
				System.out.println("Network.send.onFailure");
			}
		});
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
		service.waitForChange(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
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
