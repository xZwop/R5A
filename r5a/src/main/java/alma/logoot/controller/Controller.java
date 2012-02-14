package alma.logoot.controller;

import alma.logoot.logootengine.FactoryLogootEngine;
import alma.logoot.logootengine.ILogootEngine;
import alma.logoot.network.FactoryNetwork;
import alma.logoot.network.INetwork;
import alma.logoot.network.IReceiveListener;
import alma.logoot.ui.FactoryUI;
import alma.logoot.ui.IChangeListener;
import alma.logoot.ui.IUI;

import com.google.gwt.core.client.EntryPoint;

/**
 * The {@link IUI} Factory.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class Controller implements EntryPoint, IChangeListener,
    IReceiveListener {

  private IUI ui = FactoryUI.getInstance();
  private INetwork network = FactoryNetwork.getInstance();
  private ILogootEngine logootEngine = FactoryLogootEngine.getInstance();

  @Override
  public void onModuleLoad() {
    // On initialise la vue.
    ui.addChangeListener(this);

    // On initialise le network.
    network.addReceiverListener(this);
  }

  @Override
  public void change(String text) {
    // TODO on set l'id du client a chaque saisie de caractere, ce qu'il ne
    // faudrais pas obligatoirement faire, il faut vériofier que le client
    // n'a ps l'id -1 sinon il ne faudrait pas remplacer.
    logootEngine.setId(network.getId());
    String patch = logootEngine.generatePatch(text);
    if (!patch.equals("[]"))
      network.send(patch);
  }

  @Override
  public void receive(String o) {
    System.out.println("Objet recu :" + o);
    System.out.println("Objet de type : " + o.getClass().getName());
    try {
      String text = logootEngine.deliver(o);
      if (text != null)
        ui.setText(text);
    } catch (ClassCastException e) {
      System.err.println("Error, failed to cast the received object into a "
          + "collection of operations");
    }
  }
}
