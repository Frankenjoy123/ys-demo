package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserLikedProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Zhe on 2015/4/3.
 */
public interface UserLikedProductRepository extends JpaRepository<UserLikedProductEntity, Long> {
    Iterable<UserLikedProductEntity> findByActive(Boolean active);

    Iterable<UserLikedProductEntity> findByUserId(String userId);

    Iterable<UserLikedProductEntity> findByUserIdAndActive(String userId, Boolean active);

    Iterable<UserLikedProductEntity> findByUserIdAndBaseProductId(String userId, String baseProductId);

    Iterable<UserLikedProductEntity> findById(Long id);
}
