package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserOrganizationFollowingEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by:   Zhe
 * Created on:   2015/4/15
 * Descriptions:
 */
@Transactional
public interface UserOrganizationFollowingRepository extends FindOneAndSaveRepository<UserOrganizationFollowingEntity, String> {

    Page<UserOrganizationFollowingEntity> findByUserId(String userId, Pageable pageable);

    Page<UserOrganizationFollowingEntity> findByOrgId(String orgId, Pageable pageable);

    List<UserOrganizationFollowingEntity> findByUserIdAndOrgId(String userId, String orgId);

    Long countByOrgId(String orgId);

    @Transactional
    void deleteByUserIdAndOrgId(String userId, String orgId);
}
