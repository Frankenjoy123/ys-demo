package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.ApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-07-14
 * Descriptions:
 */
public interface ApplicationRepository extends Repository<ApplicationEntity, String> {

    ApplicationEntity findOne(String id);

    Page<ApplicationEntity> findAll(Pageable pageable);

    Page<ApplicationEntity> findByTypeCode(String typeCode, Pageable pageable);

    List<ApplicationEntity> findByTypeCodeAndIdIn(String typeCode, List<String> ids);

    ApplicationEntity save(ApplicationEntity entity);

}
