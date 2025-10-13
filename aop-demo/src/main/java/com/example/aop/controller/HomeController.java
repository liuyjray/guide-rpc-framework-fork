package com.example.aop.controller;

import com.example.aop.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页控制器
 * 提供API文档和使用说明
 *
 * @author demo
 */
@RestController
public class HomeController {

    /**
     * 首页 - API文档
     */
    @GetMapping("/")
    public ApiResponse<Map<String, Object>> home() {
        Map<String, Object> apiDoc = new HashMap<>();

        apiDoc.put("title", "🎯 Spring AOP 演示项目");
        apiDoc.put("description", "这是一个完整的Spring AOP演示项目，展示了各种切面编程的实际应用");
        apiDoc.put("version", "1.0.0");

        Map<String, String> apis = new HashMap<>();
        apis.put("GET /", "API文档首页");
        apis.put("GET /users/{id}", "获取用户信息 (缓存+脱敏)");
        apis.put("GET /users", "获取所有用户 (脱敏)");
        apis.put("POST /users", "创建用户");
        apis.put("PUT /users/{id}", "更新用户 (需要USER权限)");
        apis.put("DELETE /users/{id}", "删除用户 (需要ADMIN权限)");
        apis.put("GET /admin/stats", "用户统计 (需要ADMIN权限)");
        apis.put("GET /admin/cache/clear", "清空缓存 (需要ADMIN权限)");
        apis.put("GET /admin/cache/stats", "缓存统计 (需要ADMIN权限)");
        apis.put("GET /test/exception", "测试异常处理");
        apis.put("GET /test/slow", "测试慢方法监控");
        apis.put("GET /test/welcome", "欢迎信息");

        apiDoc.put("apis", apis);

        Map<String, String> aopFeatures = new HashMap<>();
        aopFeatures.put("日志记录", "自动记录请求信息、方法执行时间");
        aopFeatures.put("性能监控", "监控慢方法，超过1秒自动告警");
        aopFeatures.put("权限校验", "基于注解的权限控制");
        aopFeatures.put("缓存管理", "方法级缓存，支持过期时间");
        aopFeatures.put("数据脱敏", "自动脱敏敏感信息（手机号、身份证、邮箱）");
        aopFeatures.put("异常处理", "统一异常记录和告警");

        apiDoc.put("aopFeatures", aopFeatures);

        Map<String, String> usage = new HashMap<>();
        usage.put("1", "启动应用后访问各个API接口");
        usage.put("2", "观察控制台日志，查看AOP切面的执行效果");
        usage.put("3", "多次访问同一个接口，观察缓存效果");
        usage.put("4", "访问需要权限的接口，观察权限校验");
        usage.put("5", "查看返回的用户数据，观察脱敏效果");

        apiDoc.put("usage", usage);

        return ApiResponse.success("Spring AOP演示项目API文档", apiDoc);
    }
}

