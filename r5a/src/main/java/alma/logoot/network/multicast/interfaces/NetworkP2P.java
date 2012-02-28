package alma.logoot.network.multicast.interfaces;

public interface NetworkP2P {

  /**
   * Connection to the P2P network.
   * 
   * @param handler
   *          the handler to know what to do on receive a message from the
   *          network.
   * 
   * @return the Connection ID (packet.getAddress())
   */
  public String connect(OnReceiveHandler handler);

  /**
   * Send the message into the P2P network.
   * 
   * @param message
   *          the message to send.
   */
  public void sendMessage(String message);

  /**
   * Stop the connection on the P2P network.
   */
  public void stopConnection();

}
