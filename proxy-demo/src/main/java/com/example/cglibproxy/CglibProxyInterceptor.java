package com.example.cglibproxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB代理拦截器
 * 实现MethodInterceptor接口，定义代理对象的行为
 */
public class CglibProxyInterceptor implements MethodInterceptor {

    /**
     * 拦截目标方法的执行
     * @param obj 代理对象
     * @param method 被拦截的方法
     * @param args 方法参数
     * @param proxy 用于调用原始方法的代理
     * @return 方法执行结果
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        before(method.getName(), args);
        // 调用原始方法
        // 注意：这里使用proxy.invokeSuper而不是method.invoke
        // proxy.invokeSuper调用的是父类（原始类）的方法
        Object result = proxy.invokeSuper(obj, args);
        after(method.getName(), result);
        return result;
    }

    /**
     * 前置处理：在执行方法之前执行
     */
    private void before(String methodName, Object... args) {
        System.out.println(String.format("【cglibproxy】log start time [%s] - 执行方法: %s, 参数: %s",
            new java.util.Date(), methodName, java.util.Arrays.toString(args)));
    }

    /**
     * 后置处理：在执行方法之后执行
     */
    private void after(String methodName, Object result) {
        System.out.println(String.format("【cglibproxy】log end time [%s] - 方法: %s, 返回结果: %s",
            new java.util.Date(), methodName, result));
    }
}

