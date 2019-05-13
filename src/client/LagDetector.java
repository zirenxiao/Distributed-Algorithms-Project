package client;

import GUI.ConnectionInfo;
import main.Main;
import network.PingRequest;
import network.RequestHandler;

public class LagDetector extends Thread{
	
	private long currentLag = 0;
	
	private boolean run = false;
	private int failCount = 0;

	public LagDetector() {
		
	}
	
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
			RequestHandler rh = new RequestHandler(p);
			Main.getClient().sentToServer(rh.getMsg());
			failCount++;
		}
		
	}
	
	public void setCurrentLag(PingRequest p) {
		currentLag = p.getTimeDifference() / 2;
		failCount--;
		ConnectionInfo.getInstance().setConnectStatus("Current Lag: " + currentLag + "ms");
	}
	
	public long getLag() {
		return currentLag;
	}

	public void setRun(boolean run) {
		this.run = run;
	}
	
	
	

}
