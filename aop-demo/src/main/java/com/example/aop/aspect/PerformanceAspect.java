package com.example.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 性能监控切面
 * 监控方法执行时间，对慢方法进行告警
 *
 * @author demo
 */
@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    /**
     * 性能监控阈值（毫秒）
     */
    private static final long SLOW_METHOD_THRESHOLD = 1000;

    /**
     * 定义切入点：监控Service层和Controller层的所有方法
     */
    @Pointcut("execution(* com.example.aop.service.*.*(..)) || " +
              "execution(* com.example.aop.controller.*.*(..))")
    public void monitoredMethods() {}

    /**
     * 环绕通知：监控方法执行时间
     */
    @Around("monitoredMethods()")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        try {
            // 执行原方法
            Object result = joinPoint.proceed();

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // 记录执行时间
            if (duration > SLOW_METHOD_THRESHOLD) {
                log.warn("🐌 [性能告警] 慢方法检测: {}.{} 执行时间: {}ms (超过阈值{}ms)",
                        className, methodName, duration, SLOW_METHOD_THRESHOLD);

                // 这里可以添加告警逻辑，比如发送邮件、短信、钉钉通知等
                sendSlowMethodAlert(className, methodName, duration);
            } else {
                log.info("⚡ [性能监控] {}.{} 执行时间: {}ms",
                        className, methodName, duration);
            }

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.error("💥 [性能监控] {}.{} 执行异常，耗时: {}ms, 异常: {}",
                    className, methodName, duration, e.getMessage());
            throw e;
        }
    }

    /**
     * 发送慢方法告警
     */
    private void sendSlowMethodAlert(String className, String methodName, long duration) {
        // 模拟发送告警
        log.warn("📧 [告警通知] 发送慢方法告警: {}.{} 执行时间: {}ms",
                className, methodName, duration);

        // 实际项目中可以集成：
        // 1. 邮件通知
        // 2. 短信通知
        // 3. 钉钉/企业微信通知
        // 4. 监控系统（如Prometheus + Grafana）
        // 5. APM系统（如SkyWalking、Pinpoint）
    }
}

