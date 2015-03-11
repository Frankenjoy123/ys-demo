package com.yunsoo.service;

import com.yunsoo.service.contract.ProductKeyBatch;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductKeyBatchService {

    public ProductKeyBatch getById(String batchId);

    public List<List<String>> getProductKeys(String batchId);

    public ProductKeyBatch create(ProductKeyBatch keyBatch);

    public ProductKeyBatch createAsync(ProductKeyBatch keyBatch);
}
