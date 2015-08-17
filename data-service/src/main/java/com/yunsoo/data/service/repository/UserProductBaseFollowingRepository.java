package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserProductBaseFollowingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yan on 8/17/2015.
 */
@Transactional
public interface UserProductBaseFollowingRepository extends PagingAndSortingRepository<UserProductBaseFollowingEntity, Long> {

    List<UserProductBaseFollowingEntity> findById(Long Id);

    List<UserProductBaseFollowingEntity> findByUserId(String userId);

    List<UserProductBaseFollowingEntity> findByUserIdAndProductBaseId(String userId, String productBaseId);

    Page<UserProductBaseFollowingEntity> findByUserId(String userId, Pageable pageable);

    Page<UserProductBaseFollowingEntity> findByUserIdAndIsFollowing(String userId, Boolean isFollowing, Pageable pageable);

    Page<UserProductBaseFollowingEntity> findByProductBaseId(String productBaseId, Pageable pageable);

    List<UserProductBaseFollowingEntity> findByProductBaseId(String productBaseId);
}
