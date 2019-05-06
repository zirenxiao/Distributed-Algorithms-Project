package main;

import client.Client;
import client.NotePadGUI;
import crdt.Crdt;
import network.*;
import server.Server;
import tests.CrdtTests;

public class Main {
	private static Client client;
	private static Server server;
	private static ICommunicationManager communicationManager;

	public static void main(String[] args) {
		processArgs(args);
		CrdtTests.testGetNodePosition();
	}
	
	private static void processArgs(String[] args) {
		// args[0] = server port
		if (args.length==0) {
			System.err.println("You have to add server port at running.");
			System.err.println("Usage: /app_name port");
			System.exit(0);
		}else {
			System.setProperty("port", args[0]);
		}
		System.setProperty("broadcastPort", "4445");
		establishConnections(Integer.parseInt(System.getProperty("port")));
	}
	
	/**
	 * @param selfPort A port to allow connections from other clients
	 */
	private static void establishConnections(int selfPort) {
		// show connection info
		ConnectionInfo.getInstance();
		
		// create a thread to run the server
		server = new Server(selfPort);
		server.start();

		client = new Client();
		
		// broadcast send && receive service
		new LocalNetworkDiscoveryService().start();
		new LocalNetworkDiscoveryBroadcast().start();

		// initialize the Crdt (Model/Controller) and NotePadGUI (View)
		init();
	}

	private static void init(){
        communicationManager = new NetworkManager();
		Crdt data = new Crdt(communicationManager);
		NotePadGUI.init(data);
	}


	public static Client getClient() {
		return client;
	}

	public static Server getServer() {
		return server;
	}

	public static ICommunicationManager getCommunicationManager() {
		return communicationManager;
	}
	

}
