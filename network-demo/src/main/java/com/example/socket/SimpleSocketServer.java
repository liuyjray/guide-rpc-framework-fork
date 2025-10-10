package com.example.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单的 Socket 服务端示例
 * 功能：使用传统 Socket 实现多客户端并发处理
 *
 * @author JavaGuide
 */
public class SimpleSocketServer {

    private final int port;
    private final ExecutorService threadPool;
    private volatile boolean running = false;

    public SimpleSocketServer(int port) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            running = true;
            System.out.println("🚀 Socket 服务端启动成功，监听端口：" + port);
            System.out.println("📡 等待客户端连接...");

            while (running) {
                try {
                    // 接受客户端连接
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("🔗 新客户端连接：" + clientSocket.getRemoteSocketAddress());

                    // 使用线程池处理客户端请求
                    threadPool.submit(new ClientHandler(clientSocket));
                } catch (IOException e) {
                    if (running) {
                        System.err.println("💥 接受客户端连接时发生异常：" + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("💥 服务端启动失败：" + e.getMessage());
        } finally {
            stop();
        }
    }

    public void stop() {
        running = false;
        threadPool.shutdown();
        System.out.println("🔚 Socket 服务端已关闭");
    }

    /**
     * 客户端处理器
     */
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    System.out.println("📨 收到客户端消息：" + inputLine);

                    // 回显消息给客户端
                    String response = "服务端回复：" + inputLine;
                    writer.println(response);

                    // 如果客户端发送 "bye"，则断开连接
                    if ("bye".equalsIgnoreCase(inputLine.trim())) {
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("💥 处理客户端请求时发生异常：" + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("❌ 客户端连接已关闭：" + clientSocket.getRemoteSocketAddress());
                } catch (IOException e) {
                    System.err.println("💥 关闭客户端连接时发生异常：" + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        new SimpleSocketServer(8081).start();
    }
}

