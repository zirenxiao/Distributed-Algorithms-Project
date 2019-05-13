package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import main.Main;
import network.RequestHandler;

/**
 * Handles a client-side channel.
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
	
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

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, String msg) throws Exception {
		// when receive a message from a server (parent)
		// redirect to all other clients (children)
		// and run the action
		
//		System.out.println("cl:"+msg);
				
		RequestHandler rh = new RequestHandler(msg);

		if (!rh.isNotRedirect()) {
			Main.getCommunicationManager().toClients(msg);
		}
		
		rh.doAction(arg0);
	}

    
}
