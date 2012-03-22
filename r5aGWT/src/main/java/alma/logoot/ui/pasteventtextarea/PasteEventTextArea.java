package alma.logoot.ui.pasteventtextarea;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextArea;

/**
 * The {@link PasteEventTextArea} extends {@link TextArea} to add new binding
 * event. With {@link PasteEventTextArea} you can add a paste handler which is
 * reach at each paste in the {@link TextArea}.
 * 
 * @see http://stackoverflow.com/questions/9669425/paste-event-on-gwt
 * 
 * @author Adrien Bougouin adrien.bougoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class PasteEventTextArea extends TextArea {

  /**
   * List of {@link PasteHandler} register for the {@link PasteEventTextArea}.
   */
  private List<PasteHandler> pasteHandlers;

  /**
   * Constructor of {@link PasteEventTextArea}.
   */
  public PasteEventTextArea() {
    super();
    sinkEvents(Event.ONPASTE);

    this.pasteHandlers = new LinkedList<PasteHandler>();
  }

  /**
   * Add a new {@link PasteHandler} reach at each paste on TextArea.
   * 
   * @param handler
   *          The {@link PasteHandler} to add.
   */
  public void addPasteHandler(PasteHandler handler) {
    this.pasteHandlers.add(handler);
  }

  @Override
  public void onBrowserEvent(Event event) {
    // System.out.println("Event catching: " + event.getType());
    super.onBrowserEvent(event);
    switch (event.getTypeInt()) {
    case Event.ONPASTE:
      for (PasteHandler pasteHandler : this.pasteHandlers) {
        pasteHandler.onPaste(event);
      }
      break;
    }
  }
}
