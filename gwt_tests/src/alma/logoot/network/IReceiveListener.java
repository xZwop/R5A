package alma.logoot.network;

/**
 * The {@link IReceiveListener} interface specifies the method the
 * {@link INetwork} will use at each received informations.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public interface IReceiveListener {
  /**
   * Notify the user who registered the listener.
   * 
   * At each {@link INetwork} received new information, this method is call with
   * received information.
   * 
   * @param object
   *          Received object.
   */
	void receive(String object);
}

