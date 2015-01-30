package com.yunsoo.dao;

import com.yunsoo.nosql.dynamoDB.model.ProductModel;


public interface ProductDao {

    public ProductModel getByKey(String key);

    public void save(ProductModel product);

    public void update(ProductModel product);

}
