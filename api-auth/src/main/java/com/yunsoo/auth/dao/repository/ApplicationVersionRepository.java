package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.ApplicationVersionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by:   Xiaowu
 * Created on:   2016-11-29
 * Descriptions:
 */
public interface ApplicationVersionRepository extends CrudRepository<ApplicationVersionEntity, Integer> {

    @Query("from ApplicationVersionEntity where appId=:appId and versionCode = (select max(versionCode) from ApplicationVersionEntity where appId=:appId) ")
    ApplicationVersionEntity query(@Param("appId") String appId);
}
