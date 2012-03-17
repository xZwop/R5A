package alma.logoot.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import alma.logoot.network.p2p.P2PLayer;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NetworkServiceImpl extends RemoteServiceServlet implements
    NetworkService {

  private static final String SERVERADDR = "127.0.0.1";
  private static int id = 0;
  static final int PORTSEND = 9992;
  public static final int PORTCLIENT = 9990;

  private static final long serialVersionUID = 1L;
  private P2PLayer p2p = P2PLayer.getInstance();

  @Override
  public void send(String message) {
    System.out.println("NetworkServiceImple.send: " + message);

    try {
      sendToLocalUser(message);
      sendToP2PUser(message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void sendToP2PUser(String message) {
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

  public int register() {
    return Math.abs(p2p.getPeerID() * 1000 + ++id);
  }
}
