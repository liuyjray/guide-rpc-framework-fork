package com.example.aop.controller;

import com.example.aop.dto.ApiResponse;
import com.example.aop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * 用于测试各种AOP切面功能
 *
 * @author demo
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private UserService userService;

    /**
     * 测试异常处理切面
     */
    @GetMapping("/exception")
    public ApiResponse<String> testException() {
        try {
            userService.simulateException();
            return ApiResponse.success("不应该到达这里");
        } catch (Exception e) {
            return ApiResponse.error("异常测试成功: " + e.getMessage());
        }
    }

    /**
     * 测试慢方法监控
     * 演示：性能监控切面
     */
    @GetMapping("/slow")
    public ApiResponse<String> testSlowMethod() {
        String result = userService.slowMethod();
        return ApiResponse.success("慢方法测试完成", result);
    }

    /**
     * 首页欢迎信息
     */
    @GetMapping("/welcome")
    public ApiResponse<String> welcome() {
        return ApiResponse.success("欢迎使用Spring AOP演示应用！",
                "这是一个展示各种AOP切面功能的演示项目");
    }
}

