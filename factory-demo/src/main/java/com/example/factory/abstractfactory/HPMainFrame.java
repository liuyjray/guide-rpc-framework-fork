package com.example.factory.abstractfactory;

/**
 * HP 主机实现类 - 抽象工厂模式
 */
public class HPMainFrame implements MainFrame {

    @Override
    public void run() {
        System.out.println("HP主机正在运行...");
        System.out.println("使用HP EliteDesk技术，提供企业级安全和性能");
    }
}

