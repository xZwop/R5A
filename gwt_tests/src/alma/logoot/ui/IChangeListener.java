package alma.logoot.ui;

/**
 * 
 * @author R5A
 * 
 */
public interface IChangeListener {

	/**
	 * Called when something have to be send to the controller.
	 * 
	 * @param text
	 *            the changed text.
	 */
	void change(String text);
	
}
