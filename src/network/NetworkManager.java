/**
 * 
 */
package network;

import crdt.IInitMessageHandler;
import crdt.IMessageHandler;
import crdt.Operation;
import main.Main;
import utils.Settings;

/**
 * @author zirenxiao
 *
 */
public class NetworkManager implements ICommunicationManager {
	private IMessageHandler messageHandler = null;
	private IInitMessageHandler serverInitHandler = null;
	private MessageQueue mq;

	public NetworkManager() {
		mq = new MessageQueue();
	}

	@Override
	public void broadcastMessage(Operation operation) {
		Request r = new Request();
		r.add(operation);
		
		// to server
		toClients(Settings.requestToString(r));
		
		// to clients
		toServer(Settings.requestToString(r));

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
	public void receiveAction(Request r) {
		// add operation to the message queue
		if (messageHandler == null) {
			return;
		}
		while (!r.isEmpty()) {
			mq.add(r.getFirst(), this.messageHandler);
		}
		
		
	}

	@Override
	public void setServerChannelActiveHandler(IInitMessageHandler messageHandler) {
		// TODO Auto-generated method stub
		this.serverInitHandler = messageHandler;
		
	}

	@Override
	public void serverChannelActiveAction(Request r) {
		// TODO Auto-generated method stub
		if (serverInitHandler == null) {
			return;
		}
		serverInitHandler.handle(r.getList());
		
	}

}
