package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.DeviceEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Created by  : Zhe
 * Created on  : 2015/5/8
 * Descriptions:
 */
public interface DeviceRepository extends FindOneAndSaveRepository<DeviceEntity, String> {

    DeviceEntity findById(String id);

    Page<DeviceEntity> findByOrgId(String orgId, Pageable pageable);

    Page<DeviceEntity> findByLoginAccountId(String createdAccountId, Pageable pageable);

    Page<DeviceEntity> findAll(Pageable pageable);
}
