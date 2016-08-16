package com.yunsoo.key.service;

import com.yunsoo.key.dto.Product;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public interface ProductService {

    Product getByKey(String key);

    void patchUpdate(Product product);

}
