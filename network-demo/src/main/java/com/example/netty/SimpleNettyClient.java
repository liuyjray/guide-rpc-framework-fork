package com.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * 简单的 Netty 客户端示例
 * 功能：连接服务端并发送消息
 *
 * @author JavaGuide
 */
public class SimpleNettyClient {

    private final String host;
    private final int port;

    public SimpleNettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        // 1. 创建线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            // 2. 创建客户端启动器
            Bootstrap bootstrap = new Bootstrap();

            // 3. 配置启动器
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)  // 设置客户端 Channel 类型
                    .option(ChannelOption.SO_KEEPALIVE, true)  // 保持连接活跃
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 4. 设置处理器链
                            ChannelPipeline pipeline = ch.pipeline();

                            // 添加字符串解码器
                            pipeline.addLast(new StringDecoder());
                            // 添加字符串编码器
                            pipeline.addLast(new StringEncoder());
                            // 添加自定义业务处理器
                            pipeline.addLast(new EchoClientHandler());
                        }
                    });

            // 5. 连接服务端
            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("🚀 客户端启动成功，连接到：" + host + ":" + port);

            // 6. 获取 Channel 用于发送消息
            Channel channel = future.channel();

            // 7. 发送消息
            Scanner scanner = new Scanner(System.in);
            System.out.println("💬 请输入消息（输入 'quit' 退出）：");

            while (true) {
                String input = scanner.nextLine();
                if ("quit".equalsIgnoreCase(input)) {
                    break;
                }
                channel.writeAndFlush(input);
            }

        } finally {
            // 8. 优雅关闭
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 连接到 9999 端口
        new SimpleNettyClient("localhost", 9999).start();
    }
}
