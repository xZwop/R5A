package alma.logoot.logootengine;

import java.io.Serializable;

/**
 * 
 * @author R5A
 *
 */
public interface IOperation extends Serializable {

	/**
	 * @return true if the operation is an insert, else false.
	 */
	abstract public boolean isIns();
	
	/**
	 * @return true if the operation is a delete, else false.
	 */
	abstract public boolean isDel();

}
