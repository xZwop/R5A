package alma.logoot.logootengine;

import java.util.Collection;


/**
 * 
 * @author R5A
 * 
 */
public interface ILogootEngine {

	/**
	 * Generate a collection of operations to pass from a old text, to a new
	 * one.
	 * 
	 * @param text
	 *            the new text to compare to an old one.
	 * @return the list of operations to pass from the old text to the new one.
	 */
	Collection<IOperation> generatePatch(String text);

	/**
	 * From a list of operations, update the table and the text.
	 * 
	 * @param patch
	 *            a list of operation.
	 * @return the new text after the application of the operations.
	 */
	String deliver(Collection<IOperation> patch);

}
