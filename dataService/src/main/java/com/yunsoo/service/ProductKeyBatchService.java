package com.yunsoo.service;

import com.yunsoo.service.contract.Product;
import com.yunsoo.service.contract.ProductKeyBatch;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/2/1
 * Descriptions:
 */
public interface ProductKeyBatchService {

    public ProductKeyBatch getById(int batchId);

    public List<List<String>> getProductKeysByBatchId(int batchId);

    public List<List<String>> getProductKeysByAddress(String address);

    public ProductKeyBatch create(ProductKeyBatch batch);

    public ProductKeyBatch create(ProductKeyBatch batch, Product product);

    public ProductKeyBatch createAsync(ProductKeyBatch batch);

    public ProductKeyBatch createAsync(ProductKeyBatch batch, Product product);
}
