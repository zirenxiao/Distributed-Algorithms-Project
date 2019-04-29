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

class SearchResult {
    INode node;
    INode parent;
    Direction direction;
}

public class DocTree implements ITree {
    DocNode root;


    public INode addNode(DocElement element) throws Exception {
        SearchResult res = searchNode(element.getPath());
        if (res.node == null) {
            return createNode(element, res.parent, res.direction);
        } else {
            throw new Exception("There is a conflict");
            //ToDo: solveConflict();
        }
    }

    private SearchResult searchNode(TreePath path) throws Exception {
        if (path == null) {
            throw new IllegalArgumentException();
        }

        SearchResult res = new SearchResult();
        res.node = root;
        Direction d = path.getNextStep();
        while (d != null) {
            if (res.node == null) {
                throw new Exception("Path can't be reached");
            }
            res.direction = d;
            res.parent = res.node;
            if (d == Direction.right) {
                res.node = res.node.getRightChild();
            } else if (d == Direction.left) {
                res.node = res.node.getLeftChild();
            }
            d = path.getNextStep();
        }
        return res;
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
        return searchNode(path).node;
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
            if (!root.isRemoved()) {
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
        SearchResult res = searchNode(element.getPath());
        INode node = res.node;
        if (node != null) {
            if (element.getValue() != node.getElement().getValue()) {
                throw new Exception(String.format("Symbols in the received element '%s' and in the stored element '%s' don't match", element.getValue(), node.getElement().getValue()));
            }
            node.remove();
        } else {
            throw new Exception("There is a problem");
            // ToDo: What to do if the node hasn't found?
            // What if a delete message comes earlier than a create message?
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
