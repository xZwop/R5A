package alma.logoot.ui;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

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
  private TextArea textArea;

  /**
   * Default {@link UI} constructor.
   */
  public UI() {
    this.changeListeners = new HashSet<IChangeListener>();
    this.textArea = new TextArea();

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
    System.out.println("UI.R5A.setText : " + text);
    textArea.setText(text);
  }

  /**
   * Declare handler on textArea to call the callChangeListeners methods on each
   * text change.
   */
  private void setHandlers() {
    this.textArea.addKeyUpHandler(new KeyUpHandler() {
      @Override
      public void onKeyUp(KeyUpEvent event) {
        callChangeListeners();
      }
    });

    this.textArea.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        callChangeListeners();
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
      changeListener.change(text);
    }
  }
}
