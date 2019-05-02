package main;

import client.Client;
import client.NotePadGUI;
import crdt.Crdt;
import crdt.IMessageHandler;
import crdt.Operation;
import network.ICommunicationManager;
import server.Server;

public class Main {
	private static Client client;
	private static Server server;
	
	public static void main(String[] args) {
		processArgs(args);
	}
	
	private static void processArgs(String[] args) {
		// args[0] = server port, default = 8888
		int port;
		if (args.length==0) {
			port = 8888;
		}else {
			port = Integer.parseInt(args[0]);
		}
		System.err.println("Server starts at port " + port);
		establishConnections(port);
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
				

		// initialize the Crdt (Model/Controller) and NotePadGUI (View)
		init();
	}

	private static void init(){
		ICommunicationManager communicationManager = new ICommunicationManager() {
			@Override
			public void broadcastMessage(Operation o) {

			}

			@Override
			public void handleIncomingMessage(IMessageHandler messageHandler) {
				messageHandler.handle(null);
			}
		}; //ToDO: It is just a stub. Remove it after proper implementation.
		Crdt data = new Crdt(communicationManager);
		NotePadGUI.init(data);

		client = new Client();
		
	}

	public static Client getClient() {
		return client;
	}

	public static Server getServer() {
		return server;
	}

}
