package alma.logoot.ui.gwt;

import alma.logoot.ui.IChangeListener;
import alma.logoot.ui.IUI;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class R5A implements IUI {

	private IChangeListener changeListener;
	private final TextArea textArea = new TextArea();

	/**
	 * This is the init point method.
	 */
	public void init() {

		// Initialization of the textarea.
		RootPanel.get("textAreaContainer").add(textArea);

		// Hookz.
		textArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				onEvent();
			}
		});
		textArea.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				onEvent();
			}
		});
	}

	/**
	 * 
	 */
	private void onEvent() {
		changeListener.change(textArea.getText());
	}

	@Override
	public void addChangeListener(IChangeListener listener) {
		this.changeListener = listener;
	}

	@Override
	public void setText(String text) {
		textArea.setText(text);
	}

}
