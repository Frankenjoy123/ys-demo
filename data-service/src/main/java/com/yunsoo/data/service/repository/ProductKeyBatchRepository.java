package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
public interface ProductKeyBatchRepository extends FindOneAndSaveRepository<ProductKeyBatchEntity, String> {

    Page<ProductKeyBatchEntity> findByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodes, Pageable pageable);

    Page<ProductKeyBatchEntity> findByOrgIdAndProductBaseIdAndStatusCodeIn(String orgId, String productBaseId, List<String> statusCodes, Pageable pageable);
}
