package com.yunsoo.marketing.dao.repository;


import com.yunsoo.marketing.dao.entity.MarketingEntity;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public interface MarketingRepository extends Repository<MarketingEntity, String> {
    MarketingEntity findOne(String id);

    MarketingEntity save(MarketingEntity entity);

}
