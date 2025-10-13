package com.example.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Spring AOP 演示应用启动类
 *
 * @author demo
 */
@SpringBootApplication
@EnableAspectJAutoProxy  // 启用AOP自动代理
public class AopDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopDemoApplication.class, args);
        System.out.println("\n🎉 Spring AOP 演示应用启动成功！");
        System.out.println("📖 访问 http://localhost:8081 查看演示效果");
        System.out.println("📚 API文档:");
        System.out.println("   GET  /users/{id}     - 获取用户信息");
        System.out.println("   POST /users          - 创建用户");
        System.out.println("   PUT  /users/{id}     - 更新用户");
        System.out.println("   DELETE /users/{id}   - 删除用户");
        System.out.println("   GET  /admin/stats    - 管理员统计(需要权限)");
        System.out.println("   GET  /test/exception - 测试异常处理");
        System.out.println("   GET  /test/slow      - 测试慢接口监控\n");
    }
}

