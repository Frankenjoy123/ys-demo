package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.ProductKeyBatch;
import com.yunsoo.data.service.service.contract.ProductKeys;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductKeyBatchService {

    ProductKeyBatch getById(String batchId);

    ProductKeys getProductKeysByBatchId(String batchId);

    ProductKeyBatch create(ProductKeyBatch batch);

    void patchUpdate(ProductKeyBatch batch);
}
