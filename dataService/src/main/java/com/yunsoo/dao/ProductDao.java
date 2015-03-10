package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductModel;

import java.util.List;


public interface ProductDao {

    public ProductModel getByKey(String key);

    public void save(ProductModel product);

    public void batchSave(List<ProductModel> product);

//    public void update(ProductModel product);

}
