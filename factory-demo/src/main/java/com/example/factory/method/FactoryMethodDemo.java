package com.example.factory.method;

import com.example.factory.common.Context;

/**
 * 工厂方法模式演示类
 * 展示如何使用工厂方法模式创建不同品牌的键盘
 */
public class FactoryMethodDemo {

    public static void main(String[] args) {
        System.out.println("=== 工厂方法模式演示 ===");

        Context context = new Context("Design Pattern", "learning");

        // 创建不同的工厂实例
        System.out.println("\n1. 使用HP工厂创建HP键盘:");
        IKeyboardFactory hpFactory = new HPKeyboardFactory();
        Keyboard hpKeyboard = hpFactory.getInstance();
        hpKeyboard.print();
        hpKeyboard.input(context);

        System.out.println("\n2. 使用Lenovo工厂创建Lenovo键盘:");
        IKeyboardFactory lenovoFactory = new LenovoKeyboardFactory();
        Keyboard lenovoKeyboard = lenovoFactory.getInstance();
        lenovoKeyboard.print();
        lenovoKeyboard.input(context);

        System.out.println("\n3. 使用Dell工厂创建Dell键盘:");
        IKeyboardFactory dellFactory = new DellKeyboardFactory();
        Keyboard dellKeyboard = dellFactory.getInstance();
        dellKeyboard.print();
        dellKeyboard.input(context);

        System.out.println("\n4. 演示多态特性:");
        demonstratePolymorphism();

        System.out.println("\n5. 演示动态选择工厂:");
        KeyboardFactoryProvider.demonstrateDynamicSelection();
    }

    /**
     * 演示多态特性
     * 客户端代码不需要知道具体的工厂类型
     */
    private static void demonstratePolymorphism() {
        // 工厂数组，体现多态
        IKeyboardFactory[] factories = {
            new HPKeyboardFactory(),
            new LenovoKeyboardFactory(),
            new DellKeyboardFactory()
        };

        Context context = new Context("Polymorphism", "demonstration");

        for (int i = 0; i < factories.length; i++) {
            System.out.println("\n工厂 " + (i + 1) + ":");
            Keyboard keyboard = factories[i].getInstance();
            keyboard.print();
            keyboard.input(context);
        }
    }
}

