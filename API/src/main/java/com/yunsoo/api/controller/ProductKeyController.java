package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductKey;
import com.yunsoo.api.dto.ProductKeyBatch;
import com.yunsoo.api.dto.ProductKeyBatchRequest;
import com.yunsoo.common.data.object.ProductKeyObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   Lijian
 * Created on:   2015/3/9
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productkey")
public class ProductKeyController {

    private RestClient dataAPIClient;

    @Autowired
    ProductKeyController(RestClient dataAPIClient) {
        this.dataAPIClient = dataAPIClient;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET)
    public ProductKey get(@PathVariable(value = "key") String key) {
        if (StringUtils.isEmpty(key)) {
            throw new BadRequestException("please provide a valid product key");
        }
        ProductKeyObject tProductKey = dataAPIClient.get("productkey/{key}", ProductKeyObject.class, key);
        if (tProductKey == null) {
            throw new NotFoundException("product key");
        }
        ProductKey productKey = new ProductKey();
        productKey.setProductKey(tProductKey.getProductKey());
        productKey.setProductKeyTypeId(tProductKey.getProductKeyTypeId());
        productKey.setProductKeyDisabled(tProductKey.isProductKeyDisabled());
        productKey.setPrimary(tProductKey.isPrimary());
        productKey.setProductKeyBatchId(tProductKey.getProductKeyBatchId());
        productKey.setPrimaryProductKey(tProductKey.getPrimaryProductKey());
        productKey.setProductKeySet(tProductKey.getProductKeySet());
        productKey.setCreatedDateTime(tProductKey.getCreatedDateTime());
        return productKey;
    }

    @RequestMapping(value = "{key}/disable", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key) {
        if (StringUtils.isEmpty(key)) {
            throw new BadRequestException("please provide a valid product key");
        }
        dataAPIClient.put("productkey/{key}/disable", null, key);
    }


    //batch

    @RequestMapping(value = "batch/{id}", method = RequestMethod.GET)
    public ProductKeyBatch getBatchById(@PathVariable(value = "id") String idStr) {
        int idInt;
        try {
            idInt = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            throw new BadRequestException("invalid id");
        }
//        //ProductKeyBatch batch =
//        if (batch == null) {
//            throw new NotFoundException("product batch");
//        }
//        ProductKeyBatchDto batchDto = new ProductKeyBatchDto();
//        batchDto.setId(batch.getId());
//        batchDto.setQuantity(batch.getQuantity());
//        batchDto.setStatusId(batch.getStatusId());
//        batchDto.setOrganizationId(batch.getOrganizationId());
//        batchDto.setCreatedClientId(batch.getCreatedClientId());
//        batchDto.setCreatedAccountId(batch.getCreatedAccountId());
//        batchDto.setCreatedDateTime(batch.getCreatedDateTime());
//        batchDto.setProductKeyTypeIds(batch.getProductKeyTypeIds());
//        batchDto.setProductKeysAddress(batch.getProductKeysAddress());
//        return batchDto;

        return null;
    }


    @RequestMapping(value = "batch/create", method = RequestMethod.POST)
    public ProductKeyBatch batchCreateProductKeys(@RequestBody ProductKeyBatchRequest request) {

        return null;
    }

}
