package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.GroupEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/23
 * Descriptions:
 */
public interface GroupRepository extends FindOneAndSaveRepository<GroupEntity, String> {

    Page<GroupEntity> findByOrgId(String orgId, Pageable pageable);

    void delete(String id);
}
