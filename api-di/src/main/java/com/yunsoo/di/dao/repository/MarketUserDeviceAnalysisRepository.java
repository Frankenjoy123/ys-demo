package com.yunsoo.di.dao.repository;

import com.yunsoo.di.dao.entity.MarketUserDeviceAnalysisEntity;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Dake Wang on 2016/5/12.
 */
public interface MarketUserDeviceAnalysisRepository extends CrudRepository<MarketUserDeviceAnalysisEntity, String> {

    @Query("from MarketUserDeviceAnalysisEntity where orgId = :orgId " +
            "AND drawDate >= :startTime AND drawDate <= :endTime " +
            "AND (:marketingId is null or marketingId = :marketingId) " )
    List<MarketUserDeviceAnalysisEntity> query(@Param("orgId") String orgId, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime, @Param("marketingId") String marketingId);

}
