package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrgTransactionDetailEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by Zhe on 2015/4/16.
 */
public interface OrgTransactionDetailRepository extends PagingAndSortingRepository<OrgTransactionDetailEntity, Long> {

    Iterable<OrgTransactionDetailEntity> findByAccountId(Long accountId);

    Iterable<OrgTransactionDetailEntity> findByOrgId(Long orgId);

    Iterable<OrgTransactionDetailEntity> findByBatchId(Long batchId);

    Iterable<OrgTransactionDetailEntity> findByOrderId(Long orderId);

    Iterable<OrgTransactionDetailEntity> findByOrgIdAndOrderId(Long orgId, Long orderId);
}