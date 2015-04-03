package com.yunsoo.dao;

import com.yunsoo.dbmodel.ProductModel;

import java.util.List;


public interface ProductDao {

   ProductModel getByKey(String key);

   void save(ProductModel product);

   void batchSave(List<ProductModel> products);

}
