package com.yunsoo.data.service.service;

import com.amazonaws.services.s3.model.S3Object;
import com.yunsoo.data.service.service.contract.ProductKeyBatch;
import com.yunsoo.data.service.service.contract.ProductKeys;

import java.io.InputStream;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductKeyBatchService {

    ProductKeyBatch getById(String batchId);

    ProductKeys getProductKeysByBatchId(String batchId);

    S3Object getProductKeyBatchDetails(String batchId);

    void saveProductKeyBatchDetails(String batchId, InputStream inputStream, String contentType, long contentLength);

    ProductKeyBatch create(ProductKeyBatch batch);

    void patchUpdate(ProductKeyBatch batch);
}
