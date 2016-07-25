package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.DeviceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by:   Lijian
 * Created on:   2016-07-25
 * Descriptions:
 */
public interface DeviceRepository extends Repository<DeviceEntity, String> {

    DeviceEntity findOne(String id);

    Page<DeviceEntity> findByAuthAccountId(String authAccountId, Pageable pageable);

    Page<DeviceEntity> findByOrgId(String orgId, Pageable pageable);

    Page<DeviceEntity> findAll(Pageable pageable);

    DeviceEntity save(DeviceEntity entity);

    void delete(String id);

}
