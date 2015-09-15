package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.cache.annotation.ElastiCacheConfig;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by  : Lijian
 * Created on  : 2015/5/12
 * Descriptions:
 */
@Component
@ElastiCacheConfig
public class OrganizationDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Cacheable(key="T(com.yunsoo.api.rabbit.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).ORGANIZATION.toString(),#id )")
    public OrganizationObject getById(String id) {
        try {
            return dataAPIClient.get("organization/{id}", OrganizationObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public ResourceInputStream getLogoImage(String orgId, String imageName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/logo/{imageName}", orgId, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }
}
