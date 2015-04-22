package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserFollowingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Zhe on 2015/4/15.
 */
public interface UserFollowingRepository extends PagingAndSortingRepository<UserFollowingEntity, Long> {

    Iterable<UserFollowingEntity> findById(Long Id);

    Iterable<UserFollowingEntity> findByUserId(String userId);

    Page<UserFollowingEntity> findByUserId(String userId, Pageable pageable);

    Iterable<UserFollowingEntity> findByOrganizationId(String organizationId);

    Page<UserFollowingEntity> findByOrganizationId(String organizationId, Pageable pageable);

}
