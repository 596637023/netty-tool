package com.dx.bio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;

/**
 * @author dx
 * @version 1.0
 * @date 2020/7/15 0015 16:11
 */
public class BioClient {

    private String host;

    private int port;


    public BioClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress(host, port));
        //获取通道
        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\work\\tool\\anzhuangbao\\openvpn-install-2.4.7-I606-Win10.exe"));
        ByteBuffer bf = ByteBuffer.allocate(1024);
        while (fileChannel.read(bf) != -1) {
            bf.flip();
            sc.write(bf);
            bf.clear();
        }
        sc.shutdownOutput();
        int len = 0;
        while ((len = sc.read(bf)) != -1) {
            bf.flip();
            System.out.println(new String(bf.array(), 0, len));
            bf.clear();
        }
        fileChannel.close();
        sc.close();
    }

    public static void main(String[] args) throws IOException {
        new BioClient("127.0.0.1", 6666).start();
    }
}
