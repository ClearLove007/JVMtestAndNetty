package com.example.jvmtest.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetAddress;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 9:43 2020/1/19
 */
public class HelloWorldServer {

    public static void main(String[] args) {
        // EventLoop 代替原来的 ChannelFactory
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();    //创建 一个netty 服务器
            // server端采用简洁的连写方式，client端才用分段普通写法。
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)    // 指定channel[通道]类型
                    .childHandler(new ChannelInitializer<SocketChannel>() {    // 指定Handler [操纵者]

                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    // 以("\n")为结尾分割的 解码器
                    ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

                    // 字符串 解码 和 编码   默认的 StringDecoder 字符串形式输出
                    ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());

                    ch.pipeline().addLast(new HelloServerHandler());  // 添加自己的对 上传数据的处理
                }

            }).option(ChannelOption.SO_KEEPALIVE, true);

   ChannelFuture f = serverBootstrap.bind(8000).sync();  // 绑定 8000 端口
            f.channel().closeFuture().sync();

        }
        catch (InterruptedException e) {

        } finally {
            workerGroup.shutdownGracefully();  // 销毁 netty
            bossGroup.shutdownGracefully();
        }
    }


    /**
     * 自己对 处理数据
     *
     * @author flm
     * 2017年11月10日
     */
    private static class HelloServerHandler extends ChannelHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 收到消息直接打印输出
            System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);

            // 返回客户端消息 - 我已经接收到了你的消息
            ctx.writeAndFlush("server Received your message !\n");
        }

        /*
         *
         * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
         *
         * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

            System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

            ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");  //回复

            super.channelActive(ctx);
        }
    }
}
