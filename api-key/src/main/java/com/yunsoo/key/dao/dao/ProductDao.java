package com.yunsoo.key.dao.dao;

import com.yunsoo.key.dao.model.ProductModel;

import java.util.Collection;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
public interface ProductDao {

    ProductModel getByKey(String key);

    void save(ProductModel product);

    void batchSave(Collection<ProductModel> products);

}
