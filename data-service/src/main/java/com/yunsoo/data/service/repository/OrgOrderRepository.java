package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrgOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Zhe on 2015/4/16.
 */
public interface OrgOrderRepository extends PagingAndSortingRepository<OrgOrderEntity, Long> {

    Iterable<OrgOrderEntity> findByActive(Boolean active);

    Iterable<OrgOrderEntity> findByOrgId(Long orgId);

    Iterable<OrgOrderEntity> findByOrgIdAndActive(Long orgId, Boolean active);

    Page<OrgOrderEntity> findByOrgIdAndActive(Long orgId, Boolean active, Pageable pageable);
}
