package tests;

import crdt.Direction;
import crdt.TreePath;

public class PathTests {
    public static void testPath() {
        System.out.println("Test Path class");
        TreePath path = new TreePath();
        path.addStep(Direction.left);
        path.addStep(Direction.right);
        path.addStep(Direction.right);
        path.addStep(Direction.left);
        path.addStep(Direction.left);

        System.out.println(path.length());

        System.out.println("Should be: left, right, right, left, left");
        System.out.println(String.format("Actual value from toString(): %s", path.toString()));

        System.out.println("Actual value from getNextStep(): ");
        Direction s = path.getNextStep();
        while (s != null) {
            System.out.println(s);
            s = path.getNextStep();
        }
    }
}
