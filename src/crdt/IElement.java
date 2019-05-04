package crdt;

import java.sql.Timestamp;

public interface IElement {
    TreePath getPath();
    void setPath(TreePath path);
    char getValue();
    boolean isHappenedEarlier(IElement that);
    Timestamp getTimestamp();
}
