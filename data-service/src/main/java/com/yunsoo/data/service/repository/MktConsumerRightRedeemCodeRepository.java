package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MktConsumerRightRedeemCodeEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

/**
 * Created by  : haitao
 * Created on  : 2016/8/19
 * Descriptions:
 */
public interface MktConsumerRightRedeemCodeRepository extends FindOneAndSaveRepository<MktConsumerRightRedeemCodeEntity, String> {

    MktConsumerRightRedeemCodeEntity findTop1ByConsumerRightIdAndStatusCodeOrderByCreatedDateTime(String consumerRightId, String statusCode);

}
