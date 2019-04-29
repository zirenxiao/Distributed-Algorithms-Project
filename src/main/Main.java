package main;

import client.Client;
import server.Server;

public class Main {
	private static Client client;
	private static Server server;
	
	public static void main(String[] args) {
		establishConnections(8888);
		
	}
	
	/**
	 * @param selfPort A port to allow connections from other clients
	 * @param serverAddress Connect to a server via this address
	 * @param serverPort Connect to a server via this port
	 */
	private static void establishConnections(int selfPort) {
		// show connection info
		ConnectionInfo.getInstance();
		
		// create a thread to run the server
		server = new Server(selfPort);
		server.start();
				
		// run client
		client = new Client();
		
	}

	public static Client getClient() {
		return client;
	}

	public static Server getServer() {
		return server;
	}

}
