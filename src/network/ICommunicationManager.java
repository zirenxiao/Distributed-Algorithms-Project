package network;

import crdt.IMessageHandler;
import crdt.Operation;
import io.netty.channel.ChannelHandlerContext;

public interface ICommunicationManager {
	// broadcast message to other peers
    void broadcastMessage(Operation operation);
    
    // set message handler
    void setIncomingMessageHandler(IMessageHandler messageHandler);
    

    // actions when a message received
    void receiveAction(OperationRequest r);
    void echoAction(String r, ChannelHandlerContext ctx);
    
    public void toClients(String s);
    public void toServer(String s);

}
