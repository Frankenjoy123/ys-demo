package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.GroupPermissionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/23
 * Descriptions:
 */
public interface GroupPermissionRepository extends CrudRepository<GroupPermissionEntity, String> {

    List<GroupPermissionEntity> findByGroupId(String groupId);

}
