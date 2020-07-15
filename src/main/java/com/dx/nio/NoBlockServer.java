package com.dx.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;

/**
 * @author dx
 * @version 1.0
 * @date 2020/7/15 0015 16:38
 */
public class NoBlockServer {

    private int port;

    public NoBlockServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(port));
        Selector selector = Selector.open();
        //将通道注册到选择器上面，指定接收得监听通道
        server.register(selector, SelectionKey.OP_ACCEPT);
        while (selector.select()>0){
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator=selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey=iterator.next();
                if (selectionKey.isAcceptable()){
                    //获取到客户端连接
                    try {
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        //监听通道读就绪时间
                        client.register(selector,SelectionKey.OP_READ);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (selectionKey.isReadable()){
                    try {
                        SocketChannel channel =(SocketChannel) selectionKey.channel();
                        ByteBuffer buffer=ByteBuffer.allocate(1024);
                        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\work\\tool\\anzhuangbao\\openvpn-install-2.4.7-I606-Win10.exe"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
                        while (channel.read(buffer)>0){
                            buffer.flip();
                            fileChannel.write(buffer);
                            buffer.clear();
                        }
                        fileChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                iterator.remove();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        new NoBlockServer(6666).start();
    }
}
