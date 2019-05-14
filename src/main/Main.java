package main;

import GUI.ConnectionInfo;
import GUI.EntranceDialog;
import client.Client;
import GUI.NotePadGUI;
import crdt.Crdt;
import network.*;
import server.Server;
import tests.PathTests;

public class Main {
	private static Client client;
	private static Server server;
	private static ICommunicationManager communicationManager;
	private static Crdt data;
	private static HostBroadCast hb;

	public static void main(String[] args) {
		System.setProperty("broadcastPort", "4445");
		System.setProperty("certPath", "certificates/cert.pem");
		System.setProperty("pkPath", "certificates/key.pem");
//		new EntranceDialog();
		establishConnections();
//		CrdtTests.testGetNodePosition();
	}
	
	private static void establishConnections() {
		
		
		// create a thread to run the server
		server = new Server();
		server.start();

		// show connection info
		ConnectionInfo.getInstance();
		
		
		client = new Client();
		
		// broadcast send && receive service
		new LocalNetworkDiscoveryService().start();
		new LocalNetworkDiscoveryBroadcast().start();
		
		

		// initialize the Crdt (Model/Controller) and NotePadGUI (View)
		communicationManager = new NetworkManager();
		data = new Crdt(communicationManager);
		NotePadGUI.getInstance().init(data);
		
		hb = new HostBroadCast();
		hb.start();
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

	public static HostBroadCast getHostBroadCast() {
		return hb;
	}
	

}
