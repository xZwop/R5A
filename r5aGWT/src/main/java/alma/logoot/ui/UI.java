package alma.logoot.ui;

import java.util.HashSet;
import java.util.Set;

import alma.logoot.ui.pasteventtextarea.PasteEventTextArea;
import alma.logoot.ui.pasteventtextarea.PasteHandler;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The concrete {@link IUI} for gwt representation.
 * 
 * The concrete {@link IUI} offer hook to get new text entered and change
 * printed text. {@link UI} use given {@link IChangeListener} to do special
 * stuff on new text enter. In order to place the UI, a "textAreaContainer" is
 * needed in gwt {@link RootPanel}.
 * 
 * @author Adrien Bougouin adrien.bourgoin{at}gmail{dot}com
 * @author Adrien Drouet drizz764{at}gmail{dot}com
 * @author Alban Ménager alban.menager{at}gmail{dot}com
 * @author Alexandre Prenza prenza.a{at}gmail{dot}com
 * @author Ronan-Alexandre Cherrueau ronancherrueau{at}gmail{dot}com
 */
public class UI implements IUI {

  /**
   * {@link IChangeListener} container.
   */
  private Set<IChangeListener> changeListeners;

  /**
   * Text Tool.
   */
  private PasteEventTextArea textArea;

  /**
   * Timer to call change listeners after past event. Using timer cause the
   * paste event is catch before text set in TextArea. So we call$
   * UI.callChangeListeners in the future.
   * 
   * @see http://www.sencha.com/forum/showthread.php?113373-paste-event
   */
  private Timer afterPasteCallChangeListeners = new Timer() {
    @Override
    public void run() {
      callChangeListeners();
    }
  };
  
  /**
   * Laps time before call UI.callChangeListeners in ms.
   */
  public static final int CALL_TIMER = 200;

  /**
   * Default {@link UI} constructor.
   */
  public UI() {
    this.changeListeners = new HashSet<IChangeListener>();
    this.textArea = new PasteEventTextArea();

    setHandlers();

    // Set the TextArea on GWT
    RootPanel header = RootPanel.get("header");
    RootPanel content = RootPanel.get("content");

    int contentHeight = RootPanel.getBodyElement().getOffsetHeight()
        - header.getOffsetHeight();

    content.setHeight(contentHeight + "px");
    content.add(this.textArea);
  }

  @Override
  public void addChangeListener(IChangeListener listener) {
    this.changeListeners.add(listener);
  }

  @Override
  public void setText(String text) {
    int curPos = textArea.getCursorPos();
    textArea.setText(text);
    textArea.setCursorPos(curPos);
  }

  /**
   * Declare handler on textArea to call the callChangeListeners methods on each
   * text change.
   */
  private void setHandlers() {
    // Key Up Handler
    textArea.addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        System.out.println("Ready to call Change Listener from key:");
        callChangeListeners();
      }
    });

    // Change Handler
    textArea.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        System.out.println("Ready to call Change Listener from change:");
        callChangeListeners();
      }
    });

    // Paste Handler
    textArea.addPasteHandler(new PasteHandler() {
      @Override
      public void onPaste(Event event) {
        System.out.println("Ready to call Change Listener from paste:");
        afterPasteCallChangeListeners.schedule(CALL_TIMER);
      }
    });
  }

  /**
   * Call the change method of each {@link IChangeListener} to execute specific
   * given specific stuff at text change.
   */
  private void callChangeListeners() {
    String text = this.textArea.getText();

    for (IChangeListener changeListener : this.changeListeners) {
      System.out.println("\t\tCall Change Listener " + changeListener
          + " with text " + text);
      changeListener.change(text);
    }
  }
}
