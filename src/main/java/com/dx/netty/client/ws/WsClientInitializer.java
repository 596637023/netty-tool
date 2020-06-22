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
        // ��������Ӧ����Ϣ������߽���ΪHTTP��Ϣ
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        // �ͻ���Handler
        pipeline.addLast("handler", wsClientHandler);
    }
}
