package com.yunsoo.data.service.dao.impl;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.data.service.dao.ProductDao;
import com.yunsoo.data.service.dbmodel.dynamodb.ProductModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository("productDao")
public class ProductDaoImpl implements ProductDao {

    private Log log = LogFactory.getLog(this.getClass());

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
        if (product == null) {
            return;
        }
        dynamoDBMapper.save(product);
    }

    @Override
    public void batchSave(Collection<ProductModel> products) {
        if (products == null || products.size() == 0) {
            return;
        }
        List<DynamoDBMapper.FailedBatch> failedBatches = dynamoDBMapper.batchSave(products);
        if (failedBatches.size() == 0) {
            return;
        }

        Exception exception = null;
        for (DynamoDBMapper.FailedBatch fb : failedBatches) {
            Exception e = fb.getException();
            log.error("exception thrown in batchSave", e);
            if (exception == null && e != null) {
                exception = e;
            }
        }
        if (exception != null) {
            throw new RuntimeException(
                    String.format("batchSave failed with %d items in total %d requests", failedBatches.size(), products.size()),
                    exception);
        }
    }

}
