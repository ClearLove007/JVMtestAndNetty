package com.example.jvmtest.bio;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 14:23 2020/1/17
 */
public class BIOclient {
    public static void main(String[] args) {
        write();
    }

    static void write() {
        try {
            Socket socket = new Socket("127.0.0.1", 8888);
            socket.getOutputStream().write(Thread.currentThread().getName().getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            System.err.println(e);
        } finally {

        }
    }
}
