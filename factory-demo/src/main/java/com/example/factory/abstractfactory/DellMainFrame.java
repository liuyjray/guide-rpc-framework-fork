package com.example.factory.abstractfactory;

/**
 * Dell 主机实现类 - 抽象工厂模式
 */
public class DellMainFrame implements MainFrame {

    @Override
    public void run() {
        System.out.println("Dell主机正在运行...");
        System.out.println("使用Dell OptiPlex技术，提供稳定可靠的性能");
    }
}

