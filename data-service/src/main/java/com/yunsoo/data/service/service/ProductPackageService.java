package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.ProductPackage;

import java.util.List;
import java.util.Set;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductPackageService {

    ProductPackage getByKey(String key);

    Set<String> getAllChildProductKeySetByKey(String key);

    void disable(String key);

    void save(ProductPackage productPackage);

    int batchPackage(List<ProductPackage> packages);

}
