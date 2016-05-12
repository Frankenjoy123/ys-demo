package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.rabbit.dto.ProductCategory;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
@Component
@ObjectCacheConfig
public class ProductBaseDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    private LookupDomain lookupDomain;

    private Log log = LogFactory.getLog(this.getClass());

    @Cacheable(key = "T(com.yunsoo.api.rabbit.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCTBASE.toString(),#productBaseId )")
    public ProductBaseObject getProductBaseById(String productBaseId) {
        if (productBaseId == null) {
            return null;
        }
        try {
            return dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public String getProductBaseDetails(String orgId, String productBaseId, int version) {
        try {
            ResourceInputStream resourceInputStream = dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/details.json",
                    orgId, productBaseId, version);
            byte[] bytes = StreamUtils.copyToByteArray(resourceInputStream);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (NotFoundException | IOException ignored) {
            return null;
        }
    }

    public Page<ProductBaseObject> getProductBaseByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("productbase" + query, new ParameterizedTypeReference<List<ProductBaseObject>>() {
        });
    }

    // get product images
    public ResourceInputStream getProductBaseImage(String productBaseId, String imageName) {
        try {
            ProductBaseObject productBaseObject = getProductBaseById(productBaseId);
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{imageName}", productBaseObject.getOrgId(), productBaseId, productBaseObject.getVersion(), imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public ProductCategory getProductCategoryById(String id) {
        if (id == null) {
            return null;
        }
        Map<String, ProductCategoryObject> map = lookupDomain.getProductCategoryMap();
        if (map.containsKey(id)) {
            return new ProductCategory(map.get(id));
        }
        return null;
    }

    public String getProductBaseImageUrl(String imageName) {
        return StringUtils.hasText(imageName) ? String.format("/image/%s", imageName) : null;
    }

}
