package com.dx.netty.client.ws;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public final class WebSocketClient {

    public void connect(String url) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            URI uri = new URI(url);
            Bootstrap bootstrap = new Bootstrap();
            WsClientHandler webSocketClientHandler = new WsClientHandler(
                    WebSocketClientHandshakerFactory.newHandshaker(uri
                            , WebSocketVersion.V13
                            , null
                            , false
                            , new DefaultHttpHeaders()));
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).
                    handler(new WsClientInitializer(webSocketClientHandler));
            Channel channel = bootstrap.connect(uri.getHost(), uri.getPort()).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException | URISyntaxException e) {
            log.error("socket¡¨Ω”“Ï≥£:{}", e);
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new WebSocketClient().connect("ws://10.8.0.46:8212/test/hello");
    }
}