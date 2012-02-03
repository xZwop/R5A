package alma.logoot.network;

import java.util.Collection;

import alma.logoot.logootengine.IOperation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NetworkServiceAsync {

	void send(Collection<IOperation> o, AsyncCallback<Void> callback);

	void waitForChange(AsyncCallback<Collection<IOperation>> callback);

}
