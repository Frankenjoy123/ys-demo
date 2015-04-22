package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrgTransactionDetailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Zhe on 2015/4/16.
 */
public interface OrgTransactionDetailRepository extends PagingAndSortingRepository<OrgTransactionDetailEntity, Long> {

    Page<OrgTransactionDetailEntity> findByAccountId(String accountId, Pageable pageable);

    Page<OrgTransactionDetailEntity> findByOrgId(String orgId, Pageable pageable);

    Page<OrgTransactionDetailEntity> findByBatchId(String batchId, Pageable pageable);

    Page<OrgTransactionDetailEntity> findByOrderId(String orderId, Pageable pageable);

    Page<OrgTransactionDetailEntity> findByOrgIdAndOrderId(String orgId, Long orderId, Pageable pageable);
}