package crdt;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    public boolean isHappenedEarlier(IElement that) {
        return this.getTimestamp().compareTo(that.getTimestamp()) < 0;
    }

    public String toString() {
//        return String.valueOf(symbol);
        return String.format("Value: '%s', Timestamp: %s", symbol, timestamp.toString());
    }

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
    
}
