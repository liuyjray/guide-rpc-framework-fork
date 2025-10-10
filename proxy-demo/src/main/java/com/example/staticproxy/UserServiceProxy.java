package com.example.staticproxy;

import java.util.Date;

/**
 * 用户服务静态代理类
 * 在不修改原有代码的基础上，增加额外功能（如日志、权限检查等）
 * 使用before/after模式，代码更清晰简洁
 */
public class UserServiceProxy implements UserService {

    private UserService target; // 被代理的目标对象

    public UserServiceProxy(UserService target) {
        this.target = target;
    }

    @Override
    public String getUserById(Long userId) {
        before("getUserById", userId);
        String result = target.getUserById(userId); // 实际调用真实主题角色的方法
        after("getUserById", result);
        return result;
    }

    @Override
    public boolean saveUser(String userName) {
        before("saveUser", userName);
        boolean result = target.saveUser(userName); // 实际调用真实主题角色的方法
        after("saveUser", result);

        return result;
    }

    /**
     * 前置处理：在执行方法之前执行
     */
    private void before(String methodName, Object... args) {
        System.out.println(String.format("【staticproxy】log start time [%s] - 执行方法: %s, 参数: %s",
            new Date(), methodName, java.util.Arrays.toString(args)));
    }

    /**
     * 后置处理：在执行方法之后执行
     */
    private void after(String methodName, Object result) {
        System.out.println(String.format("【staticproxy】log end time [%s] - 方法: %s, 返回结果: %s",
            new Date(), methodName, result));
    }


}

