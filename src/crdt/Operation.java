package crdt;

import java.io.Serializable;

public class Operation  implements Serializable{

	private static final long serialVersionUID = -7479421250871856919L;
	private OperationType type;
    private DocElement element;

    public Operation(OperationType type, DocElement element) {
        this.type = type;
        this.element = element;
//        System.out.println(element.getPathString());
//        replaceTreePathByPathSting();
    }

    public OperationType getType() {
        return type;
    }

    public DocElement getElement() {
//        restoreTreePath();
        return element;
    }

//    private void replaceTreePathByPathSting() {
//        TreePath path = element.getPath();
//        
//        element.setPathString(path.toString());
//        element.setPath(null);
//    }
//
//    private void restoreTreePath() {
//        String pathStr = element.getPathString();
//        element.setPath(new TreePath(pathStr));
//    }
}
