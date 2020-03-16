package com.example.jvmtest.netty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 10:14 2020/1/19
 */
public class NIOFileChannel {
    public static final String path = "H:\\helloWorld.txt";

    public static final String targetPath = "H:\\helloWorld_copy.txt";

    public static void main(String[] args) {
        inPut(path);
    }

    public static void outPut(String text, String path) {

        try {
            FileOutputStream outputStream = new FileOutputStream(path);

            FileChannel channel = outputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(text.getBytes(StandardCharsets.UTF_8).length);

            byteBuffer.put(text.getBytes(StandardCharsets.UTF_8));

            byteBuffer.flip();

            channel.write(byteBuffer);

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String inPut(String path) {
        System.out.println("开始读取文件...");
        StringBuilder result = new StringBuilder();
        try {
            File file = new File(path);

            FileInputStream inputStream = new FileInputStream(file);

            FileChannel channel = inputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

            while (true) {
                int read = channel.read(byteBuffer);
                if (read == -1) {
                    break;
                }
                result.append(new String(byteBuffer.array(), StandardCharsets.UTF_8));
                byteBuffer.clear();
            }

            System.out.println(result);

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("读取文件结束...");
        }
        return result.toString();
    }


    public static void rebuild(String sourcePath, String text) {
        try {
            File sourceFile = new File(sourcePath);
            FileInputStream inputStream = new FileInputStream(sourceFile);
            FileChannel sourceChannel = inputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) sourceFile.length());
            sourceChannel.read(byteBuffer);
            System.out.println(new String(byteBuffer.array(), StandardCharsets.UTF_8));
            byteBuffer.clear();
            sourceChannel.write(byteBuffer.put(text.getBytes(StandardCharsets.UTF_8)));
            inputStream.close();
        } catch (IOException e) {

        }
    }
}
