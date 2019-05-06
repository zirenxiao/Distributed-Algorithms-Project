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

    public int addNode(DocElement element) throws Exception {
        return addNode(element, false);
    }

    public int addNode(DocElement element, boolean isRemoved) throws Exception {
        INode node = searchNode(element.getPath());
        if (node.isEmpty()) {
            node.setElement(element);
        } else {
            if (node.isRemoved()) {
                if (element.getValue() != node.getElement().getValue()) {
                    throw new Exception(String.format("Symbols in the received element '%s' and in the stored element '%s' don't match", element.getValue(), node.getElement().getValue()));
                } else {
                    System.out.println("Received an element which was removed earlier");
                }
            } else {
                System.out.println("Solving a conflict");
                solveConflict(node, element);
            }
        }
        // add removed node for full synchronization
        if (isRemoved) {
            node.remove();
            return -1;
        }
        return findNodePosition(node);
    }

    private int findNodePosition(INode node) {
        ArrayList<INode> nodes = new ArrayList();
        inorderTraverse(root, new P(-1), nodes, null);

        return nodes.indexOf(node);
    }

    private void solveConflict(INode node, IElement element) {
        DocElement firstElement  = (DocElement) element;
        DocElement secondElement = (DocElement) node.getElement();
        //INode rightChild = null;
        if (secondElement.isHappenedEarlier(firstElement)) {
            // swap
            node.setElement(firstElement);
            firstElement = secondElement;
            //rightChild = node.takeRightChild();
        }
        INode leftChild = node.getLeftChild();
        if (leftChild == null) {
            leftChild = createNode(firstElement, node, Direction.left);
        } else {
            solveConflict(leftChild, firstElement);
        }
//        if (rightChild != null) {
//            leftChild.setRightChild(rightChild);
//            updatePathForElements(rightChild);
//        }
    }

    private void updatePathForElements(INode node) {
        if (node == null)
            return;
        node.updateElementPath();
        updatePathForElements(node.getLeftChild());
        updatePathForElements(node.getRightChild());
    }

    private void swap(INode node, IElement secondElement) {
        IElement firstElement = node.getElement();
        node.setElement(secondElement);
        INode leftChild = node.getLeftChild();
        if (leftChild == null) {
            createNode((DocElement) firstElement, node, Direction.left);
        } else {
            solveConflict(leftChild, firstElement);
        }

    }

    private INode searchNode(TreePath path) {
        if (path == null) {
            if (root == null) {
                return createNode(null, null, null);
            }
            return root;
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
            parent.setLeftChild(node);
        }
        return node;
    }

    public INode getNode(TreePath path) throws Exception {
        return searchNode(path);
    }

    private ArrayList<INode> traverseTreeUntilPosition(int position) {
        ArrayList<INode> nodes = new ArrayList();
        inorderTraverse(root, new P(position), nodes, null);
        return nodes;
    }

    public INode getNode(int position) {
        ArrayList<INode> nodes = traverseTreeUntilPosition(position);
        return nodes.get(nodes.size()-1);
    }

    private static boolean inorderTraverse(INode currentNode,
                                           P positions,
                                           ArrayList<INode> visibleNodes,
                                           ArrayList<INode> allNodes) {
        if (currentNode != null) {
            boolean exit = inorderTraverse(currentNode.getLeftChild(), positions, visibleNodes, allNodes);
            if (exit) {
                return true;
            }
            if (positions.current == positions.stop) {
                return true;
            }
            if (!currentNode.isRemoved() && !currentNode.isEmpty()) {
                visibleNodes.add(currentNode);
                positions.current++;
            }
            if (allNodes != null) {
                allNodes.add(currentNode);
            }
            return inorderTraverse(currentNode.getRightChild(), positions, visibleNodes, allNodes);
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

    public int removeNode(DocElement element) throws Exception {
        int position = -1;
        if (element == null) {
            throw  new IllegalArgumentException();
        }
        INode node = searchNode(element.getPath());
        if (!node.isEmpty()) {
            if (element.getValue() != node.getElement().getValue()) {
                throw new Exception(String.format("Symbols in the received element '%s' and in the stored element '%s' don't match", element.getValue(), node.getElement().getValue()));
            }
            position = findNodePosition(node);
            node.remove();
        } else {
            node.setElement(element);
            node.remove();
            System.out.println("Removed an element which hasn't been added yet");
        }
        return position;
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

    public ArrayList<INode> getDoc() {
        ArrayList<INode> nodes = new ArrayList();
        ArrayList<INode> allNodes = new ArrayList();
        inorderTraverse(root, new P(-1), nodes, allNodes);

        return allNodes;
    }
}
