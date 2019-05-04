package network;

import crdt.IMessageHandler;
import crdt.Operation;

public interface ICommunicationManager {
	// broadcast message to other peers
    void broadcastMessage(Operation operation);
    
    // set message handler
    void setIncomingMessageHandler(IMessageHandler messageHandler);
    
    // set message handler when a connection is opened
    void setChannelActiveHandler(IMessageHandler messageHandler);

    void receiveAction(Operation op);
    
    void channelActiveAction(Operation op);
}
