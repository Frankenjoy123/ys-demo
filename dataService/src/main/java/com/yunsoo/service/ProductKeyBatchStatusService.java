package com.yunsoo.service;

import com.yunsoo.service.contract.ProductKeyBatchStatus;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/11
 * Descriptions:
 */
public interface ProductKeyBatchStatusService {

    public List<ProductKeyBatchStatus> getAll();

    public List<ProductKeyBatchStatus> getAllActive();
}
