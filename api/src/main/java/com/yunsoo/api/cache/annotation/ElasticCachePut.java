package com.yunsoo.api.cache.annotation;

import org.springframework.cache.annotation.CachePut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by yan on 7/29/2015.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@CachePut(value = "dev-cache", keyGenerator = "keyGenerator")
public @interface ElasticCachePut {
}
