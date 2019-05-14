package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

import main.Main;

public class Listener extends Thread{
	private ServerSocket serverSocket=null;
	private boolean term = false;
	private boolean initialized = false;
	
	public Listener(){
		
		while (!initialized) {
			System.setProperty("port", String.valueOf(ThreadLocalRandom.current().nextInt(8000, 9000)));
			System.out.println("Try to listening at port "+System.getProperty("port"));
			try {
				serverSocket = new ServerSocket(Integer.parseInt(System.getProperty("port")));
				initialized = true;
			} catch (NumberFormatException | IOException e) {
				initialized = false;
				continue;
			}
		}
		
		start();
	}
	
	@Override
	public void run() {

		while(!term){
			Socket clientSocket;
			try {
				clientSocket = serverSocket.accept();
				Connection newCon = new Connection(clientSocket);
				Main.getServer().inComingCon(newCon);
				Main.getServer().getHandler().initHandle(newCon);
			} catch (IOException e) {
				System.err.println("Exception received, shut down.");
				term=true;
			}
		}
	}

	public void setTerm(boolean term) {
		this.term = term;
		if(term) interrupt();
	}
	
	
}
