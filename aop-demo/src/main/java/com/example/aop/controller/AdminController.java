package com.example.aop.controller;

import com.example.aop.aspect.AuthAspect;
import com.example.aop.aspect.CacheAspect;
import com.example.aop.dto.ApiResponse;
import com.example.aop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员控制器
 * 演示权限校验切面
 *
 * @author demo
 */
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheAspect cacheAspect;

    /**
     * 获取用户统计信息（需要管理员权限）
     * 演示：权限校验切面
     */
    @GetMapping("/stats")
    @AuthAspect.RequireAuth(role = "ADMIN", description = "查看用户统计需要ADMIN权限")
    public ApiResponse<String> getUserStats() {
        String stats = userService.getUserStats();
        return ApiResponse.success("获取统计信息成功", stats);
    }

    /**
     * 清空缓存（需要管理员权限）
     */
    @GetMapping("/cache/clear")
    @AuthAspect.RequireAuth(role = "ADMIN", description = "清空缓存需要ADMIN权限")
    public ApiResponse<String> clearCache() {
        cacheAspect.clearCache();
        return ApiResponse.success("缓存清空成功", "所有缓存已清空");
    }

    /**
     * 获取缓存统计信息
     */
    @GetMapping("/cache/stats")
    @AuthAspect.RequireAuth(role = "ADMIN", description = "查看缓存统计需要ADMIN权限")
    public ApiResponse<String> getCacheStats() {
        String stats = cacheAspect.getCacheStats();
        return ApiResponse.success("获取缓存统计成功", stats);
    }
}

