package alma.logoot.client;

import alma.logoot.shared.OpInsert;
import alma.logoot.shared.Operation;
import alma.logoot.shared.Patch;

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
