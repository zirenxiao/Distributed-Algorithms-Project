package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import main.ConnectionInfo;
import utils.Settings;

public class Communication {
	static String host;// = System.getProperty("host", Settings.SERVER_ADDRESS);
    static int port;// = Integer.parseInt(System.getProperty("port", Settings.OUT_PORT));
	static EventLoopGroup group = new NioEventLoopGroup();
	private Channel ch;
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
        } catch (Exception e) {
        	ConnectionInfo.getInstance().setConnectStatus("Conncetion Failed");
        	ConnectionInfo.getInstance().setConnectEnable(true);
        }finally {
            // The connection is closed automatically on shutdown.
//            group.shutdownGracefully();
        }
	}
	public Channel getChannel() {
		return ch;
	}
	
	
}
