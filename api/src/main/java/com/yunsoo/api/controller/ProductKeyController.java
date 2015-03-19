package com.yunsoo.api.controller;

import com.yunsoo.api.domain.ProductKeyDomain;
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

    @Autowired
    private ProductKeyDomain productKeyDomain;


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public ProductKey get(@PathVariable(value = "key") String key) {
        ProductKeyObject productKeyObj = dataAPIClient.get("productkey/{key}", ProductKeyObject.class, key);
        if (productKeyObj == null) {
            throw new NotFoundException("product key");
        }
        ProductKey productKey = new ProductKey();
        productKey.setProductKey(productKeyObj.getProductKey());
        productKey.setProductKeyTypeId(productKeyObj.getProductKeyTypeId());
        productKey.setProductKeyDisabled(productKeyObj.isProductKeyDisabled());
        productKey.setPrimary(productKeyObj.isPrimary());
        productKey.setProductKeyBatchId(productKeyObj.getProductKeyBatchId());
        productKey.setPrimaryProductKey(productKeyObj.getPrimaryProductKey());
        productKey.setProductKeySet(productKeyObj.getProductKeySet());
        productKey.setCreatedDateTime(productKeyObj.getCreatedDateTime());
        return productKey;
    }

    @RequestMapping(value = "{key}/disable", method = RequestMethod.PUT)
    public void disableKey(@PathVariable(value = "key") String key) {
        dataAPIClient.put("productkey/{key}/disabled", true, key);
    }

    @RequestMapping(value = "{key}/enable", method = RequestMethod.PUT)
    public void enableKey(@PathVariable(value = "key") String key) {
        dataAPIClient.put("productkey/{key}/disabled", false, key);
    }


}
