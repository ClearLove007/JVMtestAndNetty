package com.example.jvmtest.netty;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 15:45 2020/1/19
 */
public class BIOFile {
    public static void main(String[] args) {
        input();
    }

    public static void input() {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(8888));

            while (true) {
                Socket accept = serverSocket.accept();
                new Thread(() -> {
                    handler(accept);
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handler(Socket socket) {
        try {
            long start = System.currentTimeMillis();
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            for (; ; ) {
                int read = inputStream.read(bytes);
                if (read == -1) {
                    break;
                }
                System.out.println(new String(bytes, StandardCharsets.UTF_8));
            }
            inputStream.close();
            System.out.println("接收文件耗时: " + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
