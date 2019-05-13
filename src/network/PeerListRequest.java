package network;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import GUI.ConnectionInfo;
import main.Main;

public class PeerListRequest implements Requests {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -353216899053744457L;
	private RequestType type;
	private String js;

	public PeerListRequest(ArrayList<String> s) {
		// TODO Auto-generated constructor stub
		type = RequestType.PEER;
		JSONObject obj = new JSONObject();
		JSONArray oper = new JSONArray();
		obj.put("type", type.toString());
		for (String str:s) {
			oper.add(str);
		}
		obj.put("hosts", oper);
		js = obj.toJSONString();
	}
	
	public PeerListRequest() {
		// TODO Auto-generated constructor stub
		type = RequestType.PEER;
	}

	@Override
	public RequestType getType() {
		// TODO Auto-generated method stub
		return RequestType.PEER;
	}

	@Override
	public void setType(RequestType t) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toJSONString() {
		return js;
	}

	@Override
	public void fromJSONString(String s) {
		// TODO Auto-generated method stub
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject content = (JSONObject) parser.parse(s);
			JSONArray jarr = (JSONArray) content.get("hosts");
			for (int i=0; i<jarr.size(); i++) {
				String str = jarr.get(i).toString();
				Main.getHostBroadCast().add(str);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
