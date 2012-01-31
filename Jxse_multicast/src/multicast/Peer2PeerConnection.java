package multicast;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.jxta.platform.NetworkManager;
import multicast.interfaces.NetworkP2P;
import multicast.interfaces.OnReceiveHandler;
import multicast.utils.Receiver;
import multicast.utils.Sender;

public class Peer2PeerConnection implements NetworkP2P {

	private String peerID = "";
	private NetworkManager manager = null;
	private Sender sender;
	private Receiver receiver;

	@Override
	public String connect(OnReceiveHandler handler) {
		if (peerID.equals("")) {
			this.manager = getManager();
			this.sender = new Sender(this.manager);
			this.receiver = new Receiver(this.manager, handler);
			this.receiver.start();
		}

		String uniqueID = sender.getPeerID();
		try {
			uniqueID += InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return uniqueID;
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

	@Override
	public void sendMessage(String message) {
		this.sender.sendMessage(message);
	}

	@Override
	public void stopConnection() {
		this.receiver.stopReceiver();
		this.manager.stopNetwork();
	}

}
