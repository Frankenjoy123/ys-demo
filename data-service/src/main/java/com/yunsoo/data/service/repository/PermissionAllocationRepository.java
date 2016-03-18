package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.PermissionAllocationEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-03-17
 * Descriptions:
 */
public interface PermissionAllocationRepository extends FindOneAndSaveRepository<PermissionAllocationEntity, String> {

    List<PermissionAllocationEntity> findByPrincipal(String principal);

    void delete(String id);

}
