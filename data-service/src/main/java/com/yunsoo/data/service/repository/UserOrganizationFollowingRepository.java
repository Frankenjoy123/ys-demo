package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserOrganizationFollowingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Zhe on 2015/4/15.
 */
@Transactional
public interface UserOrganizationFollowingRepository extends PagingAndSortingRepository<UserOrganizationFollowingEntity, Long> {

    Iterable<UserOrganizationFollowingEntity> findById(Long Id);

    Iterable<UserOrganizationFollowingEntity> findByUserId(String userId);

    Iterable<UserOrganizationFollowingEntity> findByUserIdAndOrganizationId(String userId, String organizationId);

    Page<UserOrganizationFollowingEntity> findByUserId(String userId, Pageable pageable);

    Page<UserOrganizationFollowingEntity> findByUserIdAndIsFollowing(String userId, Boolean isFollowing, Pageable pageable);

    Page<UserOrganizationFollowingEntity> findByOrganizationId(String organizationId, Pageable pageable);

    Iterable<UserOrganizationFollowingEntity> findByOrganizationId(String organizationId);

}
