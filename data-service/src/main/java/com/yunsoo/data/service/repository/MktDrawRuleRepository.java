package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDrawRuleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MktDrawRuleRepository extends CrudRepository<MktDrawRuleEntity, String> {
    List<MktDrawRuleEntity> findByMarketingIdOrderByAmountDesc(String marketingId);
}
