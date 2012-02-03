package alma.logoot.network;


import java.util.Collection;

import alma.logoot.logootengine.IOperation;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("logoot")
public interface NetworkService extends RemoteService{
	void send(Collection<IOperation> o);
	Collection<IOperation> waitForChange();
}
