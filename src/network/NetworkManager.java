/**
 * 
 */
package network;

import java.util.ArrayList;

import crdt.IMessageHandler;
import crdt.Operation;
import io.netty.channel.ChannelHandlerContext;
import main.Main;

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
		
		// to server
		toClients(rh.getMsg());
		
		// to clients
		toServer(rh.getMsg());

	}
	
	@Override
	public void toClients(String s) {
		toClientList.add(s);
		if (!toClient) {
			excToClient();
		}
	}
	
	@Override
	public void toServer(String s) {
		toServerList.add(s);
		if (!toServer) {
			excToServer();
		}
	}
	
	private void excToServer() {
		toServer = true;
		while (!toServerList.isEmpty()) {
			String s = toServerList.get(0);
			toServerList.remove(0);
			Main.getClient().sentToServer(s);
		}
		toServer = false;
	}
	
	private void excToClient() {
		toClient = true;
		while (!toClientList.isEmpty()) {
			String s = toClientList.get(0);
			toClientList.remove(0);
			Main.getServer().broadcastToClients(s);
		}
		toClient = false;
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
	public void echoAction(String r, ChannelHandlerContext ctx) {
		// echo the same message
		ctx.writeAndFlush(r);
	}

}
