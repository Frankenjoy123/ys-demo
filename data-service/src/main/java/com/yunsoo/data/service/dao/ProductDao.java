package com.yunsoo.data.service.dao;

import com.yunsoo.data.service.dbmodel.dynamodb.ProductModel;

import java.util.Collection;


public interface ProductDao {

   ProductModel getByKey(String key);

   void save(ProductModel product);

   void batchSave(Collection<ProductModel> products);

}
