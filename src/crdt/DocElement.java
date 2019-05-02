package crdt;
import java.io.Serializable;
import java.sql.Timestamp;

public class DocElement implements IElement, Serializable {
    private TreePath path;
    private char symbol;
    private Timestamp timestamp;

    public DocElement(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public void setPath(TreePath path) {
        this.path = path;
    }

    @Override
    public TreePath getPath() {
        return path;
    }

    @Override
    public char getValue() {
        return symbol;
    }
}
