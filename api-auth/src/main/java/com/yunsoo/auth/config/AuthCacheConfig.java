package com.yunsoo.auth.config;

import org.springframework.cache.annotation.CacheConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by:   Lijian
 * Created on:   2016-07-26
 * Descriptions:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@CacheConfig(cacheNames = {"authCache"})
public @interface AuthCacheConfig {
}
