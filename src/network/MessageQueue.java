package network;

import java.util.ArrayList;

import crdt.IMessageHandler;
import crdt.Operation;

/** Message queue of operations
 * @author zirenx
 *
 */
public class MessageQueue{
	
	private ArrayList<Operation> queue;
	private boolean active;
	
	public MessageQueue() {
		queue = new ArrayList<Operation>();
	}
	
	/** Add operation to the queue
	 * @param o
	 * @param messageHandler
	 */
	public void add(Operation o, IMessageHandler messageHandler) {
		queue.add(o);
		this.setActive(messageHandler);
	}
	
	/** Delete first operation in the queue
	 * 
	 */
	public void delete() {
		queue.remove(0);
	}
	
	/** Get length of the queue
	 * @return
	 */
	public int getSize() {
		return queue.size();
	}
	
	/** Get first operation
	 * @return
	 */
	private Operation getFirst() {
		Operation o = queue.get(0);
		delete();
		return o;
	}
	 
	/** Handle the first message
	 * @param messageHandler
	 */
	private void handle(IMessageHandler messageHandler) {
		Operation o = getFirst();
		messageHandler.handle(o);
	}
	
	/** Handle the whole queue
	 * @param messageHandler
	 */
	private void handleQueue(IMessageHandler messageHandler) {
		while (!queue.isEmpty()) {
			handle(messageHandler);
		}
		// after finished, set to inactive
		this.active = false;
	}
	
	/** Active the queue processing
	 * @param messageHandler
	 */
	public void setActive(IMessageHandler messageHandler) {
		if (!this.active) {
			// when the queue handler is active
			// don't call it second time
			this.active = true;
			handleQueue(messageHandler);
		}
	}

}
