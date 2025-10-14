package com.example.factory;

import com.example.factory.abstractfactory.AbstractFactoryDemo;
import com.example.factory.method.FactoryMethodDemo;
import com.example.factory.simple.KeyboardFactory;
import com.example.factory.singleton.SingletonFactoryDemo;

import java.util.Scanner;

/**
 * 工厂模式演示主类
 * 整合简单工厂、工厂方法和抽象工厂三种模式的演示
 */
public class FactoryPatternDemo {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("                工厂模式演示程序");
        System.out.println("============================================================");
        System.out.println("本程序演示四种工厂模式的实现和使用:");
        System.out.println("1. 简单工厂模式 (Simple Factory)");
        System.out.println("2. 工厂方法模式 (Factory Method)");
        System.out.println("3. 抽象工厂模式 (Abstract Factory)");
        System.out.println("4. 单例工厂模式 (Singleton Factory)");
        System.out.println("============================================================");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            System.out.print("请选择要演示的模式 (1-4): ");

            try {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        runSimpleFactoryDemo();
                        break;
                    case 2:
                        runFactoryMethodDemo();
                        break;
                    case 3:
                        runAbstractFactoryDemo();
                        break;
                    case 4:
                        runSingletonFactoryDemo();
                        break;
                    case 5:
                        runAllDemos();
                        break;
                    case 0:
                        System.out.println("感谢使用工厂模式演示程序！");
                        return;
                    default:
                        System.out.println("无效的选择，请重新输入！");
                        continue;
                }

                System.out.println("\n按回车键继续...");
                scanner.nextLine(); // 消费换行符
                scanner.nextLine(); // 等待用户按回车

            } catch (Exception e) {
                System.out.println("输入错误，请输入数字！");
                scanner.nextLine(); // 清除错误输入
            }
        }
    }

    /**
     * 显示菜单
     */
    private static void showMenu() {
        System.out.println("\n========================================");
        System.out.println("请选择要演示的工厂模式:");
        System.out.println("1. 简单工厂模式");
        System.out.println("2. 工厂方法模式");
        System.out.println("3. 抽象工厂模式");
        System.out.println("4. 单例工厂模式");
        System.out.println("5. 运行所有演示");
        System.out.println("0. 退出程序");
        System.out.println("========================================");
    }

    /**
     * 运行简单工厂模式演示
     */
    private static void runSimpleFactoryDemo() {
        System.out.println("\n============================================================");
        System.out.println("                简单工厂模式演示");
        System.out.println("============================================================");
        System.out.println("特点:");
        System.out.println("- 唯一工厂类，一个产品抽象类");
        System.out.println("- 工厂类的创建方法依据入参判断并创建具体产品对象");
        System.out.println("- 缺点：违背开闭原则，扩展性差");
        System.out.println("============================================================");

        KeyboardFactory.main(new String[]{});
    }

    /**
     * 运行工厂方法模式演示
     */
    private static void runFactoryMethodDemo() {
        System.out.println("\n============================================================");
        System.out.println("                工厂方法模式演示");
        System.out.println("============================================================");
        System.out.println("特点:");
        System.out.println("- 多个工厂类，一个产品抽象类");
        System.out.println("- 利用多态创建不同的产品对象，避免了大量的if-else判断");
        System.out.println("- 符合开闭原则，但会增加类的数量");
        System.out.println("- 问题：失去了简单工厂根据参数动态选择的能力");
        System.out.println("- 解决：通过工厂提供者(Factory Provider)重新获得动态选择能力");
        System.out.println("============================================================");

        FactoryMethodDemo.main(new String[]{});
    }

    /**
     * 运行抽象工厂模式演示
     */
    private static void runAbstractFactoryDemo() {
        System.out.println("\n============================================================");
        System.out.println("                抽象工厂模式演示");
        System.out.println("============================================================");
        System.out.println("特点:");
        System.out.println("- 多个工厂类，多个产品抽象类");
        System.out.println("- 产品子类分组，同一个工厂实现类创建同组中的不同产品");
        System.out.println("- 减少了工厂子类的数量，确保产品族的一致性");
        System.out.println("============================================================");

        AbstractFactoryDemo.main(new String[]{});
    }

    /**
     * 运行单例工厂模式演示
     */
    private static void runSingletonFactoryDemo() {
        System.out.println("\n============================================================");
        System.out.println("                单例工厂模式演示");
        System.out.println("============================================================");
        System.out.println("特点:");
        System.out.println("- 工厂本身是单例的，节省内存资源");
        System.out.println("- 可以控制产品实例的创建策略（单例产品或多例产品）");
        System.out.println("- 线程安全的实现");
        System.out.println("- 适用于需要全局唯一工厂实例的场景");
        System.out.println("============================================================");

        SingletonFactoryDemo.main(new String[]{});
    }

    /**
     * 运行所有演示
     */
    private static void runAllDemos() {
        System.out.println("\n============================================================");
        System.out.println("                运行所有工厂模式演示");
        System.out.println("============================================================");

        runSimpleFactoryDemo();
        System.out.println("\n============================================================");

        runFactoryMethodDemo();
        System.out.println("\n============================================================");

        runAbstractFactoryDemo();

        System.out.println("\n============================================================");
        System.out.println("                三种工厂模式对比总结");
        System.out.println("============================================================");
        printComparison();
    }

    /**
     * 打印三种工厂模式的对比
     */
    private static void printComparison() {
        System.out.println("┌─────────────┬─────────────┬─────────────┬─────────────┐");
        System.out.println("│    模式     │  工厂数量   │  产品数量   │    特点     │");
        System.out.println("├─────────────┼─────────────┼─────────────┼─────────────┤");
        System.out.println("│  简单工厂   │     1个     │     1类     │ 简单但不灵活 │");
        System.out.println("├─────────────┼─────────────┼─────────────┼─────────────┤");
        System.out.println("│  工厂方法   │    多个     │     1类     │ 灵活但类多   │");
        System.out.println("├─────────────┼─────────────┼─────────────┼─────────────┤");
        System.out.println("│  抽象工厂   │    多个     │    多类     │ 产品族一致   │");
        System.out.println("└─────────────┴─────────────┴─────────────┴─────────────┘");

        System.out.println("\n使用场景建议:");
        System.out.println("• 简单工厂：产品种类少且不经常变化的场景");
        System.out.println("• 工厂方法：需要频繁扩展产品种类的场景");
        System.out.println("• 抽象工厂：需要创建产品族，确保产品一致性的场景");
    }
}

