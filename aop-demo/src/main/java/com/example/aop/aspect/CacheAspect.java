package com.example.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存切面
 * 演示自定义缓存注解 + AOP 实现方法级缓存
 *
 * @author demo
 */
@Aspect
@Component
@Slf4j
public class CacheAspect {

    /**
     * 简单的内存缓存
     * 实际项目中建议使用Redis、Caffeine等专业缓存组件
     */
    private final ConcurrentHashMap<String, CacheItem> cache = new ConcurrentHashMap<>();

    /**
     * 自定义缓存注解
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cacheable {
        /**
         * 缓存key前缀
         */
        String value() default "";

        /**
         * 缓存过期时间（秒）
         */
        int expireTime() default 300; // 默认5分钟
    }

    /**
     * 缓存数据项
     */
    private static class CacheItem {
        private final Object data;
        private final long expireTime;

        public CacheItem(Object data, long expireTime) {
            this.data = data;
            this.expireTime = expireTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expireTime;
        }

        public Object getData() {
            return data;
        }
    }

    /**
     * 环绕通知：缓存处理
     */
    @Around("@annotation(cacheable)")
    public Object handleCache(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        // 生成缓存key
        String cacheKey = generateCacheKey(joinPoint, cacheable);

        // 尝试从缓存获取
        CacheItem cacheItem = cache.get(cacheKey);
        if (cacheItem != null && !cacheItem.isExpired()) {
            log.info("🎯 [缓存命中] key: {}", cacheKey);
            return cacheItem.getData();
        }

        // 缓存未命中或已过期，执行原方法
        log.info("❌ [缓存未命中] key: {}, 执行原方法", cacheKey);
        Object result = joinPoint.proceed();

        // 将结果存入缓存
        long expireTime = System.currentTimeMillis() + cacheable.expireTime() * 1000L;
        cache.put(cacheKey, new CacheItem(result, expireTime));
        log.info("💾 [缓存存储] key: {}, 过期时间: {}秒", cacheKey, cacheable.expireTime());

        return result;
    }

    /**
     * 生成缓存key
     */
    private String generateCacheKey(ProceedingJoinPoint joinPoint, Cacheable cacheable) {
        StringBuilder keyBuilder = new StringBuilder();

        // 添加前缀
        if (!cacheable.value().isEmpty()) {
            keyBuilder.append(cacheable.value()).append(":");
        }

        // 添加类名和方法名
        keyBuilder.append(joinPoint.getTarget().getClass().getSimpleName())
                  .append(".")
                  .append(joinPoint.getSignature().getName());

        // 添加参数
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            keyBuilder.append(":")
                      .append(Arrays.toString(args).hashCode());
        }

        return keyBuilder.toString();
    }

    /**
     * 清空缓存（可以通过管理接口调用）
     */
    public void clearCache() {
        cache.clear();
        log.info("🗑️ [缓存清理] 所有缓存已清空");
    }

    /**
     * 获取缓存统计信息
     */
    public String getCacheStats() {
        long totalItems = cache.size();
        long expiredItems = cache.values().stream()
                .mapToLong(item -> item.isExpired() ? 1 : 0)
                .sum();

        return String.format("缓存统计 - 总数: %d, 过期: %d, 有效: %d",
                totalItems, expiredItems, totalItems - expiredItems);
    }
}

