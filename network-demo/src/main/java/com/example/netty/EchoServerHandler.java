package com.example.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务端消息处理器
 * 功能：处理客户端发送的消息
 *
 * @author JavaGuide
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当接收到客户端消息时调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message = (String) msg;
        System.out.println("📨 服务端接收到消息：" + message);

        // 回显消息给客户端
        String response = "\n服务端回复：" + message;
        ctx.writeAndFlush(response);
    }

    /**
     * 当客户端连接建立时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("🔗 客户端连接建立：" + ctx.channel().remoteAddress());
    }

    /**
     * 当客户端断开连接时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("❌ 客户端断开连接：" + ctx.channel().remoteAddress());
    }

    /**
     * 当发生异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("💥 服务端发生异常：" + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}