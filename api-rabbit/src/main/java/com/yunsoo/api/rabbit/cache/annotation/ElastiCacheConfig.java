package com.yunsoo.api.rabbit.cache.annotation;

import com.yunsoo.api.rabbit.config.CacheConfiguration;
import org.springframework.cache.annotation.CacheConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by:   yan
 * Created on:   7/30/2015
 * Descriptions:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@CacheConfig(keyGenerator = "keyGenerator", cacheNames = {CacheConfiguration.CACHE_NAME})
public @interface ElastiCacheConfig {
}
