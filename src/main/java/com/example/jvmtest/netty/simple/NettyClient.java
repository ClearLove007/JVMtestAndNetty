package com.example.jvmtest.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 9:45 2020/1/20
 */
public class NettyClient {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class);

        //异步启动客户端
//        ChannelFuture channelFuture;
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            bootstrap.handler(new ChannelInitHandler(message));
            bootstrap.connect("127.0.0.1", 8080).sync();
        }
        //监听关闭通道事件
//        channelFuture.channel().closeFuture().sync();

    }

    private static class ChannelInitHandler extends ChannelInitializer<SocketChannel> {
        private String message;

        public ChannelInitHandler(String message) {
            this.message = message;
        }

        //为pipeline设置处理器
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyClientHandler(message));
        }
    }

    private static class NettyClientHandler extends ChannelHandlerAdapter {
        private String message;

        public NettyClientHandler(String message) {
            this.message = message;
        }

        /**
         * 当通道就绪时触发
         *
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client ready to send message...");
            ctx.writeAndFlush(Unpooled.copiedBuffer(message, StandardCharsets.UTF_8));
        }

        /**
         * 读取服务器返回的数据
         *
         * @param ctx
         * @param msg
         * @throws Exception
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println("收到来自服务器" + ctx.channel().remoteAddress() + "的消息:" + byteBuf.toString(StandardCharsets.UTF_8));
        }

        /**
         * 处理异常
         *
         * @param ctx
         * @param cause
         * @throws Exception
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
