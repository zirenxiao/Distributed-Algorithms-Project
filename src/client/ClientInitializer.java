package client;

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
public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public ClientInitializer() throws SSLException {
    	// Configure SSL.
    	File cert = new File(Settings.CERT_PATH);
        this.sslCtx = SslContextBuilder.forClient().trustManager(cert).build();
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(sslCtx.newHandler(ch.alloc(), Communication.getHost(), Communication.getPort()));
//        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new Decoder());
        pipeline.addLast(new Encoder());

        // and then business logic.
        pipeline.addLast(new ClientHandler());
    }
}
