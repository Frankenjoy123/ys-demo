package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserProductFollowingEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by:   yan
 * Created on:   8/17/2015
 * Descriptions:
 */
public interface UserProductFollowingRepository extends FindOneAndSaveRepository<UserProductFollowingEntity, String> {

    Page<UserProductFollowingEntity> findByUserId(String userId, Pageable pageable);

    Page<UserProductFollowingEntity> findByProductBaseId(String productBaseId, Pageable pageable);

    List<UserProductFollowingEntity> findByUserIdAndProductBaseId(String userId, String productBaseId);

    Long countByProductBaseId(String productBaseId);

    @Transactional
    void deleteByUserIdAndProductBaseId(String userId, String productBaseId);
}
