package crdt;

import java.util.ArrayList;

public interface IInitMessageHandler {
    void handle(ArrayList<Operation> doc);
}
