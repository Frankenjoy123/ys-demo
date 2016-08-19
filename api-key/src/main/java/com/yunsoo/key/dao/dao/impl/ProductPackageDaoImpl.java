package com.yunsoo.key.dao.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.key.dao.dao.ProductPackageDao;
import com.yunsoo.key.dao.model.ProductPackageModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2016-08-19
 * Descriptions:
 */
@Repository
public class ProductPackageDaoImpl implements ProductPackageDao {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public ProductPackageModel getByKey(String key) {
        if (key == null || key.length() == 0) {
            return null;
        }
        return dynamoDBMapper.load(ProductPackageModel.class, key);
    }

    @Override
    public List<ProductPackageModel> batchLoad(Collection<String> keys) {
        if (keys == null || keys.size() == 0) {
            return new ArrayList<>();
        }
        List<Object> itemsToGet = keys.stream().map(k -> {
            ProductPackageModel model = new ProductPackageModel();
            model.setProductKey(k);
            return model;
        }).collect(Collectors.toList());

        Map<String, List<Object>> items = dynamoDBMapper.batchLoad(itemsToGet);
        if (items.size() > 0) {
            for (Map.Entry<String, List<Object>> entry : items.entrySet()) {
                if (log.isDebugEnabled()) {
                    log.debug("retrieve data from table: " + entry.getKey());
                }
                //return only the first table
                return entry.getValue().stream().map(p -> (ProductPackageModel) p).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void save(ProductPackageModel productKeyModel) {
        if (productKeyModel == null) {
            return;
        }
        dynamoDBMapper.save(productKeyModel);
    }

    @Override
    public void batchSave(Collection<ProductPackageModel> productPackages) {
        if (productPackages == null || productPackages.size() == 0) {
            return;
        }
        List<DynamoDBMapper.FailedBatch> failedBatches = dynamoDBMapper.batchSave(productPackages);
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
                    String.format("batchSave failed with %d items in total %d requests", failedBatches.size(), productPackages.size()),
                    exception);
        }
    }
}
