package network;

import crdt.IMessageHandler;
import crdt.Operation;

public interface ICommunicationManager {
	// broadcast message to other peers
    void broadcastMessage(Operation operation);
    
    // set message handler
    void setIncomingMessageHandler(IMessageHandler messageHandler);
    

    // actions when a message received
    void receiveAction(Request r);
    
    public void toClients(String s);
    public void toServer(String s);

}
