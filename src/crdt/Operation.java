package crdt;

import java.io.Serializable;

public class Operation  implements Serializable{

	private static final long serialVersionUID = -7479421250871856919L;
	private OperationType type;
    private DocElement element;

    public Operation(OperationType type, DocElement element) {
        this.type = type;
        this.element = element;
    }

    public OperationType getType() {
        return type;
    }

    public DocElement getElement() {
        return element;
    }
}
