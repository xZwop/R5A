package multicast;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;

import multicast.interfaces.NetworkP2P;
import multicast.interfaces.OnReceiveHandler;
import multicast.utils.Receiver;
import multicast.utils.Sender;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.NetworkManager;

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
						NetworkManager.ConfigMode.RENDEZVOUS_RELAY,
						"JxtaMulticastSocketServer",
						new File(new File(".cache"),
								"JxtaMulticastSocketServer").toURI());
				net.jxta.peergroup.PeerGroupID pgid = createInfrastructurePeerGroupID("logout", "alma42");
				//this.manager.setInfrastructureID(pgid);
				PeerGroup npg = this.manager.startNetwork();
				System.out.println( "Infrastructure Peer Group ID : " + npg.getPeerGroupID() );
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
	
	public static final net.jxta.peergroup.PeerGroupID createInfrastructurePeerGroupID(String clearTextID, String function){
        byte[] digest = generateHash(clearTextID, function);
        net.jxta.peergroup.PeerGroupID peerGroupID = IDFactory.newPeerGroupID(digest);
        return peerGroupID;
    }
    /**
     * Generates an SHA-1 digest hash of the string: clearTextID+"-"+function or: clearTextID if function was blank.<p>
     *
     * Note that the SHA-1 used only creates a 20 byte hash.<p>
     *
     * @param clearTextID A string that is to be hashed. This can be any string used for hashing or hiding data.
     * @param function A function related to the clearTextID string. This is used to create a hash associated with clearTextID so that it is a uique code.
     *
     * @return array of bytes containing the hash of the string: clearTextID+"-"+function or clearTextID if function was blank. Can return null if SHA-1 does not exist on platform.
     */
    public static final byte[] generateHash(String clearTextID, String function) {
        String id;
        
        if (function == null) {
            id = clearTextID;
        } else {
            id = clearTextID + "-" + function;
        }
        byte[] buffer = id.getBytes();
        
        MessageDigest algorithm = null;
        
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println("Cannot load selected Digest Hash implementation: "+ e);
            return null;
        }
        
        
        // Generate the digest.
        algorithm.reset();
        algorithm.update(buffer);
        
        try{
            byte[] digest1 = algorithm.digest();
            return digest1;
        }catch(Exception de){
        	System.out.println("Failed to creat a digest. " + de);
            return null;
        }
    }


}
