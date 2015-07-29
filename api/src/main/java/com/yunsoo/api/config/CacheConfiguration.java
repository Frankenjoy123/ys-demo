package com.yunsoo.api.config;

import com.yunsoo.api.cache.CustomKeyGenerator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration;
import org.springframework.cloud.aws.cache.config.annotation.CacheClusterConfig;
import org.springframework.cloud.aws.cache.config.annotation.ElastiCacheCachingConfiguration;
import org.springframework.cloud.aws.cache.config.annotation.EnableElastiCache;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/30
 * Descriptions:
 */




@Configuration
public class CacheConfiguration {

    @Bean
    public KeyGenerator keyGenerator(){
        return new CustomKeyGenerator();
    }

    @Configuration
    @EnableCaching
    @Profile("local")
    protected static class LocalCacheConfiguration {

        @Bean
        public  CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }
    }

    @Configuration
    @EnableElastiCache(value = {@CacheClusterConfig(name = "dev-cache", expiration = 3600)})
    @EnableContextRegion(region = "cn-north-1")
    @Profile("!local")
    protected static class ElastiCacheConfiguration {



    }

}
