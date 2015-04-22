package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserOrgEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Zhe on 2015/4/15.
 */
public interface UserOrgRepository extends PagingAndSortingRepository<UserOrgEntity, Long> {

    Iterable<UserOrgEntity> findById(String Id);

    Iterable<UserOrgEntity> findByUserId(String userId);

    Page<UserOrgEntity> findByUserId(String userId, Pageable pageable);

    Iterable<UserOrgEntity> findByOrganizationId(String organizationId);

    Page<UserOrgEntity> findByOrganizationId(String organizationId, Pageable pageable);

}
