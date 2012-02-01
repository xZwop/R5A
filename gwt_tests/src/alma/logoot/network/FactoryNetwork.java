package alma.logoot.network;

public class FactoryNetwork {

	private static INetwork instance = null;

	public static INetwork getInstance() {
		if (instance == null)
			instance = new Network();
		return instance;
	}
}
