package com.dx.nio;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author dx
 * @version 1.0
 * @date 2020/7/15 0015 11:01
 * nio操作文件得工具类
 */
@Slf4j
public class FileUtil {

    public static int MAX_COPY_SIZE_PER=1024*2;

    public static void readFileByBio(String path) {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(path));
            byte[] ff = new byte[1024];
            while (in.read(ff) != -1) {
                System.out.println(new String(ff, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void readFileByNio(String path) {
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(path, "rw");
            FileChannel fc = rf.getChannel();
            ByteBuffer bf = ByteBuffer.allocate(1024);
            int read = -1;
            while ((read = fc.read(bf)) != -1) {
                bf.clear();
                byte[] bytes = bf.array();
                System.out.println(new String(bytes, 0, read, "UTF-8"));
            }
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (rf != null) {
                try {
                    rf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用MappedByteBuffer复制大文件，超过2G文件之后就不能一次性复制了，所以每次复制最大限制不能超过2G
     * @param sourcePath
     * @param targetPath
     * @param copySizePer
     */
    public static void copyFileByMappedByteBuffer(String sourcePath, String targetPath, int copySizePer) {
        if (copySizePer>=MAX_COPY_SIZE_PER){
            log.error("单次复制文件大小不能超过2G");
            return;
        }
        Long start = System.currentTimeMillis();
        Long len = Long.valueOf(1024 * 1024 * copySizePer);
        Long souceFileSize = 0L;
        Long position = 0L;
        RandomAccessFile source = null;
        RandomAccessFile target = null;
        try {
            source = new RandomAccessFile(sourcePath, "rw");
            FileChannel sourceFc = source.getChannel();
            target = new RandomAccessFile(targetPath, "rw");
            FileChannel targetFc = target.getChannel();
            souceFileSize = sourceFc.size();
            while (position < souceFileSize) {
                if (souceFileSize - position > 0 && souceFileSize - position < len) {
                    len = souceFileSize - position;
                }
                MappedByteBuffer mbS = sourceFc.map(FileChannel.MapMode.READ_WRITE, position, len);
                MappedByteBuffer mbT = targetFc.map(FileChannel.MapMode.READ_WRITE, position, len);
                for (int i = 0; i < len; i++) {
                    mbT.put(i, mbS.get(i));
                }
                position += len;
                log.debug("当前使用MappedByteBuffer复制文件大小:{} M",position / (1024 * 1024));
            }
            log.info("使用MappedByteBuffer复制完毕,文件大小:{},耗时: {} 毫秒",position,System.currentTimeMillis() - start);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (target != null) {
                try {
                    target.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用nioMapper一次性复制大概受限于最大2G得文件所以最好还是分段
     *
     * @param sourcePath
     * @param targetPath
     * @param copySizePer 每次复制文件大小 (M)
     */
    public static void copyFileByTransferTo(String sourcePath, String targetPath, int copySizePer) {
        if (copySizePer>=MAX_COPY_SIZE_PER){
            log.error("单次复制文件大小不能超过2G");
            return;
        }
        Long start = System.currentTimeMillis();
        RandomAccessFile sourceFile = null;
        RandomAccessFile targetFile = null;
        //一次复制256M
        Long len = Long.valueOf(1024 * 1024 * copySizePer);
        Long position = 0L;
        try {
            sourceFile = new RandomAccessFile(sourcePath, "rw");
            targetFile = new RandomAccessFile(targetPath, "rw");
            FileChannel sourceFc = sourceFile.getChannel();
            FileChannel targetFc = targetFile.getChannel();
            Long fileSize = sourceFile.length();
            while (position < fileSize) {
                //当读取到文件最后之后
                if (fileSize - position > 0 && fileSize - position < len) {
                    len = fileSize - position;
                }
                sourceFc.transferTo(position, len, targetFc);
                position += len;
                log.debug("当前使用TransferTo复制文件大小:{} M",position / (1024 * 1024));
            }
            log.info("使用MappedByteBuffer复制完毕,文件大小:{},耗时: {} 毫秒",position,System.currentTimeMillis() - start);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String soucePath = "D:\\work\\tool\\anzhuangbao\\aa.zip";
        String targetPath = "D:\\work\\tool\\anzhuangbao\\aa" + System.currentTimeMillis() + ".zip";
        //测试结果：2G文件花费 5676毫秒
        copyFileByMappedByteBuffer(soucePath,targetPath,1024*2);
        //测试结果：2G文件花费 23081毫秒
        copyFileByTransferTo(soucePath, targetPath, 1024*2);
    }
}
