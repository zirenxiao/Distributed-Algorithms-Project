package server;

import crdt.Operation;
import main.Main;
import network.OperationRequest;
import network.RequestHandler;

public class Handler {

	public Handler() {
		// TODO Auto-generated constructor stub
	}
	
	public void handle(String msg, Connection c) {
//		System.out.println("sv:"+msg);
    	
		RequestHandler rh = new RequestHandler(msg);
		
		
		if (!rh.isNotRedirect()) {
//			for (Channel c: channels) {
//	    		// first send to all connected clients (children)
//	    		if (c != ctx.channel()) {
//	    			// make sure don't send back to the client
//	    			c.writeAndFlush(msg);
//	    		}
//	    	}
			// Send to clients except itself
			Main.getServer().broadcastToClients(msg, c);
			
	    	// then send to the server (parent)
	    	Main.getCommunicationManager().toServer(msg);
		}
    	
    	
    	// run the action
    	rh.doAction(c);
	}
	
	public void initHandle(Connection c) {
		for (Operation o:Main.getCRDT().getDoc()) {
    		OperationRequest r = new OperationRequest(o);
        	RequestHandler rh = new RequestHandler(r.toJSONString());
        	c.writeMsg(rh.getMsg());
    	}
	}

}
