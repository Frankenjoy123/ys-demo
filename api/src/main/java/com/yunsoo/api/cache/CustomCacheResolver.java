package com.yunsoo.api.cache;

import com.yunsoo.api.AppContext;
import com.yunsoo.api.config.CacheConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by yan on 7/30/2015.
 */
public class CustomCacheResolver extends AbstractCacheResolver {

    public CustomCacheResolver(){}

    public CustomCacheResolver(CacheManager manager){
        super(manager);
    }

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        return Arrays.asList(CacheConfiguration.cacheName);
    }
}
