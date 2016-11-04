package com.yunsoo.marketing.dao.repository;


import com.yunsoo.marketing.dao.entity.UserEventEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public interface UserEventRepository extends Repository<UserEventEntity, String> {
    UserEventEntity findOne(String id);

    UserEventEntity save(UserEventEntity entity);

    List<UserEventEntity> findByKey(String key);

}
