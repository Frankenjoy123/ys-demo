package com.yunsoo.repository;

import com.yunsoo.entity.UserLikedProductEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Zhe on 2015/4/3.
 */
public interface UserLikedProductRepository extends CrudRepository<UserLikedProductEntity, Long> {
    Iterable<UserLikedProductEntity> findByActive(Boolean active);

    Iterable<UserLikedProductEntity> findByUserId(Long userId);
}
