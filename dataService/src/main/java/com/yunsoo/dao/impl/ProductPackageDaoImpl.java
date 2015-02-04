package com.yunsoo.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.dao.ProductPackageDao;
import com.yunsoo.dbmodel.ProductPackageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
@Repository("productPackageDao")
public class ProductPackageDaoImpl implements ProductPackageDao {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public ProductPackageModel getByProductKey(String key) {
        return dynamoDBMapper.load(ProductPackageModel.class, key);
    }

    @Override
    public void save(ProductPackageModel productKeyModel) {
        dynamoDBMapper.save(productKeyModel);
    }

    @Override
    public void update(ProductPackageModel productKeyModel) {
        dynamoDBMapper.save(productKeyModel);
    }
}
