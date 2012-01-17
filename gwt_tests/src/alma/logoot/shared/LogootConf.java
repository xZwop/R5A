package alma.logoot.shared;

/**
 * Logoot Configuration.
 * 
 * @author Driz
 * 
 */
public class LogootConf {
	/**
	 * Maximal value of an identifier : <BASE ,0 ,0 >
	 */
	public static final int BASE = 100;
	/**
	 * Maximal value between two successive identifiers.
	 */
	public static final int BOUNDARY = 5;
	/**
	 * Enable the boundary mode instead of the random mode.
	 */
	public static final boolean USEBOUNDARY = true;
}
