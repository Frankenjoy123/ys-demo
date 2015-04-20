package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductKeyBatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Lijian
 * Created on:   2015/4/14
 * Descriptions:
 */
public interface ProductKeyBatchRepository extends Repository<ProductKeyBatchEntity, String> {

    ProductKeyBatchEntity findOne(String id);

    ProductKeyBatchEntity save(ProductKeyBatchEntity entity);

    Page<ProductKeyBatchEntity> findByOrgIdOrderByCreatedDateTimeDesc(String orgId, Pageable pageable);

    Page<ProductKeyBatchEntity> findByOrgIdAndProductBaseIdOrderByCreatedDateTimeDesc(String orgId, String productBaseId, Pageable pageable);
}
