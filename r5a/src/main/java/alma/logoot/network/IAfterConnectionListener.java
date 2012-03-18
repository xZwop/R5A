package alma.logoot.network;

/**
 * The {@link IAfterConnectionListener} interface specifies the method the
 * {@link INetwork} will after a new connection on network.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public interface IAfterConnectionListener {
  /**
   * Call after success connection on network.
   * 
   * After {@link INetwork} succed connection, this method is call with unique
   * id of current host on network.
   * 
   * @param result
   *          Unique host identifier on network.
   */
  public void afterConnect(long result);

  /**
   * Call after success connection on network.
   * 
   * After {@link INetwork} succed connection, this method is call with unique
   * id of current host on network.
   * 
   * @param id
   *          Unique host identifier on network.
   * @param object
   *          Extra object information.
   */
  public void afterConnect(long id, Object object);
}
