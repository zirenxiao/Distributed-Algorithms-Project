package crdt;

import java.util.ArrayList;

public interface ICrdt {
    void update(OperationType operation, char symbol, int position);
    void sync(Operation operation);
    ArrayList<Operation> getDoc();

    void updateEditor(int changedPosition, OperationType operationType);

}
