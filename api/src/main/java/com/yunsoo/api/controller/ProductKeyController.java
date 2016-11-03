package com.yunsoo.api.controller;

import com.yunsoo.api.key.dto.Key;
import com.yunsoo.api.key.service.KeyService;
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
    private KeyService keyService;


    @RequestMapping(value = "{key}", method = RequestMethod.GET)
    public Key get(@PathVariable(value = "key") String key) {
        Key k = keyService.getKey(key);
        if (k == null) {
            throw new NotFoundException("product key not found. " + key);
        }
        return k;
    }

    @RequestMapping(value = "{key}/disable", method = RequestMethod.POST)
    public void disableKey(@PathVariable(value = "key") String key) {
        keyService.disableKey(key);
    }

    @RequestMapping(value = "{key}/enable", method = RequestMethod.POST)
    public void enableKey(@PathVariable(value = "key") String key) {
        keyService.enableKey(key);
    }

}