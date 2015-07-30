package com.yunsoo.api.domain;

import com.yunsoo.api.cache.annotation.ElastiCacheConfig;
import com.yunsoo.api.dto.ProductKeyBatchStatus;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.api.dto.ProductStatus;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
@ElastiCacheConfig
@Component
public class LookupDomain {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupDomain.class);


    @Autowired
    private RestClient dataAPIClient;

    @Cacheable
    public List<ProductKeyType> getProductKeyTypes() {
       // LOGGER.debug("cache missed [name: lookup.productkeytype]");
        //could not invoke the cachable method to avoid cache proxy not work
        //getProductKeyTypes(null);
        return dataAPIClient.get("productkeytype" + formatActive(null), new ParameterizedTypeReference<List<ProductKeyType>>() {
        });
    }

    @Cacheable
    public List<ProductKeyType> getProductKeyTypes(Boolean active) {
      //  LOGGER.debug("cache missed [name: lookup.productkeytype.active]");
        return dataAPIClient.get("productkeytype" + formatActive(active), new ParameterizedTypeReference<List<ProductKeyType>>() {
        });
    }


    @Cacheable
    public List<ProductKeyBatchStatus> getProductKeyBatchStatuses() {
        return dataAPIClient.get("productkeybatchstatus" + formatActive(null), new ParameterizedTypeReference<List<ProductKeyBatchStatus>>() {
        });
    }

    @Cacheable
    public List<ProductKeyBatchStatus> getProductKeyBatchStatuses(Boolean active) {
    //    LOGGER.debug("cache missed [name: lookup.productkeybatchstatus]");
        return dataAPIClient.get("productkeybatchstatus" + formatActive(active), new ParameterizedTypeReference<List<ProductKeyBatchStatus>>() {
        });
    }


    @Cacheable
    public List<ProductStatus> getProductStatuses() {
        return dataAPIClient.get("productstatus" + formatActive(null), new ParameterizedTypeReference<List<ProductStatus>>() {
        });
    }

    @Cacheable
    public List<ProductStatus> getProductStatuses(Boolean active) {
    //    LOGGER.debug("cache missed [name: lookup.productstatus]");
        return dataAPIClient.get("productstatus" + formatActive(active), new ParameterizedTypeReference<List<ProductStatus>>() {
        });
    }


    private String formatActive(Boolean active) {
        return new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
    }
}
