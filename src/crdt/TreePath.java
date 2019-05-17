package crdt;

import java.util.BitSet;

/***
 * This class is used to keep the path to reach the node
 * Usually it is the path from the root node to the particular node
 * The root node have the null or empty path.
 * Path is kept as bit array
 * Path has two possible directions (right and left) at each step
 * Each step is the level of the tree
 */
public class TreePath {

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

    /***
     * Method getNextStep() is used to iterate the path
     * @return
     */
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

    /***
     * Method returns the level of the tree at which node with this path is situated
     * @return
     */
    public int length() {
        return currentWritePosition;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            boolean step = path.get(i);
            if (step) {
                str.append('r');
            } else {
                str.append('l');
            }
        }
        return str.toString();
    }

}
