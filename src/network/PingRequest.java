package network;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PingRequest implements Requests {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2887269528991652501L;
	private RequestType type = RequestType.PING;
	private Timestamp sendTime;

	public PingRequest() {
		// TODO Auto-generated constructor stub
		sendTime = Timestamp.valueOf(LocalDateTime.now());
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

}
