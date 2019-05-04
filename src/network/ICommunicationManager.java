package network;

import crdt.IMessageHandler;
import crdt.Operation;

public interface ICommunicationManager {
	// broadcast message to other peers
    void broadcastMessage(Operation operation);
    
    // set message handler
    void setIncomingMessageHandler(IMessageHandler messageHandler);
    
    // set message handler when a connection is opened
    void setServerChannelActiveHandler(IMessageHandler messageHandler);
    
    void setClientChannelActiveHandler(IMessageHandler messageHandler);

    void receiveAction(Operation op);
    
    void serverChannelActiveAction();
    
    void clientChannelActiveAction();
}
