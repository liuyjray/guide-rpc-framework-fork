package com.example.cglibproxy;

import com.example.staticproxy.UserService;
import com.example.staticproxy.UserServiceImpl;

/**
 * CGLIB动态代理演示
 */
public class CglibProxyDemo {

    public static void main(String[] args) {
        System.out.println("=== CGLIB动态代理演示 ===\n");

        try {
            // 1. 代理没有接口的类
            demonstrateNoInterfaceProxy();

            System.out.println("\n" + "========" + "\n");

            // 2. 代理有接口的类
            demonstrateInterfaceProxy();

            System.out.println("\n" + "========" + "\n");

            // 3. 演示final方法无法被代理
            demonstrateFinalMethod();

            System.out.println("\n" + "========"+ "\n");

            // 4. 演示继承关系
            demonstrateInheritance();

            System.out.println("\n=== CGLIB动态代理特点总结 ===");
            System.out.println("✅ 优点：");
            System.out.println("  • 可以代理没有接口的类");
            System.out.println("  • 性能比JDK动态代理好（直接方法调用）");
            System.out.println("  • 功能强大，支持多种代理模式");
            System.out.println("  • 可以拦截所有public和protected方法");

            System.out.println("\n❌ 缺点：");
            System.out.println("  • 需要额外的CGLIB依赖");
            System.out.println("  • 无法代理final类和final方法");
            System.out.println("  • 无法代理private方法");
            System.out.println("  • 创建代理对象的开销较大");

            System.out.println("\n🎯 适用场景：");
            System.out.println("  • 目标对象没有实现接口");
            System.out.println("  • 需要代理第三方类库中的类");
            System.out.println("  • Spring AOP中的类代理");
            System.out.println("  • 需要拦截所有方法调用的场景");

        } catch (Exception e) {
            System.out.println("⚠️  CGLIB代理不可用，可能的原因：");
            System.out.println("   1. CGLIB依赖未添加到classpath");
            System.out.println("   2. Java 9+模块系统限制，需要添加JVM参数：");
            System.out.println("      --add-opens java.base/java.lang=ALL-UNNAMED");
            System.out.println("   3. 运行环境不支持字节码生成");
            System.out.println();
            System.out.println("解决方案：");
            System.out.println("• 使用Maven运行：mvn compile exec:java");
            System.out.println("• 或添加JVM参数：java --add-opens java.base/java.lang=ALL-UNNAMED ...");
            System.out.println();
            System.out.println("错误详情: " + e.getMessage());
        }
    }

    /**
     * 演示代理没有接口的类
     */
    private static void demonstrateNoInterfaceProxy() {
        System.out.println("--- 代理没有接口的类 ---");

        // 创建代理对象
        ProductService proxy = CglibProxyFactory.createProxy(ProductService.class);

        // 查看类信息
        System.out.println("原始类: " + ProductService.class.getName());
        System.out.println("代理类: " + proxy.getClass().getName());
        System.out.println("代理类的父类: " + proxy.getClass().getSuperclass().getName());
        System.out.println("代理类是否继承自原始类: " + ProductService.class.isAssignableFrom(proxy.getClass()));
        System.out.println();

        // 调用代理方法
        System.out.println("--- 测试查询产品 ---");
        String product = proxy.getProductById(1001L);
        System.out.println();

        System.out.println("--- 测试添加产品 ---");
        boolean addResult = proxy.addProduct("iPhone 15", 7999.0);
        System.out.println();

        System.out.println("--- 测试删除产品 ---");
        boolean deleteResult = proxy.deleteProduct(1001L);
        System.out.println();
    }

    /**
     * 演示代理有接口的类
     */
    private static void demonstrateInterfaceProxy() {
        System.out.println("--- CGLIB也可以代理有接口的类 ---");

        // CGLIB可以代理有接口的类，但是是通过继承实现的
        UserServiceImpl proxy = CglibProxyFactory.createProxy(UserServiceImpl.class);

        System.out.println("原始类: " + UserServiceImpl.class.getName());
        System.out.println("代理类: " + proxy.getClass().getName());
        System.out.println("代理类的父类: " + proxy.getClass().getSuperclass().getName());
        System.out.println("代理对象是否实现UserService接口: " + (proxy instanceof UserService));
        System.out.println();

        // 调用方法
        String user = proxy.getUserById(2001L);
        System.out.println();
        proxy.saveUser("王五");
        System.out.println();
    }

    /**
     * 演示final方法无法被代理
     */
    private static void demonstrateFinalMethod() {
        System.out.println("--- 演示final方法无法被代理 ---");

        ProductService proxy = CglibProxyFactory.createProxy(ProductService.class);

        // final方法不会被拦截，直接调用原始方法
        System.out.println("调用final方法:");
        String finalResult = proxy.getFinalMethod();
        System.out.println("结果: " + finalResult);
        System.out.println("注意：final方法没有被代理拦截！");
    }

    /**
     * 演示继承关系
     */
    private static void demonstrateInheritance() {
        System.out.println("--- 演示CGLIB代理的继承关系 ---");

        ProductService proxy = CglibProxyFactory.createProxy(ProductService.class);

        System.out.println("类型检查:");
        System.out.println("proxy instanceof ProductService: " + (proxy instanceof ProductService));
        System.out.println("proxy.getClass() == ProductService.class: " + (proxy.getClass() == ProductService.class));
        System.out.println("ProductService.class.isAssignableFrom(proxy.getClass()): " +
                          ProductService.class.isAssignableFrom(proxy.getClass()));

        System.out.println("\n类层次结构:");
        Class<?> clazz = proxy.getClass();
        int level = 0;
        while (clazz != null) {
            System.out.println("  " + "- " + clazz.getName());
            clazz = clazz.getSuperclass();
            level++;
        }

        System.out.println("\n方法数量对比:");
        System.out.println("原始类方法数: " + ProductService.class.getDeclaredMethods().length);
        System.out.println("代理类方法数: " + proxy.getClass().getDeclaredMethods().length);
        System.out.println("(代理类包含额外的CGLIB生成方法)");
    }
}

