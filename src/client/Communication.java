package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import main.ConnectionInfo;

public class Communication {
	private static String host;// = System.getProperty("host", Settings.SERVER_ADDRESS);
    private static int port;// = Integer.parseInt(System.getProperty("port", Settings.OUT_PORT));
    private static EventLoopGroup group = new NioEventLoopGroup();
	private Channel ch;
	private boolean isConnected = false;
	public Communication(String host, int port) {
		Communication.host = host;
		Communication.port = port;
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ClientInitializer());
            // Start the connection attempt.
            ch = b.connect(Communication.host, Communication.port).sync().channel();
            ConnectionInfo.getInstance().setConnectEnable(false);
            ConnectionInfo.getInstance().setConnectStatus("Connected");
            isConnected = true;
            
        } catch (Exception e) {
        	disconnect("Conncetion Failed");
        }finally {
            // The connection is closed automatically on shutdown.
//            group.shutdownGracefully();
        }
	}
	
	private void disconnect(String msg) {
		ConnectionInfo.getInstance().setConnectStatus(msg);
    	ConnectionInfo.getInstance().setConnectEnable(true);
    	isConnected = false;
    	group.shutdownGracefully();
    	group = new NioEventLoopGroup();
	}
	
	public static String getHost() {
		return host;
	}



	public static int getPort() {
		return port;
	}



	public boolean isConnected() {
		return isConnected;
	}

	public Channel getChannel() {
		return ch;
	}
	
	
}
