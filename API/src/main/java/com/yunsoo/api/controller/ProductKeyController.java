package com.yunsoo.api.controller;

import com.yunsoo.api.dto.ProductKey;
import com.yunsoo.api.object.TProductKey;
import com.yunsoo.common.web.client.RestClient;
import com.yunsoo.common.web.exception.BadRequestException;
import com.yunsoo.common.web.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
        TProductKey tProductKey = dataAPIClient.get("productkey/{key}", TProductKey.class, key);
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


}
