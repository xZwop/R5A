package alma.logoot.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import alma.logoot.network.p2p.P2PLayer;

/**
 * Servlet implementation class SendData
 */
public class SendData extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String SERVERADDR = "127.0.0.1";

	/**
	 * Max number of host connected to the node.
	 */
	private static final int MAX_HOST = 100;

	private static int id = 0;
	static final int PORTSEND = 9992;
	public static final int PORTCLIENT = 9990;

	private P2PLayer p2p = P2PLayer.getInstance();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action.equals("send")) {
			send(request, response);
		} else if (action.equals("register")) {
			register(request, response);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
		System.out.println("End servlet: " + getServletInfo());
		super.destroy();
	}

	public void send(HttpServletRequest request, HttpServletResponse response) throws UnknownHostException, IOException {

		String message = request.getParameter("message");

		sendToLocalUser(message);
		sendToP2PUser(message);
	}

	private void sendToP2PUser(String message) {
		System.out.println("Send on P2P network: " + message);
		p2p.sendMessage(message);
	}

	private void sendToLocalUser(String message) throws UnknownHostException,
			IOException {
		Socket clientSocket = new Socket(SERVERADDR, PORTSEND);
		DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());
		outToServer.write(message.getBytes());
		clientSocket.close();
		System.out.println("NetworkServiceImple.send fin");
	}

	public void register(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO: Get context from other client.
		long idRegistration = p2p.getPeerID() * MAX_HOST + ++id;

		response.setContentType("text/json;charset=utf-8;");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print(idRegistration);
	}
}
