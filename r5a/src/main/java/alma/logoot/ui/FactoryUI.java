package alma.logoot.ui;

/**
 * The {@link IUI} Factory.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class FactoryUI {

  private static IUI instance = null;

  public static IUI getInstance() {
    if (FactoryUI.instance == null) {
      FactoryUI.instance = new UI();
    }

    return FactoryUI.instance;
  }

  public static void setInstance(IUI instance) {
    FactoryUI.instance = instance;
  }
}
