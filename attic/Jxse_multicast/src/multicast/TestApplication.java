package multicast;

import multicast.handlers.SimpleHandler;

public class TestApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// New Instance of the connection.
		Peer2PeerConnection p2p = new Peer2PeerConnection();
		// Connection with the ID in return.
		String uniqueID = p2p.connect(new SimpleHandler());
		System.out.println("ID: " + uniqueID);
		// Sending a message to all the p2p network.
		p2p.sendMessage("TOTO");
		
		// Sleep before stoping (to receive the data).
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Stop the connection.
		p2p.stopConnection();
	}

}
