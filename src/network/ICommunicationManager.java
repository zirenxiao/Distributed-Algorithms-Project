package network;

import crdt.IMessageHandler;
import crdt.Operation;

public interface ICommunicationManager {
    void broadcastMessage(Operation operation);
    void handleIncomingMessage(IMessageHandler messageHandler);
}
