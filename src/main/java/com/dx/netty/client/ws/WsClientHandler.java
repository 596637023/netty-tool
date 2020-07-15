package com.dx.netty.client.ws;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author dx
 * @version 1.0
 * @date 2020/6/22 0022 11:10
 */
@Slf4j
public class WsClientHandler extends SimpleChannelInboundHandler<ByteBuffer> {


    private final WebSocketClientHandshaker webSocketClientHandshaker;

    public WsClientHandler(WebSocketClientHandshaker webSocketClientHandshaker) {
        this.webSocketClientHandshaker = webSocketClientHandshaker;
    }

    /**
     * ���ͻ����������ӷ���˵����Ӻ󣬵��ô˷���
     *
     * @param channelHandlerContext ChannelHandlerContext
     */
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        log.info("\n\t???????????????????\n" +
                "\t�� [Mock ��������]\n" +
                "\t???????????????????");

        Channel channel = channelHandlerContext.channel();
        // ����
        webSocketClientHandshaker.handshake(channel);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuffer data) {
        String msg = new String(data.array(), CharsetUtil.UTF_8);
        log.info("���յ��ͻ��˵���ӦΪ:{}", msg);
        //�Զ��崦����Ϣ
        context.channel().writeAndFlush(new TextWebSocketFrame("�Ǻ�"));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("\n\t???????????????????\n" +
                "\t�� [exception]: {}\n" +
                "\t???????????????????", cause.getMessage());
        ctx.close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("��������˶Ͽ�����");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.flush();
    }
}
