package com.yunsoo.service;

import com.yunsoo.service.contract.ProductKeyBatch;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductKeyBatchService {

    public ProductKeyBatch getById(String batchId);

    public ProductKeyBatch getByIdWithProductKeys(String batchId);

    public ProductKeyBatch create(ProductKeyBatch keyBatch);
}
