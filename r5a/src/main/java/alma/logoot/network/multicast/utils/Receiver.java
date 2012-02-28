package alma.logoot.network.multicast.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.URI;
import java.net.URISyntaxException;

import alma.logoot.network.multicast.interfaces.OnReceiveHandler;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;
import net.jxta.pipe.PipeService;
import net.jxta.platform.NetworkManager;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.socket.JxtaMulticastSocket;

public class Receiver extends Thread {

  private boolean stop;
  private NetworkManager manager;
  private OnReceiveHandler handler;

  /*
   * According to the JXTA Specification 2.0, the 0-15 octets represent the
   * group UUID, and 16-31 octets represent peer UUID. 31-62 octets are not used
   * and the 63th octet is the ID type (03 for peer ID).
   */
  public final static String SOCKETIDSTR = "urn:jxta:uuid-59616261646162614E5047205032503336E8699B341345449B7EE54AC8662A8604";

  public Receiver(NetworkManager manager, OnReceiveHandler handler) {
    this.stop = false;
    this.manager = manager;
    this.handler = handler;
  }

  /**
   * Creates a Socket Pipe Advertisement with the pre-defined pipe ID
   * 
   * @return a socket PipeAdvertisement
   */
  public static PipeAdvertisement getSocketAdvertisement() {
    PipeID socketID = null;

    try {
      socketID = (PipeID) IDFactory.fromURI(new URI(SOCKETIDSTR));
    } catch (URISyntaxException use) {
      use.printStackTrace();
    }
    PipeAdvertisement advertisement = (PipeAdvertisement) AdvertisementFactory
        .newAdvertisement(PipeAdvertisement.getAdvertisementType());

    advertisement.setPipeID(socketID);
    // set to type to propagate
    advertisement.setType(PipeService.PropagateType);
    advertisement.setName("Socket Logoot");
    return advertisement;
  }

  @Override
  public void run() {
    JxtaMulticastSocket mcastSocket = null;
    try {
      mcastSocket = new JxtaMulticastSocket(manager.getNetPeerGroup(),
          getSocketAdvertisement());
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    byte[] buffer = new byte[16384];

    receive(mcastSocket, buffer);

  }

  /**
   * Receive method. When a message is received, the 'execute' method of the
   * handler is called with the received message.
   * 
   * @param mcastSocket
   *          the socket
   * @param buffer
   *          the buffer
   */
  private void receive(JxtaMulticastSocket mcastSocket, byte[] buffer) {
    while (!stop) {

      try {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        // wait for a datagram.
        mcastSocket.receive(packet);
        String data = new String(packet.getData(), 0, packet.getLength());
        this.handler.execute(data);

        // System.out.println("Received data from :" + packet.getAddress());
        // System.out.println(data);
      } catch (IOException e) {
        receive(mcastSocket, buffer);
      }
    }
  }

  /**
   * Stop the receiver loop.
   */
  public void stopReceiver() {
    this.stop = true;
  }
}
