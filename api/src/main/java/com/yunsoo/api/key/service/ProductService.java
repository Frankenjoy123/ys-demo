package com.yunsoo.api.key.service;

import com.yunsoo.api.client.KeyApiClient;
import com.yunsoo.api.key.Constants;
import com.yunsoo.api.key.dto.Product;
import com.yunsoo.common.web.exception.NotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by:   Lijian
 * Created on:   2016-11-03
 * Descriptions:
 */
@Service
public class ProductService {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private KeyApiClient keyApiClient;


    public Product getProductByKey(String key) {
        if (!StringUtils.hasText(key)) {
            return null;
        }
        try {
            return keyApiClient.get("product/{key}", Product.class, key);
        } catch (NotFoundException e) {
            log.warn("key not found, key: " + key);
            return null;
        }
    }

    public void patchUpdate(Product product) {
        if (product == null || StringUtils.isEmpty(product.getKey())) {
            return;
        }
        try {
            keyApiClient.patch("product/{key}", product, product.getKey());
        } catch (NotFoundException e) {
            log.warn("key not found while patch updating, key: " + product.getKey());
        }
    }

    public void activeProduct(String key) {
        Product product = new Product();
        product.setKey(key);
        product.setStatusCode(Constants.ProductStatus.ACTIVATED);
        patchUpdate(product);
    }

    public void deleteProduct(String key) {
        Product product = new Product();
        product.setKey(key);
        product.setStatusCode(Constants.ProductStatus.DELETED);
        patchUpdate(product);
    }

}
