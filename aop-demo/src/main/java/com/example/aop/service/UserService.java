package com.example.aop.service;

import com.example.aop.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 用户服务类
 * 演示各种AOP切面功能
 *
 * @author demo
 */
@Service
public class UserService {

    private final ConcurrentHashMap<Long, User> userStorage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserService() {
        // 初始化一些测试数据
        initTestData();
    }

    private void initTestData() {
        User user1 = new User(1L, "张三", "zhangsan@example.com", "13812345678", "123456789012345678", 25, "USER");
        User user2 = new User(2L, "李四", "lisi@example.com", "13987654321", "987654321098765432", 30, "ADMIN");
        User user3 = new User(3L, "王五", "wangwu@example.com", "13555666777", "555666777888999000", 28, "USER");

        userStorage.put(1L, user1);
        userStorage.put(2L, user2);
        userStorage.put(3L, user3);
        idGenerator.set(4);
    }

    /**
     * 根据ID获取用户信息
     * 演示：缓存切面、日志切面、数据脱敏切面
     */
    public User getUserById(Long id) {
        System.out.println("📋 UserService.getUserById() 执行中...");

        // 模拟数据库查询延迟
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        User user = userStorage.get(id);
        if (user == null) {
            throw new RuntimeException("用户不存在，ID: " + id);
        }

        return user;
    }

    /**
     * 创建用户
     * 演示：参数校验切面、事务切面
     */
    public User createUser(User user) {
        System.out.println("📝 UserService.createUser() 执行中...");

        if (user == null) {
            throw new IllegalArgumentException("用户对象不能为空");
        }

        // 设置ID
        user.setId(idGenerator.getAndIncrement());

        // 默认角色
        if (user.getRole() == null) {
            user.setRole("USER");
        }

        // 保存用户
        userStorage.put(user.getId(), user);

        return user;
    }

    /**
     * 更新用户信息
     * 演示：权限校验切面
     */
    public User updateUser(Long id, User user) {
        System.out.println("✏️ UserService.updateUser() 执行中...");

        User existingUser = userStorage.get(id);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在，ID: " + id);
        }

        // 更新用户信息
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getAge() != null) {
            existingUser.setAge(user.getAge());
        }

        return existingUser;
    }

    /**
     * 删除用户
     * 演示：权限校验切面、审计日志切面
     */
    public boolean deleteUser(Long id) {
        System.out.println("🗑️ UserService.deleteUser() 执行中...");

        User user = userStorage.remove(id);
        return user != null;
    }

    /**
     * 获取所有用户
     * 演示：性能监控切面
     */
    public List<User> getAllUsers() {
        System.out.println("📋 UserService.getAllUsers() 执行中...");

        // 模拟复杂查询
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new ArrayList<>(userStorage.values());
    }

    /**
     * 获取用户统计信息（管理员功能）
     * 演示：权限校验切面
     */
    public String getUserStats() {
        System.out.println("📊 UserService.getUserStats() 执行中...");

        long totalUsers = userStorage.size();
        long adminUsers = userStorage.values().stream()
                .filter(user -> "ADMIN".equals(user.getRole()))
                .count();

        return String.format("总用户数: %d, 管理员数: %d", totalUsers, adminUsers);
    }

    /**
     * 模拟异常方法
     * 演示：异常处理切面
     */
    public void simulateException() {
        System.out.println("💥 UserService.simulateException() 执行中...");
        throw new RuntimeException("这是一个模拟异常，用于测试异常处理切面");
    }

    /**
     * 模拟慢方法
     * 演示：性能监控切面
     */
    public String slowMethod() {
        System.out.println("🐌 UserService.slowMethod() 执行中...");

        try {
            // 模拟耗时操作
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return "慢方法执行完成";
    }
}

