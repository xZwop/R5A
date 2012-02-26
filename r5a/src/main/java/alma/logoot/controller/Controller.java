package alma.logoot.controller;

import alma.logoot.logootengine.FactoryLogootEngine;
import alma.logoot.logootengine.ILogootEngine;
import alma.logoot.network.FactoryNetwork;
import alma.logoot.network.INetwork;
import alma.logoot.network.IAfterConnectionListener;
import alma.logoot.network.IReceiveListener;
import alma.logoot.ui.FactoryUI;
import alma.logoot.ui.IChangeListener;
import alma.logoot.ui.IUI;

import com.google.gwt.core.client.EntryPoint;

/**
 * The Controller, main entry point program.
 * 
 * Controller is in charge of instantiate and manage UI, Network and Logoot
 * components. To be reactive at each text change in the UI, the Controller
 * implements {@link IChangeListener}. And to be reactive at each new patch
 * send over network, implements {@link IReceiveListener}.
 * 
 * The flow is the following :
 *  <ul>
 *    <li>In change method:</li>
 *    <li><ol>
 *      <li>Uses {@link ILogootEngine} to generate a patch;</li>
 *      <li>Say patch is generated from local changes;</li>
 *      <li>Send patch to receive method</li>
 *    </ol></li>
 *    <li>In receive method:</li>
 *    <li><ol>
 *      <li>Update model from {@link ILogootEngine} with patch</li>
 *      <li>If this local generated patches, send it over network</li>
 *      <li>Else, update {@link IUI} with new text</li>
 *    </ol></li>
 *  </ul>
 *    
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class Controller implements EntryPoint, IChangeListener,
    IReceiveListener, IAfterConnectionListener {

  private IUI ui = FactoryUI.getInstance();
  private INetwork network = FactoryNetwork.getInstance();
  private ILogootEngine logootEngine = FactoryLogootEngine.getInstance();
  private boolean isLocalGeneratedPatch = false;

  @Override
  public void onModuleLoad() {
    // On initialise la vue.
    ui.addChangeListener(this);

    // On initialise le network et on se connecte.
    network.addReceiverListener(this);
    network.addAfterConectionListener(this);
    network.connect();
  }

  @Override
  public void change(String text) {
    String patch = logootEngine.generatePatch(text);
    if (!patch.equals("[]"))
      network.send(patch);
  }

  @Override
  public void afterConnect(int id) {
    logootEngine.setId(id);
  }

  // L'objet passé en paramètre est le contexte pour reconstruire l'idTable
  // du Logoot.
  @Override
  public void afterConnect(int id, Object context) {
    logootEngine.setId(id);
    String c = (String) context;
    try {
      String text = logootEngine.deliver(c);
      if (text != null)
        ui.setText(text);
    } catch (ClassCastException e) {
      System.err.println("Error, failed to cast the received object into a "
          + "collection of operations");
    }
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

