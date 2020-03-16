package com.example.jvmtest.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 14:01 2020/1/17
 */
public class BIO {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 8888));
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handle(socket)).start();
            }
        } catch (IOException e) {
            System.err.println(e);
        } finally {

        }
    }

    static void handle(Socket s) {
        try {
            byte[] bytes = new byte[1024];
            int len = s.getInputStream().read(bytes);
            System.out.println(new String(bytes, 0, len));
            s.shutdownInput();
            s.close();
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            s = null;
        }
    }
}
