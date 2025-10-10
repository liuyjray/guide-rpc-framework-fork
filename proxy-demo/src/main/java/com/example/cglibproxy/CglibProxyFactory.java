package com.example.cglibproxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * CGLIB代理工厂
 * 用于创建CGLIB动态代理对象
 */
public class CglibProxyFactory {

    /**
     * 创建CGLIB代理对象
     * @param targetClass 目标类的Class对象
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> targetClass) {
        // 创建Enhancer对象，用于生成代理类
        Enhancer enhancer = new Enhancer();

        // 设置父类（被代理的类）
        enhancer.setSuperclass(targetClass);

        // 设置回调函数（拦截器）
        enhancer.setCallback(new CglibProxyInterceptor());

        // 创建代理对象
        return (T) enhancer.create();
    }

    /**
     * 创建CGLIB代理对象（带自定义拦截器）
     * @param targetClass 目标类的Class对象
     * @param interceptor 自定义拦截器
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> targetClass, CglibProxyInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(interceptor);

        return (T) enhancer.create();
    }
}

