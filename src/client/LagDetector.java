package client;

import GUI.ConnectionInfo;
import main.Main;
import network.PingRequest;
import network.RequestHandler;

/** Detecting lag and alive information
 * @author zirenx
 *
 */
public class LagDetector extends Thread{
	
	private long currentLag;
	
	private boolean run;
	private int failCount;

	public LagDetector() {
		currentLag = 0;
		failCount = 0;
		run = false;
	}
	
	/** If no ping back response after 5 seconds,
	 * the connection will be shut down
	 *
	 */
	public void run() {
		while (run) {
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (failCount >= 5) {
				Main.getClient().closeConnection();
				break;
			}
			PingRequest p = new PingRequest();
			RequestHandler rh = new RequestHandler(p.toJSONString());
			Main.getCommunicationManager().toServer(rh.getMsg());
			failCount++;
		}
		
	}
	
	/** Set lag information
	 * @param t
	 */
	public void setCurrentLag(long t) {
		currentLag = t / 2;
		failCount = failCount - 1;
		ConnectionInfo.getInstance().setConnectStatus(
				"Current Lag: " + currentLag + "ms");
	}
	
	/** Get lag
	 * @return
	 */
	public long getLag() {
		return currentLag;
	}

	/** Set to run the detector or not
	 * @param run
	 */
	public void setRun(boolean run) {
		this.run = run;
	}
	
	
	

}
