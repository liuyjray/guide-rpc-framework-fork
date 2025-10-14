package com.example.spring.circular;

/**
 * 订单服务 - 演示循环依赖
 *
 * 这个服务依赖UserService，而UserService又依赖OrderService。
 * 这就形成了循环依赖，Spring使用三级缓存来解决这个问题。
 *
 * 记忆技巧：
 * - 把这个想象成盖房子类比中的"房子B"
 * - OrderService = 房子B 需要 UserService = 房子A的电钻
 * - 但是 UserService = 房子A 需要 OrderService = 房子B的梯子
 *
 * @author demo
 */
public class OrderService {

    private UserService userService;

    /**
     * Setter注入 - 允许循环依赖解决
     * ✅ 这种方式可以被Spring的三级缓存解决
     *
     * 记忆：就像房子框架建好后再安装设备
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
        System.out.println("📦 OrderService 收到了 UserService 依赖");
    }

    /**
     * 业务方法：创建订单
     */
    public void createOrder(String username, String product) {
        System.out.println("📦 OrderService: 正在为 " + username + " 创建订单 - " + product);

        // 使用注入的UserService来验证用户
        if (userService != null && userService.validateUser(username)) {
            System.out.println("📦 OrderService: 订单创建成功");
        } else {
            System.out.println("📦 OrderService: 订单创建失败 - 用户无效");
        }
    }

    /**
     * 被UserService调用的方法：创建欢迎订单
     */
    public void createWelcomeOrder(String username) {
        System.out.println("📦 OrderService: 正在为新用户 " + username + " 创建欢迎订单");

        // 创建欢迎订单前先验证用户
        if (userService != null && userService.validateUser(username)) {
            System.out.println("📦 OrderService: 欢迎订单创建成功 - 免费配送券");
        }
    }

    /**
     * 业务方法：获取订单状态
     */
    public String getOrderStatus(String orderId) {
        System.out.println("📦 OrderService: 正在获取订单状态 " + orderId);
        return "订单 " + orderId + " 正在处理中";
    }
}

