package server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import io.netty.util.concurrent.GlobalEventExecutor;
import main.ConnectionInfo;
import main.Main;

/**
 * Handles a server-side channel.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
    	// when a client is connected, add to channel 
    	// group and print in the GUI
    	channels.add(ctx.channel());
    	ConnectionInfo.getInstance().addClientConnection(ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	// when received a message from a client, redirect
    	// to all other peers and run the message
    	for (Channel c: channels) {
    		// first send to all connected clients (children)
    		if (c != ctx.channel()) {
    			// make sure don't send back to the client
    			c.writeAndFlush(msg);
    		}
    	}
    	// then send to the server (parent)
    	Main.getClient().sentToServer(msg);
    	// run the action
    	Main.getServer().receiveAction(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	ConnectionInfo.getInstance().delClientConnection(ctx.channel().remoteAddress().toString());
        cause.printStackTrace();
        ctx.close();
    }
}
