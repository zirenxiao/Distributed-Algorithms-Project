package crdt;

import client.NotePadGUI;
import network.ICommunicationManager;

import java.util.ArrayList;

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

    public void setDoc(ArrayList<Operation> operations) {
        if (operations == null) {
            return;
        }
        for (Operation o: operations) {
            sync(o);
        }
    }

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

    @Override
    public void updateEditor(int changedPosition, OperationType operationType) {
        NotePadGUI.updateEditor(doc.toString(), changedPosition, operationType);
//        System.out.println(doc.toString());
    }
}
