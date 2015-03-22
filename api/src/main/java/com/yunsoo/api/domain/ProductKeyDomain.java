package com.yunsoo.api.domain;

import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyType;
import com.yunsoo.common.data.object.LookupBase;
import com.yunsoo.common.data.object.ProductKeyBatchObject;
import com.yunsoo.common.data.object.ProductKeyBatchRequestObject;
import com.yunsoo.common.data.object.ProductKeyTypeObject;
import com.yunsoo.common.web.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/16
 * Descriptions:
 */
@Component("productKeyDomain")
public class ProductKeyDomain {

    @Autowired
    private RestClient dataAPIClient;

    //ProductKeyType

    public List<ProductKeyType> getAllProductKeyTypes(Boolean active) {

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

    public List<Integer> changeProductKeyTypeCodeToId(List<String> productKeyTypeCodeList) {
        return LookupBase.changeCodeToId(getAllProductKeyTypes(true), productKeyTypeCodeList);
    }


    //ProductKeyBatch

    public List<ProductKeyBatch> getAllProductKeyBatchByOrgId(Integer organizationId, Long productBaseId) {
        ProductKeyBatchObject[] objects =
                dataAPIClient.get("productkeybatch?organizationId={orgid}&productBaseId={pbid}",
                        ProductKeyBatchObject[].class,
                        organizationId,
                        productBaseId);
        if (objects == null) {
            return null;
        } else {
            return Arrays.stream(objects).map(this::convertFromProductKeyBatchObject).collect(Collectors.toList());
        }
    }

    public ProductKeyBatch getProductKeyBatchById(Long id) {
        return convertFromProductKeyBatchObject(dataAPIClient.get("productkeybatch/{id}", ProductKeyBatchObject.class, id));
    }

    public ProductKeyBatch createProductKeyBatch(ProductKeyBatchRequestObject requestObject) {
        ProductKeyBatchObject newBatchObj = dataAPIClient.post(
                "productkeybatch",
                requestObject,
                ProductKeyBatchObject.class);

        return convertFromProductKeyBatchObject(newBatchObj);
    }

    private ProductKeyBatch convertFromProductKeyBatchObject(ProductKeyBatchObject object) {
        if (object == null) {
            return null;
        }
        ProductKeyBatch batch = new ProductKeyBatch();
        batch.setId(object.getId());
        batch.setQuantity(object.getQuantity());
        batch.setStatusId(object.getStatusId());
        batch.setOrganizationId(object.getOrganizationId());
        batch.setCreatedClientId(object.getCreatedClientId());
        batch.setCreatedAccountId(object.getCreatedAccountId());
        batch.setCreatedDateTime(object.getCreatedDateTime());
        batch.setProductKeyTypeIds(object.getProductKeyTypeIds());
        return batch;
    }

}
