package crdt;
import java.io.Serializable;
import java.util.BitSet;

public class TreePath implements Serializable{

	private static final long serialVersionUID = 1637333504913968048L;
	public BitSet path;
    int currentWritePosition;
    int currentReadPosition;

    public TreePath() {
        path = new BitSet();
        currentWritePosition = 0;
        currentReadPosition = 0;
    }

    public TreePath(String path) {
        this();
        for (char d: path.toCharArray()) {
            if (d == 'r') {
                addStep(Direction.right);
            } else if (d == 'l') {
                addStep(Direction.left);
            }
        }
    }

    public void addStep(Direction d) {
        if (d == Direction.left) {
            path.set(currentWritePosition, false);
            currentWritePosition++;
        }
        if (d == Direction.right) {
            path.set(currentWritePosition, true);
            currentWritePosition++;
        }
    }

    public Direction getNextStep() {
        int length = path.length();
        if (currentReadPosition < currentWritePosition) {
            boolean step = path.get(currentReadPosition);
            currentReadPosition++;

            if (step) {
                return Direction.right;
            }
            return Direction.left;
        }
        else {
            currentReadPosition = 0;
            return null;
        }
    }

    public int length() {
        return currentWritePosition;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            boolean step = path.get(i);
            if (step) {
                str.append('r');
//                str.append(Direction.right);
//                str.append(" ");
            } else {
                str.append('l');
//                str.append(Direction.left);
//                str.append(" ");
            }
        }
        return str.toString();
    }

}
