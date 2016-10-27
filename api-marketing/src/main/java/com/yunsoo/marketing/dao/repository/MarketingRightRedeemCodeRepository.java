package com.yunsoo.marketing.dao.repository;


import com.yunsoo.marketing.dao.entity.MarketingRightRedeemCodeEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public interface MarketingRightRedeemCodeRepository extends Repository<MarketingRightRedeemCodeEntity, String> {
    MarketingRightRedeemCodeEntity findOne(String id);

    MarketingRightRedeemCodeEntity save(MarketingRightRedeemCodeEntity entity);

    List<MarketingRightRedeemCodeEntity> findByMarketingRightId(String marketingRightId);

    MarketingRightRedeemCodeEntity findTop1ByMarketingRightIdAndUsedOrderByCreatedDateTime(String marketingRightId, Boolean used);


}
