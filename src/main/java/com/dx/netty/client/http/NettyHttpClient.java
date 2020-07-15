package com.dx.netty.client.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

public class NettyHttpClient {

    public void connect(String host, int port) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel channel) throws Exception {
                // �ͻ��˽��յ�����httpResponse��Ӧ������Ҫʹ��HttpResponseDecoder���н���
                channel.pipeline().addLast(new HttpResponseDecoder());
                // �ͻ��˷��͵���httprequest������Ҫʹ��HttpRequestEncoder���б���
                channel.pipeline().addLast(new HttpRequestEncoder());
                channel.pipeline().addLast(new NettyHttpClientInboundHandler());
            }
        });

        // Start the client.
        ChannelFuture f = b.connect(host, port).sync();
    }

    public static void main(String[] args) throws Exception {
        NettyHttpClient client = new NettyHttpClient();
        client.connect("10.8.0.46", 60004);
    }
}
