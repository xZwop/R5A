package alma.logoot.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

import alma.logoot.logootengine.IOperation;

import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NetworkServiceImpl extends RemoteServiceServlet implements
		NetworkService {

	private class Receiver extends Thread {
		TamponNPlaces buf;
		int port;
		public Receiver (TamponNPlaces buffer,int port) {
			buf=buffer;
			this.port = port;
		}
		
		public void run() {
			try {
				ServerSocket welcomeSocket = new ServerSocket(port);
				Socket connectionSocket = welcomeSocket.accept();
				while (true) {
					
					BufferedReader inFromClient = new BufferedReader(
							new InputStreamReader(
									connectionSocket.getInputStream()));
					
					
					inFromClient.readLine();
				
				}
			} catch (Exception e) {

			}
		}
	}

	// ip
	// private static final String SERVERADDR = "172.16.134.143";
	// private static final String SERVERADDR = "localhost";
	private static final String SERVERADDR = "localhost";

	private static final int PORTREGISTER = 9991;
	private static final int PORTSEND = 9992;
	private static final String PORTCLIENT = "9990";

	private static final long serialVersionUID = 1L;
	private boolean register = false;

	@Override
	public void send(String o) {
		System.out.println("NetworkServiceImple.send");
		if (!register)
			register();
		// TODO : Send to Server by Ip
		try {
			// Socket scClient = new Socket(SERVERADDR, PORTSEND);
			// ObjectOutputStream output = new
			// ObjectOutputStream(scClient.getOutputStream());
			// output.writeObject(o);
			// scClient.close();
			Gson gson = new Gson();
			String sentence = gson.toJson(o);
			Socket clientSocket = new Socket(SERVERADDR, PORTSEND);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			outToServer.write(sentence.getBytes());
			clientSocket.close();
			register = true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	public String waitForChange() {
		System.out.println("NetworkServiceImple.waitForChange");
		if (!register)
			register();
		try {
			Socket scClient = new Socket(SERVERADDR, PORTSEND);
			ObjectInputStream input = new ObjectInputStream(
					scClient.getInputStream());
			String o = (String) input
					.readObject();
			scClient.close();
			return o;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	private void register() {
		System.out.println("NetworkServiceImple.register");
		try {
			String sentence;
			String modifiedSentence;
			Socket clientSocket = new Socket(SERVERADDR, PORTREGISTER);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
			sentence = PORTCLIENT;
			outToServer.write(sentence.getBytes());
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
			clientSocket.close();
			register = true;
			System.out.println("Fin de l'enregistrement client.");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
