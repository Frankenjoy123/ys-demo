package com.yunsoo.data.service.service.Impl;

import com.yunsoo.data.service.repository.ProductKeyBatchStatusRepository;
import com.yunsoo.data.service.service.ProductKeyBatchStatusService;
import com.yunsoo.data.service.service.contract.ProductKeyBatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/3/11
 * Descriptions:
 */
@Service("productKeyBatchStatusService")
public class ProductKeyBatchStatusServiceImpl implements ProductKeyBatchStatusService {

    @Autowired
    private ProductKeyBatchStatusRepository productKeyBatchStatusRepository;

    @Override
    public ProductKeyBatchStatus getById(int id) {
        return ProductKeyBatchStatus.fromEntity(productKeyBatchStatusRepository.findOne(id));
    }

    @Override
    public List<ProductKeyBatchStatus> getAll(Boolean activeOnly) {
        return ProductKeyBatchStatus.fromEntities(
                activeOnly == null
                        ? productKeyBatchStatusRepository.findAll()
                        : productKeyBatchStatusRepository.findByActive(activeOnly));
    }

    @Override
    public ProductKeyBatchStatus save(ProductKeyBatchStatus lookup) {
        return ProductKeyBatchStatus.fromEntity(productKeyBatchStatusRepository.save(ProductKeyBatchStatus.toEntity(lookup)));
    }

    @Override
    public void delete(ProductKeyBatchStatus lookup) {
        productKeyBatchStatusRepository.delete(ProductKeyBatchStatus.toEntity(lookup));
    }
}
