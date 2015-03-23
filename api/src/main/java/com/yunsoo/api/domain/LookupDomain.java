package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.common.data.object.ProductKeyTypeObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/23
 * Descriptions:
 */
@Component
public class LookupDomain {

    @Autowired
    private RestClient dataAPIClient;


    public List<ProductKeyType> getProductKeyTypes(Boolean active) {

        ProductKeyTypeObject[] objects = dataAPIClient.get("productkeytype?active={active}", ProductKeyTypeObject[].class, active);

        return Arrays.stream(objects).map(p -> {
            ProductKeyType t = new ProductKeyType();
            t.setId(p.getId());
            t.setCode(p.getCode());
            t.setName(p.getName());
            t.setDescription(p.getDescription());
            t.setActive(p.isActive());
            return t;
        }).collect(Collectors.toList());
    }

    //public List<ProductKeyBatchStatus>


}
