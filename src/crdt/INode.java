package crdt;

public interface INode {
    INode getLeftChild();
    INode takeLeftChild();
    void setLeftChild(INode node);
    INode getRightChild();
    INode takeRightChild();
    void setRightChild(INode node);
    void setParent(INode node);
    IElement getElement();
    void setElement(IElement element);
    void remove();
    boolean isRemoved();
    boolean isEmpty();
    void updateElementPath();
}
