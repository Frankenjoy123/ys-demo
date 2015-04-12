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

    ProductKeyBatch getById(Long batchId);

    List<ProductKeyBatch> getByOrganizationIdPaged(Integer organizationId, int pageIndex, int pageSize);

    List<ProductKeyBatch> getByFilterPaged(Integer organizationId, Long productBaseId, int pageIndex, int pageSize);

    ProductKeys getProductKeysByBatchId(Long batchId);

    ProductKeys getProductKeysByAddress(String address);

    ProductKeyBatch create(ProductKeyBatch batch);
}