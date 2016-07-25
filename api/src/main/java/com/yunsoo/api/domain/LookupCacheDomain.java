package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by:   yan
 * Created on:   9/8/2015
 * Descriptions:
 */
@ObjectCacheConfig
@Component
public class LookupCacheDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataApiClient;

    @Cacheable(key="T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).LOOKUP.toString(), 'all')")
    public List<LookupObject> getAllLookupList(){
        log.debug("cache not hit for getAllLookupList");
        return dataApiClient.get("lookup", new ParameterizedTypeReference<List<LookupObject>>() {
        });
    }

    @CacheEvict(key="T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).LOOKUP.toString(), 'all')")
    public void saveLookup(LookupObject lookup){
        dataApiClient.put("lookup", lookup, LookupObject.class);
    }

    private String formatActive(Boolean active) {
        return new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
    }
}
