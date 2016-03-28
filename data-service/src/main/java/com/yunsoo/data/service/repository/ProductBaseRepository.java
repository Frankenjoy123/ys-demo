package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductBaseEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2015/7/20
 * Descriptions:
 */
public interface ProductBaseRepository extends FindOneAndSaveRepository<ProductBaseEntity, String> {

    Page<ProductBaseEntity> findByDeletedFalseAndOrgId(String orgId, Pageable pageable);

    Page<ProductBaseEntity> findByDeletedFalse(Pageable pageable);

    Long countByOrgIdAndDeletedFalse(String orgId);

    @Query("select p.id from ProductBaseEntity p where orgId = ?1")
    List<String> findIdByOrgId(String orgId);

}
