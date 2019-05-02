package crdt;
import java.io.Serializable;
import java.util.BitSet;

/*
*        TreePath path = new TreePath();
*        path.addStep(Direction.left);
*        path.addStep(Direction.right);
*        path.addStep(Direction.right);
*        path.addStep(Direction.left);
*        path.addStep(Direction.left);
*
*        System.out.println(path.length());
*
*        Direction s = path.getNextStep();
*        while (s != null) {
*            System.out.println(s);
*            s = path.getNextStep();
*        }
*
* */

public class TreePath implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1637333504913968048L;
	public BitSet path;
    int currentWritePosition;
    int currentReadPosition;

    public TreePath() {
        path = new BitSet();
        currentWritePosition = 0;
        currentReadPosition = 0;
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

}
