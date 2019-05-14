package network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import GUI.ConnectionInfo;
import main.Main;

public class HostBroadCast extends Thread{
	
	private ArrayList<String> list;

	public HostBroadCast() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<String>();
		addSelf();
	}
	
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
