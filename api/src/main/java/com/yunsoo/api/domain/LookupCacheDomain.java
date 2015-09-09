package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ElastiCacheConfig;
import com.yunsoo.api.dto.Lookup;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yan on 9/8/2015.
 */
@ElastiCacheConfig
@Component
public class LookupCacheDomain {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupCacheDomain.class);

    @Autowired
    private RestClient dataAPIClient;

    @Cacheable
    public List<LookupObject> getAllLookupList(){
        LOGGER.debug("cache not hit for getAllLookupList");
        return dataAPIClient.get("lookup", new ParameterizedTypeReference<List<LookupObject>>() {
        });
    }

    @CacheEvict(key="T(com.yunsoo.api.cache.CustomKeyGenerator).getKey(#root.targetClass.getName(), 'getAllLookupList', null)")
    public void saveLookup(LookupObject lookup){
       dataAPIClient.put("lookup",lookup, LookupObject.class);
    }

    private String formatActive(Boolean active) {
        return new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
    }
}
