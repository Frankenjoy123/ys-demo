package com.yunsoo.marketing.dao.repository;


import com.yunsoo.marketing.dao.entity.UserRightContactEntity;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public interface UserRightContactRepository extends Repository<UserRightContactEntity, String> {
    UserRightContactEntity findOne(String userRightId);

    UserRightContactEntity save(UserRightContactEntity entity);

}
