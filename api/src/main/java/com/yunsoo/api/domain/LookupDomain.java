package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductKeyBatchStatus;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.api.dto.ProductStatus;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
    public List<ProductKeyType> getAllProductKeyTypes() {
        return getAllProductKeyTypes(null);
    }

    public List<ProductKeyType> getAllProductKeyTypes(Boolean active) {
        ProductKeyType[] objects = dataAPIClient.get("productkeytype" + formatActive(active), ProductKeyType[].class);
        return Arrays.asList(objects);
    }


    //ProductKeyBatchStatus
    public List<ProductKeyBatchStatus> getAllProductKeyBatchStatuses() {
        return getAllProductKeyBatchStatuses(null);
    }

    public List<ProductKeyBatchStatus> getAllProductKeyBatchStatuses(Boolean active) {
        ProductKeyBatchStatus[] objects = dataAPIClient.get("productkeybatchstatus" + formatActive(active), ProductKeyBatchStatus[].class);
        return Arrays.asList(objects);
    }


    //ProductStatus
    public List<ProductStatus> getAllProductStatuses() {
        return getAllProductStatuses(null);
    }

    public List<ProductStatus> getAllProductStatuses(Boolean active) {
        ProductStatus[] objects = dataAPIClient.get("productstatus" + formatActive(active), ProductStatus[].class);
        return Arrays.asList(objects);
    }

    private String formatActive(Boolean active) {
        return active != null && active ? "?active=true" : "";
    }
}
