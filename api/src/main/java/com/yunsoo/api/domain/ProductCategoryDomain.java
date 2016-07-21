package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.client.DataApiClient1;
import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by  : Lijian
 * Created on  : 2015/7/1
 * Descriptions:
 */
@ObjectCacheConfig
@Component
public class ProductCategoryDomain {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private DataApiClient1 dataApiClient;


    @Cacheable(key="T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCT_CATEGORY.toString(), 'list')")
    public List<ProductCategoryObject> getProductCategories() {
        log.debug("cache missed [name: productcategory, key: 'list']");
        return dataApiClient.get("productcategory", new ParameterizedTypeReference<List<ProductCategoryObject>>() {
        });
    }

    @Cacheable(key="T(com.yunsoo.api.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCT_CATEGORY.toString(), 'map')")
    public Map<String, ProductCategoryObject> getProductCategoryMap() {
        log.debug("cache missed [name: productcategory, key: 'map']");
        List<ProductCategoryObject> categoryObjects = dataApiClient.get("productcategory", new ParameterizedTypeReference<List<ProductCategoryObject>>() {
        });
        return categoryObjects.stream().collect(Collectors.toMap(ProductCategoryObject::getId, c -> c));
    }

    public ProductCategoryObject getById(String id) {
        if (id == null) {
            return null;
        }
        try {
            return dataApiClient.get("productcategory/{id}", ProductCategoryObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public ProductCategoryObject getById(String id, Map<String, ProductCategoryObject> map) {
        if (map != null && map.containsKey(id)) {
            return map.get(id);
        } else {
            return null;
        }
    }

}
