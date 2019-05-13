/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.net.BindException;
import java.util.concurrent.ThreadLocalRandom;

import javax.net.ssl.SSLException;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

/**
 *
 * @author NAFIS
 */
public class Server extends Thread{
	private Communication com;
	
	public void run() {
		while (true){
			try {
				System.setProperty("port", String.valueOf(ThreadLocalRandom.current().nextInt(8000, 9000)));
				System.out.println("Try to listening at port "+System.getProperty("port"));
				com = new Communication(Integer.parseInt(System.getProperty("port")));
			} catch (SSLException | InterruptedException e) {
				// TODO Auto-generated catch block
				System.err.println("Server start failed, with error '" +e.getMessage()+"'. System exit.");
				System.exit(0);
			} catch(BindException e) {
				continue;
			}
		}
		
	}
	
    public void broadcastToClients(String str) {
    	ChannelGroup channels = ServerHandler.channels;
    	for (Channel c: channels) {
//    		System.out.println("BTC:"+str);
    		c.writeAndFlush(str);
    	}
    }
    
    public void receiveAction(Object obj) {
		
	}
}
