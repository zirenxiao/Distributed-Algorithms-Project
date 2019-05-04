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
	private IMessageHandler messageHandler;
	private IMessageHandler initHandler;
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
	public void setChannelActiveHandler(IMessageHandler messageHandler) {
		// TODO Auto-generated method stub
		this.initHandler = messageHandler;
		
	}

	@Override
	public void receiveAction(Operation op) {
		// add operation to the message queue
		mq.add(op, this.messageHandler);
		
	}

	@Override
	public void channelActiveAction(Operation op) {
		// TODO Auto-generated method stub
		initHandler.handle(op);
		
	}

}
