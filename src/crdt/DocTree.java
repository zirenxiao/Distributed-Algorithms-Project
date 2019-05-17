package crdt;

import java.util.ArrayList;

/***
 * Auxiliary class to traverse the node
 * Contains current position in the tree
 * and position at which traversal should be stopped
 */
class P {
    int stop;
    int current;

    P(int position) {
        stop = position;
        current = 0;
    }
}

/***
 * class DocTree describes underlying Data Type of CRDT
 */
public class DocTree implements ITree {
    DocNode root;

    /***
     * Method addNode() adds a Node in the DocTree according to the path containing in the DocElement
     * @param element
     * @return
     * @throws Exception
     */
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

    /***
     * Method findNodePosition() returns the node position in the linear order after tree traversal
     * @param node
     * @return
     */
    private int findNodePosition(INode node) {
        ArrayList<INode> nodes = new ArrayList();
        inorderTraverse(root, new P(-1), nodes, null);

        return nodes.indexOf(node);
    }

    /***
     * Method solveConflict() defines the place of two DocElements
     * when they were created at one place simultaneously
     * @param node
     * @param element
     */
    private void solveConflict(INode node, IElement element) {
        DocElement firstElement  = (DocElement) element;
        DocElement secondElement = (DocElement) node.getElement();
        if (secondElement.isHappenedEarlier(firstElement)) {
            // swap
            node.setElement(firstElement);
            firstElement = secondElement;
        }
        INode leftChild = node.getLeftChild();
        if (leftChild == null) {
            createNode(firstElement, node, Direction.left);
        } else {
            solveConflict(leftChild, firstElement);
        }
    }

    /***
     * Method searchNode() returns the node according to the path
     * @param path
     * @return
     */
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

    /***
     * Method traverseTreeUntilPosition() performs the tree traversal until the position
     * @param position - position of the symbol in the user editor area
     * @return array of Nodes
     */
    private ArrayList<INode> traverseTreeUntilPosition(int position) {
        ArrayList<INode> nodes = new ArrayList();
        inorderTraverse(root, new P(position), nodes, null);
        return nodes;
    }

    public INode getNode(int position) {
        ArrayList<INode> nodes = traverseTreeUntilPosition(position);
        return nodes.get(nodes.size()-1);
    }

    /***
     * Method inorderTraverse() perform inorder tree traversal
     * @param currentNode - node to start traversal
     * @param positions - object containing current position in the tree and
     *                  the position at which traversal should be stopped
     * @param visibleNodes - returns only nodes which weren't removed
     * @param allNodes -  returns all traversed nodes
     * @return
     */
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

    /***
     * Method creates a DocNode and DocElement based on symbol and its position in the editor area
     * Method is used to add changes from local user editor
     * @param symbol
     * @param position
     * @return
     */
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

    /***
     * Method marks the node as removed according to the position
     * Symbol is used for additional check if the node is correct.
     * Method is used to apply changes from local user editor.
     * @param symbol
     * @param position
     * @return
     * @throws Exception
     */
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

    /***
     * Method marks the node as removed according to the path containing in the DocElement object
     * Method is used to apply changes from received messages
     * @param element
     * @return
     * @throws Exception
     */
    public int removeNode(DocElement element) throws Exception {
        int position = -1;
        if (element == null) {
            throw  new IllegalArgumentException();
        }
        INode node = searchNode(element.getPath());
        if (!node.isEmpty()) {
            if (element.getValue() != node.getElement().getValue()) {
                throw new Exception(String.format("Symbols in the received element '%s' and in the stored element '%s' don't match",
                                    element.getValue(),
                                    node.getElement().getValue()));
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

    /***
     * Method returns the whole document as an array of nodes
     * @return
     */
    public ArrayList<INode> getDoc() {
        ArrayList<INode> nodes = new ArrayList();
        ArrayList<INode> allNodes = new ArrayList();
        inorderTraverse(root, new P(-1), nodes, allNodes);

        return allNodes;
    }
}
