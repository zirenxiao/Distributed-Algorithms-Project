/**
 * 
 */
package network;

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
	private IMessageHandler serverInitHandler = null;
	private MessageQueue mq;

	public NetworkManager() {
		mq = new MessageQueue();
	}

	@Override
	public void broadcastMessage(Operation operation) {
		// to server
		Main.getClient().sentToServer(Settings.operationToString(operation));
		
		// to clients
		Main.getServer().broadcastToClients(Settings.operationToString(operation));

	}

	@Override
	public void setIncomingMessageHandler(IMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	public void receiveAction(Operation op) {
		// add operation to the message queue
		if (messageHandler == null) {
			return;
		}
		mq.add(op, this.messageHandler);
		
	}

	@Override
	public void setServerChannelActiveHandler(IMessageHandler messageHandler) {
		// TODO Auto-generated method stub
		this.serverInitHandler = messageHandler;
		
	}

	@Override
	public void serverChannelActiveAction() {
		// TODO Auto-generated method stub
		if (serverInitHandler == null) {
			return;
		}
		serverInitHandler.handle(null);
		
	}

	@Override
	public void isConsistent(String in) {
		// TODO Auto-generated method stub
		
	}

}
