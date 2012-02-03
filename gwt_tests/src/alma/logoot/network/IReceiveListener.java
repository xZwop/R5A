package alma.logoot.network;

import java.util.Collection;

import alma.logoot.logootengine.IOperation;

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
	void receive(Collection<IOperation> o);

}
