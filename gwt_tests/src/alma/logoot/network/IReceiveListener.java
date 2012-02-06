package alma.logoot.network;


/**
 * 
 * @author R5A
 * 
 */
public interface IReceiveListener {

	/**
	 * Notify the user who registered to this listener.
	 * 
	 * @param o
	 *            the object to transmit.
	 */
	void receive(String o);

}
