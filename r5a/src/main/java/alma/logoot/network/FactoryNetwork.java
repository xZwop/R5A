package alma.logoot.network;

/**
 * The {@link INetwork} Factory.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class FactoryNetwork {

  private static INetwork instance = null;

  public static INetwork getInstance() {
    if (FactoryNetwork.instance == null)
      FactoryNetwork.instance = new Network();
    return FactoryNetwork.instance;
  }

  public static void setInstance(INetwork instance) {
    FactoryNetwork.instance = instance;
  }
}

