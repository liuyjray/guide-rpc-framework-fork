package com.example.jdkproxy;

import java.lang.reflect.Proxy;

/**
 * JDK动态代理工厂
 * 用于创建动态代理对象
 */
public class JdkProxyFactory {

    /**
     * 创建代理对象
     * @param target 被代理的目标对象
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target) {
        // 获取目标对象的类加载器
        ClassLoader classLoader = target.getClass().getClassLoader();

        // 获取目标对象实现的所有接口
        Class<?>[] interfaces = target.getClass().getInterfaces();

        // 创建InvocationHandler
        JdkProxyHandler handler = new JdkProxyHandler(target);

        // 使用Proxy.newProxyInstance创建代理对象
        return (T) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }

    /**
     * 创建代理对象（带自定义处理器）
     * @param target 被代理的目标对象
     * @param handler 自定义的InvocationHandler
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, JdkProxyHandler handler) {
        ClassLoader classLoader = target.getClass().getClassLoader();
        Class<?>[] interfaces = target.getClass().getInterfaces();

        return (T) Proxy.newProxyInstance(classLoader, interfaces, handler);
    }
}

