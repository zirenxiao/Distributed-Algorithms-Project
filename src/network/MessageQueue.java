package network;

import java.util.ArrayList;

import crdt.IMessageHandler;
import crdt.Operation;
import main.Main;

public class MessageQueue{
	
	private ArrayList<Operation> queue;
	private boolean active;
	
	public MessageQueue() {
		queue = new ArrayList<Operation>();
	}
	
	public void add(Operation o, IMessageHandler messageHandler) {
		queue.add(o);
		this.setActive(messageHandler);
	}
	
	public void delete() {
		queue.remove(0);
	}
	
	public int getSize() {
		return queue.size();
	}
	
	private Operation getFirst() {
		Operation o = queue.get(0);
		delete();
		return o;
	}
	
	private void handle(IMessageHandler messageHandler) {
		Operation o = getFirst();
		messageHandler.handle(o);
	}
	
	private void handleQueue(IMessageHandler messageHandler) {
		while (!queue.isEmpty()) {
			handle(messageHandler);
		}
		// after finished, set to inactive
		this.active = false;
	}
	
	public void setActive(IMessageHandler messageHandler) {
		if (!this.active) {
			// when the queue handler is active
			// don't call it second time
			this.active = true;
			handleQueue(messageHandler);
		}
	}

}
