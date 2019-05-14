package network;

import java.io.*;
import java.net.*;

public class LocalNetworkDiscoveryBroadcast extends Thread{
	
	public LocalNetworkDiscoveryBroadcast() {
		start();
	}
 
    public void run() {
    	while (true) {
    		try {
    			String address = InetAddress.getLocalHost().getHostAddress();
    			String port = System.getProperty("port");
    			broadcast(address+":"+port+":", InetAddress.getByName("255.255.255.255"));
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

}
