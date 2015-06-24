package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.GroupPermissionPolicyEntity;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/23
 * Descriptions:
 */
public interface GroupPermissionPolicyRepository extends Repository<GroupPermissionPolicyEntity, String> {

    List<GroupPermissionPolicyEntity> findByGroupId(String groupId);
}
