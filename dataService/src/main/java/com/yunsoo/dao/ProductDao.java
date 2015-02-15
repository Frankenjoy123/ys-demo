package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductModel;


public interface ProductDao {

    public ProductModel getByKey(String key);

    public void save(ProductModel product);

    public void update(ProductModel product);

}
