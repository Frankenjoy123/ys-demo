package com.yunsoo.api.rabbit.config;

import com.yunsoo.api.rabbit.cache.ObjectKeyGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class CacheConfiguration {

    @Configuration
    @EnableCaching
    @ConditionalOnProperty(value = "yunsoo.cache.type", havingValue = "local")
    public static class LocalCacheConfiguration {

        @Bean(name = "objectKeyGenerator")
        public KeyGenerator keyGenerator() {
            return new ObjectKeyGenerator();
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }

    }

    @Configuration
    @EnableCaching
    @ConditionalOnProperty(value = "yunsoo.cache.type", havingValue = "redis")
    public static class RedisCacheConfiguration {

        @Value("${yunsoo.cache.default_expire_time:3600}")
        private long defaultExpireTime;

        @Bean(name = "objectKeyGenerator")
        public KeyGenerator keyGenerator() {
            return new ObjectKeyGenerator();
        }

        @Bean
        public CacheManager cacheManager(RedisTemplate redisTemplate) {
            RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
            if (defaultExpireTime > 0) {
                redisCacheManager.setDefaultExpiration(defaultExpireTime);
            }
            return redisCacheManager;
        }

        @Bean(name = "org.springframework.autoconfigure.redis.RedisProperties")
        @ConfigurationProperties(prefix = "yunsoo.cache.redis")
        public RedisProperties redisProperties() {
            return new RedisProperties();
        }

    }

}