package com.example.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * 简单的 Socket 客户端示例
 * 功能：连接服务端并发送消息
 *
 * @author JavaGuide
 */
public class SimpleSocketClient {

    private final String host;
    private final int port;

    public SimpleSocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("🚀 客户端启动成功，连接到：" + host + ":" + port);
            System.out.println("💬 请输入消息（输入 'bye' 退出）：");

            String input;
            while (!(input = scanner.nextLine()).equalsIgnoreCase("bye")) {
                // 发送消息到服务端
                writer.println(input);

                // 读取服务端响应
                String response = reader.readLine();
                if (response != null) {
                    System.out.println("📨 服务端回复：" + response);
                } else {
                    System.out.println("❌ 服务端连接已断开");
                    break;
                }
            }

            // 发送退出消息
            writer.println("bye");
            System.out.println("👋 客户端退出");

        } catch (IOException e) {
            System.err.println("💥 客户端发生异常：" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new SimpleSocketClient("localhost", 8081).start();
    }
}

