package com.example.jdkproxy;

import com.example.staticproxy.UserService;
import com.example.staticproxy.UserServiceImpl;

/**
 * JDK动态代理演示
 */
public class JdkProxyDemo {

    public static void main(String[] args) {
        System.out.println("=== JDK动态代理演示 ===\n");

        // 1. 创建目标对象
        UserService userService = new UserServiceImpl();

        // 2. 使用工厂创建代理对象
        UserService proxy = JdkProxyFactory.createProxy(userService);

        // 3. 查看代理对象的类信息
        System.out.println("目标对象类型: " + userService.getClass().getName());
        System.out.println("代理对象类型: " + proxy.getClass().getName());
        System.out.println("代理对象是否是Proxy的实例: " + (proxy instanceof java.lang.reflect.Proxy));
        System.out.println("代理对象实现的接口: " + java.util.Arrays.toString(proxy.getClass().getInterfaces()));
        System.out.println();

        // 4. 通过代理对象调用方法
        System.out.println("--- 测试查询用户 ---");
        String user = proxy.getUserById(2001L);
        System.out.println();

        System.out.println("--- 测试保存用户 ---");
        boolean success = proxy.saveUser("李四");
        System.out.println();

        // 5. 演示多接口代理
        demonstrateMultiInterfaceProxy();

    }

    /**
     * 演示多接口代理
     */
    private static void demonstrateMultiInterfaceProxy() {
        System.out.println("=== 多接口代理演示 ===");

        // 创建实现多个接口的对象
        MultiServiceImpl multiService = new MultiServiceImpl();

        // ❌ 错误的做法：强制转换为具体类型
//        MultiServiceImpl proxy0 = JdkProxyFactory.createProxy(multiService);
//        proxy0.getUserById(3001L);
//        proxy0.createOrder("订单001");


        // 创建代理对象
        Object proxy = JdkProxyFactory.createProxy(multiService);

        System.out.println("多接口服务代理类型: " + proxy.getClass().getSimpleName());
        System.out.println("实现的接口: " + java.util.Arrays.toString(proxy.getClass().getInterfaces()));

        // 代理对象可以转换为任何目标对象实现的接口
        if (proxy instanceof UserService) {
            UserService userProxy = (UserService) proxy;
            userProxy.getUserById(3001L);
        }

        if (proxy instanceof OrderService) {
            OrderService orderProxy = (OrderService) proxy;
            orderProxy.createOrder("订单001");
        }
    }

    /**
     * 订单服务接口
     */
    interface OrderService {
        String createOrder(String orderNo);
    }

    /**
     * 实现多个接口的服务类
     */
    static class MultiServiceImpl implements UserService, OrderService {

        @Override
        public String getUserById(Long userId) {
            System.out.println("MultiService: 查询用户 " + userId);
            return "用户" + userId;
        }

        @Override
        public boolean saveUser(String userName) {
            System.out.println("MultiService: 保存用户 " + userName);
            return true;
        }

        @Override
        public String createOrder(String orderNo) {
            System.out.println("MultiService: 创建订单 " + orderNo);
            return "订单创建成功: " + orderNo;
        }
    }
}

