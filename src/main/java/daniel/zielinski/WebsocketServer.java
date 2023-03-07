package daniel.zielinski;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebsocketServer {

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            log.info("Starting websocket chat server........");
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketServerInitializer());
            ChannelFuture chf = b.bind(5544).sync();
            log.info("!!!!Netty websocket chat server is Alive!!!!");
            chf.channel().closeFuture().sync();

        } finally {
            log.info("Shutting down websocket chat server........");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}