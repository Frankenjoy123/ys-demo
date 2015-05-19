package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductKey;
import com.yunsoo.common.data.object.ProductKeyObject;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            ProductKeyObject productKeyObject = dataAPIClient.get("productkey/{key}", ProductKeyObject.class, key);
            return fromProductKeyObject(productKeyObject);
        } catch (NotFoundException ex) {
            throw new NotFoundException("product key " + key);
        }
    }

    @RequestMapping(value = "{key}/disable", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key) {
        try {
            dataAPIClient.put("productkey/{key}/disabled", true, key);
        } catch (NotFoundException ex) {
            throw new NotFoundException("product key " + key);
        }
    }

    @RequestMapping(value = "{key}/enable", method = RequestMethod.PUT)
    public void enableKey(@PathVariable(value = "key") String key) {
        try {
            dataAPIClient.put("productkey/{key}/disabled", false, key);
        } catch (NotFoundException ex) {
            throw new NotFoundException("product key " + key);
        }
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