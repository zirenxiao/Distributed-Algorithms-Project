package crdt;

public interface IMessageHandler {
    void handle(Operation operation);
}
