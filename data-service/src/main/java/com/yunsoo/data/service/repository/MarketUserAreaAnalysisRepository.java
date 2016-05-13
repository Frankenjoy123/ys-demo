package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MarketUserAreaAnalysisEntity;
import com.yunsoo.data.service.entity.ScanRecordAnalysisEntity;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Dake Wang on 2016/2/3.
 */
public interface MarketUserAreaAnalysisRepository extends CrudRepository<MarketUserAreaAnalysisEntity, String> {

    @Query("from MarketUserAreaAnalysisEntity where orgId = :orgId " +
            "AND drawDate >= :startTime AND drawDate <= :endTime " +
            "AND (:marketingId is null or marketingId = :marketingId) " )
    List<MarketUserAreaAnalysisEntity> query(@Param("orgId") String orgId, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime, @Param("marketingId") String marketingId);

}
