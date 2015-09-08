package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ElastiCacheConfig;
import com.yunsoo.common.data.object.LookupObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * Created by yan on 9/8/2015.
 */
@ElastiCacheConfig
public class LookupCacheDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Cacheable
    public List<LookupObject> getAllLookupList(Boolean active){
        return dataAPIClient.get("lookup" + formatActive(active), new ParameterizedTypeReference<List<LookupObject>>() {
        });
    }

    private String formatActive(Boolean active) {
        return new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
    }
}
