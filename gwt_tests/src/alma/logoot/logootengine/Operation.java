package alma.logoot.logootengine;

import java.io.Serializable;

/**
 * Operation d'insertion ou d'ajout d'un caractere
 * @author driz
 *
 */
public abstract class Operation implements Serializable,IOperation
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * La position concerné
	 */
	protected LogootIdContainer position;
	
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
	
	public LogootIdContainer getPosition(){
		return position;
	}
}
