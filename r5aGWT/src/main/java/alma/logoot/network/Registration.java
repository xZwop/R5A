package alma.logoot.network;

import java.io.Serializable;

/**
 * Registration object.
 * 
 * @author Adrien Bougouin adrien.bougoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class Registration implements Serializable {
  private static final long serialVersionUID = -1336097161413827965L;
  private long id;
  private String object;

  public Registration() {
  }

  public Registration(long id, String object) {
    this.id = id;
    this.object = object;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getObject() {
    return object;
  }

  public void setObject(String object) {
    this.object = object;
  }
  
  public String toString() {
    return "Registration Object [ " + id + ": " + object + " ]";
  }
}
