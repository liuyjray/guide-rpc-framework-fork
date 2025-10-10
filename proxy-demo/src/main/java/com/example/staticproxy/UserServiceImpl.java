package com.example.staticproxy;

/**
 * 用户服务实现类（被代理的目标对象）
 */
public class UserServiceImpl implements UserService {

    @Override
    public String getUserById(Long userId) {
        // 模拟数据库查询
        System.out.println("正在查询用户ID: " + userId);
        return "用户" + userId + "的信息";
    }

    @Override
    public boolean saveUser(String userName) {
        // 模拟保存用户
        System.out.println("正在保存用户: " + userName);
        return true;
    }
}

