package com.yunsoo.api.rabbit.key.service;

import com.yunsoo.api.rabbit.client.KeyApiClient;
import com.yunsoo.api.rabbit.key.dto.Product;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-08-23
 * Descriptions:
 */
@Service
public class ProductService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;


    public Product getProductByKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        try {
            return keyApiClient.get("product/{key}", Product.class, key);
        } catch (NotFoundException e) {
            log.warn("key not found, key: " + key);
            return null;
        }
    }
}
