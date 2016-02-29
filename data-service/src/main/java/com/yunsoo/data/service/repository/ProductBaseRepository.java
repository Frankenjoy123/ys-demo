package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by  : haitao
 * Created on  : 2015/7/20
 * Descriptions:
 */
public interface ProductBaseRepository extends FindOneAndSaveRepository<ProductBaseEntity, String> {

    Page<ProductBaseEntity> findByDeletedFalseAndOrgId(String orgId, Pageable pageable);

    Page<ProductBaseEntity> findByDeletedFalse(Pageable pageable);

}
