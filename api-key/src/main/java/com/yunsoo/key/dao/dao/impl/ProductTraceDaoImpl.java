package com.yunsoo.key.dao.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yunsoo.key.dao.dao.ProductTraceDao;
import com.yunsoo.key.dao.model.ProductTraceModel;
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
 * Created by yan on 10/10/2016.
 */
@Repository("productTraceDao")
public class ProductTraceDaoImpl implements ProductTraceDao {

    private Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Override
    public ProductTraceModel getByKey(String key) {
        if (key == null || key.length() == 0) {
            return null;
        }
        return dynamoDBMapper.load(ProductTraceModel.class, key);
    }

    @Override
    public List<ProductTraceModel> batchLoad(Collection<String> keys) {
        if (keys == null || keys.size() == 0) {
            return new ArrayList<>();
        }
        List<ProductTraceModel> itemsToGet = keys.stream().map(k -> {
            ProductTraceModel model = new ProductTraceModel();
            model.setProductKey(k);
            return model;
        }).collect(Collectors.toList());

        Map<String, List<Object>> items = dynamoDBMapper.batchLoad(itemsToGet);
        if (items.size() > 0) {
            for (Map.Entry<String, List<Object>> entry : items.entrySet()) {
                log.info("retrieve data from table " + entry.getKey());
                //return only the first table
                return entry.getValue().stream().map(p -> (ProductTraceModel) p).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @Override
    public void save(ProductTraceModel productTrace) {
        if (productTrace == null) {
            return;
        }
        dynamoDBMapper.save(productTrace);
    }

    @Override
    public void batchSave(Collection<ProductTraceModel> productTraces) {
        if (productTraces == null || productTraces.size() == 0) {
            return;
        }
        List<DynamoDBMapper.FailedBatch> failedBatches = dynamoDBMapper.batchSave(productTraces);
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
                    String.format("batchSave failed with %d items in total %d requests", failedBatches.size(), productTraces.size()),
                    exception);
        }
    }
}
