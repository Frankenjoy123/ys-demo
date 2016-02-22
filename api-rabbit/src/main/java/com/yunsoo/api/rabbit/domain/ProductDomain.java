package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.cache.annotation.ElastiCacheConfig;
import com.yunsoo.api.rabbit.dto.Product;
import com.yunsoo.api.rabbit.dto.ProductCategory;
import com.yunsoo.common.data.object.ProductBaseObject;
import com.yunsoo.common.data.object.ProductObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by:   Lijian
 * Created on:   2015/3/20
 * Descriptions:
 */
@Component
@ElastiCacheConfig
public class ProductDomain {

    @Autowired
    private RestClient dataAPIClient;

    @Autowired
    ProductBaseDomain productBaseDomain;

    private Log log = LogFactory.getLog(this.getClass());

    //@Cacheable(key = "T(com.yunsoo.api.rabbit.cache.CustomKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCT.toString(),#key)")
    public ProductObject getProduct(String key) {
        try {
            return dataAPIClient.get("product/{key}", ProductObject.class, key);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    //Retrieve Product key, ProductBase entry and Product-Category entry from Backend.
    public Product getProductByKey(String key) {
        Product product = new Product();
        product.setProductKey(key);

        ProductObject productObject;
        try {
            productObject = dataAPIClient.get("product/{key}", ProductObject.class, key);
        } catch (NotFoundException ex) {
            return null;
        }

        product.setStatusCode(productObject.getProductStatusCode());
        product.setManufacturingDateTime(productObject.getManufacturingDateTime());
        product.setCreatedDateTime(productObject.getCreatedDateTime());

        //fill with ProductBase information.
        String productBaseId = productObject.getProductBaseId();
        ProductBaseObject productBaseObject = productBaseDomain.getProductBaseById(productBaseId);
        if (productBaseObject != null) {
            product.setProductBaseId(productBaseId);
            product.setBarcode(productBaseObject.getBarcode());
            product.setComment(productBaseObject.getComments());
            product.setName(productBaseObject.getName());
            product.setOrgId(productBaseObject.getOrgId());

            //fill with ProductCategory information.
            ProductCategory productCategory = getProductCategoryById(productBaseObject.getCategoryId());
            product.setProductCategory(productCategory);
        }
        return product;
    }

    public ProductCategory getProductCategoryById(String id) {
        if (id == null) {
            return null;
        }
        return dataAPIClient.get("productcategory/{id}", ProductCategory.class, id);
    }


    public Long getCommentsScore(String productBaseId) {
        return dataAPIClient.get("productcomments/avgscore/{id}", Long.class, productBaseId);
    }


}
