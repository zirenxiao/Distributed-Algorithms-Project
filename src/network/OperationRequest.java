package network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import crdt.DocElement;
import crdt.Operation;
import crdt.OperationType;
import crdt.TreePath;

public class OperationRequest implements Requests {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2172337042578617129L;
	private RequestType type;
	private Operation op;
	
	public OperationRequest(Operation o) {
		// TODO Auto-generated constructor stub
		this.op = o;
		type = RequestType.OPERATION;
	}
	
	public OperationRequest() {
		// TODO Auto-generated constructor stub
		type = RequestType.OPERATION;
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
	
	public Operation getOperation() {
		return op;
	}

	@Override
	public String toJSONString() {
		JSONObject obj = new JSONObject();
		
//		System.out.println("Path:"+op.getElement().getPath());
		obj.put("type", type.toString());
		
		JSONObject oper = new JSONObject();
		oper.put("symbol", String.valueOf(op.getElement().getValue()));
		oper.put("timestamp", op.getElement().getTimestamp().toString());
//		oper.put("path", toSerilizedString(op.getElement().getPath()));
		if (op.getElement().getPath()==null) {
			oper.put("path", null);
		}else {
			oper.put("path", op.getElement().getPath().toString());
		}
		
		oper.put("type", op.getType().toString());
		
		obj.put("operation", oper);
		
		
		return obj.toJSONString();
	}
	
	@Override
	public void fromJSONString(String s) {
		try {
			JSONObject content;
			JSONParser parser = new JSONParser();
			content = (JSONObject) parser.parse(s);
			type = RequestType.valueOf(content.get("type").toString());
			
			JSONObject operation = (JSONObject) content.get("operation");
			
			DocElement d = new DocElement(operation.get("symbol").toString().charAt(0));
//			d.setPath((TreePath) fromString(operation.get("path").toString()));
			
//			d.setPathString(operation.get("path").toString());
			if (operation.get("path")==null) {
				d.setPath(new TreePath());
			}else {
				d.setPath(new TreePath(operation.get("path").toString()));
			}
			
			
			d.setTimestamp(Timestamp.valueOf(operation.get("timestamp").toString()));
			op = new Operation(OperationType.valueOf(operation.get("type").toString()), d);
//			op = (Operation) fromString(content.get("operation").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private Object fromString(String s){
		byte [] data = Base64.getDecoder().decode(s);
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
	private String toSerilizedString(Serializable o){
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


}
