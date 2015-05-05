package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.OrgProductKeyTransactionDetailEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by  : Zhe
 * Created on  : 2015/4/16
 * Descriptions:
 */
public interface OrgProductKeyTransactionDetailRepository extends FindOneAndSaveRepository<OrgProductKeyTransactionDetailEntity, String> {

    List<OrgProductKeyTransactionDetailEntity> findByTransactionId(String transactionId);

    @Query("select o from #{#entityName} o where " +
            "(:orgId is null or o.orgId = :orgId) " +
            "and (:productKeyBatchId is null or o.productKeyBatchId = :productKeyBatchId) " +
            "and (:orderId is null or o.orderId = :orderId) ")
    List<OrgProductKeyTransactionDetailEntity> query(@Param("orgId") String orgId,
                                                     @Param("productKeyBatchId") String productKeyBatchId,
                                                     @Param("orderId") String orderId,
                                                     Pageable pageable);

    List<OrgProductKeyTransactionDetailEntity> save(Iterable<OrgProductKeyTransactionDetailEntity> detailEntities);

}