package alma.logoot.ui.gwt;

import alma.logoot.logootengine.OpInsert;
import alma.logoot.logootengine.Operation;
import alma.logoot.logootengine.Patch;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LogootServiceAsync {
	void connect(AsyncCallback<Patch> callback);
	void share(Patch patch, AsyncCallback<Void> callback);
}
