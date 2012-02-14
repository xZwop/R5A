package alma.logoot.network;

import java.io.DataOutputStream;
import java.net.Socket;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NetworkServiceImpl extends RemoteServiceServlet implements
		NetworkService {

	private static final String SERVERADDR = "127.0.0.1";
	private static int id = 0;
	static final int PORTSEND = 9992;
	public static final int PORTCLIENT = 9990;

	private static final long serialVersionUID = 1L;

	@Override
	public void send(String o) {
		System.out.println("NetworkServiceImple.send");
		System.out.println(o);
		try {
			Socket clientSocket = new Socket(SERVERADDR, PORTSEND);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			outToServer.write(o.getBytes());
			clientSocket.close();
			System.out.println("NetworkServiceImple.send fin");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String register() {
		System.out.println("NetworkServiceImple.register");
		return String.valueOf(++id);
	}
}

