package com.yunsoo.auth.dao.repository;

import com.yunsoo.auth.dao.entity.GroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by  : Lijian
 * Created on  : 2015/6/23
 * Descriptions:
 */
public interface GroupRepository extends Repository<GroupEntity, String> {

    GroupEntity findOne(String id);

    Page<GroupEntity> findByOrgId(String orgId, Pageable pageable);

    GroupEntity save(GroupEntity entity);

    void delete(String id);

}
