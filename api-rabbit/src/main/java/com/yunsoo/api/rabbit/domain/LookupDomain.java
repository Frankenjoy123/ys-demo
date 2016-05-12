package com.yunsoo.api.rabbit.domain;

import com.yunsoo.api.rabbit.cache.annotation.ObjectCacheConfig;
import com.yunsoo.api.rabbit.dto.Lookup;
import com.yunsoo.common.data.LookupCodes;
import com.yunsoo.common.data.object.ProductCategoryObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
@Component
@ObjectCacheConfig
public class LookupDomain {

    @Autowired
    private RestClient dataAPIClient;

    public List<Lookup> getAllLookupList(Boolean active) {
        return dataAPIClient.get("lookup" + formatActive(active), new ParameterizedTypeReference<List<Lookup>>() {
        });
    }

    private String formatActive(Boolean active) {
        return new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
    }


    public List<Lookup> getLookupListByType(LookupCodes.LookupType type, Boolean active) {
        List<Lookup> allList = getAllLookupList(active);
        List<Lookup> returnList = new ArrayList<>();
        allList.forEach(item -> {
            if (LookupCodes.LookupType.valueOf(item.getTypeCode()).equals(type))
                returnList.add(item);
        });

        return returnList;
    }

    public List<Lookup> getLookupListByType(LookupCodes.LookupType type) {
        return getLookupListByType(type, null);
    }


    @Cacheable(key = "T(com.yunsoo.api.rabbit.cache.ObjectKeyGenerator).generate(T(com.yunsoo.common.data.CacheType).PRODUCT_CATEGORY.toString(), 'map')")
    public Map<String, ProductCategoryObject> getProductCategoryMap() {
        List<ProductCategoryObject> categoryObjects = dataAPIClient.get("productcategory", new ParameterizedTypeReference<List<ProductCategoryObject>>() {
        });
        return categoryObjects.stream().collect(Collectors.toMap(ProductCategoryObject::getId, c -> c));
    }

}
