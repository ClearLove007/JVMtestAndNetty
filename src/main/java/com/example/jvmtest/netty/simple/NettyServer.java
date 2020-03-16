package com.example.jvmtest.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 17:18 2020/1/19
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        //设置工作组
        bootstrap.group(bossGroup, workerGroup)
                //设置通道类型
                .channel(NioServerSocketChannel.class)
                //设置线程队列得到连接数
                .option(ChannelOption.SO_BACKLOG, 128)
                //设置保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //为workerGroup的EventLoop设置处理器
                .childHandler(new ChannelInitHandler());
        System.out.println("server is ready...");
        //绑定端口并同步(同时启动服务器)
        ChannelFuture channelFuture = bootstrap.bind(8080).sync();
        //对关闭通道事件进行监听(io完成通道就关了 每个io一个通道)
        channelFuture.channel().closeFuture().sync();
    }

    private static class ChannelInitHandler extends ChannelInitializer<SocketChannel> {
        //为pipeline设置处理器
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new NettyServerHandler());
        }
    }

    /**
     * 自定义handler需继承ChannelHandlerAdapter适配器  适配器模式
     */
    private static class NettyServerHandler extends ChannelHandlerAdapter {

        /**
         * 读取数据(读取客户端发送的消息)
         *
         * @param ctx 上下文信息 （管道pipeline  通道channel  地址）
         * @param msg 客户端发送的数据
         * @throws Exception
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            //将msg转为netty的byteBuf
            ByteBuf byteBuf = (ByteBuf) msg;
            System.out.println("received ip: " + ctx.channel().remoteAddress() + " send data: " + byteBuf.toString(StandardCharsets.UTF_8));
            System.out.println("processing data ...");
            ctx.channel().eventLoop().execute(() -> {
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ctx.writeAndFlush(Unpooled.copiedBuffer("message process over", StandardCharsets.UTF_8));
                System.out.println("data process over ...");
            });
        }

        /**
         * 读取完数据
         *
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.copiedBuffer("message is processing ...", StandardCharsets.UTF_8));
        }

        /**
         * 处理异常(关闭通道)
         *
         * @param ctx
         * @param cause
         * @throws Exception
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
        }
    }
}
