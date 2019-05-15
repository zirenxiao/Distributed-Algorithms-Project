package network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import GUI.ConnectionInfo;
import main.Main;

/** Keep a list of all peers in the network 
 * and broadcast the list each 5 seconds
 * @author zirenx
 *
 */
public class HostBroadCast extends Thread{
	
	private ArrayList<String> list;

	public HostBroadCast() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<String>();
		addSelf();
	}
	
	/** Add self to the list
	 * 
	 */
	private void addSelf() {
		try {
			String address = InetAddress.getLocalHost().getHostAddress();
			String port = System.getProperty("port");
			add(address, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/** Add a host to the list
	 * in the form "host:port:"
	 * @param s
	 */
	public void add(String s) {
		if (!list.contains(s)) {
			list.add(s);
			String[] strs = s.split(":");
			ConnectionInfo.getInstance().addAvailableServer(strs[0], strs[1]);
		}
	}
	
	public void add(String address, String port) {
		add(address+":"+port+":");
	}
	
	public void run() {
		while (true) {
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PeerListRequest p = new PeerListRequest(list);
			Main.getCommunicationManager().toClients(p.toJSONString());
			Main.getCommunicationManager().toServer(p.toJSONString());
		}
		
	}

}
