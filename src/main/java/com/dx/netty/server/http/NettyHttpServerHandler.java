package com.dx.netty.server.http;

import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.net.URI;

public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyHttpServerHandler.class);
    private HttpRequest request;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        URI url = new URI("/%E9%84%82ARM004.2.2.0.MTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQzNA==");
        String meg = "hello";

        //����HttpRequest���������ݺ�һЩ������Ϣ
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_0, HttpMethod.GET, url.toASCIIString(), Unpooled.wrappedBuffer(meg.getBytes("UTF-8")));

        request.headers()
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                //����������
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                //���ô����������ݵĳ���
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        //��������
        ctx.writeAndFlush(request);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            request = (HttpRequest) msg;

            String uri = request.uri();
            System.out.println("Uri:" + uri);
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(CharsetUtil.UTF_8));
            buf.release();

            String res = "�յ���Ϣ��";
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(res.getBytes(CharsetUtil.UTF_8)));

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            if (HttpUtil.isKeepAlive(request)) {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            //ctx.write(response);
            //ctx.flush();
            ctx.writeAndFlush(response);
            //�����ظ��ͻ����κζ���
            //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause.getMessage());
        ctx.close();
    }
}
