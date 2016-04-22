package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductKeyDomain;
import com.yunsoo.api.dto.ProductKey;
import com.yunsoo.common.data.object.ProductKeyObject;
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
    private ProductKeyDomain productKeyDomain;


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ProductKey get(@PathVariable(value = "key") String key) {
        ProductKeyObject productKeyObject = productKeyDomain.getProductKey(key);
        if (productKeyObject == null) {
            throw new NotFoundException("product key not found. " + key);
        }
        return new ProductKey(productKeyObject);
    }

    @RequestMapping(value = "{key}/disable", method = RequestMethod.POST)
    public void disableKey(@PathVariable(value = "key") String key) {
        productKeyDomain.setProductKeyDisabled(key, true);
    }

    @RequestMapping(value = "{key}/enable", method = RequestMethod.POST)
    public void enableKey(@PathVariable(value = "key") String key) {
        productKeyDomain.setProductKeyDisabled(key, false);
    }

}