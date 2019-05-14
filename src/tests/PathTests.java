package tests;

import crdt.*;

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

    public static void testPathToString() {
        System.out.println("Test Path To String Transformation In Operation Class");
        TreePath path = new TreePath();
        path.addStep(Direction.left);
        path.addStep(Direction.right);
        path.addStep(Direction.right);
        path.addStep(Direction.left);
        path.addStep(Direction.left);

        DocElement element = new DocElement('a');
        element.setPath(path);

        Operation op = new Operation(OperationType.insert, element);


        System.out.println(path.length());

        System.out.println(String.format("Should be: %s", path.toString()));
        System.out.println(String.format("Actual value from operation: %s", op.getElement().getPath().toString()));
    }
}
