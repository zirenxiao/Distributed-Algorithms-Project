package crdt;

import GUI.NotePadGUI;
import network.ICommunicationManager;

import java.util.ArrayList;

/***
 * Class Crdt updates local data structure DocTree
 */
public class Crdt implements ICrdt {
    private DocTree doc;
    private ICommunicationManager comm;

    public Crdt() {
        this(null);
    }

    public Crdt(ICommunicationManager comm) {
        doc = new DocTree();
        this.comm = comm;

        comm.setIncomingMessageHandler(new IMessageHandler() {
            @Override
            public void handle(Operation o) {
                sync(o);
            }
        });
    }

    /**
     * Method update() is used to update DocTree by Editor
     * */
    @Override
    synchronized public void update(OperationType operation, char symbol, int position) {
        INode node = null;
        if (operation == OperationType.insert) {
            node = doc.addSymbol(symbol, position);
        }
        if (operation == OperationType.remove) {
            try {
                node = doc.removeSymbol(symbol, position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        broadcastUpdate(operation, node);
    }

    /***
     * Method broadcastUpdate() send update messages to other peers
     * @param type
     * @param node
     */
    private void broadcastUpdate(OperationType type, INode node) {
        if (node == null) {
            return;
        }
        
        Operation operation = new Operation(type, (DocElement) node.getElement());
        if (comm != null) {
            comm.broadcastMessage(operation);
        }
    }

    /**
     * Method sync() is used to update local DocTree by messages from remote peers
     * @param operation - Operation which was performed
     * */
    @Override
    public void sync(Operation operation) {
        int position = -1;
        if (operation == null) {
            return;
        }
        if (operation.getType() == OperationType.insert) {
            try {
                position = doc.addNode(operation.getElement());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (operation.getType() == OperationType.remove) {
            try {
                position = doc.removeNode(operation.getElement());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateEditor(position, operation.getType());
    }

    /***
     * Method serDoc() can be used to perform operations
     * @param operations - array of Operations
     */
    public void setDoc(ArrayList<Operation> operations) {
        if (operations == null) {
            return;
        }
        for (Operation o: operations) {
            sync(o);
        }
    }

    /***
     * Method getDoc() can be used to get whole document as an array of Operations
     * @return
     */
    @Override
    public ArrayList<Operation> getDoc() {
        ArrayList<Operation> operations = new ArrayList<>();
        for (INode node : doc.getDoc()) {
            OperationType type = OperationType.insert;
            if (node.isRemoved()) {
                type = OperationType.remove;
            }
            operations.add(new Operation(type, (DocElement) node.getElement()));
        }
        return operations;
    }

    /***
     * Method updateEditor() updates user editor area according to underlying DocTree
     * this method is called when operation performed
     * @param changedPosition - position of the changed element in the editor area,
     * @param operationType - type of operation (remove or insert)
     */
    @Override
    public void updateEditor(int changedPosition, OperationType operationType) {
        NotePadGUI.getInstance().updateEditor(doc.toString(), changedPosition, operationType);
    }

    /***
     * Method clear is used to clear document.
     */
    public void clear() {
        doc = new DocTree();
    }
}
