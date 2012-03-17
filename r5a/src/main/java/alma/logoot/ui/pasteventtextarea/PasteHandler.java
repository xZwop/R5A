package alma.logoot.ui.pasteventtextarea;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.Event;

/**
 * Handler interface for Paste event in {@link PastEventTextArea}.
 * 
 * @author Adrien Bougouin adrien.bougoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public interface PasteHandler extends EventHandler {

  /**
   * Called when Event.ONPASTE is fired on {@link PastEventTextArea}.
   * 
   * @param event
   *          The Event.ONPASTE that was fired.
   */
  public void onPaste(Event event);
}
