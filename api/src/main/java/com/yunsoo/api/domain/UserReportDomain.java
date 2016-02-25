package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.DefaultCacheConfig;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.data.object.UserReportObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yan on 9/28/2015.
 */
@Component
@DefaultCacheConfig
public class UserReportDomain {
    @Autowired
    private RestClient dataAPIClient;

    public Page<UserReportObject> getUserReportsByProductBaseId(List<String> productBaseIds, Pageable pageable) {
        QueryStringBuilder builder = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("product_base_id_in", productBaseIds).append(pageable);

        Page<UserReportObject> objectPage = dataAPIClient.getPaged("userReport" + builder.toString(), new ParameterizedTypeReference<List<UserReportObject>>() {
        });

        return objectPage;
    }

    public UserReportObject getReportById(String id) {
        return dataAPIClient.get("userReport/{id}", UserReportObject.class, id);
    }

    public List<String> getReportImageNames(String userId, String reportId) {
        return dataAPIClient.get("file/s3/list?path=user/{uerId}/report/{reportId}", new ParameterizedTypeReference<List<String>>() {
        }, userId, reportId);
    }

    public ResourceInputStream getReportImage(String userId, String reportId, String imageName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=user/{userId}/report/{reportId}/{imageName}", userId, reportId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    @Cacheable(key = "T(com.yunsoo.api.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).USER.toString(), #id )")
    public UserObject getUserById(String id) {
        try {
            return dataAPIClient.get("user/{id}", UserObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

}
