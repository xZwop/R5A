package alma.logoot.network.p2p;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.jxta.platform.NetworkManager;
import alma.logoot.network.p2p.interfaces.NetworkP2P;
import alma.logoot.network.p2p.interfaces.OnReceiveHandler;
import alma.logoot.network.p2p.utils.Receiver;
import alma.logoot.network.p2p.utils.Sender;

public class P2PLayer implements NetworkP2P {
	private static P2PLayer instance = null;
	private int peerID = -1;
	private NetworkManager manager = null;
	private Sender sender = null;
	private Receiver receiver = null;

	private P2PLayer() {
		peerID = connect();
	}

	public static P2PLayer getInstance() {
		if (instance == null) {
			instance = new P2PLayer();
		}

		return instance;
	}

	private int connect() {
		this.manager = getManager();
		this.sender = new Sender(this.manager);
		this.receiver = new Receiver(this.manager);

		return sender.getPeerID();
	}

	/**
	 * Create a new network manager if doesn't exist.
	 * 
	 * @return the network manager.
	 */
	private NetworkManager getManager() {
		if (this.manager == null) {
			try {
				this.manager = new net.jxta.platform.NetworkManager(
						NetworkManager.ConfigMode.ADHOC,
						"JxtaMulticastSocketServer",
						new File(new File(".cache"),
								"JxtaMulticastSocketServer").toURI());
				this.manager.startNetwork();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		return manager;
	}

	public void sendMessage(String message) {
		this.sender.sendMessage(message);
	}

	public int getPeerID() {
		return this.peerID;
	}

	public void setOnReceiveHandler(OnReceiveHandler handler) {
		if (this.receiver.isAlive()) {
			this.receiver.stopReceiver();
		}
		
		this.receiver.setHandler(handler);
		this.receiver.start();
	}
}