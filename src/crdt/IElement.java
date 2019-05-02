package crdt;

public interface IElement {
    TreePath getPath();
    void setPath(TreePath path);
    char getValue();
}
