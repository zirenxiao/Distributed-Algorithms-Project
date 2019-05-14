package network;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import client.LagDetector;
import main.Main;
import server.Connection;

public class RequestHandler {
	
	private Requests r;
	
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
	
	public boolean isNotRedirect() {
		String s = r.getType().toString();
		for (NotRedirectType n:NotRedirectType.values()) {
			
			if (s.equals(n.toString())) {
				return true;
			}
		}
		return false;
	}

	public String getMsg() {
		return r.toJSONString();
	}

	public Requests getRequest() {
		return r;
	}
	
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
