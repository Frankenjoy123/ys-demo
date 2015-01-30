package com.yunsoo.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.dao.ProductKeyDao;
import com.yunsoo.nosql.dynamoDB.model.ProductKeyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("productKeyDao")
public class ProductKeyDaoImpl implements ProductKeyDao {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public com.yunsoo.nosql.dynamoDB.model.ProductKeyModel getByProductKey(String key) {
        return dynamoDBMapper.load(ProductKeyModel.class, key);
    }

    @Override
    public void save(com.yunsoo.nosql.dynamoDB.model.ProductKeyModel productKeyModel) {
        dynamoDBMapper.save(productKeyModel);
    }

    @Override
    public void update(com.yunsoo.nosql.dynamoDB.model.ProductKeyModel productKeyModel) {
        dynamoDBMapper.save(productKeyModel); //TODO
    }

}
