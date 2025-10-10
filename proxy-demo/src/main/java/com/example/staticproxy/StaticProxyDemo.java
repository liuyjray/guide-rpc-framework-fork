package com.example.staticproxy;

/**
 * 静态代理演示
 * 展示静态代理的实现和使用
 */
public class StaticProxyDemo {

    public static void main(String[] args) {
        System.out.println("=== 静态代理演示 ===\n");

        // 1. 创建目标对象
        UserService userService = new UserServiceImpl();

        // 2. 创建代理对象，将目标对象传入
        UserService proxy = new UserServiceProxy(userService);

        // 3. 通过代理对象调用方法
        System.out.println("--- 测试查询用户 ---");
        String user = proxy.getUserById(1001L);

        System.out.println("\n--- 测试保存用户 ---");
        boolean success = proxy.saveUser("张三");

        System.out.println("\n=== 对比：直接调用目标对象 ===");
        System.out.println("--- 直接调用 ---");
        String directResult = userService.getUserById(1002L);
        System.out.println("返回结果: " + directResult);

        System.out.println("\n=== 静态代理特点总结 ===");
        System.out.println("✅ 优点：");
        System.out.println("  • 性能最好，没有反射开销");
        System.out.println("  • 代码清晰，易于理解和调试");
        System.out.println("  • 编译时确定，类型安全");

        System.out.println("\n❌ 缺点：");
        System.out.println("  • 代码冗余，每个接口都需要写一个代理类");
        System.out.println("  • 维护困难，接口变化时代理类也要修改");
        System.out.println("  • 灵活性差，代理关系在编译时就确定");

        System.out.println("\n🎯 适用场景：");
        System.out.println("  • 代理关系固定，不需要动态变化");
        System.out.println("  • 对性能要求极高的场景");
        System.out.println("  • 需要在编译时确定代理关系");
    }
}

