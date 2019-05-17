package crdt;

/***
 * This class describes an atom operation of CRDT implementation
 * Contains DocElement with
 * and Operation Type which is performed with this DocElement
 */
public class Operation {
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
