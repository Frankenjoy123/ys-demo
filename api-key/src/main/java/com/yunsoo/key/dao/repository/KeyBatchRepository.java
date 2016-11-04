package com.yunsoo.key.dao.repository;

import com.yunsoo.key.dao.entity.KeyBatchEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   Lijian
 * Created on:   2016-08-18
 * Descriptions:
 */
public interface KeyBatchRepository extends Repository<KeyBatchEntity, String> {

    KeyBatchEntity findOne(String id);

    KeyBatchEntity save(KeyBatchEntity entity);

    @Query("select e from #{#entityName} e where " +
            "    (:orgId is null or e.orgId = :orgId) " +
            "and (:productBaseId is null or e.productBaseId = :productBaseId) " +
            "and (e.statusCode in (:statusCodeIn) or :statusCodeInIgnored = true) " +
            "and (:createdAccountId is null or e.createdAccountId = :createdAccountId) " +
            "and (:createdDateTimeGE is null or e.createdDateTime >= :createdDateTimeGE) " +
            "and (:createdDateTimeLE is null or e.createdDateTime <= :createdDateTimeLE)")
    Page<KeyBatchEntity> query(@Param("orgId") String orgId,
                               @Param("productBaseId") String productBaseId,
                               @Param("statusCodeIn") List<String> statusCodeIn,
                               @Param("statusCodeInIgnored") boolean statusCodeInIgnored,
                               @Param("createdAccountId") String createdAccountId,
                               @Param("createdDateTimeGE") DateTime createdDateTimeGE,
                               @Param("createdDateTimeLE") DateTime createdDateTimeLE,
                               Pageable pageable);

}
