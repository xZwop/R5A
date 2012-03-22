package alma.logoot.logootengine;

import java.io.Serializable;

/**
 * The {@link IOperation} interface specifies the type of Logoot Operation.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public interface IOperation extends Serializable {
  /**
   * Test if the operation is an insertion.
   * 
   * @return <code>true</code> if it is an insertion, <code>false</code> else.
   */
  public boolean isIns();

  /**
   * Test if the operation is a delete.
   * 
   * @return <code>true</code> if it is a delete, <code>false</code>else.
   */
  public boolean isDel();
}
