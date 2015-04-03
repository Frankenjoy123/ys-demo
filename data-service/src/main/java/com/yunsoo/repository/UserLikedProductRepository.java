package com.yunsoo.repository;

import com.yunsoo.entity.UserLikedProduct;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Zhe on 2015/4/3.
 */
public interface UserLikedProductRepository extends CrudRepository<UserLikedProduct, Long> {
    Iterable<UserLikedProduct> findByActive(Boolean active);

    Iterable<UserLikedProduct> findByUserId(Boolean userId);
}
