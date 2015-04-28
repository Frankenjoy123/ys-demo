package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrgOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Zhe on 2015/4/16.
 */
public interface OrgOrderRepository extends PagingAndSortingRepository<OrgOrderEntity, Long> {

    Page<OrgOrderEntity> findByActive(Boolean active, Pageable pageable);

    Page<OrgOrderEntity> findByOrgId(String orgId, Pageable pageable);

    Page<OrgOrderEntity> findByOrgIdAndActive(String orgId, Boolean active, Pageable pageable);

    Page<OrgOrderEntity> findByOrgIdAndProductBaseIdAndActive(String orgId, String productBaseId, Boolean active, Pageable pageable);
}
