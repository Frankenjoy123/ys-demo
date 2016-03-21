package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.PermissionPolicyEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2015/4/12
 * Descriptions:
 */
public interface PermissionPolicyRepository extends Repository<PermissionPolicyEntity, String> {

    PermissionPolicyEntity findOne(String code);

    List<PermissionPolicyEntity> findAll();

    List<PermissionPolicyEntity> save(Iterable<PermissionPolicyEntity> entities);

    void delete(Iterable<PermissionPolicyEntity> entities);

}
