package client;

import GUI.ConnectionInfo;
import main.Main;

/** Client
 * @author zirenx
 *
 */
public class Client {
	
	private LagDetector ld;
	private Connection con;
    
    /** Connect to a server
     * @param host
     * @param port
     */
    public void connectTo(String host, int port) {
    	ld = new LagDetector();
		con = new Connection(host, port);
    }
    
    public void connectTo(String host, String port) {
    	connectTo(host, Integer.parseInt(port));
    }
    
	/** Send message to the server
	 * @param str
	 */
	public void sentToServer(String str) {
		if (con!=null) {
			con.writeMsg(str);
		}
	}

	public LagDetector getLagDetector() {
		return ld;
	}
	
	/** Close the connection to server
	 * 
	 */
	public void closeConnection() {
		this.con.closeCon();
		ConnectionInfo.getInstance().setConnectStatus("Connection Closed.");
    	ConnectionInfo.getInstance().setConnectEnable(true);
    	Main.getClient().getLagDetector().setRun(false);
	}


}
