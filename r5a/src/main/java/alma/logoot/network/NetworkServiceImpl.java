package alma.logoot.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import alma.logoot.network.multicast.P2PLayer;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class NetworkServiceImpl extends RemoteServiceServlet implements
    NetworkService {
  public static final String SERVERADDR = "127.0.0.1";
  public static final int PORTSEND = 9992;
  public static final int PORTCLIENT = 9990;

  private static final long serialVersionUID = 1L;
  private static int id = -1;
  private P2PLayer p2p = P2PLayer.getInstance();


  public int register() {
    id = p2p.connect();
    System.out.println("NetworkServiceImple.register: " + id);

    return id;
  }
  
  @Override
  public void send(String object) {
    System.out.println("NetworkServiceImple.send: " + object);
    try {
      sendToP2PUser(object);
      sendToDirectConnectedUser(object);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void sendToP2PUser(String object) {
    p2p.sendMessage(object);
  }

  private void sendToDirectConnectedUser(String o) throws UnknownHostException,
      IOException {
    Socket clientSocket = new Socket(SERVERADDR, PORTSEND);
    DataOutputStream outToServer = new DataOutputStream(
        clientSocket.getOutputStream());
    outToServer.write(o.getBytes());
    clientSocket.close();
    System.out.println("NetworkServiceImple.send fin");
  }
}
