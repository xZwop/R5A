package alma.logoot.network;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Network implements INetwork {
  public static Network instance = null;
  private IReceiveListener receiveListener;
  private IAfterConnectionListener afterConnectionListener;
  private NetworkServiceAsync service = GWT.create(NetworkService.class);

  public Network() {
    instance = this;
    initReceive();
  }

  @Override
  public void send(String o) {
    System.out.println(o);
    service.send(o, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable caught) {
        System.out.println("Network.send.onFailure");
        caught.printStackTrace();
      }

      @Override
      public void onSuccess(Void result) {
        System.out.println("Network.send.onSuccess");
      }
    });
  }

  @Override
  public void connect() {
    service.register(new AsyncCallback<Registration>() {
      @Override
      public void onFailure(Throwable caught) {
        System.err.println("Network : Erreur enregistrement au serveur.");
        caught.printStackTrace();
      }

      @Override
      public void onSuccess(Registration registration) {
        System.out.println("Network : Enregistrement au serveur: "
            + registration);

        Network.this.afterConnectionListener.afterConnect(registration.getId(),
            registration.getObject());
      }
    });
  }

  @Override
  public void addReceiverListener(IReceiveListener listener) {
    this.receiveListener = listener;
  }

  @Override
  public void addAfterConectionListener(IAfterConnectionListener listener) {
    this.afterConnectionListener = listener;
  }

  public native void initReceive() /*-{
    var source = new EventSource('getData');
    
    source.onmessage = function(event) {
      try {
      var text = event.data.replace(/<br>/g, '\n');
      var me = @alma.logoot.network.Network::instance;
      me.@alma.logoot.network.Network::receive(Ljava/lang/String;)(text);
      } catch(err) {
        console.log(err);
      }
    };
  }-*/;

  public void receive(String text) {
    System.out.println("Network reception des donnees.." + text);
    receiveListener.receive(text);
  }
}
