package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MarketUserDeviceAnalysisEntity;
import com.yunsoo.data.service.entity.MarketUserGenderAnalysisEntity;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Dake Wang on 2016/5/12.
 */
public interface MarketUserGenderAnalysisRepository extends CrudRepository<MarketUserGenderAnalysisEntity, String> {

    @Query("from MarketUserGenderAnalysisEntity where orgId = :orgId " +
            "AND drawDate >= :startTime AND drawDate <= :endTime " +
            "AND (:marketingId is null or marketingId = :marketingId) " )
    List<MarketUserGenderAnalysisEntity> query(@Param("orgId") String orgId, @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime, @Param("marketingId") String marketingId);

}
