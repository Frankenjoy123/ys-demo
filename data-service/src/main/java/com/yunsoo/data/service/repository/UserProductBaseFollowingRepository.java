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

    List<UserProductBaseFollowingEntity> findById(String Id);

    List<UserProductBaseFollowingEntity> findByUserIdAndProductBaseId(String userId, String productBaseId);

    Page<UserProductBaseFollowingEntity> findByUserId(String userId, Pageable pageable);

    Page<UserProductBaseFollowingEntity> findByProductBaseId(String productBaseId, Pageable pageable);

    Long countByProductBaseId(String productBaseId);

}
