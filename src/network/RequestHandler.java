package network;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import client.LagDetector;
import main.Main;
import server.Connection;

/** Process and handle request from a JSON string
 * @author zirenx
 *
 */
public class RequestHandler {
	
	private Requests r;
	
	/** Construct the handler from a string
	 * This will convert a string to a corresponding
	 * request.
	 * @param s
	 */
	public RequestHandler(String s) {
		RequestType t = getJSONType(s);
		if (t.equals(RequestType.OPERATION)) {
			r = new OperationRequest();
			r.fromJSONString(s);
		}else if (t.equals(RequestType.PING)) {
			r = new PingRequest();
			r.fromJSONString(s);
		}else if (t.equals(RequestType.PONG)) {
			r = new PingRequest();
			r.fromJSONString(s);
			r.setType(RequestType.PONG);
		}else if (t.equals(RequestType.PEER)) {
			r = new PeerListRequest();
			r.fromJSONString(s);
		}
	}

	/** Get the type of the request
	 * @param s
	 * @return
	 */
	private RequestType getJSONType(String s) {
//		System.out.println(s);
		try {
			JSONObject content;
			JSONParser parser = new JSONParser();
			content = (JSONObject) parser.parse(s);
			RequestType type = RequestType.valueOf(content.get("type").toString());
			return type;
		} catch (ParseException e) {
			System.err.println("Caused str:"+s+"||Issue:"+e.getErrorType());
//			e.printStackTrace();
		}
		return null;
	}
	
	/** Should this request be redirect to others
	 * @return
	 */
	public boolean isNotRedirect() {
		String s = r.getType().toString();
		for (NotRedirectType n:NotRedirectType.values()) {
			
			if (s.equals(n.toString())) {
				return true;
			}
		}
		return false;
	}

	/** Get the string content of this request
	 * @return
	 */
	public String getMsg() {
		return r.toJSONString();
	}

	/** Get the request
	 * @return
	 */
	public Requests getRequest() {
		return r;
	}
	
	/** Handle this request
	 * @param c
	 */
	public void doAction(Connection c) {
		RequestType type = r.getType();
		if (type == RequestType.OPERATION) {
			Main.getCommunicationManager().receiveAction((OperationRequest) this.r);
		}else if (type == RequestType.PING) {
			r.setType(RequestType.PONG);
			Main.getCommunicationManager().echoAction(r.toJSONString(), c);
		}else if (type == RequestType.PONG) {
			LagDetector ld = Main.getClient().getLagDetector();
			
			long t = ((PingRequest) this.r).getTimeDifference();
//			System.out.println(t);
			ld.setCurrentLag(t);
//			System.out.println("end");
		}
	}

	
}
