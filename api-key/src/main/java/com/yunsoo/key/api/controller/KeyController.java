package com.yunsoo.key.api.controller;

import com.yunsoo.common.web.exception.NotFoundException;
import com.yunsoo.key.dto.BatchSaveKeyRequest;
import com.yunsoo.key.dto.Key;
import com.yunsoo.key.dto.Product;
import com.yunsoo.key.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
@RestController
@RequestMapping("/key")
public class KeyController {

    @Autowired
    private KeyService keyService;

    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public Key getKey(@PathVariable(value = "key") String key) {
        Key productKey = keyService.get(key);
        if (productKey == null) {
            throw new NotFoundException("key not found: " + key);
        }
        return productKey;
    }

    @RequestMapping(value = "external/{partitionId}/{externalKey}", method = RequestMethod.GET)
    public Key getExternalKey(@PathVariable(value = "partitionId") String partitionId,
                              @PathVariable(value = "externalKey") String externalKey) {
        String key = String.format("%s/%s", partitionId, externalKey);
        Key productKey = keyService.get(key);
        if (productKey == null) {
            throw new NotFoundException("key not found: " + key);
        }
        return productKey;
    }

    @RequestMapping(value = "{key}/disable", method = RequestMethod.POST)
    public void disableKey(@PathVariable(value = "key") String key) {
        keyService.setDisabled(key, true);
    }

    @RequestMapping(value = "{key}/enable", method = RequestMethod.POST)
    public void enableKey(@PathVariable(value = "key") String key) {
        keyService.setDisabled(key, false);
    }

    @RequestMapping(value = "batchSave", method = RequestMethod.POST)
    public void batchSave(@RequestBody BatchSaveKeyRequest request) {
        Product productTemplate = new Product();
        productTemplate.setProductBaseId(request.getProductBaseId());
        productTemplate.setStatusCode(request.getProductStatusCode());
        productTemplate.setCreatedDateTime(request.getCreatedDateTime());
        productTemplate.setManufacturingDateTime(request.getManufacturingDateTime());

        keyService.batchSave(request.getKeyBatchId(), request.getKeyTypeCodes(), request.getKeys(), productTemplate);
    }

}
