package alma.logoot.network.multicast;

import java.io.File;

import net.jxta.platform.NetworkManager;
import alma.logoot.network.multicast.interfaces.NetworkP2P;
import alma.logoot.network.multicast.interfaces.OnReceiveHandler;
import alma.logoot.network.multicast.utils.Receiver;
import alma.logoot.network.multicast.utils.Sender;

public class P2PLayer implements NetworkP2P {
  private static P2PLayer instance = null;
  private String peerID = "";
  private NetworkManager manager = null;
  private Sender sender = null;
  private Receiver receiver = null;

  private P2PLayer() {
  }

  public static P2PLayer getInstance() {
    if (instance == null) {
      instance = new P2PLayer();
    }

    return instance;
  }

  @Override
  public int connect() {
    if (peerID.equals("")) {
      this.manager = getManager();
      this.sender = new Sender(this.manager);
    }

    return sender.getPeerID();
  }

  /**
   * Create a new network manager if doesn't exist.
   * 
   * @return the network manager.
   */
  private NetworkManager getManager() {
    if (this.manager == null) {
      try {
        this.manager = new net.jxta.platform.NetworkManager(
            NetworkManager.ConfigMode.ADHOC, "JxtaMulticastSocketServer",
            new File(new File(".cache"), "JxtaMulticastSocketServer").toURI());
        this.manager.startNetwork();
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }

    return manager;
  }

  @Override
  public void sendMessage(String message) {
    this.sender.sendMessage(message);
  }

  @Override
  public void stopConnection() {
    this.receiver.stopReceiver();
    this.manager.stopNetwork();
  }

  @Override
  public void setOnReceiveHandler(OnReceiveHandler handler) {
    this.receiver = new Receiver(this.manager, handler);
    this.receiver.start();
  }
}
