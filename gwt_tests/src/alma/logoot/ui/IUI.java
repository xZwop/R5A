package alma.logoot.ui;



/**
 * 
 * @author R5A
 * 
 */
public interface IUI {

	/**
	 * Allow an ui to listen to a controller. Whenever the controller got
	 * something, the ui will be notified.
	 * 
	 * @param listener
	 *            the listener.
	 */
	void addChangeListener(IChangeListener listener);

	/**
	 * Set the value of the text to the new text
	 * 
	 * @param text
	 *            the new text.
	 */
	void setText(String text);
}
