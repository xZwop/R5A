package alma.logoot.network.p2p.interfaces;

public interface NetworkP2P {


  /**
   * Send the message into the P2P network.
   * 
   * @param message
   *          the message to send.
   */
  public void sendMessage(String message);

  public long getPeerID();
  
  public void addOnReceiveHandler(OnReceiveHandler handler);
}
