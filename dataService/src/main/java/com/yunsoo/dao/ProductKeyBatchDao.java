package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductKeyBatchModel;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductKeyBatchDao {

    public ProductKeyBatchModel getById(String id);

    public void save(ProductKeyBatchModel productKeyModel);

    public void update(ProductKeyBatchModel productKeyModel);
}
