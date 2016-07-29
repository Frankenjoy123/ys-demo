package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.ScanRecordAnalysisEntity;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Dake Wang on 2016/2/3.
 */
public interface ScanRecordAnalysisRepository extends CrudRepository<ScanRecordAnalysisEntity, String> {

    @Query("from ScanRecordAnalysisEntity where orgId = :orgId " +
            "AND scanDate >= :startTime AND scanDate <= :endTime " +
            "AND (productBaseId = :productBaseId) " +
            "AND (:batchId is null or batchId = :batchId) ")
    List<ScanRecordAnalysisEntity> query(@Param("orgId") String orgId, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime, @Param("productBaseId") String productBaseId, @Param("batchId") String batchId);

}
