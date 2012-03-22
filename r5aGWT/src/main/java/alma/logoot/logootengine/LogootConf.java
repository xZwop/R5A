package alma.logoot.logootengine;

/**
 * Logoot Configuration.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class LogootConf {

  /**
   * Maximal value of an identifier : <BASE ,0 ,0 >
   */
  // public static final int BASE = Integer.MAX_VALUE;
  public static final int BASE = 100;

  /**
   * Number of digit in BASE.
   */
  @SuppressWarnings("unused")
  public static final int DIGITS = (BASE == 0) ? 1 : (int) Math.log10(BASE) + 1;

  /**
   * Maximal value between two successive identifiers.
   */
  public static final int BOUNDARY = 5;

  /**
   * Enable the boundary mode instead of the random mode.
   */
  public static final boolean USEBOUNDARY = true;
}
