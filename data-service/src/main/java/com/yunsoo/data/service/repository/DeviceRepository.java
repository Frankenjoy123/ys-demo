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

    Page<DeviceEntity> findByOrgId(String orgId, Pageable pageable);

    Page<DeviceEntity> findByOrgIdAndLoginAccountId(String orgId, String createdAccountId, Pageable pageable);

    Page<DeviceEntity> findAll(Pageable pageable);

    void delete(String id);
}
