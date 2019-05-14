/**
 * 
 */
package network;

import java.util.ArrayList;

import crdt.IMessageHandler;
import crdt.Operation;
import main.Main;
import server.Connection;

/**
 * @author zirenxiao
 *
 */
public class NetworkManager implements ICommunicationManager {
	private IMessageHandler messageHandler = null;
	private MessageQueue mq;
	private ArrayList<String> toServerList;
	private boolean toServer = false;
	private ArrayList<String> toClientList;
	private boolean toClient = false;

	public NetworkManager() {
		mq = new MessageQueue();
		toServerList = new ArrayList<String>();
		toClientList = new ArrayList<String>();
		
	}

	@Override
	public void broadcastMessage(Operation operation) {
		
		OperationRequest r = new OperationRequest(operation);
		
		RequestHandler rh = new RequestHandler(r.toJSONString());
		
		System.out.println("Sending:"+r.toJSONString());
		
		// to server
		toClients(rh.getMsg());
		
		// to clients
		toServer(rh.getMsg());

	}
	
	@Override
	public void toClients(String s) {
		Main.getServer().broadcastToClients(s);
	}
	
	@Override
	public void toServer(String s) {
		Main.getClient().sentToServer(s);
	}

	@Override
	public void setIncomingMessageHandler(IMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	public void receiveAction(OperationRequest r) {
		// add operation to the message queue
		if (messageHandler == null) {
			return;
		}
		mq.add(r.getOperation(), this.messageHandler);
	}

	@Override
	public void echoAction(String r, Connection c) {
		// echo the same message
//		ctx.writeAndFlush(r + "\r\n");
		c.writeMsg(r);
	}
	

}
