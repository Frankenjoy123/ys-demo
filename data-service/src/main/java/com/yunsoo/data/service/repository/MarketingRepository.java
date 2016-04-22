package com.yunsoo.data.service.repository;

import com.yunsoo.data.service.entity.MarketingEntity;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by  : haitao
 * Created on  : 2016/1/25
 * Descriptions:
 */
public interface MarketingRepository extends CrudRepository<MarketingEntity, String> {
    Page<MarketingEntity> findByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodes, Pageable pageable);

    Long countByOrgIdAndStatusCodeIn(String orgId, List<String> statusCodes);

    @Query("select m.id from MarketingEntity m where orgId = ?1")
    List<String> findMarketingIdByOrgId(String orgId);


    @Query("select m from MarketingEntity m where orgId in :orgIds and ( :status is null or statusCode = :status) " +
            "and (:searchText is null or m.name like :searchText) " +
            "and (:endTime is null or m.createdDateTime <= :endTime) and  (:startTime is null or m.createdDateTime >= :startTime) " +
            "order by createdDateTime desc")
    Page<MarketingEntity> query(@Param("orgIds")List<String> orgIds, @Param("status")String status,
                                @Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime,
                                @Param("searchText")String searchText, Pageable pageable);


    int countByOrgIdInAndStatusCode(@Param("orgIds")List<String> orgIds, @Param("status")String status );

    @Query("select m.orgId as id, sum(m.budget) as cal_value from MarketingEntity m where m.orgId in :orgIds and  statusCode in :status group by m.orgId order by cal_value desc")
    List<Object[]>  sumMarketingByOrgIds(@Param("orgIds")List<String> orgIds, @Param("status")List<String> statusList, Pageable pageable );
}
