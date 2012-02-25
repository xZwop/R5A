package alma.logoot.network;

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
