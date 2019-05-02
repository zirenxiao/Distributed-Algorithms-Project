package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import main.Main;


/**
 * Handles a client-side channel.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
		// when receive a message from a server (parent)
		// redirect to all other clients (children)
		// and run the action
		Main.getServer().broadcastToClients(msg);
		Main.getServer().receiveAction(msg);
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
    	
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
    	
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
    public static void sentToServer(Object obj) {
    	
    }
    
}
