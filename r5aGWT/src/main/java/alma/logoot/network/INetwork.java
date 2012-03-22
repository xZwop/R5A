package alma.logoot.network;

/**
 * The {@link INetwork} interface specifies the methods the
 * <code>Controller</code> will use to send and get patch for collaborative
 * work.
 * 
 * @author Adrien Bougouin adrien.bougoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public interface INetwork {
  /**
   * Sends information over network.
   * 
   * @param object
   *          Object to send.
   */
  public void send(String object);

  /**
   * Connect a new user to the network.
   * 
   * Connect a user to the network. You must register with connect before sends
   * informations with <code>INetwork::send()</code>. At end of connexion, the
   * object given in <code>INetwork::addAfterConectionListener()</code> will
   * be call with the unique id of current host on network and an extrat
   * object (if any).
   */
  public void connect();

  /**
   * Allow a user to listen to a network.
   * 
   * Whenever new information is send to this Network contributor, the
   * {@link IReceiveListener} is call to execute specific code.
   * 
   * @param listener
   *          Listener to call at each new received informations.
   */
  public void addReceiverListener(IReceiveListener listener);

  /**
   * Process to do after connection.
   *
   * After connection on server, class given here will be call to execute
   * specific code in asynchronous way.
   *
   * @param listener
   *          Listener to call after connection.
   */
  public void addAfterConectionListener(IAfterConnectionListener listener);
}

