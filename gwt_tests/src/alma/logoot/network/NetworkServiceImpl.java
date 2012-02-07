package alma.logoot.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NetworkServiceImpl extends RemoteServiceServlet implements
		NetworkService {

	 private static final String SERVERADDR = "172.16.134.143";
//	 private static final String SERVERADDR = "88.163.77.47";
//	private static final String SERVERADDR = "http://driz.dyndns.org";

	private static final int PORTREGISTER = 9991;
	private static final int PORTSEND = 9992;
	public static final int PORTCLIENT = 9990;

	private static final long serialVersionUID = 1L;

	@Override
	public void send(String o) {
		System.out.println("NetworkServiceImple.send");
		System.out.println(o);
//		if (!register)
//			register();
		// TODO : Send to Server by Ip
		try {
			
			Socket clientSocket = new Socket(SERVERADDR, PORTSEND);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			outToServer.write(o.getBytes());
			clientSocket.close();
			System.out.println("NetworkServiceImple.send fin");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("NetworkServiceImple.send fin");
		}
	}

	public String register() {
		System.out.println("NetworkServiceImple.register");
		try {
			String sentence;
			String modifiedSentence;
			Socket clientSocket = new Socket(SERVERADDR, PORTREGISTER);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			sentence = PORTCLIENT+"";
			outToServer.write(sentence.getBytes());
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
			clientSocket.close();
			System.out.println("Fin de l'enregistrement client.");
			return modifiedSentence;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

}
