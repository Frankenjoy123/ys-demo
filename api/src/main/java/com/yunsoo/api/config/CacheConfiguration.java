package com.yunsoo.api.config;

import com.yunsoo.api.cache.CustomKeyGenerator;
import com.yunsoo.api.cache.NotOnAwsCloudEnvironmentCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.aws.cache.config.annotation.CacheClusterConfig;
import org.springframework.cloud.aws.cache.config.annotation.EnableElastiCache;
import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CacheConfiguration{


   // @Value("${yunsoo.elasticachename}")
    public static final String cacheName = "dev-cache";



    @Configuration
    @EnableElastiCache(value = {@CacheClusterConfig(name = CacheConfiguration.cacheName)})
    @EnableContextRegion(region = "cn-north-1")
    @ConditionalOnAwsCloudEnvironment
   // @Conditional(NotOnAwsCloudEnvironmentCondition.class)
    public static class ElastiCacheConfiguration {

        @Bean
        public KeyGenerator keyGenerator(){
            return new CustomKeyGenerator();
        }

    }

    @Configuration
    @EnableCaching
  //  @ConditionalOnAwsCloudEnvironment
    @Conditional(NotOnAwsCloudEnvironmentCondition.class)
    public static class LocalCacheConfiguration {

        @Bean
        public  CacheManager cacheManager() {
          return new ConcurrentMapCacheManager();
        }

        @Bean
        public KeyGenerator keyGenerator(){
            return new CustomKeyGenerator();
        }

    }


}