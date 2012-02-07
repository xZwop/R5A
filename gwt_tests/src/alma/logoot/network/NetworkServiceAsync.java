package alma.logoot.network;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NetworkServiceAsync {

	void send(String o, AsyncCallback<Void> callback);

}
