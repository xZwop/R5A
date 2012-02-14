package alma.logoot.ui;

/**
 * The {@link IUI} interface specifies the methods the <code>Controller</code>
 * will use to get new text enter and to set update from other users.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public interface IUI {

  /**
   * Allow a user to listen to new text enter on UI.
   * 
   * Whenever new text enter in UI, the {@link IChangeListener} call change
   * method with the new text
   * 
   * @param listener
   *          Listener to call at each new received informations.
   */
  void addChangeListener(IChangeListener listener);

  /**
   * Set UI text value.
   * 
   * @param text
   *          The text to set.
   */
  void setText(String text);
}
