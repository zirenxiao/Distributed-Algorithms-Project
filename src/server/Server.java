/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import javax.net.ssl.SSLException;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 *
 * @author NAFIS
 */
public class Server extends Thread{
	private int port;
	private Communication com;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void run() {
		try {
			com = new Communication(port);
		} catch (SSLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public void broadcastToClients(Object obj) {
    	ChannelGroup channels = ServerHandler.channels;
    	for (Channel c: channels) {
    		c.writeAndFlush(obj);
    	}
    }
    
    public void receiveAction(Object obj) {
		
	}
}
