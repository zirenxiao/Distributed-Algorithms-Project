package tests;

import crdt.*;

public class CrdtTests {

    public static void testCrdtGetNodeByPath() {
        System.out.println("Test DocTree.GetNode(Path) if node exists");
        DocTree doc = new DocTree();
        //       b
        //   a      c
        //     f  m    d
        doc.addSymbol('b', 0);
        doc.addSymbol('c', 1);
        doc.addSymbol('d', 2);
        doc.addSymbol('a', 0);
        doc.addSymbol('f', 1);
        doc.addSymbol('m', 3);

        System.out.println(String.format("Doc: %s", doc.toString()));

        TreePath path = new TreePath();
        path.addStep(Direction.left);
        path.addStep(Direction.right);


        INode node = null;
        try {
            node = doc.getNode(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Should be: f");
        System.out.println(String.format("Actual value: %s", node.getElement().getValue()));
    }

    public static void testCrdtGetNodeByPath_1() {
        System.out.println("Test DocTree.GetNode(Path) if node doesn't exist");
        DocTree doc = new DocTree();
        //       b
        //   a      c
        //     f  m    d
        doc.addSymbol('b', 0);
        doc.addSymbol('c', 1);
        doc.addSymbol('d', 2);
        doc.addSymbol('a', 0);
        doc.addSymbol('f', 1);
        doc.addSymbol('m', 3);

        System.out.println(String.format("Doc: %s", doc.toString()));

        TreePath path = new TreePath();
        path.addStep(Direction.left);
        path.addStep(Direction.left);


        INode node = null;
        try {
            node = doc.getNode(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Should be: null");
        System.out.println(String.format("Actual value: %s", node.getElement().getValue()));
    }

    public static  void testCrdtAddNodeByPath() {
        System.out.println("Test DocTree.addNode(DocElement)");
        DocTree doc = new DocTree();

        DocElement el = new DocElement('a');
        TreePath path = new TreePath();
        el.setPath(path);

        DocElement el1 = new DocElement('b');
        TreePath path1 = new TreePath();
        path1.addStep(Direction.right);
        el1.setPath(path1);

        try {
            doc.addNode(el);
            doc.addNode(el1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Should be: ab");
        System.out.println(String.format("Actual value: %s", doc.toString()));
    }

    public static  void testCrdtAddNodeByPath_1() {
        System.out.println("Test DocTree.addNode(DocElement) if path doesn't exist");
        DocTree doc = new DocTree();

        DocElement el = new DocElement('a');
        TreePath path = new TreePath();
        el.setPath(path);

        DocElement el1 = new DocElement('b');
        TreePath path1 = new TreePath();
        path1.addStep(Direction.right);
        path1.addStep(Direction.right);
        el1.setPath(path1);

        System.out.println("Should be: ab");

        try {
            doc.addNode(el);
            doc.addNode(el1);
        } catch (Exception e) {
            System.out.println(String.format("Actual value: %s", e.getMessage()));
        }
        System.out.println(String.format("Actual value: %s", doc.toString()));
    }

    public static  void testCrdtAddNodeByPath_2() {
        System.out.println("Test DocTree.addNode(DocElement) with conflict");
        DocTree doc = new DocTree();

        DocElement el = new DocElement('a');
        TreePath path = new TreePath();
        el.setPath(path);

        DocElement el1 = new DocElement('b');
        TreePath path1 = new TreePath();
        path1.addStep(Direction.right);
        el1.setPath(path1);

        DocElement el2 = new DocElement('c');
        el2.setPath(path1);

        System.out.println("Should be: Error. There is a conflict");
        try {
            doc.addNode(el);
            doc.addNode(el1);
            doc.addNode(el2);
        } catch (Exception e) {
            System.out.println(String.format("Actual value: %s", e.getMessage()));
        }
    }

    public static  void testCrdtRemoteDelete() {
        System.out.println("Test DocTree.removeNode(DocElement)");
        DocTree doc = new DocTree();

        DocElement el = new DocElement('a');
        TreePath path = new TreePath();
        el.setPath(path);

        DocElement el1 = new DocElement('b');
        TreePath path1 = new TreePath();
        path1.addStep(Direction.right);
        el1.setPath(path1);


        System.out.println("Should be: a");
        try {
            doc.addNode(el);
            doc.addNode(el1);
            doc.removeNode(el1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(String.format("Actual value: %s", doc.toString()));

    }

    public static  void testCrdtRemoteDeleteAdd() {
        System.out.println("Test DocTree.removeNode(DocElement) before addNode()");
        DocTree doc = new DocTree();

        DocElement el = new DocElement('b');
        TreePath path = new TreePath();
        el.setPath(path);

        DocElement el1 = new DocElement('c');
        TreePath path1 = new TreePath();
        path1.addStep(Direction.right);
        el1.setPath(path1);


        System.out.println("Should be: b");
        try {
            doc.addNode(el);
            doc.removeNode(el1);
            doc.addNode(el1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(String.format("Actual value: %s", doc.toString()));

    }

}
