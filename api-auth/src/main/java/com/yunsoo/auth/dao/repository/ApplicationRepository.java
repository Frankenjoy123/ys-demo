package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.ApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Lijian
 * Created on:   2016-07-14
 * Descriptions:
 */
public interface ApplicationRepository extends Repository<ApplicationEntity, String> {

    ApplicationEntity findOne(String id);

    Page<ApplicationEntity> findAll(Pageable pageable);

    ApplicationEntity save(ApplicationEntity entity);

}
