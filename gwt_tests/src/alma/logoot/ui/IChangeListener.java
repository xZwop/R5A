package alma.logoot.ui;

/**
 * The {@link IChangeListener} interface specifies the method the {@link IUI}
 * will use at each new text enter.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public interface IChangeListener {

  /**
   * Notify the user who register the listener.
   * 
   * At each new text enter on {@link IUI}, this method is call with the new
   * text
   * 
   * @param text
   *          New text.
   */
  void change(String text);
}

