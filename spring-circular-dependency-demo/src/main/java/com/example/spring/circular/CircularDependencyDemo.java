package com.example.spring.circular;

/**
 * 循环依赖演示 - 主类
 *
 * 这个演示展示了Spring如何使用三级缓存解决循环依赖。
 *
 * 🎯 记忆技巧：
 * - 三级缓存 = 三种房子：图纸 → 毛坯 → 精装
 * - 循环依赖 = 两个房子需要对方的工具才能完工
 * - 解决方案 = 先给毛坯房，后面再完工
 *
 * 🎪 记忆口诀："Spring循环依赖不用慌，三级缓存来帮忙！"
 *
 * @author demo
 */
public class CircularDependencyDemo {

    public static void main(String[] args) {
        System.out.println("🎭 Spring循环依赖解决机制演示");
        System.out.println("========");

        // 创建我们的简化版Spring容器
        SimplifiedSpringContainer container = new SimplifiedSpringContainer();

        System.out.println("\n🎬 开始创建UserService...");
        System.out.println("这将触发循环依赖解决机制!");

        // 请求UserService - 这将触发循环依赖解决过程
        UserService userService = (UserService) container.getBean("userService");

        // 显示最终的缓存状态
        container.printCacheStatus();

        System.out.println("\n🎭 测试循环依赖是否已解决:");
        testCircularDependencyResolution(userService, container);

        System.out.println("\n🎉 循环依赖解决成功!");

        // 打印记忆技巧总结
        printMemoryTipsSummary();
    }

    /**
     * 测试循环依赖是否已经正确解决
     */
    private static void testCircularDependencyResolution(UserService userService, SimplifiedSpringContainer container) {
        System.out.println("\n--- 测试UserService功能 ---");

        // 测试UserService
        userService.createUser("Alice");
        String userInfo = userService.getUserInfo("Alice");
        System.out.println("用户信息: " + userInfo);

        // 通过UserService测试OrderService
        OrderService orderService = (OrderService) container.getBean("orderService");
        orderService.createOrder("Bob", "Laptop");
        String orderStatus = orderService.getOrderStatus("ORDER-001");
        System.out.println("订单状态: " + orderStatus);

        System.out.println("\n✅ 两个服务都正常工作!");
        System.out.println("✅ 循环依赖已经解决!");
    }

    /**
     * 打印记忆技巧总结，便于回忆
     */
    private static void printMemoryTipsSummary() {
        System.out.println("========");
        System.out.println("🧠 记忆技巧总结:");
        System.out.println("========");

        System.out.println("🎯 核心口诀: '三级缓存解循环，工厂早期到完整'");
        System.out.println();

        System.out.println("🏠 盖房子类比:");
        System.out.println("  🏭 三级缓存 = 建房图纸 (工厂方法)");
        System.out.println("  🏘️ 二级缓存 = 毛坯房 (早期对象)");
        System.out.println("  🏡 一级缓存 = 精装房 (完整对象)");
        System.out.println();

        System.out.println("🔄 解决流程:");
        System.out.println("  1️⃣ 实例化UserService对象，将ObjectFactory放入三级缓存");
        System.out.println("  2️⃣ 开始属性注入，发现需要OrderService");
        System.out.println("  3️⃣ 创建OrderService，发现需要UserService");
        System.out.println("  4️⃣ 从三级缓存获取UserService的ObjectFactory");
        System.out.println("  5️⃣ 调用ObjectFactory创建早期UserService对象 (毛坯房)");
        System.out.println("  6️⃣ 将早期UserService放入二级缓存 (毛坯房存放处)，从三级缓存移除");
        System.out.println("  7️⃣ OrderService完成创建，UserService获得OrderService引用");
        System.out.println("  8️⃣ UserService完成初始化 (毛坯房装修完成)，放入一级缓存");
        System.out.println();

        System.out.println("⚠️ 重要限制:");
        System.out.println("  ❌ 构造函数循环依赖无法解决");
        System.out.println("  ✅ setter注入循环依赖可以解决");
        System.out.println();

        System.out.println("🎪 终极记忆口诀:");
        System.out.println("  Spring循环依赖不用慌，");
        System.out.println("  三级缓存来帮忙！");
        System.out.println("  工厂图纸三级放，");
        System.out.println("  毛坯半成品二级装，");
        System.out.println("  精装成品一级藏。");
        System.out.println("  循环依赖找二级，");
        System.out.println("  早期引用解困扰！");

        System.out.println("\n💡 快速测试问题:");
        System.out.println("问：为什么需要三级缓存？");
        System.out.println("答：就像盖房子的三个阶段：图纸 → 毛坯 → 精装，");
        System.out.println("   循环依赖时可以先给个毛坯房救急！");
        System.out.println();
        System.out.println("问：为什么构造函数循环依赖无法解决？");
        System.out.println("答：构造函数就像房子地基，地基没打好怎么能先住进去？");
        System.out.println("   setter注入就像装修，房子框架好了就能先住着慢慢装修。");
        System.out.println();
        System.out.println("问：三级缓存分别存什么？");
        System.out.println("答：🏭 三级：工厂方法 '我知道怎么造'");
        System.out.println("   🏘️ 二级：早期对象 '我造了个半成品'");
        System.out.println("   🏡 一级：完整对象 '我造了个成品'");
    }
}

