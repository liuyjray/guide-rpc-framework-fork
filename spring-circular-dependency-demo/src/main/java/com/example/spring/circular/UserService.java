package com.example.spring.circular;

/**
 * 用户服务 - 演示循环依赖
 *
 * 这个服务依赖OrderService，而OrderService又依赖UserService。
 * 这就形成了循环依赖，Spring使用三级缓存来解决这个问题。
 *
 * 记忆技巧：
 * - 把这个想象成盖房子类比中的"房子A"
 * - UserService = 房子A 需要 OrderService = 房子B的梯子
 * - 但是 OrderService = 房子B 需要 UserService = 房子A的电钻
 *
 * @author demo
 */
public class UserService {

    private OrderService orderService;

    /**
     * Setter注入 - 允许循环依赖解决
     * ✅ 这种方式可以被Spring的三级缓存解决
     *
     * 记忆：就像房子框架建好后再安装设备
     */
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
        System.out.println("👤 UserService 收到了 OrderService 依赖");
    }

    /**
     * 业务方法：创建用户
     */
    public void createUser(String username) {
        System.out.println("👤 UserService: 正在创建用户 " + username);

        // 使用注入的OrderService
        if (orderService != null) {
            orderService.createWelcomeOrder(username);
        }
    }

    /**
     * 业务方法：获取用户信息
     */
    public String getUserInfo(String username) {
        System.out.println("👤 UserService: 正在获取用户信息 " + username);
        return "用户: " + username + " (活跃)";
    }

    /**
     * 被OrderService调用的方法：验证用户
     */
    public boolean validateUser(String username) {
        System.out.println("👤 UserService: 正在验证用户 " + username);
        return username != null && !username.trim().isEmpty();
    }
}

