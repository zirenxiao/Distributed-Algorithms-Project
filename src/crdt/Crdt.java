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

        comm.handleIncomingMessage(new IMessageHandler() {
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
    public void update(OperationType operation, char symbol, int position) {
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
        if (operation == null) {
            return;
        }
        if (operation.getType() == OperationType.insert) {
            try {
                doc.addNode(operation.getElement());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (operation.getType() == OperationType.remove) {
            try {
                doc.removeNode(operation.getElement());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateEditor();
    }

    @Override
    public void uploadDoc(ArrayList<INode> doc) {

    }

    @Override
    public void updateEditor() {
        NotePadGUI.updateEditor(doc.toString());
//        System.out.println(doc.toString());
    }
}
