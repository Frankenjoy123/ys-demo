package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MarketingEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MarketingRepository extends FindOneAndSaveRepository<MarketingEntity, String> {
    Page<MarketingEntity> findByOrgId(String orgId, Pageable pageable);
}
