package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import main.Main;



/** Information of each connection.
 * @author zirenx
 *
 */
public class Connection extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private BufferedReader inreader;
	private PrintWriter outwriter;
	private boolean open = false;
	private Socket socket;
	private boolean term=false;
	
	/** Construct a connection from a socket
	 * @param socket
	 * @throws IOException
	 */
	public Connection(Socket socket) throws IOException{
		in = new DataInputStream(socket.getInputStream());
	    out = new DataOutputStream(socket.getOutputStream());
	    inreader = new BufferedReader( new InputStreamReader(in));
	    outwriter = new PrintWriter(out, true);
	    this.socket = socket;
	    open = true;
	    start();
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
	
	/** Close this connection
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
				Main.getServer().removeFromList(
						Main.getServer().getIndex(this));
			}
		}
	}
	
	
	/** Keep running and receiving messages from this client
	 * If connection close method is called, this will be 
	 * terminated
	 */
	public void run(){
		try {
			String data;
			while(!term && (data = inreader.readLine())!=null){
//				term=Control.getInstance().process(this,data);
				Main.getServer().getHandler().handle(data, this);
			}

			in.close();
		} catch (IOException e) {

//			connectionClosed(this);
		}
		open=false;
	}

	public Socket getSocket() {
		return socket;
	}
}
