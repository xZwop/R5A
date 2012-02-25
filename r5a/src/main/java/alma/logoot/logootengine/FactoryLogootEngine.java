package alma.logoot.logootengine;


/**
 * The {@link ILogootEngine} Factory.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class FactoryLogootEngine {
  private static ILogootEngine instance = null;

  public static ILogootEngine getInstance() {
    if (FactoryLogootEngine.instance == null) {
      FactoryLogootEngine.instance = new LogootEngine();
    }

    return FactoryLogootEngine.instance;
  }

  public static void setInstance(ILogootEngine instance) {
    FactoryLogootEngine.instance = instance;
  }
}
