package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductKey;
import com.yunsoo.common.data.object.*;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   Lijian
 * Created on:   2015/3/9
 * Descriptions:
 */
@RestController
@RequestMapping(value = "/productkey")
public class ProductKeyController {

    @Autowired
    private RestClient dataAPIClient;


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ProductKey get(@PathVariable(value = "key") String key) {
        ProductKeyObject productKeyObject = dataAPIClient.get("productkey/{key}", ProductKeyObject.class, key);
        if (productKeyObject == null) {
            throw new NotFoundException("product key");
        }
        return fromProductKeyObject(productKeyObject);
    }

    @RequestMapping(value = "{key}/disable", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key) {
        dataAPIClient.put("productkey/{key}/disabled", true, key);
    }

    @RequestMapping(value = "{key}/enable", method = RequestMethod.PUT)
    public void enableKey(@PathVariable(value = "key") String key) {
        dataAPIClient.put("productkey/{key}/disabled", false, key);
    }

    private ProductKey fromProductKeyObject(ProductKeyObject object) {
        ProductKey productKey = new ProductKey();
        productKey.setProductKey(object.getProductKey());
        productKey.setProductKeyTypeCode(object.getProductKeyTypeCode());
        productKey.setProductKeyDisabled(object.isProductKeyDisabled());
        productKey.setPrimary(object.isPrimary());
        productKey.setProductKeyBatchId(object.getProductKeyBatchId());
        productKey.setPrimaryProductKey(object.getPrimaryProductKey());
        productKey.setProductKeySet(object.getProductKeySet());
        productKey.setCreatedDateTime(object.getCreatedDateTime());
        return productKey;
    }

}