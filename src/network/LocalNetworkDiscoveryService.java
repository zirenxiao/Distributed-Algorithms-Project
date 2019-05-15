package network;

import java.io.IOException;
import java.net.*;

import GUI.ConnectionInfo;

/** Enable receiving host message from
 * other peers in the same sub-network
 * @author zirenx
 *
 */
public class LocalNetworkDiscoveryService extends Thread {
	 
    private DatagramSocket socket;
    private byte[] buf = new byte[50];
 
    /** Start the discovery service
     * 
     */
    public LocalNetworkDiscoveryService() {
        try {
			socket = new DatagramSocket(
					Integer.parseInt(System.getProperty("broadcastPort")));
			start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println("Port "
			+ System.getProperty("broadcastPort") + 
			" is in use. Local Network Discovery Service disabled.");
		}
    }
 
    public void run() {
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
             
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength());

            String[] split = received.split(":");
            ConnectionInfo.getInstance().addAvailableServer(split[0], split[1]);
        }
    }
}