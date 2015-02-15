package com.yunsoo.dao.impl;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.dbmodel.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.yunsoo.dao.ProductDao;

import java.util.List;

@Repository("productDao")
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public ProductModel getByKey(String key) {
        return dynamoDBMapper.load(ProductModel.class, key);
    }

    @Override
    public void save(ProductModel product) {
        dynamoDBMapper.save(product);
    }

    @Override
    public void batchSave(List<ProductModel> products){
        dynamoDBMapper.batchSave(products);
    }

    @Override
    public void update(ProductModel product) {
        dynamoDBMapper.save(product);
    }

}
