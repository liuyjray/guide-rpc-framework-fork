package com.example.spring.circular;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简化版Spring容器 - 演示三级缓存机制
 *
 * 这是一个简化版的Spring IoC容器实现，演示了Spring如何使用三级缓存解决循环依赖。
 *
 * 记忆技巧 - 三级缓存（盖房子类比）：
 * 🏭 三级缓存 (singletonFactories) = 建房图纸（工厂方法）
 * 🏘️ 二级缓存 (earlySingletonObjects) = 毛坯房（早期Bean引用）
 * 🏡 一级缓存 (singletonObjects) = 精装房（完整Bean）
 *
 * 记忆口诀："工厂早期完整" - 3-2-1缓存级别
 *
 * @author demo
 */
public class SimplifiedSpringContainer {

    // 🏡 一级缓存：完整的Bean（精装房）
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    // 🏘️ 二级缓存：早期Bean引用（毛坯房）
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();

    // 🏭 三级缓存：Bean工厂（建房图纸）
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    // 正在创建的Bean集合（用于检测循环依赖）
    private final Set<String> singletonsCurrentlyInCreation = new HashSet<>();

    /**
     * 🎯 核心方法：获取Bean（记忆友好版本）
     *
     * 记忆顺序："检查精装房 → 检查毛坯房 → 使用图纸 → 建新房"
     *
     * @param beanName 要获取的Bean名称
     * @return Bean实例
     */
    public Object getBean(String beanName) {
        System.out.println("\n🔍 开始获取Bean: " + beanName);

        // 🏡 步骤1: 检查精装房（一级缓存）
        Object bean = singletonObjects.get(beanName);
        if (bean != null) {
            System.out.println("✅ 从一级缓存(精装房)获取: " + beanName);
            return bean;
        }

        // 🏘️ 步骤2: 检查毛坯房（二级缓存）- 仅在正在创建时
        if (singletonsCurrentlyInCreation.contains(beanName)) {
            bean = earlySingletonObjects.get(beanName);
            if (bean != null) {
                System.out.println("✅ 从二级缓存(毛坯房)获取: " + beanName);
                return bean;
            }

            // 🏭 步骤3: 使用图纸（三级缓存）
            ObjectFactory<?> factory = singletonFactories.get(beanName);
            if (factory != null) {
                System.out.println("🏗️ 从三级缓存(图纸)创建毛坯房: " + beanName);
                bean = factory.getObject();  // 按图纸建毛坯房
                earlySingletonObjects.put(beanName, bean);  // 毛坯房入库
                singletonFactories.remove(beanName);        // 图纸用完就扔
                System.out.println("📦 毛坯房已入库二级缓存: " + beanName);
                return bean;
            }
        }

        // 🏗️ 步骤4: 建全新的Bean
        return createBean(beanName);
    }

    /**
     * 🏗️ 创建Bean的过程
     *
     * 记忆步骤："标记 → 建框架 → 存图纸 → 装修 → 完工"
     *
     * @param beanName 要创建的Bean名称
     * @return 创建的Bean
     */
    private Object createBean(String beanName) {
        System.out.println("🏗️ 开始创建Bean: " + beanName);

        // 标记为正在创建
        singletonsCurrentlyInCreation.add(beanName);

        // 🏠 步骤1: 建房框架（实例化）
        Object bean = instantiateBean(beanName);
        System.out.println("🏘️ 房屋框架建好了: " + beanName);

        // 🏭 步骤2: 把建房图纸存入三级缓存
        singletonFactories.put(beanName, () -> {
            System.out.println("🏭 工厂方法被调用，返回早期引用: " + beanName);
            return bean;  // 简化版，真实Spring这里会处理AOP等
        });
        System.out.println("📋 建房图纸已存入三级缓存: " + beanName);

        // 🔧 步骤3: 装修房子（属性注入）
        populateBean(bean, beanName);

        // 🏡 步骤4: 精装房完工，存入一级缓存
        singletonObjects.put(beanName, bean);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
        singletonsCurrentlyInCreation.remove(beanName);

        System.out.println("🎉 精装房完工并存入一级缓存: " + beanName);
        return bean;
    }

    /**
     * 🏠 实例化Bean（建房框架）
     *
     * @param beanName 要实例化的Bean名称
     * @return 实例化的Bean
     */
    private Object instantiateBean(String beanName) {
        switch (beanName) {
            case "userService":
                return new UserService();
            case "orderService":
                return new OrderService();
            default:
                throw new RuntimeException("未知的Bean: " + beanName);
        }
    }

    /**
     * 🔧 属性注入（装修房子）
     *
     * @param bean 要填充的Bean
     * @param beanName Bean的名称
     */
    private void populateBean(Object bean, String beanName) {
        System.out.println("🔧 开始装修房子(属性注入): " + beanName);

        if (bean instanceof UserService) {
            UserService userService = (UserService) bean;
            // UserService 需要 OrderService
            OrderService orderService = (OrderService) getBean("orderService");
            userService.setOrderService(orderService);
        } else if (bean instanceof OrderService) {
            OrderService orderService = (OrderService) bean;
            // OrderService 需要 UserService
            UserService userService = (UserService) getBean("userService");
            orderService.setUserService(userService);
        }

        System.out.println("✅ 装修完成: " + beanName);
    }

    /**
     * 📊 显示缓存状态用于调试
     */
    public void printCacheStatus() {
        System.out.println("\n📊 缓存状态:");
        System.out.println("🏡 一级缓存(精装房): " + singletonObjects.keySet());
        System.out.println("🏘️ 二级缓存(毛坯房): " + earlySingletonObjects.keySet());
        System.out.println("🏭 三级缓存(图纸): " + singletonFactories.keySet());
        System.out.println("🏗️ 正在创建: " + singletonsCurrentlyInCreation);
    }

    /**
     * 检查容器中是否存在指定的Bean
     *
     * @param beanName Bean的名称
     * @return 如果Bean存在则返回true
     */
    public boolean containsBean(String beanName) {
        return singletonObjects.containsKey(beanName) ||
               earlySingletonObjects.containsKey(beanName) ||
               singletonFactories.containsKey(beanName);
    }

    /**
     * 获取容器中Bean的数量
     *
     * @return Bean的总数量
     */
    public int getBeanCount() {
        return singletonObjects.size();
    }
}

