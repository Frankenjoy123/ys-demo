package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MarketUserLocationAnalysisEntity;
import com.yunsoo.data.service.entity.MarketUserUsageAnalysisEntity;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Dake Wang on 2016/5/12.
 */
public interface MarketUserLocationAnalysisRepository extends CrudRepository<MarketUserLocationAnalysisEntity, String> {

    @Query("from MarketUserLocationAnalysisEntity where orgId = :orgId " +
            "AND drawDate >= :startTime AND drawDate <= :endTime " +
            "AND (:marketingId is null or marketingId = :marketingId) " )
    List<MarketUserLocationAnalysisEntity> query(@Param("orgId") String orgId, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime, @Param("marketingId") String marketingId);

}
