package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductKeyBatchStatus;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.api.dto.ProductStatus;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.util.QueryStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RestClient dataAPIClient;


    //ProductKeyType
    public List<ProductKeyType> getProductKeyTypes() {
        return getProductKeyTypes(null);
    }

    public List<ProductKeyType> getProductKeyTypes(Boolean active) {
        return dataAPIClient.get("productkeytype" + formatActive(active), new ParameterizedTypeReference<List<ProductKeyType>>() {
        });
    }


    //ProductKeyBatchStatus
    public List<ProductKeyBatchStatus> getProductKeyBatchStatuses() {
        return getProductKeyBatchStatuses(null);
    }

    public List<ProductKeyBatchStatus> getProductKeyBatchStatuses(Boolean active) {
        return dataAPIClient.get("productkeybatchstatus" + formatActive(active), new ParameterizedTypeReference<List<ProductKeyBatchStatus>>() {
        });
    }


    //ProductStatus
    public List<ProductStatus> getProductStatuses() {
        return getProductStatuses(null);
    }

    public List<ProductStatus> getProductStatuses(Boolean active) {
        return dataAPIClient.get("productstatus" + formatActive(active), new ParameterizedTypeReference<List<ProductStatus>>() {
        });
    }


    private String formatActive(Boolean active) {
        return new QueryStringBuilder(QueryStringBuilder.Prefix.QUESTION_MARK).append("active", active).build();
    }
}
