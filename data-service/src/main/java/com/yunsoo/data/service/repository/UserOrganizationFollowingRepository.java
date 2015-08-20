package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserOrganizationFollowingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Zhe on 2015/4/15.
 */
@Transactional
public interface UserOrganizationFollowingRepository extends PagingAndSortingRepository<UserOrganizationFollowingEntity, Long> {

    List<UserOrganizationFollowingEntity> findById(String Id);

    List<UserOrganizationFollowingEntity> findByUserIdAndOrgId(String userId, String orgId);

    Page<UserOrganizationFollowingEntity> findByUserId(String userId, Pageable pageable);

    Page<UserOrganizationFollowingEntity> findByOrgId(String orgId, Pageable pageable);

}
