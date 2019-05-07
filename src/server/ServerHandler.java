package server;

import crdt.Operation;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import io.netty.util.concurrent.GlobalEventExecutor;
import main.ConnectionInfo;
import main.Main;
import network.Request;
import utils.Settings;

/**
 * Handles a server-side channel.
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
    	// when a client is connected, add to channel 
    	// group and print in the GUI
    	channels.add(ctx.channel());
    	ConnectionInfo.getInstance().addClientConnection(ctx.channel().remoteAddress().toString());
//    	Main.getCommunicationManager().serverChannelActiveAction();
    	Request r = new Request(Main.getCRDT().getDoc());
    	ctx.writeAndFlush(Settings.requestToString(r));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	ConnectionInfo.getInstance().delClientConnection(ctx.channel().remoteAddress().toString());
        cause.printStackTrace();
        ctx.close();
    }

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// when received a message from a client, redirect
    	// to all other peers and run the message
//    	Settings.jsonToOperation(Settings.operationToJSON((Operation) msg));
    	
//    	System.out.println("sv:"+msg);
    	
    	Request op = Settings.stringToRequest(msg);
    	
    	for (Channel c: channels) {
    		// first send to all connected clients (children)
    		if (c != ctx.channel()) {
    			// make sure don't send back to the client
    			c.writeAndFlush(msg);
    		}
    	}
    	// then send to the server (parent)
    	Main.getCommunicationManager().toServer(msg);
    	// run the action
    	Main.getCommunicationManager().receiveAction(op);
	}
}
