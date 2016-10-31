package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.ScanRecordLocationAnalysisEntity;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Dake Wang on 2016/2/3.
 */
public interface ScanRecordLocationAnalysisRepository extends CrudRepository<ScanRecordLocationAnalysisEntity, String> {

    @Query("from ScanRecordLocationAnalysisEntity where orgId = :orgId " +
            "AND scanDate >= :startTime AND scanDate <= :endTime " +
            "AND (:productBaseId is null or productBaseId = :productBaseId) " +
            "AND (:batchId is null or batchId = :batchId) ")
    List<ScanRecordLocationAnalysisEntity> query(@Param("orgId") String orgId, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime, @Param("productBaseId") String productBaseId, @Param("batchId") String batchId);

}