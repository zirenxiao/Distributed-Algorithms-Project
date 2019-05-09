package network;

import java.util.ArrayList;

import crdt.Operation;

public class OperationRequest implements Requests {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2172337042578617129L;
	private ArrayList<Operation> op;
	private RequestType type = RequestType.OPERATION;

	public OperationRequest() {
		// TODO Auto-generated constructor stub
		op = new ArrayList<Operation>();
	}
	
	public OperationRequest(ArrayList<Operation> o) {
		// TODO Auto-generated constructor stub
		op = new ArrayList<Operation>();
		this.copyOperations(o);
	}

	public void add(Operation o) {
		op.add(o);
	}
	
	public void delete(Operation o) {
		op.remove(o);
	}
	
	public void delete(int i) {
		op.remove(i);
	}
	
	public ArrayList<Operation> getList(){
		return op;
	}
	
	public boolean isEmpty() {
		return op.isEmpty();
	}
	
	public Operation getFirst() {
		Operation o = op.get(0);
		this.delete(0);
		return o;
	}
	
	public void copyOperations(ArrayList<Operation> o) {
		for (int i=0; i<o.size(); i++) {
			this.add(o.get(i));
		}
	}

	@Override
	public RequestType getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public void setType(RequestType t) {
		// TODO Auto-generated method stub
		
	}
}
