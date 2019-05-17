/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.util.ArrayList;

import GUI.ConnectionInfo;

/** Server
 * @author zirenx
 *
 */
public class Server extends Thread{
	private Listener listener;
	private ArrayList<Connection> conList;
	private Handler handler;
	
	public Server() {
		listener = new Listener();
		conList = new ArrayList<Connection>();
		handler = new Handler();
	}
	
	/** Actions when received a new connection
	 * @param s
	 */
	public void inComingCon(Connection s) {
		System.out.println("New connection connected.");
		ConnectionInfo.getInstance().addClientConnection(
				s.getSocket().getRemoteSocketAddress().toString());
		conList.add(s);
	}
	
    /** Broadcast to all connected clients
     * @param str
     */
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
    
	/** Disconnect all clients
	 * 
	 */
	public void disconnectAllClients() {
		for (int i=0; i<conList.size(); i++) {
			conList.get(i).closeCon();
			conList.remove(i);
		}
	}
	
	public void removeFromList(Connection con) {
		conList.remove(con);
	}
}
