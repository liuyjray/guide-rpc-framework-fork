package com.example.staticproxy;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 根据用户ID查询用户信息
     */
    String getUserById(Long userId);

    /**
     * 保存用户信息
     */
    boolean saveUser(String userName);
}

