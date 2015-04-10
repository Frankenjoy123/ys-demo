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

    public List<ProductKeyType> getAllProductKeyTypes(Boolean active) {
        ProductKeyType[] objects = dataAPIClient.get("productkeytype?active={active}", ProductKeyType[].class, active);
        return Arrays.asList(objects);
    }

    //ProductKeyBatchStatus
    public List<ProductKeyBatchStatus> getAllProductKeyBatchStatuses(Boolean active) {
        ProductKeyBatchStatus[] objects = dataAPIClient.get("productstatus?active={active}", ProductKeyBatchStatus[].class, active);
        return Arrays.asList(objects);
    }

    //ProductStatus
    public List<ProductStatus> getAllProductStatuses(Boolean active) {
        ProductStatus[] objects = dataAPIClient.get("productstatus?active={active}", ProductStatus[].class, active);
        return Arrays.asList(objects);
    }
}
