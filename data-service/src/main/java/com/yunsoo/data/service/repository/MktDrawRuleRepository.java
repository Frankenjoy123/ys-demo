package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDrawRuleEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MktDrawRuleRepository extends FindOneAndSaveRepository<MktDrawRuleEntity, String> {
    List<MktDrawRuleEntity> findByMarketingIdOrderById(String marketingId);

    List<MktDrawRuleEntity> save(Iterable<MktDrawRuleEntity> entities);

    void delete(String id);

}
