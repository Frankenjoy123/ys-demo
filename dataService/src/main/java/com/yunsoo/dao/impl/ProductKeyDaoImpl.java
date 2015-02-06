package com.yunsoo.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.dao.ProductKeyDao;
import com.yunsoo.dbmodel.ProductKeyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductKeyDaoImpl implements ProductKeyDao {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public ProductKeyModel getByProductKey(String key) {
        return dynamoDBMapper.load(ProductKeyModel.class, key);
    }

    @Override
    public void save(ProductKeyModel productKeyModel) {
        dynamoDBMapper.save(productKeyModel);
    }

    @Override
    public void batchSave(List<ProductKeyModel> productKeyModels) {
        dynamoDBMapper.batchSave(productKeyModels);
    }

    @Override
    public void update(ProductKeyModel productKeyModel) {
        dynamoDBMapper.save(productKeyModel); //TODO
    }

}
