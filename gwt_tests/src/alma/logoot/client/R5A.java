package alma.logoot.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class R5A implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		TextArea textArea = new TextArea();
		final TextArea log=new TextArea();
		RootPanel.get("textAreaContainer").add(textArea);
		RootPanel.get("logContainer").add(log);
		textArea.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				// Création identifiantLogoot
				// Send	
				log.setText(event.getNativeKeyCode()+"-"+((char)event.getNativeKeyCode())+"\n"+log.getText());		
			}
		});
	}
		
		
}
