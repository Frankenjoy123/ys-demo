package com.yunsoo.api.cache.annotation;

import com.yunsoo.api.config.CacheConfiguration;
import org.springframework.cache.annotation.CacheConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yan on 7/30/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@CacheConfig(keyGenerator = "keyGenerator", cacheNames = {CacheConfiguration.cacheName})
public @interface ElastiCacheConfig {
}
