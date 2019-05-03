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
	IMessageHandler messageHandler;

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

}
