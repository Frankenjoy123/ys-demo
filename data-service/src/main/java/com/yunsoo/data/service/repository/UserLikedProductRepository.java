package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserLikedProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Zhe on 2015/4/3.
 */
public interface UserLikedProductRepository extends JpaRepository<UserLikedProductEntity, Long> {
    Iterable<UserLikedProductEntity> findByActive(Boolean active);

    Iterable<UserLikedProductEntity> findByUserId(String userId);

    Iterable<UserLikedProductEntity> findByUserIdAndActive(String userId, Boolean active);

    Page<UserLikedProductEntity> findByUserIdAndActive(String userId, Boolean active, Pageable pageable);

    Iterable<UserLikedProductEntity> findByUserIdAndBaseProductId(String userId, String baseProductId);

    Iterable<UserLikedProductEntity> findById(Long id);
}
