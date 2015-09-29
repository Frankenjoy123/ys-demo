package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ElastiCacheConfig;
import com.yunsoo.api.dto.User;
import com.yunsoo.common.data.object.UserObject;
import com.yunsoo.common.data.object.UserReportObject;
import com.yunsoo.common.util.IdGenerator;
import com.yunsoo.common.util.ImageProcessor;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.InternalServerErrorException;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by yan on 9/28/2015.
 */
@Component
@ElastiCacheConfig
public class UserReportDomain {
    @Autowired
    private RestClient dataAPIClient;

    public Page<UserReportObject> getUserReportsByProductBaseId(String productBaseId, Pageable pageable){
        QueryStringBuilder builder = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("product_base_id", productBaseId);

        Page<UserReportObject> objectPage = dataAPIClient.getPaged("userReport" + builder.toString(), new ParameterizedTypeReference<List<UserReportObject>>() {
        });

        return objectPage;
    }

    public UserReportObject getReportById(String id){
        return dataAPIClient.get("userReport/{id}", UserReportObject.class, id);
    }

    public List<String> getReportImageNames(String userId, String reportId){
        try {
            return dataAPIClient.get("file/s3/list?path=user/{uerId}/report/{reportId}", new ParameterizedTypeReference<List<String>>() {
            }, userId, reportId);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public ResourceInputStream getReportImage(String userId, String reportId, String imageName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=user/{userId}/report/{reportId}/{imageName}", userId, reportId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    @Cacheable(key="T(com.yunsoo.api.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).USER.toString(), #id )")
    public UserObject getUserById(String id){
        return dataAPIClient.get("user/{id}", UserObject.class, id);
    }

}
