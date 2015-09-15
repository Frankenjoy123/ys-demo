package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.cache.annotation.ElastiCacheConfig;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.Page;
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
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
@Component
@ElastiCacheConfig
public class ProductBaseDomain {

//    private static final String DETAILS_FILE_NAME = "details.json";
//
//    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private RestClient dataAPIClient;

    @Cacheable(key="T(com.yunsoo.api.rabbit.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCTBASE.toString(),#productBaseId )")
    public ProductBaseObject getProductBaseById(String productBaseId) {
        try {
            return dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

//    public ProductBaseDetails getProductBaseDetails(String orgId, String productBaseId, Integer version) {
//        ResourceInputStream resourceInputStream;
//        try {
//            resourceInputStream = dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{fileName}",
//                    orgId, productBaseId, version, DETAILS_FILE_NAME);
//        } catch (NotFoundException ex) {
//            return null;
//        }
//        try {
//            return mapper.readValue(resourceInputStream, ProductBaseDetails.class);
//        } catch (IOException e) {
//            return null;
//        }
//    }

    public Page<ProductBaseObject> getProductBaseByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("productbase" + query, new ParameterizedTypeReference<List<ProductBaseObject>>() {
        });
    }

}
