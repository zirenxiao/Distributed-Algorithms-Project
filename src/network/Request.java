package network;

import java.io.Serializable;
import java.util.ArrayList;

import crdt.Operation;

public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4398837851686836254L;
	
	private ArrayList<Operation> op;

	public Request() {
		// TODO Auto-generated constructor stub
		op = new ArrayList<Operation>();
	}
	
	public Request(ArrayList<Operation> o) {
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
}
