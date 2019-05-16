package crdt;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/***
 * Objects of DocElement class are stored in the DocTree nodes.
 * DocElement contains one symbol and timestamp when it was created
 * DocElement is the object which have to be sent to other peers.
 * For this purpose it keeps a path to the node which it is stored in.
 */
public class DocElement implements IElement, Serializable {
    private TreePath path;
    private char symbol;
    private Timestamp timestamp;


    public DocElement(char symbol) {
        this.symbol = symbol;
        timestamp = Timestamp.valueOf(LocalDateTime.now());
    }

    public Timestamp getTimestamp() {
        return timestamp;
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

    /***
     * Method isHappenedEarlier defines which of two DocElements was created first
     * @param that
     * @return
     */
    public boolean isHappenedEarlier(IElement that) {
        return this.getTimestamp().compareTo(that.getTimestamp()) < 0;
    }

    public String toString() {
        return String.format("Value: '%s', Timestamp: %s", symbol, timestamp.toString());
    }

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
    
}
