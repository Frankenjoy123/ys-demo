package com.yunsoo.api.config;

import com.yunsoo.api.cache.CustomKeyGenerator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.cloud.aws.cache.config.annotation.CacheClusterConfig;
//import org.springframework.cloud.aws.cache.config.annotation.EnableElastiCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/30
 * Descriptions:
 */
//@EnableElastiCache(@CacheClusterConfig(name = "dev-api-domain"))
@EnableCaching
@Configuration
public class CacheConfiguration extends CachingConfigurerSupport {

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator(){
        return new CustomKeyGenerator();
    }
}
