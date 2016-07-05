package com.yunsoo.auth.dao.repository;


import com.yunsoo.auth.dao.entity.PermissionAllocationEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-17
 * Descriptions:
 */
public interface PermissionAllocationRepository extends Repository<PermissionAllocationEntity, String> {

    List<PermissionAllocationEntity> findByPrincipal(String principal);

    List<PermissionAllocationEntity> findByPrincipalIn(List<String> principals);

    PermissionAllocationEntity save(PermissionAllocationEntity entity);

    void delete(String id);

}
