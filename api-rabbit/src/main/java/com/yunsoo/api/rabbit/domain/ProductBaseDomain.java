package com.yunsoo.api.rabbit.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.rabbit.cache.annotation.ElastiCacheConfig;
import com.yunsoo.api.rabbit.dto.Organization;
import com.yunsoo.api.rabbit.dto.ProductBase;
import com.yunsoo.api.rabbit.dto.ProductBaseDetails;
import com.yunsoo.api.rabbit.dto.ProductCategory;
import com.yunsoo.common.data.object.OrganizationObject;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private OrganizationDomain organizationDomain;


    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDomain.class);

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

    // get product images
    public ResourceInputStream getProductBaseImage(String productBaseId, String imageName) {
        try {
            ProductBaseObject productBaseObject = getProductBaseById(productBaseId);
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{imageName}", productBaseObject.getOrgId(), productBaseId, productBaseObject.getVersion(), imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    @Cacheable(key = "T(com.yunsoo.api.rabbit.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCTBASE.toString(),#productBaseId, 'details' )")
    public ProductBaseDetails getProductBaseDetailsById(String productBaseId) {
        try {
            ProductBaseObject productBaseObject = getProductBaseById(productBaseId);
            ResourceInputStream stream = dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/details.json", productBaseObject.getOrgId(), productBaseId, productBaseObject.getVersion());
            ProductBaseDetails details = new ObjectMapper().readValue(stream, ProductBaseDetails.class);
            return details;
        } catch (Exception ex) {
            LOGGER.error("get detail data from s3 failed.", ex);
            return null;
        }
    }

    public ProductCategory getProductCategoryById(String id) {
        if (id == null) {
            return null;
        }
        return dataAPIClient.get("productcategory/{id}", ProductCategory.class, id);
    }





}
