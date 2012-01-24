package alma.logoot.network;

import alma.logoot.logootengine.LogootIdContainer;
import alma.logoot.logootengine.LogootIdentifier;
import alma.logoot.logootengine.OpInsert;
import alma.logoot.logootengine.Operation;
import alma.logoot.logootengine.Patch;
import alma.logoot.ui.gwt.LogootService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LogootServiceImpl extends RemoteServiceServlet implements LogootService {

	private static final long serialVersionUID = 1L;
	Patch patchToSend = null;

	@Override
	public synchronized Patch connect() {
		System.err.println("Server.connect()");
		System.err.println(getServletContext());
		try {
			if (patchToSend == null)
				wait();
			Patch patch = patchToSend;
			patchToSend = null;
			return patch;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		
//		Patch patch = new Patch();
//		LogootIdContainer p = new LogootIdContainer();
//		p.add(new LogootIdentifier(10,1,1));
//		LogootIdContainer q = new LogootIdContainer();
//		q.add(new LogootIdentifier(1,1,2));
//		patch.add(new OpInsert(p, 'C'));
//		patch.add(new OpInsert(q, 'D'));
//		return patch;
		
	}

	@Override
	public synchronized void share(Patch patch) {
		System.err.println("Server.share(Patch)");
		this.patchToSend = patch;
		notify();
	}

}
