package utils;


import java.io.*;
import java.sql.Timestamp;
import java.util.Base64;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import crdt.DocElement;
import crdt.Operation;
import crdt.OperationType;
import crdt.TreePath;


public class Settings {
	public static final String CERT_PATH = "certificates/cert.pem";
	public static final String PRIVATE_KEY_PATH = "certificates/key.pem";
	
	/** Return JSON object, or null if transformation was failed.
	 * @param msg
	 * @return
	 */
	public static JSONObject stringToJson(String msg) {
		JSONObject content;
		JSONParser parser = new JSONParser();
		try {
			content = (JSONObject) parser.parse(msg);
			return content;
		} catch (ParseException e) {
			System.err.println(e);
		} catch (ClassCastException exc) {
			System.err.println(exc);
		}
		return null;
    }
	
	@SuppressWarnings("unchecked")
	public static String operationToString(Operation o) {
		JSONObject content;
		content = new JSONObject();
		content.put("type", o.getType().toString());
		content.put("symbol", Character.toString(o.getElement().getValue()));
		content.put("path", toString(o.getElement().getPath()));
		content.put("timestamp", o.getElement().getTimestamp().toString());
//		System.out.println(content.toJSONString());
//		System.out.println(stringToJson(content.toJSONString()));
		return content.toJSONString();
	}
	
	public static Operation jsonToOperation(JSONObject o) {
//		System.out.println(o.toJSONString());
		DocElement e = new DocElement(o.get("symbol").toString().charAt(0));
		Operation op = new Operation(OperationType.valueOf(o.get("type").toString()), e);
		op.getElement().setPath((TreePath) fromString(o.get("path").toString()));
		op.getElement().setTimestamp(Timestamp.valueOf(o.get("timestamp").toString()));
//		System.out.println(op.getElement().getValue());
//		System.out.println(op.getElement().getPath().length());
		return op;
	}
	
	private static Object fromString(String s){
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
	private static String toString( Serializable o ){
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
	
	public static Operation stringToOperation(String str) {
		JSONObject jo = stringToJson(str);
		return jsonToOperation(jo);
	}

}
