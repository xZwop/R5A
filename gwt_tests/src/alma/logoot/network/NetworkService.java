package alma.logoot.network;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("logoot")
public interface NetworkService extends RemoteService{
	void send(Object o);
	Object waitForChange();
}
