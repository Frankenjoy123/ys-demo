package com.yunsoo.key.service;

import com.yunsoo.key.dto.ProductPackage;

import java.util.List;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2016-08-16
 * Descriptions:
 */
public interface ProductPackageService {

    ProductPackage getByKey(String key);

    Set<String> getAllChildKeySetByKey(String key);

    void disable(String key);

    void save(ProductPackage productPackage);

    int batchSave(List<ProductPackage> packages);

}
