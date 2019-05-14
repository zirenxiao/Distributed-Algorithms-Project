/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.util.ArrayList;

public class Server extends Thread{
	private Listener listener;
	private ArrayList<Connection> conList;
	private Handler handler;
	
	public Server() {
		listener = new Listener();
		conList = new ArrayList<Connection>();
		handler = new Handler();
	}
	
	public void inComingCon(Connection s) {
		System.out.println("New connection connected.");
		conList.add(s);
	}
	
    public void broadcastToClients(String str) {
    	broadcastToClients(str, null);
    }
    
    /** Broadcast message except a connection
     * @param str
     * @param con
     */
    public void broadcastToClients(String str, Connection con) {
    	for (Connection c: conList) {
//    		System.out.println("BTC:"+str);
    		if (con!=null && c.equals(con)) {
    			continue;
    		}
    		c.writeMsg(str);
    	}
    }

	public Handler getHandler() {
		return handler;
	}
    
}
