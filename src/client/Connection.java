package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import GUI.ConnectionInfo;
import main.Main;
import network.RequestHandler;



/** Connection to a server
 * @author zirenx
 *
 */
public class Connection extends Thread{
	private DataInputStream in;
	private DataOutputStream out;
	private BufferedReader inreader;
	private PrintWriter outwriter;
	private boolean open = false;
	private boolean term=false;
	
	/** Establish a connection to a server
	 * @param address
	 * @param port
	 */
	public Connection(String address, int port) {
		try {
			Socket socket = new Socket(address, port);
			in = new DataInputStream(socket.getInputStream());
		    out = new DataOutputStream(socket.getOutputStream());
		    inreader = new BufferedReader( new InputStreamReader(in));
		    outwriter = new PrintWriter(out, true);
		    open = true;
		    start();
		    
		    ConnectionInfo.getInstance().setConnectEnable(false);
            ConnectionInfo.getInstance().setConnectStatus("Connected");
            ConnectionInfo.getInstance().setDisconnectEnable(true);
            Main.getClient().getLagDetector().setRun(true);
            Main.getClient().getLagDetector().start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ConnectionInfo.getInstance().setConnectStatus(
					"Failed to connect the server");
	    	ConnectionInfo.getInstance().setConnectEnable(true);
	    	Main.getClient().getLagDetector().setRun(false);
			System.err.println("Connect to server Failed:"+e);
		}
		
	}
	

	/** Keep running to receive messages
	 *
	 */
	public void run() {
		try {
			String data;
			while(!term && (data = inreader.readLine())!=null){
				handle(data);
				
			}
			in.close();
		} catch (IOException e) {
			System.err.println("connection closed with exception: "+e);
		}
		open=false;
	}


	/**
	 * returns true if the message was written, otherwise false
	 */
	public boolean writeMsg(String msg) {
		if(open){
			outwriter.println(msg);
			outwriter.flush();
			return true;
		}
		return false;
	}
	
	/** Close the connection
	 * 
	 */
	public void closeCon(){
		if(open){
			try {
				term=true;
				inreader.close();
				out.close();
			} catch (IOException e) {
				// already closed?
				System.err.println("received exception closing the connection");
			}
		}
	}
	
	private void handle(String data) {
		
		RequestHandler rh = new RequestHandler(data);

		if (!rh.isNotRedirect()) {
			Main.getCommunicationManager().toClients(data);
		}
		
		rh.doAction(null);
	}
	

}
