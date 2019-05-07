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
import network.Request;


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
	
	public static String requestToString(Request o) {
		return toString(o);
	}
	
	public static Request stringToRequest(String o) {
		return (Request) fromString(o);
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

}
