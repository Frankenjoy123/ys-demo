package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.GroupPermissionPolicyEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/23
 * Descriptions:
 */
public interface GroupPermissionPolicyRepository extends CrudRepository<GroupPermissionPolicyEntity, String> {

    List<GroupPermissionPolicyEntity> findByGroupId(String groupId);

    @Transactional
    void deleteByGroupId(String groupId);
}
