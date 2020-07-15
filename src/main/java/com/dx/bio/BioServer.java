package com.dx.bio;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author dx
 * @version 1.0
 * @date 2020/7/15 0015 16:15
 */
public class BioServer {

    private int port;

    public BioServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocketChannel server=ServerSocketChannel.open();
        server.bind(new InetSocketAddress(port));
        FileChannel fileChannel=FileChannel.open(Paths.get("D:\\work\\tool\\anzhuangbao\\openvpn1111.exe"), StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        ByteBuffer bf=ByteBuffer.allocate(1024);
        SocketChannel sc=server.accept();
        while(sc.read(bf)!=-1){
            bf.flip();
            fileChannel.write(bf);
            bf.clear();
        }
        bf.put("file is success".getBytes());
        bf.flip();
        sc.write(bf);
        sc.close();
        fileChannel.close();
        server.close();
    }

    public static void main(String[] args) throws IOException {
        new BioServer(6666).start();
    }

}
