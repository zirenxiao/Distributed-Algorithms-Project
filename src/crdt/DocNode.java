package crdt;


public class DocNode implements INode {
    private DocElement element;
    private DocNode leftChild;
    private DocNode rightChild;
    private DocNode parent;
    private boolean isRemoved;

    public DocNode() {
        this(null);
    }

    public DocNode(DocElement element) {
        this.element = element;
        isRemoved = false;
    }

    @Override
    public INode getLeftChild() {
        return leftChild;
    }

    @Override
    public void setLeftChild(INode node) {
        this.leftChild = (DocNode)node;
        node.setParent(this);
    }

    @Override
    public INode getRightChild() {
        return rightChild;
    }



    @Override
    public void setRightChild(INode node) {
        this.rightChild = (DocNode)node;
        node.setParent(this);
    }

    @Override
    public void setParent(INode node) {
        this.parent = (DocNode) node;
        if (element != null) {
            element.setPath(getPathToRoot());
        }
    }

    @Override
    public IElement getElement() {
        return element;
    }

    @Override
    public void setElement(IElement element) {
        if (element == null) {
            throw new IllegalArgumentException();
        }
        this.element = (DocElement) element;
        this.element.setPath(getPathToRoot());
    }

    private TreePath getPathToRoot() {
        TreePath path = new TreePath();
        getPathToParent(path);
        return path;
    }

    private void getPathToParent(TreePath path) {
        if (parent != null) {
            parent.getPathToParent(path);
            if (this == parent.rightChild) {
                path.addStep(Direction.right);
            } else {
                path.addStep(Direction.left);
            }
        }
    }

    @Override
    public void remove() {
        isRemoved = true;
    }

    @Override
    public boolean isRemoved() {
        return isRemoved;
    }

    @Override
    public boolean isEmpty() {
        if (element == null)
            return true;
        return false;
    }
}
