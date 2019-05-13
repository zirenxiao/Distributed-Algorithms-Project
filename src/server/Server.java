/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.net.BindException;

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
		for (int i=8000; i<=8200; i++) {
			try {
				System.setProperty("port", String.valueOf(i));
				com = new Communication(i);
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
