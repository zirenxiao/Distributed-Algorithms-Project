package network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

import io.netty.channel.ChannelHandlerContext;
import main.Main;

public class RequestHandler {
	
	private String msg;
	private Requests r;
	
	public RequestHandler(String s) {
		this.msg = s;
		this.r = (Requests) this.stringToRequest(s);
	}
	
	public RequestHandler(Requests r) {
		this.msg = this.requestToString(r);
		this.r = r;
	}

	public String requestToString(Requests o) {
		return toString(o);
	}
	
	public Object stringToRequest(String o) {
		return fromString(o);
	}
	
	public void setRequestType(RequestType rt) {
		this.r.setType(rt);
		this.msg = this.requestToString(this.r);
	}
	
	private Object fromString(String s){
		byte [] data = Base64.getMimeDecoder().decode(s);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o  = ois.readObject();
			ois.close();
			return o;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/** Write the object to a Base64 string. */
	private String toString(Serializable o){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Base64.getEncoder().encodeToString(baos.toByteArray()); 
	}

	public String getMsg() {
		return msg;
	}

	public Requests getRequest() {
		return r;
	}
	
	public void doAction(ChannelHandlerContext ctx) {
		RequestType type = r.getType();
		if (type == RequestType.OPERATION) {
			Main.getCommunicationManager().receiveAction((OperationRequest) this.r);
		}else if (type == RequestType.PING) {
			this.setRequestType(RequestType.PONG);
			Main.getCommunicationManager().echoAction(this.msg, ctx);
		}else if (type == RequestType.PONG) {
			Main.getClient().getLagDetector().setCurrentLag((PingRequest) this.r);
		}
	}

	
}
