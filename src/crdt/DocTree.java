package crdt;

import java.util.ArrayList;


class P {
    int stop;
    int current;

    P(int position) {
        stop = position;
        current = 0;
    }
}


public class DocTree implements ITree {
    DocNode root;


    public INode addNode(DocElement element) throws Exception {
        INode node = searchNode(element.getPath());
        if (node.isEmpty()) {
            node.setElement(element);
            return node;
        } else {
            if (node.isRemoved()) {
                if (element.getValue() != node.getElement().getValue()) {
                    throw new Exception(String.format("Symbols in the received element '%s' and in the stored element '%s' don't match", element.getValue(), node.getElement().getValue()));
                } else {
                    System.out.println("Received an element which was removed earlier");
                }
                return null;
            } else {
                throw new Exception("There is a conflict");
                //ToDo: solveConflict();
            }
        }
    }

    private INode searchNode(TreePath path) throws Exception {
        if (path == null) {
            throw new IllegalArgumentException();
        }

        INode node = root;
        INode parent = null;
        Direction direction = null;
        Direction d = path.getNextStep();
        while (d != null) {
            if (node == null) {
                node = createNode(null, parent, direction);
            }
            direction = d;
            parent = node;
            if (d == Direction.right) {
                node = node.getRightChild();
            } else if (d == Direction.left) {
                node = node.getLeftChild();
            }
            d = path.getNextStep();
        }

        if (node == null) {
            node = createNode(null, parent, direction);
        }
        return node;
    }

    private INode createNode(DocElement element, INode parent, Direction direction) {
        DocNode node = new DocNode(element);
        if (direction == null) {
            root = node;
        } else if (direction == Direction.right) {
            parent.setRightChild(node);
        } else if (direction == Direction.left) {
            parent.setRightChild(node);
        }
        return node;
    }

    public INode getNode(TreePath path) throws Exception {
        return searchNode(path);
    }

    private ArrayList<INode> traverseTreeUntilPosition(int position) {
        ArrayList<INode> nodes = new ArrayList();
        inorderTraverse(root, new P(position), nodes);
        return nodes;
    }

    public INode getNode(int position) {
        ArrayList<INode> nodes = traverseTreeUntilPosition(position);
        return nodes.get(nodes.size()-1);
    }

    private static boolean inorderTraverse(INode root, P positions, ArrayList<INode> result) {
        if (root != null) {
            boolean exit = inorderTraverse(root.getLeftChild(), positions, result);
            if (exit) {
                return true;
            }
            if (positions.current == positions.stop) {
                return true;
            }
            if (!root.isRemoved() && !root.isEmpty()) {
                result.add(root);
                positions.current++;
            }
            return inorderTraverse(root.getRightChild(), positions, result);
        }
        return false;
    }

    @Override
    public INode addSymbol(char symbol, int position) {
        DocElement el = new DocElement(symbol);
        DocNode newNode = new DocNode(el);

        if (root == null) {
            root = newNode;
            return newNode;
        }

        if (position == 0) {
            INode leaf = findLeftLeaf(root);
            leaf.setLeftChild(newNode);
            return newNode;
        }

        INode node = getNode(position);
        if (node.getRightChild() == null) {
            node.setRightChild(newNode);
        }
        else {
            INode leaf = findLeftLeaf(node.getRightChild());
            leaf.setLeftChild(newNode);
        }
        return newNode;
    }

    private INode findLeftLeaf(INode root) {
        if (root != null) {
            INode leaf =  findLeftLeaf(root.getLeftChild());
            if (leaf == null) {
                return root;
            }
            return leaf;
        }
        return null;
    }

    @Override
    public INode removeSymbol(char symbol, int position) throws Exception {
        if (position == 0)
        {
            return null;
        }
        INode node = getNode(position);
        IElement el = node.getElement();
        if (el.getValue() != symbol) {
            throw new Exception(String.format("Symbols '%s' and '%s' don't match", symbol, el.getValue()));
        }
        node.remove();
        return node;
    }

    public void removeNode(DocElement element) throws Exception {
        if (element == null) {
            throw  new IllegalArgumentException();
        }
        INode node = searchNode(element.getPath());
        if (!node.isEmpty()) {
            if (element.getValue() != node.getElement().getValue()) {
                throw new Exception(String.format("Symbols in the received element '%s' and in the stored element '%s' don't match", element.getValue(), node.getElement().getValue()));
            }
            node.remove();
        } else {
            node.setElement(element);
            node.remove();
            System.out.println("Removed an element which hasn't been added yet");
        }
    }

    @Override
    public String toString() {
        ArrayList<INode> nodes = traverseTreeUntilPosition(-1);
        StringBuilder str = new StringBuilder();
        for (INode node : nodes) {
            str.append(node.getElement().getValue());
        }
        return str.toString();
    }
}
