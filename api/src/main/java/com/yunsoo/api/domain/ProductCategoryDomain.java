package com.yunsoo.api.domain;

import com.yunsoo.api.client.DataAPIClient;
import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Component
public class ProductCategoryDomain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryDomain.class);

    @Autowired
    private DataAPIClient dataAPIClient;

    @Cacheable(value = "productcategory", key = "'list'")
    public List<ProductCategoryObject> getProductCategories() {
        LOGGER.debug("cache missed [name: productcategory, key: 'list']");
        return dataAPIClient.get("productcategory", new ParameterizedTypeReference<List<ProductCategoryObject>>() {
        });
    }

    @Cacheable(value = "productcategory", key = "'map'")
    public Map<Integer, ProductCategoryObject> getProductCategoryMap() {
        LOGGER.debug("cache missed [name: productcategory, key: 'map']");
        return getProductCategories().stream().collect(Collectors.toMap(ProductCategoryObject::getId, c -> c));
    }

    public ProductCategoryObject getById(Integer id) {
        if (id == null) {
            return null;
        }
        try {
            return dataAPIClient.get("productcategory/{id}", ProductCategoryObject.class, id);
        } catch (NotFoundException ex) {
            return null;
        }
    }

    public ProductCategoryObject getById(Integer id, Map<Integer, ProductCategoryObject> map) {
        if (map != null && map.containsKey(id)) {
            return map.get(id);
        } else {
            return null;
        }
    }

}
