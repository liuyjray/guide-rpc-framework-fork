package com.example.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK动态代理处理器
 * 实现InvocationHandler接口，定义代理对象的行为
 */
public class JdkProxyHandler implements InvocationHandler {

    private Object target; // 被代理的目标对象

    public JdkProxyHandler(Object target) {
        this.target = target;
    }

    /**
     * 代理对象的所有方法调用都会转发到这个方法
     * @param proxy 代理对象本身
     * @param method 被调用的方法
     * @param args 方法参数
     * @return 方法执行结果
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before(method.getName(), args);
        Object result = method.invoke(target, args); // 执行目标方法
        after(method.getName(), result);
        return result;
    }

    /**
     * 前置处理：在执行方法之前执行
     */
    private void before(String methodName, Object... args) {
        System.out.println(String.format("【jdkproxy】log start time [%s] - 执行方法: %s, 参数: %s",
            new java.util.Date(), methodName, java.util.Arrays.toString(args)));
    }

    /**
     * 后置处理：在执行方法之后执行
     */
    private void after(String methodName, Object result) {
        System.out.println(String.format("【jdkproxy】log end time [%s] - 方法: %s, 返回结果: %s",
            new java.util.Date(), methodName, result));
    }
}

