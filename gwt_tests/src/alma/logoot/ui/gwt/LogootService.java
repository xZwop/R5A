package alma.logoot.ui.gwt;

import alma.logoot.logootengine.OpInsert;
import alma.logoot.logootengine.Operation;
import alma.logoot.logootengine.Patch;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("logoot")
public interface LogootService extends RemoteService{
	Patch connect();
	void share(Patch patch);
}
