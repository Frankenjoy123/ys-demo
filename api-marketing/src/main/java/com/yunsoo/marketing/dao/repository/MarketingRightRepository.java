package com.yunsoo.marketing.dao.repository;


import com.yunsoo.marketing.dao.entity.MarketingRightEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public interface MarketingRightRepository extends Repository<MarketingRightEntity, String> {

    MarketingRightEntity findOne(String id);

    MarketingRightEntity save(MarketingRightEntity entity);

    List<MarketingRightEntity> save(Iterable<MarketingRightEntity> entities);


    List<MarketingRightEntity> findByMarketingId(String marketingId);

    void delete(MarketingRightEntity entity);

}
