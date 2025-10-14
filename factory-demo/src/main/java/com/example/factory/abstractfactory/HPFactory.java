package com.example.factory.abstractfactory;

/**
 * HP 工厂实现类 - 抽象工厂模式
 * 负责创建 HP 品牌的所有产品
 */
public class HPFactory implements IFactory {

    @Override
    public MainFrame createMainFrame() {
        System.out.println("HP工厂正在制造HP主机...");
        MainFrame mainFrame = new HPMainFrame();
        // 这里可以添加复杂的初始化逻辑
        // 比如安装HP专有软件、配置安全设置等
        System.out.println("HP主机制造完成，已安装HP Sure Start安全技术");
        return mainFrame;
    }

    @Override
    public Monitor createMonitor() {
        System.out.println("HP工厂正在制造HP显示器...");
        Monitor monitor = new HPMonitor();
        // 这里可以添加复杂的初始化逻辑
        // 比如校准DreamColor、设置专业模式等
        System.out.println("HP显示器制造完成，已校准DreamColor专业色彩");
        return monitor;
    }

    @Override
    public Keyboard createKeyboard() {
        System.out.println("HP工厂正在制造HP键盘...");
        Keyboard keyboard = new HPKeyboard();
        // 这里可以添加复杂的初始化逻辑
        // 比如设置人体工学参数、配置快捷键等
        System.out.println("HP键盘制造完成，已优化人体工学设计");
        return keyboard;
    }
}

