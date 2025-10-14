package com.example.factory.singleton;

import com.example.factory.common.BrandEnum;
import com.example.factory.common.Context;

/**
 * 单例工厂模式演示类
 * 展示如何使用单例工厂模式创建产品
 */
public class SingletonFactoryDemo {

    public static void main(String[] args) {
        System.out.println("=== 单例工厂模式演示 ===");

        Context context = new Context("Singleton Pattern", "learning");

        // 演示工厂的单例特性
        System.out.println("\n1. 演示工厂的单例特性:");
        demonstrateFactorySingleton();

        System.out.println("\n==================================================");

        // 演示多例产品创建
        System.out.println("\n2. 演示多例产品创建:");
        demonstrateMultipleInstances(context);

        System.out.println("\n==================================================");

        // 演示单例产品创建
        System.out.println("\n3. 演示单例产品创建:");
        demonstrateSingletonProducts(context);

        System.out.println("\n==================================================");

        // 演示缓存管理
        System.out.println("\n4. 演示缓存管理:");
        demonstrateCacheManagement();

        System.out.println("\n==================================================");

        // 演示线程安全性
        System.out.println("\n5. 演示线程安全性:");
        demonstrateThreadSafety();
    }

    /**
     * 演示工厂的单例特性
     */
    private static void demonstrateFactorySingleton() {
        System.out.println("获取工厂实例...");

        SingletonKeyboardFactory factory1 = SingletonKeyboardFactory.getInstance();
        SingletonKeyboardFactory factory2 = SingletonKeyboardFactory.getInstance();

        System.out.println("factory1 == factory2: " + (factory1 == factory2));
        System.out.println("factory1.hashCode(): " + factory1.hashCode());
        System.out.println("factory2.hashCode(): " + factory2.hashCode());
        System.out.println("工厂确实是单例的！");
    }

    /**
     * 演示多例产品创建
     */
    private static void demonstrateMultipleInstances(Context context) {
        SingletonKeyboardFactory factory = SingletonKeyboardFactory.getInstance();

        System.out.println("每次调用 createKeyboard() 都会创建新实例:");

        Keyboard hp1 = factory.createKeyboard(BrandEnum.HP);
        Keyboard hp2 = factory.createKeyboard(BrandEnum.HP);

        System.out.println("hp1 == hp2: " + (hp1 == hp2));
        System.out.println("hp1.hashCode(): " + hp1.hashCode());
        System.out.println("hp2.hashCode(): " + hp2.hashCode());

        hp1.print();
        hp1.input(context);
    }

    /**
     * 演示单例产品创建
     */
    private static void demonstrateSingletonProducts(Context context) {
        SingletonKeyboardFactory factory = SingletonKeyboardFactory.getInstance();

        System.out.println("调用 getSingletonKeyboard() 会返回缓存的实例:");

        // 第一次获取，会创建新实例
        Keyboard hp1 = factory.getSingletonKeyboard(BrandEnum.HP);

        // 第二次获取，会返回缓存的实例
        Keyboard hp2 = factory.getSingletonKeyboard(BrandEnum.HP);

        System.out.println("hp1 == hp2: " + (hp1 == hp2));
        System.out.println("hp1.hashCode(): " + hp1.hashCode());
        System.out.println("hp2.hashCode(): " + hp2.hashCode());

        hp1.print();
        hp1.input(context);

        // 测试不同品牌
        System.out.println("\n测试不同品牌的单例产品:");
        Keyboard dell1 = factory.getSingletonKeyboard(BrandEnum.DELL);
        Keyboard dell2 = factory.getSingletonKeyboard(BrandEnum.DELL);

        System.out.println("dell1 == dell2: " + (dell1 == dell2));
        dell1.print();
    }

    /**
     * 演示缓存管理
     */
    private static void demonstrateCacheManagement() {
        SingletonKeyboardFactory factory = SingletonKeyboardFactory.getInstance();

        System.out.println("初始缓存状态:");
        factory.printCacheStatus();

        System.out.println("\n创建一些单例产品:");
        factory.getSingletonKeyboard(BrandEnum.HP);
        factory.getSingletonKeyboard(BrandEnum.LENOVO);

        System.out.println("\n创建后的缓存状态:");
        factory.printCacheStatus();

        System.out.println("\n清除缓存:");
        factory.clearCache();

        System.out.println("\n清除后的缓存状态:");
        factory.printCacheStatus();

        System.out.println("\n重新创建产品:");
        factory.getSingletonKeyboard(BrandEnum.HP);
        factory.printCacheStatus();
    }

    /**
     * 演示线程安全性
     */
    private static void demonstrateThreadSafety() {
        System.out.println("启动多个线程同时获取工厂实例和产品实例...");

        Thread[] threads = new Thread[5];

        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                // 获取工厂实例
                SingletonKeyboardFactory factory = SingletonKeyboardFactory.getInstance();
                System.out.println("线程 " + threadId + " 获取到工厂实例: " + factory.hashCode());

                // 获取产品实例
                Keyboard keyboard = factory.getSingletonKeyboard(BrandEnum.HP);
                System.out.println("线程 " + threadId + " 获取到HP键盘实例: " + keyboard.hashCode());

                try {
                    Thread.sleep(100); // 模拟一些处理时间
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("所有线程执行完毕，验证单例特性保持正确！");
    }
}

