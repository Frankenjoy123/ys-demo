package com.yunsoo.service.Impl;

import com.yunsoo.dao.ProductKeyBatchStatusDao;
import com.yunsoo.service.ProductKeyBatchStatusService;
import com.yunsoo.service.contract.ProductKeyBatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by:   Lijian
 * Created on:   2015/3/11
 * Descriptions:
 */
@Service("productKeyBatchStatusService")
public class ProductKeyBatchStatusServiceImpl implements ProductKeyBatchStatusService {

    @Autowired
    private ProductKeyBatchStatusDao productKeyBatchStatusDao;

    @Override
    public List<ProductKeyBatchStatus> getAll() {
        return productKeyBatchStatusDao.getAll(false)
                .stream()
                .map(ProductKeyBatchStatus::fromModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductKeyBatchStatus> getAllActive() {
        return productKeyBatchStatusDao.getAll(true)
                .stream()
                .map(ProductKeyBatchStatus::fromModel)
                .collect(Collectors.toList());
    }
}
