package alma.logoot.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NetworkServiceImpl extends RemoteServiceServlet implements
		NetworkService {

	// ip
	private static final String SERVERADDR = "logootServeur";
	private static final int PORT = 3535;
	private static final long serialVersionUID = 1L;
	private boolean register = false;

	@Override
	public void send(Object o) {
		if (!register)
			register();
		// TODO : Send to Server by Ip
		try {
			Socket scClient = new Socket(SERVERADDR, PORT);
			ObjectOutputStream output = new ObjectOutputStream(scClient.getOutputStream());
			output.writeObject(o);
			scClient.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public Object waitForChange() {
		if (!register)
			register();
		try {
			Socket scClient = new Socket(SERVERADDR, PORT);
			ObjectInputStream input = new ObjectInputStream(scClient.getInputStream());
			Object o = input.readObject();
			scClient.close();
			return o;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private void register() {
		try {
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(SERVERADDR);
			byte[] sendData = "Register".getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, 9876);
			clientSocket.send(sendPacket);
			register = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
