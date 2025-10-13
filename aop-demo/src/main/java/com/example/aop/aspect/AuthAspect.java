package com.example.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验切面
 * 演示自定义注解 + AOP 实现权限控制
 *
 * @author demo
 */
@Aspect
@Component
@Slf4j
public class AuthAspect {

    /**
     * 自定义权限校验注解
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequireAuth {
        /**
         * 需要的角色
         */
        String role() default "USER";

        /**
         * 权限描述
         */
        String description() default "";
    }

    /**
     * 前置通知：权限校验
     */
    @Before("@annotation(requireAuth)")
    public void checkAuth(JoinPoint joinPoint, RequireAuth requireAuth) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String requiredRole = requireAuth.role();
        String description = requireAuth.description();

        log.info("🔐 [权限校验] 检查方法: {}.{}", className, methodName);
        log.info("🎭 [权限校验] 需要角色: {}", requiredRole);
        if (!description.isEmpty()) {
            log.info("📝 [权限校验] 权限描述: {}", description);
        }

        // 模拟获取当前用户角色（实际项目中从SecurityContext或Session获取）
        String currentUserRole = getCurrentUserRole();
        log.info("👤 [权限校验] 当前用户角色: {}", currentUserRole);

        // 权限校验逻辑
        if (!hasPermission(currentUserRole, requiredRole)) {
            log.error("❌ [权限校验] 权限不足！当前角色: {}, 需要角色: {}", currentUserRole, requiredRole);
            throw new SecurityException(String.format("权限不足，需要角色: %s，当前角色: %s", requiredRole, currentUserRole));
        }

        log.info("✅ [权限校验] 权限校验通过");
    }

    /**
     * 模拟获取当前用户角色
     * 实际项目中应该从以下地方获取：
     * 1. Spring Security的SecurityContext
     * 2. JWT Token解析
     * 3. Session
     * 4. ThreadLocal
     */
    private String getCurrentUserRole() {
        // 这里简单模拟，实际项目中需要根据具体的认证方案来实现
        // 为了演示，我们随机返回不同角色
        String[] roles = {"USER", "ADMIN", "GUEST"};
        int index = (int) (System.currentTimeMillis() % 3);
        return roles[index];
    }

    /**
     * 权限校验逻辑
     * 实际项目中可能需要更复杂的权限模型，比如：
     * 1. 基于角色的访问控制（RBAC）
     * 2. 基于属性的访问控制（ABAC）
     * 3. 层次化角色权限
     */
    private boolean hasPermission(String currentRole, String requiredRole) {
        if (currentRole == null) {
            return false;
        }

        // 简单的权限层次：ADMIN > USER > GUEST
        switch (requiredRole) {
            case "GUEST":
                return true; // 所有人都可以访问GUEST级别的资源
            case "USER":
                return "USER".equals(currentRole) || "ADMIN".equals(currentRole);
            case "ADMIN":
                return "ADMIN".equals(currentRole);
            default:
                return false;
        }
    }
}

