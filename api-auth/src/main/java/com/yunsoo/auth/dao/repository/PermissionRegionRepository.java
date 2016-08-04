package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.PermissionRegionEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-17
 * Descriptions:
 */
public interface PermissionRegionRepository extends Repository<PermissionRegionEntity, String> {

    PermissionRegionEntity findOne(String id);

    List<PermissionRegionEntity> findByOrgId(String orgId);

    List<PermissionRegionEntity> findByOrgIdAndTypeCode(String orgId, String typeCode);

    PermissionRegionEntity save(PermissionRegionEntity entity);

    void delete(PermissionRegionEntity entity);

}
