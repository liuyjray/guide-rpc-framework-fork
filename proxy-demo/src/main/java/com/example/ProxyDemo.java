package com.example;

import com.example.jdkproxy.JdkProxyFactory;
import com.example.staticproxy.UserService;
import com.example.staticproxy.UserServiceImpl;
import com.example.staticproxy.UserServiceProxy;

/**
 * 代理模式完整演示
 * 包含静态代理、JDK动态代理、CGLIB动态代理的对比演示
 * Java 8兼容版本
 */
public class ProxyDemo {

    public static void main(String[] args) {
        System.out.println("=== 代理模式完整演示 ===\n");

        // 创建目标对象
        UserService userService = new UserServiceImpl();

        // 1. 静态代理演示
        demonstrateStaticProxy(userService);

        System.out.println("\n============================================================\n");

        // 2. JDK动态代理演示
        demonstrateJdkProxy(userService);

        System.out.println("\n============================================================\n");

        // 3. CGLIB动态代理演示
        demonstrateCglibProxy();

        System.out.println("\n============================================================\n");

        // 4. 对比直接调用
        demonstrateDirectCall(userService);

        System.out.println("\n============================================================\n");

        // 5. 性能对比
        performanceComparison(userService);

        System.out.println("\n============================================================\n");

        // 6. 总结
        printSummary();
    }

    /**
     * 静态代理演示
     */
    private static void demonstrateStaticProxy(UserService userService) {
        System.out.println("【1. 静态代理演示】");
        System.out.println("特点：编译时确定，手动编写代理类");
        System.out.println();

        UserService staticProxy = new UserServiceProxy(userService);
        System.out.println("代理类型: " + staticProxy.getClass().getSimpleName());
        staticProxy.getUserById(1001L);
        System.out.println();
        staticProxy.saveUser("张三");
    }

    /**
     * JDK动态代理演示
     */
    private static void demonstrateJdkProxy(UserService userService) {
        System.out.println("【2. JDK动态代理演示】");
        System.out.println("特点：运行时生成，基于接口");
        System.out.println();

        UserService jdkProxy = JdkProxyFactory.createProxy(userService);
        System.out.println("代理类型: " + jdkProxy.getClass().getSimpleName());
        System.out.println("是JDK代理: " + (jdkProxy instanceof java.lang.reflect.Proxy));
        jdkProxy.getUserById(2001L);
        System.out.println();
        jdkProxy.saveUser("李四");
    }

    /**
     * CGLIB动态代理演示
     */
    private static void demonstrateCglibProxy() {
        System.out.println("【3. CGLIB动态代理演示】");
        System.out.println("特点：基于继承，可以代理没有接口的类");
        System.out.println();

        try {
            // 尝试加载CGLIB类
            Class.forName("net.sf.cglib.proxy.Enhancer");

            // 动态导入CGLIB相关类
            Class<?> productServiceClass = Class.forName("com.example.cglibproxy.ProductService");
            Class<?> cglibProxyFactoryClass = Class.forName("com.example.cglibproxy.CglibProxyFactory");

            // 使用反射调用CGLIB代理
            Object cglibProxy = cglibProxyFactoryClass.getMethod("createProxy", Class.class)
                    .invoke(null, productServiceClass);

            System.out.println("代理类型: " + cglibProxy.getClass().getSimpleName());
            System.out.println("父类: " + cglibProxy.getClass().getSuperclass().getSimpleName());

            // 调用方法
            cglibProxy.getClass().getMethod("getProductById", Long.class)
                    .invoke(cglibProxy, 3001L);
            System.out.println();

            cglibProxy.getClass().getMethod("addProduct", String.class, double.class)
                    .invoke(cglibProxy, "iPhone 15", 7999.0);

            System.out.println();
            System.out.println("--- 演示final方法无法被代理 ---");
            Object finalResult = cglibProxy.getClass().getMethod("getFinalMethod")
                    .invoke(cglibProxy);
            System.out.println("final方法结果: " + finalResult);
            System.out.println("注意：final方法没有被代理拦截！");

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
            System.out.println("CGLIB代理演示跳过，继续其他演示...");
        }
    }

    /**
     * 直接调用演示
     */
    private static void demonstrateDirectCall(UserService userService) {
        System.out.println("【4. 直接调用（无代理）】");
        System.out.println("特点：无额外功能，性能最好");
        System.out.println();

        System.out.println("对象类型: " + userService.getClass().getSimpleName());
        userService.getUserById(4001L);
    }

    /**
     * 性能对比
     */
    private static void performanceComparison(UserService userService) {
        System.out.println("【5. 性能对比】");

        int iterations = 1000; // 减少迭代次数避免输出过多

        // 创建代理对象
        UserService staticProxy = new UserServiceProxy(userService);
        UserService jdkProxy = JdkProxyFactory.createProxy(userService);

        // 测试原始对象（静默模式）
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            userService.getUserById(1L);
        }
        long originalTime = System.currentTimeMillis() - startTime;

        // 测试静态代理（静默模式）
        startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            staticProxy.getUserById(1L);
        }
        long staticTime = System.currentTimeMillis() - startTime;

        // 测试JDK代理（静默模式）
        startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            jdkProxy.getUserById(1L);
        }
        long jdkTime = System.currentTimeMillis() - startTime;

        System.out.println("执行 " + iterations + " 次方法调用的时间对比：");
        System.out.println("原始对象: " + originalTime + "ms");
        System.out.println("静态代理: " + staticTime + "ms (慢了 " + (staticTime - originalTime) + "ms)");
        System.out.println("JDK代理: " + jdkTime + "ms (慢了 " + (jdkTime - originalTime) + "ms)");

        // 尝试测试CGLIB代理性能
        try {
            Class<?> cglibProxyFactoryClass = Class.forName("com.example.cglibproxy.CglibProxyFactory");
            Class<?> userServiceImplClass = Class.forName("com.example.staticproxy.UserServiceImpl");

            Object cglibProxy = cglibProxyFactoryClass.getMethod("createProxy", Class.class)
                    .invoke(null, userServiceImplClass);

            startTime = System.currentTimeMillis();
            for (int i = 0; i < iterations; i++) {
                cglibProxy.getClass().getMethod("getUserById", Long.class)
                        .invoke(cglibProxy, 1L);
            }
            long cglibTime = System.currentTimeMillis() - startTime;
            System.out.println("CGLIB代理: " + cglibTime + "ms (慢了 " + (cglibTime - originalTime) + "ms)");

        } catch (Exception e) {
            System.out.println("CGLIB代理: 不可用（跳过性能测试）");
        }
    }

    /**
     * 总结
     */
    private static void printSummary() {
        System.out.println("【6. 总结对比】");
        System.out.println();
        System.out.println("┌─────────────┬──────────────┬──────────────┬──────────────┐");
        System.out.println("│    特性     │   静态代理   │  JDK动态代理 │ CGLIB动态代理│");
        System.out.println("├─────────────┼──────────────┼──────────────┼──────────────┤");
        System.out.println("│  实现方式   │ 手动编写代理 │  基于接口    │  基于继承    │");
        System.out.println("│  代理对象   │ 必须有接口   │  必须有接口  │  任何类      │");
        System.out.println("│  生成时机   │   编译时     │   运行时     │   运行时     │");
        System.out.println("│    性能     │     最好     │     中等     │     较好     │");
        System.out.println("│   灵活性    │      低      │      高      │      高      │");
        System.out.println("│  代码维护   │     繁琐     │     简单     │     简单     │");
        System.out.println("└─────────────┴──────────────┴──────────────┴──────────────┘");
        System.out.println();
        System.out.println("使用建议：");
        System.out.println("• 静态代理：适用于代理关系固定、对性能要求极高的场景");
        System.out.println("• JDK动态代理：适用于有接口的类，Spring AOP默认选择");
        System.out.println("• CGLIB代理：适用于没有接口的类，功能更强大");
        System.out.println();
        System.out.println("注意事项：");
        System.out.println("• JDK动态代理只能代理实现了接口的类");
        System.out.println("• CGLIB代理无法代理final类和final方法");
        System.out.println("• 性能方面：静态代理 > CGLIB代理 > JDK动态代理");
        System.out.println("• Java 9+环境下CGLIB需要额外的JVM参数支持");
    }
}

