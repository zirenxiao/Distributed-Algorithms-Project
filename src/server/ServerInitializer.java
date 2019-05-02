package server;

import java.io.File;

import javax.net.ssl.SSLException;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import utils.Decoder;
import utils.Encoder;
import utils.Settings;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public ServerInitializer() throws SSLException {
    	File cert = new File(Settings.CERT_PATH);
        File privateKey = new File(Settings.PRIVATE_KEY_PATH);
        sslCtx = SslContextBuilder.forServer(cert, privateKey).build();
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));
//        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new Decoder());
        pipeline.addLast(new Encoder());

        // and then business logic.
        pipeline.addLast(new ServerHandler());
    }
}
