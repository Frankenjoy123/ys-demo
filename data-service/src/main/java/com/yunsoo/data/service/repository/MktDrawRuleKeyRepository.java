package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktDrawRuleKeyEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/9/9
 * Descriptions:
 */
public interface MktDrawRuleKeyRepository extends FindOneAndSaveRepository<MktDrawRuleKeyEntity, String> {
    List<MktDrawRuleKeyEntity> findByProductKeyAndMarketingId(String productKey, String marketingId);

}
