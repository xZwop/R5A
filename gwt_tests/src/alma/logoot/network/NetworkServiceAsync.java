package alma.logoot.network;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NetworkServiceAsync {

	void send(Object o, AsyncCallback<Void> callback);

	void waitForChange(AsyncCallback<Object> callback);

}
