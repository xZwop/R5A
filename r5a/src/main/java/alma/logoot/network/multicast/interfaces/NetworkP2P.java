package alma.logoot.network.multicast.interfaces;

public interface NetworkP2P {

  /**
   * Connection to the P2P network.
   * 
   * @return the Connection ID (packet.getAddress())
   */
  public int connect();

  /**
   * Send the message into the P2P network.
   * 
   * @param message
   *          the message to send.
   */
  public void sendMessage(String message);

  /**
   * Set The handler on OnReceive.
   * 
   * @param handler
   *          the handler to know what to do on receive a message from the
   *          network.
   */
  public void setOnReceiveHandler(OnReceiveHandler handler);

  /**
   * Stop the connection on the P2P network.
   */
  public void stopConnection();

}
