package com.yunsoo.marketing.dao.repository;


import com.yunsoo.marketing.dao.entity.MarketingRightFlowEntity;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public interface MarketingRightFlowRepository extends Repository<MarketingRightFlowEntity, String> {
    MarketingRightFlowEntity findOne(String id);

    MarketingRightFlowEntity save(MarketingRightFlowEntity entity);

}
