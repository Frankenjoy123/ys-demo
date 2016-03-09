package com.yunsoo.data.service.dao.impl;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.data.service.dao.ProductDao;
import com.yunsoo.data.service.dbmodel.dynamodb.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("productDao")
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public ProductModel getByKey(String key) {
        if (key == null || key.length() == 0) {
            return null;
        }
        return dynamoDBMapper.load(ProductModel.class, key);
    }

    @Override
    public void save(ProductModel product) {
        dynamoDBMapper.save(product);
    }

    @Override
    public void batchSave(List<ProductModel> products) {
        dynamoDBMapper.batchSave(products);
    }

}
