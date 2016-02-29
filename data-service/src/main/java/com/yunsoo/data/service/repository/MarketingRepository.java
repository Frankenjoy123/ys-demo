package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MarketingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MarketingRepository extends CrudRepository<MarketingEntity, String> {
    Page<MarketingEntity> findByOrgId(String orgId, Pageable pageable);
}
