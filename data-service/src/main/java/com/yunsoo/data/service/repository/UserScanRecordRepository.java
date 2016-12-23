package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.UserScanRecordEntity;
import com.yunsoo.data.service.repository.basic.FindOneAndSaveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by:   Lijian
 * Created on:   2015/8/24
 * Descriptions:
 */
public interface UserScanRecordRepository extends FindOneAndSaveRepository<UserScanRecordEntity, String> {

    Page<UserScanRecordEntity> findByProductKey(String productKey, Pageable pageable);

    Page<UserScanRecordEntity> findByUserId(String userId, Pageable pageable);

    Page<UserScanRecordEntity> findByYsid(String ysid, Pageable pageable);

    @Query("select count(distinct o.productKey) from #{#entityName} o where " +
            "(o.productKeyBatchId = :productKeyBatchId) ")
    Long countByBatchId(@Param("productKeyBatchId") String productKeybatchId);

    UserScanRecordEntity findTop1ByProductKeyOrderByCreatedDateTimeDesc(String productKey);

}
