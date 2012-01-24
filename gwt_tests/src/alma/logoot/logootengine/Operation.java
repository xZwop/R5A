package alma.logoot.logootengine;

import java.io.Serializable;

/**
 * An operation consist in adding or removing a caractere.
 * 
 * @author R5A
 * 
 */
public abstract class Operation implements Serializable, IOperation {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The position of the caractere.
	 */
	protected LogootIdContainer position;

	/**
	 * @return true if the operation is an insert, else false.
	 */
	abstract public boolean isIns();

	/**
	 * @return true if the operation is a delete, else false.
	 */
	abstract public boolean isDel();

	/**
	 * @return the position of the caractere to delete or to add.
	 */
	public LogootIdContainer getPosition() {
		return position;
	}
}
