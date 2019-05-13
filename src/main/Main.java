package main;

import GUI.ConnectionInfo;
import GUI.EntranceDialog;
import client.Client;
import GUI.NotePadGUI;
import crdt.Crdt;
import network.*;
import server.Server;

public class Main {
	private static Client client;
	private static Server server;
	private static ICommunicationManager communicationManager;
	private static Crdt data;

	public static void main(String[] args) {
		System.setProperty("broadcastPort", "4445");
		System.setProperty("certPath", "certificates/cert.pem");
		System.setProperty("pkPath", "certificates/key.pem");
		new EntranceDialog();
		establishConnections();
//		CrdtTests.testGetNodePosition();
	}
	
	private static void establishConnections() {
		
		
		// create a thread to run the server
		server = new Server();
		server.start();

		client = new Client();
		
		// broadcast send && receive service
		new LocalNetworkDiscoveryService().start();
		new LocalNetworkDiscoveryBroadcast().start();
		
		// show connection info
		ConnectionInfo.getInstance();

		// initialize the Crdt (Model/Controller) and NotePadGUI (View)
		init();
	}

	private static void init(){
        communicationManager = new NetworkManager();
		data = new Crdt(communicationManager);
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

	public static Crdt getCRDT() {
		return data;
	}
	

}
