package com.dx.netty.client.ws;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


/**
 * @author dx
 * @version 1.0
 * @date 2020/6/22 0022 11:09
 */
public class WsClientInitializer extends ChannelInitializer<SocketChannel> {

    private WsClientHandler wsClientHandler;

    WsClientInitializer(WsClientHandler wsClientHandler) {
        this.wsClientHandler = wsClientHandler;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        // 将请求与应答消息编码或者解码为HTTP消息
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        // 客户端Handler
        pipeline.addLast("handler", wsClientHandler);
    }
}
