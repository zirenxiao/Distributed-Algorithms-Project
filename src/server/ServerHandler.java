/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package server;

import java.net.InetAddress;

import org.json.simple.JSONObject;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import settings.Settings;

/**
 * Handles a server-side channel.
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    @Override
                    public void operationComplete(Future<Channel> future) throws Exception {
                        ctx.writeAndFlush(serverMessage("You are now connected to " + InetAddress.getLocalHost().getHostName()) + "\n");
                        
                        broadcast(ctx, serverMessage("A new client has joined.") + "\n");
                        channels.add(ctx.channel());
                    }
        });
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    	JSONObject content = Settings.stringToJson(msg);
        if (content!=null) {
			
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
    @SuppressWarnings("unchecked")
	private String serverMessage(String msg) {
    	JSONObject obj = new JSONObject();
		obj.put(Settings.SERVER, msg);
		return obj.toJSONString();
    }
    
    private void broadcast(ChannelHandlerContext ctx, String msgToOthers) {
    	broadcast(ctx, msgToOthers, null);
    }
    
    private void broadcast(ChannelHandlerContext ctx, String msgToOthers, String msgToEcho) {
    	for (Channel c: channels) {
            if (c != ctx.channel()) {
                c.writeAndFlush(msgToOthers + '\n');
            } else {
            	if (msgToEcho!=null) {
            		c.writeAndFlush(msgToEcho + '\n');
            	}
            }
        }
    }
}
