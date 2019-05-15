package client;

import GUI.ConnectionInfo;
import main.Main;

public class Client {


//	private Communication comm;
	private LagDetector ld;
	private Connection con;
    
    public void connectTo(String host, int port) {
    	ld = new LagDetector();
//    	comm = new Communication();
//    	comm.connect(host, port);
		con = new Connection(host, port);
    }
    
    public void connectTo(String host, String port) {
    	connectTo(host, Integer.parseInt(port));
    }
    
	public void sentToServer(String str) {
		if (con!=null) {
			con.writeMsg(str);
		}
		
	}

	public LagDetector getLagDetector() {
		return ld;
	}
	
	public void closeConnection() {
		this.con.closeCon();
		ConnectionInfo.getInstance().setConnectStatus("Connection Closed.");
    	ConnectionInfo.getInstance().setConnectEnable(true);
    	Main.getClient().getLagDetector().setRun(false);
	}


}
