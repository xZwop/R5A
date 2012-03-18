package alma.logoot.logootengine;

/**
 * The {@link ILogootEngine} interface specifies the methods the
 * <code>Controller</code> will use to maintain text.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public interface ILogootEngine {

  /**
   * Generates a collection of operations to pass from a old text, to a new one.
   * 
   * Generates a list of {@link Operation} from the previous text to this one.
   * The list contains all {@link Operation} to pass from the old text to the
   * new.
   * 
   * @param text
   *          The new text to compare to an old one.
   * @return Collection of operations to pass from a old text, to a new one.
   */
  String generatePatch(String text);

  /**
   * Generates a collection of operations to pass from a old text, to a new one.
   * 
   * Generates a list of {@link Operation} from the previous text to this one.
   * The list contains all {@link Operation} to pass from the old text to the
   * new.
   * 
   * @param text
   *          The new text to compare to an old one.
   * @return Collection of operations to pass from a old text, to a new one.
   */
  String deliver(String patch);

  /**
   * Set client id.
   * 
   * @param id
   *          Unique client id over all Logoot client.
   */
  void setId(long id);
}
