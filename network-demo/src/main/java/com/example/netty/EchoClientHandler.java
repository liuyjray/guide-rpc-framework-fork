package com.example.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端消息处理器
 * 功能：处理服务端返回的消息
 *
 * @author JavaGuide
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当接收到服务端消息时调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message = (String) msg;
        System.out.println("📨 客户端接收到服务端回复：" + message);
    }

    /**
     * 当连接建立时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("🔗 连接服务端成功：" + ctx.channel().remoteAddress());
    }

    /**
     * 当连接断开时调用
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("❌ 与服务端断开连接");
    }

    /**
     * 当发生异常时调用
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.println("💥 客户端发生异常：" + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}