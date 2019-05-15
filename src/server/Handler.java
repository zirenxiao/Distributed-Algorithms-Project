package server;

import crdt.Operation;
import main.Main;
import network.OperationRequest;
import network.RequestHandler;

/** A server handler to handle received messages from clients
 * @author zirenx
 *
 */
public class Handler {

	/** Handle normal message from a client
	 * @param msg
	 * @param c
	 */
	public void handle(String msg, Connection c) {
//		System.out.println("sv:"+msg);
    	
		RequestHandler rh = new RequestHandler(msg);
		
		
		if (!rh.isNotRedirect()) {
			// Send to clients except itself
			Main.getServer().broadcastToClients(msg, c);
			
	    	// then send to the server (parent)
	    	Main.getCommunicationManager().toServer(msg);
		}
    	
    	
    	// run the action
    	rh.doAction(c);
	}
	
	/** This method should be called when a new connection
	 * is established.
	 * @param c
	 */
	public void initHandle(Connection c) {
		for (Operation o:Main.getCRDT().getDoc()) {
    		OperationRequest r = new OperationRequest(o);
        	RequestHandler rh = new RequestHandler(r.toJSONString());
        	c.writeMsg(rh.getMsg());
    	}
	}

}
