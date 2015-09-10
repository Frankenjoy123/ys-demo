package com.yunsoo.api.config;

import com.yunsoo.api.cache.CustomKeyGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.aws.cache.config.annotation.CacheClusterConfig;
import org.springframework.cloud.aws.cache.config.annotation.EnableElastiCache;
import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CacheConfiguration {

    public static final String CACHE_NAME = "yunsoo-cache";


    @Configuration
    @EnableElastiCache(value = {@CacheClusterConfig(name = CACHE_NAME)}, defaultExpiration = 3600)
    @ConditionalOnAwsCloudEnvironment
    @ConditionalOnProperty(value = "yunsoo.cache.type", havingValue = "elasticache")
    public static class ElastiCacheConfiguration {

        @Bean
        public KeyGenerator keyGenerator() {
            return new CustomKeyGenerator();
        }

    }

    @Configuration
    @EnableCaching
    @ConditionalOnProperty(value = "yunsoo.cache.type", havingValue = "local")
    public static class LocalCacheConfiguration {

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }

        @Bean
        public KeyGenerator keyGenerator() {
            return new CustomKeyGenerator();
        }

    }

}