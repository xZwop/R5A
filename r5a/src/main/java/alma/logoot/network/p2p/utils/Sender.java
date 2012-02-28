package alma.logoot.network.p2p.utils;

import java.io.IOException;
import java.net.DatagramPacket;

import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;
import net.jxta.socket.JxtaMulticastSocket;

public class Sender {

  private JxtaMulticastSocket socket;
  private net.jxta.platform.NetworkManager manager;

  public Sender(NetworkManager manager) {
    this.manager = manager;
    initSender();
  }

  /**
   * Initialize the sender.
   */
  private void initSender() {

    PeerGroup netPeerGroup = manager.getNetPeerGroup();

    try {
      socket = new JxtaMulticastSocket(netPeerGroup,
          Receiver.getSocketAdvertisement());
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  /**
   * Send the data through the network
   * 
   * @param data
   *          the data to send
   */
  public void sendMessage(String data) {

    try {
      DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length());
      socket.send(packet);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Return the PeerID.
   * 
   * @return the peer ID.
   */
  public int getPeerID() {
    return manager.getPeerID().hashCode();
  }
}
