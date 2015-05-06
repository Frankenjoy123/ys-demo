package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ProductKeyTransactionDetailEntity;
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
public interface ProductKeyTransactionDetailRepository extends FindOneAndSaveRepository<ProductKeyTransactionDetailEntity, String> {

    List<ProductKeyTransactionDetailEntity> findByTransactionId(String transactionId);

    @Query("select o from #{#entityName} o where " +
            "(:orgId is null or o.orgId = :orgId) " +
            "and (:productKeyBatchId is null or o.productKeyBatchId = :productKeyBatchId) " +
            "and (:orderId is null or o.orderId = :orderId) ")
    List<ProductKeyTransactionDetailEntity> query(@Param("orgId") String orgId,
                                                     @Param("productKeyBatchId") String productKeyBatchId,
                                                     @Param("orderId") String orderId,
                                                     Pageable pageable);

    List<ProductKeyTransactionDetailEntity> save(Iterable<ProductKeyTransactionDetailEntity> detailEntities);

}