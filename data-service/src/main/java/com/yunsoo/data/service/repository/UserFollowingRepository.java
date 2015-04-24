package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserFollowingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Zhe on 2015/4/15.
 */
@Transactional
public interface UserFollowingRepository extends PagingAndSortingRepository<UserFollowingEntity, Long> {

    Iterable<UserFollowingEntity> findById(Long Id);

    Iterable<UserFollowingEntity> findByUserId(String userId);

    Iterable<UserFollowingEntity> findByUserIdAndOrganizationId(String userId, String organizationId);

    Page<UserFollowingEntity> findByUserId(String userId, Pageable pageable);

    Iterable<UserFollowingEntity> findByOrganizationId(String organizationId);

    Page<UserFollowingEntity> findByOrganizationId(String organizationId, Pageable pageable);

}
