/**
 * 
 */
package network;

import crdt.IMessageHandler;
import crdt.Operation;
import main.Main;

/**
 * @author zirenxiao
 *
 */
public class NetworkManager implements ICommunicationManager{

	@Override
	public void broadcastMessage(Operation operation) {
		// to server
		Main.getClient().sentToServer(operation);
		
		// to clients
		Main.getServer().broadcastToClients(operation);

	}

	@Override
	public void handleIncomingMessage(IMessageHandler messageHandler) {
		// TODO Auto-generated method stub

	}

}
