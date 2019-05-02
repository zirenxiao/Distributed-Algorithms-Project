package client;

import org.json.simple.JSONObject;

import crdt.Operation;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import main.Main;
import utils.Settings;


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
    
    public static void sentToServer(Object obj) {
    	
    }

	@Override
	protected void channelRead0(ChannelHandlerContext arg0, String msg) throws Exception {
		// when receive a message from a server (parent)
		// redirect to all other clients (children)
		// and run the action
				
//		System.out.println("cl:"+msg);
		Operation op = Settings.stringToOperation(msg);
				
		Main.getServer().broadcastToClients(msg);
		Main.getCRDT().sync(op);
	}

    
}
