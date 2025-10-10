package com.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 简单的 Netty 服务端示例
 * 功能：启动服务端并处理客户端连接
 *
 */
public class SimpleNettyServer {

    private final int port;

    public SimpleNettyServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        // 1. 创建线程组
        // bossGroup 用于接收客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // workerGroup 用于处理客户端请求
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 2. 创建服务端启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            // 3. 配置启动器
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  // 设置服务端 Channel 类型
                    .option(ChannelOption.SO_BACKLOG, 128)  // 设置连接队列大小
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  // 保持连接活跃
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 4. 设置处理器链
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new EchoServerHandler());
                        }
                    });

            // 5. 绑定端口并启动服务
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("🚀 Netty 服务端启动成功，监听端口：" + port);
            System.out.println("📡 等待客户端连接...");

            // 6. 等待服务端关闭
            future.channel().closeFuture().sync();

        } finally {
            // 7. 优雅关闭线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("🔚 服务端已关闭");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 使用 9999 端口避免冲突
        new SimpleNettyServer(9999).start();
    }
}
