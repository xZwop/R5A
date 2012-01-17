package alma.logoot.client;

import alma.logoot.shared.OpInsert;
import alma.logoot.shared.Operation;
import alma.logoot.shared.Patch;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LogootServiceAsync {
	void connect(AsyncCallback<Patch> callback);
	void share(Patch patch, AsyncCallback<Void> callback);
}
