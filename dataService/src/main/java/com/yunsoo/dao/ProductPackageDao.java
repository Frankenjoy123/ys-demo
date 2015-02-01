package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductPackageModel;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductPackageDao {

    public ProductPackageModel getByProductKey(String key);

    public void save(ProductPackageModel productKeyModel);

    public void update(ProductPackageModel productKeyModel);
}
