package network;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PingRequest implements Requests {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2887269528991652501L;
	private RequestType type;
	private Timestamp sendTime;

	public PingRequest() {
		// TODO Auto-generated constructor stub
		sendTime = Timestamp.valueOf(LocalDateTime.now());
		type = RequestType.PING;
	}

	@Override
	public RequestType getType() {
		// TODO Auto-generated method stub
		return type;
	}
	
	public long getTimeDifference() {
		return Timestamp.valueOf(LocalDateTime.now()).getTime() - sendTime.getTime();
	}

	@Override
	public void setType(RequestType t) {
		this.type = t;
		
	}

	@Override
	public String toJSONString() {
		JSONObject obj = new JSONObject();
		obj.put("type", type.toString());
		obj.put("time", sendTime.toString());
		return obj.toJSONString();
	}

	@Override
	public void fromJSONString(String s) {
		try {
			JSONObject content;
			JSONParser parser = new JSONParser();
			content = (JSONObject) parser.parse(s);
			type = RequestType.valueOf(content.get("type").toString());
			sendTime = Timestamp.valueOf(content.get("time").toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
