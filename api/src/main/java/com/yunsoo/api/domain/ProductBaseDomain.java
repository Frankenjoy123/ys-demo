package com.yunsoo.api.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductBaseVersionsObject;
import com.yunsoo.common.web.client.Page;
import com.yunsoo.common.web.client.ResourceInputStream;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

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

    private static final String DETAILS_FILE_NAME = "details.json";
    private static final String IMAGE_NAME_800X400 = "image-800x400";
    private static final String IMAGE_NAME_400X200 = "image-400x200";
    private static final String IMAGE_NAME_400X400 = "image-400x400";

    private static ObjectMapper mapper = new ObjectMapper();

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private RestClient dataAPIClient;

    @Cacheable(key="T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCTBASE.toString(), #productBaseId )")
    public ProductBaseObject getProductBaseById(String productBaseId) {
        if ((productBaseId == null) || (productBaseId.equals(""))) {
            return null;
        }
        try {
            return dataAPIClient.get("productbase/{id}", ProductBaseObject.class, productBaseId);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public List<ProductBaseVersionsObject> getProductBaseVersionsByProductBaseId(String productBaseId) {
        return dataAPIClient.get("productbaseversions/{product_base_Id}", new ParameterizedTypeReference<List<ProductBaseVersionsObject>>() {
        }, productBaseId);
    }

    public ProductBaseVersionsObject getLatestProductBaseVersionsByProductBaseId(String productBaseId) {
        List<ProductBaseVersionsObject> productBaseVersionsObjects = dataAPIClient.get("productbaseversions/{product_base_Id}", new ParameterizedTypeReference<List<ProductBaseVersionsObject>>() {
        }, productBaseId);
        if (productBaseVersionsObjects.size() == 0) {
            return null;
        }
        return productBaseVersionsObjects.get(productBaseVersionsObjects.size() - 1);
    }


    public Map<String, List<ProductBaseVersionsObject>> getProductBaseVersionsByProductBaseIds(List<String> productBaseIds) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("product_base_ids", productBaseIds)
                .build();
        return dataAPIClient.get("productbaseversions" + query, new ParameterizedTypeReference<Map<String, List<ProductBaseVersionsObject>>>() {
        });
    }

    public Page<ProductBaseObject> getProductBaseByOrgId(String orgId, Pageable pageable) {
        String query = new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK)
                .append("org_id", orgId)
                .append(pageable)
                .build();

        return dataAPIClient.getPaged("productbase" + query, new ParameterizedTypeReference<List<ProductBaseObject>>() {
        });
    }

    public ProductBaseVersionsObject getProductBaseVersionsByProductBaseIdAndVersion(String productBaseId, Integer version) {
        try {
            return dataAPIClient.get("productbaseversions/{product_base_Id}/{version}", ProductBaseVersionsObject.class, productBaseId, version);
        } catch (NotFoundException ignored) {
            return null;
        }
    }

    public ProductBaseObject createProductBase(ProductBaseObject productBaseObject) {
        return dataAPIClient.post("productbase", productBaseObject, ProductBaseObject.class);
    }

    public ProductBaseVersionsObject createProductBaseVersions(ProductBaseVersionsObject productBaseVersionsObject) {
        return dataAPIClient.post("productbaseversions/{product_base_id}", productBaseVersionsObject, ProductBaseVersionsObject.class, productBaseVersionsObject.getProductBaseId());
    }

    @CacheEvict(key="T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCTBASE.toString(), #productBaseObject.getId())")
    public void updateProductBase(ProductBaseObject productBaseObject) {
        dataAPIClient.put("productbase/{id}", productBaseObject, productBaseObject.getId());
    }

    public void updateProductBaseVersions(ProductBaseVersionsObject productBaseVersionsObject) {
        dataAPIClient.put("productbaseversions/{product_base_id}/{version}", productBaseVersionsObject, productBaseVersionsObject.getProductBaseId(), productBaseVersionsObject.getVersion());
    }

    @CacheEvict(key="T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCTBASE.toString(), #productBaseId)")
    public void deleteProductBase(String productBaseId) {
        dataAPIClient.delete("productbase/{product_base_id}", productBaseId);
    }

    public void deleteProductBaseVersions(String productBaseId, Integer version) {
        dataAPIClient.delete("productbaseversions/{product_base_id}/{version}", productBaseId, version);
    }

    public ResourceInputStream getProductBaseImage(String orgId, String productBaseId, Integer version, String imageName) {
        try {
            return dataAPIClient.getResourceInputStream("file/s3?path=organization/{orgId}/product_base/{productBaseId}/{version}/{imageName}", orgId, productBaseId, version, imageName);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public ProductBaseObject copyFromProductBaseVersionsObject(ProductBaseVersionsObject productBaseVersionsObject) {
        if (productBaseVersionsObject == null) {
            return null;
        }
        ProductBaseObject productBaseObject = new ProductBaseObject();
        productBaseObject.setId(productBaseVersionsObject.getProductBaseId());
        productBaseObject.setVersion(productBaseVersionsObject.getVersion());
        productBaseObject.setStatusCode(productBaseVersionsObject.getStatusCode());
        productBaseObject.setOrgId(productBaseVersionsObject.getProductBase().getOrgId());
        productBaseObject.setCategoryId(productBaseVersionsObject.getProductBase().getCategoryId());
        productBaseObject.setName(productBaseVersionsObject.getProductBase().getName());
        productBaseObject.setDescription(productBaseVersionsObject.getProductBase().getDescription());
        productBaseObject.setBarcode(productBaseVersionsObject.getProductBase().getBarcode());
        productBaseObject.setProductKeyTypeCodes(productBaseVersionsObject.getProductBase().getProductKeyTypeCodes());
        productBaseObject.setShelfLife(productBaseVersionsObject.getProductBase().getShelfLife());
        productBaseObject.setShelfLifeInterval(productBaseVersionsObject.getProductBase().getShelfLifeInterval());
        productBaseObject.setChildProductCount(productBaseVersionsObject.getProductBase().getChildProductCount());
        productBaseObject.setComments(productBaseVersionsObject.getProductBase().getComments());
        productBaseObject.setCreatedAccountId(productBaseVersionsObject.getCreatedAccountId());
        productBaseObject.setCreatedDateTime(productBaseVersionsObject.getCreatedDateTime());
        productBaseObject.setModifiedAccountId(productBaseVersionsObject.getModifiedAccountId());
        productBaseObject.setModifiedDateTime(productBaseVersionsObject.getModifiedDateTime());
        return productBaseObject;
    }

}
