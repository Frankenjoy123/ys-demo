package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.dynamodb.ProductPackageModel;

import java.util.Collection;
import java.util.List;


/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductPackageDao {

    ProductPackageModel getByKey(String key);

    List<ProductPackageModel> batchLoad(Collection<String> keys);

    void save(ProductPackageModel productPackage);

    void batchSave(Collection<ProductPackageModel> productPackages);

}
