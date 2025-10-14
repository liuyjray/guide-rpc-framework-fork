package com.example.factory.abstractfactory;

/**
 * Dell 工厂实现类 - 抽象工厂模式
 * 负责创建 Dell 品牌的所有产品
 */
public class DellFactory implements IFactory {

    @Override
    public MainFrame createMainFrame() {
        System.out.println("Dell工厂正在制造Dell主机...");
        MainFrame mainFrame = new DellMainFrame();
        // 这里可以添加复杂的初始化逻辑
        // 比如安装Dell专有软件、配置系统等
        System.out.println("Dell主机制造完成，已安装Dell专有管理软件");
        return mainFrame;
    }

    @Override
    public Monitor createMonitor() {
        System.out.println("Dell工厂正在制造Dell显示器...");
        Monitor monitor = new DellMonitor();
        // 这里可以添加复杂的初始化逻辑
        // 比如校准色彩、设置分辨率等
        System.out.println("Dell显示器制造完成，已完成色彩校准");
        return monitor;
    }

    @Override
    public Keyboard createKeyboard() {
        System.out.println("Dell工厂正在制造Dell键盘...");
        Keyboard keyboard = new DellKeyboard();
        // 这里可以添加复杂的初始化逻辑
        // 比如设置背光、配置快捷键等
        System.out.println("Dell键盘制造完成，已配置Alienware背光效果");
        return keyboard;
    }
}

