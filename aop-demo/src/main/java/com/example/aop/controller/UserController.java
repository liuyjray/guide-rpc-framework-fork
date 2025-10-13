package com.example.aop.controller;

import com.example.aop.aspect.AuthAspect;
import com.example.aop.aspect.CacheAspect;
import com.example.aop.dto.ApiResponse;
import com.example.aop.entity.User;
import com.example.aop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 * 演示各种AOP切面的使用
 *
 * @author demo
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据ID获取用户信息
     * 演示：日志切面、性能监控切面、缓存切面、数据脱敏切面
     */
    @GetMapping("/{id}")
    @CacheAspect.Cacheable(value = "user", expireTime = 60) // 缓存1分钟
    public ApiResponse<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ApiResponse.success("获取用户信息成功", user);
    }

    /**
     * 获取所有用户
     * 演示：日志切面、性能监控切面、数据脱敏切面
     */
    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ApiResponse.success("获取用户列表成功", users);
    }

    /**
     * 创建用户
     * 演示：日志切面、参数校验切面
     */
    @PostMapping
    public ApiResponse<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ApiResponse.success("创建用户成功", createdUser);
    }

    /**
     * 更新用户信息
     * 演示：日志切面、权限校验切面
     */
    @PutMapping("/{id}")
    @AuthAspect.RequireAuth(role = "USER", description = "更新用户信息需要USER权限")
    public ApiResponse<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ApiResponse.success("更新用户成功", updatedUser);
    }

    /**
     * 删除用户
     * 演示：日志切面、权限校验切面
     */
    @DeleteMapping("/{id}")
    @AuthAspect.RequireAuth(role = "ADMIN", description = "删除用户需要ADMIN权限")
    public ApiResponse<Boolean> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        return ApiResponse.success("删除用户成功", deleted);
    }
}

