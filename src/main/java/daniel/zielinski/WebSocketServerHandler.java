package daniel.zielinski;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    static final List<Channel> channels = new ArrayList<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            String request = ((TextWebSocketFrame) frame).text();
            log.info("Received request: " + request);
            channels.forEach(channel -> {
                log.info("Sending response to client {}", channel);
                channel.writeAndFlush(new TextWebSocketFrame("Response:" + request));
            });

        } else {
            throw new UnsupportedOperationException("Unsupported frame type: " + frame.getClass().getName());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("WebSocket Client connected channel={}", ctx.channel());
        channels.add(ctx.channel());
        log.info("Number of clients {}", channels.size());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("WebSocket Client disconnected channel={}", ctx.channel());
        channels.remove(ctx.channel());
        log.info("Number of clients {}", channels.size());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Closing connection for client {}", ctx.channel(), cause);
        ctx.close();
    }
}

