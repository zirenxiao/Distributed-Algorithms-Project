/**
 * 
 */
package network;

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

	public NetworkManager() {
		mq = new MessageQueue();
	}

	@Override
	public void broadcastMessage(Operation operation) {
		
		OperationRequest r = new OperationRequest();
		r.add(operation);
		
		RequestHandler rh = new RequestHandler(r);
		
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
		while (!r.isEmpty()) {
			mq.add(r.getFirst(), this.messageHandler);
		}
	}

	@Override
	public void echoAction(String r, ChannelHandlerContext ctx) {
		// echo the same message
		ctx.writeAndFlush(r);
	}

}
