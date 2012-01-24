package alma.logoot.logootengine;

/**
 * 
 * @author R5A
 *
 */
public interface IOperation {

	/**
	 * 
	 * @return true si l'operation est une insertion
	 */
	abstract public boolean isIns();
	
	/**
	 * 
	 * @return true si l'operation est une insertion
	 */
	abstract public boolean isDel();
	
}
