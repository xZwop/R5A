package multicast.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import multicast.Peer2PeerConnection;
import multicast.interfaces.NetworkP2P;
import multicast.interfaces.OnReceiveHandler;

public class P2PServlet extends HttpServlet {

	private static final long serialVersionUID = 2118315345950586869L;

	private NetworkP2P p2p;
	private PrintWriter out;
	private SimpleHandler simpleHandler;

	class SimpleHandler implements OnReceiveHandler {
		public Boolean firstExec = true;

		public void execute(String message) {
			P2PServlet.this.out.print("data: {" + message + "}");
			P2PServlet.this.out.flush();
		}
	}

	public void init() {
		this.simpleHandler = new SimpleHandler();
		this.p2p = new Peer2PeerConnection();
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/event-stream;charset=utf-8");
		this.out = response.getWriter();

		if (this.simpleHandler.firstExec) {
			String uniqueID = p2p.connect(this.simpleHandler);
			this.out.print("uniqueID: {" + uniqueID + "}");
			this.out.flush();
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, java.io.IOException {
		processRequest(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, java.io.IOException {
		processRequest(req, resp);
	}

}