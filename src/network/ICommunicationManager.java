package network;

import crdt.IInitMessageHandler;
import crdt.IMessageHandler;
import crdt.Operation;

public interface ICommunicationManager {
	// broadcast message to other peers
    void broadcastMessage(Operation operation);
    
    // set message handler
    void setIncomingMessageHandler(IMessageHandler messageHandler);
    
    // set message handler when a connection is opened at server side
    
    void setServerChannelActiveHandler(IInitMessageHandler activeHandler);

    // actions when a message received
    void receiveAction(Request r);
    
    // when a new connection is established, the server will send the whole 
    // doc to the new connection (client). This method will be called when
    // the server received a connection. The specific action should be defined
    // in the activeHandler.
    void serverChannelActiveAction(Request r);
    
    public void toClients(String s);
    public void toServer(String s);

}
