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

    List<ProductKeyBatch> getByFilterPaged(String orgId, int pageIndex, int pageSize);

    List<ProductKeyBatch> getByFilterPaged(String orgId, String productBaseId, int pageIndex, int pageSize);

    ProductKeys getProductKeysByBatchId(String batchId);

    ProductKeys getProductKeysByUri(String uri);

    ProductKeyBatch create(ProductKeyBatch batch);
}
