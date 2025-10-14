package com.example.factory.abstractfactory;

/**
 * 抽象工厂模式演示类
 * 展示如何使用抽象工厂模式创建一整套相关的产品
 */
public class AbstractFactoryDemo {

    public static void main(String[] args) {
        System.out.println("=== 抽象工厂模式演示 ===");

        // 创建Dell电脑套装
        System.out.println("\n1. 创建Dell电脑套装:");
        createComputerSet(new DellFactory());

        System.out.println("\n==================================================");

        // 创建HP电脑套装
        System.out.println("\n2. 创建HP电脑套装:");
        createComputerSet(new HPFactory());

        System.out.println("\n==================================================");

        // 演示产品族的一致性
        System.out.println("\n3. 演示产品族的一致性:");
        demonstrateProductFamily();
    }

    /**
     * 创建一套完整的电脑设备
     * 这个方法展示了抽象工厂模式的核心优势：
     * 确保创建的产品属于同一个产品族
     */
    private static void createComputerSet(IFactory factory) {
        System.out.println("开始组装电脑套装...");

        // 创建主机
        MainFrame mainFrame = factory.createMainFrame();

        // 创建显示器
        Monitor monitor = factory.createMonitor();

        // 创建键盘
        Keyboard keyboard = factory.createKeyboard();

        System.out.println("\n电脑套装组装完成，开始测试:");

        // 测试各个组件
        mainFrame.run();
        monitor.play();
        keyboard.print();

        System.out.println("电脑套装测试完成，所有组件运行正常！");
    }

    /**
     * 演示产品族的一致性
     * 同一个工厂创建的产品具有一致的风格和兼容性
     */
    private static void demonstrateProductFamily() {
        System.out.println("演示不同品牌产品族的特色:");

        // Dell产品族特色
        System.out.println("\nDell产品族特色:");
        IFactory dellFactory = new DellFactory();
        System.out.println("- 主机: 企业级稳定性");
        System.out.println("- 显示器: UltraSharp专业显示");
        System.out.println("- 键盘: Alienware游戏体验");

        // HP产品族特色
        System.out.println("\nHP产品族特色:");
        IFactory hpFactory = new HPFactory();
        System.out.println("- 主机: Sure Start安全技术");
        System.out.println("- 显示器: DreamColor专业色彩");
        System.out.println("- 键盘: 人体工学设计");

        System.out.println("\n每个品牌的产品都有统一的设计理念和技术特色！");
    }
}

