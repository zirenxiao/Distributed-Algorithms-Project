package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import GUI.ConnectionInfo;
import GUI.NotePadGUI;
import main.Main;

public class Communication {
	private String host;
    private int port;
    private EventLoopGroup group;
	private Channel ch;
	private boolean isConnected = false;
	
	public void connect(String host, int port) {
		this.host = host;
		this.port = port;
		group = new NioEventLoopGroup();
		
		try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .option(ChannelOption.SO_KEEPALIVE, true)
             .handler(new ClientInitializer());
            // Start the connection attempt.
            ch = b.connect(this.host, this.port).sync().channel();
            ConnectionInfo.getInstance().setConnectEnable(false);
            ConnectionInfo.getInstance().setConnectStatus("Connected");
            isConnected = true;
            Main.getClient().getLagDetector().setRun(true);
            Main.getClient().getLagDetector().start();
            NotePadGUI.getInstance().getTextArea().setEditable(true);
        } catch (Exception e) {
        	disconnect("Conncetion Failed");
        }finally {
            // The connection is closed automatically on shutdown.
//            group.shutdownGracefully();
        }
	}
	
	public void disconnect(String msg) {
		ConnectionInfo.getInstance().setConnectStatus(msg);
    	ConnectionInfo.getInstance().setConnectEnable(true);
		NotePadGUI.getInstance().getTextArea().setEditable(false);
    	isConnected = false;
    	Main.getClient().getLagDetector().setRun(false);
    	this.ch.close();
//    	group.shutdownGracefully();
	}
	
	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}



	public boolean isConnected() {
		return isConnected;
	}

	public Channel getChannel() {
		return ch;
	}
	
	
}
