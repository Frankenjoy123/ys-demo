package com.yunsoo.marketing.dao.repository;


import com.yunsoo.marketing.dao.entity.UserRightEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by:   Haitao
 * Created on:   2016/10/14
 * Descriptions:
 */
public interface UserRightRepository extends Repository<UserRightEntity, String> {
    UserRightEntity findOne(String id);

    UserRightEntity save(UserRightEntity entity);

    Page<UserRightEntity> findByMarketingId(String orgId, Pageable pageable);

    List<UserRightEntity> findByUserEventId(String userEventId);

    Long countByMarketingIdIn(List<String> marketingIdIn);

    @Query("select count(o.id) from #{#entityName} o where " +
            "(o.marketingId = :marketingId) " +
            "and (:marketingRightId is null or o.marketingRightId = :marketingRightId) ")
    Long sumUserRightId(@Param("marketingId") String marketingId, @Param("marketingRightId") String marketingRightId);

    @Query("select o from #{#entityName} o where " +
            "(o.marketingId = :marketingId) " +
            "and (:marketingRightId is null or o.marketingRightId = :marketingRightId) " +
            "and (:typeCode is null or o.typeCode = :typeCode) " +
            "and (:statusCode is null or o.statusCode = :statusCode) " +
            "and (:startTime is null or o.createdDateTime >= :startTime) " +
            "and (:endTime is null or o.createdDateTime <= :endTime) ")
    Page<UserRightEntity> query(@Param("marketingId") String marketingId,
                                @Param("marketingRightId") String marketingRightId,
                                @Param("typeCode") String typeCode,
                                @Param("statusCode") String statusCode,
                                @Param("startTime") DateTime startTime,
                                @Param("endTime") DateTime endTime,
                                Pageable pageable);


}
