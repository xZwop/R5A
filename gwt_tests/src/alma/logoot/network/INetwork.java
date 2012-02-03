package alma.logoot.network;

import java.util.Collection;

import alma.logoot.logootengine.IOperation;


/**
 * 
 * @author R5A
 * 
 */
public interface INetwork {

	/**
	 * Called when something have to be send to the world.
	 * 
	 * @param o
	 *            the object sent.
	 */
	void send(Collection<IOperation> o);

	/**
	 * Allow a user to connect to the network.
	 * 
	 * @return an unique generated id per client
	 */
	int connect();

	/**
	 * Allow a user to listen to a network. Whenever the network got something,
	 * he will notify all his listeners.
	 * 
	 * @param listener
	 *            the listener.
	 */
	void addReceiverListener(IReceiveListener listener);

}
