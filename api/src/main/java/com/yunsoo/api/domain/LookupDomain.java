package com.yunsoo.api.domain;

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
@Component
public class LookupDomain {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupDomain.class);

    @Autowired
    private RestClient dataAPIClient;


    //ProductKeyType

    @Cacheable(value = "api-domain", key = "'lookup.productkeytype.all'")
    public List<ProductKeyType> getProductKeyTypes() {
        return getProductKeyTypes(null);
    }

    @Cacheable(value = "api-domain", key = "'lookup.productkeytype.all'")
    public List<ProductKeyType> getProductKeyTypes(Boolean active) {
        LOGGER.debug("cache missed [name: lookup.productkeytype]");
        return dataAPIClient.get("productkeytype" + formatActive(active), new ParameterizedTypeReference<List<ProductKeyType>>() {
        });
    }


    //ProductKeyBatchStatus

    @Cacheable("lookup.productkeybatchstatus")
    public List<ProductKeyBatchStatus> getProductKeyBatchStatuses() {
        return getProductKeyBatchStatuses(null);
    }

    @Cacheable("lookup.productkeybatchstatus")
    public List<ProductKeyBatchStatus> getProductKeyBatchStatuses(Boolean active) {
        LOGGER.debug("cache missed [name: lookup.productkeybatchstatus]");
        return dataAPIClient.get("productkeybatchstatus" + formatActive(active), new ParameterizedTypeReference<List<ProductKeyBatchStatus>>() {
        });
    }


    //ProductStatus

    @Cacheable("lookup.productstatus")
    public List<ProductStatus> getProductStatuses() {
        return getProductStatuses(null);
    }

    @Cacheable("lookup.productstatus")
    public List<ProductStatus> getProductStatuses(Boolean active) {
        LOGGER.debug("cache missed [name: lookup.productstatus]");
        return dataAPIClient.get("productstatus" + formatActive(active), new ParameterizedTypeReference<List<ProductStatus>>() {
        });
    }


    private String formatActive(Boolean active) {
        return new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
    }
}
