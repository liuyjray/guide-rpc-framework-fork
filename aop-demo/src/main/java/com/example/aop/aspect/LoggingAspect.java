package com.example.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志记录切面
 * 演示：@Around、@Before、@After、@AfterReturning、@AfterThrowing
 *
 * @author demo
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * 定义切入点：拦截Controller层的所有方法
     */
    @Pointcut("execution(* com.example.aop.controller.*.*(..))")
    public void controllerLayer() {}

    /**
     * 定义切入点：拦截Service层的所有方法
     */
    @Pointcut("execution(* com.example.aop.service.*.*(..))")
    public void serviceLayer() {}

    /**
     * 环绕通知 - Controller层
     * 记录请求信息、响应时间等
     */
    @Around("controllerLayer()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            log.info("🌐 ===== HTTP请求开始 =====");
            log.info("📍 请求URL: {}", request.getRequestURL());
            log.info("🔧 请求方法: {}", request.getMethod());
            log.info("🎯 执行方法: {}.{}",
                    joinPoint.getTarget().getClass().getSimpleName(),
                    joinPoint.getSignature().getName());
            log.info("📝 请求参数: {}", Arrays.toString(joinPoint.getArgs()));
        }

        try {
            // 执行原方法
            Object result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            log.info("✅ 请求成功，响应时间: {}ms", endTime - startTime);
            log.info("📤 响应结果: {}", result);
            log.info("🌐 ===== HTTP请求结束 =====\n");

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("❌ 请求失败，响应时间: {}ms", endTime - startTime);
            log.error("💥 异常信息: {}", e.getMessage());
            log.info("🌐 ===== HTTP请求结束 =====\n");
            throw e;
        }
    }

    /**
     * 前置通知 - Service层
     */
    @Before("serviceLayer()")
    public void logServiceBefore(JoinPoint joinPoint) {
        log.info("🔧 [前置通知] 准备执行: {}.{}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    /**
     * 后置通知 - Service层
     */
    @After("serviceLayer()")
    public void logServiceAfter(JoinPoint joinPoint) {
        log.info("🏁 [后置通知] 执行完成: {}.{}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName());
    }

    /**
     * 返回通知 - Service层
     */
    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logServiceReturn(JoinPoint joinPoint, Object result) {
        log.info("✅ [返回通知] 方法正常返回: {}.{}, 返回值: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                result);
    }

    /**
     * 异常通知 - Service层
     */
    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
    public void logServiceException(JoinPoint joinPoint, Exception exception) {
        log.error("💥 [异常通知] 方法执行异常: {}.{}, 异常: {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }
}

