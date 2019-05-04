package network;

import java.io.*;
import java.net.*;

public class LocalNetworkDiscoveryBroadcast extends Thread{
	
	public LocalNetworkDiscoveryBroadcast() {
		
	}
 
    public void run() {
    	while (true) {
    		try {
    			String address = InetAddress.getLocalHost().getHostAddress();
    			String port = System.getProperty("port");
    			broadcast(address+":"+port, InetAddress.getByName("255.255.255.255"));
    			sleep(5000);
    		} catch (UnknownHostException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public void broadcast(String broadcastMessage, InetAddress address) throws IOException {
    	DatagramSocket socket = null;
    	socket = new DatagramSocket();
    	socket.setBroadcast(true);
    	 
    	byte[] buffer = broadcastMessage.getBytes();
    	 
    	DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Integer.parseInt(System.getProperty("broadcastPort")));
    	socket.send(packet);
    	socket.close();
    }
    
	public void search() {
		int timeout=100;
		try {
			String hostAddress = getSubnet(InetAddress.getLocalHost().getHostAddress());
			for (int i=1;i<255;i++){
				String host=hostAddress + i;
			    try {
					if (InetAddress.getByName(host).isReachable(timeout)){
					    System.out.println(host + " is reachable");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private String getSubnet(String hostAddress) {
		int count = 0;
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<hostAddress.length(); i++) {
			if (count<=2) {
				sb.append(hostAddress.charAt(i));
			}else {
				break;
			}
			if (hostAddress.charAt(i) == '.') {
				count++;
			}
		}
		return sb.toString();
	}

}
