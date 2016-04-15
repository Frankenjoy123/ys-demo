package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.PermissionRegionEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-17
 * Descriptions:
 */
public interface PermissionRegionRepository extends FindOneAndSaveRepository<PermissionRegionEntity, String> {

    List<PermissionRegionEntity> findByOrgId(String orgId);

    List<PermissionRegionEntity> findByOrgIdAndTypeCode(String orgId, String typeCode);

    void delete(String id);

}
