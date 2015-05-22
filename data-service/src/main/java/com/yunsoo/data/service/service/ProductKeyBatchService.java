package com.yunsoo.data.service.service;

import com.yunsoo.data.service.service.contract.ProductKeys;
import com.yunsoo.data.service.service.contract.ProductKeyBatch;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductKeyBatchService {

    ProductKeyBatch getById(String batchId);

    List<ProductKeyBatch> getByFilterPaged(String orgId, int page, int size);

    List<ProductKeyBatch> getByFilterPaged(String orgId, String productBaseId, int page, int size);

    ProductKeys getProductKeysByBatchId(String batchId);

    ProductKeyBatch create(ProductKeyBatch batch);

    void patchUpdate(ProductKeyBatch batch);
}
