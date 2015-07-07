package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.GroupEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;

import java.util.List;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/23
 * Descriptions:
 */
public interface GroupRepository extends FindOneAndSaveRepository<GroupEntity, String> {

    List<GroupEntity> findByOrgId(String orgId);

    void delete(String id);
}
