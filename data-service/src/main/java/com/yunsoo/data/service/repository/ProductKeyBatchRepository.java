package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
public interface ProductKeyBatchRepository extends FindOneAndSaveRepository<ProductKeyBatchEntity, String> {

    Page<ProductKeyBatchEntity> findByOrgIdOrderByCreatedDateTimeDesc(String orgId, Pageable pageable);

    Page<ProductKeyBatchEntity> findByOrgIdAndProductBaseIdOrderByCreatedDateTimeDesc(String orgId, String productBaseId, Pageable pageable);
}
