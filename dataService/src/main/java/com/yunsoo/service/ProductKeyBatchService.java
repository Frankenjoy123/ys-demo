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

    public List<List<String>> getProductKeys(int batchId);

    public ProductKeyBatch create(ProductKeyBatch batch);

    public ProductKeyBatch createWithProduct(ProductKeyBatch batch, Product product);

    public ProductKeyBatch createAsync(ProductKeyBatch batch);

    public ProductKeyBatch createWithProductAsync(ProductKeyBatch batch, Product product);
}
