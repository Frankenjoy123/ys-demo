package com.yunsoo.marketing.dao.repository;


import com.yunsoo.common.web.client.Page;
import com.yunsoo.marketing.dao.entity.UserRightEntity;
import org.springframework.data.repository.Repository;

import java.awt.print.Pageable;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public interface UserRightRepository extends Repository<UserRightEntity, String> {
    UserRightEntity findOne(String id);

    UserRightEntity save(UserRightEntity entity);

    Page<UserRightEntity> findByMarketingId(String orgId, Pageable pageable);


}
