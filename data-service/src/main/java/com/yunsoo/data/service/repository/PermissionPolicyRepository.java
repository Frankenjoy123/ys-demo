package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.PermissionPolicyEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by:   Lijian
 * Created on:   2015/4/12
 * Descriptions:
 */
public interface PermissionPolicyRepository extends CrudRepository<PermissionPolicyEntity, String> {
    Iterable<PermissionPolicyEntity> findByCode(String code);
}
